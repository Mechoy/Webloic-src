package weblogic.xml.crypto.dsig;

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import weblogic.xml.crypto.NodeURIDereferencer;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.dom.DOMSignContext;
import weblogic.xml.crypto.api.dom.DOMValidateContext;
import weblogic.xml.crypto.dom.WLDOMSignContextImpl;
import weblogic.xml.crypto.dom.WLDOMValidateContextImpl;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignContext;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLValidateContext;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoImpl;
import weblogic.xml.crypto.encrypt.Utils;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.DebugStreamReader;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SignatureInfo;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.stax.util.NamespaceContextImpl;
import weblogic.xml.stream.XMLStreamException;

public class XMLSignatureImpl implements XMLSignature, SignatureInfo, Serializable {
   private static final long serialVersionUID = 1946531379486808596L;
   public static final String DEBUG_PROPERTY = "weblogic.xml.crypto.dsig.debug";
   public static final boolean DEBUG = Boolean.getBoolean("weblogic.xml.crypto.dsig.debug");
   public static final String SIGNATURE_ELEMENT = "Signature";
   public static final String SIGNATUREVALUE_ELEMENT = "SignatureValue";
   public static final String ID_ATTRIBUTE = "Id";
   private SignedInfo signedInfo;
   private transient KeyInfo keyInfo;
   private transient List objects;
   private transient String id;
   private transient String signatureValueId;
   private transient String unmarshalledSignatureValue;
   private transient String signatureValue;
   private transient Node signedInfoNode;
   private transient Element signatureNode;
   private transient NamespaceContextImpl namespaceContext;
   private transient Map namespaceMap;
   private SignatureValidateResult signatureValidateResult;

   XMLSignatureImpl(SignedInfo var1, KeyInfo var2) {
      this(var1, var2, (List)null, (String)null, (String)null);
   }

   XMLSignatureImpl(SignedInfo var1, KeyInfo var2, List var3, String var4, String var5) {
      this.namespaceMap = new HashMap();
      this.signedInfo = var1;
      this.keyInfo = var2;
      this.objects = var3;
      this.id = var4;
      this.signatureValueId = var5;
   }

