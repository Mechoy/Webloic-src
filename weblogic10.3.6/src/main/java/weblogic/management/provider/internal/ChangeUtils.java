package weblogic.management.provider.internal;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementLogger;
import weblogic.management.WebLogicMBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.CachingRealmMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.ContextCaseMBean;
import weblogic.management.configuration.CustomRealmMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.EmbeddedLDAPMBean;
import weblogic.management.configuration.FileRealmMBean;
import weblogic.management.configuration.FileT3MBean;
import weblogic.management.configuration.JoltConnectionPoolMBean;
import weblogic.management.configuration.LDAPRealmMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ManagedExternalServerMBean;
import weblogic.management.configuration.MaxThreadsConstraintMBean;
import weblogic.management.configuration.MessagingBridgeMBean;
import weblogic.management.configuration.NTRealmMBean;
import weblogic.management.configuration.PathServiceMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.RDBMSRealmMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.ShutdownClassMBean;
import weblogic.management.configuration.StartupClassMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.UnixRealmMBean;
import weblogic.management.configuration.WorkManagerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

final class ChangeUtils {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");

   public static String[] getRestartRequiredServers(BeanUpdateEvent var0) {
      say("getRestartRequiredServers");
      DescriptorBean var1 = var0.getProposedBean();
      if (var1 == null) {
         say("getRestartRequiredServers: bean is null. no servers affected");
         return new String[0];
      } else if (containedByExternal(var1)) {
         say("getRestartRequiredServers: not a server related bean");
         return new String[0];
      } else {
         BeanInfoAccess var2 = ManagementService.getBeanInfoAccess();
         BeanInfo var3 = var2.getBeanInfoForDescriptorBean(var1);
         if (var3 == null) {
            say("getRestartRequiredServers: bean info is null. no servers affected");
            return new String[0];
         } else {
            BeanUpdateEvent.PropertyUpdate[] var4 = var0.getUpdateList();
            say("getRestartRequiredServers: updates.length=" + var4.length);

            for(int var5 = 0; var5 < var4.length; ++var5) {
               BeanUpdateEvent.PropertyUpdate var6 = var4[var5];
               String var7 = var6.getPropertyName();
               if (!var6.isDynamic()) {
                  say("getRestartRequiredServers: found restart required property");
                  DescriptorBean var8 = var0.getSourceBean();
                  ManagementLogger.logNonDynamicAttributeChange(var8.toString(), var7);
                  return getAffectedServers(var0.getProposedBean());
               }
            }

            return new String[0];
         }
      }
   }

   private static String[] getAffectedServers(DescriptorBean var0) {
      say("getRestartRequiredServers");
      if (var0 instanceof StandardInterface) {
         Descriptor var8 = var0.getDescriptor();
         DescriptorBean var9 = var8.getRootBean();
         return var9 instanceof DomainMBean ? getAllServers((DomainMBean)var9) : getAllServers(ManagementService.getRuntimeAccess(KERNEL_ID).getDomain());
      } else if (!(var0 instanceof ConfigurationMBean)) {
         say("getRestartRequiredServers: getAffectedServers: not a config bean");
         return new String[0];
      } else {
         ConfigurationMBean var1 = (ConfigurationMBean)var0;
         say("checking servers affected by: " + var1.getName());
         ServerMBean var2 = containedByServer(var1);
         if (var2 != null) {
            say("getRestartRequiredServers: affected server " + var2.getName());
            String[] var10 = new String[]{var2.getName()};
            return var10;
         } else {
            say("getRestartRequiredServers: Not contained by Server");
            ClusterMBean var3 = containedByCluster(var1);
            if (var3 != null) {
               say("getRestartRequiredServers: affected cluster " + var3.getName());
               return getServers(var3);
            } else {
               say("getRestartRequiredServers: Not contained by Cluster");
               MachineMBean var4 = containedByMachine(var1);
               if (var4 != null) {
                  say("getRestartRequiredServers: affected machine " + var4.getName());
                  return getServers(var4);
               } else {
                  say("getRestartRequiredServers: Not contained by Machine");
                  if (!isSecurityConfiguration(var1) && !isSecurity(var1) && !isEmbeddedLDAP(var1) && !is6xRealm(var1)) {
                     say("getRestartRequiredServers: not security setting");
                     String[] var5 = getTargetServersIfDeployableNeedsRestart(var1);
                     String var6;
                     if (var5 == null) {
                        if (debugLogger.isDebugEnabled()) {
                           var6 = "Warning: NON-DYNAMIC CHANGE IN " + var1.getType() + "{" + var1.getName() + "}";
                           debugLogger.debug(var6);
                        }

                        return getAllServers(getDomain(var1));
                     } else {
                        if (debugLogger.isDebugEnabled()) {
                           var6 = "NON-DYNAMIC change for Deployable with target servers: ";

                           for(int var7 = 0; var7 < var5.length; ++var7) {
                              var6 = var6 + var5[var7] + (var7 < var5.length ? ", " : "");
                           }

                           debugLogger.debug(var6);
                        }

                        return var5;
                     }
                  } else {
                     say("getRestartRequiredServers: non-dynamic security setting");
                     return getAllServers(getDomain(var1));
                  }
               }
            }
         }
      }
   }

