package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ConfigurationSupportBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ConfigurationSupportBean beanTreeNode;

   public ConfigurationSupportBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ConfigurationSupportBean)var2;
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

   public String getBaseRootElement() {
      return this.beanTreeNode.getBaseRootElement();
   }

   public void setBaseRootElement(String var1) {
      this.beanTreeNode.setBaseRootElement(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BaseRootElement", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConfigRootElement() {
      return this.beanTreeNode.getConfigRootElement();
   }

   public void setConfigRootElement(String var1) {
      this.beanTreeNode.setConfigRootElement(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConfigRootElement", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getBaseNamespace() {
      return this.beanTreeNode.getBaseNamespace();
   }

   public void setBaseNamespace(String var1) {
      this.beanTreeNode.setBaseNamespace(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BaseNamespace", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConfigNamespace() {
      return this.beanTreeNode.getConfigNamespace();
   }

   public void setConfigNamespace(String var1) {
      this.beanTreeNode.setConfigNamespace(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConfigNamespace", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getBaseUri() {
      return this.beanTreeNode.getBaseUri();
   }

   public void setBaseUri(String var1) {
      this.beanTreeNode.setBaseUri(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BaseUri", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConfigUri() {
      return this.beanTreeNode.getConfigUri();
   }

   public void setConfigUri(String var1) {
      this.beanTreeNode.setConfigUri(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConfigUri", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getBasePackageName() {
      return this.beanTreeNode.getBasePackageName();
   }

   public void setBasePackageName(String var1) {
      this.beanTreeNode.setBasePackageName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BasePackageName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConfigPackageName() {
      return this.beanTreeNode.getConfigPackageName();
   }

   public void setConfigPackageName(String var1) {
      this.beanTreeNode.setConfigPackageName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConfigPackageName", (Object)null, (Object)null));
      this.setModified(true);
   }
}
