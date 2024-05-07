package weblogic.management.deploy;

import weblogic.application.utils.XMLWriter;
import weblogic.deploy.internal.diagnostics.ImageProvider;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;

public class DeploymentTaskImageProvider extends ImageProvider {
   public void writeDiagnosticImage(XMLWriter var1) {
      if (isAdminServer) {
         DeployerRuntimeMBean var2 = DeployerRuntime.getDeployerRuntime();
         DeploymentTaskRuntimeMBean[] var3 = var2.list();
         if (var3 == null) {
            var1.addElement("deployment-task-count", "0");
         } else {
            var1.addElement("deployment-task-count", "" + var3.length);

            for(int var4 = 0; var4 < var3.length && !this.timedOut; ++var4) {
               var1.addElement("deployment-task");
               var1.addElement("id", var3[var4].getId());
               var1.addElement("status", this.getState(var3[var4].getState()));
               DeploymentTaskRuntime.DeploymentAction var5 = DeploymentTaskRuntime.DeploymentAction.getDeploymentAction(var3[var4].getTask());
               var1.addElement("operation", var5.getDescription());
               var1.addElement("application-name", var3[var4].getApplicationName());
               var1.closeElement();
               var1.flush();
            }

         }
      }
   }

   private String getState(int var1) {
      switch (var1) {
         case 0:
            return "initialized";
         case 1:
            return "running";
         case 2:
            return "completed";
         case 3:
            return "failed";
         case 4:
            return "deferred";
         default:
            return "unknown";
      }
   }
}
