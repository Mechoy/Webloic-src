package weblogic.ejb.container.interfaces;

public interface SessionBeanInfo extends ClientDrivenBeanInfo, weblogic.ejb.spi.SessionBeanInfo {
   int REPLICATION_NONE = 1;
   int REPLICATION_MEMORY = 2;

   int getReplicationType();

   boolean implementsSessionSynchronization();

   boolean isStateful();

   boolean isEndpointView();

   boolean usesBeanManagedTx();

   String getSwapDirectoryName();

   long getIdleTimeoutMS();

   long getSessionTimeoutMS();

   boolean statefulSessionSerializesConcurrentCalls();

   boolean isAllowRemoveDuringTx();

   boolean getPassivateDuringReplication();

   boolean getCalculateDeltaUsingReflection();
}
