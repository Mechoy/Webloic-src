package weblogic.ejb.container.compliance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBContext;
import javax.ejb.SessionBean;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.utils.ErrorCollectionException;

class SessionBeanClassChecker extends BeanClassChecker {
   private boolean isStateful;
   private boolean isStateless;

   SessionBeanClassChecker(ClientDrivenBeanInfo var1) throws ClassNotFoundException {
      super(var1);
      SessionBeanInfo var2 = (SessionBeanInfo)var1;
      this.isStateful = var2.isStateful();
      this.isStateless = !this.isStateful;
   }

   public void checkEJBContextIsNotTransient() throws ComplianceException {
      for(Class var1 = this.beanClass; var1 != null && !var1.equals(Object.class); var1 = var1.getSuperclass()) {
         Field[] var2 = var1.getDeclaredFields();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Class var4 = var2[var3].getType();
            if (EJBContext.class.isAssignableFrom(var4)) {
               if (Modifier.isTransient(var2[var3].getModifiers())) {
                  throw new ComplianceException(this.fmt.EJBCONTEXT_IS_TRANSIENT(this.ejbName));
               }

               if (!ComplianceUtils.isLegalRMIIIOPType(var4)) {
                  throw new ComplianceException(this.fmt.NOT_RMIIIOP_LEGAL_TYPE_20(this.ejbName));
               }
            }
         }
      }

   }

   public void checkClassImplementsSessionBean() throws ComplianceException {
      if (!this.beanInfo.isEJB30() && !SessionBean.class.isAssignableFrom(this.beanClass)) {
         throw new ComplianceException(this.fmt.BEAN_IMPLEMENT_SESSIONBEAN(this.ejbName));
      }
   }

   public void checkStatelessEjbCreate() throws ComplianceException, ErrorCollectionException {
      if (!this.beanInfo.isEJB30()) {
         if (this.beanInfo.hasDeclaredRemoteHome() || this.beanInfo.hasDeclaredLocalHome()) {
            if (!this.isStateful) {
               List var1 = this.getCreateMethods();
               if (var1.size() != 1) {
                  throw new ComplianceException(this.fmt.STATELESS_NOARG_EJBCREATE(this.ejbName));
               } else {
                  try {
                     Method var2 = this.beanClass.getMethod("ejbCreate", (Class[])null);
                     ArrayList var3 = new ArrayList();
                     var3.add(var2);
                     this.validateEjbCreates(var3);
                  } catch (NoSuchMethodException var4) {
                     throw new ComplianceException(this.fmt.STATELESS_NOARG_EJBCREATE(this.ejbName));
                  }
               }
            }
         }
      }
   }

   public void checkBeanClassIsNotAbstract() throws ComplianceException {
      if (Modifier.isAbstract(this.beanClassMod)) {
         throw new ComplianceException(this.fmt.ABSTRACT_BEAN_CLASS(this.ejbName));
      }
   }

   public void checkStatefulEjbCreate() throws ErrorCollectionException, ComplianceException {
      if (!this.beanInfo.isEJB30()) {
         if (this.beanInfo.hasDeclaredRemoteHome() || this.beanInfo.hasDeclaredLocalHome()) {
            if (!this.isStateless) {
               List var1 = this.getCreateMethods();
               if (var1.size() == 0) {
                  throw new ComplianceException(this.fmt.STATEFUL_DEFINE_EJBCREATE(this.ejbName));
               } else {
                  this.validateEjbCreates(var1);
               }
            }
         }
      }
   }

   public void checkInterfacesExist() throws ErrorCollectionException, ComplianceException {
      SessionBeanInfo var1 = (SessionBeanInfo)this.beanInfo;
      if (!var1.isEndpointView()) {
         if (var1.getLocalInterfaceName() == null && var1.getRemoteInterfaceName() == null && var1.getServiceEndpointName() == null) {
            if (!var1.isEJB30()) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().COMPONENT_INTERFACE_NOT_FOUND_IN_SESSION_BEAN(this.ejbName));
            }

            if ((var1.getBusinessLocals() == null || var1.getBusinessLocals().isEmpty()) && (var1.getBusinessRemotes() == null || var1.getBusinessRemotes().isEmpty())) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().BUSINESS_INTERFACE_NOT_FOUND_IN_SESSION_BEAN(this.ejbName));
            }
         }

      }
   }

   protected void validateCreateReturnType(Method var1) throws ComplianceException {
      if (!var1.getReturnType().isAssignableFrom(Void.TYPE)) {
         throw new ComplianceException(this.fmt.EJBCREATE_RETURNS_VOID(this.ejbName));
      }
   }
}
