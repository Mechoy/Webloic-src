package weblogic.wsee.jaxws;

import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import weblogic.wsee.addressing.policy.api.UsingAddressingVersionInfo;
import weblogic.wsee.addressing.policy.internal.WSAUsingAddressingAssertion;
import weblogic.wsee.mtom.internal.OptimizedMimeSerialization;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class EndpointPolicyUtility {
   public static boolean checkMTOMPolicy(NormalizedExpression var0) {
      Set var1 = var0.getPolicyAlternatives();
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            PolicyAlternative var3 = (PolicyAlternative)var2.next();
            Set var4 = var3.getAssertions(OptimizedMimeSerialization.class);
            if (var4 != null && !var4.isEmpty()) {
               return true;
            }
         }
      }

      return false;
   }

   public static WebServiceFeature checkUsingAddressingPolicy(NormalizedExpression var0) {
      Set var1 = var0.getPolicyAlternatives();
      if (var1 != null) {
         HashSet var2 = new HashSet();
         Iterator var3 = var1.iterator();

         while(true) {
            Set var5;
            do {
               do {
                  if (!var3.hasNext()) {
                     UsingAddressingVersionInfo var9 = selectUsingAddressingVersionInfo(var2);
                     if (var9 != null) {
                        switch (var9.getWSAVersion()) {
                           case MemberSubmission:
                              return new MemberSubmissionAddressingFeature(true, var9.isRequired());
                           case WSA10:
                              return new AddressingFeature(true, var9.isRequired());
                        }
                     }

                     return null;
                  }

                  PolicyAlternative var4 = (PolicyAlternative)var3.next();
                  var5 = var4.getAssertions(WSAUsingAddressingAssertion.class);
               } while(var5 == null);
            } while(var5.isEmpty());

            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               WSAUsingAddressingAssertion var7 = (WSAUsingAddressingAssertion)var6.next();
               WSAVersion var8 = getWSAVersionByPolicyNamespace(var7.getName().getNamespaceURI());
               var2.add(new UsingAddressingVersionInfo(var8, var7.isRequired()));
            }
         }
      } else {
         return null;
      }
   }

   private static WSAVersion getWSAVersionByPolicyNamespace(String var0) {
      if (WSAddressingConstants.WSAW_QNAME.getNamespaceURI().equals(var0)) {
         return WSAVersion.MemberSubmission;
      } else {
         return WSAddressingConstants.WSAW_QNAME_10.getNamespaceURI().equals(var0) ? WSAVersion.WSA10 : null;
      }
   }

   private static UsingAddressingVersionInfo selectUsingAddressingVersionInfo(Set<UsingAddressingVersionInfo> var0) {
      if (var0 != null && var0.size() >= 1) {
         UsingAddressingVersionInfo var1 = null;
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            UsingAddressingVersionInfo var3 = (UsingAddressingVersionInfo)var2.next();
            var1 = var3;
            if (WSAVersion.WSA10.equals(var3.getWSAVersion())) {
               break;
            }
         }

         return var1;
      } else {
         return null;
      }
   }
}
