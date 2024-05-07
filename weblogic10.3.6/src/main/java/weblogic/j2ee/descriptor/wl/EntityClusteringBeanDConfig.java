package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class EntityClusteringBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private EntityClusteringBean beanTreeNode;

   public EntityClusteringBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (EntityClusteringBean)var2;
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

   public boolean isHomeIsClusterable() {
      return this.beanTreeNode.isHomeIsClusterable();
   }

   public void setHomeIsClusterable(boolean var1) {
      this.beanTreeNode.setHomeIsClusterable(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HomeIsClusterable", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getHomeLoadAlgorithm() {
      return this.beanTreeNode.getHomeLoadAlgorithm();
   }

   public void setHomeLoadAlgorithm(String var1) {
      this.beanTreeNode.setHomeLoadAlgorithm(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HomeLoadAlgorithm", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getHomeCallRouterClassName() {
      return this.beanTreeNode.getHomeCallRouterClassName();
   }

   public void setHomeCallRouterClassName(String var1) {
      this.beanTreeNode.setHomeCallRouterClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HomeCallRouterClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUseServersideStubs() {
      return this.beanTreeNode.isUseServersideStubs();
   }

   public void setUseServersideStubs(boolean var1) {
      this.beanTreeNode.setUseServersideStubs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseServersideStubs", (Object)null, (Object)null));
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
