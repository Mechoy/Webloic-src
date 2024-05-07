package weblogic.xml.crypto.wss11.internal;

import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.dom.DOMDecryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;

public class SecurityImpl extends weblogic.xml.crypto.wss.SecurityImpl implements Security {
   private static SignatureConfirmationHandler sch = new SignatureConfirmationHandler();

   public SecurityImpl() {
      this.init();
   }

   public SecurityImpl(WSSecurityContext var1) {
      super(var1);
      this.init();
   }

   protected void processAndMarshal(XMLStructure var1, XMLCryptoContext var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      if (var1 instanceof SignatureConfirmation) {
         this.marshalSigConf((SignatureConfirmation)var1, var3);
      } else {
         super.processAndMarshal(var1, var2, var3);
      }

   }

   private void marshalSigConf(SignatureConfirmation var1, ContextHandler var2) throws MarshalException {
      Node var3 = this.findInsertBeforeNode(this.security, var2, false);

      try {
         var1.marshal(this.security, var3, this.namespaces);
      } catch (weblogic.xml.dom.marshal.MarshalException var5) {
         throw new MarshalException(var5);
      }
   }

   protected EncryptedKeyProvider handleEncryptedKey(EncryptedKey var1, DOMDecryptContext var2, WSSecurityContext var3) throws WSSecurityException {
      EncryptedKeyToken var4 = new EncryptedKeyToken((EncryptedKeyProvider)null, var1.getId());
      EncryptedKeyProvider var5 = null;

      try {
         var5 = new EncryptedKeyProvider(var1, var4, var2);
         var4.setKeyProvider(var5);
      } catch (XMLEncryptionException var8) {
         throw new WSSecurityException(var8, WSSConstants.FAILURE_INVALID);
      }

      var3.addKeyProvider(var5);
      var3.addSecurityToken(var4);
      var3.addToken(var4, var3.getElementById(var1.getId()));
      List var6 = var1.getReferenceList();
      if ((var6 == null || var6.size() == 0) && var4 != null) {
         KeySelectorResult var7 = var5.getKey("http://www.w3.org/2001/04/xmlenc#aes256-cbc", KeySelector.Purpose.DECRYPT);
         if (var7 != null) {
            var4.setSecretKey(var7.getKey());
         } else {
            var7 = var5.getKey("http://www.w3.org/2001/04/xmlenc#tripledes-cbc", KeySelector.Purpose.DECRYPT);
            if (var7 != null) {
               var4.setSecretKey(var7.getKey());
            }
         }
      }

      return var5;
   }

   protected boolean isHeader(Node var1) {
      return DOMUtils.is(var1.getParentNode(), "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "EncryptedHeader");
   }

   protected void handleEncryptedHeader(Node var1) {
      Node var2 = var1.getParentNode();
      Node var3 = var2.getNextSibling();
      Node var4 = var2.getParentNode();
      var4.removeChild(var2);
      var4.insertBefore(var1, var3);
   }

   private void init() {
      this.register(sch);
   }

   static class SignatureConfirmationHandler implements weblogic.xml.crypto.wss.SecurityImpl.SecurityHeaderElementHandler {
      private static final String EMPTY_STRING = "";

      public QName getQName() {
         return WSS11Constants.SIG_CONF_QNAME;
      }

      public void process(Node var1, WSSecurityContext var2) throws MarshalException {
         SignatureConfirmation var3 = WSS11Factory.newSignatureConfirmation();

         try {
            var3.unmarshal(var1);
            ((WSS11Context)var2).addSignatureConfirmation(var3);
         } catch (weblogic.xml.dom.marshal.MarshalException var5) {
            throw new MarshalException(var5);
         }
      }

      public void validate(WSSecurityContext var1) throws WSSecurityException {
         WSS11Context var2 = (WSS11Context)var1;
         if (null == var2) {
            this.handleError("Invalid context");
         }

      }

      private void handleError(String var1) throws WSSecurityException {
         LogUtils.logWss("Failed to validate SignatureConfirmation. " + var1);
         throw new WSSecurityException("Failed to validate SignatureConfirmation." + var1, WSSConstants.FAILURE_INVALID);
      }
   }
}
