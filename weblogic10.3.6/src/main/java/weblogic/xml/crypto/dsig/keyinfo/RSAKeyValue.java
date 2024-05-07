package weblogic.xml.crypto.dsig.keyinfo;

import java.security.KeyException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyValue;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.security.utils.Utils;

public class RSAKeyValue extends KeyValueImpl implements KeyValue, XMLStructure, WLXMLStructure {
   public static final String RSAKEYVALUE_ELEMENT = "RSAKeyValue";
   private static final String MODULUS_ELEMENT = "Modulus";
   private static final String EXPONENT_ELEMENT = "Exponent";
   private static final String ALGORITHM_RSA = "RSA";
   private RSAPublicKey publicKey;
   private String modulus;
   private String exponent;

   RSAKeyValue() {
   }

   RSAKeyValue(RSAPublicKey var1) {
      this.publicKey = var1;
   }

   public PublicKey getPublicKey() throws KeyException {
      return this.publicKey;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue");
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "Modulus", Utils.toCryptoBinary(this.publicKey.getModulus()));
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "Exponent", Utils.toCryptoBinary(this.publicKey.getPublicExponent()));
         var1.writeEndElement();
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element KeyValue", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.nextTag();
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "Modulus");
         var1.next();
         this.modulus = var1.getText();
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "Modulus", var1);
         var1.nextTag();
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "Exponent");
         var1.next();
         this.exponent = var1.getText();
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue", var1);
         RSAPublicKeySpec var2 = new RSAPublicKeySpec(Utils.fromCryptoBinary(this.modulus), Utils.fromCryptoBinary(this.exponent));
         this.publicKey = (RSAPublicKey)this.createFromKeySpec("RSA", var2);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read RSAKeyValue element.", var3);
      }
   }
}
