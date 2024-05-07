package weblogic.ejb.container.compliance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.utils.ErrorCollectionException;

final class InterceptorChecker extends BaseComplianceChecker {
   private DeploymentInfo deploymentInfo;
   private EjbDescriptorBean ejbDescriptor;
   private EjbJarBean ejbJarBean;
   private InterceptorsBean interceptorsBean;
   private BusinessMethodInterceptorChecker businessMethodInterceptorChecker;
   private LifecycleCallbackInterceptorChecker lifecycleCallbackInterceptorChecker;
   private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

   public InterceptorChecker(DeploymentInfo var1) {
      this.deploymentInfo = var1;
      this.ejbDescriptor = this.deploymentInfo.getEjbDescriptorBean();
      this.ejbJarBean = this.ejbDescriptor.getEjbJarBean();
      this.interceptorsBean = this.ejbJarBean.getInterceptors();
      this.businessMethodInterceptorChecker = new BusinessMethodInterceptorChecker(var1);
      this.lifecycleCallbackInterceptorChecker = new LifecycleCallbackInterceptorChecker(var1);
   }

   public void checkInterceptorNotDeclareTwice() throws ComplianceException {
      if (this.interceptorsBean != null) {
         InterceptorBean[] var1 = this.interceptorsBean.getInterceptors();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getInterceptorClass();

            for(int var4 = var2 + 1; var4 < var1.length; ++var4) {
               if (var1[var4].getInterceptorClass().equals(var3)) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_CLASS_DECLARED_IN_DD(var3.toString()));
               }
            }
         }
      }

   }

   public void checkOnlyOneAroundInvokeMethodperClass() throws ComplianceException {
      this.businessMethodInterceptorChecker.checkOnlyOneAroundInvokeMethodperClass();
   }

   public void checkOnlyOneLifecycleCallbackMethodperLEperClass() throws ComplianceException {
      this.lifecycleCallbackInterceptorChecker.checkOnlyOneLifecycleCallbackMethodperLEperClass();
   }

   public void checkAroundInvokeMethods() throws ErrorCollectionException, ComplianceException {
      this.businessMethodInterceptorChecker.checkAroundInvokeMethods();
   }

   public void checkLifecycleCallbackMethods() throws ErrorCollectionException, ComplianceException {
      this.lifecycleCallbackInterceptorChecker.checkLifecycleCallbackMethods();
   }

   public void checkDefaultConstructorInInterceptorClass() throws ComplianceException {
      if (this.interceptorsBean != null) {
         InterceptorBean[] var1 = this.interceptorsBean.getInterceptors();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getInterceptorClass();

            try {
               Class var4 = this.classLoader.loadClass(var3);
               Constructor var5 = var4.getConstructor();
               int var6 = var5.getModifiers();
               if (!Modifier.isPublic(var6)) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_CLASS_WITHOUT_NOARG_CONSTRUCTOR(var3.toString()));
               }

               var6 = var4.getModifiers();
               if (Modifier.isFinal(var6)) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().AROUNDINVOKE_METHOD_CANNOT_BE_FINAL(var3.toString()));
               }
            } catch (ClassNotFoundException var7) {
               throw new RuntimeException(var7);
            } catch (NoSuchMethodException var8) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_CLASS_WITHOUT_NOARG_CONSTRUCTOR(var3.toString()));
            }
         }
      }

   }
}
