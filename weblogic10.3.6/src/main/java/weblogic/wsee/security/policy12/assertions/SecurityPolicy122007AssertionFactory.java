package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.util.Verbose;

public class SecurityPolicy122007AssertionFactory extends SecurityPolicy12AssertionFactory {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicy122007AssertionFactory.class);

   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      String var2 = var1.getNamespaceURI();
      if (var2 != null && (var2.equals("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702") || var2.equals("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200512") || var2.equals("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy") || var2.equals("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802"))) {
         String var3 = ExternalizationUtils.getClassNameFromMap(new QName(var1.getNamespaceURI(), var1.getLocalName()));
         if (var3 != null) {
            try {
               PolicyAssertion var4 = (PolicyAssertion)Class.forName(var3).newInstance();
               if (var4 instanceof AbstractSecurityPolicyAssertion) {
                  ((AbstractSecurityPolicyAssertion)var4).initialize((Element)var1);
               }

               return var4;
            } catch (ClassNotFoundException var5) {
               var5.printStackTrace();
            } catch (IllegalAccessException var6) {
               var6.printStackTrace();
            } catch (InstantiationException var7) {
               var7.printStackTrace();
            }
         }
      }

      return null;
   }

   private static final void init() {
      if (verbose) {
         Verbose.log((Object)"In SecurityPolicy12AssertionFactory, registering WS-SP 1.2 assertions in namespaces: http://schemas.xmlsoap.org/ws/2005/07/securitypolicy and http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702");
      }

      registerAssertions("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702");
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702", "HttpsToken", "sp"), HttpsToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702", "SecureConversationToken", "sp"), SecureConversationToken.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702", "Trust13", "sp"), Trust13.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "Nonce", "sp13"), Nonce.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "Created", "sp13"), Created.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "XPath2", "sp13"), XPath2.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "ScopePolicy15", "sp13"), ScopePolicy15.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "MustSupportInteractiveChallenge", "sp13"), MustSupportInteractiveChallenge.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "ContentSignatureTransform", "sp13"), ContentSignatureTransform.class.getName());
      ExternalizationUtils.registerExternalizable(new QName("http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802", "AttachmentCompleteSignatureTransform", "sp13"), AttachmentCompleteSignatureTransform.class.getName());
   }

   static {
      init();
   }
}
