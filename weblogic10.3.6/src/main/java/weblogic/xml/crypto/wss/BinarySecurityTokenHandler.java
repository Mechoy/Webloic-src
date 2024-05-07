package weblogic.xml.crypto.wss;

import java.security.AccessController;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.security.KeyPairCredential;
import weblogic.security.PublicCertCredential;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.security.serviceref.ServiceRefUtils;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyProviderFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.BinarySecurityTokenType;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyCredentialProvider;

public class BinarySecurityTokenHandler implements SecurityTokenHandler {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean isAuthorizationToken;
   private String valueType = null;

   public String[] getValueTypes() {
      return WSSConstants.BUILTIN_BST_VALUETYPES;
   }

   public KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2) {
      return KeyProviderFactory.create((BinarySecurityToken)var1);
   }

   public SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      String var4 = var1.getValueType();
      List var5 = var3.getSecurityTokens();
      Purpose var6 = (Purpose)var2.getProperty("weblogic.xml.crypto.wss.provider.Purpose");
      String var7 = var1.getReferenceURI();
      if (var7 != null) {
         return this.getTokenByURI(var7, var5, var6, var3);
      } else {
         KeyIdentifier var8 = var1.getKeyIdentifier();
         if (var8 != null) {
            return this.getTokenByKeyId(var8, var4, var1.getValueType(), var5, var6, var3);
         } else {
            X509IssuerSerial var9 = var1.getIssuerSerial();
            if (var9 != null) {
               return this.getTokenByIssuerSerial(var9, var4, var5, var6, var3);
            } else {
               throw new WSSecurityException("Failed to resolve SecurityToken from STR " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
            }
         }
      }
   }

   protected SecurityToken getTokenByKeyId(KeyIdentifier var1, String var2, String var3, List var4, Purpose var5, WSSecurityContext var6) throws WSSecurityException {
      Iterator var7 = var4.iterator();

      SecurityToken var8;
      do {
         if (!var7.hasNext()) {
            Object var9 = this.getCredential("com.bea.contextelement.xml.KeyIdentifier", var1, var2, var5, var6);
            if (var9 != null) {
               if (BSTUtils.matches(var1, (X509Credential)var9)) {
                  return this.getToken(var9, var2, var6);
               }

               if (BSTUtils.matchesThumbprint(var1, (X509Credential)var9)) {
                  return this.getToken(var9, var2, var6);
               }
            }

            X509Certificate var10 = CertUtils.lookupCertificate(var1.getIdentifier());
            if (var10 != null) {
               return this.getToken(var10, var2, var6);
            }

            throw new WSSecurityException("Failed to resolve security token from key identifier " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }

         var8 = (SecurityToken)var7.next();
      } while(!(var8 instanceof BinarySecurityToken) || !BSTUtils.matches(var1, (X509Credential)var8.getCredential()));

      return this.amend((BinarySecurityToken)var8, var5, var6);
   }

   private SecurityToken getTokenByIssuerSerial(X509IssuerSerial var1, String var2, List var3, Purpose var4, WSSecurityContext var5) throws WSSecurityException {
      Iterator var6 = var3.iterator();

      SecurityToken var7;
      do {
         if (!var6.hasNext()) {
            Object var8 = this.getCredential("com.bea.contextelement.xml.IssuerSerial", var1, var2, var4, var5);
            if (var8 != null && BSTUtils.matches(var1, (X509Credential)var8)) {
               return this.getToken(var8, var2, var5);
            }

            X509Certificate var9 = CertUtils.lookupCertificate(var1.getIssuerName(), var1.getSerialNumber());
            if (var9 != null) {
               return this.getToken(var9, var2, var5);
            }

            throw new WSSecurityException("Failed to resolve security token for issuer serial " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }

         var7 = (SecurityToken)var6.next();
      } while(!(var7 instanceof BinarySecurityToken) || !BSTUtils.matches(var1, (X509Credential)var7.getCredential()));

      return this.amend((BinarySecurityToken)var7, var4, var5);
   }

   protected SecurityToken getToken(X509Certificate var1, String var2, WSSecurityContext var3) throws WSSecurityException {
      SecurityToken var4 = this.getToken((Object)(new X509Credential(var1)), var2, var3);
      return var4;
   }

   protected SecurityToken getToken(Object var1, String var2, WSSecurityContext var3) throws WSSecurityException {
      SecurityTokenHandler var4 = var3.getRequiredTokenHandler(var2);
      return var4.getSecurityToken(var2, var1, (ContextHandler)null);
   }

   protected Object getCredential(String var1, Object var2, String var3, Purpose var4, WSSecurityContext var5) throws WSSecurityException {
      CredentialProvider var6 = null;
      ContextHandler var7 = this.getContextHandler(var5, var1, var2);
      LogUtils.logWss("--->Trying to get credential from token type = " + var3 + " and ctxElementName = " + var1);
      Object var8;
      if (("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var3) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier".equals(var3)) && var1 != null && var1.endsWith("KeyIdentifier")) {
         LogUtils.logWss("Changed token type from " + var3 + " to " + "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
         if (null == var4) {
            var4 = Purpose.VERIFY;
         }

         var3 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
         var6 = var5.getRequiredCredentialProvider(var3);
         if (var6 instanceof WrapperCredentialProvider) {
            var8 = ((WrapperCredentialProvider)var6).getCredentialByKeyIdentifier(var3, (String)null, var7, var4);
            if (var8 != null) {
               return var8;
            }
         }
      } else {
         var6 = var5.getRequiredCredentialProvider(var3);
      }

      var8 = var6.getCredential(var3, (String)null, var7, var4);
      return var8;
   }

   private SecurityToken getTokenByURI(String var1, List var2, Purpose var3, WSSecurityContext var4) throws WSSecurityException {
      String var5 = var1.substring(1);
      Iterator var6 = var2.iterator();

      SecurityToken var7;
      do {
         if (!var6.hasNext()) {
            throw new WSSecurityException("Failed to retrieve token for reference URI " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }

         var7 = (SecurityToken)var6.next();
      } while(!(var7 instanceof BinarySecurityToken) || !var5.equals(var7.getId()));

      return this.amend((BinarySecurityToken)var7, var3, var4);
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      SecurityTokenValidateResult var3 = new SecurityTokenValidateResult(true);
      var3.setDefferedValidation(true);
      return var3;
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      boolean var3 = true;
      BinarySecurityToken var4 = (BinarySecurityToken)var1;
      X509Certificate var5 = ((BinarySecurityToken)var1).getCertificate();
      WSSecurityContext var6 = WSSecurityContext.getSecurityContext(var2);
      boolean var7 = false;
      List var8 = var6.getSignatures(var1);
      if (var8 != null && var8.size() > 0) {
         var7 = true;
         if (!CertUtils.supportsSign(var5)) {
            var3 = false;
         }
      } else if (var6.getIdTokens().contains(var1)) {
         var3 = false;
      }

      List var9 = var6.getEncryptions(var1);
      if (var9 != null && var9.size() > 0) {
         var7 = true;
         if (!CertUtils.supportsKeyEncrypt(var5)) {
            var3 = false;
         }
      }

      if (var3 && var7) {
         BinarySecurityTokenType var10 = BinarySecurityTokenImpl.getBSTType(var1.getValueType());
         var3 = var10.validate(var4, var2);
         if (!var3) {
            return new SecurityTokenValidateResult(var3, var4.getCertificate());
         }
      }

      if (var3) {
         var4.setValidated(true);
      }

      return new SecurityTokenValidateResult(var3);
   }

   protected SecurityToken amend(BinarySecurityToken var1, Purpose var2, WSSecurityContext var3) throws WSSecurityException {
      String var4 = var1.getValueType();
      CredentialProvider var5 = var3.getCredentialProvider(var4);
      if (var5 == null) {
         return var1;
      } else {
         ContextHandler var6 = this.getContextHandler(var3, "com.bea.contextelement.xml.SecurityToken", var1);
         Object var7 = var5.getCredential(var4, (String)null, var6, var2);
         if (var7 != null && BSTUtils.matches(var1, (X509Credential)var7)) {
            SecurityTokenHandler var8 = var3.getRequiredTokenHandler(var4);
            SecurityToken var9 = var8.getSecurityToken(var4, var7, var6);
            var9.setId(var1.getId());
            return var9;
         } else {
            return var1;
         }
      }
   }

   public boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5) {
      if (var1 == null) {
         return false;
      } else {
         Object var6 = var1.getCredential();
         if (var6 == null) {
            LogUtils.logWss("Unable to find credetail for token type " + var2);
            return false;
         } else {
            WSSecurityContext var7 = (WSSecurityContext)var4.getValue("com.bea.contextelement.xml.SecurityInfo");
            if (!(var6 instanceof EncryptedKeyCredentialProvider) && !"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1".equals(var2) && !"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey".equals(var2)) {
               if (var6 instanceof X509Credential && ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3".equals(var2) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1".equals(var2) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier".equals(var2) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1".equals(var2) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7".equals(var2))) {
                  if (Purpose.IDENTITY.equals(var5) && !this.isAuthToken(var1, var7)) {
                     LogUtils.logWss("X509 token is not a auth Token!");
                     return false;
                  } else {
                     X509Credential var8 = (X509Credential)var1.getCredential();
                     if (Purpose.DECRYPT.equals(var5) && var8.getPrivateKey() == null) {
                        LogUtils.logWss("X509 token doesn't match because purpose is DECRYPT and private key is null.");
                        return false;
                     } else {
                        X509Certificate var9 = var8.getCertificate();
                        if (Purpose.ENCRYPT.equals(var5) && !CertUtils.supportsKeyEncrypt(var9)) {
                           LogUtils.logWss("X509 token does not match because purpose is ENCRYPT but the certificate does not support key encryption.");
                           return false;
                        } else if (Purpose.SIGN.equals(var5) && !CertUtils.supportsSign(var9)) {
                           LogUtils.logWss("X509 token does not match because purpose is SIGN butthe certificate does not support sign.");
                           return false;
                        } else if (var3 != null && !var9.getIssuerX500Principal().getName().equals(var3)) {
                           LogUtils.logWss("X509 token does not match because its issuerName " + var9.getIssuerX500Principal().getName() + " does not match required " + "issuerName " + var3);
                           return false;
                        } else {
                           return BSTUtils.matches(var8, var4);
                        }
                     }
                  }
               } else {
                  LogUtils.logWss("X509 token doesn't match. No token or wrong token type " + var2);
                  return false;
               }
            } else {
               LogUtils.logWss("Checking Encrypted Key with token type " + var2);
               if (Purpose.IDENTITY.equals(var5) && !this.isAuthToken(var1, var7)) {
                  LogUtils.logWss("X509 token is not a auth Token!");
                  return false;
               } else {
                  return true;
               }
            }
         }
      }
   }

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      return var2 == null ? null : new BinarySecurityTokenImpl(var1, var2, var3);
   }

   public SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      SecurityToken var5 = this.getTokenFromContext(var1, var2, var3, var4);
      if (var5 == null) {
         var5 = this.getTokenFromSubject(var1, var2, var3, var4);
      }

      return var5;
   }

   private SecurityToken getTokenFromSubject(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      Object var5 = null;
      X509Credential var7;
      if (var3.equals(Purpose.ENCRYPT)) {
         LogUtils.logWss("Trying to get trusted cert credential from PKICredMapper.");
         var5 = ServiceRefUtils.getCredential(kernelID, "weblogic.pki.TrustedCertificate", var2, var4);
         if (var5 != null) {
            PublicCertCredential var6 = (PublicCertCredential)var5;
            var7 = new X509Credential((X509Certificate)var6.getCertificate());
            return this.getSecurityToken(var1, var7, var4);
         }
      } else {
         LogUtils.logWss("Trying to get key pair credential from PKICredMapper.");
         var5 = ServiceRefUtils.getCredential(kernelID, "weblogic.pki.Keypair", var2, var4);
         if (var5 != null) {
            KeyPairCredential var8 = (KeyPairCredential)var5;
            var7 = new X509Credential((X509Certificate)var8.getCertificate(), (PrivateKey)var8.getKey());
            return this.getSecurityToken(var1, var7, var4);
         }
      }

      return null;
   }

   private SecurityToken getTokenFromContext(String var1, String var2, Purpose var3, ContextHandler var4) {
      LogUtils.logWss("Trying to get token for token type " + var1 + " and purpose " + var3 + " from context.");
      WSSecurityInfo var5 = (WSSecurityInfo)var4.getValue("com.bea.contextelement.xml.SecurityInfo");
      List var6 = var5.getSecurityTokens();
      if (var6 == null) {
         return null;
      } else {
         Iterator var7 = var6.iterator();

         while(true) {
            while(true) {
               SecurityToken var8;
               do {
                  if (!var7.hasNext()) {
                     return null;
                  }

                  var8 = (SecurityToken)var7.next();
               } while(!var8.getValueType().equals(var1));

               PrivateKey var9 = var8.getPrivateKey();
               if (Purpose.ENCRYPT.equals(var3)) {
                  if (var9 != null) {
                     LogUtils.logWss("Token for token type " + var1 + " from context" + " doesn't match because purpose is ENCRYPT and token contains " + "private key.");
                     continue;
                  }

                  List var10 = var5.getEncryptions(var8);
                  if (var10.size() != 0) {
                     LogUtils.logWss("Token for token type " + var1 + " from context " + "doesn't match because purpose is ENCRYPT and token has been " + "used to encrypt request.");
                     continue;
                  }
               }

               if (this.matches(var8, var1, var2, var4, var3)) {
                  LogUtils.logWss("Got token for token type " + var1 + " and purpose " + var3 + " from context.");
                  return var8;
               }

               LogUtils.logWss("Token for token type " + var1 + " and purpose " + var3 + " from context doesn't match.");
            }
         }
      }
   }

   public SecurityToken newSecurityToken(Node var1) throws MarshalException {
      BinarySecurityTokenImpl var2 = new BinarySecurityTokenImpl();

      try {
         var2.unmarshal(var1);
      } catch (weblogic.xml.dom.marshal.MarshalException var4) {
         throw new MarshalException("Failed to unmarshal BinarySecurityToken.", var4);
      }

      this.valueType = var2.getValueType();
      return var2;
   }

   public QName[] getQNames() {
      return WSSConstants.BUILTIN_BST_QNAMES;
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      return var3 == null ? null : new BinarySecurityTokenReference(var1, var2, var3);
   }

   public SecurityTokenReference newSecurityTokenReference(Node var1) {
      return new BinarySecurityTokenReference();
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      return this.getSubject(var1, var3);
   }

   public Subject getSubject(SecurityToken var1, WSSecurityContext var2) throws WSSecurityException {
      if (!this.isAuthToken(var1, var2)) {
         return null;
      } else {
         String var3 = SecurityServiceManager.getDefaultRealmName();
         BinarySecurityToken var4 = (BinarySecurityToken)var1;
         X509Certificate[] var5 = new X509Certificate[]{var4.getCertificate()};

         try {
            Subject var6 = SecurityUtils.assertIdentity(var5, var3).getSubject();
            if (null != var6) {
               var2.setProperty("BinarySecurityTokenHandler.AuthenticatedSubject.Cert", var4.getCertificate());
               LogUtils.logWss("BinarySecurityTokenHandler.AuthenticatedSubject.Cert saved for " + var4.getCertificate());
            }

            return var6;
         } catch (LoginException var7) {
            throw new WSSecurityException("Failed to derive subject from token." + var7, WSSConstants.FAILURE_AUTH);
         }
      }
   }

   private boolean isAuthToken(SecurityToken var1, WSSecurityContext var2) {
      if (!this.isAuthorizationToken) {
         return false;
      } else {
         boolean var3 = Boolean.getBoolean("weblogic.xml.crypto.wss.X509AuthWithoutSig");
         if (var3) {
            return true;
         } else {
            List var4 = var2.getSignatures(var1);
            return var4 != null && var4.size() != 0;
         }
      }
   }

   public void setAuthorizationToken(boolean var1) {
      this.isAuthorizationToken = var1;
   }

   public boolean isAuthorizationToken() {
      return this.isAuthorizationToken;
   }

   protected ContextHandler getContextHandler(WSSecurityInfo var1, String var2, Object var3) {
      SecurityTokenContextHandler var4 = new SecurityTokenContextHandler(var1);
      var4.addContextElement(var2, var3);
      return var4;
   }
}
