package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class LeaderQueryMessage extends BaseClusterMessage {
   private static final long serialVersionUID = -5014982557706100655L;

   private LeaderQueryMessage(ServerInformation var1) {
      super(var1, 10);
   }

   public String toString() {
      return "[LeaderQueryMessage from " + this.getSenderInformation() + "]";
   }

   static LeaderQueryMessage create(ServerInformation var0) {
      return new LeaderQueryMessage(var0);
   }
}
