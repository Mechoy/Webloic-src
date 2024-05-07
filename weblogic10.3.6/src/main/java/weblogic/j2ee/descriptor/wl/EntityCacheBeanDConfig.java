package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class EntityCacheBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private EntityCacheBean beanTreeNode;

   public EntityCacheBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (EntityCacheBean)var2;
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

   public int getMaxBeansInCache() {
      return this.beanTreeNode.getMaxBeansInCache();
   }

   public void setMaxBeansInCache(int var1) {
      this.beanTreeNode.setMaxBeansInCache(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxBeansInCache", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxQueriesInCache() {
      return this.beanTreeNode.getMaxQueriesInCache();
   }

   public void setMaxQueriesInCache(int var1) {
      this.beanTreeNode.setMaxQueriesInCache(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxQueriesInCache", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getIdleTimeoutSeconds() {
      return this.beanTreeNode.getIdleTimeoutSeconds();
   }

   public void setIdleTimeoutSeconds(int var1) {
      this.beanTreeNode.setIdleTimeoutSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IdleTimeoutSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getReadTimeoutSeconds() {
      return this.beanTreeNode.getReadTimeoutSeconds();
   }

   public void setReadTimeoutSeconds(int var1) {
      this.beanTreeNode.setReadTimeoutSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ReadTimeoutSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConcurrencyStrategy() {
      return this.beanTreeNode.getConcurrencyStrategy();
   }

   public void setConcurrencyStrategy(String var1) {
      this.beanTreeNode.setConcurrencyStrategy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConcurrencyStrategy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCacheBetweenTransactions() {
      return this.beanTreeNode.isCacheBetweenTransactions();
   }

   public void setCacheBetweenTransactions(boolean var1) {
      this.beanTreeNode.setCacheBetweenTransactions(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CacheBetweenTransactions", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDisableReadyInstances() {
      return this.beanTreeNode.isDisableReadyInstances();
   }

   public void setDisableReadyInstances(boolean var1) {
      this.beanTreeNode.setDisableReadyInstances(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DisableReadyInstances", (Object)null, (Object)null));
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
