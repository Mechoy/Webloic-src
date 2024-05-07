package weblogic.management.internal;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import javax.management.ObjectName;
import weblogic.common.internal.VersionInfo;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.ManagementLogger;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.WebLogicObjectName;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AdminResource;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.MBeanResource;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RoleManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.MBeanResource.ActionType;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.Debug;

public class SecurityHelper {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   private static final String ADMIN_ROLENAME = "Admin";
   private static final String DEPLOYER_ROLENAME = "Deployer";
   private static final String OPERATOR_ROLENAME = "Operator";
   private static final String MONITOR_ROLENAME = "Monitor";
   private static final boolean ENABLE_ACL_EXCEPTION = true;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean disableACLOnMbeans = Boolean.getBoolean("weblogic.disableMBeanAuthorization");
   private static boolean debugACLs = Boolean.getBoolean("DEBUG_ACLS");
   private static PrintStream aclPrintStream = null;
   private static boolean isSecServiceInitialized;
   private static AdminResource adminMBeanResource = new AdminResource("Configuration", (String)null, (String)null);
   private static BeanInfoAccess beanInfoAccess;
   private static RoleManager roleManager;

   public static void checkForAdminRole() {
      checkForRole("Admin");
   }

   public static void checkForDeployerRole() {
      checkForRole("Deployer");
   }

   public static void checkForOperatorRole() {
      checkForRole("Operator");
   }

   public static boolean isProtectedAttribute(ObjectName var0, String var1, PropertyDescriptor var2) {
      if (var2 != null) {
         Boolean var3 = (Boolean)var2.getValue("encrypted");
         if (var3 != null && var3) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("SecurityHelper - attribute " + var1 + " for object " + var0 + " is protected");
            }

            return true;
         }

