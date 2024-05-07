package weblogic.jms.backend;

import java.util.LinkedList;
import java.util.List;
import javax.jms.JMSException;
import javax.naming.Context;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.ModuleName;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.utils.GenericBeanListener;

public abstract class BEDestinationRuntimeDelegate implements JMSModuleManagedEntity {
   protected EntityName entityName;
   private Context applicationContext;
   protected BackEnd backEnd;
   private JMSBean wholeModule;
   protected DestinationBean specificBean;
   protected boolean temporary;
   private ModuleName auxiliaryModuleName;
   private BEQuota beQuota;
   private GenericBeanListener destinationListener;
   private GenericBeanListener localDestinationListener;
   private GenericBeanListener localDeliveryFailureListener;
   private GenericBeanListener thresholdListener;
   private GenericBeanListener deliveryOverridesListener;
   private GenericBeanListener deliveryFailureListener;
   private GenericBeanListener messageLoggingListener;
   private BEDestinationImpl managedDestination;
   private BEDestinationRuntimeMBeanImpl runtimeMBean;

   protected BEDestinationRuntimeDelegate(EntityName var1, Context var2, BackEnd var3, JMSBean var4, DestinationBean var5, boolean var6, ModuleName var7) {
      this.entityName = var1;
      this.applicationContext = var2;
      this.backEnd = var3;
      this.wholeModule = var4;
      this.specificBean = var5;
      this.temporary = var6;
      this.auxiliaryModuleName = var7;
   }

   BEDestinationRuntimeMBeanImpl getRuntimeMBean() {
      return this.runtimeMBean;
   }

   protected void initialize(int var1) throws ModuleException {
      this.managedDestination.setDuration(var1);

      try {
         this.initializeListeners();
      } catch (ManagementException var11) {
         throw new ModuleException("Could not initialize the destination " + this.entityName.toString(), var11);
      }

      try {
         this.managedDestination.valJNDIName(this.specificBean.getJNDIName());
         this.managedDestination.valLocalJNDIName(this.specificBean.getLocalJNDIName());
      } catch (BeanUpdateRejectedException var10) {
         throw new ModuleException(var10.getMessage(), var10);
      }

      String[] var2 = this.specificBean.getDestinationKeys();
      LinkedList var3 = new LinkedList();
      boolean var4 = false;

      for(int var5 = 0; var5 < var2.length; ++var5) {
         DestinationKeyBean var6 = this.wholeModule.lookupDestinationKey(var2[var5]);
         BEDestinationKey var7 = new BEDestinationKey(this.managedDestination, var6);
         var3.add(var7);
         if (var6.getProperty().equals("JMSMessageID")) {
            var4 = true;
         }
      }

      if (!var4) {
         var3.add(new BEDestinationKey(this.managedDestination));
      }

      this.managedDestination.setDestinationKeysList(var3);
      DestinationImpl var12 = new DestinationImpl(this.managedDestination.getDestinationTypeIndicator(), this.backEnd.getName(), this.backEnd.getPersistentStore() != null ? this.backEnd.getPersistentStore().getName() : null, this.managedDestination.getName(), this.entityName.getApplicationName(), this.entityName.getEARModuleName(), this.backEnd.getJMSServerId(), this.managedDestination.getJMSID(), this.managedDestination.getCreationTime(), this.managedDestination.getSAFExportPolicy());
      this.managedDestination.setDestinationImpl(var12);

      try {
         this.setQuota(this.specificBean.getQuota(), false);
      } catch (BeanUpdateFailedException var9) {
         throw new ModuleException(var9.getMessage(), var9);
      }

      this.setErrorDestination(this.specificBean.getDeliveryFailureParams().getErrorDestination());

      try {
         this.managedDestination.open();
      } catch (JMSException var8) {
         throw new ModuleException(var8);
      }
   }

   void setManagedDestination(BEDestinationImpl var1) throws ManagementException {
      this.managedDestination = var1;
      this.runtimeMBean = new BEDestinationRuntimeMBeanImpl(this.entityName.toString(), this.backEnd, false, var1);
      this.managedDestination.setRuntimeMBean(this.runtimeMBean);
      this.managedDestination.setModuleName(this.entityName.getFullyQualifiedModuleName());
   }

