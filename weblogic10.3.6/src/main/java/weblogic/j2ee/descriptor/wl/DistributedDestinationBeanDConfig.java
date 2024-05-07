package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DistributedDestinationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DistributedDestinationBean beanTreeNode;

   public DistributedDestinationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DistributedDestinationBean)var2;
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

   public String getJNDIName() {
      return this.beanTreeNode.getJNDIName();
   }

   public void setJNDIName(String var1) {
      this.beanTreeNode.setJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLoadBalancingPolicy() {
      return this.beanTreeNode.getLoadBalancingPolicy();
   }

   public void setLoadBalancingPolicy(String var1) {
      this.beanTreeNode.setLoadBalancingPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoadBalancingPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUnitOfOrderRouting() {
      return this.beanTreeNode.getUnitOfOrderRouting();
   }

   public void setUnitOfOrderRouting(String var1) {
      this.beanTreeNode.setUnitOfOrderRouting(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UnitOfOrderRouting", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSAFExportPolicy() {
      return this.beanTreeNode.getSAFExportPolicy();
   }

   public void setSAFExportPolicy(String var1) {
      this.beanTreeNode.setSAFExportPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SAFExportPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }
}
