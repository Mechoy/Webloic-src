package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprRANGE_VARIABLE extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprRANGE_VARIABLE(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprRANGE_VARIABLE ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);
      ExprID var1 = (ExprID)this.term1;
      ExprID var2 = (ExprID)this.term2;

      try {
         var1.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var16) {
         this.addCollectionException(var16);
      }

      try {
         var2.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var15) {
         this.addCollectionException(var15);
      }

      this.throwCollectionException();
      String var3 = var1.getEjbqlID();
      String var4 = var2.getEjbqlID();
      if (var4.indexOf(".") != -1) {
         Loggable var5 = EJBLogger.logFinderRVDCannotBePathExpressionLoggable(var3);
         IllegalExpressionException var6 = new IllegalExpressionException(7, var5.getMessage());
         var2.markExcAndAddCollectionException(var6);
         this.addCollectionExceptionAndThrow(var6);
      }

      RDBMSBean var17 = this.globalContext.getRDBMSBean();
      String var18 = var17.getAbstractSchemaName();
      Loggable var7;
      IllegalExpressionException var8;
      if (var18 == null) {
         var7 = EJBLogger.logFinderCouldNotGetAbstractSchemaNameForRVDLoggable(var3);
         var8 = new IllegalExpressionException(7, var7.getMessage());
         var1.markExcAndAddCollectionException(var8);
         this.addCollectionExceptionAndThrow(var8);
      }

      var7 = null;
      var8 = null;
      String var9 = null;
      RDBMSBean var19;
      String var20;
      if (var3.equals(var18)) {
         var19 = var17;
         var20 = var17.getTableName();
         var9 = this.globalContext.registerTable(var20);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("+++ processing other Bean for abstract schema: " + var3);
         }

         var19 = var17.getRDBMSBeanForAbstractSchema(var3);
         if (null == var19) {
            Loggable var10 = EJBLogger.logFinderCouldNotGetRDBMSBeanForAbstractSchemaNameLoggable(var3);
            IllegalExpressionException var11 = new IllegalExpressionException(7, var10.getMessage());
            var1.markExcAndAddCollectionException(var11);
            this.addCollectionExceptionAndThrow(var11);
         }

         var20 = var19.getTableName();
         var9 = this.globalContext.registerTable(var20);
      }

      try {
         this.globalContext.addGlobalRangeVariable(var4, var3);
         this.queryTree.addRangeVariable(var4, var3);
      } catch (IllegalExpressionException var14) {
         this.markExcAndThrowCollectionException(var14);
      }

      JoinNode var21 = this.queryTree.getJoinTree();
      JoinNode var22 = new JoinNode(var21, var4, var19, var20, var9, -1, false, false, "", this.globalContext, new ArrayList());
      var21.putChild(var4, var22);

      try {
         this.globalContext.addIdAlias(var4, var4);
      } catch (IllegalExpressionException var13) {
         var2.markExcAndAddCollectionException(var13);
         this.addCollectionExceptionAndThrow(var13);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendMainEJBQLTokenToList(var1);
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return "";
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprRANGE_VARIABLE] " + var0);
   }
}
