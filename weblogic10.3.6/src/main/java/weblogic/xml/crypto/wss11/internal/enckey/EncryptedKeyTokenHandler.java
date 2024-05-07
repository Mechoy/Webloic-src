package weblogic.xml.crypto.wss11.internal.enckey;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.SecretKeyProvider;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.TBEKey;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.dom.DOMEncryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.EncryptedKeyUtils;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.WSSecurityInfo;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.STRType;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;

public class EncryptedKeyTokenHandler implements SecurityTokenHandler {
   private static final SecurityTokenValidateResult TRUE = new SecurityTokenValidateResult(true);

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      if (var2 instanceof EncryptedKeyProvider) {
         EncryptedKeyProvider var25 = (EncryptedKeyProvider)var2;
         return new EncryptedKeyToken(var25, "");
      } else {
         try {
            Object var4 = null;
            String var5 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
            Key var6 = (Key)var2;
            WSSecurityContext var7 = (WSSecurityContext)var3.getValue("com.bea.contextelement.xml.SecurityInfo");
            CredentialProvider var8 = var7.getCredentialProvider(var5);
            if (var8 != null) {
               var4 = var8.getCredential(var5, (String)null, var3, Purpose.ENCRYPT);
            }

            if (var4 == null) {
               var5 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1";
               CredentialProvider var9 = var8;
               var8 = var7.getCredentialProvider(var5);
               if (var8 != null) {
                  var4 = var8.getCredential(var5, (String)null, var3, Purpose.ENCRYPT);
               } else {
                  var8 = var9;
               }
            }

            if (var8 == null) {
               throw new WSSecurityException("EncryptedKeyTokenHandler does not know how to handle: " + var2 + " when the x509 CredProvider not found");
            } else if (var4 != null) {
               SecurityTokenHandler var26 = var7.getTokenHandler(var5);
               SecurityToken var10 = var26.getSecurityToken(var5, var4, var3);
               SecurityTokenReference var11 = null;
               if (var3.getValue("weblogic.wsee.dk.base_token_referece_type") != null) {
                  STRType var12 = (STRType)var3.getValue("weblogic.wsee.dk.base_token_referece_type");
                  var11 = var26.getSTR(WSSConstants.KEY_IDENTIFIER_QNAME, var12.getValueType(), var10);
               } else {
                  var11 = var26.getSTR(WSSConstants.KEY_IDENTIFIER_QNAME, var5, var10);
               }

               String var27 = DOMUtils.generateId("encKey");
               EncryptedKeyToken var13 = new EncryptedKeyToken(var6, var27);
               var7.addSecurityToken(var13);
               KeyProvider var14 = var26.getKeyProvider(var10, var7.getMessageContext());
               if (var14 != null) {
                  var7.addKeyProvider(var14);
               }

               KeySelector var15 = var7.getKeySelector();
               EncryptionMethod var16 = (EncryptionMethod)var3.getValue("weblogic.wsee.ek.keywrap_method");
               Key var17 = EncryptedKeyUtils.getKey(var15, var14, var16);
               KeyInfo var18 = EncryptedKeyUtils.getKeyInfo(var7, var11);
               TBEKey var19 = new TBEKey(var6);
               EncryptedKey var20 = var7.getEncryptionFactory().newEncryptedKey(var19, var16, var18, (EncryptionProperties)null, new ArrayList(), var27, (String)null, (String)null, (CipherReference)null);
               DOMEncryptContext var21 = new DOMEncryptContext(var17);
               var13.setDOMEncryptContext(var21);

               try {
                  var13.setEncryptedKey(var20);
               } catch (XMLEncryptionException var23) {
               }

               return var13;
            } else {
               return this.getSecurityToken(var1, (String)null, Purpose.ENCRYPT, var3);
            }
         } catch (ClassCastException var24) {
            throw new WSSecurityException("EncryptedKeyTokenHandler does not know how to handle the class of: " + var2);
         }
      }
   }

   public SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      if ("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1".equals(var1) || "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey".equals(var1)) {
         WSSecurityInfo var5 = (WSSecurityInfo)var4.getValue("com.bea.contextelement.xml.SecurityInfo");
         List var6 = var5.getSecurityTokens("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
         if (var6.size() == 1) {
            return (SecurityToken)var6.get(0);
         }

         if (var6.size() > 1) {
            throw new WSSecurityException("Failed to get EncryptedKey token, more than one in context.");
         }
      }

      return null;
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      return new EncryptedKeySTR(var1, (EncryptedKeyToken)var3);
   }

   public QName[] getQNames() {
      return new QName[0];
   }

   public String[] getValueTypes() {
      return WSS11Constants.ENCRYPTED_KEY_VALUE_TYPES;
   }

   public SecurityToken newSecurityToken(Node var1) throws MarshalException {
      return null;
   }

   public SecurityTokenReference newSecurityTokenReference(Node var1) throws weblogic.xml.dom.marshal.MarshalException {
      EncryptedKeySTR var2 = new EncryptedKeySTR();
      var2.unmarshal(var1);
      return var2;
   }

   public KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2) {
      Object var3 = null;
      if (var1 instanceof EncryptedKeyToken) {
         var3 = ((EncryptedKeyToken)var1).getKeyProvider();
      }

      if (var3 == null && var1.getSecretKey() != null) {
         var3 = new SecretKeyProvider(var1.getSecretKey(), (String)null, (byte[])null, "#" + var1.getId(), var1);
      }

      return (KeyProvider)var3;
   }

   public SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      List var4 = var3.getSecurityTokens();
      String var5 = var1.getReferenceURI();
      if (var5 != null) {
         return this.getTokenByURI(var5, var4);
      } else {
         byte[] var6 = var1.getKeyIdentifier().getIdentifier();
         if (var6 != null) {
            return this.getTokenByKeyId(var6, var4);
         } else {
            throw new WSSecurityException("Failed to resolve EncryptedKey STR.", WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }
      }
   }

   private SecurityToken getTokenByKeyId(byte[] var1, List var2) throws WSSecurityException {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         SecurityToken var4 = (SecurityToken)var3.next();

         try {
            if (var4 instanceof EncryptedKeyToken) {
               Iterator var5 = ((EncryptedKeyToken)var4).getKeyIdentifiers().iterator();

               while(var5.hasNext()) {
                  byte[] var6 = (byte[])var5.next();
                  if (Arrays.equals(var1, var6)) {
                     return var4;
                  }
               }
            }
         } catch (XMLEncryptionException var7) {
            throw new WSSecurityException("Failed to retrieve token for key identifier " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }
      }

      throw new WSSecurityException("Failed to retrieve token for key identifier " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return TRUE;
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      return TRUE;
   }

   public boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5) {
      return var2.equals(var1.getValueType());
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return null;
   }

   private SecurityToken getTokenByURI(String var1, List var2) throws WSSecurityException {
      String var3 = var1.substring(1);
      Iterator var4 = var2.iterator();

      SecurityToken var5;
      do {
         if (!var4.hasNext()) {
            throw new WSSecurityException("Failed to retrieve token for reference URI " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }

         var5 = (SecurityToken)var4.next();
      } while(!(var5 instanceof EncryptedKeyToken) || !var3.equals(var5.getId()));

      return var5;
   }
}
