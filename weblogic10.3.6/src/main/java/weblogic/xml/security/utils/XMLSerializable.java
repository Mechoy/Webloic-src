package weblogic.xml.security.utils;

import javax.xml.soap.SOAPElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class XMLSerializable {
   private static final XMLReaderFactory rFactory = XMLReaderFactory.getInstance();
   private static final XMLWriterFactory wFactory = XMLWriterFactory.getInstance();

   public abstract void toXML(XMLWriter var1);

   public void toXML(XMLOutputStream var1) {
      this.toXML(wFactory.createXMLWriter(var1));
   }

   public void toXML(SOAPElement var1) {
      this.toXML(wFactory.createXMLWriter(var1));
   }

   protected abstract void fromXMLInternal(XMLReader var1) throws ValidationException;

   protected void fromXMLInternal(XMLInputStream var1) throws XMLStreamException {
      try {
         this.fromXMLInternal(rFactory.createXMLReader(var1));
      } catch (ValidationException var3) {
         throw new XMLStreamException(var3);
      }
   }

   protected void fromXMLInternal(SOAPElement var1) {
      try {
         this.fromXMLInternal(rFactory.createXMLReader(var1));
      } catch (ValidationException var3) {
         throw new RuntimeException(var3);
      }
   }

   protected static final int skip(XMLReader var0) throws ValidationException {
      int var1 = 0;
      int var2 = var0.getEventType();
      if (var2 == 4) {
         return var2;
      } else {
         do {
            switch (var2) {
               case 2:
                  ++var1;
                  break;
               case 4:
                  --var1;
               case 16:
                  break;
               default:
                  throw new ValidationException("unexpected event");
            }

            var2 = var0.next();
         } while(var1 > 0);

         return var2;
      }
   }
}
