package weblogic.ejb.container.deployer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.security.jacc.PolicyConfiguration;
import weblogic.application.ApplicationContextInternal;
import weblogic.deployment.BaseEnvironmentBuilder;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.ComplianceException;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.compliance.TimeoutCheckHelper;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.deployer.mbimpl.MethodInfoImpl;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingDescriptor;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EjbComponentCreator;
import weblogic.ejb.container.interfaces.IIOPSecurityDescriptor;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.container.internal.SecurityHelper;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.NamedMethodBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStoreManager;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;

abstract class BeanInfoImpl implements BeanInfo {
   protected static final DebugLogger debugLogger;
   private static final AuthenticatedSubject kernelId;
   private final CachingDescriptor cachingDescriptor;
   private final IIOPSecurityDescriptor iiopSecurityDescriptor;
   protected final DeploymentInfo deploymentInfo;
   private int txTimeoutMS = 0;
   protected GenericClassLoader cl;
   protected GenericClassLoader moduleCL;
   protected final Name jndiName;
   protected final String jndiNameAsString;
   protected final String ejbName;
   protected final String dispatchPolicy;
   protected final boolean stickToFirstServer;
   protected final int remoteClientTimeout;
   protected final String displayName;
   protected final Collection envEntries;
   protected final Collection ejbRefs;
   protected final Collection ejbLocalRefs;
   protected final Collection resRefs;
   protected final Collection resEnvRefs;
   protected final Collection wlResRefs;
   protected final Collection wlResEnvRefs;
   protected final Collection messageDestRefs;
   protected final Map secRoleRefs;
   protected final String beanClassName;
   protected final String isIdenticalKey;
   protected final boolean isResourceRef;
   protected final boolean isEJB30;
   protected EjbComponentCreator ejbComponentCreator;
   private final Map beanMethodInfos = new HashMap();
   protected CompositeMBeanDescriptor m_desc = null;
   protected Map ejbRefJNDINames;
   protected Map ejbLocalRefJNDINames;
   protected Class beanClass;
   protected String jaccPolicyContextId = null;
   protected PolicyConfiguration jaccPolicyConfig = null;
   protected String jaccCodeSource = null;
   protected RoleMapper jaccRoleMapper = null;
   private boolean runAsPrincipalCalculated = false;
   private String runAsPrincipalName;
   private String createAsPrincipalName;
   private String removeAsPrincipalName;
   private String passivateAsPrincipalName;
   private List methodDescriptors = new ArrayList();
   private MethodDescriptor ejbTimeoutMethodDescriptor;
   private Method ejbTimeoutMethod;
   private boolean isClusteredTimers;
   protected RuntimeHelper runtimeHelper;
   private BeanManager beanManager;
   protected String componentURI;
   protected String componentName;

