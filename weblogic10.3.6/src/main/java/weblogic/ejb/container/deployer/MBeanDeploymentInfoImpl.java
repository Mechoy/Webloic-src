package weblogic.ejb.container.deployer;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.ApplicationException;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyConfigurationFactory;
import javax.security.jacc.PolicyContextException;
import weblogic.application.ApplicationContextInternal;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.deployer.mbimpl.ContainerTransactionImpl;
import weblogic.ejb.container.deployer.mbimpl.ExcludeListImpl;
import weblogic.ejb.container.deployer.mbimpl.IsolationLevelImpl;
import weblogic.ejb.container.deployer.mbimpl.MethodDescriptorImpl;
import weblogic.ejb.container.deployer.mbimpl.MethodPermissionImpl;
import weblogic.ejb.container.deployer.mbimpl.RelationshipsImpl;
import weblogic.ejb.container.deployer.mbimpl.RetryMethodsOnRollbackImpl;
import weblogic.ejb.container.deployer.mbimpl.RoleDescriptorImpl;
import weblogic.ejb.container.deployer.mbimpl.SecurityRoleMappingImpl;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.ContainerTransaction;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.IsolationLevel;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MethodDescriptor;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.MethodPermission;
import weblogic.ejb.container.interfaces.RetryMethodsOnRollback;
import weblogic.ejb.container.interfaces.RoleDescriptor;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.container.persistence.CMPBeanDescriptorImpl;
import weblogic.ejb.container.persistence.InstalledPersistence;
import weblogic.ejb.container.persistence.PersistenceException;
import weblogic.ejb.container.persistence.PersistenceType;
import weblogic.ejb.container.persistence.spi.Dependents;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.descriptor.ApplicationExceptionBean;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.ExcludeListBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorBindingBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.MessageDestinationBean;
import weblogic.j2ee.descriptor.MethodPermissionBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.wl.IdempotentMethodsBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.descriptor.wl.MethodBean;
import weblogic.j2ee.descriptor.wl.RetryMethodsOnRollbackBean;
import weblogic.j2ee.descriptor.wl.RunAsRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.TransactionIsolationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.jacc.RoleMapperFactory;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.CombinedIterator;
import weblogic.utils.jars.VirtualJarFile;

public final class MBeanDeploymentInfoImpl implements DeploymentInfo {
   private static boolean debug = false;
   private static boolean verbose = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean SKIP_ENTITY_PKENHANCE = Boolean.getBoolean("weblogic.ejb.entity.IgnorePKClassOptimization");
   private GenericClassLoader m_classLoader;
   private HashMap m_beanInfos = new HashMap();
   private EjbDescriptorBean ejbDescriptor;
   private HashMap m_runAsRoleAssignmentsJarScoped = new HashMap();
   private Map messageDestinations = new HashMap();
   private SecurityRoleMapping m_deploymentRoles = new SecurityRoleMappingImpl();
   private Relationships m_relationships;
   private Dependents m_dependents;
   private String m_applicationName;
   private String m_applicationId;
   private String m_EJBComponentName;
   private String moduleURI;
   private String m_securityRealmName;
   private EJBComplianceTextFormatter fmt;
   private String jarFileName;
   private String clientJarFileName;
   private String jaccPolicyContextId = null;
   private PolicyConfiguration jaccPolicyConfig = null;
   private String jaccCodeSource;
   private RoleMapper jaccRoleMapper = null;
   private boolean enableDynamicQueries = false;
   private boolean enableBeanClassRedeploy = false;
   private PitchforkContext pitchforkContext = null;
   private final VirtualJarFile virtualJarFile;
   private final Set disabledWarnings;
   private HashMap applicationExceptions = new HashMap();
   private Set<Class<?>> uncheckedAppExceptions;
   private ConcurrentHashMap<Class<?>, Boolean> appExeptionsCache = new ConcurrentHashMap();
   private ConcurrentHashMap<Class<?>, Boolean> rollbackExceptionsCache = new ConcurrentHashMap();
   private HashMap ejbToInterceptor = new HashMap();
   private HashMap iceptorClassToIBean = new HashMap();

   public MBeanDeploymentInfoImpl(EjbDescriptorBean var1, GenericClassLoader var2, String var3, String var4, VirtualJarFile var5, ApplicationContextInternal var6) throws ErrorCollectionException, DeploymentDescriptorException, ClassNotFoundException, PersistenceException, WLDeploymentException {
      this.m_classLoader = var2;
      this.ejbDescriptor = var1;
      if (var6 != null) {
         this.m_applicationName = var6.getApplicationId();
         this.m_applicationId = var6.getApplicationId();
         this.m_securityRealmName = var6.getApplicationSecurityRealmName();
      } else {
         this.m_applicationName = "";
      }

      if (this.m_securityRealmName == null) {
         this.m_securityRealmName = RuntimeHelper.getDefaultRealmName();
      }

      this.m_EJBComponentName = var3;
      this.moduleURI = var4;
      this.virtualJarFile = var5;
      this.fmt = new EJBComplianceTextFormatter();
      this.jarFileName = var1.getJarFileName();
      this.clientJarFileName = var1.getEjbJarBean().getEjbClientJar();
      this.disabledWarnings = this.getDisabledWarnings(var1);
      this.enableBeanClassRedeploy = var1.getWeblogicEjbJarBean().isEnableBeanClassRedeploy();
      String var7 = var1.getWeblogicEjbJarBean().getComponentFactoryClassName();
      if (var7 == null && var6 != null && var6.getWLApplicationDD() != null) {
         var7 = var6.getWLApplicationDD().getComponentFactoryClassName();
      }

      this.pitchforkContext = new PitchforkContext(var7);
      if (isServer()) {
         this.initializeJAAC(var5, var6);
      }

      this.initializeRoles(var1);
      this.initializeJarScopedRunAsRoles(var1);
      this.initializeBeanInfos(var1, this.m_classLoader);
      this.initializeMethodPermissions(var1);
      this.initializeTransactionAttribute(var1);
      this.initializeTransactionLevels(var1);
      this.initializeIdempotentMethods(var1);
      this.initializeRetryMethodsOnRollback(var1);
      this.initializeExcludedMethods(var1);
      this.initializeMessageDestinations(var1);
      this.initializeApplicationExceptions(var1);
      this.setupRelationsForEJBAndInterceptors(var1);
   }

