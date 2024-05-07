package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.wsee.util.Verbose;

public final class WseePortRuntimeData extends WseeBasePortRuntimeData {
   private static final boolean verbose = Verbose.isVerbose(WseePortRuntimeData.class);
   private String _policySubjectResourcePattern = null;
   private String _policySubjectName = null;
   private String _policySubjectType = null;
   private String _policyAttachmentSupport = null;

   WseePortRuntimeData(String var1, String var2) throws ManagementException {
      super(var1, var2);
      if (verbose) {
         Verbose.log((Object)("WseePortRuntimeMBeanData[" + var1 + "]"));
      }

   }

   public String getPolicySubjectResourcePattern() {
      return this._policySubjectResourcePattern;
   }

   void setPolicySubjectResourcePattern(String var1) {
      this._policySubjectResourcePattern = var1;
   }

   public String getPolicySubjectName() {
      return this._policySubjectName;
   }

   void setPolicySubjectName(String var1) {
      this._policySubjectName = var1;
   }

   public String getPolicySubjectType() {
      return this._policySubjectType;
   }

   void setPolicySubjectType(String var1) {
      this._policySubjectType = var1;
   }

   public String getPolicyAttachmentSupport() {
      return this._policyAttachmentSupport;
   }

   void setPolicyAttachmentSupport(String var1) {
      this._policyAttachmentSupport = var1;
   }
}
