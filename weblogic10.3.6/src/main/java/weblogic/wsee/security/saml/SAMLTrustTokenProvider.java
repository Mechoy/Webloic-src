package weblogic.wsee.security.saml;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.security.util.CertUtils;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.EncryptedKeyInfoBuilder;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.UsernameToken;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public abstract class SAMLTrustTokenProvider implements TrustTokenProvider {
   private static boolean verbose = Verbose.isVerbose(SAMLTrustTokenProvider.class);

   public TrustToken issueTrustToken(WSTContext var1) throws WSTFaultException {
      String var2 = this.getConfirmationMethod(var1.getTokenType(), var1.getKeyType());
      if (verbose) {
         Verbose.log((Object)("Token Type =[" + var1.getTokenType() + "]  and KeyType =[" + var1.getKeyType() + "] and the Confirmation Method =[" + var2 + "]"));
      }

      SAMLCredential var3 = this.getCredential(var1.getTokenType(), var1.getAppliesTo(), this.getSubject(var1), var2, (WSSecurityContext)var1.getMessageContext().getProperty("weblogic.xml.crypto.wss.WSSecurityContext"), var1);
      if (var3 == null) {
         throw new RequestFailedException("Could not obtain SAML token.");
      } else {
         SAMLTrustCredential var4 = new SAMLTrustCredential(var3);
         this.intiTrustCredential(var4, var1);
         SAMLTrustToken var5 = new SAMLTrustToken(var4);
         return var5;
      }
   }

   protected String getConfirmationMethod(String var1, String var2) {
      if ("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer".equals(var2)) {
         return "bearer";
      } else {
         return !"http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey".equals(var2) && !"http://docs.oasis-open.org/ws-sx/ws-trust/200512/SymmetricKey".equals(var2) ? "sender-vouches" : "holder-of-key";
      }
   }

   private AuthenticatedSubject getSubject(WSTContext var1) throws WSTFaultException {
      AuthenticatedSubject var2 = null;

      try {
         var2 = this.getOnBehalfOfSubject(var1);
         if (var2 == null) {
            var2 = (AuthenticatedSubject)var1.getMessageContext().getProperty("weblogic.wsee.wss.subject");
         }

         return var2;
      } catch (LoginException var4) {
         throw new WSTFaultException("Could not get SAML token for OnBehalfOf token.");
      }
   }

   private AuthenticatedSubject getOnBehalfOfSubject(WSTContext var1) throws LoginException {
      SecurityToken var2 = var1.getOnBehalfOfToken();
      if (var2 != null && var2 instanceof UsernameToken) {
         String var3 = ((UsernameToken)var2).getUsername();
         if (var3 != null && var3.length() != 0) {
            String var4 = "weblogicDEFAULT";
            PrincipalAuthenticator var5 = SecurityServiceManager.getPrincipalAuthenticator(getKernelID(), var4);
            if (var5 == null) {
               throw new RuntimeException("PrincipalAuthenticator Unavailable");
            }

            return var5.impersonateIdentity(var3);
         }
      }

      return null;
   }

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private SAMLCredential getCredential(String var1, String var2, AuthenticatedSubject var3, String var4, WSSecurityContext var5, WSTContext var6) {
      boolean var7 = false;
      SecurityTokenContextHandler var8 = new SecurityTokenContextHandler();
      var8.addContextElement("com.bea.contextelement.xml.EndpointURL", var2);
      var8.addContextElement("com.bea.contextelement.saml.TargetResource", var2);
      if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(var1)) {
         var7 = true;
         var8.addContextElement("com.bea.contextelement.saml.subject.ConfirmationMethod", CSSUtils.mapSAML2ConfMethod(var4));
      } else {
         var8.addContextElement("com.bea.contextelement.saml.subject.ConfirmationMethod", CSSUtils.mapSAMLConfMethod(var4));
      }

      Subject var9 = null;
      if (null != var3) {
         var9 = var3.getSubject();
      }

      SAMLAttributeStatementData var10 = this.getSAMLAttributeData(var7, var2, var9, var5);
      if (var10 != null) {
         CSSUtils.setupSAMLAttributesContextElements(var7, var8, var10.isAttributeOnlyRequest(), var10);
      }

      Object var11 = null;

      try {
         if (CSSUtils.isHolderOfKey(var4)) {
            if ("http://docs.oasis-open.org/ws-sx/ws-trust/200512/SymmetricKey".equals(var6.getKeyType())) {
               if (verbose) {
                  Verbose.log((Object)"setting Symmetric Holder of Key ...");
               }

               CredentialProvider var12 = this.getEncryptionCredentialProvider(var7, var2, var5);
               var11 = this.getKeyInfoFromSymmetricKey(var6, var5, var12);
            } else {
               var11 = this.getKeyInfoCredential(var3, var5);
            }
         }

         return (SAMLCredential)CSSUtils.getSAMLCredential(var1.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"), var1, var8, var11, var3);
      } catch (WSSecurityException var13) {
         if (verbose) {
            Verbose.log("Exception while acquiring SAML credential", var13);
         }

         return null;
      }
   }

   public SAMLAttributeStatementData getSAMLAttributeData(boolean var1, String var2, Subject var3, WSSecurityContext var4) {
      if (verbose) {
         Verbose.log((Object)("No override on getSAMLAttributeData(), and no SAML Attribute data to be generated for target url =" + var2));
      }

      return null;
   }

   private CredentialProvider getEncryptionCredentialProvider(boolean var1, String var2, WSSecurityContext var3) {
      try {
         X509Certificate var4 = this.getServerEncryptionCert(var1, var2, var3);
         if (null == var4) {
            if (verbose) {
               Verbose.log((Object)"Geeting no X509 for EncryptedKey token in SAML assertion");
            }

            return null;
         } else {
            return new StubPropertyBSTCredProv(var4, (X509Certificate)null);
         }
      } catch (Exception var5) {
         if (verbose) {
            Verbose.log("Got exception when getting EncryptionCredentialProvider for for encrypting EncryptedKey token in SAML assertion", var5);
         }

         return null;
      }
   }

   public X509Certificate getServerEncryptionCert(boolean var1, String var2, WSSecurityContext var3) throws Exception {
      if (verbose) {
         Verbose.log((Object)("No override on getServerEncryptionCert(), and no encryption cert to be used for encrypting EncryptedKey token in SAML assertion for target url =" + var2));
      }

      return null;
   }

   private void intiTrustCredential(SAMLTrustCredential var1, WSTContext var2) {
      var1.setAppliesTo(var2.getAppliesTo());
      var1.setCreated(var2.getCreated());
      var1.setExpires(var2.getExpires());
   }

   public TrustToken renewTrustToken(WSTContext var1, TrustToken var2) throws WSTFaultException {
      return null;
   }

   public void cancelTrustToken(WSTContext var1, TrustToken var2) throws WSTFaultException {
   }

   public SecurityTokenReference createSecurityTokenReference(WSTContext var1, TrustToken var2) throws WSTFaultException {
      return null;
   }

   public TrustToken resolveTrustToken(WSTContext var1, SecurityTokenReference var2) throws WSTFaultException {
      return null;
   }

   public Object getKeyInfoCredential(AuthenticatedSubject var1, WSSecurityContext var2) {
      if (null != var1) {
         Object var3 = var2.getProperty("BinarySecurityTokenHandler.AuthenticatedSubject.Cert");
         if (var3 != null && var3 instanceof X509Certificate) {
            if (verbose) {
               Verbose.log((Object)("Found X509 credential of " + (X509Certificate)var3));
            }

            return new X509Credential((X509Certificate)var3, (PrivateKey)null);
         }

         if (verbose) {
            Verbose.log((Object)"looking BST from WSSecurityContext ...");
         }

         List var4 = var2.getBinarySecurityTokens();
         if (null == var4 || var4.isEmpty()) {
            return null;
         }

         Set var5 = var1.getSubject().getPrincipals();
         Object[] var6 = var5.toArray();
         String[] var7 = new String[var5.size()];

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var7[var8] = ((Principal)var6[var8]).getName();
         }

         if (verbose) {
            Verbose.log((Object)("Checking BST size = " + var4.size()));
         }

         Iterator var13 = var4.iterator();

         while(true) {
            X509Certificate var10;
            String var11;
            do {
               if (!var13.hasNext()) {
                  if (verbose) {
                     Verbose.log((Object)"No KeyInfo Credentail found on all BST");
                  }

                  return null;
               }

               BinarySecurityToken var9 = (BinarySecurityToken)var13.next();
               var10 = var9.getCertificate();
               var11 = CertUtils.getSubjectCN(var10);
            } while(null == var11);

            for(int var12 = 0; var12 < var7.length; ++var12) {
               if (var11.equals(var7[var12])) {
                  if (verbose) {
                     Verbose.log((Object)("Found X509 credential for " + var11 + " cert SubjectDN name is " + var10.getSubjectDN().getName()));
                  }

                  return new X509Credential(var10, (PrivateKey)null);
               }
            }
         }
      } else if (verbose) {
         Verbose.log((Object)"No KeyInfo Credentail due to null AuthenticatedSubject");
      }

      return null;
   }

   private Object getKeyInfoFromSymmetricKey(WSTContext var1, WSSecurityContext var2, CredentialProvider var3) throws WSSecurityException {
      EncryptedKeyInfoBuilder var4 = new EncryptedKeyInfoBuilder(var2, var3);
      return var4.getEncryptedKeyNode(var1);
   }
}
