package weblogic.wsee.security.saml;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.security.KeyPairCredential;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RemoteResource;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.spi.Resource;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.X509Data;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoImpl;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.dom.NamespaceUtils;

public class CSSUtils {
   private static boolean verbose = Verbose.isVerbose(CSSUtils.class);
   private static final AuthenticatedSubject kernelId = getKernelID();
   private static final boolean isEnableSaml11RelativePath = Boolean.parseBoolean(System.getProperty("weblogic.wsee.security.saml.EnableSaml11RelativePathConfig"));
   private static Stack<DocumentBuilder> pool = new Stack();
   protected static final String SAML_ATTRIBUTES = "com.bea.contextelement.saml.Attributes";
   protected static final String SAML2_ATTRIBUTES = "com.bea.contextelement.saml2.Attributes";
   protected static final String SAML_ATTRIBUTE_ONLY = "com.bea.contextelement.saml.AttributeOnly";

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static CredentialManager getCredentialManager() {
      String var0 = "weblogicDEFAULT";
      CredentialManager var1 = (CredentialManager)SecurityServiceManager.getSecurityService(kernelId, var0, ServiceType.CREDENTIALMANAGER);
      if (var1 == null) {
         throw new RuntimeException("CredentialManager Unavailable");
      } else {
         return var1;
      }
   }

   private static PrincipalAuthenticator getPrincipalAuthenticator() {
      String var0 = "weblogicDEFAULT";
      PrincipalAuthenticator var1 = SecurityServiceManager.getPrincipalAuthenticator(kernelId, var0);
      if (var1 == null) {
         throw new RuntimeException("PrincipalAuthenticator Unavailable");
      } else {
         return var1;
      }
   }