   BeanInfoImpl(DeploymentInfo var1, CompositeMBeanDescriptor var2, GenericClassLoader var3) throws ClassNotFoundException, WLDeploymentException {
      assert var2 != null;

      assert var3 != null;

      this.deploymentInfo = var1;
      this.m_desc = var2;
      this.componentURI = var1.getModuleURI();
      this.componentName = var1.getEJBComponentName();
      this.moduleCL = var3;
      this.cl = new GenericClassLoader(var3.getClassFinder(), var3);
      if (var3.getAnnotation() != null) {
         this.cl.setAnnotation(new Annotation(var3.getAnnotation().getApplicationName(), var3.getAnnotation().getModuleName()));
      }

      this.beanClassName = this.m_desc.getEJBClassName();
      this.dispatchPolicy = this.m_desc.getDispatchPolicy();
      this.stickToFirstServer = this.m_desc.getStickToFirstServer();
      this.remoteClientTimeout = this.m_desc.getRemoteClientTimeout();
      this.ejbName = this.m_desc.getEJBName();

      assert this.ejbName != null;

      if (isServer()) {
         this.txTimeoutMS = this.m_desc.getTransactionTimeoutSeconds() * 1000;
         this.displayName = this.ejbName + "(Application: " + var1.getApplicationName() + ", EJBComponent: " + var1.getEJBComponentName() + ")";
      } else {
         this.displayName = this.ejbName + "(Jar: " + var1.getJarFileName() + ")";
      }

      this.jaccPolicyContextId = var1.getJACCPolicyContextId();
      this.jaccPolicyConfig = var1.getJACCPolicyConfig();
      this.jaccCodeSource = var1.getJACCCodeSource();
      this.jaccRoleMapper = var1.getJACCRoleMapper();
      this.envEntries = this.m_desc.getAllEnvironmentEntries();
      this.ejbRefs = this.m_desc.getAllEJBReferences();
      this.ejbRefJNDINames = this.m_desc.getAllEJBReferenceJNDINames();
      this.ejbLocalRefs = this.m_desc.getAllEJBLocalReferences();
      this.ejbLocalRefJNDINames = this.m_desc.getAllEJBLocalReferenceJNDINames();
      this.resRefs = this.m_desc.getAllResourceReferences();
      this.isResourceRef = !this.resRefs.isEmpty();
      this.resEnvRefs = this.m_desc.getAllResourceEnvReferences();
      this.wlResRefs = this.m_desc.getAllWlResourceReferences();
      this.wlResEnvRefs = this.m_desc.getAllWlResourceEnvReferences();
      this.messageDestRefs = this.m_desc.getAllMessageDestinationReferences();
      this.secRoleRefs = new HashMap(this.m_desc.getSecurityRoleReferencesMap());
      this.cachingDescriptor = this.m_desc.getCachingDescriptor();
      this.iiopSecurityDescriptor = this.m_desc.getIIOPSecurityDescriptor();
      this.beanClass = this.loadClass(this.beanClassName);
      this.checkClassLoaders(this.m_desc, this.beanClass);
      this.isIdenticalKey = var1.getApplicationName() + var1.getModuleURI() + this.ejbName;
      this.createAsPrincipalName = this.m_desc.getCreateAsPrincipalName();
      this.removeAsPrincipalName = this.m_desc.getRemoveAsPrincipalName();
      this.passivateAsPrincipalName = this.m_desc.getPassivateAsPrincipalName();
      this.isClusteredTimers = this.m_desc.isClusteredTimers();
      this.initializeTimeoutMethod();
      this.initializeMethodInfos();
      this.isEJB30 = this.m_desc.isEJB30();
      String var4 = this.m_desc.getJNDIName();
      if (var2.getBean() instanceof MessageDrivenBeanBean) {
         if (null == var4) {
            var4 = this.m_desc.getEJBName();
         }
      } else if (this.isEJB30 && null == var4 && var2.getBean() instanceof SessionBeanBean) {
         String[] var5 = var2.getBusinessRemotes();
         if (var5 != null && var5.length > 0) {
            var4 = this.isIdenticalKey.replace('.', '_') + "_Home";
         }
      }

      this.jndiNameAsString = BaseEnvironmentBuilder.transformJNDIName(var4, this.deploymentInfo.getApplicationName());
      if (this.jndiNameAsString != null) {
         this.jndiName = this.getName(this.jndiNameAsString);
      } else {
         this.jndiName = null;
      }

   }

   public String getRunAsPrincipalName() {
      if (!this.runAsPrincipalCalculated) {
         throw new AssertionError("unexpected codepath");
      } else {
         return this.runAsPrincipalName;
      }
   }

   public AuthenticatedSubject getRunAsSubject() throws Exception {
      String var1 = this.getRunAsPrincipalName();
      AuthenticatedSubject var2 = null;
      if (var1 == null) {
         var1 = "guest";
         var2 = SecurityHelper.getAnonymousUser();
         var2.setQOS((byte)101);
      } else {
         try {
            SecurityHelper var3 = new SecurityHelper(this.getDeploymentInfo().getSecurityRealmName(), this.getJACCPolicyConfig(), this.getJACCPolicyContextId(), this.getJACCCodeSource(), this.getJACCRoleMapper());
            var2 = var3.getSubjectForPrincipal(var1);
            if (debugLogger.isDebugEnabled()) {
               debug("runAsPrincipalName is: '" + var1 + "', runAsSubject from SecurityHelper is: '" + var2 + "'");
            }
         } catch (Exception var4) {
            throw var4;
         }
      }

      return var2;
   }

