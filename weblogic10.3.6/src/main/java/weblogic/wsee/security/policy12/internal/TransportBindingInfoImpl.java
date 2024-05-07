package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.TransportBinding;
import weblogic.wsee.security.policy12.assertions.TransportToken;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;
import weblogic.wsee.security.wssp.TransportBindingInfo;

public class TransportBindingInfoImpl extends SecurityBindingPropertiesAssertionImpl implements TransportBindingInfo {
   private HttpsTokenAssertionImpl httpsTokenAsst = null;

   TransportBindingInfoImpl(TransportBinding var1) {
      super(var1);
      TransportToken var2 = var1.getTransportToken();
      if (var2 != null) {
         this.httpsTokenAsst = new HttpsTokenAssertionImpl(var2.getHttpsToken());
      }

   }

   public HttpsTokenAssertion getHttpsTokenAssertion() {
      return this.httpsTokenAsst;
   }
}
