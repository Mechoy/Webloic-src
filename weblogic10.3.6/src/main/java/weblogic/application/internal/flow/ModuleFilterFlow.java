package weblogic.application.internal.flow;

import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.TargetUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

abstract class ModuleFilterFlow extends BaseFlow {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final RuntimeAccess config;
   private final ServerMBean server;

   ModuleFilterFlow(ApplicationContextInternal var1) {
      super(var1);
      this.config = ManagementService.getRuntimeAccess(kernelId);
      this.server = this.config.getServer();
   }

   private Map makeSubDeploymentMap(SubDeploymentMBean[] var1) {
      if (var1 != null && var1.length != 0) {
         HashMap var2 = new HashMap(var1.length);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.put(EarUtils.toModuleId(this.appCtx, var1[var3].getName()), var1[var3]);
         }

         return var2;
      } else {
         return Collections.EMPTY_MAP;
      }
   }

   private String findContextRoot(String var1) {
      ApplicationBean var2 = this.appCtx.getApplicationDD();
      if (var2 == null) {
         return null;
      } else {
         ModuleBean[] var3 = var2.getModules();
         if (var3 == null) {
            return null;
         } else {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               WebBean var5 = var3[var4].getWeb();
               if (var5 != null && var1.equals(var5.getWebUri())) {
                  return var5.getContextRoot();
               }
            }

            return null;
         }
      }
   }

   private SubDeploymentMBean findSubDeployment(Map var1, String var2) {
      if (var1.size() == 0) {
         return null;
      } else {
         SubDeploymentMBean var3 = (SubDeploymentMBean)var1.get(var2);
         if (var3 != null) {
            return var3;
         } else {
            String var4 = this.findContextRoot(var2);
            if (var4 != null) {
               var3 = (SubDeploymentMBean)var1.get(var4);
               if (var3 != null) {
                  return var3;
               }
            }

            WeblogicApplicationBean var5 = this.appCtx.getWLApplicationDD();
            if (var5 != null) {
               WeblogicModuleBean[] var6 = var5.getModules();
               if (var6 != null) {
                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     if (var2.equals(var6[var7].getPath())) {
                        return (SubDeploymentMBean)var1.get(var6[var7].getName());
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   protected Module[] createWrappedModules(List var1) {
      BasicDeploymentMBean var2 = this.appCtx.getBasicDeploymentMBean();
      TargetMBean var3 = TargetUtils.findLocalTarget(var2.getTargets(), this.server);
      Map var4 = this.makeSubDeploymentMap(var2.getSubDeployments());
      Module[] var5 = new Module[var1.size()];
      Iterator var6 = var1.iterator();

      for(int var7 = 0; var7 < var5.length; ++var7) {
         Module var8 = (Module)var6.next();
         SubDeploymentMBean var9 = this.findSubDeployment(var4, var8.getId());
         if (this.hasNoTargets(var9) && var3 != null) {
            var5[var7] = new ModuleListenerInvoker(var8, var3);
         } else if (var9 != null && var9.getTargets().length > 0) {
            TargetMBean var10 = TargetUtils.findLocalTarget(var9.getTargets(), this.server);
            if (var10 != null) {
               var5[var7] = new ModuleListenerInvoker(var8, var10);
            } else {
               var5[var7] = new NonTargetedModuleInvoker(var8);
            }
         } else {
            var5[var7] = new NonTargetedModuleInvoker(var8);
         }
      }

      return var5;
   }

   private boolean hasNoTargets(SubDeploymentMBean var1) {
      return var1 == null || var1.getTargets() == null || var1.getTargets().length == 0;
   }
}
