package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.AuthenticationMechanismBean;

public class ConnectionDefinitionPropertiesBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ConnectionDefinitionPropertiesBean beanTreeNode;

   public ConnectionDefinitionPropertiesBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ConnectionDefinitionPropertiesBean)var2;
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

   public PoolParamsBean getPoolParams() {
      return this.beanTreeNode.getPoolParams();
   }

   public LoggingBean getLogging() {
      return this.beanTreeNode.getLogging();
   }

   public String getTransactionSupport() {
      return this.beanTreeNode.getTransactionSupport();
   }

   public void setTransactionSupport(String var1) {
      this.beanTreeNode.setTransactionSupport(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TransactionSupport", (Object)null, (Object)null));
      this.setModified(true);
   }

   public AuthenticationMechanismBean[] getAuthenticationMechanisms() {
      return this.beanTreeNode.getAuthenticationMechanisms();
   }

   public boolean isReauthenticationSupport() {
      return this.beanTreeNode.isReauthenticationSupport();
   }

   public void setReauthenticationSupport(boolean var1) {
      this.beanTreeNode.setReauthenticationSupport(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ReauthenticationSupport", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ConfigPropertiesBean getProperties() {
      return this.beanTreeNode.getProperties();
   }

   public String getResAuth() {
      return this.beanTreeNode.getResAuth();
   }

   public void setResAuth(String var1) {
      this.beanTreeNode.setResAuth(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResAuth", (Object)null, (Object)null));
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