   private static String[] getTargetServersIfDeployableNeedsRestart(ConfigurationMBean var0) {
      boolean var1 = false;

      DeploymentMBean var2;
      WebLogicMBean var3;
      for(var2 = null; var0 != null; var0 = var3 instanceof ConfigurationMBean ? (ConfigurationMBean)var3 : null) {
         if (var0 instanceof TargetInfoMBean) {
            return new String[0];
         }

         if (var0 instanceof DeploymentMBean) {
            if (var0 instanceof ShutdownClassMBean || var0 instanceof ContextCaseMBean || var0 instanceof FileT3MBean || var0 instanceof JoltConnectionPoolMBean || var0 instanceof MaxThreadsConstraintMBean || var0 instanceof MessagingBridgeMBean || var0 instanceof PathServiceMBean || var0 instanceof PersistentStoreMBean || var0 instanceof WorkManagerMBean) {
               var1 = true;
            }

            var2 = (DeploymentMBean)var0;
            break;
         }

         var3 = var0.getParent();
      }

      if (var2 == null) {
         return null;
      } else if (!var1) {
         return new String[0];
      } else {
         TargetMBean[] var6 = var2.getTargets();
         if (var6 == null) {
            return new String[0];
         } else {
            HashSet var4 = new HashSet();

            for(int var5 = 0; var5 < var6.length; ++var5) {
               var4.addAll(var6[var5].getServerNames());
            }

            return (String[])var4.toArray(new String[var4.size()]);
         }
      }
   }

   private static ServerMBean containedByServer(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof ServerMBean) {
            return (ServerMBean)var0;
         }

