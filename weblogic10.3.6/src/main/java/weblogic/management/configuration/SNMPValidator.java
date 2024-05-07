package weblogic.management.configuration;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import weblogic.common.internal.VersionInfo;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.WebLogicMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceClient;
import weblogic.management.provider.beaninfo.BeanInfoAccess;

public class SNMPValidator {
   private static final int AUTH_FLAG = 1;
   private static final int PRIV_FLAG = 2;
   private static final boolean DEBUG = false;
   private static BeanInfoAccess beanInfoAccess;

   public static void validateSNMPAgentDeployments(DomainMBean var0) {
      SNMPAgentDeploymentMBean[] var1 = var0.getSNMPAgentDeployments();
      if (var1 != null) {
         HashMap var2 = new HashMap();
         ServerMBean[] var3 = var0.getServers();

         int var4;
         for(var4 = 0; var4 < var3.length; ++var4) {
            var2.put(var3[var4].getName(), (Object)null);
         }

         for(var4 = 0; var4 < var1.length; ++var4) {
            SNMPAgentDeploymentMBean var5 = var1[var4];
            TargetMBean[] var6 = var5.getTargets();
            ServerMBean[] var7 = WLDFValidator.getServersInTargets(var6);
            if (var7 != null) {
               for(int var8 = 0; var8 < var7.length; ++var8) {
                  String var9 = var7[var8].getName();
                  String var10 = (String)var2.get(var9);
                  if (var10 != null) {
                     throw new IllegalArgumentException(SNMPLogger.logSNMPAgentDeployedToServerLoggable(var10, var9).getMessage());
                  }

                  var2.put(var9, var5.getName());
               }
            }
         }

      }
   }

   public static void validateSNMPProxy(SNMPProxyMBean var0) {
      if (var0 != null && var0.getParent() != null) {
         WebLogicMBean var1 = var0.getParent();
         if (var1 instanceof SNMPAgentMBean) {
            validateProxy(var0, (SNMPAgentMBean)var1);
         }
      }

   }

   private static void validateProxy(SNMPProxyMBean var0, SNMPAgentMBean var1) {
      String var2 = var0.getSecurityName();
      String var3 = var0.getSecurityLevel();
      if (var2 != null && var2.length() != 0) {
         int var4 = getSecurityLevel(var1);
         byte var5 = 0;
         if (var3.equals("authNoPriv")) {
            var5 = 1;
         } else if (var3.equals("authPriv")) {
            var5 = 3;
         }

         if (var5 > var4) {
            throw new IllegalArgumentException(SNMPLogger.logSNMPProxyInvalidSecurityLevelLoggable(var1.getName(), getAgentSecurityLevelString(var4), var0.getName(), var3).getMessage());
         }
      }

   }

