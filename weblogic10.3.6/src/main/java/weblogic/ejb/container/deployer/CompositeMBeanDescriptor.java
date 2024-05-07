package weblogic.ejb.container.deployer;

import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.dd.ClusteringDescriptor;
import weblogic.ejb.container.deployer.mbimpl.SecurityRoleRefImpl;
import weblogic.ejb.container.interfaces.CachingDescriptor;
import weblogic.ejb.container.interfaces.IIOPSecurityDescriptor;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.NamedMethodBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.RunAsBean;
import weblogic.j2ee.descriptor.SecurityIdentityBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.BusinessInterfaceJndiNameMapBean;
import weblogic.j2ee.descriptor.wl.EjbReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.EntityCacheBean;
import weblogic.j2ee.descriptor.wl.EntityCacheRefBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.InvalidationTargetBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.ResourceEnvDescriptionBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.StatelessSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.TimerDescriptorBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl60.BaseWeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;
import weblogic.logging.Loggable;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public class CompositeMBeanDescriptor {
   private static boolean verbose = false;
   private static boolean debug = true;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private EnterpriseBeanBean m_bean;
   private EjbDescriptorBean m_ejbDesc;
   private BaseWeblogicRdbmsBeanBean m_rdbmsBean;
   private WeblogicEnterpriseBeanBean m_wlBean;
   private Map m_envEntries = null;
   private Map m_ejbReferences = null;
   private Map m_ejbLocalReferences = null;
   private Map m_allServiceReferences = null;
   private Map m_allServiceReferenceDescriptions = null;
   private Map m_allResourceReferences = null;
   private Map m_allResourceEnvReferences = null;
   private Map m_allWlResourceReferences = null;
   private Map m_allWlResourceEnvReferences = null;
   private Map m_securityRoleReferences = null;
   private Map m_businessInterfaceJndiNames = null;
   private boolean m_idleTimeoutSecondsCacheInitialized = false;
   private int m_idleTimeoutSecondsCache = 0;
   private boolean m_idleTimeoutSecondsPoolInitialized = false;
   private int m_idleTimeoutSecondsPool = 0;
   private boolean m_sessionTimeoutSecondsInitialized = false;
   private int m_sessionTimeoutSeconds = 0;
   private boolean m_readTimeoutSecondsInitialized = false;
   private int m_readTimeoutSeconds = 0;
   private CachingDescriptor m_cachingDescriptor = null;
   private static final int EJB_STATELESS_SESSION = 1;
   private static final int EJB_STATEFUL_SESSION = 2;
   private static final int EJB_ENTITY = 3;
   private static final int EJB_MESSAGE_DRIVEN = 4;
   private int m_type = -1;

   public CompositeMBeanDescriptor(EnterpriseBeanBean var1, WeblogicEnterpriseBeanBean var2, EjbDescriptorBean var3) throws WLDeploymentException {
      this.m_bean = var1;
      this.m_ejbDesc = var3;
      this.m_wlBean = var2;
      this.init();
   }

   public CompositeMBeanDescriptor(String var1, EjbDescriptorBean var2) throws WLDeploymentException {
      this.m_bean = findEJB(var1, var2);
      this.m_ejbDesc = var2;
      this.init();
   }

   public CompositeMBeanDescriptor(EnterpriseBeanBean var1, EjbDescriptorBean var2) throws WLDeploymentException {
      this.m_bean = var1;
      this.m_ejbDesc = var2;
      this.init();
   }

   public boolean isEJB30() {
      return this.m_ejbDesc.isEjb30();
   }

   public void delete() {
      EnterpriseBeanBean var1 = this.getBean();
      WeblogicEnterpriseBeanBean var2 = this.getWlBean();
      EjbJarBean var3 = this.getEJBDescriptor().getEjbJarBean();
      EnterpriseBeansBean var4 = var3.getEnterpriseBeans();
      int var6;
      if (this.isEntity()) {
         EntityBeanBean[] var5 = var4.getEntities();

         for(var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getEjbName().equals(var1.getEjbName())) {
               var3.getEnterpriseBeans().destroyEntity(var5[var6]);
            }
         }
      } else if (this.isSession()) {
         SessionBeanBean[] var8 = var4.getSessions();

         for(var6 = 0; var6 < var8.length; ++var6) {
            if (var8[var6].getEjbName().equals(var1.getEjbName())) {
               var3.getEnterpriseBeans().destroySession(var8[var6]);
            }
         }
      } else if (this.isMessageDriven()) {
         MessageDrivenBeanBean[] var9 = var4.getMessageDrivens();

         for(var6 = 0; var6 < var9.length; ++var6) {
            if (var9[var6].getEjbName().equals(var1.getEjbName())) {
               var4.destroyMessageDriven(var9[var6]);
            }
         }
      }

      WeblogicEjbJarBean var10 = this.getEJBDescriptor().getWeblogicEjbJarBean();
      WeblogicEnterpriseBeanBean[] var11 = var10.getWeblogicEnterpriseBeans();

      for(int var7 = 0; var7 < var11.length; ++var7) {
         if (var2.getEjbName().equals(var11[var7].getEjbName())) {
            var10.destroyWeblogicEnterpriseBean(var11[var7]);
         }
      }

   }

   private void init() throws WLDeploymentException {
      this.initialize60(this.m_bean, this.m_ejbDesc);
      Debug.assertion(this.m_bean.getEjbName() != null);
      Debug.assertion(this.m_wlBean.getEjbName() != null);
      completeBeans(this.m_ejbDesc);
      Debug.assertion(null != this.m_wlBean);
      this.initializeType();
      this.initializeEJBReferences();
      this.initializeEJBLocalReferences();
      this.initializeServiceReferences();
      this.initializeServiceReferenceDescriptions();
   }

   public String getTableName() {
      return ((WeblogicRdbmsBeanBean)this.m_rdbmsBean).getTableName();
   }

   private void initializeType() throws WLDeploymentException {
      if (this.m_bean instanceof EntityBeanBean) {
         this.m_type = 3;
      } else if (this.m_bean instanceof MessageDrivenBeanBean) {
         this.m_type = 4;
      } else {
         this.m_type = 1;
         SessionBeanBean var1 = (SessionBeanBean)this.m_bean;
         if ("stateful".equalsIgnoreCase(var1.getSessionType())) {
            this.m_type = 2;
         }
      }

      Loggable var2;
      if (!this.isEntity() && this.m_wlBean.getEntityDescriptor() != null) {
         var2 = EJBLogger.logmismatchBetweenEJBNamesLoggable(this.m_wlBean.getEjbName());
         throw new WLDeploymentException(var2.getMessage());
      } else if (!this.isStatelessSession() && this.m_wlBean.getStatelessSessionDescriptor() != null) {
         var2 = EJBLogger.logmismatchBetweenslsbEJBNamesLoggable(this.m_wlBean.getEjbName());
         throw new WLDeploymentException(var2.getMessage());
      } else if (!this.isStatefulSession() && this.m_wlBean.getStatefulSessionDescriptor() != null) {
         var2 = EJBLogger.logmismatchBetweensfsbEJBNamesLoggable(this.m_wlBean.getEjbName());
         throw new WLDeploymentException(var2.getMessage());
      } else if (!this.isMessageDriven() && this.m_wlBean.getMessageDrivenDescriptor() != null) {
         var2 = EJBLogger.logmismatchBetweenmdbEJBNamesLoggable(this.m_wlBean.getEjbName());
         throw new WLDeploymentException(var2.getMessage());
      }
   }

   public WeblogicEnterpriseBeanBean getWl60Bean() {
      return this.m_wlBean;
   }

   private void initialize60(EnterpriseBeanBean var1, EjbDescriptorBean var2) throws WLDeploymentException {
      String var3 = var1.getEjbName();
      WeblogicEnterpriseBeanBean[] var4 = var2.getWeblogicEjbJarBean().getWeblogicEnterpriseBeans();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getEjbName().equals(var3)) {
            this.m_wlBean = var4[var5];
            break;
         }
      }

      EJBComplianceTextFormatter var13 = new EJBComplianceTextFormatter();
      if (this.m_wlBean == null) {
         throw new WLDeploymentException(var13.CANNOT_FIND_WL_DESCRIPTOR_FOR_EJB(var3));
      } else {
         if (var1 instanceof EntityBeanBean) {
            EntityBeanBean var6 = (EntityBeanBean)var1;
            WeblogicRdbmsJarBean[] var7 = var2.getWeblogicRdbms11JarBeans();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               WeblogicRdbmsJarBean var9 = var7[var8];
               WeblogicRdbmsBeanBean[] var10 = var9.getWeblogicRdbmsBeans();

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  if (var3.equals(var10[var11].getEjbName())) {
                     this.m_rdbmsBean = var10[var11];
                     break;
                  }
               }
            }

            if (null == this.m_rdbmsBean) {
               weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean[] var14 = var2.getWeblogicRdbmsJarBeans();

               for(int var15 = 0; var15 < var14.length; ++var15) {
                  weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean var16 = var14[var15];
                  weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean[] var17 = var16.getWeblogicRdbmsBeans();

                  for(int var12 = 0; var12 < var17.length; ++var12) {
                     if (var3.equals(var17[var12].getEjbName())) {
                        this.m_rdbmsBean = var17[var12];
                        break;
                     }
                  }
               }
            }
         }

      }
   }

   public EjbDescriptorBean getEJBDescriptor() {
      return this.m_ejbDesc;
   }

   public EnterpriseBeanBean getBean() {
      return this.m_bean;
   }

   public BaseWeblogicRdbmsBeanBean getRDBMSBean() {
      return this.m_rdbmsBean;
   }

   public void setRDBMSBean(BaseWeblogicRdbmsBeanBean var1) {
      this.m_rdbmsBean = var1;
   }

   public WeblogicEnterpriseBeanBean getWlBean() {
      return this.m_wlBean;
   }

   public String getCMPVersion() {
      return !this.isEntity() ? null : ((EntityBeanBean)this.m_bean).getCmpVersion();
   }

   public String getHomeInterfaceName() {
      if (this.isSession()) {
         return ((SessionBeanBean)this.m_bean).getHome();
      } else if (this.isEntity()) {
         return ((EntityBeanBean)this.m_bean).getHome();
      } else {
         throw new AssertionError("Trying to get the home interface of a MessageDriven Bean");
      }
   }

   public void setHomeInterfaceName(String var1) {
      if (this.isSession()) {
         ((SessionBeanBean)this.m_bean).setHome(var1);
      } else {
         if (!this.isEntity()) {
            throw new AssertionError("Trying to get the home interface of a MessageDriven Bean");
         }

         ((EntityBeanBean)this.m_bean).setHome(var1);
      }

   }

   public String getRemoteInterfaceName() {
      if (this.isSession()) {
         return ((SessionBeanBean)this.m_bean).getRemote();
      } else if (this.isEntity()) {
         return ((EntityBeanBean)this.m_bean).getRemote();
      } else {
         throw new AssertionError("Trying to get the remote interface of a MessageDriven Bean");
      }
   }

   public void setRemoteInterfaceName(String var1) {
      if (this.isSession()) {
         ((SessionBeanBean)this.m_bean).setRemote(var1);
      } else {
         if (!this.isEntity()) {
            throw new AssertionError("Trying to get the remote interface of a MessageDriven Bean");
         }

         ((EntityBeanBean)this.m_bean).setRemote(var1);
      }

   }

   public String getLocalHomeInterfaceName() {
      if (this.isSession()) {
         return ((SessionBeanBean)this.m_bean).getLocalHome();
      } else if (this.isEntity()) {
         return ((EntityBeanBean)this.m_bean).getLocalHome();
      } else if (this.isMessageDriven()) {
         throw new AssertionError("Trying to get the local home interface of a MessageDriven Bean");
      } else {
         return null;
      }
   }

   public String getLocalInterfaceName() {
      if (this.isSession()) {
         return ((SessionBeanBean)this.m_bean).getLocal();
      } else if (this.isEntity()) {
         return ((EntityBeanBean)this.m_bean).getLocal();
      } else if (this.isMessageDriven()) {
         throw new AssertionError("Trying to get the local interface of a MessageDriven Bean");
      } else {
         return null;
      }
   }

   public String[] getBusinessRemotes() {
      return ((SessionBeanBean)this.m_bean).getBusinessRemotes();
   }

   public String[] getBusinessLocals() {
      return ((SessionBeanBean)this.m_bean).getBusinessLocals();
   }

   public String getMessagingTypeName() {
      return this.isMessageDriven() ? ((MessageDrivenBeanBean)this.m_bean).getMessagingType() : null;
   }

   public String getServiceEndpointName() {
      return this.isSession() ? ((SessionBeanBean)this.m_bean).getServiceEndpoint() : null;
   }

   public String getEJBClassName() {
      return this.m_bean.getEjbClass();
   }

   private boolean isJNDINameDefined() {
      if (this.isEJB30() && this.isSession() && ((SessionBeanBean)this.m_bean).getMappedName() != null) {
         return true;
      } else {
         return this.m_wlBean.getJNDIName() != null;
      }
   }

   public String getJNDIName() {
      if (this.isEJB30() && this.isSession()) {
         SessionBeanBean var1 = (SessionBeanBean)this.m_bean;
         return var1.getMappedName() != null && var1.getHome() != null && this.m_wlBean.getJNDIName() == null ? var1.getMappedName() + "#" + var1.getHome() : this.m_wlBean.getJNDIName();
      } else {
         return this.m_wlBean.getJNDIName();
      }
   }

   public String getBusinessJNDIName(Class var1) {
      if (this.m_businessInterfaceJndiNames == null) {
         this.m_businessInterfaceJndiNames = new HashMap();
         BusinessInterfaceJndiNameMapBean[] var2;
         if (this.isStatefulSession()) {
            var2 = this.m_wlBean.getStatefulSessionDescriptor().getBusinessInterfaceJndiNameMaps();
         } else {
            if (!this.isStatelessSession()) {
               return null;
            }

            var2 = this.m_wlBean.getStatelessSessionDescriptor().getBusinessInterfaceJndiNameMaps();
         }

         SessionBeanBean var3 = (SessionBeanBean)this.m_bean;
         int var4;
         if (var3.getBusinessRemotes() != null && var3.getMappedName() != null) {
            for(var4 = 0; var4 < var3.getBusinessRemotes().length; ++var4) {
               String var5 = var3.getBusinessRemotes()[var4];
               this.m_businessInterfaceJndiNames.put(var5, var3.getMappedName() + "#" + var5);
            }
         }

         for(var4 = 0; var4 < var2.length; ++var4) {
            this.m_businessInterfaceJndiNames.put(var2[var4].getBusinessRemote(), var2[var4].getJNDIName());
         }
      }

      return (String)this.m_businessInterfaceJndiNames.get(var1.getName());
   }

   public String getDispatchPolicy() {
      return this.m_wlBean.getDispatchPolicy();
   }

   public boolean getStickToFirstServer() {
      return this.m_wlBean.isStickToFirstServer();
   }

   public int getRemoteClientTimeout() {
      return this.m_wlBean.getRemoteClientTimeout();
   }

   public String getLocalJNDIName() {
      if (this.isEJB30() && this.isSession()) {
         SessionBeanBean var1 = (SessionBeanBean)this.m_bean;
         return var1.getMappedName() != null && var1.getLocalHome() != null && this.m_wlBean.getLocalJNDIName() == null ? var1.getMappedName() + "#" + var1.getLocalHome() : this.m_wlBean.getLocalJNDIName();
      } else {
         return this.m_wlBean.getLocalJNDIName();
      }
   }

   public String getDestinationJNDIName() {
      if (!this.isMessageDriven()) {
         return null;
      } else {
         MessageDrivenBeanBean var1 = (MessageDrivenBeanBean)this.m_bean;
         MessageDrivenDescriptorBean var2 = this.m_wlBean.getMessageDrivenDescriptor();
         if (var2 != null) {
            return this.isEJB30() && var1.getMappedName() != null && var2.getDestinationJNDIName() == null ? var1.getMappedName() : var2.getDestinationJNDIName();
         } else {
            return this.isEJB30() && var1.getMappedName() != null ? var1.getMappedName() : null;
         }
      }
   }

   public String getEJBName() {
      return this.m_bean.getEjbName();
   }

   public Collection getAllEnvironmentEntries() {
      if (null == this.m_envEntries) {
         this.m_envEntries = new HashMap();
         EnvEntryBean[] var1 = this.m_bean.getEnvEntries();
         if (null != var1) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.m_envEntries.put(var1[var2].getEnvEntryName(), var1[var2]);
            }
         }
      }

      return this.m_envEntries.values();
   }

   public Collection getAllEJBReferences() {
      return this.m_ejbReferences.values();
   }

   public Collection getAllEJBLocalReferences() {
      return this.m_ejbLocalReferences.values();
   }

   public Collection getServiceReferences() {
      return this.m_allServiceReferences.values();
   }

   public Collection getServiceReferenceDescriptions() {
      return this.m_allServiceReferenceDescriptions.values();
   }

   public Collection getAllResourceReferences() {
      if (null == this.m_allResourceReferences) {
         this.m_allResourceReferences = new HashMap();
         ResourceRefBean[] var1 = this.m_bean.getResourceRefs();
         if (null != var1) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.m_allResourceReferences.put(var1[var2].getResRefName(), var1[var2]);
            }
         }
      }

      return this.m_allResourceReferences.values();
   }

   public Collection getAllResourceEnvReferences() {
      if (null == this.m_allResourceEnvReferences) {
         this.m_allResourceEnvReferences = new HashMap();
         ResourceEnvRefBean[] var1 = this.m_bean.getResourceEnvRefs();
         if (null != var1) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.m_allResourceEnvReferences.put(var1[var2].getResourceEnvRefName(), var1[var2]);
            }
         }
      }

      return this.m_allResourceEnvReferences.values();
   }

   public Collection getAllWlResourceReferences() {
      if (null == this.m_allWlResourceReferences) {
         this.m_allWlResourceReferences = new HashMap();
         ResourceDescriptionBean[] var1 = this.m_wlBean.getResourceDescriptions();
         if (null != var1) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.m_allWlResourceReferences.put(var1[var2].getResRefName(), var1[var2]);
            }
         }
      }

      return this.m_allWlResourceReferences.values();
   }

   public Collection getAllWlResourceEnvReferences() {
      if (null == this.m_allWlResourceEnvReferences) {
         this.m_allWlResourceEnvReferences = new HashMap();
         ResourceEnvDescriptionBean[] var1 = this.m_wlBean.getResourceEnvDescriptions();
         if (null != var1) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.m_allWlResourceEnvReferences.put(var1[var2].getResourceEnvRefName(), var1[var2]);
            }
         }
      }

      return this.m_allWlResourceEnvReferences.values();
   }

   public Collection getAllMessageDestinationReferences() {
      MessageDestinationRefBean[] var1 = this.m_bean.getMessageDestinationRefs();
      return Arrays.asList((Object[])var1);
   }

   public Map getSecurityRoleReferencesMap() {
      if (null == this.m_securityRoleReferences) {
         this.m_securityRoleReferences = new HashMap();
         if (this.isSession() || this.isEntity()) {
            SecurityRoleRefBean[] var1;
            if (this.isSession()) {
               var1 = ((SessionBeanBean)this.m_bean).getSecurityRoleRefs();
            } else {
               var1 = ((EntityBeanBean)this.m_bean).getSecurityRoleRefs();
            }

            if (null != var1) {
               for(int var2 = 0; var2 < var1.length; ++var2) {
                  this.m_securityRoleReferences.put(var1[var2].getRoleName(), new SecurityRoleRefImpl(this.m_ejbDesc, var1[var2]));
               }
            }
         }
      }

      return this.m_securityRoleReferences;
   }

   public PersistenceContextRefBean[] getPersistenceContextRefs() {
      return this.m_bean.getPersistenceContextRefs();
   }

   public PersistenceUnitRefBean[] getPersistenceUnitRefs() {
      return this.m_bean.getPersistenceUnitRefs();
   }

   public int getTransactionTimeoutSeconds() {
      TransactionDescriptorBean var1 = this.getWl60Bean().getTransactionDescriptor();
      int var2 = var1.getTransTimeoutSeconds();
      if (var2 == 0) {
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         JTAMBean var4 = var3.getJTA();
         return var4.getTimeoutSeconds();
      } else {
         return var2;
      }
   }

   public boolean useCallByReference() {
      return this.m_wlBean.isEnableCallByReference();
   }

   public String getNetworkAccessPoint() {
      return this.m_wlBean.getNetworkAccessPoint();
   }

   public boolean getClientsOnSameServer() {
      return this.m_wlBean.isClientsOnSameServer();
   }

   public int getIdleTimeoutSecondsCache() {
      if (!this.m_idleTimeoutSecondsCacheInitialized) {
         this.m_idleTimeoutSecondsCache = -1;
         this.m_idleTimeoutSecondsCacheInitialized = true;
         if (this.isStatefulSession()) {
            this.m_idleTimeoutSecondsCache = this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().getIdleTimeoutSeconds();
            this.m_idleTimeoutSecondsCacheInitialized = true;
         } else if (this.isEntity()) {
            if (this.hasEntityCacheReference()) {
               this.m_idleTimeoutSecondsCache = this.m_wlBean.getEntityDescriptor().getEntityCacheRef().getIdleTimeoutSeconds();
            } else if (this.m_wlBean.getEntityDescriptor().getEntityCache() != null) {
               this.m_idleTimeoutSecondsCache = this.m_wlBean.getEntityDescriptor().getEntityCache().getIdleTimeoutSeconds();
            }
         }
      }

      return this.m_idleTimeoutSecondsCache;
   }

   public void setIdleTimeoutSecondsCache(int var1) {
      if (this.isStatefulSession()) {
         this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().setIdleTimeoutSeconds(var1);
      } else if (this.isEntity()) {
         if (this.hasEntityCacheReference()) {
            this.m_wlBean.getEntityDescriptor().getEntityCacheRef().setIdleTimeoutSeconds(var1);
         } else {
            this.m_wlBean.getEntityDescriptor().getEntityCache().setIdleTimeoutSeconds(var1);
         }
      }

   }

   public int getIdleTimeoutSecondsPool() {
      if (!this.m_idleTimeoutSecondsPoolInitialized) {
         this.m_idleTimeoutSecondsPool = -1;
         this.m_idleTimeoutSecondsPoolInitialized = true;
         if (this.isStatelessSession()) {
            this.m_idleTimeoutSecondsPool = this.m_wlBean.getStatelessSessionDescriptor().getPool().getIdleTimeoutSeconds();
            this.m_idleTimeoutSecondsPoolInitialized = true;
         } else if (this.isMessageDriven()) {
            this.m_idleTimeoutSecondsPool = this.m_wlBean.getMessageDrivenDescriptor().getPool().getIdleTimeoutSeconds();
            this.m_idleTimeoutSecondsPoolInitialized = true;
         } else if (this.isEntity()) {
            this.m_idleTimeoutSecondsPool = this.m_wlBean.getEntityDescriptor().getPool().getIdleTimeoutSeconds();
            this.m_idleTimeoutSecondsPoolInitialized = true;
         }
      }

      return this.m_idleTimeoutSecondsPool;
   }

   public void setIdleTimeoutSecondsPool(int var1) {
      if (this.isStatelessSession()) {
         this.m_wlBean.getStatelessSessionDescriptor().getPool().setIdleTimeoutSeconds(var1);
      } else if (this.isEntity()) {
         this.m_wlBean.getEntityDescriptor().getPool().setIdleTimeoutSeconds(var1);
      }

   }

   public int getSessionTimeoutSeconds() {
      if (!this.m_sessionTimeoutSecondsInitialized && this.isStatefulSession()) {
         this.m_sessionTimeoutSeconds = this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().getSessionTimeoutSeconds();
         this.m_sessionTimeoutSecondsInitialized = true;
      }

      return this.m_sessionTimeoutSeconds;
   }

   public void setSessionTimeoutSeconds(int var1) {
      if (this.isStatefulSession()) {
         this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().setSessionTimeoutSeconds(var1);
      }

   }

   public int getReadTimeoutSeconds() {
      if (!this.m_readTimeoutSecondsInitialized) {
         this.m_readTimeoutSeconds = -1;
         this.m_readTimeoutSecondsInitialized = true;
         if (this.isEntity()) {
            EntityDescriptorBean var1 = this.m_wlBean.getEntityDescriptor();
            boolean var2 = false;
            if (this.hasEntityCacheReference()) {
               this.m_readTimeoutSeconds = var1.getEntityCacheRef().getReadTimeoutSeconds();
               var2 = ((DescriptorBean)var1.getEntityCacheRef()).isSet("ReadTimeoutSeconds");
            } else {
               this.m_readTimeoutSeconds = var1.getEntityCache().getReadTimeoutSeconds();
               var2 = ((DescriptorBean)var1.getEntityCache()).isSet("ReadTimeoutSeconds");
            }

            if (this.getCacheBetweenTransactions() && !var2) {
               this.m_readTimeoutSeconds = 0;
            }
         }
      }

      return this.m_readTimeoutSeconds;
   }

   public String getCacheType() {
      String var1 = "";
      if (this.isStatefulSession()) {
         var1 = this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().getCacheType();
      }

      return var1;
   }

   public int getMaxBeansInFreePool() {
      if (this.isStatelessSession()) {
         return this.m_wlBean.getStatelessSessionDescriptor().getPool().getMaxBeansInFreePool();
      } else if (this.isEntity()) {
         return this.m_wlBean.getEntityDescriptor().getPool().getMaxBeansInFreePool();
      } else {
         return this.isMessageDriven() ? this.m_wlBean.getMessageDrivenDescriptor().getPool().getMaxBeansInFreePool() : 0;
      }
   }

   public void setMaxBeansInFreePool(int var1) {
      if (this.isStatelessSession()) {
         Debug.assertion(null != this.m_wlBean.getStatelessSessionDescriptor());
         Debug.assertion(null != this.m_wlBean.getStatelessSessionDescriptor().getPool());
         this.m_wlBean.getStatelessSessionDescriptor().getPool().setMaxBeansInFreePool(var1);
      } else if (this.isEntity()) {
         Debug.assertion(null != this.m_wlBean.getEntityDescriptor());
         Debug.assertion(null != this.m_wlBean.getEntityDescriptor().getPool());
         this.m_wlBean.getEntityDescriptor().getPool().setMaxBeansInFreePool(var1);
      } else if (this.isMessageDriven()) {
         this.m_wlBean.getMessageDrivenDescriptor().getPool().setMaxBeansInFreePool(var1);
      }

   }

   public int getInitialBeansInFreePool() {
      if (this.isStatelessSession()) {
         return this.m_wlBean.getStatelessSessionDescriptor().getPool().getInitialBeansInFreePool();
      } else if (this.isEntity()) {
         return this.m_wlBean.getEntityDescriptor().getPool().getInitialBeansInFreePool();
      } else {
         return this.isMessageDriven() ? this.m_wlBean.getMessageDrivenDescriptor().getPool().getInitialBeansInFreePool() : 0;
      }
   }

   public int getMaxBeansInCache() {
      if (this.isEntity() && !this.hasEntityCacheReference()) {
         return this.m_wlBean.getEntityDescriptor().getEntityCache().getMaxBeansInCache();
      } else {
         return this.isStatefulSession() ? this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().getMaxBeansInCache() : -1;
      }
   }

   public void setMaxBeansInCache(int var1) {
      if (this.isEntity()) {
         this.m_wlBean.getEntityDescriptor().getEntityCache().setMaxBeansInCache(var1);
      } else if (this.isStatefulSession()) {
         Debug.assertion(null != this.m_wlBean.getStatefulSessionDescriptor());
         Debug.assertion(null != this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache());
         this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionCache().setMaxBeansInCache(var1);
      }

   }

   public int getMaxQueriesInCache() {
      if (this.isEntity() && !this.hasEntityCacheReference()) {
         int var1 = this.m_wlBean.getEntityDescriptor().getEntityCache().getMaxQueriesInCache();
         return var1;
      } else {
         return -1;
      }
   }

   public String getConcurrencyStrategy() {
      String var1 = null;
      if (this.isEntity()) {
         if (this.hasEntityCacheReference()) {
            var1 = this.m_wlBean.getEntityDescriptor().getEntityCacheRef().getConcurrencyStrategy();
         } else {
            var1 = this.m_wlBean.getEntityDescriptor().getEntityCache().getConcurrencyStrategy();
         }
      }

      return var1;
   }

   public CachingDescriptor getCachingDescriptor() {
      CachingDescriptorImpl var1 = new CachingDescriptorImpl();
      var1.setMaxBeansInCache(this.getMaxBeansInCache());
      var1.setMaxQueriesInCache(this.getMaxQueriesInCache());
      var1.setMaxBeansInFreePool(this.getMaxBeansInFreePool());
      var1.setInitialBeansInFreePool(this.getInitialBeansInFreePool());
      var1.setIdleTimeoutSecondsCache(this.getIdleTimeoutSecondsCache());
      var1.setIdleTimeoutSecondsPool(this.getIdleTimeoutSecondsPool());
      var1.setCacheType(this.getCacheType());
      var1.setReadTimeoutSeconds(this.getReadTimeoutSeconds());
      var1.setConcurrencyStrategy(this.getConcurrencyStrategy());
      return var1;
   }

   public IIOPSecurityDescriptor getIIOPSecurityDescriptor() {
      IIOPSecurityDescriptorImpl var1 = new IIOPSecurityDescriptorImpl();
      if (this.m_wlBean.getIiopSecurityDescriptor() == null) {
         return var1;
      } else {
         if (this.m_wlBean.getIiopSecurityDescriptor().getTransportRequirements() != null) {
            var1.setTransport_integrity(this.m_wlBean.getIiopSecurityDescriptor().getTransportRequirements().getIntegrity());
            var1.setTransport_confidentiality(this.m_wlBean.getIiopSecurityDescriptor().getTransportRequirements().getConfidentiality());
            var1.setTransport_client_cert_authentication(this.m_wlBean.getIiopSecurityDescriptor().getTransportRequirements().getClientCertAuthentication());
         }

         var1.setClient_authentication(this.m_wlBean.getIiopSecurityDescriptor().getClientAuthentication());
         var1.setIdentity_assertion(this.m_wlBean.getIiopSecurityDescriptor().getIdentityAssertion());
         return var1;
      }
   }

   public boolean hasEntityCacheReference() {
      if (verbose) {
         Debug.say("called hasEntityCacheReference ejb- " + this.m_wlBean.getEjbName() + " value- " + (this.m_wlBean.getEntityDescriptor().getEntityCacheRef() != null));
      }

      return this.m_wlBean.getEntityDescriptor().getEntityCacheRef() != null;
   }

   public String getEntityCacheName() {
      EntityCacheRefBean var1 = this.m_wlBean.getEntityDescriptor().getEntityCacheRef();
      if (debug) {
         Debug.assertion(var1 != null);
      }

      return var1.getEntityCacheName();
   }

   public int getEstimatedBeanSize() {
      EntityCacheRefBean var1 = this.m_wlBean.getEntityDescriptor().getEntityCacheRef();
      if (debug) {
         Debug.assertion(var1 != null);
      }

      return var1.getEstimatedBeanSize();
   }

   private String getHomeLoadAlgorithm() {
      String var1 = "";
      if (this.isEntity()) {
         Debug.assertion(null != this.m_wlBean.getEntityDescriptor());
         Debug.assertion(null != this.m_wlBean.getEntityDescriptor().getEntityClustering());
         var1 = this.m_wlBean.getEntityDescriptor().getEntityClustering().getHomeLoadAlgorithm();
      } else if (this.isStatefulSession()) {
         Debug.assertion(null != this.m_wlBean);
         Debug.assertion(null != this.m_wlBean.getStatefulSessionDescriptor());
         Debug.assertion(null != this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering());
         var1 = this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().getHomeLoadAlgorithm();
      } else {
         if (!this.isStatelessSession()) {
            throw new AssertionError("HomeLoadAlgorithm should only be for Entity or Stateful Session");
         }

         Debug.assertion(null != this.m_wlBean);
         Debug.assertion(null != this.m_wlBean.getStatelessSessionDescriptor());
         Debug.assertion(null != this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering());
         var1 = this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().getHomeLoadAlgorithm();
      }

      return var1;
   }

   public void setHomeLoadAlgorithm(String var1) {
      String var2 = "";
      if (this.isEntity()) {
         this.m_wlBean.getEntityDescriptor().getEntityClustering().setHomeLoadAlgorithm(var1);
      } else if (this.isStatefulSession()) {
         this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().setHomeLoadAlgorithm(var1);
      } else {
         if (!this.isStatelessSession()) {
            throw new AssertionError("HomeLoadAlgorithm should only be for Entity or Stateful Session");
         }

         this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().setHomeLoadAlgorithm(var1);
      }

   }

   public boolean getHomeIsClusterable() {
      if (this.isEntity()) {
         return this.m_wlBean.getEntityDescriptor().getEntityClustering().isHomeIsClusterable();
      } else if (this.isStatefulSession()) {
         return this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().isHomeIsClusterable();
      } else if (this.isStatelessSession()) {
         return this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().isHomeIsClusterable();
      } else {
         throw new AssertionError("HomeIsClusterable should only be for Entity or Stateful Session");
      }
   }

   public boolean getUseServersideStubs() {
      if (this.isEntity()) {
         return this.m_wlBean.getEntityDescriptor().getEntityClustering().isUseServersideStubs();
      } else if (this.isStatefulSession()) {
         return this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().isUseServersideStubs();
      } else {
         return this.isStatelessSession() ? this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().isUseServersideStubs() : false;
      }
   }

   public void setHomeIsClusterable(boolean var1) {
      if (this.isEntity()) {
         this.m_wlBean.getEntityDescriptor().getEntityClustering().setHomeIsClusterable(var1);
      } else if (this.isStatefulSession()) {
         this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().setHomeIsClusterable(var1);
      } else if (this.isStatelessSession()) {
         this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().setHomeIsClusterable(var1);
      }

   }

   private String getHomeCallRouterClassName() {
      String var1 = "";
      if (this.isEntity()) {
         var1 = this.m_wlBean.getEntityDescriptor().getEntityClustering().getHomeCallRouterClassName();
      } else if (this.isStatefulSession()) {
         var1 = this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().getHomeCallRouterClassName();
      } else {
         if (!this.isStatelessSession()) {
            throw new AssertionError("HomeCallRouterClassName should only be for Entity or Stateful Session");
         }

         var1 = this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().getHomeCallRouterClassName();
      }

      return var1;
   }

   public void setHomeCallRouterClassName(String var1) {
      if (this.isEntity()) {
         this.m_wlBean.getEntityDescriptor().getEntityClustering().setHomeCallRouterClassName(var1);
      } else if (this.isStatefulSession()) {
         this.m_wlBean.getStatefulSessionDescriptor().getStatefulSessionClustering().setHomeCallRouterClassName(var1);
      } else {
         if (!this.isStatelessSession()) {
            throw new AssertionError("HomeCallRouterClassName should only be for Entity or Stateful Session");
         }

         this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().setHomeCallRouterClassName(var1);
      }

   }

   private boolean getStatelessBeanIsClusterable() {
      if (this.isStatelessSession()) {
         return this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().isStatelessBeanIsClusterable();
      } else {
         throw new AssertionError("StatelessBeanIsClusterable should only be for Statless");
      }
   }

   public void setStatelessBeanIsClusterable(boolean var1) {
      if (this.isStatelessSession()) {
         this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().setStatelessBeanIsClusterable(var1);
      } else {
         throw new AssertionError("StatelessBeanIsClusterable should only be for Statless");
      }
   }

   private String getStatelessBeanLoadAlgorithm() {
      String var1 = "";
      if (this.isStatelessSession()) {
         var1 = this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().getStatelessBeanLoadAlgorithm();
         return var1;
      } else {
         throw new AssertionError("StatelessBeanLoadAlgorithm should only be for Statless");
      }
   }

   public void setStatelessBeanLoadAlgorithm(String var1) {
      if (this.isStatelessSession()) {
         this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().setStatelessBeanLoadAlgorithm(var1);
      } else {
         throw new AssertionError("StatelessBeanLoadAlgorithm should only be for Statless");
      }
   }

   private String getStatelessBeanCallRouterClassName() {
      String var1 = "";
      if (this.isStatelessSession()) {
         var1 = this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().getStatelessBeanCallRouterClassName();
         return var1;
      } else {
         throw new AssertionError("StatelessBeanCallRouterClassName should only be for Statless");
      }
   }

   public void setStatelessBeanCallRouterClassName(String var1) {
      if (this.isStatelessSession()) {
         this.m_wlBean.getStatelessSessionDescriptor().getStatelessClustering().setStatelessBeanCallRouterClassName(var1);
      } else {
         throw new AssertionError("StatelessBeanCallRouterClassName should only be for Stateless");
      }
   }

   public ClusteringDescriptor getClusteringDescriptor() {
      ClusteringDescriptor var1 = new ClusteringDescriptor();
      if (this.isJNDINameDefined()) {
         if (this.isEntity() || this.isStatefulSession() || this.isStatelessSession()) {
            var1.setHomeLoadAlgorithm(this.getHomeLoadAlgorithm());
            var1.setHomeIsClusterable(this.getHomeIsClusterable());
            var1.setHomeCallRouterClassName(this.getHomeCallRouterClassName());
            var1.setUseServersideStubs(this.getUseServersideStubs());
         }

         if (this.isStatelessSession()) {
            var1.setStatelessBeanIsClusterable(this.getStatelessBeanIsClusterable());
            var1.setStatelessBeanLoadAlgorithm(this.getStatelessBeanLoadAlgorithm());
            var1.setStatelessBeanCallRouterClassName(this.getStatelessBeanCallRouterClassName());
         }
      } else {
         var1.setHomeIsClusterable(false);
         var1.setStatelessBeanIsClusterable(false);
      }

      return var1;
   }

   public boolean isEntity() {
      return 3 == this.m_type;
   }

   public boolean isStatefulSession() {
      return 2 == this.m_type;
   }

   public boolean isStatelessSession() {
      return 1 == this.m_type;
   }

   public boolean isSession() {
      return 1 == this.m_type || 2 == this.m_type;
   }

   public boolean isMessageDriven() {
      return 4 == this.m_type;
   }

   public String getResourceJNDIName(String var1) {
      ResourceDescriptionBean[] var2 = this.m_wlBean.getResourceDescriptions();
      if (var2 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var1.equals(var2[var3].getResRefName())) {
               return var2[var3].getJNDIName();
            }
         }

         return null;
      }
   }

   public static Map getAllEJBReferenceJNDINames(WeblogicEnterpriseBeanBean var0, EnterpriseBeanBean var1) {
      HashMap var2 = new HashMap();
      HashSet var3 = new HashSet();
      EjbRefBean[] var4 = var1.getEjbRefs();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.add(var4[var5].getEjbRefName());
      }

      EjbReferenceDescriptionBean[] var7 = var0.getEjbReferenceDescriptions();
      if (null != var7) {
         for(int var6 = 0; var6 < var7.length; ++var6) {
            if (var3.contains(var7[var6].getEjbRefName())) {
               var2.put(var7[var6].getEjbRefName(), var7[var6].getJNDIName());
            }
         }
      }

      return var2;
   }

   public Map getAllEJBReferenceJNDINames() {
      HashMap var1 = new HashMap();
      EjbReferenceDescriptionBean[] var2 = this.m_wlBean.getEjbReferenceDescriptions();
      if (null != var2) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (this.m_ejbReferences.containsKey(var2[var3].getEjbRefName()) && ((EjbRefBean)this.m_ejbReferences.get(var2[var3].getEjbRefName())).getHome() != null) {
               var1.put(var2[var3].getEjbRefName(), var2[var3].getJNDIName());
            }
         }
      }

      return var1;
   }

   public Map getAllEJBLocalReferenceJNDINames() {
      HashMap var1 = new HashMap();
      EjbReferenceDescriptionBean[] var2 = this.m_wlBean.getEjbReferenceDescriptions();
      if (null != var2) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (this.m_ejbLocalReferences.containsKey(var2[var3].getEjbRefName()) && ((EjbLocalRefBean)this.m_ejbLocalReferences.get(var2[var3].getEjbRefName())).getLocalHome() != null) {
               var1.put(var2[var3].getEjbRefName(), var2[var3].getJNDIName());
            }
         }
      }

      return var1;
   }

   public String resolveResourceLink(String var1, String var2) {
      return "weblogic." + var1 + "." + var2;
   }

   public Map getAllResourceReferenceJNDINames(String var1) {
      HashMap var2 = new HashMap();
      ResourceDescriptionBean[] var3 = this.m_wlBean.getResourceDescriptions();
      if (null != var3) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getJNDIName();
            if (var1 != null && (var5 == null || var5.length() <= 0)) {
               String var6 = var3[var4].getResourceLink();
               if (var6 != null && var6.length() > 0) {
                  var5 = this.resolveResourceLink(var1, var6);
               }
            }

            var2.put(var3[var4].getResRefName(), var5);
         }
      }

      return var2;
   }

   public Map getAllResourceEnvReferenceJNDINames(String var1) {
      HashMap var2 = new HashMap();
      ResourceEnvDescriptionBean[] var3 = this.m_wlBean.getResourceEnvDescriptions();
      if (null != var3) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getJNDIName();
            if (var1 != null && (var5 == null || var5.length() <= 0)) {
               String var6 = var3[var4].getResourceLink();
               if (var6 != null && var6.length() > 0) {
                  var5 = this.resolveResourceLink(var1, var6);
               }
            }

            var2.put(var3[var4].getResourceEnvRefName(), var5);
         }
      }

      return var2;
   }

   private RunAsBean getRunAsMBean() {
      RunAsBean var1 = null;
      SecurityIdentityBean var2 = null;
      if (this.isEntity()) {
         var2 = ((EntityBeanBean)this.m_bean).getSecurityIdentity();
      } else if (this.isSession()) {
         var2 = ((SessionBeanBean)this.m_bean).getSecurityIdentity();
      } else if (this.isMessageDriven()) {
         var2 = ((MessageDrivenBeanBean)this.m_bean).getSecurityIdentity();
      }

      if (var2 != null) {
         var1 = var2.getRunAs();
      }

      return var1;
   }

   public String getRunAsRoleName() {
      String var1 = null;
      RunAsBean var2 = this.getRunAsMBean();
      if (var2 != null) {
         var1 = var2.getRoleName();
      }

      return var1;
   }

   public String getRunAsIdentityPrincipal() {
      return this.m_wlBean.getRunAsPrincipalName();
   }

   public String getCreateAsPrincipalName() {
      return this.m_wlBean.getCreateAsPrincipalName();
   }

   public String getRemoveAsPrincipalName() {
      return this.m_wlBean.getRemoveAsPrincipalName();
   }

   public String getPassivateAsPrincipalName() {
      return this.m_wlBean.getPassivateAsPrincipalName();
   }

   public NamedMethodBean getTimeoutMethod() {
      if (this.isSession()) {
         return ((SessionBeanBean)this.m_bean).getTimeoutMethod();
      } else {
         return this.isMessageDriven() ? ((MessageDrivenBeanBean)this.m_bean).getTimeoutMethod() : null;
      }
   }

   public String getTimerStoreName() {
      TimerDescriptorBean var1 = null;
      if (this.isEntity()) {
         var1 = this.m_wlBean.getEntityDescriptor().getTimerDescriptor();
      } else if (this.isMessageDriven()) {
         var1 = this.m_wlBean.getMessageDrivenDescriptor().getTimerDescriptor();
      } else if (this.isStatelessSession()) {
         var1 = this.m_wlBean.getStatelessSessionDescriptor().getTimerDescriptor();
      }

      return var1 == null ? null : var1.getPersistentStoreLogicalName();
   }

   private static void p(String var0) {
      Debug.say("@@@ " + var0);
   }

   private static WeblogicEnterpriseBeanBean findWl60Bean(String var0, WeblogicEnterpriseBeanBean[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var0.equals(var1[var2].getEjbName())) {
            return var1[var2];
         }
      }

      return null;
   }

   public static EnterpriseBeanBean[] getEnterpriseBeans(EjbJarBean var0) {
      return getEnterpriseBeans(var0.getEnterpriseBeans());
   }

   public static EnterpriseBeanBean[] getEnterpriseBeans(EnterpriseBeansBean var0) {
      SessionBeanBean[] var1 = var0.getSessions();
      EntityBeanBean[] var2 = var0.getEntities();
      MessageDrivenBeanBean[] var3 = var0.getMessageDrivens();
      EnterpriseBeanBean[] var4 = new EnterpriseBeanBean[var1.length + var2.length + var3.length];
      boolean var5 = false;

      int var7;
      for(var7 = 0; var7 < var1.length; ++var7) {
         var4[var7] = var1[var7];
      }

      int var6;
      for(var6 = 0; var6 < var2.length; ++var6) {
         var4[var7++] = var2[var6];
      }

      for(var6 = 0; var6 < var3.length; ++var6) {
         var4[var7++] = var3[var6];
      }

      return var4;
   }

   public static void completeBeans(EjbDescriptorBean var0) throws WLDeploymentException {
      if (null == var0.getEjbJarBean()) {
         EjbJarBean var1 = var0.createEjbJarBean();
         var1.createEnterpriseBeans();
      }

      if (null == var0.getWeblogicEjbJarBean()) {
         var0.createWeblogicEjbJarBean();
      }

      EnterpriseBeanBean[] var6 = getEnterpriseBeans(var0.getEjbJarBean());

      for(int var2 = 0; var2 < var6.length; ++var2) {
         Debug.assertion(null != var0.getWeblogicEjbJarBean());
         EnterpriseBeanBean var3 = var6[var2];
         WeblogicEnterpriseBeanBean var4 = findWl60Bean(var3.getEjbName(), var0.getWeblogicEjbJarBean().getWeblogicEnterpriseBeans());
         if (var4 == null) {
            EJBComplianceTextFormatter var10 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var10.CANNOT_FIND_WL_DESCRIPTOR_FOR_EJB(var3.getEjbName()));
         }

         if (var3 instanceof EntityBeanBean) {
            EntityDescriptorBean var9 = var4.getEntityDescriptor();
            if (null == var9) {
               var9 = var4.createEntityDescriptor();
            }

            if (null == var9.getEntityCache() && null == var9.getEntityCacheRef()) {
               var9.createEntityCache();
            }

            if (null == var9.getPersistence()) {
               var9.createPersistence();
            }

            if (null == var9.getEntityClustering()) {
               var9.createEntityClustering();
            }

            if (null == var9.getPool()) {
               var9.createPool();
            }
         } else if (var3 instanceof SessionBeanBean && "stateless".equalsIgnoreCase(((SessionBeanBean)var3).getSessionType())) {
            StatelessSessionDescriptorBean var8 = var4.getStatelessSessionDescriptor();
            if (null == var8) {
               var8 = var4.createStatelessSessionDescriptor();
            }

            if (null == var8.getPool()) {
               var8.createPool();
            }

            if (null == var8.getStatelessClustering()) {
               var8.createStatelessClustering();
            }
         } else if (var3 instanceof SessionBeanBean && "stateful".equalsIgnoreCase(((SessionBeanBean)var3).getSessionType())) {
            StatefulSessionDescriptorBean var7 = var4.getStatefulSessionDescriptor();
            if (null == var7) {
               var7 = var4.createStatefulSessionDescriptor();
            }

            if (null == var7.getStatefulSessionCache()) {
               var7.createStatefulSessionCache();
            }

            if (null == var7.getStatefulSessionClustering()) {
               var7.createStatefulSessionClustering();
            }

            Debug.assertion(null != var7.getStatefulSessionClustering());
         } else {
            MessageDrivenDescriptorBean var5 = var4.getMessageDrivenDescriptor();
            if (null == var5) {
               var5 = var4.createMessageDrivenDescriptor();
            }
         }

         if (var4.getTransactionDescriptor() == null) {
            var4.createTransactionDescriptor();
         }
      }

   }

   public boolean getEntityAlwaysUsesTransaction() {
      return null == this.m_ejbDesc.getWeblogicEjbJarBean() ? false : this.m_ejbDesc.getWeblogicEjbJarBean().getWeblogicCompatibility().isEntityAlwaysUsesTransaction();
   }

   public void setEJBName(String var1) {
      this.m_bean.setEjbName(var1);
      this.m_wlBean.setEjbName(var1);
      if (null != this.m_rdbmsBean) {
         this.m_rdbmsBean.setEjbName(var1);
      }

   }

   public void setJNDIName(String var1) {
      this.m_wlBean.setJNDIName(var1);
   }

   public boolean isBeanManagedPersistence() {
      EntityBeanBean var1 = (EntityBeanBean)this.m_bean;
      return "bean".equalsIgnoreCase(var1.getPersistenceType());
   }

   public boolean getCacheBetweenTransactions() {
      if (this.hasEntityCacheReference()) {
         EntityCacheRefBean var3 = this.m_wlBean.getEntityDescriptor().getEntityCacheRef();
         if (debug) {
            Debug.assertion(var3 != null);
         }

         return var3.isCacheBetweenTransactions();
      } else {
         EntityCacheBean var1 = this.m_wlBean.getEntityDescriptor().getEntityCache();
         if (debug) {
            Debug.assertion(var1 != null);
         }

         PersistenceBean var2 = this.m_wlBean.getEntityDescriptor().getPersistence();
         if (debug) {
            Debug.assertion(var2 != null);
         }

         return var1.isCacheBetweenTransactions();
      }
   }

   public boolean getDisableReadyIntances() {
      if (!this.hasEntityCacheReference()) {
         EntityCacheBean var1 = this.m_wlBean.getEntityDescriptor().getEntityCache();
         if (debug) {
            Debug.assertion(var1 != null);
         }

         PersistenceBean var2 = this.m_wlBean.getEntityDescriptor().getPersistence();
         if (debug) {
            Debug.assertion(var2 != null);
         }

         return var1.isDisableReadyInstances();
      } else {
         return false;
      }
   }

   public String getIsModifiedMethodName() {
      return this.m_wlBean.getEntityDescriptor().getPersistence().getIsModifiedMethodName();
   }

   public boolean getDelayUpdatesUntilEndOfTx() {
      return this.m_wlBean.getEntityDescriptor().getPersistence().isDelayUpdatesUntilEndOfTx();
   }

   public String getPersistenceUseIdentifier() {
      PersistenceBean var1 = this.m_wlBean.getEntityDescriptor().getPersistence();
      if (var1 != null) {
         PersistenceUseBean var2 = var1.getPersistenceUse();
         if (var2 != null) {
            return var2.getTypeIdentifier();
         }
      }

      return null;
   }

   public String getPersistenceUseVersion() {
      PersistenceBean var1 = this.m_wlBean.getEntityDescriptor().getPersistence();
      if (var1 != null) {
         PersistenceUseBean var2 = var1.getPersistenceUse();
         if (var2 != null) {
            return var2.getTypeVersion();
         }
      }

      return null;
   }

   public String getPersistenceUseStorage() {
      PersistenceBean var1 = this.m_wlBean.getEntityDescriptor().getPersistence();
      if (var1 != null) {
         PersistenceUseBean var2 = var1.getPersistenceUse();
         if (var2 != null) {
            return var2.getTypeStorage();
         }
      }

      return null;
   }

   public boolean getFindersLoadBean() {
      return this.m_wlBean.getEntityDescriptor().getPersistence().isFindersLoadBean();
   }

   public EnvEntryBean createEnvironmentEntry() {
      return this.m_bean.createEnvEntry();
   }

   public String getInvalidationTargetEJBName() {
      InvalidationTargetBean var1 = this.m_wlBean.getEntityDescriptor().getInvalidationTarget();
      return var1 == null ? null : var1.getEjbName();
   }

   public boolean isDynamicQueriesEnabled() {
      return this.m_wlBean.getEntityDescriptor() != null ? this.m_wlBean.getEntityDescriptor().isEnableDynamicQueries() : false;
   }

   public boolean isClusteredTimers() {
      String var1 = this.m_ejbDesc.getWeblogicEjbJarBean().getTimerImplementation();
      return "Clustered".equals(var1);
   }

   private static void ppp(String var0) {
      System.out.println("[CompositeMBeanDescriptor] " + var0);
   }

   public static EnterpriseBeanBean findEJB(String var0, EjbDescriptorBean var1) {
      return findEJB(var0, var1.getEjbJarBean());
   }

   public static EnterpriseBeanBean findEJB(String var0, EjbJarBean var1) {
      EnterpriseBeanBean[] var2 = getEnterpriseBeans(var1);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getEjbName().equals(var0)) {
            return var2[var3];
         }
      }

      return null;
   }

   private void initializeEJBReferences() throws WLDeploymentException {
      this.m_ejbReferences = new HashMap();
      EjbRefBean[] var1 = this.m_bean.getEjbRefs();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (this.m_ejbReferences.containsKey(var1[var2].getEjbRefName())) {
            EJBComplianceTextFormatter var3 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var3.noDuplicateEjbRefNamesAllowed(this.getEJBName(), var1[var2].getEjbRefName()));
         }

         this.m_ejbReferences.put(var1[var2].getEjbRefName(), var1[var2]);
      }

   }

   private void initializeEJBLocalReferences() throws WLDeploymentException {
      this.m_ejbLocalReferences = new HashMap();
      EjbLocalRefBean[] var1 = this.m_bean.getEjbLocalRefs();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (this.m_ejbLocalReferences.containsKey(var1[var2].getEjbRefName())) {
            EJBComplianceTextFormatter var3 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var3.noDuplicateEjbRefNamesAllowed(this.getEJBName(), var1[var2].getEjbRefName()));
         }

         this.m_ejbLocalReferences.put(var1[var2].getEjbRefName(), var1[var2]);
      }

   }

   private void initializeServiceReferences() throws WLDeploymentException {
      this.m_allServiceReferences = new HashMap();
      ServiceRefBean[] var1 = this.m_bean.getServiceRefs();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (this.m_allServiceReferences.containsKey(var1[var2].getServiceRefName())) {
            EJBComplianceTextFormatter var3 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var3.noDuplicateServiceRefNamesAllowed(this.getEJBName(), var1[var2].getServiceRefName()));
         }

         this.m_allServiceReferences.put(var1[var2].getServiceRefName(), var1[var2]);
      }

   }

   private void initializeServiceReferenceDescriptions() throws WLDeploymentException {
      this.m_allServiceReferenceDescriptions = new HashMap();
      ServiceReferenceDescriptionBean[] var1 = this.m_wlBean.getServiceReferenceDescriptions();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         EJBComplianceTextFormatter var3;
         if (this.m_allServiceReferenceDescriptions.containsKey(var1[var2].getServiceRefName())) {
            var3 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var3.noDuplicateServiceReferenceDescriptionNamesAllowed(this.getEJBName(), var1[var2].getServiceRefName()));
         }

         if (!this.m_allServiceReferences.containsKey(var1[var2].getServiceRefName())) {
            var3 = new EJBComplianceTextFormatter();
            throw new WLDeploymentException(var3.noServiceRefForReferenceDescription(this.getEJBName(), var1[var2].getServiceRefName()));
         }

         this.m_allServiceReferenceDescriptions.put(var1[var2].getServiceRefName(), var1[var2]);
      }

   }
}
