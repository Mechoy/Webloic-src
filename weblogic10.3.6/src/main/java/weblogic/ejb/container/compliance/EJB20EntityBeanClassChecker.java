package weblogic.ejb.container.compliance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.persistence.PersistenceUtils;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.ErrorCollectionException;

final class EJB20EntityBeanClassChecker extends EntityBeanClassChecker {
   private Map methodMap = null;

   EJB20EntityBeanClassChecker(ClientDrivenBeanInfo var1) throws ClassNotFoundException {
      super(var1);
      this.methodMap = PersistenceUtils.getAccessorMethodMap(this.beanClass);
   }

   private Object[] getEjbSelectMethods() {
      Method[] var1 = this.beanClass.getDeclaredMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3].getName();
         if (var4.startsWith("ejbSelect")) {
            int var5 = var1[var3].getModifiers();
            if (Modifier.isAbstract(var5) && (Modifier.isPublic(var5) || Modifier.isProtected(var5))) {
               var2.add(var1[var3]);
            }
         }
      }

      return var2.toArray();
   }

   public void checkCreatesMatchPostCreates() throws ErrorCollectionException {
      super.doCheckCreatesMatchPostCreates("9.6.2");
   }

   public void checkBeanClassImplementsEntityBean() throws ComplianceException {
      if (!this.ebi.isEJB30()) {
         super.doCheckBeanClassImplementsEntityBean("9.7.2");
      }
   }

   protected void validateCreateReturnType(Method var1) throws ComplianceException {
      super.validateCreateReturnType(var1, "9.7.5");
   }

   public void checkPostCreates() throws ErrorCollectionException {
      super.doCheckPostCreates("9.7.6");
   }

   public void checkCMPBeanDoesntDefineFinders() throws ErrorCollectionException {
      super.doCheckCMPBeanDoesntDefineFinders("9.7.2", true);
   }

   public void checkIsModifiedMethod() throws ComplianceException {
      if (this.isBMP) {
         String var1 = this.ebi.getIsModifiedMethodName();
         if (var1 != null) {
            Method var2 = null;

            try {
               var2 = this.beanClass.getMethod(var1, (Class[])null);
            } catch (NoSuchMethodException var4) {
               throw new ComplianceException(this.fmt.ISMODIFIED_NOT_EXIST(this.ejbName, var1));
            }

            if (!Boolean.TYPE.isAssignableFrom(var2.getReturnType())) {
               throw new ComplianceException(this.fmt.ISMODIFIED_RETURNS_BOOL(this.ejbName, this.methodSig(var2)));
            }
         }
      }

   }

   public void checkCMPFields() throws ComplianceException {
      if (!this.ebi.isEJB30()) {
         if (!this.isBMP) {
            Class var1 = null;
            var1 = this.ebi.getBeanClass();
            CMPInfo var2 = this.ebi.getCMPInfo();
            Collection var3 = var2.getAllContainerManagedFieldNames();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               if (Character.isUpperCase(var5.charAt(0)) || !Character.isLetter(var5.charAt(0))) {
                  throw new ComplianceException(this.fmt.CM_FIELD_MUST_START_WITH_LOWERCASE(this.ejbName, var5), new DescriptorErrorInfo("<cmp-field>", this.ejbName, var5));
               }

               String var6 = RDBMSUtils.getterMethodName(var5);
               if (!this.methodMap.containsKey(var6)) {
                  throw new ComplianceException(this.fmt.DEFINE_CMP_ACCESSOR_METHOD_20(this.ejbName, var6));
               }

               String var7 = RDBMSUtils.setterMethodName(var5);
               if (!this.methodMap.containsKey(var7)) {
                  throw new ComplianceException(this.fmt.DEFINE_CMP_ACCESSOR_METHOD_20(this.ejbName, var7));
               }

               Method var8 = (Method)this.methodMap.get(var7);
               Class var9 = var8.getReturnType();
               if (!var9.getName().equals("void")) {
                  throw new ComplianceException(this.fmt.SETTER_DOES_NOT_RETURN_VOID(this.ejbName));
               }

               int var10 = var8.getModifiers();
               if (!Modifier.isPublic(var10)) {
                  throw new ComplianceException(this.fmt.CMP_ACCESSOR_NOT_PUBLIC(this.ejbName, var7));
               }

               Method var11 = (Method)this.methodMap.get(var6);
               Class var12 = var11.getReturnType();
               Class[] var13 = var8.getParameterTypes();
               if (var13.length != 1) {
                  throw new ComplianceException(this.fmt.SETTER_DOES_NOT_HAVE_SINGLE_PARAM(this.ejbName, var7));
               }

               if (!var12.equals(var13[0])) {
                  throw new ComplianceException(this.fmt.SETTER_PARAM_DOES_NOT_MATCH_GETTER_RETURN(this.ejbName, var7));
               }

               var10 = var11.getModifiers();
               if (!Modifier.isPublic(var10)) {
                  throw new ComplianceException(this.fmt.CMP_ACCESSOR_NOT_PUBLIC(this.ejbName, var7));
               }

               boolean var14 = true;

               try {
                  var1.getField(var5);
               } catch (NoSuchFieldException var16) {
                  var14 = false;
               }

               if (var14) {
                  throw new ComplianceException(this.fmt.DO_NOT_DEFINE_CMFIELD_20(this.ejbName));
               }
            }
         }

      }
   }

   public void checkBeanClassIsNotAbstract() throws ComplianceException {
      if (this.isBMP && Modifier.isAbstract(this.beanClassMod)) {
         throw new ComplianceException(this.fmt.ABSTRACT_BEAN_CLASS(this.ejbName));
      }
   }

   public void checkEjbSelectReturnType() throws ComplianceException {
      if (!this.isBMP) {
         Object[] var1 = this.getEjbSelectMethods();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            Class var3 = ((Method)var1[var2]).getReturnType();
            if (var3.getName().equals("java.util.Enumeration")) {
               throw new ComplianceException(this.fmt.EJB_SELECT_CANNOT_RETURN_ENUMERATION(this.ejbName, ((Method)var1[var2]).getName()));
            }
         }
      }

   }

   public void checkEjbSelectThrowsClause() throws ComplianceException {
      if (!this.isBMP) {
         Object[] var1 = this.getEjbSelectMethods();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            boolean var3 = false;
            Class[] var4 = ((Method)var1[var2]).getExceptionTypes();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var4[var5].equals(FinderException.class)) {
                  var3 = true;
               }
            }

            if (!var3) {
               throw new ComplianceException(this.fmt.EJB_SELECT_MUST_THROW(this.ejbName, ((Method)var1[var2]).getName()));
            }
         }
      }

   }

   public void checkPrimaryKeyFieldClass() throws ComplianceException {
      if (!this.isBMP) {
         CMPInfo var1 = this.ebi.getCMPInfo();
         String var2 = var1.getCMPrimaryKeyFieldName();
         if (var2 != null && var2.length() > 0) {
            Class var3 = this.ebi.getPrimaryKeyClass();
            String var4 = var3.getName();
            String var5 = RDBMSUtils.getterMethodName(var2);
            Method var6 = (Method)this.methodMap.get(var5);
            if (var6 != null) {
               Class var7 = var6.getReturnType();
               if (var7 != null && !var7.equals(var3)) {
                  throw new ComplianceException(this.fmt.PRIMKEY_CLASS_DOES_NOT_MATCH_ACCESSOR_FOR_GETTER(this.ejbName, var2, var4), new DescriptorErrorInfo("<prim-key-class>", this.ejbName, var4));
               }
            }

            String var10 = RDBMSUtils.setterMethodName(var2);
            Method var8 = (Method)this.methodMap.get(var10);
            if (var8 != null) {
               Class[] var9 = var8.getParameterTypes();
               if (var9 != null && var9.length > 0 && var9[0] != null && !var9[0].equals(var3)) {
                  throw new ComplianceException(this.fmt.PRIMKEY_CLASS_DOES_NOT_MATCH_ACCESSOR_FOR_SETTER(this.ejbName, var2, var4), new DescriptorErrorInfo("<prim-key-class>", this.ejbName, var4));
               }
            }
         }
      }

   }

   public void checkBeanAndPKClassDeclareSamePKFieldType() throws ErrorCollectionException {
      if (!this.isBMP) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         boolean var2 = this.ebi.getCMPInfo().getCMPrimaryKeyFieldName() == null;
         if (var2) {
            Field[] var3 = this.ebi.getPrimaryKeyClass().getFields();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               Field var5 = var3[var4];
               if (!var5.getName().equals("serialVersionUID")) {
                  String var6 = RDBMSUtils.getterMethodName(var5.getName());
                  Method var7 = (Method)this.methodMap.get(var6);
                  if (var7 != null) {
                     Class var8 = var7.getReturnType();
                     if (var8 != null && !var8.equals(var5.getType())) {
                        var1.add(new ComplianceException(this.fmt.BEAN_PK_CLASS_DOES_NOT_MATCH_PKFIELD_FOR_GETTER(this.ejbName, var5.getName(), var8.getName(), var5.getType().getName())));
                     }
                  }

                  String var11 = RDBMSUtils.setterMethodName(var5.getName());
                  Method var9 = (Method)this.methodMap.get(var11);
                  if (var9 != null) {
                     Class[] var10 = var9.getParameterTypes();
                     if (var10 != null && var10[0] != null && !var10[0].equals(var5.getType())) {
                        var1.add(new ComplianceException(this.fmt.BEAN_PK_CLASS_DOES_NOT_MATCH_PKFIELD_FOR_SETTER(this.ejbName, var5.getName(), var10[0].getName(), var5.getType().getName())));
                     }
                  }
               }
            }
         }

         if (!var1.isEmpty()) {
            throw var1;
         }
      }
   }

   public void checkPrimaryKeyFieldIsCMPField() throws ComplianceException {
      super.doCheckPrimkeyFieldIsCMPField("9.10.1.1");
   }

   public void checkPrimaryKeyClassFieldsAreCMPFields() throws ComplianceException {
      super.doCheckPrimaryKeyClassFieldsAreCMPFields("9.10.1.2");
   }

   public void checkAbstractBeanClassImplementsEntityBean() throws ErrorCollectionException {
      if (!this.ebi.isEJB30()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         Method[] var2 = EntityBean.class.getMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Method var4 = var2[var3];

            try {
               Method var5 = this.beanClass.getMethod(var4.getName(), (Class[])var4.getParameterTypes());
               if (Modifier.isAbstract(var5.getModifiers())) {
                  var1.add(new ComplianceException(this.fmt.MISSING_ENTITY_BEAN_METHOD(this.ejbName, this.methodSig(var4))));
               }
            } catch (NoSuchMethodException var6) {
               var1.add(new ComplianceException(this.fmt.MISSING_ENTITY_BEAN_METHOD(this.ejbName, this.methodSig(var4))));
            }
         }

         if (!var1.isEmpty()) {
            throw var1;
         }
      }
   }

   public void checkBMPUsingOptimisticConcurrency() throws ComplianceException {
      if (this.isBMP && this.ebi.getConcurrencyStrategy() == 6) {
         throw new ComplianceException(this.fmt.BMP_CANNOT_USE_OPTIMISTIC_CONCURRENCY(this.ejbName));
      }
   }

   public void checkOptimisticReadTimeoutSecondsNoCacheBetweenTransactions() {
      if (this.ebi.getConcurrencyStrategy() == 6 && this.ebi.getCachingDescriptor().getReadTimeoutSeconds() > 0 && !this.ebi.getCacheBetweenTransactions()) {
         log.logWarning(this.fmt.OptimisticWithReadTimeoutSecondsNoCacheBetweenTx(this.ejbName));
      }

   }
}
