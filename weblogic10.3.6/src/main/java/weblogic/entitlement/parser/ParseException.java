package weblogic.entitlement.parser;

public class ParseException extends Exception {
   private int exprIndex = -1;

   public ParseException() {
   }

   public ParseException(String var1) {
      super(var1);
   }

   public ParseException(String var1, int var2) {
      super(var1);
      this.exprIndex = var2;
   }

   public int getExprIndex() {
      return this.exprIndex;
   }
}
