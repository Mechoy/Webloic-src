package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class TemplateBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private TemplateBean beanTreeNode;

   public TemplateBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (TemplateBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      return null;
   }

   public String keyPropertyValue() {
      return null;
   }

   public void initKeyPropertyValue(String var1) {
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String[] getDestinationKeys() {
      return this.beanTreeNode.getDestinationKeys();
   }

   public void setDestinationKeys(String[] var1) {
      this.beanTreeNode.setDestinationKeys(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DestinationKeys", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ThresholdParamsBean getThresholds() {
      return this.beanTreeNode.getThresholds();
   }

   public DeliveryParamsOverridesBean getDeliveryParamsOverrides() {
      return this.beanTreeNode.getDeliveryParamsOverrides();
   }

   public DeliveryFailureParamsBean getDeliveryFailureParams() {
      return this.beanTreeNode.getDeliveryFailureParams();
   }

   public MessageLoggingParamsBean getMessageLoggingParams() {
      return this.beanTreeNode.getMessageLoggingParams();
   }

   public String getAttachSender() {
      return this.beanTreeNode.getAttachSender();
   }

   public void setAttachSender(String var1) {
      this.beanTreeNode.setAttachSender(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AttachSender", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isProductionPausedAtStartup() {
      return this.beanTreeNode.isProductionPausedAtStartup();
   }

   public void setProductionPausedAtStartup(boolean var1) {
      this.beanTreeNode.setProductionPausedAtStartup(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ProductionPausedAtStartup", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isInsertionPausedAtStartup() {
      return this.beanTreeNode.isInsertionPausedAtStartup();
   }

   public void setInsertionPausedAtStartup(boolean var1) {
      this.beanTreeNode.setInsertionPausedAtStartup(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InsertionPausedAtStartup", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isConsumptionPausedAtStartup() {
      return this.beanTreeNode.isConsumptionPausedAtStartup();
   }

   public void setConsumptionPausedAtStartup(boolean var1) {
      this.beanTreeNode.setConsumptionPausedAtStartup(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConsumptionPausedAtStartup", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaximumMessageSize() {
      return this.beanTreeNode.getMaximumMessageSize();
   }

   public void setMaximumMessageSize(int var1) {
      this.beanTreeNode.setMaximumMessageSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaximumMessageSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public QuotaBean getQuota() {
      return this.beanTreeNode.getQuota();
   }

   public void setQuota(QuotaBean var1) {
      this.beanTreeNode.setQuota(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Quota", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDefaultUnitOfOrder() {
      return this.beanTreeNode.isDefaultUnitOfOrder();
   }

   public void setDefaultUnitOfOrder(boolean var1) {
      this.beanTreeNode.setDefaultUnitOfOrder(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultUnitOfOrder", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSafExportPolicy() {
      return this.beanTreeNode.getSafExportPolicy();
   }

   public void setSafExportPolicy(String var1) {
      this.beanTreeNode.setSafExportPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SafExportPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MulticastParamsBean getMulticast() {
      return this.beanTreeNode.getMulticast();
   }

   public GroupParamsBean[] getGroupParams() {
      return this.beanTreeNode.getGroupParams();
   }

   public int getMessagingPerformancePreference() {
      return this.beanTreeNode.getMessagingPerformancePreference();
   }

   public void setMessagingPerformancePreference(int var1) {
      this.beanTreeNode.setMessagingPerformancePreference(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessagingPerformancePreference", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUnitOfWorkHandlingPolicy() {
      return this.beanTreeNode.getUnitOfWorkHandlingPolicy();
   }

   public void setUnitOfWorkHandlingPolicy(String var1) {
      this.beanTreeNode.setUnitOfWorkHandlingPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UnitOfWorkHandlingPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getIncompleteWorkExpirationTime() {
      return this.beanTreeNode.getIncompleteWorkExpirationTime();
   }

   public void setIncompleteWorkExpirationTime(int var1) {
      this.beanTreeNode.setIncompleteWorkExpirationTime(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IncompleteWorkExpirationTime", (Object)null, (Object)null));
      this.setModified(true);
   }
}
