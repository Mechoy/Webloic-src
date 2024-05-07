package weblogic.xml.crypto.common.keyinfo;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorException;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyName;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyValue;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMDecryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.WSSecurityInfo;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.security.utils.Utils;

public class KeyResolver extends KeySelector {
   public static final String VERBOSE_PROPERTY = "weblogic.xml.crypto.keyinfo.verbose";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.keyinfo.verbose");
   private static final boolean DEBUG = false;
   private final List keyProviders;
   protected static Accessor BY_ALG_AND_PURPOSE = new Accessor() {
      public KeySelectorResult getKey(Object var1, String var2, KeySelector.Purpose var3, KeyProvider var4) {
         LogUtils.logKeyInfo("Trying to select key by mechanism: BY_ALG_AND_PURPOSE");
         LogUtils.logKeyInfo("ALG: " + var2 + "purpose: " + var3);
         return var4.getKey(var2, var3);
      }
   };
   protected static Accessor BY_TOKEN_REFERENCE = new Accessor() {
      public KeySelectorResult getKey(Object var1, String var2, KeySelector.Purpose var3, KeyProvider var4) {
         LogUtils.logKeyInfo("Trying to select key by mechanism: BY_TOKEN_REFERENCE");
         SecurityTokenReference var5 = (SecurityTokenReference)var1;
         KeySelectorResult var6 = null;
         String var7 = var5.getReferenceURI();
         if (var7 != null) {
            LogUtils.logKeyInfo("Trying to select key by uri " + var7);
            var6 = var4.getKeyByURI(var7, var2, var3);
         }

         if (var6 != null) {
            LogUtils.logKeyInfo("Key selected by URI");
            return var6;
         } else {
            KeyIdentifier var8 = var5.getKeyIdentifier();
            if (var8 != null) {
               LogUtils.logKeyInfo("Trying to select key by KeyIdentifier " + Utils.base64(var8.getIdentifier()));
               var6 = var4.getKeyByIdentifier(var8.getIdentifier(), var2, var3);
            }

            if (var6 != null) {
               LogUtils.logKeyInfo("Key selected by KeyIdentifier");
               return var6;
            } else {
               X509IssuerSerial var9 = var5.getIssuerSerial();
               if (var9 != null) {
                  String var10 = var9.getIssuerName();
                  BigInteger var11 = var9.getSerialNumber();
                  LogUtils.logKeyInfo("Trying to select key by IssuerSerial " + var10 + ", " + var11);
                  var6 = var4.getKeyByIssuerSerial(var10, var11, var2, var3);
               }

               if (var6 != null) {
                  LogUtils.logKeyInfo("Key selected by KeyIdentifier");
                  return var6;
               } else {
                  var6 = var4.getKeyBySTR(var5, var2, var3);
                  return var6;
               }
            }
         }
      }
   };
   protected static Accessor BY_KEY_NAME = new Accessor() {
      public KeySelectorResult getKey(Object var1, String var2, KeySelector.Purpose var3, KeyProvider var4) {
         LogUtils.logKeyInfo("Trying to select key by mechanism: BY_KEY_NAME");
         String var5 = (String)var1;
         return var4.getKeyByName(var5, var2, var3);
      }
   };

   private KeyResolver(List var1) {
      this.keyProviders = var1;
   }

   public KeyResolver() {
      this((List)(new ArrayList()));
   }

