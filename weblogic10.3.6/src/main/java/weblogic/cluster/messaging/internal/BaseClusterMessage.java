package weblogic.cluster.messaging.internal;

public class BaseClusterMessage implements ClusterMessage {
   private final ServerInformation senderInformation;
   private final int msgType;

   public BaseClusterMessage(ServerInformation var1, int var2) {
      this.senderInformation = var1;
      this.msgType = var2;
   }

   public ServerInformation getSenderInformation() {
      return this.senderInformation;
   }

   public int getMessageType() {
      return this.msgType;
   }

   public String toString() {
      return "[message type:" + this.msgType + ",sender information:" + this.senderInformation + "]";
   }
}
