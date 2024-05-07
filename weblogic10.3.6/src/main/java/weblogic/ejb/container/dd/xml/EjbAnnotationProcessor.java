package weblogic.ejb.container.dd.xml;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.ApplicationException;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.Init;
import javax.ejb.Local;
import javax.ejb.LocalHome;
import javax.ejb.MessageDriven;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.AroundInvoke;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.jms.MessageListener;
import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import org.apache.openjpa.lib.meta.ClassAnnotationMetaDataFilter;
import weblogic.application.utils.VirtualJarFileMetaDataIterator;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.ComplianceException;
import weblogic.ejb.container.compliance.EJBComplianceChecker;
import weblogic.ejb.container.compliance.Ejb30AnnotationChecker;
import weblogic.ejb.container.compliance.TimeoutCheckHelper;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.dd.xml.J2eeAnnotationProcessor;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.ActivationConfigPropertyBean;
import weblogic.j2ee.descriptor.ApplicationExceptionBean;
import weblogic.j2ee.descriptor.AroundInvokeBean;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbCallbackBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.ExcludeListBean;
import weblogic.j2ee.descriptor.InitMethodBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorBindingBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.J2eeClientEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.MethodBean;
import weblogic.j2ee.descriptor.MethodParamsBean;
import weblogic.j2ee.descriptor.MethodPermissionBean;
import weblogic.j2ee.descriptor.NamedMethodBean;
import weblogic.j2ee.descriptor.RemoveMethodBean;
import weblogic.j2ee.descriptor.RunAsBean;
import weblogic.j2ee.descriptor.SecurityIdentityBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.BusinessInterfaceJndiNameMapBean;
import weblogic.j2ee.descriptor.wl.IdempotentMethodsBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;
import weblogic.j2ee.descriptor.wl.RetryMethodsOnRollbackBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.StatelessSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.j2ee.descriptor.wl.TransactionIsolationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.javaee.AllowRemoveDuringTransaction;
import weblogic.javaee.CallByReference;
import weblogic.javaee.DisableWarnings;
import weblogic.javaee.Idempotent;
import weblogic.javaee.JMSClientID;
import weblogic.javaee.JNDIName;
import weblogic.javaee.MessageDestinationConfiguration;
import weblogic.javaee.TransactionIsolation;
import weblogic.javaee.TransactionTimeoutSeconds;
import weblogic.javaee.WarningCode;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.wsee.deploy.DeployUtil;

public class EjbAnnotationProcessor extends J2eeAnnotationProcessor {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final Class[] identityAnnotations = new Class[]{Stateful.class, Stateless.class, MessageDriven.class, ApplicationException.class};
   GenericClassLoader cl = null;
   private Boolean disableWarningsIsInXML = null;

   public EjbAnnotationProcessor(GenericClassLoader var1) {
      this.cl = var1;
   }

