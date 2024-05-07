package weblogic.jms.backend.udd;

import java.beans.PropertyChangeListener;
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
import weblogic.jms.extensions.JMSModuleHelper;

public class SyntheticErrorDestinationBean implements DestinationBean {
   String name;

   public SyntheticErrorDestinationBean(String var1, String var2) {
      this.name = JMSModuleHelper.uddMakeName(var1, var2);
   }

   public void addDestinationKey(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getAttachSender() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public DeliveryFailureParamsBean getDeliveryFailureParams() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public MessageLoggingParamsBean getMessageLoggingParams() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public DeliveryParamsOverridesBean getDeliveryParamsOverrides() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public String[] getDestinationKeys() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public String getJMSCreateDestinationIdentifier() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public String getJNDIName() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public String getLocalJNDIName() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public int getMaximumMessageSize() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public int getMessagingPerformancePreference() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public QuotaBean getQuota() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public TemplateBean getTemplate() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public ThresholdParamsBean getThresholds() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public String getUnitOfWorkHandlingPolicy() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public boolean isConsumptionPausedAtStartup() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public boolean isInsertionPausedAtStartup() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public boolean isProductionPausedAtStartup() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public String getSAFExportPolicy() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
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
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public void setSubDeploymentName(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public boolean isDefaultTargetingEnabled() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public void setDefaultTargetingEnabled(boolean var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getNotes() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
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
      throw new UnsupportedOperationException();
   }

   public Descriptor getDescriptor() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public DescriptorBean getParentBean() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public boolean isEditable() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public boolean isSet(String var1) {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public void removeBeanUpdateListener(BeanUpdateListener var1) {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public void unSet(String var1) {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
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
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public void setUnitOfWorkHandlingPolicy(String var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }

   public int getIncompleteWorkExpirationTime() {
      throw new AssertionError("The error destination bean does not support any operation except getName()");
   }

   public void setIncompleteWorkExpirationTime(int var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Don't want to modify fake bean");
   }
}
