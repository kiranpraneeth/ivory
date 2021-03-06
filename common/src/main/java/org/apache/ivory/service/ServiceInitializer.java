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

package org.apache.ivory.service;

import org.apache.ivory.IvoryException;
import org.apache.ivory.util.ReflectionUtils;
import org.apache.ivory.util.StartupProperties;
import org.apache.log4j.Logger;

public class ServiceInitializer {

    private static Logger LOG = Logger.getLogger(ServiceInitializer.class);
    private final Services services = Services.get();

    public void initialize() throws IvoryException {
        String serviceClassNames = StartupProperties.get().
                getProperty("application.services", "org.apache.ivory.entity.store.ConfigurationStore");
        for (String serviceClassName : serviceClassNames.split(",")) {
            serviceClassName = serviceClassName.trim();
            if (serviceClassName.isEmpty()) continue;
            IvoryService service = ReflectionUtils.getInstanceByClassName(serviceClassName);
            services.register(service);
            LOG.info("Initializing service : " + serviceClassName);
            service.init();
            LOG.info("Service initialized : " + serviceClassName);
        }
    }

    public void destroy() throws IvoryException {
        for (IvoryService service : services) {
            LOG.info("Destroying service : " + service.getClass().getName());
            service.destroy();
            LOG.info("Service destroyed : " + service.getClass().getName());
        }
    }
}
