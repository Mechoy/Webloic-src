package weblogic.wsee.security.wss.plan.helper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.security.saml.SAML2Constants;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;
import weblogic.xml.dom.DOMProcessingException;

public class SOAPSecurityHeaderHelper {
   private static final boolean verbose = Verbose.isVerbose(SOAPSecurityHeaderHelper.class);
   private static final boolean debug = false;
   private boolean hasSorted = false;
   private List<Element> signatureElements = null;
   private List<Element> encryptionElements = null;
   private List<Element> refListElements = null;
   private List<Element> otherElements = null;
   private List<Element> allElements = null;
   private Element securityHeader = null;
   private SOAPMessage soap;

   public SOAPSecurityHeaderHelper(SOAPMessage var1) throws WSSecurityException {
      this.soap = var1;
      this.securityHeader = getSecurityElement(var1);
   }

   public SOAPSecurityHeaderHelper(SOAPMessageContext var1) throws WSSecurityException {
      this.soap = var1.getMessage();
      this.securityHeader = getSecurityElement(var1);
   }

   public static Element getSecurityElement(SOAPMessageContext var0) throws WSSecurityException {
      Element var1 = null;
      WSSecurityContext var2 = WSSecurityContext.getSecurityContext(var0);
      if (var2 != null) {
         var1 = var2.getSecurityElement();
      }

      return var1 == null ? getSecurityElement(var0.getMessage()) : var1;
   }

   public static Element getSecurityElement(SOAPMessage var0) throws WSSecurityException {
      try {
         if (null == var0.getSOAPHeader()) {
            return null;
         } else {
            NodeList var1 = var0.getSOAPHeader().getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
            return var1.getLength() > 0 ? (Element)var1.item(0) : null;
         }
      } catch (SOAPException var2) {
         throw new WSSecurityException(var2.getMessage(), var2);
      }
   }

   public static List getNonSecurityElements(SOAPMessage var0, QNameExpr var1) throws WSSecurityException {
      return getNonSecurityElements(var0, var1, false);
   }

   public static List getNonSecurityElements(SOAPMessage var0, QNameExpr var1, boolean var2) throws WSSecurityException {
      try {
         SOAPHeader var3 = var0.getSOAPHeader();
         if (null == var3) {
            return null;
         } else {
            ArrayList var4 = new ArrayList();
            String var5 = null;
            QName var6 = null;
            if (null != var1) {
               if (var1.getLocalName() != null && var1.getLocalName().length() > 0) {
                  var6 = new QName(var1.getNamespaceUri(), var1.getLocalName());
               } else {
                  var5 = var1.getNamespaceUri();
               }
            }

            NodeList var7 = var3.getChildNodes();
            if (null != var7 && var7.getLength() != 0) {
               for(int var8 = 0; var8 < var7.getLength(); ++var8) {
                  Object var9 = var7.item(var8);
                  if (var9 != null && ((Node)var9).getNodeType() == 1 && !DOMUtils.is((Node)var9, WSSConstants.SECURITY_QNAME)) {
                     if (var2 && DOMUtils.is((Node)var9, WSS11Constants.ENC_HEADER_QNAME)) {
                        var9 = DOMUtils.getFirstElement((Node)var9);
                     }

                     if (var6 != null) {
                        if (DOMUtils.is((Node)var9, var6)) {
                           var4.add(var9);
                        }
                     } else if (var5 != null) {
                        if (var5.equals(((Node)var9).getNamespaceURI())) {
                           var4.add(var9);
                        }
                     } else {
                        var4.add(var9);
                     }
                  }
               }

               return var4;
            } else {
               return null;
            }
         }
      } catch (SOAPException var10) {
         throw new WSSecurityException(var10.getMessage(), var10);
      }
   }

   public Element getSecurityHeaderElement() {
      return this.securityHeader;
   }

   public Element getScToken13Element() {
      return this.getSecurityElement(WSCConstants.SCT_QNAME);
   }

   public Element getScTokenElement() {
      return this.getSecurityElement(weblogic.wsee.security.wssc.v200502.WSCConstants.SCT_QNAME);
   }

   public Element getSamlTokenElement() {
      return this.getSecurityElement(SAMLConstants.SAML_ASST_QNAME);
   }

   public Element getSaml2TokenElement() {
      return this.getSecurityElement(SAML2Constants.SAML2_ASST_QNAME);
   }

   public Element getSaml11Or20TokenElement() {
      return null != this.getSamlTokenElement() ? this.getSamlTokenElement() : this.getSaml2TokenElement();
   }

   public Element getUsernameTokenElement() {
      return this.getSecurityElement(WSSConstants.UNT_QNAME);
   }

   public Element getTimestampElement() {
      return this.getSecurityElement(WSSConstants.TIMESTAMP_QNAME);
   }

   public Element getSignatrueElement() {
      return null == this.securityHeader ? null : getFirstChildElement(this.securityHeader.getFirstChild(), DsigConstants.SIGNATURE_QNAME);
   }

