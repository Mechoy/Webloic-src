package weblogic.ejb.container.cmp.rdbms.codegen;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.ejbc.EjbCodeGenerator;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.utils.AssertionError;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public final class OneToManyGenerator extends BaseCodeGenerator {
   private boolean keepgenerated = false;
   private RDBMSBean bean = null;
   private CMPBeanDescriptor bd = null;
   private String cmrField;
   private String packageName = null;

   public OneToManyGenerator(Getopt2 var1) {
      super(var1);
      this.keepgenerated = var1.hasOption("keepgenerated");
   }

   protected List typeSpecificTemplates() {
      throw new AssertionError("This method should never be called.");
   }

   public void setRDBMSBean(RDBMSBean var1) {
      assert var1 != null;

      this.bean = var1;
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

      assert this.bean != null;

      if (debugLogger.isDebugEnabled()) {
         debug("cmp.rdbms.codegen.OneToManyGenerator.prepare() called");
      }

   }

   protected Enumeration outputs(List var1) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("OneToManyGenerator.outputs() called");
      }

      assert var1.size() == 0;

      Vector var2 = new Vector();
      this.addOutputs(var2);
      return var2.elements();
   }

   protected void addOutputs(List var1) throws RDBMSException {
      if (debugLogger.isDebugEnabled()) {
         debug("OneToManyGenerator.addOutputs called");
      }

      EjbCodeGenerator.Output[] var2 = new EjbCodeGenerator.Output[]{new EjbCodeGenerator.Output(), null};
      var2[0].setOutputFile(this.setClassName() + ".java");
      var2[0].setTemplate("/weblogic/ejb/container/cmp/rdbms/codegen/OneToManySet.j");
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

   public String pkVar() {
      return this.varPrefix() + "pk";
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

   public String pmVar() {
      return this.varPrefix() + "pm";
   }

   public String createTxIdVar() {
      return this.varPrefix() + "createTxId";
   }

   public String oldStateVar() {
      return this.varPrefix() + "oldState";
   }

   public String ejbName() {
      return this.bean.getEjbName();
   }

   public String relatedEjbName() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return var1.getEJBName();
   }

   public String cmrName() {
      return this.cmrField;
   }

   private String capitalize(String var1) {
      assert var1.length() > 0;

      return var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1);
   }

   public String wrapperSetFinder() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "remoteWrapperSetFinder" : "localWrapperSetFinder";
   }

   public String EJBObject() {
      return !this.bd.hasLocalClientView() && !this.bd.isEJB30() ? "EJBObject" : "EJBLocalObject";
   }

   public String EJBObjectForField() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "EJBObject" : "EJBLocalObject";
   }

   public String EoWrapper() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "EoWrapper" : "EloWrapper";
   }

   public String getEJBObject() {
      return !this.bd.hasLocalClientView() && !this.bd.isEJB30() ? "getEJBObject" : "getEJBLocalObject";
   }

   public String getEJBObjectForField() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "getEJBObject" : "getEJBLocalObject";
   }

   public String getWLGetEJBObject() {
      return !this.bd.hasLocalClientView() && !this.bd.isEJB30() ? "__WL_getEJBObject" : "__WL_getEJBLocalObject";
   }

   public String elementInterfaceName() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      if (var1.hasLocalClientView()) {
         return var1.getLocalInterfaceName();
      } else {
         return var1.isEJB30() ? var1.getJavaClassName() : var1.getRemoteInterfaceName();
      }
   }

   public String remoteInterfaceName() {
      if (this.bd.hasLocalClientView()) {
         return this.bd.getLocalInterfaceName();
      } else {
         return this.bd.isEJB30() ? this.bd.getJavaClassName() : this.bd.getRemoteInterfaceName();
      }
   }

   public String owningBeanClassName() {
      return this.bd.getGeneratedBeanClassName();
   }

   public String owningBeanInterfaceName() {
      return this.bd.getGeneratedBeanInterfaceName();
   }

   public String relatedBeanClassName() {
      return this.bean.getRelatedBeanClassName(this.cmrField);
   }

   public String relatedBeanInterfaceName() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return var1.getGeneratedBeanInterfaceName();
   }

   private String declarePkVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.bd.getPrimaryKeyClass().getName() + " " + this.pkVar() + " = null;");
      return var1.toString();
   }

   public String perhapsDeclarePkVar() {
      return this.bd.hasComplexPrimaryKey() ? this.declarePkVar() : "";
   }

   public String perhapsAssignPkVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.hasComplexPrimaryKey()) {
         CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(this.cmrField);
         RDBMSBean var3 = this.bean.getRelatedRDBMSBean(this.cmrField);
         String var4 = this.bean.getRelatedFieldName(this.cmrField);
         String var5 = var3.getTableForCmrField(var4);
         boolean var6 = false;
         Iterator var7 = var3.getForeignKeyColNames(var4).iterator();

         String var8;
         while(var7.hasNext()) {
            var8 = (String)var7.next();
            Class var9 = var3.getForeignKeyColClass(var4, var8);
            if (Object.class.isAssignableFrom(var9)) {
               var6 = true;
            }
         }

         String var16;
         if (var6) {
            var1.append("if (");
            boolean var15 = true;
            var7 = var3.getForeignKeyColNames(var4).iterator();

            while(var7.hasNext()) {
               var16 = (String)var7.next();
               String var10 = var3.variableForField(var4, var5, var16);
               Class var11 = var3.getForeignKeyColClass(var4, var16);
               if (Object.class.isAssignableFrom(var11)) {
                  String var12 = this.beanVar() + "." + MethodUtils.getMethodName(var10) + "()";
                  if (!var15) {
                     var1.append(" && ");
                  } else {
                     var15 = false;
                  }

                  var1.append(var12 + "!=null");
               }
            }

            var1.append(") {" + EOL);
         }

         var1.append(this.pkVar() + " = new " + this.bd.getPrimaryKeyClass().getName() + "();" + EOL);

         String var13;
         String var14;
         for(var7 = var3.getForeignKeyColNames(var4).iterator(); var7.hasNext(); var1.append(var13 + " = " + var14 + ";" + EOL)) {
            var8 = (String)var7.next();
            var16 = var3.variableForField(var4, var5, var8);
            var3.getForeignKeyColClass(var4, var8);
            String var17 = var3.getRelatedPkFieldName(var4, var8);
            Class var18 = this.bd.getFieldClass(var17);
            var13 = this.pkVar() + "." + var17;
            var14 = this.beanVar() + "." + MethodUtils.getMethodName(var16) + "()";
            if (var18.isPrimitive()) {
               var14 = MethodUtils.convertToPrimitive(var18, var14);
            }
         }

         if (var6) {
            var1.append("}" + EOL);
         }
      }

      return var1.toString();
   }

   public String pkVarForBean() {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.hasComplexPrimaryKey()) {
         var1.append(this.pkVar());
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("cmrField is: " + this.cmrField);
         }

         RDBMSBean var2 = this.bean.getRelatedRDBMSBean(this.cmrField);
         String var3 = this.bean.getRelatedFieldName(this.cmrField);
         String var4 = var2.getTableForCmrField(var3);
         String var5 = (String)var2.getForeignKeyColNames(var3).iterator().next();
         String var6 = var2.variableForField(var3, var4, var5);
         Class var7 = var2.getForeignKeyColClass(var3, var5);
         var1.append(this.perhapsConvertPrimitive(var7, this.beanVar() + "." + MethodUtils.getMethodName(var6) + "()"));
      }

      return var1.toString();
   }

   private String setterMethodName(String var1) {
      return "set" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1);
   }

   public String relatedBeanSetMethod() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.setterMethodName(this.bean.getRelatedFieldName(this.cmrField)));
      return var1.toString();
   }

   public String packageName() {
      assert this.packageName != null;

      return this.packageName;
   }

   public String packageStatement() {
      return this.packageName != null && !this.packageName.equals("") ? "package " + this.packageName + ";" : "";
   }

   public String setClassName() {
      return ClassUtils.setClassName(this.bd, this.cmrField);
   }

   public String iteratorClassName() {
      return ClassUtils.iteratorClassName(this.bd, this.cmrField);
   }

   public String perhapsInvokeFinder() throws CodeGenerationException {
      if (this.bean.getLoadRelatedBeansFromDbInPostCreate()) {
         return this.readOnlyFinderRunsInItsOwnTransaction();
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.parse(this.getProductionRule("loadIfNotPostCreate")));
         var1.append("\n else { \n");
         var1.append(this.readOnlyFinderRunsInItsOwnTransaction());
         var1.append("\n } \n");
         return var1.toString();
      }
   }

   public String isLocal() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return var1.hasLocalClientView() ? "true" : "false";
   }

   public String perhapsImplementQueryCachingMethods() throws CodeGenerationException {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return this.parse(this.getProductionRule("queryCachingMethods"));
   }

   public String perhapsPopulateFromQueryCache() {
      if (!this.bean.isQueryCachingEnabledForCMRField(this.cmrField)) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.cacheVar()).append(" = getFromQueryCache();").append(EOL);
         var1.append("if (").append(this.cacheVar()).append(" == null) {").append(EOL);
         return var1.toString();
      }
   }

   public String perhapsPutInQueryCache() {
      if (!this.bean.isQueryCachingEnabledForCMRField(this.cmrField)) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer("}");
         var1.append(EOL);
         var1.append("putInQueryCache(null);").append(EOL);
         return var1.toString();
      }
   }

   public String readOnlyFinderRunsInItsOwnTransaction() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
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
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.cmrField);
      return !var1.isReadOnly() && var1.getConcurrencyStrategy() != 4 ? "false" : "true";
   }

   public String perhapsDeclareReadWriteVars() {
      StringBuffer var1 = new StringBuffer();
      CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(this.cmrField);
      if (!var2.isReadOnly() && var2.getConcurrencyStrategy() != 4) {
         var1.append("private Set " + this.addVar() + ";" + EOL);
         var1.append("private Set " + this.removeVar() + ";" + EOL);
         var1.append("private RDBMSPersistenceManager " + this.pmVar() + ";" + EOL);
      }

      return var1.toString();
   }

   public String perhapsInitReadWriteVars() {
      StringBuffer var1 = new StringBuffer();
      CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(this.cmrField);
      if (!var2.isReadOnly() && var2.getConcurrencyStrategy() != 4) {
         var1.append(this.addVar() + " = new ArraySet(10);" + EOL);
         var1.append(this.removeVar() + " = new ArraySet(10);" + EOL);
         var1.append(this.pmVar() + " = (RDBMSPersistenceManager)" + this.creatorVar() + ".__WL_getPersistenceManager();" + EOL);
      }

      return var1.toString();
   }

   public String perhapsReconcileReadWriteChanges() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(this.cmrField);
      if (!var2.isReadOnly() && var2.getConcurrencyStrategy() != 4) {
         var1.append(this.parse(this.getProductionRule("reconcileReadWriteChanges")));
      }

      return var1.toString();
   }

   public String perhapsDoAddForReadWrite() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(this.cmrField);
      if (!var2.isReadOnly() && var2.getConcurrencyStrategy() != 4) {
         var1.append(this.parse(this.getProductionRule("doAddForReadWrite")));
      }

      return var1.toString();
   }

   public String perhapsDoRemoveForReadWrite() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(this.cmrField);
      if (!var2.isReadOnly() && var2.getConcurrencyStrategy() != 4) {
         var1.append(this.parse(this.getProductionRule("doRemoveForReadWrite")));
      }

      return var1.toString();
   }

   private static void debug(String var0) {
      debugLogger.debug("[OneToManyGenerator] " + var0);
   }
}
