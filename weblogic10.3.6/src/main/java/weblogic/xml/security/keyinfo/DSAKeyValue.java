package weblogic.xml.security.keyinfo;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public final class DSAKeyValue extends KeyValue implements DSIGConstants {
   private static final String ALGORITHM_DSA = "DSA";
   private DSAPublicKey publicKey;
   private boolean validated = false;
   private String p;
   private String q;
   private String g;
   private String y;

   DSAKeyValue(DSAPublicKey var1) {
      this.publicKey = var1;
      this.validated = true;
   }

   DSAKeyValue(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public PublicKey getPublicKey() throws KeyInfoValidationException {
      if (!this.validated) {
         this.validate();
      }

      return this.publicKey;
   }

   private BigInteger getP() {
      return this.publicKey.getParams().getP();
   }

   private BigInteger getQ() {
      return this.publicKey.getParams().getQ();
   }

   private BigInteger getG() {
      return this.publicKey.getParams().getG();
   }

   private BigInteger getY() {
      return this.publicKey.getY();
   }

   public void validate() throws KeyInfoValidationException {
      if (!this.validated) {
         DSAPublicKeySpec var1 = new DSAPublicKeySpec(weblogic.xml.security.utils.Utils.fromCryptoBinary(this.y), weblogic.xml.security.utils.Utils.fromCryptoBinary(this.p), weblogic.xml.security.utils.Utils.fromCryptoBinary(this.q), weblogic.xml.security.utils.Utils.fromCryptoBinary(this.g));
         this.publicKey = (DSAPublicKey)this.createFromKeySpec("DSA", var1);
         this.validated = true;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("DSAPublicKey:\n").append("P: ").append(this.getP()).append("\n").append("Q: ").append(this.getQ()).append("\n").append("G: ").append(this.getG()).append("\n").append("Y: ").append(this.getY()).append("\n");
      return var1.toString();
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      this.p = weblogic.xml.security.utils.Utils.toCryptoBinary(this.getP());
      this.q = weblogic.xml.security.utils.Utils.toCryptoBinary(this.getQ());
      this.g = weblogic.xml.security.utils.Utils.toCryptoBinary(this.getG());
      this.y = weblogic.xml.security.utils.Utils.toCryptoBinary(this.getY());
      StreamUtils.addStart(var1, var2, "DSAKeyValue", var3);
      if (this.p != null) {
         StreamUtils.addElement(var1, var2, "P", (String)this.p, var3 + 2, 2);
         StreamUtils.addElement(var1, var2, "Q", (String)this.q, var3 + 2, 2);
      }

      if (this.g != null) {
         StreamUtils.addElement(var1, var2, "G", (String)this.g, var3 + 2, 2);
      }

      if (this.y != null) {
         StreamUtils.addElement(var1, var2, "Y", (String)this.y, var3 + 2, 2);
      }

      StreamUtils.addEnd(var1, var2, "DSAKeyValue", var3);
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      var1.next();
      this.p = StreamUtils.getValue(var1, var2, "P");
      if (this.p != null) {
         this.q = StreamUtils.getValue(var1, var2, "Q");
         StreamUtils.required(this.q, "DSAKeyValue", "Q");
      }

      this.g = StreamUtils.getValue(var1, var2, "G");
      this.y = StreamUtils.getValue(var1, var2, "Y");
      StreamUtils.required(this.y, "DSAKeyValue", "Y");
      StreamUtils.closeScope(var1, var2, "DSAKeyValue");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<DSAKeyValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <P>\n    3eOeAvqnEyFpW+uTSgrdj7YLjaTkpyHecKFIoLu8QZNkGTQI1ciITBH0lqfIkdCH\n    Si8fiUC3DTq3J9FsJef4YVtDF7JpUvHTOQqtq7Zgx6KC8Wxkz6rQCxOr7F0ApOYi\n    89zLRoe4MkDGe6ux0+WtyOTQoVIGNTDDUFXrUQNbLrE=\n  </P>\n  <Q>\n    hDLcFK0GO/Hz1arxOOvsgM/VLyU=\n  </Q>\n  <G>\n    nnx7hbdWozGbtnFgnbFnopfRl7XRacpkPJRGf5P2IUgVspEUSUoN6i1fDBfBg43z\n    Kt7dlEaQL7b5+JTZt3MhZNPosxsgxVuT7Ts/g5k7EnpdYv0a5hw5Bw29fjbGHfgM\n    8d2rhd2Ui0xHbk0D451nhLxVWulviOSPhzKKvXrbySA=\n  </G>\n  <Y>\n    cfYpihpAQeepbNFS4MAbQRhdXpDi5wLrwxE5hIvoYqo1L8BQVu8fY1TFAPtoae1i\n    Bg/GIJyP3iLfyuBJaDvJJLP30wBH9i/s5J3656PevpOVdTfi777Fi9Gj6y/ib2Vv\n    +OZfJkkp4L50+p5TUhPmQLJtREsgtl+tnIOyJT++G9U=\n  </Y>\n</DSAKeyValue>\n");
      DSAKeyValue var2 = new DSAKeyValue(var1, "http://www.w3.org/2000/09/xmldsig#");
      var2.validate();
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
