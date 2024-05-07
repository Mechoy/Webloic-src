package weblogic.wsee.security.wst.internal;

import java.util.Locale;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.security.saml.SAMLIssuedTokenHelper;
import weblogic.wsee.security.wst.binding.AppliesTo;
import weblogic.wsee.security.wst.binding.CancelTarget;
import weblogic.wsee.security.wst.binding.ComputedKeyAlgorithm;
import weblogic.wsee.security.wst.binding.Entropy;
import weblogic.wsee.security.wst.binding.KeySize;
import weblogic.wsee.security.wst.binding.KeyType;
import weblogic.wsee.security.wst.binding.Lifetime;
import weblogic.wsee.security.wst.binding.OnBehalfOf;
import weblogic.wsee.security.wst.binding.RenewTarget;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.RequestType;
import weblogic.wsee.security.wst.binding.SecondaryParameters;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.BadRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustRequestor;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class TrustRequestorImpl implements TrustRequestor {
   private static final boolean verbose = Verbose.isVerbose(TrustRequestorImpl.class);

   public Node newRequestSecurityToken(WSTContext var1) throws WSTFaultException {
      String var2 = this.getWSTNamespaceUri(var1);
      RequestSecurityToken var3 = new RequestSecurityToken(var2);
      var3.setContext(var1.getContext());
      RequestType var4 = new RequestType(var2);
      var4.setRequestType(var2 + "/Issue");
      var3.setRequestType(var4);
      String var5 = this.setTokenType(var1, var3);
      this.setKeyType(var1, var3);
      this.setOnBehalfOf(var1, var3);
      String var6 = var1.getAppliesTo();
      Element var7 = var1.getAppliesToElement();
      AppliesTo var8;
      if (var6 != null) {
         var8 = new AppliesTo(var1.getWspNamespaceURI());
         var8.setEndpointReference(var1.getWsaNamespaceURI(), var6);
         var3.setAppliesTo(var8);
      } else if (var7 != null) {
         var8 = new AppliesTo(var1.getWspNamespaceURI());
         var8.setElement(var7);
         var3.setAppliesTo(var8);
      }

      if (var5 == null && var6 == null && var7 == null) {
         throw new BadRequestException("Either TokenType or AppliesTo should be defined");
      } else {
         Entropy var15 = null;
         String var9 = var1.getBinarySecretType();
         if (!"none".equals(var9)) {
            if (var9 == null) {
               var9 = var2 + "/Nonce";
            }

            if (var9.endsWith("/Nonce")) {
               var15 = BindingHelper.createNewEntropy(var2, var9);
               var1.setRstNonce(var15.getBinarySecret().getValue());
            } else if (!var9.endsWith("/SymmetricKey") && var9.endsWith("/AsymmetricKey")) {
            }

            var3.setEntropy(var15);
         }

         if (var1.getLifetimePeriod() != -1L) {
            Lifetime var10 = new Lifetime(var2);
            var10.setPeriod(var1.getLifetimePeriod(), var1.getWsuNamespaceURI());
            var3.setLifetime(var10);
         }

         if (var1.getKeySize() != -1) {
            KeySize var12 = new KeySize(var2);
            var12.setSize(var1.getKeySize());
            var3.setKeySize(var12);
         }

         if (var1.hasIssuedTokenClaims()) {
            SAMLIssuedTokenHelper var13 = new SAMLIssuedTokenHelper(var1.getIssuedTokenClaims());
            SecondaryParameters var11 = var13.biuldSecondaryParameters();
            var3.setSecondaryParameters(var11);
            var1.setComputedKeyAlgorithm(var2 + "/CK/PSHA1");
         }

         if (var1.getComputedKeyAlgorithm() != null) {
            ComputedKeyAlgorithm var14 = new ComputedKeyAlgorithm(var2);
            var14.setUri(var1.getComputedKeyAlgorithm());
            var3.setComputedKeyAlgorithm(var14);
         }

         return BindingHelper.marshalRST(var3, var1);
      }
   }

   public Node renewRequestSecurityToken(TrustToken var1, WSTContext var2) throws WSTFaultException {
      String var3 = this.getWSTNamespaceUri(var2);
      RequestSecurityToken var4 = new RequestSecurityToken(var3);
      RequestType var5 = new RequestType(var3);
      var5.setRequestType(var3 + "/Renew");
      var4.setRequestType(var5);
      String var6 = var2.getTokenType();
      if (var6 != null) {
         TokenType var7 = new TokenType(var3);
         var7.setTokenType(var6);
      }

      RenewTarget var11 = new RenewTarget(var3);
      TrustTokenProvider var8 = TrustTokenHelper.resolveTrustProvider(var6 == null ? var1.getValueType() : var6);
      SecurityTokenReference var9 = var8.createSecurityTokenReference(var2, var1);
      var11.setSecurityTokenReference(var9);
      var4.setRenewTarget(var11);
      Lifetime var10 = new Lifetime(var3);
      var10.setPeriod(var2.getLifetimePeriod(), var2.getWsuNamespaceURI());
      var4.setLifetime(var10);
      return BindingHelper.marshalRST(var4, var2);
   }

   public Node cancelRequestSecurityToken(TrustToken var1, WSTContext var2) throws WSTFaultException {
      String var3 = this.getWSTNamespaceUri(var2);
      TrustTokenProvider var4 = TrustTokenHelper.resolveTrustProvider(var1.getValueType());
      SecurityTokenReference var5 = var4.createSecurityTokenReference(var2, var1);
      RequestSecurityToken var6 = new RequestSecurityToken(var3);
      RequestType var7 = new RequestType(var3);
      var7.setRequestType(var3 + "/Cancel");
      var6.setRequestType(var7);
      CancelTarget var8 = new CancelTarget(var3);
      var8.setSecurityTokenReference(var5);
      var6.setCancelTarget(var8);
      Node var9 = BindingHelper.marshalRST(var6, var2);
      return var9;
   }

   public Node validateRequestSecurityToken(TrustToken var1, WSTContext var2) throws WSTFaultException {
      throw new RuntimeException("NYI");
   }

   private String getWSTNamespaceUri(WSTContext var1) throws WSTFaultException {
      String var2 = var1.getWstNamespaceURI();
      if (var2 == null) {
         String var3 = var1.getTrustVersion();
         if (var3 == null) {
            throw new WSTFaultException(" could not get WS-Trust namespace from WSTContext.  WSTContext.getTrustVersion == null !");
         }

         String var4 = var3.toLowerCase(Locale.ENGLISH);
         if (verbose) {
            Verbose.log((Object)(" getting wstNsUri from trust version='" + var4 + "'"));
         }

         if (var4.startsWith("http://schemas.xmlsoap.org/ws/2005/02/trust")) {
            var2 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
         } else {
            if (!var4.startsWith("http://docs.oasis-open.org/ws-sx/ws-trust/200512")) {
               throw new WSTFaultException(" could not get WS-Trust namespace. unknown WS-Trust Version='" + var4 + "'");
            }

            var2 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
         }

         if (verbose) {
            Verbose.log((Object)(" setting wstNsUri to '" + var2 + "'"));
         }

         var1.setWstNamespaceURI(var2);
      } else if (verbose) {
         Verbose.log((Object)("got WS-Trust namespace from WSTContext='" + var2 + "'"));
      }

      return var2;
   }

   private String setTokenType(WSTContext var1, RequestSecurityToken var2) {
      String var3 = var1.getTokenType();
      if (var3 != null) {
         TokenType var4 = new TokenType(var1.getWstNamespaceURI());
         var4.setTokenType(var3);
         var2.setTokenType(var4);
      }

      return var3;
   }

   private void setKeyType(WSTContext var1, RequestSecurityToken var2) {
      String var3 = var1.getKeyType();
      if (var3 != null) {
         KeyType var4 = new KeyType(var1.getWstNamespaceURI());
         var4.setKeyType(var3);
         var2.setKeyType(var4);
      }

   }

   private void setOnBehalfOf(WSTContext var1, RequestSecurityToken var2) {
      SecurityToken var3 = var1.getOnBehalfOfToken();
      if (var3 != null) {
         OnBehalfOf var4 = new OnBehalfOf(var1.getWstNamespaceURI());
         var4.setSecurityToken(var3);
         var2.setOnBehalfOf(var4);
      }

   }
}
