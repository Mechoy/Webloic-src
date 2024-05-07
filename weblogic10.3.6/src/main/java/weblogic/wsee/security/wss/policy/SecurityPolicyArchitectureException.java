package weblogic.wsee.security.wss.policy;

import weblogic.wsee.security.wss.SecurityPolicyException;

public class SecurityPolicyArchitectureException extends SecurityPolicyException {
   public SecurityPolicyArchitectureException(String var1) {
      super(var1);
   }

   public SecurityPolicyArchitectureException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SecurityPolicyArchitectureException(Throwable var1) {
      super(var1);
   }

   public SecurityPolicyArchitectureException() {
      super("Error on process security policy");
   }
}