   public String getCreateAsPrincipalName() {
      return this.createAsPrincipalName;
   }

   public String getRemoveAsPrincipalName() {
      return this.removeAsPrincipalName;
   }

   public String getPassivateAsPrincipalName() {
      return this.passivateAsPrincipalName;
   }

   public String getJACCPolicyContextId() {
      return this.jaccPolicyContextId;
   }

   public PolicyConfiguration getJACCPolicyConfig() {
      return this.jaccPolicyConfig;
   }

   public String getJACCCodeSource() {
      return this.jaccCodeSource;
   }

   public RoleMapper getJACCRoleMapper() {
      return this.jaccRoleMapper;
   }

   public DeploymentInfo getDeploymentInfo() {
      return this.deploymentInfo;
   }

   public boolean getClientsOnSameServer() {
      return this.m_desc.getClientsOnSameServer();
   }

   public Map getAllResourceReferenceJNDINames(String var1) {
      return this.m_desc.getAllResourceReferenceJNDINames(var1);
   }

   public Map getAllResourceEnvReferenceJNDINames(String var1) {
      return this.m_desc.getAllResourceEnvReferenceJNDINames(var1);
   }

   public Map getAllEJBReferenceJNDINames() {
      return this.ejbRefJNDINames;
   }

   public Map getAllEJBLocalReferenceJNDINames() {
      return this.ejbLocalRefJNDINames;
   }

   protected static boolean isServer() {
      return Kernel.isServer();
   }

   protected void checkClassLoaders(CompositeMBeanDescriptor var1, Class var2) {
      ClassLoader var3 = this.getClass().getClassLoader();
      if (var2.getClassLoader() == var3) {
         if (isServer()) {
            if (!this.isWarningDisabled("BEA-010001") && !var2.getName().startsWith("weblogic")) {
               EJBLogger.logRedeployClasspathFailure(var1.getEJBName(), var2.getName());
            }
         } else if (!this.isWarningDisabled("BEA-010054")) {
            EJBLogger.logEJBClassFoundInClasspath(var1.getEJBName(), var2.getName());
         }
      }

   }

   public int getTransactionTimeoutMS() {
      return this.txTimeoutMS;
   }

   public Class getBeanClass() {
      return this.beanClass;
   }

   public Name getJNDIName() {
      return this.jndiName;
   }

   public String getJNDINameAsString() {
      return this.jndiNameAsString;
   }

   public String getIsIdenticalKey() {
      return this.isIdenticalKey;
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public String getComponentURI() {
      return this.componentURI;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getBeanClassName() {
      return this.beanClassName;
   }

   public Collection getAllEnvironmentEntries() {
      return this.envEntries;
   }

   public Collection getAllEJBReferences() {
      return this.ejbRefs;
   }

   public Collection getAllEJBLocalReferences() {
      return this.ejbLocalRefs;
   }

   public Collection getAllServiceReferences() {
      return this.m_desc.getServiceReferences();
   }

   public Collection getAllServiceReferenceDescriptions() {
      return this.m_desc.getServiceReferenceDescriptions();
   }

   public Collection getAllResourceReferences() {
      return this.resRefs;
   }

   public boolean getIsResourceRef() {
      return this.isResourceRef;
   }

   public Collection getAllResourceEnvReferences() {
      return this.resEnvRefs;
   }

   public Collection getAllWlResourceReferences() {
      return this.wlResRefs;
   }

   public Collection getAllWlResourceEnvReferences() {
      return this.wlResEnvRefs;
   }

   public Collection getAllMessageDestinationReferences() {
      return this.messageDestRefs;
   }

   public Collection getAllSecurityRoleReferences() {
      return this.secRoleRefs.values();
   }

   public SecurityRoleReference getSecurityRoleReference(String var1) {
      return (SecurityRoleReference)this.secRoleRefs.get(var1);
   }

   public PersistenceContextRefBean[] getPersistenceContextRefs() {
      return this.m_desc.getPersistenceContextRefs();
   }

   public PersistenceUnitRefBean[] getPersistenceUnitRefs() {
      return this.m_desc.getPersistenceUnitRefs();
   }

   public ClassLoader getClassLoader() {
      return this.cl;
   }

   public ClassLoader getModuleClassLoader() {
      return this.moduleCL;
   }

   protected Name getName(String var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            return new CompositeName(var1);
         } catch (InvalidNameException var3) {
            throw new AssertionError("Deployment Error: " + var3);
         }
      }
   }

