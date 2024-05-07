package weblogic.wsee.monitoring;

public class WseeBaseRuntimeData {
   private String _name;
   private WseeBaseRuntimeData _parentData;

   protected WseeBaseRuntimeData(String var1, WseeBaseRuntimeData var2) {
      this._name = var1;
      this._parentData = var2;
   }

   public String getName() {
      return this._name;
   }

   public void setName(String var1) {
      this._name = var1;
   }

   public WseeBaseRuntimeData getParentData() {
      return this._parentData;
   }

   public void setParentData(WseeBaseRuntimeData var1) {
      this._parentData = var1;
   }
}