   public void processAnnotations(EjbDescriptorBean var1, VirtualJarFile var2) throws ClassNotFoundException, NoSuchMethodException, ErrorCollectionException {
      TreeSet var3 = new TreeSet(new Comparator<Class>() {
         public int compare(Class var1, Class var2) {
            return var1.getCanonicalName().compareTo(var2.getCanonicalName());
         }
      });
      File[] var4 = var2.getRootFiles();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].exists()) {
            VirtualJarFile var6 = null;
            String var7 = null;

            try {
               Loggable var9;
               try {
                  var6 = VirtualJarFactory.createVirtualJar(var4[var5]);
                  VirtualJarFileMetaDataIterator var8 = new VirtualJarFileMetaDataIterator(var6, new ClassAnnotationMetaDataFilter(identityAnnotations));

                  while(var8.hasNext()) {
                     var7 = (String)var8.next();
                     var7 = var7.replace('/', '.');
                     var7 = var7.substring(0, var7.length() - 6);
                     Class var29 = this.cl.loadClass(var7);
                     var29.getAnnotations();
                     var3.add(var29);
                  }
               } catch (LinkageError var24) {
                  var9 = EJBLogger.logUnableLinkClassLoggable(var7, var4[var5].toString(), var24.toString());
                  this.addFatalProcessingError(var9.getMessage());
               } catch (ClassNotFoundException var25) {
                  var9 = EJBLogger.logUnableLoadClassLoggable(var7, var4[var5].toString(), var25.toString());
                  this.addFatalProcessingError(var9.getMessage());
               } catch (IOException var26) {
                  var9 = EJBLogger.logUnableCreateJarLoggable(var4[var5].toString(), var26.toString());
                  this.addFatalProcessingError(var9.getMessage());
               } catch (ArrayStoreException var27) {
                  var9 = EJBLogger.logUnableLoadClassLoggable(var7, var4[var5].toString(), var27.toString());
                  this.addFatalProcessingError(var9.getMessage());
               }
            } finally {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (IOException var23) {
                  }
               }

            }
         }
      }

      this.processAnnotations((Set)var3, (EjbDescriptorBean)var1);
      if (!EJBComplianceChecker.isNeedCheck) {
         this.validate(this.cl, (DescriptorBean)var1.getEjbJarBean(), false);
         this.throwProcessingErrors();
      }
   }

   private void processAnnotations(Set<Class> var1, EjbDescriptorBean var2) throws ClassNotFoundException, NoSuchMethodException, ErrorCollectionException {
      HashSet var3 = new HashSet();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      EjbJarBean var6 = var2.getEjbJarBean();
      if (var6 == null) {
         var6 = var2.createEjbJarBean();
      }

      EnterpriseBeansBean var7 = var6.getEnterpriseBeans();
      if (var7 == null) {
         var7 = var6.createEnterpriseBeans();
      }

      SessionBeanBean[] var8 = var7.getSessions();
      int var9 = var8.length;

      int var10;
      for(var10 = 0; var10 < var9; ++var10) {
         SessionBeanBean var11 = var8[var10];
         var3.add(var11.getEjbClass());
         var4.put(var11.getEjbName(), var11);
      }

      MessageDrivenBeanBean[] var17 = var7.getMessageDrivens();
      var9 = var17.length;

      for(var10 = 0; var10 < var9; ++var10) {
         MessageDrivenBeanBean var22 = var17[var10];
         var3.add(var22.getEjbClass());
         var4.put(var22.getEjbName(), var22);
      }

      HashSet var18 = new HashSet();
      Iterator var19 = var1.iterator();

      while(var19.hasNext()) {
         Class var23 = (Class)var19.next();
         String var12;
         String[] var13;
         if (var23.isAnnotationPresent(Stateless.class)) {
            Stateless var26 = (Stateless)var23.getAnnotation(Stateless.class);
            var12 = this.getEjbName(var26.name(), var23);

            try {
               var13 = new String[]{"Stateless", var23.getName()};
               Ejb30AnnotationChecker.validateNameAnnotation(var12, var5, var13);
            } catch (ComplianceException var14) {
               this.addProcessingError(var14.getMessage());
               break;
            }

            if (!this.ensureBeanClassSet((EnterpriseBeanBean)var4.get(var12), var23) && !var3.contains(var23.getName())) {
               this.addStatelessSessionBean(var12, var23, var7);
            }
         } else if (var23.isAnnotationPresent(Stateful.class)) {
            Stateful var25 = (Stateful)var23.getAnnotation(Stateful.class);
            var12 = this.getEjbName(var25.name(), var23);

            try {
               var13 = new String[]{"Stateful", var23.getName()};
               Ejb30AnnotationChecker.validateNameAnnotation(var12, var5, var13);
            } catch (ComplianceException var15) {
               this.addProcessingError(var15.getMessage());
               break;
            }

            if (!this.ensureBeanClassSet((EnterpriseBeanBean)var4.get(var12), var23) && !var3.contains(var23.getName())) {
               this.addStatefulSessionBean(var12, var23, var7);
            }
         } else if (var23.isAnnotationPresent(MessageDriven.class)) {
            MessageDriven var24 = (MessageDriven)var23.getAnnotation(MessageDriven.class);
            var12 = this.getEjbName(var24.name(), var23);

            try {
               var13 = new String[]{"MessageDriven", var23.getName()};
               Ejb30AnnotationChecker.validateNameAnnotation(var12, var5, var13);
            } catch (ComplianceException var16) {
               this.addProcessingError(var16.getMessage());
               break;
            }

            if (!this.ensureBeanClassSet((EnterpriseBeanBean)var4.get(var12), var23) && !var3.contains(var23.getName())) {
               this.addMessageDrivenBean(var12, var23, var7);
            }
         } else if (var23.isAnnotationPresent(ApplicationException.class)) {
            var18.add(var23);
         }
      }

      this.processApplicationExceptions(var18, (EjbJarBean)var6);
      SessionBeanBean[] var20 = var7.getSessions();
      var10 = var20.length;

      int var27;
      for(var27 = 0; var27 < var10; ++var27) {
         SessionBeanBean var28 = var20[var27];
         this.processSessionAnnotations(var28, var2);
      }

      MessageDrivenBeanBean[] var21 = var7.getMessageDrivens();
      var10 = var21.length;

      for(var27 = 0; var27 < var10; ++var27) {
         MessageDrivenBeanBean var29 = var21[var27];
         this.processMessageDrivenAnnotations(var29, var2);
      }

      this.processInterceptorClasses(var2);
   }

   private boolean ensureBeanClassSet(EnterpriseBeanBean var1, Class var2) {
      if (var1 != null) {
         if (!this.isSet("EjbClass", var1)) {
            var1.setEjbClass(var2.getName());
         }

         return true;
      } else {
         return false;
      }
   }

   private String getEjbName(String var1, Class var2) {
      if (var1 == null || var1.length() == 0) {
         var1 = var2.getSimpleName();
      }

      return var1;
   }

   private void addStatelessSessionBean(String var1, Class var2, EnterpriseBeansBean var3) {
      SessionBeanBean var4 = var3.createSession();
      var4.setEjbName(var1);
      var4.setEjbClass(var2.getName());
      var4.setSessionType("Stateless");
   }

   private void addStatefulSessionBean(String var1, Class var2, EnterpriseBeansBean var3) {
      SessionBeanBean var4 = var3.createSession();
      var4.setEjbName(var1);
      var4.setEjbClass(var2.getName());
      var4.setSessionType("Stateful");
   }

   private void addMessageDrivenBean(String var1, Class var2, EnterpriseBeansBean var3) {
      MessageDrivenBeanBean var4 = var3.createMessageDriven();
      var4.setEjbName(var1);
      var4.setEjbClass(var2.getName());
   }

   private void processSessionAnnotations(SessionBeanBean var1, EjbDescriptorBean var2) throws ClassNotFoundException, NoSuchMethodException, ErrorCollectionException {
      Class var3 = this.loadBeanClass(var1.getEjbClass(), var1.getEjbName(), this.cl);
      if (var3.isAnnotationPresent(Stateful.class) || var3.isAnnotationPresent(Stateless.class)) {
         for(Class var4 = var3.getSuperclass(); !var4.equals(Object.class); var4 = var4.getSuperclass()) {
            if (var4.isAnnotationPresent(Stateful.class) || var4.isAnnotationPresent(Stateless.class)) {
               Loggable var5 = EJBLogger.logSessionBeanWithSessionBeanParentLoggable(var4.getName(), var1.getEjbClass());
               this.addProcessingError(var5.getMessage());
               break;
            }
         }
      }

      if (!this.isSet("SessionType", var1)) {
         if (var3.isAnnotationPresent(Stateful.class)) {
            var1.setSessionType("Stateful");
         } else if (var3.isAnnotationPresent(Stateless.class)) {
            var1.setSessionType("Stateless");
         } else {
            Loggable var25 = EJBLogger.logSessionBeanWithoutSetSessionTypeLoggable(var1.getEjbName());
            this.addFatalProcessingError(var25.getMessage());
         }
      }

      boolean var26 = var1.getSessionType().equals("Stateless");
      if (!this.isSet("Home", var1)) {
         RemoteHome var27 = (RemoteHome)var3.getAnnotation(RemoteHome.class);
         if (var27 != null) {
            var1.setHome(var27.value().getName());
         }
      }

      if (!this.isSet("LocalHome", var1)) {
         LocalHome var28 = (LocalHome)var3.getAnnotation(LocalHome.class);
         if (var28 != null) {
            var1.setLocalHome(var28.value().getName());
         }
      }

      HashSet var29 = new HashSet();
      HashSet var6 = new HashSet();
      Local var7 = (Local)var3.getAnnotation(Local.class);
      int var10;
      Class var11;
      if (var7 != null) {
         Class[] var8 = var7.value();
         int var9 = var8.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            var29.add(var11);
         }
      }

      Remote var30 = (Remote)var3.getAnnotation(Remote.class);
      Class var12;
      if (var30 != null) {
         Class[] var31 = var30.value();
         var10 = var31.length;

         for(int var34 = 0; var34 < var10; ++var34) {
            var12 = var31[var34];
            var6.add(var12);
         }
      }

      Set var32 = this.getImplementedInterfaces(var3);
      Iterator var33 = var32.iterator();

      while(var33.hasNext()) {
         var11 = (Class)var33.next();
         if (var11.isAnnotationPresent(Remote.class)) {
            var6.add(var11);
         } else if (var11.isAnnotationPresent(Local.class)) {
            var29.add(var11);
         }
      }

      int var13;
      int var14;
      String var15;
      if (var6.isEmpty() && var29.isEmpty() && var32.size() == 1) {
         Class var35 = (Class)var32.iterator().next();
         boolean var37 = false;
         String[] var38 = var1.getBusinessRemotes();
         var13 = var38.length;

         for(var14 = 0; var14 < var13; ++var14) {
            var15 = var38[var14];
            if (var35.getName().equals(var15)) {
               var37 = true;
               break;
            }
         }

         if (!var37) {
            if (!var3.isAnnotationPresent(Remote.class) && !this.isExtendRemote(var35)) {
               var29.add(var35);
            } else {
               var6.add(var35);
            }
         }
      }

      String var36;
      Method[] var40;
      Iterator var42;
      Method var48;
      if (var1.getHome() != null) {
         var36 = var1.getRemote();
         if (var36 == null) {
            var42 = var6.iterator();

            while(var42.hasNext()) {
               var12 = (Class)var42.next();
               if (EJBObject.class.isAssignableFrom(var12)) {
                  var1.setRemote(var12.getName());
                  var6.remove(var12);
                  break;
               }
            }

            if (var1.getRemote() == null) {
               var11 = this.cl.loadClass(var1.getHome());
               var40 = var11.getMethods();
               var13 = var40.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  var48 = var40[var14];
                  if (var48.getName().startsWith("create") && EJBObject.class.isAssignableFrom(var48.getReturnType())) {
                     var1.setRemote(var48.getReturnType().getName());
                     break;
                  }
               }
            }
         } else {
            var11 = this.cl.loadClass(var36);
            var6.remove(var11);
         }
      }

      if (var1.getLocalHome() != null) {
         var36 = var1.getLocal();
         if (var36 == null) {
            var42 = var29.iterator();

            while(var42.hasNext()) {
               var12 = (Class)var42.next();
               if (EJBLocalObject.class.isAssignableFrom(var12)) {
                  var1.setLocal(var12.getName());
                  var29.remove(var12);
                  break;
               }
            }

            if (var1.getLocal() == null) {
               var11 = this.cl.loadClass(var1.getLocalHome());
               var40 = var11.getMethods();
               var13 = var40.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  var48 = var40[var14];
                  if (var48.getName().startsWith("create") && EJBLocalObject.class.isAssignableFrom(var48.getReturnType())) {
                     var1.setLocal(var48.getReturnType().getName());
                     break;
                  }
               }
            }
         } else {
            var11 = this.cl.loadClass(var36);
            var29.remove(var11);
         }
      }

      HashSet var39;
      int var44;
      String[] var47;
      String var52;
      if (var1.getBusinessLocals().length == 0) {
         var39 = new HashSet();
         var47 = var1.getBusinessRemotes();
         var44 = var47.length;

         for(var13 = 0; var13 < var44; ++var13) {
            var52 = var47[var13];
            var39.add(var52);
         }

         var42 = var29.iterator();

         while(var42.hasNext()) {
            var12 = (Class)var42.next();
            if (!var39.contains(var12.getName())) {
               var1.addBusinessLocal(var12.getName());
            }
         }
      }

      if (var1.getBusinessRemotes().length == 0) {
         var39 = new HashSet();
         var47 = var1.getBusinessLocals();
         var44 = var47.length;

         for(var13 = 0; var13 < var44; ++var13) {
            var52 = var47[var13];
            var39.add(var52);
         }

         var42 = var6.iterator();

         while(var42.hasNext()) {
            var12 = (Class)var42.next();
            if (!var39.contains(var12.getName())) {
               var1.addBusinessRemote(var12.getName());
            }
         }
      }

      if (var1.getHome() == null && var1.getLocalHome() == null && var1.getServiceEndpoint() == null && var1.getBusinessLocals().length == 0 && var1.getBusinessRemotes().length == 0 && var32.size() > 0) {
         this.addFatalProcessingError("The session bean " + var1.getEjbName() + " does not have a client view specified.  Since the bean class " + var3 + " implements multiple interfaces, you must indicate which of these interfaces are local or remote business interfaces by means of the Local or Remote annotation or in the deployment descriptor.");
      }

      if (!this.isSet("TransactionType", var1)) {
         var1.setTransactionType(this.getTransactionType(var3));
      }

      if (var26) {
         Stateless var41 = (Stateless)var3.getAnnotation(Stateless.class);
         if (var41 != null && !this.isSet("MappedName", var1) && var41.mappedName().length() > 0) {
            var1.setMappedName(var41.mappedName());
         }
      } else {
         Stateful var43 = (Stateful)var3.getAnnotation(Stateful.class);
         if (var43 != null && !this.isSet("MappedName", var1) && var43.mappedName().length() > 0) {
            var1.setMappedName(var43.mappedName());
         }
      }

      if (var26) {
         var33 = null;
         var11 = null;

         Method var45;
         Method var55;
         try {
            var55 = this.findAnnotatedTimeoutMethod(var3);
            var45 = this.getTimeoutMethodByDD(var3, var1);
            TimeoutCheckHelper.validateTimeoutMethodsIdentical(var45, var55);
         } catch (ComplianceException var24) {
            throw new ErrorCollectionException(var24);
         }

         if (var45 == null && var55 != null) {
            this.populateMethodBean(var1.createTimeoutMethod(), var55);
         }
      }

      List var46;
      if (!var26 && var1.getInitMethods().length == 0) {
         var46 = this.findAnnotatedMethods(var3, Init.class, false);
         if (!var46.isEmpty() && (var1.getHome() != null || var1.getLocalHome() != null)) {
            var42 = var46.iterator();

            while(var42.hasNext()) {
               Method var49 = (Method)var42.next();
               Init var50 = (Init)var49.getAnnotation(Init.class);
               var52 = var50.value();
               var15 = var50.value();
               Method var16 = null;
               Method var17 = null;
               Loggable var20;
               if (var52 == null || var52.length() == 0) {
                  Class var18;
                  List var19;
                  if (var1.getHome() != null) {
                     var18 = this.cl.loadClass(var1.getHome());
                     var19 = ClassUtils.getMethodNamesForNameAndParams("create", var49.getParameterTypes(), var18.getMethods());
                     if (var19.size() == 0) {
                        var20 = EJBLogger.logNoMatchCreateMethodForInitMethodLoggable(var49.toString(), var1.getEjbName());
                        this.addProcessingError(var20.getMessage());
                     } else if (var19.size() > 1) {
                        this.addProcessingError("The value element of @Init annotation must be specified when the home interface:" + var18 + " of a stateful session bean:" + var3 + " that has more than one create<METHOD> method.");
                     } else {
                        var52 = (String)var19.get(0);
                     }
                  }

                  if (var1.getLocalHome() != null) {
                     var18 = this.cl.loadClass(var1.getLocalHome());
                     var19 = ClassUtils.getMethodNamesForNameAndParams("create", var49.getParameterTypes(), var18.getMethods());
                     if (var19.size() == 0) {
                        var20 = EJBLogger.logNoMatchCreateMethodForInitMethodLoggable(var49.toString(), var1.getEjbName());
                        this.addProcessingError(var20.getMessage());
                     } else if (var19.size() > 1) {
                        this.addProcessingError("The value element of @Init annotation must be specified when the home interface:" + var18 + " of a stateful session bean:" + var3 + " that has more than one create<METHOD> method.");
                     } else {
                        var15 = (String)var19.get(0);
                     }
                  }
               }

               boolean var61 = false;
               boolean var62 = false;

               Class var64;
               try {
                  if (var1.getHome() != null) {
                     var64 = this.cl.loadClass(var1.getHome());
                     var16 = var64.getMethod(var52, var49.getParameterTypes());
                     var61 = true;
                  }
               } catch (NoSuchMethodException var23) {
               }

               try {
                  if (var1.getLocalHome() != null) {
                     var64 = this.cl.loadClass(var1.getLocalHome());
                     var17 = var64.getMethod(var15, var49.getParameterTypes());
                     var62 = true;
                  }
               } catch (NoSuchMethodException var22) {
               }

               if (!var61 && !var62) {
                  var20 = EJBLogger.logNoMatchCreateMethodForInitMethodLoggable(var49.toString(), var1.getEjbName());
                  this.addProcessingError(var20.getMessage());
               }

               InitMethodBean var66;
               if (var61) {
                  var66 = var1.createInitMethod();
                  this.populateMethodBean(var66.createBeanMethod(), var49);
                  this.populateMethodBean(var66.createCreateMethod(), var16);
               }

               if (var62) {
                  var66 = var1.createInitMethod();
                  this.populateMethodBean(var66.createBeanMethod(), var49);
                  this.populateMethodBean(var66.createCreateMethod(), var17);
               }
            }
         }
      }

      if (!var26) {
         var46 = this.findAnnotatedMethods(var3, Remove.class, false);
         RemoveMethodBean[] var58 = var1.getRemoveMethods();
         RemoveMethodBean[] var51 = var58;
         var13 = var58.length;

         RemoveMethodBean var56;
         for(var14 = 0; var14 < var13; ++var14) {
            var56 = var51[var14];
            NamedMethodBean var57 = var56.getBeanMethod();
            String var59 = var57.getMethodName();
            MethodParamsBean var65 = var57.getMethodParams();
            String[] var63;
            if (var65 == null) {
               var63 = new String[0];
            } else {
               var63 = var65.getMethodParams();
            }

            Method var67 = ClassUtils.getMethodForNameAndParams(var59, var63, var46);
            if (!this.isSet("RetainIfException", var56) && var67 != null) {
               Remove var21 = (Remove)var67.getAnnotation(Remove.class);
               var56.setRetainIfException(var21.retainIfException());
            }

            if (var67 != null) {
               var46.remove(var67);
            }
         }

         Iterator var53 = var46.iterator();

         while(var53.hasNext()) {
            Method var54 = (Method)var53.next();
            Remove var60 = (Remove)var54.getAnnotation(Remove.class);
            var56 = var1.createRemoveMethod();
            this.populateMethodBean(var56.createBeanMethod(), var54);
            var56.setRetainIfException(var60.retainIfException());
         }
      }

      this.processEjbCallbackAnnotations(var3, var1);
      this.processJ2eeAnnotations(var3, var1);
      this.processDeclareRoles(var3, (DescriptorBean)var1);
      this.processRunAs(var3, (DescriptorBean)var1);
      this.processAssemblyDescriptor(var1, var3, this.getInterfaceNames(var1), var2);
   }

   private Set<String> getInterfaceNames(SessionBeanBean var1) {
      HashSet var2 = new HashSet();
      if (var1.getRemote() != null) {
         var2.add(var1.getRemote());
      }

      if (var1.getLocal() != null) {
         var2.add(var1.getLocal());
      }

      String[] var3 = var1.getBusinessLocals();
      int var4 = var3.length;

      int var5;
      String var6;
      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var3[var5];
         var2.add(var6);
      }

      var3 = var1.getBusinessRemotes();
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var3[var5];
         var2.add(var6);
      }

      return var2;
   }

   private Set<String> getInterfaceNames(MessageDrivenBeanBean var1) {
      HashSet var2 = new HashSet();
      String var3 = var1.getMessagingType();
      if (var3 == null) {
         var3 = "javax.jms.MessageListener";
      }

      var2.add(var3);
      return var2;
   }

   private void perhapsDeclareSecurityRoles(Collection<String> var1, EjbJarBean var2) {
      HashSet var3 = new HashSet();
      AssemblyDescriptorBean var4 = var2.getAssemblyDescriptor();
      if (var4 == null) {
         var4 = var2.createAssemblyDescriptor();
      }

      SecurityRoleBean[] var5 = var4.getSecurityRoles();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SecurityRoleBean var8 = var5[var7];
         var3.add(var8.getRoleName());
      }

      Iterator var9 = var1.iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();
         if (!var3.contains(var10)) {
            var4.createSecurityRole().setRoleName(var10);
            var3.add(var10);
         }
      }

   }

   private void processEjbCallbackAnnotations(Class var1, EjbCallbackBean var2) {
      List var3;
      Iterator var4;
      Method var5;
      if (var2.getAroundInvokes().length == 0) {
         var3 = this.findAnnotatedMethods(var1, AroundInvoke.class, true);
         var4 = var3.iterator();

         while(var4.hasNext()) {
            var5 = (Method)var4.next();
            this.populateAroundInvokeBean(var2.createAroundInvoke(), var5);
         }
      }

      if (var2.getPostActivates().length == 0) {
         var3 = this.findAnnotatedMethods(var1, PostActivate.class, true);
         var4 = var3.iterator();

         while(var4.hasNext()) {
            var5 = (Method)var4.next();
            this.populateLifecyleCallbackBean(var2.createPostActivate(), var5);
         }
      }

      if (var2.getPrePassivates().length == 0) {
         var3 = this.findAnnotatedMethods(var1, PrePassivate.class, true);
         var4 = var3.iterator();

         while(var4.hasNext()) {
            var5 = (Method)var4.next();
            this.populateLifecyleCallbackBean(var2.createPrePassivate(), var5);
         }
      }

   }

   private void processAssemblyDescriptor(EnterpriseBeanBean var1, Class var2, Set<String> var3, EjbDescriptorBean var4) throws ErrorCollectionException {
      String var5 = var1.getEjbName();
      EjbJarBean var6 = var4.getEjbJarBean();
      AssemblyDescriptorBean var7 = var6.getAssemblyDescriptor();
      if (var7 == null) {
         var7 = var6.createAssemblyDescriptor();
      }

      Set var8 = this.getBusinessInterfaces(var3);
      Set var9 = this.getBusinessMethods(var2, var8, var1);
      this.processMethodPermissions(var5, var9, var7, var4);
      this.processTransactionAttributes(var5, var9, var7);
      this.processInterceptorBindings(var5, var2, var9, var7);
      this.processApplicationExceptions(var9, var7);
   }

   private void processApplicationExceptions(Set<Class> var1, EjbJarBean var2) {
      AssemblyDescriptorBean var3 = var2.getAssemblyDescriptor();
      if (var3 == null) {
         var3 = var2.createAssemblyDescriptor();
      }

      HashSet var4 = new HashSet();
      ApplicationExceptionBean[] var5 = var3.getApplicationExceptions();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ApplicationExceptionBean var8 = var5[var7];
         var4.add(var8.getExceptionClass());
      }

      Iterator var9 = var1.iterator();

      while(var9.hasNext()) {
         Class var10 = (Class)var9.next();
         if (!var4.contains(var10.getName())) {
            ApplicationExceptionBean var11 = var3.createApplicationException();
            var11.setExceptionClass(var10.getName());
            ApplicationException var12 = (ApplicationException)var10.getAnnotation(ApplicationException.class);
            var11.setRollback(var12.rollback());
         }
      }

   }

   private void processApplicationExceptions(Set<Method> var1, AssemblyDescriptorBean var2) {
      HashSet var3 = new HashSet();
      Iterator var4 = var1.iterator();

      int var7;
      while(var4.hasNext()) {
         Method var5 = (Method)var4.next();
         Class[] var6 = var5.getExceptionTypes();
         var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Class var9 = var6[var8];
            if (var9.isAnnotationPresent(ApplicationException.class)) {
               var3.add(var9);
            }
         }
      }

      HashSet var10 = new HashSet();
      ApplicationExceptionBean[] var11 = var2.getApplicationExceptions();
      int var13 = var11.length;

      for(var7 = 0; var7 < var13; ++var7) {
         ApplicationExceptionBean var16 = var11[var7];
         var10.add(var16.getExceptionClass());
      }

      Iterator var12 = var3.iterator();

      while(var12.hasNext()) {
         Class var14 = (Class)var12.next();
         if (!var10.contains(var14.getName())) {
            ApplicationExceptionBean var15 = var2.createApplicationException();
            var15.setExceptionClass(var14.getName());
            ApplicationException var17 = (ApplicationException)var14.getAnnotation(ApplicationException.class);
            var15.setRollback(var17.rollback());
         }
      }

   }

   private void processInterceptorBindings(String var1, Class var2, Set<Method> var3, AssemblyDescriptorBean var4) {
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Method var6 = (Method)var5.next();
         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;
         if (var6.isAnnotationPresent(Interceptors.class)) {
            var7 = true;
         }

         if (var6.isAnnotationPresent(ExcludeDefaultInterceptors.class)) {
            var8 = true;
         }

         if (var6.isAnnotationPresent(ExcludeClassInterceptors.class)) {
            var9 = true;
         }

         if (var7 || var8 || var9) {
            InterceptorBindingBean var10 = this.getMethodInterceptorBinding(var1, var6, var4, var3);
            if (var10 == null) {
               var10 = var4.createInterceptorBinding();
               var10.setEjbName(var1);
               this.populateMethodBean(var10.createMethod(), var6);
            }

            if (var7) {
               Interceptors var11 = (Interceptors)var6.getAnnotation(Interceptors.class);
               this.perhapsAddInterceptors(var10, var11);
            }

            if (var8 && !this.isSet("ExcludeDefaultInterceptors", var10)) {
               var10.setExcludeDefaultInterceptors(true);
            }

            if (var9 && !this.isSet("ExcludeClassInterceptors", var10)) {
               var10.setExcludeClassInterceptors(true);
            }
         }
      }

      boolean var12 = false;
      boolean var13 = false;
      if (var2.isAnnotationPresent(Interceptors.class)) {
         var12 = true;
      }

      if (var2.isAnnotationPresent(ExcludeDefaultInterceptors.class)) {
         var13 = true;
      }

      if (var12 || var13) {
         InterceptorBindingBean var14 = null;
         InterceptorBindingBean[] var15 = var4.getInterceptorBindings();
         int var17 = var15.length;

         for(int var19 = 0; var19 < var17; ++var19) {
            InterceptorBindingBean var18 = var15[var19];
            if (var18.getEjbName().equals(var1) && var18.getMethod() == null) {
               var14 = var18;
            }
         }

         if (var14 == null) {
            var14 = var4.createInterceptorBinding();
            var14.setEjbName(var1);
         }

         if (var12) {
            Interceptors var16 = (Interceptors)var2.getAnnotation(Interceptors.class);
            this.perhapsAddInterceptors(var14, var16);
         }

         if (var13 && !this.isSet("ExcludeDefaultInterceptors", var14)) {
            var14.setExcludeDefaultInterceptors(true);
         }
      }

   }

   private void perhapsAddInterceptors(InterceptorBindingBean var1, Interceptors var2) {
      HashSet var3 = new HashSet();
      String[] var4 = var1.getInterceptorClasses();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var3.add(var7);
      }

      var4 = var1.getInterceptorClasses();
      var1.setInterceptorClasses((String[])null);
      Class[] var9 = var2.value();
      var6 = var9.length;

      int var11;
      for(var11 = 0; var11 < var6; ++var11) {
         Class var8 = var9[var11];
         if (!var3.contains(var8.getName())) {
            var1.addInterceptorClass(var8.getName());
         }
      }

      String[] var10 = var4;
      var6 = var4.length;

      for(var11 = 0; var11 < var6; ++var11) {
         String var12 = var10[var11];
         var1.addInterceptorClass(var12);
      }

   }

   private InterceptorBindingBean getMethodInterceptorBinding(String var1, Method var2, AssemblyDescriptorBean var3, Set<Method> var4) {
      InterceptorBindingBean[] var5 = var3.getInterceptorBindings();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         InterceptorBindingBean var8 = var5[var7];
         if (var8.getEjbName().equals(var1)) {
            NamedMethodBean var9 = var8.getMethod();
            if (var9 != null && var2.getName().equals(var9.getMethodName())) {
               MethodParamsBean var10 = var9.getMethodParams();
               if (var10 == null) {
                  int var18 = 0;
                  Iterator var19 = var4.iterator();

                  while(var19.hasNext()) {
                     Method var20 = (Method)var19.next();
                     if (var20.getName().equals(var2.getName())) {
                        ++var18;
                        if (var18 > 1) {
                           break;
                        }
                     }
                  }

                  if (var18 == 1) {
                     return var8;
                  }
               } else {
                  String[] var11 = var10.getMethodParams();
                  Class[] var12 = var2.getParameterTypes();
                  if (var11.length == var12.length) {
                     int var13 = 0;
                     String[] var14 = var11;
                     int var15 = var11.length;

                     for(int var16 = 0; var16 < var15; ++var16) {
                        String var17 = var14[var16];
                        if (!var12[var13].getName().equals(var17)) {
                           break;
                        }

                        ++var13;
                     }

                     if (var13 == var11.length) {
                        return var8;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private void processInterceptorClasses(EjbDescriptorBean var1) throws ErrorCollectionException {
      EjbJarBean var2 = var1.getEjbJarBean();
      HashSet var3 = new HashSet();
      AssemblyDescriptorBean var4 = var2.getAssemblyDescriptor();
      int var7;
      if (var4 != null) {
         InterceptorBindingBean[] var5 = var4.getInterceptorBindings();
         int var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            InterceptorBindingBean var8 = var5[var7];
            String[] var9 = null;
            if (var8.getInterceptorOrder() != null) {
               var9 = var8.getInterceptorOrder().getInterceptorClasses();
            } else {
               var9 = var8.getInterceptorClasses();
            }

            String[] var10 = var9;
            int var11 = var9.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               String var13 = var10[var12];
               var3.add(var13);
            }
         }
      }

      InterceptorsBean var14 = var2.getInterceptors();
      if (var14 != null) {
         InterceptorBean[] var15 = var14.getInterceptors();
         var7 = var15.length;

         for(int var18 = 0; var18 < var7; ++var18) {
            InterceptorBean var20 = var15[var18];
            this.processInterceptorClass(var20);
            var3.remove(var20.getInterceptorClass());
         }
      }

      if (!var3.isEmpty()) {
         if (var14 == null) {
            var14 = var2.createInterceptors();
         }

         Iterator var16 = var3.iterator();

         while(var16.hasNext()) {
            String var17 = (String)var16.next();
            InterceptorBean var19 = var14.createInterceptor();
            var19.setInterceptorClass(var17);
            this.processInterceptorClass(var19);
         }
      }

   }

   private void processInterceptorClass(InterceptorBean var1) throws ErrorCollectionException {
      try {
         Class var2 = this.cl.loadClass(var1.getInterceptorClass());
         this.processJ2eeAnnotations(var2, var1);
         this.processEjbCallbackAnnotations(var2, var1);
      } catch (ClassNotFoundException var4) {
         Loggable var3 = EJBLogger.logCannotLoadInterceptorClassLoggable(var1.getInterceptorClass().toString(), var4.toString());
         this.addFatalProcessingError(var3.getMessage());
      }

   }

   private void processTransactionAttributes(String var1, Set<Method> var2, AssemblyDescriptorBean var3) {
      HashSet var4 = new HashSet();
      var4.addAll(var2);
      HashSet var13 = var4;
      ContainerTransactionBean[] var5 = var3.getContainerTransactions();
      int var6 = var5.length;

      ContainerTransactionBean var8;
      for(int var7 = 0; var7 < var6; ++var7) {
         var8 = var5[var7];
         MethodBean[] var9 = var8.getMethods();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            MethodBean var12 = var9[var11];
            if (var12.getEjbName().equals(var1)) {
               this.removeMethodsFromSet(var12, var13);
            }
         }
      }

      Iterator var14 = var13.iterator();

      while(var14.hasNext()) {
         Method var15 = (Method)var14.next();
         TransactionAttribute var16 = null;
         if (var15.isAnnotationPresent(TransactionAttribute.class)) {
            var16 = (TransactionAttribute)var15.getAnnotation(TransactionAttribute.class);
         }

         if (var16 == null) {
            Class var17 = var15.getDeclaringClass();
            if (var17.isAnnotationPresent(TransactionAttribute.class)) {
               var16 = (TransactionAttribute)var17.getAnnotation(TransactionAttribute.class);
            }
         }

         if (var16 != null) {
            var8 = var3.createContainerTransaction();
            var8.setTransAttribute(this.getTransactionAttributeAsString(var16.value()));
            this.fillMethodBean(var8.createMethod(), var1, var15);
         }
      }

   }

   private void processMethodPermissions(String var1, Set<Method> var2, AssemblyDescriptorBean var3, EjbDescriptorBean var4) {
      HashSet var13 = new HashSet(var2);
      MethodPermissionBean[] var5 = var3.getMethodPermissions();
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         MethodPermissionBean var8 = var5[var7];
         MethodBean[] var9 = var8.getMethods();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            MethodBean var12 = var9[var11];
            if (var12.getEjbName().equals(var1)) {
               this.removeMethodsFromSet(var12, var13);
            }
         }
      }

      ExcludeListBean var14 = var3.getExcludeList();
      MethodBean var20;
      if (var14 != null) {
         MethodBean[] var15 = var14.getMethods();
         var7 = var15.length;

         for(int var18 = 0; var18 < var7; ++var18) {
            var20 = var15[var18];
            if (var20.getEjbName().equals(var1)) {
               this.removeMethodsFromSet(var20, var13);
            }
         }
      }

      HashSet var16 = new HashSet();
      Iterator var17 = var13.iterator();

      while(var17.hasNext()) {
         Method var19 = (Method)var17.next();
         var20 = null;
         Loggable var21;
         MethodPermissionBean var24;
         if (!var19.isAnnotationPresent(DenyAll.class)) {
            if (var19.isAnnotationPresent(PermitAll.class)) {
               if (var19.isAnnotationPresent(RolesAllowed.class)) {
                  var21 = EJBLogger.logMutipleMehtodPermissionMethodForMethodLoggable(var1, var19.toString());
                  this.addProcessingError(var21.getMessage());
               }

               MethodPermissionBean var22 = var3.createMethodPermission();
               var22.createUnchecked();
               var20 = var22.createMethod();
            } else if (var19.isAnnotationPresent(RolesAllowed.class)) {
               RolesAllowed var23 = (RolesAllowed)var19.getAnnotation(RolesAllowed.class);
               var24 = var3.createMethodPermission();
               var24.setRoleNames(var23.value());
               var20 = var24.createMethod();
               var16.addAll(Arrays.asList(var23.value()));
            }
         } else {
            if (var19.isAnnotationPresent(PermitAll.class) || var19.isAnnotationPresent(RolesAllowed.class)) {
               var21 = EJBLogger.logMutipleMehtodPermissionMethodForMethodLoggable(var1, var19.toString());
               this.addProcessingError(var21.getMessage());
            }

            if (var14 == null) {
               var14 = var3.createExcludeList();
            }

            var20 = var14.createMethod();
         }

         if (var20 == null) {
            Class var25 = var19.getDeclaringClass();
            if (var25.isAnnotationPresent(PermitAll.class)) {
               if (var25.isAnnotationPresent(RolesAllowed.class)) {
                  Loggable var26 = EJBLogger.logMutipleMehtodPermissionMethodForClassLoggable(var25.toString());
                  this.addProcessingError(var26.getMessage());
               }

               var24 = var3.createMethodPermission();
               var24.createUnchecked();
               var20 = var24.createMethod();
            } else if (var25.isAnnotationPresent(RolesAllowed.class)) {
               RolesAllowed var28 = (RolesAllowed)var25.getAnnotation(RolesAllowed.class);
               MethodPermissionBean var27 = var3.createMethodPermission();
               var27.setRoleNames(var28.value());
               var20 = var27.createMethod();
               var16.addAll(Arrays.asList(var28.value()));
            }
         }

         if (var20 != null) {
            this.fillMethodBean(var20, var1, var19);
         }
      }

      if (!var16.isEmpty()) {
         this.perhapsDeclareSecurityRoles(var16, var4.getEjbJarBean());
      }

   }

   private void removeMethodsFromSet(MethodBean var1, Set<Method> var2) {
      MethodParamsBean var3 = var1.getMethodParams();
      HashSet var4 = new HashSet();
      if (var3 == null) {
         if ("*".equals(var1.getMethodName())) {
            var2.clear();
         } else {
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               Method var6 = (Method)var5.next();
               if (var6.getName().equals(var1.getMethodName())) {
                  var4.add(var6);
               }
            }
         }
      } else {
         String[] var14 = var3.getMethodParams();
         Iterator var15 = var2.iterator();

         label49:
         while(true) {
            Method var7;
            Class[] var8;
            do {
               do {
                  if (!var15.hasNext()) {
                     break label49;
                  }

                  var7 = (Method)var15.next();
               } while(!var7.getName().equals(var1.getMethodName()));

               var8 = var7.getParameterTypes();
            } while(var14.length != var8.length);

            int var9 = 0;
            Class[] var10 = var8;
            int var11 = var8.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Class var13 = var10[var12];
               if (!var13.getName().equals(var14[var9])) {
                  break;
               }

               ++var9;
            }

            if (var9 == var14.length) {
               var4.add(var7);
            }
         }
      }

      var2.removeAll(var4);
   }

   private Set<Class> getBusinessInterfaces(Collection<String> var1) throws ErrorCollectionException {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Class var5 = null;

         try {
            var5 = this.cl.loadClass(var4);
            var2.add(var5);
         } catch (ClassNotFoundException var8) {
            Loggable var7 = EJBLogger.logUnableLoadInterfaceClassLoggable(var4, var8.toString());
            this.addFatalProcessingError(var7.getMessage());
         }
      }

      return var2;
   }

   private Set<Method> getBusinessMethods(Class var1, Set<Class> var2, EnterpriseBeanBean var3) throws ErrorCollectionException {
      HashSet var4 = new HashSet();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Class var6 = (Class)var5.next();
         Method[] var7 = var6.getMethods();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Method var10 = var7[var9];
            if (var10.getDeclaringClass() != EJBObject.class && var10.getDeclaringClass() != EJBLocalObject.class) {
               try {
                  var4.add(var1.getMethod(var10.getName(), var10.getParameterTypes()));
               } catch (NoSuchMethodException var15) {
                  boolean var12 = false;
                  Method[] var13;
                  if (!EJBObject.class.isAssignableFrom(var6) && !EJBLocalObject.class.isAssignableFrom(var6)) {
                     var13 = var1.getMethods();

                     for(int var14 = 0; var14 < var13.length && !var12; ++var14) {
                        var12 = MethodUtils.potentialBridgeCandidate(var10, var13[var14]);
                     }
                  }

                  var13 = null;
                  Loggable var16;
                  if (var12) {
                     var16 = EJBLogger.logMayBeMissingBridgeMethodLoggable(var10.toString(), var6.getName());
                  } else {
                     var16 = EJBLogger.logBeanClassNotImplementInterfaceMethodLoggable(var1.getName(), var10.toString());
                  }

                  this.addFatalProcessingError(var16.getMessage());
               }
            }
         }
      }

      if (this.isWebService(var3, var1)) {
         var4.addAll(this.getWebServiceMethods(var3, var1));
      }

      return var4;
   }

   private boolean isWebService(EnterpriseBeanBean var1, Class var2) {
      return var1 instanceof SessionBeanBean && (var2.isAnnotationPresent(WebService.class) || var2.isAnnotationPresent(WebServiceProvider.class));
   }

   private Collection<Method> getWebServiceMethods(EnterpriseBeanBean var1, Class var2) throws ErrorCollectionException {
      Class var3 = null;
      SessionBeanBean var4 = (SessionBeanBean)var1;
      if (var4.getServiceEndpoint() != null) {
         try {
            var3 = this.cl.loadClass(var4.getServiceEndpoint());
         } catch (ClassNotFoundException var7) {
            Loggable var6 = EJBLogger.logCannotFoundServiceEndPointClassLoggable(var4.getServiceEndpoint());
            this.addFatalProcessingError(var6.getMessage());
         }
      }

      return DeployUtil.getWebServiceMethods(var2, var3);
   }

   private void populateLifecyleCallbackBean(LifecycleCallbackBean var1, Method var2) {
      var1.setBeanSource(1);
      var1.setLifecycleCallbackClass(var2.getDeclaringClass().getName());
      var1.setLifecycleCallbackMethod(var2.getName());
   }

   private void populateAroundInvokeBean(AroundInvokeBean var1, Method var2) {
      var1.setClassName(var2.getDeclaringClass().getName());
      var1.setMethodName(var2.getName());
   }

   private Method findAnnotatedTimeoutMethod(Class var1) throws ComplianceException {
      List var2 = this.findAnnotatedMethods(var1, Timeout.class, true);
      TimeoutCheckHelper.validateOnlyOneTimeoutMethod(var2);
      Iterator var3 = var2.iterator();
      if (var3.hasNext()) {
         Method var4 = (Method)var3.next();
         TimeoutCheckHelper.validateTimeoutMethodIsejbTimeout(var1, var4);
         return var4;
      } else {
         return null;
      }
   }

   private Method getTimeoutMethodByDD(Class var1, EnterpriseBeanBean var2) throws ComplianceException {
      NamedMethodBean var3 = null;
      Class var4 = var1;
      if (var2 instanceof SessionBeanBean) {
         var3 = ((SessionBeanBean)var2).getTimeoutMethod();
      } else {
         if (!(var2 instanceof MessageDrivenBeanBean)) {
            return null;
         }

         var3 = ((MessageDrivenBeanBean)var2).getTimeoutMethod();
      }

      if (var3 == null) {
         return null;
      } else {
         String var5 = var3.getMethodName();
         if (var5 == null) {
            return null;
         } else {
            Method var6 = null;

            while(var4 != Object.class) {
               try {
                  var6 = var4.getDeclaredMethod(var5, Timer.class);
                  break;
               } catch (NoSuchMethodException var8) {
                  var4 = var4.getSuperclass();
               }
            }

            TimeoutCheckHelper.validateTimeoutMethodExistsInBC(var6, var1, var5);
            return var6;
         }
      }
   }

   private List<Method> findAnnotatedMethods(Class var1, Class var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      List var5;
      if (var3) {
         var5 = this.getMethods(var1);
      } else {
         var5 = Arrays.asList(var1.getMethods());
      }

      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Method var7 = (Method)var6.next();
         if (var7.isAnnotationPresent(var2)) {
            var4.add(var7);
         }
      }

      return var4;
   }

   private List<Method> getAllMethods(Class var1) {
      ArrayList var2 = new ArrayList();

      for(Class var3 = var1; !var3.equals(Object.class); var3 = var3.getSuperclass()) {
         Method[] var4 = var3.getDeclaredMethods();
         Method[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method var8 = var5[var7];
            var2.add(var8);
         }
      }

      return var2;
   }

   private void processMessageDrivenAnnotations(MessageDrivenBeanBean var1, EjbDescriptorBean var2) throws ClassNotFoundException, ErrorCollectionException {
      Class var3 = this.loadBeanClass(var1.getEjbClass(), var1.getEjbName(), this.cl);
      Loggable var5;
      if (var3.isAnnotationPresent(MessageDriven.class)) {
         for(Class var4 = var3.getSuperclass(); !var4.equals(Object.class); var4 = var4.getSuperclass()) {
            if (var4.isAnnotationPresent(MessageDriven.class)) {
               var5 = EJBLogger.logMDBWithMDBParentLoggable(var4.getName(), var1.getEjbClass().toString());
               this.addProcessingError(var5.getMessage());
               break;
            }
         }
      }

      MessageDriven var11 = (MessageDriven)var3.getAnnotation(MessageDriven.class);
      if (var11 != null) {
         if (!this.isSet("MessagingType", var1) && var11.messageListenerInterface() != Object.class) {
            var1.setMessagingType(var11.messageListenerInterface().getName());
         }

         this.processActivationConfigProperties(var1, var11.activationConfig());
         if (!this.isSet("MappedName", var1) && var11.mappedName().length() > 0) {
            var1.setMappedName(var11.mappedName());
         }
      }

      Loggable var6;
      if (var1.getMessagingType() == null) {
         Set var12 = this.getImplementedInterfaces(var3);
         if (var12.size() == 1) {
            var1.setMessagingType(((Class)var12.iterator().next()).getName());
         } else {
            if (MessageListener.class.isAssignableFrom(var3)) {
               var1.setMessagingType("javax.jms.MessageListener");
            }

            if (var1.getMessagingType() == null) {
               var6 = EJBLogger.logNoMessageListenerSpecifiedForMDBLoggable(var1.getEjbName());
               this.addFatalProcessingError(var6.getMessage());
            }
         }
      }

      if (!this.isSet("TransactionType", var1)) {
         var1.setTransactionType(this.getTransactionType(var3));
      }

      this.processJ2eeAnnotations(var3, var1);
      var5 = null;
      var6 = null;

      Method var13;
      Method var14;
      try {
         var14 = this.findAnnotatedTimeoutMethod(var3);
         var13 = this.getTimeoutMethodByDD(var3, var1);
         TimeoutCheckHelper.validateTimeoutMethodsIdentical(var13, var14);
      } catch (ComplianceException var10) {
         throw new ErrorCollectionException(var10);
      }

      if (var13 == null && var14 != null) {
         this.populateMethodBean(var1.createTimeoutMethod(), var14);
      }

      if (var1.getAroundInvokes().length == 0) {
         List var7 = this.findAnnotatedMethods(var3, AroundInvoke.class, true);
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            Method var9 = (Method)var8.next();
            this.populateAroundInvokeBean(var1.createAroundInvoke(), var9);
         }
      }

      this.processRunAs(var3, (DescriptorBean)var1);
      this.processAssemblyDescriptor(var1, var3, this.getInterfaceNames(var1), var2);
   }

   private void processActivationConfigProperties(MessageDrivenBeanBean var1, ActivationConfigProperty[] var2) {
      if (var2.length > 0) {
         ActivationConfigBean var3 = var1.getActivationConfig();
         if (var3 == null) {
            var3 = var1.createActivationConfig();
         }

         for(int var4 = 0; var4 < var2.length; ++var4) {
            ActivationConfigPropertyBean[] var5 = var3.getActivationConfigProperties();
            boolean var6 = true;
            String var7 = var2[var4].propertyName();

            for(int var8 = 0; var8 < var5.length; ++var8) {
               if (var5[var8].getActivationConfigPropertyName().equalsIgnoreCase(var7)) {
                  var6 = false;
                  break;
               }
            }

            if (var6) {
               ActivationConfigPropertyBean var9 = var3.createActivationConfigProperty();
               var9.setActivationConfigPropertyName(var2[var4].propertyName());
               var9.setActivationConfigPropertyValue(var2[var4].propertyValue());
            }
         }
      }

   }

   private String getTransactionType(Class var1) {
      TransactionManagement var2 = (TransactionManagement)var1.getAnnotation(TransactionManagement.class);
      return var2 != null && var2.value() != TransactionManagementType.CONTAINER ? "Bean" : "Container";
   }

   private void populateMethodBean(NamedMethodBean var1, Method var2) {
      var1.setMethodName(var2.getName());
      MethodParamsBean var3 = var1.createMethodParams();
      Class[] var4 = var2.getParameterTypes();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Class var7 = var4[var6];
         var3.addMethodParam(var7.getCanonicalName());
      }

   }

   private void fillMethodBean(weblogic.j2ee.descriptor.wl.MethodBean var1, String var2, Method var3) {
      var1.setEjbName(var2);
      var1.setMethodName(var3.getName());
      weblogic.j2ee.descriptor.wl.MethodParamsBean var4 = var1.createMethodParams();
      Class[] var5 = var3.getParameterTypes();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Class var8 = var5[var7];
         var4.addMethodParam(var8.getCanonicalName());
      }

   }

   private void fillMethodBean(MethodBean var1, String var2, Method var3) {
      var1.setEjbName(var2);
      var1.setMethodName(var3.getName());
      MethodParamsBean var4 = var1.createMethodParams();
      Class[] var5 = var3.getParameterTypes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         var4.addMethodParam(var5[var6].getCanonicalName());
      }

   }

   private String getTransactionAttributeAsString(TransactionAttributeType var1) {
      switch (var1) {
         case MANDATORY:
            return "Mandatory";
         case REQUIRED:
            return "Required";
         case REQUIRES_NEW:
            return "RequiresNew";
         case SUPPORTS:
            return "Supports";
         case NOT_SUPPORTED:
            return "NotSupported";
         case NEVER:
            return "Never";
         default:
            throw new AssertionError();
      }
   }

   private Set<Class> getImplementedInterfaces(Class var1) {
      HashSet var2 = new HashSet();
      Class[] var3 = var1.getInterfaces();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4] != Serializable.class && var3[var4] != Externalizable.class && !var3[var4].getName().startsWith("javax.") && var3[var4].getMethods().length != 0) {
            var2.add(var3[var4]);
         }
      }

      return var2;
   }

   private boolean isExtendRemote(Class var1) {
      Class[] var2 = var1.getInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         if (var5.getName().equals(java.rmi.Remote.class.getName())) {
            return true;
         }
      }

      return false;
   }

   public void processWLSAnnotations(EjbDescriptorBean var1, ClassLoader var2) throws ClassNotFoundException, ErrorCollectionException {
      EjbJarBean var3 = var1.getEjbJarBean();
      if (var3 != null) {
         EnterpriseBeansBean var4 = var3.getEnterpriseBeans();
         WeblogicEjbJarBean var5 = var1.getWeblogicEjbJarBean();
         SessionBeanBean[] var6 = var4.getSessions();
         int var7 = var6.length;

         int var8;
         Class var10;
         for(var8 = 0; var8 < var7; ++var8) {
            SessionBeanBean var9 = var6[var8];
            var10 = Class.forName(var9.getEjbClass(), false, var2);
            this.processWLSAnnotations(var10, var5, var9, "Stateless".equals(var9.getSessionType()) ? EjbAnnotationProcessor.EnterpriseBeanType.STATELESS : EjbAnnotationProcessor.EnterpriseBeanType.STATEFUL);
         }

         MessageDrivenBeanBean[] var11 = var4.getMessageDrivens();
         var7 = var11.length;

         for(var8 = 0; var8 < var7; ++var8) {
            MessageDrivenBeanBean var12 = var11[var8];
            var10 = Class.forName(var12.getEjbClass(), false, var2);
            this.processWLSAnnotations(var10, var5, var12, EjbAnnotationProcessor.EnterpriseBeanType.MESSAGE_DRIVEN);
         }

      }
   }

   private void processWLSAnnotations(Class var1, WeblogicEjbJarBean var2, EnterpriseBeanBean var3, EnterpriseBeanType var4) throws ErrorCollectionException {
      String var5 = var3.getEjbName();
      this.processCallByReference(var1, var5, var2, var4);
      this.processJNDIName(var1, var3, var2, var4);
      this.processTransactionTimeoutSeconds(var1, var5, var2, var4);
      this.processAllowRemoveDuringTransaction(var1, var5, var2, var4);
      this.processMessageDestinationConfiguration(var1, var5, var2, var4);
      this.processJMSClientID(var1, var5, var2, var4);
      this.processDisableWarnings(var1, var2, var4);
      Set var6;
      if (var4 == EjbAnnotationProcessor.EnterpriseBeanType.MESSAGE_DRIVEN) {
         var6 = this.getBusinessInterfaces(this.getInterfaceNames((MessageDrivenBeanBean)var3));
      } else {
         var6 = this.getBusinessInterfaces(this.getInterfaceNames((SessionBeanBean)var3));
      }

      Set var7 = this.getBusinessMethods(var1, var6, var3);
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         Method var9 = (Method)var8.next();
         this.processIdempotent(var9, var5, var2, var4);
         this.processTransactionIsolation(var9, var5, var2, var4);
      }

      if (!EJBComplianceChecker.isNeedCheck) {
         this.throwProcessingErrors();
      }
   }

   private void processCallByReference(Class var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      CallByReference var5 = (CallByReference)this.assertContext(var1, CallByReference.class, var4);
      if (var5 != null && !this.isSet("EnableCallByReference", this.getWLBean(var2, var3))) {
         this.getWLBean(var2, var3).setEnableCallByReference(true);
      }

   }

   private void processJNDIName(Class var1, EnterpriseBeanBean var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) throws ErrorCollectionException {
      String var5 = var2.getEjbName();
      if (!(var2 instanceof SessionBeanBean)) {
         this.assertContext(var1, JNDIName.class, var4);
      } else {
         Set var6 = this.getBusinessInterfaces(Arrays.asList(((SessionBeanBean)var2).getBusinessRemotes()));
         Set var7 = this.getBusinessInterfaces(Arrays.asList(((SessionBeanBean)var2).getBusinessLocals()));
         Iterator var8 = var7.iterator();

         Class var9;
         while(var8.hasNext()) {
            var9 = (Class)var8.next();
            if (var9.isAnnotationPresent(JNDIName.class)) {
               Loggable var10 = EJBLogger.logJNDINameAnnotationOnLocalInterfaceLoggable(var9.getName(), var2.getEjbName());
               this.addProcessingError(var10.getMessage());
            }
         }

         JNDIName var15;
         if (var6.size() == 1) {
            Class var12 = (Class)var6.iterator().next();
            JNDIName var14 = (JNDIName)this.assertContext(var1, JNDIName.class, var4);
            var15 = (JNDIName)this.assertContext(var12, JNDIName.class, var4);
            if (var14 != null && var15 != null) {
               Loggable var11 = EJBLogger.logDuplicateJNDINameAnnotationLoggable(var1.getName(), var12.getName());
               this.addProcessingError(var11.getMessage());
            }

            if (var14 == null && var15 == null) {
               return;
            }

            JNDIName var16 = var14 == null ? var15 : var14;
            this.addJNDIName(var16, var4, var5, var3, var12);
         } else if (var6.size() > 1) {
            if (var1.isAnnotationPresent(JNDIName.class)) {
               Loggable var13 = EJBLogger.logNoJNDINameOnMultiInterfaceImplLoggable(var1.getName(), var6.toString());
               this.addProcessingError(var13.getMessage());
            }

            var8 = var6.iterator();

            while(var8.hasNext()) {
               var9 = (Class)var8.next();
               var15 = (JNDIName)var9.getAnnotation(JNDIName.class);
               this.addJNDIName(var15, var4, var5, var3, var9);
            }
         }

      }
   }

   private BusinessInterfaceJndiNameMapBean getBusinessInterfaceBean(EnterpriseBeanType var1, String var2, WeblogicEjbJarBean var3, Class var4) {
      BusinessInterfaceJndiNameMapBean var5;
      if (var1 == EjbAnnotationProcessor.EnterpriseBeanType.STATELESS) {
         StatelessSessionDescriptorBean var6 = this.getWLBean(var2, var3).getStatelessSessionDescriptor();
         if (var6 == null) {
            var6 = this.getWLBean(var2, var3).createStatelessSessionDescriptor();
         }

         var5 = var6.lookupBusinessInterfaceJndiNameMap(var4.getName());
         if (var5 == null) {
            var5 = var6.createBusinessInterfaceJndiNameMap();
            var5.setBusinessRemote(var4.getName());
         }
      } else {
         StatefulSessionDescriptorBean var7 = this.getWLBean(var2, var3).getStatefulSessionDescriptor();
         if (var7 == null) {
            var7 = this.getWLBean(var2, var3).createStatefulSessionDescriptor();
         }

         var5 = this.getWLBean(var2, var3).getStatefulSessionDescriptor().lookupBusinessInterfaceJndiNameMap(var4.getName());
         if (var5 == null) {
            var5 = this.getWLBean(var2, var3).getStatefulSessionDescriptor().createBusinessInterfaceJndiNameMap();
            var5.setBusinessRemote(var4.getName());
         }
      }

      return var5;
   }

   private void addJNDIName(JNDIName var1, EnterpriseBeanType var2, String var3, WeblogicEjbJarBean var4, Class var5) {
      if (var1 != null && !"".equals(var1.value().trim())) {
         BusinessInterfaceJndiNameMapBean var6 = this.getBusinessInterfaceBean(var2, var3, var4, var5);
         if (!this.isSet("JNDIName", var6)) {
            var6.setJNDIName(var1.value());
         }
      }

   }

   private void processTransactionTimeoutSeconds(Class var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      TransactionTimeoutSeconds var5 = (TransactionTimeoutSeconds)this.assertContext(var1, TransactionTimeoutSeconds.class, var4);
      if (var5 != null) {
         TransactionDescriptorBean var6 = this.getWLBean(var2, var3).getTransactionDescriptor();
         if (var6 == null) {
            var6 = this.getWLBean(var2, var3).createTransactionDescriptor();
         }

         if (!this.isSet("TransTimeoutSeconds", var6)) {
            var6.setTransTimeoutSeconds(var5.value());
            var5.value();
         }

      }
   }

   private void processAllowRemoveDuringTransaction(Class var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      AllowRemoveDuringTransaction var5 = (AllowRemoveDuringTransaction)this.assertContext(var1, AllowRemoveDuringTransaction.class, var4);
      if (var5 != null) {
         StatefulSessionDescriptorBean var6 = this.getWLBean(var2, var3).getStatefulSessionDescriptor();
         if (var6 == null) {
            var6 = this.getWLBean(var2, var3).createStatefulSessionDescriptor();
         }

         if (!this.isSet("AllowRemoveDuringTransaction", var6)) {
            var6.setAllowRemoveDuringTransaction(true);
         }

      }
   }

   private void processMessageDestinationConfiguration(Class var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      MessageDestinationConfiguration var5 = (MessageDestinationConfiguration)this.assertContext(var1, MessageDestinationConfiguration.class, var4);
      if (var5 != null) {
         MessageDrivenDescriptorBean var6 = this.getWLBean(var2, var3).getMessageDrivenDescriptor();
         if (var6 == null) {
            var6 = this.getWLBean(var2, var3).createMessageDrivenDescriptor();
         }

         if (!this.isSet("ConnectionFactoryJNDIName", var6) && !"".equals(var5.connectionFactoryJNDIName())) {
            var6.setConnectionFactoryJNDIName(var5.connectionFactoryJNDIName());
         }

         if (!this.isSet("InitialContextFactory", var6)) {
            var6.setInitialContextFactory(var5.initialContextFactory().getName());
         }

         if (!this.isSet("ProviderUrl", var6) && !"".equals(var5.providerURL())) {
            var6.setProviderUrl(var5.providerURL());
         }

      }
   }

   private void processJMSClientID(Class var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      JMSClientID var5 = (JMSClientID)this.assertContext(var1, JMSClientID.class, var4);
      if (var5 != null) {
         MessageDrivenDescriptorBean var6 = this.getWLBean(var2, var3).getMessageDrivenDescriptor();
         if (var6 == null) {
            var6 = this.getWLBean(var2, var3).createMessageDrivenDescriptor();
         }

         if (!this.isSet("JmsClientId", var6)) {
            var6.setJmsClientId(var5.value());
         }

         if (!this.isSet("GenerateUniqueJmsClientId", var6)) {
            var6.setGenerateUniqueJmsClientId(var5.generateUniqueID());
         }

      }
   }

   private void processDisableWarnings(Class var1, WeblogicEjbJarBean var2, EnterpriseBeanType var3) {
      DisableWarnings var4 = (DisableWarnings)this.assertContext(var1, DisableWarnings.class, var3);
      if (var4 != null) {
         if (this.disableWarningsIsInXML == null) {
            this.disableWarningsIsInXML = this.isSet("DisableWarnings", var2);
         }

         if (!this.disableWarningsIsInXML) {
            WarningCode[] var5 = var4.value();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               WarningCode var8 = var5[var7];
               var2.addDisableWarning(var8.getWeblogicCode());
            }
         }
      }

   }

   private void processIdempotent(Method var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      Idempotent var5 = (Idempotent)this.assertContext(var1, Idempotent.class, var4);
      if (var5 != null) {
         IdempotentMethodsBean var6 = var3.getIdempotentMethods();
         if (var6 == null) {
            var6 = var3.createIdempotentMethods();
         }

         weblogic.j2ee.descriptor.wl.MethodBean var7 = this.findMethod(var1, var2, var6.getMethods());
         if (var7 == null) {
            var7 = var6.createMethod();
            this.fillMethodBean(var7, var2, var1);
         }

         RetryMethodsOnRollbackBean[] var8 = var3.getRetryMethodsOnRollbacks();
         var7 = null;
         if (var8 != null && var8.length != 0) {
            RetryMethodsOnRollbackBean[] var9 = var8;
            int var10 = var8.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               RetryMethodsOnRollbackBean var12 = var9[var11];
               var7 = this.findMethod(var1, var2, var12.getMethods());
               if (var7 != null) {
                  break;
               }
            }
         } else {
            var8 = new RetryMethodsOnRollbackBean[1];
         }

         if (var7 == null) {
            RetryMethodsOnRollbackBean var13 = var3.createRetryMethodsOnRollback();
            var13.setRetryCount(var5.retryOnRollbackCount());
            var7 = var13.createMethod();
            this.fillMethodBean(var7, var2, var1);
         }

      }
   }

   private void processTransactionIsolation(Method var1, String var2, WeblogicEjbJarBean var3, EnterpriseBeanType var4) {
      TransactionIsolation var5 = (TransactionIsolation)this.assertContext(var1, TransactionIsolation.class, var4);
      if (var5 != null) {
         TransactionIsolationBean[] var6 = var3.getTransactionIsolations();
         weblogic.j2ee.descriptor.wl.MethodBean var7 = null;
         if (var6 != null && var6.length != 0) {
            TransactionIsolationBean[] var8 = var6;
            int var9 = var6.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               TransactionIsolationBean var11 = var8[var10];
               var7 = this.findMethod(var1, var2, var11.getMethods());
               if (var7 != null) {
                  break;
               }
            }
         } else {
            var6 = new TransactionIsolationBean[1];
         }

         if (var7 == null) {
            TransactionIsolationBean var12 = var3.createTransactionIsolation();
            var12.setIsolationLevel(var5.value().getWeblogicIsolationString());
            var7 = var12.createMethod();
            this.fillMethodBean(var7, var2, var1);
         }

      }
   }

   private boolean isSet(String var1, WeblogicEjbJarBean var2, String var3) {
      if (var2 == null) {
         return false;
      } else {
         WeblogicEnterpriseBeanBean var4 = var2.lookupWeblogicEnterpriseBean(var1);
         return var4 == null ? false : this.isSet(var3, var4);
      }
   }

   private <T extends Annotation> T assertContext(Class var1, Class<T> var2, EnterpriseBeanType var3) {
      Annotation var4 = var1.getAnnotation(var2);
      if (var4 != null && !var3.validAnnotationTypes.contains(var2)) {
         Loggable var5 = EJBLogger.logAnnotationOnInvalidClassLoggable(var2.getName(), var1.getName());
         throw new IllegalStateException(var5.getMessage());
      } else {
         return var4;
      }
   }

   private <T extends Annotation> T assertContext(Method var1, Class<T> var2, EnterpriseBeanType var3) {
      Annotation var4 = var1.getAnnotation(var2);
      if (var4 != null && !var3.validAnnotationTypes.contains(var2)) {
         Loggable var5 = EJBLogger.logAnnotationOnInvalidMethodLoggable(var2.getName(), var1.getDeclaringClass().getName(), var1.getName());
         throw new IllegalStateException(var5.getMessage());
      } else {
         return var4;
      }
   }

   private WeblogicEnterpriseBeanBean getWLBean(String var1, WeblogicEjbJarBean var2) {
      WeblogicEnterpriseBeanBean var3 = var2.lookupWeblogicEnterpriseBean(var1);
      if (var3 == null) {
         var3 = var2.createWeblogicEnterpriseBean();
         var3.setEjbName(var1);
      }

      return var3;
   }

   private weblogic.j2ee.descriptor.wl.MethodBean findMethod(Method var1, String var2, weblogic.j2ee.descriptor.wl.MethodBean[] var3) {
      Object var4 = null;
      weblogic.j2ee.descriptor.wl.MethodBean[] var5 = var3;
      int var6 = var3.length;

      label43:
      for(int var7 = 0; var7 < var6; ++var7) {
         weblogic.j2ee.descriptor.wl.MethodBean var8 = var5[var7];
         if (var2.equals(var8.getEjbName()) && var1.getName().equals(var8.getMethodName())) {
            if (var8.getMethodParams() != null) {
               String[] var9 = var8.getMethodParams().getMethodParams();
               if (var9 != null) {
                  Class[] var10 = var1.getParameterTypes();
                  if (var9.length == var10.length) {
                     for(int var11 = 0; var11 < var9.length; ++var11) {
                        if (!var9[var11].equals(var10[var11].getCanonicalName())) {
                           continue label43;
                        }
                     }

                     return var8;
                  }
               }
            }
         }
      }

      return null;
   }

   private Class loadBeanClass(String var1, String var2, ClassLoader var3) throws ErrorCollectionException, ClassNotFoundException {
      if (var1 == null || var1.trim().length() == 0) {
         this.addProcessingError("In the ejb-jar.xml, the EJB " + var2 + " does not specify an ejb-class value and no annotated EJB class was found in the ejb-jar file with a matching ejb-name.  The ejb-class value must be specified.");
         this.throwProcessingErrors();
      }

      return var3.loadClass(var1);
   }

   public static void main(String[] var0) throws Exception {
      EjbDescriptorBean var1 = new EjbDescriptorBean(true);
      VirtualJarFile var2 = VirtualJarFactory.createVirtualJar(new File(var0[0]));
      ClasspathClassFinder2 var3 = new ClasspathClassFinder2(var0[0]);
      GenericClassLoader var4 = new GenericClassLoader(var3);
      EjbAnnotationProcessor var5 = new EjbAnnotationProcessor(var4);
      var5.processAnnotations(var1, var2);
      System.out.println("\n\n");
      DescriptorBean var6 = (DescriptorBean)var1.getEjbJarBean();
      var6.getDescriptor().toXML(System.out);
   }

   protected void addBeanInterfaceNotSetError(J2eeClientEnvironmentBean var1) {
      String var2;
      Loggable var3;
      if (var1 instanceof EnterpriseBeanBean) {
         var2 = ((EnterpriseBeanBean)var1).getEjbClass();
         var3 = EJBLogger.logNoSetBeanInterfaceForBeanLoggable(var2);
         this.addProcessingError(var3.getMessage());
      } else if (var1 instanceof InterceptorBean) {
         var2 = ((InterceptorBean)var1).getInterceptorClass();
         var3 = EJBLogger.logNoSetBeanInterfaceForInterceptorLoggable(var2);
         this.addProcessingError(var3.getMessage());
      }

   }

   protected void perhapsDeclareRunAs(DescriptorBean var1, String var2) {
      EnterpriseBeanBean var3 = (EnterpriseBeanBean)var1;
      SecurityIdentityBean var4 = var3.getSecurityIdentity();
      if (var4 == null) {
         var4 = var3.createSecurityIdentity();
         RunAsBean var5 = var4.createRunAs();
         var5.setRoleName(var2);
      }

   }

   protected void perhapsDeclareRoles(DescriptorBean var1, String[] var2) {
      SessionBeanBean var3 = (SessionBeanBean)var1;
      HashSet var4 = new HashSet();
      SecurityRoleRefBean[] var5 = var3.getSecurityRoleRefs();
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         SecurityRoleRefBean var8 = var5[var7];
         var4.add(var8.getRoleName());
      }

      EjbJarBean var11 = (EjbJarBean)var1.getDescriptor().getRootBean();
      this.perhapsDeclareSecurityRoles(Arrays.asList(var2), var11);
      String[] var12 = var2;
      var7 = var2.length;

      for(int var13 = 0; var13 < var7; ++var13) {
         String var9 = var12[var13];
         if (!var4.contains(var9)) {
            SecurityRoleRefBean var10 = var3.createSecurityRoleRef();
            var10.setRoleName(var9);
            var4.add(var9);
         }
      }

   }

   static enum EnterpriseBeanType {
      STATELESS(new Class[]{TransactionTimeoutSeconds.class, DisableWarnings.class, CallByReference.class, JNDIName.class, Idempotent.class, TransactionIsolation.class}),
      STATEFUL(new Class[]{TransactionTimeoutSeconds.class, DisableWarnings.class, CallByReference.class, JNDIName.class, AllowRemoveDuringTransaction.class, Idempotent.class, TransactionIsolation.class}),
      MESSAGE_DRIVEN(new Class[]{TransactionTimeoutSeconds.class, DisableWarnings.class, MessageDestinationConfiguration.class, JMSClientID.class, TransactionIsolation.class});

      private final Collection validAnnotationTypes = new HashSet();

      private EnterpriseBeanType(Class... var3) {
         this.validAnnotationTypes.addAll(Arrays.asList(var3));
      }
   }
}
