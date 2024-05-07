package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.AroundInvokeBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.utils.ErrorCollectionException;

public class BusinessMethodInterceptorChecker {
   private DeploymentInfo deploymentInfo;
   private EjbDescriptorBean ejbDescriptor;
   private EjbJarBean ejbJarBean;
   private InterceptorsBean interceptorsBean;
   private Collection beanInfos;
   private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

   public BusinessMethodInterceptorChecker(DeploymentInfo var1) {
      this.deploymentInfo = var1;
      this.ejbDescriptor = this.deploymentInfo.getEjbDescriptorBean();
      this.ejbJarBean = this.ejbDescriptor.getEjbJarBean();
      this.interceptorsBean = this.ejbJarBean.getInterceptors();
      this.beanInfos = var1.getBeanInfos();
   }

   public void checkOnlyOneAroundInvokeMethodperClass() throws ComplianceException {
      AroundInvokeBean[] var1 = null;
      SessionBeanBean[] var2 = this.ejbJarBean.getEnterpriseBeans().getSessions();

      int var3;
      BeanInfo var4;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var4 = this.deploymentInfo.getBeanInfo(var2[var3].getEjbName());
         if (var4.isEJB30()) {
            SessionBeanBean var5 = (SessionBeanBean)var2[var3];
            var1 = var5.getAroundInvokes();
            this.validateNotTwoAroundInvokeInOneClass(var1);
         }
      }

      MessageDrivenBeanBean[] var6 = this.ejbJarBean.getEnterpriseBeans().getMessageDrivens();

      for(var3 = 0; var3 < var6.length; ++var3) {
         var4 = this.deploymentInfo.getBeanInfo(var6[var3].getEjbName());
         if (var4.isEJB30()) {
            MessageDrivenBeanBean var9 = (MessageDrivenBeanBean)var6[var3];
            var1 = var9.getAroundInvokes();
            this.validateNotTwoAroundInvokeInOneClass(var1);
         }
      }

      if (this.interceptorsBean != null) {
         InterceptorBean[] var7 = this.interceptorsBean.getInterceptors();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var1 = var7[var8].getAroundInvokes();
            this.validateNotTwoAroundInvokeInOneClass(var1);
         }
      }

   }

   public void checkAroundInvokeMethods() throws ErrorCollectionException, ComplianceException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Set var2 = this.getAllAroundInvokeMethods();
      Set var3 = InterceptorHelper.getAllBusinessMethods(this.beanInfos);
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Method var5 = (Method)var4.next();

         try {
            this.validateAroundInvokeMethod(var5, var3);
         } catch (ErrorCollectionException var7) {
            var1.add(var7);
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void validateNotTwoAroundInvokeInOneClass(AroundInvokeBean[] var1) throws ComplianceException {
      if (var1 != null && var1.length > 1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getClassName();

            for(int var4 = var2 + 1; var4 < var1.length; ++var4) {
               if (var3.equals(var1[var4].getClassName())) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().TWO_ARROUNDINVOKE_METHOD(var3.toString()));
               }
            }
         }
      }

   }

   private void validateAroundInvokeMethod(Method var1, Set var2) throws ErrorCollectionException {
      ErrorCollectionException var3 = new ErrorCollectionException();
      int var4 = var1.getModifiers();
      if (var2.contains(var1)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().ARROUNDINVOKE_METHOD_CANNOT_BUSINESS_METHOD(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (Modifier.isFinal(var4)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().ARROUNDINVOKE_METHOD_CANNOT_BE_FINAL(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (Modifier.isStatic(var4)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().ARROUNDINVOKE_METHOD_CANNOT_BE_STATIC(var1.getName(), var1.getDeclaringClass().getName())));
      }

      Class var5 = var1.getReturnType();
      Class[] var6 = var1.getExceptionTypes();
      if (!var5.equals(Object.class)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().ARROUNDINVOKE_METHOD_IS_INVALID(var1.getName(), var1.getDeclaringClass().getName())));
      } else if (var6 == null || var6.length == 0 || var6.length > 1 || !var6[0].equals(Exception.class)) {
         var3.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().ARROUNDINVOKE_METHOD_IS_INVALID(var1.getName(), var1.getDeclaringClass().getName())));
      }

      if (!var3.isEmpty()) {
         throw var3;
      }
   }

   private Set getAllAroundInvokeMethods() throws ComplianceException {
      HashSet var1 = new HashSet();
      Object var2 = null;
      SessionBeanBean[] var3 = this.ejbJarBean.getEnterpriseBeans().getSessions();

      int var4;
      BeanInfo var5;
      Set var7;
      for(var4 = 0; var4 < var3.length; ++var4) {
         var5 = this.deploymentInfo.getBeanInfo(var3[var4].getEjbName());
         if (var5.isEJB30()) {
            SessionBeanBean var6 = (SessionBeanBean)var3[var4];
            var7 = InterceptorHelper.getAroundInvokeMethodinBean(this.classLoader, var6);
            var1.addAll(var7);
         }
      }

      MessageDrivenBeanBean[] var8 = this.ejbJarBean.getEnterpriseBeans().getMessageDrivens();

      for(var4 = 0; var4 < var8.length; ++var4) {
         var5 = this.deploymentInfo.getBeanInfo(var8[var4].getEjbName());
         if (var5.isEJB30()) {
            MessageDrivenBeanBean var11 = (MessageDrivenBeanBean)var8[var4];
            var7 = InterceptorHelper.getAroundInvokeMethodinBean(this.classLoader, var11);
            var1.addAll(var7);
         }
      }

      if (this.interceptorsBean != null) {
         InterceptorBean[] var9 = this.interceptorsBean.getInterceptors();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            Set var12 = InterceptorHelper.getAroundInvokeMethodinInterceptor(this.classLoader, var9[var10]);
            var1.addAll(var12);
         }
      }

      return var1;
   }
}
