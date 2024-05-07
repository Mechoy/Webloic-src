package weblogic.management.configuration;

import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.management.ManagementException;

public interface CoherenceClusterSystemResourceMBean extends SystemResourceMBean {
   String getDescriptorFileName();

   String getCustomClusterConfigurationFileName();

   boolean isUsingCustomClusterConfigurationFile();

   void importCustomClusterConfigurationFile(String var1) throws ManagementException;

   WeblogicCoherenceBean getCoherenceClusterResource();

   void setUsingCustomClusterConfigurationFile(boolean var1) throws ManagementException;
}
