package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.wsee.util.Verbose;

public final class WseeOperationRuntimeData extends WseeBaseOperationRuntimeData {
   private static final boolean verbose = Verbose.isVerbose(WseeOperationRuntimeData.class);
   private String _policySubjectResourcePattern = null;
   private String _policySubjectName = null;
   private String _policySubjectType = null;
   private String _policyAttachmentSupport = null;

   public static WseeOperationRuntimeData createWsProtocolOp(WseeBaseRuntimeData var0) throws ManagementException {
      WseeOperationRuntimeData var1 = new WseeOperationRuntimeData("Ws-Protocol");
      var1.setParentData(var0);
      return var1;
   }

   WseeOperationRuntimeData(String var1) {
      super(var1);
      if (verbose) {
         Verbose.log((Object)("WseeOperationRuntimeData[" + var1 + "]"));
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
