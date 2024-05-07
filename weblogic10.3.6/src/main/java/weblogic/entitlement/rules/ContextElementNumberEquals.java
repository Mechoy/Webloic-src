package weblogic.entitlement.rules;

import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public class ContextElementNumberEquals extends ContextElementNumberPredicate {
   public ContextElementNumberEquals() {
      super("ContextElementNumberEqualsName", "ContextElementNumberEqualsDescription");
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      Object var4 = this.getElement(var3);
      return var4 != null && getElementNumber(var4) == this.getArgumentNumber();
   }
}
