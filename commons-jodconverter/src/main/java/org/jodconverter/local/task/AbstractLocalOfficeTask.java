/*
 * Copyright 2004 - 2012 Mirko Nasato and contributors
 *           2016 - 2020 Simon Braconnier and contributors
 *
 * This file is part of JODConverter - Java OpenDocument Converter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jodconverter.local.task;

import static org.jodconverter.local.office.LocalOfficeUtils.toUnoProperties;
import static org.jodconverter.local.office.LocalOfficeUtils.toUrl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.task.*;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodconverter.core.job.SourceDocumentSpecs;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.task.AbstractOfficeTask;
import org.jodconverter.core.util.AssertUtils;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeContext;
import org.jodconverter.local.office.PasswordProtectedException;
import org.jodconverter.local.office.utils.Lo;

/**
 * Base class for all local office tasks implementation.
 *
 * @see org.jodconverter.core.task.OfficeTask
 */
public abstract class AbstractLocalOfficeTask extends AbstractOfficeTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLocalOfficeTask.class);
  private static final String ERROR_MESSAGE_LOAD = "Could not open document: ";
  protected final Map<String, Object> loadProperties;

  private static class PasswordInteractionHandler implements XInteractionHandler {

    private PasswordRequest passwordRequest;
    private String documentPath;

    /**
     * Gets the password interaction request that has been made, if any.
     *
     * @return The password interaction request that has been made, ot null if no password
     *     interaction request was made.
     */
    public PasswordRequest getPasswordRequest() {
      return passwordRequest;
    }

    /**
     * Gets whether a password interaction request has been made.
     *
     * @return {@code true} if a password interaction request was made, {@code false} otherwise.
     */
    public boolean hasPasswordInteractionRequest() {
      return passwordRequest != null;
    }

    /**
     * Gets the document path of the document for which the request was made.
     *
     * @return The document path of the document for which the request was made, null {@code false}
     *     if no password interaction request was made, or "NA" if an interaction was made, but the
     *     document pas is unknown.
     */
    public String getDocumentPath() {
      return documentPath;
    }

    @Override
    public void handle(final XInteractionRequest xInteractionRequest) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Interaction detected with request {}", xInteractionRequest.getRequest());
      }

      final Object request = xInteractionRequest.getRequest();

      if (request instanceof PasswordRequest) {
        passwordRequest = (PasswordRequest) request;
        documentPath = "NA";
        if (request instanceof DocumentPasswordRequest) {
          documentPath = ((DocumentPasswordRequest) request).Name;
        } else if (request instanceof DocumentMSPasswordRequest) {
          documentPath = ((DocumentMSPasswordRequest) request).Name;
        }
        LOGGER.debug("Password interaction detected for {}", documentPath);
      }
    }
  }

  protected static void appendProperties(
      final @NonNull Map<@NonNull String, @NonNull Object> properties,
      final @Nullable Map<@NonNull String, @NonNull Object> toAddProperties) {

    if (toAddProperties != null) {
      properties.putAll(toAddProperties);
    }
  }

  /**
   * Creates a new task with the specified source document.
   *
   * @param source The source specifications of the document.
   */
  public AbstractLocalOfficeTask(final @NonNull SourceDocumentSpecs source) {
    this(source, null);
  }

  /**
   * Creates a new task with the specified source document.
   *
   * @param source The source specifications of the document.
   * @param loadProperties The load properties to be applied when loading the document. These
   *     properties are added before the load properties of the document format specified in the
   *     {@code source} arguments.
   */
  public AbstractLocalOfficeTask(
      final @NonNull SourceDocumentSpecs source,
      final @Nullable Map<@NonNull String, @NonNull Object> loadProperties) {
    super(source);

    this.loadProperties = loadProperties;
  }

  // Gets the office properties to apply when the input file will be loaded.
  protected @NonNull Map<@NonNull String, @NonNull Object> getLoadProperties() {

    final Map<String, Object> loadProps =
        new HashMap<>(
            loadProperties == null ? LocalConverter.DEFAULT_LOAD_PROPERTIES : loadProperties);
    if (source.getFormat() != null) {
      appendProperties(loadProps, source.getFormat().getLoadProperties());
    }

    // Register a PasswordInteractionHandler handler for opening documents, but only
    // if no interaction handler has been put into the load properties.
    loadProps.putIfAbsent("InteractionHandler", new PasswordInteractionHandler());

    return loadProps;
  }

  // Loads the document from the specified source file.
  protected @NonNull XComponent loadDocument(
      final @NonNull LocalOfficeContext context, final @NonNull File sourceFile)
      throws OfficeException {

    final XComponentLoader loader = context.getComponentLoader();

    AssertUtils.notNull(loader, "Context component loader must not be null");

    try {
      final Map<String, Object> loadProps = getLoadProperties();
      final XComponent document =
          loader.loadComponentFromURL(toUrl(sourceFile), "_blank", 0, toUnoProperties(loadProps));

      // Handle password protection request to throw a meaningful exception, if required.
      handlePasswordProtection(document, loadProps);

      // The document cannot be null
      AssertUtils.notNull(document, ERROR_MESSAGE_LOAD + sourceFile.getName());
      return document;

    } catch (ErrorCodeIOException exception) {
      throw new OfficeException(
          ERROR_MESSAGE_LOAD + sourceFile.getName() + "; errorCode: " + exception.ErrCode,
          exception);
    } catch (IllegalArgumentException | IOException exception) {
      throw new OfficeException(ERROR_MESSAGE_LOAD + sourceFile.getName(), exception);
    }
  }

  // Closes the specified document.
  protected void closeDocument(final @Nullable XComponent document) {

    if (document != null) {

      // Closing the converted document. Use XCloseable.close if the
      // interface is supported, otherwise use XComponent.dispose
      final XCloseable closeable = Lo.qiOptional(XCloseable.class, document).orElse(null);
      if (closeable == null) {
        // If close is not supported by this model - try to dispose it.
        document.dispose();
        Lo.qi(XComponent.class, document).dispose();
      } else {
        try {
          // The boolean parameter deliverOwnership tells objects vetoing the
          // close process that they may assume ownership if they object the closure
          // by throwing a CloseVetoException. Here we give up ownership. To be on
          // the safe side, catch possible veto exception anyway.
          closeable.close(true);
        } catch (CloseVetoException ignored) {
          // whoever raised the veto should close the document
        }
      }
    }
  }

  private void handlePasswordProtection(
      final XComponent document, final Map<String, Object> loadProps) throws OfficeException {

    if (document == null) {
      final Object handler = loadProps.get("InteractionHandler");
      if (handler instanceof PasswordInteractionHandler) {
        final PasswordInteractionHandler pHandler = (PasswordInteractionHandler) handler;
        if (pHandler.hasPasswordInteractionRequest()) {
          throw new PasswordProtectedException(
              "Document password requested for " + pHandler.getDocumentPath(),
              pHandler.getPasswordRequest());
        }
      }
    }
  }

  @Override
  public @NonNull String toString() {
    return getClass().getSimpleName()
        + "{"
        + "source="
        + source
        + ", loadProperties="
        + loadProperties
        + '}';
  }
}
