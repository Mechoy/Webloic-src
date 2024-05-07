package weblogic.ejb.container.cmp11.rdbms.finders;

import java.util.Hashtable;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.logging.Loggable;

public final class WLQLtoEJBQLExpander extends SQLQueryExpander {
   EJBTextTextFormatter fmt = null;

   public WLQLtoEJBQLExpander(WLQLExpression var1) {
      super(var1, (Hashtable)null);
   }

   public String toEJBQL() throws IllegalExpressionException {
      return this.toSQL(super.queryExpression);
   }

   public String toSQL(WLQLExpression var1) throws IllegalExpressionException {
      switch (var1.type()) {
         case 3:
            if (var1.term(0).equals(var1.term(1))) {
               return null;
            }
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 10:
         case 11:
         case 14:
         case 15:
         default:
            return super.toSQL(var1);
         case 9:
            return this.perhapsAppendAbstractSchemaAlias(var1.getSval());
         case 12:
            Loggable var2 = EJBLogger.logejbqlNoTokenSpecialLoggable(var1.getSpecialName());
            throw new IllegalExpressionException(2, WLQLExpressionTypes.TYPE_NAMES[var1.getType()], var2.getMessage());
         case 13:
            return this.getVariable(var1);
         case 16:
            Loggable var3 = EJBLogger.logejbqlOrderByIsDifferentLoggable();
            throw new IllegalExpressionException(2, WLQLExpressionTypes.TYPE_NAMES[var1.getType()], var3.getMessage());
      }
   }

   private String getVariable(WLQLExpression var1) {
      Integer var2 = new Integer(var1.getSval());
      int var3 = var2;
      ++var3;
      return "?" + var3;
   }

   private String perhapsAppendAbstractSchemaAlias(String var1) {
      String var2 = var1;
      if (var1 != null && !var1.startsWith("o.")) {
         var2 = "o." + var1;
      }

      return var2;
   }
}
