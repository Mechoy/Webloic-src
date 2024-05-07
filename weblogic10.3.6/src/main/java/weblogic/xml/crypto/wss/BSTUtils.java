package weblogic.xml.crypto.wss;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.security.utils.Utils;

public class BSTUtils {
   private static final String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   private static final QName POLICY_SUBJECT_NAME = new QName("http://www.bea.com/wls90/security/policy", "SubjectName");

   public static boolean matches(X509Credential var0, ContextHandler var1) {
      Object var2 = var1.getValue("com.bea.contextelement.xml.IssuerSerial");
      if (var2 != null) {
         X509IssuerSerial var3 = (X509IssuerSerial)var2;
         if (!matches(var3, var0)) {
            return false;
         }
      }

      var2 = var1.getValue("com.bea.contextelement.xml.KeyIdentifier");
      KeyIdentifier var4;
      if (var2 != null) {
         var4 = (KeyIdentifier)var2;
         if (!matches(var4, var0) && !matchesThumbprint(var4, var0)) {
            return false;
         }
      }

      var2 = var1.getValue("weblogic.wsee.security.wss11.thumbprint");
      if (var2 != null) {
         var4 = (KeyIdentifier)var2;
         if (!matchesThumbprint(var4, var0)) {
            return false;
         }
      }

      var2 = var1.getValue("weblogic.xml.crypto.wss.policy.Claims");
      if (var2 != null) {
         Node var5 = (Node)var2;
         if (!matches(var5, var0)) {
            return false;
         }
      }

      var2 = var1.getValue("weblogic.xml.crypto.keyinfo.keyname");
      if (var2 != null) {
         String var6 = (String)var2;
         if (!matches(var6, var0)) {
            return false;
         }
      }

      return true;
   }

   private static boolean matches(String var0, X509Credential var1) {
      X509Certificate var2 = var1.getCertificate();
      Principal var3 = var2.getSubjectDN();
      String var4 = var3.getName();
      System.out.println("trying to match keyname " + var0 + " and subject dn principal name " + var4);
      if (var4.equals(var0)) {
         return true;
      } else {
         LogUtils.logWss("X509 certificate's subject DN does not match keyname " + var0);
         return false;
      }
   }

   public static boolean matchesThumbprint(KeyIdentifier var0, X509Credential var1) {
      return var1 != null && matchesThumbprint(var1.getCertificate(), var0.getIdentifier());
   }

   public static boolean matchesThumbprint(X509Certificate var0, byte[] var1) {
      try {
         if (KeyUtils.matches(CertUtils.getThumbprint(var0), var1)) {
            return true;
         }
      } catch (WSSecurityException var3) {
      }

      LogUtils.logWss("X509 certificate's thumbprint does not match.");
      return false;
   }

   public static boolean matches(KeyIdentifier var0, X509Credential var1) {
      if (var1 == null) {
         return false;
      } else {
         X509Certificate var2 = var1.getCertificate();
         byte[] var3 = Utils.getSubjectKeyIdentifier(var2);
         if (KeyUtils.matches(var0.getIdentifier(), var3)) {
            return true;
         } else {
            LogUtils.logWss("X509 certificate's key identifier does not match.");
            return false;
         }
      }
   }

   public static boolean matches(X509IssuerSerial var0, X509Credential var1) {
      X509Certificate var2 = var1.getCertificate();
      BigInteger var3 = var2.getSerialNumber();
      if (!var0.getSerialNumber().equals(var3)) {
         LogUtils.logWss("X509 certificate's serial number " + var3 + " does not match " + var0.getSerialNumber());
         return false;
      } else {
         return matches(var0.getIssuerName(), var2);
      }
   }

   public static boolean matches(String var0, X509Certificate var1) {
      String var2 = var1.getIssuerX500Principal().getName();
      String var3 = var1.getIssuerDN().getName();
      boolean var4 = compareIssuerName(var0, var2) || compareIssuerName(var0, var3);
      if (!var4) {
         LogUtils.logWss("X509 certificate's issuer name " + var2 + "/" + var3 + " does not match " + var0);
      }

      return var4;
   }

