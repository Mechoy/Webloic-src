package weblogic.management.diagnostics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.management.DomainDir;

public class ConfigurationImageSource implements ImageSource {
   private static final String CONFIG;
   private static final String RUNNING_MANAGED_SERVER;
   private volatile boolean imageCreationTimeOut = false;
   private volatile boolean imageCreationInProgress = false;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      this.imageCreationInProgress = true;
      PrintWriter var2 = new PrintWriter(var1);
      this.writeToStream(var2);
      this.imageCreationInProgress = false;
   }

   public void timeoutImageCreation() {
      if (this.imageCreationInProgress) {
         this.imageCreationTimeOut = true;
      }

   }

   private void writeToStream(PrintWriter var1) throws ImageSourceCreationException {
      this.writeToStream(var1, CONFIG);
      this.writeToStream(var1, RUNNING_MANAGED_SERVER);
   }

   private void writeToStream(PrintWriter var1, String var2) throws ImageSourceCreationException {
      var1.println("Contents of the file " + var2 + " Follows :-");
      if (!(new File(var2)).exists()) {
         var1.println("File " + var2 + " Not found on the disk");
      } else {
         BufferedReader var3 = null;

         try {
            var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var2)));

            do {
               var1.println(var3.readLine());
            } while(var3.readLine() != null);

            var3.close();
            var1.flush();
            if (this.imageCreationTimeOut) {
               this.imageCreationTimeOut = false;
               this.imageCreationInProgress = false;
               throw new ImageSourceCreationException("Timeout called.Complete image could not be created");
            }
         } catch (FileNotFoundException var13) {
            this.imageCreationInProgress = false;
            throw new AssertionError("File does not exist" + var2);
         } catch (IOException var14) {
            throw new ImageSourceCreationException("Image creation failed" + var14);
         } finally {
            try {
               var3.close();
            } catch (IOException var12) {
               this.imageCreationInProgress = false;
               throw new ImageSourceCreationException("Image creation failed" + var12);
            }
         }

      }
   }

   static {
      CONFIG = DomainDir.getRootDir() + File.separatorChar + "config.xml";
      RUNNING_MANAGED_SERVER = DomainDir.getRootDir() + File.separatorChar + "running-managed-servers.xml";
   }
}
