package weblogic.servlet.internal.dd.compliance;

import weblogic.j2ee.descriptor.JspConfigBean;
import weblogic.j2ee.descriptor.ListenerBean;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.SessionConfigBean;
import weblogic.j2ee.descriptor.TagLibBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.ResourceEnvDescriptionBean;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StringUtils;

public class WebAppDescriptorComplianceChecker extends BaseComplianceChecker {
   private static final String PARENT_CLASS = "java.util.EventListener";
   private static final String[] LISTENER_CLASSES = new String[]{"javax.servlet.ServletContextListener", "javax.servlet.ServletContextAttributeListener", "javax.servlet.ServletRequestListener", "javax.servlet.ServletRequestAttributeListener", "javax.servlet.http.HttpSessionListener", "javax.servlet.http.HttpSessionActivationListener", "javax.servlet.http.HttpSessionAttributeListener", "javax.servlet.http.HttpSessionBindingListener"};
   private static final String SHAREABLE = "Shareable";
   private static final String UNSHAREABLE = "Unshareable";
   private static final String AUTH_APPLICATION = "Application";
   private static final String AUTH_CONTAINER = "Container";
   private static final String AM_BASIC = "BASIC";
   private static final String AM_DIGEST = "DIGEST";
   private static final String AM_FORM = "FORM";
   private static final String AM_CLIENT_CERT = "CLIENT-CERT";

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      WebAppBean var2 = var1.getWebAppBean();
      SessionConfigBean[] var3 = var2.getSessionConfigs();
      if (var3 != null && var3.length > 0) {
         this.validateSessionConfig(var3[0]);
      }

      JspConfigBean[] var4 = var2.getJspConfigs();
      int var6;
      if (var4 != null && var4.length > 0) {
         TagLibBean[] var5 = var4[0].getTagLibs();
         if (var5 != null) {
            for(var6 = 0; var6 < var5.length; ++var6) {
               this.validateTagLib(var5[var6]);
            }
         }
      }

      ListenerBean[] var8 = var2.getListeners();
      if (var8 != null) {
         for(var6 = 0; var6 < var8.length; ++var6) {
            this.validateListeners(var8[var6], var1);
         }
      }

      ResourceEnvRefBean[] var9 = var2.getResourceEnvRefs();
      this.validateResourceEnvRefs(var9, var1);
      LoginConfigBean[] var7 = var2.getLoginConfigs();
      if (var7 != null && var7.length > 0) {
         this.validateLoginDescriptor(var7[0]);
      }

