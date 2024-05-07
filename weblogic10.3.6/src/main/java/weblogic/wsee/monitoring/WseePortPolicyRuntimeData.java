package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;

public class WseePortPolicyRuntimeData extends WseeBaseRuntimeData {
   private int atnSuccessesCount = 0;
   private int atzSuccessesCount = 0;
   private int atnViolationCount = 0;
   private int atzViolationCount = 0;
   private int conSuccessesCount = 0;
   private int intSuccessesCount = 0;
   private int conViolationCount = 0;
   private int intViolationCount = 0;
   private int totalPolicyFaults = 0;
   private int generalFaults = 0;

   public WseePortPolicyRuntimeData(String var1, WseeBaseRuntimeData var2) throws ManagementException {
      super(var1, var2);
   }

   public int getAuthenticationSuccesses() {
      return this.atnSuccessesCount;
   }

   public int getAuthenticationViolations() {
      return this.atnViolationCount;
   }

   public int getAuthorizationSuccesses() {
      return this.atzSuccessesCount;
   }

   public int getAuthorizationViolations() {
      return this.atzViolationCount;
   }

   public int getConfidentialitySuccesses() {
      return this.conSuccessesCount;
   }

   public int getConfidentialityViolations() {
      return this.conViolationCount;
   }

   public int getIntegritySuccesses() {
      return this.intSuccessesCount;
   }

   public int getIntegrityViolations() {
      return this.intViolationCount;
   }

   public int getPolicyFaults() {
      return this.totalPolicyFaults;
   }

   /** @deprecated */
   @Deprecated
   public int getTotalFaults() {
      return this.generalFaults + this.totalPolicyFaults + this.getTotalViolations();
   }

   public int getTotalSecurityFaults() {
      return this.generalFaults + this.totalPolicyFaults + this.getTotalViolations();
   }

   public int getTotalViolations() {
      return this.atnViolationCount + this.atzViolationCount + this.conViolationCount + this.intViolationCount;
   }

   public void reportAuthenticationSuccess() {
      ++this.atnSuccessesCount;
   }

   public void reportAuthenticationViolation() {
      ++this.atnViolationCount;
   }

   public void reportAuthorizationSuccess() {
      ++this.atzSuccessesCount;
   }

   public void reportAuthorizationViolation() {
      ++this.atzViolationCount;
   }

   public void reportConfidentialitySuccess() {
      ++this.conSuccessesCount;
   }

   public void reportConfidentialityViolation() {
      ++this.conViolationCount;
   }

   public void reportIntegritySuccess() {
      ++this.intSuccessesCount;
   }

   public void reportIntegrityViolation() {
      ++this.intViolationCount;
   }

   public void reportPolicyFault() {
      ++this.totalPolicyFaults;
   }

   public void reportGeneralFault() {
      ++this.generalFaults;
   }
}
