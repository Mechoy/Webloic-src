package weblogic.wsee.security.wssc.v13.sct;

import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.wssc.base.faults.WSCFaultException;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.base.sct.ServerSCCredentialProviderBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.wsee.security.wssc.v13.faults.UnableToRenewException;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ServerSCCredentialProvider extends ServerSCCredentialProviderBase implements TrustTokenProvider {
   protected String getSCT_VALUE_TYPE() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   }

   protected String[] getSCT_VALUE_TYPES() {
      return WSCConstants.SCT_VALUE_TYPES;
   }

   protected SCTokenBase newSCToken(SCCredential var1) {
      return new SCToken(var1);
   }

   protected WSCFaultException newUnableToRenewException(String var1) {
      return new UnableToRenewException(var1);
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      return super.getCredential(var1, var2, var3, var4, "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512");
   }
}
