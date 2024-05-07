package weblogic.transaction.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.health.HealthState;
import weblogic.xml.stax.util.XMLPrettyPrinter;

public class JTADiagnosticImageSource implements JTAImageSource {
   private ServerTransactionManagerImpl tm;
   private boolean timedOut;

   JTADiagnosticImageSource(ServerTransactionManagerImpl var1) {
      this.tm = var1;
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      try {
         XMLPrettyPrinter var2 = new XMLPrettyPrinter(new PrintWriter(new OutputStreamWriter(var1, "UTF-8")), 2);
         var2.writeStartDocument();

         try {
            this.tm.dump(this, var2);
         } catch (DiagnosticImageTimeoutException var4) {
            this.dumpTimeoutComment(var2);
            return;
         }

         var2.writeEndDocument();
         var2.flush();
      } catch (XMLStreamException var5) {
         throw new ImageSourceCreationException("JTA image creation failed.", var5);
      } catch (IOException var6) {
         throw new ImageSourceCreationException("JTA image creation failed.", var6);
      }
   }

   public static void dumpHealthStateElement(XMLStreamWriter var0, HealthState var1) throws XMLStreamException {
      var0.writeStartElement("Health");
      var0.writeAttribute("state", HealthState.mapToString(var1.getState()));
      String[] var2 = var1.getReasonCode();
      if (var2 != null && var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var0.writeStartElement("Reason");
            var0.writeCharacters(var2[var3]);
            var0.writeEndElement();
         }
      }

      var0.writeEndElement();
   }

   public void timeoutImageCreation() {
      this.timedOut = true;
   }

   public void checkTimeout() throws DiagnosticImageTimeoutException {
      if (this.timedOut) {
         throw new DiagnosticImageTimeoutException();
      }
   }

   public void dumpTimeoutComment(XMLStreamWriter var1) throws XMLStreamException {
      var1.writeComment("Diagnostic image creation timed out, aborting image dump");
   }
}
