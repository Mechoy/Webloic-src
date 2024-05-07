package weblogic.xml.crypto.wss;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.X509Data;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.dsig.keyinfo.X509DataImpl;
import weblogic.xml.crypto.dsig.keyinfo.X509IssuerSerialImpl;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.BinarySecurityTokenType;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.dom.marshal.MarshalException;

public class BinarySecurityTokenReference extends SecurityTokenReferenceImpl {
   public static final String VERBOSE_PROPERTY = "weblogic.xml.crypto.wss.verbose";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.wss.verbose");
   private X509Data x509Data = null;
   private X509IssuerSerial issuerSerial = null;
   private static final String DEFAULT_VALUE_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
   private boolean useIssuerSerial = false;

   public BinarySecurityTokenReference() {
      this.setValueType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
   }

   public BinarySecurityTokenReference(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      super(var1, var2, var3);
      if (VERBOSE) {
         LogUtils.logWss("STRType =" + var1.toString() + " Value Type =" + var2 + " useIssuerSerial =" + this.useIssuerSerial);
      }

      this.init(var1, var2, var3);
   }

   protected void init(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1)) {
         if (this.useIssuerSerial) {
            this.initIssuerSerial(var3);
         } else {
            try {
               this.initKeyIdentifier(var2, var3);
               LogUtils.logWss("Got Key Identifier STR OK for value type =" + var2 + " for token id =" + var3.getId() + " value type =" + var3.getValueType());
            } catch (WSSecurityException var5) {
               LogUtils.logWss("BinarySecurityTokenReference init error on KeyIdentifier, " + var5.getMessage() + " Try IssuerSerial");
               this.initIssuerSerial(var3);
            }
         }
      } else if (DsigConstants.X509ISSUER_SERIAL_QNAME.equals(var1)) {
         this.initIssuerSerial(var3);
      }

   }

   private void initKeyIdentifier(String var1, SecurityToken var2) throws WSSecurityException {
      BinarySecurityTokenType var3 = BinarySecurityTokenImpl.getBSTType(var1);
      if (null == var3) {
         LogUtils.logWss("BinarySecurityTokenReference initKeyIdentifier error, Invalid value type " + var1 + " for BST");
         throw new WSSecurityException("Invalid value type " + var1 + " for BST");
      } else {
         String var4 = var3.getKeyIdentifierValueType();
         KeyIdentifierImpl var5 = null;
         Object var6 = null;

         try {
            byte[] var9 = BinarySecurityTokenImpl.getBSTType(var1).getKeyIdRefValue(var2);
            if (var9 == null) {
               throw new WSSecurityException("Failed to create KeyIdentifier STR for BST, for value type =" + var1 + "  for token id =" + var2.getId() + " value type =" + var2.getValueType() + " and keyIdValueType = " + var4);
            }

            this.setValueType(var4);
            var5 = new KeyIdentifierImpl(var9);
         } catch (BSTEncodingException var8) {
            LogUtils.logWss("BinarySecurityTokenReference initKeyIdentifier error, error =" + var8.toString());
            var8.printStackTrace(System.err);
            throw new WSSecurityException(var8);
         }

         this.setKeyIdentifier(var5);
      }
   }

   private void initIssuerSerial(SecurityToken var1) throws WSSecurityException {
      try {
         BinarySecurityToken var2 = (BinarySecurityToken)var1;
         X509Certificate var3 = var2.getCertificate();
         KeyInfoFactory var4 = XMLSignatureFactory.getInstance().getKeyInfoFactory();
         String var5 = var3.getIssuerX500Principal().getName();
         BigInteger var6 = var3.getSerialNumber();
         this.issuerSerial = var4.newX509IssuerSerial(var5, var6);
         ArrayList var7 = new ArrayList();
         var7.add(this.issuerSerial);
         this.x509Data = var4.newX509Data(Collections.unmodifiableList(var7));
         this.setValueType(var1.getValueType());
         this.setSTRType(DsigConstants.X509ISSUER_SERIAL_QNAME);
      } catch (XMLSignatureException var8) {
         throw new WSSecurityException(var8);
      }
   }

   static void init() {
      register(new BinarySecurityTokenHandler());
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void unmarshal(Node var1) throws MarshalException {
      Element var2 = DOMUtils.getLastElement(var1);

      try {
         DOMStreamReader var3 = new DOMStreamReader(var2);
         if (DOMUtils.is(var2, DsigConstants.X509DATA_QNAME)) {
            super.unmarshalInternal(var1);
            this.x509Data = (X509DataImpl)X509DataImpl.readKeyInfoObject(var3);
            Object var4 = this.x509Data.getContent().get(0);
            if (!(var4 instanceof X509IssuerSerial)) {
               throw new MarshalException("Invalid content in SecurityTokenReferrence/X509Data.");
            }

            this.issuerSerial = (X509IssuerSerial)var4;
         } else if (DOMUtils.is(var2, DsigConstants.X509ISSUER_SERIAL_QNAME)) {
            super.unmarshalInternal(var1);
            X509IssuerSerialImpl var7 = new X509IssuerSerialImpl();
            var7.read(var3, false);
            this.issuerSerial = var7;
         } else {
            super.unmarshal(var1);
            if (this.getValueType() == null) {
               this.setValueType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
            }
         }

      } catch (XMLStreamException var5) {
         throw new MarshalException("Failed to unmarshal SecurityTokenReference child " + DOMUtils.getQName(var2) + ", " + var5.getMessage(), var5);
      } catch (weblogic.xml.crypto.api.MarshalException var6) {
         throw new MarshalException("Failed to unmarshal SecurityTokenReference child " + DOMUtils.getQName(var2) + ", " + var6.getMessage(), var6);
      }
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      if (this.getSTRType().equals(DsigConstants.X509ISSUER_SERIAL_QNAME)) {
         try {
            this.marshalIssuerSerialRef(var1, var2, var3);
         } catch (weblogic.xml.crypto.api.MarshalException var5) {
            throw new MarshalException(var5);
         }
      }

      super.marshal(var1, var2, var3);
   }

   private void marshalIssuerSerialRef(Element var1, Node var2, Map var3) throws weblogic.xml.crypto.api.MarshalException {
      String var4 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
      String var5 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      Element var6 = DOMUtils.createElement(var1, WSSConstants.STR_QNAME, var4);
      if (this.getId() != null) {
         DOMUtils.addPrefixedAttribute(var6, WSSConstants.WSU_ID_QNAME, var5, this.getId());
      }

      if (var2 != null) {
         var1.insertBefore(var6, var2);
      } else {
         var1.appendChild(var6);
      }

      DOMStreamWriter var7 = new DOMStreamWriter(var6.getOwnerDocument(), var6);
      ((WLXMLStructure)this.x509Data).write(var7);
   }

   public void unmarshalEmbeddedRef(Element var1) throws MarshalException {
      BinarySecurityTokenImpl var2 = new BinarySecurityTokenImpl();
      var2.unmarshal(var1.getFirstChild());
   }

   public String toString() {
      return super.toString() + " URI: " + this.getId();
   }

   public X509IssuerSerial getIssuerSerial() {
      return this.issuerSerial;
   }
}