   XMLSignatureImpl(XMLValidateContext var1) throws MarshalException {
      this.namespaceMap = new HashMap();
      this.unmarshal(var1);
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public String getId() {
      return this.id;
   }

   public KeyInfo getKeyInfo() {
      return this.keyInfo;
   }

   public List getObjects() {
      return this.objects != null ? Collections.unmodifiableList(this.objects) : Collections.EMPTY_LIST;
   }

   public byte[] getSignatureValue() {
      if (this.unmarshalledSignatureValue != null) {
         return Utils.base64(this.unmarshalledSignatureValue);
      } else {
         return this.signatureValue != null ? Utils.base64(this.signatureValue) : null;
      }
   }

   public String getSignatureValueId() {
      return this.signatureValueId;
   }

   public SignedInfo getSignedInfo() {
      return this.signedInfo;
   }

   public Element getSignatureNode() {
      return this.signatureNode;
   }

   public void sign(XMLSignContext var1) throws MarshalException, XMLSignatureException {
      if (var1 != null) {
         WLDOMSignContextImpl var2 = (WLDOMSignContextImpl)var1;
         var2.setXMLSignature(this);
         this.ensureURIDereferencer((DOMSignContext)var2);

         try {
            DOMStreamWriter var3 = this.createSignatureNode(var2);
            this.createSignedInfoNode(var3);
            Node var4 = var2.getNextSibling();
            if (var4 != null) {
               var2.getParent().insertBefore(this.signatureNode, var4);
            } else {
               var2.getParent().appendChild(this.signatureNode);
            }

            Iterator var8 = this.signedInfo.getReferences().iterator();

            while(var8.hasNext()) {
               ((ReferenceImpl)var8.next()).createDigest(var1);
            }

            var2.getParent().removeChild(this.signatureNode);
            var3 = this.createSignatureNode(var2);
            this.createSignedInfoNode(var3);
            var4 = var2.getNextSibling();
            if (var4 != null) {
               var2.getParent().insertBefore(this.signatureNode, var4);
            } else {
               var2.getParent().appendChild(this.signatureNode);
            }

            this.signedInfoNode = this.signatureNode.getFirstChild();
            CanonicalizationMethod var5 = this.signedInfo.getCanonicalizationMethod();
            if (var5 instanceof CanonicalizationMethodW3C) {
               ((CanonicalizationMethodW3C)var5).setupNonVisiblyUsed(this.signedInfoNode, this.namespaceMap, var1);
            }

            this.signatureValue = ((SignedInfoImpl)this.signedInfo).createSignature(var1, this.keyInfo, DOMUtils.getXMLInputStream(this.signedInfoNode), this.namespaceMap);
            this.createSignatureValueNode(var3);
            this.createKeyInfoNode(var3);
            this.createObjectNodes(var3);
         } catch (URIReferenceException var6) {
            throw new XMLSignatureException("Failed to resolve URI reference.", var6);
         } catch (XMLStreamException var7) {
            throw new XMLSignatureException("Failed to create signature.", var7);
         }
      } else {
         throw new NullPointerException("Context must not be null.");
      }
   }

   public boolean validate(XMLValidateContext var1) throws XMLSignatureException {
      if (this.unmarshalledSignatureValue == null) {
         try {
            this.unmarshal(var1);
         } catch (MarshalException var9) {
            throw new XMLSignatureException("Failed to unmarshal signature element.", var9);
         }
      }

      WLDOMValidateContextImpl var2 = (WLDOMValidateContextImpl)var1;
      var2.setSignatureNode(this.signatureNode);
      this.ensureURIDereferencer((DOMValidateContext)var2);
      boolean var3 = true;
      ArrayList var4 = new ArrayList();

      weblogic.xml.crypto.dsig.api.Reference.ValidateResult var7;
      for(Iterator var5 = this.signedInfo.getReferences().iterator(); var5.hasNext(); var4.add(var7)) {
         ReferenceImpl var6 = (ReferenceImpl)var5.next();
         var7 = var6.validate(var1);
         if (!var7.status()) {
            var3 = false;
         }
      }

      boolean var10 = true;

      try {
         SignedInfoImpl var11 = (SignedInfoImpl)this.signedInfo;
         var10 = var11.validateSignature(var1, this.keyInfo, this.unmarshalledSignatureValue, DOMUtils.getXMLInputStream(this.signedInfoNode), this.namespaceMap);
         this.signatureValidateResult = new SignatureValidateResult(var10, this.unmarshalledSignatureValue, var11.getKeySelectorResult(), var4);
      } catch (XMLStreamException var8) {
         throw new XMLSignatureException("Failed to canonicalize SignedInfo element.", var8);
      }

      LogUtils.logDsig((Object)this.signatureValidateResult);
      return var3 && var10;
   }

   private void unmarshal(XMLValidateContext var1) throws MarshalException {
      WLDOMValidateContextImpl var2 = (WLDOMValidateContextImpl)var1;
      this.signatureNode = (Element)var2.getNode();
      if (this.signatureNode.getNodeType() == 1 && this.signatureNode.getLocalName().equals("Signature") && this.signatureNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
         if (this.signatureNode.hasAttributeNS("http://www.w3.org/2000/09/xmldsig#", "Id")) {
            this.signatureValueId = this.signatureNode.getAttributeNS("http://www.w3.org/2000/09/xmldsig#", "Id");
         }

         this.fillNamespaceContext(this.signatureNode, (DOMSignContext)null);
         String var3 = this.namespaceContext.getPrefix("http://www.w3.org/2000/09/xmldsig#");
         Node var4 = this.signatureNode.getFirstChild();
         this.signedInfoNode = this.getNextSibling(var4, "http://www.w3.org/2000/09/xmldsig#", "SignedInfo", false);
         this.signedInfo = new SignedInfoImpl();
         ((WLXMLStructure)this.signedInfo).read(this.getXMLStreamReader(this.signedInfoNode));
         Element var5 = (Element)this.getNextSibling(this.signedInfoNode.getNextSibling(), "http://www.w3.org/2000/09/xmldsig#", "SignatureValue", false);
         var5.normalize();
         this.unmarshalledSignatureValue = var5.getFirstChild().getNodeValue();
         if (var5.hasAttributeNS("http://www.w3.org/2000/09/xmldsig#", "Id")) {
            this.signatureValueId = var5.getAttributeNS("http://www.w3.org/2000/09/xmldsig#", "Id");
         }

         Element var6 = (Element)this.getNextSibling(var5.getNextSibling(), "http://www.w3.org/2000/09/xmldsig#", "KeyInfo", true);
         if (var6 != null) {
            this.keyInfo = new KeyInfoImpl();
            ((KeyInfoImpl)this.keyInfo).read(this.getXMLStreamReader(var6));
         }

      } else {
         throw new MarshalException("Node on context is not a Signature node.");
      }
   }

   private Node getNextSibling(Node var1, String var2, String var3, boolean var4) throws MarshalException {
      if (var1 == null) {
         return null;
      } else {
         do {
            if (var1.getNodeType() != 8 && var1.getNodeType() != 3) {
               if (var1.getNodeType() == 1 && var1.getLocalName().equals(var3) && var1.getNamespaceURI().equals(var2)) {
                  return var1;
               }

               if (var4) {
                  return null;
               }

               throw new MarshalException("Failed to find expected node: " + var2 + ":" + var3 + ". " + "Found instead: " + var1.getNamespaceURI() + ":" + var1.getLocalName());
            }

            var1 = var1.getNextSibling();
         } while(var1 != null);

         return null;
      }
   }

   private XMLStreamReader getXMLStreamReader(Node var1) throws MarshalException {
      Object var2 = null;

      try {
         var2 = new DOMStreamReader(var1);
      } catch (javax.xml.stream.XMLStreamException var4) {
         throw new MarshalException("Failed to create XMLStreamReader from " + var1.getNodeName(), var4);
      }

      if (DEBUG) {
         var2 = new DebugStreamReader((XMLStreamReader)var2);
      }

      return (XMLStreamReader)var2;
   }

   private void createSignedInfoNode(DOMStreamWriter var1) throws MarshalException {
      ((SignedInfoImpl)this.signedInfo).write(var1);
   }

   private void createKeyInfoNode(DOMStreamWriter var1) throws MarshalException {
      if (this.keyInfo != null) {
         ((WLXMLStructure)this.keyInfo).write(var1);
      }

   }

   private void createObjectNodes(DOMStreamWriter var1) throws MarshalException {
      if (this.objects != null) {
         Iterator var2 = this.objects.iterator();

         while(var2.hasNext()) {
            ((WLXMLStructure)var2.next()).write(var1);
         }
      }

   }

   private DOMStreamWriter createSignatureNode(DOMSignContext var1) throws MarshalException {
      Node var2 = var1.getParent();
      if (var2 == null) {
         throw new MarshalException("Parent node in DOMSignContext is null.");
      } else {
         Document var3 = null;
         if (var2 instanceof Document) {
            var3 = (Document)var2;
         } else {
            var3 = var2.getOwnerDocument();
         }

         this.fillNamespaceContext(var2, var1);
         String var4 = this.namespaceContext.getPrefix("http://www.w3.org/2000/09/xmldsig#");
         this.signatureNode = null;
         if (var4 != null) {
            this.signatureNode = var3.createElementNS("http://www.w3.org/2000/09/xmldsig#", var4 + ":" + "Signature");
         } else {
            this.signatureNode = var3.createElementNS("http://www.w3.org/2000/09/xmldsig#", "dsig:Signature");
            this.signatureNode.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dsig", "http://www.w3.org/2000/09/xmldsig#");
            this.addToNamespaceContext("dsig", "http://www.w3.org/2000/09/xmldsig#");
         }

         if (this.id != null) {
            this.signatureNode.setAttributeNS("http://www.w3.org/2000/09/xmldsig#", var4 + ":" + "Id", this.id);
         }

         DOMStreamWriter var5 = new DOMStreamWriter(var3, this.signatureNode);

         try {
            var5.setNamespaceContext(this.namespaceContext);
            return var5;
         } catch (javax.xml.stream.XMLStreamException var7) {
            throw new MarshalException("Failed to set namespace context.", var7);
         }
      }
   }

   private void fillNamespaceContext(Node var1, DOMSignContext var2) {
      this.namespaceContext = new NamespaceContextImpl();
      this.namespaceMap.clear();

      ArrayList var3;
      for(var3 = new ArrayList(); var1 != null; var1 = var1.getParentNode()) {
         var3.add(var1);
      }

      for(int var4 = var3.size() - 1; var4 >= 0; --var4) {
         Node var5 = (Node)var3.get(var4);
         NamedNodeMap var6 = var5.getAttributes();
         if (var6 != null) {
            for(int var7 = 0; var7 < var6.getLength(); ++var7) {
               Attr var8 = (Attr)var6.item(var7);
               String var9 = var8.getNamespaceURI();
               if (var9 != null && var9.equals("http://www.w3.org/2000/xmlns/")) {
                  String var10 = var8.getLocalName();
                  if ("xmlns".equals(var10)) {
                     var10 = "";
                  }

                  String var11 = var8.getValue();
                  this.namespaceContext.bindNamespace(var10, var11);
                  this.namespaceMap.put(var10, var11);
                  if (var2 != null) {
                     var2.putNamespacePrefix(var10, var11);
                  }
               }
            }
         }
      }

   }

   private void addToNamespaceContext(String var1, String var2) {
      this.namespaceContext.bindNamespace(var1, var2);
      this.namespaceMap.put(var1, var2);
   }

   private void createSignatureValueNode(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
         if (this.signatureValueId != null) {
            var1.writeAttribute("Id", this.signatureValueId);
         }

         var1.writeCharacters(this.signatureValue);
         var1.writeEndElement();
      } catch (javax.xml.stream.XMLStreamException var3) {
         throw new MarshalException("Failed to write element SignatureValue", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.nextTag();
         this.id = var1.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Id");
         var1.nextTag();
      } catch (javax.xml.stream.XMLStreamException var3) {
         throw new MarshalException("Failed to read element ...", var3);
      }
   }

   public SignatureValidateResult getSignatureValidateResult() {
      return this.signatureValidateResult;
   }

   private void ensureURIDereferencer(DOMSignContext var1) {
      if (var1.getURIDereferencer() == null) {
         var1.setURIDereferencer(new NodeURIDereferencer(var1.getParent().getOwnerDocument()));
      }

   }

   private void ensureURIDereferencer(DOMValidateContext var1) {
      if (var1.getURIDereferencer() == null) {
         var1.setURIDereferencer(new NodeURIDereferencer(var1.getNode().getOwnerDocument()));
      }

   }

   public Key getKey() {
      return this.getSignatureValidateResult().getKeySelectorResult().getKey();
   }

   public List getReferences() {
      return this.signedInfo.getReferences();
   }

   public String getC14NMethod() {
      return this.signedInfo.getCanonicalizationMethod().getAlgorithm();
   }

   public String getSignatureMethod() {
      return this.signedInfo.getSignatureMethod().getAlgorithm();
   }

   public boolean containsNode(Node var1) {
      Iterator var2 = this.signedInfo.getReferences().iterator();

      SignatureInfo.Reference var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (SignatureInfo.Reference)var2.next();
      } while(!var3.containsNode(var1));

      return true;
   }
}
