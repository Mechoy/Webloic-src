package weblogic.jms.dd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSException;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSDeploymentHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.ManagementException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSConstants;
import weblogic.management.utils.BeanListenerCustomizer;
import weblogic.management.utils.GenericBeanListener;

public final class DistributedDestination implements JMSModuleManagedEntity, BeanListenerCustomizer, DDConfig, DDConstants {
   private static final String PHYS_DEST_NAME = "PhysicalDestinationName";
   private int type;
   private HashMap memberName2Listener = new HashMap();
   private HashMap memberName2InteropListener = new HashMap();
   private ArrayList addedMemberNameList;
   private ArrayList removedMemberInformation;
   private String name;
   private String jndiName;
   private String unitOfOrderRouting;
   private boolean isDefaultUnitOfOrder;
   private String loadBalancingPolicy;
   private String forwardingPolicy;
   private int queueForwardDelay;
   private boolean resetDeliveryCount = true;
   private BasicDeploymentMBean deployableApplication;
   private JMSBean module;
   private DistributedDestinationBean specificBean;
   private String EARModuleName;
   private String moduleName;
   private ApplicationContext appCtx;
   private String safExportPolicy;
   private GenericBeanListener distributedDestinationBeanListener;
   private String ddGroupTargetName = null;
   private DDHandler ddHandler;
   private static HashMap groupBeanSignatures = new HashMap();
   private boolean isInterop;

   public DistributedDestination(String var1, JMSBean var2, DistributedDestinationBean var3, BasicDeploymentMBean var4, String var5, String var6, ApplicationContext var7) {
      this.name = var1;
      this.module = var2;
      this.specificBean = var3;
      this.deployableApplication = var4;
      this.EARModuleName = var5;
      this.moduleName = var6;
      this.appCtx = var7;
      this.isInterop = this.moduleName.equals("interop-jms");
      if (this.specificBean instanceof DistributedQueueBean) {
         this.type = 0;
      } else {
         this.type = 1;
      }

      this.ddHandler = new DDHandler(this, true);
   }

   private BasicDeploymentMBean findApplication() {
      if (this.deployableApplication != null) {
         return this.deployableApplication;
      } else {
         this.deployableApplication = JMSModuleHelper.findJMSSystemResource(this.moduleName);
         if (this.deployableApplication != null) {
            return this.deployableApplication;
         } else {
            this.deployableApplication = JMSBeanHelper.getJMSInteropModule();
            return this.deployableApplication;
         }
      }
   }

   private void initializeWithBean() throws ModuleException {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Initializing distributed destination with Bean " + this.specificBean.getName());
      }