   public BEDestinationImpl getManagedDestination() {
      return this.managedDestination;
   }

   public void prepare() throws ModuleException {
      this.initialize(this.temporary ? 0 : 1);
      synchronized(this.managedDestination) {
         this.managedDestination.setStateValue(1);
      }

      try {
         this.backEnd.addDestination(this.managedDestination);
      } catch (JMSException var3) {
         throw new ModuleException("ERROR: Unable to add destination " + this.entityName + " to the back end " + this.backEnd.getName(), var3);
      }

      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Destination " + this.entityName + " successfully prepared");
      }

   }

   public void activate(JMSBean var1) throws ModuleException {
      if (var1 != null) {
         this.wholeModule = var1;
         if (this.specificBean instanceof QueueBean) {
            this.specificBean = this.wholeModule.lookupQueue(this.getEntityName());
         } else {
            this.specificBean = this.wholeModule.lookupTopic(this.getEntityName());
         }

         this.closeListeners();
      }

      this.openListeners();
      if (this.beQuota != null) {
         QuotaBean var2 = this.specificBean.getQuota();
         this.beQuota.setQuotaBean(var2);
      }

      this.managedDestination.setApplicationJNDIName(this.constructApplicationJNDIName());
      this.managedDestination.setApplicationContext(this.applicationContext);

      try {
         this.managedDestination.start();
      } catch (JMSException var3) {
         throw new ModuleException("ERROR: Could not activate " + this.managedDestination.getName(), var3);
      }

      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Destination " + this.entityName + " successfully activated");
      }

   }

   public void deactivate() throws ModuleException {
      this.managedDestination.unAdvertise();
      if (this.beQuota != null) {
         if (this.beQuota.close()) {
            this.backEnd.removeBEQuota(this.beQuota.getName());
         }

         this.beQuota = null;
      }

      this.closeListeners();
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Destination " + this.entityName + " successfully deactivated");
      }

   }

   public void unprepare() throws ModuleException {
      this.backEnd.removeDestination(this.managedDestination);
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Destination " + this.entityName + " successfully unprepared");
      }

   }

   public void remove() throws ModuleException {
      if (this.managedDestination != null) {
         this.managedDestination.adminDeletion();
      }

      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Destination " + this.entityName + " successfully removed");
      }

   }

   public void destroy() throws ModuleException {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Destination " + this.entityName + " successfully destroyed");
      }

   }

   public String getEntityName() {
      return this.specificBean.getName();
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) throws ModuleException {
      TargetMBean var3 = (TargetMBean)var1.get(0);
      throw new ModuleException(JMSExceptionLogger.logInvalidTargetChangeLoggable(this.entityName.toString(), this.backEnd.getName(), var3.getName()).getMessage());
   }

   public void activateChangeOfTargets() {
   }

   public void rollbackChangeOfTargets() {
   }

   private void initializeListeners() throws ManagementException {
      DescriptorBean var1 = (DescriptorBean)this.specificBean;
      this.destinationListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.destinationBeanSignatures, false);
      this.destinationListener.initialize();
      var1 = (DescriptorBean)this.specificBean.getThresholds();
      this.thresholdListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.thresholdBeanSignatures, false);
      this.thresholdListener.initialize();
      var1 = (DescriptorBean)this.specificBean.getDeliveryParamsOverrides();
      this.deliveryOverridesListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.deliveryOverridesSignatures, false);
      this.deliveryOverridesListener.initialize();
      var1 = (DescriptorBean)this.specificBean.getDeliveryFailureParams();
      this.deliveryFailureListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.deliveryFailureSignatures, false);
      this.deliveryFailureListener.initialize();
      var1 = (DescriptorBean)this.specificBean.getMessageLoggingParams();
      this.messageLoggingListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.messageLoggingSignatures, false);
      this.messageLoggingListener.initialize();
   }

   private void openListeners() {
      DescriptorBean var1;
      if (this.destinationListener != null) {
         this.destinationListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean;
         this.destinationListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.destinationBeanSignatures);
      }

      if (this.localDestinationListener != null) {
         this.localDestinationListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean;
         this.localDestinationListener = new GenericBeanListener(var1, this, JMSBeanHelper.localDestinationBeanSignatures);
      }

      if (this.localDeliveryFailureListener != null) {
         this.localDestinationListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean.getDeliveryFailureParams();
         this.localDeliveryFailureListener = new GenericBeanListener(var1, this, JMSBeanHelper.localDeliveryFailureSignatures);
      }

      if (this.thresholdListener != null) {
         this.thresholdListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean.getThresholds();
         this.thresholdListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.thresholdBeanSignatures);
      }

      if (this.deliveryOverridesListener != null) {
         this.deliveryOverridesListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean.getDeliveryParamsOverrides();
         this.deliveryOverridesListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.deliveryOverridesSignatures);
      }

      if (this.deliveryFailureListener != null) {
         this.deliveryFailureListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean.getDeliveryFailureParams();
         this.deliveryFailureListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.deliveryFailureSignatures);
      }

      if (this.messageLoggingListener != null) {
         this.messageLoggingListener.open();
      } else {
         var1 = (DescriptorBean)this.specificBean.getMessageLoggingParams();
         this.messageLoggingListener = new GenericBeanListener(var1, this.managedDestination, JMSBeanHelper.messageLoggingSignatures);
      }

   }

   private void closeListeners() {
      if (this.messageLoggingListener != null) {
         this.messageLoggingListener.close();
         this.messageLoggingListener = null;
      }

      if (this.deliveryFailureListener != null) {
         this.deliveryFailureListener.close();
         this.deliveryFailureListener = null;
      }

      if (this.deliveryOverridesListener != null) {
         this.deliveryOverridesListener.close();
         this.deliveryOverridesListener = null;
      }

      if (this.thresholdListener != null) {
         this.thresholdListener.close();
         this.thresholdListener = null;
      }

      if (this.localDeliveryFailureListener != null) {
         this.localDeliveryFailureListener.close();
         this.localDeliveryFailureListener = null;
      }

      if (this.localDestinationListener != null) {
         this.localDestinationListener.close();
         this.localDestinationListener = null;
      }

      if (this.destinationListener != null) {
         this.destinationListener.close();
         this.destinationListener = null;
      }

   }

   public void setErrorDestination(DestinationBean var1) {
      EntityName var2 = var1 == null ? null : new EntityName(this.auxiliaryModuleName, var1.getName());
      this.managedDestination.setErrorDestination(var2);
   }

   public void setQuota(QuotaBean var1) throws BeanUpdateFailedException {
      this.setQuota(var1, true);
   }

   private void setQuota(QuotaBean var1, boolean var2) throws BeanUpdateFailedException {
      if (this.beQuota != null) {
         if (this.beQuota.close()) {
            this.backEnd.removeBEQuota(this.beQuota.getName());
         }

         this.beQuota = null;
      }

      if (var1 == null) {
         this.managedDestination.setQuota(this.backEnd.getQuota());
      } else {
         String var3;
         if (var1.isShared()) {
            var3 = JMSBeanHelper.getDecoratedName(this.auxiliaryModuleName.getFullyQualifiedModuleName(), var1.getName());
            this.beQuota = this.backEnd.findBEQuota(var3);
            if (this.beQuota == null) {
               this.beQuota = this.backEnd.createBEQuota(var3, var1);
            }
         } else {
            var3 = JMSBeanHelper.getDecoratedName(this.entityName.toString(), var1.getName());
            this.beQuota = this.backEnd.createBEQuota(var3, var1);
         }

         this.managedDestination.setQuota(this.beQuota.getQuota());
         if (var2) {
            this.beQuota.setQuotaBean(var1);
         }
      }

   }

   private String constructApplicationJNDIName() {
      return this.entityName.getEARModuleName() != null && this.entityName.getEARModuleName().length() > 0 ? this.entityName.getEARModuleName() + "#" + this.specificBean.getName() : null;
   }
}
