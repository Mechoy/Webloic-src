package weblogic.wsee.monitoring;

public class WseeOperationConfigurationRuntimeData extends WseeBaseRuntimeData {
   private String _resourcePattern;
   private String _subjectName;

   public WseeOperationConfigurationRuntimeData(String var1, WseePortConfigurationRuntimeData var2) {
      super(var1, var2);
      this._subjectName = var1;
      this._resourcePattern = var2.getResourcePattern() + "/OPERATIONs/" + this._subjectName;
   }

   public String getResourcePattern() {
      return this._resourcePattern;
   }

   public String getSubjectName() {
      return this._subjectName;
   }
}
