package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.util.ServiceFinder;
import java.util.Iterator;
import java.util.List;

public class SecurityPolicyListImpl implements SecurityPolicyList {
   public List<String> getSecurityPolicies() throws SecurityPolicyException {
      return SecurityPolicyListImpl.SecurityPolicyListHolder.instance != null ? SecurityPolicyListImpl.SecurityPolicyListHolder.instance.getSecurityPolicies() : null;
   }

   static class SecurityPolicyListHolder {
      static SecurityPolicyList instance = null;

      static {
         ClassLoader var0 = Thread.currentThread().getContextClassLoader();
         Iterator var1 = ServiceFinder.find(SecurityPolicyList.class, var0).iterator();
         if (var1.hasNext()) {
            instance = (SecurityPolicyList)var1.next();
         }

         if (var1.hasNext()) {
            throw new IllegalArgumentException("More than one List for OWSM Security Policies found." + SecurityPolicyList.class);
         }
      }
   }
}
