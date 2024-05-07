package weblogic.entitlement.rules;

import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public class HttpRequestAttrNumberGreater extends HttpRequestAttrNumberPredicate {
   public HttpRequestAttrNumberGreater() {
      super("HttpRequestAttrNumberGreaterName", "HttpRequestAttrNumberGreaterDescription");
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      Object var4 = this.getAttribute(var3);
      return var4 != null && getAttributeNumber(var4) > this.getArgumentNumber();
   }
}