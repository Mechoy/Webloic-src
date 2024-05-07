package weblogic.xml.security.utils;

import javax.xml.soap.SOAPElement;
import org.w3c.dom.Node;
import weblogic.xml.babel.stream.DOMInputStream;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class XMLReaderFactory {
   private static final XMLReaderFactory theOne = new XMLReaderFactory();

   public static XMLReaderFactory getInstance() {
      return theOne;
   }

   public XMLReader createXMLReader(SOAPElement var1) {
      return new SOAPElementReader(var1);
   }

   public XMLReader createXMLReader(XMLInputStream var1) {
      try {
         return new XMLInputStreamReader(var1);
      } catch (XMLStreamException var3) {
         throw new RuntimeException("problem with stream", var3);
      }
   }

   public XMLReader createXMLReader(Node var1) {
      DOMInputStream var2 = new DOMInputStream();

      try {
         var2.open(var1);
         return this.createXMLReader((XMLInputStream)var2);
      } catch (XMLStreamException var4) {
         throw new IllegalArgumentException("unable to parse node");
      }
   }
}
