package weblogic.deploy.api.internal.utils;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.validators.DeploymentPlanBeanValidator;
import weblogic.logging.Loggable;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class ConfigHelper {
   public static File getConfigRootFile(DeploymentPlanBean var0) {
      return var0 != null && var0.getConfigRoot() != null ? new File(var0.getConfigRoot()) : null;
   }

   public static File getAppRootFromPlan(DeploymentPlanBean var0) {
      if (var0 != null && var0.getConfigRoot() != null) {
         File var1 = (new File(var0.getConfigRoot())).getParentFile();
         return var1;
      } else {
         return null;
      }
   }

   public static void initPlanDirFromPlan(DeploymentPlanBean var0, InstallDir var1) {
      if (var0 != null && var0.getConfigRoot() != null) {
         File var2 = (new File(var0.getConfigRoot())).getAbsoluteFile();

         try {
            DeploymentPlanBeanValidator.validateConfigRoot(var0.getConfigRoot());
         } catch (IllegalArgumentException var4) {
         }

         if (var2.exists() && var2.isDirectory()) {
            var1.setConfigDir(var2);
            var0.setConfigRoot(var1.getConfigDir().getPath());
         } else {
            var1.setConfigDir((File)null);
            var0.setConfigRoot(var1.getConfigDir().getPath());
         }
      } else {
         var1.setConfigDir((File)null);
      }

   }

   public static String getText(DDBean var0) {
      String var1 = var0.getText();
      return var1 == null ? "" : var1;
   }

   public static String getNSPrefix(DDBean var0, String var1) {
      if (var0 instanceof DDBeanRoot) {
         String var2 = var0.getText();
         if (var2 == null) {
            return null;
         } else {
            int var3 = var2.indexOf(var1);
            if (var3 == -1) {
               return null;
            } else {
               int var4 = var2.indexOf("xmlns");
               if (var4 == -1) {
                  return null;
               } else {
                  return var2.charAt(var4 + "xmlns".length()) == '=' ? null : var2.substring(var4 + "xmlns".length() + 1, var3 - 2);
               }
            }
         }
      } else {
         return getNSPrefix(var0.getRoot(), var1);
      }
   }

   public static String applyNamespace(String var0, String var1) {
      if (var0 != null) {
         StringTokenizer var2 = new StringTokenizer(var1, "/");
         StringBuffer var3 = new StringBuffer();

         while(var2.hasMoreTokens()) {
            String var4 = var2.nextToken();
            if (var4.indexOf(":") == -1) {
               var3.append(var0 + ":");
            }

            var3.append(var4);
            if (var2.hasMoreTokens()) {
               var3.append("/");
            }
         }

         return var3.toString();
      } else {
         return var1;
      }
   }

   public static void beanWalker(DDBeanRoot var0, DConfigBeanRoot var1) throws ConfigurationException {
      checkParam("DDBeanRoot", var0);
      checkParam("DConfigBeanRoot", var1);
      beanWalker((DDBean)var0, (DConfigBean)var1);
   }

   private static void beanWalker(DDBean var0, DConfigBean var1) throws ConfigurationException {
      if (var1 != null) {
         String[] var4 = var1.getXpaths();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               DDBean[] var3 = var0.getChildBean(var4[var5]);
               if (var3 != null) {
                  for(int var6 = 0; var6 < var3.length; ++var6) {
                     DConfigBean var2 = var1.getDConfigBean(var3[var6]);
                     beanWalker(var3[var6], var2);
                  }
               }
            }

         }
      }
   }

   public static void checkParam(String var0, Object var1) {
      if (var1 == null) {
         Loggable var2 = SPIDeployerLogger.logNullParamLoggable(var0);
         var2.log();
         throw new IllegalArgumentException(var2.getMessage());
      }
   }

   private static String extractLibraryName(File var0, DeploymentOptions var1) {
      try {
         if (var1 != null && var1.isLibrary() && var0 != null) {
            VirtualJarFile var2 = VirtualJarFactory.createVirtualJar(var0);
            return ApplicationVersionUtils.getLibName(var2);
         }
      } catch (IOException var3) {
      }

      return null;
   }

   public static String normalize(String var0) {
      if (var0 == null) {
         return null;
      } else if ("/".equals(File.separator)) {
         if (var0.length() >= 2 && var0.charAt(1) == ':') {
            var0 = var0.substring(2);
         }

         return var0.replace('\\', File.separatorChar);
      } else {
         return var0.replace('/', File.separatorChar);
      }
   }

   public static File normalize(File var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = normalize(var0.getPath());
         return new File(var1);
      }
   }

   public static String getAppName(DeploymentOptions var0, File var1, DeploymentPlanBean var2) {
      String var3 = null;
      var3 = extractLibraryName(var1, var0);
      if (var3 != null) {
         return var3;
      } else if (var0 != null && var0.getName() != null) {
         return appendVersionToAppName(var0.getName(), var0);
      } else if (var2 != null && var2.getApplicationName() != null && var2.getApplicationName().length() > 0) {
         return var2.getApplicationName();
      } else {
         if (var1 != null) {
            var3 = normalize(var1).getName();
         }

         return appendVersionToAppName(var3, var0);
      }
   }

   private static String appendVersionToAppName(String var0, DeploymentOptions var1) {
      if (var0 != null && var1 != null && var1.getArchiveVersion() != null) {
         StringBuffer var2 = new StringBuffer(var0);
         var2.append('#');
         var2.append(var1.getArchiveVersion());
         if (var1.getPlanVersion() != null) {
            var2.append('#');
            var2.append(var1.getPlanVersion());
         }

         var0 = var2.toString();
      }

      return var0;
   }

   public static String getAppName(TargetModuleID[] var0, DeploymentOptions var1) {
      if (var1 != null && var1.getName() != null) {
         return var1.getName();
      } else if (var0 != null && var0.length != 0) {
         return getAppName(var0[0]);
      } else {
         throw new IllegalArgumentException(SPIDeployerLogger.nullTmids());
      }
   }

   public static String getAppName(TargetModuleID var0) {
      return var0.getModuleID();
   }
}
