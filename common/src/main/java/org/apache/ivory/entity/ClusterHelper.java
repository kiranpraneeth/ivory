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

package org.apache.ivory.entity;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.ivory.entity.v0.cluster.Cluster;
import org.apache.ivory.entity.v0.cluster.Interface;
import org.apache.ivory.entity.v0.cluster.Interfacetype;
import org.apache.ivory.entity.v0.cluster.Location;
import org.apache.ivory.entity.v0.cluster.Property;

public final class ClusterHelper {
	public static final String DEFAULT_BROKER_IMPL_CLASS = "org.apache.activemq.ActiveMQConnectionFactory";

    private ClusterHelper() {}

    public static Configuration getConfiguration(Cluster cluster) {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", getHdfsUrl(cluster));
        conf.set("mapred.job.tracker", getMREndPoint(cluster));
        if(cluster.getProperties() != null)
            for(Property prop:cluster.getProperties().getProperties()) {
                conf.set(prop.getName(), prop.getValue());
            }
        return conf;
    }

    public static String getOozieUrl(Cluster cluster) {
        return getInterface(cluster, Interfacetype.WORKFLOW).getEndpoint();
    }

    public static String getHdfsUrl(Cluster cluster) {
        return getNormalizedUrl(cluster, Interfacetype.WRITE);
    }

    public static String getReadOnlyHdfsUrl(Cluster cluster) {
        return getNormalizedUrl(cluster, Interfacetype.READONLY);
    }

    public static String getMREndPoint(Cluster cluster) {
        return getInterface(cluster, Interfacetype.EXECUTE).getEndpoint();
    }
    
    public static String getMessageBrokerUrl(Cluster cluster) {
        return getInterface(cluster, Interfacetype.MESSAGING).getEndpoint();
    }
    
    public static String getMessageBrokerImplClass(Cluster cluster) {
        if(cluster.getProperties() != null)
            for(Property prop:cluster.getProperties().getProperties())
                if(prop.getName().equals("brokerImplClass"))
                    return prop.getValue();
        return DEFAULT_BROKER_IMPL_CLASS;
    }

    public static Interface getInterface(Cluster cluster, Interfacetype type) {
        for(Interface interf:cluster.getInterfaces().getInterfaces())
            if(interf.getType() == type)
                return interf;
        return null;
    }

    private static String getNormalizedUrl(Cluster cluster, Interfacetype type) {
    	String normalizedUrl = getInterface(cluster, type).getEndpoint();
    	String normalizedPath = new Path(normalizedUrl + "/").toString();
    	return normalizedPath.substring(0, normalizedPath.length() - 1);
    }

    public static String getCompleteLocation(Cluster cluster, String locationKey) {
        return getHdfsUrl(cluster) + "/" + getLocation(cluster, locationKey);
    }
    
    public static String getLocation(Cluster cluster, String locationKey) {
        for(Location loc:cluster.getLocations().getLocations()) {
            if(loc.getName().equals(locationKey))
                return loc.getPath();
        }
        return null;
    }
    
	public static String getPropertyValue(Cluster cluster, String propName) {
		if (cluster.getProperties() != null) {
			for (Property prop : cluster.getProperties().getProperties()) {
				if (prop.getName().equals(propName))
					return prop.getValue();
			}
		}
		return null;
	}
}
