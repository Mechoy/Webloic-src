package weblogic.wsee.policy.deployment.config;

import java.io.File;
import java.io.IOException;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.wsee.util.StringUtil;

public class DeploymentConfigurationManager {
   private static final DeploymentConfigurationManager INSTANCE = new DeploymentConfigurationManager();
   private static SessionHelper tmpSessionHelper = null;
   private static WebLogicDeploymentManager tmpDeploymentManager = null;

   public static final DeploymentConfigurationManager getInstance() {
      return INSTANCE;
   }

   private DeploymentConfigurationManager() {
   }

   public DeploymentConfigurationHelper getDeploymentConfiguration(AppDeploymentMBean var1) throws ManagementException {
      return createDeploymentConfigurationHelper(var1);
   }

   private static DeploymentConfigurationHelper createDeploymentConfigurationHelper(AppDeploymentMBean var0) throws ManagementException {
      DeploymentConfigurationHelper var1 = null;
      String var2 = var0.getAbsoluteSourcePath();
      String var3 = var0.getAbsoluteInstallDir();
      String var4 = var0.getAbsolutePlanPath();
      File var5 = new File(var2);
      if (StringUtil.isEmpty(var3)) {
         var3 = var5.getParent();
         if (var3 == null) {
            String var6 = var5.getAbsolutePath();
            if (var6 != null) {
               File var7 = new File(var6);
               var3 = var7.getParent();
            }
         }
      }

      try {
         if (StringUtil.isEmpty(var4)) {
            SessionHelper var15 = getSessionHelper();
            var15.setApplication(var5);
            String var16 = var15.getNewPlanName();
            File[] var8 = var15.findPlans();
            if (var8 != null && var8.length > 0) {
               var4 = var8[0].getAbsolutePath() + File.pathSeparator + var16;
            }

            releaseSessionHelper();
            var1 = new DeploymentConfigurationHelper(var3, var2, var4, true, (String)null, 0);
         } else {
            var1 = new DeploymentConfigurationHelper(var3, var2, var4, false, (String)null, 0);
         }
      } catch (DeploymentManagerCreationException var9) {
      } catch (ConfigurationException var10) {
      } catch (IOException var11) {
      } catch (InvalidModuleException var12) {
      } catch (ClassNotFoundException var13) {
      } catch (DDBeanCreateException var14) {
      }

      return var1;
   }

   public static SessionHelper getSessionHelper() throws ManagementException {
      releaseSessionHelper();
      tmpSessionHelper = SessionHelper.getInstance(getTmpDeploymentManager());
      return tmpSessionHelper;
   }

   public static void releaseSessionHelper() {
      if (tmpSessionHelper != null) {
         tmpSessionHelper.close();
         tmpSessionHelper = null;
      }

   }

   public static WebLogicDeploymentManager getTmpDeploymentManager() throws ManagementException {
      if (tmpDeploymentManager == null) {
         try {
            tmpDeploymentManager = SessionHelper.getDeploymentManager((String)null, (String)null);
         } catch (DeploymentManagerCreationException var1) {
            throw new ManagementException(var1);
         }
      }

      return tmpDeploymentManager;
   }
}
