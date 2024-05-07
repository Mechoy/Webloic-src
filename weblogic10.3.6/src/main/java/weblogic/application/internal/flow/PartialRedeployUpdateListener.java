package weblogic.application.internal.flow;

import java.util.ArrayList;
import java.util.List;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Deployment;
import weblogic.application.DeploymentManager;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.application.utils.EarUtils;
import weblogic.deploy.internal.targetserver.DeploymentContextImpl;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.management.DeploymentException;

final class PartialRedeployUpdateListener implements UpdateListener {
   private final DeploymentManager dm = DeploymentManager.getDeploymentManager();
   private final ApplicationContextInternal appCtx;
   private final List redeployModules = new ArrayList();
   private int activateCount;

   PartialRedeployUpdateListener(ApplicationContextInternal var1) {
      this.appCtx = var1;
   }

   public boolean acceptURI(String var1) {
      ApplicationBean var2 = this.appCtx.getApplicationDD();
      if (var2 == null) {
         return false;
      } else {
         ModuleBean[] var3 = var2.getModules();
         if (var3 == null) {
            return false;
         } else {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var1.equals(EarUtils.reallyGetModuleURI(var3[var4]))) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public void prepareUpdate(String var1) throws ModuleException {
      ModuleBean[] var2 = this.appCtx.getApplicationDD().getModules();
      String var3 = var1;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4].getWeb() != null && var2[var4].getWeb().getWebUri().equals(var1)) {
            var3 = EarUtils.getContextRootName(var2[var4]);
            break;
         }
      }

      this.redeployModules.add(var3);
      this.activateCount = 0;
   }

   public void activateUpdate(String var1) throws ModuleException {
      ++this.activateCount;
      if (this.activateCount >= this.redeployModules.size()) {
         try {
            String[] var2 = (String[])((String[])this.redeployModules.toArray(new String[this.redeployModules.size()]));
            Deployment var3 = this.dm.findDeployment(this.appCtx.getApplicationId());

            try {
               DeploymentContextImpl var4 = new DeploymentContextImpl(this.appCtx.getDeploymentInitiator());
               var4.setProposedDomain(this.appCtx.getProposedDomain());
               var4.setUpdatedResourceURIs(var2);
               var3.stop(var4);
               var3.start(var4);
            } catch (DeploymentException var9) {
               throw new ModuleException(var9);
            }
         } finally {
            this.redeployModules.clear();
         }

      }
   }

   public void rollbackUpdate(String var1) {
      this.redeployModules.clear();
   }
}
