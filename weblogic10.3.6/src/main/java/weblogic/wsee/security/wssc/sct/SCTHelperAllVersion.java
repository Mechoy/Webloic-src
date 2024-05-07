package weblogic.wsee.security.wssc.sct;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.security.wssc.v13.sct.SCTHelper;
import weblogic.wsee.util.Verbose;

public class SCTHelperAllVersion {
   private static final boolean verbose = Verbose.isVerbose(SCTHelperAllVersion.class);

   public static final String getCredentialIdentifier(SOAPMessageContext var0) {
      String var1 = SCTHelper.getCredentialIdentifier(var0);
      if (var1 == null) {
         var1 = weblogic.wsee.security.wssc.v200502.sct.SCTHelper.getCredentialIdentifier(var0);
      }

      if (verbose) {
         if (var1 == null) {
            Verbose.log((Object)"Could not find SCToken Credential Identifier.  Tried SecureConversation versions 1.3 and 200502.  If you are expecting to find an Identifier for a different version, you need to add the new version to weblogic.wsee.security.wssc.sct.SCTHelperAllVersion.getCredentialIdentifier(..) ");
         } else {
            Verbose.log((Object)("Found SCToken Credential Identifier='" + var1 + "'"));
         }
      }

      return var1;
   }
}
