package weblogic.wsee.security.wst.internal;

import org.w3c.dom.Node;
import weblogic.wsee.security.wst.binding.RenewTarget;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestedSecurityToken;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class RenewProcessor extends ProcessorBase {
   public String getRequestType() {
      return "/Renew";
   }

   public Node processRequestSecurityToken(Node var1, WSTContext var2) throws WSTFaultException {
      RequestSecurityToken var3 = BindingHelper.unmarshalRSTNode(var1, getSecurityTokenHandler(var2));
      RequestSecurityTokenResponse var4 = createRSTR(var3, var2);
      setRequestType(var3, var4, var2);
      this.setLifetime(var3, var4, var2, true);
      RenewTarget var5 = var3.getRenewTarget();
      if (var5 == null) {
         throw new InvalidRequestException("RenewTarget is missing in renewal RST");
      } else {
         SecurityTokenReference var6 = var5.getSecurityTokenReference();
         TrustTokenProvider var7 = TrustTokenHelper.resolveTrustProvider(var3, var6);
         TrustToken var8 = var7.resolveTrustToken(var2, var6);
         var7.renewTrustToken(var2, var8);
         RequestedSecurityToken var9 = new RequestedSecurityToken(var2.getWstNamespaceURI());
         var9.setSecurityToken(var8);
         var4.setRequestedSecurityToken(var9);
         return BindingHelper.marshalRST(var4, var2);
      }
   }
}
