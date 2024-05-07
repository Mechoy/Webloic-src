package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprEQUAL extends BaseExpr implements Expr, ExpressionTypes {
   private static final int TYPE_EQ_CMP_FIELD = 0;
   private static final int TYPE_EQ_SUBQUERY_BEAN = 1;
   private static final int TYPE_EQ_ON_BEAN = 2;
   private static final int TYPE_EQ_ON_BOOLEAN_LITERAL = 3;
   private static final int OP_EQ_ON_BEAN = 0;
   private static final int OP_SUBQUERY_IN_ON_BEAN = 1;
   private static final int OP_EQ_SUBQUERY_ANY_ON_BEAN = 2;
   private static final int OP_EQ_SUBQUERY_ALL_ON_BEAN = 3;
   private static final int OP_EQ_SUBQUERY_ON_BEAN = 4;
   private static final int OP_NOT_EQ_ON_BEAN = 5;
   private static final int OP_SUBQUERY_NOT_IN_ON_BEAN = 6;
   private static final int OP_NOT_EQ_SUBQUERY_ANY_ON_BEAN = 7;
   private static final int OP_NOT_EQ_SUBQUERY_ALL_ON_BEAN = 8;
   private static final int OP_NOT_EQ_SUBQUERY_ON_BEAN = 9;
   private int eqType;
   protected boolean isEqual = true;
   private StringBuffer preCalcSQLBuf = null;

   protected ExprEQUAL(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.isEqual = true;
      this.debugClassName = "ExprEQUAL ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (Exception var4) {
         this.addCollectionException(var4);
      }

      try {
         this.term2.init(this.globalContext, this.queryTree);
      } catch (Exception var3) {
         this.addCollectionException(var3);
      }

      try {
         this.eqType = this.getEqType();
      } catch (Exception var2) {
         this.addCollectionException(var2);
         this.throwCollectionException();
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.preCalcSQLBuf = new StringBuffer();
      switch (this.eqType) {
         case 0:
            doSimpleEQ(this.globalContext, this.queryTree, this, this.preCalcSQLBuf, this.isEqual);
            return;
         case 1:
            doCalcEQonSubQuerySelectBean(this.globalContext, this.queryTree, this, this.preCalcSQLBuf, this.isEqual, false);
            return;
         case 2:
            doCalcEQonBean(this.globalContext, this.queryTree, this, this.preCalcSQLBuf, this.isEqual);
            return;
         case 3:
            doCalcEQonBooleanLiteral(this.globalContext, this.queryTree, this, this.preCalcSQLBuf, this.isEqual);
            return;
         default:
            this.markExcAndThrowCollectionException(new Exception("Internal Error. " + this.debugClassName + " unknown EQ Type: '" + this.eqType + "'"));
      }
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprNOT_EQUAL var1 = new ExprNOT_EQUAL(10, this.term1, this.term2);
      var1.setPreEJBQLFrom(this);
      var1.setMainEJBQL("<> ");
      var1.setPostEJBQLFrom(this);
      return var1;
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

   public String toSQL() throws ErrorCollectionException {
      return this.preCalcSQLBuf.toString();
   }

   private int getEqType() throws ErrorCollectionException {
      if (isCalcEQonSubQuerySelectBean(this)) {
         return 1;
      } else if (isCalcEQonBean(this)) {
         return 2;
      } else {
         return isCalcEQonBooleanLiteral(this) ? 3 : 0;
      }
   }

   public static boolean isCalcEQonSubQuerySelectBean(Expr var0) throws ErrorCollectionException {
      if (var0.type() != 5 && var0.type() != 10 && var0.type() != 13) {
         return false;
      } else {
         boolean var1 = false;
         if (var0.getTerm2() == null) {
            return false;
         } else {
            Expr var2;
            Expr var3;
            if (var0.getTerm1().type() == 43) {
               var2 = var0.getTerm1();
               var3 = var0.getTerm2();
            } else {
               if (var0.getTerm2().type() != 43) {
                  return false;
               }

               var2 = var0.getTerm2();
               var3 = var0.getTerm1();
            }

            Expr var4 = BaseExpr.getExpressionFromTerms(var2, 34);
            if (var4 == null) {
               return false;
            } else {
               var4 = BaseExpr.getAggregateExpressionFromSubQuerySelect(var2);
               if (var4 != null) {
                  return false;
               } else {
                  if (var3 instanceof ExprID) {
                     if (((ExprID)var3).isPathExpressionEndingInCmpFieldWithNoSQLGen()) {
                        return false;
                     }
                  } else if (var3.type() == 25) {
                     Loggable var5 = EJBLogger.logejbqlSubQueryBeansCannotTestVariablesLoggable();
                     var3.markExcAndThrowCollectionException(new ErrorCollectionException(var5.getMessage()));
                  }

                  return true;
               }
            }
         }
      }
   }

   public static boolean isCalcEQonBean(Expr var0) throws ErrorCollectionException {
      if (var0.type() != 5 && var0.type() != 10) {
         return false;
      } else {
         ExprID var1;
         Expr var2;
         if (var0.getTerm1().type() == 17) {
            var1 = (ExprID)var0.getTerm1();
            var2 = var0.getTerm2();
         } else {
            if (var0.getTerm2().type() != 17) {
               return false;
            }

            var1 = (ExprID)var0.getTerm2();
            var2 = var0.getTerm1();
         }

         return !var1.isPathExpressionEndingInCmpFieldWithNoSQLGen();
      }
   }

   public static boolean isCalcEQonBooleanLiteral(Expr var0) {
      if (var0.type() != 5 && var0.type() != 10) {
         return false;
      } else {
         Expr var1;
         Expr var2;
         if (var0.getTerm1().type() == 17) {
            var1 = var0.getTerm1();
            var2 = var0.getTerm2();
         } else {
            if (var0.getTerm2().type() != 17) {
               return false;
            }

            var1 = var0.getTerm2();
            var2 = var0.getTerm1();
         }

         return var2.type() == 14 || var2.type() == 15;
      }
   }

   private static void doSimpleEQ(QueryContext var0, QueryNode var1, Expr var2, StringBuffer var3, boolean var4) throws ErrorCollectionException {
      try {
         if (var2.getTerm1() instanceof ExprID) {
            ((ExprID)var2.getTerm1()).calcTableAndColumnForCmpField();
         } else {
            var2.getTerm1().calculate();
         }
      } catch (ErrorCollectionException var8) {
         var2.addCollectionException(var8);
      }

      try {
         if (var2.getTerm2() instanceof ExprID) {
            ((ExprID)var2.getTerm2()).calcTableAndColumnForCmpField();
         } else {
            var2.getTerm2().calculate();
         }
      } catch (ErrorCollectionException var7) {
         var2.addCollectionException(var7);
      }

      var2.throwCollectionException();
      var3.append(var2.getTerm1().toSQL());

      try {
         var3.append(BaseExpr.getComparisonOpStringFromType(var2.type()));
      } catch (ErrorCollectionException var6) {
         var2.markExcAndThrowCollectionException(var6);
      }

      var3.append(var2.getTerm2().toSQL());
      var2.throwCollectionException();
   }

   private static void doCalcEQonBean(QueryContext var0, QueryNode var1, Expr var2, StringBuffer var3, boolean var4) throws ErrorCollectionException {
      ExprID var5 = null;
      Expr var6 = null;
      if (var2.getTerm1().type() == 17) {
         var5 = (ExprID)var2.getTerm1();
         var6 = var2.getTerm2();
      } else if (var2.getTerm2().type() == 17) {
         var5 = (ExprID)var2.getTerm2();
         var6 = var2.getTerm1();
      }

      String var7 = var5.getDealiasedEjbqlID();
      if (var6.type() != 25 && var6.type() != 17) {
         Loggable var8 = EJBLogger.logejbqlCanOnlyTestBeanVsSameBeanTypeLoggable(var7);
         IllegalExpressionException var9 = new IllegalExpressionException(7, var8.getMessage());
         var2.markExcAndThrowCollectionException(new ErrorCollectionException(var9));
      }

      if (var6.type() == 17) {
         try {
            var5.calculate();
         } catch (ErrorCollectionException var20) {
            var2.addCollectionException(var20);
         }

         try {
            var6.calculate();
         } catch (ErrorCollectionException var19) {
            var2.addCollectionException(var19);
         }

         var2.throwCollectionException();

         try {
            if (var4) {
               doCalcEQonBean_ID_ID(var0, var1, var5, var6, var3, 0);
            } else {
               doCalcEQonBean_ID_ID(var0, var1, var5, var6, var3, 5);
            }
         } catch (ErrorCollectionException var18) {
            var2.addCollectionExceptionAndThrow(var18);
         }

      } else {
         Expr var24 = var6;
         int var25 = Integer.parseInt(var6.getSval());
         Class var10 = var0.getFinderParameterTypeAt(var25 - 1);
         if (var5.isPathExpressionEndingInRemoteInterfaceWithSQLGen()) {
            try {
               var5.calculate();
            } catch (Exception var21) {
               var2.addCollectionException(var21);
            }

            var2.throwCollectionException();
            String var11 = var10.getName();
            if (!var11.equals("javax.ejb.EJBObject")) {
               IllegalExpressionException var12 = new IllegalExpressionException(7, "<cmr-field> " + var7 + ", the input parameter to be " + "tested against this field must be of type " + "javax.ejb.EJBObject.  Instead, it is: " + var10.getName());
               var5.markExcAndAddCollectionException(var12);
               var2.addCollectionExceptionAndThrow(var12);
            }

            RDBMSBean var26 = var5.getRDBMSBeanWithSQLGen();
            String var13 = var5.getLastField();
            EjbEntityRef var14 = var5.getEntityRefWithSQLGen(var13);
            String var15 = var14.getHome();
            String var16 = "param" + (var25 - 1);
            ParamNode var17 = new ParamNode(var26, var16, var25, var10, var7, var15, true, false, (Class)null, false, var0.isOracleNLSDataType(var13));
            var0.addFinderInternalQueryParmList(var17);
            var0.addFinderRemoteBeanParamList(var17);
            if (var4) {
               var0.setFinderRemoteBeanCommandEQ(true);
            } else {
               var0.setFinderRemoteBeanCommandEQ(false);
            }

         } else {
            try {
               var5.calculate();
            } catch (ErrorCollectionException var23) {
               var2.addCollectionException(var23);
            }

            var2.throwCollectionException();

            try {
               if (var4) {
                  doCalcEQonBean_ID_VARIABLE(var0, var1, var5, var24, var3, 0);
               } else {
                  doCalcEQonBean_ID_VARIABLE(var0, var1, var5, var24, var3, 5);
               }
            } catch (ErrorCollectionException var22) {
               var2.addCollectionExceptionAndThrow(var22);
            }

         }
      }
   }

   public static void doCalcEQonSubQuerySelectBean(QueryContext var0, QueryNode var1, Expr var2, StringBuffer var3, boolean var4) throws ErrorCollectionException {
      doCalcEQonSubQuerySelectBean(var0, var1, var2, var3, var4, false);
   }

   public static void doCalcEQonSubQuerySelectBean(QueryContext var0, QueryNode var1, Expr var2, StringBuffer var3, boolean var4, boolean var5) throws ErrorCollectionException {
      byte var6 = 0;
      Expr var7 = var2.getTerm1();
      Expr var8 = var2.getTerm2();
      if (var2.getTerm2().type() == 43) {
         var7 = var2.getTerm2();
         var8 = var2.getTerm1();
      }

      Expr var9 = var7.getTerm1();
      if (var9.type() != 19) {
         IllegalExpressionException var10 = new IllegalExpressionException(7, "Error in doCalcEQonSubQuerySelectBean(), the first term in SUBQUERY token is not INTEGER as expected !");
         var9.markExcAndAddCollectionException(var10);
         var2.addCollectionExceptionAndThrow(var10);
      }

      int var21 = (int)var9.getIval();
      Expr var11 = var7.getTerm2();
      if (var5) {
         if (var4) {
            var6 = 1;
         } else {
            var6 = 6;
         }
      } else if (var11.type() == 64) {
         if (var4) {
            var6 = 2;
         } else {
            var6 = 7;
         }
      } else if (var11.type() == 49) {
         if (var4) {
            var6 = 3;
         } else {
            var6 = 8;
         }
      } else if (var11.type() == 34) {
         if (var4) {
            var6 = 4;
         } else {
            var6 = 9;
         }
      } else {
         IllegalExpressionException var12 = new IllegalExpressionException(7, "Error in doCalcEQonSubQuerySelectBean(), unable to handle Expression type: '" + var11.type() + "'");
         var7.markExcAndAddCollectionException(var12);
         var2.addCollectionExceptionAndThrow(var12);
      }

      if (var8 instanceof ExprID) {
         doCalcEQonBean_ID_ID(var0, var1, var8, (Expr)null, var3, var6);
         switch (var6) {
            case 2:
            case 7:
               var3.append("ANY ");
               break;
            case 3:
            case 8:
               var3.append("ALL ");
            case 4:
            case 5:
            case 6:
         }

         var7.calculate();
         var3.append(var7.toSQL());
         String var23 = ((ExprID)var8).getDealiasedEjbqlID();
         RDBMSBean var24 = ((ExprID)var8).getRDBMSBeanWithSQLGen();
         QueryNode var14 = var1.getQueryNodeForQueryId(var21);
         if (var14 == null) {
            IllegalExpressionException var15 = new IllegalExpressionException(7, "Unable to locate SubQuery Node for SubQuery number '" + var21 + "'");
            var7.markExcAndAddCollectionException(var15);
            var2.addCollectionExceptionAndThrow(var15);
         }

         List var25 = var14.getSelectList();
         if (var25.size() > 1) {
            Loggable var16 = EJBLogger.logejbqlSubQuerySelectCanOnlyHaveOneItemLoggable();
            IllegalExpressionException var17 = new IllegalExpressionException(7, var16.getMessage());
            var7.markExcAndAddCollectionException(var17);
            var2.addCollectionExceptionAndThrow(var17);
         }

         SelectNode var26 = (SelectNode)var25.get(0);
         RDBMSBean var27 = var26.getSelectBean();
         Loggable var18;
         IllegalExpressionException var19;
         if (var27 == null) {
            var18 = EJBLogger.logejbqlCanOnlyTestBeanVsSameBeanTypeLoggable(var23);
            var19 = new IllegalExpressionException(7, var18.getMessage());
            var7.markExcAndAddCollectionException(var19);
            var2.addCollectionExceptionAndThrow(var19);
         }

         if (!var27.equals(var24)) {
            var18 = EJBLogger.logejbqlCanOnlyTestBeanVsSameBeanTypeLoggable(var23);
            var19 = new IllegalExpressionException(7, var18.getMessage());
            var7.markExcAndAddCollectionException(var19);
            var2.addCollectionExceptionAndThrow(var19);
         }

         CMPBeanDescriptor var28 = var24.getCMPBeanDescriptor();
         if (var28.hasComplexPrimaryKey()) {
            Loggable var29 = EJBLogger.logejbqlSubQueryBeansCanOnlyHaveSimplePKsLoggable(var23);
            IllegalExpressionException var20 = new IllegalExpressionException(7, var29.getMessage());
            var7.markExcAndAddCollectionException(var20);
            var2.addCollectionExceptionAndThrow(var20);
         }

      } else {
         if (var8.type() == 25) {
            Loggable var22 = EJBLogger.logejbqlSubQueryBeansCannotTestVariablesLoggable();
            IllegalExpressionException var13 = new IllegalExpressionException(7, var22.getMessage());
            var8.markExcAndAddCollectionException(var13);
            var2.addCollectionExceptionAndThrow(var13);
         }

      }
   }

   private static void doCalcEQonBean_ID_ID(QueryContext var0, QueryNode var1, Expr var2, Expr var3, StringBuffer var4, int var5) throws ErrorCollectionException {
      boolean var6 = false;
      switch (var5) {
         case 0:
         case 5:
            var6 = true;
            break;
         default:
            var6 = false;
      }

      String var7 = ((ExprID)var2).getDealiasedEjbqlID();
      RDBMSBean var8 = ((ExprID)var2).getRDBMSBeanWithSQLGen();
      String var9 = null;
      if (var6) {
         var9 = ((ExprID)var3).getDealiasedEjbqlID();
         RDBMSBean var10 = ((ExprID)var3).getRDBMSBeanWithSQLGen();
         if (!var8.equals(var10)) {
            Loggable var11 = EJBLogger.logejbqlCanOnlyTestBeanVsSameBeanTypeLoggable(var7);
            IllegalExpressionException var12 = new IllegalExpressionException(7, var11.getMessage());
            var3.markExcAndAddCollectionException(var12);
         }
      }

      CMPBeanDescriptor var24 = var8.getCMPBeanDescriptor();
      boolean var25 = var24.hasComplexPrimaryKey();
      List var26 = var8.getPrimaryKeyFields();
      int var13 = var26.size();
      String var14 = null;
      String var15 = null;
      if (var6) {
         var4.append("(");
      }

      for(int var16 = 0; var16 < var13; ++var16) {
         String var17 = (String)var26.get(var16);
         String var18 = var7 + (var7.length() > 0 ? "." : "") + var17;
         String var19 = null;

         Loggable var21;
         try {
            var19 = ExprID.calcTableAndColumn(var0, var1, var18);
         } catch (Exception var23) {
            var21 = EJBLogger.logfinderCouldNotGetTableAndFieldLoggable(var7);
            var2.markExcAndThrowCollectionException(new IllegalExpressionException(7, var21.getMessage()));
         }

         switch (var5) {
            case 0:
            case 5:
               var14 = var9 + (var9.length() > 0 ? "." : "") + var17;

               try {
                  var15 = ExprID.calcTableAndColumn(var0, var1, var14);
               } catch (Exception var22) {
                  var21 = EJBLogger.logfinderCouldNotGetTableAndFieldLoggable(var9);
                  var3.markExcAndThrowCollectionException(new IllegalExpressionException(7, var21.getMessage()));
               }
         }

         switch (var5) {
            case 0:
               var4.append(var19).append(" = ").append(var15).append(" AND ");
               break;
            case 1:
               var4.append(var19).append(" IN ");
               break;
            case 2:
            case 3:
            case 4:
               var4.append(var19).append(" = ");
               break;
            case 5:
               var4.append(var19).append(" != ").append(var15).append("  OR ");
               break;
            case 6:
               var4.append(var19).append(" NOT IN ");
               break;
            case 7:
            case 8:
            case 9:
               var4.append(var19).append(" <> ");
               break;
            default:
               var2.markExcAndThrowCollectionException(new IllegalExpressionException(7, "unknown operation encountered for Equality on Bean: '" + var5 + "'"));
         }
      }

      if (var6) {
         if (var4.length() > 5) {
            var4.setLength(var4.length() - 5);
         }

         var4.append(")");
      }

      if (debugLogger.isDebugEnabled()) {
         debug(" Bean equal SQL is: " + var4.toString() + "\n\n\n");
      }

   }

   private static void doCalcEQonBean_ID_VARIABLE(QueryContext var0, QueryNode var1, Expr var2, Expr var3, StringBuffer var4, int var5) throws ErrorCollectionException {
      int var6 = Integer.parseInt(var3.getSval());
      Class var7 = var0.getFinderParameterTypeAt(var6 - 1);
      String var8 = var7.getName();
      String var9 = ((ExprID)var2).getDealiasedEjbqlID();
      RDBMSBean var10 = ((ExprID)var2).getRDBMSBeanWithSQLGen();
      CMPBeanDescriptor var11 = var10.getCMPBeanDescriptor();
      boolean var12 = false;
      String var13 = "";
      if (var11.hasLocalClientView()) {
         var13 = var11.getLocalInterfaceName();
         if (var13.equals(var8)) {
            var12 = true;
         }
      }

      if (!var12 && var11.hasRemoteClientView()) {
         var13 = var11.getRemoteInterfaceName();
         if (var13.equals(var8)) {
            var12 = true;
         }
      }

      if (!var12) {
         Loggable var14 = EJBLogger.logejbqlWrongBeanTestedAgainstVariableLoggable(var9, var13, Integer.toString(var6), var8);
         var2.markExcAndThrowCollectionException(new IllegalExpressionException(7, var14.getMessage()));
      }

      boolean var30 = var11.hasComplexPrimaryKey();
      Class var15 = null;
      if (var30) {
         var15 = var11.getPrimaryKeyClass();
      }

      String var16 = "param" + (var6 - 1);
      ParamNode var17 = new ParamNode(var10, var16, var6, var7, var9, "", true, false, var15, var30, false);
      if (debugLogger.isDebugEnabled()) {
         debug(" processing Bean Parameter Node: " + var17);
      }

      String var18 = var9;
      List var19 = var10.getPrimaryKeyFields();
      int var20 = var19.size();
      var4.append("(");

      for(int var21 = 0; var21 < var20; ++var21) {
         String var22 = (String)var19.get(var21);
         var15 = var11.getFieldClass(var22);
         if (var15 == null) {
            if (debugLogger.isDebugEnabled()) {
               debug("  PK CLASS: " + var22 + " is NULL !!!!");
            }

            Loggable var23 = EJBLogger.logfinderNoPKClassForFieldLoggable(var22);
            var2.markExcAndThrowCollectionException(new IllegalExpressionException(7, "Bean: " + var10.getEjbName() + "  " + var23.getMessage()));
         }

         boolean var31 = var0.isOracleNLSDataType(var22);
         if (var21 == 0 && !var30) {
            var17.setPrimaryKeyClass(var15);
            var17.setOracleNLSDataType(var31);
         }

         String var24 = var18 + (var18.length() > 0 ? "." : "") + var22;
         ParamNode var25 = new ParamNode(var10, "N_A", var6, var15, var22, "", false, false, var15, false, var31);
         if (debugLogger.isDebugEnabled()) {
            debug(" added ParamNode: " + var25.toString());
         }

         var17.addParamSubList(var25);
         String var26 = null;

         try {
            var26 = ExprID.calcTableAndColumn(var0, var1, var24);
         } catch (Exception var29) {
            Loggable var28 = EJBLogger.logfinderCouldNotGetTableAndFieldLoggable(var9);
            var2.markExcAndThrowCollectionException(new IllegalExpressionException(7, var28.getMessage()));
         }

         switch (var5) {
            case 0:
               var4.append(var26).append(" = ? ").append(" AND ");
               break;
            case 5:
               var4.append(var26).append(" != ? ").append("  OR ");
               break;
            default:
               var2.markExcAndThrowCollectionException(new IllegalExpressionException(7, "unknown operation: " + var5 + ", encountered for QJB QL WHERE clause testing Equality of Bean " + "of Interface type '" + var13 + "' to Input Variable '?" + var6 + "'."));
         }
      }

      if (var4.length() > 5) {
         var4.setLength(var4.length() - 5);
      }

      var4.append(")");
      if (debugLogger.isDebugEnabled()) {
         debug(" Bean Parameter SQL is: " + var4.toString());
      }

      var0.addFinderInternalQueryParmList(var17);
   }

   private static void doCalcEQonBooleanLiteral(QueryContext var0, QueryNode var1, Expr var2, StringBuffer var3, boolean var4) throws ErrorCollectionException {
      Expr var5 = null;
      Expr var6 = null;
      if (var2.getTerm1().type() == 17) {
         var5 = var2.getTerm1();
         var6 = var2.getTerm2();
      } else if (var2.getTerm2().type() == 17) {
         var5 = var2.getTerm2();
         var6 = var2.getTerm1();
      }

      String var7 = ((ExprID)var5).getDealiasedEjbqlID();
      String var8 = ((ExprID)var5).calcTableAndColumnForCmpField();
      if (var6.type() == 14) {
         var3.append(var8);
         if (var4) {
            var3.append(" = ");
         } else {
            var3.append(" <> ");
         }

         var3.append("1 ");
      } else if (var6.type() == 15) {
         var3.append(var8);
         if (var4) {
            var3.append(" = ");
         } else {
            var3.append(" <> ");
         }

         var3.append("0 ");
      } else {
         Loggable var9 = EJBLogger.logfinderInvalidBooleanLiteralLoggable();
         var5.markExcAndThrowCollectionException(new IllegalExpressionException(7, "<cmr-field> " + var7 + ", " + var9.getMessage()));
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprEQUAL] " + var0);
   }
}