   public static void validateSNMPAgent(SNMPAgentMBean var0) {
      String var1;
      if (var0.getSNMPTrapVersion() == 1 && var0.isInformEnabled()) {
         var1 = SNMPLogger.logSNMPInvalidTrapVersionLoggable().getMessageBody();
         throw new IllegalArgumentException(var1);
      } else {
         var1 = var0.getAuthenticationProtocol();
         String var2 = var0.getPrivacyProtocol();
         if (var1.equals("noAuth") && !var2.equals("noPriv")) {
            String var10 = SNMPLogger.logInvalidAuthenticationProtocolLoggable(var0.getName(), var2).getMessageBody();
            throw new IllegalArgumentException(var10);
         } else {
            int var3 = getSecurityLevel(var0);
            String var4 = getAgentSecurityLevelString(var3);
            SNMPTrapDestinationMBean[] var5 = var0.getSNMPTrapDestinations();
            if (var5 != null) {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  String var7 = var5[var6].getSecurityLevel();
                  byte var8 = 0;
                  if (var7.equals("authNoPriv")) {
                     var8 = 1;
                  } else if (var7.equals("authPriv")) {
                     var8 = 3;
                  }

                  if (var3 < var8) {
                     String var9 = SNMPLogger.logInvalidSNMPTrapDestinationSecurityLevelLoggable(var5[var6].getName(), var5[var6].getSecurityLevel(), var4).getMessageBody();
                     throw new IllegalArgumentException(var9);
                  }

                  validateSNMPTrapDestination(var5[var6]);
               }
            }

         }
      }
   }

   private static String getAgentSecurityLevelString(int var0) {
      String var1 = "noAuthNoPriv";
      switch (var0) {
         case 1:
            var1 = "authNoPriv";
            break;
         case 3:
            var1 = "authPriv";
            break;
         default:
            var1 = "noAuthNoPriv";
      }

      return var1;
   }

   private static void validateSNMPTrapDestination(SNMPTrapDestinationMBean var0) {
      SNMPAgentMBean var1 = (SNMPAgentMBean)var0.getParent();
      if (var1.getSNMPTrapVersion() == 3 && (var0.getSecurityName() == null || var0.getSecurityName().length() == 0)) {
         String var2 = SNMPLogger.logSecurityNameNotSpecifiedForV3TrapDestinationLoggable(var0.getName()).getMessageBody();
         throw new IllegalArgumentException(var2);
      }
   }

   public static void validateJMXMonitorMBean(SNMPJMXMonitorMBean var0) {
      String var1 = var0.getMonitoredMBeanType();
      String var2 = var0.getMonitoredAttributeName();
      BeanInfo var3 = getBeanInfo(var1);
      if (var3 == null) {
         Loggable var8 = SNMPLogger.logInvalidTypeNameLoggable(var1);
         throw new IllegalArgumentException(var8.getMessageBody());
      } else {
         PropertyDescriptor[] var4 = var3.getPropertyDescriptors();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               PropertyDescriptor var6 = var4[var5];
               String var7 = var6.getName();
               if (var7 != null && var7.equals(var2)) {
                  return;
               }
            }
         }

         Loggable var9 = SNMPLogger.logInvalidAttributeNameLoggable(var1, var2);
         throw new IllegalArgumentException(var9.getMessageBody());
      }
   }

   public static void validateAttributeChangeMBean(SNMPAttributeChangeMBean var0) {
      String var1 = var0.getAttributeMBeanType();
      String var2 = var0.getAttributeName();
      BeanInfo var3 = getBeanInfo(var1);
      if (var3 == null) {
         Loggable var8 = SNMPLogger.logInvalidTypeNameLoggable(var1);
         throw new IllegalArgumentException(var8.getMessageBody());
      } else {
         PropertyDescriptor[] var4 = var3.getPropertyDescriptors();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               PropertyDescriptor var6 = var4[var5];
               String var7 = var6.getName();
               if (var7 != null && var7.equals(var2)) {
                  return;
               }
            }
         }

         Loggable var9 = SNMPLogger.logInvalidAttributeNameLoggable(var1, var2);
         throw new IllegalArgumentException(var9.getMessageBody());
      }
   }

   private static BeanInfo getBeanInfo(String var0) {
      if (beanInfoAccess == null) {
         beanInfoAccess = Kernel.isServer() ? ManagementService.getBeanInfoAccess() : ManagementServiceClient.getBeanInfoAccess();
      }

      String var1 = VersionInfo.theOne().getReleaseVersion();
      BeanInfo var2 = beanInfoAccess.getBeanInfoForInterface(var0, true, var1);
      String var3;
      if (var2 == null && var0.indexOf(".") == -1) {
         var3 = "weblogic.management.configuration." + var0 + "MBean";
         var2 = beanInfoAccess.getBeanInfoForInterface(var3, true, var1);
      }

      if (var2 == null && var0.indexOf(".") == -1) {
         var3 = "weblogic.management.runtime." + var0 + "MBean";
         var2 = beanInfoAccess.getBeanInfoForInterface(var3, true, var1);
      }

      Boolean var5 = (Boolean)var2.getBeanDescriptor().getValue("exclude");
      if (var5 != null && var5) {
         return null;
      } else {
         String var4 = (String)var2.getBeanDescriptor().getValue("obsolete");
         return var4 != null && var4.length() > 0 ? null : var2;
      }
   }

   public static int getSecurityLevel(SNMPAgentMBean var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         String var2 = var0.getAuthenticationProtocol();
         String var3 = var0.getPrivacyProtocol();
         if (!var2.equals("noAuth")) {
            var1 |= 1;
         }

         if (!var3.equals("noPriv")) {
            var1 |= 2;
         }

         return var1;
      }
   }
}