         Boolean var4 = (Boolean)var2.getValue("sensitive");
         if (var4 != null && var4) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("SecurityHelper - attribute " + var1 + " for object " + var0 + " is protected");
            }

            return true;
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SecurityHelper - attribute " + var1 + " for object " + var0 + " is NOT protected");
      }

      return false;
   }

   public static void isAccessAllowed(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3) throws NoAccessRuntimeException {
      isAccessAllowed(var0, var1, var2, var3, (BeanDescriptor)null, (MethodDescriptor)null, (PropertyDescriptor)null);
   }

   public static void isAccessAllowed(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4) throws NoAccessRuntimeException {
      isAccessAllowed(var0, var1, var2, var3, var4, (MethodDescriptor)null, (PropertyDescriptor)null);
   }

   public static void isAccessAllowed(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4, PropertyDescriptor var5) throws NoAccessRuntimeException {
      isAccessAllowed(var0, var1, var2, var3, var4, (MethodDescriptor)null, var5);
   }

   public static void isAccessAllowed(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4, MethodDescriptor var5) throws NoAccessRuntimeException {
      isAccessAllowed(var0, var1, var2, var3, var4, var5, (PropertyDescriptor)null);
   }

   private static boolean isAllowed(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, PropertyDescriptor var4) {
      try {
         isAccessAllowed(var0, var1, var2, var3, (BeanDescriptor)null, (MethodDescriptor)null, var4);
         return true;
      } catch (NoAccessRuntimeException var6) {
         return false;
      }
   }

   public static boolean isAllowedAnon(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, PropertyDescriptor var4) {
      return isAllowed(AuthenticatedSubject.ANON, var0, var1, var2, var3, var4);
   }

   public static boolean isAllowed(AuthenticatedSubject var0, final ObjectName var1, final MBeanResource.ActionType var2, final String var3, final String var4, final PropertyDescriptor var5) {
      Boolean var6 = (Boolean)SecurityServiceManager.runAs(KERNEL_ID, var0, new PrivilegedAction() {
         public Object run() {
            return new Boolean(SecurityHelper.isAllowed(var1, var2, var3, var4, var5));
         }
      });
      return var6;
   }

   private static void isAccessAllowed(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4, MethodDescriptor var5, PropertyDescriptor var6) throws NoAccessRuntimeException {
      if (!disableACLOnMbeans) {
         if (var1 != ActionType.FIND) {
            if (var0 == null) {
               checkForAdminRole();
            } else {
               if (var1 == ActionType.READ) {
                  if (var6 == null) {
                     return;
                  }

                  Boolean var7 = (Boolean)var6.getValue("encrypted");
                  Boolean var8 = (Boolean)var6.getValue("sensitive");
                  if ((var7 == null || !var7) && (var8 == null || !var8)) {
                     return;
                  }
               }

               AuthenticatedSubject var9 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
               if (!SecurityServiceManager.isKernelIdentity(var9)) {
                  IsAccessAllowedPrivilegeAction var10 = new IsAccessAllowedPrivilegeAction(var9, var0, var1, var2, var4, var5, var6);
                  SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, var10);
               }

            }
         }
      }
   }

   public static void isAccessAllowedCommo(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4) throws NoAccessRuntimeException {
      isAccessAllowedCommo(var0, var1, var2, var3, var4, (MethodDescriptor)null, (PropertyDescriptor)null);
   }

   public static void isAccessAllowedCommo(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4, PropertyDescriptor var5) throws NoAccessRuntimeException {
      isAccessAllowedCommo(var0, var1, var2, var3, var4, (MethodDescriptor)null, var5);
   }

   public static void isAccessAllowedCommo(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4, MethodDescriptor var5) throws NoAccessRuntimeException {
      isAccessAllowedCommo(var0, var1, var2, var3, var4, var5, (PropertyDescriptor)null);
   }

   private static void isAccessAllowedCommo(ObjectName var0, MBeanResource.ActionType var1, String var2, String var3, BeanDescriptor var4, MethodDescriptor var5, PropertyDescriptor var6) throws NoAccessRuntimeException {
      if (!disableACLOnMbeans) {
         if (var1 != ActionType.FIND) {
            if (var1 == ActionType.WRITE) {
               checkForAdminRole();
            } else if (var1 == ActionType.UNREGISTER) {
               checkForAdminRole();
            } else if (var1 != ActionType.REGISTER) {
               if (var0 == null) {
                  throw new IllegalArgumentException("Object name for an MBean can not be null");
               } else {
                  AuthenticatedSubject var7 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
                  if (!SecurityServiceManager.isKernelIdentity(var7)) {
                     Boolean var9;
                     if (var1 == ActionType.READ) {
                        if (var6 != null) {
                           Boolean var11 = (Boolean)var6.getValue("encrypted");
                           if (var11 != null && var11) {
                              if (debugLogger.isDebugEnabled()) {
                                 debugLogger.debug("SecurityHelper - read encrypted, check for admin, attr = " + var2);
                              }

                              checkForAdminRole();
                           }

                           var9 = (Boolean)var6.getValue("sensitive");
                           if (var9 != null && var9) {
                              if (debugLogger.isDebugEnabled()) {
                                 debugLogger.debug("SecurityHelper - read encrypted, check for admin, attr = " + var2);
                              }

                              checkForAdminRole();
                           }

                        }
                     } else if (var1 == ActionType.EXECUTE) {
                        String[] var10;
                        if (var4 != null) {
                           var10 = (String[])((String[])var4.getValue("rolesAllowed"));
                           if (checkForRoles(var10)) {
                              return;
                           }

                           var9 = (Boolean)var4.getValue("rolePermitAll");
                           if (var9 != null && var9) {
                              if (debugLogger.isDebugEnabled()) {
                                 debugLogger.debug("SecurityHelper - rolePermitAll found for interface " + var2);
                              }

                              return;
                           }
                        }

                        if (var5 != null) {
                           var10 = (String[])((String[])var5.getValue("rolesAllowed"));
                           if (checkForRoles(var10)) {
                              return;
                           }

                           var9 = (Boolean)var5.getValue("rolePermitAll");
                           if (var9 != null && var9) {
                              if (debugLogger.isDebugEnabled()) {
                                 debugLogger.debug("SecurityHelper - rolePermitAll found for method " + var2);
                              }

                              return;
                           }
                        }

                        checkForAdminRole();
                     } else {
                        String var8 = "Access not allowed for subject: " + var7 + ", on Resource" + var0.toString() + " Action: " + var1 + ", Target: " + var2;
                        throw new NoAccessRuntimeException(var8);
                     }
                  }
               }
            }
         }
      }
   }

   public static void assertIfNotKernel(AuthenticatedSubject var0) {
      if (var0 != KERNEL_ID) {
         throw new AssertionError("The internal method that you have invoked is not available unless you are running as Kernel!\nYou are running as: " + var0);
      }
   }

   public static void assertIfNotKernel() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (!SecurityServiceManager.isKernelIdentity(var0)) {
         throw new AssertionError("The internal method that you have invoked is not available unless you are running as Kernel!\nYou are running as: " + var0);
      }
   }

   private static boolean isInRole(Map var0, String var1) {
      return var0 != null && var0.get(var1) != null;
   }

   private static boolean isInRole(Map var0, String[] var1) {
      if (var0 != null && var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var0.get(var1[var2]) != null) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("SecurityHelper - in role is true, role " + var1[var2]);
               }

               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static RoleManager getRoleManager() {
      return roleManager != null ? roleManager : (roleManager = (RoleManager)SecurityServiceManager.getSecurityService(KERNEL_ID, "weblogicDEFAULT", ServiceType.ROLE));
   }

   private static void checkForRole(final String var0) {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (!SecurityServiceManager.isKernelIdentity(var1)) {
         final AuthenticatedSubject var2 = SecurityServiceManager.seal(KERNEL_ID, var1);
         Boolean var3 = (Boolean)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedAction() {
            public Object run() {
               Map var1 = SecurityHelper.getRoleManager().getRoles(var2, SecurityHelper.adminMBeanResource, (ContextHandler)null);
               return var1 == null || var1.get("Admin") == null && var1.get(var0) == null ? Boolean.FALSE : Boolean.TRUE;
            }
         });
         if (!var3) {
            Loggable var4 = ManagementLogger.logNoAccessForSubjectRoleLoggable(var2.toString(), var0);
            throw new NoAccessRuntimeException(var4.getMessage());
         }
      }
   }

   private static boolean checkForRoles(final String[] var0) {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      final AuthenticatedSubject var2 = SecurityServiceManager.seal(KERNEL_ID, var1);
      Boolean var3 = (Boolean)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedAction() {
         public Object run() {
            Map var1 = SecurityHelper.getRoleManager().getRoles(var2, SecurityHelper.adminMBeanResource, (ContextHandler)null);
            if (var1 != null && var0 != null) {
               for(int var2x = 0; var2x < var0.length; ++var2x) {
                  if (var1.get(var0[var2x]) != null) {
                     if (SecurityHelper.debugLogger.isDebugEnabled()) {
                        SecurityHelper.debugLogger.debug("SecurityHelper - role found " + var0[var2x]);
                     }

                     return Boolean.TRUE;
                  }
               }

               if (SecurityHelper.debugLogger.isDebugEnabled()) {
                  SecurityHelper.debugLogger.debug("SecurityHelper - role not found ");
               }

               return Boolean.FALSE;
            } else {
               return Boolean.FALSE;
            }
         }
      });
      return var3;
   }

   private static synchronized void dumpAclDebug(AuthenticatedSubject var0, ObjectName var1, MBeanResource.ActionType var2, String var3, String var4) {
      try {
         if (aclPrintStream == null) {
            String var5 = ManagementService.getRuntimeAccess(KERNEL_ID).getServerName() + "_debug_acls.txt";
            Debug.say("Opening ACL Log" + var5);
            File var6 = new File(var5);
            FileOutputStream var7 = new FileOutputStream(var6);
            aclPrintStream = new PrintStream(var7);
         }

         aclPrintStream.println("START: INVALID MBEAN ACCESS");
         aclPrintStream.println("PRINCIPALS:" + var0.getPrincipals());
         aclPrintStream.println("RESOURCE:" + var1 + "|" + var2 + "|" + var3 + "|" + var4);
         Exception var9 = new Exception();
         var9.printStackTrace(aclPrintStream);
         aclPrintStream.println("END:INVALID MBEAN ACCESS");
      } catch (FileNotFoundException var8) {
         Debug.say("**** UNABLE TO OPEN DEBUG FILE *****");
      }

   }

   private static BeanInfo getBeanInfo(String var0) {
      if (beanInfoAccess == null) {
         beanInfoAccess = ManagementService.getBeanInfoAccess();
      }

      String var1 = VersionInfo.theOne().getReleaseVersion();
      BeanInfo var2 = beanInfoAccess.getBeanInfoForInterface(var0, false, var1);
      String var3;
      if (var2 == null && var0.indexOf(".") == -1) {
         var3 = "weblogic.management.configuration." + var0 + "MBean";
         var2 = beanInfoAccess.getBeanInfoForInterface(var3, false, var1);
      }

      if (var2 == null && var0.indexOf(".") == -1) {
         var3 = "weblogic.management.runtime." + var0 + "MBean";
         var2 = beanInfoAccess.getBeanInfoForInterface(var3, true, var1);
      }

      return var2;
   }

   public static BeanDescriptor getBeanDescriptor(String var0) {
      BeanInfo var1 = getBeanInfo(var0);
      return var1 != null ? var1.getBeanDescriptor() : null;
   }

   public static PropertyDescriptor getPropertyDescriptor(String var0, String var1) {
      BeanInfo var2 = getBeanInfo(var0);
      if (var2 == null) {
         return null;
      } else {
         PropertyDescriptor[] var3 = var2.getPropertyDescriptors();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            PropertyDescriptor var5 = var3[var4];
            String var6 = var5.getName();
            if (var1.equals(var6)) {
               return var5;
            }
         }

         return null;
      }
   }

   public static MethodDescriptor getMethodDescriptor(String var0, String var1) {
      BeanInfo var2 = getBeanInfo(var0);
      if (var2 == null) {
         return null;
      } else {
         MethodDescriptor[] var3 = var2.getMethodDescriptors();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            MethodDescriptor var5 = var3[var4];
            String var6 = var5.getName();
            if (var1.equals(var6)) {
               return var5;
            }
         }

         return null;
      }
   }

   private static class IsAccessAllowedPrivilegeAction implements PrivilegedAction {
      private final AuthenticatedSubject subject;
      private final ObjectName name;
      private final MBeanResource.ActionType action;
      private final String target;
      private final String type;
      private final BeanDescriptor beanDescriptor;
      private final MethodDescriptor methodDescriptor;
      private final PropertyDescriptor propertyDescriptor;

      IsAccessAllowedPrivilegeAction(AuthenticatedSubject var1, ObjectName var2, MBeanResource.ActionType var3, String var4, BeanDescriptor var5, MethodDescriptor var6, PropertyDescriptor var7) {
         this.subject = var1;
         this.name = var2;
         this.action = var3;
         this.target = var4;
         this.type = this.name.getKeyProperty("Type");
         this.beanDescriptor = var5;
         this.methodDescriptor = var6;
         this.propertyDescriptor = var7;
      }

      public Object run() {
         Map var1 = SecurityHelper.getRoleManager().getRoles(this.subject, SecurityHelper.adminMBeanResource, (ContextHandler)null);
         if (SecurityHelper.isInRole(var1, "Admin")) {
            return Boolean.TRUE;
         } else if (this.name instanceof WebLogicObjectName) {
            return this.wlsRun(var1);
         } else {
            return this.type == null ? Boolean.TRUE : this.wlsRun(var1);
         }
      }

      private Object wlsRun(Map var1) {
         String var2 = "Access not allowed for subject: " + this.subject + ", on Resource" + "Type: " + (this.type != null ? this.type : this.name.toString()) + " Action: " + this.action + ", Target: " + this.target;
         Boolean var5;
         String[] var8;
         if (this.action != ActionType.READ) {
            if (this.action != ActionType.WRITE && this.action != ActionType.EXECUTE && this.action != ActionType.REGISTER && this.action != ActionType.UNREGISTER) {
               NoAccessRuntimeException var9 = new NoAccessRuntimeException("Uknown ActionType: " + this.action + " found");
               throw var9;
            } else {
               BeanDescriptor var7 = this.beanDescriptor;
               if (var7 == null) {
                  var7 = SecurityHelper.getBeanDescriptor(this.type);
               }

               if (var7 != null) {
                  var8 = (String[])((String[])var7.getValue("rolesAllowed"));
                  if (SecurityHelper.isInRole(var1, var8)) {
                     return Boolean.TRUE;
                  }

                  var5 = (Boolean)var7.getValue("rolePermitAll");
                  if (var5 != null && var5) {
                     if (SecurityHelper.debugLogger.isDebugEnabled()) {
                        SecurityHelper.debugLogger.debug("SecurityHelper - rolePermitAll found for interface ");
                     }

                     return Boolean.TRUE;
                  }
               }

               Boolean var6;
               String[] var12;
               if (this.action == ActionType.WRITE) {
                  PropertyDescriptor var11 = this.propertyDescriptor;
                  if (var11 == null) {
                     var11 = SecurityHelper.getPropertyDescriptor(this.type, this.target);
                  }

                  if (var11 != null) {
                     var12 = (String[])((String[])var11.getValue("rolesAllowedSet"));
                     if (SecurityHelper.isInRole(var1, var12)) {
                        return Boolean.TRUE;
                     }

                     var6 = (Boolean)var11.getValue("rolePermitAllSet");
                     if (var6 != null && var6) {
                        if (SecurityHelper.debugLogger.isDebugEnabled()) {
                           SecurityHelper.debugLogger.debug("SecurityHelper - rolePermitAllSet found for set " + this.target);
                        }

                        return Boolean.TRUE;
                     }
                  }

                  throw new NoAccessRuntimeException(var2);
               } else {
                  if (this.action == ActionType.EXECUTE) {
                     MethodDescriptor var10 = this.methodDescriptor;
                     if (var10 == null) {
                        var10 = SecurityHelper.getMethodDescriptor(this.type, this.target);
                     }

                     if (var10 != null) {
                        var12 = (String[])((String[])var10.getValue("rolesAllowed"));
                        if (SecurityHelper.isInRole(var1, var12)) {
                           return Boolean.TRUE;
                        }

                        var6 = (Boolean)var10.getValue("rolePermitAll");
                        if (var6 != null && var6) {
                           if (SecurityHelper.debugLogger.isDebugEnabled()) {
                              SecurityHelper.debugLogger.debug("SecurityHelper - rolePermitAll found for invoke " + this.target);
                           }

                           return Boolean.TRUE;
                        }
                     }
                  }

                  if (SecurityHelper.debugACLs) {
                     SecurityHelper.dumpAclDebug(this.subject, this.name, this.action, this.target, "");
                  }

                  throw new NoAccessRuntimeException(var2);
               }
            }
         } else {
            PropertyDescriptor var3 = this.propertyDescriptor;
            if (var3 == null) {
               var3 = SecurityHelper.getPropertyDescriptor(this.type, this.target);
            }

            if (var3 == null) {
               throw new NoAccessRuntimeException(var2);
            } else {
               Boolean var4 = (Boolean)var3.getValue("encrypted");
               var5 = (Boolean)var3.getValue("sensitive");
               if ((var4 == null || !var4) && (var5 == null || !var5)) {
                  if (SecurityHelper.debugLogger.isDebugEnabled()) {
                     SecurityHelper.debugLogger.debug("SecurityHelper - read non-encrypted attr, attr = " + this.target);
                  }

                  return Boolean.TRUE;
               } else {
                  var8 = (String[])((String[])var3.getValue("rolesAllowedGet"));
                  if (SecurityHelper.isInRole(var1, var8)) {
                     return Boolean.TRUE;
                  } else {
                     var5 = (Boolean)var3.getValue("rolePermitAllGet");
                     if (var5 != null && var5) {
                        if (SecurityHelper.debugLogger.isDebugEnabled()) {
                           SecurityHelper.debugLogger.debug("SecurityHelper - rolePermitAll found for get " + this.target);
                        }

                        return Boolean.TRUE;
                     } else {
                        throw new NoAccessRuntimeException(var2);
                     }
                  }
               }
            }
         }
      }
   }
}
