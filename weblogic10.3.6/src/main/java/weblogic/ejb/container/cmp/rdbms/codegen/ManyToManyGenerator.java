package weblogic.ejb.container.cmp.rdbms.codegen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.ejbc.EjbCodeGenerator;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public final class ManyToManyGenerator extends BaseCodeGenerator {
   private static final DebugLogger debugLogger;
   private boolean keepgenerated = false;
   private RDBMSBean rbean = null;
   private CMPBeanDescriptor bd = null;
   private String cmrField;
   private String packageName = null;

   public ManyToManyGenerator(Getopt2 var1) {
      super(var1);
      this.keepgenerated = var1.hasOption("keepgenerated");
   }

   protected List typeSpecificTemplates() {
      throw new AssertionError("This method should never be called.");
   }

   public void setRDBMSBean(RDBMSBean var1) {
      assert var1 != null;

      this.rbean = var1;
   }

   public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
      this.bd = var1;
      this.packageName = RDBMSUtils.head(var1.getBeanClass().getName());
      Set var2 = var1.getPrimaryKeyFieldNames();
   }

   public void setCmrFieldName(String var1) {
      assert var1 != null;

      this.cmrField = var1;
   }

   protected void prepare(CodeGenerator.Output var1) throws EJBCException, ClassNotFoundException {
      super.prepare(var1);

      assert this.rbean != null;

      if (debugLogger.isDebugEnabled()) {
         debug("cmp.rdbms.codegen.ManyToManyGenerator.prepare() called");
      }

   }

   protected Enumeration outputs(List var1) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("ManyToManyGenerator.outputs() called");
      }

      assert var1.size() == 0;

      Vector var2 = new Vector();
      this.addOutputs(var2);
      return var2.elements();
   }

   protected void addOutputs(List var1) throws RDBMSException {
      if (debugLogger.isDebugEnabled()) {
         debug("ManyToManyGenerator.addOutputs called");
      }

      EjbCodeGenerator.Output[] var2 = new EjbCodeGenerator.Output[]{new EjbCodeGenerator.Output(), null};
      var2[0].setOutputFile(this.setClassName() + ".java");
      var2[0].setTemplate("/weblogic/ejb/container/cmp/rdbms/codegen/ManyToManySet.j");
      var2[0].setPackage(this.packageName());
      var1.add(var2[0]);
      var2[1] = new EjbCodeGenerator.Output();
      var2[1].setOutputFile(this.iteratorClassName() + ".java");
      var2[1].setTemplate("/weblogic/ejb/container/cmp/rdbms/codegen/OneToManyIterator.j");
      var2[1].setPackage(this.packageName());
      var1.add(var2[1]);
   }

   public String varPrefix() {
      return "__WL_";
   }

   public String debugVar() {
      return this.varPrefix() + "debugLogger";
   }

   public String debugEnabled() {
      return this.debugVar() + ".isDebugEnabled()";
   }

   public String debugSay() {
      return this.varPrefix() + "debug";
   }

   public String beanVar() {
      return this.varPrefix() + "bean";
   }

   public String stmtVar() {
      return this.varPrefix() + "stmt";
   }

   public String conVar() {
      return this.varPrefix() + "con";
   }

   public String rsVar() {
      return this.varPrefix() + "rs";
   }

   public String pmVar() {
      return this.varPrefix() + "pm";
   }

   public String pkVar() {
      return this.varPrefix() + "pk";
   }

   public String pk1Var() {
      return this.varPrefix() + "pk1";
   }

   public String pk2Var() {
      return this.varPrefix() + "pk2";
   }

   public String fkVar() {
      return this.varPrefix() + "fk";
   }

   public String keyVar() {
      return this.varPrefix() + "key";
   }

   public String numVar() {
      return this.varPrefix() + "num";
   }

   public String iVar() {
      return this.varPrefix() + "i";
   }

   public String countVar() {
      return this.varPrefix() + "count";
   }

   public String ctxVar() {
      return this.varPrefix() + "ctx";
   }

   public String ceoVar() {
      return this.varPrefix() + "createEo";
   }

   public String cpkVar() {
      return this.varPrefix() + "createPk";
   }

   public String colVar() {
      return this.varPrefix() + "collection";
   }

   public String bmVar() {
      return this.varPrefix() + "bm";
   }

   public String finderVar() {
      return this.varPrefix() + "finder";
   }

   public String creatorVar() {
      return this.varPrefix() + "creator";
   }

   public String cacheVar() {
      return this.varPrefix() + "cache";
   }

   public String addVar() {
      return this.varPrefix() + "add";
   }

   public String removeVar() {
      return this.varPrefix() + "rem";
   }

   public String addIter() {
      return this.varPrefix() + "additer";
   }

   public String remIter() {
      return this.varPrefix() + "remiter";
   }

   public String wrapperVar() {
      return this.varPrefix() + "wrapper";
   }

   public String iterVar() {
      return this.varPrefix() + "iter";
   }

   public String queryVar() {
      return this.varPrefix() + "query";
   }

   public String symmetricVar() {
      return this.varPrefix() + "symmetric";
   }

   public String createTxIdVar() {
      return this.varPrefix() + "createTxId";
   }

   public String addSetVar() {
      return this.varPrefix() + "addSet";
   }

   public String ejbName() {
      return this.rbean.getEjbName();
   }

   public String cmrName() {
      return this.cmrField;
   }

   public String oldStateVar() {
      return this.varPrefix() + "oldState";
   }

   private String capitalize(String var1) {
      assert var1.length() > 0;

      return var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1);
   }

   public String relatedEjbName() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return var1.getEJBName();
   }

   public String wrapperSetFinder() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "remoteWrapperSetFinder" : "localWrapperSetFinder";
   }

   public String EJBObject() {
      return !this.bd.hasLocalClientView() && !this.bd.isEJB30() ? "EJBObject" : "EJBLocalObject";
   }

   public String EJBObjectForField() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "EJBObject" : "EJBLocalObject";
   }

   public String EoWrapper() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "EoWrapper" : "EloWrapper";
   }

   public String getEJBObject() {
      return !this.bd.hasLocalClientView() && !this.bd.isEJB30() ? "getEJBObject" : "getEJBLocalObject";
   }

   public String getEJBObjectForField() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "getEJBObject" : "getEJBLocalObject";
   }

   public String setClassName() {
      return ClassUtils.setClassName(this.bd, this.cmrField);
   }

   public String iteratorClassName() {
      return ClassUtils.iteratorClassName(this.bd, this.cmrField);
   }

   public String owningBeanClassName() {
      return this.bd.getGeneratedBeanClassName();
   }

   public String owningBeanInterfaceName() {
      return this.bd.getGeneratedBeanInterfaceName();
   }

   public String remoteInterfaceName() {
      if (this.bd.hasLocalClientView()) {
         return this.bd.getLocalInterfaceName();
      } else {
         return this.bd.isEJB30() ? this.bd.getJavaClassName() : this.bd.getRemoteInterfaceName();
      }
   }

   public String relatedBeanClassName() {
      return this.rbean.getRelatedBeanClassName(this.cmrField);
   }

   public String relatedBeanInterfaceName() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return var1.getGeneratedBeanInterfaceName();
   }

   public String relatedRemoteInterfaceName() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      if (var1.hasLocalClientView()) {
         return var1.getLocalInterfaceName();
      } else {
         return var1.isEJB30() ? this.bd.getJavaClassName() : var1.getRemoteInterfaceName();
      }
   }

   private String declarePkVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.bd.getPrimaryKeyClass().getName() + " " + this.pkVar() + " = ");
      if (this.bd.hasComplexPrimaryKey()) {
         var1.append("new " + this.bd.getPrimaryKeyClass().getName() + "();");
      } else {
         var1.append("null;");
      }

      return var1.toString();
   }

   public String perhapsDeclarePkVar() {
      return this.bd.hasComplexPrimaryKey() ? this.declarePkVar() : "";
   }

   public String perhapsAssignPkVar() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public String pkVarForBean() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public String declareAddSet() {
      return "private Set " + this.addSetVar() + " = null;";
   }

   private String setterMethodName(String var1) {
      return "set" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1);
   }

   public String relatedBeanSetMethod() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.setterMethodName(this.rbean.getRelatedFieldName(this.cmrField)));
      return var1.toString();
   }

   public String packageName() {
      assert this.packageName != null;

      return this.packageName;
   }

   public String packageStatement() {
      return this.packageName != null && !this.packageName.equals("") ? "package " + this.packageName + ";" : "";
   }

   public String symmetricRelationship() {
      RDBMSBean var1 = this.rbean.getRelatedRDBMSBean(this.cmrField);
      String var2 = this.rbean.getRelatedFieldName(this.cmrField);
      return var1 == this.rbean && var2.equals(this.cmrField) ? "true" : "false";
   }

   public String joinParamsSql() {
      StringBuffer var1 = new StringBuffer();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.rbean.getForeignKeyColNames(this.cmrField).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(var4);
         var1.append("(" + var4 + " = ?)");
         if (var3.hasNext()) {
            var1.append(" AND ");
         }
      }

      RDBMSBean var7 = this.rbean.getRelatedRDBMSBean(this.cmrField);
      String var5 = this.rbean.getRelatedFieldName(this.cmrField);
      if (var7 == this.rbean && var5.equals(this.cmrField)) {
         var3 = var7.getSymmetricKeyColNames(var5).iterator();
      } else {
         var3 = var7.getForeignKeyColNames(var5).iterator();
      }

      while(var3.hasNext()) {
         String var6 = (String)var3.next();
         if (!var2.contains(var6)) {
            var1.append(" AND ");
            var1.append("(" + var6 + " = ?)");
         }
      }

      return var1.toString();
   }

   private void addPrimaryKeyBinding(StringBuffer var1, String var2, int var3, String var4, Class var5, String var6) {
      var1.append(this.stmtVar() + ".set" + var2 + "(");
      var1.append(var3).append(", ");
      var1.append(var4 + MethodUtils.convertToPrimitive(var5, var6));
      var1.append(");" + EOL);
   }

   private int addPrimaryKeyBindings(StringBuffer var1, RDBMSBean var2, CMPBeanDescriptor var3, String var4, int var5, String var6, List var7, boolean var8) throws CodeGenerationException {
      boolean var9 = var3.hasComplexPrimaryKey();
      Class var10 = var3.getPrimaryKeyClass();
      Iterator var11 = null;
      RDBMSBean var12 = var2.getRelatedRDBMSBean(var4);
      String var13 = var2.getRelatedFieldName(var4);
      boolean var14 = var8 && var12 == var2 && var13.equals(var4);
      Map var15 = null;
      if (var14) {
         var11 = var12.getSymmetricKeyColNames(var13).iterator();
         var15 = var12.getSymmetricColumn2FieldName(var13);
      } else {
         var11 = var2.getForeignKeyColNames(var4).iterator();
      }

      while(var11.hasNext()) {
         String var16 = (String)var11.next();
         if (!var7.contains(var16)) {
            var7.add(var16);
            String var17 = null;
            if (var14) {
               var17 = (String)var15.get(var16);
            } else {
               var17 = var2.getRelatedPkFieldName(var4, var16);
            }

            String var18 = null;
            Class var19 = null;
            String var20 = null;
            if (var9) {
               var18 = "((" + var10.getName() + ")" + var6 + ").";
               Field var21 = null;

               try {
                  var21 = var10.getField(var17);
               } catch (NoSuchFieldException var24) {
                  Loggable var23 = EJBLogger.logFieldNotFoundInClassLoggable(var17, var10.getName());
                  throw new CodeGenerationException(var23.getMessage());
               }

               var19 = var21.getType();
               var20 = StatementBinder.getStatementTypeNameForClass(var19);
            } else {
               var18 = "";
               var19 = var10;
               var20 = StatementBinder.getStatementTypeNameForClass(var10);
               var17 = "((" + var10.getName() + ")" + var6 + ")";
            }

            if (RDBMSUtils.isOracleNLSDataType(var12, var17)) {
               var1.append("if(").append(this.stmtVar()).append(" instanceof oracle.jdbc.OraclePreparedStatement) {" + EOL);
               var1.append("((oracle.jdbc.OraclePreparedStatement)").append(this.stmtVar()).append(").setFormOfUse(").append(var5).append(", oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);").append(EOL);
               var1.append("}" + EOL);
            }

            this.addPrimaryKeyBinding(var1, var20, var5, var18, var19, var17);
            ++var5;
         }
      }

      return var5;
   }

   public String setJoinTableParams() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      int var2 = 1;
      ArrayList var3 = new ArrayList();
      var2 = this.addPrimaryKeyBindings(var1, this.rbean, this.bd, this.cmrField, var2, this.pk1Var(), var3, false);
      RDBMSBean var4 = this.rbean.getRelatedRDBMSBean(this.cmrField);
      CMPBeanDescriptor var5 = this.rbean.getRelatedDescriptor(this.cmrField);
      String var6 = this.rbean.getRelatedFieldName(this.cmrField);
      this.addPrimaryKeyBindings(var1, var4, var5, var6, var2, this.pk2Var(), var3, true);
      return var1.toString();
   }

   public String joinColumnsSql() {
      StringBuffer var1 = new StringBuffer();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.rbean.getForeignKeyColNames(this.cmrField).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.append(var4);
         var2.add(var4);
         if (var3.hasNext()) {
            var1.append(", ");
         }
      }

      RDBMSBean var7 = this.rbean.getRelatedRDBMSBean(this.cmrField);
      String var5 = this.rbean.getRelatedFieldName(this.cmrField);
      if (var7 == this.rbean && var5.equals(this.cmrField)) {
         var3 = var7.getSymmetricKeyColNames(var5).iterator();
      } else {
         var3 = var7.getForeignKeyColNames(var5).iterator();
      }

      while(var3.hasNext()) {
         String var6 = (String)var3.next();
         if (!var2.contains(var6)) {
            var1.append(", ");
            var1.append(var6);
         }
      }

      return var1.toString();
   }

   public String joinColsQMs() {
      StringBuffer var1 = new StringBuffer();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.rbean.getForeignKeyColNames(this.cmrField).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.append("?");
         var2.add(var4);
         if (var3.hasNext()) {
            var1.append(", ");
         }
      }

      RDBMSBean var7 = this.rbean.getRelatedRDBMSBean(this.cmrField);
      String var5 = this.rbean.getRelatedFieldName(this.cmrField);
      if (var7 == this.rbean && var5.equals(this.cmrField)) {
         var3 = var7.getSymmetricKeyColNames(var5).iterator();
      } else {
         var3 = var7.getForeignKeyColNames(var5).iterator();
      }

      while(var3.hasNext()) {
         String var6 = (String)var3.next();
         if (!var2.contains(var6)) {
            var1.append(", ");
            var1.append("?");
         }
      }

      return var1.toString();
   }

   public String joinTableName() {
      assert this.rbean.getJoinTableName(this.cmrField) != null;

      return this.rbean.getJoinTableName(this.cmrField);
   }

   public String relatedGetMethodName() {
      String var1 = this.rbean.getRelatedFieldName(this.cmrField);
      return MethodUtils.getMethodName(var1);
   }

   public String existsJoinTableQuery() {
      return this.existsJoinTableQuery(0);
   }

   public String existsJoinTableQueryForUpdate() {
      return this.existsJoinTableQuery(1);
   }

   public String existsJoinTableQueryForUpdateNoWait() {
      return this.existsJoinTableQuery(2);
   }

   private String existsJoinTableQuery(int var1) {
      StringBuffer var2 = new StringBuffer("SELECT 7 FROM ");
      var2.append(this.joinTableName());
      int var3 = this.rbean.getDatabaseType();
      switch (var3) {
         case 0:
         case 1:
         case 3:
         case 4:
         case 6:
         case 8:
         case 9:
            var2.append(" WHERE ");
            var2.append(this.joinParamsSql());
            var2.append(RDBMSUtils.selectForUpdateToString(var1));
            break;
         case 2:
         case 7:
            if (var1 == 1) {
               var2.append(" WITH(UPDLOCK)");
            }

            var2.append(" WHERE ");
            var2.append(this.joinParamsSql());
            if (var1 == 2) {
               var2.append(RDBMSUtils.selectForUpdateToString(var1));
            } else if (var1 == 0) {
            }
            break;
         case 5:
            if (var1 == 1) {
               var2.append(" HOLDLOCK");
            }

            var2.append(" WHERE ");
            var2.append(this.joinParamsSql());
            if (var1 == 2) {
               var2.append(RDBMSUtils.selectForUpdateToString(var1));
            } else if (var1 == 0) {
            }
            break;
         default:
            throw new AssertionError("Undefined database type " + var3);
      }

      return var2.toString();
   }

   public String perhapsImplementQueryCachingMethods() throws CodeGenerationException {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return this.parse(this.getProductionRule("queryCachingMethods"));
   }

   public String isLocal() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "false" : "true";
   }

   public String perhapsPopulateFromQueryCache() {
      if (!this.rbean.isQueryCachingEnabledForCMRField(this.cmrField)) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.cacheVar()).append(" = getFromQueryCache();").append(EOL);
         var1.append("if (").append(this.cacheVar()).append(" == null) {").append(EOL);
         return var1.toString();
      }
   }

   public String perhapsPutInQueryCache() {
      if (!this.rbean.isQueryCachingEnabledForCMRField(this.cmrField)) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer("}");
         var1.append(EOL);
         var1.append("putInQueryCache(null);").append(EOL);
         return var1.toString();
      }
   }

   public String readOnlyFinderRunsInItsOwnTransaction() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      if (var1.getConcurrencyStrategy() == 4) {
         StringBuffer var2 = new StringBuffer();
         var2.append("Transaction orgTx = TransactionHelper.getTransactionHelper().getTransaction();\n");
         var2.append("try {\n");
         var2.append("TransactionManager tms = TxHelper.getTransactionManager();\n");
         var2.append("tms.suspend();\n");
         var2.append("tms.begin();\n");
         var2.append("Transaction tx = tms.getTransaction();\n");
         var2.append(this.cacheVar() + " = " + this.bmVar() + "." + this.wrapperSetFinder() + "(" + this.finderVar() + ", new Object[] {" + this.cpkVar() + "}, true);\n");
         var2.append("tx.commit();\n");
         var2.append("} catch(Exception e) {\n");
         var2.append(" throw e; }\n");
         var2.append("finally {\n");
         var2.append("TxHelper.getTransactionManager().resume(orgTx);\n");
         var2.append("}\n");
         return var2.toString();
      } else {
         return this.cacheVar() + " = " + this.bmVar() + "." + this.wrapperSetFinder() + "(" + this.finderVar() + ", new Object[] {" + this.cpkVar() + "}, true);";
      }
   }

   public String isReadOnly() {
      CMPBeanDescriptor var1 = this.rbean.getRelatedDescriptor(this.cmrField);
      return !var1.isReadOnly() && var1.getConcurrencyStrategy() != 4 ? "false" : "true";
   }

   private static void debug(String var0) {
      debugLogger.debug("[ManyToManyGenerator] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
