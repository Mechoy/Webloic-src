package weblogic.wsee.monitoring;

public class WseePortConfigurationRuntimeData extends WseeBaseRuntimeData {
   private String _resourcePattern;
   private String _subjectName;

   public WseePortConfigurationRuntimeData(String var1, String var2) {
      super(var1, (WseeBaseRuntimeData)null);
      this._subjectName = var1;
      this._resourcePattern = var2;
   }

   public String getResourcePattern() {
      return this._resourcePattern;
   }

   public String getSubjectName() {
      return this._subjectName;
   }
}
