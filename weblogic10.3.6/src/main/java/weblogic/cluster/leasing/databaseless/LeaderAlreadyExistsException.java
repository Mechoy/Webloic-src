package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.ClusterMessageProcessingException;

public final class LeaderAlreadyExistsException extends ClusterMessageProcessingException {
   public LeaderAlreadyExistsException(String var1) {
      super(var1);
   }
}
