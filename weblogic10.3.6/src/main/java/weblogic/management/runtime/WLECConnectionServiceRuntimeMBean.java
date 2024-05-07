package weblogic.management.runtime;

public interface WLECConnectionServiceRuntimeMBean extends RuntimeMBean {
   int getConnectionPoolCount();

   WLECConnectionPoolRuntimeMBean[] getConnectionPools();
}
