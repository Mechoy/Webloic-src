package weblogic.wsee.security.saml;

import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.security.SimplePrincipal;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;

public class SAMLUtils {
   private static final boolean verbose = Verbose.isVerbose(SAMLUtils.class);
   private static final String SAML_ASSERTION_TAG = "Assertion";
   private static final String SAML11_ID_ATTR = "AssertionID";
   private static final String SAML11_MAJOR_VERSION_ATTR = "MajorVersion";
   private static final String SAML11_MINOR_VERSION_ATTR = "MinorVersion";
   private static final String SAML11_VERSION = "1.1";
   private static final String SAML20_ID_ATTR = "ID";
   private static final String SAML20_VERSION_ATTR = "Version";
   private static final String SAML20_VERSION = "2.0";
   private static final String SAML_ISSUER_ATTR = "Issuer";
   private static final String SAML_CONDITIONS_TAG = "Conditions";
   private static final String SAML_NOTBEFORE_ATTR = "NotBefore";
   private static final String SAML_NOTONORAFTER_ATTR = "NotOnOrAfter";
   public static final String SAML_SUBJECT_TAG = "Subject";
   public static final String SAML_ATN_STATEMENT_TAG = "AuthenticationStatement";
   public static final String SAML_ATTRIBUTE_STATEMENT_TAG = "AttributeStatement";
   private static final String SAML11_NAMEID_TAG = "NameIdentifier";
   private static final String SAML20_NAMEID_TAG = "NameID";
   public static final String SAML_SUBJCONF_TAG = "SubjectConfirmation";
   private static final String SAML20_SUBJCONF_METHOD_ATTR = "Method";
   private static final String SAML11_SUBJCONF_METHOD_TAG = "ConfirmationMethod";
   private static final String SAML_KEYINFO_TAG = "KeyInfo";
   private static final String SAML_SUBJCONF_DATA_TAG = "SubjectConfirmationData";
   private static final String FORMAT = "Format";
   private static final String X509_SUBJECT_NAME = "X509SubjectName";
   private static final String AUTHN_INSTANT = "AuthenticationInstant";
   private static final String AUTHN_METHOD = "AuthenticationMethod";
   private static final String AUTHN_METHOD_PASSWORD = "urn:oasis:names:tc:SAML:1.0:am:password";
   private static Set saml11TokenSet = null;

   public static String getTokenTypeFromAssertionElement(Element var0) {
      if (var0 != null && "Assertion".equals(var0.getLocalName())) {
         String var1 = var0.getAttribute("Version");
         if ("2.0".equals(var1)) {
            return "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
         }

         var1 = var0.getAttribute("MajorVersion") + "." + var0.getAttribute("MinorVersion");
         if ("1.1".equals(var1)) {
            return "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1";
         }
      }

      return null;
   }

   public static boolean isEquivalentSamlTokenType(String var0, String var1) {
      if (var0 != null && var0.equals(var1) && var0.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0")) {
         return true;
      } else {
         return saml11TokenSet.contains(var0) && saml11TokenSet.contains(var1);
      }
   }

   public static boolean isSamlTokenType(String var0) {
      return "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(var0) || saml11TokenSet.contains(var0);
   }

   public static boolean isSymmetricKeyType(String var0) {
      return null == var0 ? false : var0.endsWith("/SymmetricKey");
   }

   public static Element getFirstMatchingChildElement(Element var0, String var1) {
      NodeList var2 = var0.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         if (var4.getNodeType() == 1 && var4.getLocalName().equals(var1)) {
            return (Element)var4;
         }
      }

      return null;
   }

   public static Node getReplaceAttributeStmtToAuthenticateStmtSAMLNode(Element var0) {
      if (null == getFirstMatchingChildElement(var0, "AttributeStatement")) {
         return var0;
      } else {
         Node var1 = var0.cloneNode(true);
         QName var2 = new QName(var0.getNamespaceURI(), "AuthenticationStatement", var0.getPrefix());
         Element var3 = DOMUtils.createElement((Element)var1, var2, var0.getPrefix());
         Element var4 = getFirstMatchingChildElement(var0, "Conditions");
         String var5 = "2008-12-02T20:08:42.359Z";
         if (null != var4) {
            var5 = DOMUtils.getAttributeValue(var4, new QName("NotBefore"), var5);
         }

         var3.setAttribute("AuthenticationInstant", var5);
         var3.setAttribute("AuthenticationMethod", "urn:oasis:names:tc:SAML:1.0:am:password");
         Element var6 = getFirstMatchingChildElement((Element)var1, "AttributeStatement");
         Element var7 = getFirstMatchingChildElement(var6, "Subject");
         var3.appendChild(var7);
         var1.replaceChild(var3, var6);
         return var1;
      }
   }

   public static boolean hasAttributeNoAuthenticateStmt(Element var0) {
      if (null == var0) {
         return false;
      } else if (null != getFirstMatchingChildElement(var0, "AuthenticationStatement")) {
         return false;
      } else {
         Element var1 = getFirstMatchingChildElement(var0, "AttributeStatement");
         if (null == var1) {
            return false;
         } else {
            return null != getFirstMatchingChildElement(var1, "Subject");
         }
      }
   }

   public static Element getSubjectElementFromSamlAssertion(Element var0) {
      Element var1 = getFirstMatchingChildElement(var0, "AuthenticationStatement");
      if (var1 != null) {
         return getFirstMatchingChildElement(var1, "Subject");
      } else {
         var1 = getFirstMatchingChildElement(var0, "AttributeStatement");
         return var1 != null ? getFirstMatchingChildElement(var1, "Subject") : getFirstMatchingChildElement(var0, "Subject");
      }
   }

   public static Element getNameIdentifierElm(Element var0) {
      if (null == var0) {
         return null;
      } else {
         Element var1 = getFirstMatchingChildElement(var0, "NameIdentifier");
         return null != var1 ? var1 : getFirstMatchingChildElement(var0, "NameID");
      }
   }

   public static boolean hasX509SubjectName(Element var0) {
      if (null == var0) {
         return false;
      } else {
         String var1 = DOMUtils.getAttributeValue(var0, new QName("Format"));
         if (null == var1) {
            var1 = DOMUtils.getAttributeValue(var0, new QName(var0.getNamespaceURI(), "Format"));
         }

         if (null != var1) {
            return var1.indexOf("X509SubjectName") != -1;
         } else {
            String var2 = DOMUtils.getText(var0);
            return null == var2 ? false : var2.startsWith("CN=");
         }
      }
   }

   public static Subject getJavaSubjectFromSamlElement(Node var0) {
      Element var1 = (Element)var0;
      Element var2 = getSubjectElementFromSamlAssertion(var1);
      Element var3 = getNameIdentifierElm(var2);
      if (null == var3) {
         return null;
      } else {
         String var4 = DOMUtils.getText(var3);
         Subject var5 = new Subject();
         if (null == var4) {
            return var5;
         } else {
            if (hasX509SubjectName(var3)) {
               if (verbose) {
                  Verbose.log((Object)("X509 Subject name =" + var4));
               }

               X500Principal var6 = new X500Principal(var4);
               var5.getPrincipals().add(var6);
            } else {
               if (verbose) {
                  Verbose.log((Object)("Simple principla name =" + var4));
               }

               SimplePrincipal var7 = new SimplePrincipal(var4);
               var5.getPrincipals().add(var7);
            }

            return var5;
         }
      }
   }

   static {
      saml11TokenSet = new HashSet();
      saml11TokenSet.add("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1");
      saml11TokenSet.add("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID");
      saml11TokenSet.add("http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID");
   }
}
