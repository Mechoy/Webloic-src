package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DeliveryParamsOverridesBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DeliveryParamsOverridesBean beanTreeNode;

   public DeliveryParamsOverridesBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DeliveryParamsOverridesBean)var2;
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

   public String getDeliveryMode() {
      return this.beanTreeNode.getDeliveryMode();
   }

   public void setDeliveryMode(String var1) {
      this.beanTreeNode.setDeliveryMode(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DeliveryMode", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTimeToDeliver() {
      return this.beanTreeNode.getTimeToDeliver();
   }

   public void setTimeToDeliver(String var1) {
      this.beanTreeNode.setTimeToDeliver(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TimeToDeliver", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getTimeToLive() {
      return this.beanTreeNode.getTimeToLive();
   }

   public void setTimeToLive(long var1) {
      this.beanTreeNode.setTimeToLive(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TimeToLive", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPriority() {
      return this.beanTreeNode.getPriority();
   }

   public void setPriority(int var1) {
      this.beanTreeNode.setPriority(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Priority", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getRedeliveryDelay() {
      return this.beanTreeNode.getRedeliveryDelay();
   }

   public void setRedeliveryDelay(long var1) {
      this.beanTreeNode.setRedeliveryDelay(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RedeliveryDelay", (Object)null, (Object)null));
      this.setModified(true);
   }

   public TemplateBean getTemplateBean() {
      return this.beanTreeNode.getTemplateBean();
   }
}
