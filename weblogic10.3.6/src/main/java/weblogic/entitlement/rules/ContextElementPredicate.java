package weblogic.entitlement.rules;

import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;

public abstract class ContextElementPredicate extends BasePredicate {
   private static final String VERSION = "1.0";
   protected static final PredicateArgument ELEMENT_NAME_ARGUMENT = new StringPredicateArgument("ContextElementPredicateElementArgumentName", "ContextElementPredicateElementArgumentDescription", (String)null);
   private String elementName;

   public ContextElementPredicate(String var1, String var2) {
      super(var1, var2);
   }

   protected String getElementName() {
      return this.elementName;
   }

   protected void setElementName(String var1) throws IllegalPredicateArgumentException {
      if (var1 == null) {
         throw new IllegalPredicateArgumentException("Null context element name");
      } else {
         this.elementName = var1;
      }
   }

   protected Object getElement(ContextHandler var1) {
      return var1 != null ? var1.getValue(this.elementName) : null;
   }

   public String getVersion() {
      return "1.0";
   }
}
