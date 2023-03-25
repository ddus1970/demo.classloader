/*
 * Copyright 2023 ddus.
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
package demo.classloader.logging;

import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author ddus
 */
public class DemoLogger implements System.Logger {
    java.util.logging.Logger _logger;
    
    DemoLogger(java.util.logging.Logger logger) {
        _logger = logger;
    }
    
    @Override
    public String getName() {
        return _logger.getName();
    }

    @Override
    public boolean isLoggable(System.Logger.Level level) {
        final var logginLevel = DemoLoggerUtils.systemToLoggingLevel(level);
        return _logger.isLoggable(logginLevel);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        final var logginLevel = DemoLoggerUtils.systemToLoggingLevel(level);
        _logger.logrb(logginLevel, bundle, msg, thrown);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        final var logginLevel = DemoLoggerUtils.systemToLoggingLevel(level);
        _logger.logrb(logginLevel, bundle, format, params);
    }
}
