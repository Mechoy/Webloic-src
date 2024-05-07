package weblogic.ejb.container.deployer;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.rmi.Remote;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import weblogic.application.ApplicationContextInternal;
import weblogic.deployment.BaseEnvironmentBuilder;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.dd.ClusteringDescriptor;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.dd.DDDefaults;
import weblogic.ejb.container.deployer.mbimpl.MethodInfoImpl;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.QueryCache;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.internal.WSObjectFactoryImpl;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.monitoring.EJBRuntimeMBeanImpl;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.ExecuteQueueRuntimeMBean;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.annotation.BeaSynthetic.Helper;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.CombinedIterator;
import weblogic.utils.reflect.MethodText;
import weblogic.wsee.deploy.DeployUtil;

abstract class ClientDrivenBeanInfoImpl extends BeanInfoImpl implements ClientDrivenBeanInfo {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Name localJndiName;
   private String localJndiNameAsString;
   private String homeInterfaceName;
   private String remoteInterfaceName;
   private String homeClassName;
   private String ejbObjectClassName;
   private Class homeInterfaceClass;
   private Class remoteInterfaceClass;
   private Class homeClass;
   private Class ejbObjectClass;
   private String localHomeInterfaceName;
   private String localInterfaceName;
   private String localHomeClassName;
   private String ejbLocalObjectClassName;
   private Class localHomeInterfaceClass;
   private Class localInterfaceClass;
   private Class localHomeClass;
   private Class localClass;
   private String serviceEndpointName;
   private Class serviceEndpointClass;
   private String webserviceObjectClassName;
   private Class webserviceObjectClass;
   protected WSObjectFactory webserviceObjectFactory;
   protected final Map remoteMethods = new HashMap();
   protected final Map homeMethods = new HashMap();
   protected final Map localHomeMethods = new HashMap();
   protected final Map localMethods = new HashMap();
   protected Map webserviceMethods = null;
   private BaseEJBRemoteHomeIntf remoteHome;
   private BaseEJBLocalHomeIntf localHome;
   private ClusteringDescriptor clusteringDescriptor;
   private boolean callByReference;
   protected PersistenceUnitRegistry persistenceUnitRegistry;
   protected EjbJndiBinder jndiBinder;
   private Class[] immutableClasses = new Class[]{Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, String.class, BigDecimal.class};

   ClientDrivenBeanInfoImpl(DeploymentInfo var1, CompositeMBeanDescriptor var2, GenericClassLoader var3) throws ClassNotFoundException, WLDeploymentException {
      super(var1, var2, var3);
      if (isServer()) {
         this.checkSecurityRoleRefs();
      }

      this.homeInterfaceName = var2.getHomeInterfaceName();
      this.remoteInterfaceName = var2.getRemoteInterfaceName();
      this.localHomeInterfaceName = var2.getLocalHomeInterfaceName();
      this.localInterfaceName = var2.getLocalInterfaceName();
      if (var2.isStatelessSession()) {
         this.serviceEndpointName = var2.getServiceEndpointName();
      }

      this.checkClientViews();
   }

   public void init() throws ClassNotFoundException, WLDeploymentException {
      NamingConvention var1 = new NamingConvention(this.beanClassName, this.ejbName);
      boolean var2 = false;
      if (this.isSessionBean()) {
         var2 = "stateful".equalsIgnoreCase(((SessionBeanBean)this.m_desc.getBean()).getSessionType());
      }

      this.localJndiNameAsString = BaseEnvironmentBuilder.transformJNDIName(this.m_desc.getLocalJNDIName(), this.deploymentInfo.getApplicationName());
      this.localJndiName = this.getName(this.localJndiNameAsString);
      if (this.hasRemoteClientView()) {
         if (this.hasDeclaredRemoteHome()) {
            this.homeClassName = var1.getHomeClassName();
            this.homeInterfaceClass = this.loadClass(this.homeInterfaceName);
            this.checkClassLoaders(this.m_desc, this.homeInterfaceClass);
            this.remoteInterfaceClass = this.loadClass(this.remoteInterfaceName);
            this.checkClassLoaders(this.m_desc, this.remoteInterfaceClass);
         } else {
            this.homeClassName = var1.getHomeImplClassName(var2);
         }

         this.ejbObjectClassName = var1.getEJBObjectClassName();
         this.clusteringDescriptor = this.m_desc.getClusteringDescriptor();
      }

      if (this.hasLocalClientView()) {
         if (this.hasDeclaredLocalHome()) {
            this.localHomeClassName = var1.getLocalHomeClassName();
            this.localHomeInterfaceClass = this.loadClass(this.localHomeInterfaceName);
            this.checkClassLoaders(this.m_desc, this.localHomeInterfaceClass);
            this.localInterfaceClass = this.loadClass(this.localInterfaceName);
            this.checkClassLoaders(this.m_desc, this.localInterfaceClass);
         } else {
            this.localHomeClassName = var1.getLocalHomeImplClassName(var2);
         }

         this.ejbLocalObjectClassName = var1.getEJBLocalObjectClassName();
      }

      if (this.hasWebserviceClientView()) {
         this.webserviceObjectClassName = var1.getWsObjectClassName();
         if (this.serviceEndpointName != null) {
            this.serviceEndpointClass = this.loadClass(this.serviceEndpointName);
         }

         this.webserviceObjectClass = null;
      }

      this.callByReference = this.m_desc.useCallByReference();
      if (!this.callByReference) {
         this.callByReference = this.checkIfItsSafeToUseCallByReference();
      }

      if (!this.callByReference) {
         this.warnIfParameterNotSerializable();
      }

      this.initializeMethodInfos();
   }

