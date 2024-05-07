package weblogic.xml.crypto.dsig;

import com.bea.xbean.util.XsTypeConverter;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import weblogic.utils.Hex;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLValidateContext;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.crypto.wss.SignatureInfo;
import weblogic.xml.security.utils.Utils;

public class ReferenceImpl implements XMLStructure, Reference, WLXMLStructure, SignatureInfo.Reference, Serializable {
   private static final long serialVersionUID = 1543097730829042873L;
   public static final String REFERENCE_ELEMENT = "Reference";
   private static final String URI_ATTRIBUTE = "URI";
   private static final String TYPE_ATTRIBUTE = "Type";
   private static final String DIGESTVALUE_ELEMENT = "DigestValue";
   private static final String TRANSFORMS_ELEMENT = "Transforms";
   private DigestMethod digestMethod;
   private transient String id;
   private List transforms;
   private List transformURIs;
   private transient List appliedTransforms;
   private String uri;
   private transient String type;
   private transient Reference.DigestValue digestValue;
   private transient byte[] unmarshalledDigest;
   private transient Data derefData;

   public ReferenceImpl() {
      this.appliedTransforms = new ArrayList();
      this.transforms = new ArrayList();
   }

   public ReferenceImpl(String var1, DigestMethod var2) {
      this(var1, var2, (List)null, (String)null, (String)null);
   }

   public ReferenceImpl(String var1, DigestMethod var2, List var3, String var4, String var5) {
      this.appliedTransforms = new ArrayList();
      this.uri = var1;
      this.digestMethod = var2;
      this.transforms = ReferenceUtils.getAppliedTransforms(ReferenceUtils.getTransforms(var3));
      this.fillAppliedTransforms();
      this.type = var4;
      this.id = var5;
   }

   public DigestMethod getDigestMethod() {
      return this.digestMethod;
   }

   public String getId() {
      return this.id;
   }

   public List getTransforms() {
      return Collections.unmodifiableList(this.transforms);
   }

   public Reference.DigestValue getDigestValue() {
      return this.digestValue;
   }

   public Reference.ValidateResult validate(XMLValidateContext var1) throws XMLSignatureException {
      try {
         this.createDigest(var1);
      } catch (URIReferenceException var3) {
         throw new XMLSignatureException("Failed to resolve reference.", var3);
      }

      boolean var2 = Arrays.equals(this.digestValue.getDigest(), this.unmarshalledDigest);
      return new ValidateResultImpl(var2, this.digestValue, this.unmarshalledDigest, this.uri);
   }

   public String getURI() {
      return this.uri;
   }

   public void setUri(String var1) {
      this.uri = var1;
   }

