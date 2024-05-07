package weblogic.servlet.internal.dd.compliance;

import java.util.HashSet;
import java.util.Set;
import weblogic.j2ee.descriptor.AuthConstraintBean;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.UserDataConstraintBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebResourceCollectionBean;
import weblogic.utils.ErrorCollectionException;

public class SecurityConstraintComplianceChecker extends BaseComplianceChecker {
   private Set resourceNames;

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      WebAppBean var2 = var1.getWebAppBean();
      SecurityConstraintBean[] var3 = var2.getSecurityConstraints();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this.checkSecurityConstraint(var3[var4], var1);
      }

   }

   private void checkSecurityConstraint(SecurityConstraintBean var1, DeploymentInfo var2) throws ErrorCollectionException {
      String[] var3 = var1.getDisplayNames();
      String var4 = null;
      if (var3 != null && var3.length > 0) {
         var4 = var3[0];
      }

      WebResourceCollectionBean[] var5 = var1.getWebResourceCollections();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            this.checkResourceCollection(var5[var6]);
         }
      }

      UserDataConstraintBean var15 = var1.getUserDataConstraint();
      if (var15 != null) {
         String var7 = var15.getTransportGuarantee();
         if (!isTransportGuaranteeValid(var7)) {
            this.addDescriptorError(this.fmt.INVALID_TRANSPORT_GUARANTEE(var7));
         }
      }

      AuthConstraintBean var16 = var1.getAuthConstraint();
      if (var16 != null) {
         SecurityRoleBean[] var8 = var2.getWebAppBean().getSecurityRoles();
         String[] var9 = null;
         if (var8 != null) {
            var9 = new String[var8.length];

            for(int var10 = 0; var10 < var8.length; ++var10) {
               var9[var10] = var8[var10].getRoleName();
            }
         }

         String[] var17 = var16.getRoleNames();
         if (var17 != null) {
            for(int var11 = 0; var11 < var17.length; ++var11) {
               String var12 = var17[var11];
               if (var12 != null && "*".equals(var12)) {
                  this.update("info : Since '*' is specified, all roles will be given access to the resource " + (var4 != null ? ": " + var4 : ""));
               } else {
                  boolean var13 = false;

                  for(int var14 = 0; var14 < var9.length; ++var14) {
                     if (var9[var14].equals(var12)) {
                        var13 = true;
                        break;
                     }
                  }

                  if (!var13) {
                     this.addDescriptorError(this.fmt.NO_SECURITY_ROLE_FOR_AUTH(var12));
                  }
               }
            }
         }
      }

      this.checkForExceptions();
   }

   private void checkResourceCollection(WebResourceCollectionBean var1) throws ErrorCollectionException {
      String var2 = var1.getWebResourceName();
      String[] var3 = var1.getUrlPatterns();
      if (!this.addResourceName(var2)) {
         this.addDescriptorError(this.fmt.DUPLICATE_RESOURCE_NAME(var2));
      }

      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.validateURLPattern(var2, var3[var4]);
         }
      }

      this.checkForExceptions();
   }

   private boolean addResourceName(String var1) {
      if (this.resourceNames == null) {
         this.resourceNames = new HashSet();
      }

      return this.resourceNames.add(var1);
   }

   private static boolean isTransportGuaranteeValid(String var0) {
      return "NONE".equals(var0) || "INTEGRAL".equals(var0) || "CONFIDENTIAL".equals(var0);
   }

   private void validateURLPattern(String var1, String var2) {
      if (var2 == null || var2.length() == 0) {
         this.addDescriptorError(this.fmt.ILLEGAL_URL_PATTERN(var1));
      }

   }
}
