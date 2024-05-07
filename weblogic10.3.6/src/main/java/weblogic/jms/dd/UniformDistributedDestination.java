package weblogic.jms.dd;

import java.util.ArrayList;
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
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.udd.SyntheticDQBean;
import weblogic.jms.backend.udd.SyntheticDTBean;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSException;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSDeploymentHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSConstants;
import weblogic.management.utils.BeanListenerCustomizer;
import weblogic.management.utils.GenericBeanListener;

public class UniformDistributedDestination implements JMSModuleManagedEntity, BeanListenerCustomizer, DDConfig, DDConstants {
   private static final String PHYS_DEST_NAME = "PhysicalDestinationName";
   private String forwardingPolicy;
   private int type;
   private GenericBeanListener uddBeanListener;
   private UniformDistributedDestinationBean specificBean;
   private String name;
   private JMSBean module;
   private DDHandler ddHandler;
   private String jndiName;
   private ApplicationContext appCtx;
   private String safExportPolicy;
   private String EARModuleName;
   private String moduleName;
   private String unitOfOrderRouting;
   private boolean isDefaultUnitOfOrder;
   private String loadBalancingPolicy;
   private int queueForwardDelay;
   private boolean resetDeliveryCount = true;

   public UniformDistributedDestination(String var1, JMSBean var2, UniformDistributedDestinationBean var3, String var4, String var5, ApplicationContext var6) {
      this.name = var1;
      this.module = var2;
      this.specificBean = var3;
      this.EARModuleName = var4;
      this.moduleName = var5;
      this.appCtx = var6;
      if (this.specificBean instanceof UniformDistributedQueueBean) {
         this.type = 0;
      } else {
         this.type = 1;
      }

      this.ddHandler = new DDHandler(this, true);
   }

