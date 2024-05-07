package weblogic.diagnostics.snmp.agent.monfox;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;
import javax.management.ObjectName;
import monfox.toolkit.snmp.SnmpOid;
import monfox.toolkit.snmp.agent.SnmpAgent;
import monfox.toolkit.snmp.agent.SnmpMib;
import monfox.toolkit.snmp.agent.SnmpMibLeaf;
import monfox.toolkit.snmp.agent.SnmpMibNode;
import monfox.toolkit.snmp.agent.SnmpMibTableRow;
import monfox.toolkit.snmp.agent.ext.acm.AppAcm;
import monfox.toolkit.snmp.engine.SnmpEngineID;
import monfox.toolkit.snmp.v3.usm.ext.UsmUserSecurityExtension;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.mbeanservers.SecurityUtil;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public class WLSAccessController implements AppAcm.AccessController {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final WLSAccessController SINGLETON = new WLSAccessController();
   private String community = "";
   private boolean communityBasedAccessEnabled = true;
   private SnmpEngineID snmpEngineId;
   private Set validContextNames = new HashSet();
   private int accessFailureCount;

   public static WLSAccessController getInstance() {
      return SINGLETON;
   }

   private WLSAccessController() {
      if (ManagementService.getRuntimeAccess(KERNEL_ID).isAdminServer()) {
         ServerMBean[] var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().getServers();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.validContextNames.add(var1[var2].getName());
         }

         this.validContextNames.add(ManagementService.getRuntimeAccess(KERNEL_ID).getDomainName());
      }

   }

   public boolean checkAccess(SnmpAgent var1, String var2, int var3, int var4, boolean var5, String var6) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLSAccessController: [" + var2 + "," + var3 + "," + var4 + "," + var5 + "," + var6 + "]");
      }

      if (!this.isAccessAllowed(var5, var3, var4, var2)) {
         ++this.accessFailureCount;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Access failed");
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean checkAccess(SnmpAgent var1, String var2, int var3, int var4, boolean var5, String var6, SnmpOid var7, String var8, SnmpOid var9) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLSAccessController: [" + var2 + "," + var3 + "," + var4 + "," + var5 + "," + var6 + "," + var7 + "," + var8 + "," + var9 + "," + "]");
      }

      if (!this.isAccessAllowed(var5, var3, var4, var2)) {
         ++this.accessFailureCount;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Access failed");
         }

         return false;
      } else if (var3 <= 2 && this.communityBasedAccessEnabled) {
         return true;
      } else {
         SnmpMib var10 = var1.getMib();
         SnmpMibNode var11 = var10.get(var9);
         if (var11 != null && var11 instanceof SnmpMibLeaf) {
            SnmpMibLeaf var12 = (SnmpMibLeaf)var11;
            SnmpMibTableRow var13 = var12.getRow();
            if (var13 != null) {
               Object var14 = var13.getUserObject();
               if (var14 instanceof MBeanInstanceTableRow) {
                  MBeanInstanceTableRow var15 = (MBeanInstanceTableRow)var14;

                  boolean var16;
                  try {
                     String var17 = var15.getAttributeName(var8);
                     if (DEBUG_LOGGER.isDebugEnabled()) {
                        DEBUG_LOGGER.debug("Checking access for attribute " + var17);
                     }

                     if (var17 == null) {
                        var16 = false;
                     } else if (!var17.equals("Index") && !var17.equals("ObjectName")) {
                        var16 = this.isMBeanAccessAllowed(var2, var15.getObjectName(), var17);
                     } else {
                        var16 = true;
                     }
                  } catch (Exception var18) {
                     if (DEBUG_LOGGER.isDebugEnabled()) {
                        DEBUG_LOGGER.debug("Exception checking MBean access", var18);
                     }

                     var16 = false;
                  }

                  if (!var16) {
                     ++this.accessFailureCount;
                  }

                  if (DEBUG_LOGGER.isDebugEnabled()) {
                     DEBUG_LOGGER.debug("MBean access check = " + var16);
                  }

                  return var16;
               }
            }
         }

         return true;
      }
   }

   private boolean isMBeanAccessAllowed(String var1, final ObjectName var2, final String var3) throws Exception {
      PrincipalAuthenticator var4 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, SecurityServiceManager.getDefaultRealmName(), ServiceType.AUTHENTICATION);
      AuthenticatedSubject var5 = var4.impersonateIdentity(var1, (ContextHandler)null);
      Object var6 = SecurityServiceManager.runAs(KERNEL_ID, var5, new PrivilegedAction() {
         public Object run() {
            try {
               boolean var1 = SecurityUtil.isGetAccessAllowed(2, var2, var3);
               return new Boolean(var1);
            } catch (Exception var2x) {
               return Boolean.FALSE;
            }
         }
      });
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Returned object is " + var6 + " for user_name " + var1 + " object_name " + var2 + "attribute_name " + var3);
      }

      if (var6 instanceof Boolean) {
         Boolean var7 = (Boolean)var6;
         return var7;
      } else {
         return false;
      }
   }

   private boolean isAccessAllowed(boolean var1, int var2, int var3, String var4) {
      if (var1) {
         return false;
      } else {
         if (var2 == 3) {
            UsmUserSecurityExtension.UserInfo var5 = WLSSecurityExtension.getInstance().getUserInfo(var4, this.snmpEngineId);
            if (var5 == null) {
               return false;
            }

            boolean var6 = this.isAuthSecurityLevel(var5.getSecLevel());
            boolean var7 = this.isAuthSecurityLevel(var3);
            if (var6 && !var7) {
               return false;
            }
         }

         if (var2 <= 2 && !this.communityBasedAccessEnabled) {
            return false;
         } else {
            String var9 = var4;
            int var10 = var4.indexOf("@");
            if (var10 > 0) {
               var9 = var4.substring(0, var10);
               String var11 = var10 < var4.length() - 1 ? var4.substring(var10 + 1) : "";
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Context Name = " + var11);
               }

               var11 = var11 == null ? "" : var11;
               if (!this.validContextNames.contains(var11)) {
                  String var8 = "Invalid Context Name " + var11;
                  if (DEBUG_LOGGER.isDebugEnabled()) {
                     DEBUG_LOGGER.debug(var8);
                  }

                  throw new IllegalArgumentException(var8);
               }
            }

            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Input community = " + var9);
            }

            return var2 > 2 || var9.equals(this.community);
         }
      }
   }

   private boolean isAuthSecurityLevel(int var1) {
      switch (var1) {
         case 1:
         case 3:
            return true;
         default:
            return false;
      }
   }

   String getCommunity() {
      return this.community;
   }

   void setCommunity(String var1) {
      this.community = var1;
   }

   boolean isCommunityBasedAccessEnabled() {
      return this.communityBasedAccessEnabled;
   }

   void setCommunityBasedAccessEnabled(boolean var1) {
      this.communityBasedAccessEnabled = var1;
   }

   void setSnmpEngineId(SnmpEngineID var1) {
      this.snmpEngineId = var1;
   }

   public int getFailedAuthorizationCount() {
      return this.accessFailureCount;
   }
}
