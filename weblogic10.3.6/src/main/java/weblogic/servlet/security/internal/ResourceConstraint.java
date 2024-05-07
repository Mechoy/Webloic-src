package weblogic.servlet.security.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.management.DeploymentException;
import weblogic.security.service.ResourceCreationException;
import weblogic.security.service.URLResource;
import weblogic.servlet.HTTPLogger;

final class ResourceConstraint {
   private final String id;
   private final String httpMethod;
   private int transportGuarantee = 0;
   private String[] roles = null;
   private boolean forbidden = false;
   private boolean unrestricted = false;
   private boolean loginRequired = false;

   public ResourceConstraint(String var1, String var2, SecurityConstraintBean var3) {
      this.id = var1;
      this.httpMethod = var2;
      if (var3.getAuthConstraint() != null) {
         this.roles = var3.getAuthConstraint().getRoleNames();
      } else {
         this.unrestricted = true;
      }

      if (var3.getUserDataConstraint() != null) {
         this.transportGuarantee = getTransportGuarantee(var3.getUserDataConstraint().getTransportGuarantee());
      }

   }

   boolean isForbidden() {
      return this.forbidden;
   }

   boolean isUnrestricted() {
      return this.unrestricted;
   }

   boolean isLoginRequired() {
      return this.loginRequired;
   }

   int getTransportGuarantee() {
      return this.transportGuarantee;
   }

   String getHttpMethod() {
      return this.httpMethod;
   }

   String[] getRoles() {
      return this.roles;
   }

   String getResourceId() {
      return this.id;
   }

   void addRoles(String[] var1) {
      String[] var2 = new String[this.roles.length + var1.length];
      ArrayList var3 = new ArrayList(Arrays.asList(this.roles));
      var3.addAll(Arrays.asList(var1));
      var3.toArray(var2);
      this.roles = var2;
   }

   void setTransportGuarantee(int var1) {
      this.transportGuarantee = var1;
   }

   public void deploy(WebAppSecurityWLS var1) throws DeploymentException {
      URLResource var2 = new URLResource(var1.getContext().getApplicationId(), var1.getContext().getContextPath(), this.id, this.httpMethod, (String)null);

      try {
         if (this.unrestricted) {
            var1.getAuthManager().deployUncheckedPolicy(var1.getAuthMgrHandle(), var2);
         } else if (this.roles != null && this.roles.length >= 1) {
            for(int var3 = 0; var3 < this.roles.length; ++var3) {
               if (this.roles[var3].equals("*")) {
                  if (var1.getContext().getConfigManager().isAllowAllRoles()) {
                     this.loginRequired = true;
                     if (var1.isFullSecurityDelegationRequired()) {
                        var1.getAuthManager().deployUncheckedPolicy(var1.getAuthMgrHandle(), var2);
                     }

                     return;
                  }

                  Set var4 = var1.getRoles();
                  if (var4.size() == 0) {
                     this.forbidden = true;
                     var1.getAuthManager().deployExcludedPolicy(var1.getAuthMgrHandle(), var2);
                     return;
                  }

                  this.roles = new String[var4.size()];
                  var4.toArray(this.roles);
                  break;
               }
            }

            var1.getAuthManager().deployPolicy(var1.getAuthMgrHandle(), var2, this.roles);
         } else {
            this.forbidden = true;
            var1.getAuthManager().deployExcludedPolicy(var1.getAuthMgrHandle(), var2);
         }
      } catch (ResourceCreationException var5) {
         this.forbidden = true;
         if (var2 == null) {
            HTTPLogger.logCouldNotDeployPolicy(this.id, var5);
         } else {
            HTTPLogger.logCouldNotDeployPolicy(var2.toString(), var5);
         }

         throw new DeploymentException(var5);
      }
   }

   private static int getTransportGuarantee(String var0) {
      if (var0 == null) {
         return 0;
      } else if ("INTEGRAL".equalsIgnoreCase(var0)) {
         return 1;
      } else {
         return "CONFIDENTIAL".equalsIgnoreCase(var0) ? 2 : 0;
      }
   }
}
