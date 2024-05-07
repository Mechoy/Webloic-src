package weblogic.xml.crypto.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.OctetStreamData;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.dsig.TransformImpl;
import weblogic.xml.crypto.dsig.WLTransform;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.utils.ByteVector;
import weblogic.xml.crypto.utils.StaxUtils;

public class WLCipherReference extends WLCipherData implements URIReference, CipherReference {
   public static final String TAG_CIPHER_REFERENCE = "CipherReference";
   private WLURIReference uriRef;
   private InputStream cipherText;
   private List transforms;
   public static final String ATTR_URI = "URI";
   public static final String TAG_TRANSFORMS = "Transforms";

   WLCipherReference(String var1, List var2) {
      this.uriRef = new WLURIReference(var1, (String)null);
      this.transforms = var2 != null ? Collections.unmodifiableList(var2) : var2;
   }

   WLCipherReference() {
      this.uriRef = new WLURIReference((String)null, (String)null);
   }

   public void setCipherText(InputStream var1) {
      this.cipherText = var1;
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         StaxUtils.findStart(var1, "http://www.w3.org/2001/04/xmlenc#", "CipherReference", true);

         for(int var2 = 0; var2 < var1.getAttributeCount(); ++var2) {
            String var3 = var1.getAttributeNamespace(var2);
            if (!this.emptyNamespace(var3) && !var3.equals("http://www.w3.org/2001/04/xmlenc#")) {
               throw new MarshalException("XML Encryption does not allow non-native attributes: " + var3 + ":" + var1.getAttributeLocalName(var2));
            }

            String var4 = var1.getAttributeLocalName(var2);
            String var5 = var1.getAttributeValue(var2);
            if (!var4.equals("URI")) {
               throw new MarshalException("Unexpected attribute: " + var4);
            }

            this.uriRef.setURI(var5);
         }

         this.readTransforms(var1);
         StaxUtils.findEnd(var1, "http://www.w3.org/2001/04/xmlenc#", "CipherReference");
         var1.next();
      } catch (XMLStreamException var6) {
         throw new MarshalException(var6);
      }
   }

   private boolean emptyNamespace(String var1) {
      return var1 == null || "".equals(var1);
   }

   private void readTransforms(XMLStreamReader var1) throws MarshalException {
      boolean var2 = false;

      try {
         var2 = StaxUtils.findStart(var1, "http://www.w3.org/2001/04/xmlenc#", "Transforms");
         if (var2) {
            this.transforms = new ArrayList();

            while("http://www.w3.org/2000/09/xmldsig#".equals(var1.getNamespaceURI()) && "Transform".equals(var1.getLocalName())) {
               this.transforms.add(TransformImpl.readTransform(var1));
               var1.nextTag();
            }

            StaxUtils.findEnd(var1, "http://www.w3.org/2001/04/xmlenc#", "Transforms");
         }

      } catch (XMLStreamException var4) {
         throw new MarshalException(var4);
      }
   }

   protected void writeDataSource(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2001/04/xmlenc#", "CipherReference");
         String var2 = this.uriRef.getURI();
         if (var2 != null) {
            var1.writeAttribute("URI", var2);
         }

         this.writeTransforms(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   private void writeTransforms(XMLStreamWriter var1) throws XMLStreamException, MarshalException {
      if (this.transforms != null && !this.transforms.isEmpty()) {
         var1.writeStartElement("http://www.w3.org/2001/04/xmlenc#", "Transforms");

         for(int var2 = 0; var2 < this.transforms.size(); ++var2) {
            WLTransform var3 = (WLTransform)this.transforms.get(var2);
            var3.write(var1);
         }

         var1.writeEndElement();
      }

   }

   public InputStream getCipherTextInternal() {
      return this.cipherText;
   }

   public InputStream getCipherTextInternal(XMLDecryptContext var1) throws URIReferenceException {
      URIDereferencer var2 = var1.getURIDereferencer();
      Data var3 = var2.dereference(this.uriRef, var1);
      if (var3 instanceof OctetStreamData) {
         OctetStreamData var4 = (OctetStreamData)var3;
         return var4.getOctetStream();
      } else {
         throw new URIReferenceException("Unable to resolve reference to " + this.uriRef.getURI());
      }
   }

   public InputStream getCipherText() {
      return this.getCipherTextInternal();
   }

   public byte[] getCipherBytes() throws XMLEncryptionException {
      InputStream var1 = this.getCipherTextInternal();
      ByteVector var2 = new ByteVector();

      try {
         var2.addElements(var1);
      } catch (IOException var4) {
         throw new XMLEncryptionException(var4);
      }

      return var2.minSizedElementArray();
   }

   public void clear() {
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public List getTransforms() {
      return this.transforms;
   }

   public String getType() {
      return this.uriRef.getType();
   }

   public String getURI() {
      return this.uriRef.getURI();
   }
}