      this.checkForExceptions();
   }

   private void validateSessionConfig(SessionConfigBean var1) {
      if (var1 != null) {
         ;
      }
   }

   private void validateListeners(ListenerBean var1, DeploymentInfo var2) throws ErrorCollectionException {
      if (var1 != null) {
         String var3 = var1.getListenerClass();
         ClassLoader var4 = var2.getClassLoader();
         if (var3 != null && var4 != null) {
            Class var5 = null;
            Class var6 = null;

            try {
               var5 = var4.loadClass(var3);
               var6 = var4.loadClass("java.util.EventListener");
            } catch (ClassNotFoundException var11) {
               this.addDescriptorError(this.fmt.CLASS_NOT_FOUND("listener", var3));
               return;
            }

            boolean var7 = var6.isAssignableFrom(var5);
            if (!var7) {
               this.addDescriptorError(this.fmt.INVALID_LISTENER_CLASS(var3));
               return;
            }

            boolean var8 = false;

            for(int var9 = 0; var9 < LISTENER_CLASSES.length; ++var9) {
               try {
                  Class var10 = var4.loadClass(LISTENER_CLASSES[var9]);
                  var8 = var10.isAssignableFrom(var5);
                  if (var8) {
                     break;
                  }
               } catch (ClassNotFoundException var12) {
                  this.addDescriptorError(this.fmt.CLASS_NOT_FOUND("listener", LISTENER_CLASSES[var9]));
                  this.checkForExceptions();
               }
            }

            if (!var8) {
               this.addDescriptorError(this.fmt.INVALID_LISTENER_CLASS(var3));
               this.checkForExceptions();
            }
         }

      }
   }

   private void validateTagLib(TagLibBean var1) {
      String var2 = var1.getTagLibLocation();
      String var3 = var1.getTagLibUri();
      if (var3 == null || var3.length() == 0) {
         this.addDescriptorError(this.fmt.NO_TAGLIB_URI());
      }

      if (var2 == null || var2.length() == 0) {
         this.addDescriptorError(this.fmt.NO_TAGLIB_LOCATION());
      }

   }

   private void validateResourceReferences(ResourceRefBean[] var1, DeploymentInfo var2) {
      if (var1 != null) {
         ResourceDescriptionBean[] var3 = null;
         if (var2.getWeblogicWebAppBean() != null) {
            var3 = var2.getWeblogicWebAppBean().getResourceDescriptions();
         }

         for(int var4 = 0; var4 < var1.length; ++var4) {
            String var5 = null;
            String var6 = var1[var4].getResRefName();
            String var7 = var1[var4].getResSharingScope();
            String var8 = var1[var4].getResAuth();
            boolean var9 = false;
            if (var6 != null && var3 != null) {
               for(int var10 = 0; var10 < var3.length; ++var10) {
                  String var11 = var3[var10].getResRefName();
                  if (var6.equals(var11)) {
                     var9 = true;
                     var5 = var3[var10].getJNDIName();
                     break;
                  }
               }

               if (!var9) {
                  this.addDescriptorError(this.fmt.NO_RES_DESC_FOR_RESOURCE_REF(var6));
               } else if (var5 == null || var5.equals("")) {
                  this.addDescriptorError(this.fmt.NO_JNDI_NAME_FOR_RESOURCE_DESCRIPTOR(var6));
               }
            }

            if (var7 != null && !"Shareable".equalsIgnoreCase(var7) && !"Unshareable".equalsIgnoreCase(var7)) {
               this.addDescriptorError(this.fmt.INVALID_SHARING_SCOPE(var7, var6));
            }

            if (!"Application".equalsIgnoreCase(var8) && !"Container".equalsIgnoreCase(var8)) {
               this.addDescriptorError(this.fmt.INVALID_RES_AUTH(var6, var8));
            }
         }

      }
   }

   private void validateResourceEnvRefs(ResourceEnvRefBean[] var1, DeploymentInfo var2) {
      if (var1 != null) {
         ResourceEnvDescriptionBean[] var3 = null;
         if (var2.getWeblogicWebAppBean() != null) {
            var3 = var2.getWeblogicWebAppBean().getResourceEnvDescriptions();
         }

         if (var3 != null) {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               String var5 = var1[var4].getResourceEnvRefName();
               boolean var6 = false;
               if (var3 != null && var5 != null) {
                  for(int var7 = 0; var7 < var3.length; ++var7) {
                     if (var5.equals(var3[var7].getResourceEnvRefName())) {
                        var6 = true;
                        break;
                     }
                  }
               }

               if (!var6) {
                  this.addDescriptorError(this.fmt.NO_RES_DESC_FOR_ENV_REF(var5));
               }
            }
         }

      }
   }

   private void validateLoginDescriptor(LoginConfigBean var1) {
      if (var1 != null) {
         String var2 = var1.getAuthMethod();
         if (var2 != null) {
            String[] var3 = StringUtils.splitCompletely(var2, ", ");
            if (var3.length == 0) {
               this.addDescriptorError(this.fmt.INVALID_AUTH_METHOD(var2));
            }

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (!var3[var4].equals("BASIC") && !var3[var4].equals("FORM") && !var3[var4].equals("CLIENT-CERT") && !var3[var4].equals("DIGEST")) {
                  this.addDescriptorError(this.fmt.INVALID_AUTH_METHOD(var3[var4]));
               }

               if ((var3[var4].equals("BASIC") || var3[var4].equals("FORM")) && var4 != var3.length - 1) {
                  this.addDescriptorError(this.fmt.INVALID_AUTH_METHOD(var2));
               }

               if (var3[var4].equals("FORM")) {
                  if (var1.getFormLoginConfig() == null || var1.getFormLoginConfig().getFormLoginPage() == null || var1.getFormLoginConfig().getFormLoginPage().length() < 1) {
                     this.addDescriptorError(this.fmt.LOGIN_PAGE_MISSING());
                  }

                  if (var1.getFormLoginConfig() == null || var1.getFormLoginConfig().getFormErrorPage() == null || var1.getFormLoginConfig().getFormErrorPage().length() < 1) {
                     this.addDescriptorError(this.fmt.ERROR_PAGE_MISSING());
                  }
               }

               if (var3[var4].equals("DIGEST")) {
                  this.update(this.fmt.warning() + this.fmt.DIGEST_NOT_IMPLEMENTED());
               }
            }

         }
      }
   }
}
