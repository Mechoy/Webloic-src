package weblogic.connector.common;

import java.io.OutputStream;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;

public class ConnectorDiagnosticImageSource implements ImageSource {
   private boolean timedout = false;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      try {
         RACollectionManager.getXMLBean(this).save(var1);
      } catch (Throwable var8) {
         String var3 = Debug.getExceptionImageSourceCreation(var8.toString());
         throw new ImageSourceCreationException(var3, var8);
      } finally {
         this.timedout = false;
      }

   }

   public void timeoutImageCreation() {
      this.timedout = true;
      Debug.logDiagnosticImageTimedOut();
   }

   public boolean timedout() {
      return this.timedout;
   }
}
