package weblogic.xml.security.encryption;

import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class DataReference implements XMLEncConstants {
   private String uri;

   public DataReference(String var1) {
      this.uri = var1;
   }

   private DataReference(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public String getURI() {
      return this.uri;
   }

   public String toString() {
      return "DataReference:  URI=" + this.uri;
   }

   void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{StreamUtils.createAttribute("URI", "#" + this.uri)};
      StreamUtils.addStart(var1, var2, "DataReference", var4, var3);
      StreamUtils.addEnd(var1, var2, "DataReference", var3);
   }

   static DataReference fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      return StreamUtils.peekElement(var0, var1, "DataReference") ? new DataReference(var0, var1) : null;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)var1.next();
      this.uri = StreamUtils.getAttribute(var3, "URI");
      StreamUtils.requiredAttr(this.uri, "DataReference", "URI");
      StreamUtils.closeScope(var1, var2, "DataReference");
   }
}
