package weblogic.servlet.internal.dd.compliance;

import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.EjbReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.utils.ErrorCollectionException;

public class EJBRefsComplianceChecker extends BaseComplianceChecker {
   private static final String EJB_LOCAL_REF = "<ejb-local-ref>";
   private static final String EJB_REF = "<ejb-ref>";
   private static final String SESSION = "Session";
   private static final String ENTITY = "Entity";

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      WebAppBean var2 = var1.getWebAppBean();
      if (var2 != null) {
         EjbRefBean[] var3 = var2.getEjbRefs();
         EjbLocalRefBean[] var4 = var2.getEjbLocalRefs();
         int var5;
         if (var3 != null) {
            for(var5 = 0; var5 < var3.length; ++var5) {
               this.checkEJBRef(var3[var5], var1);
            }
         }

         if (var4 != null && var4.length > 0) {
            for(var5 = 0; var5 < var4.length; ++var5) {
               this.checkEJBLocalRef(var4[var5], var1);
            }
         }

         this.checkForExceptions();
      }
   }

   private void checkEJBLocalRef(EjbLocalRefBean var1, DeploymentInfo var2) {
      if (var1 != null) {
         this.validate(var1.getEjbRefName(), var1.getLocalHome(), var1.getLocal(), var1.getEjbRefType(), var1.getEjbLink(), true, var2, true);
      }
   }

   private void checkEJBRef(EjbRefBean var1, DeploymentInfo var2) {
      if (var1 != null) {
         this.validate(var1.getEjbRefName(), var1.getHome(), var1.getRemote(), var1.getEjbRefType(), var1.getEjbLink(), false, var2, false);
      }
   }

   private void validate(String var1, String var2, String var3, String var4, String var5, boolean var6, DeploymentInfo var7, boolean var8) {
      WeblogicWebAppBean var9 = var7.getWeblogicWebAppBean();
      if (var9 == null && (var5 == null || var5.length() == 0)) {
         this.addDescriptorError(this.fmt.NO_EJBLINK_AND_JNDI_NAME(var6 ? "<ejb-local-ref>" : "<ejb-ref>", var1));
      } else {
         if (var9 != null && (var5 == null || var5.length() == 0)) {
            boolean var10 = false;
            EjbReferenceDescriptionBean[] var11 = null;
            var11 = var9.getEjbReferenceDescriptions();
            if (var11 != null) {
               for(int var12 = 0; var12 < var11.length; ++var12) {
                  if (var1.equals(var11[var12].getEjbRefName())) {
                     var10 = true;
                     break;
                  }
               }
            }

            if (!var10) {
               this.addDescriptorError(this.fmt.NO_EJBLINK_AND_JNDI_NAME(var6 ? "<ejb-local-ref>" : "<ejb-ref>", var1));
               return;
            }
         }

         ModuleValidationInfo var13 = var7.getModuleValidationInfo();
         if (var13 != null && var5 != null) {
            var13.addEJBRef(var1, var6, var3, var2, var4, var5, var8);
         }

      }
   }
}