   public List getSignatrueConfirmationElements() {
      if (null == this.securityHeader) {
         return null;
      } else {
         Element var1 = DOMUtils.getFirstElement(this.securityHeader);

         ArrayList var2;
         for(var2 = new ArrayList(1); var1 != null; var1 = DOMUtils.getNextElement(var1)) {
            if (DOMUtils.is(var1, WSS11Constants.SIG_CONF_QNAME)) {
               var2.add(var1);
            }
         }

         if (var2.size() == 0) {
            return null;
         } else {
            return var2;
         }
      }
   }

   public Element getReferenceListElement() {
      return this.getSecurityElement(SecurityImpl.REFERENCE_LIST_QNAME);
   }

   public List getDerivedKeyTokenElements() {
      if (null == this.securityHeader) {
         return null;
      } else {
         Element var1 = DOMUtils.getFirstElement(this.securityHeader);

         ArrayList var2;
         for(var2 = new ArrayList(1); var1 != null; var1 = DOMUtils.getNextElement(var1)) {
            if ("DerivedKeyToken".equals(var1.getLocalName())) {
               var2.add(var1);
            }
         }

         if (var2.size() == 0) {
            return null;
         } else {
            return var2;
         }
      }
   }

   public List<Element> getAllElements() {
      this.sortItOut();
      return this.allElements;
   }

   public List<Element> getSignatrueElements() {
      this.sortItOut();
      return this.signatureElements;
   }

   public List<Element> getEncryptionElements() {
      this.sortItOut();
      return this.encryptionElements;
   }

   public List<Element> getRefListElements() {
      this.sortItOut();
      return this.refListElements;
   }

   public List<Element> getOtherElements() {
      this.sortItOut();
      return this.otherElements;
   }

   private void sortItOut() {
      if (!this.hasSorted) {
         this.hasSorted = true;
         if (null != this.securityHeader) {
            this.signatureElements = new ArrayList();
            this.encryptionElements = new ArrayList();
            this.refListElements = new ArrayList();
            this.otherElements = new ArrayList();
            this.allElements = new ArrayList();

            for(Element var1 = DOMUtils.getFirstElement(this.securityHeader); var1 != null; var1 = DOMUtils.getNextElement(var1)) {
               this.allElements.add(var1);
               if (DOMUtils.is(var1, DsigConstants.SIGNATURE_QNAME)) {
                  this.signatureElements.add(var1);
               } else if (DOMUtils.is(var1, SecurityImpl.ENCRYPTED_KEY_QNAME)) {
                  this.encryptionElements.add(var1);
               } else if (DOMUtils.is(var1, SecurityImpl.REFERENCE_LIST_QNAME)) {
                  this.refListElements.add(var1);
               } else {
                  this.otherElements.add(var1);
               }
            }

         }
      }
   }

   public Element getSecurityElement(QName var1) {
      return null == this.securityHeader ? null : weblogic.xml.dom.DOMUtils.getFirstElement(this.securityHeader, var1);
   }

   public static Element getFirstChildElement(Node var0, QName var1) {
      if (var0 == null) {
         return null;
      } else if (var0.getNodeType() == 1 && weblogic.xml.dom.DOMUtils.equalsQName(var0, var1)) {
         return (Element)var0;
      } else {
         for(Node var2 = var0.getNextSibling(); var2 != null; var2 = var2.getNextSibling()) {
            if (var2.getNodeType() == 1 && weblogic.xml.dom.DOMUtils.equalsQName(var2, var1)) {
               return (Element)var2;
            }
         }

         for(Node var3 = var0.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            Element var4 = getFirstChildElement(var3, var1);
            if (var4 != null) {
               return var4;
            }
         }

         return null;
      }
   }

   public Element getDummyElement(String var1) throws DOMProcessingException {
      weblogic.xml.dom.DOMUtils.addEmptyElement(this.securityHeader, var1);
      return weblogic.xml.dom.DOMUtils.getElementByTagName(this.securityHeader, var1);
   }

   public void removeDummyElement(Node var1) throws DOMProcessingException {
      this.securityHeader.removeChild(var1);
   }

   public void dumpSoapMessage(String var1) {
      dumpSoapMessage(this.soap, var1, false);
   }

   public static void dumpSoapMessage(SOAPMessageContext var0, String var1) {
      dumpSoapMessage(var0.getMessage(), var1, false);
   }

   public static void dumpSoapMessage(SOAPMessage var0, String var1) {
      dumpSoapMessage(var0, var1, false);
   }

   public static void dumpSoapMessage(SOAPMessageContext var0, String var1, boolean var2) {
      dumpSoapMessage(var0.getMessage(), var1, var2);
   }

   public static void dumpSoapMessage(SOAPMessage var0, String var1, boolean var2) {
      if (null == var1) {
         var1 = "SoapMessageDump";
      }

      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         var0.writeTo(var3);
         var3.flush();
         var3.close();
         if (var2) {
            System.out.println(var1 + " MSG: " + var3);
         }

         if (null != var1) {
            File var4 = File.createTempFile(var1, ".xml");
            BufferedOutputStream var5 = new BufferedOutputStream(new FileOutputStream(var4));
            var0.writeTo(var5);
            var5.flush();
            var5.close();
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
