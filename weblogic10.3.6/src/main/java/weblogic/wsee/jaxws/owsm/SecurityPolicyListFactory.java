package weblogic.wsee.jaxws.owsm;

public abstract class SecurityPolicyListFactory {
   private static SecurityPolicyList policyList = new SecurityPolicyListImpl();

   public static final SecurityPolicyList getSecurityPolicyList() {
      return policyList;
   }
}
