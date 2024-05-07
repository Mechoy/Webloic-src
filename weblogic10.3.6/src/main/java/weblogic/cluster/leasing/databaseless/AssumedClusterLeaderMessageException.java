package weblogic.cluster.leasing.databaseless;

import java.util.HashMap;
import weblogic.cluster.messaging.internal.ClusterMessageProcessingException;
import weblogic.cluster.messaging.internal.ClusterResponse;

public class AssumedClusterLeaderMessageException extends ClusterMessageProcessingException {
   private static final long serialVersionUID = -510834080848939801L;

   public AssumedClusterLeaderMessageException(Exception var1) {
      super(var1);
   }

   public AssumedClusterLeaderMessageException(String var1) {
      super(var1);
   }

   public AssumedClusterLeaderMessageException(ClusterResponse[] var1, HashMap var2) {
      super(var1, var2);
   }
}
