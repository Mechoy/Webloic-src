package weblogic.cluster;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.xml.stream.XMLStreamException;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.xml.stax.util.XMLPrettyPrinter;

public class ClusterDiagnosticImageSource implements ImageSource {
   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      try {
         XMLPrettyPrinter var2 = new XMLPrettyPrinter(new PrintWriter(new OutputStreamWriter(var1, "UTF-8")), 2);
         var2.writeStartDocument();
         var2.writeStartElement("Cluster");
         MulticastManager var3 = MulticastManager.theOne();
         if (var3 != null) {
            var3.dumpDiagnosticImageData(var2);
         }

         var2.writeEndElement();
         var2.writeEndDocument();
         var2.flush();
      } catch (XMLStreamException var4) {
         throw new ImageSourceCreationException("Cluster image creation failed.", var4);
      } catch (IOException var5) {
         throw new ImageSourceCreationException("Cluster image creation failed.", var5);
      }
   }

   public void timeoutImageCreation() {
   }
}
