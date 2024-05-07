package weblogic.xml.security.keyinfo;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class RSAKeyValue extends KeyValue implements DSIGConstants {
   private static final String ALGORITHM_RSA = "RSA";
   private RSAPublicKey publicKey;
   private boolean validated = false;
   private String modulus;
   private String exponent;

   public RSAKeyValue(RSAPublicKey var1) {
      this.publicKey = var1;
      this.validated = true;
   }

   public RSAKeyValue(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public PublicKey getPublicKey() throws KeyInfoValidationException {
      if (!this.validated) {
         this.validate();
      }

      return this.publicKey;
   }

   private BigInteger getModulus() {
      return this.publicKey.getModulus();
   }

   private BigInteger getPublicExponent() {
      return this.publicKey.getPublicExponent();
   }

   public void validate() throws KeyInfoValidationException {
      if (!this.validated) {
         RSAPublicKeySpec var1 = new RSAPublicKeySpec(weblogic.xml.security.utils.Utils.fromCryptoBinary(this.modulus), weblogic.xml.security.utils.Utils.fromCryptoBinary(this.exponent));
         this.publicKey = (RSAPublicKey)this.createFromKeySpec("RSA", var1);
         this.validated = true;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("RSAPublicKey:\n").append("  Modulus: " + this.getModulus()).append("\n").append("  Exponent: " + this.getPublicExponent()).append("\n");
      return var1.toString();
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      this.modulus = weblogic.xml.security.utils.Utils.toCryptoBinary(this.getModulus());
      this.exponent = weblogic.xml.security.utils.Utils.toCryptoBinary(this.getPublicExponent());
      StreamUtils.addStart(var1, var2, "RSAKeyValue", var3);
      StreamUtils.addElement(var1, var2, "Modulus", (String)this.modulus, var3 + 2, 2);
      StreamUtils.addElement(var1, var2, "Exponent", this.exponent, var3 + 2);
      StreamUtils.addEnd(var1, var2, "RSAKeyValue", var3);
   }

   public void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      var1.next();
      this.modulus = StreamUtils.getValue(var1, var2, "Modulus");
      StreamUtils.required(this.modulus, "RSAKeyValue", "Modulus");
      this.exponent = StreamUtils.getValue(var1, var2, "Exponent");
      StreamUtils.required(this.exponent, "RSAKeyValue", "Exponent");
      StreamUtils.closeScope(var1, var2, "RSAKeyValue");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<RSAKeyValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <Modulus>\nq07hpxA5DGFfvJFZueFl/LI85XxQxrvqgVugL25V090A9MrlLBg5PmAsxFTe+G6axvWJQwYOVHj/nuiCnNLa9a7uAtPFiTtW+v5H3wlLaY3ws4atRBNOQlYkIBp38sTfQBkk4i8PEU1GQ2M0CLIJq4/2Akfv1wxzSQ9+8oWkArc=\n  </Modulus>\n  <Exponent>\n    AQAB\n  </Exponent>\n</RSAKeyValue>\n");
      RSAKeyValue var2 = new RSAKeyValue(var1, "http://www.w3.org/2000/09/xmldsig#");
      var2.validate();
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
