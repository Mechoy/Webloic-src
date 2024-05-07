package weblogic.deploy.service.internal.targetserver;

import java.util.Iterator;
import java.util.Map;
import weblogic.application.utils.XMLWriter;
import weblogic.deploy.internal.diagnostics.ImageProvider;

public class TargetDeploymentServiceImageProvider extends ImageProvider {
   public void writeDiagnosticImage(XMLWriter var1) {
      TargetRequestManager var2 = TargetRequestManager.getInstance();
      Iterator var3 = var2.getRequests().iterator();

      while(var3.hasNext() && !this.timedOut) {
         TargetRequestImpl var4 = (TargetRequestImpl)((Map.Entry)var3.next()).getValue();
         TargetRequestStatus var5 = var4.getDeploymentStatus();
         var1.addElement("target-deployment-service-request");
         var1.addElement("id", "" + var5.getId());
         var1.addElement("state", var5.getCurrentState().toString());
         if (var5.isTimedOut()) {
            var1.addElement("timed-out", "true");
         }

         var1.closeElement();
         var1.flush();
      }

   }
}
