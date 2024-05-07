package weblogic.application.internal.flow;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.SecurityRole;
import weblogic.application.internal.Flow;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.wl.ApplicationSecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.SecurityBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ApplicationResource;
import weblogic.security.service.DeployHandleCreationException;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.ResourceBase;
import weblogic.security.service.RoleCreationException;
import weblogic.security.service.RoleManager;
import weblogic.security.service.RoleManagerDeployHandle;
import weblogic.security.service.RoleRemovalException;
import weblogic.security.service.SecurityApplicationInfo;
import weblogic.security.service.SecurityApplicationInfoImpl;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.spi.ApplicationInfo.ComponentType;

public final class SecurityRoleFlow extends BaseFlow implements Flow {
   private static final boolean DEBUG = false;
   private RoleManagerDeployHandle handle;
   private RoleManager roleManager = null;
   private static AuthenticatedSubject kernelId = null;
   private SecurityApplicationInfo secInfo = null;
   private String realmName = null;
   private static final SecurityRole NOOP_MAPPING = new SecurityRole((String[])null);

   public SecurityRoleFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      if (!this.appCtx.useJACC()) {
         this.realmName = this.getDefaultRealmName();
         this.appCtx.setApplicationSecurityRealmName(this.realmName);
         if (this.roleManager == null) {
            this.initSecurityService();
         }

         this.secInfo = new SecurityApplicationInfoImpl(this.appCtx.getAppDeploymentMBean(), ComponentType.APPLICATION, this.appCtx.getApplicationName());

         try {
            this.handle = this.roleManager.startDeployRoles(this.secInfo);

            try {
               this.deployRoles();
            } finally {
               this.roleManager.endDeployRoles(this.handle);
            }

         } catch (DeployHandleCreationException var6) {
            throw new DeploymentException(var6);
         } catch (RoleCreationException var7) {
            throw new DeploymentException(var7);
         }
      }
   }

   public void unprepare() throws DeploymentException {
      if (!this.appCtx.useJACC()) {
         this.undeployRoles();
      }
   }

   private void initSecurityService() {
      this.roleManager = (RoleManager)SecurityServiceManager.getSecurityService(this.getKernelID(), this.realmName, ServiceType.ROLE);
   }

   private String[] getSecurityRoleNames() {
      String[] var1 = null;
      ApplicationBean var2 = this.appCtx.getApplicationDD();
      SecurityRoleBean[] var3 = var2 != null ? var2.getSecurityRoles() : null;
      if (var3 != null && var3.length != 0) {
         var1 = new String[var3.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1[var4] = var3[var4].getRoleName();
         }
      }

      return var1;
   }

   private Map getSecurityRoleAssignments() {
      HashMap var1 = new HashMap();
      WeblogicApplicationBean var2 = this.appCtx.getWLApplicationDD();
      if (var2 == null) {
         return var1;
      } else {
         SecurityBean var3 = var2.getSecurity();
         if (var3 == null) {
            return var1;
         } else {
            ApplicationSecurityRoleAssignmentBean[] var4 = var3.getSecurityRoleAssignments();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  String var6 = var4[var5].getRoleName();
                  String[] var7 = var4[var5].getPrincipalNames();
                  if (var4[var5].getExternallyDefined() != null) {
                     var1.put(var6, new SecurityRole());
                  } else {
                     var1.put(var6, new SecurityRole(var7));
                  }
               }
            }

            return var1;
         }
      }
   }

   private AuthenticatedSubject getKernelID() {
      if (kernelId == null) {
         kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      }

      return kernelId;
   }

   private String getDefaultRealmName() {
      String var1 = "weblogicDEFAULT";
      WeblogicApplicationBean var2 = this.appCtx.getWLApplicationDD();
      if (var2 != null) {
         SecurityBean var3 = var2.getSecurity();
         if (var3 != null) {
            String var4 = var3.getRealmName();
            if (var4 != null) {
               var1 = var4;
            }
         }
      }

      return var1;
   }

   private boolean isCompatibilitySecMode() {
      return SecurityServiceManager.getRoleMappingBehavior(this.realmName, this.secInfo) == 0;
   }

   private boolean isApplicationSecMode() {
      return SecurityServiceManager.getRoleMappingBehavior(this.realmName, this.secInfo) == 1;
   }

   private boolean isExternallyDefinedSecMode() {
      return SecurityServiceManager.getRoleMappingBehavior(this.realmName, this.secInfo) == 2;
   }

   private void deployRoles() throws DeploymentException {
      String[] var1 = this.getSecurityRoleNames();
      Map var2 = this.getSecurityRoleAssignments();
      if (var1 == null && var2 != null) {
         var1 = (String[])((String[])var2.keySet().toArray(new String[var2.size()]));
      }

      if (var1 != null) {
         if (!this.isCompatibilitySecMode()) {
            this.appCtx.setAppLevelRoleMappings(var2);
         }

         ApplicationResource var3 = new ApplicationResource(this.appCtx.getApplicationId());
         if (this.isCompatibilitySecMode()) {
            this.deployRolesAllowEmptyRoleMapping(var3, var1, var2);
         } else if (this.isApplicationSecMode()) {
            this.deployRolesAllowEmptyRoleMapping(var3, var1, var2);
         } else {
            if (!this.isExternallyDefinedSecMode()) {
               throw new AssertionError("Unknown security mode");
            }

            this.deployRolesNoEmptyRoleMapping(var3, var1, var2);
         }

      }
   }

   private void deployRolesAllowEmptyRoleMapping(ResourceBase var1, String[] var2, Map var3) throws DeploymentException {
      this.deployRoles(var1, var2, var3, true);
   }

   private void deployRolesNoEmptyRoleMapping(ResourceBase var1, String[] var2, Map var3) throws DeploymentException {
      this.deployRoles(var1, var2, var3, false);
   }

   private void deployRoles(ResourceBase var1, String[] var2, Map var3, boolean var4) throws DeploymentException {
      for(int var5 = 0; var5 < var2.length; ++var5) {
         if (var2[var5] != null) {
            SecurityRole var6 = (SecurityRole)var3.get(var2[var5]);
            if (var6 == null) {
               var6 = NOOP_MAPPING;
            }

            if (!var6.isExternallyDefined()) {
               String[] var7 = var6.getPrincipalNames();
               if (var7 == null) {
                  if (!var4) {
                     continue;
                  }

                  var7 = new String[0];
               }

               this.deployRole(var1, var2[var5], var7);
            }
         }
      }

   }

   private void deployRole(ResourceBase var1, String var2, String[] var3) throws DeploymentException {
      try {
         this.roleManager.deployRole(this.handle, var1, var2, var3);
      } catch (RoleCreationException var6) {
         Loggable var5 = J2EELogger.logCouldNotDeployRoleLoggable(var2, ApplicationVersionUtils.getDisplayName(this.appCtx.getApplicationId()), var6);
         var5.log();
         throw new DeploymentException(var5.getMessage());
      }
   }

   private void undeployRoles() throws DeploymentException {
      try {
         this.roleManager.undeployAllRoles(this.handle);
      } catch (RoleRemovalException var2) {
         throw new DeploymentException(var2);
      }
   }

   private static class SecurityRoleAssignmentUpdateListener implements BeanUpdateListener {
      private final ApplicationContextInternal appCtx;
      private final SecurityApplicationInfo secInfo;
      private final RoleManager roleManager;

      private SecurityRoleAssignmentUpdateListener(ApplicationContextInternal var1, SecurityApplicationInfo var2, RoleManager var3) {
         this.appCtx = var1;
         this.secInfo = var2;
         this.roleManager = var3;
      }

      static void registerListeners(ApplicationContextInternal var0, SecurityApplicationInfo var1, RoleManager var2) {
         WeblogicApplicationBean var3 = var0.getWLApplicationDD();
         if (var3 != null) {
            SecurityBean var4 = var3.getSecurity();
            if (var4 != null) {
               ApplicationSecurityRoleAssignmentBean[] var5 = var4.getSecurityRoleAssignments();
               if (var5 != null && var5.length != 0) {
                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     ((DescriptorBean)var5[var6]).addBeanUpdateListener(new SecurityRoleAssignmentUpdateListener(var0, var1, var2));
                  }

               }
            }
         }
      }

      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      }

      public void activateUpdate(BeanUpdateEvent var1) {
         ApplicationSecurityRoleAssignmentBean var2 = (ApplicationSecurityRoleAssignmentBean)var1.getProposedBean();

         try {
            RoleManagerDeployHandle var3 = this.roleManager.startDeployRoles(this.secInfo);

            try {
               ApplicationResource var10 = new ApplicationResource(this.appCtx.getApplicationId());
               this.roleManager.deployRole(var3, var10, var2.getRoleName(), var2.getPrincipalNames());
            } finally {
               this.roleManager.endDeployRoles(var3);
            }
         } catch (Exception var9) {
            Loggable var4 = J2EELogger.logCouldNotDeployRoleLoggable(var2.getRoleName(), ApplicationVersionUtils.getDisplayName(this.appCtx.getApplicationId()), var9);
            var4.log();
         }

      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
      }
   }
}
