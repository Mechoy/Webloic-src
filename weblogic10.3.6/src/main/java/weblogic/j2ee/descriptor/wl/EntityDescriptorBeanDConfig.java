package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class EntityDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private EntityDescriptorBean beanTreeNode;

   public EntityDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (EntityDescriptorBean)var2;
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

   public PoolBean getPool() {
      return this.beanTreeNode.getPool();
   }

   public TimerDescriptorBean getTimerDescriptor() {
      return this.beanTreeNode.getTimerDescriptor();
   }

   public EntityCacheBean getEntityCache() {
      return this.beanTreeNode.getEntityCache();
   }

   public EntityCacheRefBean getEntityCacheRef() {
      return this.beanTreeNode.getEntityCacheRef();
   }

   public PersistenceBean getPersistence() {
      return this.beanTreeNode.getPersistence();
   }

   public EntityClusteringBean getEntityClustering() {
      return this.beanTreeNode.getEntityClustering();
   }

   public InvalidationTargetBean getInvalidationTarget() {
      return this.beanTreeNode.getInvalidationTarget();
   }

   public boolean isEnableDynamicQueries() {
      return this.beanTreeNode.isEnableDynamicQueries();
   }

   public void setEnableDynamicQueries(boolean var1) {
      this.beanTreeNode.setEnableDynamicQueries(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableDynamicQueries", (Object)null, (Object)null));
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
