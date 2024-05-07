package weblogic.xml.security.signature;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import weblogic.utils.io.NullOutputStream;
import weblogic.xml.security.transforms.DigestTransform;
import weblogic.xml.security.transforms.IncompatibleTransformException;
import weblogic.xml.security.transforms.Transform;
import weblogic.xml.security.transforms.Transforms;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.utils.XMLSecurityException;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class Reference implements DSIGConstants {
   private static final OutputStream NULL_OS = new NullOutputStream();
   private String uri;
   private DigestMethod digestMethod;
   private String digestValue;
   private Transforms transforms = new Transforms();

   public Reference(String var1) {
      this.uri = var1;

      try {
         this.digestMethod = DigestMethod.get("http://www.w3.org/2000/09/xmldsig#sha1");
      } catch (XMLSecurityException var3) {
         throw new AssertionError(var3);
      }
   }

   public Reference(String var1, DigestMethod var2) {
      this.uri = var1;
      this.digestMethod = var2;
   }

   private Reference(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public String getURI() {
      return this.uri;
   }

   public void addTransform(Transform var1) throws IncompatibleTransformException {
      this.transforms.add(var1);
   }

   protected abstract void process(Transforms var1) throws InvalidReferenceException;

   void digest() throws InvalidReferenceException {
      this.digestValue = Utils.base64(this.getDigest());
   }

   public void print(String var1, byte[] var2) {
      if (VERBOSE) {
         System.out.println(" +++++++ Printing " + var1 + " : " + Utils.base64(var2));
      }

   }

   void validate() throws InvalidReferenceException {
      byte[] var1 = this.getDigest();
      this.print("computedDigest", var1);
      byte[] var2 = Utils.base64(this.digestValue);
      this.print("referenceDigest", var2);
      if (!Arrays.equals(var1, var2)) {
         throw new InvalidReferenceException("Invalid digest", this);
      }
   }

   private byte[] getDigest() throws InvalidReferenceException {
      if (this.digestMethod == null) {
         throw new InvalidReferenceException("No DigestMethod set", this);
      } else {
         MessageDigest var1 = this.digestMethod.getMessageDigest();
         DigestTransform var2 = null;
         if (VERBOSE) {
            System.out.println("<!-- -- Begin Reference" + this.refDebug() + " ----->");
            var2 = new DigestTransform(new UncloseableOutputStream(System.out), var1);
         } else {
            var2 = new DigestTransform(NULL_OS, var1);
         }

         try {
            this.transforms.add(var2);
            this.process(this.transforms);
            this.transforms.remove(var2);
         } catch (IncompatibleTransformException var4) {
            throw new AssertionError(var4);
         }

         if (VERBOSE) {
            System.out.println("\n<!----- End Reference " + this.refDebug() + "  -- -->");
         }

         byte[] var3 = var1.digest();
         return var3;
      }
   }

   private final String refDebug() {
      return this.uri == null ? "" : "(uri=" + this.uri + ")";
   }

   public final Transform[] getTransforms() {
      return this.transforms.getTransforms();
   }

   void copyDigestValue(Reference var1) {
      this.digestValue = var1.digestValue;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Reference:  URI=").append(this.uri).append("\n").append("  digest: ").append(this.digestValue).append("\n").append("  digestMethod: ").append(this.digestMethod).append("\n").append("  transforms: ").append(this.transforms).append("\n");
      return var1.toString();
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("URI", this.uri)};
      StreamUtils.addStart(var1, var2, "Reference", var4, var3);
      this.transforms.toXML(var1, var2, var3 + 2);
      this.digestMethod.toXML(var1, var2, var3 + 2);
      StreamUtils.addElement(var1, var2, "DigestValue", (String)this.digestValue, var3 + 2, 2);
      StreamUtils.addEnd(var1, var2, "Reference", var3);
   }

   static Reference fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      StartElement var2 = (StartElement)StreamUtils.getElement(var0, var1, "Reference");
      String var3 = StreamUtils.getAttribute(var2, "URI");
      StreamUtils.requiredAttr(var3, "Reference", "URI");
      Object var4;
      if (var3.equals("")) {
         var4 = new EnvelopedReference();
      } else if (var3.startsWith("#")) {
         var4 = new InternalReference(var3);
         ((InternalReference)var4).setNamespaces(var2.getNamespaceMap());
      } else {
         var4 = new ExternalReference(var3);
      }

      ((Reference)var4).fromXMLInternal(var0, var1);
      return (Reference)var4;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      this.transforms = (Transforms)DSIGReader.read(var1, 12);
      if (this.transforms == null) {
         this.transforms = new Transforms();
      }

      this.digestMethod = (DigestMethod)DSIGReader.read(var1, 2);
      this.digestValue = StreamUtils.getValue(var1, var2, "DigestValue");
      StreamUtils.closeScope(var1, var2, "Reference");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<Reference xmlns=\"http://www.w3.org/2000/09/xmldsig#\" URI=\"#MyData\">\n  <Transforms>\n    <Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n  </Transforms>\n  <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n  <DigestValue>fdy6S2NLpnT4fMdokUHSHsmpcvo=</DigestValue>\n</Reference>\n");
      Reference var2 = (Reference)DSIGReader.read(var1, 6);
      var2.digest();
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }

   private static class UncloseableOutputStream extends OutputStream {
      private final OutputStream destination;

      public UncloseableOutputStream(OutputStream var1) {
         this.destination = var1;
      }

      public void write(int var1) throws IOException {
         this.destination.write(var1);
      }

      public void close() throws IOException {
      }

      public void flush() throws IOException {
         this.destination.flush();
      }
   }
}