   protected Class loadClass(String var1) throws ClassNotFoundException {
      assert var1 != null;

      return this.cl.loadClass(var1);
   }

   public abstract Iterator getAllMethodInfosIterator();

   public Collection getAllBeanMethodInfos() {
      return this.beanMethodInfos.values();
   }

   public MethodInfo getBeanMethodInfo(String var1) {
      return (MethodInfo)this.beanMethodInfos.get(var1);
   }

   public boolean isEJB30() {
      return this.isEJB30;
   }

   public void setEjbComponentCreator(EjbComponentCreator var1) {
      this.ejbComponentCreator = var1;
   }

   public EjbComponentCreator getEjbComponentCreator() {
      return this.ejbComponentCreator;
   }

   public boolean isTimerDriven() {
      return this.ejbTimeoutMethod != null;
   }

   public boolean isClusteredTimers() {
      return this.isClusteredTimers;
   }

   public String getTimerStoreName() {
      return this.m_desc.getTimerStoreName();
   }

   private void prepareTimerService() throws WLDeploymentException {
      if (this.isTimerDriven()) {
         if (this.isClusteredTimers()) {
            this.validateClusteredTimerConfig();
         } else {
            this.validateLocalTimerConfig();
         }

      }
   }

   private void validateClusteredTimerConfig() throws WLDeploymentException {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      ClusterMBean var2 = var1.getCluster();
      Loggable var3;
      if (var2 == null) {
         var3 = EJBLogger.logClusteredTimersRequireClusterLoggable(this.getDisplayName(), var1.getName());
         throw new WLDeploymentException(var3.getMessage());
      } else if (var2.getDataSourceForJobScheduler() == null) {
         var3 = EJBLogger.logJobSchedulerNotConfiguredForClusteredTimersLoggable(this.getDisplayName());
         throw new WLDeploymentException(var3.getMessage());
      }
   }

   private void validateLocalTimerConfig() throws WLDeploymentException {
      String var1 = this.getTimerStoreName();
      if (var1 != null) {
         PersistentStoreManager var2 = PersistentStoreManager.getManager();
         if (var2.getStoreByLogicalName(var1) == null) {
            ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer();
            String var4 = var3.getName();
            Loggable var5 = EJBLogger.logUnableToFindPersistentStoreLoggable(this.getDisplayName(), var1, var4);
            throw new WLDeploymentException(var5.getMessage());
         }
      }

   }

   private void initializeTimeoutMethod() throws WLDeploymentException {
      String var1 = null;
      NamedMethodBean var2 = this.m_desc.getTimeoutMethod();
      if (var2 != null) {
         var1 = var2.getMethodName();
      }

      if (var1 != null) {
         Class var3 = this.beanClass;

         while(var3 != Object.class) {
            try {
               this.ejbTimeoutMethod = var3.getDeclaredMethod(var1, Timer.class);
               this.ejbTimeoutMethod.setAccessible(true);
               break;
            } catch (NoSuchMethodException var7) {
               var3 = var3.getSuperclass();
            }
         }

         if (this.ejbTimeoutMethod == null) {
            EJBComplianceTextFormatter var4 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var4.EJB_TIMEOUT_METHOD_NOT_FOUND(this.getDisplayName(), getMethodSignature(var1, new String[]{"javax.ejb.Timer"})));
         }
      }

