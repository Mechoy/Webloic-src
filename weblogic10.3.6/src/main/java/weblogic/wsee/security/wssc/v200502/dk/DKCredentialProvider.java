package weblogic.wsee.security.wssc.v200502.dk;

import weblogic.wsee.security.wssc.base.dk.DKCredentialProviderBase;
import weblogic.wsee.security.wssc.v200502.WSCConstants;
import weblogic.wsee.security.wssc.v200502.faults.UnknownDerivationSourceException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public class DKCredentialProvider extends DKCredentialProviderBase implements CredentialProvider {
   public String[] getValueTypes() {
      return WSCConstants.DK_VALUE_TYPES;
   }

   protected String getURI_P_SHA1() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc/dk/p_sha1";
   }

   protected UnknownDerivationSourceException newUnknownDerivationSourceException(String var1) {
      return new UnknownDerivationSourceException(var1);
   }
}
