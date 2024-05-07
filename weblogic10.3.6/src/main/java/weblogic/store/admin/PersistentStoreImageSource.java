package weblogic.store.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.stream.XMLStreamException;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.store.PersistentStoreManager;
import weblogic.xml.stax.util.XMLPrettyPrinter;

public class PersistentStoreImageSource implements ImageSource {
   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      try {
         XMLPrettyPrinter var2 = new XMLPrettyPrinter(new PrintWriter(new OutputStreamWriter(var1, "UTF-8")), 2);
         var2.writeStartDocument();
         PersistentStoreManager.getManager().dump(var2);
         var2.writeEndDocument();
         var2.flush();
      } catch (XMLStreamException var3) {
         this.dumpError(var1, var3);
         throw new ImageSourceCreationException("PersistentStore image  creation failed.", var3);
      } catch (IOException var4) {
         this.dumpError(var1, var4);
         throw new ImageSourceCreationException("PersistentStore image  creation failed.", var4);
      }
   }

   private void dumpError(OutputStream var1, Exception var2) {
      PrintStream var3 = new PrintStream(var1);
      var3.println("Diagnostic image creation failed");
      var2.printStackTrace(var3);
   }

   public void timeoutImageCreation() {
   }
}
