package weblogic.ejb.container.ejbc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.ejb.EntityContext;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.codegen.MethodSignature;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.PlatformConstants;
import weblogic.utils.annotation.BeaSynthetic.Helper;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.reflect.MethodText;

public abstract class EjbCodeGenerator extends CodeGenerator {
   private static final String EJB_HOME = "ejbHome";
   private static final boolean DEBUG = false;
   private boolean keepgenerated = false;
   private Getopt2 opts = null;
   protected final List<Output> generatedOutputs = new LinkedList();
   protected Output currentOutput;
   protected Method[] beanHomeMethods;
   protected Method[] remoteMethods;
   protected Method[] createMethods;
   protected Method[] findMethods;
   protected Method[] homeMethods;
   protected Method[] localMethods;
   protected Method[] localCreateMethods;
   protected Method[] localFindMethods;
   protected Method[] localHomeMethods;
   protected Method method;
   protected MethodSignature methodSignature;
   private MethodText mt = new MethodText();
   protected EntityBeanInfo bi;
   protected NamingConvention nc;
   protected Class ejbClass;
   protected Class homeInterfaceClass;
   protected Class remoteInterfaceClass;
   protected Class localHomeInterfaceClass;
   protected Class localInterfaceClass;
   protected Class primaryKeyClass;
   protected boolean isContainerManagedBean;
   protected boolean isCompoundPK;
   protected boolean hasLocalClientView;
   protected boolean hasRemoteClientView;
   protected boolean hasDeclaredRemoteHome;
   protected boolean hasDeclaredLocalHome;
   protected short methodType = 0;
   private Set<Method> cmpGetterSetterMethods;

   public EjbCodeGenerator() {
   }

   public EjbCodeGenerator(Getopt2 var1) {
      super(var1);
      this.opts = var1;
      this.keepgenerated = var1.hasOption("keepgenerated");
   }

   protected Enumeration outputs(List var1) throws Exception {
      this.generatedOutputs.clear();
      Vector var2 = new Vector();
      BeanInfo var3 = (BeanInfo)var1.get(0);
      NamingConvention var4 = new NamingConvention(var3.getBeanClassName(), var3.getEJBName());
      this.addOutputs(var2, var3, var4);
      return var2.elements();
   }

   protected final void interpretBeanInfo(BeanInfo var1) throws EJBCException {
      EntityBeanInfo var2 = (EntityBeanInfo)var1;
      this.setBooleans(var2);
      this.setClasses(var2);
      this.setBeanHomeMethods(var2);
      if (this.hasDeclaredRemoteHome) {
         this.remoteMethods = EJBMethodsUtil.getRemoteMethods(this.remoteInterfaceClass, true);
         this.createMethods = EJBMethodsUtil.getCreateMethods(this.homeInterfaceClass);
         this.findMethods = EJBMethodsUtil.getFindMethods(this.homeInterfaceClass);
         this.setHomeMethods(var2);
      }

      if (this.hasDeclaredLocalHome) {
         this.localMethods = EJBMethodsUtil.getLocalMethods(this.localInterfaceClass, true);
         this.localCreateMethods = EJBMethodsUtil.getLocalCreateMethods(this.localHomeInterfaceClass);
         this.localFindMethods = EJBMethodsUtil.getLocalFindMethods(this.localHomeInterfaceClass);
         this.setLocalHomeMethods(var2);
         if (!var2.getIsBeanManagedPersistence() && var2.getCMPInfo().uses20CMP()) {
            this.setCmpGettersAndSetters(var2, this.localInterfaceClass);
         }
      }

   }

   protected void prepare(CodeGenerator.Output var1) throws EJBCException, ClassNotFoundException {
      this.currentOutput = (Output)var1;
      this.generatedOutputs.add(this.currentOutput);
      if (this.currentOutput.getBeanInfo() == null) {
         this.nc = this.currentOutput.getNamingConvention();
      } else if (this.bi != this.currentOutput.getBeanInfo()) {
         this.bi = (EntityBeanInfo)this.currentOutput.getBeanInfo();
         this.nc = this.currentOutput.getNamingConvention();
         this.interpretBeanInfo(this.bi);
      }
   }

   private void setBooleans(EntityBeanInfo var1) {
      this.hasRemoteClientView = false;
      this.hasLocalClientView = false;
      this.hasDeclaredRemoteHome = false;
      this.hasDeclaredLocalHome = false;
      CMPInfo var2 = var1.getCMPInfo();
      this.isContainerManagedBean = var2 != null;
      this.isCompoundPK = this.isContainerManagedBean && var2.getCMPrimaryKeyFieldName() == null;
      this.hasRemoteClientView = var1.hasRemoteClientView();
      this.hasLocalClientView = var1.hasLocalClientView();
      this.hasDeclaredRemoteHome = var1.hasDeclaredRemoteHome();
      this.hasDeclaredLocalHome = var1.hasDeclaredLocalHome();
   }

   private final void setClasses(EntityBeanInfo var1) throws EJBCException {
      this.ejbClass = var1.getBeanClass();
      if (this.hasLocalClientView) {
         this.localInterfaceClass = var1.getLocalInterfaceClass();
         this.localHomeInterfaceClass = var1.getLocalHomeInterfaceClass();
      }

      if (this.hasRemoteClientView) {
         this.remoteInterfaceClass = var1.getRemoteInterfaceClass();
         this.homeInterfaceClass = var1.getHomeInterfaceClass();
      }

      this.primaryKeyClass = var1.getPrimaryKeyClass();
      if (var1.isUnknownPrimaryKey() && !var1.getIsBeanManagedPersistence()) {
         CMPBeanDescriptor var2 = var1.getCMPInfo().getCMPBeanDescriptor(var1.getEJBName());
         this.primaryKeyClass = var2.getPrimaryKeyClass();
      }

   }

