package weblogic.xml.crypto.wss11.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.common.keyinfo.KeySelectorResultImpl;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.ReferenceImpl;
import weblogic.xml.crypto.dsig.XMLSignatureImpl;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.Encryption;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class SecurityValidatorImpl extends weblogic.xml.crypto.wss.SecurityValidatorImpl implements SecurityValidator {
   public static final String VERBOSE_PROPERTY = "weblogic.xml.crypto.wss11.verbose";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.wss11.verbose");
   private static final boolean DEBUG = true;
   private static final boolean WS_POLICY_INTEROP = false;
   private static final STRType enckeyDirectSTR;
   private static final List enckeyDirectSTRList;
   public static final String DK_VALUE_TYPE_V2005 = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   public static final String DK_VALUE_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";

   public SecurityValidatorImpl(WSSecurityContext var1) {
      super(var1);
   }

   public boolean validateSignature(SignedInfo var1, String var2, List var3, String var4, Node var5) throws WSSecurityException {
      List var6 = this.securityCtx.getSignatures();
      Iterator var7 = var6.iterator();

      while(var7.hasNext()) {
         XMLSignature var8 = (XMLSignature)var7.next();
         XMLSignatureImpl var9 = (XMLSignatureImpl)var8;
         KeySelectorResultImpl var10 = (KeySelectorResultImpl)var9.getSignatureValidateResult().getKeySelectorResult();
         SecurityToken var11 = var10.getSecurityToken();
         if (match(var8.getSignedInfo(), var1) && this.match(var3, var8.getKeyInfo()) && this.validateSecurityToken(var11, var2, var4, var5, Purpose.SIGN) && this.validateIncludedInMessage(var11)) {
            return true;
         }

         if (!match(var8.getSignedInfo(), var1)) {
            System.err.println("SignInfo mismatch " + this.getMismatchInfo(var8.getSignedInfo(), var1));
            LogUtils.logWss("SignInfo mismatch " + this.getMismatchInfo(var8.getSignedInfo(), var1));
         }

         if (!this.match(var3, var8.getKeyInfo())) {
            System.err.println("STR type mismatch " + this.getMismatchInfo(var3, var8.getKeyInfo()));
            LogUtils.logWss("STR type mismatch " + this.getMismatchInfo(var3, var8.getKeyInfo()));
         }

         if (!this.validateSecurityToken(var11, var2, var4, var5, Purpose.SIGN)) {
            System.err.println("Security Token mismatch, token type =" + var2 + " and actual is" + var11.getValueType());
            LogUtils.logWss("Security Token mismatch, token type =" + var2 + " and actual is" + var11.getValueType());
         }

         if (!this.validateIncludedInMessage(var11)) {
            System.err.println("Security Token doesn't match Token Inclusion assertion in policy, included in message = " + this.securityCtx.getProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage") + ", token id = " + var11.getId() + ", token type = " + var11.getValueType());
            LogUtils.logWss("Security Token doesn't match [Security Token Inclusion] assertion in policy, token id = " + var11.getId() + ", token type = " + var11.getValueType());
         }
      }

      return false;
   }

   private String getMismatchInfo(List var1, List var2) throws WSSecurityException {
      StringBuffer var3 = new StringBuffer(" Refs:");
      var3.append(" Msg size =" + var1.size());

      int var4;
      Reference var5;
      for(var4 = 0; var4 < var1.size(); ++var4) {
         var5 = (Reference)var1.get(var4);
         var3.append(var5.getURI() + ",");
      }

      var3.append(" Policy size =" + var2.size());

      for(var4 = 0; var4 < var2.size(); ++var4) {
         var5 = (Reference)var2.get(var4);
         var3.append(" " + var5.getURI() + ",");
      }

      return var3.toString();
   }

   private String getMismatchInfo(SignedInfo var1, SignedInfo var2) throws WSSecurityException {
      StringBuffer var3 = new StringBuffer();
      if (!var1.getCanonicalizationMethod().getAlgorithm().equals(var2.getCanonicalizationMethod().getAlgorithm())) {
         var3.append("C14N mismatch " + var1.getCanonicalizationMethod().getAlgorithm() + " VS. " + var2.getCanonicalizationMethod().getAlgorithm());
      }

      if (!var1.getSignatureMethod().getAlgorithm().equals(var2.getSignatureMethod().getAlgorithm())) {
         var3.append(" Algo mismatch " + var1.getSignatureMethod().getAlgorithm() + " VS. " + var2.getSignatureMethod().getAlgorithm());
      }

      var3.append(this.getMismatchInfo(var1.getReferences(), var2.getReferences()));
      return var3.toString();
   }

   private String getMismatchInfo(List var1, KeyInfo var2) {
      StringBuffer var3 = new StringBuffer("Actual KeyInfo:");
      List var4 = var2.getContent();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         if (var6 instanceof SecurityTokenReference) {
            SecurityTokenReference var7 = (SecurityTokenReference)var6;
            QName var8 = var7.getSTRType();
            var3.append(var8);
            var3.append("|" + var7.getValueType() + ", ");
         }
      }

      var3.append(" StrTypes size=" + var1.size() + " :");
      var5 = var1.iterator();

      while(var5.hasNext()) {
         STRType var9 = (STRType)var5.next();
         var3.append(var9.getTopLevelElement().toString() + "|");
         var3.append("|" + var9.getValueType() + ", ");
      }

      return var3.toString();
   }

   private boolean match(List var1, KeyInfo var2) {
      if (null != var1 && var1.size() != 0) {
         List var3 = var2.getContent();
         Iterator var4 = var3.iterator();

         while(true) {
            Object var5;
            do {
               if (!var4.hasNext()) {
                  return false;
               }

               var5 = var4.next();
            } while(!(var5 instanceof SecurityTokenReference));

            SecurityTokenReference var6 = (SecurityTokenReference)var5;
            QName var7 = var6.getSTRType();
            String var8 = var6.getValueType();
            Iterator var9 = var1.iterator();

            while(var9.hasNext()) {
               STRType var10 = (STRType)var9.next();
               if (var10.getTopLevelElement().equals(var7) && (var10.getValueType() == null || var10.getValueType().equals(var8) || null == var8)) {
                  return true;
               }

               if (var10.getTopLevelElement().equals(DsigConstants.X509ISSUER_SERIAL_QNAME) && var6.getIssuerSerial() != null) {
                  return true;
               }
            }
         }
      } else {
         return true;
      }
   }

   public boolean validateEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, Node var7) throws WSSecurityException, XMLEncryptionException {
      return this.validateEncryption(var1, var2, var3, var4, var5, var6, var7, (Map)null);
   }

   public boolean validateEncryptionforEncryptFirst(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, Node var7, Map var8) throws WSSecurityException, XMLEncryptionException {
      if (null == var8) {
         var8 = new HashMap();
      }

      return this.validateEncryption(var1, var2, var3, var4, var5, var6, var7, (Map)var8);
   }

   private boolean validateEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, Node var7, Map var8) throws WSSecurityException, XMLEncryptionException {
      EncryptedKey var9 = null;
      this.hasDkStrType(var5);
      List var11 = this.securityCtx.getEncryptions();
      Iterator var12 = var1.iterator();

      boolean var14;
      do {
         if (!var12.hasNext()) {
            return true;
         }

         TBE var13 = (TBE)var12.next();
         var14 = false;
         Iterator var15 = var11.iterator();

         while(var15.hasNext()) {
            Encryption var16 = (Encryption)var15.next();
            EncryptedData var17 = var16.getEncryptedData();
            EncryptedKey var18 = var16.getEncryptedKey();
            List var19 = var16.getNodes();
            if (var9 != null && var9 == var18) {
               if (var17.getEncryptionMethod().getAlgorithm().equals(var3.getAlgorithm()) && this.matchNodes(var19, var13)) {
                  var14 = true;
                  this.addReferenceIntoEncryptedDataMap(var8, var19, var17);
                  break;
               }
            } else {
               KeySelectorResultImpl var20 = (KeySelectorResultImpl)var16.getKeySelectorResult();
               if ((var16.getEncryptedKey() == null || !this.match(var5, var16.getEncryptedKey().getKeyInfo())) && var16.getEncryptedKey() != null || !this.validateSecurityToken(var20.getSecurityToken(), var4, var6, var7, Purpose.DECRYPT) || !this.validateIncludedInMessage(var20.getSecurityToken())) {
                  return false;
               }

               var9 = var18;
               if (var17.getEncryptionMethod().getAlgorithm().equals(var3.getAlgorithm()) && this.matchNodes(var19, var13)) {
                  var14 = true;
                  this.addReferenceIntoEncryptedDataMap(var8, var19, var17);
                  break;
               }
            }
         }
      } while(var14);

      return false;
   }

   private void addReferenceIntoEncryptedDataMap(Map var1, List var2, EncryptedData var3) {
      if (null != var1) {
         for(int var4 = 0; var4 < var2.size(); ++var4) {
            Node var5 = (Node)var2.get(var4);
            if (null != var5.getLocalName()) {
               var1.put(new QName(var5.getNamespaceURI(), var5.getLocalName()), var3.getId());

               try {
                  String var6 = this.getUri((Element)var5);
                  var1.put(var6, var3.getId());
               } catch (WSSecurityException var7) {
                  LogUtils.logEncrypt("NO URI for " + var5.getLocalName() + " ERROR " + var7.toString());
               }
            }
         }
      }

   }

   private boolean hasDkStrType(List var1) {
      if (null != var1 && var1.size() != 0) {
         Iterator var2 = var1.iterator();

         STRType var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (STRType)var2.next();
         } while(!"http://schemas.xmlsoap.org/ws/2005/02/sc/dk".equals(var3.getValueType()) && !"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk".equals(var3.getValueType()));

         return true;
      } else {
         return false;
      }
   }

   public boolean validateSignatureAndEncryptionRequest(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, XMLEncryptionException {
      if (var2 != null && var2.size() != 0) {
         boolean var10 = this.validateEncryption(var2, var3, var4, var5, var6, var7, (Node)null);
         if (!var10) {
            return false;
         }
      }

      if (var1 != null) {
         this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
         return this.validateSignature(var1, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", enckeyDirectSTRList, var7, (Node)null);
      } else {
         return true;
      }
   }

   public boolean validateEncryptionAndSignatureRequest(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, XMLEncryptionException {
      boolean var10 = true;
      if (var2 != null && var2.size() != 0) {
         HashMap var11 = new HashMap();
         var10 = this.validateEncryption(var2, var3, var4, var5, var6, var7, (Node)null, var11);
         if (!var10) {
            this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
            var10 = this.validateEncryption(var2, var3, var4, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", var6, var7, (Node)null, var11);
         }

         if (!var10) {
            return var10;
         }

         if (!var11.isEmpty()) {
            ((SecurityTokenContextHandler)var9).addContextElement("weblogic.wsee.security.encrypted_element.map", var11);
         }
      }

      if (var1 != null) {
         Map var14 = (Map)var9.getValue("weblogic.wsee.security.encrypted_element.map");
         if (null != var14) {
            Iterator var12 = var1.getReferences().iterator();

            while(var12.hasNext()) {
               ReferenceImpl var13 = (ReferenceImpl)var12.next();
               if (var14.containsKey(var13.getURI())) {
                  var13.setUri("#" + (String)var14.get(var13.getURI()));
               }
            }
         }

         this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
         var10 = this.validateSignature(var1, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", enckeyDirectSTRList, var7, (Node)null);
      }

      return var10;
   }

   public boolean validateSignatureAndEncryptionResponse(SignedInfo var1, List var2, EncryptionMethod var3, ContextHandler var4) throws WSSecurityException, XMLEncryptionException {
      this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
      if (var2 != null && var2.size() != 0) {
         boolean var5 = this.validateEncryption(var2, (EncryptionMethod)null, var3, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", (String)null, (Node)null);
         if (!var5) {
            return false;
         }
      }

      return var1 != null ? this.validateSignature(var1, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", (String)null, (Node)null) : true;
   }

   public boolean validateSignatureConfirmation() {
      WSS11Context var1 = (WSS11Context)this.securityCtx;
      List var2 = var1.getSignatureConfirmations();
      if (var2 != null && var2.size() != 0) {
         List var3 = var1.getPreviousMessageSignatureValues();
         boolean var4 = false;
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            String[] var6 = (String[])var5.next();
            if (this.validateSignatureConfirmationForRequest(var2, var6)) {
               var4 = true;
               break;
            }
         }

         return var4 ? true : true;
      } else {
         LogUtils.logWss("No SignatureConfirmation element.");
         return false;
      }
   }

   private boolean validateSignatureConfirmationForRequest(List var1, String[] var2) {
      String var4;
      if (var2 != null && var2.length > 0) {
         if (var2.length != var1.size()) {
            LogUtils.logWss("Number of SignatureConfirmation elements does not match number of Signatures from request.");
            return false;
         }

         for(int var8 = 0; var8 < var2.length; ++var8) {
            var4 = var2[var8];
            boolean var5 = false;
            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               SignatureConfirmation var7 = (SignatureConfirmation)var6.next();
               if (null != var7.getSignatureValue() && var7.getSignatureValue().equals(var4)) {
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               LogUtils.logWss("Failed to validate SignatureConfirmation/@Value: " + var4);
               return false;
            }
         }
      } else {
         if (var1.size() > 1) {
            LogUtils.logWss("No Signature in request, but more than one SignatureConfirmation in response.");
            return false;
         }

         SignatureConfirmation var3 = (SignatureConfirmation)var1.get(0);
         var4 = var3.getSignatureValue();
         if (var4 != null && !var4.trim().equals("")) {
            LogUtils.logWss("No Signature in request, but SignatureConfirmation/@Value is not empty.");
            return false;
         }
      }

      return true;
   }

   static {
      enckeyDirectSTR = new STRType(WSSConstants.REFERENCE_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
      enckeyDirectSTRList = new ArrayList();
      enckeyDirectSTRList.add(enckeyDirectSTR);
   }
}
