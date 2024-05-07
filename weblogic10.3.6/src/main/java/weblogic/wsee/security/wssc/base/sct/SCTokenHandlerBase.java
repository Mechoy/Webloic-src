package weblogic.wsee.security.wssc.base.sct;

import java.security.Key;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.faults.FaultVersionHelper;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.sct.SCTStore;
import weblogic.wsee.security.wssc.sct.SCTokenReference;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.SecretKeyProvider;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.SecurityTokenHelper;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public abstract class SCTokenHandlerBase implements SecurityTokenHandler {
   private static final boolean verbose = Verbose.isVerbose(SCTokenHandlerBase.class);

   protected abstract String getSCT_IDENTIFIER();

   protected abstract String getSCT_RST_ACTION();

   protected abstract String getSCT_RST_CANCEL_ACTION();

   protected abstract String getSCT_RST_RENEW_ACTION();

   protected abstract QName[] getSCT_QNAMES();

   protected abstract String[] getSCT_VALUE_TYPES();

   protected abstract String getSCT_VALUE_TYPE();

   protected abstract String getXMLNS_WSC();

   protected abstract SCTokenBase newSCToken();

   protected abstract SCTokenBase newSCToken(SCCredential var1);

   protected abstract SCCredential newSCCredential();

   protected abstract String getCANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY();

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      if (!(var2 instanceof SCCredential)) {
         return null;
      } else {
         if (var3 != null) {
            SecurityToken var4 = this.getSecurityToken(var1, (String)null, (Purpose)null, var3);
            if (var4 != null && ((SCTokenBase)var4).getCredential().equals(var2)) {
               return var4;
            }
         }

         return this.newSCToken((SCCredential)var2);
      }
   }

   public SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      return SecurityTokenHelper.findSecurityTokenInContext(var4, var1);
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      SCTokenBase var4 = (SCTokenBase)var3;
      SCCredential var5 = var4.getCredential();
      if (var5 != null) {
         SCCredential.SecurityTokenReferenceInfo var6 = null;
         var6 = var5.getUnattachedSecurityTokenReferenceInfo();
         if (var6 == null) {
            var6 = var5.getAttachedSecurityTokenReferenceInfo();
         }

         if (var6 != null) {
            SCTokenReference var7 = new SCTokenReference(var4);
            SCCredential.copyFromInfoToSTR(var6, var7);
            return var7;
         }
      }

      return new SCTokenReference(var1, var4);
   }

   public QName[] getQNames() {
      return this.getSCT_QNAMES();
   }

   public String[] getValueTypes() {
      return this.getSCT_VALUE_TYPES();
   }

   public SecurityToken newSecurityToken(Node var1) throws MarshalException {
      SCTokenBase var2 = this.newSCToken();

      try {
         var2.unmarshal(var1);
         return var2;
      } catch (weblogic.xml.dom.marshal.MarshalException var4) {
         throw new MarshalException("Failed to unmarshal SecurityContextToken.", var4);
      }
   }

   public SecurityTokenReference newSecurityTokenReference(Node var1) throws weblogic.xml.dom.marshal.MarshalException {
      SCTokenReference var2 = new SCTokenReference();
      var2.unmarshal(var1);
      return var2;
   }

   public KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2) {
      SecretKeyProvider var3 = null;
      if (var1 instanceof SCTokenBase) {
         SCTokenBase var4 = (SCTokenBase)var1;
         Key var5 = var4.getSecretKey();
         if (var5 != null) {
            byte[] var6 = var4.getId() != null ? var4.getId().getBytes() : null;
            String var7 = var4.getTrustCredential().getIdentifier();
            Object var8 = var7 != null ? new HashSet(Arrays.asList(SecurityTokenHelper.getURI(var4), var7)) : Collections.singleton(SecurityTokenHelper.getURI(var4));
            var3 = new SecretKeyProvider(var5, (String)null, var6, (Collection)var8, var1);
         }
      }

      return var3;
   }

   public SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      String var4 = null;
      if (WSSConstants.REFERENCE_QNAME.equals(var1.getSTRType())) {
         String var5 = var1.getReferenceURI();
         var4 = var5.startsWith("#") ? SecurityTokenHelper.getIdFromURI(var1.getReferenceURI()) : var5;
      } else if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1.getSTRType())) {
         KeyIdentifier var8 = var1.getKeyIdentifier();
         var4 = var8.getIdentifier() == null ? null : new String(var8.getIdentifier());
      }

      SecurityToken var9 = null;
      if (var4 != null) {
         var9 = this.findSecurityTokenByIdInContext(var3, var1.getValueType(), var4);
      }

      if (var9 != null) {
         if (var9 instanceof SCTokenBase) {
            SCTokenBase var10 = (SCTokenBase)var9;
            checkExpiration(var2, var10.getCredential(), this.getXMLNS_WSC());
         }

         return var9;
      } else {
         String var6 = SCCredentialProviderBase.getPhysicalStoreNameFromMessageContext(var2);
         SCCredential var7 = SCTStore.get(var4, var6);
         if (var7 != null) {
            if (verbose) {
               Verbose.log((Object)("=== got SCCredential '" + var4 + "' from SCTStore ==="));
            }

            checkExpiration(var2, var7, this.getXMLNS_WSC());
            return this.newSCToken(var7);
         } else {
            if (verbose) {
               Verbose.log((Object)("===  NO SCCredential '" + var4 + "' in SCTStore ==="));
            }

            throw new WSSecurityException("Failed to retrieve token for reference " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }
      }
   }

   static SCCredential checkExpiration(MessageContext var0, SCCredential var1, String var2) {
      if ("true".equals(var0.getProperty("weblogic.wsee.security.wssc.needCheckSCTExpiration"))) {
         if (null == var1) {
            return null;
         }

         if (verbose) {
            Verbose.log((Object)"Checking SCT experation");
         }

         String var3 = (String)var0.getProperty("weblogic.wsee.security.wssc.checkingSCTExpiration");
         if ("laxCheckingSCTExpiration".equals(var3)) {
            return var1;
         }

         Calendar var4 = null;
         if (null != var1.getExpires()) {
            var4 = (Calendar)var1.getExpires().clone();
            if ("tolerantCheckingSCTExpiration".equals(var3)) {
               var4.add(13, 60);
            }
         }

         if (var1 != null && TrustTokenHelper.isExpired(var0, var1.getCreated(), var4)) {
            WSTFaultUtil.raiseFault(FaultVersionHelper.newRenewNeededException(var2, "SCToken expired: " + var1.getIdentifier()));
         }
      }

      return var1;
   }

   private final SecurityToken findSecurityTokenByIdInContext(WSSecurityContext var1, String var2, String var3) {
      List var4 = var1.getSecurityTokens();
      SecurityToken var5 = null;
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         SecurityToken var7 = (SecurityToken)var6.next();
         if (var7 instanceof SCTokenBase && var7.getValueType().equals(var2) && (var3.equals(var7.getId()) || var3.equals(((SCTokenBase)var7).getTrustCredential().getIdentifier()))) {
            var5 = var7;
            break;
         }
      }

      return var5;
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      this.resolveCredential((SCTokenBase)var1, var2);
      return new SecurityTokenValidateResult(true);
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      return new SecurityTokenValidateResult(true);
   }

   public boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5) {
      if (!(var1 instanceof SCTokenBase)) {
         return false;
      } else {
         return var2.equals(this.getSCT_VALUE_TYPE());
      }
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return ((SCCredential)var1.getCredential()).getSubject();
   }

   private void resolveCredential(SCTokenBase var1, MessageContext var2) throws WSSecurityException {
      SCCredential var3 = var1.getCredential();
      SCCredential var4 = SCCredentialProviderBase.getSCFromContext(var2);
      if (var3.equals(var4)) {
         var1.setCredential(var4);
      } else {
         WSSecurityContext var5 = WSSecurityContext.getSecurityContext(var2);
         CredentialProvider var6 = var5.getCredentialProvider(var1.getValueType());
         SecurityTokenContextHandler var7 = new SecurityTokenContextHandler(var5);
         SCCredentialProviderBase.setSCToContext(var2, var3);
         var3 = (SCCredential)var6.getCredential(var1.getValueType(), (String)null, var7, Purpose.IDENTITY);
         if (var3 == null) {
            throw new WSSecurityException("Can not find SCT with id: " + var1.getId(), WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }

         var1.setCredential(var3);
         SCCredentialProviderBase.setSCToContext(var2, var3);
      }

   }
}
