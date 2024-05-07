package weblogic.wsee.wstx.wsat.security;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.base.sct.SCTHelper;
import weblogic.wsee.security.wssc.base.sct.SCTokenHandlerBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.sct.SCTokenHandler;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public class IssuedTokenCPBuilder {
   private SCTokenHandlerBase tokenHandler;
   private Node rstrNode = null;
   private String algorithm = "AES";

   public IssuedTokenCPBuilder algorithm(String var1) {
      this.algorithm = var1;
      return this;
   }

   public IssuedTokenCPBuilder issuedTokens(Element var1) {
      String var2 = var1.getNodeName();
      String var3 = var1.getNamespaceURI();
      if ("IssuedTokens".equals(var2)) {
         if ("http://docs.oasis-open.org/ws-sx/ws-trust/200512".equals(var3)) {
            this.tokenHandler = new SCTokenHandler();
         } else if ("http://schemas.xmlsoap.org/ws/2005/02/trust".equals(var3)) {
            this.tokenHandler = new weblogic.wsee.security.wssc.v200502.sct.SCTokenHandler();
         }
      }

      if (this.tokenHandler == null) {
         throw new IllegalArgumentException("not valid issuedTokens");
      } else {
         NodeList var4 = var1.getElementsByTagNameNS(var3, "RequestSecurityTokenResponse");
         if (var4.getLength() > 0) {
            this.rstrNode = var4.item(0);
         }

         if (this.rstrNode == null) {
            throw new IllegalArgumentException("issuedTokens doesn't include RequestSecurityTokenResponse element");
         } else {
            return this;
         }
      }
   }

   public CredentialProvider build() {
      try {
         RequestSecurityTokenResponse var1 = BindingHelper.unmarshalRSTRNode(this.rstrNode, this.tokenHandler);
         SecretKeySpec var2 = new SecretKeySpec(var1.getRequestedProofToken().getBinarySecret().getValue(), this.algorithm);
         final SCCredential var3 = SCTHelper.getSCCredentialFromRSTR((WSTContext)null, var2, var1, this.tokenHandler);
         return new CredentialProvider() {
            public String[] getValueTypes() {
               return IssuedTokenCPBuilder.this.tokenHandler.getValueTypes();
            }

            public Object getCredential(String var1, String var2, ContextHandler var3x, Purpose var4) {
               return var3;
            }
         };
      } catch (Exception var4) {
         throw new WebServiceException(var4);
      }
   }
}
