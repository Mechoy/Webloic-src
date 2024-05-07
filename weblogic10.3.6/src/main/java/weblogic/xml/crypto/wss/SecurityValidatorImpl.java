package weblogic.xml.crypto.wss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeySelectorResultImpl;
import weblogic.xml.crypto.dsig.ReferenceUtils;
import weblogic.xml.crypto.dsig.XMLSignatureImpl;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.XMLValidateContext;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.Timestamp;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;

public class SecurityValidatorImpl implements SecurityValidator {
   protected WSSecurityContext securityCtx;

   public SecurityValidatorImpl(WSSecurityContext var1) {
      this.securityCtx = var1;
   }

   public XMLSignatureFactory getXMLSignatureFactory() {
      return this.securityCtx.getSignatureFactory();
   }

   public XMLEncryptionFactory getXMLEncryptionFactory() {
      return this.securityCtx.getEncryptionFactory();
   }

   public String getUri(Element var1) throws WSSecurityException {
      String var2 = DOMUtils.getExistingId(var1, this.securityCtx.getIdQNames());
      if (var2 != null && var2.length() != 0) {
         return "#" + var2;
      } else if (var1.isSameNode(var1.getOwnerDocument().getDocumentElement())) {
         return "";
      } else {
         throw new WSSecurityException("No id attribute on element " + var1.getNamespaceURI() + ":" + var1.getLocalName(), WSSConstants.FAILURE_INVALID);
      }
   }

   public Reference getReference(Element var1, DigestMethod var2, List var3) throws WSSecurityException, MarshalException {
      String var4 = null;
      SecurityToken var5 = this.securityCtx.getToken(var1);
      if (var5 != null) {
         return this.getReference(var5, var2, ReferenceUtils.getTransforms(var3), true);
      } else {
         var4 = this.getUri(var1);
         return this.getReference(var4, var2, ReferenceUtils.getTransforms(var3));
      }
   }

   public Reference getReference(String var1, String var2, Node var3, DigestMethod var4, List var5) throws WSSecurityException {
      return this.getReference(var1, var2, var3, var4, var5, true);
   }