   private void initializeJAAC(VirtualJarFile var1, ApplicationContextInternal var2) throws WLDeploymentException {
      if (var2 != null && var2.useJACC()) {
         if (verbose) {
            Debug.say("\n+++++++++++  " + ManagementService.getRuntimeAccess(kernelId).getServer().getName() + "_" + this.m_applicationName + "_" + this.m_EJBComponentName + "  JACC is enabled\n");
         }

         this.jaccPolicyContextId = ManagementService.getRuntimeAccess(kernelId).getServer().getName() + "_" + this.m_applicationName + "_" + this.m_EJBComponentName;
         File var3 = var1.getDirectory();
         if (var3 != null) {
            this.jaccCodeSource = var3.getName();
         } else {
            this.jaccCodeSource = var1.getName();
            int var4 = this.jaccCodeSource.lastIndexOf(File.separatorChar);
            if (var4 != -1 && this.jaccCodeSource.length() > 1) {
               this.jaccCodeSource = this.jaccCodeSource.substring(0, var4);
            }
         }

         try {
            PolicyConfigurationFactory var8 = PolicyConfigurationFactory.getPolicyConfigurationFactory();
            this.jaccPolicyConfig = var8.getPolicyConfiguration(this.jaccPolicyContextId, true);
            RoleMapperFactory var5 = RoleMapperFactory.getRoleMapperFactory();
            this.jaccRoleMapper = var5.getRoleMapper(this.m_applicationId, this.jaccPolicyContextId, false);
         } catch (ClassNotFoundException var6) {
            throw new WLDeploymentException(var6.getMessage(), var6);
         } catch (PolicyContextException var7) {
            throw new WLDeploymentException(var7.getMessage(), var7);
         }
      } else {
         this.jaccPolicyContextId = null;
         this.jaccPolicyConfig = null;
         this.jaccCodeSource = null;
         this.jaccRoleMapper = null;
      }

   }

   public String getRunAsRoleAssignment(String var1) {
      return (String)this.m_runAsRoleAssignmentsJarScoped.get(var1);
   }

   public String getApplicationName() {
      return this.m_applicationName;
   }

   public String getEJBComponentName() {
      return this.m_EJBComponentName;
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

   public String getModuleURI() {
      return this.moduleURI;
   }

   public String getSecurityRealmName() {
      return this.m_securityRealmName;
   }

   public String getJarFileName() {
      return this.jarFileName;
   }

   public String getClientJarFileName() {
      return this.clientJarFileName;
   }

   public boolean isDynamicQueriesEnabled() {
      return this.enableDynamicQueries;
   }

   public EjbDescriptorBean getEjbDescriptorBean() {
      return this.ejbDescriptor;
   }

   public Map getApplicationExceptions() {
      return this.applicationExceptions;
   }

   public Set<Class<?>> getUncheckedAppExceptionClasses() {
      return this.uncheckedAppExceptions;
   }

   public Collection getBeanInfos() {
      return this.m_beanInfos.values();
   }

   public BeanInfo getBeanInfo(String var1) {
      return (BeanInfo)this.m_beanInfos.get(var1);
   }

   public ClassLoader getModuleClassLoader() {
      return this.m_classLoader;
   }

   public SecurityRoleMapping getDeploymentRoles() {
      return this.m_deploymentRoles;
   }

   public Relationships getRelationships() {
      return this.m_relationships;
   }

   public Dependents getDependents() {
      return this.m_dependents;
   }

   public VirtualJarFile getVirtualJarFile() {
      return this.virtualJarFile;
   }

   public boolean isWarningDisabled(String var1) {
      return this.disabledWarnings.contains(var1);
   }

   public boolean isEnableBeanClassRedeploy() {
      return this.enableBeanClassRedeploy;
   }

   public PitchforkContext getPitchforkContext() {
      return this.pitchforkContext;
   }

   public List getInterceptorBeans(String var1) {
      LinkedList var2 = new LinkedList();
      HashSet var3 = (HashSet)this.ejbToInterceptor.get(var1);
      HashSet var4 = (HashSet)this.ejbToInterceptor.get("*");
      if (var3 == null) {
         var3 = var4;
      } else if (var4 != null) {
         var3.addAll(var4);
      }

      if (var3 != null) {
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            InterceptorBean var7 = (InterceptorBean)this.iceptorClassToIBean.get(var6);
            var2.add(var7);
         }
      }

      return var2;
   }

