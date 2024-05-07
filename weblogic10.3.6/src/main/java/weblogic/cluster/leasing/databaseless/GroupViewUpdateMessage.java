package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public class GroupViewUpdateMessage extends BaseClusterMessage {
   static final int ADD = 1;
   static final int REMOVE = 2;
   private final int operation;
   private final ServerInformation serverInformation;
   private long versionNumber;
   private static final long serialVersionUID = 562510693137563962L;

   public GroupViewUpdateMessage(ServerInformation var1, int var2, ServerInformation var3, long var4) {
      super(var1, 6);
      this.operation = var2;
      this.serverInformation = var3;
      this.versionNumber = var4;
   }

   public static GroupViewUpdateMessage createMemberAdded(ServerInformation var0, ServerInformation var1, long var2) {
      return new GroupViewUpdateMessage(var0, 1, var1, var2);
   }

   public static GroupViewUpdateMessage createMemberRemoved(ServerInformation var0, ServerInformation var1, long var2) {
      return new GroupViewUpdateMessage(var0, 2, var1, var2);
   }

   int getOperation() {
      return this.operation;
   }

   ServerInformation getServerInformation() {
      return this.serverInformation;
   }

   public String toString() {
      return this.operation == 1 ? "[GroupViewUpdate ADD operation with server info " + this.serverInformation + "]" : "[GroupViewUpdate REMOVE operation with server info " + this.serverInformation + "]";
   }

   public long getVersionNumber() {
      return this.versionNumber;
   }
}
