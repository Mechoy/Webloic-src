package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WeblogicQueryBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicQueryBean beanTreeNode;

   public WeblogicQueryBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicQueryBean)var2;
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

   public String getDescription() {
      return this.beanTreeNode.getDescription();
   }

   public void setDescription(String var1) {
      this.beanTreeNode.setDescription(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Description", (Object)null, (Object)null));
      this.setModified(true);
   }

   public QueryMethodBean getQueryMethod() {
      return this.beanTreeNode.getQueryMethod();
   }

   public EjbQlQueryBean getEjbQlQuery() {
      return this.beanTreeNode.getEjbQlQuery();
   }

   public SqlQueryBean getSqlQuery() {
      return this.beanTreeNode.getSqlQuery();
   }

   public int getMaxElements() {
      return this.beanTreeNode.getMaxElements();
   }

   public void setMaxElements(int var1) {
      this.beanTreeNode.setMaxElements(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxElements", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIncludeUpdates() {
      return this.beanTreeNode.isIncludeUpdates();
   }

   public void setIncludeUpdates(boolean var1) {
      this.beanTreeNode.setIncludeUpdates(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IncludeUpdates", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIncludeUpdatesSet() {
      return this.beanTreeNode.isIncludeUpdatesSet();
   }

   public boolean isSqlSelectDistinct() {
      return this.beanTreeNode.isSqlSelectDistinct();
   }

   public void setSqlSelectDistinct(boolean var1) {
      this.beanTreeNode.setSqlSelectDistinct(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SqlSelectDistinct", (Object)null, (Object)null));
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

   public boolean getEnableQueryCaching() {
      return this.beanTreeNode.getEnableQueryCaching();
   }

   public void setEnableQueryCaching(boolean var1) {
      this.beanTreeNode.setEnableQueryCaching(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableQueryCaching", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getEnableEagerRefresh() {
      return this.beanTreeNode.getEnableEagerRefresh();
   }

   public void setEnableEagerRefresh(boolean var1) {
      this.beanTreeNode.setEnableEagerRefresh(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableEagerRefresh", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIncludeResultCacheHint() {
      return this.beanTreeNode.isIncludeResultCacheHint();
   }

   public void setIncludeResultCacheHint(boolean var1) {
      this.beanTreeNode.setIncludeResultCacheHint(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IncludeResultCacheHint", (Object)null, (Object)null));
      this.setModified(true);
   }
}
