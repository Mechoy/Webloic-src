package weblogic.wsee.reliability;

public class WsrmSAFManagerFactory {
   private static WsrmSAFReceivingManager receivingManager = new WsrmSAFReceivingManager();
   private static WsrmSAFSendingManager sendingManager = new WsrmSAFSendingManager();

   public static WsrmSAFReceivingManager getWsrmSAFReceivingManager() {
      return receivingManager;
   }

   public static WsrmSAFSendingManager getWsrmSAFSendingManager() {
      return sendingManager;
   }
}
