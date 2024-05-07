package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.utils.ErrorCollectionException;

abstract class HomeInterfaceChecker extends BaseComplianceChecker {
   protected Class homeInterface;
   protected Class compInterface;
   protected Class beanClass;
   private boolean isEntityBean;
   protected String ejbName;
   protected Class ejbHomeInterface;
   private ClientDrivenBeanInfo cbi;

   HomeInterfaceChecker(Class var1, Class var2, Class var3, ClientDrivenBeanInfo var4, Class var5) {
      this.homeInterface = var1;
      this.compInterface = var2;
      this.beanClass = var3;
      this.isEntityBean = var4 instanceof EntityBeanInfo;
      this.ejbName = var4.getEJBName();
      this.ejbHomeInterface = var5;
      this.cbi = var4;
   }

   protected String section(String var1, String var2) {
      return this.isEntityBean ? var2 : var1;
   }

   protected List getCreateMethods() {
      Method[] var1 = this.homeInterface.getMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getName().startsWith("create")) {
            var2.add(var1[var3]);
         }
      }

      return var2;
   }

   protected List getHomeMethods() {
      ArrayList var1 = new ArrayList();
      Method[] var2 = this.beanClass.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().startsWith("ejbHome")) {
            var1.add(var2[var3]);
         }
      }

      return var1;
   }

   protected List getHomeInterfaceHomeMethods() {
      ArrayList var1 = new ArrayList();
      Method[] var2 = this.homeInterface.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Method var4 = var2[var3];
         String var5 = var4.getName();
         boolean var6 = var5.startsWith("create") || var5.startsWith("find");
         boolean var7 = var4.getDeclaringClass().equals(EJBHome.class);
         boolean var8 = "<clinit>".equals(var5);
         if (!var6 && !var7 && !var8) {
            var1.add(var4);
         }
      }

      return var1;
   }

   protected boolean checkingRemoteClientView() {
      return this.ejbHomeInterface.equals(EJBHome.class);
   }

   public void checkExtendsEJBHome() throws ComplianceException {
      if (!this.ejbHomeInterface.isAssignableFrom(this.homeInterface)) {
         if (this.checkingRemoteClientView()) {
            throw new ComplianceException(this.fmt.HOME_EXTENDS_EJBHOME(this.ejbName, this.homeInterface.getName()));
         } else {
            throw new ComplianceException(this.fmt.LOCAL_HOME_EXTENDS_EJBLOCALHOME(this.ejbName, this.homeInterface.getName()));
         }
      }
   }

   public void checkRMIIIOPTypes() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      if (this.checkingRemoteClientView()) {
         Method[] var2 = this.homeInterface.getDeclaredMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Method var4 = var2[var3];
            Class var5 = var4.getReturnType();
            if (!ComplianceUtils.isLegalRMIIIOPType(var5)) {
               var1.add(new ComplianceException(this.fmt.NOT_RMIIIOP_LEGAL_TYPE_20(this.ejbName)));
            }

            Class[] var6 = var4.getParameterTypes();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var5 = var6[var7];
               if (!ComplianceUtils.isLegalRMIIIOPType(var5)) {
                  var1.add(new ComplianceException(this.fmt.NOT_RMIIIOP_LEGAL_TYPE_20(this.ejbName)));
               }
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkMethodsThrowRemoteException() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Method[] var2 = this.homeInterface.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].getName().equals("<clinit>")) {
            if (this.checkingRemoteClientView()) {
               if (!ComplianceUtils.methodThrowsException(var2[var3], RemoteException.class)) {
                  var1.add(new ComplianceException(this.fmt.HOME_METHOD_NOT_THROW_REMOTE_EXCEPTION(this.ejbName, this.methodSig(var2[var3]))));
               }
            } else if (ComplianceUtils.methodThrowsException(var2[var3], RemoteException.class)) {
               var1.add(new ComplianceException(this.fmt.LOCAL_HOME_METHOD_THROW_REMOTE_EXCEPTION(this.ejbName, this.methodSig(var2[var3]))));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkApplicationExceptions() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.getHomeInterfaceHomeMethods().iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         Class[] var4 = var3.getExceptionTypes();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            Class var6 = var4[var5];
            boolean var7 = false;
            if (this.checkingRemoteClientView()) {
               var7 = ComplianceUtils.checkApplicationException(var6, RemoteException.class);
            } else {
               var7 = ComplianceUtils.checkApplicationException(var6, EJBException.class);
            }

            if (!var7) {
               var1.add(new ComplianceException(this.fmt.INVALID_APPLICATION_EXCEPTION_ON_HOME(this.ejbName, this.methodSig(var3), var3.getDeclaringClass().getName(), var6.getName())));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkCreateReturnsCompInterface() throws ErrorCollectionException {
      Iterator var1 = this.getCreateMethods().iterator();
      ErrorCollectionException var2 = new ErrorCollectionException();

      while(var1.hasNext()) {
         Method var3 = (Method)var1.next();
         Class var4 = var3.getReturnType();
         if (!var4.equals(this.compInterface)) {
            if (this.checkingRemoteClientView()) {
               var2.add(new ComplianceException(this.fmt.CREATE_METHOD_RETURNS_COMPONENT_INTERFACE(this.ejbName, this.methodSig(var3))));
            } else {
               var2.add(new ComplianceException(this.fmt.CREATE_METHOD_RETURNS_LOCAL_COMPONENT_INTERFACE(this.ejbName, this.methodSig(var3))));
            }
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkCreateThrowsCreateException() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.getCreateMethods().iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         if (!ComplianceUtils.methodThrowsException(var3, CreateException.class)) {
            if (this.checkingRemoteClientView()) {
               var1.add(new ComplianceException(this.fmt.CREATE_METHOD_THROWS_CREATE_EXCEPTION(this.ejbName, this.methodSig(var3))));
            } else {
               var1.add(new ComplianceException(this.fmt.LOCAL_CREATE_METHOD_THROWS_CREATE_EXCEPTION(this.ejbName, this.methodSig(var3))));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkCreateMethodsMatchBeanCreates() throws ErrorCollectionException {
      if (this.cbi.isEJB30()) {
         if (!(this.cbi instanceof Ejb3SessionBeanInfo)) {
            return;
         }

         if (!((Ejb3SessionBeanInfo)this.cbi).isStateful()) {
            return;
         }
      }

      Iterator var1 = this.getCreateMethods().iterator();
      ErrorCollectionException var2 = new ErrorCollectionException();

      while(var1.hasNext()) {
         Method var3 = (Method)var1.next();
         String var4 = var3.getName();
         String var5;
         if (this.cbi.isEJB30()) {
            var5 = ((Ejb3SessionBeanInfo)this.cbi).getEjbCreateInitMethodName(var3);
         } else {
            var5 = "ejbC" + var4.substring(1, var4.length());
         }

         try {
            Method var6 = this.beanClass.getMethod(var5, var3.getParameterTypes());
            ComplianceUtils.exceptionTypesMatch(var3, var6);
         } catch (NoSuchMethodException var7) {
            if (this.checkingRemoteClientView()) {
               var2.add(new ComplianceException(this.fmt.CREATE_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, this.methodSig(var5, var3.getParameterTypes()))));
            } else {
               var2.add(new ComplianceException(this.fmt.LOCAL_CREATE_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, this.methodSig(var5, var3.getParameterTypes()))));
            }
         } catch (ExceptionTypeMismatchException var8) {
            if (this.checkingRemoteClientView()) {
               var2.add(new ComplianceException(this.fmt.CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var5, var3.getParameterTypes()))));
            } else {
               var2.add(new ComplianceException(this.fmt.LOCAL_CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var5, var3.getParameterTypes()))));
            }
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkLocalExposeThroughRemote() throws ErrorCollectionException {
      if (this.checkingRemoteClientView()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         Method[] var2 = this.homeInterface.getMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (ComplianceUtils.localExposeThroughRemote(var2[var3])) {
               var1.add(new ComplianceException(this.fmt.LOCAL_INTERFACE_TYPES_EXPOSE_THROUGH_HOME_INTERFACE(this.ejbName, this.methodSig(var2[var3]))));
            }
         }

         if (!var1.isEmpty()) {
            throw var1;
         }
      }
   }
}
