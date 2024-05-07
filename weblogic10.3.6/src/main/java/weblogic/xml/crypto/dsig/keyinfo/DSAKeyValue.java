package weblogic.xml.crypto.dsig.keyinfo;

import java.security.KeyException;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyValue;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.security.utils.Utils;

public final class DSAKeyValue extends KeyValueImpl implements KeyValue, XMLStructure, WLXMLStructure {
   public static final String DSAKEYVALUE_ELEMENT = "DSAKeyValue";
   private static final String P_ELEMENT = "P";
   private static final String Q_ELEMENT = "Q";
   private static final String G_ELEMENT = "G";
   private static final String Y_ELEMENT = "Y";
   private static final String J_ELEMENT = "J";
   private static final String ALGORITHM_DSA = "DSA";
   private DSAPublicKey publicKey;
   private String p;
   private String q;
   private String g;
   private String y;

   DSAKeyValue() {
   }

   DSAKeyValue(DSAPublicKey var1) {
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
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue");
         DSAParams var2 = this.publicKey.getParams();
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "P", Utils.toCryptoBinary(var2.getP()));
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "Q", Utils.toCryptoBinary(var2.getQ()));
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "G", Utils.toCryptoBinary(var2.getG()));
         StaxUtils.writeElement(var1, "http://www.w3.org/2000/09/xmldsig#", "Y", Utils.toCryptoBinary(this.publicKey.getY()));
         var1.writeEndElement();
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element KeyValue", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.nextTag();
         String var2 = var1.getLocalName();
         if ("P".equals(var2)) {
            var1.next();
            this.p = var1.getText();
            var1.nextTag();
            var1.nextTag();
            var1.next();
            this.q = var1.getText();
            var1.nextTag();
            var1.nextTag();
         }

         var2 = var1.getLocalName();
         if ("G".equals(var2)) {
            var1.next();
            this.g = var1.getText();
            var1.nextTag();
            var1.nextTag();
         }

         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "Y");
         var1.next();
         this.y = var1.getText();
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue", var1);
         DSAPublicKeySpec var3 = new DSAPublicKeySpec(Utils.fromCryptoBinary(this.y), Utils.fromCryptoBinary(this.p), Utils.fromCryptoBinary(this.q), Utils.fromCryptoBinary(this.g));
         this.publicKey = (DSAPublicKey)this.createFromKeySpec("DSA", var3);
      } catch (XMLStreamException var4) {
         throw new MarshalException("Failed to read DSAKeyValue element.", var4);
      }
   }
}
