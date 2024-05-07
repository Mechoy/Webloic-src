package weblogic.management.mbeans.custom;

import com.bea.xml.XmlValidationError;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.ManagementRuntimeException;
import weblogic.management.configuration.ConfigurationExtensionMBean;
import weblogic.management.internal.PendingDirectoryManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.provider.internal.DescriptorInfo;
import weblogic.management.provider.internal.DescriptorInfoUtils;
import weblogic.management.provider.internal.DescriptorManagerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConfigurationExtension extends ConfigurationMBeanCustomizer {
   private static final String SCHEMA_VALIDATION_ENABLED_PROP = "weblogic.configuration.schemaValidationEnabled";
   private static final boolean schemaValidationEnabled = getBooleanProperty("weblogic.configuration.schemaValidationEnabled", true);
   private DescriptorBean extensionRootBean;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ConfigurationExtension(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   protected Descriptor loadDescriptor(DescriptorManager var1, InputStream var2, List var3) throws Exception {
      return var1.createDescriptor(var2, var3);
   }

   public synchronized DescriptorBean getExtensionRoot(Class var1, String var2, String var3) {
      if (this.extensionRootBean != null) {
         return this.extensionRootBean;
      } else {
         Object var4 = null;

         Descriptor var7;
         try {
            ConfigurationExtensionMBean var5 = (ConfigurationExtensionMBean)this.getMbean();
            Descriptor var40 = var5.getDescriptor();
            var7 = null;
            PendingDirectoryManager var8 = PendingDirectoryManager.getInstance();
            boolean var9 = var40.isEditable();
            DescriptorImpl var10 = (DescriptorImpl)var40;
            Map var11 = var10.getContext();
            Boolean var12 = (Boolean)var11.get("DescriptorExtensionLoad");
            String var13;
            if (var12 != null && !var12) {
               var13 = null;
               return var13;
            }

            var13 = var5.getDescriptorFileName();
            String var14 = DomainDir.getConfigDir() + File.separator + var13;
            String var15 = null;
            String var16 = null;
            if (var3 != null) {
               var15 = var3 + File.separator + var13;
               var16 = DomainDir.getConfigDir() + File.separator + var3 + var13;
            }

            Object var17 = null;
            if (!var9) {
               String var41 = DomainDir.getConfigDir() + File.separator + var13;
               var4 = new FileInputStream(var41);
               var17 = DescriptorManagerHelper.getDescriptorManager(false);
               ArrayList var43 = new ArrayList();
               var7 = this.loadDescriptor((DescriptorManager)var17, (InputStream)var4, var43);
               this.checkErrors(var41, var43);
            } else {
               if (!var5.isSet("DescriptorFileName")) {
                  var5.setDescriptorFileName(var13);
               }

               EditableDescriptorManager var18 = (EditableDescriptorManager)DescriptorManagerHelper.getDescriptorManager(true);
               var17 = var18;
               boolean var19 = false;
               Iterator var20 = DescriptorInfoUtils.getDeletedDescriptorInfos(var40);

               DescriptorInfo var21;
               while(var20 != null && var20.hasNext()) {
                  var21 = (DescriptorInfo)var20.next();
                  String var22 = var21.getConfigurationExtension().getDescriptorFileName();
                  if (var13.equals(var22)) {
                     var19 = true;
                  }
               }

               ArrayList var46;
               if (!var19 && (var8.fileExists(var13) || (new File(var14)).exists())) {
                  var4 = var8.getFileAsStream(var13);
                  var46 = new ArrayList();
                  var7 = this.loadDescriptor(var18, (InputStream)var4, var46);
                  this.checkErrors(var13, var46);
               } else if (!var19 && var3 != null && (var8.fileExists(var15) || (new File(var16)).exists())) {
                  var4 = var8.getFileAsStream(var15);
                  var46 = new ArrayList();
                  var7 = this.loadDescriptor(var18, (InputStream)var4, var46);
                  this.checkErrors(var15, var46);
               } else {
                  if (var1 == null) {
                     var21 = null;
                     return var21;
                  }

                  this.extensionRootBean = var18.createDescriptorRoot(var1, "UTF-8").getRootBean();
                  var7 = this.extensionRootBean.getDescriptor();
                  RuntimeAccess var45 = ManagementService.getRuntimeAccess(kernelId);
                  if (var45 != null) {
                     Descriptor var47 = var45.getDomain().getDescriptor();
                     Iterator var23 = DescriptorInfoUtils.getNotFoundDescriptorInfos(var47);

                     while(var23 != null && var23.hasNext()) {
                        DescriptorInfo var24 = (DescriptorInfo)var23.next();
                        ConfigurationExtensionMBean var25 = var24.getConfigurationExtension();
                        if (var5.getName().equals(var25.getName())) {
                           ((DescriptorImpl)var7).setModified(false);
                        }
                     }
                  }
               }
            }

            this.extensionRootBean = var7.getRootBean();
            DescriptorInfo var42 = new DescriptorInfo(var7, var1, this.extensionRootBean, (DescriptorManager)var17, var5);
            DescriptorInfoUtils.addDescriptorInfo(var42, var10);
            DescriptorInfoUtils.setDescriptorConfigExtension(var7, var5, var2);
            DescriptorBean var44 = this.extensionRootBean;
            return var44;
         } catch (FileNotFoundException var37) {
            ManagementLogger.logCouldNotFindSystemResource(this.getMbean().getName());
            DescriptorInfo var6 = new DescriptorInfo((Descriptor)null, var1, (DescriptorBean)null, (DescriptorManager)null, (ConfigurationExtensionMBean)this.getMbean());
            DescriptorInfoUtils.addNotFoundDescriptorInfo(var6, (DescriptorImpl)this.getMbean().getDescriptor());
            var7 = null;
         } catch (Exception var38) {
            throw new ManagementRuntimeException(var38);
         } finally {
            if (var4 != null) {
               try {
                  ((InputStream)var4).close();
               } catch (Exception var36) {
               }
            }

         }

         return var7;
      }
   }

   private void checkErrors(String var1, ArrayList var2) {
      if (var2.size() > 0) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof XmlValidationError) {
               XmlValidationError var5 = (XmlValidationError)var4;
               ManagementLogger.logConfigurationValidationProblem(var1, var5.getMessage());
            } else {
               ManagementLogger.logConfigurationValidationProblem(var1, var4.toString());
            }
         }

         if (schemaValidationEnabled) {
            String var7 = "-Dweblogic.configuration.schemaValidationEnabled=false";
            Loggable var6 = ManagementLogger.logConfigurationSchemaFailureLoggable(var1, var7);
            throw new RuntimeException(var6.getMessage());
         }
      }

   }

   public static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.parseBoolean(var2) : var1;
   }
}
