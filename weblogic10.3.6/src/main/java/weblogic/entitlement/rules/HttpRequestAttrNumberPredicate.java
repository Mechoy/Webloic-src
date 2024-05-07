package weblogic.entitlement.rules;

import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;

public abstract class HttpRequestAttrNumberPredicate extends HttpRequestAttrPredicate {
   private static final PredicateArgument[] arguments;
   private double numValue = 0.0;

   public HttpRequestAttrNumberPredicate(String var1, String var2) {
      super(var1, var2);
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length == 2) {
         this.setAttributeName(var1[0]);
         this.numValue = parseNumber(var1[1]);
      } else {
         throw new IllegalPredicateArgumentException("Two arguments are expected");
      }
   }

   private static double parseNumber(String var0) throws IllegalPredicateArgumentException {
      return ((Number)arguments[1].parseExprValue(var0)).doubleValue();
   }

   protected static double getAttributeNumber(Object var0) {
      if (var0 instanceof Number) {
         return ((Number)var0).doubleValue();
      } else {
         if (var0 instanceof String) {
            try {
               return parseNumber((String)var0);
            } catch (IllegalPredicateArgumentException var2) {
            }
         }

         throw new RuntimeException("Attribute value is not a number");
      }
   }

   protected double getArgumentNumber() {
      return this.numValue;
   }

   public int getArgumentCount() {
      return arguments.length;
   }

   public PredicateArgument getArgument(int var1) {
      return arguments[var1];
   }

   static {
      arguments = new PredicateArgument[]{ATTR_NAME_ARGUMENT, new NumberPredicateArgument()};
   }
}
