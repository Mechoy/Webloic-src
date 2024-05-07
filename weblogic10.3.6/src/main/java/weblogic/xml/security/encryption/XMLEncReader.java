package weblogic.xml.security.encryption;

import weblogic.xml.security.utils.XMLObjectReader;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class XMLEncReader extends XMLObjectReader implements XMLEncConstants {
   private static XMLEncReader singleton = new XMLEncReader();

   private XMLEncReader() {
      this.register("http://www.w3.org/2001/04/xmlenc#", "EncryptedData", 1);
      this.register("http://www.w3.org/2001/04/xmlenc#", "EncryptedKey", 2);
      this.register("http://www.w3.org/2001/04/xmlenc#", "ReferenceList", 4);
      this.register("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod", 3);
   }

   public Object readObjectInternal(int var1, String var2, XMLInputStream var3) throws XMLStreamException {
      switch (var1) {
         case 1:
            return new EncryptedData(var3, var2);
         case 2:
            return new EncryptedKey(var3, var2);
         case 3:
            return EncryptionMethod.fromXML(var3, var2);
         case 4:
            return new ReferenceList(var3, var2);
         default:
            throw new AssertionError("Unknown typecode: " + var1);
      }
   }

   public static Object read(XMLInputStream var0) throws XMLStreamException {
      return singleton.readObject(var0);
   }

   public static Object read(XMLInputStream var0, int var1) throws XMLStreamException {
      return singleton.readObject(var0, var1);
   }
}
