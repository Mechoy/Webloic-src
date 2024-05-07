package weblogic.servlet.internal.dd.compliance;

import java.util.HashSet;
import java.util.Set;
import weblogic.j2ee.descriptor.ErrorPageBean;
import weblogic.utils.ErrorCollectionException;

public class ErrorPageComplianceChecker extends BaseComplianceChecker {
   private Set errorCodeSet;
   private Set exceptionTypeSet;

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      ErrorPageBean[] var2 = var1.getWebAppBean().getErrorPages();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.checkErrorPage(var2[var3], var1);
         }

         this.checkForExceptions();
      }
   }

   private void checkErrorPage(ErrorPageBean var1, DeploymentInfo var2) throws ErrorCollectionException {
      int var3 = var1.getErrorCode();
      String var4 = var1.getExceptionType();
      Object var5 = null;
      boolean var6 = false;
      if (var3 < 1 && var4 != null && var4.length() > 0) {
         this.addDescriptorError(this.fmt.MULTIPLE_DEFINES_ERROR_PAGE("" + var3, var4));
         this.checkForExceptions();
      }

      if (!this.addErrorCode("" + var3)) {
         this.addDescriptorError(this.fmt.DUPLICATE_ERROR_DEF("<error-code>", (String)var5));
      }

      if (var4 != null && var3 < 1) {
         var6 = true;
         ClassLoader var7 = var2.getClassLoader();
         if (var7 != null) {
            this.isClassAssignable(var7, "<exception-type>", var4, "java.lang.Throwable");
         }

         if (!this.addExceptionType(var4)) {
            this.addDescriptorError(this.fmt.DUPLICATE_ERROR_DEF("<exception-type>", var4));
         }
      }

      String var8 = var1.getLocation();
      if (var8 == null || var8.length() == 0) {
         if (var6) {
            this.addDescriptorError(this.fmt.NO_ERROR_PAGE_LOCATION_TYPE(var4));
         } else {
            this.addDescriptorError(this.fmt.NO_ERROR_PAGE_LOCATION_CODE("" + var3));
         }
      }

      this.checkForExceptions();
   }

   private boolean addErrorCode(String var1) {
      if (this.errorCodeSet == null) {
         this.errorCodeSet = new HashSet();
      }

      return this.errorCodeSet.add(var1);
   }

   private boolean addExceptionType(String var1) {
      if (this.exceptionTypeSet == null) {
         this.exceptionTypeSet = new HashSet();
      }

      return this.exceptionTypeSet.add(var1);
   }
}
