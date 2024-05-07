package weblogic.xml.crypto.wss11.internal;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.dsig.ReferenceImpl;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.TBEKey;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.dom.DOMEncryptContext;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeySTR;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;
import weblogic.xml.saaj.SOAPConstants;

public class SecurityBuilderImpl extends weblogic.xml.crypto.wss.SecurityBuilderImpl implements SecurityBuilder {
   private final boolean DEBUG = false;
   private String layout = "Strict";
   private String version = "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd";
   private boolean processingStarted = false;
   private static final List EMPTY_LIST = new ArrayList();
   private static final List supportedLayouts = new ArrayList();

   public SecurityBuilderImpl(WSSecurityContext var1, Element var2) {
      super(var1, var2);
   }

   public SecurityBuilderImpl(WSSecurityContext var1) {
      super(var1);
   }

   public void setLayout(String var1) {
      if (this.processingStarted && !this.layout.equals(var1)) {
         throw new IllegalStateException("Layout can not be changed after SecurityBuilder processing started.");
      } else {
         this.checkLayout(var1);
         this.layout = var1;
      }
   }

   public static void setEncryptBeforeSign(ContextHandler var0, boolean var1) {
      ((SecurityTokenContextHandler)var0).addContextElement("weblogic.wsee.security.encrypt_sign", new Boolean(var1));
   }

   public void setWSSVersion(String var1) {
      if (this.processingStarted && !this.version.equals(var1)) {
         throw new IllegalStateException("WSS version can not be changed after SecurityBuilder processing started.");
      } else if (!"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd".equals(var1) && !"http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd".equals(var1)) {
         throw new IllegalArgumentException("Unsupported WSS version: " + var1);
      } else {
         this.version = var1;
      }
   }

   public SignatureConfirmation[] addSignatureConfirmation(String[] var1, ContextHandler var2) throws MarshalException, WSSecurityException {
      this.ensureWSS11();
      this.setLayout(var2);
      this.start();
      SignatureConfirmation[] var3;
      if (var1.length == 0) {
         var3 = new SignatureConfirmation[]{this.addSigConf((String)null, var2)};
      } else {
         var3 = new SignatureConfirmation[var1.length];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            String var5 = var1[var4];
            if (var5 == null) {
               throw new NullPointerException();
            }

            var3[var4] = this.addSigConf(var5, var2);
         }
      }

