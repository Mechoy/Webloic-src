package weblogic.wsee.security.policy11.assertions;

import javax.xml.namespace.QName;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.security.policy12.assertions.SecurityPolicy12AssertionFactory;
import weblogic.wsee.util.Verbose;

public class SecurityPolicy11AssertionFactory extends SecurityPolicy12AssertionFactory {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicy11AssertionFactory.class);

   private static final void init() {
      if (verbose) {
         Verbose.log((Object)"In SecurityPolicy11AssertionFactory, registering WS-SP 1.2 assertions in namespace: http://schemas.xmlsoap.org/ws/2005/07/securitypolicy");
      }

      registerAssertions("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy");
      ExternalizationUtils.registerExternalizable(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "HttpsToken", "sp"), HttpsToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "SecureConversationToken", "sp"), SecureConversationToken.class.getName());
   }

   static {
      init();
   }
}
