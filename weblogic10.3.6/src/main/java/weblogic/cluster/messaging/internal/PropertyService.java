package weblogic.cluster.messaging.internal;

public interface PropertyService {
   int getDiscoveryPeriodMillis();

   long getHeartbeatTimeoutMillis();
}
