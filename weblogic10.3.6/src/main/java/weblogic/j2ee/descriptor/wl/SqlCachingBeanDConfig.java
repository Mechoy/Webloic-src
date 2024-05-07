package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.EmptyBean;

public class SqlCachingBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SqlCachingBean beanTreeNode;

   public SqlCachingBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SqlCachingBean)var2;
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
      return this.getSqlCachingName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setSqlCachingName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("SqlCachingName: ");
      var1.append(this.beanTreeNode.getSqlCachingName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getDescription() {
      return this.beanTreeNode.getDescription();
   }

   public void setDescription(String var1) {
      this.beanTreeNode.setDescription(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Description", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSqlCachingName() {
      return this.beanTreeNode.getSqlCachingName();
   }

   public void setSqlCachingName(String var1) {
      this.beanTreeNode.setSqlCachingName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SqlCachingName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public EmptyBean[] getResultColumns() {
      return this.beanTreeNode.getResultColumns();
   }

   public TableBean[] getTables() {
      return this.beanTreeNode.getTables();
   }
}
