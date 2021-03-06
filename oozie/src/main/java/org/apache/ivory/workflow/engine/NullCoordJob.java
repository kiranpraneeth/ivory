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

package org.apache.ivory.workflow.engine;

import org.apache.oozie.client.CoordinatorAction;
import org.apache.oozie.client.CoordinatorJob;

import java.util.Date;
import java.util.List;

public class NullCoordJob implements CoordinatorJob {

    @Override
    public String getAppPath() {
        return null;
    }

    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getConf() {
        return null;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public int getFrequency() {
        return 0;
    }

    @Override
    public Timeunit getTimeUnit() {
        return null;
    }

    @Override
    public String getTimeZone() {
        return null;
    }

    @Override
    public int getConcurrency() {
        return 0;
    }

    @Override
    public Execution getExecutionOrder() {
        return null;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public Date getLastActionTime() {
        return null;
    }

    @Override
    public Date getNextMaterializedTime() {
        return null;
    }

    @Override
    public Date getStartTime() {
        return null;
    }

    @Override
    public Date getEndTime() {
        return null;
    }

    @Override
    public void setStatus(Status status) {
    }

    @Override
    public void setPending() {
    }

    @Override
    public void resetPending() {
    }

    @Override
    public Date getPauseTime() {
        return null;
    }

    @Override
    public String getExternalId() {
        return null;
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public String getBundleId() {
        return null;
    }

    @Override
    public String getConsoleUrl() {
        return null;
    }

    @Override
    public List<CoordinatorAction> getActions() {
        return null;
    }
}
