package weblogic.wsee.security.saml;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.KeyIdentifierImpl;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;
import weblogic.xml.dom.marshal.MarshalException;

public class SAMLSecurityTokenReference extends SecurityTokenReferenceImpl {
   private static final boolean verbose = Verbose.isVerbose(SAMLSecurityTokenReference.class);
   private static final boolean debug = false;
   private boolean isDirectReference = false;
   private String tokenType;

   public SAMLSecurityTokenReference() {
   }

   public SAMLSecurityTokenReference(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      super(var1, var2, var3);
      SAMLToken var4 = (SAMLToken)var3;
      if (var4.isSaml2()) {
         this.tokenType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
         this.setValueType("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID");
      } else if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1".equals(var2)) {
         this.tokenType = var2;
         this.setValueType("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID");
      } else if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID".equals(var2)) {
         this.tokenType = var2;
         this.setValueType(var2);
      } else {
         this.tokenType = "http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID";
         this.setValueType("http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID");
      }

      if (WSSConstants.REFERENCE_QNAME.equals(var1)) {
         this.setDirectReference(true);
         this.setReferenceURI("#" + var3.getId());
      } else {
         KeyIdentifierImpl var5 = new KeyIdentifierImpl(var4.getAssertionID().getBytes());
         this.setKeyIdentifier(var5);
      }

   }

   private boolean isDirectReference() {
      return this.isDirectReference;
   }

   private void setDirectReference(boolean var1) {
      this.isDirectReference = var1;
   }

   public String getValueType() {
      String var1 = super.getValueType();
      if (this.isDirectReference()) {
         var1 = this.tokenType;
      }

      return var1;
   }

   public Node marshalDirectRef(Element var1, Node var2, Map var3, String var4) {
      Element var5 = this.marshalInternal(var1, var2, var3);
      Element var6 = (Element)var5.getParentNode();
      DOMUtils.addAttribute(var6, WSS11Constants.TOKEN_TYPE_QNAME, var3, this.tokenType);
      DOMUtils.addAttribute(var5, WSSConstants.URI_QNAME, var3, var4);
      return var6;
   }

   public void unmarshalDirectRef(Element var1) {
      this.setReferenceURI(DOMUtils.getAttributeValue(var1, WSSConstants.URI_QNAME));
      this.setValueType(DOMUtils.getAttributeValue(var1, WSSConstants.VALUE_TYPE_QNAME));
      Element var2 = (Element)var1.getParentNode();
      this.tokenType = DOMUtils.getAttributeValue(var2, WSS11Constants.TOKEN_TYPE_QNAME);
      if (this.getValueType() == null && this.tokenType != null && "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(this.tokenType)) {
         this.setValueType("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID");
      }

      this.setDirectReference(true);
   }

   public Node marshalKeyIdRef(Element var1, Node var2, Map var3, KeyIdentifier var4) throws MarshalException {
      Element var5 = this.marshalInternal(var1, var2, var3);
      Element var6 = (Element)var5.getParentNode();
      DOMUtils.addAttribute(var5, WSSConstants.VALUE_TYPE_QNAME, var3, this.getValueType());
      DOMUtils.addText(var5, new String(var4.getIdentifier()));
      return var6;
   }

   public void unmarshalKeyIdRef(Element var1) throws MarshalException {
      this.tokenType = DOMUtils.getAttributeValue((Element)var1.getParentNode(), WSS11Constants.TOKEN_TYPE_QNAME);
      String var2 = DOMUtils.getAttributeValue(var1, WSSConstants.ENCODING_TYPE_QNAME);
      this.setValueType(DOMUtils.getAttributeValue(var1, WSSConstants.VALUE_TYPE_QNAME));
      this.setKeyIdentifier(new KeyIdentifierImpl(DOMUtils.getText(var1).trim().getBytes(), var2));
   }
}