   private static boolean compareIssuerName(String var0, String var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (var1 == null) {
         return false;
      } else {
         String[] var2 = var0.replaceAll("\\s", "").split(",");
         String[] var3 = var1.replaceAll("\\s", "").split(",");
         Arrays.sort(var2);
         Arrays.sort(var3);
         boolean var4 = Arrays.equals(var2, var3);
         if (!var4 && var2.length >= 3 && var3.length >= 3) {
            int var5 = var0.indexOf(" S=");
            int var6 = var1.indexOf(" S=");
            if (var5 == -1 && var6 == -1) {
               return var4;
            } else {
               if (var5 != -1) {
                  var2 = replace2ST(var2);
               }

               if (var6 != -1) {
                  var3 = replace2ST(var3);
               }

               return Arrays.equals(var2, var3);
            }
         } else {
            return var4;
         }
      }
   }

   private static String[] replace2ST(String[] var0) {
      for(int var1 = var0.length - 1; var1 > 1; --var1) {
         if (var0[var1] != null && var0[var1].startsWith("S=")) {
            var0[var1] = "ST=" + var0[var1].substring(2);
            break;
         }
      }

      return var0;
   }

   public static boolean matches(BinarySecurityToken var0, X509Credential var1) {
      return X509Credential.matches((X509Credential)var0.getCredential(), var1);
   }

   public static boolean matches(Node var0, X509Credential var1) {
      X509Certificate var2 = var1.getCertificate();
      String var3 = ClaimsBuilder.getClaimFromElt(var0, POLICY_SUBJECT_NAME);
      if (var3 != null && !var2.getSubjectX500Principal().getName().equals(var3)) {
         LogUtils.logWss("X509 certificate's subject name " + var2.getSubjectX500Principal().getName() + " does not match claims " + "subject name " + var3);
         return false;
      } else {
         return true;
      }
   }

   public static void marshalToken(BinarySecurityToken var0, Element var1, Map var2, Node var3, boolean var4) throws MarshalException {
      Map var5 = DOMUtils.getNamespaceMap(var1);
      String var6 = (String)var2.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
      String var7 = (String)var2.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      Element var8 = DOMUtils.createElement(var1, WSSConstants.BST_QNAME, var6);
      DOMUtils.declareNamespace(var8, WSSConstants.BST_QNAME.getNamespaceURI(), var6);
      if (var0.getId() != null) {
         DOMUtils.addPrefixedAttribute(var8, WSSConstants.WSU_ID_QNAME, var7, var0.getId());
         DOMUtils.declareNamespace(var8, WSSConstants.WSU_ID_QNAME.getNamespaceURI(), var7, var5);
      }

      String var9 = var0.getValueType();
      if (var9 != null) {
         DOMUtils.addAttribute(var8, WSSConstants.VALUE_TYPE_QNAME, var9);
      }

      if (var4) {
         DOMUtils.addAttribute(var8, WSSConstants.ENCODING_TYPE_QNAME, var0.getEncodingType());
      }

      String var10 = null;

      try {
         var10 = var0.getEncodedValue();
      } catch (WSSecurityException var12) {
         throw new MarshalException("Failed to encode BinarySecurityToken.", var12);
      }

      DOMUtils.addText(var8, var10);
      if (var3 != null) {
         var1.insertBefore(var8, var3);
      } else {
         var1.appendChild(var8);
      }

   }

   public static boolean isX509Type(String var0) {
      for(int var1 = 0; var1 < WSSConstants.BUILTIN_BST_VALUETYPES.length; ++var1) {
         String var2 = WSSConstants.BUILTIN_BST_VALUETYPES[var1];
         if (var2.equals(var0)) {
            return true;
         }
      }

      return false;
   }
}
