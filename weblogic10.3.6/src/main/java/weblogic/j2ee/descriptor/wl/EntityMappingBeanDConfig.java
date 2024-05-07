package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class EntityMappingBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private EntityMappingBean beanTreeNode;

   public EntityMappingBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (EntityMappingBean)var2;
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
      return this.getEntityMappingName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setEntityMappingName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EntityMappingName: ");
      var1.append(this.beanTreeNode.getEntityMappingName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getEntityMappingName() {
      return this.beanTreeNode.getEntityMappingName();
   }

   public void setEntityMappingName(String var1) {
      this.beanTreeNode.setEntityMappingName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EntityMappingName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPublicId() {
      return this.beanTreeNode.getPublicId();
   }

   public void setPublicId(String var1) {
      this.beanTreeNode.setPublicId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PublicId", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSystemId() {
      return this.beanTreeNode.getSystemId();
   }

   public void setSystemId(String var1) {
      this.beanTreeNode.setSystemId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SystemId", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getEntityUri() {
      return this.beanTreeNode.getEntityUri();
   }

   public void setEntityUri(String var1) {
      this.beanTreeNode.setEntityUri(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EntityUri", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getWhenToCache() {
      return this.beanTreeNode.getWhenToCache();
   }

   public void setWhenToCache(String var1) {
      this.beanTreeNode.setWhenToCache(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WhenToCache", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCacheTimeoutInterval() {
      return this.beanTreeNode.getCacheTimeoutInterval();
   }

   public void setCacheTimeoutInterval(int var1) {
      this.beanTreeNode.setCacheTimeoutInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CacheTimeoutInterval", (Object)null, (Object)null));
      this.setModified(true);
   }
}
