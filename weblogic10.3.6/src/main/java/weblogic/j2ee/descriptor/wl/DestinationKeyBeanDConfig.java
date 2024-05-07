package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DestinationKeyBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DestinationKeyBean beanTreeNode;

   public DestinationKeyBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DestinationKeyBean)var2;
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

   public String getProperty() {
      return this.beanTreeNode.getProperty();
   }

   public void setProperty(String var1) {
      this.beanTreeNode.setProperty(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Property", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getKeyType() {
      return this.beanTreeNode.getKeyType();
   }

   public void setKeyType(String var1) {
      this.beanTreeNode.setKeyType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeyType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSortOrder() {
      return this.beanTreeNode.getSortOrder();
   }

   public void setSortOrder(String var1) {
      this.beanTreeNode.setSortOrder(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SortOrder", (Object)null, (Object)null));
      this.setModified(true);
   }
}
