package weblogic.ejb.container.compliance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EntityBean;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.ErrorCollectionException;

abstract class EntityBeanClassChecker extends BeanClassChecker {
   protected Class pkClass;
   protected boolean isBMP;
   protected EntityBeanInfo ebi = null;

   EntityBeanClassChecker(ClientDrivenBeanInfo var1) throws ClassNotFoundException {
      super(var1);
      this.beanClass = var1.getBeanClass();
      this.beanClassMod = this.beanClass.getModifiers();
      this.beanInfo = var1;
      this.ejbName = this.beanInfo.getEJBName();
      this.ebi = (EntityBeanInfo)var1;
      this.pkClass = this.ebi.getPrimaryKeyClass();
      this.isBMP = this.ebi.getIsBeanManagedPersistence();
   }

   protected List getPostCreateMethods() {
      Method[] var1 = this.beanClass.getMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getName().startsWith("ejbPostCreate")) {
            var2.add(var1[var3]);
         }
      }

      return var2;
   }

   protected List getFinderMethods() {
      Method[] var1 = this.beanClass.getMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getName().startsWith("ejbFind")) {
            var2.add(var1[var3]);
         }
      }

      return var2;
   }

   public void doCheckCreatesMatchPostCreates(String var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      List var3 = this.getCreateMethods();
      HashSet var4 = new HashSet(this.getPostCreateMethods());
      Iterator var5 = var3.iterator();

      Method var6;
      while(var5.hasNext()) {
         var6 = (Method)var5.next();

         try {
            String var7 = "ejbPostC" + var6.getName().substring(4);
            Method var8 = this.beanClass.getMethod(var7, var6.getParameterTypes());
            var4.remove(var8);
         } catch (NoSuchMethodException var9) {
            var2.add(new ComplianceException(this.fmt.CREATES_MATCH_POSTCREATE(this.ejbName, this.methodSig(var6))));
         }
      }

      var5 = var4.iterator();

      while(var5.hasNext()) {
         var6 = (Method)var5.next();
         var2.add(new ComplianceException(this.fmt.EXTRA_POSTCREATE(this.ejbName, this.methodSig(var6))));
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkEjbCreateMethods() throws ErrorCollectionException {
      List var1 = this.getCreateMethods();
      if (var1.size() != 0) {
         this.validateEjbCreates(var1);
      }

   }

   public void doCheckBeanClassImplementsEntityBean(String var1) throws ComplianceException {
      if (!EntityBean.class.isAssignableFrom(this.beanClass)) {
         throw new ComplianceException(this.fmt.MUST_IMPLEMENT_ENTITYBEAN(this.ejbName));
      }
   }

   protected void validateCreateReturnType(Method var1, String var2) throws ComplianceException {
      if (!var1.getReturnType().isAssignableFrom(this.pkClass)) {
         throw new ComplianceException(this.fmt.EJBCREATE_RETURNS_PK(this.ejbName, this.methodSig(var1)));
      }
   }

   public void doCheckPostCreates(String var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      Method[] var3 = this.beanClass.getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Method var5 = var3[var4];
         if (var5.getName().startsWith("ejbPostCreate")) {
            int var6 = var5.getModifiers();
            if (!Modifier.isPublic(var6)) {
               var2.add(new ComplianceException(this.fmt.EJBPOSTCREATE_MUST_BE_PUBLIC(this.ejbName, this.methodSig(var5))));
            }

            if (Modifier.isFinal(var6)) {
               var2.add(new ComplianceException(this.fmt.EJBPOSTCREATE_MUST_NOT_BE_FINAL(this.ejbName, this.methodSig(var5))));
            }

            if (Modifier.isStatic(var6)) {
               var2.add(new ComplianceException(this.fmt.EJBPOSTCREATE_MUST_NOT_BE_STATIC(this.ejbName, this.methodSig(var5))));
            }

            if (!Void.TYPE.isAssignableFrom(var5.getReturnType())) {
               var2.add(new ComplianceException(this.fmt.EJBPOSTCREATE_MUST_RETURN_VOID(this.ejbName, this.methodSig(var5))));
            }
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void doCheckCMPBeanDoesntDefineFinders(String var1, boolean var2) throws ErrorCollectionException {
      ErrorCollectionException var3 = new ErrorCollectionException();
      if (!this.isBMP) {
         List var4 = this.getFinderMethods();
         if (var4.size() != 0) {
            Iterator var5 = var4.iterator();

            label28:
            while(true) {
               Method var6;
               boolean var7;
               do {
                  if (!var5.hasNext()) {
                     break label28;
                  }

                  var6 = (Method)var5.next();
                  if (!var2) {
                     break;
                  }

                  var7 = Modifier.isAbstract(var6.getModifiers());
               } while(var7);

               var3.add(new ComplianceException(this.fmt.FINDER_IN_CMP_BEAN(this.ejbName, this.methodSig(var6))));
            }
         }

         if (!var3.isEmpty()) {
            throw var3;
         }
      }
   }

   public void doCheckCMPBeanHasPersistenceUse() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      if (!this.isBMP) {
         CMPInfo var2 = this.ebi.getCMPInfo();
         if (var2.getPersistenceUseIdentifier() == null) {
            var1.add(new ComplianceException(this.fmt.BEAN_MISSING_PERSISTENCE_USE(this.ejbName)));
         }

         if (!var1.isEmpty()) {
            throw var1;
         }
      }
   }

   public void doCheckPrimkeyFieldIsCMPField(String var1) throws ComplianceException {
      if (!this.isBMP) {
         CMPInfo var2 = this.ebi.getCMPInfo();
         String var3 = var2.getCMPrimaryKeyFieldName();
         if (var3 != null && !var2.getAllContainerManagedFieldNames().contains(var3)) {
            throw new ComplianceException(this.fmt.PRIMKEY_FIELD_MUST_BE_CMP_FIELD(this.ejbName), new DescriptorErrorInfo("<cmp-field>", this.ejbName, var3));
         }
      }

   }

   public void doCheckPrimaryKeyClassFieldsAreCMPFields(String var1) throws ComplianceException {
      if (!this.isBMP) {
         CMPInfo var2 = this.ebi.getCMPInfo();
         if (var2.getCMPrimaryKeyFieldName() == null) {
            Collection var3 = var2.getAllContainerManagedFieldNames();
            Field[] var4 = this.pkClass.getFields();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               Field var6 = var4[var5];
               String var7 = var6.getName();
               if (!var7.equals("serialVersionUID") && !var3.contains(var7)) {
                  throw new ComplianceException(this.fmt.PK_FIELDS_MUST_BE_CMP_FIELDS(this.ejbName, var6.getName()), new DescriptorErrorInfo("<cmp-field>", this.ejbName, var6.getName()));
               }
            }
         }
      }

   }

   public void checkConcurrencyDatabaseAndCacheBetweenTransactionsMatch() {
      if (this.ebi.getConcurrencyStrategy() == 2 && this.ebi.getCacheBetweenTransactions()) {
         log.logWarning(this.fmt.CACHE_BETWEEN_TRANS_MUST_BE_FALSE_FOR_CONCURRENCY_DB(this.ejbName));
      }

   }

   public void checkInvalidationTarget() throws ComplianceException {
      String var1 = this.ebi.getInvalidationTargetEJBName();
      if (var1 != null) {
         BeanInfo var2 = this.ebi.getDeploymentInfo().getBeanInfo(var1);
         if (var2 == null) {
            throw new ComplianceException(this.fmt.INVALIDATION_TARGET_DOES_NOT_EXIST(this.ejbName, var1), new DescriptorErrorInfo("<invalidation-target>", this.ejbName, var1));
         } else if (!(var2 instanceof EntityBeanInfo)) {
            throw new ComplianceException(this.fmt.INVALIDATION_TARGET_MUST_BE_RO_ENTITY(this.ejbName, var1), new DescriptorErrorInfo("<invalidation-target>", this.ejbName, var1));
         } else if (((EntityBeanInfo)var2).getConcurrencyStrategy() != 5) {
            throw new ComplianceException(this.fmt.INVALIDATION_TARGET_MUST_BE_RO_ENTITY(this.ejbName, var1), new DescriptorErrorInfo("<invalidation-target>", this.ejbName, var1));
         } else if (this.ebi.getConcurrencyStrategy() == 5) {
            throw new ComplianceException(this.fmt.INVALIDATION_TARGET_CANNOT_BE_SET_FOR_RO_ENTITY(this.ejbName), new DescriptorErrorInfo("<invalidation-target>", this.ejbName, var1));
         } else if (this.ebi.getIsBeanManagedPersistence() || !this.ebi.getCMPInfo().uses20CMP()) {
            throw new ComplianceException(this.fmt.INVALIDATION_TARGET_MUST_BE_SET_ON_CMP20(this.ejbName), new DescriptorErrorInfo("<invalidation-target>", this.ejbName, var1));
         }
      }
   }
}
