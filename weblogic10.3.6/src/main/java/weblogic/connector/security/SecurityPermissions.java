package weblogic.connector.security;

import java.net.URL;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.SecPermSpecUtils;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.SecurityPermissionInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SupplementalPolicyObject;

public class SecurityPermissions {
   private static AuthenticatedSubject kernelId = null;

   public static void unSetSecurityPermissions(RAInfo var0) {
      URL var1 = var0.getURL();
      SupplementalPolicyObject.removePolicies(getKernelId(), var1);
   }

   public static void setSecurityPermissions(RAInfo var0) {
      setSecurityPermissions(var0, (ApplicationContextInternal)null);
   }

   public static void setSecurityPermissions(RAInfo var0, ApplicationContextInternal var1) {
      URL var2 = var0.getURL();
      List var3 = var0.getSecurityPermissions();
      String var6 = null;
      if (var3 != null && !var3.isEmpty()) {
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            SecurityPermissionInfo var4 = (SecurityPermissionInfo)var5.next();
            var6 = var4.getSpec();
            SupplementalPolicyObject.setPoliciesFromGrantStatement(getKernelId(), var2, var1 == null ? var6 : SecPermSpecUtils.getNewSecurityPermissionSpec(var1, var6), "CONNECTOR");
         }
      } else {
         SupplementalPolicyObject.setPoliciesFromGrantStatement(getKernelId(), var2, var1 == null ? var6 : SecPermSpecUtils.getNewSecurityPermissionSpec(var1, var6), "CONNECTOR");
      }

   }

   private static AuthenticatedSubject getKernelId() {
      if (kernelId == null) {
         kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      }

      return kernelId;
   }
}
