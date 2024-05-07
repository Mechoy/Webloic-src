package weblogic.wsee.security.saml;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import weblogic.security.utils.SAMLAssertionInfo;

public class SAMLAssertionInfoImpl implements SAMLAssertionInfo {
   private static final String SAML_ASSERTION_TAG = "Assertion";
   private static final String SAML11_ID_ATTR = "AssertionID";
   private static final String SAML11_VERSION = "1.1";
   private static final String SAML20_ID_ATTR = "ID";
   private static final String SAML20_VERSION = "2.0";
   private static final String SAML_ISSUER_ATTR = "Issuer";
   private static final String SAML_CONDITIONS_TAG = "Conditions";
   private static final String SAML_NOTBEFORE_ATTR = "NotBefore";
   private static final String SAML_NOTONORAFTER_ATTR = "NotOnOrAfter";
   private static final String SAML_SUBJECT_TAG = "Subject";
   private static final String SAML_ATN_STATEMENT_TAG = "AuthenticationStatement";
   private static final String SAML_ATTRIBUTE_STATEMENT_TAG = "AttributeStatement";
   private static final String SAML11_NAMEID_TAG = "NameIdentifier";
   private static final String SAML20_NAMEID_TAG = "NameID";
   private static final String SAML_SUBJCONF_TAG = "SubjectConfirmation";
   private static final String SAML20_SUBJCONF_METHOD_ATTR = "Method";
   private static final String SAML11_SUBJCONF_METHOD_TAG = "ConfirmationMethod";
   private static final String SAML_KEYINFO_TAG = "KeyInfo";
   private static final String SAML_SUBJCONF_DATA_TAG = "SubjectConfirmationData";
   private static final String SAML_SIGNATURE_TAG = "Signature";
   Element _assertion = null;
   String _version = null;
   boolean _isVersion20Assertion = true;
   String _id = null;
   String _issuer = null;
   String _subject = null;
   String _confirmation = null;
   Date _notBefore = null;
   Date _notOnOrAfter = null;
   Element _keyInfo = null;
   Element _issuerKeyInfo = null;

   SAMLAssertionInfoImpl(Element var1) {
      this.init(var1);
   }

   SAMLAssertionInfoImpl(String var1) {
      try {
         Element var2 = this.parseAssertion(var1);
         this.init(var2);
      } catch (Exception var3) {
         throw new IllegalArgumentException("Invalid assertion xml: failed to parse", var3);
      }
   }

   private Element parseAssertion(String var1) throws SAXException, IOException {
      DocumentBuilder var2 = null;

      Element var4;
      try {
         var2 = CSSUtils.getParser();
         Document var3 = var2.parse(var1);
         var4 = var3.getDocumentElement();
      } finally {
         if (var2 != null) {
            CSSUtils.returnParser(var2);
         }

      }

      return var4;
   }

   private void init(Element var1) {
      this._assertion = var1;
      if (!"Assertion".equals(this._assertion.getLocalName())) {
         throw new IllegalArgumentException("DOM Element is not an assertion");
      } else {
         this._version = this._assertion.getAttribute("Version");
         if (this._version == null || !this._version.equals("2.0")) {
            String var2 = this._assertion.getAttribute("MajorVersion");
            String var3 = this._assertion.getAttribute("MinorVersion");
            this._version = var2 + "." + var3;
            if (this._version == null || !this._version.equals("1.1")) {
               throw new IllegalArgumentException("DOM Element does not have a valid SAML version");
            }

            this._isVersion20Assertion = false;
         }

         if (this._isVersion20Assertion) {
            this._id = this._assertion.getAttribute("ID");
         } else {
            this._id = this._assertion.getAttribute("AssertionID");
         }

         this._issuer = this._assertion.getAttribute("Issuer");
         this.initConditions();
         this.initSubject();
         this._issuerKeyInfo = getKeyInfoFromIssuer(this._assertion);
      }
   }

