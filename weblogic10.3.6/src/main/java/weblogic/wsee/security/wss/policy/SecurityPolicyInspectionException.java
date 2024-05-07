package weblogic.wsee.security.wss.policy;

import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SecurityPolicyInspectionException extends WSSecurityException {
   private int errorCode;
   private static final String ERROR_MESSAGE = "Error on verifying message against security policy";

   public SecurityPolicyInspectionException(int var1, String var2) {
      super(var2 + " Error code:" + var1, WSSConstants.FAILURE_INVALID);
      this.errorCode = 9999;
      this.errorCode = var1;
   }

   public SecurityPolicyInspectionException(int var1, String var2, Throwable var3) {
      super(var2 + " Error code:" + var1, (Object)var3, WSSConstants.FAILURE_INVALID);
      this.errorCode = 9999;
      this.errorCode = var1;
   }

   public SecurityPolicyInspectionException(int var1, Throwable var2) {
      super("Error code:" + var1, (Object)var2, WSSConstants.FAILURE_INVALID);
      this.errorCode = 9999;
      this.errorCode = var1;
   }

   public SecurityPolicyInspectionException(int var1) {
      this(var1, "Error on verifying message against security policy");
   }

   public SecurityPolicyInspectionException() {
      this(9999, (String)"Error on verifying message against security policy");
   }

   public SecurityPolicyInspectionException(String var1) {
      super("Error on verifying message against security policy" + var1);
      this.errorCode = 9999;
   }

   public int getErrorCode() {
      return this.errorCode;
   }
}
