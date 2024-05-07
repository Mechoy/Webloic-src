package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class BufferingQueueBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private BufferingQueueBean beanTreeNode;

   public BufferingQueueBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (BufferingQueueBean)var2;
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

   public String getName() {
      return this.beanTreeNode.getName();
   }

   public void setName(String var1) {
      this.beanTreeNode.setName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Name", (Object)null, (Object)null));
      this.setModified(true);
   }

   public Boolean getEnabled() {
      return this.beanTreeNode.getEnabled();
   }

   public void setEnabled(Boolean var1) {
      this.beanTreeNode.setEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Enabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionFactoryJndiName() {
      return this.beanTreeNode.getConnectionFactoryJndiName();
   }

   public void setConnectionFactoryJndiName(String var1) {
      this.beanTreeNode.setConnectionFactoryJndiName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionFactoryJndiName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public Boolean getTransactionEnabled() {
      return this.beanTreeNode.getTransactionEnabled();
   }

   public void setTransactionEnabled(Boolean var1) {
      this.beanTreeNode.setTransactionEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TransactionEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }
}