   public abstract String getGeneratedBeanClassName();

   public abstract Class getGeneratedBeanClass();

   public abstract String getGeneratedBeanInterfaceName();

   public abstract Class getGeneratedBeanInterface();

   public Name getLocalJNDIName() {
      return this.localJndiName;
   }

   public String getLocalJNDINameAsString() {
      return this.localJndiNameAsString;
   }

   public Class getHomeClass() {
      try {
         if (this.homeClass == null) {
            this.homeClass = this.loadClass(this.homeClassName);
         }

         return this.homeClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public Class getLocalHomeClass() {
      try {
         if (this.localHomeClass == null) {
            this.localHomeClass = this.loadClass(this.localHomeClassName);
         }

         return this.localHomeClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public String getHomeInterfaceName() {
      return this.homeInterfaceName;
   }

   public String getLocalHomeInterfaceName() {
      return this.localHomeInterfaceName;
   }

   public boolean hasDeclaredRemoteHome() {
      return this.homeInterfaceName != null;
   }

   public boolean hasDeclaredLocalHome() {
      return this.localHomeInterfaceName != null;
   }

   public Class getHomeInterfaceClass() {
      return this.homeInterfaceClass;
   }

   public Class getLocalHomeInterfaceClass() {
      return this.localHomeInterfaceClass;
   }

   public Class getRemoteClass() {
      try {
         if (this.ejbObjectClass == null) {
            this.ejbObjectClass = this.loadClass(this.ejbObjectClassName);
         }

         return this.ejbObjectClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public Class getLocalClass() {
      try {
         if (this.localClass == null) {
            this.localClass = this.loadClass(this.ejbLocalObjectClassName);
         }

         return this.localClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public Class getWebserviceObjectClass() {
      try {
         if (this.webserviceObjectClass == null) {
            this.webserviceObjectClass = this.loadClass(this.webserviceObjectClassName);
         }

         return this.webserviceObjectClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public String getRemoteInterfaceName() {
      return this.remoteInterfaceName;
   }

   public String getLocalInterfaceName() {
      return this.localInterfaceName;
   }

   public String getServiceEndpointName() {
      return this.serviceEndpointName;
   }

   public Class getRemoteInterfaceClass() {
      return this.remoteInterfaceClass;
   }

   public Class getLocalInterfaceClass() {
      return this.localInterfaceClass;
   }

   public Class getServiceEndpointClass() {
      return this.serviceEndpointClass;
   }

   public boolean hasRemoteClientView() {
      return this.remoteInterfaceName != null;
   }

   public boolean hasLocalClientView() {
      return this.localInterfaceName != null;
   }

   public boolean hasWebserviceClientView() {
      return this.serviceEndpointName != null;
   }

   public BaseEJBRemoteHomeIntf getRemoteHome() {
      return this.remoteHome;
   }

   public BaseEJBLocalHomeIntf getLocalHome() {
      return this.localHome;
   }

   public ClusteringDescriptor getClusteringDescriptor() {
      return this.clusteringDescriptor;
   }

   private void initializeMethodInfos() throws WLDeploymentException {
      try {
         List var1;
         List var5;
         if (this.hasDeclaredRemoteHome()) {
            var1 = Arrays.asList((Object[])this.remoteInterfaceClass.getMethods());
            this.createMethodInfoImpls(var1, "Remote", this.remoteMethods);
            var5 = Arrays.asList((Object[])this.homeInterfaceClass.getMethods());
            this.createMethodInfoImpls(var5, "Home", this.homeMethods);
         }

         if (this.hasDeclaredLocalHome()) {
            ArrayList var4 = new ArrayList();
            var4.addAll(Arrays.asList((Object[])this.localInterfaceClass.getMethods()));
            var4.addAll(Arrays.asList((Object[])BaseEJBLocalObjectIntf.class.getMethods()));
            this.createMethodInfoImpls(var4, "Local", this.localMethods);
            var5 = Arrays.asList((Object[])this.localHomeInterfaceClass.getMethods());
            this.createMethodInfoImpls(var5, "LocalHome", this.localHomeMethods);
         }

         if (this.hasWebserviceClientView()) {
            if (this.isEJB30()) {
               var1 = DeployUtil.getWebServiceMethods(this.beanClass, this.serviceEndpointClass);
            } else {
               var1 = Arrays.asList((Object[])this.serviceEndpointClass.getMethods());
            }

            this.createMethodInfoImpls(var1, "ServiceEndpoint", this.getWebserviceMethods());
         }

      } catch (Throwable var3) {
         Loggable var2 = EJBLogger.logunableToInitializeInterfaceMethodInfoLoggable(this.ejbName, StackTraceUtils.throwable2StackTrace(var3));
         throw new WLDeploymentException(var2.getMessage(), var3);
      }
   }

   protected void createMethodInfoImpls(Collection var1, String var2, Map var3) {
      Iterator var4 = var1.iterator();

      MethodInfoImpl var7;
      for(boolean var5 = debugLogger.isDebugEnabled(); var4.hasNext(); var3.put(var7.getSignature(), var7)) {
         Method var6 = (Method)var4.next();
         var7 = MethodInfoImpl.createMethodInfoImpl(var6, var2, this.jaccPolicyContextId);
         if (var5) {
            debug(var2 + ".....result.put(" + var7.getSignature() + ")");
         }
      }

   }

   public MethodInfo getRemoteMethodInfo(String var1) {
      return (MethodInfo)this.remoteMethods.get(var1);
   }

   public MethodInfo getRemoteMethodInfo(String var1, String[] var2) {
      return (MethodInfo)this.remoteMethods.get(getMethodSignature(var1, var2));
   }

   public MethodInfo getRemoteMethodInfo(Method var1) {
      return (MethodInfo)this.remoteMethods.get(getMethodSignature(var1));
   }

   public Collection getAllRemoteMethodInfos() {
      return this.remoteMethods.values();
   }

   public MethodInfo getHomeMethodInfo(String var1) {
      return (MethodInfo)this.homeMethods.get(var1);
   }

   public MethodInfo getHomeMethodInfo(String var1, String[] var2) {
      return (MethodInfo)this.homeMethods.get(getMethodSignature(var1, var2));
   }

   public MethodInfo getHomeMethodInfo(Method var1) {
      return (MethodInfo)this.homeMethods.get(getMethodSignature(var1));
   }

   public Collection getAllHomeMethodInfos() {
      return this.homeMethods.values();
   }

   public MethodInfo getLocalMethodInfo(String var1) {
      return (MethodInfo)this.localMethods.get(var1);
   }

   public MethodInfo getLocalMethodInfo(String var1, String[] var2) {
      return (MethodInfo)this.localMethods.get(getMethodSignature(var1, var2));
   }

   public MethodInfo getLocalMethodInfo(Method var1) {
      return (MethodInfo)this.localMethods.get(getMethodSignature(var1));
   }

   public Collection getAllLocalMethodInfos() {
      return this.localMethods.values();
   }

   public MethodInfo getLocalHomeMethodInfo(String var1) {
      return (MethodInfo)this.localHomeMethods.get(var1);
   }

   public MethodInfo getLocalHomeMethodInfo(String var1, String[] var2) {
      return (MethodInfo)this.localHomeMethods.get(getMethodSignature(var1, var2));
   }

   public MethodInfo getLocalHomeMethodInfo(Method var1) {
      return (MethodInfo)this.localHomeMethods.get(getMethodSignature(var1));
   }

   public Collection getAllLocalHomeMethodInfos() {
      return this.localHomeMethods.values();
   }

   private Map getWebserviceMethods() {
      if (this.webserviceMethods == null) {
         this.webserviceMethods = new HashMap();
      }

      return this.webserviceMethods;
   }

   public MethodInfo getWebserviceMethodInfo(String var1) {
      return (MethodInfo)this.getWebserviceMethods().get(var1);
   }

   public MethodInfo getWebserviceMethodInfo(String var1, String[] var2) {
      return (MethodInfo)this.getWebserviceMethods().get(getMethodSignature(var1, var2));
   }

   public MethodInfo getWebserviceMethodInfo(Method var1) {
      return (MethodInfo)this.getWebserviceMethods().get(getMethodSignature(var1));
   }

   public Collection getAllWebserviceMethodInfos() {
      return this.getWebserviceMethods().values();
   }

   public Iterator getAllMethodInfosIterator() {
      ArrayList var1 = new ArrayList();
      if (this.hasRemoteClientView()) {
         var1.add(this.homeMethods.values().iterator());
         var1.add(this.remoteMethods.values().iterator());
      }

      if (this.hasLocalClientView()) {
         var1.add(this.localHomeMethods.values().iterator());
         var1.add(this.localMethods.values().iterator());
      }

      if (this.hasWebserviceClientView()) {
         var1.add(this.getWebserviceMethods().values().iterator());
         var1.add(this.localHomeMethods.values().iterator());
      }

      var1.add(this.getAllBeanMethodInfos().iterator());
      return new CombinedIterator(var1);
   }

   protected void setMethodDescriptors(BaseEJBHomeIntf var1, Class var2, String var3) throws WLDeploymentException {
      Method[] var4 = var2.getMethods();
      this.setMethodDescriptors(var1, var2, var4, var3);
   }

   protected void setMethodDescriptors(BaseEJBHomeIntf var1, Class var2, Method[] var3, String var4) throws WLDeploymentException {
      String var6 = EntityBeanInfoImpl.getCreateQuerySignature();

      for(int var7 = 0; var7 < var3.length; ++var7) {
         Method var8 = var3[var7];
         if (!Helper.isBeaSyntheticMethod(var8)) {
            boolean var9 = false;
            MethodInfo var5 = null;
            if (var4.equals("Remote")) {
               var5 = this.getRemoteMethodInfo(var8);
            } else if (var4.equals("Home")) {
               var5 = this.getHomeMethodInfo(var8);
            } else if (var4.equals("Local")) {
               var5 = this.getLocalMethodInfo(var8);
               var9 = true;
            } else if (var4.equals("LocalHome")) {
               var5 = this.getLocalHomeMethodInfo(var8);
               var9 = true;
            } else if (var4.equals("ServiceEndpoint")) {
               var5 = this.getWebserviceMethodInfo(var8);
               var9 = true;
            }

            if (var5 != null) {
               if (this instanceof EntityBeanInfoImpl && var6.equals(var5.getSignature())) {
                  EntityBeanInfo var12 = (EntityBeanInfo)this;
                  if (var12.isDynamicQueriesEnabled()) {
                     var5.setTransactionAttribute((short)1);
                     var5.setTxIsolationLevel(-1);
                     var5.setSelectForUpdate(0);
                     this.setMethodDescriptor(var1, var8, var2, var5, var9, "createQuery", var4);
                  }
               } else {
                  MethodText var10 = new MethodText();
                  var10.setMethod(var8);
                  var10.setOptions(128);
                  String var11 = var10.toString();
                  if (!var4.equals("Home") && !var4.equals("LocalHome")) {
                     if (!var4.equals("Remote") && !var4.equals("Local") && !var4.equals("ServiceEndpoint")) {
                        Debug.stackdump("encountered unknown interface type: '" + var4 + "'");
                     } else {
                        var11 = "eo_" + var11;
                     }
                  } else if ((!var11.equals("getEJBMetaData") || !var4.equals("Home")) && (!var11.equals("getHomeHandle") || !var4.equals("Home")) && (!var11.equals("getLocalHomeHandle") || !var4.equals("LocalHome"))) {
                     var11 = this.homeToBeanName(var11);
                  }

                  this.setMethodDescriptor(var1, var8, var2, var5, var9, var11, var4);
               }
            }
         }
      }

   }

   protected MethodDescriptor setMethodDescriptor(BaseEJBHomeIntf var1, Method var2, Class var3, MethodInfo var4, boolean var5, String var6, String var7) throws WLDeploymentException {
      MethodDescriptor var8;
      try {
         var8 = super.setMethodDescriptor(var1, var2, var3, var4, var5, var6, var7);
      } catch (Exception var10) {
         throw new WLDeploymentException(var10.toString());
      }

      Method var9 = this.perhapsGetBeanMethod(var2, var7);
      var8.setMethod(var9);
      return var8;
   }

   private String homeToBeanName(String var1) {
      if (!var1.startsWith("create") && !var1.startsWith("find") && !var1.startsWith("remove")) {
         return "ejbHome" + var1;
      } else {
         StringBuffer var2 = new StringBuffer("ejb" + var1);
         var2.setCharAt(3, Character.toUpperCase(var2.charAt(3)));
         return var2.toString();
      }
   }

   private Method perhapsGetBeanMethod(Method var1, String var2) {
      Class var3 = var1.getDeclaringClass();
      if ((var2.equals("Home") || var2.equals("LocalHome")) && this instanceof EntityBeanInfoImpl) {
         String var4 = var1.getName();
         if (var4.startsWith("find")) {
            Class var5 = this.getGeneratedBeanInterface();
            String var6 = "ejb" + var4.substring(0, 1).toUpperCase(Locale.ENGLISH) + var4.substring(1);

            try {
               Method var7 = var5.getMethod(var6, (Class[])var1.getParameterTypes());
               return var7;
            } catch (NoSuchMethodException var11) {
               Method[] var8;
               if (!var4.equals("findByPrimaryKey")) {
                  var8 = null;
                  Class var12;
                  if (var2.equals("Home")) {
                     var12 = this.getHomeInterfaceClass();
                  } else {
                     var12 = this.getLocalHomeInterfaceClass();
                  }

                  try {
                     Method var13 = var12.getMethod(var1.getName(), (Class[])var1.getParameterTypes());
                     return var13;
                  } catch (NoSuchMethodException var10) {
                     throw new AssertionError("ejbFindXXX method for '" + getMethodSignature(var1) + "' not found on class '" + var5.getName() + "'.");
                  }
               }

               var8 = var5.getMethods();

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  if (var8[var9].getName().equals("ejbFindByPrimaryKey")) {
                     return var8[var9];
                  }
               }
            }
         }
      }

      return var1;
   }

   private void dumpMethodDescriptors() {
      Field[] var1;
      if (this.hasRemoteClientView()) {
         debug("** Dumping Remote MethodDescriptor for: " + this.getDisplayName());
         if (this.hasDeclaredRemoteHome()) {
            var1 = this.getHomeClass().getFields();
            this.dumpMethodDescriptorFields(var1, this.remoteHome);
            var1 = this.getRemoteClass().getFields();
            this.dumpMethodDescriptorFields(var1, (Object)null);
         }
      }

      if (this.hasLocalClientView()) {
         debug("** Dumping Local MethodDescriptor for: " + this.getLocalJNDIName());
         if (this.hasDeclaredLocalHome()) {
            var1 = this.getLocalHomeClass().getFields();
            this.dumpMethodDescriptorFields(var1, this.localHome);
            var1 = this.getLocalClass().getFields();
            this.dumpMethodDescriptorFields(var1, (Object)null);
         }
      }

      if (this.hasWebserviceClientView()) {
         var1 = this.getWebserviceObjectClass().getFields();
         debug("** Dumping Webservice MethodDescriptor for: " + this.getServiceEndpointName());
         this.dumpMethodDescriptorFields(var1, (Object)null);
      }

   }

   private void dumpMethodInfos() {
      Iterator var1;
      if (this.hasRemoteClientView()) {
         debug("Dumping Remote MethodInfos for: " + this.getDisplayName());
         debug("Remote Methods:");
         var1 = this.getAllRemoteMethodInfos().iterator();

         while(var1.hasNext()) {
            debug(var1.next().toString());
         }

         if (this.hasDeclaredRemoteHome()) {
            debug("Home Methods:");
            var1 = this.getAllHomeMethodInfos().iterator();

            while(var1.hasNext()) {
               debug(var1.next().toString());
            }
         }
      }

      if (this.hasLocalClientView()) {
         debug("Dumping Local MethodInfos for: " + this.getLocalJNDIName());
         debug("Local Methods:");
         var1 = this.getAllLocalMethodInfos().iterator();

         while(var1.hasNext()) {
            debug(var1.next().toString());
         }

         if (this.hasDeclaredLocalHome()) {
            debug("Local Home Methods:");
            var1 = this.getAllLocalHomeMethodInfos().iterator();

            while(var1.hasNext()) {
               debug(var1.next().toString());
            }
         }
      }

      if (this.hasWebserviceClientView()) {
         debug("Dumping Webservice MethodInfos for: " + this.getEJBName());
         debug("Webservice Methods:");
         var1 = this.getAllWebserviceMethodInfos().iterator();

         while(var1.hasNext()) {
            debug(var1.next().toString());
         }
      }

   }

   public void prepare(ApplicationContextInternal var1, DeploymentInfo var2) throws WLDeploymentException {
      super.prepare(var1);
      BeanManager var3 = this.getBeanManager();
      this.registerRoleRefs();

      try {
         InitialContext var4;
         try {
            var4 = new InitialContext();
         } catch (NamingException var8) {
            AssertionError var6 = new AssertionError("Error creating InitialContext!");
            var6.initCause(var8);
            throw var6;
         }

         if (this.hasRemoteClientView()) {
            this.remoteHome = (BaseEJBRemoteHomeIntf)this.getHomeClass().newInstance();
            this.remoteHome.setBeanInfo(this);
            this.remoteHome.setDeploymentInfo(var2);
            this.remoteHome.setup(this, this.localHome, var3);
            if (this.hasDeclaredRemoteHome()) {
               this.setMethodDescriptors(this.remoteHome, this.getHomeClass(), "Home");
               this.setMethodDescriptors((BaseEJBHomeIntf)null, this.getRemoteClass(), "Remote");
            }

            if (this.getJNDIName() != null && this.hasDeclaredRemoteHome()) {
               try {
                  Remote var5 = (Remote)var4.lookup(this.getJNDIName());
                  if (!ServerHelper.isClusterable(var5) || ServerHelper.isLocal(var5)) {
                     Loggable var14 = EJBLogger.logJNDINameAlreadyInUseLoggable(this.getDisplayName(), this.getJNDINameAsString());
                     throw new WLDeploymentException(var14.getMessage());
                  }
               } catch (NamingException var10) {
               }
            }
         }

         if (this.hasLocalClientView()) {
            this.localHome = (BaseEJBLocalHomeIntf)this.getLocalHomeClass().newInstance();
            this.localHome.setBeanInfo(this);
            this.localHome.setDeploymentInfo(var2);
            this.localHome.setup(this, this.remoteHome, var3);
            if (this.hasDeclaredLocalHome()) {
               this.setMethodDescriptors(this.localHome, this.getLocalHomeClass(), "LocalHome");
               this.setMethodDescriptors((BaseEJBHomeIntf)null, this.getLocalClass(), "Local");
            }

            if (this.getLocalJNDIName() != null && this.hasDeclaredLocalHome()) {
               try {
                  var4.lookup(this.getLocalJNDIName());
                  Loggable var13 = EJBLogger.logJNDINameAlreadyInUseLoggable(this.getDisplayName(), this.getLocalJNDINameAsString());
                  throw new WLDeploymentException(var13.getMessage());
               } catch (NamingException var9) {
               }
            }
         }

         if (this.hasWebserviceClientView()) {
            this.webserviceObjectFactory = new WSObjectFactoryImpl(var3, this);

            try {
               this.setMethodDescriptors((BaseEJBHomeIntf)null, this.getWebserviceObjectClass(), "ServiceEndpoint");
            } catch (Throwable var7) {
               Debug.say(" development time message:  no webservice available for EJB '" + this.getEJBName() + "' " + var7.getMessage());
            }
         }

         if (debugLogger.isDebugEnabled()) {
            this.dumpMethodInfos();
            this.dumpMethodDescriptors();
         }

      } catch (InstantiationException var11) {
         throw new AssertionError(var11);
      } catch (IllegalAccessException var12) {
         throw new AssertionError(var12);
      }
   }

   public EjbJndiBinder getJndiBinder() {
      if (this.jndiBinder == null) {
         this.jndiBinder = new Ejb2JndiBinder(this);
      }

      return this.jndiBinder;
   }

   public void activate(Context var1, Map var2, Map var3, DeploymentInfo var4, Context var5) throws WLDeploymentException {
      BeanManager var6 = this.getBeanManager();
      if (var6 instanceof CachingManager) {
         EJBCache var7 = this.getCache(var2);
         if (var6 instanceof TTLManager) {
            QueryCache var8 = this.getQueryCache(var3);
            ((TTLManager)var6).setup(this.remoteHome, this.localHome, this, var5, var7, var8);
         } else {
            ((CachingManager)var6).setup(this.remoteHome, this.localHome, this, var5, var7);
         }
      } else {
         var6.setup(this.remoteHome, this.localHome, this, var5);
      }

      EJBRuntimeMBean var13 = var6.getEJBRuntimeMBean();
      if (var13 != null) {
         EJBRuntimeMBeanImpl var14 = (EJBRuntimeMBeanImpl)var13;
         String var9 = this.getDispatchPolicy();
         if (var9 == null || var9.trim().length() == 0) {
            var9 = "default";
         }

         ExecuteQueueRuntimeMBean[] var10 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getExecuteQueueRuntimes();
         ExecuteQueueRuntimeMBean var11 = null;

         for(int var12 = 0; var12 < var10.length; ++var12) {
            if (var10[var12].getName().equals(var9)) {
               var11 = var10[var12];
               break;
            }
         }

         if (var11 != null) {
            var14.addResource(var11);
         } else if (debugLogger.isDebugEnabled()) {
            debug("Error looking up ExecuteQueueRuntimeMBean!!!!!!");
         }
      }

      if (this.hasRemoteClientView()) {
         this.remoteHome.activate();
      }

      this.getJndiBinder().bindToJNDI();
   }

   public void bindEJBRefs(Context var1) throws NamingException {
      if (this.hasDeclaredLocalHome()) {
         var1.bind(this.getLocalHomeInterfaceName(), this.getLocalHome());
         var1.bind("local-home", this.getLocalHome());
      }

      if (this.hasDeclaredRemoteHome()) {
         Object var2 = this.getRemoteHome().getReferenceToBind();
         var1.bind(this.getHomeInterfaceName(), var2);
         var1.bind("home", var2);
      }

   }

   public void unbindEJBRefs(Context var1) throws NamingException {
      if (this.hasDeclaredLocalHome()) {
         var1.unbind(this.getLocalHomeInterfaceName());
         var1.unbind("local-home");
      }

      if (this.hasDeclaredRemoteHome()) {
         var1.unbind(this.getHomeInterfaceName());
         var1.unbind("home");
      }

   }

   public void onUndeploy() {
      this.getJndiBinder().unbindFromJNDI();
      super.onUndeploy();
   }

   protected abstract EJBCache getCache(Map var1) throws WLDeploymentException;

   protected QueryCache getQueryCache(Map var1) throws WLDeploymentException {
      throw new AssertionError("Only EntityBeanInfoImpl implements this method");
   }

   public void prepareLockTimeout(int var1) throws BeanUpdateRejectedException {
      if (var1 < -1) {
         throw new BeanUpdateRejectedException("Value for newLockTimout is < -1, it should be >= -1.");
      }
   }

   public void prepareDataCacheTimeout(int var1) throws BeanUpdateRejectedException {
      if (var1 < -1) {
         throw new BeanUpdateRejectedException("Value for dataCacheTimout is < -1, it should be >= -1.");
      }
   }

   public void prepareFetchBatchSize(int var1) throws BeanUpdateRejectedException {
      if (var1 < -1) {
         throw new BeanUpdateRejectedException("Value for fetchBatchSize is < -1, it should be >= -1.");
      }
   }

   public void updateImplClassLoader() throws WLDeploymentException {
      super.updateImplClassLoader();
   }

   public void updateTransactionTimeoutSeconds(int var1) {
      super.updateTransactionTimeoutSeconds(var1);
      int var2 = var1 * 1000;
      Iterator var3 = this.getMethodDescriptors().iterator();

      while(var3.hasNext()) {
         MethodDescriptor var4 = (MethodDescriptor)var3.next();
         var4.updateTxTimeoutMS(var2);
      }

   }

   public void updateMaxBeansInCache(int var1) {
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof CachingManager) {
         ((CachingManager)var2).updateMaxBeansInCache(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated MaxBeansInCache to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void updateLockTimeout(int var1, String var2) {
      PersistenceUnitInfoImpl var3 = this.persistenceUnitRegistry.getPersistenceUnit(var2);
      EntityManagerFactory var4 = var3.getEntityManagerFactory();
      OpenJPAConfigurationImpl var5 = this.getConfiguration(var4);
      var5.lockTimeout.set(var1);
   }

   private OpenJPAConfigurationImpl getConfiguration(EntityManagerFactory var1) {
      return (OpenJPAConfigurationImpl)((OpenJPAEntityManagerFactorySPI)OpenJPAPersistence.cast(var1)).getConfiguration();
   }

   public void updateDataCacheTimeout(int var1, String var2) {
      PersistenceUnitInfoImpl var3 = this.persistenceUnitRegistry.getPersistenceUnit(var2);
      EntityManagerFactory var4 = var3.getEntityManagerFactory();
      OpenJPAConfigurationImpl var5 = this.getConfiguration(var4);
      int var6 = var5.dataCacheTimeout.get();
      var5.dataCacheTimeout.set(var1);
      MetaDataRepository var7 = var5.getMetaDataRepositoryInstance();
      if (var7 != null) {
         ClassMetaData[] var8 = var7.getMetaDatas();
         if (var8 != null) {
            ArrayList var9 = new ArrayList();
            ClassMetaData[] var10 = var8;
            int var11 = var8.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               ClassMetaData var13 = var10[var12];
               if (var13.getDataCacheTimeout() == var6) {
                  var9.add(var13);
               }
            }

            Iterator var14 = var9.iterator();

            while(var14.hasNext()) {
               ClassMetaData var15 = (ClassMetaData)var14.next();
               var15.setDataCacheTimeout(var1);
            }

         }
      }
   }

   public void updateFetchBatchSize(int var1, String var2) {
      PersistenceUnitInfoImpl var3 = this.persistenceUnitRegistry.getPersistenceUnit(var2);
      EntityManagerFactory var4 = var3.getEntityManagerFactory();
      this.getConfiguration(var4).fetchBatchSize.set(var1);
   }

   public void assignDefaultTXAttributesIfNecessary() {
      StringBuffer var1 = new StringBuffer();
      short var2 = DDDefaults.getTransactionAttribute(this);
      StringBuffer var3 = new StringBuffer();
      short var4 = DDDefaults.getBeanMethodTransactionAttribute(this);
      if (this.hasDeclaredRemoteHome()) {
         var1.append(this.assignDefaultTXAttributesIfNecessary("home", this.getAllHomeMethodInfos(), var2));
      }

      var1.append(this.assignDefaultTXAttributesIfNecessary("remote", this.getAllRemoteMethodInfos(), var2));
      if (this.hasDeclaredLocalHome()) {
         var1.append(this.assignDefaultTXAttributesIfNecessary("local-home", this.getAllLocalHomeMethodInfos(), var2));
      }

      var1.append(this.assignDefaultTXAttributesIfNecessary("local", this.getAllLocalMethodInfos(), var2));
      var1.append(this.assignDefaultTXAttributesIfNecessary("webservice", this.getAllWebserviceMethodInfos(), var2));
      var3.append(this.assignDefaultTXAttributesIfNecessary("beanClass", this.getAllBeanMethodInfos(), var4));
      if (var1.length() > 0 && !this.isEJB30()) {
         EJBLogger.logEJBUsesDefaultTXAttribute(this.getDisplayName(), DDConstants.TX_ATTRIBUTE_STRINGS[var2], var1.toString());
      }

      if (var3.length() > 0 && !this.isEJB30()) {
         EJBLogger.logEJBUsesDefaultTXAttribute(this.getDisplayName(), DDConstants.TX_ATTRIBUTE_STRINGS[var4], var3.toString());
      }

   }

   private void registerRoleRefs() throws WLDeploymentException {
      this.runtimeHelper.registerRoleRefs(this.ejbName, this.secRoleRefs);
   }

   private void checkClientViews() throws WLDeploymentException {
      EJBComplianceTextFormatter var1;
      if ((this.homeInterfaceName == null || this.remoteInterfaceName != null) && (this.homeInterfaceName != null || this.remoteInterfaceName == null)) {
         if (this.localHomeInterfaceName != null && this.localInterfaceName == null || this.localHomeInterfaceName == null && this.localInterfaceName != null) {
            var1 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var1.INCONSISTENT_LOCAL_VIEW(this.getEJBName()));
         }
      } else {
         var1 = new EJBComplianceTextFormatter();
         throw new WLDeploymentException(var1.INCONSISTENT_REMOTE_VIEW(this.getEJBName()));
      }
   }

   private void checkSecurityRoleRefs() throws WLDeploymentException {
      Set var1 = this.secRoleRefs.keySet();
      Iterator var2 = var1.iterator();

      String var3;
      SecurityRoleReference var4;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (String)var2.next();
         var4 = (SecurityRoleReference)this.secRoleRefs.get(var3);
      } while(var4.getReferencedRole() != null);

      EJBComplianceTextFormatter var5 = new EJBComplianceTextFormatter();
      throw new WLDeploymentException(var5.NULL_SECURITY_ROLE_REF_LINK(this.getEJBName(), var3));
   }

   protected boolean checkIfItsSafeToUseCallByReference() {
      Debug.assertion(!this.callByReference);
      if (!this.hasRemoteClientView()) {
         return false;
      } else {
         if (this.hasDeclaredRemoteHome()) {
            Method[] var1 = this.remoteInterfaceClass.getMethods();

            int var2;
            for(var2 = 0; var2 < var1.length; ++var2) {
               if (this.businessMethod(var1[var2]) && !this.checkIfMethodCanUseCallByReference(var1[var2])) {
                  return false;
               }
            }

            var1 = this.homeInterfaceClass.getMethods();

            for(var2 = 0; var2 < var1.length; ++var2) {
               if (this.homeMethod(var1[var2]) && !this.checkIfMethodCanUseCallByReference(var1[var2])) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean businessMethod(Method var1) {
      try {
         EJBObject.class.getMethod(var1.getName(), (Class[])var1.getParameterTypes());
         return false;
      } catch (NoSuchMethodException var3) {
         return true;
      }
   }

   private boolean homeMethod(Method var1) {
      try {
         EJBHome.class.getMethod(var1.getName(), (Class[])var1.getParameterTypes());
         return false;
      } catch (NoSuchMethodException var4) {
         String var3 = var1.getName();
         return !var3.startsWith("find") && !var3.startsWith("create") && !var3.startsWith("remove");
      }
   }

   private boolean isMutable(Class var1) {
      if (var1.isPrimitive()) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.immutableClasses.length; ++var2) {
            if (var1.equals(this.immutableClasses[var2])) {
               return false;
            }
         }

         return true;
      }
   }

   protected boolean checkIfMethodCanUseCallByReference(Method var1) {
      if (this.isMutable(var1.getReturnType())) {
         return false;
      } else {
         Class[] var2 = var1.getParameterTypes();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (this.isMutable(var2[var3])) {
               return false;
            }
         }

         return true;
      }
   }

   protected void warnIfParameterNotSerializable() {
      if (this.hasRemoteClientView()) {
         if (this.remoteInterfaceClass != null) {
            Method[] var1 = this.remoteInterfaceClass.getMethods();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (this.businessMethod(var1[var2])) {
                  this.warnIfParamNotSerializableForMethod(var1[var2]);
               }
            }

         }
      }
   }

   protected void warnIfParamNotSerializableForMethod(Method var1) {
      Type[] var2 = var1.getGenericParameterTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] instanceof Class) {
            Class var4 = (Class)var2[var3];
            if (!var4.isPrimitive() && !Serializable.class.isAssignableFrom(var4) && !this.getDeploymentInfo().isWarningDisabled("BEA-012034")) {
               EJBLogger.logWarningParameterIsNotSerializable(var1.toString(), this.m_desc.getEJBName(), var4.getName());
            }
         }
      }

   }

   public boolean useCallByReference() {
      return this.callByReference;
   }

   private static void debug(String var0) {
      debugLogger.debug("[ClientDrivenBeanInfoImpl] " + var0);
   }

   public boolean isClientDriven() {
      return true;
   }

   public void updateCacheIdleTimeoutSeconds(int var1) {
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof CachingManager) {
         CachingManager var3 = (CachingManager)var2;
         var3.updateIdleTimeoutSecondsCache(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated Cache IdleTimeoutSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void unprepare() {
      BaseEJBRemoteHomeIntf var1 = this.getRemoteHome();
      if (var1 != null) {
         var1.unprepare();
      }

   }

   public PersistenceUnitRegistry getPersistenceUnitRegistry() {
      return this.persistenceUnitRegistry;
   }

   public void setPersistenceUnitRegistry(PersistenceUnitRegistry var1) {
      this.persistenceUnitRegistry = var1;
   }
}
