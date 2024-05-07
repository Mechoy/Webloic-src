package weblogic.management.runtime;

public interface WLECConnectionPoolRuntimeMBean extends RuntimeMBean {
   String getPoolName();

   String getWLEDomainName();

   int getMaxCapacity();

   String getPoolState();

   boolean isSecurityContextPropagation();

   boolean isCertificateBasedAuthentication();

   WLECConnectionRuntimeMBean[] getConnections();

   int resetConnectionPool();
}