      return var3;
   }

   public void addSignatureAndEncryption(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, String var6, boolean var7, ContextHandler var8) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.addSignAndEncInternal(var1, var2, var3, var4, var5, (List)null, var6, var7, var8);
   }

   protected EncryptedKeyToken addEncryptedKeyToken(Key var1, String var2) {
      EncryptedKeyToken var3 = new EncryptedKeyToken(var1, var2);
      this.securityCtx.addSecurityToken(var3);
      return var3;
   }

   public void addSignatureAndEncryption(SignedInfo var1, List var2, EncryptionMethod var3, ContextHandler var4) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.ensureWSS11();
      this.setLayout(var4);
      this.start();
      boolean var5 = SecurityImpl.isEncryptBeforeSign(var4);
      EncryptedKeyToken var6 = (EncryptedKeyToken)this.getToken("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", (String)null, var4);
      EncryptedKeySTR var7;
      KeyInfo var8;
      DOMEncryptContext var9;
      List var10;
      if (var5) {
         var7 = new EncryptedKeySTR(WSSConstants.KEY_IDENTIFIER_QNAME, var6);
         var8 = this.getKeyInfo(var7);
         var9 = new DOMEncryptContext((Key)null);
         var9.setKeySelector(this.securityCtx.getKeySelector());
         var10 = this.encryptData(var2, var9, var3, var8, true, var4);
         this.addReferenceList(var10, var6, var4);
         this.addSignatureWithToken(var1, var7, (Boolean)null, var4);
         this.moveTimestampToTop();
      } else {
         var7 = new EncryptedKeySTR(WSSConstants.KEY_IDENTIFIER_QNAME, var6);
         this.addSignatureWithToken(var1, var7, (Boolean)null, var4);
         var8 = this.getKeyInfo(var7);
         var9 = new DOMEncryptContext((Key)null);
         var9.setKeySelector(this.securityCtx.getKeySelector());
         var10 = this.encryptData(var2, var9, var3, var8, true, var4);
         if (!"LaxTimestampFirst".equals(this.layout)) {
            ((SecurityTokenContextHandler)var4).addContextElement("weblogic.wsee.security.move_node_to_top", "true");
         }

         this.addReferenceList(var10, var6, var4);
      }

   }

   public void addSignatureAndEncryptionAndEndorsing(SignedInfo var1, List var2, EncryptionMethod var3, int var4, SignedInfo var5, String var6, List var7, String var8, boolean var9, ContextHandler var10) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.ensureWSS11();
      this.setLayout(var10);
      this.start();
      if ((var4 & 4096) == 4096) {
         throw new WSSecurityException("Wrong Policy on Encrypt before Sign and Encrypt Signature");
      } else {
         boolean var11 = (var4 & 8192) == 8192;
         EncryptedKeyToken var12 = (EncryptedKeyToken)this.getToken("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", (String)null, var10);
         EncryptedKeySTR var13 = new EncryptedKeySTR(WSSConstants.KEY_IDENTIFIER_QNAME, var12);
         Node var14 = this.addSignatureWithToken(var1, var13, (Boolean)null, var10);
         Node var15 = null;
         if (var5 != null) {
            this.setSignatureReference(var5, (Element)var14);
            var15 = this.addSignatureInternal(var5, var6, var7, var8, var9, var10);
         }

         if (var11) {
            CanonicalizationMethod var16 = var1.getCanonicalizationMethod();
            DOMTBEXML var17 = new DOMTBEXML((Element)var14, var16);
            var2.add(var17);
            if (var15 != null) {
               var16 = var5.getCanonicalizationMethod();
               DOMTBEXML var18 = new DOMTBEXML((Element)var15, var16);
               var2.add(var18);
            }
         }

         KeyInfo var19 = this.getKeyInfo(var13);
         DOMEncryptContext var20 = new DOMEncryptContext((Key)null);
         var20.setKeySelector(this.securityCtx.getKeySelector());
         List var21 = this.encryptData(var2, var20, var3, var19, true, var10);
         if (!"LaxTimestampFirst".equals(this.layout)) {
            ((SecurityTokenContextHandler)var10).addContextElement("weblogic.wsee.security.move_node_to_top", "true");
         }

         this.addReferenceList(var21, var12, var10);
         if (var11) {
            this.moveTimestampToTop();
         }

      }
   }

   public void addSignature(SignedInfo var1, EncryptionMethod var2, String var3, String var4, boolean var5, ContextHandler var6) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.addSignatureInternal(var1, var2, var3, (List)null, var4, var5, var6);
   }

   public void addSignature(SignedInfo var1, ContextHandler var2) throws MarshalException, WSSecurityException {
      this.ensureWSS11();
      this.start();
      this.setLayout(var2);
      EncryptedKeyToken var3 = (EncryptedKeyToken)this.getToken("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", (String)null, var2);
      EncryptedKeySTR var4 = new EncryptedKeySTR(WSSConstants.KEY_IDENTIFIER_QNAME, var3);
      this.addSignatureWithToken(var1, var4, (Boolean)null, var2);
   }

   private void setLayout(ContextHandler var1) {
      if (this.layout.equals("Strict")) {
         ((SecurityTokenContextHandler)var1).addContextElement("weblogic.wsee.security.strict_layout", Boolean.TRUE);
      }

   }

   public void addEncryption(List var1, EncryptionMethod var2, ContextHandler var3) throws MarshalException, WSSecurityException {
      this.ensureWSS11();
      this.start();
      EncryptedKeyToken var4 = (EncryptedKeyToken)this.getToken("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", (String)null, var3);
      EncryptedKeySTR var5 = new EncryptedKeySTR(WSSConstants.KEY_IDENTIFIER_QNAME, var4);
      KeyInfo var6 = this.getKeyInfo(var5);
      DOMEncryptContext var7 = new DOMEncryptContext((Key)null);
      var7.setKeySelector(this.securityCtx.getKeySelector());
      List var8 = this.encryptData(var1, var7, var2, var6, true, var3);
      this.addReferenceList(var8, var4, var3);
   }

   public Reference createReference(String var1, List var2, String var3, DigestMethod var4, List var5, boolean var6, ContextHandler var7) throws WSSecurityException {
      this.setLayout(var7);
      this.start();
      return this.createReferenceInternal(var1, var2, var3, var4, var5, var6, var7);
   }

   public Node addSignature(SignedInfo var1, Reference var2, List var3, ContextHandler var4) throws WSSecurityException, MarshalException {
      this.start();
      this.setLayout(var4);
      return this.addSignatureInternal(var1, var2, var3, var4);
   }

   public Node addSignature(SignedInfo var1, String var2, List var3, String var4, boolean var5, ContextHandler var6) throws WSSecurityException, MarshalException {
      this.start();
      this.setLayout(var6);
      boolean var7 = isMoveTimestampNeeded(var6);
      Node var8 = this.addSignatureInternal(var1, var2, var3, var4, var5, var6);
      if (null != var8 && var7) {
         this.moveTimestampToTop();
      }

      return var8;
   }

   public boolean addEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, boolean var7, ContextHandler var8) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.start();
      return this.addEncryptionInternal(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void addSignatureAndEncryption(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.addSignAndEncInternal(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   private void setSignatureReference(SignedInfo var1, Element var2) throws WSSecurityException {
      Iterator var3 = var1.getReferences().iterator();

      while(var3.hasNext()) {
         ReferenceImpl var4 = (ReferenceImpl)var3.next();
         if ("#weblogic.wsee.security.signature_node".equals(var4.getURI())) {
            String var5 = DOMUtils.getExistingId(var2, this.idQNames);
            if (null == var5) {
               throw new WSSecurityException("Missing Signature Id");
            }

            var4.setUri("#" + var5);
            break;
         }
      }

   }

   public void addSignatureAndEncryptionAndEndorsing(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, int var9, SignedInfo var10, String var11, List var12, String var13, boolean var14, ContextHandler var15) throws WSSecurityException, MarshalException, XMLEncryptionException {
      boolean var16 = false;
      this.ensureWSS11();
      this.start();
      this.setLayout(var15);
      if ((var9 & 4096) == 4096) {
         throw new WSSecurityException("Wrong Policy on Encrypt before Sign and Encrypt Signature");
      } else if (null == var3) {
         throw new WSSecurityException("Wrong state on Encrypted Key and DK");
      } else {
         boolean var17 = (var9 & 1024) == 1024;
         boolean var18 = (var9 & 8192) == 8192;
         SecurityToken var19 = this.getToken(var5, var7, var15);
         this.addToken(var8, var19, var15);
         SecurityTokenReference var20 = this.getSTR(var5, var6, var19, var8);
         Key var21 = this.generateKey(var4);
         String var22 = DOMUtils.generateId("encKey");
         EncryptedKeyToken var23 = this.addEncryptedKeyToken(var21, var22);
         EncryptedKeySTR var24 = new EncryptedKeySTR(WSSConstants.REFERENCE_QNAME, var23);
         Node var25 = this.addSignatureWithToken(var1, var24, (Boolean)null, var15);
         Node var26 = null;
         if (var17) {
            this.setSignatureReference(var10, (Element)var25);
            var26 = this.addSignatureInternal(var10, var11, var12, var13, var14, var15);
         }

         if (var18) {
            CanonicalizationMethod var27 = var1.getCanonicalizationMethod();
            DOMTBEXML var28 = new DOMTBEXML((Element)var25, var27);
            var2.add(var28);
            if (var26 != null) {
               var27 = var10.getCanonicalizationMethod();
               DOMTBEXML var29 = new DOMTBEXML((Element)var26, var27);
               var2.add(var29);
            }

            var16 = true;
         }

         KeyInfo var34 = this.getKeyInfo(var20);
         DOMEncryptContext var35 = new DOMEncryptContext(var21);
         List var36 = this.encryptData(var2, var35, var4, var34, false, var15);
         KeyProvider var30 = this.getKeyProvider(var5, var19);
         KeySelector var31 = this.securityCtx.getKeySelector();
         if (null != var3) {
            Key var32 = this.getKey(var31, var30, var3);
            EncryptedKey var33 = this.addEncryptedKey(var21, var32, var3, var34, var36, var22, var8, var19, var15);
            var23.setEncryptedKey(var33);
         } else {
            this.addReferenceList(var36, var19, var15);
         }

         if (var16) {
            this.moveTimestampToTop();
         }

      }
   }

   public void addSignature(SignedInfo var1, EncryptionMethod var2, String var3, List var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.addSignatureInternal(var1, var2, var3, var4, var5, var6, var7);
   }

   private void addSignAndEncInternal(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.ensureWSS11();
      this.start();
      this.setLayout(var9);
      boolean var10 = SecurityImpl.isEncryptBeforeSign(var9);
      SecurityToken var11 = this.getToken(var5, var7, var9);
      this.addToken(var8, var11, var9);
      SecurityTokenReference var12 = this.getSTR(var5, var6, var11, var8);
      Key var13 = this.generateKey(var4);
      String var14 = DOMUtils.generateId("encKey");
      EncryptedKeyToken var15 = this.addEncryptedKeyToken(var13, var14);
      if (var10) {
         KeyInfo var16 = this.getKeyInfo(var12);
         DOMEncryptContext var17 = new DOMEncryptContext(var13);
         STRType var18 = new STRType(WSSConstants.REFERENCE_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1", "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
         ArrayList var19 = new ArrayList();
         var19.add(var18);
         SecurityTokenReference var20 = this.getSTR("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", var19, var15, false);
         KeyInfo var21 = this.getKeyInfo(var20);
         List var22 = this.encryptData(var2, var17, var4, var21, true, var9);
         KeyProvider var23 = this.getKeyProvider(var5, var11);
         KeySelector var24 = this.securityCtx.getKeySelector();
         Key var25 = this.getKey(var24, var23, var3);
         EncryptedKey var26 = this.addEncryptedKey(var13, var25, var3, var16, new ArrayList(), var14, var8, var11, var9);
         this.addReferenceList(var22, var15, var9);
         var15.setEncryptedKey(var26);
         Map var27 = (Map)var9.getValue("weblogic.wsee.security.encrypted_element.map");
         if (null != var27) {
            Iterator var28 = var1.getReferences().iterator();

            while(var28.hasNext()) {
               ReferenceImpl var29 = (ReferenceImpl)var28.next();
               if (var27.containsKey(var29.getURI())) {
                  var29.setUri("#" + (String)var27.get(var29.getURI()));
               }
            }
         }

         EncryptedKeySTR var33 = new EncryptedKeySTR(WSSConstants.REFERENCE_QNAME, var15);
         this.addSignatureWithToken(var1, var33, (Boolean)null, var9);
         this.moveTimestampToTop();
      } else {
         EncryptedKeySTR var34 = new EncryptedKeySTR(WSSConstants.REFERENCE_QNAME, var15);
         this.addSignatureWithToken(var1, var34, (Boolean)null, var9);
         KeyInfo var35 = this.getKeyInfo(var12);
         DOMEncryptContext var36 = new DOMEncryptContext(var13);
         List var37 = this.encryptData(var2, var36, var4, var35, false, var9);
         KeyProvider var38 = this.getKeyProvider(var5, var11);
         KeySelector var30 = this.securityCtx.getKeySelector();
         Key var31 = this.getKey(var30, var38, var3);
         EncryptedKey var32 = this.addEncryptedKey(var13, var31, var3, var35, var37, var14, var8, var11, var9);
         var15.setEncryptedKey(var32);
      }

   }

   private void moveTimestampToTop() {
      if (!"LaxTimestampLast".equals(this.layout)) {
         Element var1 = this.securityCtx.getSecurityElement();
         Element var2 = DOMUtils.getLastElement(var1);
         if ("Timestamp".equals(var2.getLocalName())) {
            this.moveToTop((Node)var2);
         }
      }

   }

   private void addSignatureInternal(SignedInfo var1, EncryptionMethod var2, String var3, List var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.ensureWSS11();
      this.setLayout(var7);
      this.start();
      SecurityToken var8 = this.getToken(var3, var5, var7);
      this.addToken(var6, var8, var7);
      SecurityTokenReference var9 = this.getSTR(var3, var4, var8, var6);
      EncryptionMethod var10 = null;

      try {
         var10 = this.securityCtx.getEncryptionFactory().newEncryptionMethod("http://www.w3.org/2001/04/xmlenc#aes256-cbc", (Integer)null, (EncryptionMethodParameterSpec)null);
      } catch (InvalidAlgorithmParameterException var20) {
         throw new WSSecurityException(var20);
      }

      Key var11 = this.generateKey(var10);
      String var12 = DOMUtils.generateId("encKey");
      EncryptedKeyToken var13 = this.addEncryptedKeyToken(var11, var12);
      EncryptedKeySTR var14 = new EncryptedKeySTR(WSSConstants.REFERENCE_QNAME, var13);
      this.addSignatureWithToken(var1, var14, (Boolean)null, var7);
      KeyInfo var15 = this.getKeyInfo(var9);
      KeyProvider var16 = this.getKeyProvider(var3, var8);
      KeySelector var17 = this.securityCtx.getKeySelector();
      Key var18 = this.getKey(var17, var16, var2);
      EncryptedKey var19 = this.addEncryptedKey(var11, var18, var2, var15, EMPTY_LIST, var12, var6, var8, var7);
      var13.setEncryptedKey(var19);
   }

   private SignatureConfirmation addSigConf(String var1, ContextHandler var2) throws WSSecurityException, MarshalException {
      if (this.security == null) {
         this.createSecurity(this.securityCtx);
      }

      SignatureConfirmation var3 = WSS11Factory.newSignatureConfirmation(var1);
      this.security.add((XMLStructure)var3, (XMLCryptoContext)null, var2);
      return var3;
   }

   private void start() {
      this.processingStarted = true;
   }

   private boolean isWSS10() {
      return this.version.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
   }

   private void checkLayout(String var1) {
      if (!supportedLayouts.contains(var1)) {
         throw new IllegalArgumentException("Unsupported layout: " + var1);
      }
   }

   private void ensureWSS11() {
      if (this.isWSS10()) {
         throw new UnsupportedOperationException("Operation not supported with WSS version " + this.version);
      }
   }

   public Node addSignature(SignedInfo var1, String var2, String var3, boolean var4, ContextHandler var5) throws WSSecurityException, MarshalException {
      this.setLayout(var5);
      return super.addSignature(var1, var2, var3, var4, var5);
   }

   public Node addSignature(SignedInfo var1, Reference var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      this.setLayout(var3);
      return super.addSignature(var1, var2, var3);
   }

   protected SecurityTokenReference getSTR(String var1, List var2, SecurityToken var3, boolean var4) throws WSSecurityException {
      if (var2 != null && var2.size() != 0) {
         SecurityTokenHandler var5 = null;

         for(int var6 = 0; var6 < var2.size(); ++var6) {
            String var7 = ((STRType)var2.get(var6)).getValueType();
            if (var7 != null) {
               var5 = this.securityCtx.getTokenHandler(var7);
               if (null == var5 && var2.size() == 1) {
                  throw new WSSecurityException("Unable to get handler for str value type =" + var7);
               }
               break;
            }
         }

         if (var5 == null) {
            var5 = this.securityCtx.getRequiredTokenHandler(var1);
         }

         SecurityTokenReference var12 = null;
         QName var13 = null;
         QName var8 = null;

         for(int var9 = 0; var12 == null && var2.size() > var9; ++var9) {
            STRType var10 = (STRType)var2.get(var9);
            var8 = var10.getTopLevelElement();
            if (var8.equals(WSSConstants.REFERENCE_QNAME)) {
               return this.createDirectSTR(var1, var3);
            }

            String var11 = var10.getValueType();
            if ("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var11)) {
               this.ensureWSS11();
               var12 = var5.getSTR(var8, var10.getValueType(), var3);
            } else {
               var12 = var5.getSTR(var8, var11 != null ? var10.getTokenType() : var1, var3);
               if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var8) && !WSSConstants.KEY_IDENTIFIER_QNAME.equals(var12.getSTRType())) {
                  if (var2.size() <= var9) {
                     throw new WSSecurityException("Failed to create KeyIdentifier STR");
                  }

                  var13 = var12.getSTRType();
                  var12 = null;
               }
            }
         }

         if (var12 == null) {
            throw new WSSecurityException("Failed to create STR for QName =" + var8 + " error STR QName =" + var13);
         } else {
            if (var12.getId() == null) {
               var12.setId(getId("str"));
            }

            return var12;
         }
      } else {
         return super.getSTR(var1, var2, var3, var4);
      }
   }

   protected SecurityTokenReference createKeyIdSTRInternal(String var1, List var2, SecurityToken var3, boolean var4) throws WSSecurityException {
      return this.getSTR(var1, var2, var3, var4);
   }

   protected void createSecurity(WSSecurityContext var1) throws MarshalException {
      WSS11Factory.getInstance();
      this.security = WSS11Factory.newSecurity(var1);
   }

   protected void processEncryptedHeader(Node var1, Node var2) {
      if (this.isWSS10()) {
         super.processEncryptedHeader(var1, var2);
      } else {
         boolean var3 = this.namespaces.get("http://schemas.xmlsoap.org/soap/envelope/") == null;
         String var4 = (String)this.namespaces.get("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd");
         if (var4 == null) {
            var4 = "wsse11";
         }

         Element var5 = DOMUtils.createAndAddElement((Element)var2.getParentNode(), WSS11Constants.ENC_HEADER_QNAME, var4);
         NamedNodeMap var6 = var1.getAttributes();
         if (null != this.securityCtx.getSecurityElement()) {
            var6 = this.securityCtx.getSecurityElement().getAttributes();
         }

         Node var7 = var6.getNamedItemNS("http://schemas.xmlsoap.org/soap/envelope/", "actor");
         if (var7 == null) {
            var7 = var6.getNamedItemNS("http://www.w3.org/2003/05/soap-envelope", SOAPConstants.HEADER12_ROLE.getLocalPart());
         }

         if (var7 != null) {
            var5.setAttributeNodeNS((Attr)var7.cloneNode(true));
         }

         Node var8 = var6.getNamedItemNS("http://www.w3.org/2003/05/soap-envelope", SOAPConstants.HEADER12_RELAY.getLocalPart());
         if (var8 != null) {
            var5.setAttributeNodeNS((Attr)var8.cloneNode(true));
         }

         SecurityImpl.setMustUnderstand(var5, this.namespaces, var3);
         Node var9 = var2.getParentNode();
         var9.removeChild(var2);
         var5.appendChild(var2);
      }
   }

   protected EncryptedKey addEncryptedKey(Key var1, Key var2, EncryptionMethod var3, KeyInfo var4, List var5, String var6, boolean var7, SecurityToken var8, ContextHandler var9) throws WSSecurityException, MarshalException {
      TBEKey var10 = new TBEKey(var1);
      boolean var12 = false;
      EncryptedKey var11;
      if (var12) {
         var11 = this.getEncryptionFactory().newEncryptedKey(var10, var3, var4, (EncryptionProperties)null, (List)null, var6, (String)null, (String)null, (CipherReference)null);
      } else {
         var11 = this.getEncryptionFactory().newEncryptedKey(var10, var3, var4, (EncryptionProperties)null, var5, var6, (String)null, (String)null, (CipherReference)null);
      }

      DOMEncryptContext var13 = new DOMEncryptContext(var2);
      this.addEncryptedKey(var11, var13, var9);
      EncryptedKeyToken var14 = this.addEncryptedKeyToken(var1, var6);

      try {
         var14.setEncryptedKey(var11);
      } catch (XMLEncryptionException var16) {
         throw new WSSecurityException(var16);
      }

      this.addToken(var7, var8, var9);
      return var11;
   }

   protected void updateContext(Node var1, Node var2, ContextHandler var3) {
      super.updateContext(var1, var2, var3);
      if (SecurityImpl.isEncryptBeforeSign(var3)) {
         SecurityTokenContextHandler var4 = (SecurityTokenContextHandler)var3;
         Object var5 = (Map)var4.getValue("weblogic.wsee.security.encrypted_element.map");
         if (null == var5) {
            var5 = new HashMap();
            var4.addContextElement("weblogic.wsee.security.encrypted_element.map", var5);
         }

         ((Map)var5).put(new QName(var1.getNamespaceURI(), var1.getLocalName()), var2);
         String var6 = this.getExisitingUri((Element)var1);
         String var7 = this.getExisitingUri((Element)var2);
         if (null != var6 && null != var7) {
            ((Map)var5).put(this.getUri(var6), var7);
         }
      }

   }

   protected void moveToTop(SecurityToken var1) {
      Node var2 = this.securityCtx.getNode(var1);
      this.moveToTop(var2);
   }

   protected void moveToTop(Node var1) {
      Element var2 = this.securityCtx.getSecurityElement();
      var2.removeChild(var1);
      Node var3 = var2.getFirstChild();
      if ("LaxTimestampFirst".equals(this.layout) && null != var3 && WSSConstants.TIMESTAMP_QNAME.getLocalPart().equals(var3.getLocalName())) {
         var3 = var3.getNextSibling();
      }

      var2.insertBefore(var1, var3);
   }

   public boolean isCredentialAvailable(String var1) {
      CredentialProvider var2 = this.getCredentialProvider(var1);
      return null != var2;
   }

   public static boolean isMoveTimestampNeeded(ContextHandler var0) {
      Boolean var1 = (Boolean)var0.getValue("weblogic.wsee.security.need_to_move_timestamp");
      return var1 != null ? var1 : false;
   }

   public Key getEncryptedKeyToken(EncryptionMethod var1, EncryptionMethod var2, String var3, List var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException, XMLEncryptionException {
      this.start();
      return this.getEncryptedKeyTokenInternal(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Key getEncryptedKeyTokenInternal(EncryptionMethod var1, EncryptionMethod var2, String var3, List var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException {
      SecurityToken var8 = this.getToken(var3, var5, var7);
      SecurityTokenReference var9 = this.createKeyIdSTRInternal(var3, var4, var8, var6);
      if (var9 == null) {
         throw new WSSecurityException("Failed to create reference for token: " + var8);
      } else {
         KeyInfo var10 = this.getKeyInfo(var9);
         KeyProvider var11 = this.getKeyProvider(var3, var8);
         KeySelector var12 = this.securityCtx.getKeySelector();
         Key var13 = this.generateKey(var2);
         String var14 = DOMUtils.generateId("encKey");
         Key var15 = this.getKey(var12, var11, var1);
         EncryptedKey var16 = this.addEncryptedKey(var13, var15, var1, var10, EMPTY_LIST, var14, var6, var8, var7);
         if (null == var16) {
            LogUtils.logWss("Unable to add Encrypted Key");
            return null;
         } else {
            EncryptedKeyToken var17 = new EncryptedKeyToken(var13, var14);
            EncryptedKeyProvider var18 = null;

            try {
               var18 = new EncryptedKeyProvider(var16, var13, var17);
               var17.setKeyProvider(var18);
            } catch (XMLEncryptionException var20) {
               throw new WSSecurityException(var20, WSSConstants.FAILURE_INVALID);
            }

            this.securityCtx.addKeyProvider(var18);
            this.securityCtx.addSecurityToken(var17);
            this.securityCtx.addToken(var17, this.securityCtx.getElementById(var16.getId()));
            return var13;
         }
      }
   }

   static {
      supportedLayouts.add("Strict");
      supportedLayouts.add("Lax");
      supportedLayouts.add("LaxTimestampFirst");
      supportedLayouts.add("LaxTimestampLast");
   }
}
