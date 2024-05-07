package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.sql.SQLException;
import javax.sql.RowSet;
import javax.sql.rowset.Predicate;
import weblogic.utils.expressions.Expression;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.expressions.ExpressionParser;
import weblogic.utils.expressions.ExpressionParserException;
import weblogic.utils.expressions.Variable;
import weblogic.utils.expressions.VariableBinder;
import weblogic.utils.expressions.Expression.Type;

public class SQLPredicate implements Predicate, Serializable {
   private Expression expression = null;

   public SQLPredicate(String var1) throws ExpressionParserException {
      this.expression = (new ExpressionParser()).parse(var1, SQLPredicate.SQLVariableBinder.THE_ONE);
   }

   public boolean evaluate(RowSet var1) {
      try {
         return this.expression.evaluate(var1);
      } catch (Throwable var3) {
         throw new RuntimeException(var3.getMessage());
      }
   }

   public boolean evaluate(Object var1, String var2) throws SQLException {
      throw new SQLException("This is not supported by SQLPredicate.");
   }

   public boolean evaluate(Object var1, int var2) throws SQLException {
      throw new SQLException("This is not supported by SQLPredicate.");
   }

   private static class SQLVariable implements Variable {
      private final String key;

      private SQLVariable(String var1) {
         this.key = var1;
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return ((RowSet)var1).getObject(this.key);
         } catch (SQLException var3) {
            throw new ExpressionEvaluationException("Could not find field: " + this.key, var3);
         }
      }

      public Expression.Type getType() {
         return Type.ANY;
      }

      // $FF: synthetic method
      SQLVariable(String var1, Object var2) {
         this(var1);
      }
   }

   private static class SQLVariableBinder implements VariableBinder {
      private static final SQLVariableBinder THE_ONE = new SQLVariableBinder();

      public Variable getVariable(String var1) {
         return new SQLVariable(var1);
      }
   }
}
