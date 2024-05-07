package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MessageDrivenDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MessageDrivenDescriptorBean beanTreeNode;

   public MessageDrivenDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MessageDrivenDescriptorBean)var2;
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
      var1.append("ResourceAdapterJNDIName: ");
      var1.append(this.beanTreeNode.getResourceAdapterJNDIName());
      var1.append("\n");
      var1.append("DestinationJNDIName: ");
      var1.append(this.beanTreeNode.getDestinationJNDIName());
      var1.append("\n");
      var1.append("ConnectionFactoryJNDIName: ");
      var1.append(this.beanTreeNode.getConnectionFactoryJNDIName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public PoolBean getPool() {
      return this.beanTreeNode.getPool();
   }

   public TimerDescriptorBean getTimerDescriptor() {
      return this.beanTreeNode.getTimerDescriptor();
   }

   public String getResourceAdapterJNDIName() {
      return this.beanTreeNode.getResourceAdapterJNDIName();
   }

   public void setResourceAdapterJNDIName(String var1) {
      this.beanTreeNode.setResourceAdapterJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResourceAdapterJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDestinationJNDIName() {
      return this.beanTreeNode.getDestinationJNDIName();
   }

   public void setDestinationJNDIName(String var1) {
      this.beanTreeNode.setDestinationJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DestinationJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getInitialContextFactory() {
      return this.beanTreeNode.getInitialContextFactory();
   }

   public void setInitialContextFactory(String var1) {
      this.beanTreeNode.setInitialContextFactory(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitialContextFactory", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getProviderUrl() {
      return this.beanTreeNode.getProviderUrl();
   }

   public void setProviderUrl(String var1) {
      this.beanTreeNode.setProviderUrl(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ProviderUrl", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionFactoryJNDIName() {
      return this.beanTreeNode.getConnectionFactoryJNDIName();
   }

   public void setConnectionFactoryJNDIName(String var1) {
      this.beanTreeNode.setConnectionFactoryJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionFactoryJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDestinationResourceLink() {
      return this.beanTreeNode.getDestinationResourceLink();
   }

   public void setDestinationResourceLink(String var1) {
      this.beanTreeNode.setDestinationResourceLink(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DestinationResourceLink", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionFactoryResourceLink() {
      return this.beanTreeNode.getConnectionFactoryResourceLink();
   }

   public void setConnectionFactoryResourceLink(String var1) {
      this.beanTreeNode.setConnectionFactoryResourceLink(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionFactoryResourceLink", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getJmsPollingIntervalSeconds() {
      return this.beanTreeNode.getJmsPollingIntervalSeconds();
   }

   public void setJmsPollingIntervalSeconds(int var1) {
      this.beanTreeNode.setJmsPollingIntervalSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JmsPollingIntervalSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getJmsClientId() {
      return this.beanTreeNode.getJmsClientId();
   }

   public void setJmsClientId(String var1) {
      this.beanTreeNode.setJmsClientId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JmsClientId", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isGenerateUniqueJmsClientId() {
      return this.beanTreeNode.isGenerateUniqueJmsClientId();
   }

   public void setGenerateUniqueJmsClientId(boolean var1) {
      this.beanTreeNode.setGenerateUniqueJmsClientId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "GenerateUniqueJmsClientId", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDurableSubscriptionDeletion() {
      return this.beanTreeNode.isDurableSubscriptionDeletion();
   }

   public void setDurableSubscriptionDeletion(boolean var1) {
      this.beanTreeNode.setDurableSubscriptionDeletion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DurableSubscriptionDeletion", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxMessagesInTransaction() {
      return this.beanTreeNode.getMaxMessagesInTransaction();
   }

   public void setMaxMessagesInTransaction(int var1) {
      this.beanTreeNode.setMaxMessagesInTransaction(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxMessagesInTransaction", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDistributedDestinationConnection() {
      return this.beanTreeNode.getDistributedDestinationConnection();
   }

   public void setDistributedDestinationConnection(String var1) {
      this.beanTreeNode.setDistributedDestinationConnection(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DistributedDestinationConnection", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUse81StylePolling() {
      return this.beanTreeNode.isUse81StylePolling();
   }

   public void setUse81StylePolling(boolean var1) {
      this.beanTreeNode.setUse81StylePolling(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Use81StylePolling", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getInitSuspendSeconds() {
      return this.beanTreeNode.getInitSuspendSeconds();
   }

   public void setInitSuspendSeconds(int var1) {
      this.beanTreeNode.setInitSuspendSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitSuspendSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxSuspendSeconds() {
      return this.beanTreeNode.getMaxSuspendSeconds();
   }

   public void setMaxSuspendSeconds(int var1) {
      this.beanTreeNode.setMaxSuspendSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxSuspendSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public SecurityPluginBean getSecurityPlugin() {
      return this.beanTreeNode.getSecurityPlugin();
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }
}
