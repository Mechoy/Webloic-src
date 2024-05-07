package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class FastSwapBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private FastSwapBean beanTreeNode;

   public FastSwapBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (FastSwapBean)var2;
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

   public boolean isEnabled() {
      return this.beanTreeNode.isEnabled();
   }

   public void setEnabled(boolean var1) {
      this.beanTreeNode.setEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Enabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getRefreshInterval() {
      return this.beanTreeNode.getRefreshInterval();
   }

   public void setRefreshInterval(int var1) {
      this.beanTreeNode.setRefreshInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RefreshInterval", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getRedefinitionTaskLimit() {
      return this.beanTreeNode.getRedefinitionTaskLimit();
   }

   public void setRedefinitionTaskLimit(int var1) {
      this.beanTreeNode.setRedefinitionTaskLimit(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RedefinitionTaskLimit", (Object)null, (Object)null));
      this.setModified(true);
   }
}
