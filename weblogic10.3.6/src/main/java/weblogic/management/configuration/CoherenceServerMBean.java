package weblogic.management.configuration;

public interface CoherenceServerMBean extends ManagedExternalServerMBean {
   void setCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1);

   CoherenceClusterSystemResourceMBean getCoherenceClusterSystemResource();

   String getUnicastListenAddress();

   void setUnicastListenAddress(String var1);

   int getUnicastListenPort();

   void setUnicastListenPort(int var1);

   boolean isUnicastPortAutoAdjust();

   void setUnicastPortAutoAdjust(boolean var1);

   CoherenceServerStartMBean getCoherenceServerStart();
}