   static void processSAMLClaims(boolean var0, SecurityTokenContextHandler var1, Node var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("claims of SAML token is null");
      } else {
         String var3 = ClaimsBuilder.getClaimFromElt(var2, SAMLConstants.CONFIRMATION_METHOD_QNAME);
         if (var0) {
            var3 = mapSAML2ConfMethod(var3);
         } else {
            var3 = mapSAMLConfMethod(var3);
         }

         if (var3 == null) {
            throw new IllegalArgumentException("ConfirmationMethod of saml token is not specified.");
         } else {
            if (verbose) {
               Verbose.log((Object)("Requested subject confirmation is: " + var3));
            }

            var1.addContextElement("com.bea.contextelement.saml.subject.ConfirmationMethod", var3);
         }
      }
   }

   public static void setupSAMLContextElements(boolean var0, SecurityTokenContextHandler var1, ContextHandler var2) {
      String var3 = (String)var2.getValue("com.bea.contextelement.xml.EndpointURL");
      if (var0) {
         if (verbose) {
            Verbose.log((Object)("Setting: com.bea.contextelement.saml.TargetResource to " + var3));
         }

         var1.addContextElement("com.bea.contextelement.saml.TargetResource", var3);
      } else {
         if (verbose) {
            Verbose.log((Object)("Setting ENDPOINT_URL and SAML_TARGET_RESOURCE to: " + var3));
         }

         var1.addContextElement("com.bea.contextelement.xml.EndpointURL", var3);
         var1.addContextElement("com.bea.contextelement.saml.TargetResource", var3);
      }

   }

   public static void setupSAMLAttributesContextElements(boolean var0, SecurityTokenContextHandler var1, boolean var2, SAMLAttributeStatementData var3) {
      if (verbose) {
         Verbose.log((Object)"Setting SAML Attributes....");
      }

      if (null != var3 && !var3.isEmpty()) {
         if (var0) {
            if (verbose) {
               Verbose.log((Object)"Requesting SAML Attributes to be generated from colliction of <SAML2AttributeStatementInfo>");
            }

            var1.addContextElement("com.bea.contextelement.saml2.Attributes", var3.getCollectionsForSAML2AttributeStatementInfo());
         } else {
            if (verbose) {
               Verbose.log((Object)"Requesting SAML Attributes to be generated from colliction of <SAMLAttributeStatementInfo>");
            }

            var1.addContextElement("com.bea.contextelement.saml.Attributes", var3.getCollectionsForSAMLAttributeStatementInfo());
         }

         if (var2) {
            if (verbose) {
               Verbose.log((Object)"Finally, requesting a SAML Token with SAML Attributes only");
            }

            if (verbose) {
               Verbose.log((Object)"Adding com.bea.contextelement.saml.AttributeOnly with \"True\" into CtxHandler");
            }

            var1.addContextElement("com.bea.contextelement.saml.AttributeOnly", new Boolean("true"));
         }

      } else {
         if (verbose) {
            Verbose.log((Object)"No SAML Attributes data found");
         }

         if (!var2) {
            if (verbose) {
               Verbose.log((Object)"Requesting a SAML Token without SAML Attributes");
            }

         } else {
            if (verbose) {
               Verbose.log((Object)"Requesting a SAML Token with SAML Attributes only but no attributes value");
            }

            if (verbose) {
               Verbose.log((Object)"Adding only com.bea.contextelement.saml.AttributeOnly with \"True\" into CtxHandler");
            }

            var1.addContextElement("com.bea.contextelement.saml.AttributeOnly", new Boolean("true"));
            ArrayList var4;
            if (var0) {
               var4 = new ArrayList();
               var1.addContextElement("com.bea.contextelement.saml2.Attributes", var4);
            } else {
               var4 = new ArrayList();
               var1.addContextElement("com.bea.contextelement.saml.Attributes", var4);
            }

         }
      }
   }

   public static Object getSAMLCredential(boolean var0, String var1, SecurityTokenContextHandler var2, Object var3) throws WSSecurityException {
      return getSAMLCredential(var0, var1, var2, var3, (AuthenticatedSubject)null);
   }

   public static Object getSAMLCredential(boolean var0, String var1, SecurityTokenContextHandler var2, Object var3, AuthenticatedSubject var4) throws WSSecurityException {
      PrivateKey var5 = null;
      Node var6;
      if (var3 != null) {
         var6 = null;
         if (var3 instanceof X509Credential) {
            X509Credential var7 = (X509Credential)var3;
            ArrayList var8 = new ArrayList();
            var8.add(var7.getCertificate());
            var6 = getKeyInfoNodeFromCerts(var8);
            var5 = var7.getPrivateKey();
            if (var6 != null) {
               if (verbose) {
                  Verbose.log((Object)("Adding KeyInfo element to context handler: " + var6));
               }

               var2.addContextElement("com.bea.contextelement.saml.subject.dom.KeyInfo", var6);
            } else if (verbose) {
               Verbose.log((Object)"Null KeyInfo element from X509 cert is NOT added to context handler ");
            }
         } else if (var3 instanceof Node) {
            var6 = (Node)var3;
            if (verbose) {
               Verbose.log((Object)("Adding KeyInfo Node to context handler fot SAML assertion: [" + DOMUtils.toXMLString(var6) + "]"));
            }

            var2.addContextElement("com.bea.contextelement.saml.subject.dom.KeyInfo", var6);
         } else if (verbose) {
            Verbose.log((Object)"Not supported Key Info type found! NO ACTION");
         }
      }

      var6 = null;
      Object[] var9;
      if (var0) {
         var9 = getSAMLAssertionFromCredMapper(var2, "SAML2.Assertion.DOM", var4);
      } else {
         var9 = getSAMLAssertionFromCredMapper(var2, "SAML.Assertion.DOM", var4);
      }

      if (var9 != null && var9.length > 0 && var9[0] instanceof Element) {
         Element var10 = (Element)var9[0];
         if (verbose) {
            Verbose.log((Object)("Returning new SAML Assertion from CSS for CredentialImpl: [" + DOMUtils.toXMLString(var10) + "]"));
         }

         return new SAMLCredentialImpl(var1, var10, var5);
      } else {
         if (verbose) {
            Verbose.log((Object)"Didn't get assertion, returning null credential");
         }

         return null;
      }
   }

   public static boolean isHolderOfKey(boolean var0, SecurityTokenContextHandler var1) {
      String var2 = (String)var1.getValue("com.bea.contextelement.saml.subject.ConfirmationMethod");
      if (var0) {
         return var2 != null && var2.equals("urn:oasis:names:tc:SAML:2.0:cm:holder-of-key");
      } else {
         return var2 != null && var2.equals("holder-of-key");
      }
   }

   public static boolean isHolderOfKey(String var0) {
      return "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key".equals(var0) || "holder-of-key".equals(var0) || "HOLDER_OF_KEY".equals(var0);
   }

   public static Subject getCurrentAuthenticatedSubject() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(kernelId);
      return var0.getSubject();
   }

   public static X509Credential getX509CredFromPKICredMapper(SecurityTokenContextHandler var0) {
      CredentialManager var1 = getCredentialManager();
      AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(kernelId);
      RemoteResource var3 = getRemoteResource(var0);
      if (var3 == null) {
         return null;
      } else {
         Object[] var4 = var1.getCredentials(kernelId, var2, var3, var0, "weblogic.pki.Keypair");
         if (var4 != null && var4.length != 0) {
            KeyPairCredential var5 = (KeyPairCredential)var4[0];
            return new X509Credential((X509Certificate)var5.getCertificate(), (PrivateKey)var5.getKey());
         } else {
            if (verbose) {
               Verbose.log((Object)"can't find holder-of-key from PKICreditMapper");
            }

            return null;
         }
      }
   }

   public static Object[] getSAMLAssertionFromCredMapper(SecurityTokenContextHandler var0, String var1) {
      return getSAMLAssertionFromCredMapper(var0, var1, (AuthenticatedSubject)null);
   }

   public static Object[] getSAMLAssertionFromCredMapper(SecurityTokenContextHandler var0, String var1, AuthenticatedSubject var2) {
      CredentialManager var3 = getCredentialManager();
      if (var2 == null) {
         var2 = SecurityServiceManager.getCurrentSubject(kernelId);
      }

      if (verbose) {
         Verbose.log((Object)("Calling CSS for subject " + var2.toString() + " and token type " + var1));
      }

      return var3.getCredentials(kernelId, var2, (Resource)null, var0, var1);
   }

   static RemoteResource getRemoteResource(SecurityTokenContextHandler var0) {
      String var1 = (String)var0.getValue("com.bea.contextelement.xml.EndpointURL");
      URL var2 = null;

      try {
         if (var1 == null) {
            return null;
         }

         var2 = new URL(var1);
      } catch (MalformedURLException var4) {
         if (verbose) {
            Verbose.log("Failed to resolve remote target URL", var4);
         }

         return null;
      }

      RemoteResource var3 = new RemoteResource(var2.getProtocol(), var2.getHost(), String.valueOf(var2.getPort()), var2.getPath(), (String)null);
      return var3;
   }

   public static AuthenticatedSubject assertIdentity(Node var0, ContextHandler var1, boolean var2) throws LoginException {
      if (verbose) {
         Verbose.log((Object)"Attempting assertIdentity");
      }

      if (verbose) {
         Verbose.log((Object)("SAML_TARGET_RESOURCE is: " + var1.getValue("com.bea.contextelement.saml.TargetResource")));
      }

      PrincipalAuthenticator var3 = getPrincipalAuthenticator();
      if (verbose) {
         Verbose.log((Object)"Got Principal Authenticator");
      }

      Document var4 = getParser().newDocument();
      Node var5 = var4.importNode(var0, true);
      var4.appendChild(var5);
      String var6 = var2 ? "SAML2.Assertion.DOM" : "SAML.Assertion.DOM";
      if (verbose) {
         Verbose.log((Object)("Cred type is: " + var6 + ", Node: " + var5));
      }

      AuthenticatedSubject var7 = null;

      try {
         var7 = var3.assertIdentity(var6, var5, var1);
      } catch (LoginException var9) {
         if (verbose) {
            Verbose.log((Object)("Exception while asserting identity: " + var9.toString()));
         }

         if (verbose) {
            Verbose.log((Object)var9);
         }

         throw var9;
      }

      if (verbose) {
         Verbose.log((Object)("Got subject: " + var7));
      }

      return var7;
   }

   private static DocumentBuilder createNewParser() {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         var0.setNamespaceAware(true);
         return var0.newDocumentBuilder();
      } catch (FactoryConfigurationError var1) {
         throw new RuntimeException(var1);
      } catch (ParserConfigurationException var2) {
         throw new RuntimeException(var2);
      }
   }

   protected static DocumentBuilder getParser() {
      DocumentBuilder var0 = null;
      synchronized(pool) {
         if (pool.empty()) {
            var0 = createNewParser();
         } else {
            var0 = (DocumentBuilder)pool.pop();
         }

         return var0;
      }
   }

   protected static void returnParser(DocumentBuilder var0) {
      synchronized(pool) {
         pool.push(var0);
      }
   }

   private static Node getKeyInfoNodeFromCerts(List var0) {
      try {
         XMLSignatureFactory var1 = XMLSignatureFactory.getInstance();
         KeyInfoFactory var2 = var1.getKeyInfoFactory();
         X509Data var3 = var2.newX509Data(var0);
         KeyInfoImpl var4 = (KeyInfoImpl)var2.newKeyInfo(Collections.singletonList(var3));
         Document var5 = getParser().newDocument();
         DOMStreamWriter var6 = new DOMStreamWriter(var5);
         var4.write(var6);
         Node var7 = var5.getFirstChild();
         declarePrefixOnKeyInfoNode(var7);
         return var7;
      } catch (XMLSignatureException var8) {
         throw new RuntimeException(var8);
      } catch (XMLStreamException var9) {
         throw new RuntimeException(var9);
      } catch (MarshalException var10) {
         throw new RuntimeException(var10);
      }
   }

   private static void declarePrefixOnKeyInfoNode(Node var0) {
      if (var0.getNodeType() == 1) {
         String var1 = var0.getPrefix();
         if (var1 == null || var1.length() == 0) {
            var0.setPrefix("dsig");
            NamespaceUtils.defineNamespace((Element)var0, "dsig", "http://www.w3.org/2000/09/xmldsig#");
         }
      }

      NodeList var3 = var0.getChildNodes();

      for(int var2 = 0; var2 < var3.getLength(); ++var2) {
         declarePrefixOnKeyInfoNode(var3.item(var2));
      }

   }

   protected static String getEndpointPath(boolean var0, String var1) {
      if (var0 || isEnableSaml11RelativePath) {
         try {
            URL var2 = new URL(var1);
            return var2.getPath();
         } catch (MalformedURLException var3) {
         }
      }

      return var1;
   }

   protected static String mapSAML2ConfMethod(String var0) {
      if (!"bearer".equals(var0) && !"BEARER".equals(var0)) {
         if (!"sender-vouches".equals(var0) && !"SENDER_VOUCHES".equals(var0)) {
            if (!"holder-of-key".equals(var0) && !"HOLDER_OF_KEY".equals(var0)) {
               if (verbose) {
                  Verbose.log((Object)("Unable to map the SAML 2.0 confirmation method on: [" + var0 + "]"));
               }

               return null;
            } else {
               return "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
            }
         } else {
            return "urn:oasis:names:tc:SAML:2.0:cm:sender-vouches";
         }
      } else {
         return "urn:oasis:names:tc:SAML:2.0:cm:bearer";
      }
   }

   protected static String mapSAMLConfMethod(String var0) {
      if (!"bearer".equals(var0) && !"BEARER".equals(var0)) {
         if (!"sender-vouches".equals(var0) && !"SENDER_VOUCHES".equals(var0)) {
            if (!"holder-of-key".equals(var0) && !"HOLDER_OF_KEY".equals(var0)) {
               if (verbose) {
                  Verbose.log((Object)("Unable to map the SAML 1.1 confirmation method on: [" + var0 + "]"));
               }

               return null;
            } else {
               return "holder-of-key";
            }
         } else {
            return "sender-vouches";
         }
      } else {
         return "bearer";
      }
   }
}
