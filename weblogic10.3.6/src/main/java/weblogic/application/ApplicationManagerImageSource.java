package weblogic.application;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import weblogic.application.utils.XMLWriter;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;

public class ApplicationManagerImageSource implements ImageSource {
   private boolean timedOut = false;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      XMLWriter var2 = null;

      try {
         var2 = new XMLWriter(new PrintWriter(new OutputStreamWriter(var1, "UTF-8")));
         var2.addElement("application-container");
         Iterator var3 = DeploymentManager.getDeploymentManager().getDeployments();

         while(var3.hasNext() && !this.timedOut) {
            Deployment var4 = (Deployment)var3.next();
            var2.addElement("application");

            while(var4 instanceof DeploymentWrapper) {
               var4 = ((DeploymentWrapper)var4).getDeployment();
            }

            var2.addElement("deployment-class", var4.getClass().getName());
            var4.getApplicationContext().writeDiagnosticImage(var2);
            var2.closeElement();
         }
      } catch (IOException var8) {
         throw new ImageSourceCreationException(var8);
      } finally {
         if (var2 != null) {
            var2.finish();
         }

      }

   }

   public void timeoutImageCreation() {
      this.timedOut = true;
   }
}
