package weblogic.cluster.messaging.internal;

import java.rmi.Remote;

public interface ClusterMessageEndPoint extends Remote {
   ClusterResponse process(ClusterMessage var1) throws ClusterMessageProcessingException;

   void processOneWay(ClusterMessage var1) throws ClusterMessageProcessingException;
}