   public String getType() {
      return this.type;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   private String printB64(byte[] var1) {
      CharSequence var2 = XsTypeConverter.printHexBinary(var1);
      return var2.toString();
   }

   protected void createDigest(XMLCryptoContext var1) throws URIReferenceException, XMLSignatureException {
      LogUtils.logDsig("creating digest for reference with uri: " + this.uri + " and algorithm: " + this.digestMethod.getAlgorithm());
      this.derefData = ReferenceUtils.dereference(this, var1);
      Data var2 = ReferenceUtils.applyTransforms(this.derefData, this.appliedTransforms, var1);
      Object var3 = null;

      final byte[] var6;
      try {
         var6 = DataUtils.getBytes(var2);
         LogUtils.logDsig(new LogUtils.LogMethod() {
            public String log() {
               return "digest input: " + ReferenceImpl.this.getString(var6);
            }
         });
      } catch (IOException var5) {
         throw new XMLSignatureException("Failed to convert transform result to digest input.", var5);
      }

      final byte[] var4 = ((WLDigestMethod)this.digestMethod).digest(var6);
      LogUtils.logDsig(new LogUtils.LogMethod() {
         public String log() {
            return "digest value: " + ReferenceImpl.this.printB64(var4);
         }
      });
      this.digestValue = new DigestValueImpl(this.derefData, var6, var4);
      if (this.unmarshalledDigest == null) {
         this.unmarshalledDigest = var4;
      }

   }

   private void fillAppliedTransforms() {
      this.appliedTransforms = ReferenceUtils.getAppliedTransforms(this.transforms);
   }

   private static void addC14NTransform(List var0) {
      try {
         var0.add(TransformImpl.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315"));
      } catch (NoSuchAlgorithmException var2) {
      } catch (InvalidAlgorithmParameterException var3) {
      }

   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "Reference");
         if (this.id != null) {
            var1.writeAttribute("Id", this.id);
         }

         if (this.uri != null) {
            var1.writeAttribute("URI", this.uri);
         }

         if (this.type != null) {
            var1.writeAttribute("Type", this.type);
         }

         if (!this.transforms.isEmpty()) {
            this.writeTransforms(var1);
         }

         ((WLXMLStructure)this.digestMethod).write(var1);
         if (this.digestValue != null) {
            this.writeDigestValue(var1);
         }

         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element Reference", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         this.id = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Id", var1);
         this.uri = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "URI", var1);
         this.type = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Type", var1);
         var1.nextTag();
         this.readTransforms(var1);
         this.digestMethod = DigestMethodImpl.newDigestMethod(var1);
         this.readDigestValue(var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read Reference element.", var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new MarshalException("Failed to instantiate object for child of Reference element.", var4);
      }
   }

   private void readDigestValue(XMLStreamReader var1) throws XMLStreamException {
      var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "DigestValue");
      final String var2 = var1.getElementText();
      this.unmarshalledDigest = Utils.base64(var2);
      LogUtils.logDsig(new LogUtils.LogMethod() {
         public String log() {
            return "unmarshalled digest value: " + var2 + "\nbase 64 encoded: " + ReferenceImpl.this.printB64(ReferenceImpl.this.unmarshalledDigest);
         }
      });
      StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "Reference", var1);
   }

   private void readTransforms(XMLStreamReader var1) throws XMLStreamException, MarshalException {
      if ("http://www.w3.org/2000/09/xmldsig#".equals(var1.getNamespaceURI()) && "Transforms".equals(var1.getLocalName())) {
         var1.nextTag();

         while("http://www.w3.org/2000/09/xmldsig#".equals(var1.getNamespaceURI()) && "Transform".equals(var1.getLocalName())) {
            this.transforms.add(TransformImpl.readTransform(var1));
            var1.nextTag();
         }

         this.fillAppliedTransforms();
         StaxUtils.findEnd(var1, "http://www.w3.org/2000/09/xmldsig#", "Transforms");
         var1.nextTag();
      }

   }

   private void writeTransforms(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "Transforms");
         Iterator var2 = this.transforms.iterator();

         while(var2.hasNext()) {
            ((WLXMLStructure)var2.next()).write(var1);
         }

         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element Reference", var3);
      }
   }

   private void writeDigestValue(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
         var1.writeCharacters(Utils.base64(this.digestValue.getDigest()));
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element DigestValue", var3);
      }
   }

   private String getString(byte[] var1) {
      try {
         return "(as string, platform default encoding) " + new String(var1);
      } catch (Exception var3) {
         return "(as hex) " + Hex.asHex(var1, var1.length);
      }
   }

   public List getTransformURIs() {
      if (this.transformURIs == null) {
         this.transformURIs = new ArrayList();
         Iterator var1 = this.transforms.iterator();

         while(var1.hasNext()) {
            Transform var2 = (Transform)var1.next();
            this.transformURIs.add(var2.getAlgorithm());
         }
      }

      return this.transformURIs;
   }

   public String getDigestURI() {
      return this.digestMethod.getAlgorithm();
   }

   public boolean containsNode(Node var1) {
      return this.derefData instanceof NodeSetData ? ((NodeSetData)this.derefData).contains(var1) : false;
   }

   public class ValidateResultImpl implements Reference.ValidateResult {
      private boolean status;
      private Reference.DigestValue digestValue;
      private String refURI;
      private byte[] unmarshalledDigestValue;

      protected ValidateResultImpl(boolean var2, Reference.DigestValue var3) {
         this(var2, var3, (byte[])null, (String)null);
      }

      protected ValidateResultImpl(boolean var2, Reference.DigestValue var3, byte[] var4, String var5) {
         this.status = var2;
         this.digestValue = var3;
         this.refURI = var5;
         this.unmarshalledDigestValue = var4;
      }

      public boolean status() {
         return this.status;
      }

      public Reference.DigestValue getDigestValue() {
         return this.digestValue;
      }

      public String getReferenceURI() {
         return this.refURI;
      }

      public String toString() {
         return "[ReferenceImpl.ValidateResultImpl:\n[refURI: " + this.refURI + "]" + "\n[status: " + this.status + "]" + "\n[digestValue: " + ReferenceImpl.this.printB64(this.digestValue.getDigest()) + "]" + "\n[unmarshalledDigestValue: " + ReferenceImpl.this.printB64(this.unmarshalledDigestValue) + "]]";
      }
   }

   public class DigestValueImpl implements Reference.DigestValue {
      private byte[] digest;
      private byte[] digestInput;
      private Data dereferencedData;

      protected DigestValueImpl(Data var2, byte[] var3, byte[] var4) {
         this.digestInput = var3;
         this.digest = var4;
         this.dereferencedData = var2;
      }

      public boolean isFeatureSupported(String var1) {
         return false;
      }

      public byte[] getDigest() {
         return (byte[])((byte[])this.digest.clone());
      }

      protected void setDigest(byte[] var1) {
         this.digest = var1;
      }

      public byte[] getDigestInput() {
         return (byte[])((byte[])this.digestInput.clone());
      }

      protected void setDigestInput(byte[] var1) {
         this.digestInput = var1;
      }

      public Data getDereferencedData() {
         return this.dereferencedData;
      }

      protected void setDereferencedData(Data var1) {
         this.dereferencedData = var1;
      }
   }
}