         WebLogicMBean var1 = var0.getParent();
         var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
      }

      return null;
   }

   private static boolean containedByExternal(DescriptorBean var0) {
      WebLogicMBean var2;
      if (var0 instanceof ConfigurationMBean) {
         for(ConfigurationMBean var1 = (ConfigurationMBean)var0; var1 != null; var1 = var2 instanceof ConfigurationMBean ? (ConfigurationMBean)var2 : null) {
            if (var1 instanceof ManagedExternalServerMBean) {
               return true;
            }

            var2 = var1.getParent();
         }
      }

      return false;
   }

   private static ClusterMBean containedByCluster(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof ClusterMBean) {
            return (ClusterMBean)var0;
         }

         WebLogicMBean var1 = var0.getParent();
         var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
      }

      return null;
   }

   private static MachineMBean containedByMachine(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof MachineMBean) {
            return (MachineMBean)var0;
         }

         WebLogicMBean var1 = var0.getParent();
         var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
      }

      return null;
   }

   private static boolean isSecurityConfiguration(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof SecurityConfigurationMBean) {
            return true;
         }

         WebLogicMBean var1 = var0.getParent();
         var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
      }

      return false;
   }

   private static boolean isSecurity(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof SecurityMBean) {
            return true;
         }

         WebLogicMBean var1 = var0.getParent();
         var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
      }

      return false;
   }

   private static boolean isEmbeddedLDAP(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof EmbeddedLDAPMBean) {
            return true;
         }

         WebLogicMBean var1 = var0.getParent();
         var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
      }

      return false;
   }

   private static boolean is6xRealm(ConfigurationMBean var0) {
      while(true) {
         if (var0 != null) {
            if (!(var0 instanceof CachingRealmMBean) && !(var0 instanceof CustomRealmMBean) && !(var0 instanceof FileRealmMBean) && !(var0 instanceof LDAPRealmMBean) && !(var0 instanceof NTRealmMBean) && !(var0 instanceof RDBMSRealmMBean) && !(var0 instanceof UnixRealmMBean)) {
               WebLogicMBean var1 = var0.getParent();
               var0 = var1 instanceof ConfigurationMBean ? (ConfigurationMBean)var1 : null;
               continue;
            }

            return true;
         }

         return false;
      }
   }

   private static String[] getServers(ClusterMBean var0) {
      ServerMBean[] var1 = var0.getServers();
      if (var1 == null) {
         return new String[0];
      } else {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = var1[var3].getName();
         }

         return var2;
      }
   }

   private static String[] getServers(MachineMBean var0) {
      ServerMBean[] var1 = getDomain(var0).getServers();
      if (var1 == null) {
         return new String[0];
      } else {
         int var2 = 0;
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (var1[var4].getMachine() != null && var0.getName().equals(var1[var4].getMachine().getName())) {
               var3.add(var1[var4].getName());
               ++var2;
            }
         }

         if (var2 == 0) {
            return new String[0];
         } else {
            String[] var6 = new String[var2];

            for(int var5 = 0; var5 < var6.length; ++var5) {
               var6[var5] = (String)var3.get(var5);
            }

            return var6;
         }
      }
   }

   private static String[] getAllServers(DomainMBean var0) {
      if (var0 == null) {
         return new String[0];
      } else {
         ServerMBean[] var1 = var0.getServers();
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getName();
         }

         return var2;
      }
   }

   private static DomainMBean getDomain(ConfigurationMBean var0) {
      while(var0 != null) {
         if (var0 instanceof DomainMBean) {
            return (DomainMBean)var0;
         }

         WebLogicMBean var1 = var0.getParent();
         if (var1 instanceof ConfigurationMBean) {
            var0 = (ConfigurationMBean)var1;
         } else {
            var0 = null;
         }
      }

      return null;
   }

   public static boolean getRestartValue(PropertyDescriptor var0) {
      if (var0 == null) {
         say("getRestartValue: property descriptor is null, return true");
         return true;
      } else {
         say("getRestartValue: checking restart value of " + var0.getName());
         Class var1 = var0.getPropertyType();
         if (var1.isArray()) {
            var1 = var1.getComponentType();
         }

         if (!TargetInfoMBean.class.isAssignableFrom(var1) && (!DeploymentMBean.class.isAssignableFrom(var1) || StartupClassMBean.class.isAssignableFrom(var1) || ShutdownClassMBean.class.isAssignableFrom(var1) || ContextCaseMBean.class.isAssignableFrom(var1) || FileT3MBean.class.isAssignableFrom(var1) || JoltConnectionPoolMBean.class.isAssignableFrom(var1) || MaxThreadsConstraintMBean.class.isAssignableFrom(var1) || MessagingBridgeMBean.class.isAssignableFrom(var1) || PathServiceMBean.class.isAssignableFrom(var1) || PersistentStoreMBean.class.isAssignableFrom(var1) || WorkManagerMBean.class.isAssignableFrom(var1))) {
            try {
               say("getRestartValue: value of dynamic is " + var0.getValue("dynamic"));
               Boolean var2 = (Boolean)var0.getValue("dynamic");
               if (var2 != null) {
                  if (var2) {
                     say("return false");
                     return false;
                  }

                  say("return true");
                  return true;
               }

               say("getRestartValue: value of non-dynamic is " + var0.getValue("non-dynamic"));
               Boolean var3 = (Boolean)var0.getValue("non-dynamic");
               if (var3 != null) {
                  if (!var3) {
                     say("return false");
                     return false;
                  }

                  say("return true");
                  return true;
               }
            } catch (Exception var4) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("ChangeUtils: " + var4, var4);
               }
            }

            say("return true");
            return true;
         } else {
            return false;
         }
      }
   }

   private static void say(String var0) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("ChangeUtils: " + var0);
      }

   }

   private static class DeploymentRestartInfo {
      boolean restartRequired = false;
      TargetInfoMBean targetInfo = null;

      DeploymentRestartInfo(ConfigurationMBean var1) {
      }
   }
}
