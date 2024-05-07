package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprEXISTS extends BaseExpr implements Expr, ExpressionTypes {
   private int numTerms;
   private boolean notExists;

   protected ExprEXISTS(int var1, boolean var2) {
      super(var1);
      this.numTerms = 0;
      this.notExists = false;
      this.notExists = var2;
      this.debugClassName = "ExprEXISTS ";
   }

   protected ExprEXISTS(int var1) {
      this(var1, false);
   }

   public void init_method() throws ErrorCollectionException {
   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprEXISTS var1;
      if (this.notExists) {
         var1 = new ExprEXISTS(65, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("EXISTS ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprEXISTS(65, true);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("NOT EXISTS ");
         var1.setPostEJBQLFrom(this);
         return var1;
      }
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      this.clearSQLBuf();
      if (this.notExists) {
         this.appendSQLBuf("NOT ");
      }

      this.appendSQLBuf("EXISTS ");
      return this.getSQLBuf().toString();
   }
}
