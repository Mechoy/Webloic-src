package weblogic.deployment;

import commonj.timers.TimerManager;
import commonj.work.WorkManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.management.MBeanServer;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.ejb20.portable.HandleDelegateImpl;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.ClassLoaderUtils;
import weblogic.utils.reflect.ReflectUtils;
import weblogic.work.j2ee.J2EEWorkManager;
import weblogic.workarea.WorkContextHelper;

public abstract class BaseEnvironmentBuilder {
   private static final HandleDelegateImpl handleDelegate = new HandleDelegateImpl();
   protected static final boolean DEBUG = System.getProperty("weblogic.compenv.debug") != null;
   protected static final boolean VERBOSE = System.getProperty("weblogic.compenv.verbose") != null;
   public static final String BEA_CTX = "bea";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final String MODULE_NAME_BINDING = "bea/ModuleName";
   public static final String USER_TX_BINDING = "javax/transaction/UserTransaction";
   public static final String LOCAL_USER_TX_BINDING = "comp/UserTransaction";
   public static final String TM_BINDING = "javax/transaction/TransactionManager";
   public static final String LOCAL_TM_BINDING = "comp/TransactionSynchronizationRegistry";
   protected static final String LOCAL_ORB_BINDING = "comp/ORB";
   protected static final String HANDLE_DELEGATE_BINDING = "HandleDelegate";
   protected final Context rootCtx;
   protected Context envCtx;
   protected Context resCtx;
   protected final String applicationName;
   protected final String moduleName;
   protected final String componentName;
   private Set serviceRefHelpers;

   public BaseEnvironmentBuilder(Context var1, String var2, String var3, String var4) throws NamingException {
      this.serviceRefHelpers = new HashSet();
      this.rootCtx = var1;
      this.applicationName = var2;
      this.moduleName = var3;
      this.componentName = var4;
      var1.createSubcontext("bea");
      var1.bind("bea/ModuleName", var3);
      Context var5 = var1.createSubcontext("comp");
      this.envCtx = var5.createSubcontext("env");
      this.resCtx = this.envCtx.createSubcontext("wls-connector-resref");
      this.addUserTransaction();
      this.addTransactionManager();
      this.addORB();
      WorkContextHelper.bind(var5);
      this.addDefaultJ2EEWorkManager();
      MBeanServer var6 = ManagementService.getRuntimeMBeanServer(kernelId);
      Context var7;
      if (var6 != null) {
         var7 = this.envCtx.createSubcontext("jmx");
         var7.bind("runtime", var6);
      }

      var6 = ManagementService.getDomainRuntimeMBeanServer(kernelId);
      if (var6 != null) {
         var7 = this.envCtx.createSubcontext("jmx");
         var7.bind("domainRuntime", var6);
      }

      DisconnectMonitorListImpl.bindToJNDI(var5);
      var5.bind("HandleDelegate", handleDelegate);
   }

   public BaseEnvironmentBuilder(Context var1, String var2, String var3) throws NamingException {
      this(var1, var2, var3, var3);
   }

   public Context getContext() {
      return this.rootCtx;
   }

   public Context getEnvCtx() {
      return this.envCtx;
   }

   protected LinkRef createLinkRef(String var1) {
      return new LinkRef(this.transformJNDIName(var1));
   }

   protected abstract String transformJNDIName(String var1);

