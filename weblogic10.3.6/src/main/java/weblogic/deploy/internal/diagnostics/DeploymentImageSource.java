package weblogic.deploy.internal.diagnostics;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import weblogic.application.utils.XMLWriter;
import weblogic.deploy.service.internal.adminserver.AdminDeploymentServiceImageProvider;
import weblogic.deploy.service.internal.targetserver.TargetDeploymentServiceImageProvider;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.management.deploy.DeploymentTaskImageProvider;
import weblogic.management.deploy.internal.AppRuntimeStateImageProvider;

public class DeploymentImageSource implements ImageSource {
   private boolean timedOut = false;
   private ImageProvider[] ips = null;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      XMLWriter var2 = null;

      try {
         var2 = new XMLWriter(new PrintWriter(new OutputStreamWriter(var1, "UTF-8")));
         var2.addElement("deployment");
         this.ips = DeploymentImageSource.IPFactory.getProviders();

         for(int var3 = 0; var3 < this.ips.length && !this.timedOut; ++var3) {
            this.ips[var3].writeDiagnosticImage(var2);
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

      for(int var1 = 0; var1 < this.ips.length && !this.timedOut; ++var1) {
         this.ips[var1].timeoutImageCreation();
      }

   }

   private static class IPFactory {
      public static ImageProvider[] getProviders() {
         return new ImageProvider[]{new AppRuntimeStateImageProvider(), new DeploymentTaskImageProvider(), new AdminDeploymentServiceImageProvider(), new TargetDeploymentServiceImageProvider()};
      }
   }
}
