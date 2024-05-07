package weblogic.ejb.container.deployer;

import java.security.AccessController;
import javax.naming.NamingException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class DynamicEJBModule extends EJBModule implements weblogic.ejb.spi.DynamicEJBModule {
   private static final DebugLogger debugLogger;
   private EjbDescriptorBean ejbDescriptor;
   private String ejbName;
   private static final int STATE_NEW = 1;
   private static final int STATE_INITIALIZED = 2;
   private static final int STATE_PREPARED = 4;
   private static final int STATE_ADMIN = 8;
   private static final int STATE_ACTIVE = 16;
   private int state = 1;
   private static final AuthenticatedSubject KERNEL_ID;

   public DynamicEJBModule(String var1) {
      super(var1 + "_dynamic_internal");
   }

   public DynamicEJBModule(String var1, ApplicationContextInternal var2) {
      super(var1 + "_dynamic_internal");
      this.appCtx = var2;
   }

   private static void debug(String var0) {
      debugLogger.debug("[DynamicEJBModule] " + var0);
   }

   private void pushRunAsSubject(AuthenticatedSubject var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("pushRunAsSubject to push: '" + var1.toString() + "', currentSubject is: '" + this.getCurrentSubject() + "' ");
      }

      SecurityServiceManager.pushSubject(KERNEL_ID, var1);
   }

   private void popRunAsSubject() {
      if (debugLogger.isDebugEnabled()) {
         debug("\n popRunAsSubject,  subject before pop is: '" + this.getCurrentSubject() + "'");
      }

      SecurityServiceManager.popSubject(KERNEL_ID);
      if (debugLogger.isDebugEnabled()) {
         debug("\n popRunAsSubject,  subject after  pop is: '" + this.getCurrentSubject() + "'");
      }

   }

   private AuthenticatedSubject getCurrentSubject() {
      return SecurityServiceManager.getCurrentSubject(KERNEL_ID);
   }

   public void setEjbDescriptorBean(EjbDescriptorBean var1) {
      this.ejbDescriptor = var1;
   }

   protected EjbDescriptorBean loadEJBDescriptor() {
      return this.ejbDescriptor;
   }

   protected void setupPersistenceUnitRegistry() {
   }

   public boolean deployDynamicEJB() {
      boolean var2;
      try {
         this.pushRunAsSubject(KERNEL_ID);
         this.init();
         this.state = 2;
         this.prepare();
         this.state = 4;
         this.activate();
         return true;
      } catch (ModuleException var6) {
         debug("dynamic deployment of " + this.ejbName + " failed with exception: " + var6);
         if (debugLogger.isDebugEnabled()) {
            var6.getNestedException().printStackTrace();
         }

         this.undeployDynamicEJB();
         var2 = false;
      } finally {
         this.popRunAsSubject();
      }

      return var2;
   }

   public boolean startDynamicEJB() {
      boolean var2;
      try {
         this.pushRunAsSubject(KERNEL_ID);
         this.start();
         this.state = 8;
         this.adminToProduction();
         this.state = 16;
         return true;
      } catch (ModuleException var6) {
         debug("dynamic deployment of " + this.ejbName + " failed with exception: " + var6);
         if (debugLogger.isDebugEnabled()) {
            var6.getNestedException().printStackTrace();
         }

         this.undeployDynamicEJB();
         var2 = false;
      } finally {
         this.popRunAsSubject();
      }

      return var2;
   }

   public void undeployDynamicEJB() {
      this.pushRunAsSubject(KERNEL_ID);
      if (this.state >= 16) {
         this.forceProductionToAdmin();
      }

      if (this.state >= 8) {
         try {
            this.deactivate();
         } catch (ModuleException var3) {
            if (debugLogger.isDebugEnabled()) {
               debug("Ignoring " + var3);
            }
         }
      }

      if (this.state >= 4) {
         try {
            this.unprepare();
         } catch (ModuleException var2) {
            if (debugLogger.isDebugEnabled()) {
               debug("Ignoring " + var2);
            }
         }
      }

      this.state = 1;
      this.popRunAsSubject();
   }

   public void init() {
      if (this.appCtx == null) {
         this.appCtx = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      }

      try {
         this.appCtx.getEnvContext().createSubcontext("ejb");
      } catch (NamingException var2) {
      }

      this.classLoader = this.appCtx.getAppClassLoader();
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
