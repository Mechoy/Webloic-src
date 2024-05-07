package weblogic.wsee.security.wssc.v13.dk;

import weblogic.wsee.security.wssc.base.dk.DKCredentialProviderBase;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.wsee.security.wssc.v13.faults.UnknownDerivationSourceException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public class DKCredentialProvider extends DKCredentialProviderBase implements CredentialProvider {
   public String[] getValueTypes() {
      return WSCConstants.DK_VALUE_TYPES;
   }

   protected String getURI_P_SHA1() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   }

   protected UnknownDerivationSourceException newUnknownDerivationSourceException(String var1) {
      return new UnknownDerivationSourceException(var1);
   }
}
