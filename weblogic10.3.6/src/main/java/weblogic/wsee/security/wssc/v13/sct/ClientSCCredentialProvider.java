package weblogic.wsee.security.wssc.v13.sct;

import javax.xml.rpc.handler.MessageContext;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.base.sct.ClientSCCredentialProviderBase;
import weblogic.wsee.security.wssc.base.sct.SCTokenHandlerBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustSoapClient;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ClientSCCredentialProvider extends ClientSCCredentialProviderBase {
   public static void cancelSCToken(MessageContext var0) {
      ClientSCCredentialProviderBase.cancelSCToken(var0, (SCTokenHandlerBase)(new SCTokenHandler()));
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      return this.getCredential(var1, var2, var3, var4, new SCTokenHandler());
   }

   protected String[] getSCT_VALUE_TYPES() {
      return WSCConstants.SCT_VALUE_TYPES;
   }

   public static SCCredential createSCCredential(TrustSoapClient var0, WSTContext var1) throws WSTFaultException {
      return ClientSCCredentialProviderBase.createSCCredential(var0, var1, new SCTokenHandler());
   }
}
