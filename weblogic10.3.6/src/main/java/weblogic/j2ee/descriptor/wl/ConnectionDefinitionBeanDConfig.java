package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ConnectionDefinitionBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ConnectionDefinitionBean beanTreeNode;

   public ConnectionDefinitionBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ConnectionDefinitionBean)var2;
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
      return this.getConnectionFactoryInterface();
   }

   public void initKeyPropertyValue(String var1) {
      this.setConnectionFactoryInterface(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ConnectionFactoryInterface: ");
      var1.append(this.beanTreeNode.getConnectionFactoryInterface());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getConnectionFactoryInterface() {
      return this.beanTreeNode.getConnectionFactoryInterface();
   }

   public void setConnectionFactoryInterface(String var1) {
      this.beanTreeNode.setConnectionFactoryInterface(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionFactoryInterface", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ConnectionDefinitionPropertiesBean getDefaultConnectionProperties() {
      return this.beanTreeNode.getDefaultConnectionProperties();
   }

   public ConnectionInstanceBean[] getConnectionInstances() {
      return this.beanTreeNode.getConnectionInstances();
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
