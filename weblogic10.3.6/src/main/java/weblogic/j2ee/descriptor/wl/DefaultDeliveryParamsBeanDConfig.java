package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DefaultDeliveryParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DefaultDeliveryParamsBean beanTreeNode;

   public DefaultDeliveryParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DefaultDeliveryParamsBean)var2;
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

   public String getDefaultDeliveryMode() {
      return this.beanTreeNode.getDefaultDeliveryMode();
   }

   public void setDefaultDeliveryMode(String var1) {
      this.beanTreeNode.setDefaultDeliveryMode(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultDeliveryMode", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDefaultTimeToDeliver() {
      return this.beanTreeNode.getDefaultTimeToDeliver();
   }

   public void setDefaultTimeToDeliver(String var1) {
      this.beanTreeNode.setDefaultTimeToDeliver(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultTimeToDeliver", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getDefaultTimeToLive() {
      return this.beanTreeNode.getDefaultTimeToLive();
   }

   public void setDefaultTimeToLive(long var1) {
      this.beanTreeNode.setDefaultTimeToLive(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultTimeToLive", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getDefaultPriority() {
      return this.beanTreeNode.getDefaultPriority();
   }

   public void setDefaultPriority(int var1) {
      this.beanTreeNode.setDefaultPriority(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultPriority", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getDefaultRedeliveryDelay() {
      return this.beanTreeNode.getDefaultRedeliveryDelay();
   }

   public void setDefaultRedeliveryDelay(long var1) {
      this.beanTreeNode.setDefaultRedeliveryDelay(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultRedeliveryDelay", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getSendTimeout() {
      return this.beanTreeNode.getSendTimeout();
   }

   public void setSendTimeout(long var1) {
      this.beanTreeNode.setSendTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SendTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getDefaultCompressionThreshold() {
      return this.beanTreeNode.getDefaultCompressionThreshold();
   }

   public void setDefaultCompressionThreshold(int var1) {
      this.beanTreeNode.setDefaultCompressionThreshold(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultCompressionThreshold", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDefaultUnitOfOrder() {
      return this.beanTreeNode.getDefaultUnitOfOrder();
   }

   public void setDefaultUnitOfOrder(String var1) {
      this.beanTreeNode.setDefaultUnitOfOrder(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultUnitOfOrder", (Object)null, (Object)null));
      this.setModified(true);
   }
}
