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

import static java.lang.System.Logger.Level;

/**
 *
 * @author ddus
 */
public class DemoLoggerUtils {

    public static java.util.logging.Level systemToLoggingLevel(System.Logger.Level level) {
        switch (level) {
            case ALL : return java.util.logging.Level.ALL; 
            case TRACE : return java.util.logging.Level.FINER; 
            case DEBUG : return java.util.logging.Level.FINE; 
            case INFO : return java.util.logging.Level.INFO;             
            case WARNING : return java.util.logging.Level.WARNING; 
            case ERROR : return java.util.logging.Level.SEVERE; 
            case OFF : return java.util.logging.Level.OFF; 
        }
        throw new IllegalArgumentException("Logginn level " + level + " is unacceptible");
    }
}
