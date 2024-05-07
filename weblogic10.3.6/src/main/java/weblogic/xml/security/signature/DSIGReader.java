package weblogic.xml.security.signature;

import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.keyinfo.KeyValue;
import weblogic.xml.security.keyinfo.X509Data;
import weblogic.xml.security.transforms.Transforms;
import weblogic.xml.security.utils.XMLObjectReader;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class DSIGReader extends XMLObjectReader implements DSIGConstants {
   private static DSIGReader singleton = new DSIGReader();

   private DSIGReader() {
      this.register("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod", 1);
      this.register("http://www.w3.org/2000/09/xmldsig#", "DigestMethod", 2);
      this.register("http://www.w3.org/2000/09/xmldsig#", "KeyInfo", 4);
      this.register("http://www.w3.org/2000/09/xmldsig#", "KeyValue", 5);
      this.register("http://www.w3.org/2000/09/xmldsig#", "Reference", 6);
      this.register("http://www.w3.org/2000/09/xmldsig#", "Signature", 8);
      this.register("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod", 9);
      this.register("http://www.w3.org/2000/09/xmldsig#", "SignedInfo", 10);
      this.register("http://www.w3.org/2000/09/xmldsig#", "X509Data", 11);
      this.register("http://www.w3.org/2000/09/xmldsig#", "Transforms", 12);
   }

   public Object readObjectInternal(int var1, String var2, XMLInputStream var3) throws XMLStreamException {
      switch (var1) {
         case 1:
            return CanonicalizationMethod.fromXML(var3, var2);
         case 2:
            return DigestMethod.fromXML(var3, var2);
         case 3:
         case 7:
         default:
            throw new AssertionError("Unknown typecode: " + var1);
         case 4:
            return new KeyInfo(var3, var2);
         case 5:
            return KeyValue.fromXML(var3, var2);
         case 6:
            return Reference.fromXML(var3, var2);
         case 8:
            return new Signature(var3, var2);
         case 9:
            return SignatureMethod.fromXML(var3, var2);
         case 10:
            return new SignedInfo(var3, var2);
         case 11:
            return new X509Data(var3, var2);
         case 12:
            return Transforms.fromXML(var3, var2);
      }
   }

   public static Object read(XMLInputStream var0, int var1) throws XMLStreamException {
      return singleton.readObject(var0, var1);
   }
}
