package weblogic.wsee.security.wst.internal;

import java.security.Key;
import org.w3c.dom.Node;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.wsee.security.wst.binding.Entropy;
import weblogic.wsee.security.wst.binding.OnBehalfOf;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestedAttachedReference;
import weblogic.wsee.security.wst.binding.RequestedProofToken;
import weblogic.wsee.security.wst.binding.RequestedSecurityToken;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.EncryptedKeyInfoBuilder;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class IssueProcessor extends ProcessorBase {
   public String getRequestType() {
      return "/Issue";
   }

   public Node processRequestSecurityToken(Node var1, WSTContext var2) throws WSTFaultException {
      RequestSecurityToken var3 = BindingHelper.unmarshalRSTNode(var1);
      RequestSecurityTokenResponse var4 = createRSTR(var3, var2);
      setRequestType(var3, var4, var2);
      this.setAppliesTo(var3, var4, var2);
      this.setLifetime(var3, var4, var2, false);
      this.setKeySize(var3, var4, var2);
      String var5 = setTokenType(var3, var4, var2);
      this.setKeyType(var3, var5, var2);
      TrustTokenProvider var6 = TrustTokenHelper.resolveTrustProvider(var5);
      EntropyHandler var7 = new EntropyHandler(var2, var3.getEntropy());
      Key var8 = var7.getKey();
      var2.setSymmetricKey(var8);
      EncryptedKeyInfoBuilder.debugKey(var2.getSymmetricKey(), "Gerenated secretKey and Saved Key onto RSTR");
      Entropy var9 = var7.getResponseEntropy();
      if (var9 != null) {
         var4.setEntropy(var9);
      }

      OnBehalfOf var10 = var3.getOnBehalfOf();
      if (var10 != null) {
         SecurityToken var11 = var10.getSecurityToken();
         var2.setOnBehalfOfToken(var11);
      }

      TrustToken var15 = var6.issueTrustToken(var2);
      RequestedSecurityToken var12 = new RequestedSecurityToken(var2.getWstNamespaceURI());
      var12.setSecurityToken(var15);
      var4.setRequestedSecurityToken(var12);
      String var13 = var2.getKeyType();
      if (SAMLUtils.isSamlTokenType(var5) && SAMLUtils.isSymmetricKeyType(var13)) {
         EntropyHandler var14 = new EntropyHandler(var2, var2.getSymmetricKey());
         var9 = var14.getResponseEntropy(var2.getSymmetricKey());
         EncryptedKeyInfoBuilder.debugKey(var2.getSymmetricKey(), "Saving Symmetric Key onto RSTR from wstCtx");
         if (var9 != null) {
            var4.setEntropy(var9);
         }
      }

      if (var13 != null && var13.endsWith("Bearer")) {
         var4.setRequestedProofToken((RequestedProofToken)null);
      } else {
         RequestedProofToken var16 = var7.getRequestedProofToken();
         if (var16 != null) {
            var4.setRequestedProofToken(var16);
         }
      }

      if (var15.getId() == null) {
         RequestedAttachedReference var17 = new RequestedAttachedReference(var2.getWstNamespaceURI());
         var17.setSecurityTokenReference(var6.createSecurityTokenReference(var2, var15));
         var4.setRequestedAttachedReference(var17);
      }

      return BindingHelper.marshalRST(var4, var2);
   }
}
