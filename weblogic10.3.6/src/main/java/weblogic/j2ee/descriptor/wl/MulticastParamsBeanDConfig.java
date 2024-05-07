package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MulticastParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MulticastParamsBean beanTreeNode;

   public MulticastParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MulticastParamsBean)var2;
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

   public String getMulticastAddress() {
      return this.beanTreeNode.getMulticastAddress();
   }

   public void setMulticastAddress(String var1) {
      this.beanTreeNode.setMulticastAddress(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MulticastAddress", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMulticastPort() {
      return this.beanTreeNode.getMulticastPort();
   }

   public void setMulticastPort(int var1) {
      this.beanTreeNode.setMulticastPort(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MulticastPort", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMulticastTimeToLive() {
      return this.beanTreeNode.getMulticastTimeToLive();
   }

   public void setMulticastTimeToLive(int var1) {
      this.beanTreeNode.setMulticastTimeToLive(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MulticastTimeToLive", (Object)null, (Object)null));
      this.setModified(true);
   }

   public TemplateBean getTemplateBean() {
      return this.beanTreeNode.getTemplateBean();
   }
}
