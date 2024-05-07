package weblogic.jms.backend.udd;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEDestinationRuntimeDelegate;
import weblogic.jms.backend.BETopicImpl;
import weblogic.jms.backend.DestinationEntityProvider;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSTargetsListener;
import weblogic.jms.dd.UniformDistributedDestination;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.module.TargetListSave;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JMSServerRuntimeMBean;
import weblogic.management.utils.GenericBeanListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class UDDEntity implements JMSModuleManagedEntity, JMSTargetsListener {
   private static LinkedList instances = new LinkedList();
   private static HashMap localJMSServers = new HashMap();
   private HashMap destinations = new HashMap();
   private HashMap destinationBeans = new HashMap();
   private UniformDistributedDestination udd;
   private String name;
   private UniformDistributedDestinationBean uDestBean;
   private SyntheticDDBean ddBean;
   private boolean isQueue;
   private ApplicationContextInternal appCtx;
   private String moduleName;
   private String earModuleName;
   private Context namingContext;
   private JMSBean wholeModule;
   private static final HashMap targetSignatures = new HashMap();
   private static final HashMap uDQueueBeanSignatures = new HashMap();
   private static final HashMap uDTopicBeanSignatures = new HashMap();
   private List lotsOfListeners = new LinkedList();
   private HashMap activeTargetedServers = new HashMap();
   private HashMap preparedTargetedServers = new HashMap();
   private LinkedList addedLocalDestinations = null;
   private LinkedList preparedForRemovalTargetedServers = new LinkedList();
   private SyntheticJMSBean fakeJMSBean;
   private boolean reconciled = false;
   private TargetListSave savedTargets = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private String prTargets(List var1) {
      ListIterator var2 = var1.listIterator();

      String var3;
      for(var3 = ""; var2.hasNext(); var3 = var3 + ((TargetMBean)var2.next()).getName()) {
         if (var3.length() != 0) {
            var3 = var3 + ", ";
         }
      }

      return var3;
   }

   private void fillWithMyTargets(DomainMBean var1, HashMap var2, List var3) {
      if (var3 == null) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("fillWithMyTargets called for " + this.name + ": targets is null");
         }

      } else {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("fillWithMyTargets called for " + this.name + ": targets are: " + this.prTargets(var3));
         }

         JMSModuleHelper.uddFillWithMyTargets(var2, var1, (TargetMBean[])((TargetMBean[])((TargetMBean[])var3.toArray(new TargetMBean[0]))));
      }
   }

   public JMSBean getJMSModuleBean() {
      return this.wholeModule;
   }

   public UDDEntity(String var1, ApplicationContext var2, String var3, String var4, Context var5, JMSBean var6, NamedEntityBean var7, List var8, DomainMBean var9) throws ModuleException {
      this.name = var1;
      this.uDestBean = (UniformDistributedDestinationBean)var7;
      this.appCtx = (ApplicationContextInternal)var2;
      this.moduleName = var3;
      this.earModuleName = var4;
      this.namingContext = var5;
      this.wholeModule = var6;
      this.isQueue = this.uDestBean instanceof UniformDistributedQueueBean;
      this.fakeJMSBean = new SyntheticJMSBean(this);
      if (var8 != null && var8.size() == 0) {
         var8 = null;
      } else if (var8 != null && JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Constructor called for " + this.name + ": targets are: " + this.prTargets(var8));
      }

      this.savedTargets = new TargetListSave(var8);
      this.makeDD(var9, var8);
   }

   private void makeDestination(String var1, SyntheticDestinationBean var2) throws BeanUpdateRejectedException {
      EntityName var4 = new EntityName(this.appCtx.getApplicationId(), this.earModuleName, var2.getName());

      JMSModuleManagedEntity var3;
      try {
         var3 = (new DestinationEntityProvider()).createEntity(this.appCtx, var4, this.namingContext, this.fakeJMSBean, var2, (List)null, (DomainMBean)null);
      } catch (ModuleException var6) {
         throw new BeanUpdateRejectedException("Failed to create Destination", var6);
      }

      this.destinations.put(var1, var3);
   }

   private SyntheticMemberBean addMember(DomainMBean var1, String var2) throws BeanUpdateRejectedException {
      SyntheticMemberBean var3 = this.ddBean.findMemberBean(var2);
      if (var3 != null && this.destinations.containsKey(var2)) {
         return var3;
      } else {
         if (var3 == null) {
            var3 = this.ddBean.addMember(var2);
         }

         Object var4;
         if (this.isQueue) {
            var4 = new SyntheticQueueBean(this, var2);
         } else {
            var4 = new SyntheticTopicBean(this, var2);
         }

         this.destinationBeans.put(JMSModuleHelper.uddMakeName(var2, this.name), var4);
         if (isJMSServerLocal(var2)) {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Making destination for " + this.name + " from jms server name: " + var2 + "; it has a JNDI name of " + ((SyntheticDestinationBean)var4).getJNDIName());
            }

            this.makeDestination(var2, (SyntheticDestinationBean)var4);
         }

         return var3;
      }
   }

   private void removeMember(String var1) {
      this.ddBean.removeMember(var1);
      this.destinationBeans.remove(JMSModuleHelper.uddMakeName(var1, this.name));
      this.destinations.remove(var1);
   }

   boolean isQueue() {
      return this.isQueue;
   }

   private void makeDD(DomainMBean var1, List var2) throws ModuleException {
      if (var1 == null) {
         if (var2 != null) {
            var1 = JMSBeanHelper.getDomain((TargetMBean)var2.get(0));
         } else {
            ManagementService.getRuntimeAccess(kernelId).getDomain();
         }
      }

      if (this.isQueue) {
         this.ddBean = new SyntheticDQBean(this);
      } else {
         this.ddBean = new SyntheticDTBean(this);
      }

      this.fillWithMyTargets(var1, this.preparedTargetedServers, var2);
      Iterator var3 = this.preparedTargetedServers.values().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         try {
            this.addMember(var1, var4);
         } catch (BeanUpdateRejectedException var6) {
            throw new ModuleException("Could not create Uniform Distributed Destination", var6);
         }
      }

      this.udd = new UniformDistributedDestination(this.name, this.fakeJMSBean, this.ddBean, this.earModuleName, this.moduleName, this.appCtx);
   }

   private static boolean isJMSServerLocal(String var0) {
      return localJMSServers.get(var0) != null;
   }

   public static Map getLocalJMSServers() {
      return localJMSServers;
   }

   private Iterator allDestinationsIterator() {
      return this.destinations.values().iterator();
   }

   private Iterator makeDestinationsIterator(HashMap var1) {
      LinkedList var2 = new LinkedList();
      if (var1 != null) {
         Iterator var3 = var1.values().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            JMSModuleManagedEntity var5 = (JMSModuleManagedEntity)this.destinations.get(var4);
            if (var5 != null) {
               var2.add(this.destinations.get(var4));
            }
         }
      }

      return var2.listIterator();
   }

   private Iterator preparedDestinationsIterator() {
      return this.makeDestinationsIterator(this.preparedTargetedServers);
   }

   private Iterator activeDestinationsIterator() {
      return this.makeDestinationsIterator(this.activeTargetedServers);
   }

   private JMSModuleManagedEntity findDestinationEntity(String var1) {
      return (JMSModuleManagedEntity)this.destinations.get(var1);
   }

   public void activate(JMSBean var1) throws ModuleException {
      this.wholeModule = var1;
      if (this.uDestBean instanceof UniformDistributedQueueBean) {
         this.uDestBean = this.wholeModule.lookupUniformDistributedQueue(this.getEntityName());
      } else {
         this.uDestBean = this.wholeModule.lookupUniformDistributedTopic(this.getEntityName());
      }

      Iterator var2 = this.preparedDestinationsIterator();

      while(var2.hasNext()) {
         JMSModuleManagedEntity var3 = (JMSModuleManagedEntity)var2.next();
         var3.activate((JMSBean)null);
      }

      if (this.udd != null) {
         this.udd.activate((JMSBean)null);
      }

      this.activeTargetedServers = this.preparedTargetedServers;
      this.preparedTargetedServers = new HashMap();
      this.unregisterBeanUpdateListeners();
      this.registerBeanUpdateListeners();
   }

   private void registerBeanUpdateListeners() {
      JMSService.getJMSService().addJMSServerListener(this);
      if (this.isQueue) {
         this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean, this, uDQueueBeanSignatures, (Map)null));
      } else {
         this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean, this, uDTopicBeanSignatures, (Map)null));
         this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)((UniformDistributedTopicBean)this.uDestBean).getMulticast(), this, JMSBeanHelper.multicastBeanSignatures, (Map)null));
      }

      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean.getThresholds(), this, JMSBeanHelper.thresholdBeanSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean.getDeliveryParamsOverrides(), this, JMSBeanHelper.deliveryOverridesSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean.getDeliveryFailureParams(), this, JMSBeanHelper.deliveryFailureSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean.getDeliveryFailureParams(), this, JMSBeanHelper.localDeliveryFailureSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean.getMessageLoggingParams(), this, JMSBeanHelper.messageLoggingSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.uDestBean, this, JMSBeanHelper.localDestinationBeanSignatures, (Map)null));
   }

   private void unregisterBeanUpdateListeners() {
      JMSService.getJMSService().removeJMSServerListener(this);
      ListIterator var1 = this.lotsOfListeners.listIterator();

      while(var1.hasNext()) {
         ((GenericBeanListener)var1.next()).close();
      }

      this.lotsOfListeners.clear();
   }

   public void deactivate() throws ModuleException {
      this.unregisterBeanUpdateListeners();
      Iterator var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         JMSModuleManagedEntity var2 = (JMSModuleManagedEntity)var1.next();
         var2.deactivate();
      }

      if (this.udd != null) {
         this.udd.deactivate();
      }

   }

   public void destroy() throws ModuleException {
      if (this.udd != null) {
         this.udd.destroy();
      }

      Iterator var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         JMSModuleManagedEntity var2 = (JMSModuleManagedEntity)var1.next();
         var2.destroy();
      }

   }

   public String getEntityName() {
      return this.uDestBean.getName();
   }

   public String getName() {
      return this.name;
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) throws ModuleException {
      if (var2 == null) {
         throw new AssertionError("Cannot find domain!");
      } else {
         this.savedTargets = new TargetListSave(var1);

         try {
            this.reconcileTargets(var2);
         } catch (BeanUpdateRejectedException var4) {
            throw new ModuleException("Rejected targeting change", var4);
         }
      }
   }

   public void activateChangeOfTargets() throws ModuleException {
      try {
         this.activateTargetUpdates(true);
      } catch (BeanUpdateRejectedException var2) {
         throw new ModuleException("Rejected targeting change", var2);
      }
   }

   public void rollbackChangeOfTargets() {
      try {
         this.activateTargetUpdates(false);
      } catch (BeanUpdateRejectedException var2) {
         throw new AssertionError("Rejected targeting change" + var2);
      }
   }

   public void prepare() throws ModuleException {
      if (!instances.contains(this)) {
         instances.add(this);
         Iterator var1 = this.preparedDestinationsIterator();

         while(var1.hasNext()) {
            JMSModuleManagedEntity var2 = (JMSModuleManagedEntity)var1.next();
            var2.prepare();
         }

         if (this.udd != null) {
            this.udd.prepare();
         }

      }
   }

   public void remove() throws ModuleException {
      Iterator var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         JMSModuleManagedEntity var2 = (JMSModuleManagedEntity)var1.next();
         var2.remove();
      }

      if (this.udd != null) {
         this.udd.remove();
      }

   }

   public void unprepare() throws ModuleException {
      Iterator var1 = this.preparedDestinationsIterator();

      JMSModuleManagedEntity var2;
      while(var1.hasNext()) {
         var2 = (JMSModuleManagedEntity)var1.next();
         var2.unprepare();
      }

      var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         var2 = (JMSModuleManagedEntity)var1.next();
         var2.unprepare();
      }

      instances.remove(this);
      if (this.udd != null) {
         this.udd.unprepare();
      }

   }

   public void setIncompleteWorkExpirationTime(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setIncompleteWorkExpirationTime(var1);
      }

   }

   public void setUnitOfWorkHandlingPolicy(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setUnitOfWorkHandlingPolicy(var1);
      }

   }

   public void setAttachSender(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setAttachSender(var1);
      }

   }

   public void setConsumptionPausedAtStartup(boolean var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setConsumptionPausedAtStartup(var1);
      }

   }

   public QueueBean[] getQueues() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.destinationBeans.values().iterator();

      while(var2.hasNext()) {
         SyntheticDestinationBean var3 = (SyntheticDestinationBean)var2.next();
         if (var3 instanceof SyntheticQueueBean) {
            var1.add(var3);
         }
      }

      return (QueueBean[])((QueueBean[])var1.toArray(new QueueBean[0]));
   }

   public TopicBean[] getTopics() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.destinationBeans.values().iterator();

      while(var2.hasNext()) {
         SyntheticDestinationBean var3 = (SyntheticDestinationBean)var2.next();
         if (var3 instanceof SyntheticTopicBean) {
            var1.add(var3);
         }
      }

      return (TopicBean[])((TopicBean[])var1.toArray(new TopicBean[0]));
   }

   public TopicBean lookupTopic(String var1) {
      Object var2 = this.destinationBeans.get(var1);
      return !(var2 instanceof TopicBean) ? null : (TopicBean)var2;
   }

   public QueueBean lookupQueue(String var1) {
      Object var2 = this.destinationBeans.get(var1);
      return !(var2 instanceof QueueBean) ? null : (QueueBean)var2;
   }

   public void setDestinationKeys(String[] var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setDestinationKeys(var1);
      }

   }

   public void setJMSCreateDestinationIdentifier(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         if (var1 == null) {
            var3.getManagedDestination().setJMSCreateDestinationIdentifier((String)null);
         } else {
            String var4 = JMSModuleHelper.uddMakeName(this.name, var1);
            var3.getManagedDestination().setJMSCreateDestinationIdentifier(var4);
         }
      }

   }

   public void setInsertionPausedAtStartup(boolean var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setInsertionPausedAtStartup(var1);
      }

   }

   public void setJNDIName(String var1) {
      this.udd.setJNDIName(var1);
      JMSServerRuntimeMBean[] var2 = JMSService.getJMSService().getJMSServers();
      JMSServerRuntimeMBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JMSServerRuntimeMBean var6 = var3[var5];
         String var7 = var6.getName();
         if (this.activeTargetedServers.containsKey(var7)) {
            JMSModuleManagedEntity var8 = this.findDestinationEntity(var7);
            BEDestinationRuntimeDelegate var9 = (BEDestinationRuntimeDelegate)var8;
            String var10;
            if (var1 == null) {
               var10 = null;
            } else {
               var10 = JMSModuleHelper.uddMakeName(var7, var1);
            }

            if (var9 != null) {
               var9.getManagedDestination().setJNDIName(var10);
            }
         }
      }

   }

   UniformDistributedDestinationBean getUDestBean() {
      return this.uDestBean;
   }

   SyntheticDDBean getDDBean() {
      return this.ddBean;
   }

   public void setLocalJNDIName(String var1) {
      BEDestinationRuntimeDelegate var5;
      String var6;
      for(Iterator var2 = this.activeTargetedServers.values().iterator(); var2.hasNext(); var5.getManagedDestination().setJNDIName(var6)) {
         String var3 = (String)var2.next();
         JMSModuleManagedEntity var4 = this.findDestinationEntity(var3);
         var5 = (BEDestinationRuntimeDelegate)var4;
         if (var1 == null) {
            var6 = null;
         } else {
            var6 = JMSModuleHelper.uddMakeName(var3, var1);
         }
      }

   }

   public void setMaximumMessageSize(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setMaximumMessageSize(var1);
      }

   }

   public void setMessagingPerformancePreference(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setMessagingPerformancePreference(var1);
      }

   }

   public void setProductionPausedAtStartup(boolean var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setProductionPausedAtStartup(var1);
      }

   }

   public void setQuota(QuotaBean var1) throws BeanUpdateFailedException {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.setQuota(var1);
      }

   }

   public void setTemplate(String var1) {
   }

   public void setUnitOfOrderRouting(String var1) {
      this.udd.setUnitOfOrderRouting(var1);
      this.udd.activateFinished();
   }

   public void setSubDeploymentName(String var1) throws BeanUpdateRejectedException {
      this.reconcileTargets(ManagementService.getRuntimeAccess(kernelId).getDomain());
      this.activateTargetUpdates(true);
   }

   public void setForwardDelay(int var1) {
      this.udd.setForwardDelay(var1);
      this.udd.activateFinished();
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      this.udd.setResetDeliveryCountOnForward(var1);
      this.udd.activateFinished();
   }

   public void setLoadBalancingPolicy(String var1) {
      this.udd.setLoadBalancingPolicy(var1);
      this.udd.activateFinished();
   }

   public String getForwardingPolicy() {
      return this.udd.getForwardingPolicy();
   }

   public void setForwardingPolicy(String var1) {
      this.udd.setForwardingPolicy(var1);
      this.udd.activateFinished();
   }

   public void setBytesHigh(long var1) {
      Iterator var3 = this.allDestinationsIterator();

      while(var3.hasNext()) {
         BEDestinationRuntimeDelegate var4 = (BEDestinationRuntimeDelegate)var3.next();
         var4.getManagedDestination().setBytesHigh(var1);
      }

   }

   public void setBytesLow(long var1) {
      Iterator var3 = this.allDestinationsIterator();

      while(var3.hasNext()) {
         BEDestinationRuntimeDelegate var4 = (BEDestinationRuntimeDelegate)var3.next();
         var4.getManagedDestination().setBytesLow(var1);
      }

   }

   public void setMessagesHigh(long var1) {
      Iterator var3 = this.allDestinationsIterator();

      while(var3.hasNext()) {
         BEDestinationRuntimeDelegate var4 = (BEDestinationRuntimeDelegate)var3.next();
         var4.getManagedDestination().setMessagesHigh(var1);
      }

   }

   public void setMessagesLow(long var1) {
      Iterator var3 = this.allDestinationsIterator();

      while(var3.hasNext()) {
         BEDestinationRuntimeDelegate var4 = (BEDestinationRuntimeDelegate)var3.next();
         var4.getManagedDestination().setMessagesLow(var1);
      }

   }

   public void setMessageLoggingEnabled(boolean var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setMessageLoggingEnabled(var1);
      }

   }

   public void setMessageLoggingFormat(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setMessageLoggingFormat(var1);
      }

   }

   public void setDeliveryMode(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setDeliveryMode(var1);
      }

   }

   public void setTimeToDeliver(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setTimeToDeliver(var1);
      }

   }

   public void setTimeToLive(long var1) {
      Iterator var3 = this.allDestinationsIterator();

      while(var3.hasNext()) {
         BEDestinationRuntimeDelegate var4 = (BEDestinationRuntimeDelegate)var3.next();
         var4.getManagedDestination().setTimeToLive(var1);
      }

   }

   public void setPriority(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setPriority(var1);
      }

   }

   public void setRedeliveryDelay(long var1) {
      Iterator var3 = this.allDestinationsIterator();

      while(var3.hasNext()) {
         BEDestinationRuntimeDelegate var4 = (BEDestinationRuntimeDelegate)var3.next();
         var4.getManagedDestination().setRedeliveryDelay(var1);
      }

   }

   public void setRedeliveryLimit(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setRedeliveryLimit(var1);
      }

   }

   public void setErrorDestination(DestinationBean var1) {
      assert var1 instanceof UniformDistributedDestinationBean;

      JMSServerRuntimeMBean[] var2 = JMSService.getJMSService().getJMSServers();
      JMSServerRuntimeMBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JMSServerRuntimeMBean var6 = var3[var5];
         String var7 = var6.getName();
         if (this.activeTargetedServers.containsKey(var7)) {
            JMSModuleManagedEntity var8 = this.findDestinationEntity(var7);
            BEDestinationRuntimeDelegate var9 = (BEDestinationRuntimeDelegate)var8;
            SyntheticDestinationBean var10 = (SyntheticDestinationBean)this.destinationBeans.get(JMSModuleHelper.uddMakeName(var7, this.name));
            if (var8 != null && var10 != null) {
               var9.setErrorDestination(var10.getErrorDestination());
            }
         }
      }

   }

   public void setExpirationPolicy(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setExpirationPolicy(var1);
      }

   }

   public void setExpirationLoggingPolicy(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setExpirationLoggingPolicy(var1);
      }

   }

   public void setMulticastAddress(String var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         ((BETopicImpl)var3.getManagedDestination()).setMulticastAddress(var1);
      }

   }

   public void setMulticastPort(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         ((BETopicImpl)var3.getManagedDestination()).setMulticastPort(var1);
      }

   }

   public void setMulticastTimeToLive(int var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         ((BETopicImpl)var3.getManagedDestination()).setMulticastTimeToLive(var1);
      }

   }

   public void setSAFExportPolicy(String var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         ((BETopicImpl)var3.getManagedDestination()).setSAFExportPolicy(var1);
      }

   }

   public void setDefaultTargetingEnabled(boolean var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setDefaultTargetingEnabled(var1);
      }

   }

   public static void broadcastChangeToAllUDDs(DomainMBean var0, boolean var1) throws DeploymentException {
      boolean var2 = false;
      UDDEntity var3 = null;

      try {
         ListIterator var4 = instances.listIterator();

         while(var4.hasNext()) {
            var3 = (UDDEntity)var4.next();

            try {
               if (var1) {
                  var3.reconcileTargets(var0);
               } else {
                  var3.activateTargetUpdates(true);
               }
            } catch (BeanUpdateRejectedException var16) {
               throw new DeploymentException("Rejected bean update", var16);
            }
         }

         var2 = true;
      } finally {
         if (!var2 && var1) {
            ListIterator var8 = instances.listIterator();

            while(var8.hasNext()) {
               UDDEntity var9 = (UDDEntity)var8.next();
               if (var9 == var3) {
                  break;
               }

               try {
                  var9.activateTargetUpdates(false);
               } catch (BeanUpdateRejectedException var15) {
               }
            }
         }

      }

   }

   public static void prepareLocalJMSServer(JMSServerMBean var0) throws DeploymentException {
      localJMSServers.put(var0.getName(), var0.getName());
      broadcastChangeToAllUDDs(JMSBeanHelper.getDomain(var0), true);
   }

   public static void activateLocalJMSServer(JMSServerMBean var0) throws DeploymentException {
      localJMSServers.put(var0.getName(), var0.getName());
      broadcastChangeToAllUDDs(JMSBeanHelper.getDomain(var0), true);
      broadcastChangeToAllUDDs(JMSBeanHelper.getDomain(var0), false);
   }

   public static void deactivateLocalJMSServer(JMSServerMBean var0) {
      localJMSServers.remove(var0.getName());

      try {
         broadcastChangeToAllUDDs(JMSBeanHelper.getDomain(var0), true);
         broadcastChangeToAllUDDs(JMSBeanHelper.getDomain(var0), false);
      } catch (DeploymentException var2) {
      }

   }

   public static void unprepareLocalJMSServer(JMSServerMBean var0) {
   }

   private void reconcileAddedLocalDestinations(DomainMBean var1, HashMap var2) throws BeanUpdateRejectedException {
      this.addedLocalDestinations = new LinkedList();
      Iterator var3 = var2.values().iterator();
      LinkedList var4 = new LinkedList();
      new LinkedList();
      boolean var6 = false;

      try {
         while(var3.hasNext()) {
            String var7 = (String)var3.next();
            if (!this.destinations.containsKey(var7) && isJMSServerLocal(var7)) {
               this.addedLocalDestinations.add(var7);
               this.makeDestination(var7, (SyntheticDestinationBean)this.destinationBeans.get(JMSModuleHelper.uddMakeName(var7, this.name)));
               JMSModuleManagedEntity var8 = this.findDestinationEntity(var7);

               try {
                  var8.prepare();
               } catch (ModuleException var19) {
                  throw new BeanUpdateRejectedException("Cannot prepare destination; ", var19);
               }
            }
         }

         var6 = true;
      } finally {
         if (!var6) {
            ListIterator var22 = var4.listIterator();

            while(var22.hasNext()) {
               JMSModuleManagedEntity var12 = (JMSModuleManagedEntity)var22.next();

               try {
                  var12.unprepare();
               } catch (ModuleException var18) {
               }
            }

            var22 = this.addedLocalDestinations.listIterator();

            while(var22.hasNext()) {
               String var21 = (String)var22.next();
               this.destinations.remove(var21);
            }

            this.addedLocalDestinations = null;
         }

      }

   }

   private void reconcileAddedMembers(DomainMBean var1, HashMap var2) throws BeanUpdateRejectedException {
      if (this.preparedTargetedServers == null) {
         this.preparedTargetedServers = new HashMap();
      }

      Iterator var3 = var2.values().iterator();
      LinkedList var4 = new LinkedList();
      boolean var5 = false;

      try {
         while(var3.hasNext()) {
            String var6 = (String)var3.next();
            if (!this.activeTargetedServers.containsKey(var6)) {
               this.addMember(var1, var6);
               JMSModuleManagedEntity var8 = this.findDestinationEntity(var6);
               if (var8 != null && !this.preparedTargetedServers.containsKey(var6)) {
                  try {
                     var8.prepare();
                     var4.add(var6);
                     this.preparedTargetedServers.put(var6, var6);
                  } catch (ModuleException var20) {
                     throw new BeanUpdateRejectedException("Cannot prepare destination; ", var20);
                  }
               }
            }
         }

         var5 = true;
      } finally {
         if (!var5) {
            ListIterator var22 = var4.listIterator();

            while(var22.hasNext()) {
               String var12 = (String)var22.next();
               JMSModuleManagedEntity var13 = this.findDestinationEntity(var12);

               try {
                  var13.unprepare();
                  this.removeMember(var12);
                  this.preparedTargetedServers.remove(var12);
               } catch (ModuleException var19) {
               }
            }
         }

      }

   }

   private void reconcileSubtractedMembers(HashMap var1) {
      this.preparedForRemovalTargetedServers = new LinkedList();
      Iterator var2 = this.activeTargetedServers.values().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!var1.containsKey(var3)) {
            this.ddBean.findMemberBean(var3);
            this.preparedForRemovalTargetedServers.add(var3);
         }
      }

   }

   private void reconcileTargets(DomainMBean var1) throws BeanUpdateRejectedException {
      HashMap var2 = new HashMap();
      if (!this.reconciled) {
         this.reconciled = true;
         this.fillWithMyTargets(var1, var2, this.savedTargets.restoreTargets(var1));
         this.reconcileAddedMembers(var1, var2);
         this.reconcileAddedLocalDestinations(var1, var2);
         this.reconcileSubtractedMembers(var2);
      }
   }

   private void activateAddedLocalDestinations(boolean var1) throws BeanUpdateRejectedException {
      if (this.addedLocalDestinations != null) {
         LinkedList var2 = new LinkedList();

         try {
            ListIterator var3 = this.addedLocalDestinations.listIterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               if (!var1) {
                  this.destinations.remove(var4);
               } else {
                  JMSModuleManagedEntity var5 = this.findDestinationEntity(var4);

                  try {
                     var5.activate((JMSBean)null);
                     var2.add(var5);
                  } catch (ModuleException var17) {
                     throw new BeanUpdateRejectedException("activate failed", var17);
                  }
               }
            }

            this.addedLocalDestinations = null;
         } finally {
            if (this.addedLocalDestinations != null) {
               ListIterator var9 = var2.listIterator();

               while(var9.hasNext()) {
                  JMSModuleManagedEntity var10 = (JMSModuleManagedEntity)var9.next();

                  try {
                     var10.deactivate();
                  } catch (ModuleException var16) {
                  }
               }
            }

         }
      }
   }

   private void activateAddedMembers(boolean var1) throws BeanUpdateRejectedException {
      if (this.preparedTargetedServers != null) {
         LinkedList var2 = new LinkedList();

         try {
            Iterator var3 = this.preparedTargetedServers.values().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               SyntheticMemberBean var5 = this.ddBean.findMemberBean(var4);
               JMSModuleManagedEntity var6 = this.findDestinationEntity(var4);
               if (!var1) {
                  this.removeMember(var4);
                  if (var6 != null) {
                     try {
                        var6.unprepare();
                     } catch (ModuleException var20) {
                        throw new AssertionError("Unprepare failed");
                     }
                  }
               } else {
                  if (var6 != null) {
                     try {
                        var6.activate((JMSBean)null);
                        var2.add(var6);
                     } catch (ModuleException var19) {
                        throw new BeanUpdateRejectedException("activate failed", var19);
                     }
                  }

                  this.activeTargetedServers.put(var4, var4);
               }

               if (this.isQueue) {
                  this.udd.finishAddDistributedQueueMembers(var5, var1);
               } else {
                  this.udd.finishAddDistributedTopicMembers(var5, var1);
               }
            }

            this.preparedTargetedServers = null;
         } finally {
            if (this.preparedTargetedServers != null) {
               ListIterator var10 = var2.listIterator();

               while(var10.hasNext()) {
                  JMSModuleManagedEntity var11 = (JMSModuleManagedEntity)var10.next();

                  try {
                     var11.deactivate();
                  } catch (ModuleException var18) {
                  }
               }
            }

         }

      }
   }

   private void activateSubtractedMembers(boolean var1) throws BeanUpdateRejectedException {
      if (this.preparedForRemovalTargetedServers != null) {
         ListIterator var2 = this.preparedForRemovalTargetedServers.listIterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            SyntheticMemberBean var4 = this.ddBean.findMemberBean(var3);
            if (var1) {
               JMSModuleManagedEntity var5 = this.findDestinationEntity(var3);
               this.removeMember(var3);
               if (var5 != null) {
                  try {
                     var5.deactivate();
                     var5.destroy();
                     DomainMBean var6 = ManagementService.getRuntimeAccess(kernelId).getDomain();
                     if (var6.lookupJMSServer(var3) == null) {
                        var5.remove();
                     }

                     var5.unprepare();
                  } catch (ModuleException var7) {
                     throw new BeanUpdateRejectedException("Cannot bring down UDD member for " + var3, var7);
                  }
               }

               this.activeTargetedServers.remove(var3);
            }

            if (this.isQueue) {
               this.udd.finishRemoveDistributedQueueMembers(var4, var1);
            } else {
               this.udd.finishRemoveDistributedTopicMembers(var4, var1);
            }
         }

         this.preparedForRemovalTargetedServers = null;
      }
   }

   private void activateSubtractedLocalDestinations() throws BeanUpdateRejectedException {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      Iterator var2 = this.destinations.keySet().iterator();

      while(true) {
         String var3;
         JMSModuleManagedEntity var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (String)var2.next();
            var4 = this.findDestinationEntity(var3);
         } while(isJMSServerLocal(var3));

         try {
            var4.deactivate();
            var4.destroy();
            if (var1.lookupJMSServer(var3) == null) {
               var4.remove();
            }

            var4.unprepare();
         } catch (ModuleException var6) {
            throw new BeanUpdateRejectedException("destroy failed", var6);
         }

         var2.remove();
      }
   }

   private void activateTargetUpdates(boolean var1) throws BeanUpdateRejectedException {
      if (this.reconciled) {
         this.activateAddedLocalDestinations(var1);
         this.activateAddedMembers(var1);
         this.activateSubtractedMembers(var1);
         this.activateSubtractedLocalDestinations();
         this.udd.activateFinished();
         this.reconciled = false;
      }

   }

   private boolean onlyRemoteNewJMSServers(DomainMBean var1) {
      HashMap var2 = new HashMap();
      this.fillWithMyTargets(var1, var2, this.savedTargets.restoreTargets(var1));
      boolean var3 = false;
      Iterator var4 = var2.values().iterator();

      String var5;
      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (!this.activeTargetedServers.containsKey(var5)) {
            var3 = true;
            if (isJMSServerLocal(var5)) {
               return false;
            }
         }
      }

      var4 = this.activeTargetedServers.values().iterator();

      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (!var2.containsKey(var5)) {
            var3 = true;
         }
      }

      return var3;
   }

   public void prepareUpdate(DomainMBean var1, TargetMBean var2, int var3, boolean var4) throws BeanUpdateRejectedException {
      if (this.onlyRemoteNewJMSServers(var1)) {
         this.reconcileTargets(var1);
      }
   }

   public void rollbackUpdate() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (this.onlyRemoteNewJMSServers(var1)) {
         try {
            this.activateTargetUpdates(false);
         } catch (BeanUpdateRejectedException var3) {
            throw new AssertionError("activateTargetUpdates failed");
         }
      }
   }

   public void activateUpdate() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (this.onlyRemoteNewJMSServers(var1)) {
         try {
            this.activateTargetUpdates(true);
         } catch (BeanUpdateRejectedException var3) {
            throw new AssertionError("activateTargetUpdates failed");
         }
      }
   }

   public void startAddTargets(TargetMBean var1) throws BeanUpdateRejectedException {
      this.reconcileTargets(JMSBeanHelper.getDomain(var1));
   }

   public void finishAddTargets(TargetMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.activateTargetUpdates(var2);
   }

   public void startRemoveTargets(TargetMBean var1) throws BeanUpdateRejectedException {
      this.reconcileTargets(JMSBeanHelper.getDomain(var1));
   }

   public void finishRemoveTargets(TargetMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.activateTargetUpdates(var2);
   }

   public void setDefaultUnitOfOrder(boolean var1) {
      Iterator var2 = this.allDestinationsIterator();

      while(var2.hasNext()) {
         BEDestinationRuntimeDelegate var3 = (BEDestinationRuntimeDelegate)var2.next();
         var3.getManagedDestination().setDefaultUnitOfOrder(var1);
      }

   }

   static {
      targetSignatures.put("Targets", TargetMBean.class);
      Iterator var0 = JMSBeanHelper.distributedTopicBeanSignatures.entrySet().iterator();

      Map.Entry var1;
      while(var0.hasNext()) {
         var1 = (Map.Entry)var0.next();
         uDTopicBeanSignatures.put(var1.getKey(), var1.getValue());
      }

      var0 = JMSBeanHelper.distributedQueueBeanSignatures.entrySet().iterator();

      while(var0.hasNext()) {
         var1 = (Map.Entry)var0.next();
         uDQueueBeanSignatures.put(var1.getKey(), var1.getValue());
      }

      var0 = JMSBeanHelper.destinationBeanSignatures.entrySet().iterator();

      while(var0.hasNext()) {
         var1 = (Map.Entry)var0.next();
         uDQueueBeanSignatures.put(var1.getKey(), var1.getValue());
         uDTopicBeanSignatures.put(var1.getKey(), var1.getValue());
      }

   }
}