   private void initializeBeanUpdateListeners() throws JMSException {
      DescriptorBean var1 = (DescriptorBean)this.specificBean;
      if (this.type == 0) {
         this.uddBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.uniformDistributedQueueBeanSignatures, false);
      } else {
         this.uddBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.uniformDistributedTopicBeanSignatures, false);
      }

      try {
         this.uddBeanListener.initialize();
      } catch (ManagementException var3) {
         throw new JMSException(var3);
      }

      this.uddBeanListener.setCustomizer(this);
   }

   public void activate(JMSBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Activating uniform distributed destination: " + this.name);
      }

      if (var1 != null) {
         this.module = var1;
         if (this.specificBean instanceof UniformDistributedQueueBean) {
            this.specificBean = this.module.lookupUniformDistributedQueue(this.getEntityName());
         } else {
            this.specificBean = this.module.lookupUniformDistributedTopic(this.getEntityName());
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
            JMSDebug.JMSModule.debug("Sucessfully activated uniform distributed destination: " + this.name);
         } else {
            JMSDebug.JMSModule.debug("failed to activate uniform distributed destination: " + this.name, var2);
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

   private void closeBeanUpdateListeners() {
      if (this.uddBeanListener != null) {
         this.uddBeanListener.close();
         this.uddBeanListener = null;
      }

   }

   private void openBeanUpdateListeners() {
      DescriptorBean var1 = (DescriptorBean)this.specificBean;
      if (this.uddBeanListener != null) {
         this.uddBeanListener.open();
      } else {
         if (this.type == 0) {
            this.uddBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.uniformDistributedQueueBeanSignatures);
         } else {
            this.uddBeanListener = new GenericBeanListener(var1, this, JMSBeanHelper.uniformDistributedTopicBeanSignatures);
         }

         this.uddBeanListener.setCustomizer(this);
      }

   }

   private void activateMembers() {
      if (this.ddHandler != null) {
         this.ddHandler.updateMembers(this.fullMemberNames());
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

   public DistributedDestinationMemberBean[] getMemberBeans() {
      if (this.specificBean == null) {
         return new DistributedDestinationMemberBean[0];
      } else if (this.type == 0) {
         UniformDistributedQueueBean var2 = (UniformDistributedQueueBean)this.specificBean;
         return ((SyntheticDQBean)var2).getDistributedQueueMembers();
      } else {
         UniformDistributedTopicBean var1 = (UniformDistributedTopicBean)this.specificBean;
         return ((SyntheticDTBean)var1).getDistributedTopicMembers();
      }
   }

   public void deactivate() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Start deactivate uniform distributed destination: " + this.name);
      }

      this.closeBeanUpdateListeners();
      if (this.ddHandler != null) {
         this.ddHandler.deactivate();
      } else if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("ddHandler is null for " + this.name);
      }

      DDScheduler.drain();
      Throwable var1 = DDScheduler.waitForComplete();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         if (var1 == null) {
            JMSDebug.JMSModule.debug("Sucessfully deactivated uniform distributed destination: " + this.name);
         } else {
            JMSDebug.JMSModule.debug("failed to deactivate uniform distributed destination: " + this.name, var1);
         }
      } else if (var1 != null) {
         JMSDebug.JMSModule.debug("ddHandler is null for " + this.name);
         var1.printStackTrace();
      }

      this.ddHandler = null;
   }

   public void prepare() throws ModuleException {
      this.initializeWithBean();

      try {
         this.valJNDIName(this.jndiName);
      } catch (BeanUpdateRejectedException var2) {
         throw new ModuleException(var2.getMessage(), var2.getCause());
      }

      if (this.type == 0) {
         this.queueForwardDelay = ((UniformDistributedQueueBean)this.specificBean).getForwardDelay();
         this.setResetDeliveryCountOnForward(((UniformDistributedQueueBean)this.specificBean).getResetDeliveryCountOnForward());
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("UniformDistributedDestination.prepare()  queueForwardDelay " + this.queueForwardDelay + " resetDeliveryCount " + this.resetDeliveryCount);
         }
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Sucessfully prepared uniform distributed destination: " + this.name);
      }

   }

   private final void valJNDIName(String var1) throws BeanUpdateRejectedException {
      this.internalValJndiName(var1, false);
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

   private void initializeWithBean() throws ModuleException {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Initializing uniform distributed destination with Bean " + this.specificBean.getName());
      }

      try {
         this.initializeBeanUpdateListeners();
      } catch (JMSException var2) {
         throw new ModuleException(var2.getMessage(), var2);
      }
   }

   public void unprepare() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Sucessfully unprepared uniform distributed destination: " + this.name);
      }

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

   public String getForwardingPolicy() {
      return this.forwardingPolicy;
   }

   public void setForwardingPolicy(String var1) {
      this.forwardingPolicy = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setForwardingPolicy(translateForwardingPolicy(var1));
      }

   }

   public int getForwardingPolicyAsInt() {
      return translateForwardingPolicy(this.forwardingPolicy);
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

   public boolean getResetDeliveryCountOnForward() {
      return this.resetDeliveryCount;
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      this.resetDeliveryCount = var1;
      if (this.ddHandler != null) {
         this.ddHandler.setResetDeliveryCountOnForward(var1);
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

   public void destroy() {
   }

   public void remove() {
   }

   public void finishAddDistributedQueueMembers(DistributedDestinationMemberBean var1, boolean var2) {
      this.finishAddDistributedDestinationMember(var1.getPhysicalDestinationName(), var1, var2);
   }

   private void finishAddDistributedDestinationMember(String var1, DistributedDestinationMemberBean var2, boolean var3) {
      if (var1 != null) {
         if (var3 && this.ddHandler != null) {
            this.ddHandler.updateMembers(this.fullMemberNames());
         }

      }
   }

   public void finishAddDistributedTopicMembers(DistributedDestinationMemberBean var1, boolean var2) {
      this.finishAddDistributedDestinationMember(var1.getPhysicalDestinationName(), var1, var2);
   }

   public void finishRemoveDistributedQueueMembers(DistributedDestinationMemberBean var1, boolean var2) throws BeanUpdateRejectedException {
      String var3 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      this.finishRemoveDistributedDestinationMember(var3, var1, var2);
   }

   private void finishRemoveDistributedDestinationMember(String var1, DistributedDestinationMemberBean var2, boolean var3) throws BeanUpdateRejectedException {
      if (var1 != null) {
         if (var3 && this.ddHandler != null) {
            this.ddHandler.updateMembers(this.fullMemberNames());
         }

      }
   }

   public void finishRemoveDistributedTopicMembers(DistributedDestinationMemberBean var1, boolean var2) throws BeanUpdateRejectedException {
      String var3 = JMSDeploymentHelper.getMemberName(this.moduleName, var1);
      this.finishRemoveDistributedDestinationMember(var3, var1, var2);
   }

   public void activateFinished() {
   }

   public String getEntityName() {
      return this.specificBean.getName();
   }

   public int getType() {
      return this.type;
   }

   public boolean isDefaultUnitOfOrder() {
      return this.isDefaultUnitOfOrder;
   }

   public String getApplicationName() {
      return this.appCtx != null ? this.appCtx.getApplicationId() : null;
   }

   public void rollbackChangeOfTargets() {
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

   public String getName() {
      return this.name;
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) {
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

   public String getUnitOfOrderRouting() {
      return this.unitOfOrderRouting;
   }

   public void setUnitOfOrderRouting(String var1) {
      this.unitOfOrderRouting = var1.intern();
      if (this.ddHandler != null) {
         this.ddHandler.setUnitOfOrderRouting(this.unitOfOrderRouting);
      }

   }

   public String getEARModuleName() {
      return this.EARModuleName;
   }

   public void activateChangeOfTargets() {
   }

   public String getReferenceName() {
      return null;
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
            if ("PhysicalDestinationName".equals(var6) && this.physicalDestinationNameSet) {
               throw new BeanUpdateRejectedException("An attempt was made to change the physical destination from " + this.memberBean.getPhysicalDestinationName() + " to " + var2.getPhysicalDestinationName() + ".  This is not allowed.  The physical" + " destination may only be changed from an unset state to a particular physical destination");
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
               UniformDistributedDestination.this.finishAddDistributedDestinationMember(this.memberBean.getPhysicalDestinationName(), this.memberBean, true);
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
               UniformDistributedDestination.this.finishAddDistributedDestinationMember(var2.getPhysicalDestinationName(), var2, false);
            }
         }

      }
   }
}