      try {
         this.initializeBeanUpdateListeners();
      } catch (JMSException var2) {
         throw new ModuleException(var2.getMessage(), var2);
      }
   }

   public String getApplicationName() {
      return this.appCtx != null ? this.appCtx.getApplicationId() : null;
   }

   private void prepareMembers() throws ModuleException {
      DistributedDestinationMemberBean[] var1 = null;
      if (this.type == 0) {
         var1 = (DistributedDestinationMemberBean[])((DistributedQueueBean)this.specificBean).getDistributedQueueMembers();
      } else if (this.type == 1) {
         var1 = (DistributedDestinationMemberBean[])((DistributedTopicBean)this.specificBean).getDistributedTopicMembers();
      }

      if (var1 != null) {
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("DistributedDestination -- " + var1.length + " members are found in " + this.name);
         }

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (JMSDebug.JMSConfig.isDebugEnabled()) {
               JMSDebug.JMSConfig.debug("Preparing distributed destination member " + var1[var2].getName());
            }

            this.prepareMember(var1[var2], false);
         }
      }

   }

   private synchronized void prepareMember(DistributedDestinationMemberBean var1, boolean var2) throws ModuleException {
      String var3 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      if (this.isInterop && !var1.isSet("PhysicalDestinationName")) {
         if (var2) {
            return;
         }

         var3 = null;
      }

      if (var3 == null) {
         JMSLogger.logJMSDDNullMember(this.name, var1.getName());
      }
   }

   private List fullMemberNames() {
      DistributedDestinationMemberBean[] var1 = this.getMemberBeans();
      ArrayList var2 = new ArrayList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         DistributedDestinationMemberBean var4 = var1[var3];
         String var5 = JMSDeploymentHelper.getMemberName(this.moduleName, var4);
         var2.add(var5);
      }

      return var2;
   }

   private void activateMembers() {
      if (this.ddHandler != null) {
         this.ddHandler.updateMembers(this.fullMemberNames());
      }

      DistributedDestinationMemberBean[] var1 = this.getMemberBeans();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         DistributedDestinationMemberBean var3 = var1[var2];
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Activating distributed destination member " + var3.getName());
         }

         this.activateMember(var3);
      }

   }

   private synchronized void activateMember(DistributedDestinationMemberBean var1) {
      String var2 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      if (this.isInterop) {
         if (this.memberName2InteropListener.get(var1.getName()) == null) {
            InteropMemberHandler var3 = new InteropMemberHandler(var1);
            this.memberName2InteropListener.put(var1.getName(), var3);
         }

         if (!var1.isSet("PhysicalDestinationName")) {
            return;
         }
      }

      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("new member " + var2 + " added.");
      }

      if (this.ddHandler != null) {
         DDMember var5 = this.ddHandler.findMemberByName(var2);
         GenericBeanListener var4 = new GenericBeanListener((DescriptorBean)var1, var5, groupBeanSignatures);
         this.memberName2Listener.put(var2, var4);
         this.ddHandler.setMemberWeight(var2, var1.getWeight());
      }

   }

   private void deactivateMembers() {
      DistributedDestinationMemberBean[] var1 = this.getMemberBeans();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.deactivateMember(var1[var2]);
      }

   }

   private void deactivateMember(DistributedDestinationMemberBean var1) {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Deactivating distributed destination member " + var1.getName());
      }

      InteropMemberHandler var2 = (InteropMemberHandler)this.memberName2InteropListener.get(var1.getName());
      if (var2 != null) {
         var2.close();
      }

      GenericBeanListener var3 = (GenericBeanListener)this.memberName2Listener.get(var1.getName());
      if (var3 != null) {
         var3.close();
      }

   }

   private void unprepareMembers() {
      DistributedDestinationMemberBean[] var1 = this.getMemberBeans();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         DistributedDestinationMemberBean var3 = var1[var2];
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Unpreparing distributed destination member " + var3.getName());
         }

         this.unprepareMember(var3, false);
      }

   }

   private void unprepareMember(DistributedDestinationMemberBean var1, boolean var2) {
      String var3 = var1.getName();
      GenericBeanListener var4 = (GenericBeanListener)this.memberName2Listener.remove(var3);
      if (var4 != null) {
         var4.close();
      }

   }

   public String getName() {
      return this.name;
   }

   public String getEARModuleName() {
      return this.EARModuleName;
   }

   public String getReferenceName() {
      return null;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public ApplicationContext getApplicationContext() {
      return this.appCtx;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(String var1) throws IllegalArgumentException {
      this.jndiName = JMSServerUtilities.transformJNDIName(var1);
      if (this.ddHandler != null) {
         this.ddHandler.setJNDIName(var1);
      }

   }

   public static int translateLoadBalancingPolicy(String var0) {
      byte var1 = 0;
      if (var0 != null) {
         if (var0.equals("Round-Robin")) {
            var1 = 0;
         } else if (var0.equals("Random")) {
            var1 = 1;
         }
      }

      return var1;
   }

   public int getLoadBalancingPolicyAsInt() {
      return translateLoadBalancingPolicy(this.loadBalancingPolicy);
   }

   public String getLoadBalancingPolicy() {
      return this.loadBalancingPolicy;
   }

   public void setLoadBalancingPolicy(String var1) {
      this.loadBalancingPolicy = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setLoadBalancingPolicyAsInt(translateLoadBalancingPolicy(var1));
      }

   }

   public static int translateForwardingPolicy(String var0) {
      byte var1 = 1;
      if (var0 != null) {
         if (var0.equals(JMSConstants.FORWARDING_POLICY_REPLICATED)) {
            var1 = 1;
         } else {
            if (!var0.equals(JMSConstants.FORWARDING_POLICY_PARTITIONED)) {
               throw new IllegalArgumentException("Unrecognized forwarding policy " + var0 + ". Allowed valid values=" + JMSConstants.FORWARDING_POLICY_REPLICATED + "," + JMSConstants.FORWARDING_POLICY_PARTITIONED);
            }

            var1 = 0;
         }
      }

      return var1;
   }

   public int getForwardingPolicyAsInt() {
      return translateForwardingPolicy(this.forwardingPolicy);
   }

   public String getForwardingPolicy() {
      return this.forwardingPolicy;
   }

   public void setForwardingPolicy(String var1) {
      this.forwardingPolicy = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setForwardingPolicy(translateForwardingPolicy(var1));
      }

   }

   public int getForwardDelay() {
      return this.queueForwardDelay;
   }

   public void setForwardDelay(int var1) {
      this.queueForwardDelay = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setForwardDelay(var1);
      }

   }

   public boolean getResetDeliveryCountOnForward() {
      return this.resetDeliveryCount;
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      this.resetDeliveryCount = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setResetDeliveryCountOnForward(var1);
      }

   }

   public int getType() {
      return this.type;
   }

   public DistributedDestinationMemberBean getMemberBean(String var1) {
      if (this.specificBean == null) {
         return null;
      } else if (this.type == 0) {
         DistributedQueueBean var3 = (DistributedQueueBean)this.specificBean;
         return var3.lookupDistributedQueueMember(var1);
      } else {
         DistributedTopicBean var2 = (DistributedTopicBean)this.specificBean;
         return var2.lookupDistributedTopicMember(var1);
      }
   }

   public DistributedDestinationMemberBean[] getMemberBeans() {
      if (this.specificBean == null) {
         return new DistributedDestinationMemberBean[0];
      } else if (this.type == 0) {
         DistributedQueueBean var2 = (DistributedQueueBean)this.specificBean;
         return var2.getDistributedQueueMembers();
      } else {
         DistributedTopicBean var1 = (DistributedTopicBean)this.specificBean;
         return var1.getDistributedTopicMembers();
      }
   }

   private String getMemberJndiName(DistributedDestinationMemberBean var1) {
      String var2 = var1.getPhysicalDestinationName();
      DestinationBean var3 = JMSBeanHelper.findDestinationBean(var2, this.module);
      return var3 == null ? null : var3.getJNDIName();
   }

   private String getMemberLocalJndiName(DistributedDestinationMemberBean var1) {
      String var2 = var1.getPhysicalDestinationName();
      DestinationBean var3 = JMSBeanHelper.findDestinationBean(var2, this.module);
      return var3 == null ? null : var3.getLocalJNDIName();
   }

   public String toString() {
      return "DistributedDestination(name = " + this.name + "; jndiName = " + this.jndiName + ")";
   }

   private final void internalValJndiName(String var1, boolean var2) throws BeanUpdateRejectedException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      if (var1 != null) {
         if (!this.ddHandler.isActive() || this.jndiName == null || !this.jndiName.equals(var1)) {
            Context var3 = JMSService.getContext(!var2);
            Object var4 = null;

            for(int var5 = 0; var5 < 40; ++var5) {
               try {
                  var4 = var3.lookup(var1);
               } catch (NameNotFoundException var7) {
                  return;
               } catch (NamingException var8) {
                  throw new BeanUpdateRejectedException(var8.getMessage(), var8);
               }

               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var9) {
                  break;
               }
            }

            throw new BeanUpdateRejectedException("The proposed " + (var2 ? "Local" : "") + " JNDI name " + var1 + " for destination " + this.name + " is already bound by another object of type " + (var4 == null ? "null" : var4.getClass().getName()));
         }
      }
   }

   private final void valJNDIName(String var1) throws BeanUpdateRejectedException {
      this.internalValJndiName(var1, false);
   }

   public void prepare() throws ModuleException {
      this.initializeWithBean();

      try {
         this.valJNDIName(this.jndiName);
      } catch (BeanUpdateRejectedException var2) {
         throw new ModuleException(var2.getMessage(), var2.getCause());
      }

      if (this.type == 0) {
         this.queueForwardDelay = ((DistributedQueueBean)this.specificBean).getForwardDelay();
         this.setResetDeliveryCountOnForward(((DistributedQueueBean)this.specificBean).getResetDeliveryCountOnForward());
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("DistributedDestination.prepare()  queueForwardDelay " + this.queueForwardDelay + " resetDeliveryCount " + this.resetDeliveryCount);
         }
      }

      this.prepareMembers();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Sucessfully prepared distributed destination: " + this.name);
      }

   }

   public void activate(JMSBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Activating distributed destination: " + this.name);
      }

      if (var1 != null) {
         this.module = var1;
         if (this.specificBean instanceof DistributedQueueBean) {
            this.specificBean = this.module.lookupDistributedQueue(this.getEntityName());
         } else {
            this.specificBean = this.module.lookupDistributedTopic(this.getEntityName());
         }

         this.closeBeanUpdateListeners();
      }

      this.openBeanUpdateListeners();
      if (this.ddHandler != null) {
         this.ddHandler = DDManager.activateOrUpdate(this.ddHandler);
      }

      this.activateMembers();
      JMSLogger.logDDDeployed(this.name);
      DDScheduler.drain();
      Throwable var2 = DDScheduler.waitForComplete();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         if (var2 == null) {
            JMSDebug.JMSModule.debug("Sucessfully activated distributed destination: " + this.name);
         } else {
            JMSDebug.JMSModule.debug("failed to activate distributed destination: " + this.name, var2);
         }
      }

      if (var2 != null) {
         if (var2 instanceof ModuleException) {
            throw (ModuleException)var2;
         } else {
            throw new ModuleException(var2);
         }
      }
   }

   public void deactivate() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Start deactivate distributed destination: " + this.name);
      }

      this.closeBeanUpdateListeners();
      this.deactivateMembers();
      if (this.ddHandler != null) {
         this.ddHandler.deactivate();
      } else if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("ddHandler is null for " + this.name);
      }

      DDScheduler.drain();
      Throwable var1 = DDScheduler.waitForComplete();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         if (var1 == null) {
            JMSDebug.JMSModule.debug("Sucessfully deactivated distributed destination: " + this.name);
         } else {
            JMSDebug.JMSModule.debug("failed to deactivate distributed destination: " + this.name, var1);
         }
      } else if (var1 != null) {
         JMSDebug.JMSModule.debug("ddHandler is null for " + this.name);
         var1.printStackTrace();
      }

      this.ddHandler = null;
   }

   public void unprepare() {
      this.unprepareMembers();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Sucessfully unprepared distributed destination: " + this.name);
      }

   }

   public void remove() {
   }

   public void destroy() {
   }

   public String getEntityName() {
      return this.specificBean.getName();
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) {
   }

   public void activateChangeOfTargets() {
   }

   public void rollbackChangeOfTargets() {
   }

   public String getUnitOfOrderRouting() {
      return this.unitOfOrderRouting;
   }

   public void setUnitOfOrderRouting(String var1) {
      this.unitOfOrderRouting = var1.intern();
      if (this.ddHandler != null) {
         this.ddHandler.setUnitOfOrderRouting(this.unitOfOrderRouting);
      }

   }

   public boolean isDefaultUnitOfOrder() {
      return this.isDefaultUnitOfOrder;
   }

   public void setDefaultUnitOfOrder(boolean var1) {
      this.isDefaultUnitOfOrder = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setDefaultUnitOfOrder(this.isDefaultUnitOfOrder);
      }

   }

   private void addMemberBean(DistributedDestinationMemberBean var1) throws BeanUpdateRejectedException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Preparing distributed destination member for add " + var1.getName());
      }

      try {
         this.prepareMember(var1, true);
      } catch (ModuleException var3) {
         throw new BeanUpdateRejectedException(JMSExceptionLogger.logCannotDynamicallyAddDDMemberLoggable(this.name, var1.getName()).getMessage(), var3);
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Member " + var1.getName() + " is prepared for add ");
      }

   }

   private void removeMemberBean(DistributedDestinationMemberBean var1) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Deactivating distributed destination member for removal" + var1.getName());
      }

      String var2 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      if (var2 != null) {
         this.deactivateMember(var1);
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Member " + var1.getName() + " is deactivated for removal ");
      }

   }

   private void finishAddDistributedDestinationMember(String var1, DistributedDestinationMemberBean var2, boolean var3) {
      if (var1 != null) {
         if (var3) {
            if (this.ddHandler != null) {
               this.ddHandler.updateMembers(this.fullMemberNames());
            }

            this.activateMember(var2);
         } else {
            this.deactivateMember(var2);
         }

      }
   }

   private void finishRemoveDistributedDestinationMember(String var1, DistributedDestinationMemberBean var2, boolean var3) throws BeanUpdateRejectedException {
      if (var1 != null) {
         if (var3) {
            if (this.ddHandler != null) {
               this.ddHandler.updateMembers(this.fullMemberNames());
            }

            this.unprepareMember(var2, true);
         } else {
            try {
               this.prepareMember(var2, true);
            } catch (ModuleException var5) {
               throw new BeanUpdateRejectedException(JMSExceptionLogger.logCannotDynamicallyRemoveDDMemberLoggable(this.name, var1).getMessage(), var5);
            }
         }

      }
   }

   public void startAddDistributedQueueMembers(DistributedDestinationMemberBean var1) throws BeanUpdateRejectedException {
      if (var1 != null) {
         this.addMemberBean(var1);
      }
   }

   public void finishAddDistributedQueueMembers(DistributedDestinationMemberBean var1, boolean var2) {
      this.finishAddDistributedDestinationMember(var1.getPhysicalDestinationName(), var1, var2);
   }

   public void startAddDistributedTopicMembers(DistributedDestinationMemberBean var1) throws BeanUpdateRejectedException {
      if (var1 != null) {
         this.addMemberBean(var1);
      }
   }

   public void finishAddDistributedTopicMembers(DistributedDestinationMemberBean var1, boolean var2) {
      this.finishAddDistributedDestinationMember(var1.getPhysicalDestinationName(), var1, var2);
   }

   public void startRemoveDistributedQueueMembers(DistributedDestinationMemberBean var1) {
      if (var1 != null) {
         this.removeMemberBean(var1);
      }
   }

   public void finishRemoveDistributedQueueMembers(DistributedDestinationMemberBean var1, boolean var2) throws BeanUpdateRejectedException {
      String var3 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      this.finishRemoveDistributedDestinationMember(var3, var1, var2);
   }

   public void startRemoveDistributedTopicMembers(DistributedDestinationMemberBean var1) {
      if (var1 != null) {
         this.removeMemberBean(var1);
      }
   }

   public void finishRemoveDistributedTopicMembers(DistributedDestinationMemberBean var1, boolean var2) throws BeanUpdateRejectedException {
      String var3 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      this.finishRemoveDistributedDestinationMember(var3, var1, var2);
   }

   private void initializeBeanUpdateListeners() throws JMSException {
      DescriptorBean var1 = (DescriptorBean)this.specificBean;
      if (this.type == 0) {
         this.distributedDestinationBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.distributedQueueBeanSignatures, JMSBeanHelper.distributedQueueAdditionSignatures, false);
      } else {
         this.distributedDestinationBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.distributedTopicBeanSignatures, JMSBeanHelper.distributedTopicAdditionSignatures, false);
      }

      try {
         this.distributedDestinationBeanListener.initialize();
      } catch (ManagementException var3) {
         throw new JMSException(var3);
      }

      this.distributedDestinationBeanListener.setCustomizer(this);
   }

   private void openBeanUpdateListeners() {
      DescriptorBean var1 = (DescriptorBean)this.specificBean;
      if (this.distributedDestinationBeanListener != null) {
         this.distributedDestinationBeanListener.open();
      } else {
         if (this.type == 0) {
            this.distributedDestinationBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.distributedQueueBeanSignatures, JMSBeanHelper.distributedQueueAdditionSignatures);
         } else {
            this.distributedDestinationBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.distributedTopicBeanSignatures, JMSBeanHelper.distributedTopicAdditionSignatures);
         }

         this.distributedDestinationBeanListener.setCustomizer(this);
      }

   }

   private void closeBeanUpdateListeners() {
      if (this.distributedDestinationBeanListener != null) {
         this.distributedDestinationBeanListener.close();
         this.distributedDestinationBeanListener = null;
      }

   }

   public void activateFinished() {
   }

   public DistributedDestinationMemberBean lookupDistributedQueueMember(String var1) {
      return null;
   }

   public DistributedDestinationMemberBean lookupDistributedTopicMember(String var1) {
      return null;
   }

   public String getSAFExportPolicy() {
      return this.safExportPolicy;
   }

   public void setSAFExportPolicy(String var1) {
      if (var1 == null) {
         this.safExportPolicy = "All";
      } else {
         this.safExportPolicy = var1;
      }

      if (this.ddHandler != null) {
         this.ddHandler.setSAFExportPolicy(this.safExportPolicy);
      }

   }

   static {
      groupBeanSignatures.put("Weight", Integer.TYPE);
   }

   private class InteropMemberHandler implements BeanUpdateListener {
      DistributedDestinationMemberBean memberBean;
      boolean physicalDestinationNameSet;

      InteropMemberHandler(DistributedDestinationMemberBean var2) {
         this.memberBean = var2;
         this.physicalDestinationNameSet = this.memberBean.isSet("PhysicalDestinationName");
         ((DescriptorBean)this.memberBean).addBeanUpdateListener(this);
      }

      private void close() {
         ((DescriptorBean)this.memberBean).removeBeanUpdateListener(this);
      }

      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         DistributedDestinationMemberBean var2 = (DistributedDestinationMemberBean)var1.getProposedBean();
         BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            BeanUpdateEvent.PropertyUpdate var5 = var3[var4];
            String var6 = var5.getPropertyName();
            if ("PhysicalDestinationName".equals(var6)) {
               if (this.physicalDestinationNameSet) {
                  throw new BeanUpdateRejectedException("An attempt was made to change the physical destination from " + this.memberBean.getPhysicalDestinationName() + " to " + var2.getPhysicalDestinationName() + ".  This is not allowed.  The physical" + " destination may only be changed from an unset state to a particular physical destination");
               }

               DistributedDestination.this.addMemberBean(var2);
            }
         }

      }

      public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
         BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            BeanUpdateEvent.PropertyUpdate var4 = var2[var3];
            String var5 = var4.getPropertyName();
            if ("PhysicalDestinationName".equals(var5)) {
               this.physicalDestinationNameSet = true;
               DistributedDestination.this.finishAddDistributedDestinationMember(this.memberBean.getPhysicalDestinationName(), this.memberBean, true);
            }
         }

      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
         DistributedDestinationMemberBean var2 = (DistributedDestinationMemberBean)var1.getProposedBean();
         BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            BeanUpdateEvent.PropertyUpdate var5 = var3[var4];
            String var6 = var5.getPropertyName();
            if ("PhysicalDestinationName".equals(var6)) {
               DistributedDestination.this.finishAddDistributedDestinationMember(var2.getPhysicalDestinationName(), var2, false);
            }
         }

      }
   }
}
