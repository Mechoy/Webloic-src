package weblogic.servlet.internal.dd.compliance;

import java.util.HashSet;
import weblogic.j2ee.descriptor.FilterBean;
import weblogic.j2ee.descriptor.FilterMappingBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.utils.ErrorCollectionException;

public class FilterComplianceChecker extends BaseComplianceChecker {
   private static final String SUPER_CLASS = "javax.servlet.Filter";
   private static final boolean debug = false;

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      FilterBean[] var2 = var1.getWebAppBean().getFilters();
      FilterMappingBean[] var3 = var1.getWebAppBean().getFilterMappings();
      if (var2 != null || var3 != null) {
         if (var2 != null) {
            HashSet var4 = var2.length > 1 ? new HashSet() : null;

            for(int var5 = 0; var5 < var2.length; ++var5) {
               this.checkFilter(var2[var5], var1.getClassLoader());
               if (var4 != null && !var4.add(var2[var5].getFilterName())) {
                  this.update(this.fmt.warning() + this.fmt.DUPLICATE_FILTER_DEF(var2[var5].getFilterName()));
               }
            }
         }

         this.checkForExceptions();
         int var6;
         if (var3 == null && var2 != null) {
            for(var6 = 0; var6 < var2.length; ++var6) {
               this.update(this.fmt.warning() + this.fmt.NO_MAPPING_FOR_FILTER(var2[var6].getFilterName()));
            }
         }

         if (var3 != null) {
            for(var6 = 0; var6 < var3.length; ++var6) {
               this.checkFilterMapping(var2, var3[var6], var1);
            }
         }

      }
   }

   private void checkFilter(FilterBean var1, ClassLoader var2) throws ErrorCollectionException {
      String var3 = var1.getFilterName();
      if (var3 == null || var3.length() == 0) {
         this.addDescriptorError(this.fmt.NO_FILTER_NAME());
      }

      this.checkForExceptions();
      String var4 = var1.getFilterClass();
      if (var4 == null || var4.length() == 0) {
         this.addDescriptorError(this.fmt.NO_FILTER_CLASS(var3));
      }

      this.checkForExceptions();
      if (var4 != null && var2 != null && !this.isClassAssignable(var2, "filter-class", var4, "javax.servlet.Filter")) {
         this.checkForExceptions();
      }

   }

   private void checkFilterMapping(FilterBean[] var1, FilterMappingBean var2, DeploymentInfo var3) throws ErrorCollectionException {
      String var4 = var2.getFilterName();
      if (var4 != null) {
         this.update(this.fmt.CHECKING_FILTER_MAPPING(var4));
      }

      boolean var5 = false;

      for(int var6 = 0; var6 < var1.length; ++var6) {
         if (var4.equals(var1[var6].getFilterName())) {
            var5 = true;
            break;
         }
      }

      String[] var10 = var2.getUrlPatterns();
      String var7 = null;
      if (var10 != null && var10.length > 0) {
         var7 = var10[0];
      }

      if (!var5) {
         this.addDescriptorError(this.fmt.NO_FILTER_DEF_FOR_MAPPING(var7), new DescriptorErrorInfo(new String[]{"<filter-mapping>", "<url-pattern>"}, var7, new Object[]{"<filter-name>"}));
         this.checkForExceptions();
      }

      String[] var8 = var2.getServletNames();
      int var9;
      if (var8 != null && var8.length > 0) {
         for(var9 = 0; var9 < var8.length; ++var9) {
            if (!this.validateServletName(var8[var9], var3)) {
               this.addDescriptorError(this.fmt.NO_SERVLET_DEF_FOR_FILTER(var8[var9], var4), new DescriptorErrorInfo(new String[]{"<filter-mapping>", "<filter-name>"}, var4, new Object[]{"<servlet-name>"}));
            }
         }
      } else if (var10 != null && var10.length > 0) {
         for(var9 = 0; var9 < var10.length; ++var9) {
            this.validateURLPattern(var4, var10[var9]);
         }
      }

      this.checkForExceptions();
   }

   private void validateURLPattern(String var1, String var2) {
      if (var2 == null || var2.length() == 0) {
         this.addDescriptorError(this.fmt.NO_URL_PATTERN_FOR_FILTER(var1), new DescriptorErrorInfo("<filter-name>", var1, "<url-pattern>"));
      }

   }

   public boolean validateServletName(String var1, DeploymentInfo var2) {
      ServletBean[] var3 = var2.getWebAppBean().getServlets();
      if (var3 != null && var1 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var1.equals(var3[var4].getServletName())) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
