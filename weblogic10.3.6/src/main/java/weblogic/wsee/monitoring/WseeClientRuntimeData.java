package weblogic.wsee.monitoring;

public class WseeClientRuntimeData extends WseeBaseRuntimeData {
   private String _clientId;
   private WseeClientPortRuntimeData _port;

   protected WseeClientRuntimeData(String var1, String var2) {
      super(var1, (WseeBaseRuntimeData)null);
      this._clientId = var2;
   }

   public String getClientId() {
      return this._clientId;
   }

   public WseeClientPortRuntimeData getPort() {
      return this._port;
   }

   public void setPort(WseeClientPortRuntimeData var1) {
      this._port = var1;
      this._port.setParentData(this);
   }
}
