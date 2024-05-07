package weblogic.servlet.internal.dd.compliance;

import java.util.HashSet;
import weblogic.j2ee.descriptor.RunAsBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.utils.ErrorCollectionException;

public class ServletComplianceChecker extends BaseComplianceChecker {
   private static final boolean debug = false;
   private static final String SUPER_CLASS = "javax.servlet.Servlet";

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      HashSet var2 = null;
      ServletMappingBean[] var3 = var1.getWebAppBean().getServletMappings();
      ServletBean[] var4 = var1.getWebAppBean().getServlets();
      if (var4 != null || var3 != null) {
         int var5;
         if (var4 != null) {
            if (var4.length > 1) {
               var2 = new HashSet(var4.length);
            }

            for(var5 = 0; var5 < var4.length; ++var5) {
               this.checkServlet(var1, var4[var5], var1.getClassLoader());
               if (var2 != null && !var2.add(var4[var5].getServletName())) {
                  this.addDescriptorError(this.fmt.DUPLICATE_SERVLET_DEF(var4[var5].getServletName()));
               }
            }

            this.checkForExceptions();
         }

         if (var3 != null) {
            for(var5 = 0; var5 < var3.length; ++var5) {
               this.checkServletMapping(var4, var3[var5]);
            }

            this.checkForExceptions();
         }

      }
   }

   private void checkServlet(DeploymentInfo var1, ServletBean var2, ClassLoader var3) throws ErrorCollectionException {
      if (var2 != null) {
         String var4 = var2.getServletName();
         if (var4 == null || var4.length() == 0) {
            this.addDescriptorError(this.fmt.NO_SERVLET_NAME());
         }

         this.checkForExceptions();
         this.update(this.fmt.CHECKING_SERVLET(var2.getServletName()));
         String var5 = var2.getServletClass();
         String var6 = var2.getJspFile();
         if (var5 != null && var5.length() > 0 && var6 != null && var6.length() > 0) {
            this.addDescriptorError(this.fmt.MULTIPLE_DEFINES_SERVLET_DEF(var2.getServletName()), new DescriptorErrorInfo(new String[]{"<servlet>"}, var2.getServletName(), new Object[]{"<jsp-file>", "<servlet-class>"}));
         }

         if ((var5 == null || var5.length() == 0) && (var6 == null || var6.length() == 0)) {
            this.addDescriptorError(this.fmt.NO_SERVLET_DEF(var2.getServletName()), new DescriptorErrorInfo("<servlet-name>", var2.getServletName(), "<servlet-class>"));
         }

         if (var5 != null && var3 != null && !var1.isWebServiceModule()) {
            boolean var7 = false;

            try {
               Class var8 = var3.loadClass(var5);
               var7 = this.hasWebServiceAnnotations(var8);
            } catch (ClassNotFoundException var13) {
            } catch (NoClassDefFoundError var14) {
            } catch (Exception var15) {
            }

            if (!var7 && !this.isClassAssignable(var3, "servlet-class", var5, "javax.servlet.Servlet")) {
               this.checkForExceptions();
            }
         }

         RunAsBean var16 = var2.getRunAs();
         if (var16 != null) {
            String var17 = var16.getRoleName();
            SecurityRoleBean[] var9 = var1.getWebAppBean().getSecurityRoles();
            boolean var10 = false;
            if (var9 != null) {
               for(int var11 = 0; var11 < var9.length; ++var11) {
                  String var12 = var9[var11].getRoleName();
                  if (var12.equals(var17)) {
                     var10 = true;
                     break;
                  }
               }
            }

            if (!var10) {
               this.addDescriptorError(this.fmt.NO_SECURITY_ROLE_FOR_RUNAS(var2.getServletName(), var17), new DescriptorErrorInfo("<servlet>", var2.getServletName(), "<run-as>"));
            }
         }

      }
   }

   private boolean hasWebServiceAnnotations(Class var1) throws ClassNotFoundException {
      return var1.isAnnotationPresent(Class.forName("javax.jws.WebService")) || var1.isAnnotationPresent(Class.forName("javax.xml.ws.WebServiceProvider"));
   }

   private void checkServletMapping(ServletBean[] var1, ServletMappingBean var2) throws ErrorCollectionException {
      String var3 = var2.getServletName();
      if (var3 != null) {
         this.update(this.fmt.CHECKING_SERVLET_MAPPING(var3));
      }

      boolean var4 = false;
      if (var1 != null) {
         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var1[var5].getServletName().equals(var3)) {
               var4 = true;
               break;
            }
         }
      }

      String[] var8 = var2.getUrlPatterns();
      String var6 = null;
      if (var8 != null && var8.length > 0) {
         var6 = var8[0];
      }

      if (!var4) {
         this.addDescriptorError(this.fmt.NO_SERVLET_DEF_FOR_MAPPING(var6), new DescriptorErrorInfo(new String[]{"<servlet-mapping>", "<url-pattern>"}, var6, new Object[]{"<servlet-name>"}));
         this.checkForExceptions();
      }

      if (var8 != null && var8.length > 0) {
         for(int var7 = 0; var7 < var8.length; ++var7) {
            this.validateURLPattern(var3, var8[var7]);
         }
      }

   }

   private void validateURLPattern(String var1, String var2) {
      if (var2 != null && var2.length() != 0) {
         if (var2.equalsIgnoreCase("*.jsp")) {
            this.update(this.fmt.warning() + this.fmt.STAR_JSP_URL_PATTERN(var1));
         }

      } else {
         this.addDescriptorError(this.fmt.NO_URL_PATTERN(var1), new DescriptorErrorInfo("<servlet-name>", var1, "<url-pattern>"));
      }
   }
}
