package weblogic.jdbc.common.internal;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;

public final class JDBCImageSource implements ImageSource {
   private boolean imageCreationTimeout = false;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      this.imageCreationTimeout = false;
      PrintWriter var2 = new PrintWriter(var1);
      synchronized(ConnectionPoolManager.getLockObject()) {
         Iterator var4 = ConnectionPoolManager.getConnectionPools();

         while(var4.hasNext()) {
            ConnectionPool var5 = (ConnectionPool)var4.next();
            var5.dumpPool(var2);
            if (this.imageCreationTimeout) {
               var2.println("ImageSource timed out.");
               break;
            }
         }
      }

      var2.flush();
   }

   public void timeoutImageCreation() {
      this.imageCreationTimeout = true;
   }
}
