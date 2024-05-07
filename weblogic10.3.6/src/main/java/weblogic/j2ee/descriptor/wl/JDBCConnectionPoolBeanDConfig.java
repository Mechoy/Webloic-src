package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCConnectionPoolBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCConnectionPoolBean beanTreeNode;

   public JDBCConnectionPoolBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCConnectionPoolBean)var2;
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
      return this.getDataSourceJNDIName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setDataSourceJNDIName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("DataSourceJNDIName: ");
      var1.append(this.beanTreeNode.getDataSourceJNDIName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getDataSourceJNDIName() {
      return this.beanTreeNode.getDataSourceJNDIName();
   }

   public void setDataSourceJNDIName(String var1) {
      this.beanTreeNode.setDataSourceJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DataSourceJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ConnectionFactoryBean getConnectionFactory() {
      return this.beanTreeNode.getConnectionFactory();
   }

   public ApplicationPoolParamsBean getPoolParams() {
      return this.beanTreeNode.getPoolParams();
   }

   public DriverParamsBean getDriverParams() {
      return this.beanTreeNode.getDriverParams();
   }

   public String getAclName() {
      return this.beanTreeNode.getAclName();
   }

   public void setAclName(String var1) {
      this.beanTreeNode.setAclName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AclName", (Object)null, (Object)null));
      this.setModified(true);
   }
}
