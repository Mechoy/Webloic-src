package weblogic.entitlement.rules;

import javax.security.auth.Subject;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public class ContextElementIsSet extends ContextElementPredicate {
   private static final PredicateArgument[] arguments;

   public ContextElementIsSet() {
      super("ContextElementIsSetName", "ContextElementIsSetDescription");
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length == 1) {
         this.setElementName(var1[0]);
      } else {
         throw new IllegalPredicateArgumentException("One Element name arguments is expected");
      }
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      return this.getElement(var3) != null;
   }

   public int getArgumentCount() {
      return arguments.length;
   }

   public PredicateArgument getArgument(int var1) {
      return arguments[var1];
   }

   static {
      arguments = new PredicateArgument[]{ELEMENT_NAME_ARGUMENT};
   }
}
