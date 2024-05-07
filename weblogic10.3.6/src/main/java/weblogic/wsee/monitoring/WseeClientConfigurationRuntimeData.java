package weblogic.wsee.monitoring;

public class WseeClientConfigurationRuntimeData extends WseeBaseRuntimeData {
   private String _serviceRefName;

   protected WseeClientConfigurationRuntimeData(String var1) {
      super(var1, (WseeBaseRuntimeData)null);
      this._serviceRefName = var1;
   }

   public String getServiceRefName() {
      return this._serviceRefName;
   }
}
