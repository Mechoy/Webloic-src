package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCDriverParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCDriverParamsBean beanTreeNode;

   public JDBCDriverParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCDriverParamsBean)var2;
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

   public String getUrl() {
      return this.beanTreeNode.getUrl();
   }

   public void setUrl(String var1) {
      this.beanTreeNode.setUrl(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Url", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDriverName() {
      return this.beanTreeNode.getDriverName();
   }

   public void setDriverName(String var1) {
      this.beanTreeNode.setDriverName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DriverName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public JDBCPropertiesBean getProperties() {
      return this.beanTreeNode.getProperties();
   }

   public byte[] getPasswordEncrypted() {
      return this.beanTreeNode.getPasswordEncrypted();
   }

   public void setPasswordEncrypted(byte[] var1) {
      this.beanTreeNode.setPasswordEncrypted(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PasswordEncrypted", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUseXaDataSourceInterface() {
      return this.beanTreeNode.isUseXaDataSourceInterface();
   }

   public void setUseXaDataSourceInterface(boolean var1) {
      this.beanTreeNode.setUseXaDataSourceInterface(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseXaDataSourceInterface", (Object)null, (Object)null));
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

   public boolean isUsePasswordIndirection() {
      return this.beanTreeNode.isUsePasswordIndirection();
   }

   public void setUsePasswordIndirection(boolean var1) {
      this.beanTreeNode.setUsePasswordIndirection(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UsePasswordIndirection", (Object)null, (Object)null));
      this.setModified(true);
   }
}
