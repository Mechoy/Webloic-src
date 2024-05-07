package weblogic.messaging.path;

import java.io.OutputStream;
import java.io.PrintWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.messaging.runtime.DiagnosticImageSource;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.store.PersistentStoreException;
import weblogic.xml.stax.util.XMLPrettyPrinter;

public class PathServiceDiagnosticImageSource extends DiagnosticImageSource {
   private PathService pathService;

   public PathServiceDiagnosticImageSource(PathService var1) {
      this.pathService = var1;
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      super.createDiagnosticImage(var1);

      try {
         XMLPrettyPrinter var2 = new XMLPrettyPrinter(new PrintWriter(var1), 2);
         var2.writeStartDocument();

         try {
            this.dump(var2);
         } catch (DiagnosticImageTimeoutException var4) {
            this.dumpTimeoutComment(var2);
            return;
         } catch (PersistentStoreException var5) {
            throw new ImageSourceCreationException("PathService image creation failed.", var5);
         }

         var2.writeEndDocument();
         var2.flush();
      } catch (XMLStreamException var6) {
         throw new ImageSourceCreationException("PathService image creation failed.", var6);
      }
   }

   public void dump(XMLStreamWriter var1) throws DiagnosticImageTimeoutException, XMLStreamException, PersistentStoreException {
      this.checkTimeout();
      var1.writeStartElement("PathService");
      if (this.pathService != null && this.pathService.getPathServiceAdmin() != null) {
         var1.writeAttribute("running", String.valueOf(this.pathService.getPathServiceAdmin().isRunning()));
         var1.writeAttribute("registered", String.valueOf(this.pathService.isRegistered()));
         if (this.pathService.getPathServiceAdmin().getPathMap() != null) {
            this.pathService.getPathServiceAdmin().getPathMap().dump(this, var1);
         }
      }

      var1.writeEndElement();
   }
}