   public Reference getReference(String var1, String var2, Node var3, DigestMethod var4, List var5, boolean var6) throws WSSecurityException {
      SecurityTokenContextHandler var7 = new SecurityTokenContextHandler();
      var7.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var3);
      var7.addContextElement("com.bea.contextelement.xml.SecurityInfo", this.securityCtx);
      SecurityTokenHandler var8 = this.securityCtx.getRequiredTokenHandler(var1);
      SecurityToken var9 = null;
      List var10 = this.getEquivalentSecurityTokens(var1);
      if ((var10 == null || var10.size() == 0) && ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1".equals(var1) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7".equals(var1))) {
         var10 = this.securityCtx.getSecurityTokens("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
         var10.addAll(this.securityCtx.getSecurityTokens("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1"));
      }

      Iterator var11 = var10.iterator();

      while(var11.hasNext()) {
         SecurityToken var12 = (SecurityToken)var11.next();
         this.securityCtx.getNode(var12);
         List var14 = this.securityCtx.getSignatures(var12);
         if (var14 != null && var14.size() > 0 && var8.matches(var12, var1, var2, var7, Purpose.VERIFY)) {
            var9 = var12;
            break;
         }
      }

      if (var9 == null) {
         throw new WSSecurityException("Failed to create Reference for token of type " + var1 + ", token handler did not return a token for claims " + var3, WSSConstants.FAILURE_INVALID);
      } else {
         return this.getReference(var9, var4, var5, var6);
      }
   }

   public Reference getReference(SecurityToken var1, DigestMethod var2, List var3, boolean var4) {
      String var5 = null;
      String var6 = var1.getId();
      String var7 = var1.getValueType();
      boolean var8 = var7 != null && (var7.startsWith("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile") || var7.startsWith("http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile"));
      boolean var9 = false;
      Object var10 = this.securityCtx.getSTR(var1);
      if (var4 && var6 != null && (!var8 || var10 == null)) {
         var5 = this.getUri(var6);
      } else {
         var9 = true;
         if (var10 == null) {
            return null;
         }

         String var11 = null;
         if (var10 instanceof String) {
            var11 = (String)var10;
         } else if (var10 instanceof SecurityTokenReference) {
            var11 = this.getSTRId((SecurityTokenReference)var10);
         }

         var5 = this.getUri(var11);
         var3.add(0, STRTransform.getInstance());
      }

      Object var12 = this.getReference(var5, var2, var3);
      if (var8 && var9 && var4 && var6 != null) {
         var5 = this.getUri(var6);
         var3.remove(0);
         var12 = new CombinedReference((Reference)var12, this.getReference(var5, var2, var3));
      }

      return (Reference)var12;
   }

   public Reference getReference(String var1, DigestMethod var2, List var3, boolean var4) {
      String var5 = null;
      if (var4 && var1 != null) {
         var5 = this.getUri(var1);
      }

      return this.getReference(var5, var2, var3);
   }

   private Reference getReference(String var1, DigestMethod var2, List var3) {
      List var4 = ReferenceUtils.getTransforms(var3);
      return this.getXMLSignatureFactory().newReference(var1, var2, var4, (String)null, (String)null);
   }

   public boolean validateTimestamp(short var1) throws WSSecurityException {
      Timestamp var2 = this.securityCtx.getTimestamp();
      if (var2 != null) {
         TimestampHandler var3 = this.securityCtx.getTimestampHandler();
         var3.validate(var2, var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean validateSecurityToken(String var1, String var2, Node var3) throws WSSecurityException {
      return this.validateSecurityToken(var1, var2, var3, Purpose.IDENTITY);
   }

   private boolean validateSecurityToken(String var1, String var2, Node var3, Purpose var4) throws WSSecurityException {
      List var5 = this.securityCtx.getSecurityTokens();
      Iterator var6 = var5.iterator();

      SecurityToken var7;
      do {
         if (!var6.hasNext()) {
            return false;
         }

         var7 = (SecurityToken)var6.next();
      } while(!this.validateSecurityToken(var7, var1, var2, var3, var4) || !this.validateIncludedInMessage(var7));

      this.securityCtx.addIdToken(var7);
      return true;
   }

   protected boolean validateSecurityToken(SecurityToken var1, String var2, String var3, Node var4, Purpose var5) throws WSSecurityException {
      if (!this.isEquivalentTokenType(var1.getValueType(), var2)) {
         return false;
      } else {
         SecurityTokenHandler var6 = this.securityCtx.getRequiredTokenHandler(var2);
         SecurityTokenContextHandler var7 = new SecurityTokenContextHandler(var4, this.securityCtx);
         if (var1 instanceof BinarySecurityToken) {
            BinarySecurityToken var8 = (BinarySecurityToken)var1;
            if (!var8.isValidated()) {
               SecurityTokenValidateResult var9 = var6.validateProcessed(var1, this.securityCtx.getMessageContext());
               if (!var9.status()) {
                  return false;
               }
            }
         }

         return var6.matches(var1, var1.getValueType(), var3, var7, var5);
      }
   }

   protected boolean validateIncludedInMessage(SecurityToken var1) {
      Object var2 = this.securityCtx.getProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage");
      boolean var3 = true;
      if (var2 instanceof Boolean) {
         var3 = (Boolean)var2;
         if (var1 instanceof EncryptedKeyToken) {
            return true;
         } else {
            return var3 && null != this.securityCtx.getNode(var1) || !var3 && null == this.securityCtx.getNode(var1);
         }
      } else {
         return true;
      }
   }

   private boolean isSameKindOfX509ValueType(String var1, String var2) {
      if (null != var1 && null != var2) {
         int var3 = var1.indexOf("#");
         int var4 = var2.indexOf("#");
         if (var3 != -1 && var4 != -1 && var1.endsWith("#EncryptedKey") && var2.endsWith("#X509v3")) {
            return true;
         } else if (var3 != -1 && var4 != -1 && var3 == var4 && var1.length() >= var3 + 6) {
            return var1.substring(0, var3 - 1).equals(var2.substring(0, var4 - 1)) && var1.indexOf("x509-token") != -1 && "#X509".equals(var1.substring(var3, var3 + 5));
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean validateSignature(SignedInfo var1, String var2, String var3, Node var4) throws WSSecurityException {
      List var5 = this.securityCtx.getSignatures();
      Iterator var6 = var5.iterator();

      XMLSignature var7;
      SecurityToken var10;
      do {
         if (!var6.hasNext()) {
            return false;
         }

         var7 = (XMLSignature)var6.next();
         XMLSignatureImpl var8 = (XMLSignatureImpl)var7;
         KeySelectorResultImpl var9 = (KeySelectorResultImpl)var8.getSignatureValidateResult().getKeySelectorResult();
         var10 = var9.getSecurityToken();
      } while(!match(var7.getSignedInfo(), var1) || !this.validateSecurityToken(var10, var2, var3, var4, Purpose.SIGN));

      return true;
   }

   public boolean validateEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, String var5, Node var6) throws WSSecurityException, XMLEncryptionException {
      List var7 = this.securityCtx.getEncryptions();
      Iterator var8 = var1.iterator();

      while(var8.hasNext()) {
         TBE var9 = (TBE)var8.next();
         Iterator var10 = var7.iterator();

         while(var10.hasNext()) {
            Encryption var11 = (Encryption)var10.next();
            EncryptedData var12 = var11.getEncryptedData();
            List var13 = var11.getNodes();
            KeySelectorResultImpl var14 = (KeySelectorResultImpl)var11.getKeySelectorResult();
            if (var12.getEncryptionMethod().getAlgorithm().equals(var3.getAlgorithm()) && this.matchNodes(var13, var9) && this.validateSecurityToken(var14.getSecurityToken(), var4, var5, var6, Purpose.DECRYPT)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasSecurity() {
      return this.securityCtx.getSecurityElement() != null;
   }

   protected static boolean match(SignedInfo var0, SignedInfo var1) throws WSSecurityException {
      return var0.getCanonicalizationMethod().getAlgorithm().equals(var1.getCanonicalizationMethod().getAlgorithm()) && var0.getSignatureMethod().getAlgorithm().equals(var1.getSignatureMethod().getAlgorithm()) && matchReferences(var0.getReferences(), var1.getReferences());
   }

   private static boolean matchReferences(List var0, List var1) throws WSSecurityException {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         boolean var3 = false;
         Reference var4 = (Reference)var1.get(var2);

         for(int var5 = 0; var5 < var0.size(); ++var5) {
            Reference var6 = (Reference)var0.get(var5);
            if (matchReference(var6, var4)) {
               var3 = true;
               break;
            }
         }

         if (!var3) {
            return false;
         }
      }

      return true;
   }

   protected static boolean matchReference(Reference var0, Reference var1) throws WSSecurityException {
      if (var1 instanceof CombinedReference) {
         CombinedReference var2 = (CombinedReference)var1;
         return !matchReferenceInternal(var0, var2.refOnSTR) ? matchReferenceInternal(var0, var2.refOnToken) : true;
      } else {
         return matchReferenceInternal(var0, var1);
      }
   }

   private static boolean matchReferenceInternal(Reference var0, Reference var1) throws WSSecurityException {
      String var2 = var0.getURI();
      String var3 = var1.getURI();
      if (var2 != null && var3 != null) {
         return var2.equals(var3) && var0.getDigestMethod().getAlgorithm().equals(var1.getDigestMethod().getAlgorithm()) && matchTransforms(var0.getTransforms(), var1.getTransforms());
      } else {
         throw new WSSecurityException("Can not validate Reference without URI.", WSSConstants.FAILURE_INVALID);
      }
   }

   private static boolean matchTransforms(List var0, List var1) {
      if (var0.size() == 0 && var1.size() == 1 && ((Transform)var1.get(0)).getAlgorithm().equals("http://www.w3.org/2001/10/xml-exc-c14n#")) {
         return true;
      } else if (var0.size() != var1.size()) {
         return false;
      } else {
         for(int var2 = 0; var2 < var0.size(); ++var2) {
            Transform var3 = (Transform)var0.get(var2);
            Transform var4 = (Transform)var1.get(var2);
            if (!var3.getAlgorithm().equals(var4.getAlgorithm())) {
               return false;
            }
         }

         return true;
      }
   }

   protected boolean matchNodes(List var1, TBE var2) {
      if (!(var2 instanceof DOMTBEXML)) {
         return false;
      } else {
         DOMTBEXML var3 = (DOMTBEXML)var2;
         NodeList var4 = var3.getNodeList();

         for(int var5 = 0; var5 < var1.size(); ++var5) {
            Node var6 = (Node)var1.get(var5);
            Node var7 = var4.item(var5);
            if (var6 != var7) {
               if (var6 == null || var7 == null) {
                  return false;
               }

               if (!this.isEquals(var6.getNamespaceURI(), var7.getNamespaceURI()) || !this.isEquals(var6.getLocalName(), var7.getLocalName())) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean isEquals(String var1, String var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var1.equals(var2);
      }
   }

   private String getUri(String var1) {
      return "#" + var1;
   }

   private String getSTRId(SecurityTokenReference var1) {
      return var1.getId();
   }

   private boolean isEquivalentTokenType(String var1, String var2) {
      if (var1 != null && var1.equals(var2)) {
         return true;
      } else {
         return this.isSameKindOfX509ValueType(var1, var2) ? true : SAMLUtils.isEquivalentSamlTokenType(var1, var2);
      }
   }

   private List getEquivalentSecurityTokens(String var1) {
      ArrayList var2 = new ArrayList();
      List var3 = this.securityCtx.getSecurityTokens();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         SecurityToken var5 = (SecurityToken)var4.next();
         if (this.isEquivalentTokenType(var5.getValueType(), var1)) {
            var2.add(var5);
         }
      }

      return var2;
   }

   private class CombinedReference implements Reference {
      public Reference refOnSTR;
      public Reference refOnToken;

      public CombinedReference(Reference var2, Reference var3) {
         this.refOnSTR = var2;
         this.refOnToken = var3;
      }

      public DigestMethod getDigestMethod() {
         return this.refOnSTR.getDigestMethod();
      }

      public Reference.DigestValue getDigestValue() {
         return this.refOnSTR.getDigestValue();
      }

      public String getId() {
         return this.refOnSTR.getId();
      }

      public List getTransforms() {
         return this.refOnSTR.getTransforms();
      }

      public Reference.ValidateResult validate(XMLValidateContext var1) throws XMLSignatureException {
         return this.refOnSTR.validate(var1);
      }

      public String getType() {
         return this.refOnSTR.getType();
      }

      public String getURI() {
         return this.refOnSTR.getURI();
      }

      public boolean isFeatureSupported(String var1) {
         return this.refOnSTR.isFeatureSupported(var1);
      }
   }
}
