package weblogic.ejb.container.cmp.rdbms.codegen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.EntityContext;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.FieldGroup;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.cmp.rdbms.RelationshipCaching;
import weblogic.ejb.container.cmp.rdbms.finders.EjbqlFinder;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.cmp.rdbms.finders.ParamNode;
import weblogic.ejb.container.cmp.rdbms.finders.SqlFinder;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.ejbc.codegen.MethodSignature;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.logging.Loggable;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public final class RDBMSCodeGenerator extends BaseCodeGenerator {
   private RDBMSBean bean = null;
   private List cmpFieldNames = null;
   private List cmrFieldNames = null;
   private List pkFieldNames = null;
   private Map variableToClass = null;
   private Map variableToField = null;
   private List finderList = null;
   private Map beanMap = null;
   private List ejbSelectInternalList = null;
   private Map ejbSelectBeanTargetMap = null;
   private Map declaredManagerVars = new HashMap();
   private Finder curFinder = null;
   private String curField = null;
   private String curTableName = null;
   private int curTableIndex = 0;
   private FieldGroup curGroup = null;
   private String curTable = null;
   private String curSQL = null;
   private RelationshipCaching curRelationshipCaching = null;
   private int preparedStatementParamIndex = -1;
   private Map parameterMap = null;
   private char[] illegalJavaCharacters = new char[]{'@', '.', '-'};
   private boolean currFinderLoadsQueryCachingEnabledCMRFields = false;
   private Getopt2 options;
   private String currCachingElementCmrField = null;

   public RDBMSCodeGenerator(Getopt2 var1) {
      super(var1);
      this.options = var1;
   }

   public void setRDBMSBean(RDBMSBean var1) {
      assert var1 != null;

      assert this.bd != null;

      this.bean = var1;
      this.variableToClass = new HashMap();
      this.variableToField = new HashMap();
      this.cmpFieldNames = var1.getCmpFieldNames();
      this.cmrFieldNames = var1.getCmrFieldNames();
      Iterator var2 = this.cmpFieldNames.iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         this.variableToClass.put(var3, var1.getCmpFieldClass(var3));
         this.variableToField.put(var3, var3);
      }

      this.pkFieldNames = new ArrayList(this.bd.getPrimaryKeyFieldNames());
      var2 = var1.getForeignKeyFieldNames().iterator();

      while(true) {
         String var4;
         do {
            if (!var2.hasNext()) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Variable to Class Map--------------------");
                  Iterator var9 = this.variableToClass.keySet().iterator();

                  while(var9.hasNext()) {
                     var4 = (String)var9.next();
                     debug("(" + var4 + ", \t" + ((Class)this.variableToClass.get(var4)).getName());
                  }
               }

               this.curTableName = var1.getTableName();
               this.beanMap = var1.getBeanMap();
               return;
            }

            var3 = (String)var2.next();
         } while(!var1.containsFkField(var3));

         var4 = var1.getTableForCmrField(var3);
         Iterator var5 = var1.getForeignKeyColNames(var3).iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (!var1.hasCmpField(var4, var6)) {
               String var7 = var1.variableForField(var3, var4, var6);
               Class var8 = var1.getForeignKeyColClass(var3, var6);
               if (debugLogger.isDebugEnabled()) {
                  debug("fkField: " + var3 + " fkColumn: " + var6 + " fkVar: " + var7 + " fkClass: " + var8.getName());
               }

               this.variableToClass.put(var7, var8);
               this.variableToField.put(var7, var3);
            }
         }
      }
   }

   public void setFinderList(List var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("RDBMSCodeGenerator.setFinderList(" + var1 + ")");
      }

      this.finderList = var1;
      this.ejbSelectBeanTargetMap = new HashMap();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Finder var3 = (Finder)var2.next();
         if (var3.getQueryType() == 4) {
            RDBMSBean var4 = var3.getSelectBeanTarget();
            if (var4 != null) {
               String var5 = var4.getEjbName();
               if (!this.ejbSelectBeanTargetMap.containsKey(var5)) {
                  this.ejbSelectBeanTargetMap.put(var5, var5);
               }
            }
         }
      }

   }

   public void setEjbSelectInternalList(List var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("RDBMSCodeGenerator.setEjbSelectInternalList(" + var1 + ")");
      }

      this.ejbSelectInternalList = var1;
   }

   public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
      super.setCMPBeanDescriptor(var1);
   }

   protected List typeSpecificTemplates() {
      ArrayList var1 = new ArrayList();
      var1.add("weblogic/ejb/container/cmp/rdbms/codegen/bean.j");
      var1.add("weblogic/ejb/container/cmp/rdbms/codegen/relationship.j");
      return var1;
   }

   public void setParameterMap(Map var1) {
      this.parameterMap = var1;
   }

   protected void prepare(CodeGenerator.Output var1) throws EJBCException, ClassNotFoundException {
      super.prepare(var1);

      assert this.bean != null;

      assert this.finderList != null;

      assert this.pkFieldNames != null;

      assert this.parameterMap != null;

      if (debugLogger.isDebugEnabled()) {
         debug("cmp.rdbms.codegen.RDBMSCodeGenerator.prepare() called");
      }

   }

   public String getCategoryValueMethodBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.bean.getCategoryCmpField();
      if (var2 != null) {
         var1.append("return ");
         var1.append(var2);
         var1.append(";");
         return var1.toString();
      } else {
         var1.append("throw new AssertionError(\"Categories not supported!\");");
         return var1.toString();
      }
   }

   public String declareEjbSelectMethodVars() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.finderList.iterator();

      while(var2.hasNext()) {
         Finder var3 = (Finder)var2.next();
         boolean var4 = var3.getQueryType() == 4 || var3.getQueryType() == 2 || var3 instanceof SqlFinder && var3.isSelect();
         if (var4) {
            var1.append("\n  public static java.lang.reflect.Method ");
            var1.append(this.ejbSelectMDName(var3));
            var1.append(";");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   private String ejbSelectMDName(Finder var1) throws CodeGenerationException {
      return MethodUtils.ejbSelectMDName(var1);
   }

   public String implementEjbSelectMethods() throws CodeGenerationException {
      Iterator var1 = this.finderList.iterator();
      StringBuffer var2 = new StringBuffer();

      while(true) {
         while(var1.hasNext()) {
            Finder var3 = (Finder)var1.next();
            int var4 = var3.getQueryType();
            Class var5;
            String var6;
            String var11;
            if (var3.isSelect() && var3 instanceof SqlFinder) {
               var5 = var3.getReturnClassType();
               var6 = this.varPrefix() + "ret";
               List var12 = var3.getExternalMethodParmList();
               var2.append(MethodUtils.getFinderMethodDeclaration(var3, var5.getName(), var3.getName(), var12));
               var2.append("  {" + EOL);
               var2.append("Object " + var6 + " = ");
               var2.append(this.pmVar());
               var2.append(".processSqlFinder (");
               var2.append(this.ejbSelectMDName(var3));
               var2.append(", new Object [] {");
               Iterator var13 = var12.iterator();

               while(var13.hasNext()) {
                  ParamNode var14 = (ParamNode)var13.next();
                  Class var15 = var14.getParamClass();
                  var11 = var14.getParamName();
                  var2.append(this.perhapsConvertPrimitive(var15, var11));
                  if (var13.hasNext()) {
                     var2.append(", ");
                  }
               }

               var2.append("}, ");
               var2.append(var3.hasLocalResultType());
               var2.append(");");
               var2.append(EOL);
               var2.append(EOL);
               var2.append("if (" + var6 + "== null) {" + EOL);
               if (var5.isPrimitive()) {
                  var2.append("throw new javax.ejb.FinderException (\"" + var3.getName() + " cannot return null.  Return type is " + ClassUtils.classToJavaSourceType(var5) + ".\");" + EOL);
               } else {
                  var2.append("return null;" + EOL);
               }

               var2.append("}" + EOL);
               if (var5.isPrimitive()) {
                  var2.append("return ((" + ClassUtils.classToJavaSourceType(ClassUtils.getObjectClass(var5)) + ")" + var6 + ")" + this.lookupConvert(ClassUtils.getObjectClass(var5)) + ";" + EOL);
               } else {
                  var2.append("return (" + ClassUtils.classToJavaSourceType(var5) + ")" + var6 + ";" + EOL);
               }

               var2.append("}" + EOL);
            } else if (var4 != 4 && var4 != 2) {
               if (var4 == 3 || var4 == 5) {
                  var2.append(this.implementEJBSelectField(var3));
               }
            } else {
               var5 = var3.getReturnClassType();
               var6 = this.varPrefix() + "ret";
               String var7 = var3.getSelectBeanTarget().getEjbName();
               String var8 = this.scalarFinder(var3);
               if (var5.equals(Collection.class)) {
                  var8 = this.collectionFinder(var3);
               } else if (var5.equals(Set.class)) {
                  var8 = this.setFinder(var3);
               }

               String var9 = "";
               if (var4 == 4) {
                  var9 = this.bmVar((String)this.declaredManagerVars.get(var7));
               } else if (var4 == 2) {
                  var9 = "((CMPBeanManager) " + this.pmVar() + ".getBeanManager())";
               }

               var2.append("  ");
               List var10 = var3.getExternalMethodParmList();
               var11 = MethodUtils.getFinderMethodDeclaration(var3, var3.getReturnClassType().getName(), var3.getName(), var10);
               var2.append(var11);
               var2.append("  {" + EOL);
               var2.append("    ");
               var2.append(var5.getName());
               var2.append(" " + var6 + " = null;");
               var2.append(EOL);
               if (var3.isSelectInEntity()) {
                  var2.append("Object ").append(this.selectInEntityPKVar()).append(" = ");
                  var2.append(" __WL_ctx.getPrimaryKey();");
                  var2.append(EOL);
               }

               var2.append("try {");
               var2.append("      ");
               var2.append(var6 + " = " + "(" + var5.getName() + ")" + EOL);
               var2.append(var9);
               var2.append(".");
               var2.append(var8 + "(");
               var2.append(this.ejbSelectMDName(var3) + "," + EOL);
               var2.append("new Object[] { ");
               var2.append(this.wrapped_ejbSelect_params(var3.getExternalMethodAndInEntityParmList()));
               var2.append(" } ");
               var2.append(" ); " + EOL);
               var2.append(this.parse(this.getProductionRule("selectCatch")));
               var2.append("return " + var6 + ";" + EOL);
               var2.append("}");
               var2.append(EOL);
            }
         }

         return var2.toString();
      }
   }

   public String implementEjbSelectInternalMethods() throws CodeGenerationException {
      if (this.ejbSelectInternalList == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.ejbSelectInternalList.iterator();

         while(var2.hasNext()) {
            Finder var3 = (Finder)var2.next();

            try {
               if (debugLogger.isDebugEnabled()) {
                  debug("generating ejbSelectInternal Finder: " + var3);
               }

               var1.append(this.implementFinderMethod(var3));
            } catch (EJBCException var6) {
               Loggable var5 = EJBLogger.logCouldNotGenerateFinderLoggable("ejbSelectInternal", var3.toString(), var6.toString());
               throw new CodeGenerationException(var5.getMessage());
            }
         }

         return var1.toString();
      }
   }

   private String implementEJBSelectField(Finder var1) throws CodeGenerationException {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.implementEJBSelectField(" + var1 + ") called.");
      }

      assert this.bd != null;

      this.curFinder = var1;
      if (this.curFinder == null) {
         Loggable var7 = EJBLogger.logNullFinderLoggable("implementEJBSelectField");
         throw new CodeGenerationException(var7.getMessage());
      } else {
         StringBuffer var2 = new StringBuffer();
         var2.append(MethodUtils.getFinderMethodDeclaration(var1, var1.getReturnClassType()));
         var2.append("{" + EOL);

         try {
            if (var1.isResultSetFinder()) {
               var2.append(this.parse(this.getProductionRule("ejbSelectFieldBodyResultSet")));
            } else if (var1.isMultiFinder()) {
               var2.append(this.parse(this.getProductionRule("ejbSelectFieldBodyMulti")));
            } else {
               try {
                  var2.append(this.parse(this.getProductionRule("ejbSelectFieldBodyScalar")));
               } catch (Throwable var4) {
                  var4.printStackTrace();
                  throw new CodeGenerationException(var4.getMessage());
               }
            }
         } catch (CodeGenerationException var5) {
            if (debugLogger.isDebugEnabled()) {
               debug("finderMethod cought CodeGenerationException : " + var5);
            }
         } catch (Exception var6) {
            var6.printStackTrace();
            throw new CodeGenerationException(var6.getMessage());
         }

         var2.append("" + EOL + "}" + EOL);
         this.curFinder = null;
         return var2.toString();
      }
   }

   public String implementClearCMRFields() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         this.curField = (String)var2.next();
         if (this.addCMRClearForCurrentField()) {
            var1.append(this.fieldVarForField() + " = null;" + EOL);
         }
      }

      return var1.toString();
   }

   private boolean addCMRClearForCurrentField() {
      return this.bean.isOneToOneRelation(this.curField) && this.bean.isForeignKeyField(this.curField) || this.bean.isOneToManyRelation(this.curField) && !this.bean.getRelatedMultiplicity(this.curField).equals("One") || this.bean.isManyToManyRelation(this.curField);
   }

   public String ejbSelectFieldResultVar() {
      return this.varPrefix() + "retVal";
   }

   public boolean validateEjbSelectFieldReturnType(String var1) {
      return true;
   }

   public boolean validateEjbSelectFieldCollReturnType(String var1) {
      return true;
   }

   public String declareEjbSelectFieldResultVar() {
      Class var1 = this.curFinder.getReturnClassType();
      String var2 = var1.getName();
      this.validateEjbSelectFieldReturnType(var2);
      return this.declareEjbSelectFieldResultVar(var1);
   }

   public String declareEjbSelectFieldCollResultVar() {
      Class var1 = this.curFinder.getSelectFieldClass();
      if (var1.isPrimitive()) {
         var1 = ClassUtils.getObjectClass(var1);
      }

      String var2 = var1.getName();
      this.validateEjbSelectFieldCollReturnType(var2);
      return this.declareEjbSelectFieldResultVar(var1);
   }

   public String declareEjbSelectFieldResultVar(Class var1) {
      StringBuffer var2 = new StringBuffer();
      String var3 = ClassUtils.classToJavaSourceType(var1);
      var2.append("    ");
      var2.append(var3);
      var2.append(" ");
      var2.append(this.ejbSelectFieldResultVar());
      if (var1.isPrimitive()) {
         var2.append(" = ");
         var2.append(ClassUtils.getDefaultValue(var1));
         var2.append(";");
      } else {
         var2.append(" = null;");
      }

      var2.append(EOL);
      return var2.toString();
   }

   public String perhapsDeclareDistinctFilterVar() {
      return this.curFinder.isMultiFinder() && !this.curFinder.isSetFinder() && this.curFinder.isSelectDistinct() ? new String(this.declareSetVar() + EOL) : "";
   }

   public String assignEjbSelectFieldResultVar() {
      Class var1 = this.curFinder.getReturnClassType();
      return this.assignEjbSelectFieldResultVar(var1);
   }

   public String assignEjbSelectFieldCollResultVar() {
      Class var1 = this.curFinder.getSelectFieldClass();
      if (var1.isPrimitive()) {
         var1 = ClassUtils.getObjectClass(var1);
      }

      return this.assignEjbSelectFieldResultVar(var1);
   }

   public String assignEjbSelectFieldResultVar(Class var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("    ");
      var2.append(this.ejbSelectFieldResultVar());
      var2.append(" = ");
      var2.append(this.resultSetToVariable(1, var1, (String)null));
      var2.append(";");
      if (!var1.isPrimitive()) {
         var2.append("  if (").append(this.rsVar()).append(".wasNull()) { ");
         var2.append(this.ejbSelectFieldResultVar());
         var2.append(" = null; }").append(EOL);
      }

      return var2.toString();
   }

   public String checkNullForAggregateQuery() throws CodeGenerationException {
      Class var1 = this.curFinder.getReturnClassType();
      return var1.isPrimitive() && this.curFinder.isAggregateQuery() ? this.parse(this.getProductionRule("checkNullForAggregateQueries")) : "";
   }

   public String addEjbSelectFieldToList() {
      StringBuffer var1 = new StringBuffer();
      if (this.curFinder.isSelectDistinct()) {
         var1.append("if (" + this.setVar() + ".add(" + this.ejbSelectFieldResultVar() + ")) {" + EOL);
         var1.append("list.add(" + this.ejbSelectFieldResultVar() + ");" + EOL);
         var1.append("}" + EOL);
      } else {
         var1.append("list.add(" + this.ejbSelectFieldResultVar() + ");" + EOL);
      }

      return var1.toString();
   }

   public String ejbSelect_result_set_to_collection_class() throws CodeGenerationException {
      assert this.curFinder != null;

      StringBuffer var1 = new StringBuffer();
      Class var2 = this.curFinder.getReturnClassType();
      if (var2.equals(Collection.class)) {
         var1.append(this.parse(this.getProductionRule("ejbSelectFieldToCollection")));
      } else {
         if (!var2.equals(Set.class)) {
            throw new AssertionError("Invalid return type for ejbSelect.");
         }

         var1.append(this.parse(this.getProductionRule("ejbSelectFieldToSet")));
      }

      return var1.toString();
   }

   private String wrapped_ejbSelect_params(List var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ParamNode var4 = (ParamNode)var3.next();
         if (var4.isBeanParam()) {
            var2.append(var4.getParamName());
            var2.append(", ");
         } else if (var4.isSelectInEntity()) {
            var2.append("__WL_ctx.getPrimaryKey(), ");
         } else {
            var2.append(this.wrapped_param(var4.getParamClass().getName(), var4.getParamName()));
            var2.append(", ");
         }
      }

      return var2.toString();
   }

   private String wrapped_param(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      boolean var4 = false;
      var4 = !this.primConversion(var1).equals(var1);
      if (var4) {
         var3.append("new " + this.primConversion(var1) + "(");
      }

      var3.append(" " + var2);
      if (var4) {
         var3.append(" )");
      }

      var3.append(" ");
      return var3.toString();
   }

   private String primConversion(String var1) {
      String var2 = var1;
      if (var1.equals("boolean")) {
         var2 = "Boolean";
      }

      if (var1.equals("int")) {
         var2 = "Integer";
      }

      if (var1.equals("short")) {
         var2 = "Short";
      }

      if (var1.equals("long")) {
         var2 = "Long";
      }

      if (var1.equals("double")) {
         var2 = "Double";
      }

      if (var1.equals("float")) {
         var2 = "Float";
      }

      if (var1.equals("char")) {
         var2 = "Character";
      }

      if (var1.equals("byte")) {
         var2 = "Byte";
      }

      return var2;
   }

   private String primInitialization(String var1) {
      String var2 = "null";
      if (var1.equals("boolean")) {
         var2 = "false";
      }

      if (var1.equals("int")) {
         var2 = "0";
      }

      if (var1.equals("short")) {
         var2 = "0";
      }

      if (var1.equals("long")) {
         var2 = "0";
      }

      if (var1.equals("double")) {
         var2 = "0.0";
      }

      if (var1.equals("float")) {
         var2 = "0.0";
      }

      if (var1.equals("char")) {
         var2 = "0x00";
      }

      if (var1.equals("byte")) {
         var2 = "0x00";
      }

      return var2;
   }

   public void checkCurFinder() throws CodeGenerationException {
      if (this.curFinder == null) {
         Loggable var1 = EJBLogger.logNullFinderLoggable("checkCurFinder");
         throw new CodeGenerationException(var1.getMessage());
      }
   }

   public String getRemoteHomeVarForFinder() throws CodeGenerationException {
      String var1 = null;
      EjbqlFinder var2 = (EjbqlFinder)this.curFinder;
      if (var2.hasRemoteBeanParam()) {
         ParamNode var3 = var2.getRemoteBeanParam();
         if (var3 != null) {
            var1 = var3.getId();
         }
      }

      if (var1 == null) {
         Loggable var4 = EJBLogger.logNoCMRFieldForRemoteRelationshipLoggable(this.curFinder.getName());
         throw new CodeGenerationException(var4.getMessage());
      } else {
         return this.homeVar(var1);
      }
   }

   public String currentFinderName() throws CodeGenerationException {
      this.checkCurFinder();
      return this.curFinder.getName();
   }

   public String declarePkVarIfLoadsBean() throws CodeGenerationException {
      return this.curFinder.finderLoadsBean() ? this.declarePkVar() : "";
   }

   public Class getRemotePKClass() throws CodeGenerationException {
      String var1 = null;

      assert this.curFinder instanceof EjbqlFinder;

      EjbqlFinder var2 = (EjbqlFinder)this.curFinder;
      if (!var2.hasRemoteBeanParam()) {
         Loggable var10 = EJBLogger.logNoRemoteHomeLoggable(var2.getName());
         throw new CodeGenerationException(var10.getMessage());
      } else {
         ParamNode var3 = var2.getRemoteBeanParam();
         var1 = var3.getRemoteHomeName();
         var3 = null;
         Class var9 = this.loadClass(var1);
         Method[] var4 = var9.getMethods();
         Method var5 = null;

         for(int var6 = 0; var6 < var4.length; ++var6) {
            if (var4[var6].getName().compareTo("findByPrimaryKey") == 0) {
               var5 = var4[var6];
               break;
            }
         }

         if (var5 == null) {
            Loggable var12 = EJBLogger.logMethodNotFoundInInterfaceLoggable("findByPrimaryKey", var1);
            throw new CodeGenerationException(var12.getMessage());
         } else {
            Class[] var11 = var5.getParameterTypes();
            if (var11.length > 1) {
               Loggable var13 = EJBLogger.logMethodHasWrongParamCountLoggable("findByPrimaryKey", var1, "1");
               throw new CodeGenerationException(var13.getMessage());
            } else {
               Class var7 = var11[0];
               String var8 = var11[0].getName();
               return var7;
            }
         }
      }
   }

   public String getRemotePKClassString() throws CodeGenerationException {
      Class var1 = this.getRemotePKClass();
      return var1.getName();
   }

   public String getRemotePKObject() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.getRemotePKClass();
      String var3 = var2.getName();
      if (var3.startsWith("java.lang.")) {
         var1.append(this.parse(this.getProductionRule("remoteReadJavaLangEOColumn")));
      } else {
         var1.append(this.parse(this.getProductionRule("remoteReadJavaObjectEOColumn")));
      }

      return var1.toString();
   }

   public String PkOrGenBeanClassName() throws CodeGenerationException {
      return this.curFinder.finderLoadsBean() ? this.getGeneratedBeanClassName() : this.pk_class();
   }

   public String PkVarOrWLBean() throws CodeGenerationException {
      return this.curFinder.finderLoadsBean() ? this.beanVar() : this.pkVar();
   }

   public int getPKOrGroupColumnCount() {
      return ((EjbqlFinder)this.curFinder).getPKOrGroupColumnCount();
   }

   public int getGroupColumnCount(RDBMSBean var1, String var2) {
      FieldGroup var3 = var1.getFieldGroup(var2);
      if (var3 == null) {
         return var1.getPrimaryKeyFields().size();
      } else {
         HashSet var4 = new HashSet();
         TreeSet var5 = new TreeSet(var3.getCmpFields());
         var5.addAll(var1.getPrimaryKeyFields());
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            var4.add(var1.getCmpColumnForField(var7));
         }

         Iterator var11 = var3.getCmrFields().iterator();

         while(var11.hasNext()) {
            String var8 = (String)var11.next();
            Iterator var9 = var1.getForeignKeyColNames(var8).iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               var4.add(var10);
            }
         }

         return var4.size();
      }
   }

   public String otherPkVar() {
      return this.pkVar() + "_2";
   }

   public String declareOtherPkVar() {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      var1.append(this.pk_class() + " " + this.otherPkVar() + " = ");
      if (var3.hasComplexPrimaryKey()) {
         var1.append("new " + this.pk_class() + "();");
      } else {
         var1.append("null;");
      }

      return var1.toString();
   }

   private Class loadClass(String var1) throws CodeGenerationException {
      Class var2 = null;

      try {
         var2 = this.bd.getClassLoader().loadClass(var1);
         return var2;
      } catch (ClassNotFoundException var5) {
         Loggable var4 = EJBLogger.logUnableToLoadClassLoggable("RDBMSCodeGenerator", var1);
         throw new CodeGenerationException(var4.getMessage());
      }
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

   public String eoVar() {
      return this.varPrefix() + "eo";
   }

   public String eoRCVar() {
      return this.varPrefix() + "eo_rc";
   }

   public String pkVar() {
      return this.varPrefix() + "pk";
   }

   public String fkVar() {
      return this.varPrefix() + "fk";
   }

   public String conVar() {
      return this.varPrefix() + "con";
   }

   public String rsVar() {
      return this.varPrefix() + "rs";
   }

   public String rsInfoVar() {
      return this.varPrefix() + "rsInfo";
   }

   public String stmtVar() {
      return this.varPrefix() + "stmt";
   }

   public String currStmtArrayVar() {
      return this.stmtArrayElement(this.curTableIndex);
   }

   public String stmtArrayVar() {
      return this.varPrefix() + "stmt_array";
   }

   public String stmtArrayElement(int var1) {
      return this.varPrefix() + "stmt_array[" + var1 + "]";
   }

   public String stmtTableVar(String var1) {
      return this.varPrefix() + "stmt" + "_" + this.replaceIllegalJavaCharacters(var1) + "_" + this.bean.tableIndex(var1);
   }

   public String pmVar() {
      return this.varPrefix() + "pm";
   }

   public String keyVar() {
      return this.varPrefix() + "key";
   }

   public String numVar() {
      return this.varPrefix() + "num";
   }

   public String txVar() {
      return this.varPrefix() + "tx";
   }

   public String offsetVar() {
      return this.varPrefix() + "offset";
   }

   public String offsetIntObjVar() {
      return this.varPrefix() + "offsetIntObj";
   }

   public String pkMapVar() {
      return this.varPrefix() + "pkMap";
   }

   public String isMultiVar() {
      return this.varPrefix() + "isMulti";
   }

   public String queryVar() {
      return this.varPrefix() + "query";
   }

   public String queryArrayVar() {
      return this.varPrefix() + "query_array";
   }

   public String queryArrayElement(int var1) {
      return this.varPrefix() + "query_array[" + var1 + "]";
   }

   public String iVar() {
      return this.varPrefix() + "i";
   }

   public String countVar() {
      return this.varPrefix() + "count";
   }

   public String totalVar() {
      return this.varPrefix() + "total";
   }

   public String blobClobCountVar() {
      return this.varPrefix() + "blobClob_count";
   }

   public String oldStateVar() {
      return this.varPrefix() + "oldState";
   }

   public String setBlobClobForOutputMethodName() {
      return this.varPrefix() + "set_" + this.curField + "ForOutput";
   }

   public String setBlobClobForInputMethodName() {
      return this.varPrefix() + "set_" + this.curField + "ForInput";
   }

   public String classLoaderVar() {
      return this.varPrefix() + "classLoader";
   }

   public String ctxVar() {
      return this.varPrefix() + "ctx";
   }

   public String jctxVar() {
      return this.varPrefix() + "initialCtx";
   }

   public String isModifiedVar() {
      return this.varPrefix() + "isModified";
   }

   public String beanIsModifiedVar() {
      return this.varPrefix() + "beanIsModified";
   }

   public String isModifiedUnionVar() {
      return this.varPrefix() + "isModifiedUnion";
   }

   public String modifiedBeanIsRegisteredVar() {
      return this.varPrefix() + "modifiedBeanIsRegistered";
   }

   public String beanIsLoadedVar() {
      return this.varPrefix() + "beanIsLoaded";
   }

   public String invalidatedBeanIsRegisteredVar() {
      return this.varPrefix() + "invalidatedBeanIsRegistered";
   }

   public String isLoadedVar() {
      return this.varPrefix() + "isLoaded";
   }

   public String colVar() {
      return this.varPrefix() + "collection";
   }

   public String setVar() {
      return this.varPrefix() + "set";
   }

   public String orderedSetVar() {
      return this.varPrefix() + "orderedSet";
   }

   public String selectInEntityPKVar() {
      return this.varPrefix() + "selectInEntityPK";
   }

   public String mapVar() {
      return this.varPrefix() + "map";
   }

   public String stringVar(int var1) {
      return this.varPrefix() + "stringVar" + var1;
   }

   public String tempVar(int var1) {
      return this.varPrefix() + "tempVar" + var1;
   }

   public String sqlTimestampVar(int var1) {
      return this.varPrefix() + "sqlTimestampVar" + var1;
   }

   private String bmVar(String var1) {
      return this.varPrefix() + ClassUtils.makeLegalName(var1) + "_bm";
   }

   private String genKeyVar() {
      return this.varPrefix() + "genKey";
   }

   private String byteArrayVar(String var1) {
      return this.varPrefix() + "byteArray_" + var1;
   }

   private String tableModifiedVar(String var1) {
      return this.bean.hasMultipleTables() ? this.varPrefix() + "tableModified" + this.replaceIllegalJavaCharacters(var1) : this.modifiedBeanIsRegisteredVar();
   }

   public String tableModifiedVar() {
      return this.tableModifiedVar(this.curTable);
   }

   private String tableLoadedVar(String var1) {
      return this.bean.hasMultipleTables() ? this.varPrefix() + "tableLoaded" + this.replaceIllegalJavaCharacters(var1) : this.beanIsLoadedVar();
   }

   public String tableLoadedVar() {
      return this.tableLoadedVar(this.curTable);
   }

   private String snapshotBufferVar() {
      return "sb_snap";
   }

   public String createMethodName() {
      return this.varPrefix() + "create";
   }

   public String existsMethodName() {
      return this.varPrefix() + "exists";
   }

   public String invalidVar() {
      return this.varPrefix() + "invalid";
   }

   public String rowSetFactoryVar() {
      return this.varPrefix() + "rowSetFactory";
   }

   public String rowSetVar() {
      return this.varPrefix() + "rowSet";
   }

   public String rowSetFactoryName() {
      return "weblogic.jdbc.rowset.RowSetFactory ";
   }

   public String replaceIllegalJavaCharacters(String var1) {
      String var2 = var1;

      for(int var3 = 0; var3 < this.illegalJavaCharacters.length; ++var3) {
         var2 = var2.replace(this.illegalJavaCharacters[var3], '_');
      }

      return var2;
   }

   public String perhapsDeclareRowSetFactoryVar() {
      return this.bean.hasResultSetFinder() ? "private static " + this.rowSetFactoryName() + " " + this.rowSetFactoryVar() + " = null;" : "";
   }

   public String declareStmtArrayVars() {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      var1.append("java.sql.PreparedStatement[] ").append(this.stmtArrayVar()).append(" = ");
      var1.append("new java.sql.PreparedStatement[").append(this.bean.tableCount()).append("];");
      var1.append(EOL).append(EOL);

      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         this.curTableIndex = var2;
         this.curTableName = this.bean.tableAt(var2);
         var1.append("java.sql.PreparedStatement ").append(this.stmtTableVar(this.curTableName)).append(" = null;").append(EOL);
         var1.append(this.stmtArrayElement(var2)).append(" = ").append(this.stmtTableVar(this.curTableName)).append(";").append(EOL);
      }

      var1.append(EOL);
      return var1.toString();
   }

   public String declareQueryArrayVars() {
      return EOL + "String[] " + this.queryArrayVar() + " = new String[" + this.bean.tableCount() + "];" + EOL;
   }

   public String declareContainerManagedFieldVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.cmpFieldNames.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append("public ");
         var1.append(ClassUtils.classToJavaSourceType(this.bean.getCmpFieldClass(var3)) + " ");
         var1.append(var3 + ";");
         var1.append(EOL);
      }

      return var1.toString();
   }

   private List foreignKeyVarNames(String var1) {
      String var2 = this.bean.getTableForCmrField(var1);
      Iterator var3 = this.bean.getForeignKeyColNames(var1).iterator();
      ArrayList var4 = new ArrayList();

      while(var3.hasNext()) {
         String var5 = (String)var3.next();
         var4.add(this.bean.variableForField(var1, var2, var5));
      }

      return var4;
   }

   public String declareForeignKeyFieldVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getForeignKeyFieldNames().iterator();

      while(true) {
         String var3;
         do {
            do {
               if (!var2.hasNext()) {
                  return var1.toString();
               }

               var3 = (String)var2.next();
            } while(!this.bean.containsFkField(var3));
         } while(this.bean.isForeignCmpField(var3));

         String var4 = this.bean.getTableForCmrField(var3);
         Iterator var5 = this.bean.getForeignKeyColNames(var3).iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            String var7 = ClassUtils.classToJavaSourceType(this.bean.getForeignKeyColClass(var3, var6));
            String var8 = this.bean.variableForField(var3, var4, var6);
            var1.append("public ");
            var1.append(var7 + " ");
            var1.append(var8 + ";");
            var1.append(EOL);
         }
      }
   }

   public String declareRelationFieldVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = CodeGenUtils.fieldVarName(var3);
         String var5 = ClassUtils.classToJavaSourceType(this.bean.getCmrFieldClass(var3));
         var1.append("public ");
         var1.append(var5 + " ");
         var1.append(var4 + ";");
         var1.append(EOL);
         if (this.bean.isSelfRelationship(var3)) {
            String var6 = CodeGenUtils.fieldRemovedVarName(var3);
            var1.append("public ");
            var1.append(var5 + " ");
            var1.append(var6 + ";");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   private String finderVarName(String var1) {
      return this.varPrefix() + var1 + "_finder_";
   }

   public String declareStaticFinderVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.relInterfaceNameForField(var3);
         this.bean.finderMethodName(this.bd, var3);
         String var6 = this.finderVarName(var3);
         var1.append("private static final java.lang.reflect.Method ");
         var1.append(var6);
         var1.append(";");
         var1.append(EOL);
      }

      var1.append(EOL);
      return var1.toString();
   }

   public String assignStaticFinderVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();
      if (var2.hasNext()) {
         var1.append("static {");
         var1.append(EOL);
         var1.append("Method m = null;");
         var1.append(EOL);
         var2 = this.bean.getCmrFieldNames().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            String var4 = this.relInterfaceNameForField(var3);
            String var5 = this.finderVarName(var3);
            String var6 = this.generateCMRFieldFinderMethodName(var3);
            var1.append("try {");
            var1.append(EOL);
            var1.append("m = ");
            var1.append(var4);
            var1.append(".class.getMethod(\"");
            var1.append(var6);
            var1.append("\", ");
            if (this.bean.isManyToManyRelation(var3)) {
               var1.append(" new Class[] { Object.class } ");
            } else {
               var1.append(this.primaryFieldClassesArray(var3));
            }

            var1.append(");" + EOL);
            var1.append("} catch (NoSuchMethodException ignore) {");
            var1.append(EOL);
            var1.append("m = null;");
            var1.append(EOL);
            var1.append("}");
            var1.append(EOL);
            var1.append(var5);
            var1.append(" = m;");
            var1.append(EOL);
         }

         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String perhapsInitializeIsModified() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append(this.isModifiedVar());
         var1.append("[");
         var1.append(this.iVar());
         var1.append("] = false;");
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String perhapsInitializeModifiedBeanIsRegisteredVar() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append(this.modifiedBeanIsRegisteredVar());
         var1.append(" = false;");
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String perhapsOrTermForIsModified() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("|| ");
         var1.append(this.isModifiedVarForField());
      }

      return var1.toString();
   }

   public String declareIsModifiedVar() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("private boolean[] " + this.isModifiedVar() + " = new boolean[" + this.bean.getFieldCount() + "];" + EOL);
         var1.append("private boolean " + this.modifiedBeanIsRegisteredVar() + "= false;" + EOL);
      }

      return var1.toString();
   }

   public String declareisModifiedUnionVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.isModifiedUnionVar() + " = new boolean[" + this.bean.getFieldCount() + "];" + EOL);
      return var1.toString();
   }

   public String perhapsComputeModifiedUnion() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append(this.parse(this.getProductionRule("computeModifiedUnion")));
      }

      return var1.toString();
   }

   public String perhapsImplementResetIsModifiedVarsMethodBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append(this.parse(this.getProductionRule("implementResetIsModifiedVarsMethodBody")));
      }

      return var1.toString();
   }

   public String fieldsWoFkColumns() {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      int var3;
      for(var3 = 0; var3 < this.cmpFieldNames.size(); ++var3) {
         var1.append("(" + this.iVar() + "==" + var2 + ") || ");
         ++var2;
      }

      for(var3 = 0; var3 < this.bean.tableCount(); ++var3) {
         List var4 = this.bean.getCMRFields(var3);

         for(Iterator var5 = var4.iterator(); var5.hasNext(); ++var2) {
            String var6 = (String)var5.next();
            if (this.bean.isSelfRelationship(var6) && this.bean.containsFkField(var6) && !this.bean.isForeignCmpField(var6)) {
               var1.append("(" + this.iVar() + "==" + var2 + ") || ");
            }
         }
      }

      var1.setLength(var1.length() - 4);
      return var1.toString();
   }

   public String perhapsIsFkColsNullableCheck(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (this.bean.isOneToOneRelation(var1) && !this.bean.isSelfRelationship(var1) || this.bean.isOneToManyRelation(var1)) {
         String var3 = null;
         if (this.bean.isSelfRelationship(var1)) {
            var3 = CodeGenUtils.fieldRemovedVarName(var1);
         } else {
            var3 = CodeGenUtils.fieldVarName(var1);
         }

         var2.append(" && (" + this.pmVar() + ".isFkColsNullable(\"" + var1 + "\") || " + var3 + " != null || !__WL_getIsRemoved())");
      }

      return var2.toString();
   }

   public String perhapsAddSelfRelatedBeansToInsertStmt() throws CodeGenerationException {
      if (this.bean.getOrderDatabaseOperations() && this.bean.isSelfRelationship()) {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getDeclaredFieldNames().iterator();
         if (!var2.hasNext()) {
            return "";
         } else {
            while(var2.hasNext()) {
               this.curField = (String)var2.next();
               if (this.bean.isOneToManyRelation(this.curField) && this.bean.isSelfRelationship(this.curField) && this.bean.getRelatedMultiplicity(this.curField).equals("One")) {
                  var1.append(this.parse(this.getProductionRule("implementAddSelfRelatedBeansToInsertStmtMethodBody")));
               }
            }

            return var1.toString();
         }
      } else {
         return "";
      }
   }

   public String perhapsAddSelfRelatedBeansToDeleteStmt() throws CodeGenerationException {
      if (this.bean.getOrderDatabaseOperations() && this.bean.isSelfRelationship()) {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getDeclaredFieldNames().iterator();
         if (!var2.hasNext()) {
            return "";
         } else {
            while(var2.hasNext()) {
               this.curField = (String)var2.next();
               if (this.bean.isOneToManyRelation(this.curField) && this.bean.isSelfRelationship(this.curField) && this.bean.getRelatedMultiplicity(this.curField).equals("Many")) {
                  var1.append(this.parse(this.getProductionRule("implementAddSelfRelatedBeansToDeleteStmtMethodBody")));
               }
            }

            return var1.toString();
         }
      } else {
         return "";
      }
   }

   public String perhapsImplementResetIsModifiedVarsMethodBodyOpnBased() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var1.append(this.parse(this.getProductionRule("implementResetIsModifiedVarsMethodBodyOpnBased")));
      }

      return var1.toString();
   }

   public String setBlobClobBasedOnOperation() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.hasBlobClobColumn()) {
         var1.append("// No blob/clob field is defined for this cmp bean ;" + EOL);
      } else {
         var1.append("if (operation == DDConstants.INSERT) { " + EOL);
         var1.append(this.setBlobClobForCreate());
         var1.append("} else{ " + EOL);
         var1.append(this.setBlobClobForStore());
         var1.append("} " + EOL);
      }

      return var1.toString();
   }

   public String determineBeanParamsForCreateArray() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.hasClobColumn()) {
         var1.append(this.setBeanParamsForCreateArray() + EOL);
      } else {
         var1.append("if(" + this.pmVar() + ".perhapsUseSetStringForClobForOracle()){" + EOL);
         var1.append(this.setBeanParamsForCreateArrayOptimizedForClobUpdate() + EOL);
         var1.append("} else{ " + EOL);
         var1.append(this.setBeanParamsForCreateArray() + EOL);
         var1.append("} " + EOL);
      }

      return var1.toString();
   }

   public String declareIsInvalidatedVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append("private boolean " + this.invalidatedBeanIsRegisteredVar() + "= false;" + EOL);
      return var1.toString();
   }

   public String declareIsLoadedVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append("private boolean[] ");
      var1.append(this.isLoadedVar());
      var1.append(" = new boolean[");
      var1.append(this.bean.getFieldCount());
      var1.append("];");
      var1.append(EOL);
      var1.append("private boolean " + this.beanIsLoadedVar() + "= false;" + EOL);
      return var1.toString();
   }

   public String isCmrLoadedVarName(String var1) {
      return this.varPrefix() + var1 + "_isLoaded_";
   }

   public String isCmrLoadedVarNameForField() {
      return this.isCmrLoadedVarName(this.curField);
   }

   public String declareCmrIsLoadedVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = this.isCmrLoadedVarName(var3);
         var1.append("public ");
         var1.append("boolean ");
         var1.append(var4 + " = false;");
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String assignCmrIsLoadedFalse() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();
      if (var2.hasNext()) {
         var1.append(EOL);
      }

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = this.isCmrLoadedVarName(var3);
         var1.append(var4 + " = false;");
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String declareEntityContextVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append("private EntityContext " + this.ctxVar() + ";");
      return var1.toString();
   }

   public String perhapsDeclareInitialContext() {
      return this.bean.getRemoteFieldNames().size() > 0 ? "Context " + this.jctxVar() + " = null;" : "";
   }

   public String perhapsDeclareTableLoadedModifiedVars() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append("private boolean " + this.tableModifiedVar(var3) + " = false;" + EOL);
            var1.append("private boolean " + this.tableLoadedVar(var3) + " = false;" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsInitializeTableLoadedModifiedVars() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.tableModifiedVar(var3) + " = false;" + EOL);
            var1.append(this.tableLoadedVar(var3) + " = false;" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsSetTableLoadedVarsForBean() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.tableLoadedVar(var3) + " = true;" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsSetTableLoadedVarsForGroup() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         assert this.curGroup != null;

         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTableNamesForGroup(this.curGroup.getName()).iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.tableLoadedVar(var3) + " = true;" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsSetTableModifiedVarForCmpField() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         String var2 = this.bean.getTableForCmpField(this.curField);
         var1.append(this.tableModifiedVar(var2) + " = true;" + EOL);
         return var1.toString();
      }
   }

   public String perhapsSetTableModifiedVarForCmrField() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         String var2 = this.bean.getTableForCmrField(this.curField);
         var1.append(this.tableModifiedVar(var2) + " = true;" + EOL);
         return var1.toString();
      }
   }

   public String perhapsSetTableModifiedVarsForBean() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.tableModifiedVar(var3) + " = true;" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsResetTableModifiedVarsForBean() {
      if (!this.bean.isOptimistic() && !this.bean.getVerifyReads()) {
         return "";
      } else if (!this.bean.hasMultipleTables()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.tableModifiedVar(var3) + " = false;" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsOptFieldCheckForBatch() throws CodeGenerationException {
      if (this.bd.isOptimistic()) {
         String var1 = this.curTableName;
         if (this.bean.getVerifyColumns(var1).equalsIgnoreCase("version") || this.bean.getVerifyColumns(var1).equalsIgnoreCase("timestamp")) {
            String var2 = this.bean.getOptimisticColumn(var1);
            String var3 = this.bean.getCmpField(var1, var2);
            return "|| (" + this.isLoadedVar(var3) + " || " + this.isModifiedVar(var3) + ")";
         }
      }

      return "";
   }

   public String perhapsAppendVerifySqlForBatch() throws CodeGenerationException {
      if (!this.bean.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();

         for(Iterator var2 = this.bean.getTables().iterator(); var2.hasNext(); this.curTable = null) {
            this.curTable = (String)var2.next();
            var1.append(this.parse(this.getProductionRule("appendVerifySqlForTableForBatch")));
         }

         return var1.toString();
      }
   }

   public String perhapsSetVerifyParamsForBatch() throws CodeGenerationException {
      if (!this.bean.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();

         for(Iterator var2 = this.bean.getTables().iterator(); var2.hasNext(); this.curTable = null) {
            this.curTable = (String)var2.next();
            int var3 = this.bean.tableIndex(this.curTable);
            var1.append("if ((" + this.tableLoadedVar() + " && " + this.tableModifiedVar() + ") " + this.perhapsOptFieldCheckForBatch() + ") {" + EOL);
            var1.append("int " + this.numVar() + " = " + "verifyCount[" + var3 + "];" + EOL);
            String[] var4 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
            var1.append(this.preparedStatementBindings(var4, "this", true, true, true, false, this.stmtArrayElement(var3)));
            var1.append(this.setSnapshotParams());
            var1.append("verifyCount[" + var3 + "] = " + this.numVar() + ";" + EOL);
            var1.append("}" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsAppendVerifySql() throws CodeGenerationException {
      if (!this.bean.getVerifyReads()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();

         for(Iterator var2 = this.bean.getTables().iterator(); var2.hasNext(); this.curTable = null) {
            this.curTable = (String)var2.next();
            this.curTableIndex = this.bean.tableIndex(this.curTable);
            var1.append(this.parse(this.getProductionRule("appendVerifySqlForTable")));
         }

         return var1.toString();
      }
   }

   private void needAndCheck(StringBuffer var1) {
      var1.append("if (needAnd) {" + EOL);
      var1.append("verifySql[" + this.bean.tableIndex(this.curTable) + "].append(\" AND \");" + EOL);
      var1.append("}" + EOL);
      var1.append("else {" + EOL);
      var1.append("needAnd = true;" + EOL);
      var1.append("}" + EOL);
   }

   public String verifySqlForTable() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append("verifySql[" + this.bean.tableIndex(this.curTable) + "].append(\"(");
      Iterator var2 = this.pkFieldNames.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = this.bean.getPKColumnName(this.curTable, var3);
         var1.append(var4 + " = ? ");
         if (var2.hasNext()) {
            var1.append("AND ");
         }
      }

      if (!this.bean.getVerifyColumns(this.curTable).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(this.curTable).equalsIgnoreCase("timestamp")) {
         var1.append("\");" + EOL);
         var1.append("StringBuffer " + this.snapshotBufferVar() + " = new StringBuffer();" + EOL);
         var1.append(this.perhapsConstructSnapshotPredicate());
         var1.append("verifySql[" + this.bean.tableIndex(this.curTable) + "].append(" + this.snapshotBufferVar() + ".toString() + \")\");" + EOL);
      } else {
         var1.append(" AND " + this.bean.getOptimisticColumn(this.curTable) + " = ?)\");" + EOL);
      }

      return var1.toString();
   }

   public String perhapsSetVerifyParams() throws CodeGenerationException {
      if (!this.bean.getVerifyReads()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();

         for(Iterator var2 = this.bean.getTables().iterator(); var2.hasNext(); this.curTable = null) {
            this.curTable = (String)var2.next();
            this.curTableIndex = this.bean.tableIndex(this.curTable);
            var1.append("if (" + this.tableLoadedVar() + " && !" + this.tableModifiedVar() + ") {" + EOL);
            var1.append("int " + this.numVar() + " = " + "verifyCount[" + this.curTableIndex + "];" + EOL);
            var1.append(this.bd.getPrimaryKeyClass().getName() + " " + this.pkVar() + " = (" + this.bd.getPrimaryKeyClass().getName() + ")((EntityEJBContextImpl)" + this.ctxVar() + ").__WL_getPrimaryKey();" + EOL);
            String[] var3 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
            var1.append(this.preparedStatementBindings(var3, this.pkVar(), false, this.bd.hasComplexPrimaryKey(), true, false, this.currStmtArrayVar()));
            if (!this.bean.getVerifyColumns(this.curTable).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(this.curTable).equalsIgnoreCase("timestamp")) {
               var1.append(this.setSnapshotParams());
            } else {
               String var4 = this.bean.getOptimisticColumn(this.curTable);
               String var5 = this.bean.getCmpField(this.curTable, var4);
               var3 = new String[]{var5};
               var1.append(this.preparedStatementBindings(var3, "this", true, true, true, false, this.currStmtArrayVar()));
            }

            var1.append("verifyCount[" + this.curTableIndex + "] = " + this.numVar() + ";" + EOL);
            var1.append("}" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsAssignInitialContext() throws CodeGenerationException {
      if (this.bean.getRemoteFieldNames().size() > 0) {
         StringBuffer var1 = new StringBuffer();
         var1.append("try {");
         var1.append(this.jctxVar() + " = new InitialContext();" + EOL);
         var1.append(this.parse(this.getProductionRule("standardCatch")));
         return var1.toString();
      } else {
         return "";
      }
   }

   private String homeVar(String var1) {
      return this.varPrefix() + var1 + "_home";
   }

   public String homeVarForField() {
      return this.homeVar(this.curField);
   }

   public String declareHomeVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getRemoteFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         EjbEntityRef var4 = this.bean.getEjbEntityRef(var3);
         var1.append(var4.getHome() + " " + this.homeVar(var3) + ";" + EOL);
      }

      return var1.toString();
   }

   public String declareManagerVars() {
      StringBuffer var1 = new StringBuffer();
      var1.append("private RDBMSPersistenceManager " + this.pmVar() + ";" + EOL);
      var1.append("private ClassLoader " + this.classLoaderVar() + ";" + EOL);
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!this.bean.isRemoteField(var3)) {
            var1.append(this.declareManagerVarField(var3));
         }
      }

      Set var8 = this.ejbSelectBeanTargetMap.keySet();
      Iterator var4 = var8.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var1.append(this.declareManagerVarBean(var5));
      }

      Iterator var9 = this.bean.getRelationshipCachings().iterator();

      while(var9.hasNext()) {
         RelationshipCaching var6 = (RelationshipCaching)var9.next();
         Iterator var7 = var6.getCachingElements().iterator();
         var1.append(this.declareManagerVarsForCachingElements(this.bean, var7));
      }

      return var1.toString();
   }

   private String declareManagerVarsForCachingElements(RDBMSBean var1, Iterator var2) {
      StringBuffer var3 = new StringBuffer();

      while(var2.hasNext()) {
         RelationshipCaching.CachingElement var4 = (RelationshipCaching.CachingElement)var2.next();
         String var5 = var4.getCmrField();
         String var6 = var4.getGroupName();
         RDBMSBean var7 = var1.getRelatedRDBMSBean(var5);
         String var8 = var7.getEjbName();
         var3.append(this.declareManagerVarBean(var8));
         Iterator var9 = var4.getCachingElements().iterator();
         if (var9.hasNext()) {
            var3.append(this.declareManagerVarsForCachingElements(var7, var9));
         }
      }

      return var3.toString();
   }

   private String declareManagerVarField(String var1) {
      String var2 = this.bean.getRelatedRDBMSBean(var1).getEjbName();
      if (!this.declaredManagerVars.containsKey(var1)) {
         this.declaredManagerVars.put(var1, var2);
         return this.managerVar(var1);
      } else {
         return "";
      }
   }

   private String declareManagerVarBean(String var1) {
      if (!this.declaredManagerVars.containsKey(var1)) {
         this.declaredManagerVars.put(var1, var1);
         return this.managerVar(var1);
      } else {
         return "";
      }
   }

   private String managerVar(String var1) {
      return "private CMPBeanManager " + this.bmVar(var1) + ";" + EOL;
   }

   public String initializePersistentVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.variableToClass.keySet().iterator();
      if (var2.hasNext()) {
         var1.append(EOL);
      }

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Class var4 = (Class)this.variableToClass.get(var3);
         if (this.bean.hasCmpField(var3)) {
            var1.append(this.setCmpField(var3, ClassUtils.getDefaultValue(var4)) + ";" + EOL);
         } else {
            var1.append("this." + var3 + " = " + ClassUtils.getDefaultValue(var4) + ";" + EOL);
         }
      }

      return var1.toString();
   }

   public String initializeRelationVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();
      if (var2.hasNext()) {
         var1.append(EOL);
      }

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = CodeGenUtils.fieldVarName(var3);
         Class var5 = this.bean.getCmrFieldClass(var3);
         var1.append("this." + var4 + " = " + ClassUtils.getDefaultValue(var5) + ";" + EOL);
         if (this.bean.isSelfRelationship(var3)) {
            String var6 = CodeGenUtils.fieldRemovedVarName(var3);
            var1.append("this." + var6 + " = " + ClassUtils.getDefaultValue(var5) + ";" + EOL);
         }
      }

      var1.append(EOL);
      return var1.toString();
   }

   private boolean doSnapshot(String var1) {
      String var2 = (String)this.variableToField.get(var1);
      String var3 = null;
      String var4 = null;
      if (this.bean.hasCmpField(var2)) {
         var3 = this.bean.getTableForCmpField(var2);
         var4 = this.bean.getColumnForCmpFieldAndTable(var2, var3);
         return !this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp") ? true : var4.equals(this.bean.getOptimisticColumn(var3));
      } else {
         var3 = this.bean.getTableForVariable(var1);
         return !this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp");
      }
   }

   public String perhapsDeclareSnapshotVars() {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(true) {
            label33:
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               String var5;
               Class var7;
               if (!this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp")) {
                  Iterator var9 = this.variableToField.keySet().iterator();

                  while(true) {
                     String var10;
                     do {
                        do {
                           do {
                              do {
                                 if (!var9.hasNext()) {
                                    continue label33;
                                 }

                                 var5 = (String)var9.next();
                                 var10 = (String)this.variableToField.get(var5);
                              } while(this.bean.isBlobCmpColumnTypeForField(var5));
                           } while(this.bean.isClobCmpColumnTypeForField(var5));
                        } while(this.bd.getPrimaryKeyFieldNames().contains(var5));
                     } while((!this.bean.hasCmpField(var10) || !this.bean.getTableForCmpField(var10).equals(var3)) && (this.bean.hasCmpField(var10) || !this.bean.getTableForVariable(var5).equals(var3)));

                     var7 = this.getVariableClass(var5);
                     Class var8 = CodeGenUtils.getSnapshotClass(this.bean, var7);
                     var1.append("public ");
                     var1.append(ClassUtils.classToJavaSourceType(var8) + " ");
                     var1.append(CodeGenUtils.snapshotNameForVar(var5) + ";");
                     var1.append(EOL);
                  }
               } else {
                  String var4 = this.bean.getOptimisticColumn(var3);
                  var5 = this.bean.getCmpField(var3, var4);
                  Class var6 = this.getVariableClass(var5);
                  var7 = CodeGenUtils.getSnapshotClass(this.bean, var6);
                  var1.append("public ");
                  var1.append(ClassUtils.classToJavaSourceType(var7) + " ");
                  var1.append(CodeGenUtils.snapshotNameForVar(var5) + ";");
                  var1.append(EOL);
               }
            }

            return var1.toString();
         }
      }
   }

   public String perhapsCreateSnapshotBuffer() {
      return !this.bd.isOptimistic() ? "" : "StringBuffer " + this.snapshotBufferVar() + " = new StringBuffer();";
   }

   public String perhapsAssignOptimisticField() {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(true) {
            String var3;
            do {
               if (!var2.hasNext()) {
                  return var1.toString();
               }

               var3 = (String)var2.next();
            } while(!this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && (!this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp") || this.bean.getTriggerUpdatesOptimisticColumn(var3)));

            String var4 = this.bean.getOptimisticColumn(var3);
            String var5 = this.bean.getCmpField(var3, var4);
            this.curField = var5;
            var1.append("if (!" + this.isModifiedVar(var5) + ") {" + EOL);
            var1.append(this.setterMethodNameForField() + "(" + this.initialOptimisticValue(var3) + ");" + EOL);
            var1.append("}" + EOL);
            this.curField = null;
         }
      }
   }

   public String perhapsResetOurOptimisticColumnVariable() {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(true) {
            String var3;
            do {
               if (!var2.hasNext()) {
                  return var1.toString();
               }

               var3 = (String)var2.next();
            } while(!this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp"));

            String var4 = this.bean.getOptimisticColumn(var3);
            String var5 = this.bean.getCmpField(var3, var4);
            this.curField = var5;
            var1.append(this.curField + " = ");
            var1.append("this." + CodeGenUtils.snapshotNameForVar(this.curField));
            var1.append(";" + EOL);
            this.curField = null;
         }
      }
   }

   private String initialOptimisticValue(String var1) {
      String var2 = this.bean.getVerifyColumns(var1);
      if (var2.equalsIgnoreCase("version")) {
         return "new Long(" + this.bean.getVersionColumnInitialValue(var1) + ")";
      } else if (var2.equalsIgnoreCase("timestamp")) {
         return "new java.sql.Timestamp(System.currentTimeMillis())";
      } else {
         throw new AssertionError("Invalid verify-columns: " + var2);
      }
   }

   public String updateOptimisticField(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (this.bean.getVerifyColumns(var1).equalsIgnoreCase("version")) {
         var2.append(this.setMethodNameForField() + "__WL_optimisticField" + "(new Long(" + this.getMethodNameForField() + "().longValue()+1));" + EOL);
      } else {
         var2.append("long cur = System.currentTimeMillis();" + EOL);
         var2.append("if ((cur-1000)<=" + this.getMethodNameForField() + "().getTime()) {" + EOL);
         var2.append("cur = " + this.getMethodNameForField() + "().getTime()+1000;" + EOL);
         var2.append("}" + EOL);
         var2.append(this.setMethodNameForField() + "__WL_optimisticField" + "(new java.sql.Timestamp(cur));" + EOL);
      }

      return var2.toString();
   }

   public String perhapsUpdateOptimisticField() {
      if (this.bd.isOptimistic()) {
         String var1 = this.curTableName;
         boolean var2 = this.bean.getVerifyColumns(var1).equalsIgnoreCase("timestamp");
         boolean var3 = this.bean.getVerifyColumns(var1).equalsIgnoreCase("version");
         boolean var4 = this.bean.getTriggerUpdatesOptimisticColumn(var1);
         if ((var3 || var2) && (!var2 || !var4)) {
            StringBuffer var5 = new StringBuffer();
            String var6 = this.bean.getOptimisticColumn(var1);
            String var7 = this.bean.getCmpField(var1, var6);
            this.curField = var7;
            var5.append("if (" + this.isLoadedVar(var7) + " || " + this.isModifiedVar(var7) + ") {" + EOL);
            var5.append(this.updateOptimisticField(var1));
            if (!this.bean.getTriggerUpdatesOptimisticColumn(var1)) {
               if (this.useVersionOrTimestampCheckingForBlobClob(var1)) {
                  var5.append("if (" + this.countVar() + " > 0) sb.append(\", \");" + EOL);
                  var5.append("sb.append(\" " + var6 + " = ? \");" + EOL);
               } else {
                  var5.append("sb.append(\", " + var6 + " = ? \");" + EOL);
               }
            }

            var5.append("}" + EOL);
            this.curField = null;
            return var5.toString();
         }
      }

      return "";
   }

   public String perhapsSetUpdateOptimisticFieldStringForBatch() {
      if (this.bd.isOptimistic()) {
         String var1 = this.curTableName;
         if (this.bean.getVerifyColumns(var1).equalsIgnoreCase("version") || this.bean.getVerifyColumns(var1).equalsIgnoreCase("timestamp")) {
            StringBuffer var2 = new StringBuffer();
            String var3 = this.bean.getOptimisticColumn(var1);
            String var4 = this.bean.getCmpField(var1, var3);
            this.curField = var4;
            var2.append("if (" + this.isLoadedVar(var4) + " || " + this.isModifiedVar(var4) + ") {" + EOL);
            var2.append(this.isModifiedVar(var4) + " = true;" + EOL);
            if (!this.bean.getTriggerUpdatesOptimisticColumn(var1)) {
               if (this.useVersionOrTimestampCheckingForBlobClob(var1)) {
                  var2.append("if (" + this.countVar() + " > 0) sb.append(\", \");" + EOL);
                  var2.append("sb.append(\" " + var3 + " = ? \");" + EOL);
               } else {
                  var2.append("sb.append(\", " + var3 + " = ? \");" + EOL);
               }
            }

            var2.append("}" + EOL);
            this.curField = null;
            return var2.toString();
         }
      }

      return "";
   }

   public String perhapsUpdateOptimisticFieldForBatch() {
      if (this.bd.isOptimistic()) {
         String var1 = this.curTableName;
         boolean var2 = this.bean.getVerifyColumns(var1).equalsIgnoreCase("timestamp");
         boolean var3 = this.bean.getVerifyColumns(var1).equalsIgnoreCase("version");
         boolean var4 = this.bean.getTriggerUpdatesOptimisticColumn(var1);
         if ((var3 || var2) && (!var2 || !var4)) {
            StringBuffer var5 = new StringBuffer();
            String var6 = this.bean.getOptimisticColumn(var1);
            String var7 = this.bean.getCmpField(var1, var6);
            this.curField = var7;
            var5.append("if (" + this.isLoadedVar(var7) + " || " + this.isModifiedVar(var7) + ") {" + EOL);
            var5.append(this.updateOptimisticField(var1));
            var5.append("}" + EOL);
            this.curField = null;
            return var5.toString();
         }
      }

      return "";
   }

   public String perhapsUpdateOptimisticFieldInTables() {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         if (this.bean.hasMultipleTables()) {
            for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
               this.curTableIndex = var2;
               this.curTableName = this.bean.tableAt(this.curTableIndex);
               var1.append("if (" + this.tableModifiedVar(this.curTableName) + ") {" + EOL);
               var1.append(this.perhapsUpdateOptimisticFieldForBatch());
               var1.append("}" + EOL);
            }
         } else {
            var1.append(this.perhapsUpdateOptimisticFieldForBatch());
         }

         return var1.toString();
      }
   }

   public String declareByteArrayVars() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.variableToField.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!this.bean.isBlobCmpColumnTypeForField(var3) && !this.bean.isClobCmpColumnTypeForField(var3) && !this.bd.getPrimaryKeyFieldNames().contains(var3)) {
            Class var4 = this.getVariableClass(var3);
            if (!this.bean.isValidSQLType(var4)) {
               var1.append("byte[] " + this.byteArrayVar(var3) + " = null;" + EOL);
            }
         }
      }

      return var1.toString();
   }

   public String perhapsTransactionParam() {
      return this.bd.isOptimistic() && this.bean.getDatabaseType() != 1 ? this.txVar() : "";
   }

   public String perhapsSuspendTransaction() {
      if (this.bd.isOptimistic() && this.bean.getDatabaseType() != 1) {
         StringBuffer var1 = new StringBuffer();
         var1.append(EOL);
         var1.append("Integer isolation = ").append(this.pmVar()).append(".getTransactionIsolationLevel();").append(EOL);
         var1.append("boolean shouldSuspendTx = (isolation != null && (").append(EOL);
         var1.append("isolation.intValue() != ").append("Connection.TRANSACTION_READ_UNCOMMITTED &&").append(EOL);
         var1.append("isolation.intValue() != ").append("Connection.TRANSACTION_READ_COMMITTED));").append(EOL);
         var1.append("javax.transaction.Transaction " + this.txVar() + "= null;" + EOL);
         var1.append(EOL);
         var1.append("try {" + EOL);
         var1.append("if (shouldSuspendTx) {").append(EOL);
         var1.append(this.txVar() + " = " + this.pmVar() + ".suspendTransaction();" + EOL);
         var1.append("}").append(EOL);
         return var1.toString();
      } else {
         return "";
      }
   }

   public String perhapsResumeTransaction() {
      if (this.bd.isOptimistic() && this.bean.getDatabaseType() != 1) {
         StringBuffer var1 = new StringBuffer();
         var1.append("} finally {" + EOL);
         var1.append("try {" + EOL);
         var1.append("if (shouldSuspendTx) {").append(EOL);
         var1.append(this.pmVar() + ".resumeTransaction(" + this.txVar() + ");" + EOL);
         var1.append("}").append(EOL);
         var1.append("} catch (weblogic.ejb20.persistence.spi.PersistenceRuntimeException e) {" + EOL);
         var1.append(this.pmVar() + ".releaseResources(__WL_con, " + this.stmtVar() + ", " + this.rsVar() + ");" + EOL);
         var1.append("throw e;" + EOL);
         var1.append("}" + EOL);
         var1.append("}" + EOL);
         return var1.toString();
      } else {
         return "";
      }
   }

   public String perhapsGetNullSnapshotVariables() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isOptimistic()) {
         var1.append("return null;");
         return var1.toString();
      } else {
         var1.append("Collection nullSnapshotVariables = new HashSet();" + EOL);
         Iterator var2 = this.bean.getTables().iterator();

         while(true) {
            label38:
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               String var5;
               if (!this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp")) {
                  Iterator var10 = this.variableToField.keySet().iterator();

                  while(true) {
                     String var11;
                     do {
                        do {
                           do {
                              do {
                                 if (!var10.hasNext()) {
                                    continue label38;
                                 }

                                 var5 = (String)var10.next();
                                 var11 = (String)this.variableToField.get(var5);
                              } while(this.bean.isBlobCmpColumnTypeForField(var5));
                           } while(this.bean.isClobCmpColumnTypeForField(var5));
                        } while(this.bd.getPrimaryKeyFieldNames().contains(var11));
                     } while((!this.bean.hasCmpField(var11) || !this.bean.getTableForCmpField(var11).equals(var3)) && (this.bean.hasCmpField(var11) || !this.bean.getTableForVariable(var5).equals(var3)));

                     Class var12 = this.getVariableClass(var5);
                     String var13 = CodeGenUtils.snapshotNameForVar(var5);
                     if (!var12.isPrimitive()) {
                        var1.append("if ( this." + var13 + " == null) {" + EOL);
                        var1.append("  nullSnapshotVariables.add(\"" + var5 + "\");" + EOL);
                        var1.append("}" + EOL);
                     }
                  }
               } else {
                  String var4 = this.bean.getOptimisticColumn(var3);
                  var5 = this.bean.getCmpField(var3, var4);
                  Class var6 = this.getVariableClass(var5);
                  String var7 = CodeGenUtils.snapshotNameForVar(var5);
                  Class var8 = CodeGenUtils.getSnapshotClass(this.bean, var6);
                  String var9 = ClassUtils.classToJavaSourceType(var8);
                  if (!var6.isPrimitive()) {
                     var1.append("if ( this." + var7 + " == null) {" + EOL);
                     var1.append("  nullSnapshotVariables.add(\"" + var5 + "\");" + EOL);
                     var1.append("}" + EOL);
                  }
               }
            }

            var1.append("return nullSnapshotVariables;" + EOL);
            return var1.toString();
         }
      }
   }

   public String perhapsInitializeSnapshotVars() {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(true) {
            label34:
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               String var5;
               if (!this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp")) {
                  Iterator var11 = this.variableToField.keySet().iterator();

                  while(true) {
                     String var12;
                     do {
                        do {
                           do {
                              do {
                                 if (!var11.hasNext()) {
                                    var1.append(EOL);
                                    continue label34;
                                 }

                                 var5 = (String)var11.next();
                                 var12 = (String)this.variableToField.get(var5);
                              } while(this.bean.isBlobCmpColumnTypeForField(var5));
                           } while(this.bean.isClobCmpColumnTypeForField(var5));
                        } while(this.bd.getPrimaryKeyFieldNames().contains(var12));
                     } while((!this.bean.hasCmpField(var12) || !this.bean.getTableForCmpField(var12).equals(var3)) && (this.bean.hasCmpField(var12) || !this.bean.getTableForVariable(var5).equals(var3)));

                     Class var13 = this.getVariableClass(var5);
                     String var14 = CodeGenUtils.snapshotNameForVar(var5);
                     Class var15 = CodeGenUtils.getSnapshotClass(this.bean, var13);
                     String var10 = ClassUtils.classToJavaSourceType(var15);
                     var1.append(var14 + " = " + ClassUtils.getDefaultValue(var15) + ";" + EOL);
                  }
               } else {
                  String var4 = this.bean.getOptimisticColumn(var3);
                  var5 = this.bean.getCmpField(var3, var4);
                  Class var6 = this.getVariableClass(var5);
                  String var7 = CodeGenUtils.snapshotNameForVar(var5);
                  Class var8 = CodeGenUtils.getSnapshotClass(this.bean, var6);
                  String var9 = ClassUtils.classToJavaSourceType(var8);
                  var1.append(var7 + " = " + ClassUtils.getDefaultValue(var8) + ";" + EOL);
               }
            }

            return var1.toString();
         }
      }
   }

   private String takeSnapshotForVar(String var1, String var2, boolean var3) {
      if (!this.bean.isBlobCmpColumnTypeForField(var2) && !this.bean.isClobCmpColumnTypeForField(var2)) {
         StringBuffer var4 = new StringBuffer();
         if (!this.bd.getPrimaryKeyFieldNames().contains(var2)) {
            Class var5 = this.getVariableClass(var2);
            String var6 = CodeGenUtils.snapshotNameForVar(var2);
            Class var7 = CodeGenUtils.getSnapshotClass(this.bean, var5);
            String var8 = ClassUtils.classToJavaSourceType(var7);
            String var9 = null;
            if (var3) {
               var9 = var1 + "." + MethodUtils.getMethodName(var2) + "()";
            } else {
               var9 = var1 + "." + var2;
            }

            if (Date.class.isAssignableFrom(var5)) {
               var4.append("if (" + var9 + "==null) {" + EOL);
               var4.append(var1 + "." + var6 + " = null;" + EOL);
               var4.append("}" + EOL);
               var4.append("else {" + EOL);
               var4.append(var1 + "." + var6 + " = " + "(" + var5.getName() + ")" + var9 + ".clone();" + EOL);
               var4.append("}" + EOL);
            } else {
               var4.append(var1 + "." + var6 + " = " + var9 + ";" + EOL);
            }
         }

         return var4.toString();
      } else {
         return "";
      }
   }

   public String perhapsTakeSnapshot() {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(true) {
            label54:
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               boolean var4 = this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp");
               boolean var5 = this.bean.getVerifyColumns(var3).equalsIgnoreCase("version");
               String var7;
               if (!var5 && !var4) {
                  Iterator var10 = this.variableToField.keySet().iterator();
                  var1.append(EOL);

                  while(true) {
                     while(true) {
                        String var8;
                        do {
                           do {
                              if (!var10.hasNext()) {
                                 continue label54;
                              }

                              var7 = (String)var10.next();
                              var8 = (String)this.variableToField.get(var7);
                              if (this.bean.isBlobCmpColumnTypeForField(var7) || this.bean.isClobCmpColumnTypeForField(var7)) {
                                 return "";
                              }
                           } while(this.bd.getPrimaryKeyFieldNames().contains(var8));
                        } while((!this.bean.hasCmpField(var8) || !this.bean.getTableForCmpField(var8).equals(var3)) && (this.bean.hasCmpField(var8) || !this.bean.getTableForVariable(var7).equals(var3)));

                        Class var9 = this.getVariableClass(var7);
                        var1.append("if (" + this.isLoadedVar(var8) + ")" + EOL);
                        if (this.bean.isValidSQLType(var9)) {
                           var1.append(this.takeSnapshotForVar("this", var7, !this.bd.isBeanClassAbstract() && this.bean.hasCmpField(var7)));
                        } else {
                           var1.append(CodeGenUtils.snapshotNameForVar(var7) + " = " + this.byteArrayVar(var7) + ";" + EOL);
                        }
                     }
                  }
               } else if (!var4 || !this.bean.getTriggerUpdatesOptimisticColumn(var3)) {
                  String var6 = this.bean.getOptimisticColumn(var3);
                  var7 = this.bean.getCmpField(var3, var6);
                  var1.append("if (" + this.isLoadedVar(var7) + " || " + this.isModifiedVar(var7) + ") {" + EOL);
                  var1.append(this.takeSnapshotForVar("this", var7, !this.bd.isBeanClassAbstract() && this.bean.hasCmpField(var7)));
                  var1.append("}" + EOL);
               }
            }

            return var1.toString();
         }
      }
   }

   public String perhapsInvalidateOptimisticColumnField() throws CodeGenerationException {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp") && this.bean.getTriggerUpdatesOptimisticColumn(var3)) {
               String var4 = this.bean.getOptimisticColumn(var3);
               String var5 = this.bean.getCmpField(var3, var4);
               var1.append(this.isLoadedVar(var5) + " = false;" + EOL);
            }
         }

         if (var1.length() > 0 && this.bd.getCacheBetweenTransactions()) {
            FieldGroup var6 = this.bean.getFieldGroup("optimisticTimestampTriggerGroup");
            var1.append("int oldState = __WL_method_state;" + EOL);
            var1.append("try {" + EOL);
            var1.append("__WL_method_state = STATE_EJBSTORE;" + EOL);
            var1.append(this.loadMethodName(this.getFieldGroupSuffix(var6)) + "();" + EOL);
            var1.append("} finally {" + EOL);
            var1.append("__WL_method_state = oldState;" + EOL);
            var1.append("}" + EOL);
         }

         return var1.toString();
      }
   }

   public String perhapsReloadOptimisticColumn() throws CodeGenerationException {
      if (!this.bd.isOptimistic()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = this.bean.getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp") && this.bean.getTriggerUpdatesOptimisticColumn(var3) && !this.bd.getCacheBetweenTransactions()) {
               String var4 = this.bean.getOptimisticColumn(var3);
               String var5 = this.bean.getCmpField(var3, var4);
               var1.append("if (!" + this.isLoadedVar(var5) + " && !" + this.isModifiedVar(var5) + ") {" + EOL);
               FieldGroup var6 = this.bean.getFieldGroup("optimisticTimestampTriggerGroup");
               var1.append(this.loadMethodName(this.getFieldGroupSuffix(var6)) + "();" + EOL);
               var1.append("}" + EOL);
            }
         }

         return var1.toString();
      }
   }

   public String perhapsConstructSnapshotPredicate() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.isOptimistic()) {
         String var2 = this.bean.tableAt(this.curTableIndex);
         var1.append(EOL);
         var1.append(this.snapshotBufferVar() + ".setLength(0);" + EOL);
         String var4;
         if (!this.bean.getVerifyColumns(var2).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var2).equalsIgnoreCase("timestamp")) {
            for(int var12 = 0; var12 < this.bean.getFieldCount(); ++var12) {
               var4 = this.isModifiedIndexToField(var12);
               int var5;
               if (this.bean.isCmpFieldName(var4)) {
                  if (!this.bean.isBlobCmpColumnTypeForField(var4) && !this.bean.isClobCmpColumnTypeForField(var4) && !this.bd.getPrimaryKeyFieldNames().contains(var4)) {
                     var5 = this.bean.getTableIndexForCmpField(var4);
                     if (var5 == -1) {
                        throw new CodeGenerationException("perhapsConstructSnapshotPredicate: Could not find tableIndex for cmp-field: '" + var4 + "'");
                     }

                     if (var5 == this.curTableIndex) {
                        Class var6 = this.bean.getCmpFieldClass(var4);
                        Class var7 = CodeGenUtils.getSnapshotClass(this.bean, var6);
                        var1.append("if (" + this.isLoadedVar(var4));
                        if (this.bean.getVerifyColumns(var2).equalsIgnoreCase("modified")) {
                           var1.append(" && " + this.isModifiedVar(var4));
                        }

                        var1.append(") {" + EOL);
                        if (var7.isPrimitive()) {
                           var1.append(this.snapshotBufferVar() + ".append(\" AND \" +" + this.pmVar() + ".getSnapshotPredicate(" + this.bean.getIsModifiedIndex(var4) + "));" + EOL);
                        } else {
                           var1.append(this.snapshotBufferVar() + ".append(\" AND \" +" + this.pmVar() + ".getSnapshotPredicate(" + this.bean.getIsModifiedIndex(var4) + ", " + "this." + CodeGenUtils.snapshotNameForVar(var4) + "));" + EOL);
                        }

                        var1.append("}" + EOL);
                     }
                  }
               } else if (this.bean.containsFkField(var4) && !this.bean.isForeignCmpField(var4)) {
                  var5 = this.bean.getTableIndexForCmrf(var4);
                  if (var5 == -1) {
                     throw new CodeGenerationException("perhapsConstructSnapshotPredicate: Could not find tableIndex for cmr-field: '" + var4 + "'");
                  }

                  if (var5 == this.curTableIndex) {
                     Iterator var13 = this.bean.getForeignKeyColNames(var4).iterator();
                     String var14 = (String)var13.next();
                     String var8 = this.bean.variableForField(var4, var2, var14);
                     Class var9 = this.bean.getForeignKeyColClass(var4, var14);
                     String var10 = CodeGenUtils.snapshotNameForVar(var8);
                     Class var11 = CodeGenUtils.getSnapshotClass(this.bean, var9);
                     var1.append("if (" + this.isLoadedVar(var4));
                     if (this.bean.getVerifyColumns(var2).equalsIgnoreCase("modified")) {
                        var1.append(" && " + this.isModifiedVar(var4));
                     }

                     var1.append(") {" + EOL);
                     if (var11.isPrimitive()) {
                        var1.append(this.snapshotBufferVar() + ".append(\" AND \" +" + this.pmVar() + ".getSnapshotPredicate(" + this.bean.getIsModifiedIndex(var4) + "));" + EOL);
                     } else {
                        var1.append(this.snapshotBufferVar() + ".append(\" AND \" +" + this.pmVar() + ".getSnapshotPredicate(" + this.bean.getIsModifiedIndex(var4) + ", " + "this." + var10 + "));" + EOL);
                     }

                     var1.append("}" + EOL);
                  }
               }
            }
         } else {
            String var3 = this.bean.getOptimisticColumn(var2);
            var4 = this.bean.getCmpField(var2, var3);
            var1.append("if (" + this.isLoadedVar(var4) + " || " + this.isModifiedVar(var4) + ") {" + EOL);
            var1.append(this.snapshotBufferVar() + ".append(\" AND \" +" + this.pmVar() + ".getSnapshotPredicate(" + this.bean.getIsModifiedIndex(var4) + ", " + "this." + CodeGenUtils.snapshotNameForVar(var4) + "));" + EOL);
            var1.append("}" + EOL);
         }
      }

      return var1.toString();
   }

   public String perhapsAddSnapshotPredicate() {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.isOptimistic()) {
         var1.append("+ " + this.snapshotBufferVar() + ".toString()");
      }

      return var1.toString();
   }

   public String perhapsSetSnapshotParameters() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.isOptimistic()) {
         var1.append(this.setSnapshotParams());
      }

      return var1.toString();
   }

   public String setSnapshotParams() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      String var2 = this.bean.tableAt(this.curTableIndex);
      String var4;
      if (!this.bean.getVerifyColumns(var2).equalsIgnoreCase("version") && !this.bean.getVerifyColumns(var2).equalsIgnoreCase("timestamp")) {
         for(int var10 = 0; var10 < this.bean.getFieldCount(); ++var10) {
            var4 = this.isModifiedIndexToField(var10);

            assert var4 != null;

            int var11;
            if (this.bean.isCmpFieldName(var4)) {
               if (!this.bd.getPrimaryKeyFieldNames().contains(var4) && !this.bean.isBlobCmpColumnTypeForField(var4) && !this.bean.isClobCmpColumnTypeForField(var4)) {
                  var11 = this.bean.getTableIndexForCmpField(var4);
                  if (var11 == -1) {
                     throw new CodeGenerationException("perhapsSetSnapshotParameters: Could not find tableIndex for field: '" + var4 + "'");
                  }

                  if (var11 == this.curTableIndex) {
                     String var12 = CodeGenUtils.snapshotNameForVar(var4);
                     var1.append("if (" + this.isLoadedVar(var4));
                     if (this.bean.getVerifyColumns(var2).equalsIgnoreCase("modified")) {
                        var1.append(" && " + this.isModifiedVar(var4));
                     }

                     var1.append(") {" + EOL);
                     var1.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var12 + "' using column \" +" + this.numVar() + " + \". Value is \" + this." + var12 + ");" + EOL);
                     this.addSnapshotBinding(var1, var4, "this", this.numVar(), this.currStmtArrayVar());
                     var1.append("}" + EOL);
                  }
               }
            } else {
               var11 = this.bean.getTableIndexForCmrf(var4);
               if (var11 == -1) {
                  throw new CodeGenerationException("perhapsSetSnapshotParameters: Could not find tableIndex for field: '" + var4 + "'");
               }

               if (var11 == this.curTableIndex) {
                  Iterator var6 = this.bean.getForeignKeyColNames(var4).iterator();

                  while(var6.hasNext()) {
                     String var7 = (String)var6.next();
                     String var8 = this.bean.variableForField(var4, var2, var7);
                     String var9 = CodeGenUtils.snapshotNameForVar(var8);
                     var1.append("if (" + this.isLoadedVar(var4));
                     if (this.bean.getVerifyColumns(var2).equalsIgnoreCase("modified")) {
                        var1.append(" && " + this.isModifiedVar(var4));
                     }

                     var1.append(") {" + EOL);
                     var1.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var9 + "' using column \" +" + this.numVar() + " + \". Value is \" + this." + var9 + ");" + EOL);
                     this.addSnapshotBinding(var1, var8, "this", this.numVar(), this.currStmtArrayVar());
                     var1.append("}" + EOL);
                  }
               }
            }
         }
      } else {
         String var3 = this.bean.getOptimisticColumn(var2);
         var4 = this.bean.getCmpField(var2, var3);
         String var5 = CodeGenUtils.snapshotNameForVar(var4);
         var1.append("if (" + this.isLoadedVar(var4) + " || " + this.isModifiedVar(var4) + ") {" + EOL);
         var1.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var5 + "' using column \" +" + this.numVar() + " + \". Value is \" + this." + var5 + ");" + EOL);
         this.addSnapshotBinding(var1, var4, "this", this.numVar(), this.currStmtArrayVar());
         var1.append("}" + EOL);
      }

      return var1.toString();
   }

   private void addSnapshotBinding(StringBuffer var1, String var2, String var3, String var4, String var5) {
      if (debugLogger.isDebugEnabled()) {
         debug("Adding a snapshot binding: ");
         debug("\t\tvar = " + var2);
         debug("\t\tobj = " + var3);
         debug("\t\tparamIdx = " + var4);
      }

      if (!this.bean.isBlobCmpColumnTypeForField(var2) && !this.bean.isClobCmpColumnTypeForField(var2)) {
         Class var6 = this.getVariableClass(var2);
         String var7 = CodeGenUtils.snapshotNameForVar(var2);
         Class var8 = CodeGenUtils.getSnapshotClass(this.bean, var6);
         if (!var8.isPrimitive()) {
            var1.append("if (" + var7 + "!= null) {" + EOL);
         }

         this.snapshotBindingBody(var1, var3, var2, var6, var4, var5);
         var1.append(this.numVar() + "++;" + EOL);
         if (!var8.isPrimitive()) {
            var1.append("}" + EOL);
         }

      } else {
         var1.append("  ");
      }
   }

   private void snapshotBindingBody(StringBuffer var1, String var2, String var3, Class var4, String var5, String var6) {
      String var7 = CodeGenUtils.snapshotNameForVar(var3);
      CodeGenUtils.getSnapshotClass(this.bean, var4);
      if (this.bean.isValidSQLType(var4) && !ClassUtils.isByteArray(var4)) {
         String var9 = StatementBinder.getStatementTypeNameForClass(var4);
         if (RDBMSUtils.isOracleNLSDataType(this.bean, var3)) {
            var1.append("if(").append(var6).append(" instanceof oracle.jdbc.OraclePreparedStatement) {" + EOL);
            var1.append("((oracle.jdbc.OraclePreparedStatement)").append(var6).append(").setFormOfUse(").append(var5).append(", oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);").append(EOL);
            var1.append("}" + EOL);
         }

         var1.append(var6);
         var1.append(".set" + var9 + "(");
         var1.append(var5).append(", ");
         if (var4 == Character.TYPE) {
            var1.append("String.valueOf(" + var2 + "." + var7 + ")");
         } else if (var4 == Character.class) {
            var1.append("String.valueOf(" + var2 + "." + var7 + ".charValue())");
         } else if (var4 == Date.class) {
            var1.append("new java.sql.Timestamp(");
            var1.append(var2 + ".");
            var1.append(MethodUtils.convertToPrimitive(var4, var7));
            var1.append(".getTime())");
         } else {
            var1.append(var2 + ".");
            var1.append(MethodUtils.convertToPrimitive(var4, var7));
         }

         var1.append(");" + EOL);
      } else if (!ClassUtils.isByteArray(var4) || !"SybaseBinary".equalsIgnoreCase(this.bean.getCmpColumnTypeForField(var3)) && !this.perhapsSybaseBinarySetForAnyCmpField()) {
         var1.append("InputStream inputStream  = new ByteArrayInputStream(" + var7 + ");" + EOL);
         var1.append(var6 + ".setBinaryStream(" + var5 + ", inputStream, " + var7 + ".length);" + EOL);
      } else {
         var1.append(var6 + ".setBytes(" + var5 + "," + var7 + ");" + EOL);
      }

   }

   public String throwOperationFailedException() {
      return this.bd.isOptimistic() ? " Loggable l = EJBLogger.logoptimisticUpdateFailedLoggable( \"" + this.bd.getEJBName() + "\"," + this.pkVar() + ".toString());" + EOL + " throw new OptimisticConcurrencyException(l.getMessage());" : " Loggable l = EJBLogger.logbeanDoesNotExistLoggable(\"" + this.bd.getEJBName() + "\"," + this.pkVar() + ".toString()); " + EOL + "throw new NoSuchEntityException(l.getMessage()); ";
   }

   public String primaryFieldClassesArray(String var1) {
      CMPBeanDescriptor var2 = null;
      if (this.bean.isForeignKeyField(var1) && this.bean.containsFkField(var1)) {
         var2 = this.bean.getRelatedDescriptor(var1);
      } else {
         var2 = this.bd;
      }

      return "new Class[] {" + var2.getPrimaryKeyClass().getName() + ".class }";
   }

   public String assignHomeVars() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      Iterator var3 = this.bean.getRemoteFieldNames().iterator();
      if (var3.hasNext()) {
         var1.append("try {");
         var2 = true;
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append("Context ctx = (Context)" + this.jctxVar() + ".lookup(\"java:comp/env\");" + EOL);
         var1.append("assert (ctx!=null);" + EOL);
         var1.append(this.debugSay() + "(\"Listing contents of java:comp/env\");" + EOL);
         var1.append("NamingEnumeration nenum = ctx.list(\"\");" + EOL);
         var1.append("while (nenum.hasMore()) {" + EOL);
         var1.append("NameClassPair ncp = (NameClassPair)nenum.next();" + EOL);
         var1.append(this.debugSay() + "(\"name bound- \" + ncp.getName());" + EOL);
         var1.append("}" + EOL);
         var1.append("}" + EOL);
      }

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         EjbEntityRef var5 = this.bean.getEjbEntityRef(var4);
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append(this.debugSay() + "(\"Looking up name- " + var5.getEjbRefName() + "\");" + EOL);
         var1.append("}" + EOL);
         var1.append(this.homeVar(var4) + " = (" + var5.getHome() + ")");
         var1.append(this.jctxVar() + ".lookup(\"java:comp/env/");
         var1.append(var5.getEjbRefName() + "\");" + EOL);
      }

      if (var2) {
         var1.append(this.parse(this.getProductionRule("standardCatch")));
      }

      return var1.toString();
   }

   public String assignManagerVars() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      Set var3 = this.declaredManagerVars.keySet();

      for(Iterator var4 = var3.iterator(); var4.hasNext(); var2 = true) {
         String var5 = (String)var4.next();
         String var6 = (String)this.declaredManagerVars.get(var5);
         HashMap var7 = new HashMap();
         if (var7.containsKey(var6)) {
            var1.append(this.bmVar(var5)).append(" = ");
            var1.append(var7.get(var6)).append(";").append(EOL);
         } else {
            var1.append(this.bmVar(var5) + " = ");
            var1.append("(CMPBeanManager)bmMap.get(\"");
            var1.append(var6);
            var1.append("\");" + EOL);
            var1.append("assert (bmMap.get(\"");
            var1.append(var6);
            var1.append("\")!=null);" + EOL + EOL);
            var7.put(var6, this.bmVar(var5));
         }
      }

      if (var2) {
         return "try {" + var1.toString() + this.parse(this.getProductionRule("standardCatch"));
      } else {
         return "";
      }
   }

   public String assignEjbSelectMethodVars() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      Iterator var3 = this.finderList.iterator();

      while(true) {
         while(true) {
            Finder var4;
            do {
               if (!var3.hasNext()) {
                  if (var2) {
                     var1.append("}");
                     var1.append(EOL);
                     return var1.toString();
                  }

                  return "";
               }

               var4 = (Finder)var3.next();
            } while(var4.getQueryType() != 4 && var4.getQueryType() != 2 && (!var4.isSelect() || !(var4 instanceof SqlFinder)));

            if (!var2) {
               var1.append("static {");
               var1.append(EOL);
               var1.append("Method m = null;");
               var1.append(EOL);
               var2 = true;
            }

            if (var4.isSelect() && var4 instanceof SqlFinder) {
               String var12 = this.bean.getCMPBeanDescriptor().getGeneratedBeanInterfaceName();
               var1.append("try {");
               var1.append(EOL);
               var1.append("m = ");
               var1.append(var12);
               var1.append(".class.getMethod(\"");
               var1.append(var4.getName());
               var1.append("\", ");
               var1.append("new Class[] { ");
               Class[] var14 = var4.getParameterClassTypes();

               for(int var16 = 0; var16 < var14.length; ++var16) {
                  var1.append(ClassUtils.classToJavaSourceType(var14[var16]));
                  var1.append(".class");
                  if (var16 < var14.length - 1) {
                     var1.append(", ");
                  }
               }

               var1.append("});" + EOL);
               var1.append("} catch (NoSuchMethodException ignore) {");
               var1.append(EOL);
               var1.append("m = null;");
               var1.append(EOL);
               var1.append("}");
               var1.append(EOL);
               var1.append(this.ejbSelectMDName(var4));
               var1.append(" = m;");
               var1.append(EOL);
            } else {
               RDBMSBean var5 = var4.getSelectBeanTarget();
               if (var5 == null) {
                  Loggable var13 = EJBLogger.logGotNullXForFinderLoggable("getSelectBeanTarget", var4.toString());
                  throw new CodeGenerationException(var13.getMessage());
               }

               CMPBeanDescriptor var6 = (CMPBeanDescriptor)this.beanMap.get(var5.getEjbName());
               if (var6 == null) {
                  Loggable var15 = EJBLogger.logGotNullBeanFromBeanMapLoggable(var5.getEjbName());
                  throw new CodeGenerationException(var15.getMessage());
               }

               String var7 = var6.getGeneratedBeanInterfaceName();
               var1.append("try {");
               var1.append(EOL);
               var1.append("m = ");
               var1.append(var7);
               var1.append(".class.getMethod(\"");
               var1.append(this.ejbSelectMDName(var4));
               var1.append("\", ");
               var1.append(EOL);
               var1.append("         ");
               var1.append("new Class[] { ");
               StringBuffer var8 = new StringBuffer();
               Iterator var9 = var4.getExternalMethodAndInEntityParmList().iterator();

               while(var9.hasNext()) {
                  ParamNode var10 = (ParamNode)var9.next();
                  Class var11 = var10.getParamClass();
                  var8.append(var11.getName());
                  var8.append(".class, ");
                  var8.append(EOL);
               }

               if (var8.length() > 2) {
                  var8.setLength(var8.length() - 2);
               }

               var1.append(var8.toString());
               var1.append("} );");
               var1.append("} catch (NoSuchMethodException ignore) {");
               var1.append(EOL);
               var1.append("m = null;");
               var1.append(EOL);
               var1.append("}");
               var1.append(EOL);
               var1.append(this.ejbSelectMDName(var4));
               var1.append(" = m;");
               var1.append(EOL + EOL);
            }
         }
      }
   }

   public String fieldNameGetPrimaryKey() {
      return this.fieldNameForField() + ".getPrimaryKey()";
   }

   public String fieldVarGetPrimaryKey() {
      return this.fieldVarForField() + ".getPrimaryKey()";
   }

   public String fieldNameForField() {
      return this.curField;
   }

   public String assignFieldVarForFieldWithFieldNameForField() {
      return this.fieldVarForField() + " = " + this.fieldNameForField() + ";";
   }

   public String assignFieldVarForFieldWithAllocatedOneToManySet() {
      return this.fieldVarForField() + " = " + this.allocateOneToManySet() + ";";
   }

   public String assignFieldVarForFieldWithOneToManySetClone() {
      return this.fieldVarForField() + " = (Set)(((" + this.collectionClassForField() + ")" + EOL + "      " + this.fieldVarForField() + ").clone());";
   }

   public String assignFieldVarForFieldWithAllocatedManyToManySet() {
      return this.fieldVarForField() + " = " + this.allocateManyToManySet() + ";";
   }

   public String assignFieldVarForFieldWithNull() {
      return this.fieldVarForField() + " = null;";
   }

   public String setterMethodNameForField() {
      return "set" + this.curField.substring(0, 1).toUpperCase(Locale.ENGLISH) + this.curField.substring(1);
   }

   public String doSetMethodNameForField() {
      return MethodUtils.doSetMethodName(this.curField);
   }

   public String setRestMethodNameForField() {
      assert this.curField != null;

      return MethodUtils.setRestMethodName(this.curField);
   }

   public String getMethodNameForField() {
      return MethodUtils.getMethodName(this.curField);
   }

   public String setMethodNameForField() {
      return MethodUtils.setMethodName(this.curField);
   }

   public String getRelatedMethodNameForField() {
      String var1 = this.bean.getRelatedFieldName(this.curField);
      return MethodUtils.getMethodName(var1);
   }

   public String javaClassCommonMethodPrefix() {
      String var1 = this.method.getName();
      if (var1.startsWith("get") || var1.startsWith("set")) {
         String var2 = MethodUtils.decapitalize(var1.substring(3));
         if (this.cmpFieldNames.contains(var2) || this.cmrFieldNames.contains(var2)) {
            return "__WL_internal_";
         }
      }

      return "__WL_super_";
   }

   public String declareCmpGettersAndSetters() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.cmpFieldNames.iterator();

      while(var2.hasNext()) {
         this.curField = (String)var2.next();
         var1.append(this.parse(this.getProductionRule("cmpGetMethod")));
         var1.append(this.parse(this.getProductionRule("cmpSetMethod")));
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String cmpSetMethodGuard() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("Loggable l = EJBLogger.logCannotCallSetForReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
         var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
      } else {
         String var3;
         if (this.bean.isDbmsDefaultValueField(this.curField)) {
            var1.append("if (__WL_method_state==STATE_EJB_CREATE ||");
            var1.append(EOL);
            if (!this.bean.getDelayInsertUntil().equals("ejbCreate")) {
               var1.append("__WL_method_state==STATE_EJB_POSTCREATE ||");
               var1.append(EOL);
            }

            Iterator var2 = this.bd.getPrimaryKeyFieldNames().iterator();

            while(var2.hasNext()) {
               var3 = (String)var2.next();
               var1.append(this.isModifiedVar(var3));
               if (var2.hasNext()) {
                  var1.append("||").append(EOL);
               }
            }

            var1.append(") {" + EOL);
            var1.append("Loggable l = EJBLogger.logCannotCallSetOnDBMSDefaultFieldBeforeInsertLoggable();");
            var1.append(EOL);
            var1.append("throw new IllegalStateException(l.getMessage());");
            var1.append(EOL);
            var1.append("}" + EOL);
         }

         if (this.bd.getPrimaryKeyFieldNames().contains(this.curField)) {
            var1.append("if (__WL_method_state!=STATE_EJB_CREATE) {" + EOL);
            var1.append("Loggable l = EJBLogger.logcannotCallSetOnPkLoggable();");
            var1.append("throw new IllegalStateException(l.getMessage());");
            var1.append("}" + EOL);
            var1.append(this.parse(this.getProductionRule("cmpSetMethodBody")));
         } else if (this.bean.isCmrMappedCmpField(this.curField)) {
            var1.append("Loggable l = EJBLogger.logcannotCallSetOnCmpCmrFieldLoggable();");
            var1.append("throw new EJBException(l.getMessage());" + EOL + EOL);
         } else if (this.bd.isOptimistic()) {
            boolean var7 = false;
            var3 = null;
            String var4 = null;
            String var5 = null;

            for(int var6 = 0; var6 < this.bean.tableCount(); ++var6) {
               var3 = this.bean.tableAt(var6);
               if (this.bean.getVerifyColumns(var3).equalsIgnoreCase("version") || this.bean.getVerifyColumns(var3).equalsIgnoreCase("timestamp")) {
                  var4 = this.bean.getOptimisticColumn(var3);
                  var5 = this.bean.getCmpField(var3, var4);
                  if (var5.equals(this.curField)) {
                     var7 = true;
                     break;
                  }
               }
            }

            String var8 = this.fieldNameForField();
            if (var7) {
               var1.append(this.setMethodNameForField() + "__WL_optimisticField(" + var8 + ");" + EOL);
               var1.append("if (!__WL_createAfterRemove) {" + EOL);
               var1.append("this." + CodeGenUtils.snapshotNameForVar(this.curField) + " = ");
               var1.append(var8 + ";" + EOL);
               var1.append("}" + EOL);
               var1.append("}" + EOL + EOL);
               var1.append("public void " + this.setMethodNameForField() + "__WL_optimisticField(" + this.fieldClassForCmpField() + " " + var8 + ") {" + EOL);
               var1.append(this.parse(this.getProductionRule("cmpSetMethodBody")));
            } else {
               var1.append(this.parse(this.getProductionRule("cmpSetMethodBodyForOptimistic")));
            }
         } else {
            var1.append(this.parse(this.getProductionRule("cmpSetMethodBody")));
         }
      }

      return var1.toString();
   }

   public String cmpSetMethodCheck() {
      assert this.curField != null;

      String var1 = "";
      if (this.bd.getPrimaryKeyFieldNames().contains(this.curField)) {
         return var1;
      } else {
         String var2 = this.fieldNameForField();
         StringBuffer var3 = new StringBuffer();
         CMPCodeGenerator.Output var4 = (CMPCodeGenerator.Output)this.currentOutput;
         CMPBeanDescriptor var5 = var4.getCMPBeanDescriptor();
         Class var6 = this.bean.getCmpFieldClass(this.curField);
         if (ClassUtils.isPrimitiveOrImmutable(var6)) {
            var3.append("this." + this.fieldNameForField());
            var3.append(" == " + var2 + "  ");
            var1 = var3.toString();
            var3.setLength(0);
            if (!var6.isPrimitive()) {
               var3.append("(" + var1 + " || (");
               var3.append("this." + this.fieldNameForField());
               var3.append("!=null && ");
               var3.append("this." + this.fieldNameForField());
               var3.append(".equals(" + var2 + ")))  ");
               var1 = var3.toString();
            }

            var1 = "if (" + var1 + " && " + this.isLoadedVarForField() + ") return;" + EOL;
         }

         return var1;
      }
   }

   public String trimStringTypes() {
      if (this.bean.isStringTrimmingEnabled()) {
         Class var1 = this.bean.getCmpFieldClass(this.curField);
         String var2 = this.fieldNameForField();
         StringBuffer var3;
         if (var1 == String.class) {
            var3 = new StringBuffer();
            var3.append("if(");
            var3.append(var2);
            var3.append("!= null) {");
            var3.append(EOL);
            var3.append(this.trimStringTypedValue(var2));
            var3.append("}");
            var3.append(EOL);
            return var3.toString();
         }

         if (this.bean.isCharArrayMappedToString(var1)) {
            var3 = new StringBuffer();
            var3.append("if(");
            var3.append(var2);
            var3.append("!= null) {");
            var3.append(EOL);
            var3.append("int i = ");
            var3.append(var2);
            var3.append(".length;\n");
            var3.append("while(i > 0 && Character.isWhitespace(");
            var3.append(var2);
            var3.append("[i-1])) {i--;}\n");
            var3.append("if(i<");
            var3.append(var2);
            var3.append(".length) {\n");
            var3.append("char[] temp = new char[i];\n");
            var3.append("for(int j=0;j<i;j++) {\n");
            var3.append("temp[j] = ");
            var3.append(var2);
            var3.append("[j];\n");
            var3.append("}\n");
            var3.append(var2);
            var3.append("= temp;\n");
            var3.append("}\n");
            var3.append("}\n");
            return var3.toString();
         }
      }

      return "";
   }

   public String trimStringTypedValue(String var1) {
      if (!this.bean.isStringTrimmingEnabled()) {
         return "";
      } else {
         StringBuffer var2 = new StringBuffer();
         var2.append("int i = ");
         var2.append(var1);
         var2.append(".length();");
         var2.append(EOL);
         var2.append("while(i > 0 && Character.isWhitespace(");
         var2.append(var1);
         var2.append(".charAt(i-1))) {i--;}");
         var2.append(EOL);
         var2.append("if(i<");
         var2.append(var1);
         var2.append(".length()) {");
         var2.append(EOL);
         var2.append(var1);
         var2.append(" = ");
         var2.append(var1);
         var2.append(".substring(0,i);");
         var2.append(EOL);
         var2.append("}");
         var2.append(EOL);
         return var2.toString();
      }
   }

   public String getFieldGroupSuffix(FieldGroup var1) {
      return "group" + var1.getIndex();
   }

   public String getMethodSuffix(String var1) {
      String var2 = this.bean.getGroupNameForCmpField(var1);
      return this.getFieldGroupSuffix(this.bean.getFieldGroup(var2));
   }

   public String implementGroupLoadMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      for(Iterator var2 = this.bean.getFieldGroups().iterator(); var2.hasNext(); var1.append("}" + EOL)) {
         this.curGroup = (FieldGroup)var2.next();
         var1.append(EOL);
         var1.append("// loadGroup method for the '" + this.curGroup.getName() + "' group." + EOL);
         var1.append("public void " + this.loadMethodName(this.getFieldGroupSuffix(this.curGroup)) + "() ");
         var1.append("throws Exception {" + EOL);
         if (this.groupColumnCount() > 0) {
            var1.append(this.parse(this.getProductionRule("implementGroupLoadMethodBody")));
         }
      }

      return var1.toString();
   }

   public String constructorExceptionList() {
      StringBuffer var1 = new StringBuffer();
      CMPCodeGenerator.Output var2 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      Class var4 = var3.getBeanClass();

      try {
         Constructor var5 = var4.getConstructor();

         assert var5 != null;

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

   public String ejbLoadExceptionList() {
      return this.ejbCallbackMethodExceptionList("ejbLoad");
   }

   public String ejbRemoveExceptionList() {
      return this.ejbCallbackMethodExceptionList("ejbRemove");
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
         throw new AssertionError("Unable to find " + var1 + " on class '" + var5.getName() + "'.");
      }

      return var2.toString();
   }

   private String declareBeanMethod(String var1, Class[] var2, boolean var3) {
      StringBuffer var4 = new StringBuffer();
      Class var5 = this.bd.getBeanClass();

      try {
         Method var6 = var5.getMethod(var1, var2);

         assert var6 != null;

         MethodSignature var7 = new MethodSignature(var6);
         var4.append(var7.toString());
      } catch (NoSuchMethodException var8) {
         if (var3) {
            throw new AssertionError("Unable to find '" + var1 + "' method on class '" + var5.getName() + "'.");
         }

         return null;
      }

      return var4.toString();
   }

   public String declareSetEntityContextMethod() {
      return this.declareBeanMethod("setEntityContext", new Class[]{EntityContext.class}, true);
   }

   public String declareEjbLoadMethod() {
      return this.declareBeanMethod("ejbLoad", new Class[0], true);
   }

   public String declareEjbStoreMethod() {
      return this.declareBeanMethod("ejbStore", new Class[0], true);
   }

   public String beanVarEjbStoreForField() {
      return this.beanVar() + ".ejbStore();";
   }

   private boolean beanHasEjbStoreMethod(CMPBeanDescriptor var1) {
      Class var2 = var1.getBeanClass();
      if (var2 != null) {
         try {
            Method var3 = var2.getMethod("ejbStore");
            return true;
         } catch (Exception var4) {
         }
      }

      return false;
   }

   public String declareEjbRemoveMethod() {
      return this.declareBeanMethod("ejbRemove", new Class[0], true);
   }

   public String declareEjbPassivateMethod() {
      return this.declareBeanMethod("ejbPassivate", new Class[0], true);
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

   public String returnPkPerhapsInsertBean() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      Class var2 = this.bd.getPrimaryKeyClass();
      var1.append(ClassUtils.classToJavaSourceType(var2) + " " + this.pkVar() + " = null;" + EOL);
      if (this.bean.getDelayInsertUntil().equals("ejbCreate")) {
         var1.append("if (!" + this.pmVar() + ".getOrderDatabaseOperations() || " + "TransactionHelper.getTransactionHelper().getTransaction()==null) {" + EOL);
         var1.append(this.pkVar() + " = (" + ClassUtils.classToJavaSourceType(var2) + ")" + this.createMethodName() + "();" + EOL);
         var1.append("} else {");
         var1.append(EOL);
      }

      var1.append(this.pkVar() + " = (" + ClassUtils.classToJavaSourceType(var2) + ") __WL_getPrimaryKey();" + EOL);
      if (this.bean.getDelayInsertUntil().equals("ejbCreate")) {
         var1.append("}");
         var1.append(EOL);
      }

      var1.append("return " + this.pkVar() + ";" + EOL);
      var1.append(this.perhapsCatchCreateException());
      if (!this.bean.getDelayInsertUntil().equals("ejbCreate") && this.bean.hasAutoKeyGeneration()) {
         var1.append("}" + EOL + EOL);
      } else {
         var1.append(this.parse(this.getProductionRule("standardCatch")));
      }

      return var1.toString();
   }

   public String perhapsCatchCreateException() {
      if (this.throwsCreateException(this.method)) {
         StringBuffer var1 = new StringBuffer();
         var1.append("} catch (javax.ejb.CreateException ce) {" + EOL);
         var1.append("throw ce;" + EOL);
         return var1.toString();
      } else {
         return "";
      }
   }

   public String perhapsInsertBean() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.bd.getPrimaryKeyClass();
      if (this.bean.getDelayInsertUntil().equals("ejbPostCreate")) {
         var1.append("if (!" + this.pmVar() + ".getOrderDatabaseOperations() || " + "TransactionHelper.getTransactionHelper().getTransaction()==null)" + EOL);
         var1.append(this.createMethodName() + "();" + EOL);
      }

      return var1.toString();
   }

   public String implementEjbCreateMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.bd.getBeanClass();
      Method[] var3 = var2.getMethods();
      Method var4 = null;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4 = var3[var5];
         if (var4.getName().startsWith("ejbCreate")) {
            this.setMethod(var4, (short)0);
            var1.append(this.method_signature_no_throws());
            var1.append(this.beanmethod_throws_clause());
            var1.append("{");
            var1.append(EOL);
            if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
               var1.append("Loggable l = EJBLogger.logCannotCreateReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
               var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
            } else {
               var1.append(this.parse(this.getProductionRule("ejbCreateMethodBody")));
            }

            var1.append("}");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   public String implementEjbPostCreateMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.bd.getBeanClass();
      Method[] var3 = var2.getMethods();
      Method var4 = null;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4 = var3[var5];
         if (var4.getName().startsWith("ejbPostCreate")) {
            this.setMethod(var4, (short)0);
            var1.append(this.method_signature_no_throws());
            var1.append(this.beanmethod_throws_clause());
            var1.append("{");
            var1.append(EOL);
            if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
               var1.append("Loggable l = EJBLogger.logCannotCreateReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
               var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
            } else {
               var1.append(this.parse(this.getProductionRule("ejbPostCreateMethod")));
            }

            var1.append("}");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   public String implementEjbRemoveMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.declareEjbRemoveMethod());
      var1.append(" {" + EOL);
      if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("Loggable l = EJBLogger.logCannotRemoveReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
         var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
      } else {
         var1.append(this.parse(this.getProductionRule("implementEjbRemoveMethodBody")));
      }

      var1.append("}");
      return var1.toString();
   }

   public String implementSetNullMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      for(Iterator var2 = this.bean.getCmrFieldNames().iterator(); var2.hasNext(); this.curField = null) {
         this.curField = (String)var2.next();
         if (this.bean.isOneToOneRelation(this.curField) && !this.bean.isRemoteField(this.curField)) {
            var1.append("public void " + MethodUtils.setNullMethodName(this.curField) + "(boolean ejbStore) {" + EOL);
            if (this.bean.isForeignKeyField(this.curField)) {
               var1.append(this.parse(this.getProductionRule("oneToOneSetNullBody_fkOwner")));
            } else {
               var1.append(this.parse(this.getProductionRule("oneToOneSetNullBody")));
            }

            var1.append("}" + EOL + EOL);
         }
      }

      return var1.toString();
   }

   public String implementMakeCascadeDelListMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.isCascadeDelete()) {
         var1.append(this.parse(this.getProductionRule("implementMakeCascadeDelListMethodBody")));
      } else {
         var1.append(this.parse(this.getProductionRule("implementMakeCascadeDelListMethodBody_AddThisBean")));
      }

      return var1.toString();
   }

   public String get11_RelBeans_RootBeanFKOwner() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.curField = var3;
         if (this.bean.isCascadeDelete(var3) && this.bean.isOneToOneRelation(var3) && !this.bean.isRemoteField(var3)) {
            RDBMSBean var4 = this.bean.getRelatedRDBMSBean(var3);
            String var5 = this.bean.getRelatedFieldName(var3);
            if (!this.bean.relatedFieldIsFkOwner(var3) && var4.relatedFieldIsFkOwner(var5)) {
               var1.append(EOL);
               var1.append(this.parse(this.getProductionRule("oneToOneCascadeDel")));
            }
         }
      }

      return var1.toString();
   }

   public String get1N11_RelBeans_RootBeanNotFKOwner() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.curField = var3;
         if (this.bean.isCascadeDelete(var3)) {
            RDBMSBean var4;
            String var5;
            if (this.bean.isOneToOneRelation(var3)) {
               if (!this.bean.isRemoteField(var3)) {
                  var4 = this.bean.getRelatedRDBMSBean(var3);
                  var5 = this.bean.getRelatedFieldName(var3);
                  if (this.bean.relatedFieldIsFkOwner(var3) && !var4.relatedFieldIsFkOwner(var5)) {
                     var1.append(EOL);
                     var1.append(this.parse(this.getProductionRule("oneToOneCascadeDel")));
                  }
               }
            } else if (this.bean.isOneToManyRelation(var3) && !this.bean.isRemoteField(var3) && this.bean.getRelatedMultiplicity(var3).equals("Many")) {
               var4 = this.bean.getRelatedRDBMSBean(var3);
               var5 = this.bean.getRelatedFieldName(var3);
               if (this.bean.relatedFieldIsFkOwner(var3) && !var4.relatedFieldIsFkOwner(var5)) {
                  var1.append(EOL);
                  var1.append(this.parse(this.getProductionRule("oneToManyCascadeDel")));
               }
            }
         }
      }

      return var1.toString();
   }

   public String get11_RelBeans_EachOtherFKOwner() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.curField = var3;
         if (this.bean.isCascadeDelete(var3) && this.bean.isOneToOneRelation(var3) && !this.bean.isRemoteField(var3)) {
            RDBMSBean var4 = this.bean.getRelatedRDBMSBean(var3);
            String var5 = this.bean.getRelatedFieldName(var3);
            if (this.bean.relatedFieldIsFkOwner(var3) && var4.relatedFieldIsFkOwner(var5)) {
               var1.append(EOL);
               var1.append(this.parse(this.getProductionRule("oneToOneCascadeDel")));
            }
         }
      }

      return var1.toString();
   }

   public boolean isPrimaryKeyCMRField(String var1) {
      if (this.bean.getForeignKeyColNames(var1) == null) {
         return true;
      } else {
         Iterator var2 = this.bean.getForeignKeyColNames(var1).iterator();

         int var3;
         for(var3 = 0; var2.hasNext(); ++var3) {
            String var4 = (String)var2.next();
            String var5 = this.bean.getCmpFieldForColumn(var4);
            if (var5 == null) {
               return false;
            }

            if (!this.bean.isPrimaryKeyField(var5)) {
               break;
            }
         }

         return var3 == this.bean.getForeignKeyColNames(var1).size();
      }
   }

   public String perhapsLoadDefaultGroup() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      Iterator var3 = this.bean.getCmrFieldNames().iterator();
      boolean var4 = false;

      while(var3.hasNext()) {
         String var5 = (String)var3.next();
         if (this.bean.isCascadeDelete(var5)) {
            if (!this.bean.isManyToManyRelation(var5) && this.isPrimaryKeyCMRField(var5) && this.bean.getRelationshipCaching(var5) != null) {
               var4 = true;
            }

            RDBMSBean var6 = this.bean.getRelatedRDBMSBean(var5);
            if (this.bean.getLockOrder() < var6.getLockOrder()) {
               FieldGroup var7 = this.bean.getFieldGroup("defaultGroup");
               var1.append(this.loadMethodName(this.getFieldGroupSuffix(var7)) + "();" + EOL);
               var2 = true;
               break;
            }
         }
      }

      if (!var2 && var4) {
         var1.append(this.loadByRelationFinder());
      }

      return var1.toString();
   }

   public String listRelBeansVar() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.isDBCascadeDelete(this.curField)) {
         var1.append("listRelBeans_WithoutDBUpdate");
      } else {
         var1.append("listRelBeans");
      }

      return var1.toString();
   }

   public String addBeanToRelationships() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();
      if (var2.hasNext()) {
         var1.append(EOL);
      }

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!this.bean.isRemoteField(var3) && this.bean.isForeignPrimaryKeyField(var3)) {
            var1.append(MethodUtils.postSetMethodName(var3) + "();" + EOL);
         }
      }

      return var1.toString();
   }

   public String loadByRelationFinder() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();
      String var3 = null;
      var1.append("//load related bean in join sql" + EOL);
      var1.append("boolean executed = false;" + EOL);
      var1.append("if((__WL_isModified() || !__WL_beanIsLoaded())){" + EOL);
      var1.append("Transaction currentTx = TransactionHelper.getTransactionHelper().getTransaction();" + EOL);

      while(true) {
         do {
            do {
               do {
                  do {
                     if (!var2.hasNext()) {
                        var1.append("}" + EOL);
                        return var1.toString();
                     }

                     var3 = (String)var2.next();
                  } while(!this.bean.isCascadeDelete(var3));
               } while(this.bean.isManyToManyRelation(var3));
            } while(!this.isPrimaryKeyCMRField(var3));
         } while(this.bean.getRelationshipCaching(var3) == null);

         EjbqlFinder var4 = (EjbqlFinder)this.bean.getRelatedFinder(var3);
         if (this.bean.isManyToManyRelation(var3)) {
            var1.append("if(!executed||" + CodeGenUtils.fieldVarName(var3) + " == null ||(" + CodeGenUtils.fieldVarName(var3) + "!=null && currentTx!=null && !(((RDBMSSet)" + CodeGenUtils.fieldVarName(var3) + ").checkIfCurrentTxEqualsCreateTx(currentTx)))){" + EOL);
         } else {
            List var5 = this.bean.getPrimaryKeyFields();
            var1.append("if (!executed");
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               var1.append("||!" + this.isLoadedVar(var7));
            }

            var1.append(") {" + EOL);
         }

         var1.append(MethodUtils.convertToFinderName(var4.getName()) + "((" + this.bean.getCMPBeanDescriptor().getPrimaryKeyClass().getName() + ")" + this.ctxVar() + ".getPrimaryKey());" + EOL);
         var1.append("executed=true;" + EOL);
         var1.append("}" + EOL);
      }
   }

   public String removeBeanFromRelationships() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();
      if (var2.hasNext()) {
         var1.append(EOL);
      }

      while(true) {
         String var3;
         RDBMSBean var4;
         String var5;
         String var6;
         label62:
         do {
            while(var2.hasNext()) {
               var3 = (String)var2.next();
               this.curField = var3;
               if (this.bean.isOneToOneRelation(var3)) {
                  continue label62;
               }

               if (!this.bean.isOneToManyRelation(var3)) {
                  var1.append(MethodUtils.getMethodName(var3) + "().clear();" + EOL);
               } else if (!this.bean.isRemoteField(var3)) {
                  if (!this.bean.getRelatedMultiplicity(var3).equals("Many")) {
                     var1.append(this.varPrefix() + MethodUtils.setMethodName(var3) + "(null, false);" + EOL);
                  } else {
                     var4 = this.bean.getRelatedRDBMSBean(var3);
                     var5 = this.bean.getRelatedFieldName(var3);
                     var6 = this.bean.relatedFieldIsFkOwner(var3) && !var4.isForeignPrimaryKeyField(var5) && !this.bean.isCascadeDelete(var3) ? "true" : "false";
                     var1.append(MethodUtils.getMethodName(var3) + "();" + EOL);
                     if (this.bean.isSelfRelationship(var3)) {
                        var1.append("try {" + EOL);
                        var1.append("if (" + this.fieldRemovedVarForField() + " == null)" + EOL);
                        var1.append(this.fieldRemovedVarForField() + " = (Set)((" + this.collectionClassForField() + ")" + this.fieldVarForField() + ").clone();" + EOL);
                        var1.append("else" + EOL);
                        var1.append(this.fieldRemovedVarForField() + ".addAll((Set)((" + this.collectionClassForField() + ")" + this.fieldVarForField() + ").clone());" + EOL);
                        var1.append("} catch (CloneNotSupportedException e) {" + EOL);
                        var1.append("// clone() failed, do nothing" + EOL);
                        var1.append("}" + EOL);
                     }

                     var1.append("((" + ClassUtils.setClassName(this.bd, var3) + ")");
                     var1.append(CodeGenUtils.fieldVarName(var3));
                     var1.append(").clear(" + var6 + ");" + EOL);
                  }
               } else {
                  var1.append(MethodUtils.setMethodName(var3) + "(null);" + EOL);
               }
            }

            return var1.toString();
         } while(this.bean.isRemoteField(var3));

         var4 = this.bean.getRelatedRDBMSBean(var3);
         var5 = this.bean.getRelatedFieldName(var3);
         var6 = this.bean.relatedFieldIsFkOwner(var3) && !var4.isForeignPrimaryKeyField(var5) && !this.bean.isCascadeDelete(var3) ? "true" : "false";
         if (this.bean.isSelfRelationship(var3)) {
            var1.append(this.fieldRemovedVarForField() + " = " + this.fieldVarForField() + ";" + EOL);
         }

         var1.append(MethodUtils.setNullMethodName(var3) + "(" + var6 + ");" + EOL);
         if (!this.bean.isSelfRelationship(var3)) {
            var1.append(MethodUtils.doSetMethodName(var3) + "(null);" + EOL);
         }
      }
   }

   public String implementEjbStoreMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      var1.append(this.declareEjbStoreMethod());
      var1.append(" { " + EOL);
      var1.append(this.parse(this.getProductionRule("implementEjbStoreMethodBody")));
      var1.append("}");
      return var1.toString();
   }

   public String declareCmrGettersAndSetters() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getCmrFieldNames().iterator();

      while(var2.hasNext()) {
         this.curField = (String)var2.next();
         var1.append(this.generateCmrGetterMethod());
         if (this.bean.isOneToOneRelation(this.curField)) {
            var1.append(this.oneToOneSetMethod());
            var1.append(this.oneToManyAddMethod());
         } else if (this.bean.isOneToManyRelation(this.curField)) {
            var1.append(this.oneToManySetMethod());
            var1.append(this.oneToManyAddMethod());
         } else {
            if (!this.bean.isManyToManyRelation(this.curField)) {
               throw new AssertionError("Invalid  multiplicity for relation.");
            }

            var1.append(this.manyToManySetMethod());
         }
      }

      return var1.toString();
   }

   public String implementCmrFieldPutInQueryCacheMethod() {
      StringBuffer var1 = new StringBuffer();

      try {
         Iterator var2 = this.bean.getCmrFieldNames().iterator();
         boolean var3 = false;

         while(true) {
            while(var2.hasNext()) {
               String var4 = (String)var2.next();
               if (this.bean.getCmrFieldNames().size() > 1) {
                  if (!var3) {
                     var1.append("if (").append(this.cmrFieldNameVar()).append(".equals(\"");
                     var3 = true;
                  } else {
                     var1.append("} else if (").append(this.cmrFieldNameVar()).append(".equals(\"");
                  }

                  var1.append(var4).append("\")) {").append(EOL);
               }

               RDBMSBean var5 = this.bean.getRelatedRDBMSBean(var4);
               String var6 = this.bean.getRelatedFieldName(var4);
               CMPBeanDescriptor var7 = this.bean.getRelatedDescriptor(var4);
               if (this.bean.isOneToOneRelation(var4) && var7.hasLocalClientView() && !var5.relatedFieldIsFkOwner(var6)) {
                  String var8 = CodeGenUtils.fieldVarName(var4);
                  String var9 = this.generateCMRFieldFinderMethodName(var4);
                  var1.append("TTLManager roMgr = (TTLManager)");
                  var1.append(this.bmVar(var4)).append(";").append(EOL);
                  var1.append("QueryCacheKey qckey = new QueryCacheKey(\"");
                  var1.append(var9).append("\", new Object[]{");
                  var1.append("__WL_getPrimaryKey()").append("}, roMgr, ");
                  var1.append("QueryCacheKey.RET_TYPE_SINGLETON);").append(EOL);
                  var1.append("if (").append(this.sourceQueryCacheKeyVar());
                  var1.append(" != null) {").append(EOL);
                  var1.append(this.sourceQueryCacheKeyVar()).append(".addDestinationQuery(");
                  var1.append("qckey);").append(EOL);
                  var1.append("qckey.addSourceQuery(").append(this.sourceQueryCacheKeyVar());
                  var1.append(");").append(EOL);
                  var1.append("}").append(EOL);
                  var1.append("QueryCacheElement qce = new QueryCacheElement(");
                  var1.append(var8);
                  var1.append(".getPrimaryKey(), roMgr);").append(EOL);
                  var1.append("roMgr.putInQueryCache(qckey, qce);").append(EOL);
               } else if (this.bean.isOneToManyRelation(var4) && !var5.relatedFieldIsFkOwner(var6) || this.bean.isManyToManyRelation(var4)) {
                  var1.append("((RDBMSSet)").append(CodeGenUtils.fieldVarName(var4));
                  var1.append(").putInQueryCache(").append(this.sourceQueryCacheKeyVar());
                  var1.append(");").append(EOL);
               }
            }

            if (this.bean.getCmrFieldNames().size() > 1) {
               var1.append("} else {").append(EOL);
               var1.append("throw new AssertionError(\"Unknown CMR field: \"");
               var1.append("+").append(this.cmrFieldNameVar()).append(");").append(EOL);
               var1.append("}").append(EOL);
            }
            break;
         }
      } catch (NullPointerException var10) {
         var10.printStackTrace();
         throw var10;
      }

      return var1.toString();
   }

   public String cmrFieldQueryCachingMethodName() {
      return this.varPrefix() + "putCmrFieldInQueryCache";
   }

   public String cmrFieldNameVar() {
      return this.varPrefix() + "cmrFieldName";
   }

   public String sourceQueryCacheKeyVar() {
      return this.varPrefix() + "sourceQCKey";
   }

   public String loadCheckForCmrField() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.isForeignCmpField(this.curField)) {
         HashSet var2 = new HashSet();
         Iterator var3 = this.bean.getForeignKeyColNames(this.curField).iterator();

         String var5;
         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var5 = this.bean.getCmpFieldForColumn(var4);
            var2.add(this.getMethodSuffix(var5));
         }

         boolean var7 = var2.size() > 1;
         var3 = this.bean.getForeignKeyColNames(this.curField).iterator();

         while(var3.hasNext()) {
            var5 = (String)var3.next();
            String var6 = this.bean.getCmpFieldForColumn(var5);
            var1.append("if (!" + this.isLoadedVar(var6) + ") ");
            var1.append(this.callLoadMethod(var6) + EOL);
            if (!var7) {
               break;
            }
         }
      } else {
         var1.append(this.parse(this.getProductionRule("simpleLoadCheckForField")));
      }

      return var1.toString();
   }

   public String declareCmrVariableGetters() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      try {
         Iterator var2 = this.bean.getForeignKeyFieldNames().iterator();

         while(true) {
            String var3;
            do {
               do {
                  if (!var2.hasNext()) {
                     return var1.toString();
                  }

                  var3 = (String)var2.next();
               } while(!this.bean.isOneToManyRelation(var3));
            } while(this.bean.isRemoteField(var3));

            this.curField = var3;
            String var4 = this.bean.getTableForCmrField(var3);
            Iterator var5 = this.bean.getForeignKeyColNames(var3).iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               if (!this.bean.hasCmpField(var4, var6)) {
                  String var7 = this.bean.variableForField(var3, var4, var6);
                  Class var8 = this.bean.getForeignKeyColClass(var3, var6);
                  String var9 = ClassUtils.classToJavaSourceType(var8);
                  var1.append("public ");
                  var1.append(var9 + " ");
                  var1.append(MethodUtils.getMethodName(var7));
                  var1.append("() {" + EOL);
                  var1.append("try {" + EOL);
                  var1.append(this.loadCheckForCmrField());
                  var1.append(this.parse(this.getProductionRule("standardCatch")));
                  var1.append("return " + var7 + ";" + EOL);
                  var1.append("}" + EOL + EOL);
               }
            }

            this.curField = null;
         }
      } catch (CodeGenerationException var10) {
         throw var10;
      } catch (Exception var11) {
         if (debugLogger.isDebugEnabled()) {
            var11.printStackTrace();
         }

         throw new CodeGenerationException("Error in RDBMSCodeGenerator.generateCmrVariableGetterMethods: " + StackTraceUtils.throwable2StackTrace(var11));
      }
   }

   private String generateCmrGetterMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = ClassUtils.classToJavaSourceType(this.bean.getCmrFieldClass(this.curField));
      boolean var3 = this.bean.isQueryCachingEnabledForCMRField(this.curField);
      var1.append("public ");
      var1.append(var2 + " ");
      var1.append(this.getMethodNameForField());
      var1.append("() {");
      var1.append(EOL);

      try {
         if (this.bean.isOneToOneRelation(this.curField)) {
            if (this.bean.isForeignKeyField(this.curField)) {
               var1.append(this.parse(this.getProductionRule("oneToOneGetterBody_fkOwner")));
            } else {
               if (var3) {
                  var1.append(this.fieldVarForField()).append(" = ");
                  var1.append(this.bmVarForField()).append(".getFromQueryCache(");
                  var1.append(this.finderVarForField()).append(".getName(), new Object[]{");
                  var1.append(this.ctxVar()).append(".getPrimaryKey()});").append(EOL);
                  var1.append("if (").append(this.fieldVarForField()).append(" != null) ");
                  var1.append("return ").append(this.fieldVarForField());
                  var1.append(";").append(EOL);
               }

               var1.append(this.parse(this.getProductionRule("oneToOneGetterBody")));
               if (var3) {
                  var1.append("QueryCacheKey qckey = new QueryCacheKey(");
                  var1.append(this.finderVarForField()).append(".getName(), ");
                  var1.append("new Object[] {").append(this.ctxVar());
                  var1.append(".getPrimaryKey()}, ").append("(TTLManager)");
                  var1.append(this.bmVarForField());
                  var1.append(", QueryCacheKey.RET_TYPE_SINGLETON);").append(EOL);
                  var1.append("QueryCacheElement qcelem = new QueryCacheElement(");
                  var1.append(this.fieldVarGetPrimaryKey()).append(", ");
                  var1.append(this.bmVarForField()).append(");").append(EOL);
                  var1.append(this.bmVarForField()).append(".putInQueryCache(qckey, ");
                  var1.append("qcelem);").append(EOL);
               }
            }
         } else if (this.bean.isOneToManyRelation(this.curField)) {
            if (this.bean.getRelatedMultiplicity(this.curField).equals("One")) {
               var1.append(this.parse(this.getProductionRule("oneToManyGetterBody_fkOwner")));
            } else {
               var1.append(this.parse(this.getProductionRule("oneToManyGetterBody")));
               if (!this.bean.isRemoteField(this.curField)) {
                  this.generateOneToManyCollection();
               }
            }
         } else {
            if (!this.bean.isManyToManyRelation(this.curField)) {
               throw new AssertionError("Invalid  multiplicity for relation.");
            }

            var1.append(this.parse(this.getProductionRule("ManyToManyGetterBody")));
            if (!this.bean.isRemoteField(this.curField)) {
               this.generateManyToManyCollection();
            }
         }
      } catch (Exception var5) {
         throw new CodeGenerationException("Error in RDBMSCodeGenerator.generateCmrGetterMethod: " + StackTraceUtils.throwable2StackTrace(var5));
      }

      var1.append("}");
      var1.append(EOL);
      return var1.toString();
   }

   public String allocateOneToManySet() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.isRemoteField(this.curField)) {
         var1.append("new " + this.collectionClassForField() + "(this, " + this.bmVarForField() + ", " + this.finderVarForField() + ")" + EOL);
      } else {
         var1.append("new " + this.collectionClassForField() + "(this, " + this.homeVarForField() + ", " + this.pmVar() + ")" + EOL);
      }

      return var1.toString();
   }

   private void generateOneToManyCollection() throws CodeGenerationException {
      try {
         OneToManyGenerator var1 = new OneToManyGenerator(this.options);
         var1.setRootDirectoryName(this.getRootDirectoryName());
         var1.setTargetDirectory(this.getRootDirectoryName());
         var1.setRDBMSBean(this.bean);
         var1.setCMPBeanDescriptor(this.bd);
         var1.setCmrFieldName(this.curField);
         List var4 = var1.generate(new ArrayList());
         this.getGeneratedOutputs().addAll(var1.getGeneratedOutputs());
         this.currentOutput.addExtraOutputFiles(var4);
      } catch (Exception var3) {
         Loggable var2 = EJBLogger.logErrorWhileGeneratingLoggable("One to Many Collection", var3);
         EJBLogger.logStackTraceAndMessage(var2.getMessage(), var3);
         throw new CodeGenerationException(var2.getMessage());
      }
   }

   public String allocateManyToManySet() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.isRemoteField(this.curField)) {
         var1.append("new " + this.collectionClassForField() + "(this, " + this.bmVarForField() + ", " + this.finderVarForField() + ", " + this.pmVar() + ")" + EOL);
      } else {
         var1.append("new " + this.collectionClassForField() + "(this, " + this.homeVarForField() + ", " + this.pmVar() + ")" + EOL);
      }

      return var1.toString();
   }

   private void generateManyToManyCollection() throws CodeGenerationException {
      if (debugLogger.isDebugEnabled()) {
         debug("called generateManyToManyCollection");
      }

      try {
         ManyToManyGenerator var1 = new ManyToManyGenerator(this.options);
         var1.setRootDirectoryName(this.getRootDirectoryName());
         var1.setTargetDirectory(this.getRootDirectoryName());
         var1.setRDBMSBean(this.bean);
         var1.setCMPBeanDescriptor(this.bd);
         var1.setCmrFieldName(this.curField);
         List var4 = var1.generate(new ArrayList());
         this.getGeneratedOutputs().addAll(var1.getGeneratedOutputs());
         this.currentOutput.addExtraOutputFiles(var4);
      } catch (Exception var3) {
         Loggable var2 = EJBLogger.logErrorWhileGeneratingLoggable("One to Many Collection", var3);
         EJBLogger.logStackTraceAndMessage(var2.getMessage(), var3);
         throw new CodeGenerationException(var2.getMessage());
      }
   }

   public String perhapsGetM2NSQL() throws CodeGenerationException {
      if (!this.bean.getOrderDatabaseOperations()) {
         return "return \"\";";
      } else {
         List var1 = this.bean.getDeclaredFieldNames();
         Iterator var2 = var1.iterator();
         StringBuffer var3 = null;
         if (!var2.hasNext()) {
            return "return \"\";";
         } else {
            var3 = new StringBuffer();
            boolean var4 = false;

            while(var2.hasNext()) {
               String var5 = (String)var2.next();
               if (this.bean.isManyToManyRelation(var5)) {
                  this.curField = var5;
                  var3.append(this.parse(this.getProductionRule("manyToManyGetSQL")));
               }
            }

            var3.append("throw new AssertionError(\" in __WL_getM2NSQL: unknown Many To Many cmr-field \"+cmrf+\".\");");
            var3.append(EOL);
            return var3.toString();
         }
      }
   }

   public String perhapsGetCmrBeansForCmrField() throws CodeGenerationException {
      if (!this.bean.getOrderDatabaseOperations()) {
         return "return null;";
      } else {
         List var1 = this.bean.getDeclaredFieldNames();
         Iterator var2 = var1.iterator();
         StringBuffer var3 = null;
         if (!var2.hasNext()) {
            return "return null;";
         } else {
            var3 = new StringBuffer();
            boolean var4 = false;

            while(var2.hasNext()) {
               String var5 = (String)var2.next();
               if (this.bean.isManyToManyRelation(var5)) {
                  this.curField = var5;
                  var3.append(this.parse(this.getProductionRule("getCmrBeansForCmrField")));
               }
            }

            var3.append("throw new AssertionError(\" in __WL_getCmrBeansForCmrField: unknown Many To Many cmr-field \"+cmrf+\".\");");
            var3.append(EOL);
            return var3.toString();
         }
      }
   }

   public String perhapsManyToManySetSymmetricBeanInsertParams() throws CodeGenerationException {
      if (!this.bean.getOrderDatabaseOperations()) {
         return "";
      } else {
         return !this.bean.isSymmetricField(this.curField) ? "" : this.parse(this.getProductionRule("manyToManySetSymmetricBeanInsertParams"));
      }
   }

   public String doCheckExistsOnMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      this.curGroup = this.bean.getFieldGroup("defaultGroup");
      var1.append(this.parse(this.getProductionRule("checkExistsOnMethodBody")));
      this.curGroup = null;
      return var1.toString();
   }

   public String perhapsDoCheckExistsOnMethod() throws CodeGenerationException {
      return this.bean.getCheckExistsOnMethod() ? "__WL_doCheckExistsOnMethod();" : "";
   }

   public String perhapsCheckRelatedExistsOneMany() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      RDBMSBean var2 = this.bean.getRelatedRDBMSBean(this.curField);
      if (var2.getCheckExistsOnMethod()) {
         var1.append(this.parse(this.getProductionRule("checkRelatedExistsOneMany")));
      }

      return var1.toString();
   }

   public String perhapsCheckExistsOneOne() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.getCheckExistsOnMethod()) {
         var1.append(this.parse(this.getProductionRule("checkExistsOneOne")));
      }

      return var1.toString();
   }

   public String bmVarForField() {
      return this.bmVar(this.curField);
   }

   public String finderInvokerForField() {
      return this.bean.isRemoteField(this.curField) ? this.homeVarForField() : this.bmVarForField();
   }

   public String finderVarForField() {
      return this.finderVarName(this.curField);
   }

   public String finderParamForField() {
      return this.bean.isRemoteField(this.curField) ? "" : this.finderVarForField() + ", ";
   }

   public String fieldClassForCmpField() {
      return ClassUtils.classToJavaSourceType(this.bean.getCmpFieldClass(this.curField));
   }

   public String cmpColumnForCmpField() {
      return this.bean.getCmpColumnForField(this.curField);
   }

   public String collectionClassForField() {
      return ClassUtils.setClassName(this.bd, this.curField);
   }

   public String isLoadedVar(String var1) {
      return this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var1) + "]";
   }

   public String isLoadedVarForField() {
      return this.isLoadedVar(this.curField);
   }

   public String isModifiedVar(String var1) {
      return this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var1) + "]";
   }

   public String isModifiedVarForField() {
      return this.isModifiedVar(this.curField);
   }

   public String loadMethodNameForGroup() {
      return this.loadMethodName(this.getFieldGroupSuffix(this.curGroup));
   }

   public String loadMethodName(String var1) {
      return this.varPrefix() + "load" + this.capitalize(var1);
   }

   public String callLoadMethod(String var1) {
      String var2 = this.getMethodSuffix(var1);
      return this.loadMethodName(var2) + "();";
   }

   public String callLoadMethodForField() {
      return this.callLoadMethod(this.curField);
   }

   public String fieldVarForField() {
      return CodeGenUtils.fieldVarName(this.curField);
   }

   public String fieldRemovedVarForField() {
      return CodeGenUtils.fieldRemovedVarName(this.curField);
   }

   public String classNameForField() {
      return this.bean.getCmrFieldClass(this.curField).getName();
   }

   public String fkVarForFieldIsNull() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.bean.getTableForCmrField(this.curField);
      Iterator var3 = this.bean.getForeignKeyColNames(this.curField).iterator();
      boolean var4 = false;
      var1.append("(");

      while(var3.hasNext()) {
         String var5 = (String)var3.next();
         String var6 = this.bean.variableForField(this.curField, var2, var5);
         Class var7 = this.bean.getForeignKeyColClass(this.curField, var5);
         if (!var7.isPrimitive()) {
            if (var4) {
               var1.append(" || ");
            } else {
               var4 = true;
            }

            var1.append(var6 + "==null");
         }
      }

      if (!var4) {
         var1.append("false");
      }

      var1.append(")");
      return var1.toString();
   }

   public String fkVarForField() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      if (var1.hasComplexPrimaryKey()) {
         return this.fkVar();
      } else {
         String var2 = this.bean.getTableForCmrField(this.curField);
         String var3 = (String)this.bean.getForeignKeyColNames(this.curField).iterator().next();
         Class var4 = this.bean.getForeignKeyColClass(this.curField, var3);
         return this.perhapsConvertPrimitive(var4, "this." + this.bean.variableForField(this.curField, var2, var3));
      }
   }

   private String capitalize(String var1) {
      Debug.assertion(var1.length() > 0);
      return var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1);
   }

   public String declareFkVarForField() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      String var2 = var1.getPrimaryKeyClass().getName();
      return var2 + " " + this.fkVar() + ";" + EOL;
   }

   public String perhapsDeclareFkVar() {
      if (!this.bean.isRemoteField(this.curField)) {
         CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
         if (var1.hasComplexPrimaryKey()) {
            return this.declareFkVarForField();
         }
      }

      return "";
   }

   public String allocateFkVarForField() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      String var2 = var1.getPrimaryKeyClass().getName();
      return this.fkVar() + " = new " + var2 + "();" + EOL;
   }

   public String perhapsAllocateFkVar() {
      if (!this.bean.isRemoteField(this.curField)) {
         CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
         if (var1.hasComplexPrimaryKey()) {
            return this.allocateFkVarForField();
         }
      }

      return "";
   }

   public String assignFkVarForField() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getForeignKeyColNames(this.curField).iterator();
      String var3 = this.bean.getTableForCmrField(this.curField);

      while(var2.hasNext()) {
         String var4 = (String)var2.next();
         String var5 = this.bean.getRelatedPkFieldName(this.curField, var4);
         var1.append(this.fkVar()).append(".").append(var5).append(" = ");
         RDBMSBean var6 = this.bean.getRelatedRDBMSBean(this.curField);
         CMPBeanDescriptor var7 = var6.getCMPBeanDescriptor();
         Class var8 = var7.getFieldClass(var5);
         String var9 = this.bean.variableForField(this.curField, var3, var4);
         Class var10 = this.bean.getForeignKeyColClass(this.curField, var4);
         String var11 = this.perhapsConvert(var8, var10, var9);
         var1.append("this").append(".").append(var11).append(";");
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String perhapsAssignFkVar() {
      if (!this.bean.isRemoteField(this.curField)) {
         CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
         if (var1.hasComplexPrimaryKey()) {
            return this.assignFkVarForField();
         }
      }

      return "";
   }

   public String declarePkVar() {
      return this.pk_class() + " " + this.pkVar() + " = null;";
   }

   public String allocatePkVar() {
      return this.bd.hasComplexPrimaryKey() ? this.pkVar() + " = new " + this.pk_class() + "();" : "";
   }

   public String perhapsDeclarePkVar() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      return var2.hasComplexPrimaryKey() ? this.declarePkVar() : "";
   }

   public String perhapsCopyKeyValuesToPkVar() {
      CMPCodeGenerator.Output var1 = (CMPCodeGenerator.Output)this.currentOutput;
      CMPBeanDescriptor var2 = var1.getCMPBeanDescriptor();
      return var2.hasComplexPrimaryKey() ? this.copyKeyValuesToPkVar() : "";
   }

   public String pkVarForField() {
      return this.bd.hasComplexPrimaryKey() ? this.pkVar() : (String)this.pkFieldNames.iterator().next();
   }

   public String implementGetPrimaryKey() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.declarePkVar() + EOL);
      var1.append(this.allocatePkVar() + EOL);
      var1.append(this.assignPkFieldsToPkVar() + EOL);
      var1.append("return " + this.pkVar() + ";" + EOL);
      return var1.toString();
   }

   public String implementSetPrimaryKey() {
      return this.assignPkVarToPkFields() + EOL;
   }

   private String oneToOneSetMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = ClassUtils.classToJavaSourceType(this.bean.getCmrFieldClass(this.curField));
      var1.append("public void ");
      var1.append(MethodUtils.setMethodName(this.curField));
      var1.append("(");
      var1.append(var2);
      var1.append(" ");
      var1.append(this.curField);
      var1.append(") {");
      var1.append(EOL);
      if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("Loggable l = EJBLogger.logCannotCallSetForReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
         var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
      } else if (this.bean.isForeignKeyField(this.curField)) {
         var1.append(this.oneToOneSetBody_fkOwner());
      } else {
         var1.append(this.oneToOneSetBody());
      }

      var1.append("}");
      var1.append(EOL);
      var1.append(EOL);
      var1.append("public void ");
      var1.append(MethodUtils.doSetMethodName(this.curField));
      var1.append("(");
      var1.append(var2);
      var1.append(" ");
      var1.append(this.curField);
      var1.append(") {");
      var1.append(EOL);
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         if (this.bean.isForeignKeyField(this.curField)) {
            var1.append(this.parse(this.getProductionRule("oneToOneDoSetBody_fkOwner")));
         } else {
            var1.append(this.oneToOneDoSetBody());
         }
      }

      var1.append("}");
      var1.append(EOL);
      var1.append(EOL);
      if (!this.bean.isForeignPrimaryKeyField(this.curField)) {
         var1.append("public boolean ");
         var1.append(MethodUtils.checkIsRemovedMethodName(this.curField));
         var1.append("(");
         var1.append(var2);
         var1.append(" ");
         var1.append(this.curField);
         var1.append(") throws java.lang.Exception {");
         var1.append(EOL);
         var1.append(this.parse(this.getProductionRule("checkIsRemovedBody")));
         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
      }

      if (!this.bean.isRemoteField(this.curField)) {
         var1.append("private void ");
         var1.append(MethodUtils.postSetMethodName(this.curField));
         var1.append("() throws java.lang.Exception {");
         var1.append(EOL);
         if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
            if (this.bean.isForeignKeyField(this.curField)) {
               var1.append(this.parse(this.getProductionRule("oneToOnePostSetBody_fkOwner")));
            } else {
               var1.append(this.parse(this.getProductionRule("oneToOnePostSetBody")));
            }
         }

         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
         var1.append("public void ");
         var1.append(MethodUtils.setRestMethodName(this.curField));
         var1.append("(");
         var1.append(var2);
         var1.append(" ");
         var1.append(this.curField);
         var1.append(", int methodState) {");
         var1.append(EOL);
         if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
            if (this.bean.isForeignKeyField(this.curField)) {
               var1.append(this.parse(this.getProductionRule("oneToOneSetRestBody_fkOwner")));
            } else {
               var1.append(this.parse(this.getProductionRule("oneToOneSetRestBody")));
            }
         }

         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
         var1.append("public void ");
         var1.append(MethodUtils.setCmrIsLoadedMethodName(this.curField));
         var1.append("(boolean b) {");
         var1.append(EOL);
         if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
            var1.append(this.isCmrLoadedVarName(this.curField) + " = b;");
            var1.append(EOL);
         }

         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String perhapsOptimizeOneToOne() {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.isBiDirectional(this.curField)) {
         var1.append("if(methodState != WLEnterpriseBean.STATE_EJB_POSTCREATE)" + EOL);
         var1.append(this.getMethodNameForField() + "();" + EOL);
      } else {
         var1.append("if(this.__WL_getMethodState() != WLEnterpriseBean.STATE_EJB_POSTCREATE) " + EOL);
         var1.append(this.getMethodNameForField() + "();" + EOL);
      }

      return var1.toString();
   }

   public String relClassNameForField() {
      return this.bean.getRelatedBeanClassName(this.curField);
   }

   public String relInterfaceNameForField() {
      return this.relInterfaceNameForField(this.curField);
   }

   public String relInterfaceNameForField(String var1) {
      CMPBeanDescriptor var2 = this.bean.getRelatedDescriptor(var1);
      return var2.getGeneratedBeanInterfaceName();
   }

   public String relatedDoSetForField() {
      String var1 = this.bean.getRelatedFieldName(this.curField);
      return MethodUtils.doSetMethodName(var1);
   }

   public String componentInterfaceForBean() {
      if (this.bd.hasLocalClientView()) {
         return this.bd.getLocalInterfaceClass().getName();
      } else {
         return this.bd.isEJB30() ? this.bd.getJavaClassName() : this.bd.getRemoteInterfaceClass().getName();
      }
   }

   public String relatedSetRestForField() {
      String var1 = this.bean.getRelatedFieldName(this.curField);
      return MethodUtils.setRestMethodName(var1);
   }

   public String relatedIsCmrLoadedVarNameForField() {
      String var1 = this.bean.getRelatedFieldName(this.curField);
      return MethodUtils.setCmrIsLoadedMethodName(var1);
   }

   public String oneToOneSetBody_fkOwner() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.isForeignPrimaryKeyField(this.curField)) {
         var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldAsPkLoggable();" + EOL);
         var1.append("throw new EJBException(l.getMessage());" + EOL + EOL);
      } else {
         var1.append("if (__WL_method_state==STATE_EJB_CREATE) {" + EOL);
         var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldDuringEjbCreateLoggable();" + EOL);
         var1.append("throw new IllegalStateException(l.getMessage());" + EOL);
         var1.append("}" + EOL);
         var1.append("try {" + EOL);
         var1.append("if (" + MethodUtils.checkIsRemovedMethodName(this.curField) + "(" + this.curField + ")) {" + EOL);
         var1.append("Loggable l = EJBLogger.logillegalAttemptToAssignRemovedBeanToCMRFieldLoggable(");
         var1.append(this.curField).append(".getPrimaryKey().toString());").append(EOL);
         var1.append("throw new IllegalArgumentException(l.getMessage());" + EOL);
         var1.append("}" + EOL);
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append(this.debugSay() + "(\"[\" + " + this.ctxVar() + ".getPrimaryKey() + \"]called ");
         var1.append(MethodUtils.setMethodName(this.curField) + "...\");" + EOL);
         var1.append("}" + EOL);
         if (!this.bean.isRemoteField(this.curField)) {
            var1.append(MethodUtils.setNullMethodName(this.curField) + "(false);" + EOL);
         }

         var1.append(MethodUtils.doSetMethodName(this.curField) + "(" + this.curField + ");" + EOL);
         if (!this.bean.isRemoteField(this.curField)) {
            var1.append(MethodUtils.postSetMethodName(this.curField) + "();" + EOL);
         }

         var1.append(this.parse(this.getProductionRule("standardCatch")));
      }

      return var1.toString();
   }

   public String oneToOneSetBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      RDBMSBean var2 = this.bean.getRelatedRDBMSBean(this.curField);
      String var3 = this.bean.getRelatedFieldName(this.curField);
      if (var2.isForeignPrimaryKeyField(var3)) {
         var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldAsPkLoggable();" + EOL);
         var1.append("throw new EJBException(l.getMessage());" + EOL + EOL);
      } else {
         var1.append("if (__WL_method_state==STATE_EJB_CREATE) {" + EOL);
         var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldDuringEjbCreateLoggable();" + EOL);
         var1.append("throw new IllegalStateException(l.getMessage());" + EOL);
         var1.append("}" + EOL);
         var1.append("try {" + EOL);
         var1.append("if (" + MethodUtils.checkIsRemovedMethodName(this.curField) + "(" + this.curField + ")) {" + EOL);
         var1.append("Loggable l = EJBLogger.logillegalAttemptToAssignRemovedBeanToCMRFieldLoggable(");
         var1.append(this.curField).append(".getPrimaryKey().toString());").append(EOL);
         var1.append("throw new IllegalArgumentException(l.getMessage());" + EOL);
         var1.append("}" + EOL);
         var1.append(MethodUtils.setNullMethodName(this.curField) + "(false);" + EOL);
         var1.append(MethodUtils.doSetMethodName(this.curField) + "(" + this.curField + ");" + EOL + EOL);
         var1.append(MethodUtils.postSetMethodName(this.curField) + "();" + EOL);
         var1.append(this.parse(this.getProductionRule("standardCatch")));
      }

      return var1.toString();
   }

   public String assignFkVarsNull_forField() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.isForeignPrimaryKeyField(this.curField)) {
         String var2 = this.bean.getTableForCmrField(this.curField);
         Iterator var3 = this.bean.getForeignKeyColNames(this.curField).iterator();

         String var5;
         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var5 = this.bean.variableForField(this.curField, var2, var4);
            var1.append(var5 + " = null;" + EOL);
         }

         if (this.bean.isForeignCmpField(this.curField)) {
            Iterator var7 = this.bean.getForeignKeyColNames(this.curField).iterator();

            while(var7.hasNext()) {
               var5 = (String)var7.next();
               String var6 = this.bean.getCmpFieldForColumn(var5);
               var1.append(this.isModifiedVar(var6) + " = true;" + EOL);
            }
         } else {
            var1.append(this.isModifiedVarForField() + " = true;" + EOL);
         }

         var1.append(this.perhapsSetTableModifiedVarForCmrField());
      }

      return var1.toString();
   }

   public String assignFkVarsFkField_forField() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.isForeignPrimaryKeyField(this.curField)) {
         boolean var2 = false;
         CMPBeanDescriptor var3;
         String var5;
         String var6;
         if (!this.bean.isRemoteField(this.curField)) {
            var3 = this.bean.getRelatedDescriptor(this.curField);
            Class var4 = var3.getPrimaryKeyClass();
            var5 = var4.getName();
            if (var3.hasComplexPrimaryKey()) {
               var2 = true;
               var1.append(this.declareFkVarForField());
               var1.append(this.fkVar() + " = (" + var5 + ")");
               var1.append(this.fieldVarGetPrimaryKey()).append(";").append(EOL);
               var6 = this.bean.getTableForCmrField(this.curField);

               String var9;
               Field var11;
               for(Iterator var7 = this.bean.getForeignKeyColNames(this.curField).iterator(); var7.hasNext(); var1.append(this.assignPkFieldToVariable(var9, this.fkVar(), var11))) {
                  String var8 = (String)var7.next();
                  var9 = this.bean.variableForField(this.curField, var6, var8);
                  String var10 = this.bean.getRelatedPkFieldName(this.curField, var8);
                  var11 = null;

                  try {
                     var11 = var4.getField(var10);
                  } catch (Exception var13) {
                     throw new AssertionError("Unable to access field '" + var10 + "' in class '" + var5 + "'. " + StackTraceUtils.throwable2StackTrace(var13));
                  }
               }
            }
         }

         String var15;
         if (!var2) {
            var3 = this.bean.getRelatedDescriptor(this.curField);
            var15 = this.bean.getTableForCmrField(this.curField);
            var5 = (String)this.bean.getForeignKeyColNames(this.curField).iterator().next();
            var6 = this.bean.variableForField(this.curField, var15, var5);
            String var16 = ClassUtils.classToJavaSourceType((Class)this.variableToClass.get(var6));
            var1.append(var6 + " = ");
            var1.append("(" + var16 + ")");
            var1.append(this.fieldVarGetPrimaryKey()).append(";").append(EOL);
         }

         if (this.bean.isForeignCmpField(this.curField)) {
            Iterator var14 = this.bean.getForeignKeyColNames(this.curField).iterator();

            while(var14.hasNext()) {
               var15 = (String)var14.next();
               var5 = this.bean.getCmpFieldForColumn(var15);
               var1.append(this.isModifiedVar(var5) + " = true;" + EOL);
            }
         } else {
            var1.append(this.isModifiedVarForField() + " = true;" + EOL);
         }

         var1.append(this.perhapsSetTableModifiedVarForCmrField());
      }

      return var1.toString();
   }

   public String assignPkFieldToVariable(String var1, String var2, Field var3) {
      Class var4 = var3.getType();
      String var5 = var2 + "." + var3.getName();
      if (var4.isPrimitive()) {
         if (var4 == Boolean.TYPE) {
            var5 = "new Boolean(" + var5 + ")";
         } else if (var4 == Byte.TYPE) {
            var5 = "new Byte(" + var5 + ")";
         } else if (var4 == Character.TYPE) {
            var5 = "new Character(" + var5 + ")";
         } else if (var4 == Double.TYPE) {
            var5 = "new Double(" + var5 + ")";
         } else if (var4 == Float.TYPE) {
            var5 = "new Float(" + var5 + ")";
         } else if (var4 == Integer.TYPE) {
            var5 = "new Integer(" + var5 + ")";
         } else if (var4 == Long.TYPE) {
            var5 = "new Long(" + var5 + ")";
         } else {
            if (var4 != Short.TYPE) {
               throw new AssertionError("Missing primitive in CommonRules.assignPkFieldToVariable");
            }

            var5 = "new Short(" + var5 + ")";
         }
      }

      return var1 + " = " + var5 + ";" + EOL;
   }

   public String oneToOneDoSetBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.assignFieldVarForFieldWithFieldNameForField()).append(EOL);
      var1.append(this.isCmrLoadedVarName(this.curField) + " = true;" + EOL);
      var1.append(EOL);
      var1.append("  // whenever this bean's relationship with " + this.curField + " changes" + EOL);
      var1.append("  // this __WL_doSet method is invoked." + EOL);
      var1.append("  // so we mark the nonFKHolderRelationChange bit in this __WL_doSet method." + EOL);
      var1.append("  __WL_setNonFKHolderRelationChange(true);" + EOL);
      var1.append(this.parse(this.getProductionRule("registerInvalidatedBean")));
      return var1.toString();
   }

   private String oneToManySetMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.bean.getCmrFieldClass(this.curField);
      String var3 = ClassUtils.classToJavaSourceType(var2);
      var1.append("public void ");
      var1.append(MethodUtils.setMethodName(this.curField));
      var1.append("(");
      var1.append(var3);
      var1.append(" ");
      var1.append(this.curField);
      var1.append(") {");
      var1.append(EOL);
      if (this.bean.getRelatedMultiplicity(this.curField).equals("One")) {
         if (this.bean.isForeignPrimaryKeyField(this.curField)) {
            var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldAsPkLoggable();" + EOL);
            var1.append("throw new EJBException(l.getMessage());" + EOL + EOL);
         } else if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
            var1.append("Loggable l = EJBLogger.logCannotCallSetForReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
            var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
         } else {
            var1.append("if (__WL_method_state==STATE_EJB_CREATE) {" + EOL);
            var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldDuringEjbCreateLoggable();" + EOL);
            var1.append("throw new IllegalStateException(l.getMessage());" + EOL);
            var1.append("}" + EOL);
            var1.append("try {" + EOL);
            var1.append("if (" + MethodUtils.checkIsRemovedMethodName(this.curField) + "(" + this.curField + ")) {" + EOL);
            var1.append("Loggable l = EJBLogger.logillegalAttemptToAssignRemovedBeanToCMRFieldLoggable(");
            var1.append(this.curField).append(".getPrimaryKey().toString());");
            var1.append(EOL);
            var1.append("throw new IllegalArgumentException(l.getMessage());" + EOL);
            var1.append("}" + EOL);
            var1.append(this.parse(this.getProductionRule("standardCatch")));
            var1.append(this.varPrefix() + MethodUtils.setMethodName(this.curField) + "(" + this.curField + ", false);" + EOL);
         }

         var1.append("}" + EOL);
         var1.append("public void ");
         var1.append(this.varPrefix() + MethodUtils.setMethodName(this.curField));
         var1.append("(");
         var1.append(var3);
         var1.append(" ");
         var1.append(this.curField);
         var1.append(", boolean ejbStore) {");
         var1.append(EOL);
         var1.append(this.varPrefix() + MethodUtils.setMethodName(this.curField));
         var1.append("(").append(this.curField).append(", ejbStore, true);" + EOL);
         var1.append("}" + EOL + EOL);
         var1.append("// The flag 'remove' controls whether the Relationship's" + EOL);
         var1.append("// underlying __WL_cache does a remove() operation." + EOL);
         var1.append("// If an Iterator of the __WL_cache is used to effect a remove()" + EOL);
         var1.append("// then we must be sure to not to do a__WL_cache.remove()" + EOL);
         var1.append("//   that is the intended use of the 'remove' flag." + EOL + EOL);
         var1.append("public void ");
         var1.append(this.varPrefix() + MethodUtils.setMethodName(this.curField));
         var1.append("(");
         var1.append(var3);
         var1.append(" ");
         var1.append(this.curField);
         var1.append(", boolean ejbStore");
         var1.append(", boolean remove) {");
         var1.append(EOL);
         if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
            if (!this.bean.isRemoteField(this.curField)) {
               var1.append(this.parse(this.getProductionRule("oneToManySetBody_local_fkOwner")));
            } else {
               var1.append(this.parse(this.getProductionRule("oneToManySetBody_remote_fkOwner")));
            }
         }

         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
         if (!this.bean.isForeignPrimaryKeyField(this.curField)) {
            var1.append("public boolean ");
            var1.append(MethodUtils.checkIsRemovedMethodName(this.curField));
            var1.append("(");
            var1.append(var3);
            var1.append(" ");
            var1.append(this.curField);
            var1.append(") throws java.lang.Exception {");
            var1.append(EOL);
            var1.append(this.parse(this.getProductionRule("checkIsRemovedBody")));
            var1.append("}");
            var1.append(EOL);
            var1.append(EOL);
         }

         if ((!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) && !this.bean.isRemoteField(this.curField)) {
            var1.append("private void " + MethodUtils.postSetMethodName(this.curField) + "() throws java.lang.Exception {" + EOL);
            var1.append(this.parse(this.getProductionRule("oneToManyPostSetBody")));
            var1.append("}");
            var1.append(EOL);
            var1.append(EOL);
         }
      } else {
         boolean var4 = false;
         if (!this.bean.isRemoteField(this.curField)) {
            RDBMSBean var5 = this.bean.getRelatedRDBMSBean(this.curField);
            String var6 = this.bean.getRelatedFieldName(this.curField);
            if (var5.isForeignPrimaryKeyField(var6)) {
               var4 = true;
               var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldAsPkLoggable();");
               var1.append("throw new EJBException(l.getMessage());" + EOL + EOL);
            }
         }

         if (!var4) {
            if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
               var1.append("Loggable l = EJBLogger.logCannotCallSetForReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
               var1.append("throw new javax.ejb.EJBException(l.getMessage());" + EOL);
            } else {
               var1.append("if (__WL_method_state==STATE_EJB_CREATE) {" + EOL);
               var1.append("Loggable l = EJBLogger.logsetCheckForCmrFieldDuringEjbCreateLoggable();");
               var1.append("throw new IllegalStateException(l.getMessage());" + EOL);
               var1.append("}" + EOL);
               var1.append(this.parse(this.getProductionRule("oneToManySetBody")));
            }
         }

         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
      }

      if (this.bean.getRelatedMultiplicity(this.curField).equals("One") && (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove())) {
         var1.append("public void ");
         var1.append(MethodUtils.doSetMethodName(this.curField));
         var1.append("(");
         var1.append(var3);
         var1.append(" ");
         var1.append(this.curField);
         var1.append(") {");
         var1.append(EOL);
         var1.append(this.parse(this.getProductionRule("oneToOneDoSetBody_fkOwner")));
         var1.append("}");
         var1.append(EOL);
         var1.append(EOL);
      }

      return var1.toString();
   }

   public String perhapsCallPostSetMethodForField() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (!this.bean.isForeignPrimaryKeyField(this.curField)) {
         var1.append(MethodUtils.postSetMethodName(this.curField) + "();" + EOL);
      }

      return var1.toString();
   }

   private String manyToManySetMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = ClassUtils.classToJavaSourceType(this.bean.getCmrFieldClass(this.curField));
      var1.append("public void ");
      var1.append(MethodUtils.setMethodName(this.curField));
      var1.append("(");
      var1.append(var2);
      var1.append(" ");
      var1.append(this.curField);
      var1.append(") {");
      var1.append(EOL);
      var1.append(this.parse(this.getProductionRule("oneToManySetBody")));
      var1.append("}");
      var1.append(EOL);
      var1.append(EOL);
      return var1.toString();
   }

   public String copyFromMethodBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.cmpBeanClassName() + " " + this.beanVar() + " = null;" + EOL);
      var1.append("try {" + EOL);
      var1.append(this.beanVar() + " = (" + this.cmpBeanClassName() + ")otherBean;" + EOL);
      var1.append(this.parse(this.getProductionRule("standardCatch")));
      Iterator var2 = this.cmpFieldNames.iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var1.append(this.checkFieldNotModifiedOrLoaded("this", var3));
         var1.append("if (" + this.beanVar() + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]) {" + EOL);
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append(this.debugSay() + "(\"copying field '" + var3 + "' to bean '\" +" + EOL + this.beanVar() + ".__WL_getPrimaryKey() + \"'.\");" + EOL);
         var1.append("}" + EOL);
         var1.append(this.setCmpField(var3, this.getCmpField(this.beanVar(), var3)) + ";" + EOL);
         var1.append("this." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]");
         var1.append(" = true;" + EOL);
         if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
            var1.append("if (!__WL_initSnapshotVars) {").append(EOL);
            var1.append("this." + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]" + " = true;" + EOL);
            var1.append(this.beanIsModifiedVar()).append(" = true;").append(EOL);
            var1.append("}").append(EOL);
         }

         if (this.bd.isOptimistic() && !this.bd.getPrimaryKeyFieldNames().contains(var3) && !this.bean.isBlobCmpColumnTypeForField(var3) && !this.bean.isClobCmpColumnTypeForField(var3) && this.doSnapshot(var3)) {
            var1.append("if (__WL_initSnapshotVars) ");
            var1.append("this." + CodeGenUtils.snapshotNameForVar(var3) + " = " + this.beanVar() + "." + CodeGenUtils.snapshotNameForVar(var3) + ";" + EOL);
         }

         var1.append("}" + EOL);
         var1.append("}" + EOL + EOL);
      }

      var2 = this.bean.getForeignKeyFieldNames().iterator();

      while(true) {
         do {
            do {
               if (!var2.hasNext()) {
                  if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
                     var1.append("this." + this.modifiedBeanIsRegisteredVar() + " = " + this.beanVar() + "." + this.modifiedBeanIsRegisteredVar() + ";" + EOL);
                  }

                  var1.append("this." + this.beanIsLoadedVar() + " = " + this.beanVar() + "." + this.beanIsLoadedVar() + ";" + EOL + EOL);
                  var2 = this.bean.getAllCmrFields().iterator();

                  while(var2.hasNext()) {
                     var3 = (String)var2.next();
                     this.curField = var3;
                     var1.append("if (!this." + this.isCmrLoadedVarName(var3) + ") {" + EOL);
                     var1.append("if (" + this.beanVar() + "." + this.isCmrLoadedVarName(var3) + "==true) {" + EOL);
                     var1.append("if (" + this.debugEnabled() + ") {" + EOL);
                     var1.append(this.debugSay() + "(\"copying cmr field '" + var3 + "' to bean '\" +" + EOL + this.beanVar() + ".__WL_getPrimaryKey() + \"'.\");" + EOL);
                     var1.append("}" + EOL);
                     var1.append("this." + CodeGenUtils.fieldVarName(var3) + " = " + this.beanVar() + "." + CodeGenUtils.fieldVarName(var3) + ";" + EOL);
                     var1.append("this." + this.isCmrLoadedVarName(var3) + " = true;" + EOL);
                     var1.append("}" + EOL);
                     var1.append("}" + EOL + EOL);
                  }

                  return var1.toString();
               }

               var3 = (String)var2.next();
            } while(!this.bean.containsFkField(var3));
         } while(this.bean.isForeignCmpField(var3));

         var1.append(this.checkFieldNotModifiedOrLoaded("this", var3));
         var1.append("if (" + this.beanVar() + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]==true) {" + EOL);
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append(this.debugSay() + "(\"copying field '" + var3 + "' to bean '\" +" + EOL + this.beanVar() + ".__WL_getPrimaryKey() + \"'.\");" + EOL);
         var1.append("}" + EOL);
         String var4 = this.bean.getTableForCmrField(var3);
         Iterator var5 = this.bean.getForeignKeyColNames(var3).iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            String var7 = this.bean.variableForField(var3, var4, var6);
            var1.append("this." + var7 + " = " + this.beanVar() + "." + var7 + ";" + EOL);
            if (this.bd.isOptimistic() && this.doSnapshot(var7)) {
               var1.append("if (__WL_initSnapshotVars) ");
               var1.append("this." + CodeGenUtils.snapshotNameForVar(var7) + " = " + this.beanVar() + "." + CodeGenUtils.snapshotNameForVar(var7) + ";" + EOL);
            }
         }

         var1.append("this." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]");
         var1.append(" = true;" + EOL);
         if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
            var1.append("if (!__WL_initSnapshotVars) {").append(EOL);
            var1.append("this." + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]" + " = true;" + EOL);
            var1.append(this.beanIsModifiedVar()).append(" = true;").append(EOL);
            var1.append("}").append(EOL);
         }

         var1.append("}" + EOL);
         var1.append("}" + EOL + EOL);
      }
   }

   public String tableName() {
      return this.bean.getQuotedTableName();
   }

   public String curTableName() {
      return this.curTableName;
   }

   public int curTableIndex() {
      return this.curTableIndex;
   }

   public String implementFinderMethods() throws EJBCException {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.implementFinderMethods() called.");
      }

      StringBuffer var1 = new StringBuffer(100);
      Iterator var2 = this.finderList.iterator();

      while(var2.hasNext()) {
         Finder var3 = (Finder)var2.next();
         if (var3.getQueryType() == 0 && !(var3 instanceof SqlFinder)) {
            try {
               if (debugLogger.isDebugEnabled()) {
                  debug("generating finder: " + var3);
               }

               var1.append(this.implementFinderMethod(var3));
            } catch (CodeGenerationException var8) {
               Loggable var5 = EJBLogger.logCouldNotGenerateFinderLoggable("finder", var3.toString(), var8.getMessage());
               throw new EJBCException(var5.getMessage());
            }
         }
      }

      Iterator var9 = this.bean.getRelationFinders();

      while(var9.hasNext()) {
         Finder var4 = (Finder)var9.next();
         if (var4.getQueryType() == 0) {
            try {
               var1.append(this.implementFinderMethod(var4));
            } catch (CodeGenerationException var7) {
               Loggable var6 = EJBLogger.logCouldNotGenerateFinderLoggable(" relation finder", var4.toString(), var7.getMessage());
               throw new EJBCException(var6.getMessage());
            }
         }
      }

      return var1.toString();
   }

   public String perhapsImplementRelCachingForDynamicFinders() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      List var3 = this.bean.getRelationshipCachings();
      Iterator var4 = var3.iterator();
      if (var3 != null) {
         var2 = var3.iterator().hasNext();
      }

      if (!var2) {
         return "";
      } else {
         while(true) {
            RelationshipCaching var5;
            String var6;
            do {
               if (!var4.hasNext()) {
                  return var1.toString();
               }

               var5 = (RelationshipCaching)var4.next();
               var6 = var5.getCachingName();
            } while(var6 == null && var6 == "");

            var1.append(this.generateMethodsToLoadBeansForCachingNames(var5, this.bean));
         }
      }
   }

   private String generateMethodsToLoadBeansForCachingNames(RelationshipCaching var1, RDBMSBean var2) throws CodeGenerationException {
      StringBuffer var3 = new StringBuffer();
      String var4 = var1.getCachingName();
      List var5 = var1.getCachingElements();
      var3.append(EOL + "public void loadBeansFor" + this.replaceIllegalJavaCharacters(var4) + "(java.sql.ResultSet " + this.rsVar() + ", CMPBean bean, " + "int groupColumnCount, QueryCachingHandler qcHandler) " + EOL);
      var3.append(" throws Exception {" + EOL + EOL);
      var3.append(EOL + "// load related beans " + EOL);
      var3.append(this.getGeneratedBeanClassName() + " " + this.beanVar() + " = (" + this.getGeneratedBeanClassName() + ") bean;" + EOL + EOL);
      this.perhapsRelationshipCachingPooledBeanVar(var2, var5.iterator(), (String)null, 1, var3);
      var3.append(EOL);
      this.perhapsRelationshipCachingPooledBeanVar(var2, var5.iterator(), (String)null, 2, var3);
      var3.append(EOL);
      var3.append("Integer " + this.offsetIntObjVar() + " = null;" + EOL);
      var3.append("Object " + this.eoRCVar() + " = null;" + EOL);
      var3.append("Object " + this.eoVar() + " = null;" + EOL);
      var3.append(EOL + "Map " + this.pkMapVar() + " = new HashMap();" + EOL);
      var3.append(this.declarePKMapVarForCachingElements(var2, var5.iterator(), (String)null));
      var3.append(EOL + EOL);
      var3.append("int increment = groupColumnCount;" + EOL);
      this.invokeEagerCachingMethodsForCachingElements(var2, var5.iterator(), (String)null, var3, true);
      var3.append("}" + EOL);
      return var3.toString();
   }

   public String implementLoaderMethodForDynamicFinders() {
      StringBuffer var1 = new StringBuffer();
      List var2 = this.bean.getRelationshipCachings();
      Iterator var3 = var2.iterator();
      var1.append("public void __WL_loadBeansRelatedToCachingName(String cachingName, java.sql.ResultSet rs, CMPBean bean, int groupColumnCount, QueryCachingHandler qcHandler)" + EOL);
      var1.append("throws Exception {" + EOL);

      while(true) {
         String var5;
         do {
            if (!var3.hasNext()) {
               var1.append("}" + EOL);
               return var1.toString();
            }

            RelationshipCaching var4 = (RelationshipCaching)var3.next();
            var5 = var4.getCachingName();
         } while(var5 == null && var5 != "");

         var1.append("if(cachingName.equals(\"" + var5 + "\")) {" + EOL);
         var1.append("loadBeansFor" + this.replaceIllegalJavaCharacters(var5) + "(rs, bean, groupColumnCount, qcHandler);" + EOL);
         var1.append("}" + EOL);
      }
   }

   private String implementFinderMethod(Finder var1) throws EJBCException, CodeGenerationException {
      if (debugLogger.isDebugEnabled()) {
         debug("implementFinderMethod(" + var1 + ") called.");
      }

      assert this.bd != null;

      this.curFinder = var1;
      if (this.curFinder == null) {
         Loggable var7 = EJBLogger.logNullFinderLoggable("implementFinderMethod");
         throw new EJBCException(var7.getMessage());
      } else {
         StringBuffer var2 = new StringBuffer();
         String var3 = null;
         if (var1.getQueryType() != 4 && var1.getQueryType() != 2) {
            var3 = MethodUtils.getFinderMethodDeclaration(var1, this.bd.getPrimaryKeyClass());
         } else {
            var3 = MethodUtils.getEjbSelectInternalMethodDeclaration(var1, this.bd.getPrimaryKeyClass());
         }

         var2.append(var3);
         var2.append("{" + EOL);

         try {
            if (this.curFinder.isMultiFinder()) {
               var2.append(this.parse(this.getProductionRule("finderMethodBodyMulti")));
            } else {
               var2.append(this.parse(this.getProductionRule("finderMethodBodyScalar")));
            }
         } catch (CodeGenerationException var6) {
            if (debugLogger.isDebugEnabled()) {
               debug("finderMethod cought CodeGenerationException : " + var6);
            }

            if (debugLogger.isDebugEnabled()) {
               var6.printStackTrace();
            }

            Loggable var5 = EJBLogger.logCouldNotProduceProductionRuleLoggable("finder ");
            throw var6;
         }

         var2.append("" + EOL + "}" + EOL + EOL);
         this.curFinder = null;
         return var2.toString();
      }
   }

   public String finderMethodName() {
      assert this.curFinder != null;

      return this.curFinder.getName();
   }

   public String firstPrimaryKeyColumn() {
      assert this.curTableName != null;

      Map var1 = this.bean.getPKCmpf2ColumnForTable(this.curTableName);
      String var2 = (String)var1.values().iterator().next();
      return var2;
   }

   public String finderQuery() {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.finderQuery() called for method " + this.curFinder.getName());
      }

      assert this.curFinder != null;

      assert this.curFinder.getSQLQuery() != null;

      String var1 = this.curFinder.getSQLQuery();
      return var1;
   }

   public String finderQueryForUpdate() {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.finderQueryForUpdate() called for method " + this.curFinder.getName());
      }

      assert this.curFinder != null;

      assert this.curFinder.getSQLQueryForUpdate() != null;

      String var1 = this.curFinder.getSQLQueryForUpdate();
      return var1;
   }

   public String finderQueryForUpdateNoWait() {
      if (debugLogger.isDebugEnabled()) {
         debug("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.finderQueryForUpdateNoWait() called for method " + this.curFinder.getName());
      }

      assert this.curFinder != null;

      assert this.curFinder.getSQLQueryForUpdateNoWait() != null;

      String var1 = this.curFinder.getSQLQueryForUpdateNoWait();
      return var1;
   }

   private String finderQueryForUpdateSelective() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.finderQueryForUpdateSelective() called for method " + this.curFinder.getName());
      }

      if (debugLogger.isDebugEnabled()) {
         Debug.assertion(this.curFinder != null);
      }

      String var1 = this.curFinder.getSQLQueryForUpdateSelective();
      return var1;
   }

   public String generateSqlQueryOrSqlQueryForUpdateOrSqlQueryForUpdateOf() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("ejb.container.cmp.rdbms.codegen.RDBMSCodeGenerator.generateSqlQueryOrSqlQueryForUpdateOrSqlQueryForUpdateOf called for method " + this.curFinder.getName());
      }

      if (this.finderQueryForUpdateSelective() != null) {
         return this.queryVar() + " = \"" + this.finderQueryForUpdateSelective() + "\"";
      } else {
         return this.bean.getUseSelectForUpdate() ? this.queryVar() + " = \"" + this.finderQueryForUpdate() + "\"" : this.queryVar() + " = \"" + this.finderQuery() + "\"";
      }
   }

   public String perhapsSetMaxRows() {
      StringBuffer var1 = new StringBuffer();
      if (this.curFinder.getMaxElements() != 0) {
         var1.append(this.stmtVar() + ".setMaxRows(" + this.curFinder.getMaxElements() + ");" + EOL);
      }

      return var1.toString();
   }

   public String perhapsFlushCaches() {
      StringBuffer var1 = new StringBuffer();
      if (debugLogger.isDebugEnabled()) {
         debug("perhaps flush caches for finder- " + this.curFinder.getName() + "include updates- " + this.curFinder.getIncludeUpdates());
      }

      if (this.curFinder.getIncludeUpdates() && !this.curFinder.isFindByPrimaryKey()) {
         var1.append(this.pmVar() + ".flushModifiedBeans();" + EOL);
      }

      return var1.toString();
   }

   public String findOrEjbSelectBeanNotFound() {
      StringBuffer var1 = new StringBuffer();
      var1.append("throw new ");
      var1.append("javax.ejb.ObjectNotFoundException(");
      if (this.curFinder.isFindByPrimaryKey()) {
         var1.append("\"Bean with primary key '\" + param0.toString() + \"' was not found by 'findByPrimaryKey'.\");");
      } else {
         var1.append("\"Bean not found in '");
         var1.append(this.finderMethodName()).append("'.\");");
      }

      return var1.toString();
   }

   public String declareResultVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.curFinder.isMultiFinder()) {
         if (this.curFinder.isSetFinder()) {
            var1.append(this.declareOrderedSetVar() + EOL);
         } else {
            var1.append(this.declareColVar() + EOL);
         }
      }

      if (this.curFinder.finderLoadsBean()) {
         var1.append(this.declareBeanVar());
         var1.append(this.declareEoVar());
      } else {
         var1.append(this.declarePkVar());
      }

      return var1.toString();
   }

   public String declareColVar() {
      return "java.util.Collection " + this.colVar() + " = new java.util.ArrayList();";
   }

   public String declareSetVar() {
      return "java.util.Set " + this.setVar() + " = new java.util.HashSet();";
   }

   public String declareOrderedSetVar() {
      return "java.util.Set " + this.orderedSetVar() + " = new weblogic.ejb20.utils.OrderedSet();";
   }

   public String resultVar() {
      StringBuffer var1 = new StringBuffer();
      if (this.curFinder.isMultiFinder()) {
         if (this.curFinder.isSetFinder()) {
            var1.append(this.orderedSetVar());
         } else {
            var1.append(this.colVar());
         }
      } else if (this.curFinder.finderLoadsBean()) {
         var1.append(this.eoVar());
      } else {
         var1.append(this.pkVar());
      }

      return var1.toString();
   }

   public String getSimpleGeneratedBeanClassName() {
      return MethodUtils.tail(this.bd.getGeneratedBeanClassName());
   }

   public String getGeneratedBeanClassName() {
      return this.bd.getGeneratedBeanClassName();
   }

   public String getGeneratedBeanInterfaceName() {
      return this.bd.getGeneratedBeanInterfaceName();
   }

   public String declareBeanVar() {
      return this.getGeneratedBeanClassName() + " " + this.beanVar() + " = null;" + EOL;
   }

   public String getBeanFromRS() {
      StringBuffer var1 = new StringBuffer();
      var1.append("RSInfo " + this.rsInfoVar() + " = new RSInfoImpl(" + this.rsVar() + ", " + this.curGroup.getIndex() + ", 0, " + this.pkVar() + ");" + EOL);
      var1.append(this.beanVar() + " = (" + this.getGeneratedBeanClassName() + ")" + this.pmVar() + ".getBeanFromRS(" + this.pkVar() + ", " + this.rsInfoVar() + ");");
      var1.append(EOL + EOL);
      return var1.toString();
   }

   public String allocateBeanVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.beanVar() + " = (" + this.getGeneratedBeanClassName() + ")" + this.pmVar() + ".getBeanFromPool();" + EOL);
      var1.append(this.beanVar() + ".__WL_initialize();" + EOL);
      return var1.toString();
   }

   public String declareEoVar() {
      return "Object " + this.eoVar() + " = null;" + EOL + "Object " + this.eoRCVar() + " = null;" + EOL;
   }

   public String finderColumnsSql() throws EJBCException {
      StringBuffer var1 = new StringBuffer();
      List var2 = null;
      if (this.curFinder.finderLoadsBean()) {
         var2 = this.cmpFieldNames;
      } else {
         var2 = this.pkFieldNames;
      }

      Iterator var3 = var2.iterator();

      for(int var4 = 0; var3.hasNext(); ++var4) {
         String var5 = (String)var3.next();
         String var6 = this.bean.getCmpColumnForField(var5);

         assert var6 != null;

         var1.append(RDBMSUtils.escQuotedID(var6));
         if (var4 < var2.size() - 1) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   public String setFinderQueryParams() throws EJBCException {
      StringBuffer var1 = new StringBuffer();
      var1.append("\n");
      Finder var2 = this.curFinder;

      assert var2 != null;

      assert var2.getSQLQuery() != null;

      int var3 = var2.getQueryType();
      Class[] var4 = null;
      String[] var5 = null;
      Object var6 = null;
      String var12;
      if (var2 instanceof EjbqlFinder) {
         EjbqlFinder var7 = (EjbqlFinder)var2;
         if (var7.isKeyFinder() && var7.getKeyBean().getCMPBeanDescriptor().hasComplexPrimaryKey()) {
            var6 = new ArrayList();
            RDBMSBean var8 = var7.getKeyBean();
            CMPBeanDescriptor var9 = var8.getCMPBeanDescriptor();
            String[] var10 = (String[])((String[])var9.getPrimaryKeyFieldNames().toArray(new String[0]));
            var4 = new Class[var10.length];
            var5 = new String[var10.length];

            for(int var11 = 0; var11 < var4.length; ++var11) {
               var4[var11] = var9.getFieldClass(var10[var11]);
               var12 = "param0." + var10[var11];
               ParamNode var13 = new ParamNode(var8, var12, var11 + 1, var4[var11], "", "", false, false, (Class)null, false, RDBMSUtils.isOracleNLSDataType(var8, var10[var11]));
               ((List)var6).add(var13);
            }
         } else {
            var6 = var2.getInternalQueryAndInEntityParmList();
         }
      }

      int var19 = 0;
      if (debugLogger.isDebugEnabled()) {
         debug("\n  prepStmt setXX processing, parameterList is: " + var6);
      }

      Iterator var20 = ((List)var6).iterator();

      while(true) {
         while(true) {
            while(var20.hasNext()) {
               ParamNode var21 = (ParamNode)var20.next();
               ++var19;
               Iterator var15;
               ParamNode var16;
               String var17;
               Class var18;
               String var23;
               String var25;
               Class var26;
               String var28;
               boolean var30;
               if (var21.isBeanParam()) {
                  var23 = var21.getParamName();
                  var25 = this.varPrefix() + var23 + "_PK";
                  var26 = var21.getPrimaryKeyClass();
                  var28 = var26.getName();
                  var1.append(var28 + " " + var25 + ";" + EOL);
                  var1.append(var25 + " = " + "(" + var28 + ")" + var23 + ".getPrimaryKey();" + EOL + EOL);
                  if (var21.hasCompoundKey()) {
                     var30 = false;
                     var15 = var21.getParamSubList().iterator();

                     while(var15.hasNext()) {
                        var16 = (ParamNode)var15.next();
                        var17 = this.varPrefix() + var23 + "_PK." + var16.getId();
                        var18 = var16.getParamClass();
                        this.writePrepStmtSet(var1, var19, var17, var18, var16.isOracleNLSDataType());
                        if (var15.hasNext()) {
                           ++var19;
                        }
                     }
                  } else {
                     this.writePrepStmtSet(var1, var19, var25, var26, var21.isOracleNLSDataType());
                  }
               } else if (var21.isSelectInEntity()) {
                  if (var3 != 5 && var3 != 3) {
                     var23 = var21.getParamName();
                     var25 = this.varPrefix() + var23 + "_PK";
                     var26 = var21.getPrimaryKeyClass();
                     var28 = var26.getName();
                     var1.append(var28).append(" ").append(var25).append(";");
                     var1.append(EOL);
                     var1.append(var25).append(" = ");
                     var1.append("(").append(var28).append(")");
                     var1.append(var23).append(";");
                     var1.append(EOL);
                     if (var21.hasCompoundKey()) {
                        var30 = false;
                        var15 = var21.getParamSubList().iterator();

                        while(var15.hasNext()) {
                           var16 = (ParamNode)var15.next();
                           var17 = this.varPrefix() + var23 + "_PK." + var16.getId();
                           var18 = var16.getParamClass();
                           this.writePrepStmtSet(var1, var19, var17, var18, var16.isOracleNLSDataType());
                           if (var15.hasNext()) {
                              ++var19;
                           }
                        }
                     } else {
                        this.writePrepStmtSet(var1, var19, var25, var26, var21.isOracleNLSDataType());
                     }
                  } else {
                     Iterator var22 = var21.getParamSubList().iterator();

                     while(var22.hasNext()) {
                        ParamNode var24 = (ParamNode)var22.next();
                        if (debugLogger.isDebugEnabled()) {
                           debug(" process Select In Entity returning Field  param node: " + var24.toString());
                        }

                        var12 = var24.getId();
                        Class var27 = var24.getParamClass();
                        if (var27 == null) {
                           Loggable var29 = EJBLogger.logCouldNotGetClassForParamLoggable(this.curFinder.getName(), var12);
                           throw new EJBCException(var29.getMessage());
                        }

                        String var14 = this.varPrefix() + var12;
                        var1.append(var27.getName());
                        var1.append(" ");
                        var1.append(var14 + " = ");
                        var1.append(MethodUtils.getMethodName(var12) + "();");
                        var1.append(EOL);
                        this.writePrepStmtSet(var1, var19, var14, var27, var24.isOracleNLSDataType());
                     }
                  }
               } else {
                  if (debugLogger.isDebugEnabled()) {
                     debug(" process param node: " + var21.toString());
                  }

                  this.writePrepStmtSet(var1, var19, var21.getParamName(), var21.getParamClass(), var21.isOracleNLSDataType());
               }
            }

            return var1.toString();
         }
      }
   }

   private void writePrepStmtSet(StringBuffer var1, int var2, String var3, Class var4, boolean var5) throws EJBCException {
      if (!var4.isPrimitive()) {
         this.addNullCheck(var1, var3, ClassUtils.getSQLTypeForClass(var4), var2);
      }

      this.preparedStatementBindingBody(var1, var3, var3, var4, String.valueOf(var2), false, false, false, false, var5);
      if (!var4.isPrimitive()) {
         var1.append("}" + EOL);
      }

   }

   private void addNullCheck(StringBuffer var1, String var2, String var3, String var4) {
      this.addNullCheck(var1, var2, var3, var4, this.stmtVar());
   }

   private void addNullCheck(StringBuffer var1, String var2, String var3, String var4, String var5) {
      var1.append("if(!" + this.pmVar() + ".setParamNull(" + var5 + ", " + var4 + ", " + var2 + ", " + "\"" + var3 + "\"" + ")) {" + EOL);
   }

   private void addNullCheck(StringBuffer var1, String var2, String var3, int var4) {
      this.addNullCheck(var1, var2, var3, var4, this.stmtVar());
   }

   private void addNullCheck(StringBuffer var1, String var2, String var3, int var4, String var5) {
      var1.append("if (" + var2 + " == null) {" + EOL);
      var1.append(var5 + ".setNull(" + var4 + "," + var3 + ");" + EOL);
      var1.append("} else {" + EOL);
   }

   private String getCmpField(String var1) {
      return this.getCmpField("this", var1);
   }

   private String getCmpField(String var1, String var2) {
      return this.bd.isBeanClassAbstract() ? var1 + "." + var2 : var1 + "." + "__WL_super_" + MethodUtils.getMethodName(var2) + "()";
   }

   private String setCmpField(String var1, String var2) {
      return this.bd.isBeanClassAbstract() ? "this." + var1 + " = " + var2 : "__WL_super_" + MethodUtils.setMethodName(var1) + "(" + var2 + ")";
   }

   public String assignPkFieldsToPkVar() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bd.getPrimaryKeyFieldNames().iterator();
      if (this.bd.hasComplexPrimaryKey()) {
         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.pkVar() + "." + var3 + " = " + this.getCmpField(var3) + ";" + EOL);
         }
      } else {
         var1.append(this.pkVar() + " = " + this.getCmpField((String)var2.next()) + ";");
      }

      return var1.toString();
   }

   public String assignPkVarToPkFields() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bd.getPrimaryKeyFieldNames().iterator();
      if (this.bd.hasComplexPrimaryKey()) {
         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(this.setCmpField(var3, this.pkVar() + "." + var3) + ";" + EOL);
         }
      } else {
         var1.append(this.setCmpField((String)var2.next(), this.pkVar()) + ";");
      }

      return var1.toString();
   }

   public String assignResultVar() throws CodeGenerationException {
      assert this.curFinder != null;

      StringBuffer var1 = new StringBuffer();
      boolean var2 = this.curFinder.isMultiFinder() && (this.curFinder.isSetFinder() || this.curFinder.isSelectDistinct());
      if (this.curFinder.finderLoadsBean()) {
         boolean var3 = false;
         String var5;
         if (this.curFinder instanceof EjbqlFinder) {
            EjbqlFinder var4 = (EjbqlFinder)this.curFinder;
            var5 = var4.getGroupName();
            this.curGroup = this.bean.getFieldGroup(var5);
            String var6 = var4.getCachingName();
            if (var6 != null) {
               this.curRelationshipCaching = this.bean.getRelationshipCaching(var6);
               if (this.curRelationshipCaching != null) {
                  var3 = this.curRelationshipCaching.getCachingElements().iterator().hasNext();
               }
            }
         }

         var1.append(EOL);
         var1.append("Integer " + this.offsetIntObjVar() + " = new Integer(0);" + EOL);
         var1.append("Object " + this.pkVar() + " = " + this.getPKFromRSMethodName() + this.getPKFromRSMethodParams() + EOL);
         var1.append(this.eoVar() + " = null;" + EOL);
         var1.append("if (" + this.pkVar() + " != null) { " + EOL);
         if (var3) {
            var1.append("if (!" + this.pkMapVar() + ".containsKey(" + this.pkVar() + ")) {" + EOL);
         } else if (var2) {
            if (!this.bean.getUseSelectForUpdate()) {
               var1.append("if (updateLockType == ").append("DDConstants.UPDATE_LOCK_NONE || ").append("updateLockType == ").append("DDConstants.UPDATE_LOCK_AS_GENERATED || ").append("(!" + this.pkMapVar() + ".containsKey(" + this.pkVar() + "))) {");
            } else {
               var1.append("if ").append("(!" + this.pkMapVar() + ".containsKey(" + this.pkVar() + ")) {");
            }

            var1.append(EOL);
         }

         var1.append(this.perhapsLoadPKsForQueryCaching());
         var1.append(this.getBeanFromRS());
         var1.append(this.eoVar() + " = " + this.pmVar() + ".finderGetEoFromBeanOrPk(" + this.beanVar() + ", " + this.pkVar() + ", __WL_getIsLocal());" + EOL);
         var1.append("if (" + this.debugEnabled() + ") " + this.debugSay() + "(\"bean after finder load: \" + ((__WL_bean == null) ? \"null\" : __WL_bean.hashCode()));" + EOL);
         if (this.curFinder.isMultiFinder()) {
            var1.append("if( " + this.beanVar() + " == null || ( " + this.beanVar() + " != null &&" + " !" + this.beanVar() + ".__WL_getIsRemoved()))" + EOL);
            var1.append("{" + EOL);
            if (this.curFinder.isSetFinder()) {
               var1.append(this.orderedSetVar() + ".add(" + this.eoVar() + ");" + EOL);
            } else {
               var1.append(this.colVar() + ".add(" + this.eoVar() + ");" + EOL);
            }

            var1.append("}" + EOL);
         }

         var1.append("Object __WL_retVal = " + this.pkMapVar() + ".put(" + this.pkVar() + ", " + this.beanVar() + ");" + EOL);
         if (var3) {
            Iterator var7 = this.bean.getCmrFieldNames().iterator();

            while(var7.hasNext()) {
               var5 = (String)var7.next();
               var1.append(this.beanVar() + "." + CodeGenUtils.fieldVarName(var5) + " = null;" + EOL);
            }
         }

         if (!var3 && !this.curFinder.isMultiFinder()) {
            var1.append("if (__WL_retVal!=null) " + this.isMultiVar() + "=true;" + EOL);
         }

         if (var3) {
            var1.append("} else {" + EOL);
            var1.append(this.beanVar() + " = (" + this.getGeneratedBeanClassName() + ") " + this.pkMapVar() + ".get(" + this.pkVar() + ");" + EOL);
            var1.append(this.eoVar() + " = " + this.pmVar() + ".finderGetEoFromBeanOrPk(" + this.beanVar() + ", " + this.pkVar() + ", __WL_getIsLocal());" + EOL);
            var1.append("}" + EOL);
         } else if (var2) {
            var1.append("}").append(EOL);
         }

         if (this.curFinder.isMultiFinder()) {
            var1.append("} else {").append(EOL);
            var1.append("if (").append(this.pmVar()).append(".isFindersReturnNulls()) {").append(EOL);
            if (this.curFinder.isSetFinder()) {
               var1.append(this.orderedSetVar()).append(".add(null);").append(EOL);
            } else {
               var1.append(this.colVar()).append(".add(null);").append(EOL);
            }

            var1.append("}");
         } else if (!this.curFinder.isFindByPrimaryKey()) {
            var1.append("} else {").append(EOL);
            var1.append("if (").append(this.pmVar()).append(".isFindersReturnNulls()) {").append(EOL);
            var1.append(this.pkMapVar()).append(".put(null, null); ").append(EOL);
            var1.append("}");
         }

         var1.append("}").append(EOL).append(EOL);
         if (var3) {
            var1.append(this.invokeEagerCachingMethods() + EOL);
         }

         this.curGroup = null;
      } else {
         if (this.bd.hasComplexPrimaryKey()) {
            var1.append(this.pkVar()).append(" = new ");
            var1.append(this.pk_class()).append("();").append(EOL);
         }

         this.assignToVars(var1, this.pkVar(), this.bd.hasComplexPrimaryKey(), this.pkFieldNames, 1, false);
         if (var2) {
            if (!this.bean.getUseSelectForUpdate()) {
               var1.append("if (updateLockType == ").append("DDConstants.UPDATE_LOCK_NONE || ").append("updateLockType == ").append("DDConstants.UPDATE_LOCK_AS_GENERATED || ").append("(!" + this.pkMapVar() + ".containsKey(" + this.pkVar() + "))) {");
            } else {
               var1.append("if ").append("(!" + this.pkMapVar() + ".containsKey(" + this.pkVar() + ")) {");
            }
         }

         var1.append(this.perhapsLoadPKsForQueryCaching());
         if (this.curFinder.isMultiFinder()) {
            var1.append("if (").append(this.rsVar()).append(".wasNull()) {").append(EOL);
            var1.append("if (").append(this.pmVar()).append(".isFindersReturnNulls()) {").append(EOL);
            var1.append(this.pkVar()).append(" = null;").append(EOL);
            if (this.curFinder.isSetFinder()) {
               var1.append(this.orderedSetVar() + ".add(" + this.pkVar() + ");" + EOL);
            } else {
               var1.append(this.colVar() + ".add(" + this.pkVar() + ");").append(EOL);
            }

            var1.append("}").append(EOL);
            var1.append("}").append(EOL);
            var1.append("else {").append(EOL);
            if (this.curFinder.isSetFinder()) {
               var1.append(this.orderedSetVar() + ".add(" + this.pkVar() + ");" + EOL);
            } else {
               var1.append(this.colVar() + ".add(" + this.pkVar() + ");").append(EOL);
            }

            var1.append("}").append(EOL);
         } else if (!this.curFinder.isFindByPrimaryKey()) {
            var1.append("if (").append(this.rsVar()).append(".wasNull()) {").append(EOL);
            var1.append(this.pkVar()).append(" = null;").append(EOL);
            var1.append("if (").append(this.pmVar()).append(".isFindersReturnNulls()) {").append(EOL);
            var1.append(this.pkMapVar()).append(".put(null, null);").append(EOL);
            var1.append("}").append(EOL);
            var1.append("}").append(EOL);
         }

         var1.append("if (" + this.pkVar() + " != null) {");
         var1.append("Object __WL_retVal = " + this.pkMapVar() + ".put(" + this.pkVar() + ", " + this.pkVar() + ");" + EOL);
         if (!this.curFinder.isMultiFinder()) {
            var1.append("if (__WL_retVal!=null) " + this.isMultiVar() + "=true;" + EOL);
         }

         var1.append("}" + EOL);
         if (var2) {
            var1.append("}").append(EOL);
         }
      }

      return var1.toString();
   }

   public String invokeLoadGroupMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.loadGroupFromRSMethodName(this.curGroup) + "(" + this.rsVar() + ", " + this.offsetIntObjVar() + ", " + this.pkVar() + ", " + this.beanVar() + ");" + EOL);
      return var1.toString();
   }

   private String perhapsLoadPKsForQueryCaching() {
      StringBuffer var1 = new StringBuffer("");
      if (this.shouldImplementQueryCaching(this.curFinder)) {
         var1.append("// Load PK for query-caching - start").append(EOL);
         var1.append("QueryCacheElement ").append(this.queryCacheElementVar());
         var1.append(" = null;").append(EOL);
         var1.append("if (").append(this.pkVar()).append(" == null && ");
         var1.append(this.pmVar()).append(".isFindersReturnNulls()) {").append(EOL);
         var1.append(this.queryCacheElementVar()).append(" = new QueryCacheElement(");
         var1.append("null);").append(EOL);
         var1.append("} else if (").append(this.pkVar()).append(" != null) {");
         var1.append(EOL);
         var1.append(this.queryCacheElementVar()).append(" = new QueryCacheElement(");
         var1.append(this.pkVar()).append(", (TTLManager)").append(this.pmVar());
         var1.append(".getBeanManager());").append(EOL);
         var1.append("}").append(EOL);
         var1.append("if (").append(this.queryCacheElementVar()).append(" != null) {");
         var1.append(EOL);
         if (this.curFinder.isMultiFinder()) {
            var1.append(this.queryCacheElementsVar()).append(".add(");
            var1.append(this.queryCacheElementVar()).append(");").append(EOL);
         } else {
            var1.append(this.queryCacheElementsVar()).append(" = ");
            var1.append(this.queryCacheElementVar()).append(";").append(EOL);
         }

         var1.append("}").append(EOL);
         var1.append("// Load PK for query-caching - end").append(EOL);
      }

      return var1.toString();
   }

   private Class getVariableClass(String var1) {
      return (Class)this.variableToClass.get(var1);
   }

   private String checkFieldNotModifiedOrLoaded(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("if (!(" + var1 + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var2) + "]");
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         var3.append(" || " + var1 + "." + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var2) + "]");
      }

      var3.append(")) {" + EOL);
      return var3.toString();
   }

   private void assignToVars(StringBuffer var1, String var2, boolean var3, List var4, int var5, boolean var6) throws CodeGenerationException {
      Iterator var7 = var4.iterator();
      int var8 = var4.size();

      while(true) {
         while(var7.hasNext()) {
            String var9 = (String)var7.next();
            Class var10 = this.getVariableClass(var9);
            boolean var11 = var6 && !this.bd.isBeanClassAbstract() && this.bean.hasCmpField(var9);
            String var12 = null;
            if (var3) {
               var12 = var2 + "." + var9;
            } else {
               var12 = var2;
            }

            boolean var13 = this.bean.isCharArrayMappedToString(var10);
            if (var13) {
               var10 = Character.TYPE;
            }

            String var14 = this.getFromResultSet(var5, var10, this.bean.getCmpColumnTypeForField(var9));
            if (var6) {
               var1.append(this.checkFieldNotModifiedOrLoaded(var2, (String)this.variableToField.get(var9)));
            }

            if (this.bean.hasCmpColumnType(var9) && !"SybaseBinary".equals(this.bean.getCmpColumnTypeForField(var9)) && !"LongString".equals(this.bean.getCmpColumnTypeForField(var9)) && !"NChar".equals(this.bean.getCmpColumnTypeForField(var9)) && !"NVarchar2".equals(this.bean.getCmpColumnTypeForField(var9)) && !"SQLXML".equals(this.bean.getCmpColumnTypeForField(var9))) {
               if (!this.bean.isBlobCmpColumnTypeForField(var9) && !this.bean.isClobCmpColumnTypeForField(var9)) {
                  throw new AssertionError("Unrecognized Cmp Column Type " + this.bean.getCmpColumnTypeForField(var9) + " for field " + var9);
               }

               this.curField = var9;
               var1.append("\n" + this.setBlobClobForInputMethodName() + "(" + this.rsVar() + "," + var2 + ");");
               if (var6) {
                  var1.append("}" + EOL);
               }

               ++var5;
            } else {
               boolean var15 = false;
               if (this.bean.hasCmpColumnType(var9) && "SQLXML".equals(this.bean.getCmpColumnTypeForField(var9))) {
                  if (var10 != String.class) {
                     throw new CodeGenerationException("only String can be mapped into SQLXML : " + var9);
                  }

                  var15 = true;
               }

               String var16;
               if (!this.bean.isValidSQLType(var10)) {
                  var1.append("byte[] byteArray = " + var14 + ";" + EOL);
                  var1.append("if (" + this.debugEnabled() + ") {" + EOL);
                  var1.append(this.debugSay() + "(\"returned bytes\" + byteArray " + ");" + EOL);
                  var1.append("if (byteArray!=null) {" + EOL);
                  var1.append(this.debugSay() + "(\"length- \" + byteArray.length);" + EOL);
                  var1.append("}" + EOL);
                  var1.append("}" + EOL);
                  var1.append("if (byteArray==null || byteArray.length==0) { " + EOL);
                  this.doSimpleAssignment(var1, var12, var9, "null", var11);
                  if (var6 && this.bd.isOptimistic() && this.doSnapshot(var9)) {
                     var1.append(var2 + "." + CodeGenUtils.snapshotNameForVar(var9) + " = null;" + EOL);
                  }

                  var1.append("}" + EOL);
                  var16 = "(" + this.javaCodeForType(var10) + ")";
                  var1.append("else { " + EOL);
                  if (var6 && this.bd.isOptimistic() && this.doSnapshot(var9)) {
                     var1.append(var2 + "." + CodeGenUtils.snapshotNameForVar(var9) + " = byteArray;" + EOL);
                  }

                  var1.append("ByteArrayInputStream bstr = new java.io.ByteArrayInputStream(byteArray);" + EOL);
                  var1.append("RDBMSObjectInputStream ostr = new RDBMSObjectInputStream(bstr, " + this.classLoaderVar() + ");" + EOL);
                  if (EJBHome.class.isAssignableFrom(var10)) {
                     var1.append("HomeHandle handle = (HomeHandle)ostr.readObject();" + EOL);
                     this.doSimpleAssignment(var1, var12, var9, var16 + "handle.getEJBHome();", var11);
                  } else if (EJBObject.class.isAssignableFrom(var10)) {
                     var1.append("Handle handle = (Handle)ostr.readObject();" + EOL);
                     this.doSimpleAssignment(var1, var12, var9, var16 + "handle.getEJBObject();", var11);
                  } else {
                     this.doSimpleAssignment(var1, var12, var9, var16 + "ostr.readObject();", var11);
                  }

                  var1.append("}" + EOL);
               } else {
                  var16 = this.resultSetToVariable(var5, var10, this.bean.getCmpColumnTypeForField(var9));
                  if (var13) {
                     var1.append("String " + this.stringVar(var5) + " = " + var16 + ";" + EOL);
                     var1.append("if (" + this.stringVar(var5) + "==null ) { " + EOL);
                     this.doSimpleAssignment(var1, var12, var9, "null", var11);
                     var1.append("}" + EOL);
                     var1.append("else { " + EOL);
                     var1.append(this.trimStringTypedValue(this.stringVar(var5)));
                     this.doSimpleAssignment(var1, var12, var9, this.stringVar(var5) + ".toCharArray();", var11);
                     var1.append("}" + EOL);
                  } else if (var10 != Character.class && var10 != Character.TYPE) {
                     if (var10 == Date.class) {
                        var1.append("java.sql.Timestamp " + this.sqlTimestampVar(var5) + " = " + var16 + ";" + EOL);
                        var1.append("if (" + this.sqlTimestampVar(var5) + "==null) { " + EOL);
                        this.doSimpleAssignment(var1, var12, var9, ClassUtils.getDefaultValue(var10), var11);
                        var1.append("}" + EOL);
                        var1.append("else { " + EOL);
                        this.doSimpleAssignment(var1, var12, var9, "new java.util.Date(" + this.sqlTimestampVar(var5) + ".getTime())", var11);
                        var1.append("}" + EOL);
                     } else {
                        String var17;
                        if (var10 == String.class) {
                           var17 = null;
                           if (var11) {
                              var17 = this.stringVar(var5);
                              var1.append("String ");
                           } else {
                              var17 = var12;
                           }

                           if (var15) {
                              var16 = var16 + " != null ? " + var16 + ".getString() : null";
                           }

                           var1.append(var17 + " = " + var16 + ";" + EOL);
                           var1.append("if(");
                           var1.append(var17);
                           var1.append(" != null) {");
                           var1.append(EOL);
                           var1.append(this.trimStringTypedValue(var17));
                           var1.append("}");
                           var1.append(EOL);
                           if (var11) {
                              var1.append(MethodUtils.setMethodName(var9) + "(" + var17 + ");" + EOL);
                           }
                        } else if (var10 != BigDecimal.class && var10 != java.sql.Date.class && var10 != Timestamp.class) {
                           this.doAssignment(var1, var12, var5, var9, var10, var16, var11);
                        } else {
                           var17 = null;
                           if (var11) {
                              var17 = this.tempVar(var5);
                              var1.append(var10.getName()).append(" ");
                           } else {
                              var17 = var12;
                           }

                           var1.append(var17 + " = " + var16 + ";" + EOL);
                           if (var11) {
                              var1.append(MethodUtils.setMethodName(var9) + "(" + var17 + ");" + EOL);
                           }
                        }
                     }
                  } else {
                     var1.append("String " + this.stringVar(var5) + " = " + var16 + ";" + EOL);
                     var1.append("if (" + this.stringVar(var5) + "==null || " + this.stringVar(var5) + ".length()==0) { " + EOL);
                     this.doSimpleAssignment(var1, var12, var9, ClassUtils.getDefaultValue(var10), var11);
                     var1.append("}" + EOL);
                     var1.append("else { " + EOL);
                     if (var10 == Character.class) {
                        this.doSimpleAssignment(var1, var12, var9, "new Character(" + this.stringVar(var5) + ".charAt(0))", var11);
                     } else {
                        this.doSimpleAssignment(var1, var12, var9, this.stringVar(var5) + ".charAt(0);", var11);
                     }

                     var1.append("}" + EOL);
                  }

                  if (var6 && this.bd.isOptimistic() && this.doSnapshot(var9)) {
                     var1.append(this.takeSnapshotForVar(var2, var9, var11));
                  }
               }

               if (var6) {
                  var1.append("}" + EOL);
                  var1.append("else {" + EOL);
                  var1.append(var14 + ";" + EOL);
                  var1.append("}" + EOL);
               }

               ++var5;
            }
         }

         return;
      }
   }

   private void doAssignment(StringBuffer var1, String var2, int var3, String var4, Class var5, String var6, boolean var7) {
      if (var7) {
         if (var5.isPrimitive()) {
            var1.append("__WL_super_");
            var1.append(MethodUtils.setMethodName(var4) + "(" + var6 + ");" + EOL);
         } else {
            var1.append(this.javaCodeForType(var5) + " " + this.tempVar(var3) + " = " + var6 + ";" + EOL);
            var1.append("if (" + this.rsVar() + ".wasNull()) { " + EOL);
            var1.append("__WL_super_");
            var1.append(MethodUtils.setMethodName(var4) + "(null);" + EOL);
            var1.append("} else {" + EOL);
            var1.append("__WL_super_");
            var1.append(MethodUtils.setMethodName(var4) + "(" + this.tempVar(var3) + ");" + EOL);
            var1.append("}" + EOL);
         }
      } else {
         var1.append(var2 + " = " + var6 + ";" + EOL);
         if (!var5.isPrimitive()) {
            var1.append("if (" + this.rsVar() + ".wasNull()) { " + EOL);
            var1.append(var2 + " = null;" + EOL);
            var1.append("}" + EOL);
         }
      }

   }

   private void doSimpleAssignment(StringBuffer var1, String var2, String var3, String var4, boolean var5) {
      if (var5) {
         var1.append("__WL_super_");
         var1.append(MethodUtils.setMethodName(var3) + "(" + var4 + ");" + EOL);
      } else {
         var1.append(var2 + " = " + var4 + ";" + EOL);
      }

   }

   private String resultSetToVariable(int var1, Class var2, String var3) {
      String var4 = "";
      String var5 = this.getFromResultSet(var1, var2, var3);
      if (var2 == Boolean.class) {
         return "new Boolean(" + var5 + ")";
      } else if (var2 == Byte.class) {
         return "new Byte(" + var5 + ")";
      } else if (var2 != Character.class && var2 != Character.TYPE) {
         if (var2 == Double.class) {
            return "new Double(" + var5 + ")";
         } else if (var2 == Float.class) {
            return "new Float(" + var5 + ")";
         } else if (var2 == Integer.class) {
            return "new Integer(" + var5 + ")";
         } else if (var2 == Long.class) {
            return "new Long(" + var5 + ")";
         } else if (var2 == Short.class) {
            return "new Short(" + var5 + ")";
         } else if (var2 == Date.class) {
            var4 = "(java.sql.Timestamp)";
            return var4 + var5;
         } else {
            return var4 + var5;
         }
      } else {
         return "(java.lang.String)" + var5;
      }
   }

   private String getFromResultSet(int var1, Class var2, String var3) {
      String var4;
      if (var3 != null && "SQLXML".equals(var3)) {
         var4 = "SQLXML";
      } else {
         var4 = MethodUtils.getResultSetMethodPostfix(var2);
      }

      String var5 = this.rsVar() + ".get" + var4 + "(" + var1 + "+" + this.offsetVar() + ")";
      return var5;
   }

   public String indexForTable() {
      return String.valueOf(this.bean.tableIndex(this.curTable));
   }

   public String updateBeanColumnsSqlForTable(String var1) {
      List var2 = this.bean.getNonPKFields(this.bean.tableIndex(var1));

      assert var2 != null;

      String[] var3 = (String[])((String[])var2.toArray(new String[0]));
      return this.attrsAsColumnsAsParamsForTable(var1, var3, ", ");
   }

   public String idParamsSqlForCurTable() {
      return this.idParamsSqlForTable(this.curTableName);
   }

   public String idParamsSqlForTable(String var1) {
      String[] var2 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
      return this.attrsAsColumnsAsParamsForTable(var1, var2, " AND ");
   }

   public String idParamsSql() {
      String[] var1 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));

      assert var1 != null;

      return this.attrsAsColumnsAsParams(var1, " AND ");
   }

   public String idColumnsSql() {
      String[] var1 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));

      assert var1 != null;

      return StringUtils.join(var1, ", ");
   }

   private List getAllVarNames() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.cmpFieldNames);
      Iterator var2 = this.bean.getForeignKeyFieldNames().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (this.bean.containsFkField(var3)) {
            var1.addAll(this.foreignKeyVarNames(var3));
         }
      }

      return var1;
   }

   public String groupSqlNonFinder() throws CodeGenerationException {
      try {
         return EjbqlFinder.generateGroupSQLNonFinder(this.bean, this.curGroup.getName(), 0);
      } catch (Exception var2) {
         throw new CodeGenerationException("Internal Error while attempting to generate an Internal Finder for FieldGroup: '" + this.curGroup.getName());
      }
   }

   public String groupSqlNonFinderPerhapsForUpdate() throws CodeGenerationException {
      return this.bean.getUseSelectForUpdate() ? this.groupSqlNonFinderForUpdate() : this.groupSqlNonFinder();
   }

   public String groupSqlNonFinderForUpdate() throws CodeGenerationException {
      try {
         return EjbqlFinder.generateGroupSQLNonFinder(this.bean, this.curGroup.getName(), 1);
      } catch (Exception var2) {
         throw new CodeGenerationException("Internal Error while attempting to generate an Internal Finder for FieldGroup: '" + this.curGroup.getName());
      }
   }

   public String groupSqlNonFinderForUpdateNoWait() throws CodeGenerationException {
      try {
         return EjbqlFinder.generateGroupSQLNonFinder(this.bean, this.curGroup.getName(), 2);
      } catch (Exception var2) {
         throw new CodeGenerationException("Internal Error while attempting to generate an Internal Finder for FieldGroup: '" + this.curGroup.getName());
      }
   }

   public String groupColumnsSql() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.curGroup.getCmpFields().iterator();

      String var4;
      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var4 = RDBMSUtils.escQuotedID(this.bean.getCmpColumnForField(var3));

         assert var4 != null;

         var1.append(var4);
         var1.append(", ");
      }

      Iterator var7 = this.curGroup.getCmrFields().iterator();

      while(true) {
         do {
            if (!var7.hasNext()) {
               if (var1.length() > 1) {
                  var1.setCharAt(var1.length() - 2, ' ');
               }

               return var1.toString();
            }

            var4 = (String)var7.next();
         } while(this.bean.isForeignCmpField(var4));

         Iterator var5 = this.bean.getForeignKeyColNames(var4).iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var1.append(RDBMSUtils.escQuotedID(var6));
            var1.append(", ");
         }
      }
   }

   public int groupColumnCount() {
      int var1 = 0;

      for(Iterator var2 = this.curGroup.getCmpFields().iterator(); var2.hasNext(); ++var1) {
         var2.next();
      }

      Iterator var3 = this.curGroup.getCmrFields().iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               return var1;
            }

            var4 = (String)var3.next();
         } while(this.bean.isForeignCmpField(var4));

         for(Iterator var5 = this.bean.getForeignKeyColNames(var4).iterator(); var5.hasNext(); ++var1) {
            var5.next();
         }
      }
   }

   public String createColumnsQMs() {
      String[] var1 = (String[])((String[])this.cmpFieldNames.toArray(new String[0]));
      int var2 = var1.length;
      if (this.bean.genKeyExcludePKColumn()) {
         --var2;
      }

      StringBuffer var3 = new StringBuffer();

      String var5;
      for(int var4 = 0; var4 < var2; ++var4) {
         var5 = var1[var4];
         var3.append(this.getInsertQuoteStringForField(var5));
         if (var4 < var2 - 1) {
            var3.append(", ");
         }
      }

      Iterator var7 = this.bean.getForeignKeyFieldNames().iterator();

      while(true) {
         do {
            do {
               if (!var7.hasNext()) {
                  return var3.toString();
               }

               var5 = (String)var7.next();
            } while(!this.bean.containsFkField(var5));
         } while(this.bean.isForeignCmpField(var5));

         Iterator var6 = this.bean.getForeignKeyColNames(var5).iterator();
         var3.append(", ");

         while(var6.hasNext()) {
            var3.append("?");
            var6.next();
            if (var6.hasNext()) {
               var3.append(", ");
            }
         }
      }
   }

   public String copyKeyValuesToPkVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      String[] var2 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         String var5 = var2[var3];
         if (ClassUtils.isObjectPrimitive(this.bd.getPrimaryKeyClass())) {
            assert var3 == 0 : "Too many fields for an object primitive PK class";

            this.bean.getCmpFieldClass(var2[var3]);
            var1.append(this.pkVar());
            var1.append(" = ");
            var1.append(this.getCmpField(var2[var3])).append(";");
         } else {
            if (this.bd.hasComplexPrimaryKey()) {
               var1.append(this.pkVar()).append(".").append(var5);
            } else {
               var1.append(this.pkVar());
            }

            var1.append(" = ").append(this.getCmpField(var5)).append(";");
         }

         var1.append(EOL);
      }

      return var1.toString();
   }

   public String copyBeanToComplexFk_forField() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.bean.getTableForCmrField(this.curField);
      Iterator var3 = this.bean.getForeignKeyColNames(this.curField).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5 = this.bean.getRelatedPkFieldName(this.curField, var4);
         var1.append(this.fkVar()).append(".").append(var5);
         var1.append(" = ").append("this").append(".").append(this.bean.variableForField(this.curField, var2, var4)).append(";");
         var1.append(EOL);
      }

      return var1.toString();
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

   public String perhapsImplementWLStoreMethodBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("throw new AssertionError(\"internal error: ejbStore called ");
         var1.append("for bean '");
         var1.append(this.bd.getEJBName());
         var1.append("' which uses ReadOnly concurrency.\");");
         var1.append(EOL);
      } else {
         var1.append(this.parse(this.getProductionRule("implementWLStoreMethodBody")));
      }

      return var1.toString();
   }

   public String wlStoreToTables() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         this.curTableIndex = var2;
         this.curTableName = this.bean.tableAt(this.curTableIndex);
         var1.append(this.parse(this.getProductionRule("implementStoreTable")));
      }

      return var1.toString();
   }

   public String needsStoreOptimisticField() {
      return this.useVersionOrTimestampCheckingForBlobClob(this.curTableName) ? "true" : "false";
   }

   public String iVarIsNotPK() {
      StringBuffer var1 = new StringBuffer();
      var1.append("  if (! (");
      Iterator var2 = this.bean.getIsModifiedIndices_PK().iterator();

      while(var2.hasNext()) {
         Integer var3 = (Integer)var2.next();
         var1.append(this.iVar()).append(" == ").append(var3.toString());
         if (var2.hasNext()) {
            var1.append(" || ");
         }
      }

      var1.append(" ) )  ").append("//  ").append(this.iVar()).append(" does not point to a PK field").append(EOL);
      return var1.toString();
   }

   public String perhapsIVarContinueOnTableCheck() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.iVarIsNotPK());
      var1.append("            if (").append(this.isModifiedToTableMethodName()).append("(").append(this.iVar()).append(") != ");
      var1.append(this.curTableIndex).append(")").append(EOL);
      var1.append("              continue;  // this Field's Column is not in the current Table").append(EOL).append(EOL);
      return var1.toString();
   }

   public String isModifiedIndexToField(int var1) {
      return this.bean.getFieldName(new Integer(var1));
   }

   public String isModifiedToTableMethodName() {
      return "isModifiedToTableIndex";
   }

   public String isModifiedToTableMethod() {
      StringBuffer var1 = new StringBuffer();
      var1.append("int ").append(this.isModifiedToTableMethodName()).append("(int isModifiedIndex) {").append(EOL);
      var1.append("    switch (isModifiedIndex) {").append(EOL);
      Iterator var2 = this.bean.getIsModifiedIndexToTableNumber().iterator();

      for(int var3 = 0; var2.hasNext(); ++var3) {
         Integer var4 = (Integer)var2.next();
         var1.append("     case ").append(Integer.toString(var3)).append(EOL);
         var1.append("       return ").append(var4.toString()).append(";").append(EOL).append(EOL);
      }

      var1.append("      default:").append(EOL);
      var1.append("        throw new RuntimeException(");
      var1.append("\" Internal Error attempt to call " + this.isModifiedToTableMethodName() + ", with isModifiedIndex = \"+").append(EOL);
      var1.append("           \" '\"+isModifiedIndex+\"', this exceeds the expected size limit: '" + this.bean.getFieldNameToIsModifiedIndex().size() + "'. \"); ").append(EOL);
      var1.append("    }").append(EOL);
      var1.append("  }").append(EOL);
      var1.append(EOL);
      return var1.toString();
   }

   public String constructModifiedFieldStoreColumnStrings() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL);
      var1.append("sb.setLength(0);" + EOL);
      List var2 = this.bean.getForeignKeyFieldNames();

      for(int var3 = 0; var3 < this.bean.getFieldCount(); ++var3) {
         if (!this.bean.getIsModifiedIndices_PK().contains(new Integer(var3))) {
            Integer var4 = this.bean.getTableNumber(var3);
            Debug.assertion(var4 != null);
            int var5 = var4;
            if (var5 == this.curTableIndex) {
               String var6 = this.isModifiedIndexToField(var3);
               Debug.assertion(var6 != null);
               StringBuffer var8 = new StringBuffer();
               String var9 = "";
               boolean var10 = false;
               if (this.bean.isCmpFieldName(var6)) {
                  String var7 = this.bean.getColumnForCmpFieldAndTable(var6, this.curTableName);
                  if (this.bean.hasOptimisticColumn(this.curTableName) && this.bean.getOptimisticColumn(this.curTableName).equals(var7)) {
                     var10 = true;
                  } else {
                     if (var2 != null) {
                        String var11 = null;
                        Iterator var12 = var2.iterator();

                        while(var12.hasNext()) {
                           var11 = (String)var12.next();
                           if (this.bean.getForeignKeyColNames(var11).contains(var7)) {
                              var9 = this.perhapsIsFkColsNullableCheck(var11);
                              break;
                           }
                        }
                     }

                     var8.append(var7 + " = ? ");
                  }
               } else {
                  Debug.assertion(this.bean.isForeignKeyField(var6));
                  if (this.bean.containsFkField(var6) && !this.bean.isForeignCmpField(var6)) {
                     var9 = this.perhapsIsFkColsNullableCheck(var6);
                     Iterator var13 = this.bean.getForeignKeyColNames(var6).iterator();

                     while(var13.hasNext()) {
                        String var14 = (String)var13.next();
                        var8.append(var14 + " = ? ");
                        if (var13.hasNext()) {
                           var8.append(", ");
                        }
                     }
                  }
               }

               if (!var10) {
                  var1.append("if (" + this.isModifiedVar() + "[" + var3 + "]" + var9 + ")  {" + EOL);
                  if (!this.bean.isBlobCmpColumnTypeForField(var6) && !this.bean.isClobCmpColumnTypeForField(var6)) {
                     var1.append("if (" + this.countVar() + " > 0) sb.append(\", \");" + EOL);
                     var1.append("sb.append(\"" + var8.toString() + "\");" + EOL);
                     var1.append(this.countVar() + "++;" + EOL);
                  } else {
                     var1.append(this.blobClobCountVar() + "++;" + EOL);
                  }

                  var1.append("}" + EOL + EOL);
               }
            }
         }
      }

      return var1.toString();
   }

   public String implementStoreUtilities() {
      String[] var1 = (String[])((String[])this.cmpFieldNames.toArray(new String[0]));
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append("private void setParam" + var1[var3]);
         var2.append("(PreparedStatement " + this.stmtVar() + ", ");
         var2.append("int " + this.numVar() + ", ");
         var2.append(this.bean.getCmpFieldClass(var1[var3]).getName() + " ");
         var2.append(var1[var3] + ") {" + EOL);
         this.addPreparedStatementBinding(var2, var1[var3], var1[var3], this.numVar(), true, false, false, false);
         var2.append(EOL);
         var2.append("}" + EOL + EOL);
      }

      return var2.toString();
   }

   public String createUpdateFailureMsg() {
      StringBuffer var1 = new StringBuffer("Failed to CREATE Bean.");
      if (this.bean.hasAutoKeyGeneration() && !this.bean.getGenKeyBeforeInsert()) {
         return var1.toString();
      } else {
         var1.append("  Primary Key Value: '\" + ").append(this.pkVar()).append(" + \"'");
         return var1.toString();
      }
   }

   public String createExceptionCheckForDuplicateKey() throws CodeGenerationException {
      return this.bean.hasAutoKeyGeneration() && !this.bean.getGenKeyBeforeInsert() ? "throw se;   // not possible for there to be duplicate key with DBMS Identity Column, skip dup key check." : this.parse(this.getProductionRule("createMethodDuplicateKeyCheck"));
   }

   public String implementLoadIndexedGroupFromRSMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getFieldGroups().iterator();

      while(var2.hasNext()) {
         this.curGroup = (FieldGroup)var2.next();
         var1.append("case " + this.curGroup.getIndex() + ": " + this.loadGroupFromRSMethodName(this.curGroup) + "(rs, offset, " + this.pkVar() + ", (" + this.getGeneratedBeanClassName() + ")eb); break;" + EOL);
      }

      var1.append("default: throw new AssertionError(\"Bad Group index: \"+index);" + EOL);
      return var1.toString();
   }

   public String implementLoadGroupFromRSMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getFieldGroups().iterator();

      while(var2.hasNext()) {
         this.curGroup = (FieldGroup)var2.next();
         var1.append(EOL);
         var1.append("// loadGroup from ResultSet to bean method for the '" + this.curGroup.getName() + "' group." + EOL);
         var1.append("public void " + this.loadGroupFromRSMethodName(this.curGroup) + EOL + "(java.sql.ResultSet " + this.rsVar() + ", " + EOL + "java.lang.Integer " + this.offsetIntObjVar() + ", " + EOL + "Object " + this.pkVar() + "," + EOL + this.getGeneratedBeanInterfaceName() + " beanIntf)" + EOL);
         var1.append("throws java.sql.SQLException, java.lang.Exception" + EOL);
         var1.append("{" + EOL);
         var1.append(this.getGeneratedBeanClassName());
         var1.append(" ");
         var1.append(this.beanVar());
         var1.append(" = (");
         var1.append(this.getGeneratedBeanClassName());
         var1.append(")beanIntf;");
         var1.append(EOL);
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append(this.debugSay() + "(\"" + this.loadGroupFromRSMethodName(this.curGroup) + "\");" + EOL);
         var1.append("}" + EOL + EOL);
         var1.append(this.assignOffsetVar() + EOL);
         var1.append(this.assignGroupColumnsToBean() + EOL);
         var1.append(this.beanIsLoadedVar() + " = true;" + EOL);
         var1.append(this.perhapsSetTableLoadedVarsForGroup());
         var1.append("}" + EOL);
      }

      return var1.toString();
   }

   public String implementLoadCMRFieldFromRSMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getAllCmrFields().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append("if (\"" + var3 + "\".equalsIgnoreCase(cmrField)) " + this.loadCMRFieldFromRSMethodName(var3) + "(rs, offset,(" + this.getGeneratedBeanClassName() + ")eb);" + EOL);
      }

      return var1.toString();
   }

   public String implementLoadCMRFieldFromRSMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      for(Iterator var2 = this.bean.getAllCmrFields().iterator(); var2.hasNext(); var1.append("}" + EOL)) {
         String var3 = (String)var2.next();
         var1.append(EOL);
         var1.append("public void " + this.loadCMRFieldFromRSMethodName(var3) + EOL + "(java.sql.ResultSet " + this.rsVar() + ", " + EOL + "java.lang.Integer " + this.offsetIntObjVar() + ", " + EOL + this.getGeneratedBeanInterfaceName() + " beanIntf) " + EOL);
         var1.append("throws java.sql.SQLException, java.lang.Exception" + EOL);
         var1.append("{" + EOL);
         var1.append(this.getGeneratedBeanClassName());
         var1.append(" ");
         var1.append(this.beanVar());
         var1.append(" = (");
         var1.append(this.getGeneratedBeanClassName());
         var1.append(")beanIntf;");
         var1.append(EOL);
         var1.append("if (" + this.debugEnabled() + ") {" + EOL);
         var1.append(this.debugSay() + "(\"" + this.loadCMRFieldFromRSMethodName(var3) + "\");" + EOL);
         var1.append("}" + EOL + EOL);
         var1.append(this.assignOffsetVar() + EOL);
         RDBMSBean var4 = this.bean.getRelatedRDBMSBean(var3);
         String var5 = this.bean.getRelatedFieldName(var3);
         if (this.lhsBeanHasFKForLocal11or1NPath(var4, var5)) {
            var1.append(this.assignCMRFieldPKColumns(this.beanVar()) + EOL);
         } else {
            var1.append(this.assignCMRFieldFKColumns(this.beanVar(), var3) + EOL);
         }
      }

      return var1.toString();
   }

   public String assignRSToPkVar() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      ArrayList var2 = new ArrayList(this.bd.getPrimaryKeyFieldNames());
      if (this.bd.hasComplexPrimaryKey()) {
         this.assignToVars(var1, this.pkVar(), true, var2, 1, false);
      } else {
         this.assignToVars(var1, this.pkVar(), false, var2, 1, false);
      }

      return var1.toString();
   }

   public String genIsPKNull() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.hasComplexPrimaryKey()) {
         Iterator var2 = this.bd.getPrimaryKeyFieldNames().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Class var4 = this.getVariableClass(var3);
            if (!var4.isPrimitive()) {
               var1.append("if (" + this.pkVar() + "." + var3 + " == null) return null;" + EOL);
            }
         }
      }

      return var1.toString();
   }

   public String invokeEagerCachingMethods() {
      StringBuffer var1 = new StringBuffer();
      var1.append("int increment = " + this.getGroupColumnCount(this.bean, this.curGroup.getName()) + ";" + EOL);
      Iterator var2 = this.curRelationshipCaching.getCachingElements().iterator();
      this.invokeEagerCachingMethodsForCachingElements(this.bean, var2, (String)null, var1, false);
      return var1.toString();
   }

   public void invokeEagerCachingMethodsForCachingElements(RDBMSBean var1, Iterator var2, String var3, StringBuffer var4, boolean var5) {
      while(var2.hasNext()) {
         RelationshipCaching.CachingElement var6 = (RelationshipCaching.CachingElement)var2.next();
         String var7 = var6.getCmrField();
         String var8 = var6.getGroupName();
         this.setCurrCachingElementCmrField(var7);
         RDBMSBean var9 = var1.getRelatedRDBMSBean(var7);
         String var10 = this.bmVar((String)this.declaredManagerVars.get(var9.getEjbName()));
         CMPBeanDescriptor var11 = (CMPBeanDescriptor)this.beanMap.get(var9.getEjbName());
         String var12 = var11.getGeneratedBeanInterfaceName();
         var4.append("// load " + var7 + " bean from RS" + EOL);
         var4.append(this.offsetIntObjVar() + " = new Integer(increment" + ");" + EOL);
         var4.append("Object " + this.cmrFieldPKVar(var3) + " = " + this.cmrFieldBeanVar() + "_pooledBean." + this.getPKFromRSMethodName() + "Instance" + this.getPKFromRSMethodParams() + EOL);
         var4.append(var12 + " " + this.cmrFieldBeanVar() + " = null;" + EOL);
         var4.append(this.eoRCVar() + " = null;" + EOL);
         var4.append("if (" + this.cmrFieldPKVar(var3) + " != null) {" + EOL);
         var4.append("if (!" + this.cmrFieldPKMapVar(var3) + ".containsKey(" + this.cmrFieldPKVar(var3) + ")) {" + EOL);
         String var13 = var1.getRelatedFieldName(var7);
         int var14 = this.getGroupColumnCount(var9, var8);
         var4.append("RSInfo " + this.rsInfoVar() + " = new RSInfoImpl(" + this.rsVar() + ", " + this.loadGroupFromRSIndex(var9, var8) + ", " + "increment, \"" + var13 + "\", " + "increment + " + var14 + ", " + this.cmrFieldPKVar(var3) + ");" + EOL);
         var4.append(this.cmrFieldBeanVar() + " = (" + var12 + ") " + var10 + ".getBeanFromRS(" + this.cmrFieldPKVar(var3) + ", " + this.rsInfoVar() + ");" + EOL);
         if (debugLogger.isDebugEnabled()) {
            debug(var1.getEjbName() + " is relationship caching " + var9.getEjbName() + " through cmrField " + var7 + ", " + var9.getEjbName() + " has cmrField " + var13);
         }

         int var15;
         if (this.lhsBeanHasFKForLocal11or1NPath(var1, var7)) {
            var15 = var14 + var1.getForeignKeyColNames(var7).size();
         } else {
            var15 = var14 + var9.getForeignKeyColNames(var13).size();
         }

         var4.append(this.cmrFieldPKMapVar(var3) + ".put(" + this.cmrFieldPKVar(var3) + ", " + this.cmrFieldBeanVar() + ");" + EOL);
         var4.append(this.eoRCVar() + " = " + var10 + ".finderGetEoFromBeanOrPk((javax.ejb.EntityBean)" + this.cmrFieldBeanVar() + ", " + this.cmrFieldPKVar(var3) + ", true);" + EOL);
         String var16 = "";
         if (var3 != null) {
            int var17 = var3.lastIndexOf("_");
            if (var17 == -1) {
               var16 = var3;
            } else {
               var16 = var3.substring(var17 + 1);
            }
         }

         var4.append("// set *_isLoaded_ flag even when " + this.eoRCVar() + " is null" + EOL);
         var4.append("if (" + this.cmrFieldBeanVar(var16) + " != null) {" + EOL);
         var4.append(this.cmrFieldBeanVar(var16) + "." + CodeGenUtils.cacheRelationshipMethodName(var7) + "(" + this.eoRCVar() + ");" + EOL);
         String var21;
         if (var5) {
            if (!var1.isQueryCachingEnabledForCMRField(var7)) {
               var4.append("if (qcHandler.isQueryCachingEnabledForFinder()) {");
               var4.append(EOL);
            }

            var21 = this.generateCMRFieldFinderMethodName(var7);
            var4.append("TTLManager roMgr = (TTLManager)");
            var4.append(var10).append(";").append(EOL);
            Class var18 = var1.getCmrFieldClass(var7);
            var4.append("QueryCacheKey qckey = new QueryCacheKey(\"");
            var4.append(var21).append("\", new Object[]{");
            var4.append("__WL_getPrimaryKey()").append("}, roMgr, ");
            if (Set.class.isAssignableFrom(var18)) {
               var4.append("QueryCacheKey.RET_TYPE_SET);").append(EOL);
            } else {
               var4.append("QueryCacheKey.RET_TYPE_SINGLETON);").append(EOL);
            }

            var4.append("QueryCacheElement qce = new QueryCacheElement(");
            var4.append("((CMPBean)").append(this.cmrFieldBeanVar());
            var4.append(").__WL_getPrimaryKey(), roMgr);").append(EOL);
            var4.append("qcHandler.addQueryCachingEntry(roMgr, qckey, qce);");
            var4.append(EOL);
            if (!var1.isQueryCachingEnabledForCMRField(var7)) {
               var4.append("}").append(EOL);
            }
         } else if (var1.isQueryCachingEnabledForCMRField(var7) || this.shouldImplementQueryCaching(this.curFinder)) {
            this.currFinderLoadsQueryCachingEnabledCMRFields = true;
            var4.append("if (").append(this.beanMapVar()).append(" == null) {");
            var4.append(EOL);
            var4.append(this.beanMapVar()).append(" = new MultiMap();");
            var4.append(EOL);
            var4.append("}").append(EOL);
            var4.append("if (").append(this.beanMapVar()).append(".get(");
            var4.append(this.cmrFieldBeanVar(var16)).append(", \"");
            var4.append(var7).append("\") == null) {");
            var4.append(EOL);
            var4.append(this.beanMapVar()).append(".put(");
            var4.append(this.cmrFieldBeanVar(var16)).append(", \"");
            var4.append(var7).append("\");").append(EOL);
            var4.append("}").append(EOL);
         }

         var21 = null;
         if (var3 != null) {
            var21 = this.bmVar((String)this.declaredManagerVars.get(var1.getEjbName()));
            var4.append(this.eoRCVar() + " = " + var21 + ".finderGetEoFromBeanOrPk((javax.ejb.EntityBean)" + this.cmrFieldBeanVar(var16) + ", null, true);" + EOL);
         } else {
            var4.append(this.eoRCVar() + " = " + this.eoVar() + ";" + EOL);
         }

         var4.append("} else {" + EOL);
         var4.append(this.eoRCVar() + " = null;" + EOL);
         var4.append("}" + EOL);
         var4.append("if (" + this.cmrFieldBeanVar() + " != null) {" + EOL);
         String var19;
         if (var5) {
            if (!var1.relatedFieldIsFkOwner(var7) || var1.isManyToManyRelation(var7)) {
               if (!var9.isQueryCachingEnabledForCMRField(var13)) {
                  var4.append("if (qcHandler.isQueryCachingEnabledForFinder()) {");
                  var4.append(EOL);
               }

               String var22 = CodeGenUtils.fieldVarName(var13);
               var19 = this.generateCMRFieldFinderMethodName(var13);
               var4.append("TTLManager roMgr = (TTLManager)");
               if (var3 != null) {
                  var4.append(var21).append(";").append(EOL);
               } else {
                  var4.append(this.pmVar()).append(".getBeanManager();").append(EOL);
               }

               Class var20 = var9.getCmrFieldClass(var13);
               var4.append("QueryCacheKey qckey = new QueryCacheKey(\"");
               var4.append(var19).append("\", new Object[]{");
               var4.append("((CMPBean)").append(this.cmrFieldBeanVar());
               var4.append(").__WL_getPrimaryKey()").append("}, roMgr, ");
               if (Set.class.isAssignableFrom(var20)) {
                  var4.append("QueryCacheKey.RET_TYPE_SET);").append(EOL);
               } else {
                  var4.append("QueryCacheKey.RET_TYPE_SINGLETON);").append(EOL);
               }

               var4.append("QueryCacheElement qce = new QueryCacheElement(");
               if (var3 != null) {
                  var4.append(this.cmrFieldBeanVar(var16));
                  var4.append(".__WL_getPrimaryKey(), roMgr);").append(EOL);
               } else {
                  var4.append("__WL_getPrimaryKey(), roMgr);").append(EOL);
               }

               var4.append("qcHandler.addQueryCachingEntry(roMgr, qckey, qce);");
               var4.append(EOL);
               if (!var9.isQueryCachingEnabledForCMRField(var13)) {
                  var4.append("}").append(EOL);
               }
            }
         } else if (this.shouldImplementQueryCaching(this.curFinder) && (!var1.relatedFieldIsFkOwner(var7) || var1.isManyToManyRelation(var7)) || var9.isQueryCachingEnabledForCMRField(var13)) {
            this.currFinderLoadsQueryCachingEnabledCMRFields = true;
            var4.append("if (").append(this.beanMapVar()).append(" == null) {");
            var4.append(EOL);
            var4.append(this.beanMapVar()).append(" = new MultiMap();");
            var4.append(EOL);
            var4.append("}").append(EOL);
            var4.append("if (").append(this.beanMapVar()).append(".get(");
            var4.append(this.cmrFieldBeanVar()).append(", \"");
            var4.append(var13).append("\") == null) {");
            var4.append(EOL);
            var4.append(this.beanMapVar()).append(".put(");
            var4.append(this.cmrFieldBeanVar()).append(", \"");
            var4.append(var13).append("\");").append(EOL);
            var4.append("}").append(EOL);
         }

         var4.append(this.cmrFieldBeanVar() + "." + CodeGenUtils.cacheRelationshipMethodName(var13) + "(" + this.eoRCVar() + ");" + EOL);
         var4.append("}" + EOL);
         var4.append("} else {" + EOL);
         var4.append(this.cmrFieldBeanVar() + " = (" + var12 + ") " + this.cmrFieldPKMapVar(var3) + ".get(" + this.cmrFieldPKVar(var3) + ");" + EOL);
         var4.append("}" + EOL);
         var4.append("}" + EOL + EOL);
         var4.append("increment = increment + " + var15 + ";" + EOL);
         Iterator var23 = var6.getCachingElements().iterator();
         if (var23.hasNext()) {
            var19 = null;
            if (var3 == null) {
               var19 = var7;
            } else {
               var19 = var3 + "_" + var7;
            }

            this.invokeEagerCachingMethodsForCachingElements(var9, var23, var19, var4, var5);
         }
      }

   }

   public String perhapsPostFinderCleanupForEagerCaching() {
      if (!(this.curFinder instanceof EjbqlFinder)) {
         return "";
      } else {
         EjbqlFinder var1 = (EjbqlFinder)this.curFinder;
         String var2 = var1.getCachingName();
         if (var2 == null) {
            return "";
         } else {
            List var3 = this.curRelationshipCaching.getCachingElements();
            if (var3.size() <= 0) {
               return "";
            } else {
               Iterator var4 = var3.iterator();
               StringBuffer var5 = new StringBuffer();
               var5.append("// After all related beans have been loaded into cache").append(EOL);
               var5.append("// we run postFinderCleanup on the related beans here.").append(EOL);
               var5.append("// postFinderCleanup on the primary beans is done in the beanManager.");
               var5.append(EOL).append(EOL);
               this.postFinderCleanupForEagerCaching(this.bean, var4, (String)null, var5);
               return var5.toString();
            }
         }
      }
   }

   private void postFinderCleanupForEagerCaching(RDBMSBean var1, Iterator var2, String var3, StringBuffer var4) {
      if (var2.hasNext()) {
         while(var2.hasNext()) {
            RelationshipCaching.CachingElement var5 = (RelationshipCaching.CachingElement)var2.next();
            String var6 = var5.getCmrField();
            String var7 = var5.getGroupName();
            this.setCurrCachingElementCmrField(var6);
            RDBMSBean var8 = var1.getRelatedRDBMSBean(var6);
            CMPBeanDescriptor var9 = var1.getRelatedDescriptor(var6);
            String var10 = this.bmVar((String)this.declaredManagerVars.get(var8.getEjbName()));
            boolean var11 = var9.hasLocalClientView();
            var4.append(var10).append(".postFinderCleanup(");
            var4.append("null,  // Object argument").append(EOL);
            var4.append(this.cmrFieldPKMapVar(var3)).append(".keySet(),  // Collection of the pks").append(EOL);
            var4.append("true,  // related cached pks").append(EOL);
            var4.append(var11).append("  // isLocal ?").append(EOL);
            var4.append(");").append(EOL).append(EOL);
            Iterator var12 = var5.getCachingElements().iterator();
            if (var12.hasNext()) {
               String var13 = null;
               if (var3 == null) {
                  var13 = var6;
               } else {
                  var13 = var3 + "_" + var6;
               }

               this.postFinderCleanupForEagerCaching(var8, var12, var13, var4);
            }
         }

         var4.append(EOL);
      }
   }

   public String perhapsDeclareRelationshipCachingPooledBeanVar() {
      return this.perhapsRelationshipCachingPooledBeanVar(1);
   }

   public String perhapsAllocateRelationshipCachingPooledBeanVar() {
      return this.perhapsRelationshipCachingPooledBeanVar(2);
   }

   public String perhapsRemoveRelationshipCachingPooledBeanVar() {
      return this.perhapsRelationshipCachingPooledBeanVar(3);
   }

   public String perhapsRelationshipCachingPooledBeanVar(int var1) {
      boolean var2 = false;
      if (this.curFinder instanceof EjbqlFinder) {
         EjbqlFinder var3 = (EjbqlFinder)this.curFinder;
         String var4 = var3.getCachingName();
         if (var4 != null) {
            this.curRelationshipCaching = this.bean.getRelationshipCaching(var4);
            if (this.curRelationshipCaching != null) {
               var2 = this.curRelationshipCaching.getCachingElements().iterator().hasNext();
            }
         }
      }

      if (!var2) {
         return "";
      } else {
         StringBuffer var5 = new StringBuffer();
         Iterator var6 = this.curRelationshipCaching.getCachingElements().iterator();
         this.perhapsRelationshipCachingPooledBeanVar(this.bean, var6, (String)null, var1, var5);
         return var5.toString();
      }
   }

   public void perhapsRelationshipCachingPooledBeanVar(RDBMSBean var1, Iterator var2, String var3, int var4, StringBuffer var5) {
      while(var2.hasNext()) {
         RelationshipCaching.CachingElement var6 = (RelationshipCaching.CachingElement)var2.next();
         String var7 = var6.getCmrField();
         this.setCurrCachingElementCmrField(var7);
         RDBMSBean var8 = var1.getRelatedRDBMSBean(var7);
         String var9 = this.bmVar((String)this.declaredManagerVars.get(var8.getEjbName()));
         CMPBeanDescriptor var10 = (CMPBeanDescriptor)this.beanMap.get(var8.getEjbName());
         String var11 = var10.getGeneratedBeanInterfaceName();
         if (var4 == 1) {
            var5.append(var11 + " " + this.cmrFieldBeanVar() + "_pooledBean = null;" + EOL);
         } else if (var4 == 2) {
            var5.append(this.cmrFieldBeanVar() + "_pooledBean = (" + var11 + ") " + var9 + ".getBeanFromPool();" + EOL);
         } else if (var4 == 3) {
            var5.append("if (" + this.cmrFieldBeanVar() + "_pooledBean!=null) " + "((weblogic.ejb.container.manager.BaseEntityManager)" + var9 + ").releaseBeanToPool(((javax.ejb.EntityBean)" + this.cmrFieldBeanVar() + "_pooledBean));" + EOL);
         }

         Iterator var12 = var6.getCachingElements().iterator();
         if (var12.hasNext()) {
            String var13 = null;
            if (var3 == null) {
               var13 = var7;
            } else {
               var13 = var3 + "_" + var7;
            }

            this.perhapsRelationshipCachingPooledBeanVar(var8, var12, var13, var4, var5);
         }
      }

   }

   public String declarePKMapVar() {
      StringBuffer var1 = new StringBuffer();
      var1.append(EOL + "Map " + this.pkMapVar() + " = new HashMap();" + EOL);
      String var2 = null;
      if (this.curFinder instanceof EjbqlFinder) {
         EjbqlFinder var3 = (EjbqlFinder)this.curFinder;
         var2 = var3.getCachingName();
      }

      if (var2 == null) {
         return var1.toString();
      } else {
         RelationshipCaching var5 = this.bean.getRelationshipCaching(var2);
         if (var5 == null) {
            return var1.toString();
         } else {
            Iterator var4 = var5.getCachingElements().iterator();
            var1.append(this.declarePKMapVarForCachingElements(this.bean, var4, (String)null));
            return var1.toString();
         }
      }
   }

   public String declarePKMapVarForCachingElements(RDBMSBean var1, Iterator var2, String var3) {
      StringBuffer var4 = new StringBuffer();

      while(var2.hasNext()) {
         RelationshipCaching.CachingElement var5 = (RelationshipCaching.CachingElement)var2.next();
         String var6 = var5.getCmrField();
         String var7 = var5.getGroupName();
         this.setCurrCachingElementCmrField(var6);
         RDBMSBean var8 = var1.getRelatedRDBMSBean(var6);
         var4.append("Map " + this.cmrFieldPKMapVar(var3) + " = new HashMap();" + EOL);
         Iterator var9 = var5.getCachingElements().iterator();
         if (var9.hasNext()) {
            String var10 = null;
            if (var3 == null) {
               var10 = var6;
            } else {
               var10 = var3 + "_" + var6;
            }

            var4.append(this.declarePKMapVarForCachingElements(var8, var9, var10));
         }
      }

      return var4.toString();
   }

   private void setCurrCachingElementCmrField(String var1) {
      this.currCachingElementCmrField = var1;
   }

   private String cmrFieldBeanVar() {
      return this.beanVar() + "_" + this.currCachingElementCmrField;
   }

   private String cmrFieldBeanVar(String var1) {
      return "".equals(var1) ? this.beanVar() : this.beanVar() + "_" + var1;
   }

   private String cmrFieldPKVar(String var1) {
      return var1 == null ? this.pkVar() + "_" + this.currCachingElementCmrField : this.pkVar() + "_" + var1 + "_" + this.currCachingElementCmrField;
   }

   private String cmrFieldPKMapVar(String var1) {
      return var1 == null ? this.pkMapVar() + "_" + this.currCachingElementCmrField : this.pkMapVar() + "_" + var1 + "_" + this.currCachingElementCmrField;
   }

   private String cmrFieldClassLoaderVar() {
      return this.classLoaderVar() + "_" + this.currCachingElementCmrField;
   }

   public String assignOffsetIntObjVar(int var1) {
      return this.offsetIntObjVar() + " = new Integer(" + var1 + ");" + EOL;
   }

   public String assignOffsetVar() {
      return "int " + this.offsetVar() + " = " + this.offsetIntObjVar() + ".intValue();" + EOL;
   }

   public String getPKFromRSMethodName() {
      return this.varPrefix() + "getPKFromRS";
   }

   private String getPKFromRSMethodParams() {
      return "(" + this.rsVar() + ", " + this.offsetIntObjVar() + ", " + this.classLoaderVar() + ");";
   }

   private int loadGroupFromRSIndex(RDBMSBean var1, String var2) {
      return var1.getFieldGroup(var2).getIndex();
   }

   private String loadGroupFromRSMethodName(RDBMSBean var1, String var2) {
      return this.loadGroupFromRSMethodName(var1.getFieldGroup(var2));
   }

   private String loadGroupFromRSMethodName(FieldGroup var1) {
      return this.loadMethodName(this.getFieldGroupSuffix(var1) + "FromRS");
   }

   private String loadCMRFieldFromRSMethodName(String var1) {
      return this.varPrefix() + "loadCMRFieldFromRS_" + var1;
   }

   private String loadFromRSMethodParams() {
      return "(" + this.rsVar() + ", " + this.offsetIntObjVar() + ", " + this.cmrFieldBeanVar() + ");";
   }

   private boolean lhsBeanHasFKForLocal11or1NPath(RDBMSBean var1, String var2) {
      return var1.isForeignKeyField(var2);
   }

   private String oneToManyAddMethod() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append("public void " + CodeGenUtils.cacheRelationshipMethodName(this.curField) + "(Object " + this.curField + ") {" + EOL);
      if (this.bean.getRelatedMultiplicity(this.curField).equals("One")) {
         String var2 = ClassUtils.classToJavaSourceType(this.bean.getCmrFieldClass(this.curField));
         var1.append("if (" + this.curField + " != null) {" + EOL);
         var1.append(CodeGenUtils.fieldVarName(this.curField) + " = (" + var2 + ") " + this.curField + ";" + EOL);
         var1.append("}" + EOL);
      } else {
         var1.append("if (");
         var1.append(CodeGenUtils.fieldVarName(this.curField));
         var1.append(" == null) {" + EOL);
         var1.append(CodeGenUtils.fieldVarName(this.curField) + " = " + this.allocateOneToManySet() + ";" + EOL);
         var1.append("}" + EOL);
         if (this.bean.isReadOnly() || this.bean.getCacheBetweenTransactions()) {
            var1.append("else {").append(EOL);
            var1.append("Transaction currentTx = TransactionHelper");
            var1.append(".getTransactionHelper().getTransaction();").append(EOL);
            var1.append("if (currentTx == null || !((RDBMSSet)");
            var1.append(CodeGenUtils.fieldVarName(this.curField));
            var1.append(").checkIfCurrentTxEqualsCreateTx(currentTx)) {");
            var1.append(EOL);
            var1.append(CodeGenUtils.fieldVarName(this.curField)).append(" = ");
            var1.append(this.allocateOneToManySet()).append(";").append(EOL);
            var1.append("}").append(EOL);
            var1.append("}").append(EOL);
         }

         var1.append("((RDBMSSet)");
         var1.append(CodeGenUtils.fieldVarName(this.curField));
         var1.append(").doAddToCache(" + this.curField + ");" + EOL);
      }

      var1.append(this.isCmrLoadedVarName(this.curField) + " = true;" + EOL);
      var1.append("}" + EOL + EOL);
      return var1.toString();
   }

   public String perhapsGenKeyBeforeInsert() throws CodeGenerationException {
      if (this.bean.hasAutoKeyGeneration() && this.bean.getGenKeyBeforeInsert()) {
         StringBuffer var1 = new StringBuffer();
         var1.append(EOL);
         var1.append(this.genKeyQuery()).append(EOL);
         var1.append(this.genKeyCallSetPK()).append(EOL);
         return var1.toString();
      } else {
         return "";
      }
   }

   public String perhapsDeclarePkCheckMethod() throws CodeGenerationException {
      return !this.bean.hasAutoKeyGeneration() ? this.parse(this.getProductionRule("declarePkCheckMethod")) : "";
   }

   public String implementPkCheckMethodBody() {
      StringBuffer var1 = new StringBuffer();
      if (!this.bd.isReadOnly() || this.bean.allowReadonlyCreateAndRemove()) {
         Iterator var2 = this.pkFieldNames.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append("// check that '" + var3 + "' was set" + EOL);
            var1.append("if (!" + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var3) + "]) {" + EOL);
            var1.append("Loggable l = EJBLogger.logpkNotSetLoggable(\"" + this.bean.getEjbName() + "\",\"" + var3 + "\");");
            var1.append("throw new javax.ejb.CreateException(l.getMessage());" + EOL);
            var1.append("}");
            if (var2.hasNext()) {
               var1.append(EOL);
            }
         }
      }

      return var1.toString();
   }

   public String perhapsImplementCreateMethodBody() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bd.isReadOnly() && !this.bean.allowReadonlyCreateAndRemove()) {
         var1.append("Loggable l = EJBLogger.logCannotCreateReadOnlyBeanLoggable(\"" + this.bean.getEjbName() + "\");" + EOL);
         var1.append("throw new javax.ejb.CreateException(l.getMessage());" + EOL);
      } else {
         var1.append(this.parse(this.getProductionRule("implementCreateMethodBody")));
      }

      return var1.toString();
   }

   public String perhapsCallPkCheck() {
      return !this.bean.hasAutoKeyGeneration() ? EOL + "pkCheck();" + EOL : "";
   }

   public String genKeyQuery() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      short var2 = this.bean.getGenKeyType();
      String var3 = this.genKeyGetPKClassName();
      var1.append(var3).append(" ").append(this.genKeyVar());
      if (var2 == 2) {
         var1.append(" = (").append(var3).append(") __WL_pm.getNextSequenceKey();");
      } else {
         if (var2 != 3) {
            throw new AssertionError("Unknown prefetch auto-key generator for " + this.bd.getEJBName());
         }

         var1.append(" = (").append(var3).append(") __WL_pm.getNextSequenceTableKey();");
      }

      var1.append(EOL);
      return var1.toString();
   }

   private String genKeyCallSetPK() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = (String)this.bd.getPrimaryKeyFieldNames().iterator().next();
      Class var3 = this.bd.getFieldClass(var2);
      String var4 = this.genKeyVar();
      if (var3.equals(Integer.TYPE)) {
         var4 = var4 + ".intValue()";
      } else if (var3.equals(Long.TYPE)) {
         var4 = var4 + ".longValue()";
      }

      var1.append(RDBMSUtils.setterMethodName(this.bean.getGenKeyPKField()));
      var1.append("(").append(var4).append(");").append(EOL);
      return var1.toString();
   }

   private String genKeyGetPKClassName() throws CodeGenerationException {
      switch (this.bean.getGenKeyPKFieldClassType()) {
         case 0:
            return "java.lang.Integer";
         case 1:
            return "java.lang.Long";
         default:
            throw new CodeGenerationException(" Internal Error, unknown genKeyPKFieldClassType: " + this.bean.getGenKeyPKFieldClassType());
      }
   }

   private String attrsAsColumnsAsParamsForTable(String var1, String[] var2, String var3) {
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;

      for(int var6 = var2.length; var5 < var6; ++var5) {
         String var7 = RDBMSUtils.escQuotedID(this.bean.getColumnForCmpFieldAndTable(var2[var5], var1));
         var4.append(var7).append(" = ?");
         if (var5 < var6 - 1) {
            var4.append(var3);
         }
      }

      return var4.toString();
   }

   private String attrsAsColumnsAsParams(String[] var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      for(int var5 = var1.length; var4 < var5; ++var4) {
         String var6 = RDBMSUtils.escQuotedID(this.bean.getCmpColumnForField(var1[var4]));
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

   public String getTableCount() {
      return Integer.toString(this.bean.tableCount());
   }

   public String setCreateQueryArray() throws CodeGenerationException {
      return this.setCreateQueryArray(false);
   }

   public String setCreateQueryArrayWoFkColumns() throws CodeGenerationException {
      return this.setCreateQueryArray(true);
   }

   public String setCreateQueryArray(boolean var1) throws CodeGenerationException {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < this.bean.tableCount(); ++var3) {
         this.curTableName = this.bean.tableAt(var3);
         StringBuffer var4 = new StringBuffer();
         String var5 = this.getCreateQueryColumnSQLForTable(var3, var4, var1);
         var2.append(this.queryArrayElement(var3)).append(" = \"INSERT INTO ");
         var2.append(this.curTableName).append(" (");
         var2.append(var5);
         var2.append(") VALUES (");
         var2.append(var4);
         if (this.bean.getGenKeyType() != 1 || this.bean.getDatabaseType() != 2 && this.bean.getDatabaseType() != 7) {
            var2.append(")\";").append(EOL);
         } else {
            var2.append(") " + this.bean.getGenKeyGeneratorQuery() + "\";").append(EOL);
         }
      }

      return var2.toString();
   }

   public String executeUpdateOrExecuteQuery() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      short var2 = this.bean.getGenKeyType();
      Class var3 = this.bd.getPrimaryKeyClass();
      if (var2 == 1) {
         var1.append(this.genKeyGetPKClassName()).append(" " + this.genKeyVar() + " = null;");
         var1.append(EOL);
      }

      for(int var4 = 0; var4 < this.bean.tableCount(); ++var4) {
         if (var2 != 1 || this.bean.getDatabaseType() != 2 && this.bean.getDatabaseType() != 7) {
            var1.append("if (" + this.stmtArrayElement(var4)).append(".executeUpdate() != 1)").append(EOL);
            var1.append("throw new java.lang.Exception(\"" + this.createUpdateFailureMsg() + "\");").append(EOL);
            if (var2 == 1) {
               var1.append(this.rsVar() + " = " + this.stmtArrayElement(var4));
               var1.append(".getGeneratedKeys();").append(EOL);
            }
         } else {
            var1.append(this.rsVar() + " = " + this.stmtArrayElement(var4)).append(".executeQuery();").append(EOL);
         }

         if (var2 == 1) {
            var1.append("if (" + this.rsVar() + ".next()) {").append(EOL);
            var1.append(this.genKeyVar());
            var1.append(" = new ").append(this.genKeyGetPKClassName() + "(" + this.rsVar() + ".get");
            if (this.genKeyGetPKClassName().equals("java.lang.Integer")) {
               var1.append("Int(1)");
            } else if (this.genKeyGetPKClassName().equals("java.lang.Long")) {
               var1.append("Long(1)");
            }

            var1.append(");").append(EOL);
            var1.append("this.").append(this.bean.getGenKeyPKField()).append(" = ");
            var1.append(this.genKeyVar()).append(";").append(EOL);
            var1.append(this.pkVar());
            var1.append(" = (").append(ClassUtils.classToJavaSourceType(var3)).append(") __WL_getPrimaryKey();").append(EOL);
            var1.append("} else {").append(EOL);
            var1.append("Loggable l = EJBLogger.logNoGeneratedPKReturnedLoggable();").append(EOL);
            var1.append("throw new javax.ejb.CreateException(l.getMessage());" + EOL);
            var1.append("}").append(EOL);
            var1.append("if (" + this.rsVar() + ".next()) {").append(EOL);
            var1.append("Loggable l = EJBLogger.logMultiplGeneratedKeysReturnedLoggable();").append(EOL);
            var1.append("throw new javax.ejb.CreateException(l.getMessage());" + EOL);
            var1.append("}").append(EOL);
         }
      }

      return var1.toString();
   }

   public String setUpdateQueryArray() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         this.curTableIndex = var2;
         this.curTableName = this.bean.tableAt(var2);
         var1.append(this.perhapsResetBlobClobCountVar()).append(EOL);
         var1.append(this.countVar() + " = 0;");
         var1.append(this.constructModifiedFieldStoreColumnStrings());
         if (this.useVersionOrTimestampCheckingForBlobClob(this.curTableName)) {
            var1.append("if ( (").append(this.countVar()).append(" != 0) || ");
            var1.append("(").append(this.blobClobCountVarOrZero()).append(" > 0) ) {").append(EOL);
         } else {
            var1.append("if (").append(this.countVar()).append(" != 0) {").append(EOL);
         }

         var1.append(this.perhapsSetUpdateOptimisticFieldStringForBatch());
         var1.append(this.perhapsConstructSnapshotPredicate());
         var1.append("if (sb.length() != 0) {").append(EOL);
         var1.append(this.queryArrayElement(var2)).append(" = \"UPDATE ");
         var1.append(this.curTableName);
         var1.append(" SET \" + ");
         var1.append("sb.toString() + ");
         var1.append("\" WHERE " + this.idParamsSqlForCurTable() + "\"");
         var1.append(this.perhapsAddSnapshotPredicate() + ";").append(EOL);
         var1.append("}").append(EOL);
         var1.append("}").append(EOL);
      }

      return var1.toString();
   }

   public String setBeanParamsForUpdateArray() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.declareByteArrayVars());

      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         this.curTableIndex = var2;
         this.curTableName = this.bean.tableAt(var2);
         var1.append(this.parse(this.getProductionRule("implementSetBeanParamsForUpdateArrayMethodBody")));
      }

      return var1.toString();
   }

   private boolean appendComma(boolean var1, StringBuffer var2, StringBuffer var3) {
      if (var1) {
         var2.append(", ");
         var3.append(", ");
      } else {
         var1 = true;
      }

      return var1;
   }

   private String getCreateQueryColumnSQLForTable(int var1, StringBuffer var2, boolean var3) {
      StringBuffer var4 = new StringBuffer();
      this.curTableIndex = var1;
      this.curTableName = this.bean.tableAt(var1);
      ArrayList var5 = new ArrayList(this.bean.getFields(var1));
      String var6 = null;
      String var7 = null;
      if (this.bean.hasAutoKeyGeneration()) {
         var6 = this.bean.getGenKeyPKField();
         var7 = this.bean.getGenKeyDefaultColumnVal();
      }

      if (this.bean.hasAutoKeyGeneration() && this.bean.genKeyExcludePKColumn()) {
         assert var5.contains(var6);

         var5.remove(var6);
      }

      if (this.bean.getTriggerUpdatesOptimisticColumn(this.curTableName)) {
         String var8 = this.bean.getOptimisticColumn(this.curTableName);
         String var9 = this.bean.getCmpField(this.curTableName, var8);

         assert var5.contains(var9);

         var5.remove(var9);
      }

      boolean var13 = false;
      Iterator var14 = var5.iterator();

      while(true) {
         while(true) {
            String var10;
            Iterator var11;
            String var12;
            while(var14.hasNext()) {
               var10 = (String)var14.next();
               if (this.bean.isCmpFieldName(var10)) {
                  if (var10.equals(var6) && var7 != null) {
                     var13 = this.appendComma(var13, var4, var2);
                     var4.append(var7);
                     var2.append(var7);
                     if (debugLogger.isDebugEnabled()) {
                        debug("substitute default column value '" + var7 + "'");
                     }
                  } else if (!this.bean.isDbmsDefaultValueField(var10)) {
                     var13 = this.appendComma(var13, var4, var2);
                     var4.append(this.getInsertColumnStringForField(var10, var1));
                     var2.append(this.getInsertQuoteStringForField(var10));
                  }
               } else if (this.bean.isSelfRelationship(var10) && this.bean.containsFkField(var10) && !this.bean.isForeignCmpField(var10)) {
                  var11 = this.bean.getForeignKeyColNames(var10).iterator();

                  while(var11.hasNext()) {
                     var12 = (String)var11.next();
                     var13 = this.appendComma(var13, var4, var2);
                     var4.append(RDBMSUtils.escQuotedID(var12));
                     var2.append("?");
                  }
               }
            }

            var14 = var5.iterator();

            while(true) {
               do {
                  do {
                     do {
                        do {
                           do {
                              if (!var14.hasNext()) {
                                 return var4.toString();
                              }

                              var10 = (String)var14.next();
                           } while(this.bean.isCmpFieldName(var10));
                        } while(var3);
                     } while(this.bean.isSelfRelationship(var10));
                  } while(!this.bean.containsFkField(var10));
               } while(this.bean.isForeignCmpField(var10));

               var11 = this.bean.getForeignKeyColNames(var10).iterator();

               while(var11.hasNext()) {
                  var12 = (String)var11.next();
                  var13 = this.appendComma(var13, var4, var2);
                  var4.append(RDBMSUtils.escQuotedID(var12));
                  var2.append("?");
               }
            }
         }
      }
   }

   private String getInsertColumnStringForField(String var1, int var2) {
      this.curTableName = this.bean.tableAt(var2);
      String var3 = this.bean.getColumnForCmpFieldAndTable(var1, this.curTableName);

      assert var3 != null;

      return RDBMSUtils.escQuotedID(var3);
   }

   private String getInsertQuoteStringForField(String var1) {
      if (this.bean.isBlobCmpColumnTypeForField(var1) && this.bean.getDatabaseType() == 1) {
         return "EMPTY_BLOB()";
      } else {
         return this.bean.isClobCmpColumnTypeForField(var1) && this.bean.getDatabaseType() == 1 ? "\"+((" + this.pmVar() + ".perhapsUseSetStringForClobForOracle()) ? \"?\":\"EMPTY_CLOB()\")+\"" : "?";
      }
   }

   public String prepareCreateStmtArray() {
      return this.bean.getGenKeyType() == 1 && this.bean.getDatabaseType() != 2 && this.bean.getDatabaseType() != 7 ? this.prepareStmtArray(true) : this.prepareStmtArray(false);
   }

   public String prepareStmtArray() {
      return this.prepareStmtArray(false);
   }

   public String prepareStmtArray(boolean var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(EOL);

      for(int var3 = 0; var3 < this.bean.tableCount(); ++var3) {
         var2.append("if (" + this.queryArrayElement(var3) + " != null) ");
         var2.append(this.stmtArrayElement(var3)).append(" = ");
         var2.append(this.conVar()).append(".prepareStatement(").append(this.queryArrayElement(var3));
         if (var1) {
            var2.append(", java.sql.Statement.RETURN_GENERATED_KEYS");
         }

         var2.append(");").append(EOL);
      }

      return var2.toString();
   }

   public String setBeanParamsForCreateArray() {
      StringBuffer var1 = new StringBuffer();
      var1.append("int " + this.numVar() + " = 0;" + EOL);

      label117:
      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         var1.append(this.resetParams() + EOL);
         var1.append(this.numVar() + " = 1;" + EOL);
         ArrayList var3 = new ArrayList(this.bean.getCMPFields(var2));
         String var4;
         if (this.bean.genKeyExcludePKColumn()) {
            var4 = this.bean.getGenKeyPKField();
            if (debugLogger.isDebugEnabled()) {
               debug("CreateBeanParams Exclude PK field: " + var4);
            }

            var3.remove(var4);
         }

         var4 = this.bean.tableAt(var2);
         if (this.bean.getTriggerUpdatesOptimisticColumn(var4)) {
            String var5 = this.bean.getOptimisticColumn(var4);
            String var6 = this.bean.getCmpField(var4, var5);

            assert var3.contains(var6);

            var3.remove(var6);
         }

         ArrayList var15 = new ArrayList(this.bean.getCMPFields(var2));
         Iterator var16 = var15.iterator();

         while(true) {
            while(var16.hasNext()) {
               String var7 = (String)var16.next();
               if ((this.bean.isBlobCmpColumnTypeForField(var7) || this.bean.isClobCmpColumnTypeForField(var7)) && this.bean.getDatabaseType() == 1) {
                  var3.remove(var7);
               } else if (this.bean.isDbmsDefaultValueField(var7)) {
                  var3.remove(var7);
               }
            }

            List var17 = this.bean.getCMRFields(var2);
            var16 = var17.iterator();

            while(true) {
               String var8;
               String var11;
               do {
                  do {
                     do {
                        if (!var16.hasNext()) {
                           String[] var18 = (String[])((String[])var3.toArray(new String[0]));
                           var1.append(this.preparedStatementBindings(var18, "this", true, true, true, this.bd.isOptimistic(), this.stmtArrayElement(var2)));
                           ArrayList var19 = new ArrayList();
                           var17 = this.bean.getCMRFields(var2);
                           var16 = var17.iterator();

                           while(true) {
                              String var20;
                              do {
                                 do {
                                    do {
                                       if (!var16.hasNext()) {
                                          String[] var21 = (String[])((String[])var19.toArray(new String[0]));
                                          var1.append("if (!woFkCols) {");
                                          var1.append(this.preparedStatementBindings(var21, "this", true, true, true, this.bd.isOptimistic(), this.stmtArrayElement(var2)));
                                          var1.append("}\n");
                                          continue label117;
                                       }

                                       var20 = (String)var16.next();
                                    } while(this.bean.isSelfRelationship(var20));
                                 } while(!this.bean.containsFkField(var20));
                              } while(this.bean.isForeignCmpField(var20));

                              var11 = this.bean.getTableForCmrField(var20);
                              Iterator var22 = this.bean.getForeignKeyColNames(var20).iterator();

                              while(var22.hasNext()) {
                                 String var13 = RDBMSUtils.escQuotedID((String)var22.next());
                                 String var14 = this.bean.variableForField(var20, var11, var13);
                                 var19.add(var14);
                              }
                           }
                        }

                        var8 = (String)var16.next();
                     } while(!this.bean.isSelfRelationship(var8));
                  } while(!this.bean.containsFkField(var8));
               } while(this.bean.isForeignCmpField(var8));

               String var9 = this.bean.getTableForCmrField(var8);
               Iterator var10 = this.bean.getForeignKeyColNames(var8).iterator();

               while(var10.hasNext()) {
                  var11 = RDBMSUtils.escQuotedID((String)var10.next());
                  String var12 = this.bean.variableForField(var8, var9, var11);
                  var3.add(var12);
               }
            }
         }
      }

      return var1.toString();
   }

   public String setBeanParamsForCreateArrayOptimizedForClobUpdate() {
      StringBuffer var1 = new StringBuffer();
      var1.append("int " + this.numVar() + " = 0;" + EOL);

      label119:
      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         var1.append(this.resetParams() + EOL);
         var1.append(this.numVar() + " = 1;" + EOL);
         ArrayList var3 = new ArrayList(this.bean.getCMPFields(var2));
         String var4;
         if (this.bean.genKeyExcludePKColumn()) {
            var4 = this.bean.getGenKeyPKField();
            if (debugLogger.isDebugEnabled()) {
               debug("CreateBeanParams Exclude PK field: " + var4);
            }

            var3.remove(var4);
         }

         var4 = this.bean.tableAt(var2);
         if (this.bean.getTriggerUpdatesOptimisticColumn(var4)) {
            String var5 = this.bean.getOptimisticColumn(var4);
            String var6 = this.bean.getCmpField(var4, var5);

            assert var3.contains(var6);

            var3.remove(var6);
         }

         ArrayList var16 = new ArrayList(this.bean.getCMPFields(var2));
         Iterator var17 = var16.iterator();
         int var7 = 0;

         while(true) {
            while(var17.hasNext()) {
               String var8 = (String)var17.next();
               ++var7;
               if (this.bean.isBlobCmpColumnTypeForField(var8) && this.bean.getDatabaseType() == 1) {
                  --var7;
                  var3.remove(var8);
               } else if (this.bean.isClobCmpColumnTypeForField(var8) && this.bean.getDatabaseType() == 1) {
                  var1.append(this.doPreparedStmtBindingForClob(var8, var7, "this", true, false, this.bd.isOptimistic(), this.stmtArrayElement(var2)));
               } else if (this.bean.isDbmsDefaultValueField(var8)) {
                  --var7;
                  var3.remove(var8);
               }
            }

            List var18 = this.bean.getCMRFields(var2);
            var17 = var18.iterator();

            while(true) {
               String var9;
               String var12;
               do {
                  do {
                     do {
                        if (!var17.hasNext()) {
                           String[] var19 = (String[])((String[])var3.toArray(new String[0]));
                           var1.append(this.preparedStatementBindings(var19, "this", true, true, true, this.bd.isOptimistic(), this.stmtArrayElement(var2)));
                           ArrayList var20 = new ArrayList();
                           var18 = this.bean.getCMRFields(var2);
                           var17 = var18.iterator();

                           while(true) {
                              String var21;
                              do {
                                 do {
                                    do {
                                       if (!var17.hasNext()) {
                                          String[] var22 = (String[])((String[])var20.toArray(new String[0]));
                                          var1.append("if (!woFkCols) {");
                                          var1.append(this.preparedStatementBindings(var22, "this", true, true, true, this.bd.isOptimistic(), this.stmtArrayElement(var2)));
                                          var1.append("}\n");
                                          continue label119;
                                       }

                                       var21 = (String)var17.next();
                                    } while(this.bean.isSelfRelationship(var21));
                                 } while(!this.bean.containsFkField(var21));
                              } while(this.bean.isForeignCmpField(var21));

                              var12 = this.bean.getTableForCmrField(var21);
                              Iterator var23 = this.bean.getForeignKeyColNames(var21).iterator();

                              while(var23.hasNext()) {
                                 String var14 = RDBMSUtils.escQuotedID((String)var23.next());
                                 String var15 = this.bean.variableForField(var21, var12, var14);
                                 var20.add(var15);
                              }
                           }
                        }

                        var9 = (String)var17.next();
                     } while(!this.bean.isSelfRelationship(var9));
                  } while(!this.bean.containsFkField(var9));
               } while(this.bean.isForeignCmpField(var9));

               String var10 = this.bean.getTableForCmrField(var9);
               Iterator var11 = this.bean.getForeignKeyColNames(var9).iterator();

               while(var11.hasNext()) {
                  var12 = RDBMSUtils.escQuotedID((String)var11.next());
                  String var13 = this.bean.variableForField(var9, var10, var12);
                  var3.add(var13);
               }
            }
         }
      }

      return var1.toString();
   }

   public String setBeanParamsForCreate() {
      ArrayList var1 = new ArrayList(this.cmpFieldNames);
      if (this.bean.genKeyExcludePKColumn()) {
         String var2 = this.bean.getGenKeyPKField();
         if (debugLogger.isDebugEnabled()) {
            debug("CreateBeanParams Exclude PK field: " + var2);
         }

         var1.remove(var2);
      }

      Iterator var9 = this.cmpFieldNames.iterator();

      while(true) {
         String var3;
         do {
            if (!var9.hasNext()) {
               Iterator var10 = this.bean.getForeignKeyFieldNames().iterator();

               while(true) {
                  String var4;
                  do {
                     do {
                        if (!var10.hasNext()) {
                           String[] var11 = (String[])((String[])var1.toArray(new String[0]));
                           return this.preparedStatementBindings(var11, "this", true, true, false, this.bd.isOptimistic());
                        }

                        var4 = (String)var10.next();
                     } while(!this.bean.containsFkField(var4));
                  } while(this.bean.isForeignCmpField(var4));

                  String var5 = this.bean.getTableForCmrField(var4);
                  Iterator var6 = this.bean.getForeignKeyColNames(var4).iterator();

                  while(var6.hasNext()) {
                     String var7 = RDBMSUtils.escQuotedID((String)var6.next());
                     String var8 = this.bean.variableForField(var4, var5, var7);
                     var1.add(var8);
                  }
               }
            }

            var3 = (String)var9.next();
         } while(!this.bean.isBlobCmpColumnTypeForField(var3) && !this.bean.isClobCmpColumnTypeForField(var3));

         var1.remove(var3);
      }
   }

   public String setRemoveQueryArray() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         this.curTableIndex = var2;
         this.curTableName = this.bean.tableAt(this.curTableIndex);
         var1.append(this.perhapsConstructSnapshotPredicate());
         var1.append(this.queryArrayElement(var2)).append(" = \"DELETE FROM ");
         var1.append(this.curTableName).append(" WHERE ");
         var1.append(this.idParamsSqlForTable(this.curTableName) + "\" ");
         var1.append(this.perhapsAddSnapshotPredicate());
         var1.append(";").append(EOL);
      }

      return var1.toString();
   }

   public String perhapsDeclareSetBlobClobForOutputMethod() throws CodeGenerationException {
      if (!this.bean.hasBlobClobColumn()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         String[] var2 = (String[])((String[])this.cmpFieldNames.toArray(new String[0]));
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            String var5 = var2[var3];
            if (this.bean.hasCmpColumnType(var5)) {
               if (this.bean.isBlobCmpColumnTypeForField(var5)) {
                  this.curField = var2[var3];
                  this.curTableName = this.bean.getTableForCmpField(this.curField);
                  this.curTableIndex = this.bean.tableIndex(this.curTableName);
                  var1.append(this.parse(this.getProductionRule("setBlobForOutputBody")));
                  var1.append(this.parse(this.getProductionRule("setBlobForInputBody")));
               }

               if (this.bean.isClobCmpColumnTypeForField(var5)) {
                  this.curField = var2[var3];
                  this.curTableName = this.bean.getTableForCmpField(this.curField);
                  this.curTableIndex = this.bean.tableIndex(this.curTableName);
                  var1.append(this.parse(this.getProductionRule("setClobForOutputBody")));
                  var1.append(this.parse(this.getProductionRule("setClobForInputBody")));
               }
            }
         }

         return var1.toString();
      }
   }

   public String perhapsTruncateClob() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.getDatabaseType() == 1) {
         var1.append(this.parse(this.getProductionRule("truncateOracleClob")));
      } else {
         var1.append(this.parse(this.getProductionRule("truncateGenericClob")));
      }

      return var1.toString();
   }

   public String getClobWriter() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.getDatabaseType() == 1) {
         var1.append(this.parse(this.getProductionRule("getOracleClobWriter")));
      } else {
         var1.append(this.parse(this.getProductionRule("getGenericClobWriter")));
      }

      return var1.toString();
   }

   public String perhapsUpdateClob() throws CodeGenerationException {
      return this.bean.getDatabaseType() == 4 ? this.parse(this.getProductionRule("updateClob")) : "";
   }

   public String setClobParam() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.stmtVar());
      var1.append(".setClob(");
      var1.append(this.preparedStatementParamIndex++).append(", lob");
      var1.append(");" + EOL);
      return var1.toString();
   }

   public String setLobAsTypeParam() {
      StringBuffer var1 = new StringBuffer();
      this.addPreparedStatementBinding(var1, this.curField, "this", String.valueOf(this.preparedStatementParamIndex), true, true, false, false, this.stmtVar());
      ++this.preparedStatementParamIndex;
      return var1.toString();
   }

   public String perhapsTruncateBlob() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.getDatabaseType() == 1) {
         var1.append(this.parse(this.getProductionRule("truncateOracleBlob")));
      } else {
         var1.append(this.parse(this.getProductionRule("truncateGenericBlob")));
      }

      return var1.toString();
   }

   public String setBlobParam() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.stmtVar());
      var1.append(".setBlob(");
      var1.append(this.preparedStatementParamIndex++).append(", lob");
      var1.append(");" + EOL);
      return var1.toString();
   }

   public String writeBlobToOutputStream() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.bean.getDatabaseType() == 1) {
         var1.append(this.parse(this.getProductionRule("writeOracleBlobToOutputStream")));
      } else {
         var1.append(this.parse(this.getProductionRule("writeGenericBlobToOutputStream")));
      }

      return var1.toString();
   }

   public String perhapsUpdateBlob() throws CodeGenerationException {
      return this.bean.getDatabaseType() == 4 ? this.parse(this.getProductionRule("updateBlob")) : "";
   }

   public String setBlobClobForCreate() throws CodeGenerationException {
      if (this.bean.hasBlobClobColumn() && this.bean.getDatabaseType() == 1) {
         StringBuffer var1 = new StringBuffer();
         String[] var2 = (String[])((String[])this.cmpFieldNames.toArray(new String[0]));
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            String var5 = var2[var3];
            if (this.bean.hasCmpColumnType(var5)) {
               this.curField = var2[var3];
               this.curTableName = this.bean.getTableForCmpField(this.curField);
               this.curTableIndex = this.bean.tableIndex(this.curTableName);
               if (this.bean.isBlobCmpColumnTypeForField(var5)) {
                  var1.append("\n" + this.setBlobClobForOutputMethodName() + "(" + this.conVar() + "," + this.pkVar() + ");");
               } else if (this.bean.isClobCmpColumnTypeForField(var5)) {
                  var1.append(EOL + "if(!" + this.pmVar() + ".perhapsUseSetStringForClobForOracle()){" + EOL);
                  var1.append("// Using 3 step procedure to insert Clob column as the  setStringForClob API is not supported by current driver " + EOL);
                  var1.append(this.setBlobClobForOutputMethodName() + "(" + this.conVar() + "," + this.pkVar() + ");" + EOL);
                  var1.append("}" + EOL);
               }
            }
         }

         return var1.toString();
      } else {
         return "";
      }
   }

   public String setBlobClobForStore() throws CodeGenerationException {
      if (!this.bean.hasBlobClobColumn()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         String[] var2 = (String[])((String[])this.cmpFieldNames.toArray(new String[0]));
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            String var5 = var2[var3];
            if (this.bean.hasCmpColumnType(var5) && (this.bean.isBlobCmpColumnTypeForField(var5) || this.bean.isClobCmpColumnTypeForField(var5))) {
               this.curField = var2[var3];
               this.curTableName = this.bean.getTableForCmpField(this.curField);
               this.curTableIndex = this.bean.tableIndex(this.curTableName);
               var1.append(EOL);
               var1.append("if (" + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var5) + "]) {" + EOL);
               var1.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var5 + "' using column \" +" + this.numVar() + " + \". Value is \" + this." + var5 + ");" + EOL);
               var1.append(this.setBlobClobForOutputMethodName() + "(" + this.conVar() + "," + this.pkVar() + ");\n");
               var1.append("}").append(EOL);
            }
         }

         return var1.toString();
      }
   }

   public String perhapsDeclareBlobClobCountVar() {
      return this.bean.hasBlobClobColumn() ? "int " + this.blobClobCountVar() + " = 0;" : "";
   }

   public String perhapsResetBlobClobCountVar() {
      return this.bean.hasBlobClobColumn() ? this.blobClobCountVar() + " = 0;" : "";
   }

   public String blobClobCountVarOrZero() {
      return this.bean.hasBlobClobColumn() ? this.blobClobCountVar() : "0";
   }

   public String perhapsByteArrayIsSerializedToBlob() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.fieldNameForField();
      Class var3 = this.bean.getCmpFieldClass(var2);
      if (ClassUtils.isByteArray(var3) && !this.bean.getByteArrayIsSerializedToOracleBlob()) {
         var1.append("byte[] outByteArray = ").append(var2).append(";");
         var1.append(EOL);
      } else {
         var1.append(this.parse(this.getProductionRule("convertFieldToByteArray")));
      }

      return var1.toString();
   }

   public String perhapsByteArrayIsDeserializedFromBlob() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.fieldNameForField();
      Class var3 = this.bean.getCmpFieldClass(var2);
      if (ClassUtils.isByteArray(var3) && !this.bean.getByteArrayIsSerializedToOracleBlob()) {
         var1.append(var2 + " = inByteArray;");
      } else {
         var1.append("ByteArrayInputStream bais = new ByteArrayInputStream(inByteArray, 0, length);").append(EOL);
         var1.append("ObjectInputStream ois = new ObjectInputStream(bais);").append(EOL);
         var1.append("try {").append(EOL);
         var1.append(var2 + " = (" + this.fieldClassForCmpField() + ") ois.readObject();").append(EOL);
         var1.append("} catch (ClassNotFoundException cnfe) {").append(EOL).append(EOL);
         var1.append("if (" + this.debugEnabled() + ") {").append(EOL);
         var1.append(this.debugSay() + "(\"ClassNotFoundException for Blob-Clob\" + " + "cnfe.getMessage());").append(EOL);
         var1.append("}").append(EOL);
         var1.append("throw cnfe;").append(EOL);
         var1.append("}").append(EOL);
         var1.append("bais.close();").append(EOL);
         var1.append("ois.close();").append(EOL);
      }

      return var1.toString();
   }

   private boolean useVersionOrTimestampCheckingForBlobClob(String var1) {
      return this.bean.hasBlobClobColumn() && this.bd.isOptimistic() && (this.bean.getVerifyColumns(var1).equalsIgnoreCase("version") || this.bean.getVerifyColumns(var1).equalsIgnoreCase("timestamp"));
   }

   public String setPrimaryKeyParamsArray() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      var1.append("int " + this.numVar() + " = 0;" + EOL);

      for(int var2 = 0; var2 < this.bean.tableCount(); ++var2) {
         this.curTableIndex = var2;
         this.curTableName = this.bean.tableAt(var2);
         this.resetParams();
         var1.append(this.numVar() + " = 1;" + EOL);
         var1.append(this.setPrimaryKeyParamsUsingNum());
         var1.append(this.perhapsSetSnapshotParameters());
      }

      return var1.toString();
   }

   public String setPrimaryKeyParamsForTableIndex(int var1) {
      new StringBuffer();
      String[] var3 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
      return this.preparedStatementBindings(var3, this.pkVar(), false, this.bd.hasComplexPrimaryKey(), false, false, this.stmtArrayElement(var1));
   }

   public String setPrimaryKeyParamsForTableUsingNum(String var1) {
      return "whoa..  setPrimaryKeyParamsForTableUsingNum  needs to be implemented";
   }

   public String setPrimaryKeyParams() {
      String[] var1 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
      return this.preparedStatementBindings(var1, this.pkVar(), false, this.bd.hasComplexPrimaryKey(), false, false);
   }

   public String setPrimaryKeyParamsUsingNum() {
      String[] var1 = (String[])((String[])this.pkFieldNames.toArray(new String[0]));
      return this.preparedStatementBindings(var1, this.pkVar(), false, this.bd.hasComplexPrimaryKey(), true, false, this.stmtArrayElement(this.curTableIndex));
   }

   private String preparedStatementBindings(String[] var1, String var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return this.preparedStatementBindings(var1, var2, var3, var4, var5, var6, this.stmtVar());
   }

   private String preparedStatementBindings(String[] var1, String var2, boolean var3, boolean var4, boolean var5, boolean var6, String var7) {
      StringBuffer var8 = new StringBuffer();
      var8.append("\n");
      int var9 = 0;

      for(int var10 = var1.length; var9 < var10; ++var9) {
         String var11 = var1[var9];
         String var12;
         if (var5) {
            var12 = this.numVar();
         } else {
            var12 = String.valueOf(this.preparedStatementParamIndex);
         }

         this.addPreparedStatementBinding(var8, var11, var2, var12, var3, var4, var6 && !this.bd.getPrimaryKeyFieldNames().contains(var11), false, var7);
         if (var5) {
            var8.append(this.numVar() + "++;" + EOL);
         } else {
            ++this.preparedStatementParamIndex;
         }

         if (var9 < var10 - 1) {
            var8.append(EOL);
         }
      }

      return var8.toString();
   }

   private void addPreparedStatementBinding(StringBuffer var1, String var2, String var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      this.addPreparedStatementBinding(var1, var2, var3, var4, var5, var6, var7, var8, this.stmtVar());
   }

   private void addPreparedStatementBinding(StringBuffer var1, String var2, String var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8, String var9) {
      if (this.bean.isClobCmpColumnTypeForField(var2) && this.bean.getDatabaseType() == 1) {
         var1.append("// Clob column " + var2 + " is already binded in " + "PreparedStatement as first variable, but we still need to increment " + "local variable to keep correct indexing." + EOL);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("Adding a prepared statement binding: ");
            debug("\t\tfield = " + var2);
            debug("\t\tobj = " + var3);
            debug("\t\tparamIdx = " + var4);
            debug("\t\tobjIsCompound = " + var6);
         }

         String var10 = null;
         if (var5 && this.bean.hasCmpField(var2)) {
            var10 = this.getCmpField(var3, var2);
         } else if (var6) {
            var10 = var3 + "." + var2;
         } else {
            var10 = var3;
         }

         Class var11 = this.getVariableClass(var2);

         assert var11 != null;

         if (!var11.isPrimitive()) {
            this.addNullCheck(var1, var10, var2, var4, var9);
         }

         this.preparedStatementBindingBody(var1, var3, var2, var11, var4, var5, var6, var7, var8, RDBMSUtils.isOracleNLSDataType(this.bean, var2), var9);
         if (!var11.isPrimitive()) {
            var1.append("}" + EOL);
            if (var7 && this.doSnapshot(var2) && !this.bean.isClobCmpColumnTypeForField(var2) && !this.bean.isBlobCmpColumnTypeForField(var2)) {
               var1.append("else {" + EOL);
               var1.append(var3 + "." + CodeGenUtils.snapshotNameForVar(var2) + " = null;" + EOL);
               var1.append("}" + EOL);
            }
         }

      }
   }

   private void preparedStatementBindingBody(StringBuffer var1, String var2, String var3, Class var4, String var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10) {
      this.preparedStatementBindingBody(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, this.stmtVar());
   }

   private void preparedStatementBindingBody(StringBuffer var1, String var2, String var3, Class var4, String var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10, String var11) {
      String var12 = this.bean.getCmpColumnTypeForField(var3);
      if (this.bean.hasCmpColumnType(var3) && (var12.equalsIgnoreCase("Blob") || var12.equalsIgnoreCase("Clob")) && this.bean.getDatabaseType() == 1) {
         var1.append("  ");
      } else {
         boolean var13 = var6 && !this.bd.isBeanClassAbstract() && this.bean.hasCmpField(var3);
         String var14 = null;
         if (var13) {
            var14 = var2 + "." + "__WL_super_" + MethodUtils.getMethodName(var3) + "()";
         } else if (var7) {
            var14 = var2 + "." + var3;
         } else {
            var14 = var2;
         }

         boolean var15 = this.bean.isCharArrayMappedToString(var4);
         if (var15) {
            var4 = Character.TYPE;
         }

         String var16;
         if (!this.bean.isValidSQLType(var4)) {
            var1.append("ByteArrayOutputStream bstr = new ByteArrayOutputStream();" + EOL);
            var1.append("ObjectOutputStream ostr = new ObjectOutputStream(bstr);" + EOL);
            if (EJBHome.class.isAssignableFrom(var4)) {
               var1.append("HomeHandle handle = " + var14 + ".getHomeHandle();" + EOL);
               var1.append("ostr.writeObject(handle);" + EOL);
            } else if (EJBObject.class.isAssignableFrom(var4)) {
               var1.append("Handle handle = " + var14 + ".getHandle();" + EOL);
               var1.append("ostr.writeObject(handle);" + EOL);
            } else {
               var1.append("ostr.writeObject(" + var14 + ");" + EOL);
            }

            var16 = null;
            if (var9) {
               var16 = this.byteArrayVar(var3);
            } else {
               var16 = "byteArray";
               var1.append("byte[] ");
            }

            var1.append(var16 + " = bstr.toByteArray();" + EOL);
            var1.append("if (" + this.debugEnabled() + ") {" + EOL);
            var1.append(this.debugSay() + "(\"writing bytes: \" + " + var16 + ");" + EOL);
            var1.append("if (" + var16 + "!=null) {" + EOL);
            var1.append(this.debugSay() + "(\"bytes length: \" + " + var16 + ".length);" + EOL);
            var1.append("}" + EOL);
            var1.append("}" + EOL);
            if (var8 && this.doSnapshot(var3) && !this.bean.isBlobCmpColumnTypeForField(var3)) {
               var1.append(var2 + "." + CodeGenUtils.snapshotNameForVar(var3) + " = " + var16 + ";" + EOL);
            }

            if (!"SybaseBinary".equalsIgnoreCase(this.bean.getCmpColumnTypeForField(var3)) && !this.perhapsSybaseBinarySetForAnyCmpField()) {
               var1.append("InputStream inputStream  = new ByteArrayInputStream(" + var16 + ");" + EOL);
               var1.append(var11 + ".setBinaryStream(" + var5 + ", inputStream, " + var16 + ".length);" + EOL);
            } else {
               var1.append(var11 + ".setBytes(" + var5 + "," + var16 + ");" + EOL);
            }
         } else {
            if (var8 && this.doSnapshot(var3)) {
               var1.append(this.takeSnapshotForVar(var2, var3, var13));
            }

            if (ClassUtils.isByteArray(var4)) {
               if (!"SybaseBinary".equalsIgnoreCase(this.bean.getCmpColumnTypeForField(var3)) && !this.perhapsSybaseBinarySetForAnyCmpField()) {
                  var1.append("InputStream inputStream  = new ByteArrayInputStream(" + var14 + ");" + EOL);
                  var1.append(var11 + ".setBinaryStream(" + var5 + ", inputStream, " + var14 + ".length);" + EOL);
               } else {
                  var1.append(var11 + ".setBytes(" + var5 + "," + var14 + ");" + EOL);
               }
            } else {
               var16 = StatementBinder.getStatementTypeNameForClass(var4);
               if (String.class.equals(var4) && "LongString".equals(this.bean.getCmpColumnTypeForField(var3))) {
                  var1.append("java.io.StringReader stringReader  = new java.io.StringReader(" + var14 + ");" + EOL);
                  var1.append(var11 + ".setCharacterStream(" + var5 + ", stringReader, " + var14 + ".length());" + EOL);
               } else if (String.class.equals(var4) && "SQLXML".equals(this.bean.getCmpColumnTypeForField(var3))) {
                  var1.append("java.sql.SQLXML sqlXml  = " + var11 + ".getConnection().createSQLXML();" + EOL);
                  var1.append("sqlXml.setString(" + var14 + ");" + EOL);
                  var1.append(var11 + ".setSQLXML(" + var5 + ", sqlXml);" + EOL);
               } else {
                  if (var10) {
                     var1.append("if(").append(var11).append(" instanceof oracle.jdbc.OraclePreparedStatement) {" + EOL);
                     var1.append("((oracle.jdbc.OraclePreparedStatement)").append(var11).append(").setFormOfUse(").append(var5).append(", oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);").append(EOL);
                     var1.append("}" + EOL);
                  }

                  var1.append(var11);
                  var1.append(".set" + var16 + "(");
                  var1.append(var5).append(", ");
                  if (var7) {
                     if (var4 == Character.TYPE) {
                        if (var15) {
                           var1.append("new String(" + var14 + ")");
                        } else {
                           var1.append("String.valueOf(" + var14 + ")");
                        }
                     } else if (var4 == Character.class) {
                        var1.append("String.valueOf(" + var14 + ".charValue())");
                     } else if (var4 == Date.class) {
                        var1.append("new java.sql.Timestamp(");
                        var1.append(var14);
                        var1.append(".getTime())");
                     } else {
                        var1.append(MethodUtils.convertToPrimitive(var4, var14));
                     }
                  } else if (var4 == Character.TYPE) {
                     if (var15) {
                        var1.append("new String(" + var2 + ")");
                     } else {
                        var1.append("String.valueOf(" + var2 + ")");
                     }
                  } else if (var4 == Character.class) {
                     var1.append("String.valueOf(" + var2 + ".charValue())");
                  } else if (var4 == Date.class) {
                     var1.append("new java.sql.Timestamp(");
                     var1.append(var2 + ".getTime())");
                  } else {
                     var1.append(MethodUtils.convertToPrimitive(var4, var2));
                  }

                  var1.append(");" + EOL);
               }

               var1.append("if (" + this.debugEnabled() + ") {" + EOL);
               var1.append(this.debugSay() + "(\"paramIdx :\"+" + var5 + "+\" binded with value :\"+" + var14 + ");" + EOL);
               var1.append("}" + EOL);
            }
         }

      }
   }

   private boolean perhapsSybaseBinarySetForAnyCmpField() {
      boolean var1 = false;
      List var2 = this.bean.getCmpFieldNames();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if ("SybaseBinary".equalsIgnoreCase(this.bean.getCmpColumnTypeForField(var4))) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   public String setUpdateBeanParams() throws CodeGenerationException {
      String[] var1 = (String[])((String[])this.cmpFieldNames.toArray(new String[0]));
      StringBuffer var2 = new StringBuffer();
      List var3 = this.bean.getForeignKeyFieldNames();

      String var7;
      String var9;
      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (!this.bean.isBlobCmpColumnTypeForField(var1[var4]) && !this.bean.isClobCmpColumnTypeForField(var1[var4]) && !this.bd.getPrimaryKeyFieldNames().contains(var1[var4])) {
            int var5 = this.bean.getTableIndexForCmpField(var1[var4]);

            assert var5 >= 0;

            if (var5 == this.curTableIndex) {
               String var6 = this.bean.getTableForCmpField(var1[var4]);
               var7 = this.bean.getColumnForCmpFieldAndTable(var1[var4], var6);
               if (!this.bean.hasOptimisticColumn(var6) || !this.bean.getOptimisticColumn(var6).equals(var7)) {
                  String var8 = "";
                  if (var3 != null) {
                     var9 = null;
                     Iterator var10 = var3.iterator();

                     while(var10.hasNext()) {
                        var9 = (String)var10.next();
                        if (this.bean.getForeignKeyColNames(var9).contains(var7)) {
                           var8 = this.perhapsIsFkColsNullableCheck(var9);
                           break;
                        }
                     }
                  }

                  var2.append("if (" + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var1[var4]) + "] " + var8 + ") {" + EOL);
                  var2.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var1[var4] + "' using column \" +" + this.numVar() + " + \". Value is \" +" + this.getCmpField(var1[var4]) + ");" + EOL);
                  this.addPreparedStatementBinding(var2, var1[var4], "this", this.numVar(), true, true, false, !this.bd.getPrimaryKeyFieldNames().contains(var1[var4]), this.stmtArrayElement(this.curTableIndex));
                  var2.append(this.numVar() + "++;" + EOL);
                  var2.append("};" + EOL + EOL);
               }
            }
         }
      }

      Iterator var11 = this.bean.getForeignKeyFieldNames().iterator();

      while(true) {
         String var12;
         int var13;
         do {
            do {
               do {
                  if (!var11.hasNext()) {
                     return var2.toString();
                  }

                  var12 = (String)var11.next();
               } while(!this.bean.containsFkField(var12));
            } while(this.bean.isForeignCmpField(var12));

            var13 = this.bean.getTableIndexForCmrf(var12);
            Debug.assertion(var13 >= 0);
         } while(var13 != this.curTableIndex);

         var7 = this.bean.getTableForCmrField(var12);
         Iterator var14 = this.bean.getForeignKeyColNames(var12).iterator();

         while(var14.hasNext()) {
            var9 = (String)var14.next();
            String var15 = this.bean.variableForField(var12, var7, var9);
            var2.append("if (" + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var12) + "]" + this.perhapsIsFkColsNullableCheck(var12) + ") {" + EOL);
            var2.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var15 + "' using column \" +" + this.numVar() + " + \". Value is \" + this." + var15 + ");" + EOL);
            this.addPreparedStatementBinding(var2, var15, "this", this.numVar(), true, true, false, true, this.stmtArrayElement(this.curTableIndex));
            var2.append(this.numVar() + "++;" + EOL);
            var2.append("};" + EOL);
         }
      }
   }

   public String perhapsVerifyOptimistic() {
      assert this.curGroup != null;

      StringBuffer var1 = new StringBuffer();
      Set var2 = this.bean.getTableNamesForGroup(this.curGroup.getName());
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (this.bean.hasOptimisticColumn(var4) && this.bean.getTriggerUpdatesOptimisticColumn(var4)) {
            String var5 = this.bean.getOptimisticColumn(var4);
            String var6 = this.bean.getCmpField(var4, var5);
            var1.append("if (this.");
            var1.append(var6);
            var1.append(" == null) {");
            var1.append(EOL);
            var1.append("Loggable l = EJBLogger.logoptimisticColumnIsNullLoggable(\"");
            var1.append(this.bean.getEjbName());
            var1.append("\", \"");
            var1.append(var4);
            var1.append("\", \"");
            var1.append(var5);
            var1.append("\");");
            var1.append(EOL);
            var1.append("throw new EJBException(l.getMessage());");
            var1.append(EOL);
            var1.append("}");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   public String perhapsSetOptimisticColumnParam() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.bean.tableAt(this.curTableIndex);
      if (this.bean.hasOptimisticColumn(var2) && !this.bean.getTriggerUpdatesOptimisticColumn(var2)) {
         String var3 = this.bean.getOptimisticColumn(var2);
         String var4 = this.bean.getCmpField(var2, var3);
         var1.append("if (" + this.isModifiedVar() + "[" + this.bean.getIsModifiedIndex(var4) + "]) {" + EOL);
         var1.append("if(" + this.debugEnabled() + ") " + this.debugSay() + "(\"setting(\"+this+\") '" + var4 + "' using column \" +" + this.numVar() + " + \". Value is \" + " + this.getCmpField(var4) + ");" + EOL);
         this.addPreparedStatementBinding(var1, var4, "this", this.numVar(), true, true, false, !this.bd.getPrimaryKeyFieldNames().contains(var4), this.stmtArrayElement(this.curTableIndex));
         var1.append(this.numVar() + "++;" + EOL);
         var1.append("};" + EOL + EOL);
      }

      return var1.toString();
   }

   public String assignGroupColumnsToThis() throws CodeGenerationException {
      return this.assignGroupColumns("this", false);
   }

   public String assignGroupColumnsToBean() throws CodeGenerationException {
      return this.assignGroupColumns(this.beanVar(), true);
   }

   public String assignGroupColumns(String var1, boolean var2) throws CodeGenerationException {
      StringBuffer var3 = new StringBuffer();
      ArrayList var4 = new ArrayList();
      HashSet var5 = new HashSet();
      TreeSet var6 = new TreeSet();
      if (var2) {
         var4.addAll(this.bd.getPrimaryKeyFieldNames());
         var3.append("if (" + this.pkVar() + " == null) {" + EOL);
         this.assignToVars(var3, var1, true, var4, 1, true);
         var3.append("} else {" + EOL);
         var3.append(this.beanVar() + ".__WL_setPrimaryKey((" + this.pk_class() + ") " + this.pkVar() + ");" + EOL);
         var3.append("}" + EOL);
      }

      TreeSet var7 = new TreeSet(this.curGroup.getCmpFields());
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         if (!var4.contains(var9)) {
            var4.add(var9);
         }
      }

      var6.addAll(var4);
      Iterator var15 = var6.iterator();

      while(var15.hasNext()) {
         String var10 = (String)var15.next();
         var5.add(this.bean.getCmpColumnForField(var10));
      }

      Iterator var16 = this.curGroup.getCmrFields().iterator();

      String var11;
      String var12;
      String var14;
      while(var16.hasNext()) {
         var11 = (String)var16.next();
         var12 = this.bean.getTableForCmrField(var11);
         Iterator var13 = this.bean.getForeignKeyColNames(var11).iterator();

         while(var13.hasNext()) {
            var14 = (String)var13.next();
            if (!var5.contains(var14)) {
               var4.add(this.bean.variableForField(var11, var12, var14));
            }
         }
      }

      if (var2) {
         var4.removeAll(this.bd.getPrimaryKeyFieldNames());
         this.assignToVars(var3, var1, true, var4, 1 + this.bd.getPrimaryKeyFieldNames().size(), true);
      } else {
         this.assignToVars(var3, var1, true, var4, 1, true);
      }

      var15 = var6.iterator();

      while(var15.hasNext()) {
         var11 = (String)var15.next();
         var3.append(var1 + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var11) + "]");
         var3.append(" = true;" + EOL);
      }

      var16 = this.curGroup.getCmrFields().iterator();

      while(var16.hasNext()) {
         var11 = (String)var16.next();
         var12 = this.bean.getTableForCmrField(var11);
         String var17 = (String)this.bean.getForeignKeyColNames(var11).iterator().next();
         if (!var5.contains(var17)) {
            var14 = this.bean.variableForField(var11, var12, var17);
            var3.append(var1 + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex((String)this.variableToField.get(var14)) + "]");
            var3.append(" = true;" + EOL);
         }
      }

      var3.append(var1 + "." + this.beanIsLoadedVar() + " = true;");
      return var3.toString();
   }

   public String assignCMRFieldFKColumns(String var1, String var2) throws CodeGenerationException {
      StringBuffer var3 = new StringBuffer();
      ArrayList var4 = new ArrayList();
      String var5 = this.bean.getTableForCmrField(var2);
      Iterator var6 = this.bean.getForeignKeyColNames(var2).iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         var4.add(this.bean.variableForField(var2, var5, var7));
      }

      this.assignToVars(var3, var1, true, var4, 1, true);

      for(Iterator var9 = var4.iterator(); var9.hasNext(); var3.append(" = true;" + EOL)) {
         String var8 = (String)var9.next();
         if (this.bean.getIsModifiedIndex(var8) != null) {
            var3.append(var1 + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var8) + "]");
         } else {
            var3.append(var1 + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var2) + "]");
         }
      }

      return var3.toString();
   }

   public String assignCMRFieldPKColumns(String var1) throws CodeGenerationException {
      StringBuffer var2 = new StringBuffer();
      ArrayList var3 = new ArrayList();
      var3.addAll(this.bd.getPrimaryKeyFieldNames());
      this.assignToVars(var2, var1, true, var3, 1, true);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2.append(var1 + "." + this.isLoadedVar() + "[" + this.bean.getIsModifiedIndex(var5) + "]");
         var2.append(" = true;" + EOL);
      }

      return var2.toString();
   }

   public String allFieldsCount() {
      return String.valueOf(this.bean.getFieldCount());
   }

   public String refresh_bean_from_key() {
      return this.isContainerManagedBean ? "loadByPrimaryKey(ctx);" : "((" + this.ejbClass.getName() + ")(ctx.getBean())).ejbFindByPrimaryKey(pk);";
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
               var1.append(this.parse(this.getProductionRule("home_method")));
            } catch (NoSuchMethodException var5) {
               throw new AssertionError(var5);
            }
         }
      }

      return var1.toString();
   }

   private String homeToBeanName(String var1, String var2) {
      StringBuffer var3 = new StringBuffer(var1 + var2);
      var3.setCharAt(var1.length(), Character.toUpperCase(var3.charAt(var1.length())));
      return var3.toString();
   }

   public String getEJBObject() {
      return !this.bd.hasLocalClientView() && !this.bd.isEJB30() ? "getEJBObject" : "getEJBLocalObject";
   }

   public String EJBObjectForField() {
      assert this.curField != null;

      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      return !var1.hasLocalClientView() && !this.bd.isEJB30() ? "javax.ejb.EJBObject" : "javax.ejb.EJBLocalObject";
   }

   public String findByPrimaryKeyForField() {
      assert this.curField != null;

      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      return !var1.hasLocalClientView() && !var1.isEJB30() ? "remoteFindByPrimaryKey" : "localFindByPrimaryKey";
   }

   public String scalarFinderForField() {
      assert this.curField != null;

      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      return !var1.hasLocalClientView() && !this.bd.isEJB30() ? "remoteScalarFinder" : "localScalarFinder";
   }

   public String scalarFinder(Finder var1) {
      RDBMSBean var2 = var1.getSelectBeanTarget();
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      if (var1.hasLocalResultType()) {
         return !var3.hasLocalClientView() && !this.bd.isEJB30() ? "remoteScalarFinder" : "localScalarFinder";
      } else {
         assert var3.hasRemoteClientView();

         return "remoteScalarFinder";
      }
   }

   public String collectionFinder(Finder var1) {
      RDBMSBean var2 = var1.getSelectBeanTarget();
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      if (var1.hasLocalResultType()) {
         return !var3.hasLocalClientView() && !this.bd.isEJB30() ? "remoteCollectionFinder" : "localCollectionFinder";
      } else {
         assert var3.hasRemoteClientView();

         return "remoteCollectionFinder";
      }
   }

   public String setFinder(Finder var1) {
      RDBMSBean var2 = var1.getSelectBeanTarget();
      CMPBeanDescriptor var3 = var2.getCMPBeanDescriptor();
      if (var1.hasLocalResultType()) {
         return !var3.hasLocalClientView() && !this.bd.isEJB30() ? "remoteSetFinder" : "localSetFinder";
      } else {
         assert var3.hasRemoteClientView();

         return "remoteSetFinder";
      }
   }

   public String registerInvalidationBean() {
      EntityBeanInfo var1 = this.bi;
      StringBuffer var2 = new StringBuffer();
      if (var1.getInvalidationTargetEJBName() != null) {
         var2.append(this.pmVar() + ".registerModifiedBean(" + this.ctxVar() + ".getPrimaryKey());" + EOL);
      }

      return var2.toString();
   }

   public String readOnlyFinderOneToOneGetterBody() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      StringBuffer var2 = new StringBuffer();
      if (var1.getConcurrencyStrategy() == 4) {
         var2.append("TransactionManager tms = TxHelper.getTransactionManager();\n");
         var2.append("tms.suspend();\n");
         var2.append("tms.begin();\n");
         var2.append("Transaction tx = tms.getTransaction();\n");
         var2.append("try { " + EOL);
      }

      var2.append(this.fieldVarForField() + " = (" + this.classNameForField() + ")" + this.bmVarForField() + "." + this.scalarFinderForField() + "(\n");
      var2.append(this.finderVarForField() + ",new Object[]{" + this.ctxVar() + ".getPrimaryKey()});\n");
      if (var1.getConcurrencyStrategy() == 4) {
         var2.append("} finally { " + EOL);
         var2.append("// Dont need to worry for rollback call etc, " + EOL);
         var2.append("// as this is readonly tx and this is used only for a finder call. " + EOL);
         var2.append("tx.commit();\n");
         var2.append("} " + EOL);
      }

      return var2.toString();
   }

   public String readOnlyOneToOneGetterBody_fkOwner() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      StringBuffer var2 = new StringBuffer();
      if (var1.getConcurrencyStrategy() == 4) {
         var2.append("TransactionManager tms = TxHelper.getTransactionManager();\n");
         var2.append("tms.suspend();\n");
         var2.append("tms.begin();\n");
         var2.append("Transaction tx = tms.getTransaction();\n");
         var2.append("try { " + EOL);
      }

      var2.append(this.fieldVarForField() + " = (" + this.classNameForField() + ")" + this.finderInvokerForField() + "." + this.findByPrimaryKeyForField() + "(\n");
      var2.append(this.finderParamForField() + this.fkVarForField() + ");\n");
      if (var1.getConcurrencyStrategy() == 4) {
         var2.append("} finally { " + EOL);
         var2.append("// Dont need to worry for rollback call etc, " + EOL);
         var2.append("// as this is readonly tx and this is used only for a finder call. " + EOL);
         var2.append("tx.commit();\n");
         var2.append("} " + EOL);
      }

      return var2.toString();
   }

   public String readOnlyOneToManyGetterBody_fkOwner() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      StringBuffer var2 = new StringBuffer();
      if (var1.getConcurrencyStrategy() == 4) {
         var2.append("TransactionManager tms = TxHelper.getTransactionManager();" + EOL);
         var2.append("tms.suspend();" + EOL);
         var2.append("tms.begin();" + EOL);
         var2.append("Transaction tx = tms.getTransaction();" + EOL);
         var2.append("try { " + EOL);
      }

      var2.append(this.fieldVarForField() + " = (" + this.classNameForField() + ")" + this.finderInvokerForField() + "." + this.findByPrimaryKeyForField() + "(" + EOL);
      var2.append(this.finderParamForField() + this.fkVarForField() + ");" + EOL);
      if (var1.getConcurrencyStrategy() == 4) {
         var2.append("} finally { " + EOL);
         var2.append("// Dont need to worry for rollback call etc, " + EOL);
         var2.append("// as this is readonly tx and this is used only for a finder call. " + EOL);
         var2.append("tx.commit(); " + EOL);
         var2.append("} " + EOL);
      }

      return var2.toString();
   }

   public String readOnlyResumeTx() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      if (var1.getConcurrencyStrategy() == 4) {
         StringBuffer var2 = new StringBuffer();
         var2.append("TxHelper.getTransactionManager().resume(orgTx);\n");
         return var2.toString();
      } else {
         return "";
      }
   }

   public String preReadOnlyStateChange() {
      return this.bd.isReadOnly() ? "synchronized(this) {" + EOL : "";
   }

   public String postReadOnlyStateChange() {
      return this.bd.isReadOnly() ? "}" + EOL : "";
   }

   public String perhapsUpdateLastLoadTimeDueToEJBStore() {
      EntityBeanInfo var1 = this.bi;
      if (var1.isOptimistic() && var1.getCacheBetweenTransactions()) {
         StringBuffer var2 = new StringBuffer();
         var2.append("else {");
         var2.append(EOL);
         var2.append("  __WL_setLastLoadTime(System.currentTimeMillis());");
         var2.append(EOL);
         var2.append("}");
         var2.append(EOL);
         return var2.toString();
      } else {
         return "";
      }
   }

   public String perhapsUpdateLastLoadTime() {
      EntityBeanInfo var1 = this.bi;
      if (!var1.isReadOnly() && (!var1.isOptimistic() || !var1.getCacheBetweenTransactions())) {
         return "";
      } else {
         StringBuffer var2 = new StringBuffer();
         var2.append("if(!__WL_beanIsLoaded()) {");
         var2.append(EOL);
         var2.append("  __WL_setLastLoadTime(System.currentTimeMillis());");
         var2.append(EOL);
         var2.append("}");
         var2.append(EOL);
         return var2.toString();
      }
   }

   public String isReadOnly() {
      CMPBeanDescriptor var1 = this.bean.getRelatedDescriptor(this.curField);
      return !var1.isReadOnly() && var1.getConcurrencyStrategy() != 4 ? "false" : "true";
   }

   public String declare_bean_interface_methods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.declare_bean_interface_methods());
      ArrayList var2 = new ArrayList();
      var2.addAll(this.bean.getCmrFieldNames());
      var2.removeAll(this.bean.getDeclaredFieldNames());
      var1.append(this.declareCmrVariableGetterMethods(var2));
      var1.append(this.declareCmrVariableSetterMethods(var2));
      var1.append(this.declareRelationshipFinderMethods());
      var1.append(this.declareRelationshipSelectMethods());
      var1.append("public Object ");
      var1.append(this.getPKFromRSMethodName());
      var1.append("Instance(java.sql.ResultSet rs, java.lang.Integer offset, ClassLoader cl)");
      var1.append(EOL);
      var1.append("throws java.sql.SQLException, java.lang.Exception;");
      var1.append(EOL);
      Iterator var3 = this.bean.getFieldGroups().iterator();

      while(var3.hasNext()) {
         this.curGroup = (FieldGroup)var3.next();
         var1.append("public void ");
         var1.append(this.loadGroupFromRSMethodName(this.curGroup));
         var1.append(EOL);
         var1.append("(java.sql.ResultSet rs, java.lang.Integer offset, ");
         var1.append("Object " + this.pkVar() + ", ");
         var1.append(this.getGeneratedBeanInterfaceName());
         var1.append(" var) ");
         var1.append(EOL);
         var1.append("throws java.sql.SQLException, java.lang.Exception;");
         var1.append(EOL);
      }

      Iterator var4 = this.bean.getAllCmrFields().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var1.append("public void " + this.loadCMRFieldFromRSMethodName(var5) + EOL + "(java.sql.ResultSet rs, java.lang.Integer offset, " + this.getGeneratedBeanInterfaceName() + " var) " + EOL);
         var1.append("throws java.sql.SQLException, java.lang.Exception;" + EOL);
      }

      var1.append("public boolean ");
      var1.append(this.existsMethodName());
      var1.append("(Object key);");
      var1.append(EOL);
      var1.append("public boolean __WL_beanIsLoaded();");
      var1.append(EOL);
      Iterator var7 = this.finderList.iterator();

      while(var7.hasNext()) {
         Finder var6 = (Finder)var7.next();
         if (var6.getQueryType() == 0 && var6 instanceof EjbqlFinder) {
            var1.append(MethodUtils.getFinderMethodDeclaration(var6, this.bd.getPrimaryKeyClass()));
            var1.append(";");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   public String declareRelationshipSelectMethods() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.ejbSelectInternalList.iterator();

      while(true) {
         Finder var3;
         do {
            if (!var2.hasNext()) {
               return var1.toString();
            }

            var3 = (Finder)var2.next();
         } while(var3.getQueryType() != 4 && var3.getQueryType() != 2);

         var1.append(MethodUtils.getEjbSelectInternalMethodDeclaration(var3, this.bd.getPrimaryKeyClass()));
         var1.append(";");
         var1.append(EOL);
      }
   }

   public String declareRelationshipFinderMethods() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.bean.getRelationFinders();

      while(var2.hasNext()) {
         Finder var3 = (Finder)var2.next();
         if (var3.getQueryType() == 0) {
            var1.append(MethodUtils.getFinderMethodDeclaration(var3, this.bd.getPrimaryKeyClass()));
            var1.append(";");
            var1.append(EOL);
         }
      }

      return var1.toString();
   }

   public String declareCmrVariableGetterMethods(List var1) throws CodeGenerationException {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.iterator();

      String var5;
      while(var3.hasNext()) {
         this.curField = (String)var3.next();
         Class var4 = this.bean.getCmrFieldClass(this.curField);
         var5 = ClassUtils.classToJavaSourceType(var4);
         var2.append("public ");
         var2.append(var5 + " ");
         var2.append(this.getMethodNameForField());
         var2.append("();");
         var2.append(EOL);
      }

      Iterator var12 = this.bean.getForeignKeyFieldNames().iterator();

      while(true) {
         do {
            do {
               if (!var12.hasNext()) {
                  return var2.toString();
               }

               var5 = (String)var12.next();
            } while(!this.bean.isOneToManyRelation(var5));
         } while(this.bean.isRemoteField(var5));

         this.curField = var5;
         String var6 = this.bean.getTableForCmrField(var5);
         Iterator var7 = this.bean.getForeignKeyColNames(var5).iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            if (!this.bean.hasCmpField(var6, var8)) {
               String var9 = this.bean.variableForField(var5, var6, var8);
               Class var10 = this.bean.getForeignKeyColClass(var5, var8);
               String var11 = ClassUtils.classToJavaSourceType(var10);
               var2.append("public ");
               var2.append(var11 + " ");
               var2.append(MethodUtils.getMethodName(var9));
               var2.append("();" + EOL);
            }
         }

         this.curField = null;
      }
   }

   public String declareCmrVariableSetterMethods(List var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = this.bean.getCmrFieldNames().iterator();

      while(var3.hasNext()) {
         this.curField = (String)var3.next();
         Class var4 = this.bean.getCmrFieldClass(this.curField);
         String var5 = ClassUtils.classToJavaSourceType(var4);
         if (this.bean.isOneToManyRelation(this.curField)) {
            var2.append("public void ");
            var2.append(CodeGenUtils.cacheRelationshipMethodName(this.curField));
            var2.append("(Object obj);");
            if (var1.contains(this.curField)) {
               var2.append("public void ");
               var2.append(this.setMethodNameForField());
               var2.append("(");
               var2.append(var5);
               var2.append(" ");
               var2.append(this.curField);
               var2.append(");");
               var2.append(EOL);
            }

            if (this.bean.getRelatedMultiplicity(this.curField).equals("One")) {
               var2.append("public void ");
               var2.append(this.varPrefix() + this.setMethodNameForField());
               var2.append("(");
               var2.append(var5);
               var2.append(" ");
               var2.append(this.curField);
               var2.append(", boolean ejbStore");
               var2.append(", boolean remove);");
               var2.append(EOL);
            }
         } else if (this.bean.isOneToOneRelation(this.curField)) {
            var2.append("public void ");
            var2.append(this.doSetMethodNameForField());
            var2.append("(");
            var2.append(var5);
            var2.append(" ");
            var2.append(this.curField);
            var2.append(");");
            var2.append(EOL);
            var2.append("public void ");
            var2.append(this.setRestMethodNameForField());
            var2.append("(");
            var2.append(var5);
            var2.append(" ");
            var2.append(this.curField);
            var2.append(", int methodState);");
            var2.append(EOL);
            var2.append("public void ");
            var2.append(MethodUtils.setCmrIsLoadedMethodName(this.curField));
            var2.append("(boolean b);");
            var2.append(EOL);
            var2.append("public void ");
            var2.append(CodeGenUtils.cacheRelationshipMethodName(this.curField));
            var2.append("(Object obj);");
            var2.append(EOL);
         }
      }

      return var2.toString();
   }

   public String doPreparedStmtBindingForClob(String var1, int var2, String var3, boolean var4, boolean var5, boolean var6, String var7) {
      StringBuffer var8 = new StringBuffer();
      var8.append(EOL);
      String var9 = null;
      if (var4) {
         var9 = var3 + "." + var1;
      } else {
         var9 = var3;
      }

      boolean var10000;
      if (var6 && !this.bd.getPrimaryKeyFieldNames().contains(var1)) {
         var10000 = true;
      } else {
         var10000 = false;
      }

      Class var10 = this.getVariableClass(var1);
      if (!var10.isPrimitive()) {
         this.addNullCheck(var8, var9, var1, "" + var2, var7);
      }

      var8.append("    if (" + this.debugEnabled() + ") {" + EOL);
      var8.append("      Debug.say(\"Adding a prepared statement binding: \");" + EOL);
      var8.append("      Debug.say(\"\\t\\tfield = \" +" + var1 + ");" + EOL);
      var8.append("      Debug.say(\"\\t\\tobj = \" +" + var3 + ");" + EOL);
      var8.append("      Debug.say(\"\\t\\tparamIdx = \" +" + var2 + ");" + EOL);
      var8.append("      Debug.say(\"\\t\\tobjIsCompound = \" +" + var4 + ");" + EOL);
      var8.append("    }" + EOL);
      if (debugLogger.isDebugEnabled()) {
         Debug.assertion(var10 != null);
      }

      if (this.bean.isNClobCmpColumnTypeForField(var1)) {
         var8.append("    if(" + var7 + " instanceof oracle.jdbc.OraclePreparedStatement) {" + EOL);
         var8.append("      ((oracle.jdbc.OraclePreparedStatement)" + var7 + ").setFormOfUse(" + var2 + ", oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);" + EOL);
         var8.append("    }" + EOL);
      }

      var8.append("      java.lang.reflect.Method[] meths = " + var7 + ".getClass().getMethods();\n");
      var8.append("      java.lang.reflect.Method meth = null;\n");
      var8.append("      for (int i = 0; i < meths.length; i++) {\n");
      var8.append("        if (meths[i].getName().equalsIgnoreCase(\"setStringForClob\"))\n");
      var8.append("        {\n" + EOL);
      var8.append("          meth = meths[i];\n");
      var8.append("          break;\n");
      var8.append("        }\n");
      var8.append("      }\n");
      var8.append("      if (meth != null) {\n");
      var8.append("        meth.invoke(" + var7 + ", new Object[]{ Integer.valueOf(\"" + var2 + "\") , " + var9 + " });\n");
      var8.append("      }\n");
      if (!var10.isPrimitive()) {
         var8.append("}" + EOL);
      }

      return var8.toString();
   }

   public String perhapsDeclareQueryCachingVars() {
      StringBuffer var1 = new StringBuffer();
      if (this.shouldImplementQueryCaching(this.curFinder)) {
         var1.append("QueryCacheKey ").append(this.queryCacheKeyVar());
         var1.append(" = new QueryCacheKey(\"");
         var1.append(this.curFinder.getFinderIndex()).append("\", new Object[] {");
         var1.append(this.getParametersAsArray(this.curFinder)).append("}, ");
         var1.append("(TTLManager)").append(this.pmVar()).append(".getBeanManager(), ");
         if (Collection.class.isAssignableFrom(this.curFinder.getReturnClassType())) {
            var1.append("QueryCacheKey.RET_TYPE_COLLECTION);").append(EOL);
         } else {
            var1.append("QueryCacheKey.RET_TYPE_SINGLETON);").append(EOL);
         }

         if (this.curFinder.isMultiFinder()) {
            var1.append("    Collection ").append(this.queryCacheElementsVar());
            var1.append(" = new ArrayList();").append(EOL);
         } else {
            var1.append("    QueryCacheElement ").append(this.queryCacheElementsVar());
            var1.append(" = null;").append(EOL);
         }
      }

      boolean var2 = this.isRelationshipCaching(this.curFinder);
      if (var2) {
         var1.append("MultiMap ").append(this.beanMapVar()).append(" = ");
         var1.append("null;").append(EOL);
      }

      return var1.toString();
   }

   public String perhapsPutInQueryCache() {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = this.isRelationshipCaching(this.curFinder);
      if (this.currFinderLoadsQueryCachingEnabledCMRFields) {
         var1.append("Iterator iterator = ").append(this.beanMapVar());
         var1.append(".keySet().iterator();").append(EOL);
         var1.append("while (iterator.hasNext()) {").append(EOL);
         var1.append("CMPBean __WL_relBean = (CMPBean)iterator.next();").append(EOL);
         var1.append("List list = ").append(this.beanMapVar()).append(".get(__WL_relBean);");
         var1.append(EOL);
         var1.append("for (int i=0; i<list.size(); i++) {").append(EOL);
         var1.append("__WL_relBean.").append(this.cmrFieldQueryCachingMethodName());
         if (this.shouldImplementQueryCaching(this.curFinder)) {
            var1.append("((String)list.get(i), ").append(this.queryCacheKeyVar());
         } else {
            var1.append("((String)list.get(i), null");
         }

         var1.append(");").append(EOL);
         var1.append("}").append(EOL);
         var1.append("}").append(EOL);
      }

      if (this.shouldImplementQueryCaching(this.curFinder)) {
         var1.append("((TTLManager)").append(this.pmVar()).append(".getBeanManager())");
         var1.append(".putInQueryCache(").append(this.queryCacheKeyVar());
         var1.append(", ").append(this.queryCacheElementsVar()).append(");").append(EOL);
      }

      this.currFinderLoadsQueryCachingEnabledCMRFields = false;
      return var1.toString();
   }

   private String queryCacheKeyVar() {
      return this.varPrefix() + "qckey";
   }

   private String queryCacheElementVar() {
      return this.varPrefix() + "querycacheelement";
   }

   private String queryCacheElementsVar() {
      return this.varPrefix() + "qcelements";
   }

   private String cmrFieldVar(String var1) {
      return this.varPrefix() + var1 + "_field_";
   }

   private String beanMapVar() {
      return this.varPrefix() + "beanMap";
   }

   private boolean shouldImplementQueryCaching(Finder var1) {
      return !var1.isFindByPrimaryKey() ? var1.isQueryCachingEnabled() : false;
   }

   private String getParametersAsArray(Finder var1) {
      Class[] var2 = var1.getParameterClassTypes();
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var4 > 0) {
            var3.append(", ");
         }

         if (var2[var4].equals(Boolean.TYPE)) {
            var3.append("new Boolean(param").append(var4).append(")");
         } else if (var2[var4].equals(Character.TYPE)) {
            var3.append("new Character(param").append(var4).append(")");
         } else if (var2[var4].equals(Byte.TYPE)) {
            var3.append("new Byte(param").append(var4).append(")");
         } else if (var2[var4].equals(Short.TYPE)) {
            var3.append("new Short(param").append(var4).append(")");
         } else if (var2[var4].equals(Integer.TYPE)) {
            var3.append("new Integer(param").append(var4).append(")");
         } else if (var2[var4].equals(Long.TYPE)) {
            var3.append("new Long(param").append(var4).append(")");
         } else if (var2[var4].equals(Float.TYPE)) {
            var3.append("new Float(param").append(var4).append(")");
         } else if (var2[var4].equals(Double.TYPE)) {
            var3.append("new Double(param").append(var4).append(")");
         } else {
            var3.append("param").append(var4);
         }
      }

      return var3.toString();
   }

   private String generateCMRFieldFinderMethodName(String var1) {
      return CodeGenUtils.getCMRFieldFinderMethodName(this.bean, var1);
   }

   private boolean isRelationshipCaching(Finder var1) {
      if (var1 instanceof EjbqlFinder) {
         EjbqlFinder var2 = (EjbqlFinder)var1;
         String var3 = var2.getCachingName();
         if (var3 != null) {
            RelationshipCaching var4 = this.bean.getRelationshipCaching(var3);
            if (var4 != null) {
               return var4.getCachingElements().iterator().hasNext();
            }
         }
      }

      return false;
   }

   private static void debug(String var0) {
      debugLogger.debug("[RDBMSCodeGenerator] " + var0);
   }

   private void p(String var1) {
      System.err.println("\n" + var1 + "\n");
   }
}
