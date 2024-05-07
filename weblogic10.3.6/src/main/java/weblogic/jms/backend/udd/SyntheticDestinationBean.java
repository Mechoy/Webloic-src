package weblogic.jms.backend.udd;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.MessageLoggingParamsBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.ThresholdParamsBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;
import weblogic.j2ee.descriptor.wl.constants.JMSConstants;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.management.ManagementException;

public class SyntheticDestinationBean implements DestinationBean, DescriptorBean, DeliveryFailureParamsBean {
   LinkedList listeners = new LinkedList();
   String serverName;
   boolean defaultTargetingEnabled;
   UDDEntity udd;

   public SyntheticDestinationBean(UDDEntity var1, String var2) {
      this.udd = var1;
      this.serverName = var2;

      try {
         JMSSecurityHelper.getSecurityHelper().mapDestinationName(this.getName(), var1.getUDestBean().getName());
      } catch (ManagementException var4) {
         throw new AssertionError("Cannot find Security Helper");
      }
   }

   public void addDestinationKey(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getAttachSender() {
      return this.udd.getUDestBean().getAttachSender();
   }

   public DeliveryFailureParamsBean getDeliveryFailureParams() {
      return this;
   }

   public MessageLoggingParamsBean getMessageLoggingParams() {
      return this.udd.getUDestBean().getMessageLoggingParams();
   }

   public DeliveryParamsOverridesBean getDeliveryParamsOverrides() {
      return this.udd.getUDestBean().getDeliveryParamsOverrides();
   }

   public String[] getDestinationKeys() {
      return this.udd.getUDestBean().getDestinationKeys();
   }

   public String getJMSCreateDestinationIdentifier() {
      return this.udd.getUDestBean().getJMSCreateDestinationIdentifier();
   }

   public String getJNDIName() {
      return this.udd.getUDestBean().getJNDIName() == null ? null : JMSModuleHelper.uddMakeName(this.serverName, this.udd.getUDestBean().getJNDIName());
   }

   public String getLocalJNDIName() {
      return this.udd.getUDestBean().getLocalJNDIName() == null ? null : JMSModuleHelper.uddMakeName(this.serverName, this.udd.getUDestBean().getLocalJNDIName());
   }

   public int getMaximumMessageSize() {
      return this.udd.getUDestBean().getMaximumMessageSize();
   }

   public int getMessagingPerformancePreference() {
      return this.udd.getUDestBean().getMessagingPerformancePreference();
   }

   public QuotaBean getQuota() {
      return this.udd.getUDestBean().getQuota();
   }

   public TemplateBean getTemplate() {
      return this.udd.getUDestBean().getTemplate();
   }

   public ThresholdParamsBean getThresholds() {
      return this.udd.getUDestBean().getThresholds();
   }

   public boolean isConsumptionPausedAtStartup() {
      return this.udd.getUDestBean().isConsumptionPausedAtStartup();
   }

   public boolean isInsertionPausedAtStartup() {
      return this.udd.getUDestBean().isInsertionPausedAtStartup();
   }

   public boolean isProductionPausedAtStartup() {
      return this.udd.getUDestBean().isProductionPausedAtStartup();
   }

   public String getSAFExportPolicy() {
      return this.udd.getUDestBean().getSAFExportPolicy();
   }

   public String getForwardingPolicy() {
      return this.udd.getUDestBean() instanceof UniformDistributedTopicBean ? ((UniformDistributedTopicBean)this.udd.getUDestBean()).getForwardingPolicy() : JMSConstants.FORWARDING_POLICY_REPLICATED;
   }

   public void removeDestinationKey(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public void setAttachSender(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setSAFExportPolicy(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setConsumptionPausedAtStartup(boolean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setDestinationKeys(String[] var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setJMSCreateDestinationIdentifier(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setInsertionPausedAtStartup(boolean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setJNDIName(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setLocalJNDIName(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setMaximumMessageSize(int var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setMessagingPerformancePreference(int var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setProductionPausedAtStartup(boolean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setQuota(QuotaBean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setTemplate(TemplateBean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public String getSubDeploymentName() {
      return this.serverName;
   }

   public boolean isDefaultTargetingEnabled() {
      return this.defaultTargetingEnabled;
   }

   public void setDefaultTargetingEnabled(boolean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setSubDeploymentName(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public String getName() {
      return JMSModuleHelper.uddMakeName(this.serverName, this.udd.getUDestBean().getName());
   }

   public void setName(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getNotes() {
      return this.udd.getUDestBean().getNotes();
   }

   public void setNotes(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void addBeanUpdateListener(BeanUpdateListener var1) {
      this.listeners.add(var1);
   }

   public Descriptor getDescriptor() {
      return null;
   }

   public DescriptorBean getParentBean() {
      return ((DescriptorBean)this.udd.getUDestBean()).getParentBean();
   }

   public boolean isEditable() {
      return false;
   }

   public boolean isSet(String var1) {
      return false;
   }

   public void removeBeanUpdateListener(BeanUpdateListener var1) {
      this.listeners.remove(var1);
   }

   public void unSet(String var1) {
   }

   public DescriptorBean createChildCopy(String var1, DescriptorBean var2) {
      throw new UnsupportedOperationException();
   }

   public DescriptorBean createChildCopyIncludingObsolete(String var1, DescriptorBean var2) {
      throw new UnsupportedOperationException();
   }

   public void setDefaultUnitOfOrder(boolean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public boolean isDefaultUnitOfOrder() {
      return this.udd.getUDestBean().isDefaultUnitOfOrder();
   }

   public DestinationBean getErrorDestination() {
      DestinationBean var1 = this.udd.getUDestBean().getDeliveryFailureParams().getErrorDestination();
      return var1 == null ? null : new SyntheticErrorDestinationBean(this.serverName, var1.getName());
   }

   public int getRedeliveryLimit() {
      return this.udd.getUDestBean().getDeliveryFailureParams().getRedeliveryLimit();
   }

   public String getExpirationPolicy() {
      return this.udd.getUDestBean().getDeliveryFailureParams().getExpirationPolicy();
   }

   public String getExpirationLoggingPolicy() {
      return this.udd.getUDestBean().getDeliveryFailureParams().getExpirationLoggingPolicy();
   }

   public String findSubDeploymentName() {
      return this.udd.getUDestBean().getDeliveryFailureParams().findSubDeploymentName();
   }

   public TemplateBean getTemplateBean() {
      return this.udd.getUDestBean().getDeliveryFailureParams().getTemplateBean();
   }

   public void setErrorDestination(DestinationBean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setRedeliveryLimit(int var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setExpirationPolicy(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setExpirationLoggingPolicy(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public void setUnitOfWorkHandlingPolicy(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public String getUnitOfWorkHandlingPolicy() {
      return this.udd.getUDestBean().getUnitOfWorkHandlingPolicy();
   }

   public int getIncompleteWorkExpirationTime() {
      return this.udd.getUDestBean().getIncompleteWorkExpirationTime();
   }

   public void setIncompleteWorkExpirationTime(int var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }
}
