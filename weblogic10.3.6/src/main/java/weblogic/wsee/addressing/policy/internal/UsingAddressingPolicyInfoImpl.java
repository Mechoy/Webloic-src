package weblogic.wsee.addressing.policy.internal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.addressing.policy.api.UsingAddressingPolicyInfo;
import weblogic.wsee.addressing.policy.api.UsingAddressingVersionInfo;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class UsingAddressingPolicyInfoImpl implements UsingAddressingPolicyInfo {
   private Set<UsingAddressingVersionInfo> allWSAVersionInfo = null;
   private UsingAddressingVersionInfo selectedWSAVersionInfo = null;

   public UsingAddressingPolicyInfoImpl() {
   }

   public UsingAddressingPolicyInfoImpl(NormalizedExpression var1) {
      Set var2 = var1.getPolicyAlternatives();
      if (var2 != null) {
         this.allWSAVersionInfo = new HashSet();
         Iterator var3 = var2.iterator();

         while(true) {
            Set var5;
            do {
               if (!var3.hasNext()) {
                  this.selectedWSAVersionInfo = this.selectUsingAddressingVersionInfo(this.allWSAVersionInfo);
                  return;
               }

               PolicyAlternative var4 = (PolicyAlternative)var3.next();
               var5 = var4.getAssertions(WSAUsingAddressingAssertion.class);
            } while(var5 == null);

            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               WSAUsingAddressingAssertion var7 = (WSAUsingAddressingAssertion)var6.next();
               WSAVersion var8 = this.getWSAVersionByPolicyNamespace(var7.getName().getNamespaceURI());
               this.allWSAVersionInfo.add(new UsingAddressingVersionInfo(var8, var7.isRequired()));
            }
         }
      }
   }

   protected UsingAddressingVersionInfo selectUsingAddressingVersionInfo(Set<UsingAddressingVersionInfo> var1) {
      if (var1 != null && var1.size() >= 1) {
         UsingAddressingVersionInfo var2 = null;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            UsingAddressingVersionInfo var4 = (UsingAddressingVersionInfo)var3.next();
            var2 = var4;
            if (WSAVersion.WSA10.equals(var4.getWSAVersion())) {
               break;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public UsingAddressingVersionInfo getUsingAddressingWSAVersionInfo() {
      return this.selectedWSAVersionInfo;
   }

   public boolean isValidWSAVersion(WSAVersion var1) {
      if (var1 == null) {
         return false;
      } else if (this.selectedWSAVersionInfo != null && this.allWSAVersionInfo != null) {
         if (var1.equals(this.selectedWSAVersionInfo.getWSAVersion())) {
            return true;
         } else {
            Iterator var2 = this.allWSAVersionInfo.iterator();

            UsingAddressingVersionInfo var3;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               var3 = (UsingAddressingVersionInfo)var2.next();
            } while(!var1.equals(var3.getWSAVersion()));

            return true;
         }
      } else {
         return true;
      }
   }

   private WSAVersion getWSAVersionByPolicyNamespace(String var1) {
      if (WSAddressingConstants.WSAW_QNAME.getNamespaceURI().equals(var1)) {
         return WSAVersion.MemberSubmission;
      } else {
         return WSAddressingConstants.WSAW_QNAME_10.getNamespaceURI().equals(var1) ? WSAVersion.WSA10 : null;
      }
   }
}
