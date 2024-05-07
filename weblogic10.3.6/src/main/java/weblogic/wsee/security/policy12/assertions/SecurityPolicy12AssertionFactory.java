package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.util.Verbose;

public class SecurityPolicy12AssertionFactory extends PolicyAssertionFactory {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicy12AssertionFactory.class);
   private static final SecurityPolicy12AssertionFactory theOne = new SecurityPolicy12AssertionFactory();

   public static SecurityPolicy12AssertionFactory getInstance() {
      return theOne;
   }

   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      String var2 = var1.getNamespaceURI();
      if (var2 != null && (var2.equals("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702") || var2.equals("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200512") || var2.equals("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy") || var2.equals("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802"))) {
         String var3 = ExternalizationUtils.getClassNameFromMap(new QName(var1.getNamespaceURI(), var1.getLocalName()));
         if (var3 != null) {
            try {
               PolicyAssertion var4 = (PolicyAssertion)Class.forName(var3).newInstance();
               if (var4 instanceof AbstractSecurityPolicyAssertion) {
                  ((AbstractSecurityPolicyAssertion)var4).initialize((Element)var1);
               }

               return var4;
            } catch (ClassNotFoundException var5) {
            } catch (IllegalAccessException var6) {
            } catch (InstantiationException var7) {
            }
         }
      }

      return null;
   }

   private static final void init() {
      if (verbose) {
         Verbose.log((Object)"In SecurityPolicy12AssertionFactory, registering WS-SP 1.2 assertions in namespace: http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200512");
      }

      registerAssertions("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200512");
   }

   protected static final void registerAssertions(String var0) {
      ExternalizationUtils.registerExternalizable(new QName(var0, "TransportBinding", "sp"), TransportBinding.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "TransportToken", "sp"), TransportToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "HttpsToken", "sp"), HttpsToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "HttpBasicAuthentication", "sp"), HttpBasicAuthentication.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireClientCertificate", "sp"), RequireClientCertificate.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "AlgorithmSuite", "sp"), AlgorithmSuite.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic256", "sp"), Basic256.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "TripleDes", "sp"), TripleDes.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic192", "sp"), Basic192.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic128", "sp"), Basic128.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic256Rsa15", "sp"), Basic256Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic128Rsa15", "sp"), Basic128Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic192Rsa15", "sp"), Basic192Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "TripleDesRsa15", "sp"), TripleDesRsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic256Sha256", "sp"), Basic256Sha256.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "TripleDesSha256", "sp"), TripleDesSha256.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic192Sha256", "sp"), Basic192Sha256.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic128Sha256", "sp"), Basic128Sha256.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic256Sha256Rsa15", "sp"), Basic256Sha256Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "TripleDesSha256Rsa15", "sp"), TripleDesSha256Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic192Sha256Rsa15", "sp"), Basic192Sha256Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Basic128Sha256Rsa15", "sp"), Basic128Sha256Rsa15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Layout", "sp"), Layout.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Lax", "sp"), Lax.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Strict", "sp"), Strict.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "LaxTsFirst", "sp"), LaxTsFirst.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "LaxTsLast", "sp"), LaxTsLast.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "IncludeTimestamp", "sp"), IncludeTimestamp.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "UsernameToken", "sp"), UsernameToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssUsernameToken10", "sp"), WssUsernameToken10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssUsernameToken11", "sp"), WssUsernameToken11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "HashPassword", "sp"), HashPassword.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "NoPassword", "sp"), NoPassword.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireDerivedKeys", "sp"), RequireDerivedKeys.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireImplicitDerivedKeys", "sp"), RequireImplicitDerivedKeys.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireExplicitDerivedKeys", "sp"), RequireExplicitDerivedKeys.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SecureConversationToken", "sp"), SecureConversationToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "BootstrapPolicy", "sp"), BootstrapPolicy.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireExternalUriReference", "sp"), RequireExternalUriReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SC200502SecurityContextToken", "sp"), SC200502SecurityContextToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EncryptBeforeSigning", "sp"), EncryptBeforeSigning.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EncryptSignature", "sp"), EncryptSignature.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "ProtectTokens", "sp"), ProtectTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "OnlySignEntireHeadersAndBody", "sp"), OnlySignEntireHeadersAndBody.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SymmetricBinding", "sp"), SymmetricBinding.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "AsymmetricBinding", "sp"), AsymmetricBinding.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "ProtectionToken", "sp"), ProtectionToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EncryptionToken", "sp"), EncryptionToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SignatureToken", "sp"), SignatureToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Issuer", "sp"), Issuer.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "IssuerName", "sp"), IssuerName.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "IssuedToken", "sp"), IssuedToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Trust10", "sp"), Trust10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportClientChallenge", "sp"), MustSupportClientChallenge.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportServerChallenge", "sp"), MustSupportServerChallenge.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireClientEntropy", "sp"), RequireClientEntropy.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireServerEntropy", "sp"), RequireServerEntropy.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportIssuedTokens", "sp"), MustSupportIssuedTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SupportingTokens", "sp"), SupportingTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SignedSupportingTokens", "sp"), SignedSupportingTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EncryptedSupportingTokens", "sp"), EncryptedSupportingTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SignedEncryptedSupportingTokens", "sp"), SignedEncryptedSupportingTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EndorsingSupportingTokens", "sp"), EndorsingSupportingTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SignedEndorsingSupportingTokens", "sp"), SignedEndorsingSupportingTokens.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "InitiatorToken", "sp"), InitiatorToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "InitiatorSignatureToken", "sp"), InitiatorSignatureToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "InitiatorEncryptionToken", "sp"), InitiatorEncryptionToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RecipientToken", "sp"), RecipientToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RecipientSignatureToken", "sp"), RecipientSignatureToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RecipientEncryptionToken", "sp"), RecipientEncryptionToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "X509Token", "sp"), X509Token.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireKeyIdentifierReference", "sp"), RequireKeyIdentifierReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireIssuerSerialReference", "sp"), RequireIssuerSerialReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireEmbeddedTokenReference", "sp"), RequireEmbeddedTokenReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireThumbprintReference", "sp"), RequireThumbprintReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509V3Token10", "sp"), WssX509V3Token10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509Pkcs7Token10", "sp"), WssX509Pkcs7Token10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509PkiPathV1Token10", "sp"), WssX509PkiPathV1Token10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509V1Token11", "sp"), WssX509V1Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509V3Token11", "sp"), WssX509V3Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509Pkcs7Token11", "sp"), WssX509Pkcs7Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssX509PkiPathV1Token11", "sp"), WssX509PkiPathV1Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Body", "sp"), Body.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Header", "sp"), Header.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SignedParts", "sp"), SignedParts.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EncryptedParts", "sp"), EncryptedParts.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequiredParts", "sp"), RequiredParts.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "XPath", "sp"), XPath.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SignedElements", "sp"), SignedElements.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "EncryptedElements", "sp"), EncryptedElements.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "ContentEncryptedElements", "sp"), ContentEncryptedElements.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequiredElements", "sp"), RequiredElements.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Wss10", "sp"), Wss10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Wss11", "sp"), Wss11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportRefKeyIdentifier", "sp"), MustSupportRefKeyIdentifier.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportRefIssuerSerial", "sp"), MustSupportRefIssuerSerial.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportRefExternalURI", "sp"), MustSupportRefExternalURI.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportRefEmbeddedToken", "sp"), MustSupportRefEmbeddedToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportRefThumbprint", "sp"), MustSupportRefThumbprint.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "MustSupportRefEncryptedKey", "sp"), MustSupportRefEncryptedKey.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireSignatureConfirmation", "sp"), RequireSignatureConfirmation.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "KerberosToken", "sp"), KerberosToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssKerberosV5ApReqToken11", "sp"), WssKerberosV5ApReqToken11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssGssKerberosV5ApReqToken11", "sp"), WssGssKerberosV5ApReqToken11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RelToken", "sp"), RelToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssRelV10Token10", "sp"), WssRelV10Token10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssRelV20Token10", "sp"), WssRelV20Token10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssRelV10Token11", "sp"), WssRelV10Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssRelV20Token11", "sp"), WssRelV20Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "SamlToken", "sp"), SamlToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssSamlV11Token10", "sp"), WssSamlV11Token10.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssSamlV11Token11", "sp"), WssSamlV11Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "WssSamlV20Token11", "sp"), WssSamlV20Token11.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireInternalReference", "sp"), RequireInternalReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "RequireExternalReference", "sp"), RequireExternalReference.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Nonce", "sp"), Nonce.class.getName());
      ExternalizationUtils.registerExternalizable(new QName(var0, "Created", "sp"), Created.class.getName());
   }

   static {
      init();
   }
}
