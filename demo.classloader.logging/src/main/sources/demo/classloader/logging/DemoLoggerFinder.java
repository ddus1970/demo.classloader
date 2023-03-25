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

import java.io.InputStream;
import java.lang.System.LoggerFinder;
import java.util.logging.LogManager;

/**
 *
 * @author ddus
 */
public class DemoLoggerFinder extends LoggerFinder {

    static {
        configLogger();
    }
    
    private static void configLogger() {
        try {
            InputStream stream = DemoLoggerFinder.class.getClassLoader()
                    .getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public System.Logger getLogger(String name, Module module) {
        //return new DemoLogger(LogManager.getLogManager().getLogger(name));
        return new DemoLogger(java.util.logging.Logger.getLogger(name));
    }
}