   public void addEnvironmentEntries(Collection var1) throws NamingException, EnvironmentException {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.bindEnvEntry(var2.next());
      }

   }

   public void removeEnvironmentEntries(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.unbindEnvEntry(var2.next());
      }

   }

   protected abstract void bindEnvEntry(Object var1) throws NamingException, EnvironmentException;

   protected abstract Map createJMSPoolProperties(String var1, AuthenticatedSubject var2) throws NamingException;

   protected void bindResRef(String var1, boolean var2, boolean var3, String var4, String var5, int var6) throws NamingException {
      if ("javax.mail.Session".equals(var1) && !var2) {
         MailSessionOpaqueReferenceImpl var9 = new MailSessionOpaqueReferenceImpl(var4, this.applicationName);
         this.envCtx.bind(var5, var9);
      } else if ("javax.jms.ConnectionFactory".equals(var1) && !var2) {
         String var7 = this.applicationName + "-" + this.componentName + "-" + var4;
         JMSConnFactoryOpaqueReferenceImpl var8 = new JMSConnFactoryOpaqueReferenceImpl(var7, var6, var3, this.createJMSPoolProperties(var4, (AuthenticatedSubject)null));
         this.envCtx.bind(var5, var8);
      } else {
         this.envCtx.bind(var5, new LinkRef(var4));
      }

   }

   protected abstract void unbindEnvEntry(Object var1);

   protected Object findObject(String var1) {
      try {
         return this.findObject(var1, new InitialContext());
      } catch (NamingException var3) {
         throw new AssertionError(var3);
      }
   }

   protected Object findObject(String var1, Context var2) {
      Object var3 = null;

      try {
         if (var1.indexOf(":") > 0 && var1.indexOf("java:") == -1) {
            return null;
         } else {
            var3 = var2.lookup(var1);
            return var3;
         }
      } catch (NamingException var5) {
         return null;
      }
   }

   protected Object findObjectOrCreateLinkRef(String var1) {
      Object var2 = this.findObject(var1);
      if (var2 == null) {
         var2 = this.createLinkRef(var1);
      }

      return var2;
   }

   protected boolean lookUpDataSrc(String var1) {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      if (var2.getDomain().lookupJDBCConnectionPool(var1) != null) {
         return true;
      } else if (var2.getDomain().lookupJDBCDataSource(var1) != null) {
         return true;
      } else if (var2.getDomain().lookupJDBCMultiPool(var1) != null) {
         return true;
      } else {
         return var2.getDomain().lookupJDBCTxDataSource(var1) != null;
      }
   }

   protected boolean lookUpGlobalJNDI(String var1) {
      InitialContext var2 = null;

      boolean var4;
      try {
         var2 = new InitialContext();
         Object var3 = var2.lookup(var1);
         if (var3 != null) {
            var4 = true;
            return var4;
         }

         return false;
      } catch (NamingException var15) {
         var4 = false;
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (Exception var14) {
         }

      }

      return var4;
   }

   private void addUserTransaction() throws NamingException {
      Object var1 = this.findObjectOrCreateLinkRef("javax/transaction/UserTransaction");
      this.rootCtx.bind("comp/UserTransaction", var1);
      if (DEBUG) {
         System.out.println("Bound javax/transaction/UserTransaction to " + this.rootCtx.getNameInNamespace() + ":" + "comp/UserTransaction");
      }

   }

   private void addTransactionManager() throws NamingException {
      Object var1 = this.findObjectOrCreateLinkRef("javax/transaction/TransactionManager");
      this.rootCtx.bind("comp/TransactionSynchronizationRegistry", var1);
      if (DEBUG) {
         System.out.println("Bound javax/transaction/TransactionManager to " + this.rootCtx.getNameInNamespace() + ":" + "comp/TransactionSynchronizationRegistry");
      }

   }

   private void removeTransactionManager() {
      try {
         this.rootCtx.unbind("comp/TransactionSynchronizationRegistry");
      } catch (NamingException var2) {
         if (DEBUG) {
            var2.printStackTrace();
         }
      }

   }

   private void addDefaultJ2EEWorkManager() throws NamingException {
      WorkManager var1 = J2EEWorkManager.getDefault(this.applicationName);
      if (var1 != null) {
         this.envCtx.bind("wm/default", var1);
      }
   }

   private void addORB() throws NamingException {
      this.rootCtx.bind("comp/ORB", new Reference("org.omg.CORBA.ORB", "weblogic.deployment.ORBObjectFactory", (String)null));
   }

   protected void autowireEJBRef(String var1, String var2, String var3) throws NamingException, EnvironmentException {
      Context var4 = null;

      try {
         var4 = (Context)this.rootCtx.lookup("app/ejb");
      } catch (NamingException var8) {
      }

      String var5 = var2;
      if (var2 == null) {
         var5 = var3;
      }

      String var6 = null;
      if (var4 != null) {
         var6 = this.findejbinfo(var4, var1, var5, var6);
      }

      if (var6 == null) {
         Loggable var7 = J2EELogger.logFailedToAutoLinkEjbRefNoMatchesLoggable(var1, this.moduleName, this.applicationName, var5);
         throw new EnvironmentException(var7.getMessage());
      } else {
         this.envCtx.bind(var1, new LinkRef("java:app/ejb/" + var6));
      }
   }

   private String findejbinfo(Context var1, String var2, String var3, String var4) throws NamingException, EnvironmentException {
      NamingEnumeration var5 = var1.listBindings("");

      while(var5.hasMoreElements()) {
         Binding var6 = (Binding)var5.nextElement();
         Object var7 = var6.getObject();
         if (var7 instanceof Context) {
            EjbInfo var8 = null;

            try {
               var8 = (EjbInfo)((Context)var7).lookup("ejb-info");
            } catch (Exception var10) {
            }

            if (var8 != null && var8.implementsInterface(var3)) {
               if (var4 != null) {
                  Loggable var9 = J2EELogger.logFailedToAutoLinkEjbRefMultipleInterfacesLoggable(var2, this.moduleName, this.applicationName, var3);
                  throw new EnvironmentException(var9.getMessage());
               }

               var4 = var6.getName() + "/" + var3;
            }

            if (var8 == null) {
               var4 = this.findejbinfo((Context)var7, var2, var3, var4);
            }
         }
      }

      return var4;
   }

   protected void addEJBLinkRef(String var1, String var2, String var3, String var4, String var5, boolean var6) throws NamingException, EnvironmentException {
      Context var7 = null;

      Loggable var9;
      try {
         var7 = (Context)this.rootCtx.lookup("app/ejb");
      } catch (NamingException var14) {
         var9 = J2EELogger.logNoEJBDeploymentsFoundForLinkRefLoggable(this.applicationName, this.componentName);
         throw new EnvironmentException(var9.getMessage());
      }

      String var8 = J2EEUtils.getAppScopedLinkPath(var2, var5, var7);
      if (var8 == null) {
         var9 = J2EELogger.logUnableToResolveEJBLinkLoggable(var2, var1, var5);
         throw new EnvironmentException(var9.getMessage());
      } else {
         var9 = null;

         Loggable var11;
         EjbInfo var15;
         try {
            var15 = (EjbInfo)(new InitialContext()).lookup("java:app/ejb/" + var8 + "/ejb-info");
         } catch (NamingException var13) {
            var11 = J2EELogger.logUnableToResolveEJBLinkLoggable(var2, var1, var5);
            throw new EnvironmentException(var11.getMessage());
         }

         String var10 = null;
         Loggable var12;
         if (var3 != null) {
            var10 = var3;
            if (var6 && var15.getLocalHomeName() != null) {
               var10 = var15.getLocalHomeName();
            }

            if (!var6 && var15.getHomeName() != null) {
               var10 = var15.getHomeName();
            }
         } else if (var4 != null) {
            var10 = var4;
         } else {
            String[] var16 = var15.getImplementedInterfaceNames();
            if (var16.length == 1) {
               var10 = var16[0];
            } else {
               if (var16.length != 2) {
                  var12 = J2EELogger.logFailedToCreateEjbRefMultipleInterfacesLoggable(var1, this.moduleName, this.applicationName);
                  throw new EnvironmentException(var12.getMessage());
               }

               if (var15.getHomeName() != null && var15.getLocalHomeName() != null) {
                  if (var6) {
                     var10 = var15.getLocalHomeName();
                  } else {
                     var10 = var15.getHomeName();
                  }
               }
            }
         }

         if (!var15.implementsInterface(var10)) {
            var11 = J2EELogger.logEJBRefTargetDoesNotImplementInterfaceLoggable(var1, this.moduleName, this.applicationName, var10);
            throw new EnvironmentException(var11.getMessage());
         } else {
            if (DEBUG) {
               Debug.say("binding-java:app/ejb/" + var8 + "/" + var10 + " to " + var1);
            }

            this.envCtx.bind(var1, new LinkRef("java:app/ejb/" + var8 + "/" + var10));
            if (var3 != null) {
               Object var17 = this.envCtx.lookup(var1);
               if (var6 && !ClassLoaderUtils.visibleToClassLoader(var17)) {
                  var12 = J2EELogger.logEjbLocalRefNotVisibleLoggable(this.applicationName, this.componentName, var8);
                  throw new EnvironmentException(var12.getMessage());
               }
            }

         }
      }
   }

   public void addServiceReferences(Collection var1, Collection var2, ServletContext var3, String var4) throws NamingException, EnvironmentException {
      HashMap var5 = new HashMap();
      Iterator var6;
      if (var2 != null) {
         var6 = var2.iterator();

         while(var6.hasNext()) {
            ServiceReferenceDescriptionBean var7 = (ServiceReferenceDescriptionBean)var6.next();
            var5.put(var7.getServiceRefName(), var7);
         }
      }

      var6 = var1.iterator();

      while(var6.hasNext()) {
         ServiceRefBean var11 = (ServiceRefBean)var6.next();
         ServiceReferenceDescriptionBean var8 = (ServiceReferenceDescriptionBean)var5.get(var11.getServiceRefName());

         try {
            ServiceRefProcessor var9 = ServiceRefProcessorFactory.getInstance().getProcessor(var11, var8, var3);
            var9.bindServiceRef(this.rootCtx, this.envCtx, var4);
            this.serviceRefHelpers.add(var9);
         } catch (ServiceRefProcessorException var10) {
            throw new EnvironmentException(var10.getMessage(), var10);
         }
      }

   }

   public void removeUserTransaction() {
      try {
         this.rootCtx.unbind("comp/UserTransaction");
      } catch (NamingException var2) {
         if (DEBUG) {
            var2.printStackTrace();
         }
      }

   }

   public void removeDefaultJ2EEWorkManager() {
      try {
         this.envCtx.unbind("wm/default");
         this.envCtx.destroySubcontext("wm");
      } catch (NamingException var2) {
         if (DEBUG) {
            var2.printStackTrace();
         }
      }

   }

   public void removeORB() {
      try {
         this.rootCtx.unbind("comp/ORB");
      } catch (NamingException var2) {
         if (DEBUG) {
            var2.printStackTrace();
         }
      }

   }

   public void removeHandleDelegate() {
      try {
         Context var1 = (Context)this.rootCtx.lookup("comp");
         var1.unbind("HandleDelegate");
      } catch (NamingException var2) {
         if (DEBUG) {
            var2.printStackTrace();
         }
      }

   }

   public void removeServiceReferences(Collection var1) {
      Iterator var2 = this.serviceRefHelpers.iterator();

      while(var2.hasNext()) {
         ServiceRefProcessor var3 = (ServiceRefProcessor)var2.next();

         try {
            var3.unbindServiceRef(this.envCtx);
         } catch (NamingException var5) {
            if (DEBUG) {
               var5.printStackTrace();
            }
         }
      }

      this.serviceRefHelpers.clear();
   }

   public void removeStandardEntries() {
      this.removeUserTransaction();
      this.removeORB();
      this.removeHandleDelegate();
      this.removeDefaultJ2EEWorkManager();
      this.removeTransactionManager();

      try {
         this.destroyContextsRecursively((Context)this.envCtx.lookup("wls-connector-resref"), this.envCtx, "wls-connector-resref");
         Context var1 = (Context)this.envCtx.lookup("jmx");
         if (var1 != null) {
            var1.unbind("runtime");

            try {
               var1.unbind("domainRuntime");
            } catch (NamingException var3) {
               if (DEBUG) {
                  var3.printStackTrace();
               }
            }

            this.envCtx.destroySubcontext("jmx");
         }

         Context var2 = (Context)this.rootCtx.lookup("comp");
         this.destroyContextsRecursively(this.envCtx, var2, "env");
         WorkContextHelper.unbind(var2);
         DisconnectMonitorListImpl.unbindFromJNDI(var2);
         this.destroyContextsRecursively(var2, this.rootCtx, "comp");
         this.rootCtx.unbind("bea/ModuleName");
         this.rootCtx.destroySubcontext("bea");
      } catch (NamingException var4) {
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

   }

   private void destroyContextsRecursively(Context var1, Context var2, String var3) {
      if (var1 != null) {
         if (DEBUG) {
            J2EELogger.logDebug("Cleanup the intermediateContexts for :" + var1);
         }

         try {
            NamingEnumeration var4 = var1.listBindings("");

            while(var4.hasMoreElements()) {
               Object var5 = var4.next();
               Binding var6 = (Binding)var5;
               String var7 = var6.getName();
               var5 = var6.getObject();
               if (DEBUG) {
                  J2EELogger.logDebug("Removing " + var5);
               }

               if (var5 instanceof Context) {
                  Context var8 = (Context)var5;
                  this.destroyContextsRecursively(var8, var1, var7);
               } else {
                  J2EELogger.logDebug("Warning : Binding" + var7 + " still exists for Context :" + var1 + ". Unbinding it.");
                  var1.unbind(var7);
               }
            }

            var2.destroySubcontext(var3);
         } catch (NamingException var9) {
            J2EELogger.logDebug("Naming exception while removing  intermediate contexts for " + var1);
            if (DEBUG) {
               var9.printStackTrace();
            }
         }

      }
   }

   public void addMessageDestinationReferences(Collection var1, String var2) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         MessageDestinationRefBean var4 = (MessageDestinationRefBean)var3.next();
         Context var5 = (Context)this.rootCtx.lookup("app/messageDestination");
         String var6 = var4.getMessageDestinationLink() == null ? var4.getMessageDestinationRefName() : var4.getMessageDestinationLink();
         String var7 = J2EEUtils.getAppScopedLinkPath(var6, var2, var5);
         String var8 = var4.getMappedName();
         if (var7 == null && var8 == null) {
            var8 = var6;
         }

         MessageDestinationDescriptorBean var9 = null;
         String var10 = null;
         String var11 = null;
         String var12 = null;
         if (var7 != null) {
            var7 = J2EEUtils.normalizeJNDIName(var7);

            try {
               var9 = (MessageDestinationDescriptorBean)var5.lookup(var7);
               var11 = var9.getInitialContextFactory();
               var12 = var9.getProviderUrl();
               var10 = var9.getDestinationJNDIName();
               var10 = transformJNDIName(var10, this.applicationName);
            } catch (NamingException var15) {
               Loggable var14 = J2EELogger.logUnableToResolveMessageDestinationLinkLoggable(var4.getMessageDestinationLink(), var4.getMessageDestinationRefName(), var2);
               throw new EnvironmentException(var14.getMessage());
            }
         } else {
            var11 = "weblogic.jndi.WLInitialContextFactory";
            var10 = var8;
         }

         MessageDestinationReference var13 = new MessageDestinationReference(var10, var11, var12, var4.getMessageDestinationType(), "weblogic.deployment.BaseEnvironmentBuilder.MessageDestinationObjectFactory");
         this.envCtx.bind(var4.getMessageDestinationRefName(), var13);
      }

   }

   public void removeMessageDestinationReferences(Collection var1) {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         MessageDestinationRefBean var3 = (MessageDestinationRefBean)var2.next();

         try {
            this.envCtx.unbind(var3.getMessageDestinationRefName());
         } catch (NamingException var5) {
            if (DEBUG) {
               var5.printStackTrace();
            }
         }
      }

   }

   public static String transformJNDIName(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         return var0.indexOf("${APPNAME}") != -1 ? StringUtils.replaceGlobal(var0, "${APPNAME}", var1) : var0;
      }
   }

   protected void removeTimerManagerRef(String var1) {
      TimerManager var2 = null;

      try {
         var2 = (TimerManager)this.envCtx.lookup(var1);
         this.envCtx.unbind(var1);
      } catch (NamingException var7) {
         J2EELogger.logDebug("Cannot unbind " + var1);
         if (DEBUG) {
            var7.printStackTrace();
         }
      } finally {
         if (var2 != null) {
            var2.stop();
         }

      }

   }

   protected void addTimerManager(String var1) throws NamingException, EnvironmentException {
      if (var1 != null && var1.length() != 0) {
         ApplicationContextInternal var2 = ApplicationAccess.getApplicationAccess().getApplicationContext(this.applicationName);
         StringBuffer var3 = new StringBuffer();
         if (this.applicationName != null) {
            var3.append(this.applicationName);
         }

         if (this.moduleName != null) {
            var3.append("@" + this.moduleName);
         }

         if (this.componentName != null) {
            var3.append("@" + this.componentName);
         }

         var3.append("@" + var1);
         this.envCtx.bind(var1, TimerManagerFactory.getTimerManagerFactory().getCommonjTimerManager(var3.toString(), var2.getWorkManagerCollection().getDefault()));
      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   public void addPersistenceContextRefs(PersistenceContextRefBean[] var1, ClassLoader var2, PersistenceUnitRegistryProvider var3) throws EnvironmentException, NamingException {
      for(int var4 = 0; var1 != null && var4 < var1.length; ++var4) {
         Object var5 = this.createPersistenceContextProxy(var1[var4], var2, var3);
         this.envCtx.bind(var1[var4].getPersistenceContextRefName(), var5);
      }

   }

   public void removePersistenceContextRefs(PersistenceContextRefBean[] var1) {
      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         try {
            this.envCtx.unbind(var1[var2].getPersistenceContextRefName());
         } catch (NamingException var4) {
            if (System.getProperty("weblogic.compenv.debug") != null) {
               var4.printStackTrace();
            }
         }
      }

   }

   public void addPersistenceUnitRefs(PersistenceUnitRefBean[] var1, ClassLoader var2, PersistenceUnitRegistryProvider var3) throws EnvironmentException, NamingException {
      for(int var4 = 0; var1 != null && var4 < var1.length; ++var4) {
         Object var5 = this.getPersistenceContextFactory(var1[var4], var3, var2);
         this.envCtx.bind(var1[var4].getPersistenceUnitRefName(), var5);
      }

   }

   public void removePersistenceUnitRefs(PersistenceUnitRefBean[] var1) {
      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         try {
            this.envCtx.unbind(var1[var2].getPersistenceUnitRefName());
         } catch (NamingException var4) {
            if (System.getProperty("weblogic.compenv.debug") != null) {
               var4.printStackTrace();
            }
         }
      }

   }

   private Class getInjectionType(Class var1, String var2) throws EnvironmentException {
      Class var3 = this.getInjectionTypeForField(var1, var2);
      if (var3 == null) {
         var3 = this.getInjectionTypeForMethod(var1, var2);
      }

      if (var3 == null) {
         throw new EnvironmentException("Unable to find injection target named: " + var2 + " on class: " + var1);
      } else {
         return var3;
      }
   }

   private Class getInjectionTypeForField(Class var1, String var2) {
      try {
         return var1.getDeclaredField(var2).getType();
      } catch (NoSuchFieldException var4) {
         return null;
      }
   }

   private Class getInjectionTypeForMethod(Class var1, String var2) {
      char var3 = var2.charAt(0);
      String var4 = ("" + var3).toUpperCase(Locale.US) + var2.substring(1);
      var4 = "set" + var4;
      Method[] var5 = var1.getDeclaredMethods();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (var5[var6].getName().equals(var4)) {
            Class[] var7 = var5[var6].getParameterTypes();
            if (var7.length == 1) {
               return var7[0];
            }
         }
      }

      return null;
   }

   private Object createPersistenceContextProxy(PersistenceContextRefBean var1, ClassLoader var2, PersistenceUnitRegistryProvider var3) throws EnvironmentException {
      List var4 = this.computeInterfaces(var1.getInjectionTargets(), var2);
      PersistenceUnitRegistry var5 = var3.getPersistenceUnitRegistry();
      String var6 = this.getPersistenceUnitName(var1.getPersistenceUnitName(), var5, var1.getInjectionTargets());
      var1.setPersistenceUnitName(var6);
      Object var7;
      if (var1.getPersistenceContextType().equals("Transaction")) {
         if (this.isJDOPersistenceContext(var1, var1.getInjectionTargets(), var4, var2)) {
            var7 = new TransactionalPersistenceManagerProxyImpl(this.applicationName, this.moduleName, var6, var5);
         } else {
            var7 = EntityManagerInvocationHandlerFactory.createTransactionalEntityManagerInvocationHandler(this.applicationName, this.moduleName, var6, var5);
         }
      } else {
         var7 = EntityManagerInvocationHandlerFactory.createExtendedEntityManagerInvocationHandler(this.applicationName, this.moduleName, var6, var5);
      }

      return Proxy.newProxyInstance(var2, (Class[])((Class[])var4.toArray(new Class[var4.size()])), (InvocationHandler)var7);
   }

   private List computeInterfaces(InjectionTargetBean[] var1, ClassLoader var2) throws EnvironmentException {
      ArrayList var3 = new ArrayList();
      if (var1.length == 0) {
         var3.add(EntityManager.class);
      } else {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            try {
               Class var5 = var2.loadClass(var1[var4].getInjectionTargetClass());
               Class var6 = this.getInjectionType(var5, var1[var4].getInjectionTargetName());
               if (var6.isInterface()) {
                  var3.add(var6);
               }

               Enumeration var7 = ReflectUtils.allInterfaces(var6);

               while(var7.hasMoreElements()) {
                  var3.add(var7.nextElement());
               }
            } catch (ClassNotFoundException var8) {
               throw new EnvironmentException("Error loading class: ", var8);
            }
         }
      }

      return var3;
   }

   private boolean isJDOPersistenceContext(PersistenceContextRefBean var1, InjectionTargetBean[] var2, Collection var3, ClassLoader var4) throws EnvironmentException {
      if (var3 == null) {
         var3 = this.computeInterfaces(var2, var4);
      }

      boolean var5 = ((Collection)var3).contains(EntityManager.class);
      boolean var6 = ((Collection)var3).contains(PersistenceManager.class);
      if (var5 && var6) {
         throw new EnvironmentException("Persistence context " + var1 + " has injection target(s) that implement both " + EntityManager.class.getName() + " and " + PersistenceManager.class.getName() + ". This is illegal.");
      } else if (!var5 && !var6) {
         throw new EnvironmentException("Persistence context " + var1 + " has injection target(s) that implement neither  " + EntityManager.class.getName() + " nor " + PersistenceManager.class.getName() + ". This is illegal.");
      } else {
         return var6;
      }
   }

   private boolean isJDOPersistenceContextFactory(PersistenceUnitRefBean var1, InjectionTargetBean[] var2, Collection var3, ClassLoader var4) throws EnvironmentException {
      if (var3 == null) {
         if (var2.length == 0) {
            return false;
         }

         var3 = this.computeInterfaces(var2, var4);
      }

      boolean var5 = ((Collection)var3).contains(EntityManagerFactory.class);
      boolean var6 = ((Collection)var3).contains(PersistenceManagerFactory.class);
      if (var5 && var6) {
         throw new EnvironmentException("Persistence unit " + var1 + " has injection target(s) that implement both " + EntityManagerFactory.class.getName() + " and " + PersistenceManagerFactory.class.getName() + ". This is illegal.");
      } else if (!var5 && !var6) {
         throw new EnvironmentException("Persistence unit " + var1 + " has injection target(s) that implement neither  " + EntityManagerFactory.class.getName() + " nor " + PersistenceManagerFactory.class.getName() + ". This is illegal.");
      } else {
         return var6;
      }
   }

   private Object getPersistenceContextFactory(PersistenceUnitRefBean var1, PersistenceUnitRegistryProvider var2, ClassLoader var3) throws EnvironmentException {
      PersistenceUnitRegistry var4 = var2.getPersistenceUnitRegistry();
      String var5 = this.getPersistenceUnitName(var1.getPersistenceUnitName(), var4, var1.getInjectionTargets());
      PersistenceUnitInfoImpl var6 = var4.getPersistenceUnit(var5);
      EntityManagerFactory var7 = var6.getEntityManagerFactory();
      EntityManagerFactoryProxyImpl var8 = (EntityManagerFactoryProxyImpl)Proxy.getInvocationHandler(var7);
      var8.setAppName(this.applicationName);
      var8.setModuleName(this.moduleName);
      return this.isJDOPersistenceContextFactory(var1, var1.getInjectionTargets(), (Collection)null, var3) ? TransactionalPersistenceManagerProxyImpl.toPersistenceManagerFactory(var7) : var7;
   }

   private String getPersistenceUnitName(String var1, PersistenceUnitRegistry var2, InjectionTargetBean[] var3) throws EnvironmentException {
      if (var1 != null && !"".equals(var1)) {
         return var1;
      } else {
         Collection var4 = var2.getPersistenceUnitNames();
         if (var4.size() == 1) {
            return (String)var4.iterator().next();
         } else if (var3 != null && var3.length == 1) {
            return var3[0].getInjectionTargetName();
         } else if (var3 != null && var3.length != 0) {
            throw new EnvironmentException("PersistenceContext refs defined with multiple injection targets must explicitly name a persistence unit.");
         } else {
            throw new EnvironmentException("PersistenceContext refs defined without any injection targets must explicitly name a persistence unit.");
         }
      }
   }

   public static final class MessageDestinationObjectFactory implements ObjectFactory {
      public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
         MessageDestinationReference var5 = (MessageDestinationReference)var1;
         String var6 = var5.getDestinationJNDIName();
         String var7 = var5.getInitialContextFactory();
         String var8 = var5.getProviderURL();
         Hashtable var9 = new Hashtable();
         var9.put("java.naming.factory.initial", var7);
         if (null != var8) {
            var9.put("java.naming.provider.url", var8);
         }

         InitialContext var10 = new InitialContext(var9);
         return var10.lookup(var6);
      }
   }

   public static final class MessageDestinationReference extends Reference {
      private String destinationJNDIName;
      private String initialContextFactory;
      private String providerURL;

      public MessageDestinationReference(String var1, String var2, String var3, String var4, String var5) {
         super(var4, var5, (String)null);
         this.destinationJNDIName = var1;
         this.initialContextFactory = var2;
         this.providerURL = var3;
      }

      public String getDestinationJNDIName() {
         return this.destinationJNDIName;
      }

      public String getInitialContextFactory() {
         return this.initialContextFactory;
      }

      public String getProviderURL() {
         return this.providerURL;
      }

      public String getFactoryClassName() {
         return MessageDestinationObjectFactory.class.getName();
      }
   }
}
