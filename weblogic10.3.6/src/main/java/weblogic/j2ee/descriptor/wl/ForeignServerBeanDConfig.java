package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ForeignServerBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ForeignServerBean beanTreeNode;

   public ForeignServerBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ForeignServerBean)var2;
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

   public ForeignDestinationBean[] getForeignDestinations() {
      return this.beanTreeNode.getForeignDestinations();
   }

   public ForeignConnectionFactoryBean[] getForeignConnectionFactories() {
      return this.beanTreeNode.getForeignConnectionFactories();
   }

   public String getInitialContextFactory() {
      return this.beanTreeNode.getInitialContextFactory();
   }

   public void setInitialContextFactory(String var1) {
      this.beanTreeNode.setInitialContextFactory(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitialContextFactory", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionURL() {
      return this.beanTreeNode.getConnectionURL();
   }

   public void setConnectionURL(String var1) {
      this.beanTreeNode.setConnectionURL(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionURL", (Object)null, (Object)null));
      this.setModified(true);
   }

   public byte[] getJNDIPropertiesCredentialEncrypted() {
      return this.beanTreeNode.getJNDIPropertiesCredentialEncrypted();
   }

   public void setJNDIPropertiesCredentialEncrypted(byte[] var1) {
      this.beanTreeNode.setJNDIPropertiesCredentialEncrypted(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIPropertiesCredentialEncrypted", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getJNDIPropertiesCredential() {
      return this.beanTreeNode.getJNDIPropertiesCredential();
   }

   public void setJNDIPropertiesCredential(String var1) {
      this.beanTreeNode.setJNDIPropertiesCredential(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIPropertiesCredential", (Object)null, (Object)null));
      this.setModified(true);
   }

   public PropertyBean[] getJNDIProperties() {
      return this.beanTreeNode.getJNDIProperties();
   }
}