   public KeyResolver(KeyProvider[] var1) {
      this((List)(new ArrayList()));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         KeyProvider var3 = var1[var2];
         this.keyProviders.add(var3);
      }

   }

   public KeyResolver copy() {
      ArrayList var1 = new ArrayList(this.keyProviders);
      return new KeyResolver(var1);
   }

   public void addKeyProvider(KeyProvider var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Provider cannot be null");
      } else {
         this.keyProviders.add(0, var1);
      }
   }

   public boolean removeKeyProvider(KeyProvider var1) {
      return this.keyProviders.remove(var1);
   }

   public KeyProvider[] getKeyProviders() {
      KeyProvider[] var1 = new KeyProvider[this.keyProviders.size()];
      this.keyProviders.toArray(var1);
      return var1;
   }

   public KeySelectorResult select(KeyInfo var1, KeySelector.Purpose var2, AlgorithmMethod var3, XMLCryptoContext var4) throws KeySelectorException {
      String var5 = var3.getAlgorithm();
      KeyProvider[] var6 = this.getKeyProviders();
      this.log(var5, var2, var1, var6);
      if (var1 != null) {
         LogUtils.logKeyInfo("Trying to get key using KeyInfo");
         List var7 = var1.getContent();

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            Object var9 = var7.get(var8);
            if (var9 instanceof EncryptedKey) {
               return this.getKeyFromEncryptedKey((EncryptedKey)var9, var3, var4);
            }

            if (var9 instanceof SecurityTokenReference) {
               return this.getKeyFromSTR((SecurityTokenReference)var9, var4, var2, var5);
            }

            if (var9 instanceof KeyName) {
               WSSecurityContext var10 = this.getSecurityContext(var4);
               KeySelectorResult var11 = this.getKeyByKeyName((KeyName)var9, var2, var5, var4, var10);
               if (var11 != null) {
                  return var11;
               }
            }

            if (var9 instanceof KeyValue) {
               KeySelectorResult var12 = this.getKeyFromKeyValue((KeyValue)var9, var2, var5);
               if (var12 != null) {
                  return var12;
               }
            }
         }
      }

      LogUtils.logKeyInfo("No key found using KeyInfo");
      return this.getKey((Object)null, BY_ALG_AND_PURPOSE, var5, var2, this.getKeyProviders());
   }

   private KeySelectorResult getKeyFromKeyValue(KeyValue var1, KeySelector.Purpose var2, String var3) throws KeySelectorException {
      try {
         PublicKey var4 = var1.getPublicKey();
         LogUtils.logKeyInfo("Trying to get key from public key" + var4);
         if (KeyUtils.serves(KeyUtils.getPurposes(var4), var2) && KeyUtils.supports(KeyUtils.getAlgorithms(var4), var3)) {
            LogUtils.logKeyInfo("Selecting key by mechanism: public key from KeyInfo");
            return new KeySelectorResultImpl(var4);
         }
      } catch (KeyException var6) {
         throw new KeySelectorException("Failed to get public key from KeyValue.", var6);
      }

      LogUtils.logKeyInfo("No key found using KeyInfo KeyValue");
      return null;
   }

   private KeySelectorResult getKeyByKeyName(KeyName var1, KeySelector.Purpose var2, String var3, XMLCryptoContext var4, WSSecurityContext var5) throws KeySelectorException {
      if (var5 != null) {
         this.setupKeyNameKeyProvider(var1.getName(), var2, var5, var4);
      }

      KeySelectorResult var6 = this.getKey((Object)var1.getName(), BY_KEY_NAME, var3, var2, this.getKeyProviders());
      if (var6 == null) {
         LogUtils.logKeyInfo("No key found using KeyInfo KeyName.");
      }

      return var6;
   }

   private void setupKeyNameKeyProvider(String var1, KeySelector.Purpose var2, WSSecurityContext var3, XMLCryptoContext var4) throws KeySelectorException {
      try {
         Object var5 = null;
         String var6 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
         SecurityTokenHandler var7 = var3.getTokenHandler(var6);
         if (var7 != null) {
            var5 = this.getCredential("weblogic.xml.crypto.keyinfo.keyname", var1, var6, weblogic.xml.crypto.wss.provider.Purpose.convert(var2), var3);
         }

         if (var5 == null) {
            var6 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1";
            SecurityTokenHandler var8 = var7;
            var7 = var3.getTokenHandler(var6);
            if (var7 != null) {
               var5 = this.getCredential("weblogic.xml.crypto.keyinfo.keyname", var1, var6, weblogic.xml.crypto.wss.provider.Purpose.convert(var2), var3);
            } else {
               var7 = var8;
            }
         }

         if (var7 != null && var5 != null) {
            SecurityToken var12 = var7.getSecurityToken(var6, var5, (ContextHandler)null);
            MessageContext var9 = (MessageContext)var4.getProperty("javax.xml.rpc.handler.MessageContext");
            this.validateAndAddToken(var7, var12, var9, var2, var3);
            KeyProvider var10 = var7.getKeyProvider(var12, var9);
            this.addKeyProvider(var10);
         }

      } catch (WSSecurityException var11) {
         throw new KeySelectorException("Failed to resolve key using KeyName " + var1, var11);
      }
   }

   private void log(String var1, KeySelector.Purpose var2, KeyInfo var3, KeyProvider[] var4) {
      if (VERBOSE) {
         LogUtils.logKeyInfo("Selecting key for\nalgorithm: " + var1 + "\npurpose: " + var2 + "\nKeyInfo: " + var3 + "\nfrom providers: ");

         for(int var5 = 0; var5 < var4.length; ++var5) {
            KeyProvider var6 = var4[var5];
            if (var6 != null) {
               LogUtils.logKeyInfo(var6.toString());
            }
         }
      }

   }

   private KeySelectorResult getKeyFromEncryptedKey(EncryptedKey var1, AlgorithmMethod var2, XMLCryptoContext var3) throws KeySelectorException {
      DOMDecryptContext var4 = new DOMDecryptContext(var3.getKeySelector(), (Element)null);

      Key var5;
      try {
         var5 = var1.decryptKey(var4, var2);
      } catch (XMLEncryptionException var7) {
         throw new KeySelectorException(var7);
      }

      this.addKeyProvider(new SecretKeyProvider(var5, var1.getCarriedKeyName(), (byte[])null, var1.getId()));
      KeySelectorResultImpl var6 = new KeySelectorResultImpl(var5);
      var6.setSecurityToken(((KeySelectorResultImpl)var4.getProperty("weblogic.xml.crypto.ksr")).getSecurityToken());
      var4.setProperty("weblogic.xml.crypto.ksr", var6);
      return var6;
   }

   private KeySelectorResult getKeyFromSTR(SecurityTokenReference var1, XMLCryptoContext var2, KeySelector.Purpose var3, String var4) throws KeySelectorException {
      LogUtils.logKeyInfo("Trying to get key using STR");
      MessageContext var6 = (MessageContext)var2.getProperty("javax.xml.rpc.handler.MessageContext");
      WSSecurityContext var7 = null;
      SecurityTokenReference var8;
      if (var6 != null) {
         var7 = WSSecurityContext.getSecurityContext(var6);
         if (!KeySelector.Purpose.DECRYPT.equals(var3) && !KeySelector.Purpose.VERIFY.equals(var3)) {
            this.setupKeyProviderFromContext(var1, var7, var6, var3);
         } else {
            var8 = this.getSecurityTokenReference(var1, var7);
            if (null == var8) {
               this.setupKeyProviderFromContext(var1, var7, var6, var3);
            } else {
               this.setupKeyProviderFromContext(var8, var7, var6, var3);
            }
         }
      } else if (KeySelector.Purpose.DECRYPT.equals(var3) || KeySelector.Purpose.VERIFY.equals(var3)) {
         var7 = (WSSecurityContext)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");
         if (var7 != null) {
            KeyResolverMessageContext var9 = new KeyResolverMessageContext();
            var9.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var7);
            var8 = this.getSecurityTokenReference(var1, var7);
            if (null == var8) {
               this.setupKeyProviderFromContext(var1, var7, var9, var3);
            } else {
               this.setupKeyProviderFromContext(var8, var7, var9, var3);
            }
         }
      }

      KeySelectorResult var5 = this.getKey((Object)var1, BY_TOKEN_REFERENCE, var4, var3, this.getKeyProviders());
      if (var5 != null) {
         return var5;
      } else {
         throw new KeySelectorException("Failed to resolve key using SecurityTokenReference " + var1);
      }
   }

   private void setupKeyProviderFromContext(SecurityTokenReference var1, WSSecurityContext var2, MessageContext var3, KeySelector.Purpose var4) throws KeySelectorException {
      String var5 = this.getValueType(var1, var2);
      SecurityTokenHandler var6 = null;
      SecurityToken var7 = null;

      try {
         var6 = var2.getRequiredTokenHandler(var5);
         var3.setProperty("weblogic.xml.crypto.wss.provider.Purpose", weblogic.xml.crypto.wss.provider.Purpose.convert(var4));
         var7 = var6.getSecurityToken(var1, var3);
         if (var7 != null) {
            this.validateAndAddToken(var6, var7, var3, var4, var2);
            KeyProvider var8 = var6.getKeyProvider(var7, var3);
            this.addKeyProvider(var8);
            LogUtils.logKeyInfo("Added key provider " + var8);
         }

      } catch (WSSecurityException var9) {
         throw new KeySelectorException(var9);
      }
   }

   private void validateAndAddToken(SecurityTokenHandler var1, SecurityToken var2, MessageContext var3, KeySelector.Purpose var4, WSSecurityContext var5) throws WSSecurityException {
      SecurityTokenValidateResult var6 = var1.validateUnmarshalled(var2, var3);
      if (!var6.status()) {
         throw new WSSecurityException("Security token failed to validate.", var6, WSSConstants.FAILURE_TOKEN_INVALID);
      } else {
         var3.removeProperty("weblogic.xml.crypto.wss.provider.Purpose");
         if (!KeySelector.Purpose.DECRYPT.equals(var4)) {
            var5.addSecurityToken(var2);
         }

      }
   }

   private WSSecurityContext getSecurityContext(XMLCryptoContext var1) {
      MessageContext var2 = (MessageContext)var1.getProperty("javax.xml.rpc.handler.MessageContext");
      WSSecurityContext var3 = null;
      if (var2 != null) {
         var3 = WSSecurityContext.getSecurityContext(var2);
      }

      return var3;
   }

   private String getValueType(SecurityTokenReference var1, WSSecurityContext var2) throws KeySelectorException {
      String var3 = var1.getValueType();
      if (var3 != null) {
         return var3;
      } else {
         String var4 = var1.getReferenceURI();
         Element var5 = var2.getElementById(var4.substring(1));
         if (null == var5) {
            throw new KeySelectorException("Failed to unmarshal STR, token is null for uri =" + var4);
         } else {
            QName var6 = DOMUtils.getQName(var5);
            if (WSSConstants.BST_QNAME.equals(var6)) {
               return DOMUtils.getAttributeValue(var5, WSSConstants.VALUE_TYPE_QNAME);
            } else if (WSSConstants.UNT_QNAME.equals(var6)) {
               return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
            } else if (WSCConstants.DK_QNAME.equals(var6)) {
               return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
            } else if (weblogic.wsee.security.wssc.v200502.WSCConstants.DK_QNAME.equals(var6)) {
               return "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
            } else if (SecurityImpl.ENCRYPTED_KEY_QNAME.equals(var6)) {
               throw new KeySelectorException("Failed to unmarshal STR from a encrypted key -- uri =" + var4);
            } else if (WSSConstants.STR_QNAME.equals(var6)) {
               throw new KeySelectorException("Failed to unmarshal STR from a STR-- uri =" + var4);
            } else {
               throw new KeySelectorException("Failed to unmarshal STR, token type not recognized -- uri =" + var4 + " QName = " + var6.toString());
            }
         }
      }
   }

   private SecurityTokenReference getSecurityTokenReference(SecurityTokenReference var1, WSSecurityContext var2) {
      if (var1.getValueType() != null) {
         return null;
      } else {
         String var3 = var1.getReferenceURI();
         Element var4 = var2.getElementById(var3.substring(1));
         QName var5 = DOMUtils.getQName(var4);
         if (WSSConstants.STR_QNAME.equals(var5)) {
            return (SecurityTokenReference)var4;
         } else {
            if (SecurityImpl.ENCRYPTED_KEY_QNAME.equals(var5)) {
               XMLEncryptionFactory var6 = var2.getEncryptionFactory();
               KeySelector var7 = var2.getKeySelector();
               DOMDecryptContext var8 = new DOMDecryptContext(var7, var4);
               MessageContext var9 = var2.getMessageContext();
               var8.setProperty("javax.xml.rpc.handler.MessageContext", var9);
               var8.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var2);

               try {
                  EncryptedKey var10 = (EncryptedKey)var6.unmarshalEncryptedType(var8);
                  if (null != var10.getKeyInfo()) {
                     KeyInfo var11 = var10.getKeyInfo();
                     List var12 = var11.getContent();
                     if (null != var12) {
                        Iterator var13 = var12.iterator();

                        while(var13.hasNext()) {
                           Object var14 = var13.next();
                           if (var14 instanceof SecurityTokenReference) {
                              return (SecurityTokenReference)var14;
                           }
                        }
                     }
                  }
               } catch (Exception var15) {
                  LogUtils.logKeyInfo("5  exception found STR in key info " + var15.toString());
                  return null;
               }
            }

            return null;
         }
      }
   }

   private String providersToString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[");
      Iterator var2 = this.keyProviders.iterator();

      while(var2.hasNext()) {
         KeyProvider var3 = (KeyProvider)var2.next();
         var1.append(var3).append(" ");
      }

      var1.append("]");
      return var1.toString();
   }

   private KeySelectorResult getKey(Iterator var1, Accessor var2, String var3, KeySelector.Purpose var4, KeyProvider[] var5) {
      KeySelectorResult var6;
      Object var7;
      for(var6 = null; var1.hasNext() && var6 == null; var6 = this.getKey(var7, var2, var3, var4, var5)) {
         var7 = var1.next();
      }

      return var6;
   }

   private KeySelectorResult getKey(Object var1, Accessor var2, String var3, KeySelector.Purpose var4, KeyProvider[] var5) {
      KeySelectorResult var6 = null;

      for(int var7 = 0; var6 == null && var7 < var5.length; ++var7) {
         KeyProvider var8 = var5[var7];
         if (null == var8) {
            LogUtils.logKeyInfo("Trying to get key from key object " + var1 + " but the  KeyProvider is null");
         } else {
            LogUtils.logKeyInfo("Trying to get key from key object " + var1 + " and KeyProvider " + var8);
            var6 = var2.getKey(var1, var3, var4, var8);
            if (var6 != null) {
               return var6;
            }
         }
      }

      return var6;
   }

   private Object getCredential(String var1, Object var2, String var3, weblogic.xml.crypto.wss.provider.Purpose var4, WSSecurityContext var5) throws WSSecurityException {
      CredentialProvider var6 = var5.getRequiredCredentialProvider(var3);
      ContextHandler var7 = this.getContextHandler(var5, var1, var2);
      Object var8 = var6.getCredential(var3, (String)null, var7, var4);
      return var8;
   }

   protected ContextHandler getContextHandler(WSSecurityInfo var1, String var2, Object var3) {
      SecurityTokenContextHandler var4 = new SecurityTokenContextHandler(var1);
      var4.addContextElement(var2, var3);
      return var4;
   }

   private class KeyResolverMessageContext implements MessageContext {
      private HashMap props;

      private KeyResolverMessageContext() {
         this.props = new HashMap();
      }

      public void setProperty(String var1, Object var2) {
         this.props.put(var1, var2);
      }

      public Object getProperty(String var1) {
         return this.props.get(var1);
      }

      public void removeProperty(String var1) {
         this.props.remove(var1);
      }

      public boolean containsProperty(String var1) {
         return this.props.containsKey(var1);
      }

      public Iterator getPropertyNames() {
         return this.props.keySet().iterator();
      }

      // $FF: synthetic method
      KeyResolverMessageContext(Object var2) {
         this();
      }
   }

   protected interface Accessor {
      KeySelectorResult getKey(Object var1, String var2, KeySelector.Purpose var3, KeyProvider var4);
   }
}
