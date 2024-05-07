package weblogic.ejb.container.cmp11.rdbms.finders;

import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.utils.PlatformConstants;

public final class IllegalExpressionException extends Exception implements PlatformConstants {
   private static final long serialVersionUID = -3707529156302719304L;
   public static final int INVALID_IDENTIFIER = 1;
   public static final int INVALID_OPERATION = 2;
   public static final int INVALID_EXPRESSION_TYPE = 3;
   public static final int COULD_NOT_PARSE = 4;
   private int errorCode;
   private String identifier;
   private Finder finder;
   private Finder.FinderExpression expression;
   private String message;

   public IllegalExpressionException(int var1, String var2, Finder.FinderExpression var3) {
      this.errorCode = 0;
      this.identifier = null;
      this.finder = null;
      this.expression = null;
      this.message = null;
      this.errorCode = var1;
      this.identifier = var2;
      this.expression = var3;
   }

   public IllegalExpressionException(int var1, String var2) {
      this(var1, var2, (Finder.FinderExpression)null);
   }

   public IllegalExpressionException(int var1, String var2, String var3) {
      this(var1, var2);
      this.message = var3;
   }

   public void setFinder(Finder var1) {
      this.finder = var1;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer();
      EJBTextTextFormatter var2 = new EJBTextTextFormatter();
      var1.append(this.getClass().getName() + ":" + EOL);
      var1.append(var2.whileTryingToProcess(this.finder == null ? null : this.finder.toUserLevelString(true)) + EOL);
      String var3 = "WLQL";
      switch (this.errorCode) {
         case 1:
            var1.append(var2.invalidIdInExpression(var3, this.identifier));
            break;
         case 2:
            var1.append(var2.invalidOp(var3, this.identifier));
            break;
         case 3:
            var1.append("Finder Expression: " + this.expression + " is invalid." + EOL);
            var1.append("It has type " + this.identifier + ", which is not a valid Finder Expression type.");
            break;
         case 4:
            var1.append(var2.couldNotParse(var3, this.identifier));
      }

      var1.append("  ").append(this.message);
      var1.append(EOL);
      return var1.toString();
   }
}
