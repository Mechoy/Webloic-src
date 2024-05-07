package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.utils.ErrorCollectionException;

final class LifecycleCallbackInterceptorChecker extends BaseComplianceChecker {
   private DeploymentInfo deploymentInfo;
   private EjbDescriptorBean ejbDescriptor;
   private EjbJarBean ejbJarBean;
   private InterceptorsBean interceptorsBean;
   private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

   public LifecycleCallbackInterceptorChecker(DeploymentInfo var1) {
      this.deploymentInfo = var1;
      this.ejbDescriptor = this.deploymentInfo.getEjbDescriptorBean();
      this.ejbJarBean = this.ejbDescriptor.getEjbJarBean();
      this.interceptorsBean = this.ejbJarBean.getInterceptors();
   }

   public void checkOnlyOneLifecycleCallbackMethodperLEperClass() throws ComplianceException {
      LifecycleCallbackBean[] var1 = null;
      SessionBeanBean[] var2 = this.ejbJarBean.getEnterpriseBeans().getSessions();

      int var3;
      BeanInfo var4;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var4 = this.deploymentInfo.getBeanInfo(var2[var3].getEjbName());
         if (var4.isEJB30()) {
            SessionBeanBean var5 = (SessionBeanBean)var2[var3];
            var1 = var5.getPostConstructs();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PostConstruct");
            var1 = var5.getPreDestroys();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PreDestroy");
            SessionBeanInfo var6 = (SessionBeanInfo)var4;
            if (var6.isStateful()) {
               var1 = var5.getPostActivates();
               this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PostActivate");
               var1 = var5.getPrePassivates();
               this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PrePassivate");
            }
         }
      }

      MessageDrivenBeanBean[] var7 = this.ejbJarBean.getEnterpriseBeans().getMessageDrivens();

      for(var3 = 0; var3 < var7.length; ++var3) {
         var4 = this.deploymentInfo.getBeanInfo(var7[var3].getEjbName());
         if (var4.isEJB30()) {
            MessageDrivenBeanBean var10 = (MessageDrivenBeanBean)var7[var3];
            var1 = var10.getPostConstructs();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PostConstruct");
            var1 = var10.getPreDestroys();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PreDestroy");
         }
      }

      if (this.interceptorsBean != null) {
         InterceptorBean[] var8 = this.interceptorsBean.getInterceptors();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var1 = var8[var9].getPostConstructs();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PostConstruct");
            var1 = var8[var9].getPreDestroys();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PreDestroy");
            var1 = var8[var9].getPostActivates();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PostActivate");
            var1 = var8[var9].getPrePassivates();
            this.validateNotTwoSameTypeLifecycleCallbackPerClass(var1, "PrePassivate");
         }
      }

   }

   public void checkLifecycleCallbackMethods() throws ErrorCollectionException, ComplianceException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Set var2 = this.getAllLifecycleCallbackMethodsFromBean();
      Iterator var3 = var2.iterator();

      Method var4;
      while(var3.hasNext()) {
         var4 = (Method)var3.next();

         try {
            this.validateLifecycleCallbackMethod(var4, true);
         } catch (ErrorCollectionException var7) {
            var1.add(var7);
         }
      }

      var2 = this.getAllLifecycleCallbackMethodsFromInterceptor();
      var3 = var2.iterator();

      while(var3.hasNext()) {
         var4 = (Method)var3.next();

         try {
            this.validateLifecycleCallbackMethod(var4, false);
         } catch (ErrorCollectionException var6) {
            var1.add(var6);
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void validateLifecycleCallbackMethod(Method var1, boolean var2) throws ErrorCollectionException {
      ErrorCollectionException var3 = new ErrorCollectionException();
      int var4 = var1.getModifiers();
      if (Modifier.isFinal(var4)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().LIFECYCLE_INTERCEPTOR_METHOD_NOT_BE_FINAL(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (Modifier.isStatic(var4)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().LIFECYCLE_INTERCEPTOR_METHOD_NOT_BE_STATIC(var1.getName(), var1.getDeclaringClass().getName())));
      }

      Class var5 = var1.getReturnType();
      Class[] var6 = var1.getExceptionTypes();
      if (!var5.equals(Void.TYPE)) {
         if (var2) {
            var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().LIFECYCLE_INTERCEPTOR_METHOD_WITH_INVALID_SIGNATURE(var1.getName(), var1.getDeclaringClass().getName(), "void <METHOD>()")));
         } else {
            var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().LIFECYCLE_INTERCEPTOR_METHOD_WITH_INVALID_SIGNATURE(var1.getName(), var1.getDeclaringClass().getName(), "void <METHOD>(InvocationContext)")));
         }
      }

      if (!var3.isEmpty()) {
         throw var3;
      }
   }

   private void validateNotTwoSameTypeLifecycleCallbackPerClass(LifecycleCallbackBean[] var1, String var2) throws ComplianceException {
      if (var1 != null && var1.length > 1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3].getLifecycleCallbackClass();

            for(int var5 = var3 + 1; var5 < var1.length; ++var5) {
               if (var4.equals(var1[var5].getLifecycleCallbackClass())) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().TWO_LIFECYCLE_INTERCEPTOR_METHOD_IN_BEAN(var2.toString(), var4.toString()));
               }
            }
         }
      }

   }

   private Set getAllLifecycleCallbackMethodsFromBean() throws ComplianceException {
      HashSet var1 = new HashSet();
      SessionBeanBean[] var2 = this.ejbJarBean.getEnterpriseBeans().getSessions();

      int var3;
      BeanInfo var4;
      Set var6;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var4 = this.deploymentInfo.getBeanInfo(var2[var3].getEjbName());
         if (var4.isEJB30()) {
            SessionBeanBean var5 = (SessionBeanBean)var2[var3];
            var6 = InterceptorHelper.getPostConstructCallbackMethodinBean(this.classLoader, var5);
            var1.addAll(var6);
            var6 = InterceptorHelper.getPreDestroyCallbackMethodinBean(this.classLoader, var5);
            var1.addAll(var6);
            SessionBeanInfo var7 = (SessionBeanInfo)var4;
            if (var7.isStateful()) {
               var6 = InterceptorHelper.getPostActivateCallbackMethodinBean(this.classLoader, var5);
               var1.addAll(var6);
               var6 = InterceptorHelper.getPrePassivateCallbackMethodinBean(this.classLoader, var5);
               var1.addAll(var6);
            }
         }
      }

      MessageDrivenBeanBean[] var8 = this.ejbJarBean.getEnterpriseBeans().getMessageDrivens();

      for(var3 = 0; var3 < var8.length; ++var3) {
         var4 = this.deploymentInfo.getBeanInfo(var8[var3].getEjbName());
         if (var4.isEJB30()) {
            MessageDrivenBeanBean var9 = (MessageDrivenBeanBean)var8[var3];
            var6 = InterceptorHelper.getPostConstructCallbackMethodinBean(this.classLoader, var9);
            var1.addAll(var6);
            var6 = InterceptorHelper.getPreDestroyCallbackMethodinBean(this.classLoader, var9);
            var1.addAll(var6);
         }
      }

      return var1;
   }

   private Set getAllLifecycleCallbackMethodsFromInterceptor() throws ComplianceException {
      HashSet var1 = new HashSet();
      if (this.interceptorsBean != null) {
         InterceptorBean[] var2 = this.interceptorsBean.getInterceptors();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Set var4 = InterceptorHelper.getLifecycleCallbackMethodinInterceptor(this.classLoader, var2[var3]);
            var1.addAll(var4);
         }
      }

      return var1;
   }
}
