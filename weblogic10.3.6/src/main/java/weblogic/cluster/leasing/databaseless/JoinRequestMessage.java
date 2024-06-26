package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class JoinRequestMessage extends BaseClusterMessage {
   private static final long serialVersionUID = 8701712072134118636L;

   private JoinRequestMessage(ServerInformation var1) {
      super(var1, 2);
   }

   static JoinRequestMessage create(ServerInformation var0) {
      return new JoinRequestMessage(var0);
   }
}
