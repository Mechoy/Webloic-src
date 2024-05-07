package weblogic.jms.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.health.HealthState;
import weblogic.jms.JMSService;
import weblogic.messaging.kernel.runtime.MessagingKernelDiagnosticImageSource;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.xml.stax.util.XMLPrettyPrinter;

public class JMSDiagnosticImageSource extends MessagingKernelDiagnosticImageSource {
   public JMSDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();
      var1.registerImageSource("JMS", this);
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      super.createDiagnosticImage(var1);

      try {
         XMLPrettyPrinter var2 = new XMLPrettyPrinter(new PrintWriter(new OutputStreamWriter(var1, "UTF-8")), 2);
         var2.writeStartDocument();

         try {
            JMSService.getJMSService().dump(this, var2);
         } catch (DiagnosticImageTimeoutException var4) {
            this.dumpTimeoutComment(var2);
            var2.flush();
            return;
         }

         var2.writeEndDocument();
         var2.flush();
      } catch (XMLStreamException var5) {
         throw new ImageSourceCreationException("JMS image creation failed.", var5);
      } catch (IOException var6) {
         throw new ImageSourceCreationException("JMS image creation failed.", var6);
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

   public static void dumpDestinationImpl(XMLStreamWriter var0, DestinationImpl var1) throws XMLStreamException {
      var0.writeAttribute("name", var1.getName() != null ? var1.getName() : "");
      var0.writeAttribute("serverName", var1.getServerName());
      var0.writeAttribute("applicationName", var1.getApplicationName() != null ? var1.getApplicationName() : "");
      var0.writeAttribute("moduleName", var1.getModuleName() != null ? var1.getModuleName() : "");
      var0.writeAttribute("id", var1.getDestinationId() != null ? var1.getDestinationId().toString() : "");
   }
}
