package weblogic.wsee.security.wssc.base.dk;

import java.security.Key;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.dk.DKClaims;
import weblogic.wsee.security.wssc.dk.DKCredential;
import weblogic.wsee.security.wssc.dk.DKTokenReference;
import weblogic.wsee.security.wst.helpers.EncryptedKeyInfoBuilder;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.SecretKeyProvider;
import weblogic.xml.crypto.wss.SecurityTokenHelper;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public abstract class DKTokenHandlerBase implements SecurityTokenHandler {
   protected abstract QName[] getDK_QNAMES();

   protected abstract String[] getDK_VALUE_TYPES();

   protected abstract DKTokenBase newDKToken();

   protected abstract DKTokenBase newDKToken(DKCredential var1);

   protected abstract String getDK_VALUE_TYPE();

   protected abstract String getSCT_RST_ACTION();

   protected abstract String getXMLNS_WSC();

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      if (var2 instanceof DKCredential) {
         DKCredential var4 = (DKCredential)var2;
         if (var4.getLabel() == null) {
            var4.setLabel(DKClaims.getLabelFromContextHandler(var3));
         }

         if (var4.getLength() == -1) {
            var4.setLength(DKClaims.getLengthFromContextHandler(var3));
         }

         return this.newDKToken(var4);
      } else {
         return null;
      }
   }

   public SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      return SecurityTokenHelper.findSecurityTokenInContext(var4, var1);
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      return var3 instanceof DKTokenBase && var1.equals(WSSConstants.REFERENCE_QNAME) ? new DKTokenReference((DKTokenBase)var3) : null;
   }

   public QName[] getQNames() {
      return this.getDK_QNAMES();
   }

   public String[] getValueTypes() {
      return this.getDK_VALUE_TYPES();
   }

   public SecurityToken newSecurityToken(Node var1) throws MarshalException {
      DKTokenBase var2 = this.newDKToken();

      try {
         var2.unmarshal(var1);
         return var2;
      } catch (weblogic.xml.dom.marshal.MarshalException var4) {
         throw new MarshalException("Failed to unmarshal DerivedKeyToken.", var4);
      }
   }

   public SecurityTokenReference newSecurityTokenReference(Node var1) throws weblogic.xml.dom.marshal.MarshalException {
      DKTokenReference var2 = new DKTokenReference();
      var2.unmarshal(var1);
      return var2;
   }

   public KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2) {
      SecretKeyProvider var3 = null;
      if (var1 instanceof DKTokenBase) {
         Key var4 = ((DKTokenBase)var1).getSecretKey(var2);
         if (var4 != null) {
            byte[] var5 = var1.getId() != null ? var1.getId().getBytes() : null;
            var3 = new SecretKeyProvider(var4, (String)null, var5, SecurityTokenHelper.getURI(var1), var1);
            EncryptedKeyInfoBuilder.debugKey(var4, "secretKey returned from DK TokenHandler of KeyProvider");
         }
      }

      return var3;
   }

   public SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      if (WSSConstants.REFERENCE_QNAME.equals(var1.getSTRType())) {
         return SecurityTokenHelper.findSecurityTokenByIdInContext(var3, var1.getValueType(), SecurityTokenHelper.getIdFromURI(var1.getReferenceURI()));
      } else if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1.getSTRType())) {
         KeyIdentifier var4 = var1.getKeyIdentifier();
         return SecurityTokenHelper.findSecurityTokenByIdInContext(var3, var1.getValueType(), new String(var4.getIdentifier()));
      } else {
         throw new WSSecurityException("Failed to retrieve token for reference " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
      }
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return new SecurityTokenValidateResult(true);
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      return new SecurityTokenValidateResult(true);
   }

   public boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5) {
      return var1 instanceof DKTokenBase && var2.equals(this.getDK_VALUE_TYPE());
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return null;
   }
}