   private void initializeRoles(EjbDescriptorBean var1) {
      Collection var2 = this.getAllRoleDescriptors(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         RoleDescriptor var4 = (RoleDescriptor)var3.next();
         this.m_deploymentRoles.addRoleToPrincipalsMapping(var4.getName(), var4.getAllSecurityPrincipals());
         if (var4.isExternallyDefined()) {
            this.m_deploymentRoles.addExternallyDefinedRole(var4.getName());
         }
      }

   }

   private Collection getAllRoleDescriptors(EjbDescriptorBean var1) {
      ArrayList var2 = new ArrayList();
      AssemblyDescriptorBean var3 = var1.getEjbJarBean().getAssemblyDescriptor();
      if (null != var3) {
         SecurityRoleBean[] var4 = var3.getSecurityRoles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var2.add(new RoleDescriptorImpl(var1, var4[var5]));
         }
      }

      return var2;
   }

   private void initializeJarScopedRunAsRoles(EjbDescriptorBean var1) {
      RunAsRoleAssignmentBean[] var2 = var1.getWeblogicEjbJarBean().getRunAsRoleAssignments();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.m_runAsRoleAssignmentsJarScoped.put(var2[var3].getRoleName(), var2[var3].getRunAsPrincipalName());
      }

   }

   private void initializeBeanInfos(EjbDescriptorBean var1, GenericClassLoader var2) throws DeploymentDescriptorException, PersistenceException, WLDeploymentException {
      CompositeMBeanDescriptor var3 = null;
      InstalledPersistence var4 = new InstalledPersistence();
      ArrayList var5 = new ArrayList();
      HashMap var6 = new HashMap();
      ArrayList var7 = new ArrayList();
      HashMap var8 = new HashMap();
      EnterpriseBeanBean[] var9 = CompositeMBeanDescriptor.getEnterpriseBeans(var1.getEjbJarBean());
      EnterpriseBeanBean[] var11;
      if (!SKIP_ENTITY_PKENHANCE) {
         ArrayList var10 = new ArrayList();
         var11 = var9;
         int var12 = var9.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            EnterpriseBeanBean var14 = var11[var13];
            if (var14 instanceof EntityBeanBean) {
               String var15 = ((EntityBeanBean)var14).getPrimKeyClass();
               var10.add(var15);
            }
         }

         PKClassPreProcessor var20 = new PKClassPreProcessor(var10);
         var2.addInstanceClassPreProcessor(var20);
         Iterator var22 = var10.iterator();

         while(var22.hasNext()) {
            String var26 = (String)var22.next();

            try {
               var2.loadClass(var26);
            } catch (ClassNotFoundException var17) {
               throw new WLDeploymentException(var26, var17);
            }
         }
      }

      for(int var18 = 0; var18 < var9.length; ++var18) {
         var3 = new CompositeMBeanDescriptor(var9[var18], var1);
         var11 = null;

         BeanInfoImpl var21;
         try {
            var21 = BeanInfoImpl.createBeanInfoImpl(this, var3, var2);
         } catch (ClassNotFoundException var16) {
            Loggable var27 = EJBLogger.logUnableLoadClassSpecifiedInDDLoggable(var16.getMessage());
            throw new DeploymentDescriptorException(var27.getMessage(), new DescriptorErrorInfo("<ejb-class>", var9[var18].getEjbName(), var9[var18].getEjbName()));
         }

         this.m_beanInfos.put(var9[var18].getEjbName(), var21);
         if (var21 instanceof EntityBeanInfoImpl) {
            EntityBeanInfoImpl var24 = (EntityBeanInfoImpl)var21;
            if (!var24.getIsBeanManagedPersistence()) {
               CMPInfoImpl var28 = (CMPInfoImpl)var24.getCMPInfo();
               CMPBeanDescriptorImpl var29 = new CMPBeanDescriptorImpl(var24, var1);
               if (var28.uses20CMP()) {
                  var6.put(var24.getEJBName(), var29);
                  var5.add(var24);
                  this.enableDynamicQueries |= var24.isDynamicQueriesEnabled();
               }

               var8.put(var24.getEJBName(), var29);
               var7.add(var24);
               var28.setPersistenceType(this.getPersistenceType(var24, var4));
            }
         }
      }

      this.m_relationships = new RelationshipsImpl(var1.getEjbJarBean().getRelationships(), getRelationshipJNDIMap(var1));
      Iterator var19 = var5.iterator();

      EntityBeanInfoImpl var23;
      CMPInfoImpl var25;
      while(var19.hasNext()) {
         var23 = (EntityBeanInfoImpl)var19.next();
         var25 = (CMPInfoImpl)var23.getCMPInfo();
         var25.setBeanMap(var6);
         var25.setRelationships(this.m_relationships);
      }

      var19 = var7.iterator();

      while(var19.hasNext()) {
         var23 = (EntityBeanInfoImpl)var19.next();
         var25 = (CMPInfoImpl)var23.getCMPInfo();
         var25.setAllBeanMap(var8);
      }

   }

   private static Map getRelationshipJNDIMap(EjbDescriptorBean var0) {
      HashMap var1 = new HashMap();
      return var1;
   }

   private PersistenceType getPersistenceType(EntityBeanInfo var1, InstalledPersistence var2) throws PersistenceException {
      CMPInfo var3 = var1.getCMPInfo();
      if (debug) {
         Debug.assertion(var3 != null);
      }

      String var4 = var3.getPersistenceUseIdentifier();
      if (var4 == null) {
         throw new PersistenceException(this.fmt.BEAN_MISSING_PERSISTENCE_USE(var1.getEJBName()));
      } else {
         String var5 = var3.getPersistenceUseVersion();
         if (debug) {
            Debug.assertion(var4 != null);
         }

         if (debug) {
            Debug.assertion(var5 != null);
         }

         PersistenceType var6 = var2.getInstalledType(var4, var5);
         if (var6 != null) {
            if (!var6.getCmpVersion().equalsIgnoreCase(var1.getCMPInfo().getCMPVersion())) {
               throw new PersistenceException(this.fmt.incompatibleCmpVersion(var1.getEJBName(), var1.getCMPInfo().getCMPVersion(), var6.getCmpVersion()));
            } else if (!var6.getWeblogicVersion().equalsIgnoreCase("7")) {
               throw new PersistenceException(this.fmt.incompatibleVendorPersistenceType(var1.getEJBName(), var6.getWeblogicVersion(), "7"));
            } else {
               return var6;
            }
         } else {
            StringBuffer var7 = new StringBuffer();
            var7.append(this.fmt.vendorPersistenceTypeNotInstalled(var4, var5, var1.getEJBName()));
            Iterator var8 = var2.getInstalledTypes().iterator();
            if (!var8.hasNext()) {
               var7.append("<No persistence types installed.>");
            } else {
               while(var8.hasNext()) {
                  PersistenceType var9 = (PersistenceType)var8.next();
                  var7.append("(" + var9.getIdentifier() + ", " + var9.getVersion() + ")");
                  if (var8.hasNext()) {
                     var7.append(", ");
                  }
               }

               var7.append(".");
            }

            throw new PersistenceException(var7.toString());
         }
      }
   }

   private Collection getAllMethodPermissions(EjbDescriptorBean var1) {
      ArrayList var2 = new ArrayList();
      AssemblyDescriptorBean var3 = var1.getEjbJarBean().getAssemblyDescriptor();
      if (null != var3) {
         MethodPermissionBean[] var4 = var3.getMethodPermissions();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            MethodPermissionImpl var6 = new MethodPermissionImpl(var4[var5]);
            var2.add(var6);
         }
      }

      return var2;
   }

   private Iterator getAllMethodInfosIterator() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getBeanInfos().iterator();

      while(var2.hasNext()) {
         var1.add(((BeanInfo)var2.next()).getAllMethodInfosIterator());
      }

      return new CombinedIterator(var1);
   }

   private void initializeMethodPermissions(EjbDescriptorBean var1) throws DeploymentDescriptorException {
      Collection var2 = this.getAllMethodPermissions(var1);
      if (var2.size() > 0) {
         this.processDefaultMethodMPs(var1, var2);
         this.processGenericMethodMPs(var1, var2);
         this.processSpecificMethodMPs(var1, var2);
      }

      Iterator var3 = this.getAllMethodInfosIterator();

      while(var3.hasNext()) {
         MethodInfo var4 = (MethodInfo)var3.next();
         var4.setRealmName(this.m_securityRealmName);
      }

   }

   private void initializeTransactionAttribute(EjbDescriptorBean var1) throws DeploymentDescriptorException {
      AssemblyDescriptorBean var2 = var1.getEjbJarBean().getAssemblyDescriptor();
      if (null != var2) {
         ContainerTransactionBean[] var3 = var2.getContainerTransactions();
         ArrayList var4 = new ArrayList();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            ContainerTransactionImpl var6 = new ContainerTransactionImpl(var3[var5]);
            var4.add(var6);
         }

         this.processDefaultMethodCTs(var1, var4);
         this.processGenericMethodCTs(var1, var4);
         this.processSpecificMethodCTs(var1, var4);
      }

      Iterator var7 = this.getBeanInfos().iterator();

      while(var7.hasNext()) {
         BeanInfo var8 = (BeanInfo)var7.next();
         var8.assignDefaultTXAttributesIfNecessary();
      }

   }

   private void initializeTransactionLevels(EjbDescriptorBean var1) throws DeploymentDescriptorException {
      Collection var2 = this.getAllIsolationLevels(var1);
      this.processDefaultMethodISOs(var2);
      this.processGenericMethodISOs(var2);
      this.processSpecificMethodISOs(var2);
   }

   private void initializeIdempotentMethods(EjbDescriptorBean var1) throws DeploymentDescriptorException {
      Collection var2 = this.getAllIdempotentMethods(var1);
      this.processDefaultMethodIdempotency(var2);
      this.processGenericMethodIdempotency(var2);
      this.processSpecificMethodIdempotency(var2);
   }

   private void initializeRetryMethodsOnRollback(EjbDescriptorBean var1) throws DeploymentDescriptorException {
      Collection var2 = this.getAllRetryMethods(var1);
      this.processDefaultMethodRetries(var2);
      this.processGenericMethodRetries(var2);
      this.processSpecificMethodRetries(var2);
   }

   private void initializeExcludedMethods(EjbDescriptorBean var1) throws DeploymentDescriptorException, WLDeploymentException {
      AssemblyDescriptorBean var2 = var1.getEjbJarBean().getAssemblyDescriptor();
      if (null != var2) {
         ExcludeListBean var3 = var2.getExcludeList();
         if (var3 != null) {
            ExcludeListImpl var4 = new ExcludeListImpl(var3);
            Collection var5 = var4.getAllMethodDescriptors();
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               MethodDescriptor var7 = (MethodDescriptor)var6.next();
               Collection var8 = this.lookupMethodInfos(var7);
               Iterator var9 = var8.iterator();

               while(var9.hasNext()) {
                  MethodInfo var10 = (MethodInfo)var9.next();
                  var10.setIsExcluded(true);
                  var10.setMethodDescriptorMethodType(var7.getMethodType());
               }
            }
         }
      }

   }

   private void initializeMessageDestinations(EjbDescriptorBean var1) throws WLDeploymentException {
      WeblogicEjbJarBean var2 = var1.getWeblogicEjbJarBean();
      if (var2 != null) {
         MessageDestinationDescriptorBean[] var3 = var2.getMessageDestinationDescriptors();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.messageDestinations.put(var3[var4].getMessageDestinationName(), var3[var4]);
         }
      }

      EjbJarBean var8 = var1.getEjbJarBean();
      AssemblyDescriptorBean var9 = var8.getAssemblyDescriptor();
      if (var9 != null) {
         MessageDestinationBean[] var5 = var9.getMessageDestinations();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (!this.messageDestinations.containsKey(var5[var6].getMessageDestinationName())) {
               Loggable var7 = EJBLogger.logMissingMessageDestinationDescriptorLoggable(var5[var6].getMessageDestinationName(), this.jarFileName);
               throw new WLDeploymentException(var7.getMessage());
            }
         }
      }

   }

   private void initializeApplicationExceptions(EjbDescriptorBean var1) throws WLDeploymentException {
      if (!var1.isEjb30()) {
         this.uncheckedAppExceptions = Collections.emptySet();
      } else {
         EjbJarBean var2 = var1.getEjbJarBean();
         AssemblyDescriptorBean var3 = var2.getAssemblyDescriptor();
         ApplicationExceptionBean[] var4 = null;
         if (var3 != null) {
            var4 = var3.getApplicationExceptions();
         }

         if (var4 == null) {
            this.uncheckedAppExceptions = Collections.emptySet();
         } else {
            this.uncheckedAppExceptions = new HashSet(var4.length);

            for(int var5 = 0; var5 < var4.length; ++var5) {
               String var6 = var4[var5].getExceptionClass();
               this.applicationExceptions.put(var6, var4[var5].isRollback() ? Boolean.TRUE : Boolean.FALSE);

               try {
                  Class var7 = this.m_classLoader.loadClass(var6);
                  if (RuntimeException.class.isAssignableFrom(var7)) {
                     this.uncheckedAppExceptions.add(var7);
                  }
               } catch (ClassNotFoundException var8) {
                  throw new WLDeploymentException("Failed to load class : " + var6, var8);
               }
            }

            this.uncheckedAppExceptions = Collections.unmodifiableSet(this.uncheckedAppExceptions);
         }
      }
   }

   public boolean addApplicationException(Class var1) {
      if (this.ejbDescriptor.isEjb30() && !this.ejbDescriptor.getEjbJarBean().isMetadataComplete() && var1.isAnnotationPresent(ApplicationException.class)) {
         ApplicationException var2 = (ApplicationException)var1.getAnnotation(ApplicationException.class);
         synchronized(this.applicationExceptions) {
            if (!this.applicationExceptions.containsKey(var1.getName())) {
               this.applicationExceptions.put(var1.getName(), var2.rollback());
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public Collection getMessageDestinationDescriptors() {
      return this.messageDestinations.values();
   }

   public MessageDestinationDescriptorBean getMessageDestinationDescriptor(String var1) {
      return (MessageDestinationDescriptorBean)this.messageDestinations.get(var1);
   }

   private void setupRelationsForEJBAndInterceptors(EjbDescriptorBean var1) {
      EjbJarBean var2 = var1.getEjbJarBean();
      HashSet var3 = new HashSet();
      AssemblyDescriptorBean var4 = var2.getAssemblyDescriptor();
      if (var4 != null) {
         InterceptorBindingBean[] var5 = var4.getInterceptorBindings();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            String[] var7 = null;
            if (var5[var6].getInterceptorOrder() != null) {
               var7 = var5[var6].getInterceptorOrder().getInterceptorClasses();
            } else {
               var7 = var5[var6].getInterceptorClasses();
            }

            String var8 = var5[var6].getEjbName();
            HashSet var9 = (HashSet)this.ejbToInterceptor.get(var8);
            if (var9 == null) {
               var9 = new HashSet();
            }

            for(int var10 = 0; var10 < var7.length; ++var10) {
               var3.add(var7[var10]);
               var9.add(var7[var10]);
            }

            this.ejbToInterceptor.put(var8, var9);
         }
      }

      InterceptorsBean var11 = var2.getInterceptors();
      if (var11 != null) {
         InterceptorBean[] var12 = var11.getInterceptors();

         for(int var13 = 0; var13 < var12.length; ++var13) {
            InterceptorBean var14 = var12[var13];
            if (var3.contains(var14.getInterceptorClass())) {
               this.iceptorClassToIBean.put(var14.getInterceptorClass(), var14);
            }
         }
      }

   }

   private void processDefaultMethodISOs(Collection var1) throws DeploymentDescriptorException {
      this.processISOs(var1, (short)1);
   }

   private void processGenericMethodISOs(Collection var1) throws DeploymentDescriptorException {
      this.processISOs(var1, (short)2);
   }

   private void processSpecificMethodISOs(Collection var1) throws DeploymentDescriptorException {
      this.processISOs(var1, (short)3);
   }

   private static int selectForUpdateFromIsoLevel(String var0) {
      if (var0.equalsIgnoreCase("TransactionReadCommittedForUpdate")) {
         return 1;
      } else if (var0.equalsIgnoreCase("TransactionReadCommittedForUpdateNoWait")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TRANSACTION_READ_COMMITTED_FOR_UPDATE")) {
         return 1;
      } else {
         return var0.equalsIgnoreCase("TRANSACTION_READ_COMMITTED_FOR_UPDATE_NO_WAIT") ? 2 : 0;
      }
   }

   private void processISOs(Collection var1, short var2) throws DeploymentDescriptorException {
      if (debug) {
         Debug.assertion(var2 == 1 || var2 == 2 || var2 == 3);
      }

      Iterator var3 = var1.iterator();

      label46:
      while(var3.hasNext()) {
         IsolationLevel var4 = (IsolationLevel)var3.next();
         Collection var5 = var4.getAllMethodDescriptors();
         Iterator var6 = var5.iterator();

         while(true) {
            MethodDescriptor var7;
            do {
               if (!var6.hasNext()) {
                  continue label46;
               }

               var7 = (MethodDescriptor)var6.next();
            } while(var7.getMethodType() != var2);

            Collection var8 = this.lookupMethodInfos(var7);
            if (var8.size() == 0) {
               String var12 = "";
               if (var7.getMethodIntf() != null) {
                  var12 = var7.getMethodIntf() + " ";
               }

               throw new DeploymentDescriptorException(this.fmt.noMethodFoundForEJBDeploymentDescriptorSetting(var7.getMethodSignature(), var7.getEjbName(), var12, "weblogic-ejb-jar.xml", "isolation-level"), new DescriptorErrorInfo("<isolation-level>", var7.getEjbName(), var7.getMethodSignature()));
            }

            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               MethodInfo var10 = (MethodInfo)var9.next();
               String var11 = var4.getIsolationLevel();
               var10.setSelectForUpdate(selectForUpdateFromIsoLevel(var11));
               var10.setTxIsolationLevel(DDUtils.isoStringToInt(var11));
            }
         }
      }

   }

   private Collection getAllIsolationLevels(EjbDescriptorBean var1) {
      ArrayList var2 = new ArrayList();
      TransactionIsolationBean[] var3 = var1.getWeblogicEjbJarBean().getTransactionIsolations();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.add(new IsolationLevelImpl(var3[var4]));
      }

      return var2;
   }

   private Collection getAllIdempotentMethods(EjbDescriptorBean var1) {
      ArrayList var2 = new ArrayList();
      IdempotentMethodsBean var3 = var1.getWeblogicEjbJarBean().getIdempotentMethods();
      if (var3 != null) {
         MethodBean[] var4 = var3.getMethods();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var2.add(new MethodDescriptorImpl(var4[var5]));
         }
      }

      return var2;
   }

   private Collection getAllRetryMethods(EjbDescriptorBean var1) {
      ArrayList var2 = new ArrayList();
      RetryMethodsOnRollbackBean[] var3 = var1.getWeblogicEjbJarBean().getRetryMethodsOnRollbacks();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.add(new RetryMethodsOnRollbackImpl(var3[var4]));
      }

      return var2;
   }

   private void processDefaultMethodIdempotency(Collection var1) throws DeploymentDescriptorException {
      this.processIdempotency(var1, (short)1);
   }

   private void processGenericMethodIdempotency(Collection var1) throws DeploymentDescriptorException {
      this.processIdempotency(var1, (short)2);
   }

   private void processSpecificMethodIdempotency(Collection var1) throws DeploymentDescriptorException {
      this.processIdempotency(var1, (short)3);
   }

   private void processIdempotency(Collection var1, short var2) throws DeploymentDescriptorException {
      if (debug) {
         Debug.assertion(var2 == 1 || var2 == 2 || var2 == 3);
      }

      Iterator var3 = var1.iterator();

      while(true) {
         MethodDescriptor var4;
         do {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (MethodDescriptor)var3.next();
         } while(var4.getMethodType() != var2);

         Collection var5 = this.lookupMethodInfos(var4);
         if (var5.size() == 0) {
            String var8 = "";
            if (var4.getMethodIntf() != null) {
               var8 = var4.getMethodIntf() + " ";
            }

            throw new DeploymentDescriptorException(this.fmt.noMethodFoundForEJBDeploymentDescriptorSetting(var4.getMethodSignature(), var4.getEjbName(), var8, "weblogic-ejb-jar.xml", "idempotency"), new DescriptorErrorInfo("<idempotent-methods>", var4.getEjbName(), var4.getMethodSignature()));
         }

         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            MethodInfo var7 = (MethodInfo)var6.next();
            var7.setIdempotent(true);
         }
      }
   }

   private void processDefaultMethodRetries(Collection var1) throws DeploymentDescriptorException {
      this.processRetries(var1, (short)1);
   }

   private void processGenericMethodRetries(Collection var1) throws DeploymentDescriptorException {
      this.processRetries(var1, (short)2);
   }

   private void processSpecificMethodRetries(Collection var1) throws DeploymentDescriptorException {
      this.processRetries(var1, (short)3);
   }

   private void processRetries(Collection var1, short var2) throws DeploymentDescriptorException {
      if (debug) {
         Debug.assertion(var2 == 1 || var2 == 2 || var2 == 3);
      }

      Iterator var3 = var1.iterator();

      label46:
      while(var3.hasNext()) {
         RetryMethodsOnRollback var4 = (RetryMethodsOnRollback)var3.next();
         Collection var5 = var4.getAllMethodDescriptors();
         Iterator var6 = var5.iterator();

         while(true) {
            MethodDescriptor var7;
            do {
               if (!var6.hasNext()) {
                  continue label46;
               }

               var7 = (MethodDescriptor)var6.next();
            } while(var7.getMethodType() != var2);

            Collection var8 = this.lookupMethodInfos(var7);
            if (var8.size() == 0) {
               String var12 = "";
               if (var7.getMethodIntf() != null) {
                  var12 = var7.getMethodIntf() + " ";
               }

               throw new DeploymentDescriptorException(this.fmt.noMethodFoundForEJBDeploymentDescriptorSetting(var7.getMethodSignature(), var7.getEjbName(), var12, "weblogic-ejb-jar.xml", "retry-methods-on-rollback"), new DescriptorErrorInfo("<isolation-level>", var7.getEjbName(), var7.getMethodSignature()));
            }

            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               MethodInfo var10 = (MethodInfo)var9.next();
               int var11 = var4.getRetryCount();
               var10.setRetryOnRollbackCount(var11);
            }
         }
      }

   }

   private void processDefaultMethodMPs(EjbDescriptorBean var1, Collection var2) throws DeploymentDescriptorException {
      this.processMPs(var1, var2, (short)1);
   }

   private void processGenericMethodMPs(EjbDescriptorBean var1, Collection var2) throws DeploymentDescriptorException {
      this.processMPs(var1, var2, (short)2);
   }

   private void processSpecificMethodMPs(EjbDescriptorBean var1, Collection var2) throws DeploymentDescriptorException {
      this.processMPs(var1, var2, (short)3);
   }

   private void processMPs(EjbDescriptorBean var1, Collection var2, short var3) throws DeploymentDescriptorException {
      Debug.assertion(var3 == 1 || var3 == 2 || var3 == 3);
      Iterator var4 = var2.iterator();

      label62:
      while(var4.hasNext()) {
         MethodPermission var5 = (MethodPermission)var4.next();
         boolean var6 = var5.isUnchecked();
         Collection var7 = var5.getAllMethodDescriptors();
         Iterator var8 = var7.iterator();

         label60:
         while(true) {
            MethodDescriptor var9;
            do {
               if (!var8.hasNext()) {
                  continue label62;
               }

               var9 = (MethodDescriptor)var8.next();
            } while(var9.getMethodType() != var3);

            Collection var10 = this.lookupMethodInfos(var9);
            if (var10.size() == 0) {
               String[] var16 = new String[]{"<method>", "<method-permission>"};
               String var17 = "";
               if (var9.getMethodIntf() != null) {
                  var17 = var9.getMethodIntf() + " ";
               }

               throw new DeploymentDescriptorException(this.fmt.noMethodFoundForEJBDeploymentDescriptorSetting(var9.getMethodSignature(), var9.getEjbName(), var17, "ejb-jar.xml", "method permission"), new DescriptorErrorInfo(var16, var9.getEjbName(), var9.getMethodSignature()));
            }

            Iterator var11 = var10.iterator();

            while(true) {
               while(true) {
                  if (!var11.hasNext()) {
                     continue label60;
                  }

                  MethodInfo var12 = (MethodInfo)var11.next();
                  var12.setMethodDescriptorMethodType(var9.getMethodType());
                  if (var6) {
                     var12.setUnchecked(true);
                  } else {
                     Collection var13 = var5.getAllRoleNames();
                     Iterator var14 = var13.iterator();

                     while(var14.hasNext()) {
                        String var15 = (String)var14.next();
                        if (!this.m_deploymentRoles.hasRole(var15)) {
                           throw new DeploymentDescriptorException(this.fmt.METHOD_PERMISSION_ROLE_NAME_NOT_DECLARED(var15), new DescriptorErrorInfo("<role-name>", var15, var15));
                        }

                        var12.addSecurityRoleRestriction(var15);
                     }
                  }
               }
            }
         }
      }

   }

   private Collection lookupMethodInfos(MethodDescriptor var1) throws DeploymentDescriptorException {
      BeanInfo var2 = this.getBeanInfo(var1.getEjbName());
      if (var2 == null) {
         throw new DeploymentDescriptorException("Could not find an EJB named " + var1.getEjbName() + " while parsing the assembly-descriptor", new DescriptorErrorInfo("<ejb-name>", var1.getEjbName(), var1.getEjbName()));
      } else {
         ArrayList var3 = new ArrayList();
         String var4 = var1.getMethodSignature();
         if (var2 instanceof MessageDrivenBeanInfo) {
            if (((MessageDrivenBeanInfo)var2).getIsWeblogicJMS()) {
               MethodInfo var5 = ((MessageDrivenBeanInfo)var2).getOnMessageMethodInfo();
               if (debug) {
                  Debug.assertion(var5 != null);
               }

               var3.add(var5);
            } else {
               var3.addAll(this.getMatchingMethods(var2, "MessagingType", var4));
            }

            var3.addAll(this.getMatchingMethods(var2, (String)null, var4));
         } else {
            if (debug) {
               Debug.assertion(var2 instanceof ClientDrivenBeanInfo);
            }

            String var6 = var1.getMethodIntf();
            if ("Remote".equals(var6)) {
               var3.addAll(this.getMatchingMethods(var2, "Remote", var4));
            } else if ("Home".equals(var6)) {
               var3.addAll(this.getMatchingMethods(var2, "Home", var4));
            } else if ("Local".equals(var6)) {
               var3.addAll(this.getMatchingMethods(var2, "Local", var4));
            } else if ("LocalHome".equals(var6)) {
               var3.addAll(this.getMatchingMethods(var2, "LocalHome", var4));
            } else if ("ServiceEndpoint".equals(var6)) {
               var3.addAll(this.getMatchingMethods(var2, "ServiceEndpoint", var4));
            } else {
               var3.addAll(this.getMatchingMethods(var2, "Local", var4));
               var3.addAll(this.getMatchingMethods(var2, "LocalHome", var4));
               var3.addAll(this.getMatchingMethods(var2, "Remote", var4));
               var3.addAll(this.getMatchingMethods(var2, "Home", var4));
               var3.addAll(this.getMatchingMethods(var2, "ServiceEndpoint", var4));
               var3.addAll(this.getMatchingMethods(var2, (String)null, var4));
            }
         }

         return var3;
      }
   }

   private Collection getMatchingMethods(BeanInfo var1, String var2, String var3) {
      if (debug) {
         Debug.assertion(var1 != null);
         Debug.assertion(var3 != null);
      }

      ArrayList var4 = new ArrayList();
      Collection var5 = null;
      ClientDrivenBeanInfo var6 = null;
      MessageDrivenBeanInfo var7 = null;
      if (var1 instanceof MessageDrivenBeanInfo) {
         var7 = (MessageDrivenBeanInfo)var1;
      } else {
         var6 = (ClientDrivenBeanInfo)var1;
      }

      if (var2 == "Home") {
         var5 = var6.getAllHomeMethodInfos();
      } else if (var2 == "Remote") {
         var5 = var6.getAllRemoteMethodInfos();
      } else if (var2 == "LocalHome") {
         var5 = var6.getAllLocalHomeMethodInfos();
      } else if (var2 == "Local") {
         var5 = var6.getAllLocalMethodInfos();
      } else if (var2 == "MessagingType") {
         var5 = var7.getAllMessagingTypeMethodInfos();
      } else if (var2 == "ServiceEndpoint") {
         var5 = var6.getAllWebserviceMethodInfos();
      } else {
         if (var2 != null) {
            throw new AssertionError("Unknown interface type: " + var2);
         }

         if (var7 == null) {
            var5 = var6.getAllBeanMethodInfos();
         } else {
            var5 = var7.getAllBeanMethodInfos();
         }
      }

      if (var3.equals("*")) {
         var4.addAll(var5);
         return var4;
      } else {
         Iterator var8 = var5.iterator();

         while(true) {
            MethodInfo var9;
            do {
               if (!var8.hasNext()) {
                  return var4;
               }

               var9 = (MethodInfo)var8.next();
            } while(!var3.equals(var9.getSignature()) && !var3.equals(var9.getMethodName()));

            var4.add(var9);
         }
      }
   }

   private void processDefaultMethodCTs(EjbDescriptorBean var1, Collection var2) throws DeploymentDescriptorException {
      this.processCTs(var1, var2, (short)1);
   }

   private void processGenericMethodCTs(EjbDescriptorBean var1, Collection var2) throws DeploymentDescriptorException {
      this.processCTs(var1, var2, (short)2);
   }

   private void processSpecificMethodCTs(EjbDescriptorBean var1, Collection var2) throws DeploymentDescriptorException {
      this.processCTs(var1, var2, (short)3);
   }

   private void processCTs(EjbDescriptorBean var1, Collection var2, short var3) throws DeploymentDescriptorException {
      if (debug) {
         Debug.assertion(var3 == 1 || var3 == 2 || var3 == 3);
      }

      Iterator var4 = var2.iterator();

      label46:
      while(var4.hasNext()) {
         ContainerTransaction var5 = (ContainerTransaction)var4.next();
         Collection var6 = var5.getAllMethodDescriptors();
         Iterator var7 = var6.iterator();

         while(true) {
            MethodDescriptor var8;
            do {
               if (!var7.hasNext()) {
                  continue label46;
               }

               var8 = (MethodDescriptor)var7.next();
            } while(var8.getMethodType() != var3);

            Collection var9 = this.lookupMethodInfos(var8);
            if (var9.size() == 0) {
               String var14 = "";
               if (var8.getMethodIntf() != null) {
                  var14 = var8.getMethodIntf() + " ";
               }

               throw new DeploymentDescriptorException(this.fmt.noMethodFoundForEJBDeploymentDescriptorSetting(var8.getMethodSignature(), var8.getEjbName(), var14, "ejb-jar.xml", "transaction attribute"), new DescriptorErrorInfo("<trans-attribute>", var8.getEjbName(), var8.getMethodSignature()));
            }

            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               MethodInfo var11 = (MethodInfo)var10.next();
               String var12 = var5.getTransactionAttribute();
               Short var13 = (Short)DDConstants.VALID_TX_ATTRIBUTES.get(var12);
               var11.setTransactionAttribute(var13);
            }
         }
      }

   }

   private Set getDisabledWarnings(EjbDescriptorBean var1) {
      HashSet var2 = new HashSet();
      String[] var3 = var1.getWeblogicEjbJarBean().getDisableWarnings();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.add(var3[var4]);
      }

      return var2;
   }

   static boolean isServer() {
      return Kernel.isServer();
   }

   public String getApplicationId() {
      return this.m_applicationId;
   }

   public ConcurrentHashMap<Class<?>, Boolean> getRollBackExeptionsCache() {
      return this.rollbackExceptionsCache;
   }

   public ConcurrentHashMap<Class<?>, Boolean> getApplicationExeptionsCache() {
      return this.appExeptionsCache;
   }
}
