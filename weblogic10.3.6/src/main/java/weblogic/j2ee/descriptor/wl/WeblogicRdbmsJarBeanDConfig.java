package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.descriptor.DescriptorBean;

public class WeblogicRdbmsJarBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicRdbmsJarBean beanTreeNode;

   public WeblogicRdbmsJarBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicRdbmsJarBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   public WeblogicRdbmsJarBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (WeblogicRdbmsJarBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public WeblogicRdbmsJarBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(WeblogicRdbmsJarBean.class);
         this.beanTreeNode = (WeblogicRdbmsJarBean)this.beanTree;
         this.customInit();
      } catch (ConfigurationException var6) {
         throw new InvalidModuleException(var6.toString());
      }
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

   public WeblogicRdbmsBeanBean[] getWeblogicRdbmsBeans() {
      return this.beanTreeNode.getWeblogicRdbmsBeans();
   }

   public WeblogicRdbmsRelationBean[] getWeblogicRdbmsRelations() {
      return this.beanTreeNode.getWeblogicRdbmsRelations();
   }

   public boolean isOrderDatabaseOperations() {
      return this.beanTreeNode.isOrderDatabaseOperations();
   }

   public void setOrderDatabaseOperations(boolean var1) {
      this.beanTreeNode.setOrderDatabaseOperations(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OrderDatabaseOperations", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isEnableBatchOperations() {
      return this.beanTreeNode.isEnableBatchOperations();
   }

   public void setEnableBatchOperations(boolean var1) {
      this.beanTreeNode.setEnableBatchOperations(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableBatchOperations", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCreateDefaultDbmsTables() {
      return this.beanTreeNode.getCreateDefaultDbmsTables();
   }

   public void setCreateDefaultDbmsTables(String var1) {
      this.beanTreeNode.setCreateDefaultDbmsTables(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CreateDefaultDbmsTables", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getValidateDbSchemaWith() {
      return this.beanTreeNode.getValidateDbSchemaWith();
   }

   public void setValidateDbSchemaWith(String var1) {
      this.beanTreeNode.setValidateDbSchemaWith(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ValidateDbSchemaWith", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDatabaseType() {
      return this.beanTreeNode.getDatabaseType();
   }

   public void setDatabaseType(String var1) {
      this.beanTreeNode.setDatabaseType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DatabaseType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDefaultDbmsTablesDdl() {
      return this.beanTreeNode.getDefaultDbmsTablesDdl();
   }

   public void setDefaultDbmsTablesDdl(String var1) {
      this.beanTreeNode.setDefaultDbmsTablesDdl(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultDbmsTablesDdl", (Object)null, (Object)null));
      this.setModified(true);
   }

   public CompatibilityBean getCompatibility() {
      return this.beanTreeNode.getCompatibility();
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getVersion() {
      return this.beanTreeNode.getVersion();
   }

   public void setVersion(String var1) {
      this.beanTreeNode.setVersion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Version", (Object)null, (Object)null));
      this.setModified(true);
   }

   public DConfigBean[] getSecondaryDescriptors() {
      return super.getSecondaryDescriptors();
   }
}
