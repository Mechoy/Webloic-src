package weblogic.xml.security.transforms;

import java.util.Map;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class EnvelopedSignatureTransform extends NodeTransform {
   public static final String XPATH_PROPERTY_NAME = "xpath";
   private NodeTransform dest;
   private StartElement startEvent = null;

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      return new SignatureStrippingStream(this.startEvent, this.dest.getXMLOutputStream());
   }

   public String getURI() {
      return "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
   }

   public void setParentSignature(StartElement var1) {
      this.startEvent = var1;
   }

   public void setNamespaces(Map var1) {
      this.dest.setNamespaces(var1);
   }

   public void setDest(NodeTransform var1) {
      this.dest = var1;
   }

   private static class SignatureStrippingStream extends XMLOutputStreamBase {
      private XMLOutputStream dest = null;
      private StartElement sigStart = null;
      private boolean matched = false;
      private int depth = 0;

      public SignatureStrippingStream(StartElement var1, XMLOutputStream var2) {
         this.sigStart = var1;
         this.dest = var2;
      }

      public void addXMLEvent(XMLEvent var1) throws XMLStreamException {
         if (!this.matched && this.depth != 0) {
            if (this.depth > 0) {
               if (var1.isStartElement()) {
                  ++this.depth;
               } else if (var1.isEndElement()) {
                  --this.depth;
               }

               if (this.depth == 0) {
                  this.matched = true;
               }

               this.dest.add(var1);
            } else {
               if (var1 == this.sigStart) {
                  ++this.depth;
                  this.dest.add(var1);
               }

            }
         } else {
            this.dest.add(var1);
         }
      }

      public void flush() throws XMLStreamException {
         this.lastStartElement = null;
         this.dest.flush();
      }

      public void close() throws XMLStreamException {
         this.flush();
         this.dest.close();
      }

      public void close(boolean var1) throws XMLStreamException {
         if (var1) {
            this.flush();
         }

         this.dest.close(var1);
      }
   }
}
