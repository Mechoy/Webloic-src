package weblogic.entitlement.rules;

import javax.security.auth.Subject;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public class ContextElementStringEquals extends ContextElementPredicate {
   private static final PredicateArgument[] arguments;
   private String stringValue = null;

   public ContextElementStringEquals() {
      super("ContextElementStringEqualsName", "ContextElementStringEqualsDescription");
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length == 2) {
         this.setElementName(var1[0]);
         this.stringValue = var1[1];
      } else {
         throw new IllegalPredicateArgumentException("Two arguments are expected");
      }
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      Object var4 = this.getElement(var3);
      return var4 != null && var4.toString().equals(this.stringValue);
   }

   public int getArgumentCount() {
      return arguments.length;
   }

   public PredicateArgument getArgument(int var1) {
      return arguments[var1];
   }

   static {
      arguments = new PredicateArgument[]{ELEMENT_NAME_ARGUMENT, new StringPredicateArgument()};
   }
}
