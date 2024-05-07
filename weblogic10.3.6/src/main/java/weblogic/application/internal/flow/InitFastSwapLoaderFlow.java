package weblogic.application.internal.flow;

import com.bea.wls.redef.ClassRedefInitializationException;
import com.bea.wls.redef.ClassRedefinerFactory;
import com.bea.wls.redef.RedefiningClassLoader;
import com.bea.wls.redef.runtime.ClassRedefinitionRuntimeImpl;
import java.security.AccessController;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.ApplicationContextImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.wl.FastSwapBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.classloaders.GenericClassLoader;

public class InitFastSwapLoaderFlow extends BaseFlow {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugClassRedef");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean PRODUCTION_MODE;

   public InitFastSwapLoaderFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      this.initLoader(this.appCtx.getWLApplicationDD());
   }

   public void initLoader(WeblogicApplicationBean var1) throws DeploymentException {
      if (!PRODUCTION_MODE) {
         if (var1 != null) {
            FastSwapBean var2 = var1.getFastSwap();
            if (var2 != null) {
               boolean var3 = var2.isEnabled();
               int var4 = var2.getRefreshInterval();
               if (logger.isDebugEnabled()) {
                  logger.debug(" Enabled : " + var3 + " Reload : " + var4);
               }

               if (var3) {
                  GenericClassLoader var5 = this.appCtx.getAppClassLoader();

                  try {
                     GenericClassLoader var6 = ClassRedefinerFactory.makeClassLoader(var5.getClassFinder(), var5.getParent());
                     var6.setAnnotation(var5.getAnnotation());
                     ((RedefiningClassLoader)var6).getRedefinitionRuntime().setRedefinitionTaskLimit(var2.getRedefinitionTaskLimit());
                     this.createRuntime(var6);
                     if (logger.isDebugEnabled()) {
                        logger.debug(" Appclassloader reset to " + var6);
                     }

                     ((ApplicationContextImpl)this.appCtx).resetAppClassLoader(var6);
                  } catch (ClassRedefInitializationException var7) {
                     throw new DeploymentException(var7);
                  }
               }

            }
         }
      }
   }

   private void createRuntime(GenericClassLoader var1) throws ClassRedefInitializationException {
      try {
         J2EEApplicationRuntimeMBeanImpl var3 = this.appCtx.getRuntime();
         ClassRedefinitionRuntimeImpl var2 = new ClassRedefinitionRuntimeImpl(var3, var1);
         var3.setClassRedefinitionRuntime(var2);
      } catch (ManagementException var4) {
         throw new ClassRedefInitializationException(var4.getMessage(), var4);
      } catch (ClassCastException var5) {
         throw new ClassRedefInitializationException(var5.getMessage(), var5);
      }
   }

   static {
      PRODUCTION_MODE = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().isProductionModeEnabled();
   }
}
