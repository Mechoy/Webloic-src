package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCOracleParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCOracleParamsBean beanTreeNode;

   public JDBCOracleParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCOracleParamsBean)var2;
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

   public boolean isFanEnabled() {
      return this.beanTreeNode.isFanEnabled();
   }

   public void setFanEnabled(boolean var1) {
      this.beanTreeNode.setFanEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FanEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOnsNodeList() {
      return this.beanTreeNode.getOnsNodeList();
   }

   public void setOnsNodeList(String var1) {
      this.beanTreeNode.setOnsNodeList(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OnsNodeList", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOnsWalletFile() {
      return this.beanTreeNode.getOnsWalletFile();
   }

   public void setOnsWalletFile(String var1) {
      this.beanTreeNode.setOnsWalletFile(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OnsWalletFile", (Object)null, (Object)null));
      this.setModified(true);
   }

   public byte[] getOnsWalletPasswordEncrypted() {
      return this.beanTreeNode.getOnsWalletPasswordEncrypted();
   }

   public void setOnsWalletPasswordEncrypted(byte[] var1) {
      this.beanTreeNode.setOnsWalletPasswordEncrypted(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OnsWalletPasswordEncrypted", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOnsWalletPassword() {
      return this.beanTreeNode.getOnsWalletPassword();
   }

   public void setOnsWalletPassword(String var1) {
      this.beanTreeNode.setOnsWalletPassword(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OnsWalletPassword", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isOracleEnableJavaNetFastPath() {
      return this.beanTreeNode.isOracleEnableJavaNetFastPath();
   }

   public void setOracleEnableJavaNetFastPath(boolean var1) {
      this.beanTreeNode.setOracleEnableJavaNetFastPath(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OracleEnableJavaNetFastPath", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isOracleOptimizeUtf8Conversion() {
      return this.beanTreeNode.isOracleOptimizeUtf8Conversion();
   }

   public void setOracleOptimizeUtf8Conversion(boolean var1) {
      this.beanTreeNode.setOracleOptimizeUtf8Conversion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OracleOptimizeUtf8Conversion", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionInitializationCallback() {
      return this.beanTreeNode.getConnectionInitializationCallback();
   }

   public void setConnectionInitializationCallback(String var1) {
      this.beanTreeNode.setConnectionInitializationCallback(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionInitializationCallback", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getAffinityPolicy() {
      return this.beanTreeNode.getAffinityPolicy();
   }

   public void setAffinityPolicy(String var1) {
      this.beanTreeNode.setAffinityPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AffinityPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isOracleProxySession() {
      return this.beanTreeNode.isOracleProxySession();
   }

   public void setOracleProxySession(boolean var1) {
      this.beanTreeNode.setOracleProxySession(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OracleProxySession", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUseDatabaseCredentials() {
      return this.beanTreeNode.isUseDatabaseCredentials();
   }

   public void setUseDatabaseCredentials(boolean var1) {
      this.beanTreeNode.setUseDatabaseCredentials(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseDatabaseCredentials", (Object)null, (Object)null));
      this.setModified(true);
   }
}
