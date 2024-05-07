package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ApplicationAdminModeTriggerBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ApplicationAdminModeTriggerBean beanTreeNode;

   public ApplicationAdminModeTriggerBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ApplicationAdminModeTriggerBean)var2;
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
      return this.getId();
   }

   public void initKeyPropertyValue(String var1) {
      this.setId(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Id: ");
      var1.append(this.beanTreeNode.getId());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public int getMaxStuckThreadTime() {
      return this.beanTreeNode.getMaxStuckThreadTime();
   }

   public void setMaxStuckThreadTime(int var1) {
      this.beanTreeNode.setMaxStuckThreadTime(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxStuckThreadTime", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getStuckThreadCount() {
      return this.beanTreeNode.getStuckThreadCount();
   }

   public void setStuckThreadCount(int var1) {
      this.beanTreeNode.setStuckThreadCount(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StuckThreadCount", (Object)null, (Object)null));
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