   private void setCmpGettersAndSetters(EntityBeanInfo var1, Class var2) {
      CMPInfo var3 = var1.getCMPInfo();
      CMPBeanDescriptor var4 = (CMPBeanDescriptor)var3.getBeanMap().get(var1.getEJBName());
      Debug.assertion(var4 != null);
      this.cmpGetterSetterMethods = new HashSet();
      Iterator var5 = var1.getCMPInfo().getAllContainerManagedFieldNames().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Method var7 = var4.getGetterMethod(var2, var6);
         Method var8 = var4.getSetterMethod(var2, var6);
         if (var7 != null) {
            this.cmpGetterSetterMethods.add(var7);
         }

         if (var8 != null) {
            this.cmpGetterSetterMethods.add(var8);
         }
      }

   }

   private void setBeanHomeMethods(BeanInfo var1) {
      ArrayList var2 = new ArrayList();
      Method[] var3 = var1.getBeanClass().getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Method var5 = var3[var4];
         String var6 = var5.getName();
         if (var6.startsWith("ejbHome") && !Modifier.isVolatile(var5.getModifiers())) {
            var2.add(var5);
         }
      }

      if (var2.size() > 0) {
         this.beanHomeMethods = (Method[])var2.toArray(new Method[var2.size()]);
      } else {
         this.beanHomeMethods = null;
      }

   }

   private void setHomeMethods(BeanInfo var1) {
      if (this.beanHomeMethods != null) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < this.beanHomeMethods.length; ++var3) {
            Method var4 = this.beanHomeMethods[var3];
            String var5 = var4.getName();
            char var6 = var5.charAt("ejbHome".length());
            String var7 = Character.toLowerCase(var6) + var5.substring("ejbHome".length() + 1);

            try {
               Method var8 = this.homeInterfaceClass.getMethod(var7, var4.getParameterTypes());
               var2.add(var8);
            } catch (NoSuchMethodException var13) {
               MethodSignature var9 = new MethodSignature(var4, var1.getBeanClass());
               Method[] var10 = this.homeInterfaceClass.getMethods();

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  MethodSignature var12 = new MethodSignature(var10[var11], this.homeInterfaceClass);
                  if (MethodSignature.equalsMethodsBySig(var9, var12)) {
                     var2.add(var10[var11]);
                  }
               }
            }
         }

         if (var2.size() > 0) {
            this.homeMethods = (Method[])var2.toArray(new Method[var2.size()]);
         } else {
            this.homeMethods = null;
         }
      } else {
         this.homeMethods = null;
      }

   }

   private void setLocalHomeMethods(BeanInfo var1) {
      if (this.beanHomeMethods != null) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < this.beanHomeMethods.length; ++var3) {
            Method var4 = this.beanHomeMethods[var3];
            String var5 = var4.getName();
            char var6 = var5.charAt("ejbHome".length());
            String var7 = Character.toLowerCase(var6) + var5.substring("ejbHome".length() + 1);

            try {
               Method var8 = this.localHomeInterfaceClass.getMethod(var7, var4.getParameterTypes());
               var2.add(var8);
            } catch (NoSuchMethodException var13) {
               MethodSignature var9 = new MethodSignature(var4, var1.getBeanClass());
               Method[] var10 = this.localHomeInterfaceClass.getMethods();

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  MethodSignature var12 = new MethodSignature(var10[var11], this.localHomeInterfaceClass);
                  if (MethodSignature.equalsMethodsBySig(var9, var12)) {
                     var2.add(var10[var11]);
                  }
               }
            }
         }

         if (var2.size() > 0) {
            this.localHomeMethods = (Method[])var2.toArray(new Method[var2.size()]);
         } else {
            this.localHomeMethods = null;
         }
      } else {
         this.localHomeMethods = null;
      }

   }

   protected abstract void addOutputs(Vector var1, BeanInfo var2, NamingConvention var3) throws EJBCException;

   public String ejb_callbacks() throws CodeGenerationException {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.parse(this.getProductionRule("common_ejb_callbacks")));
      var1.append(this.parse(this.getProductionRule("entity_callbacks")));
      return var1.toString();
   }

   public String method_state() {
      String var1 = this.method.getName();
      if (var1.startsWith("ejbCreate")) {
         return "STATE_EJB_CREATE";
      } else if (var1.startsWith("ejbPostCreate")) {
         return "STATE_EJB_POSTCREATE";
      } else if (var1.startsWith("ejbFind")) {
         return "STATE_EJBFIND";
      } else if (var1.startsWith("ejbHome")) {
         return "STATE_EJBHOME";
      } else {
         return var1.startsWith("ejbTimeout") ? "STATE_EJBTIMEOUT" : "STATE_BUSINESS_METHOD";
      }
   }

   public String bean_postcreate_methods() throws CodeGenerationException {
      Method[] var1 = this.ejbClass.getMethods();
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3].getName();
         if (var4.startsWith("ejbPostCreate")) {
            this.setMethod(var1[var3], (short)0);
            var2.append(this.parse(this.getProductionRule("home_method")));
         }
      }

      return var2.toString();
   }

   public String pre_home_invoke_method() {
      return "preEntityHomeInvoke";
   }

   public String post_home_invoke_method() {
      return "postEntityHomeInvoke(wrap, ee);";
   }

   public String bean_home_methods() throws CodeGenerationException {
      StringBuilder var1 = new StringBuilder();
      Method[] var2 = EJBMethodsUtil.getBeanHomeClassMethods(this.ejbClass);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.setMethod(var2[var3], (short)0, (Class)this.ejbClass);
         var1.append(this.parse(this.getProductionRule("home_method")));
      }

      return var1.toString();
   }

   public String local_preInvoke() throws CodeGenerationException {
      return this.parse(this.getProductionRule("localPreInvoke"));
   }

   public String local_postInvokeTxRetry() throws CodeGenerationException {
      return this.parse(this.getProductionRule("localPostInvokeTxRetry"));
   }

   public String local_postInvokeCleanup() throws CodeGenerationException {
      return this.parse(this.getProductionRule("localPostInvokeCleanup"));
   }

   public String beanmethod_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod(this.method.getName(), this.method.getParameterTypes());
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         throw new AssertionError(this.method + " method" + " missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
      }
   }

   public String setentitycontext_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("setEntityContext", EntityContext.class);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("setEntityContext(EntityContext) method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String unsetentitycontext_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("unsetEntityContext", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("unsetEntityContext() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String ejbload_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("ejbLoad", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("ejbLoad() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String ejbstore_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("ejbStore", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("ejbStore() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String activate_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("ejbActivate", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("ejbActivate() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String passivate_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("ejbPassivate", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("ejbPassivate() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String remove_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("ejbRemove", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         if (!this.bi.isEJB30()) {
            throw new AssertionError("ejbRemove() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
         } else {
            return "";
         }
      }
   }

   public String afterbegin_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("afterBegin", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("afterBegin() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
      }
   }

   public String beforecompletion_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("beforeCompletion", (Class[])null);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("beforeCompletion() method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
      }
   }

   public String aftercompletion_throws_clause() {
      try {
         Method var1 = this.ejbClass.getMethod("afterCompletion", Boolean.TYPE);
         return this.method_throws_clause(var1);
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("afterCompletion(boolean) method missing in bean class: " + this.ejbClass.getName() + " exception: " + var2);
      }
   }

   public String method_throws_clause(Method var1) {
      Class[] var2 = var1.getExceptionTypes();
      if (var2 != null && var2.length != 0) {
         StringBuilder var3 = new StringBuilder(200);
         var3.append("throws ");

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var4 != 0) {
               var3.append(",");
            }

            var3.append(this.arg_type(var2[var4]));
         }

         return var3.toString();
      } else {
         return "";
      }
   }

   public String create_methods() throws CodeGenerationException {
      if (this.createMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.createMethods.length; ++var2) {
            this.setMethod(this.createMethods[var2], (short)1);
            var1.append(this.parse(this.getProductionRule("create_method_en")));
         }

         return var1.toString();
      }
   }

   public String local_create_methods() throws CodeGenerationException {
      if (this.localCreateMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.localCreateMethods.length; ++var2) {
            this.setMethod(this.localCreateMethods[var2], (short)1, (Class)this.localHomeInterfaceClass);
            var1.append(this.parse(this.getProductionRule("create_method_en")));
         }

         return var1.toString();
      }
   }

   public String optional_finder_type() throws CodeGenerationException {
      if (this.method.getName().equals("findByPrimaryKey")) {
         return "";
      } else if (this.method.getReturnType().equals(this.remoteInterfaceClass)) {
         return ", weblogic.ejb.container.internal.EntityEJBHome.SCALAR_FINDER";
      } else if (this.method.getReturnType().equals(this.localInterfaceClass)) {
         return ", weblogic.ejb.container.internal.EntityEJBHome.SCALAR_FINDER";
      } else if (this.method.getReturnType().equals(Collection.class)) {
         return ", weblogic.ejb.container.internal.EntityEJBHome.COLL_FINDER";
      } else if (this.method.getReturnType().equals(Enumeration.class)) {
         return ", weblogic.ejb.container.internal.EntityEJBHome.ENUM_FINDER";
      } else {
         throw new AssertionError("Unrecognized finder, return type is :" + this.method.getReturnType());
      }
   }

   public String finder_name() throws CodeGenerationException {
      if (this.method.getName().equals("findByPrimaryKey")) {
         return "findByPrimaryKey";
      } else if (this.method.getReturnType().equals(this.remoteInterfaceClass)) {
         return "finder";
      } else if (this.method.getReturnType().equals(this.localInterfaceClass)) {
         return "finder";
      } else if (this.method.getReturnType().equals(Collection.class)) {
         return "finder";
      } else if (this.method.getReturnType().equals(Enumeration.class)) {
         return "finder";
      } else {
         throw new AssertionError("Unrecognized finder, return type is :" + this.method.getReturnType());
      }
   }

   public String finder_parameters() throws CodeGenerationException {
      return this.method.getName().equals("findByPrimaryKey") ? this.wrapped_method_parameters_without_types() : this.method_parameters_in_array();
   }

   public String find_methods() throws CodeGenerationException {
      if (this.findMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.findMethods.length; ++var2) {
            Method var3 = this.findMethods[var2];
            this.setMethod(var3, (short)1);
            var1.append(this.parse(this.getProductionRule("finder")));
         }

         return var1.toString();
      }
   }

   public String local_find_methods() throws CodeGenerationException {
      if (this.localFindMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.localFindMethods.length; ++var2) {
            Method var3 = this.localFindMethods[var2];
            this.setMethod(var3, (short)1, (Class)this.localHomeInterfaceClass);
            var1.append(this.parse(this.getProductionRule("finder")));
         }

         return var1.toString();
      }
   }

   public String home_methods() throws CodeGenerationException {
      if (this.homeMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.homeMethods.length; ++var2) {
            Method var3 = this.homeMethods[var2];
            this.setMethod(var3, (short)1);
            MethodSignature var4 = new MethodSignature(var3);
            var4.setModifiers(1);
            var1.append(var4 + "{" + EOL);
            var1.append(this.parse(this.getProductionRule("home_method_body")));
            var1.append(EOL + "}" + EOL);
         }

         return var1.toString();
      }
   }

   public String local_home_methods() throws CodeGenerationException {
      if (this.localHomeMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.localHomeMethods.length; ++var2) {
            Method var3 = this.localHomeMethods[var2];
            this.setMethod(var3, (short)1);
            MethodSignature var4 = new MethodSignature(var3);
            var4.setModifiers(1);
            var1.append(var4 + "{" + EOL);
            var1.append(this.parse(this.getProductionRule("home_method_body")));
            var1.append(EOL + "}" + EOL);
         }

         return var1.toString();
      }
   }

   public String declare_result() {
      Class var1 = this.method.getReturnType();
      if (var1.getName().equals("void")) {
         return "// No return value";
      } else {
         StringBuilder var2 = new StringBuilder(50);
         String var3 = this.methodSignature.getReturnTypeName();
         Class var4 = ClassUtils.getPrimitiveClass(var3);
         if (var4 == null && var3 != null) {
            try {
               var4 = Class.forName(var3, false, this.getClass().getClassLoader());
            } catch (ClassNotFoundException var6) {
            }
         }

         if (var4 != null) {
            var2.append(this.return_type(var4) + " result = ");
            if (var4.isPrimitive()) {
               if (var4.getName().equals("boolean")) {
                  var2.append("false;");
               } else {
                  var2.append("0;");
               }
            } else {
               var2.append("null;");
            }
         } else {
            var2.append(var3 + " result = null;");
         }

         return var2.toString();
      }
   }

   public String ejb_class_name() {
      return this.ejbClass.getName();
   }

   public String enum_exceptions() {
      StringBuilder var1 = new StringBuilder(80);
      Set var2 = this.bi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         var1.append("else if (e instanceof " + this.arg_type(var4) + ") {" + EOL);
         var1.append("   throw (" + this.arg_type(var4) + ") e;" + EOL + "}");
      }

      Class[] var7 = this.method.getExceptionTypes();
      if (var7.length == 0) {
         return var1.toString();
      } else {
         for(int var5 = 0; var5 < var7.length; ++var5) {
            Class var6 = var7[var5];
            if (!RemoteException.class.isAssignableFrom(var6) && !var2.contains(var6)) {
               var1.append("else if (e instanceof " + this.arg_type(var6) + ") {" + EOL);
               var1.append("   throw (" + this.arg_type(var6) + ") e;" + EOL + "}");
            }
         }

         return var1.toString();
      }
   }

   public String enum_exceptions_home_method() {
      Class[] var1 = this.method.getExceptionTypes();
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(80);
         boolean var3 = false;

         for(int var4 = 0; var4 < var1.length; ++var4) {
            Class var5 = var1[var4];
            if (!var5.isAssignableFrom(RemoteException.class)) {
               if (var3) {
                  var2.append("else ");
               }

               var2.append("if (ee instanceof " + this.arg_type(var5) + ") {" + EOL);
               var2.append("   throw (" + this.arg_type(var5) + ") ee;" + EOL + "}");
               var3 = true;
            }
         }

         return var2.toString();
      }
   }

   public String entity() {
      return "true";
   }

   public String extends_type() {
      return "weblogic.ejb.container.internal.EntityEJBObject_Activatable";
   }

   public String extends_local_type() {
      return "weblogic.ejb.container.internal.EntityEJBLocalObject";
   }

   public String find_method_throws_clause() {
      return this.exceptions(this.method);
   }

   public String home_class_name() {
      return this.nc.getHomeClassName();
   }

   public String home_interface_name() {
      return this.convertToLegalInnerClassName(this.homeInterfaceClass);
   }

   public String local_home_interface_name() {
      return this.convertToLegalInnerClassName(this.localHomeInterfaceClass);
   }

   public String create_method_name() {
      return "ejbC" + this.method.getName().substring(1);
   }

   public String postCreate_method_name() {
      return this.postCreate_method_name(this.method);
   }

   public String postCreate_method_name(Method var1) {
      String var2 = var1.getName();
      return "ejbPostC" + var2.substring(1, var2.length());
   }

   public String ejbCreate_method_name(Method var1) {
      String var2 = var1.getName();
      return "ejbC" + var2.substring(1, var2.length());
   }

   public String method_return() {
      return this.arg_type(this.method.getReturnType());
   }

   public String capitalized_method_name() {
      String var1 = this.method.getName();
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public String method_name() {
      return this.method.getName();
   }

   public String method_parameters_in_array() {
      StringBuilder var1 = new StringBuilder();
      var1.append("new Object [] { ");
      var1.append(this.wrapped_method_parameters_without_types());
      var1.append("}");
      return var1.toString();
   }

   public String method_parameters() {
      this.mt.setOptions(513);
      return this.mt.toString();
   }

   private String classToStringForm(Class var1) {
      if (var1.isArray()) {
         String var2 = this.classToStringForm(var1.getComponentType());
         return var2 + "[]";
      } else {
         return this.arg_type(var1);
      }
   }

   public String method_types_as_array() {
      Debug.assertion(this.method != null);
      Class[] var1 = this.method.getParameterTypes();
      if (var1 != null && var1.length != 0) {
         StringBuilder var2 = new StringBuilder();
         var2.append("new Class [] {");

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = this.classToStringForm(var1[var3]) + ".class";
            if (var3 == 0) {
               var2.append(var4);
            } else {
               var2.append(", " + var4);
            }
         }

         var2.append("}");
         return var2.toString();
      } else {
         return "new Class [] {}";
      }
   }

   public String method_parameters_without_types() {
      this.mt.setOptions(1);
      return this.mt.toString();
   }

   public String wrapped_method_parameters_without_types() {
      this.mt.setOptions(1024);
      return this.mt.toString();
   }

   public String method_sig() {
      this.mt.setOptions(128);
      String var1 = this.mt.toString();
      if (this.methodType == 1) {
         var1 = EJBMethodsUtil.homeClassMethodNameMapper(var1);
      }

      return var1;
   }

   public String method_signature_no_throws() {
      this.methodSignature.setAbstract(false);
      this.methodSignature.setPrintThrowsClause(false);
      return this.methodSignature.toString();
   }

   public String method_signature() {
      this.methodSignature.setAbstract(false);
      return this.methodSignature.toString();
   }

   public String method_signature_throws_remote_exception() {
      this.methodSignature.setAbstract(false);
      Class[] var1 = this.methodSignature.getExceptionTypes();
      Class[] var2 = new Class[var1.length + 1];
      boolean var3 = false;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2[var4] = var1[var4];
         if (var1[var4].equals(RemoteException.class)) {
            var3 = true;
         }
      }

      if (var3) {
         this.methodSignature.setExceptionTypes(var1);
      } else {
         var2[var1.length] = RemoteException.class;
         this.methodSignature.setExceptionTypes(var2);
      }

      return this.methodSignature.toString();
   }

   public String ctor_throws_clause() {
      Constructor[] var1 = this.ejbClass.getConstructors();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         Class[] var3 = var1[var2].getParameterTypes();
         if (var3 == null || var3.length == 0) {
            return this.exceptions(var1[var2].getExceptionTypes());
         }
      }

      throw new AssertionError("Bean class: " + this.ejbClass + " did not have a no-arg constructor.  The EJB Compliance " + "Checker should have caught this.");
   }

   public String method_throws_clause() {
      return this.exceptions(this.method);
   }

   public String pk_class() {
      return this.primaryKeyClass.getName();
   }

   public String declare_static_eo_method_descriptors() throws CodeGenerationException {
      return this.declare_method_descriptors(this.remoteMethods, (short)0, true);
   }

   public String declare_static_elo_method_descriptors() throws CodeGenerationException {
      return this.declare_method_descriptors(this.localMethods, (short)0, true);
   }

   public String declare_create_method_descriptors() throws CodeGenerationException {
      return this.declare_method_descriptors(this.createMethods, (short)1);
   }

   public String declare_local_create_method_descriptors() throws CodeGenerationException {
      return this.declare_method_descriptors(this.localCreateMethods, (short)1);
   }

   public String declare_find_method_descriptors() throws CodeGenerationException {
      return this.declare_method_descriptors(this.findMethods, (short)1);
   }

   public String declare_local_find_method_descriptors() throws CodeGenerationException {
      return this.declare_method_descriptors(this.localFindMethods, (short)1);
   }

   private String declare_method_descriptors(Method[] var1, short var2) {
      return this.declare_method_descriptors(var1, var2, false);
   }

   private String declare_method_descriptors(Method[] var1, short var2, boolean var3) {
      if (var1 == null) {
         return "";
      } else {
         StringBuilder var4 = new StringBuilder(200);

         for(int var5 = 0; var5 < var1.length; ++var5) {
            this.setMethod(var1[var5], var2);
            this.declare_method_descriptor(var3, var4);
         }

         return var4.toString();
      }
   }

   public String declare_create_methods() throws CodeGenerationException {
      if (this.createMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.createMethods.length; ++var2) {
            Method var3 = this.createMethods[var2];
            this.setMethod(var3, (short)1);
            var1.append(this.parse(this.getProductionRule("declare_create_method")));
            var1.append(this.parse(this.getProductionRule("declare_postCreate_method")));
         }

         return var1.toString();
      }
   }

   public String declare_local_create_methods() throws CodeGenerationException {
      if (this.localCreateMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.localCreateMethods.length; ++var2) {
            Method var3 = this.localCreateMethods[var2];
            this.setMethod(var3, (short)1, (Class)this.localHomeInterfaceClass);
            var1.append(this.parse(this.getProductionRule("declare_create_method")));
            var1.append(this.parse(this.getProductionRule("declare_postCreate_method")));
         }

         return var1.toString();
      }
   }

   public String declare_home_method_descriptors() throws CodeGenerationException {
      if (this.homeMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.homeMethods.length; ++var2) {
            Method var3 = this.homeMethods[var2];
            this.setMethod(var3, (short)1);
            var1.append(this.parse(this.getProductionRule("declare_home_method_descriptor")));
         }

         return var1.toString();
      }
   }

   public String declare_local_home_method_descriptors() throws CodeGenerationException {
      if (this.localHomeMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.localHomeMethods.length; ++var2) {
            Method var3 = this.localHomeMethods[var2];
            this.setMethod(var3, (short)1);
            var1.append(this.parse(this.getProductionRule("declare_home_method_descriptor")));
         }

         return var1.toString();
      }
   }

   private void declare_method_descriptor(boolean var1, StringBuilder var2) {
      var2.append(" public");
      if (var1) {
         var2.append(" static");
      }

      var2.append(" weblogic.ejb.container.internal.MethodDescriptor ");
      var2.append(EJBMethodsUtil.methodDescriptorPrefix(this.methodType));
      var2.append(this.method_sig());
      var2.append(";" + PlatformConstants.EOL);
   }

   public String initialize_create_methods() throws CodeGenerationException {
      if (this.createMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.createMethods.length; ++var2) {
            this.setMethod(this.createMethods[var2], (short)1);
            var1.append(this.parse(this.getProductionRule("initialize_create_method")) + EOL);
            var1.append(this.parse(this.getProductionRule("initialize_postCreate_method")) + EOL);
         }

         return var1.toString();
      }
   }

   public String initialize_local_create_methods() throws CodeGenerationException {
      if (this.localCreateMethods == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(200);

         for(int var2 = 0; var2 < this.localCreateMethods.length; ++var2) {
            this.setMethod(this.localCreateMethods[var2], (short)1, (Class)this.localHomeInterfaceClass);
            var1.append(this.parse(this.getProductionRule("initialize_create_method")) + EOL);
            var1.append(this.parse(this.getProductionRule("initialize_postCreate_method")) + EOL);
         }

         return var1.toString();
      }
   }

   public String perhaps_initialize_create_methods() throws CodeGenerationException {
      return this.parse(this.getProductionRule("static_block_to_init_create_methods"));
   }

   public String perhaps_initialize_local_create_methods() throws CodeGenerationException {
      return this.parse(this.getProductionRule("static_block_to_init_create_methods"));
   }

   private List<Method> getAllMethods(Class var1) {
      Method[] var2 = var1.getMethods();
      ArrayList var3 = new ArrayList(Arrays.asList(var2));
      Iterator var4 = var3.iterator();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      while(true) {
         Method var7;
         do {
            do {
               if (!var4.hasNext()) {
                  var3.removeAll(var5);
                  var6.addAll(var3);
                  return var6;
               }

               var7 = (Method)var4.next();
            } while(!Modifier.isVolatile(var7.getModifiers()));
         } while(!var7.toGenericString().equals(var7.toString()));

         for(Class var8 = var1.getSuperclass(); !var8.equals(Object.class); var8 = var8.getSuperclass()) {
            if (!Modifier.isPublic(var8.getModifiers())) {
               try {
                  Method var9 = var8.getMethod(var7.getName(), var7.getParameterTypes());
                  if (!Modifier.isVolatile(var9.getModifiers())) {
                     var5.add(var7);
                     var6.add(var9);
                     break;
                  }
               } catch (NoSuchMethodException var10) {
               }
            }
         }
      }
   }

   public String declare_bean_interface_methods() throws CodeGenerationException {
      StringBuilder var1 = new StringBuilder(200);
      Class var2 = this.bi.getBeanClass();
      List var3 = this.getAllMethods(this.bi.getBeanClass());
      ArrayList var4 = new ArrayList();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Method var6 = (Method)var5.next();
         if (!var6.isBridge()) {
            var4.add(var6);
         }
      }

      Comparator var10 = new Comparator<Method>() {
         public int compare(Method var1, Method var2) {
            if (!var1.getName().equals(var2.getName())) {
               return var1.getName().compareTo(var2.getName());
            } else {
               Class[] var3 = var1.getParameterTypes();
               Class[] var4 = var2.getParameterTypes();
               if (var3.length != var4.length) {
                  return var3.length < var4.length ? -1 : 1;
               } else {
                  for(int var5 = 0; var5 < var3.length; ++var5) {
                     if (var3[var5] != var4[var5]) {
                        return var3[var5].getName().compareTo(var4[var5].getName());
                     }
                  }

                  if (var1.isBridge() || var2.isBridge()) {
                     Class var7 = var1.getReturnType();
                     Class var6 = var2.getReturnType();
                     if (!var7.equals(var6)) {
                        return var7.getName().compareTo(var6.getName());
                     }
                  }

                  return 0;
               }
            }
         }
      };
      TreeSet var11 = new TreeSet(var10);
      var11.addAll(var4);
      Iterator var7 = var11.iterator();

      while(var7.hasNext()) {
         Method var8 = (Method)var7.next();
         if (!var8.isSynthetic() && !Helper.isBeaSyntheticMethod(var8) && var8.getDeclaringClass() != Object.class) {
            int var9 = var8.getModifiers();
            if (!Modifier.isStatic(var9) && !Modifier.isVolatile(var9)) {
               this.appendInterfaceMethodDeclaration(var1, var8, var2);
            }
         }
      }

      return var1.toString();
   }

   public String extendsCMPBean() {
      return this.isContainerManagedBean ? ", weblogic.ejb.container.persistence.spi.CMPBean" : "";
   }

   private void appendInterfaceMethodDeclaration(StringBuilder var1, Method var2, Class var3) {
      MethodSignature var4 = null;
      if (!var2.toGenericString().equals(var2.toString())) {
         var4 = new MethodSignature(var2, var3);
      } else {
         var4 = new MethodSignature(var2);
      }

      var4.setFinal(false);
      var4.setNative(false);
      var1.append(var4 + ";" + EOL);
   }

   public String remote_interface_methods() throws CodeGenerationException {
      return this.remote_interface_methods(this.remoteMethods, this.remoteInterfaceClass);
   }

   public String remote_interface_methods(Method[] var1, Class var2) throws CodeGenerationException {
      StringBuilder var3 = new StringBuilder(200);

      for(int var4 = 0; var4 < var1.length; ++var4) {
         this.setMethod(var1[var4], (short)0, (Class)var2);
         var3.append(this.parse(this.getProductionRule("remote_interface_method")));
      }

      return var3.toString();
   }

   public String local_interface_methods() throws CodeGenerationException {
      return this.local_interface_methods(this.localMethods);
   }

   private String local_interface_methods(Method[] var1) throws CodeGenerationException {
      StringBuilder var2 = new StringBuilder(200);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.setMethod(var1[var3], (short)0, (Class)this.localInterfaceClass);
         var2.append(this.parse(this.getProductionRule("local_interface_method")));
      }

      return var2.toString();
   }

   protected String javaClassCommonMethodPrefix() {
      return "";
   }

   public String remote_interface_name() {
      return this.convertToLegalInnerClassName(this.remoteInterfaceClass);
   }

   public String local_interface_name() {
      return this.convertToLegalInnerClassName(this.localInterfaceClass);
   }

   public String result() {
      return this.method.getReturnType().getName().equals("void") ? "" : "result = ";
   }

   public String return_result() {
      return this.method.getReturnType().getName().equals("void") ? "// No return result" : "return result;";
   }

   public String perhaps_return() {
      return this.method.getReturnType().getName().equals("void") ? "" : "return ";
   }

   public String declareBeanStateVar() {
      if (!this.bi.isReadOnly() && (!this.bi.isOptimistic() || !this.bi.getCacheBetweenTransactions())) {
         return this.bi.getCacheBetweenTransactions() ? "  private boolean __WL_beanStateValid = true;" + EOL : "";
      } else {
         StringBuilder var1 = new StringBuilder(200);
         var1.append("  private weblogic.utils.concurrent.atomic.AtomicLong __WL_lastLoadTime = weblogic.utils.concurrent.atomic.AtomicFactory.createAtomicLong();");
         var1.append(EOL);
         var1.append("  public static int __WL_readTimeoutMS = 0;");
         var1.append(EOL);
         return var1.toString();
      }
   }

   public String perhapsSetLastLoadTime() {
      return !this.bi.isReadOnly() && (!this.bi.isOptimistic() || !this.bi.getCacheBetweenTransactions()) ? "" : "__WL_setLastLoadTime(System.currentTimeMillis());" + EOL;
   }

   public String perhapsSetLastLoadTimeMethodBody() throws CodeGenerationException {
      if (!this.bi.isReadOnly() && (!this.bi.isOptimistic() || !this.bi.getCacheBetweenTransactions())) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(" __WL_lastLoadTime.set(time);");
         var1.append(EOL);
         return var1.toString();
      }
   }

   public String perhapsGetLastLoadTimeMethodBody() {
      if (!this.bi.isReadOnly() && (!this.bi.isOptimistic() || !this.bi.getCacheBetweenTransactions())) {
         return "return 0;" + EOL;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(" return __WL_lastLoadTime.get();");
         var1.append(EOL);
         return var1.toString();
      }
   }

   public String perhapsDeclareBeanStateValidAccessors() throws CodeGenerationException {
      return this.parse(this.getProductionRule("bean_state_valid_accessors"));
   }

   public String setBeanStateValidMethodBody() {
      if (!this.bi.isReadOnly() && (!this.bi.isOptimistic() || !this.bi.getCacheBetweenTransactions())) {
         return this.bi.getCacheBetweenTransactions() ? "    __WL_beanStateValid = valid;" + EOL : "";
      } else {
         return "    if(!valid) __WL_setLastLoadTime(0);" + EOL;
      }
   }

   public String isBeanStateValidMethodBody() throws CodeGenerationException {
      if (!this.bi.isReadOnly() && (!this.bi.isOptimistic() || !this.bi.getCacheBetweenTransactions())) {
         return this.bi.getCacheBetweenTransactions() ? "    return __WL_beanStateValid;" + EOL : "    return true;" + EOL;
      } else {
         return this.parse(this.getProductionRule("bean_state_timeout_check"));
      }
   }

   public String simple_bean_class_name() {
      return this.nc.getSimpleBeanClassName();
   }

   public String simple_beanimpl_interface_name() {
      return this.nc.getSimpleGeneratedBeanInterfaceName();
   }

   public String simple_beanimpl_class_name() {
      return this.nc.getSimpleGeneratedBeanClassName();
   }

   public String simple_eoimpl_class_name() {
      return this.nc.getSimpleEJBObjectClassName();
   }

   public String simple_eloimpl_class_name() {
      return this.nc.getSimpleEJBLocalObjectClassName();
   }

   public String simple_home_class_name() {
      return this.nc.getSimpleHomeClassName();
   }

   public String simple_local_home_class_name() {
      return this.nc.getSimpleLocalHomeClassName();
   }

   public String home_superclass_name() {
      return "weblogic.ejb.container.internal.EntityEJBHome";
   }

   public String wlBeanInterface_name() {
      return "weblogic.ejb.container.interfaces.WLEntityBean";
   }

   public String invalidation_interface_name() {
      switch (this.bi.getConcurrencyStrategy()) {
         case 5:
         case 6:
            return ", weblogic.ejb.CachingHome";
         default:
            return "";
      }
   }

   public String local_invalidation_interface_name() {
      switch (this.bi.getConcurrencyStrategy()) {
         case 5:
         case 6:
            return ", weblogic.ejb.CachingLocalHome";
         default:
            return "";
      }
   }

   public String dynamic_query_interface_name() {
      return this.is20CMP() ? ", weblogic.ejb.QueryHome" : "";
   }

   public String dynamic_query_local_interface_name() {
      return this.is20CMP() ? ", weblogic.ejb.QueryLocalHome" : "";
   }

   private boolean is20CMP() {
      return this.isContainerManagedBean && this.bi.getCMPInfo().uses20CMP();
   }

   public String local_home_superclass_name() {
      return "weblogic.ejb.container.internal.EntityEJBLocalHome";
   }

   public String packageStatement() {
      String var1 = this.currentOutput.getPackage();
      return var1 != null && !var1.equals("") ? "package " + var1 + ";" : "";
   }

   public String checkExistsOnMethod() {
      StringBuilder var1 = new StringBuilder();
      if (!this.bi.getIsBeanManagedPersistence()) {
         String var2 = this.bi.getPersistenceUseIdentifier();
         String var3 = this.bi.getPersistenceUseVersion();
         if (var2.equals("WebLogic_CMP_RDBMS") && var3.equals("6.0")) {
            var1.append("((weblogic.ejb.container.persistence.spi.CMPBean) __bean).__WL_checkExistsOnMethod();" + EOL);
         }
      }

      return var1.toString();
   }

   public String writeObject() throws CodeGenerationException {
      return this.parse(this.getProductionRule("write_object_code"));
   }

   public String readObject() throws CodeGenerationException {
      return this.parse(this.getProductionRule("read_object_code"));
   }

   public String wl_entitybean_fields() throws CodeGenerationException {
      return this.parse(this.getProductionRule("wl_entitybean_fields_code"));
   }

   public String wl_entitybean_methods() throws CodeGenerationException {
      return this.parse(this.getProductionRule("wl_entitybean_methods_code"));
   }

   public String return_type(Class var1) {
      String var2 = null;
      if (var1.isPrimitive()) {
         var2 = this.arg_type(var1);
      } else if (var1.isArray() && this.is_primitive_array(var1)) {
         var2 = this.array_as_primitive_type(var1);
      } else if (var1.isArray()) {
         var2 = this.array_as_type(var1);
      } else {
         var2 = this.arg_type(var1);
      }

      return var2;
   }

   public String arg_as_primitive_type(Class var1) {
      if (var1.equals(Boolean.TYPE)) {
         return "boolean";
      } else if (var1.equals(Character.TYPE)) {
         return "char";
      } else if (var1.equals(Byte.TYPE)) {
         return "byte";
      } else if (var1.equals(Short.TYPE)) {
         return "short";
      } else if (var1.equals(Integer.TYPE)) {
         return "int";
      } else if (var1.equals(Long.TYPE)) {
         return "long";
      } else if (var1.equals(Float.TYPE)) {
         return "float";
      } else if (var1.equals(Double.TYPE)) {
         return "double";
      } else {
         throw new AssertionError("Primitive type not found");
      }
   }

   public String array_as_type(Class var1) {
      int var2;
      for(var2 = 0; var1.isArray(); var1 = var1.getComponentType()) {
         ++var2;
      }

      StringBuilder var3 = new StringBuilder(this.arg_type(var1));

      for(int var4 = 0; var4 < var2; ++var4) {
         var3 = var3.append("[]");
      }

      return var3.toString();
   }

   public String arg_type(Class var1) {
      return ClassUtils.getCanonicalName(var1);
   }

   public String array_as_primitive_type(Class var1) {
      StringBuilder var2 = new StringBuilder(" ");
      int var3 = var1.getName().length() - 1;
      if (var3 > 0) {
         for(int var4 = 0; var4 < var3; ++var4) {
            var2 = var2.append("[]");
         }
      }

      if (var1.getName().endsWith("[Z")) {
         return "boolean" + var2;
      } else if (var1.getName().endsWith("[C")) {
         return "char" + var2;
      } else if (var1.getName().endsWith("[B")) {
         return "byte" + var2;
      } else if (var1.getName().endsWith("[S")) {
         return "short" + var2;
      } else if (var1.getName().endsWith("[I")) {
         return "int" + var2;
      } else if (var1.getName().endsWith("[J")) {
         return "long" + var2;
      } else if (var1.getName().endsWith("[F")) {
         return "float" + var2;
      } else if (var1.getName().endsWith("[D")) {
         return "double" + var2;
      } else {
         throw new AssertionError("Primitive type not found");
      }
   }

   public void setMethod(Method var1, short var2) {
      this.setMethod(var1, var2, (String[])null, (Class)null);
   }

   public void setMethod(Method var1, short var2, Class var3) {
      if (var1.toGenericString().equals(var1.toString())) {
         this.setMethod(var1, var2, (String[])null, (Class)null);
      } else {
         this.setMethod(var1, var2, (String[])null, var3);
      }

   }

   public void setMethod(Method var1, short var2, String[] var3) {
      this.setMethod(var1, var2, var3, (Class)null);
   }

   public void setMethod(Method var1, short var2, String[] var3, Class var4) {
      this.method = var1;
      this.mt.setMethod(this.method);
      this.methodSignature = new MethodSignature(var1, var4);
      if (var3 != null) {
         this.methodSignature.setParameterNames(var3);
      }

      this.methodType = var2;
   }

   public String perhapsLite() {
      return this.cmpGetterSetterMethods != null && this.cmpGetterSetterMethods.contains(this.method) ? "Lite" : "";
   }

   public String push_bean() {
      return "weblogic.ejb.container.internal.AllowedMethodsHelper.pushBean(this);";
   }

   public String pop_bean() {
      return "weblogic.ejb.container.internal.AllowedMethodsHelper.popBean();";
   }

   protected void writeToFile(String var1, File var2) throws IOException {
      this.currentOutput.setAbsoluteFilePath(var2.getAbsolutePath());
      this.currentOutput.setOutputContent(var1);
      if (this.keepgenerated || !this.isJDTBased()) {
         super.writeToFile(var1, var2);
      }

   }

   protected PrintWriter makeOutputStream(File var1) throws IOException {
      return this.keepgenerated ? this.makeIndentingOutputStream(var1) : super.makeOutputStream(var1);
   }

   private static void p(String var0) {
      System.out.println("*** <EjbCodeGenerator> " + var0);
   }

   private String convertToLegalInnerClassName(Class var1) {
      StringBuilder var2 = new StringBuilder(var1.getName());

      for(Class var3 = var1.getDeclaringClass(); var3 != null; var3 = var3.getDeclaringClass()) {
         var2.setCharAt(var3.getName().length(), '.');
      }

      return var2.toString();
   }

   public boolean isJDTBased() {
      if (this.opts == null) {
         return true;
      } else {
         String var1 = this.opts.getOption("compiler");
         return var1 == null || "jdt".equalsIgnoreCase(var1);
      }
   }

   public List<Output> getGeneratedOutputs() {
      return this.generatedOutputs;
   }

   public static class Output extends CodeGenerator.Output implements Cloneable {
      private BeanInfo bi;
      private NamingConvention namingConvention;
      private String outputContent;
      private String absoluteFilePath;

      public String getOutputContent() {
         return this.outputContent;
      }

      private void setOutputContent(String var1) {
         this.outputContent = var1;
      }

      public String getAbsoluteFilePath() {
         return this.absoluteFilePath;
      }

      private void setAbsoluteFilePath(String var1) {
         this.absoluteFilePath = var1;
      }

      public BeanInfo getBeanInfo() {
         return this.bi;
      }

      public void setBeanInfo(BeanInfo var1) {
         this.bi = var1;
      }

      public NamingConvention getNamingConvention() {
         return this.namingConvention;
      }

      public void setNamingConvention(NamingConvention var1) {
         this.namingConvention = var1;
      }

      public Object clone() {
         try {
            return super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new AssertionError(var2 + "");
         }
      }
   }
}
