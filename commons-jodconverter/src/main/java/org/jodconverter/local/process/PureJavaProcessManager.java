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

package org.jodconverter.local.process;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * {@link ProcessManager} implementation for Java.
 *
 * <p>This manager does not allow the retrieval of the process id.
 */
public class PureJavaProcessManager implements ProcessManager {

  /**
   * This class is required in order to create the default PureJavaProcessManager only on demand, as
   * explained by the Initialization-on-demand holder idiom:
   * https://www.wikiwand.com/en/Initialization-on-demand_holder_idiom
   */
  private static class DefaultHolder { // NOPMD - Disable utility class name rule violation
    /* default */ static final PureJavaProcessManager INSTANCE = new PureJavaProcessManager();
  }

  @Override
  public boolean canFindPid() {
    return false;
  }

  /**
   * Gets the default instance of {@code PureJavaProcessManager}.
   *
   * @return The default {@code PureJavaProcessManager} instance.
   */
  public static @NonNull PureJavaProcessManager getDefault() {
    return DefaultHolder.INSTANCE;
  }
}
