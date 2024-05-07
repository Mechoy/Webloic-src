package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ProxyBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ProxyBean beanTreeNode;

   public ProxyBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ProxyBean)var2;
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

   public int getInactiveConnectionTimeoutSeconds() {
      return this.beanTreeNode.getInactiveConnectionTimeoutSeconds();
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) {
      this.beanTreeNode.setInactiveConnectionTimeoutSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InactiveConnectionTimeoutSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isConnectionProfilingEnabled() {
      return this.beanTreeNode.isConnectionProfilingEnabled();
   }

   public void setConnectionProfilingEnabled(boolean var1) {
      this.beanTreeNode.setConnectionProfilingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionProfilingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUseConnectionProxies() {
      return this.beanTreeNode.getUseConnectionProxies();
   }

   public void setUseConnectionProxies(String var1) {
      this.beanTreeNode.setUseConnectionProxies(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseConnectionProxies", (Object)null, (Object)null));
      this.setModified(true);
   }
}
