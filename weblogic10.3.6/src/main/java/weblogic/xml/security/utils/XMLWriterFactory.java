package weblogic.xml.security.utils;

import java.util.HashMap;
import java.util.Map;
import javax.xml.soap.SOAPElement;
import org.w3c.dom.Document;
import weblogic.xml.babel.stream.DOMOutputStream;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.XMLOutputStream;

public class XMLWriterFactory {
   private static final XMLWriterFactory theOne = new XMLWriterFactory();
   private static final Map DEFAULT_PREFIXES = new HashMap();

   public static XMLWriterFactory getInstance() {
      return theOne;
   }

   public XMLWriter createXMLWriter(XMLOutputStream var1) {
      XMLOutputStreamWriter var2;
      if (var1 instanceof NSOutputStream) {
         var2 = new XMLOutputStreamWriter((NSOutputStream)var1);
      } else {
         var2 = new XMLOutputStreamWriter(var1);
      }

      var2.setDefaultPrefixes(DEFAULT_PREFIXES);
      return var2;
   }

   public XMLWriter createXMLWriter(SOAPElement var1) {
      SOAPElementWriter var2 = new SOAPElementWriter(var1);
      var2.setDefaultPrefixes(DEFAULT_PREFIXES);
      return var2;
   }

   public XMLWriter createXMLWriter(Document var1) {
      return this.createXMLWriter((XMLOutputStream)(new DOMOutputStream(var1)));
   }

   static {
      DEFAULT_PREFIXES.put(WSUConstants.WSU_URI, "wsu");
      DEFAULT_PREFIXES.put(WSSEConstants.WSSE_URI, "wsse");
      DEFAULT_PREFIXES.put("http://www.w3.org/2001/04/xmlenc#", "xenc");
      DEFAULT_PREFIXES.put("http://www.w3.org/2000/09/xmldsig#", "dsig");
      DEFAULT_PREFIXES.put("http://www.w3.org/2001/10/xml-exc-c14n#", "c14n");
   }
}
