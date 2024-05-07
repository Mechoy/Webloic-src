package weblogic.wsee.security.wst.internal;

import org.w3c.dom.Node;
import weblogic.wsee.security.wst.binding.CancelTarget;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestedTokenCancelled;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class CancelProcessor extends ProcessorBase {
   public String getRequestType() {
      return "/Cancel";
   }

   public Node processRequestSecurityToken(Node var1, WSTContext var2) throws WSTFaultException {
      RequestSecurityToken var3 = BindingHelper.unmarshalRSTNode(var1, getSecurityTokenHandler(var2));
      CancelTarget var4 = var3.getCancelTarget();
      if (var4 == null) {
         throw new InvalidRequestException("CancelTarget must be specified in RST");
      } else {
         SecurityTokenReference var5 = var4.getSecurityTokenReference();
         TrustTokenProvider var6 = TrustTokenHelper.resolveTrustProvider(var5.getValueType());
         TrustToken var7 = var6.resolveTrustToken(var2, var5);
         var6.cancelTrustToken(var2, var7);
         RequestSecurityTokenResponse var8 = createRSTR(var3, var2);
         RequestedTokenCancelled var9 = new RequestedTokenCancelled(var2.getWstNamespaceURI());
         var8.setRequestedTokenCancelled(var9);
         return BindingHelper.marshalRST(var8, var2);
      }
   }
}
