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

package org.jodconverter.core.job;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jodconverter.core.office.TemporaryFileMaker;
import org.jodconverter.core.util.AssertUtils;
import org.jodconverter.core.util.FileUtils;
import org.jodconverter.core.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Optional;

/** Source document specifications for from an input stream. */
public class SourceDocumentSpecsFromInputStream extends AbstractSourceDocumentSpecs
    implements SourceDocumentSpecs {

  private final InputStream inputStream;
  private final TemporaryFileMaker fileMaker;
  private final boolean closeStream;

  /**
   * Creates specs from the specified stream.
   *
   * @param inputStream The source stream.
   * @param fileMaker Temporary file maker.
   * @param closeStream If we close the stream on completion.
   */
  public SourceDocumentSpecsFromInputStream(
      final @NonNull InputStream inputStream,
      final @NonNull TemporaryFileMaker fileMaker,
      final boolean closeStream) {
    super();

    AssertUtils.notNull(inputStream, "inputStream must not be null");
    AssertUtils.notNull(fileMaker, "fileMaker must not be null");
    this.inputStream = inputStream;
    this.fileMaker = fileMaker;
    this.closeStream = closeStream;
  }

  @Override
  public @NonNull File getFile() {

    // Write the InputStream to the temp file.
    final File tempFile =
        Optional.ofNullable(getFormat())
            .map(format -> fileMaker.makeTemporaryFile(format.getExtension()))
            .orElse(fileMaker.makeTemporaryFile());
    try (FileOutputStream outputStream = new FileOutputStream(tempFile);
        FileChannel channel = outputStream.getChannel();
        FileLock ignored = channel.lock()) {
      IOUtils.copy(inputStream, outputStream);
      return tempFile;
    } catch (IOException ex) {
      throw new DocumentSpecsIOException(
          String.format("Could not write stream to file '%s'", tempFile), ex);
    }
  }

  @Override
  public void onConsumed(final @NonNull File tempFile) {

    // The temporary file must be deleted
    FileUtils.deleteQuietly(tempFile);

    if (closeStream) {
      try {
        inputStream.close();
      } catch (IOException ex) {
        throw new DocumentSpecsIOException("Could not close input stream", ex);
      }
    }
  }
}
