/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ivory.listener;

import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ivory.IvoryException;
import org.apache.ivory.entity.store.ConfigurationStore;
import org.apache.ivory.service.ServiceInitializer;
import org.apache.ivory.util.RuntimeProperties;
import org.apache.ivory.util.StartupProperties;
import org.apache.log4j.Logger;

public class ContextStartupListener implements ServletContextListener {

    private static Logger LOG = Logger.getLogger(ContextStartupListener.class);

    private static final String BUILD_PROPERTIES = "ivory-buildinfo.properties";

    private final ServiceInitializer startupServices = new ServiceInitializer();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        showStartupInfo();

        //Initialize Startup and runtime properties instance for use
        LOG.info("Initializing startup properties ...");
        StartupProperties.get();

        LOG.info("Initializing runtime properties ...");
        RuntimeProperties.get();
        
        try {
            startupServices.initialize();
            ConfigurationStore.get();
        } catch (IvoryException e) {
            throw new RuntimeException(e);
        }
    }

    private void showStartupInfo() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n############################################");
        buffer.append("############################################");
        buffer.append("\n                               Ivory Server (STARTUP)");
        buffer.append("\n");
        Properties buildProperties = new Properties();
        try {
            buildProperties.load(getClass().getClassLoader().
                        getResourceAsStream(BUILD_PROPERTIES));
            for (Map.Entry entry : buildProperties.entrySet()) {
                buffer.append('\n').append('\t').append(entry.getKey()).
                        append(":\t").append(entry.getValue());
            }
        } catch (Throwable e) {
            buffer.append("*** Unable to get build info ***");
        }
        buffer.append("\n############################################");
        buffer.append("############################################");
        LOG.info(buffer);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            startupServices.destroy();
        } catch (IvoryException e) {
            LOG.warn("Error destroying services", e);
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n############################################");
        buffer.append("\n         Ivory Server (SHUTDOWN)            ");
        buffer.append("\n############################################");
        LOG.info(buffer);
    }
}
