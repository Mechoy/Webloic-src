package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MessageDestinationDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MessageDestinationDescriptorBean beanTreeNode;

   public MessageDestinationDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MessageDestinationDescriptorBean)var2;
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
      return this.getMessageDestinationName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setMessageDestinationName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("MessageDestinationName: ");
      var1.append(this.beanTreeNode.getMessageDestinationName());
      var1.append("\n");
      var1.append("DestinationJNDIName: ");
      var1.append(this.beanTreeNode.getDestinationJNDIName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getMessageDestinationName() {
      return this.beanTreeNode.getMessageDestinationName();
   }

   public void setMessageDestinationName(String var1) {
      this.beanTreeNode.setMessageDestinationName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessageDestinationName", (Object)null, (Object)null));
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

   public String getDestinationResourceLink() {
      return this.beanTreeNode.getDestinationResourceLink();
   }

   public void setDestinationResourceLink(String var1) {
      this.beanTreeNode.setDestinationResourceLink(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DestinationResourceLink", (Object)null, (Object)null));
      this.setModified(true);
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
