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

package org.apache.ivory.util;

import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class StartupPropertiesTest {
    @BeforeClass
    public void setUp() {
        StartupProperties.get();
    }
    
    public void testDomain() {
        StartupProperties props = (StartupProperties) StartupProperties.get();
        assertEquals("debug", props.getDomain());
        assertEquals("vm://localhost?broker.useJmx=false&broker.persistent=true", props.get("broker.url"));
        assertEquals("IVORY.ENTITY.TOPIC", props.get("entity.topic"));
    }
}
