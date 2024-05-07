package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ClientParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ClientParamsBean beanTreeNode;

   public ClientParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ClientParamsBean)var2;
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

   public String getClientId() {
      return this.beanTreeNode.getClientId();
   }

   public void setClientId(String var1) {
      this.beanTreeNode.setClientId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClientId", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getClientIdPolicy() {
      return this.beanTreeNode.getClientIdPolicy();
   }

   public void setClientIdPolicy(String var1) {
      this.beanTreeNode.setClientIdPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClientIdPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSubscriptionSharingPolicy() {
      return this.beanTreeNode.getSubscriptionSharingPolicy();
   }

   public void setSubscriptionSharingPolicy(String var1) {
      this.beanTreeNode.setSubscriptionSharingPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SubscriptionSharingPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getAcknowledgePolicy() {
      return this.beanTreeNode.getAcknowledgePolicy();
   }

   public void setAcknowledgePolicy(String var1) {
      this.beanTreeNode.setAcknowledgePolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AcknowledgePolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isAllowCloseInOnMessage() {
      return this.beanTreeNode.isAllowCloseInOnMessage();
   }

   public void setAllowCloseInOnMessage(boolean var1) {
      this.beanTreeNode.setAllowCloseInOnMessage(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AllowCloseInOnMessage", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMessagesMaximum() {
      return this.beanTreeNode.getMessagesMaximum();
   }

   public void setMessagesMaximum(int var1) {
      this.beanTreeNode.setMessagesMaximum(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessagesMaximum", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMulticastOverrunPolicy() {
      return this.beanTreeNode.getMulticastOverrunPolicy();
   }

   public void setMulticastOverrunPolicy(String var1) {
      this.beanTreeNode.setMulticastOverrunPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MulticastOverrunPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSynchronousPrefetchMode() {
      return this.beanTreeNode.getSynchronousPrefetchMode();
   }

   public void setSynchronousPrefetchMode(String var1) {
      this.beanTreeNode.setSynchronousPrefetchMode(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SynchronousPrefetchMode", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getReconnectPolicy() {
      return this.beanTreeNode.getReconnectPolicy();
   }

   public void setReconnectPolicy(String var1) {
      this.beanTreeNode.setReconnectPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ReconnectPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getReconnectBlockingMillis() {
      return this.beanTreeNode.getReconnectBlockingMillis();
   }

   public void setReconnectBlockingMillis(long var1) {
      this.beanTreeNode.setReconnectBlockingMillis(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ReconnectBlockingMillis", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getTotalReconnectPeriodMillis() {
      return this.beanTreeNode.getTotalReconnectPeriodMillis();
   }

   public void setTotalReconnectPeriodMillis(long var1) {
      this.beanTreeNode.setTotalReconnectPeriodMillis(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TotalReconnectPeriodMillis", (Object)null, (Object)null));
      this.setModified(true);
   }
}
