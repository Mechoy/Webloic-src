package weblogic.management.deploy.internal;

import java.util.Iterator;
import java.util.Map;
import weblogic.application.utils.XMLWriter;
import weblogic.deploy.internal.diagnostics.ImageProvider;

public class AppRuntimeStateImageProvider extends ImageProvider {
   public void writeDiagnosticImage(XMLWriter var1) {
      AppRuntimeStateManager var2 = AppRuntimeStateManager.getManager();
      Iterator var3 = var2.getAppStates().iterator();

      while(var3.hasNext() && !this.timedOut) {
         ApplicationRuntimeState var4 = (ApplicationRuntimeState)((Map.Entry)var3.next()).getValue();
         var1.addElement("app-runtime-state");
         var1.addElement("app-id", "" + var4.getAppId());
         var1.addElement("retire-timeout-secs", "" + var4.getRetireTimeoutSeconds());
         var1.addElement("retire-time-millis", "" + var4.getRetireTimeMillis());
         if (var4.getModules() != null) {
            var1.addElement("module-state", var4.getModules().toString());
         }

         if (var4.getAppTargetState() != null) {
            var1.addElement("app-target-state", var4.getAppTargetState().toString());
         }

         if (var4.getDeploymentVersion() != null) {
            var1.addElement("deployment-version", var4.getDeploymentVersion().toString());
         }

         var1.closeElement();
         var1.flush();
      }

   }
}