      try {
         TimeoutCheckHelper.validateTimeoutMethodIsejbTimeout(this.beanClass, this.ejbTimeoutMethod);
      } catch (ComplianceException var6) {
         throw new WLDeploymentException(var6.getMessage(), var6);
      }

      if (TimedObject.class.isAssignableFrom(this.beanClass)) {
         try {
            this.ejbTimeoutMethod = this.beanClass.getMethod("ejbTimeout", Timer.class);
            this.ejbTimeoutMethod.setAccessible(true);
         } catch (NoSuchMethodException var5) {
         }
      }

   }

   public Method getTimeoutMethod() {
      return this.ejbTimeoutMethod;
   }

   private void initializeMethodInfos() {
      if (this.isTimerDriven()) {
         MethodInfoImpl var1 = new MethodInfoImpl(this.ejbTimeoutMethod, (String)null, this.jaccPolicyContextId);
         this.beanMethodInfos.put(var1.getSignature(), var1);
      }

   }

   public CachingDescriptor getCachingDescriptor() {
      return this.cachingDescriptor;
   }

   public IIOPSecurityDescriptor getIIOPSecurityDescriptor() {
      return this.iiopSecurityDescriptor;
   }

   public boolean isWarningDisabled(String var1) {
      return this.deploymentInfo.isWarningDisabled(var1);
   }

   static BeanInfoImpl createBeanInfoImpl(DeploymentInfo var0, CompositeMBeanDescriptor var1, GenericClassLoader var2) throws ClassNotFoundException, WLDeploymentException {
      Debug.assertion(var2 != null);
      EnterpriseBeanBean var3 = var1.getBean();
      Object var4 = null;
      if (var3 instanceof SessionBeanBean) {
         if (var1.isEJB30()) {
            var4 = new Ejb3SessionBeanInfoImpl(var0, var1, var2);
         } else {
            var4 = new SessionBeanInfoImpl(var0, var1, var2);
         }
      } else if (var3 instanceof MessageDrivenBeanBean) {
         var4 = new MessageDrivenBeanInfoImpl(var0, var1, var2);
      } else {
         if (!(var3 instanceof EntityBeanBean)) {
            throw new AssertionError("Uknown type of bean:" + var3);
         }

         var4 = new EntityBeanInfoImpl(var0, var1, var2);
      }

      ((BeanInfoImpl)var4).init();
      return (BeanInfoImpl)var4;
   }

   public BeanManager getBeanManager() {
      return this.beanManager;
   }

   public abstract BeanManager getBeanManagerInstance(EJBComponentRuntimeMBeanImpl var1);

   public void setupBeanManager(EJBComponentRuntimeMBeanImpl var1) {
      this.beanManager = this.getBeanManagerInstance(var1);
   }

   public void onUndeploy() {
   }

   public String getDispatchPolicy() {
      return this.dispatchPolicy;
   }

   public boolean getStickToFirstServer() {
      return this.stickToFirstServer;
   }

   public int getRemoteClientTimeout() {
      return this.remoteClientTimeout;
   }

   public void updateImplClassLoader() throws WLDeploymentException {
      this.cl = new GenericClassLoader(this.cl.getClassFinder(), this.cl.getParent());

      try {
         this.beanClass = this.loadClass(this.beanClassName);
      } catch (ClassNotFoundException var2) {
         throw new WLDeploymentException("Couldn't load updated impl class: " + var2);
      }
   }

   public void updateTransactionTimeoutSeconds(int var1) {
      this.txTimeoutMS = var1 * 1000;
      if (debugLogger.isDebugEnabled()) {
         debug("updated TransactionTimeoutSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public abstract void updateMaxBeansInFreePool(int var1);

   protected StringBuffer assignDefaultTXAttributesIfNecessary(String var1, Collection var2, short var3) {
      StringBuffer var4 = new StringBuffer();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         MethodInfo var6 = (MethodInfo)var5.next();
         if (var6.getTransactionAttribute() == -1) {
            if (var4.length() == 0) {
               var4.append(var1);
               var4.append('[');
            } else {
               var4.append(", ");
            }

            var4.append(var6.getSignature());
            var6.setTransactionAttribute(var3);
         }
      }

      if (var4.length() > 0) {
         var4.append("]  ");
      }

      return var4;
   }

   protected abstract short getTxAttribute(MethodInfo var1, Class var2);

   protected MethodDescriptor setMethodDescriptor(BaseEJBHomeIntf var1, Method var2, Class var3, MethodInfo var4, boolean var5, String var6, String var7) throws WLDeploymentException {
      MethodDescriptor var8 = this.createMethodDescriptor(var2, var3, var4, var5);
      this.setMDField(var1, var3, var8, var6, var7);
      return var8;
   }

   public MethodDescriptor createMethodDescriptor(Method var1, Class var2, MethodInfo var3, boolean var4) throws WLDeploymentException {
      short var5 = this.getTxAttribute(var3, var2);

      try {
         MethodDescriptor var6 = new MethodDescriptor(this, this.deploymentInfo.getApplicationName(), this.deploymentInfo.getEJBComponentName(), this.ejbName, var1, var3, var5, this.getTransactionTimeoutMS(), this.m_desc.getEntityAlwaysUsesTransaction(), var4);
         this.methodDescriptors.add(var6);
         if (!isServer()) {
            throw new AssertionError("unexpected codepath");
         } else {
            var3.setRuntimeHelper(this.runtimeHelper);
            this.runtimeHelper.setRunAsSubject(var6, this.getRunAsPrincipalName());
            if (var6 != null && this.runtimeHelper.processUncheckedExcludedMethod(var6) && debugLogger.isDebugEnabled()) {
               debug("method: '" + var3.getMethodName() + "' is unchecked or excluded " + "runtime unchecked/excluded list has been updated.");
            }

            return var6;
         }
      } catch (PrincipalNotFoundException var8) {
         throw new WLDeploymentException(var8.toString());
      }
   }

   private void setMDField(BaseEJBHomeIntf var1, Class var2, MethodDescriptor var3, String var4, String var5) throws WLDeploymentException {
      Loggable var7;
      WLDeploymentException var8;
      try {
         if (!var5.equals("Home") && !var5.equals("LocalHome")) {
            if (!var5.equals("Remote") && !var5.equals("Local") && !var5.equals("ServiceEndpoint") && !var5.equals("MessagingType")) {
               Debug.assertion(false, "encountered unknown interface type: '" + var5 + "'");
            } else {
               this.setObjectMDField(var2, var3, var4);
            }
         } else {
            this.setHomeMDField(var1, var3, var4);
         }

      } catch (IllegalAccessException var9) {
         var7 = EJBLogger.logMismatchBetweenBeanAndGeneratedCodeLoggable(this.getDisplayName(), StackTraceUtils.throwable2StackTrace(var9));
         var8 = new WLDeploymentException(var7.getMessage(), var9);
         throw var8;
      } catch (NoSuchFieldException var10) {
         var7 = EJBLogger.logMismatchBetweenBeanAndGeneratedCodeLoggable(this.getDisplayName(), StackTraceUtils.throwable2StackTrace(var10));
         var8 = new WLDeploymentException(var7.getMessage(), var10);
         throw var8;
      }
   }

   private void setHomeMDField(BaseEJBHomeIntf var1, MethodDescriptor var2, String var3) throws IllegalAccessException, NoSuchFieldException {
      Field var4 = var1.getClass().getField("md_" + var3);
      var4.set(var1, var2);
   }

   private void setObjectMDField(Class var1, MethodDescriptor var2, String var3) throws IllegalAccessException, NoSuchFieldException {
      Field var4 = var1.getField("md_" + var3);
      var4.set(var1, var2);
   }

   public void setRuntimeHelper(RuntimeHelper var1) {
      this.runtimeHelper = var1;
   }

   protected List getMethodDescriptors() {
      return this.methodDescriptors;
   }

   protected static String getMethodSignature(Method var0) {
      return DDUtils.getMethodSignature(var0);
   }

   protected static String getMethodSignature(String var0, String[] var1) {
      return DDUtils.getMethodSignature(var0, var1);
   }

   protected void dumpMethodDescriptorFields(Field[] var1, Object var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         Field var4 = var1[var3];
         if (var4.getName().startsWith("md")) {
            try {
               MethodDescriptor var5 = (MethodDescriptor)var4.get(var2);
               debug("MethodDescriptor: " + var5);
            } catch (IllegalAccessException var6) {
               debug("", var6);
            } catch (ClassCastException var7) {
               debug("", var7);
            }
         }
      }

   }

   public String getNetworkAccessPoint() {
      return this.m_desc.getNetworkAccessPoint();
   }

   public MethodDescriptor getEjbTimeoutMethodDescriptor() {
      return this.ejbTimeoutMethodDescriptor;
   }

   protected void prepare(ApplicationContextInternal var1) throws WLDeploymentException {
      this.prepareTimerService();
      this.calculateRunAsPrincipal();
      this.runtimeHelper.checkRunAsPrivileges(this);
      if (this.isTimerDriven()) {
         MethodInfo var2 = this.getBeanMethodInfo(DDUtils.getMethodSignature(this.ejbTimeoutMethod));
         var2.setUnchecked(true);
         this.ejbTimeoutMethodDescriptor = this.createMethodDescriptor(this.ejbTimeoutMethod, TimedObject.class, var2, true);
         this.methodDescriptors.add(this.ejbTimeoutMethodDescriptor);
      }

   }

   public void unprepare() {
   }

   private void calculateRunAsPrincipal() throws WLDeploymentException {
      String var1 = this.m_desc.getRunAsRoleName();
      if (var1 != null) {
         this.runAsPrincipalName = this.m_desc.getRunAsIdentityPrincipal();
         if (this.runAsPrincipalName == null) {
            this.runAsPrincipalName = this.deploymentInfo.getRunAsRoleAssignment(var1);
         }

         if (this.runAsPrincipalName == null) {
            this.runAsPrincipalName = this.runtimeHelper.getRunAsPrincipalFromRoleMapping(this.ejbName, var1, this.deploymentInfo.getDeploymentRoles());
         }

         if (!this.runtimeHelper.isUserPrincipal(this.runAsPrincipalName)) {
            EJBComplianceTextFormatter var2 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var2.INVALID_RUN_AS_PRINCIPAL_FOR_EJB(this.getDisplayName(), this.runAsPrincipalName));
         }
      }

      this.runAsPrincipalCalculated = true;
   }

   private static void debug(String var0) {
      debugLogger.debug("[BeanInfoImpl] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[BeanInfoImpl] " + var0, var1);
   }

   public String getHomeInterfaceName() {
      return null;
   }

   public String getLocalHomeInterfaceName() {
      return null;
   }

   public String getLocalInterfaceName() {
      return null;
   }

   public String getRemoteInterfaceName() {
      return null;
   }

   public boolean hasLocalClientView() {
      return false;
   }

   public boolean hasRemoteClientView() {
      return false;
   }

   public boolean isClientDriven() {
      return false;
   }

   public boolean isEntityBean() {
      return false;
   }

   public boolean isSessionBean() {
      return false;
   }

   public Set getBusinessLocals() {
      return Collections.emptySet();
   }

   public Set getBusinessRemotes() {
      return Collections.emptySet();
   }

   public String getComponentName() {
      return this.componentName;
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
