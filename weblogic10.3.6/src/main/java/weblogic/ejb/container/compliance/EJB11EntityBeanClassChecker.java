package weblogic.ejb.container.compliance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

final class EJB11EntityBeanClassChecker extends EntityBeanClassChecker {
   EJB11EntityBeanClassChecker(ClientDrivenBeanInfo var1) throws ClassNotFoundException {
      super(var1);
   }

   public void checkCreatesMatchPostCreates() throws ErrorCollectionException {
      super.doCheckCreatesMatchPostCreates("9.1.5.1");
   }

   public void checkBeanClassImplementsEntityBean() throws ComplianceException {
      super.doCheckBeanClassImplementsEntityBean("9.2.2");
   }

   protected void validateCreateReturnType(Method var1) throws ComplianceException {
      super.validateCreateReturnType(var1, "9.2.3");
   }

   public void checkPostCreates() throws ErrorCollectionException {
      super.doCheckPostCreates("9.2.4");
   }

   public void checkCMPBeanDoesntDefineFinders() throws ErrorCollectionException {
      super.doCheckCMPBeanDoesntDefineFinders("9.4.6", false);
   }

   public void checkBeanClassIsNotAbstract() throws ComplianceException {
      if (Modifier.isAbstract(this.beanClassMod)) {
         throw new ComplianceException(this.fmt.ABSTRACT_BEAN_CLASS(this.ejbName));
      }
   }

   public void checkIsModifiedMethod() throws ComplianceException {
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

   public void checkCMPFields() throws ComplianceException {
      if (!this.isBMP) {
         Class var1 = this.ebi.getBeanClass();
         Debug.assertion(var1 != null, "Could not find bean class in ComplianceChecker.");
         CMPInfo var2 = this.ebi.getCMPInfo();
         Collection var3 = var2.getAllContainerManagedFieldNames();
         Iterator var4 = var3.iterator();

         String var5;
         Field var6;
         while(var4.hasNext()) {
            var5 = (String)var4.next();
            var6 = null;

            try {
               var6 = var1.getField(var5);
            } catch (NoSuchFieldException var9) {
               throw new ComplianceException(this.fmt.CMP_FIELDS_MUST_BE_BEAN_FIELDS(this.ejbName, var5));
            }

            int var7 = var6.getModifiers();
            if (!Modifier.isPublic(var7)) {
               throw new ComplianceException(this.fmt.CMP_FIELDS_MUST_BE_PUBLIC(this.ejbName, var6.getName()));
            }

            if (Modifier.isStatic(var7)) {
               throw new ComplianceException(this.fmt.CMP_FIELDS_MUST_NOT_BE_STATIC(this.ejbName, var6.getName()));
            }
         }

         var5 = var2.getCMPrimaryKeyFieldName();
         if (var5 == null) {
            this.doCheckPrimaryKeyClassFieldsAreCMPFields("9.4.7.2");
         } else {
            this.doCheckPrimkeyFieldIsCMPField("9.4.7.1");
            var6 = null;

            try {
               var6 = var1.getField(var5);
            } catch (NoSuchFieldException var8) {
               throw new ComplianceException(this.fmt.PK_FIELD_MUST_EXIST(this.ejbName, var5), new DescriptorErrorInfo("<primkey-field>", this.ejbName, var5));
            }

            Class var10 = var6.getType();
            if (!var10.getName().equals(this.pkClass.getName())) {
               throw new ComplianceException(this.fmt.PK_FIELD_WRONG_TYPE(this.ejbName, var5, var10.getName()), new DescriptorErrorInfo("<primkey-field>", this.ejbName, var10.getName()));
            }
         }
      }

   }

   public void checkCMP11UsingOptimisticConcurrency() throws ComplianceException {
      if (this.ebi.getConcurrencyStrategy() == 6) {
         throw new ComplianceException(this.fmt.CMP11_CANNOT_USE_OPTIMISTIC_CONCURRENCY(this.ejbName));
      }
   }
}
