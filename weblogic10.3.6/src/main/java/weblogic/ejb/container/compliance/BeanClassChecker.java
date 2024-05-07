package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionSynchronization;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.utils.ErrorCollectionException;

abstract class BeanClassChecker extends BaseComplianceChecker {
   protected Class beanClass;
   protected int beanClassMod;
   private Class remoteClass;
   private Class localClass;
   protected ClientDrivenBeanInfo beanInfo;
   protected String ejbName;

   BeanClassChecker(ClientDrivenBeanInfo var1) throws ClassNotFoundException {
      this.beanClass = var1.getBeanClass();
      this.beanClassMod = this.beanClass.getModifiers();
      this.beanInfo = var1;
      this.remoteClass = this.beanInfo.getRemoteInterfaceClass();
      this.localClass = this.beanInfo.getLocalInterfaceClass();
      this.ejbName = this.beanInfo.getEJBName();
   }

   protected String section(String var1, String var2) {
      return this.beanInfo instanceof EntityBeanInfo ? var2 : var1;
   }

   protected List getCreateMethods() {
      Method[] var1 = this.beanClass.getMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getName().startsWith("ejbCreate")) {
            var2.add(var1[var3]);
         }
      }

      return var2;
   }

   protected List getBusMethods() {
      ArrayList var1 = new ArrayList();
      Method[] var2;
      int var3;
      if (this.remoteClass != null) {
         var2 = this.remoteClass.getMethods();

         for(var3 = 0; var3 < var2.length; ++var3) {
            if (!this.isEJBObjectMethod(var2[var3])) {
               var1.add(var2[var3]);
            }
         }
      }

      if (this.localClass != null) {
         var2 = this.localClass.getMethods();

         for(var3 = 0; var3 < var2.length; ++var3) {
            if (!this.isEJBObjectMethod(var2[var3])) {
               var1.add(var2[var3]);
            }
         }
      }

      return var1;
   }

   private boolean isEJBObjectMethod(Method var1) {
      Class var2 = var1.getDeclaringClass();
      return EJBObject.class.equals(var2) || EJBLocalObject.class.equals(var2);
   }

   abstract void validateCreateReturnType(Method var1) throws ComplianceException;

   protected void validateEjbCreates(List var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Method var4 = (Method)var3.next();
         int var5 = var4.getModifiers();
         if (!Modifier.isPublic(var5)) {
            var2.add(new ComplianceException(this.fmt.PUBLIC_EJBCREATE(this.ejbName)));
         }

         if (Modifier.isFinal(var5)) {
            var2.add(new ComplianceException(this.fmt.FINAL_EJBCREATE(this.ejbName)));
         }

         if (Modifier.isStatic(var5)) {
            var2.add(new ComplianceException(this.fmt.STATIC_EJBCREATE(this.ejbName)));
         }

         try {
            this.validateCreateReturnType(var4);
         } catch (ComplianceException var7) {
            var2.add(var7);
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkRemoteView() throws ComplianceException {
      if (this.beanInfo.getHomeInterfaceName() != null && this.beanInfo.getRemoteInterfaceName() == null || this.beanInfo.getHomeInterfaceName() == null && this.beanInfo.getRemoteInterfaceName() != null) {
         throw new ComplianceException(this.fmt.INCONSISTENT_REMOTE_VIEW(this.ejbName));
      }
   }

   public void checkLocalView() throws ComplianceException {
      if (this.beanInfo.getLocalHomeInterfaceName() != null && this.beanInfo.getLocalInterfaceName() == null || this.beanInfo.getLocalHomeInterfaceName() == null && this.beanInfo.getLocalInterfaceName() != null) {
         throw new ComplianceException(this.fmt.INCONSISTENT_LOCAL_VIEW(this.ejbName));
      }
   }

   public void checkClientView() throws ComplianceException {
      if (!this.beanInfo.isEJB30()) {
         if (!this.beanInfo.hasRemoteClientView()) {
            if (!this.beanInfo.hasLocalClientView()) {
               if (!this.beanInfo.hasWebserviceClientView()) {
                  throw new ComplianceException(this.fmt.NO_CLIENT_VIEW(this.ejbName));
               }
            }
         }
      }
   }

   public void checkSessionSynchronization() throws ComplianceException {
      if (SessionSynchronization.class.isAssignableFrom(this.beanClass)) {
         if (this.beanInfo instanceof EntityBeanInfo) {
            throw new ComplianceException(this.fmt.ENTITY_IMPLEMENT_SESSIONSYNCHRONIZATION(this.ejbName));
         }

         SessionBeanInfo var1 = (SessionBeanInfo)this.beanInfo;
         if (!var1.isStateful()) {
            throw new ComplianceException(this.fmt.STATELESS_IMPLEMENT_SESSIONSYNCHRONIZATION(this.ejbName));
         }

         if (var1.usesBeanManagedTx()) {
            throw new ComplianceException(this.fmt.BEAN_MANAGED_IMPLEMENT_SESSIONSYNCHRONIZATION(this.ejbName));
         }
      }

   }

   public void checkTransactionAttribute() throws ComplianceException {
      if (this.beanInfo.isTimerDriven()) {
         MethodInfo var1 = this.beanInfo.getBeanMethodInfo(DDUtils.getMethodSignature(this.beanInfo.getTimeoutMethod()));
         short var2 = var1.getTransactionAttribute();
         if (1 != var2 && 3 != var2 && 0 != var2) {
            throw new ComplianceException(this.fmt.EJB_TIMEOUT_BAD_TX_ATTRIBUTE(this.beanInfo.getDisplayName()));
         }
      }

   }

   public void checkBeanClassIsPublic() throws ComplianceException {
      if (!Modifier.isPublic(this.beanClassMod)) {
         throw new ComplianceException(this.fmt.PUBLIC_BEAN_CLASS(this.ejbName));
      }
   }

   public void checkBeanClassIsNotFinal() throws ComplianceException {
      if (Modifier.isFinal(this.beanClassMod)) {
         throw new ComplianceException(this.fmt.FINAL_BEAN_CLASS(this.ejbName));
      }
   }

   public void checkBeanClassHasPublicNoArgCtor() throws ComplianceException {
      if (!ComplianceUtils.classHasPublicNoArgCtor(this.beanClass)) {
         throw new ComplianceException(this.fmt.PUBLIC_NOARG_BEAN_CTOR(this.ejbName));
      }
   }

   public void checkBeanClassDoesNotDefineFinalize() throws ComplianceException {
      try {
         Method var1 = this.beanClass.getMethod("finalize", (Class[])null);
         throw new ComplianceException(this.fmt.NO_FINALIZE_IN_BEAN(this.ejbName));
      } catch (NoSuchMethodException var2) {
      }
   }

   public void checkBeanMethodsAreSynchronized() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Method[] var2 = this.beanClass.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         int var4 = var2[var3].getModifiers();
         if (Modifier.isSynchronized(var4)) {
            var1.add(new ComplianceException(this.fmt.NO_SYNCHRONIZED_METHODS(this.ejbName, this.methodSig(var2[var3]))));
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkLocalReferences() throws ErrorCollectionException {
      if (!this.beanInfo.isEJB30()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         Map var2 = this.beanInfo.getAllEJBLocalReferenceJNDINames();
         Iterator var3 = this.beanInfo.getAllEJBLocalReferences().iterator();

         while(true) {
            EjbLocalRefBean var4;
            String var6;
            do {
               String var5;
               do {
                  if (!var3.hasNext()) {
                     if (!var1.isEmpty()) {
                        throw var1;
                     }

                     return;
                  }

                  var4 = (EjbLocalRefBean)var3.next();
                  var5 = var4.getEjbLink();
               } while(var5 != null && var5.length() > 0);

               var6 = (String)var2.get(var4.getEjbRefName());
            } while(var6 != null && var6.length() != 0);

            var1.add(new ComplianceException(this.fmt.BEAN_MISSING_LREF_JNDI_NAME(this.ejbName, var4.getEjbRefName()), new DescriptorErrorInfo("<jndi-name>", this.ejbName, var6)));
         }
      }
   }

   public void checkReferences() throws ErrorCollectionException {
      if (!this.beanInfo.isEJB30()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         Map var2 = this.beanInfo.getAllEJBReferenceJNDINames();
         Iterator var3 = this.beanInfo.getAllEJBReferences().iterator();

         while(true) {
            EjbRefBean var4;
            String var6;
            do {
               String var5;
               do {
                  if (!var3.hasNext()) {
                     if (!var1.isEmpty()) {
                        throw var1;
                     }

                     return;
                  }

                  var4 = (EjbRefBean)var3.next();
                  var5 = var4.getEjbLink();
               } while(var5 != null && var5.length() > 0);

               var6 = (String)var2.get(var4.getEjbRefName());
            } while(var6 != null && var6.length() != 0);

            var1.add(new ComplianceException(this.fmt.BEAN_MISSING_REF_JNDI_NAME(this.ejbName, var4.getEjbRefName())));
         }
      }
   }

   public void checkCallByReference() {
      if (this.beanInfo.hasRemoteClientView() && !this.beanInfo.useCallByReference() && !this.beanInfo.isWarningDisabled("BEA-010202")) {
         EJBLogger.logCallByReferenceNotEnabled(this.ejbName);
      }

   }

   public void checkBusinessMethods() throws ErrorCollectionException {
      List var1 = this.getBusMethods();
      ErrorCollectionException var2 = new ErrorCollectionException();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Method var4 = (Method)var3.next();
         Method var5 = null;

         try {
            var5 = ClassUtils.getDeclaredMethod(this.beanClass, var4.getName(), var4.getParameterTypes());
            String var6 = var5.getName();
            int var7 = var5.getModifiers();
            if (!Modifier.isPublic(var7)) {
               var2.add(new ComplianceException(this.fmt.BUS_METHOD_NOT_PUBLIC(this.ejbName, var6)));
            }

            if (Modifier.isFinal(var7)) {
               var2.add(new ComplianceException(this.fmt.BUS_METHOD_MUST_NOT_FINAL(this.ejbName, var6)));
            }

            if (Modifier.isStatic(var7)) {
               var2.add(new ComplianceException(this.fmt.BUS_METHOD_MUST_NOT_STATIC(this.ejbName, var6)));
            }
         } catch (NoSuchMethodException var8) {
            if (EJBObject.class.isAssignableFrom(var4.getDeclaringClass())) {
               var2.add(new ComplianceException(this.fmt.EO_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, DDUtils.getMethodSignature(var4))));
            } else {
               var2.add(new ComplianceException(this.fmt.ELO_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, DDUtils.getMethodSignature(var4))));
            }
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkTimeoutMethods() throws ErrorCollectionException, ComplianceException {
      TimeoutCheckHelper.validateTimeoutMethod(this.beanInfo);
   }

   public void checkAppExceptions() throws ErrorCollectionException {
      if (this.beanInfo.isEJB30()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         Map var2 = this.beanInfo.getDeploymentInfo().getApplicationExceptions();
         if (var2 != null) {
            Set var3 = var2.keySet();
            ClassLoader var4 = this.beanInfo.getClassLoader();
            Iterator var5 = var3.iterator();
            Class var6 = null;

            while(var5.hasNext()) {
               Object var7 = var5.next();

               try {
                  var6 = var4 == null ? Class.forName((String)var7) : var4.loadClass((String)var7);
               } catch (ClassNotFoundException var9) {
                  var1.add(var9);
                  continue;
               }

               if (RemoteException.class.isAssignableFrom(var6)) {
                  var1.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().EXCEPTION_CANNOT_EXTEND_REMOTEEXCEPTION(this.ejbName)));
               }
            }

            if (!var1.isEmpty()) {
               throw var1;
            }
         }
      }
   }
}