   private void initConditions() {
      Element var1 = getFirstMatchingChildElement(this._assertion, "Conditions");
      if (var1 != null) {
         SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

         try {
            this._notBefore = var2.parse(var1.getAttribute("NotBefore"));
            this._notOnOrAfter = var2.parse(var1.getAttribute("NotOnOrAfter"));
         } catch (ParseException var4) {
            throw new IllegalArgumentException("Unable to parse conditions", var4);
         }
      }
   }

   private void initSubject() {
      Element var1 = this.getSubjectFromAssertion(this._assertion);
      if (var1 == null) {
         throw new IllegalArgumentException("No Subject in assertion");
      } else {
         this._subject = this.getNameFromSubject(var1);
         this._confirmation = this.getConfirmationFromSubject(var1);
         this._keyInfo = this.getKeyInfoFromSubject(var1);
      }
   }

   private Element getSubjectFromAssertion(Element var1) {
      if (this._isVersion20Assertion) {
         return getFirstMatchingChildElement(var1, "Subject");
      } else {
         Element var2 = getFirstMatchingChildElement(var1, "AuthenticationStatement");
         if (var2 != null) {
            return getFirstMatchingChildElement(var2, "Subject");
         } else {
            var2 = getFirstMatchingChildElement(var1, "AttributeStatement");
            return var2 != null ? getFirstMatchingChildElement(var2, "Subject") : null;
         }
      }
   }

   private String getNameFromSubject(Element var1) {
      Element var2 = getFirstMatchingChildElement(var1, this._isVersion20Assertion ? "NameID" : "NameIdentifier");
      return this.getElementText(var2);
   }

   private static Element getFirstMatchingChildElement(Element var0, String var1) {
      NodeList var2 = var0.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         if (var4.getNodeType() == 1 && var4.getLocalName().equals(var1)) {
            return (Element)var4;
         }
      }

      return null;
   }

   private String getConfirmationFromSubject(Element var1) {
      Element var2 = getFirstMatchingChildElement(var1, "SubjectConfirmation");
      if (var2 != null) {
         if (this._isVersion20Assertion) {
            return var2.getAttribute("Method");
         } else {
            Element var3 = getFirstMatchingChildElement(var2, "ConfirmationMethod");
            return this.getElementText(var3);
         }
      } else {
         return null;
      }
   }

   private Element getKeyInfoFromSubject(Element var1) {
      Element var2 = getFirstMatchingChildElement(var1, "SubjectConfirmation");
      if (this._confirmation != null && var2 != null) {
         if (this._isVersion20Assertion && this._confirmation.equals("urn:oasis:names:tc:SAML:2.0:cm:holder-of-key")) {
            Element var3 = getFirstMatchingChildElement(var2, "SubjectConfirmationData");
            if (var3 != null) {
               return getFirstMatchingChildElement(var3, "KeyInfo");
            }
         } else if (!this._isVersion20Assertion && this._confirmation.equals("urn:oasis:names:tc:SAML:1.0:cm:holder-of-key")) {
            return getFirstMatchingChildElement(var2, "KeyInfo");
         }
      }

      return null;
   }

   private static Element getKeyInfoFromIssuer(Element var0) {
      Element var1 = getFirstMatchingChildElement(var0, "Signature");
      return null == var1 ? null : getFirstMatchingChildElement(var1, "KeyInfo");
   }

   private String getElementText(Element var1) {
      NodeList var2 = var1.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         if (var4.getNodeType() == 3) {
            return var4.getNodeValue();
         }
      }

      return null;
   }

   public String getId() {
      return this._id;
   }

   public String getSubjectName() {
      return this._subject;
   }

   public String getSubjectConfirmationMethod() {
      return this._confirmation;
   }

   public Element getSubjectKeyInfo() {
      return this._keyInfo;
   }

   public String getVersion() {
      return this._version;
   }

   public Date getNotBefore() {
      return this._notBefore;
   }

   public Date getNotOnOrAfter() {
      return this._notOnOrAfter;
   }

   public Element getSamlAssertionSignatureKeyInfoElement() {
      return this._issuerKeyInfo;
   }

   public static Element getSamlAssertionSignatureKeyInfoElement(Element var0) {
      return null == var0 ? null : getKeyInfoFromIssuer(var0);
   }
}
