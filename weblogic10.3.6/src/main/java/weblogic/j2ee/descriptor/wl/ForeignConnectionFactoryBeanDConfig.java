package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ForeignConnectionFactoryBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ForeignConnectionFactoryBean beanTreeNode;

   public ForeignConnectionFactoryBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ForeignConnectionFactoryBean)var2;
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

   public String getUsername() {
      return this.beanTreeNode.getUsername();
   }

   public void setUsername(String var1) {
      this.beanTreeNode.setUsername(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Username", (Object)null, (Object)null));
      this.setModified(true);
   }

   public byte[] getPasswordEncrypted() {
      return this.beanTreeNode.getPasswordEncrypted();
   }

   public void setPasswordEncrypted(byte[] var1) {
      this.beanTreeNode.setPasswordEncrypted(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PasswordEncrypted", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPassword() {
      return this.beanTreeNode.getPassword();
   }

   public void setPassword(String var1) {
      this.beanTreeNode.setPassword(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Password", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionHealthChecking() {
      return this.beanTreeNode.getConnectionHealthChecking();
   }

   public void setConnectionHealthChecking(String var1) {
      this.beanTreeNode.setConnectionHealthChecking(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionHealthChecking", (Object)null, (Object)null));
      this.setModified(true);
   }
}
