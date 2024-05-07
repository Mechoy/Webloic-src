package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprISNULL extends BaseExpr implements Expr, ExpressionTypes {
   private int relationshipType;
   private int argType;
   private int databaseType;
   private String ejbqlId;
   private String dealiasedEjbqlId;
   private String lastPathExpressionElement;
   protected boolean isNull = true;
   private StringBuffer preCalcSQLBuf = null;

   protected ExprISNULL(int var1, Expr var2, boolean var3) {
      super(var1, var2);
      this.isNull = var3;
      this.debugClassName = "ExprISNULL ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      this.term1.init(this.globalContext, this.queryTree);
      this.argType = this.term1.type();
      this.databaseType = this.globalContext.getDatabaseType();
      if (this.argType != 17 && this.argType != 25) {
         String var1 = this.term1.getMainEJBQL();
         if (var1 == null) {
            var1 = "-- unknown --";
         }

         Loggable var2 = EJBLogger.logISNULLArgMustBePathExpressionOrVariableLoggable(var1);
         Exception var3 = new Exception(var2.getMessage());
         this.term1.markExcAndAddCollectionException(var3);
         this.addCollectionExceptionAndThrow(var3);
      }

      if (this.argType == 17) {
         this.ejbqlId = this.term1.getSval();
         this.dealiasedEjbqlId = this.globalContext.replaceIdAliases(this.ejbqlId);

         try {
            this.relationshipType = ((ExprID)this.term1).getRelationshipTypeForPathExpressionWithNoSQLGen();
         } catch (Exception var7) {
            Exception var9 = new Exception("Error encountered while compiling EJB QL IS [NOT] NULL, " + var7.toString());
            this.term1.markExcAndAddCollectionException(var9);
            this.addCollectionExceptionAndThrow(var9);
         }

         if (this.relationshipType == 4 || this.relationshipType == 6) {
            JoinNode var8 = null;

            try {
               JoinNode var10 = this.queryTree.getJoinNodeForLastId(this.dealiasedEjbqlId);
               var8 = var10.getPrevNode();
            } catch (Exception var6) {
            }

            String var11 = " - unknown - ";
            this.lastPathExpressionElement = ((ExprID)this.term1).getLastField();
            if (var8 != null) {
               RDBMSBean var12 = var8.getRDBMSBean();
               var11 = var12.getEjbName();
            }

            String var13 = RDBMSUtils.relationshipTypeToString(this.relationshipType);
            Loggable var4 = EJBLogger.logFinderNotNullOnWrongTypeLoggable(var11, this.lastPathExpressionElement, var13);
            Exception var5 = new Exception(var4.getMessage());
            this.term1.markExcAndAddCollectionException(var5);
            this.addCollectionExceptionAndThrow(var5);
         }

         this.lastPathExpressionElement = ((ExprID)this.term1).getLastField();
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.preCalcSQLBuf = new StringBuffer();
      if (this.argType == 25) {
         this.term1.calculate();
         String var3 = this.term1.toSQL();
         this.preCalcSQLBuf.append(" " + var3 + " IS " + (this.isNull ? "" : "NOT ") + "NULL ");
      } else {
         switch (this.relationshipType) {
            case 0:
               this.term1.calculate();
               ((ExprID)this.term1).prepareIdentifierForSQLGen();
               this.preCalcSQLBuf.append(this.term1.toSQL());
               if (this.isNull) {
                  this.preCalcSQLBuf.append("IS NULL ");
               } else {
                  this.preCalcSQLBuf.append("IS NOT NULL ");
               }

               return;
            case 2:
            case 5:
               this.compute11orN1_fk_on_lhs(this.preCalcSQLBuf, ((ExprID)this.term1).getDealiasedEjbqlID());
               return;
            case 3:
               this.compute11_fk_on_rhs(this.preCalcSQLBuf, ((ExprID)this.term1).getDealiasedEjbqlID());
               return;
            case 7:
               this.compute_remote_interface(this.preCalcSQLBuf, ((ExprID)this.term1).getDealiasedEjbqlID());
               return;
            case 8:
               Exception var1 = new Exception("Cannot compute IS [NOT] NULL on path expression '" + ((ExprID)this.term1).getDealiasedEjbqlID() + "' that terminates in a remote relationship that involves a join table.");
               this.term1.markExcAndAddCollectionException(var1);
               this.addCollectionExceptionAndThrow(var1);
            case 1:
            case 4:
            case 6:
            default:
               Exception var2 = new Exception("Internal Error, IS [NOT] NULL cannot handle relationship type number '" + this.relationshipType + "'  " + RDBMSUtils.relationshipTypeToString(this.relationshipType));
               this.term1.markExcAndAddCollectionException(var2);
               this.addCollectionExceptionAndThrow(var2);
         }
      }
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprISNULL var1;
      if (this.isNull) {
         var1 = new ExprISNULL(3, this.term1, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IS NOT NULL ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprISNULL(3, this.term1, true);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IS NULL ");
         var1.setPostEJBQLFrom(this);
         return var1;
      }
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendMainEJBQLTokenToList(var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      return this.preCalcSQLBuf.toString();
   }

   private void compute11orN1_fk_on_lhs(StringBuffer var1, String var2) throws ErrorCollectionException {
      ExprID var3 = null;
      List var4 = null;

      try {
         var3 = this.prepareTruncatedPathExpression(var2);
         var4 = this.globalContext.getTableAndFKColumnListForLocal11or1NPath(this.queryTree, this.dealiasedEjbqlId);
      } catch (Exception var10) {
         this.term1.markExcAndAddCollectionException(var10);
         this.addCollectionExceptionAndThrow(var10);
      }

      Iterator var5 = var4.iterator();
      String var6;
      if (var5.hasNext()) {
         while(var5.hasNext()) {
            var6 = (String)var5.next();
            var1.append(var6);
            if (this.isNull) {
               var1.append(" IS NULL ");
            } else {
               var1.append(" IS NOT NULL ");
            }

            if (var5.hasNext()) {
               var1.append("AND ");
            }
         }

      } else {
         var6 = " - unknown - ";

         try {
            JoinNode var7 = this.queryTree.getJoinNodeForLastId(var3.getDealiasedEjbqlID());
            var6 = var7.getRDBMSBean().getEjbName();
         } catch (Exception var9) {
         }

         Loggable var11 = EJBLogger.logfinderCouldNotGetFKColumnsLoggable(var6, this.lastPathExpressionElement, "IS NULL");
         Exception var8 = new Exception("(1 to 1) or (1 to N) Relationship.  " + var11.getMessage());
         this.term1.markExcAndAddCollectionException(var8);
         this.addCollectionExceptionAndThrow(var8);
      }
   }

   private void compute11_fk_on_rhs(StringBuffer var1, String var2) throws ErrorCollectionException {
      ExprID var3 = null;

      try {
         var3 = this.prepareTruncatedPathExpression(var2);
      } catch (Exception var22) {
         this.term1.markExcAndAddCollectionException(var22);
         this.addCollectionExceptionAndThrow(var22);
      }

      String var4 = var3.getDealiasedEjbqlID();
      RDBMSBean var5 = var3.getRDBMSBeanWithSQLGen();
      JoinNode var6 = null;

      try {
         var6 = this.queryTree.getJoinNodeForLastId(var4);
      } catch (Exception var21) {
         Exception var8 = new Exception("Error encountered while compiling EJB QL IS [NOT] NULL, " + var21.toString());
         this.term1.markExcAndAddCollectionException(var8);
         this.addCollectionExceptionAndThrow(var8);
      }

      String var7 = var6.getTableName();
      String var23 = var6.getRDBMSBean().getEjbName();
      String var9 = var6.getTableAlias();
      RDBMSBean var10 = null;

      try {
         var10 = this.queryTree.getLastRDBMSBeanForPathExpressionWithNoSQLGen(var2);
      } catch (Exception var20) {
         Exception var12 = new Exception("Error encountered while compiling EJB QL IS [NOT] NULL, " + var20.toString());
         this.term1.markExcAndAddCollectionException(var12);
         this.addCollectionExceptionAndThrow(var12);
      }

      String var11 = var5.getRelatedFieldName(this.lastPathExpressionElement);
      String var24 = var10.getTableForCmrField(var11);
      if (var24 == null) {
         Exception var13 = new Exception("Internal Error,  " + this.debugClassName + ": " + " for path: '" + var2 + "', for the cmr-field: '" + var11 + "', we could not get the name of the Table on Bean: '" + var10.getEjbName() + "' that holds the Foreign Key Columns for the Relationship.  " + "RelatedFieldName used for lookup was: '" + var11 + "'");
         this.term1.markExcAndAddCollectionException(var13);
         this.addCollectionExceptionAndThrow(var13);
      }

      String var25 = this.globalContext.registerTable(var24);
      this.queryTree.addTableAliasExclusionList(var25);
      Map var14 = var10.getColumnMapForCmrfAndPkTable(var11, var7);
      Iterator var15 = var14.keySet().iterator();
      if (!var15.hasNext()) {
         Loggable var16 = EJBLogger.logfinderFKColumnsMissingLoggable("IS [NOT] NULL", var2, var24, var7);
         Exception var17 = new Exception(var16.getMessage());
         this.term1.markExcAndAddCollectionException(var17);
         this.addCollectionExceptionAndThrow(var17);
      }

      List var26 = var5.getPrimaryKeyFields();
      String var18;
      String var27;
      if (var26.size() == 1) {
         var27 = this.globalContext.registerTable(var7);
         this.queryTree.addTableAliasExclusionList(var27);
         var18 = (String)var15.next();
         String var19 = (String)var14.get(var18);
         var1.append(var9).append(".");
         var1.append(RDBMSUtils.escQuotedID(var19));
         var1.append(" ");
         if (this.isNull) {
            var1.append("NOT ");
         }

         var1.append("IN ( SELECT ");
         var1.append(var27).append(".").append(RDBMSUtils.escQuotedID(var19));
         var1.append(" FROM ");
         var1.append(RDBMSUtils.escQuotedID(var7)).append(" ").append(var27);
         var1.append(", ");
         var1.append(RDBMSUtils.escQuotedID(var24)).append(" ").append(var25);
         var1.append(" ");
         var1.append("WHERE ");
         var1.append(var27).append(".").append(RDBMSUtils.escQuotedID(var19));
         var1.append(" = ");
         var1.append(var25).append(".").append(RDBMSUtils.escQuotedID(var18));
         var1.append(") ");
      } else {
         var1.append(" ( 0 ");
         if (this.isNull) {
            var1.append(" = ");
         } else {
            var1.append(" < ");
         }

         var1.append("( ");
         var1.append("SELECT COUNT(*) ");
         var1.append("FROM ").append(var24).append(" ").append(var25).append(" ");
         var1.append("WHERE ");

         while(var15.hasNext()) {
            var27 = (String)var15.next();
            var18 = (String)var14.get(var27);
            var1.append(var9).append(".").append(var18);
            var1.append(" = ");
            var1.append(var25).append(".").append(var27);
            if (var15.hasNext()) {
               var1.append(" AND ");
            }
         }

         var1.append(" ) ) ");
      }
   }

   private void compute_remote_interface(StringBuffer var1, String var2) throws ErrorCollectionException {
      this.term1.calculate();
      ((ExprID)this.term1).prepareIdentifierForSQLGen();
      List var3 = null;

      try {
         var3 = this.globalContext.getTableAndFKColumnListForLocal11or1NPath(this.queryTree, ((ExprID)this.term1).getDealiasedEjbqlID());
      } catch (Exception var6) {
         this.term1.markExcAndAddCollectionException(var6);
         this.addCollectionExceptionAndThrow(var6);
      }

      if (var3.size() != 1) {
         Loggable var4 = EJBLogger.logFinderExpectedSingleFKLoggable("Remote Relationship", ((ExprID)this.term1).getDealiasedEjbqlID(), Integer.toString(var3.size()));
         Exception var5 = new Exception(var4.getMessage());
         this.term1.markExcAndAddCollectionException(var5);
         this.addCollectionExceptionAndThrow(var5);
      }

      Iterator var8 = var3.iterator();
      String var7 = (String)var8.next();
      var1.append(var7);
      if (this.isNull) {
         var1.append(" IS NULL ");
      } else {
         var1.append(" IS NOT NULL ");
      }

   }

   private ExprID prepareTruncatedPathExpression(String var1) throws ErrorCollectionException {
      int var2 = var1.lastIndexOf(".");
      if (var2 == -1) {
         Loggable var3 = EJBLogger.logFinderNotNullOnBadPathLoggable(var1);
         this.markExcAndThrowCollectionException(new Exception(var3.getMessage()));
      }

      String var5 = var1.substring(0, var2);
      ExprID var4 = ExprID.newInitExprID(this.globalContext, this.queryTree, var5);
      var4.prepareIdentifierForSQLGen();
      return var4;
   }
}
