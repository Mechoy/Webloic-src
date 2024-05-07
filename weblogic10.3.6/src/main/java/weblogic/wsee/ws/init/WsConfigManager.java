package weblogic.wsee.ws.init;

class WsConfigManager {
   private static WsConfig singleton = (new WsConfigFactory()).newInstance();

   private WsConfigManager() {
   }

   static WsConfig getInstance() {
      return singleton;
   }
}
