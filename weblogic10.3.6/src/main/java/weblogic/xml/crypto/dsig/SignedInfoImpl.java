package weblogic.xml.crypto.dsig;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorException;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;

public class SignedInfoImpl implements SignedInfo, XMLStructure, WLXMLStructure {
   public static final String SIGNEDINFO_ELEMENT = "SignedInfo";
   private CanonicalizationMethod c14nMethod;
   private SignatureMethod signatureMethod;
   private List refs;
   private transient String id;
   private KeySelectorResult ksr;

   SignedInfoImpl(CanonicalizationMethod var1, SignatureMethod var2, List var3) {
      this(var1, var2, var3, (String)null);
   }

   SignedInfoImpl(CanonicalizationMethod var1, SignatureMethod var2, List var3, String var4) {
      if (var1 != null && var2 != null && var3 != null) {
         this.c14nMethod = var1;
         this.signatureMethod = var2;
         this.refs = var3;
         this.id = var4;
      } else {
         throw new NullPointerException("CanonicalizationMethod, SignatuareMethod, and List references must not be null.");
      }
   }

   public SignedInfoImpl() {
      this.refs = new ArrayList();
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public CanonicalizationMethod getCanonicalizationMethod() {
      return this.c14nMethod;
   }

   public String getId() {
      return this.id;
   }

   public List getReferences() {
      return Collections.unmodifiableList(this.refs);
   }

   public SignatureMethod getSignatureMethod() {
      return this.signatureMethod;
   }

   public void addReference(Reference var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Reference is null");
      } else {
         this.refs.add(var1);
      }
   }

   protected String createSignature(XMLCryptoContext var1, KeyInfo var2, XMLInputStream var3, Map var4) throws XMLSignatureException {
      byte[] var5 = this.c14n(var3, var4);
      Key var6 = null;

      try {
         var6 = this.getSignKey(var1, var2);
      } catch (KeySelectorException var8) {
         throw new XMLSignatureException("Failed to get key for signing.", var8);
      }

      return ((WLSignatureMethod)this.signatureMethod).sign(var6, var5);
   }

   boolean validateSignature(XMLCryptoContext var1, KeyInfo var2, String var3, XMLInputStream var4, Map var5) throws XMLSignatureException {
      byte[] var6 = this.c14n(var4, var5);
      Key var7 = null;

      try {
         var7 = this.getVerifyKey(var1, var2);
      } catch (KeySelectorException var9) {
         throw new XMLSignatureException("Failed to get key for validating.", var9);
      }

      return ((WLSignatureMethod)this.signatureMethod).verify(var7, var6, var3);
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
         if (this.id != null) {
            var1.writeAttribute("Id", this.id);
         }

         ((WLXMLStructure)this.c14nMethod).write(var1);
         ((WLXMLStructure)this.signatureMethod).write(var1);
         if (this.refs != null) {
            Iterator var2 = this.refs.iterator();

            while(var2.hasNext()) {
               ((WLXMLStructure)var2.next()).write(var1);
            }
         }

         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element SignedInfo", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
         this.id = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Id", var1);
         var1.nextTag();
         String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var1);
         this.c14nMethod = CanonicalizationMethodImpl.newCanonicalizationMethod(var2);
         ((WLXMLStructure)this.c14nMethod).read(var1);
         var1.nextTag();
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
         String var3 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var1);
         this.signatureMethod = SignatureMethodImpl.newSignatureMethod(var3);
         ((WLXMLStructure)this.signatureMethod).read(var1);
         var1.nextTag();
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "Reference");

         while("http://www.w3.org/2000/09/xmldsig#".equals(var1.getNamespaceURI()) && "Reference".equals(var1.getLocalName())) {
            this.readReference(var1);
            var1.nextTag();
         }

         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "SignedInfo", var1);
      } catch (XMLStreamException var4) {
         throw new MarshalException("Failed to read SignedInfoelement.", var4);
      } catch (NoSuchAlgorithmException var5) {
         throw new MarshalException("Failed to instantiate object for child of SignedInfo element.", var5);
      }
   }

   private void readReference(XMLStreamReader var1) throws MarshalException {
      ReferenceImpl var2 = new ReferenceImpl();
      var2.read(var1);
      this.refs.add(var2);
   }

   private byte[] c14n(XMLInputStream var1, Map var2) throws XMLSignatureException {
      UnsyncByteArrayOutputStream var3 = new UnsyncByteArrayOutputStream();
      XMLOutputStream var4 = ((WLCanonicalizationMethod)this.c14nMethod).canonicalize(var3, var2);

      try {
         var4.add(var1);
         var4.close(true);
      } catch (weblogic.xml.stream.XMLStreamException var7) {
         throw new XMLSignatureException("canonicalization error", var7);
      }

      final byte[] var5 = var3.toByteArray();
      LogUtils.logDsig(new LogUtils.LogMethod() {
         public String log() {
            return "Canonicalized SignedInfo element: " + new String(var5);
         }
      });
      return var5;
   }

   private Key getSignKey(XMLCryptoContext var1, KeyInfo var2) throws KeySelectorException {
      KeySelectorResult var3 = var1.getKeySelector().select(var2, KeySelector.Purpose.SIGN, this.signatureMethod, var1);
      return var3.getKey();
   }

   private Key getVerifyKey(XMLCryptoContext var1, KeyInfo var2) throws KeySelectorException {
      this.ksr = var1.getKeySelector().select(var2, KeySelector.Purpose.VERIFY, this.signatureMethod, var1);
      return this.ksr.getKey();
   }

   public KeySelectorResult getKeySelectorResult() {
      return this.ksr;
   }
}
