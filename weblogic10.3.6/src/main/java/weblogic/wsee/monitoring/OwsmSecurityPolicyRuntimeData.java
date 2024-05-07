package weblogic.wsee.monitoring;

import java.util.List;
import weblogic.management.ManagementException;
import weblogic.wsee.jaxws.owsm.SecurityPolicyException;
import weblogic.wsee.jaxws.owsm.SecurityPolicyList;
import weblogic.wsee.jaxws.owsm.SecurityPolicyListFactory;

public class OwsmSecurityPolicyRuntimeData extends WseeBaseRuntimeData {
   public OwsmSecurityPolicyRuntimeData(String var1, WseeBaseRuntimeData var2) throws ManagementException {
      super(var1, var2);
   }

   public String[] getAvailablePolicies() throws ManagementException {
      List var1 = null;
      SecurityPolicyList var2 = SecurityPolicyListFactory.getSecurityPolicyList();
      if (var2 != null) {
         try {
            var1 = var2.getSecurityPolicies();
         } catch (SecurityPolicyException var4) {
         }
      }

      return var1 != null ? (String[])((String[])var1.toArray(new String[var1.size()])) : null;
   }
}
