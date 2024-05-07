package weblogic.ejb.container.cmp11.rdbms.codegen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.EntityContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp11.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp11.rdbms.finders.Finder;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.ejbc.codegen.MethodSignature;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.StringUtils;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.string.Sprintf;

public final class RDBMSCodeGenerator extends CMPCodeGenerator {
   private static final DebugLogger debugLogger;
   private RDBMSBean beanData = null;
   private Map variableToClass = null;
   private List finderList = null;
   private List primaryKeyFieldList = null;
   private FinderMethodInfo finderMethodInfo = null;
   private int preparedStatementParamIndex = -1;
   private Map parameterMap = null;
   private List fieldList = null;
   private List nonPrimaryKeyFieldList = null;
   private SnapshotFieldInfo[] snapshotFieldInfo;
   private boolean useTunedUpdates;
   private int level = 0;
   private Class[] immutableClasses = new Class[]{Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, String.class, BigDecimal.class, BigInteger.class};
   private int current_index;
   private SnapshotFieldInfo current;

   public RDBMSCodeGenerator(Getopt2 var1) {
      super(var1);
   }

   public void setRDBMSBean(RDBMSBean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("RDBMSCodeGenerator.setRDBMSBean(" + var1 + ")");
      }

      assert var1 != null;

      this.beanData = var1;
      this.variableToClass = new HashMap();
      List var2 = var1.getCmpFieldNames();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.variableToClass.put(var4, this.bd.getFieldClass(var4));
      }

   }

   public void setFinderList(List var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("RDBMSCodeGenerator.setFinderList(" + var1 + ")");
      }

      this.finderList = var1;
   }

   public void setCMFields(List var1) {
      this.nonPrimaryKeyFieldList = this.deriveNonPrimaryKeyFields(var1);
      this.fieldList = new ArrayList();
      this.fieldList.addAll(this.primaryKeyFieldList);
      this.fieldList.addAll(this.nonPrimaryKeyFieldList);
   }

   public void setPrimaryKeyFields(List var1) {
      this.primaryKeyFieldList = var1;
   }

   protected List typeSpecificTemplates() {
      ArrayList var1 = new ArrayList();
      var1.add("weblogic/ejb/container/cmp11/rdbms/codegen/template.j");
      return var1;
   }

   public void setParameterMap(Map var1) {
      this.parameterMap = var1;
   }

   private List deriveNonPrimaryKeyFields(List var1) {
      Debug.assertion(this.primaryKeyFieldList != null);
      Debug.assertion(var1 != null);
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!this.primaryKeyFieldList.contains(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   protected void prepare(CodeGenerator.Output var1) throws EJBCException, ClassNotFoundException {
      super.prepare(var1);

      assert this.beanData != null;

      assert this.finderList != null;

      assert this.primaryKeyFieldList != null;

      assert this.fieldList != null;

      assert this.parameterMap != null;

      assert this.nonPrimaryKeyFieldList != null;

      if (debugLogger.isDebugEnabled()) {
         debug("cmp11.rdbms.codegen.RDBMSCodeGenerator.prepare() called");
      }

   }

   public static String varPrefix() {
      return "__WL_";
   }

   public String debugVar() {
      return varPrefix() + "debugLogger";
   }

   public String debugEnabled() {
      return this.debugVar() + ".isDebugEnabled()";
   }

   public String debugSay() {
      return varPrefix() + "debug";
   }

   public String beanVar() {
      return varPrefix() + "bean";
   }

   public String pkVar() {
      return varPrefix() + "pk";
   }

   public String eoVar() {
      return varPrefix() + "eo";
   }

   public String ctxVar() {
      return varPrefix() + "ctx";
   }

   public String conVar() {
      return varPrefix() + "con";
   }

   public String rsVar() {
      return varPrefix() + "rs";
   }

   public String rsInfoVar() {
      return varPrefix() + "rsInfo";
   }

   public String stmtVar() {
      return varPrefix() + "stmt";
   }

   public String pmVar() {
      return varPrefix() + "pm";
   }

   public String keyVar() {
      return varPrefix() + "key";
   }

   public String numVar() {
      return varPrefix() + "num";
   }

   public String queryVar() {
      return varPrefix() + "query";
   }

   public String iVar() {
      return varPrefix() + "i";
   }

   public String countVar() {
      return varPrefix() + "count";
   }

   public String stringVar() {
      return varPrefix() + "stringVar";
   }

   public String stringVar(String var1) {
      return varPrefix() + "stringVar" + "_" + var1;
   }

   public String sqlTimestampVar() {
      return varPrefix() + "sqlTimestampVar";
   }

   public String sqlTimestampVar(String var1) {
      return varPrefix() + "sqlTimestampVar" + "_" + var1;
   }

   public String byteArrayVar() {
      return varPrefix() + "byteArrayVar";
   }

   public String byteArrayVar(String var1) {
      return varPrefix() + "byteArrayVar" + "_" + var1;
   }

   public String isModifiedVar() {
      return "isModified";
   }

   public String createMethodName() {
      return varPrefix() + "create";
   }

   public String existsMethodName() {
      return varPrefix() + "exists";
   }

   public String standardCatch() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.lvl(2) + "} catch (RuntimeException e) {" + EOL);
      var1.append(this.upLvl() + "if (" + this.debugEnabled() + ") " + this.debugSay() + "(\"throwing runtime exception\");" + EOL);
      var1.append(this.lvl() + "throw e;" + EOL);
      var1.append(this.dnLvl() + "}" + EOL);
      var1.append(this.lvl() + "catch (Exception ex) {" + EOL);
      var1.append(this.upLvl() + "if (" + this.debugEnabled() + ") " + this.debugSay() + "(\"throwing ejbeception\");" + EOL);
      var1.append(this.lvl() + "throw new PersistenceRuntimeException(ex);" + EOL);
      var1.append(this.dnLvl() + "}" + EOL);
      return var1.toString();
   }

   public String declareEntityContextVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.lvl(0) + "private EntityContext " + this.ctxVar() + ";");
      return var1.toString();
   }

   public String declareIsModified() {
      StringBuffer var1 = new StringBuffer();
      var1.append("\tprivate boolean[] ");
      var1.append(this.isModifiedVar());
      var1.append(" = new boolean[");
      var1.append(this.fieldList.size());
      var1.append("];");
      var1.append(EOL);
      return var1.toString();
   }

   public String tableName() {
      String var1 = "";
      if (this.beanData.getUseQuotedNames()) {
         if (this.beanData.getSchemaName() != null && !this.beanData.getSchemaName().equals("")) {
            var1 = var1 + '"' + this.beanData.getSchemaName() + '"' + ".";
         }

         var1 = var1 + '"' + this.beanData.getTableName() + '"';
      } else {
         if (this.beanData.getSchemaName() != null && !this.beanData.getSchemaName().equals("")) {
            var1 = var1 + this.beanData.getSchemaName() + ".";
         }

         var1 = var1 + this.beanData.getTableName();
      }

      return var1;
   }

   public String getSimpleBeanClassName() {
      return MethodUtils.tail(this.bd.getGeneratedBeanClassName());
   }

   public String declareNoArgsConstructor() {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      Class var4 = var3.getBeanClass();

      try {
         Constructor var5 = var4.getConstructor();

         assert var5 != null;

         var1.append(this.lvl(1) + "public " + MethodUtils.tail(var3.getGeneratedBeanClassName()));
         var1.append("()");
         Class[] var6 = var5.getExceptionTypes();
         if (var6.length > 0) {
            var1.append(" throws ");

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var1.append(this.javaCodeForType(var6[var7]));
               if (var7 < var6.length - 1) {
                  var1.append(", ");
               }
            }
         }
      } catch (NoSuchMethodException var8) {
         throw new AssertionError("Unable to find constructor on class '" + var4.getName() + "'.");
      }

      return var1.toString();
   }

   public String declareBeanMethod(String var1, Class[] var2) {
      StringBuffer var3 = new StringBuffer();
      CMPCodeGenerator.Output var4 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var5 = var4.getCMPBeanDescriptor();
      Class var6 = var5.getBeanClass();

      try {
         Method var7 = var6.getMethod(var1, var2);

         assert var7 != null;

         MethodSignature var8 = new MethodSignature(var7);
         var3.append(this.lvl(1) + var8.toString());
      } catch (NoSuchMethodException var9) {
         throw new AssertionError("Unable to find '" + var1 + "' method on class '" + var6.getName() + "'.");
      }

      return var3.toString();
   }

   public String initializePersistentVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.variableToClass.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Class var4 = (Class)this.variableToClass.get(var3);
         var1.append(this.lvl(2) + var3 + " = " + ClassUtils.getDefaultValue(var4) + ";" + EOL);
      }

      return var1.toString();
   }

   public String copyFromMethodBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      var1.append(this.lvl(2) + var3.getGeneratedBeanClassName() + " " + this.beanVar() + " = null;" + EOL);
      var1.append(this.lvl() + "try {" + EOL);
      var1.append(this.upLvl() + this.beanVar() + " = (" + var3.getGeneratedBeanClassName() + ")otherBean;" + EOL);
      var1.append(this.standardCatch());
      Iterator var4 = var3.getCMFieldNames().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var1.append(var5 + " = " + this.beanVar() + "." + var5 + ";" + EOL);
      }

      return var1.toString();
   }

   public String initializePersistentVarsForBeanVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.finderMethodInfo.loadBean) {
         Iterator var2 = this.variableToClass.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Class var4 = (Class)this.variableToClass.get(var3);
            var1.append(this.lvl(2) + this.beanVar() + "." + var3 + " = " + ClassUtils.getDefaultValue(var4) + ";" + EOL);
         }
      }

      return var1.toString();
   }

   public String declareSetEntityContextMethod() {
      return this.declareBeanMethod("setEntityContext", new Class[]{EntityContext.class});
   }

   public String declareEjbLoadMethod() {
      return this.declareBeanMethod("ejbLoad", new Class[0]);
   }

   private boolean throwsCreateException(Method var1) {
      Class[] var2 = var1.getExceptionTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].isAssignableFrom(CreateException.class)) {
            return true;
         }
      }

      return false;
   }

   public String ejbLoadExceptionList() {
      return this.ejbCallbackMethodExceptionList("ejbLoad");
   }

   public String ejbStoreExceptionList() {
      return this.ejbCallbackMethodExceptionList("ejbStore");
   }

   public String ejbCallbackMethodExceptionList(String var1) {
      StringBuffer var2 = new StringBuffer();
      CMPCodeGenerator.Output var3 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var4 = var3.getCMPBeanDescriptor();
      Class var5 = var4.getBeanClass();

      try {
         Method var6 = var5.getMethod(var1);

         assert var6 != null;

         Class[] var7 = var6.getExceptionTypes();
         if (var7.length > 0) {
            var2.append(" throws ");

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var2.append(this.javaCodeForType(var7[var8]));
               if (var8 < var7.length - 1) {
                  var2.append(", ");
               }
            }
         }
      } catch (NoSuchMethodException var9) {
         throw new AssertionError("Unable to find ejbLoad on class '" + var5.getName() + "'.");
      }

      return var2.toString();
   }

   public String implementEjbCreateMethods() {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      Class var4 = var3.getBeanClass();
      Method[] var5 = var4.getMethods();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         Method var7 = var5[var6];
         if (var7.getName().equals("ejbCreate")) {
            MethodSignature var8 = new MethodSignature(var7);
            var1.append(this.lvl(1) + var8.toString() + " {" + EOL);
            var1.append("int oldState = __WL_method_state;" + EOL + "try {" + EOL);
            var1.append("__WL_method_state = STATE_EJB_CREATE;" + EOL);
            var1.append("__WL_initialize();" + EOL);
            var1.append(this.upLvl() + "super.ejbCreate(");
            var1.append(var8.getParametersAsArgs());
            var1.append(");" + EOL + EOL);
            Class var9 = var8.getReturnType();
            var1.append(this.lvl() + "try {" + EOL);
            var1.append(this.upLvl() + "return (" + ClassUtils.classToJavaSourceType(var9) + ")" + this.createMethodName() + "();" + EOL);
            if (this.throwsCreateException(var7)) {
               var1.append(this.dnLvl() + "} catch (javax.ejb.CreateException ce) {" + EOL);
               var1.append("System.out.println(\"throwing create exception.\");" + EOL);
               var1.append(this.upLvl() + "throw ce;" + EOL);
            }

            var1.append(this.standardCatch());
            var1.append("} finally { __WL_method_state = oldState; }" + EOL);
            var1.append(this.dnLvl() + "}" + EOL + EOL);
         }
      }

      return var1.toString();
   }

   public String implementEjbRemoveMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.lvl(1) + this.declareBeanMethod("ejbRemove", new Class[0]) + " {" + EOL);
      var1.append(this.parse(this.getProductionRule("implementEjbRemoveMethodBody")));
      var1.append(this.lvl(1) + "}");
      return var1.toString();
   }

   public String implementEjbStoreMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.lvl(1) + this.declareBeanMethod("ejbStore", new Class[0]) + " {" + EOL);
      var1.append(this.parse(this.getProductionRule("implementEjbStoreMethodBody")));
      var1.append(this.lvl(1) + "}");
      return var1.toString();
   }

   public String implementFinderMethods() throws EJBCException {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb20.cmp11.rdbms.codegen.RDBMSCodeGenerator.implementFinderMethods() called.");
      }

      StringBuffer var1 = new StringBuffer(100);
      Iterator var2 = this.finderList.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();

         try {
            if (debugLogger.isDebugEnabled()) {
               debug("generating finder: " + var3);
            }

            var1.append(this.implementFinderMethod(var3));
         } catch (CodeGenerationException var5) {
            throw new EJBCException("Could not generate finder method code for finder " + var3 + " due to CodeGenerationException: " + var5.getMessage());
         }
      }

      return var1.toString();
   }

   private String implementFinderMethod(Method var1) throws EJBCException, CodeGenerationException {
      if (debugLogger.isDebugEnabled()) {
         debug("implementFinderMethod(" + var1 + ") called.");
      }

      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();

      assert var3 != null;

      this.finderMethodInfo = new FinderMethodInfo();
      this.finderMethodInfo.method = var1;
      this.finderMethodInfo.finder = this.beanData.getFinderForMethod(var1);
      this.finderMethodInfo.loadBean = var3.getFindersLoadBean();

      assert this.finderMethodInfo.method != null;

      if (this.finderMethodInfo.finder == null) {
         throw new EJBCException("Could not find finder descriptor for method with signature " + var1);
      } else {
         StringBuffer var4 = new StringBuffer();
         String var5 = MethodUtils.getFinderMethodDeclaration(var1, var3.getPrimaryKeyClass(), true);
         var4.append(var5);
         var4.append("{" + EOL);

         try {
            if (this.isMultiFinder(var1)) {
               var4.append(this.parse(this.getProductionRule("finderMethodBodyMulti")));
            } else {
               var4.append(this.parse(this.getProductionRule("finderMethodBodyScalar")));
            }
         } catch (CodeGenerationException var8) {
            if (debugLogger.isDebugEnabled()) {
               debug("finderMethod cought CodeGenerationException : " + var8);
            }

            if (debugLogger.isDebugEnabled()) {
               var8.printStackTrace();
            }

            String var7 = "Could not produce production rule for this finder.";
            EJBLogger.logStackTraceAndMessage(var7, var8);
            throw new EJBCException(var7);
         }

         var4.append("" + EOL + "}" + EOL);
         this.finderMethodInfo = null;
         return var4.toString();
      }
   }

   private boolean isMultiFinder(Method var1) {
      return Collection.class.isAssignableFrom(var1.getReturnType()) || Enumeration.class.isAssignableFrom(var1.getReturnType());
   }

   public String finderMethodName() {
      assert this.finderMethodInfo != null;

      return this.finderMethodInfo.method.getName();
   }

   public String finderQuery() {
      if (debugLogger.isDebugEnabled()) {
         debug("finderQuery() called for method " + this.finderMethodInfo.method);
      }

      assert this.finderMethodInfo != null;

      assert this.finderMethodInfo.finder != null;

      assert this.finderMethodInfo.finder.getSQLQuery() != null;

      String var1 = this.finderMethodInfo.finder.getSQLQuery();
      if (debugLogger.isDebugEnabled()) {
         debug("finder query is: " + var1);
      }

      return var1;
   }

   public String declareResultVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.finderMethodInfo.loadBean) {
         var1.append(this.declareBeanVar());
         var1.append(this.declareEoVar());
      } else {
         var1.append(this.lvl(2) + "Object " + this.pkVar() + " = null;");
      }

      return var1.toString();
   }

   public String allocateResultVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.finderMethodInfo.loadBean) {
         var1.append(this.getBeanFromRS());
      }

      return var1.toString();
   }

   public String finderGetEo() {
      StringBuffer var1 = new StringBuffer();
      if (this.finderMethodInfo.loadBean) {
         var1.append(this.eoVar() + " = " + this.pmVar() + ".finderGetEoFromBeanOrPk(" + this.beanVar() + ", " + this.pkVar() + ", __WL_getIsLocal());");
      }

      return var1.toString();
   }

   public String resultVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.finderMethodInfo.loadBean) {
         var1.append(this.eoVar());
      } else {
         var1.append(this.pkVar());
      }

      return var1.toString();
   }

   public String declareEoVar() {
      return "Object " + this.eoVar() + " = null;" + EOL;
   }

   public String getGeneratedBeanClassName() {
      return this.bd.getGeneratedBeanClassName();
   }

   public String declareBeanVar() {
      return this.lvl(2) + this.bd.getGeneratedBeanClassName() + " " + this.beanVar() + " = null;" + EOL;
   }

   public String declarePkVar() {
      return this.lvl(2) + this.pk_class() + " " + this.pkVar() + " = null;";
   }

   public String allocatePkVar() {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      if (var3.hasComplexPrimaryKey()) {
         var1.append(this.lvl(2) + this.pkVar() + " = ");
         var1.append("new " + this.pk_class() + "();");
      }

      return var1.toString();
   }

   public String allocateBeanVar() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      return this.lvl(2) + this.beanVar() + " = (" + var2.getGeneratedBeanClassName() + ")" + this.pmVar() + ".getBeanFromPool();" + EOL;
   }

   public String getBeanFromRS() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      StringBuffer var3 = new StringBuffer();
      var3.append("RSInfo " + this.rsInfoVar() + " = new RSInfoImpl(" + this.rsVar() + ", 0, 0, " + this.pkVar() + ");" + EOL);
      var3.append(this.beanVar() + " = (" + var2.getGeneratedBeanClassName() + ")" + this.pmVar() + ".getBeanFromRS(" + this.pkVar() + ", " + this.rsInfoVar() + ");" + EOL);
      return var3.toString();
   }

   public String getPkVarFromRS() {
      StringBuffer var1 = new StringBuffer();
      if (this.finderMethodInfo.loadBean) {
         var1.append("Object ");
      }

      var1.append(this.pkVar() + " = " + this.getPKFromRSMethodName() + this.getPKFromRSMethodParams() + EOL);
      return var1.toString();
   }

   public String implementGetPKFromRSStaticMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      var1.append("public static Object " + this.getPKFromRSMethodName() + "(java.sql.ResultSet " + this.rsVar() + ") " + EOL);
      var1.append("throws java.sql.SQLException, java.lang.Exception" + EOL);
      var1.append("{" + EOL);
      var1.append(this.declarePkVar() + EOL);
      var1.append(this.allocatePkVar() + EOL + EOL);
      this.assignToFields(this.primaryKeyFieldList, var1, 1, this.pkVar(), this.bd.hasComplexPrimaryKey());
      var1.append("return " + this.pkVar() + ";" + EOL);
      var1.append("}" + EOL);
      return var1.toString();
   }

   public String getPKFromRSMethodName() {
      return varPrefix() + "getPKFromRS";
   }

   private String getPKFromRSMethodParams() {
      return "(" + this.rsVar() + ");";
   }

   public String finderColumnsSql() throws EJBCException {
      StringBuffer var1 = new StringBuffer();
      List var2 = null;
      if (this.finderMethodInfo.loadBean) {
         var2 = this.fieldList;
      } else {
         var2 = this.primaryKeyFieldList;
      }

      Iterator var3 = var2.iterator();

      for(int var4 = 0; var3.hasNext(); ++var4) {
         String var5 = (String)var3.next();
         String var6 = this.beanData.getColumnForField(var5);

         assert var6 != null;

         var1.append(var6);
         if (var4 < var2.size() - 1) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   public String implementGetPrimaryKey() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.lvl(2) + this.declarePkVar() + EOL);
      var1.append(this.lvl() + this.allocatePkVar() + EOL);
      var1.append(this.lvl() + this.assignPkFieldsToPkVar() + EOL);
      var1.append(this.lvl() + "return " + this.pkVar() + ";" + EOL);
      return var1.toString();
   }

   public String implementSetPrimaryKey() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.assignPkVarToPkFields() + EOL);
      return var1.toString();
   }

   public String assignPkFieldsToPkVar() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.primaryKeyFieldList.iterator();
      if (this.bd.hasComplexPrimaryKey()) {
         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.lvl(3) + this.pkVar() + "." + var3 + " = " + "this." + var3 + ";");
         }
      } else {
         var1.append(this.lvl(3) + this.pkVar() + " = " + "this." + (String)var2.next() + ";");
      }

      return var1.toString();
   }

   public String assignPkVarToPkFields() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bd.getPrimaryKeyFieldNames().iterator();
      if (this.bd.hasComplexPrimaryKey()) {
         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append("this." + var3 + " = " + this.pkVar() + "." + var3 + ";" + EOL);
         }
      } else {
         var1.append("this." + (String)var2.next() + " = " + this.pkVar() + ";");
      }

      return var1.toString();
   }

   public boolean isFindByPrimaryKey(Finder var1) {
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      if (!var1.getName().equals("findByPrimaryKey")) {
         return false;
      } else {
         Iterator var4 = var1.getParameterTypes();
         if (!var4.hasNext()) {
            return false;
         } else {
            String var5 = (String)var4.next();
            if (!var5.equals(var3.getPrimaryKeyClass().getName())) {
               return false;
            } else {
               return !var4.hasNext();
            }
         }
      }
   }

   public String setFinderQueryParams() throws EJBCException {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      Method var4 = this.finderMethodInfo.method;
      Finder var5 = this.beanData.getFinderForMethod(var4);

      assert var5 != null;

      assert var5.getSQLQuery() != null;

      Class[] var6 = null;
      String[] var7 = null;
      boolean var8 = this.isFindByPrimaryKey(var5);
      int var9;
      int var10;
      if (var8 && var3.hasComplexPrimaryKey()) {
         String[] var12 = (String[])((String[])this.primaryKeyFieldList.toArray(new String[0]));
         var6 = new Class[var12.length];
         var7 = new String[var12.length];

         for(var10 = 0; var10 < var6.length; ++var10) {
            var6[var10] = var3.getFieldClass(var12[var10]);
            var7[var10] = MethodUtils.getParameterName(0) + "." + var12[var10];
         }
      } else {
         var6 = var4.getParameterTypes();
         var7 = new String[var6.length];

         for(var9 = 0; var9 < var7.length; ++var9) {
            var7[var9] = MethodUtils.getParameterName(var9);
         }
      }

      for(var9 = 1; var9 <= var5.getVariableCount(); ++var9) {
         var10 = var5.getParameterIndex(var9);
         if (!var6[var10].isPrimitive()) {
            this.addNullCheck("", var1, var7[var10], TypeUtils.getSQLTypeForClass(var6[var10]), var9);
         }

         Object[] var11 = new Object[]{TypeUtils.getPreparedStatementMethodPostfix(var6[var10]), new Integer(var9), this.getParameterSetterName(var6[var10], var7[var10])};
         Sprintf.sprintf(this.stmtVar() + ".set%s(%d, %s);" + EOL, var11, var1);
      }

      return var1.toString();
   }

   private void addNullCheck(String var1, StringBuffer var2, String var3, String var4, String var5) {
      var2.append(var1 + "if(!" + this.pmVar() + ".setParamNull(" + this.stmtVar() + ", " + var5 + ", " + var3 + ", " + "\"" + var4 + "\"" + ")) {" + EOL);
   }

   private void addNullCheck(String var1, StringBuffer var2, String var3, int var4, int var5) {
      var2.append("");
      var2.append("if (" + var3 + " == null) {" + EOL);
      var2.append("  " + this.stmtVar() + ".setNull(" + var5 + "," + var4 + ");" + EOL);
      var2.append("} else " + EOL);
   }

   public String assignResultVar() {
      StringBuffer var1 = new StringBuffer();
      this.assignToFields(this.fieldList, var1, 1, this.beanVar(), true, true);
      return var1.toString();
   }

   private void assignToFields(List var1, StringBuffer var2, int var3, String var4, boolean var5) {
      this.assignToFields(var1, var2, var3, var4, var5, false);
   }

   private void assignToFields(List var1, StringBuffer var2, int var3, String var4, boolean var5, boolean var6) {
      CMPCodeGenerator.Output var7 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var8 = var7.getCMPBeanDescriptor();
      Iterator var9 = var1.iterator();
      int var10 = var1.size();

      while(var9.hasNext()) {
         String var11 = (String)var9.next();
         String var12 = this.convertToField(var11, var3++);
         Class var13 = var8.getFieldClass(var11);
         String var14 = null;
         if (var6 && this.primaryKeyFieldList.contains(var11)) {
            var2.append("if (" + this.pkVar() + " == null) {" + EOL);
         }

         if (var5) {
            var14 = var4 + "." + var11;
         } else {
            var14 = var4;
         }

         if (var13 != Character.class && var13 != Character.TYPE) {
            if (var13 == Date.class) {
               var2.append("java.sql.Timestamp " + this.sqlTimestampVar(var11) + " = " + var12 + ";" + EOL);
               var2.append("  if (" + this.rsVar() + ".wasNull() || " + this.sqlTimestampVar(var11) + "==null) { " + EOL);
               var2.append("    " + var14 + " = null");
               var2.append(";" + EOL);
               var2.append("  }" + EOL);
               var2.append("  else { " + EOL);
               var2.append("    " + var14 + " = ");
               var2.append("new java.util.Date(" + this.sqlTimestampVar(var11) + ".getTime());" + EOL);
               var2.append("  }" + EOL);
            } else if (!ClassUtils.isValidSQLType(var13)) {
               var2.append(this.lvl() + "byte[] " + this.byteArrayVar(var11) + " = " + var12 + ";" + EOL);
               var2.append(this.lvl() + "if (" + this.debugEnabled() + ") {" + EOL);
               var2.append(this.upLvl() + this.debugSay() + "(\"returned bytes\" + " + this.byteArrayVar(var11) + ");" + EOL);
               var2.append(this.lvl() + "if (" + this.byteArrayVar(var11) + "!=null) {" + EOL);
               var2.append(this.upLvl() + this.debugSay() + "(\"length- \" + " + this.byteArrayVar(var11) + ".length);" + EOL);
               var2.append(this.dnLvl() + "}" + EOL);
               var2.append(this.dnLvl() + "}" + EOL);
               var2.append(this.lvl() + "if (" + this.rsVar() + ".wasNull() || " + this.byteArrayVar(var11) + " ==null || " + this.byteArrayVar(var11) + ".length==0) { " + EOL);
               var2.append(this.upLvl() + var14 + " = null;" + EOL);
               var2.append(this.dnLvl() + "}" + EOL);
               String var15 = "(" + this.javaCodeForType(var13) + ")";
               var2.append(this.lvl() + "else { " + EOL);
               var2.append(this.upLvl() + "ByteArrayInputStream bstr = " + "new java.io.ByteArrayInputStream(" + this.byteArrayVar(var11) + ");" + EOL);
               var2.append(this.lvl() + "RDBMSObjectInputStream ostr = " + "new RDBMSObjectInputStream(bstr, " + this.pmVar() + ".getClassLoader()" + ");" + EOL);
               var2.append(this.lvl() + var14 + " = " + var15 + "ostr.readObject();" + EOL);
               var2.append(this.dnLvl() + "}" + EOL);
            } else {
               var2.append(var14 + " = " + var12 + ";" + EOL);
            }
         } else {
            var2.append("String " + this.stringVar(var11) + " = " + var12 + ";" + EOL);
            var2.append("  if (" + this.rsVar() + ".wasNull() || " + this.stringVar(var11) + "==null || " + this.stringVar(var11) + ".length()==0) { " + EOL);
            var2.append("    " + var14 + " = ");
            if (var13 == Character.class) {
               var2.append("null");
            } else {
               var2.append("'\\u0000'");
            }

            var2.append(";" + EOL);
            var2.append("  }" + EOL);
            var2.append("  else { " + EOL);
            var2.append("    " + var14 + " = ");
            if (var13 == Character.class) {
               var2.append("new Character(" + this.stringVar(var11) + ".charAt(0));" + EOL);
            } else {
               var2.append(this.stringVar(var11) + ".charAt(0);" + EOL);
            }

            var2.append("  }" + EOL);
         }

         if (!var13.isPrimitive() && var13 != Character.class && var13 != Date.class && ClassUtils.isValidSQLType(var13)) {
            var2.append("  if (").append(this.rsVar()).append(".wasNull()) { ");
            var2.append(var14 + " = null; }").append(EOL);
         }

         if (var6 && this.primaryKeyFieldList.contains(var11)) {
            var2.append("} else {" + EOL);
            var2.append(this.beanVar() + ".__WL_setPrimaryKey((" + this.pk_class() + ") " + this.pkVar() + ");" + EOL);
            var2.append("}" + EOL);
         }
      }

   }

   private String convertToField(String var1, int var2) {
      CMPCodeGenerator.Output var3 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var4 = var3.getCMPBeanDescriptor();
      Class var5 = var4.getFieldClass(var1);
      String var6 = "(" + this.javaCodeForType(var5) + ")";
      String var7 = this.getFromResultSet(var2, var5);
      if (var5 == Boolean.class) {
         return "new Boolean(" + var7 + ")";
      } else if (var5 == Byte.class) {
         return "new Byte(" + var7 + ")";
      } else if (var5 != Character.class && var5 != Character.TYPE) {
         if (var5 == Double.class) {
            return "new Double(" + var7 + ")";
         } else if (var5 == Float.class) {
            return "new Float(" + var7 + ")";
         } else if (var5 == Integer.class) {
            return "new Integer(" + var7 + ")";
         } else if (var5 == Long.class) {
            return "new Long(" + var7 + ")";
         } else if (var5 == Short.class) {
            return "new Short(" + var7 + ")";
         } else if (var5 == Date.class) {
            var6 = "(java.sql.Timestamp)";
            return var6 + var7;
         } else {
            return !ClassUtils.isValidSQLType(var5) ? var7 : var6 + var7;
         }
      } else {
         return "(java.lang.String)" + var7;
      }
   }

   private String getFromResultSet(int var1, Class var2) {
      String var3 = null;
      if (var2.equals(BigDecimal.class)) {
         var3 = this.rsVar() + ".get" + TypeUtils.getResultSetMethodPostfix(var2) + "(" + var1 + ")";
      } else {
         var3 = this.rsVar() + ".get" + TypeUtils.getResultSetMethodPostfix(var2) + "(" + var1 + ")";
      }

      return var3;
   }

   public String result_set_to_collection_class() {
      assert this.finderMethodInfo != null;

      assert this.finderMethodInfo.method != null;

      StringBuffer var1 = new StringBuffer();
      var1.append("resultSetToCollection(" + this.rsVar() + ", " + "\"" + this.finderMethodInfo.method.getName() + "\");");
      return var1.toString();
   }

   public String beanIsUpdateable() {
      return this.fieldList.size() > this.primaryKeyFieldList.size() ? "true" : "false";
   }

   public String updateBeanColumnsSql() {
      String[] var1 = (String[])((String[])this.nonPrimaryKeyFieldList.toArray(new String[0]));
      return this.attrsAsColumnsAsParams(var1, ", ");
   }

   public String idParamsSql() {
      String[] var1 = (String[])((String[])this.primaryKeyFieldList.toArray(new String[0]));

      assert var1 != null;

      return this.attrsAsColumnsAsParams(var1, " AND ");
   }

   public String idColumnsSql() {
      String[] var1 = (String[])((String[])this.primaryKeyFieldList.toArray(new String[0]));

      assert var1 != null;

      return StringUtils.join(var1, ", ");
   }

   public String allColumnsSql() {
      String[] var1 = (String[])((String[])this.fieldList.toArray(new String[0]));
      StringBuffer var2 = new StringBuffer();
      int var3 = 0;

      for(int var4 = var1.length; var3 < var4; ++var3) {
         String var5 = var1[var3];
         String var6 = this.beanData.getColumnForField(var5);

         assert var6 != null;

         var2.append(var6);
         if (var3 < var4 - 1) {
            var2.append(", ");
         }
      }

      return var2.toString();
   }

   public String allColumnsQMs() {
      String[] var1 = (String[])((String[])this.fieldList.toArray(new String[0]));
      int var2 = var1.length;
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.append("?");
         if (var4 < var2 - 1) {
            var3.append(", ");
         }
      }

      return var3.toString();
   }

   public String copyKeyValuesToPkVar() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      String[] var3 = (String[])((String[])this.primaryKeyFieldList.toArray(new String[0]));
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;

      for(int var6 = var3.length; var5 < var6; ++var5) {
         String var7 = var3[var5];
         if (ClassUtils.isObjectPrimitive(var2.getPrimaryKeyClass())) {
            assert var5 == 0 : "Too many fields for an object primitive PK class";

            Class var8 = var2.getFieldClass(var3[var5]);
            var4.append(this.pkVar());
            if (var8.getName().equals(var2.getPrimaryKeyClass().getName())) {
               var4.append(" = ");
               var4.append("this").append(".").append(var3[var5]).append(";");
            } else {
               var4.append(" = new ");
               var4.append(this.primaryKeyClass.getName());
               var4.append("(");
               var4.append(");");
            }
         } else {
            if (var2.hasComplexPrimaryKey()) {
               var4.append(this.pkVar()).append(".").append(var7);
            } else {
               var4.append(this.pkVar());
            }

            var4.append(" = ").append("this").append(".").append(var7).append(";");
         }

         var4.append(EOL);
      }

      return var4.toString();
   }

   public String perhaps_include_result_set_to_collection() throws CodeGenerationException {
      boolean var1 = true;

      try {
         Class var2 = Class.forName("java.util.Collection");
         var1 = true;
      } catch (ClassNotFoundException var3) {
         var1 = false;
      }

      StringBuffer var4 = new StringBuffer();
      if (var1) {
         var4.append(this.parse(this.getProductionRule("resultSetToCollection")));
      }

      return var4.toString();
   }

   public String cm_bean_field_copy() {
      return this.cm_field_to_field_assign(this.ejbClass, "src.", "dest.");
   }

   public String cm_field_to_field_assign(Class var1, String var2, String var3) {
      StringBuffer var4 = new StringBuffer(100);
      Field[] var5 = (Field[])((Field[])this.fieldList.toArray(new Field[0]));

      for(int var6 = 0; var6 < var5.length; ++var6) {
         Field var7 = var5[var6];
         var4.append(var3 + var7.getName() + " = " + var2 + var7.getName() + ";" + EOL);
      }

      return var4.toString();
   }

   public String implementStoreUtilities() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      String[] var3 = (String[])((String[])this.fieldList.toArray(new String[0]));
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4.append("private void setParam" + var3[var5]);
         var4.append("(PreparedStatement " + this.stmtVar() + ", ");
         var4.append("int " + this.numVar() + ", ");
         var4.append(var2.getFieldClass(var3[var5]).getName() + " ");
         var4.append(var3[var5] + ") {" + EOL);
         this.addPreparedStatementBinding("", var4, var3[var5], var3[var5], this.numVar(), false, var2.getFieldClass(var3[var5]));
         var4.append(EOL);
         var4.append("}" + EOL + EOL);
      }

      return var4.toString();
   }

   private String lvl() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.level; ++var2) {
         var1.append("  ");
      }

      return var1.toString();
   }

   private String lvl(int var1) {
      this.level = var1;
      return this.lvl();
   }

   private String upLvl() {
      ++this.level;
      return this.lvl();
   }

   private String dnLvl() {
      --this.level;
      return this.lvl();
   }

   private String attrsAsColumnsAsParams(String[] var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      for(int var5 = var1.length; var4 < var5; ++var4) {
         String var6 = this.beanData.getColumnForField(var1[var4]);
         var3.append(var6).append(" = ?");
         if (var4 < var5 - 1) {
            var3.append(var2);
         }
      }

      return var3.toString();
   }

   private Field nameToField(String var1) throws NoSuchFieldException {
      return this.ejbClass.getField(var1);
   }

   public String resetParams() {
      this.preparedStatementParamIndex = 1;
      return "// preparedStatementParamIndex reset.";
   }

   public String setBeanParams() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      String[] var3 = (String[])((String[])this.fieldList.toArray(new String[0]));
      return this.preparedStatementBindings(var3, "this", true, var2.getBeanClass(), false);
   }

   public String setPrimaryKeyParams() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      String[] var3 = (String[])((String[])this.primaryKeyFieldList.toArray(new String[0]));
      return this.preparedStatementBindings(var3, this.pkVar(), var2.hasComplexPrimaryKey(), var2.getPrimaryKeyClass(), false);
   }

   public String setPrimaryKeyParamsUsingNum() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      String[] var3 = (String[])((String[])this.primaryKeyFieldList.toArray(new String[0]));
      return this.preparedStatementBindings(var3, this.pkVar(), var2.hasComplexPrimaryKey(), var2.getPrimaryKeyClass(), true);
   }

   private String preparedStatementBindings(String[] var1, String var2, boolean var3, Class var4, boolean var5) {
      StringBuffer var6 = new StringBuffer();
      int var7 = 0;

      for(int var8 = var1.length; var7 < var8; ++var7) {
         String var9 = var1[var7];
         String var10;
         if (var5) {
            var10 = this.numVar();
         } else {
            var10 = String.valueOf(this.preparedStatementParamIndex);
         }

         this.addPreparedStatementBinding("", var6, var9, var2, var10, var3, var4);
         if (var5) {
            var6.append(this.numVar() + "++;" + EOL);
         }

         ++this.preparedStatementParamIndex;
         if (var7 < var8 - 1) {
            var6.append(EOL);
         }
      }

      return var6.toString();
   }

   private void addPreparedStatementBinding(String var1, StringBuffer var2, String var3, String var4, String var5, boolean var6, Class var7) {
      if (debugLogger.isDebugEnabled()) {
         debug("Adding a prepared statement binding: ");
         debug("\t\tfield = " + var3);
         debug("\t\tobj = " + var4);
         debug("\t\tparamIdx = " + var5);
         debug("\t\tobjIsCompound = " + var6);
         debug("\t\tobjectType = " + var7);
      }

      CMPCodeGenerator.Output var8 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var9 = var8.getCMPBeanDescriptor();
      Class var10 = var9.getFieldClass(var3);
      String var11 = StatementBinder.getStatementTypeNameForClass(var10);
      String var12 = var4;
      if (!var10.isPrimitive()) {
         if (var6) {
            var12 = var4 + "." + var3;
         }

         this.addNullCheck(var1, var2, var12, var3, var5);
      }

      if (!ClassUtils.isValidSQLType(var10)) {
         var2.append(this.upLvl() + "ByteArrayOutputStream bstr = new ByteArrayOutputStream();" + EOL);
         var2.append(this.lvl() + "ObjectOutputStream ostr = new ObjectOutputStream(bstr);" + EOL);
         var2.append(this.lvl() + "ostr.writeObject(" + var12 + ");" + EOL);
         var2.append(this.lvl() + "byte[] byteArray = bstr.toByteArray();" + EOL);
         var2.append(this.lvl() + "if (" + this.debugEnabled() + ") {" + EOL);
         var2.append(this.upLvl() + this.debugSay() + "(\"writing bytes: \" + byteArray);" + EOL);
         var2.append(this.lvl() + "if (byteArray!=null) {" + EOL);
         var2.append(this.upLvl() + this.debugSay() + "(\"bytes length: \" + byteArray.length);" + EOL);
         var2.append(this.dnLvl() + "}" + EOL);
         var2.append(this.dnLvl() + "}" + EOL);
         var2.append(this.lvl() + "InputStream inputStream " + " = new ByteArrayInputStream(byteArray);" + EOL);
         var2.append(this.lvl() + this.stmtVar() + ".setBinaryStream(" + var5 + ", inputStream, byteArray.length);" + EOL);
      } else if (ClassUtils.isByteArray(var10)) {
         var2.append(this.lvl() + "InputStream inputStream " + " = new ByteArrayInputStream(" + var12 + ");" + EOL);
         var2.append(this.lvl() + this.stmtVar() + ".setBinaryStream(" + var5 + ", inputStream, " + var12 + ".length);" + EOL);
      } else {
         var2.append(var1 + "\t" + this.stmtVar());
         var2.append(".set" + var11 + "(");
         var2.append(var5).append(", ");
         if (var6) {
            if (var10 == Character.TYPE) {
               var2.append("String.valueOf(" + var4 + "." + var3 + ")");
            } else if (var10 == Character.class) {
               var2.append("String.valueOf(" + var4 + "." + var3 + ".charValue())");
            } else if (var10 == Date.class) {
               var2.append("new java.sql.Timestamp(");
               var2.append(var4).append(".").append(this.getParameterSetterName(var10, var3));
               var2.append(".getTime())");
            } else {
               var2.append(var4).append(".").append(this.getParameterSetterName(var10, var3));
            }
         } else if (ClassUtils.isObjectPrimitive(var7)) {
            if (var7 == Character.class) {
               var2.append("String.valueOf(" + var4 + ".charValue())");
            } else {
               var2.append(this.getParameterSetterName(var7, var4));
            }

            if (debugLogger.isDebugEnabled()) {
               debug("\tThis type IS an object primitive.");
            }
         } else {
            if (var7 == Character.TYPE) {
               var2.append("String.valueOf(" + var4 + ")");
            } else if (var10 == Date.class) {
               var2.append("new java.sql.Timestamp(");
               var2.append(var4);
               var2.append(".getTime())");
            } else {
               var2.append(var4);
            }

            if (debugLogger.isDebugEnabled()) {
               debug("\tThis type is not an object primitive.");
            }
         }

         var2.append(");" + EOL);
      }

      var2.append(this.lvl() + "if (" + this.debugEnabled() + ") {" + EOL);
      var2.append(this.upLvl() + this.debugSay() + "(\"paramIdx :\"+" + var5 + "+\" binded with value :\"+" + var12 + ");" + EOL);
      var2.append(this.dnLvl() + "}" + EOL);
      if (!var10.isPrimitive()) {
         var2.append(var1 + "}" + EOL);
      }

   }

   private String getParameterSetterName(Class var1, String var2) {
      if (var1 == Boolean.class) {
         return var2 + ".booleanValue()";
      } else if (var1 == Byte.class) {
         return var2 + ".byteValue()";
      } else if (var1 == Character.class) {
         return var2 + ".charValue()";
      } else if (var1 == Double.class) {
         return var2 + ".doubleValue()";
      } else if (var1 == Float.class) {
         return var2 + ".floatValue()";
      } else if (var1 == Integer.class) {
         return var2 + ".intValue()";
      } else if (var1 == Long.class) {
         return var2 + ".longValue()";
      } else if (var1 == Short.class) {
         return var2 + ".shortValue()";
      } else {
         return var1 == Character.TYPE ? "String.valueOf(" + var2 + ")" : var2;
      }
   }

   public String setUpdateBeanParams() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      String[] var3 = (String[])((String[])this.nonPrimaryKeyFieldList.toArray(new String[0]));
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4.append("\t\tif((! __WL_snapshots_enabled) || __WL_modified[" + var5 + "]) {" + EOL);
         this.addPreparedStatementBinding("", var4, var3[var5], var3[var5], this.numVar(), false, var2.getFieldClass(var3[var5]));
         var4.append("\t\t" + this.numVar() + "++;" + EOL);
         var4.append("__WL_modified[" + var5 + "] = false;" + EOL);
         var4.append("}" + EOL);
      }

      return var4.toString();
   }

   public String assignAllColumnsToBean() {
      StringBuffer var1 = new StringBuffer();
      this.assignToFields(this.fieldList, var1, 1, "this", true);
      return var1.toString();
   }

   public String refresh_bean_from_key() {
      return this.isContainerManagedBean ? "loadByPrimaryKey(ctx);" : "((" + this.ejbClass.getName() + ")(ctx.getBean())).ejbFindByPrimaryKey(pk);";
   }

   public String snapshots_enabled() {
      this.useTunedUpdates = this.beanData.useTunedUpdates();
      Iterator var1 = this.fieldList.iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         Class var3 = this.getBeanField(var2).getType();
         if (!ClassUtils.isValidSQLType(var3)) {
            this.useTunedUpdates = false;
            break;
         }
      }

      this.initializeSnapshotArray();
      return "" + this.useTunedUpdates;
   }

   private Field getBeanField(String var1) {
      try {
         return this.ejbClass.getField(var1);
      } catch (NoSuchFieldException var3) {
         EJBLogger.logStackTrace(var3);
         throw new AssertionError(var3);
      }
   }

   private void initializeSnapshotArray() {
      int var1 = this.nonPrimaryKeyFieldList.size();
      String[] var2 = new String[this.nonPrimaryKeyFieldList.size()];
      var2 = (String[])((String[])this.nonPrimaryKeyFieldList.toArray(var2));
      this.snapshotFieldInfo = new SnapshotFieldInfo[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         Class var4 = this.getBeanField(var2[var3]).getType();
         this.snapshotFieldInfo[var3] = new SnapshotFieldInfo(var4, var2[var3], this.getSnapFieldTypeForClass(var4), "__WL_snap_" + var2[var3]);
      }

   }

   public String modified_array_count() {
      return "" + this.nonPrimaryKeyFieldList.size();
   }

   private boolean isImmutableType(Class var1) {
      for(int var2 = 0; var2 < this.immutableClasses.length; ++var2) {
         if (var1.equals(this.immutableClasses[var2])) {
            return true;
         }
      }

      return false;
   }

   private Class getSnapFieldTypeForClass(Class var1) {
      if (!var1.isPrimitive() && !this.isImmutableType(var1)) {
         if (Date.class.isAssignableFrom(var1)) {
            return Long.class;
         } else {
            return byte[].class.equals(var1) ? var1 : var1;
         }
      } else {
         return var1;
      }
   }

   private String getSnapAssignmentCode(String var1, Class var2) {
      return Date.class.isAssignableFrom(var2) ? "((" + var1 + "==null)?null:new Long(" + var1 + ".getTime()))" : var1;
   }

   public String declare_snapshot_variables() {
      StringBuffer var1 = new StringBuffer(200);

      for(int var2 = 0; var2 < this.snapshotFieldInfo.length; ++var2) {
         SnapshotFieldInfo var3 = this.snapshotFieldInfo[var2];
         String var4;
         if (byte[].class.equals(var3.snapFieldType)) {
            var4 = "byte []";
         } else if (String[].class.equals(var3.snapFieldType)) {
            var4 = "String []";
         } else if (char[].class.equals(var3.snapFieldType)) {
            var4 = "char []";
         } else {
            var4 = var3.snapFieldType.getName();
         }

         var1.append("private " + var4 + " " + var3.snapFieldName + ";" + EOL);
      }

      return var1.toString();
   }

   public String clear_snapshot_variables() {
      StringBuffer var1 = new StringBuffer(200);

      for(int var2 = 0; var2 < this.snapshotFieldInfo.length; ++var2) {
         SnapshotFieldInfo var3 = this.snapshotFieldInfo[var2];
         if (!var3.snapFieldType.isPrimitive()) {
            var1.append("\t\t" + var3.snapFieldName + " = null;" + EOL);
         }
      }

      return var1.toString();
   }

   public String take_snapshot_variables() {
      StringBuffer var1 = new StringBuffer(200);

      for(int var2 = 0; var2 < this.snapshotFieldInfo.length; ++var2) {
         SnapshotFieldInfo var3 = this.snapshotFieldInfo[var2];
         if (byte[].class.equals(var3.beanFieldType)) {
            var1.append(var3.snapFieldName + " = " + "__WL_snapshot_byte_array(" + var3.beanFieldName + ");" + EOL);
         } else {
            var1.append(var3.snapFieldName + " = " + this.getSnapAssignmentCode(var3.beanFieldName, var3.beanFieldType) + ";" + EOL);
         }
      }

      return var1.toString();
   }

   public String modified_field_index() {
      return "" + this.current_index;
   }

   public String snapshot_field() {
      return this.current.snapFieldName;
   }

   public String modified_field() {
      return this.current.beanFieldName;
   }

   public String modified_column_name() {
      return this.beanData.getColumnForField(this.current.beanFieldName);
   }

   public String determineSetString() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer(200);

      for(int var2 = 0; var2 < this.snapshotFieldInfo.length; ++var2) {
         this.current_index = var2;
         this.current = this.snapshotFieldInfo[var2];
         if (Date.class.isAssignableFrom(this.current.beanFieldType)) {
            var1.append(this.parse(this.getProductionRule("check_for_date_modified_field")));
         } else if (byte[].class.isAssignableFrom(this.current.beanFieldType)) {
            var1.append(this.parse(this.getProductionRule("check_for_bytea_modified_field")));
         } else {
            var1.append(this.parse(this.getProductionRule("check_for_simple_modified_field")));
         }
      }

      return var1.toString();
   }

   public String home_methods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer(200);
      if (this.homeMethods != null) {
         for(int var2 = 0; var2 < this.homeMethods.length; ++var2) {
            try {
               String var3 = null;
               var3 = this.homeToBeanName("ejbHome", this.homeMethods[var2].getName());
               Method var4 = this.ejbClass.getMethod(var3, this.homeMethods[var2].getParameterTypes());
               this.setMethod(var4, (short)0);
               var1.append(this.parse(this.getProductionRule("business_method")));
            } catch (NoSuchMethodException var5) {
               throw new AssertionError(var5);
            }
         }
      }

      return var1.toString();
   }

   public String declare_bean_interface_methods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.declare_bean_interface_methods());
      Iterator var2 = this.finderList.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         String var4 = MethodUtils.getFinderMethodDeclaration(var3, this.bd.getPrimaryKeyClass(), true);
         var1.append(var4);
         var1.append(";");
         var1.append(EOL);
      }

      return var1.toString();
   }

   private String homeToBeanName(String var1, String var2) {
      StringBuffer var3 = new StringBuffer(var1 + var2);
      var3.setCharAt(var1.length(), Character.toUpperCase(var3.charAt(var1.length())));
      return var3.toString();
   }

   private static void debug(String var0) {
      debugLogger.debug("[RDBMSCodeGenerator] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }

   private class FinderMethodInfo {
      public Method method;
      public Finder finder;
      public boolean loadBean;

      private FinderMethodInfo() {
      }

      // $FF: synthetic method
      FinderMethodInfo(Object var2) {
         this();
      }
   }

   private static class SnapshotFieldInfo {
      private Class beanFieldType;
      private String beanFieldName;
      private Class snapFieldType;
      private String snapFieldName;

      public SnapshotFieldInfo(Class var1, String var2, Class var3, String var4) {
         this.beanFieldType = var1;
         this.beanFieldName = var2;
         this.snapFieldType = var3;
         this.snapFieldName = var4;
      }
   }
}
