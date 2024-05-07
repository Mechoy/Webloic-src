package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ApplicationPoolParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ApplicationPoolParamsBean beanTreeNode;

   public ApplicationPoolParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ApplicationPoolParamsBean)var2;
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

   public SizeParamsBean getSizeParams() {
      return this.beanTreeNode.getSizeParams();
   }

   public XAParamsBean getXAParams() {
      return this.beanTreeNode.getXAParams();
   }

   public int getLoginDelaySeconds() {
      return this.beanTreeNode.getLoginDelaySeconds();
   }

   public void setLoginDelaySeconds(int var1) {
      this.beanTreeNode.setLoginDelaySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoginDelaySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isLeakProfilingEnabled() {
      return this.beanTreeNode.isLeakProfilingEnabled();
   }

   public void setLeakProfilingEnabled(boolean var1) {
      this.beanTreeNode.setLeakProfilingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LeakProfilingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ConnectionCheckParamsBean getConnectionCheckParams() {
      return this.beanTreeNode.getConnectionCheckParams();
   }

   public int getJDBCXADebugLevel() {
      return this.beanTreeNode.getJDBCXADebugLevel();
   }

   public void setJDBCXADebugLevel(int var1) {
      this.beanTreeNode.setJDBCXADebugLevel(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JDBCXADebugLevel", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRemoveInfectedConnectionsEnabled() {
      return this.beanTreeNode.isRemoveInfectedConnectionsEnabled();
   }

   public void setRemoveInfectedConnectionsEnabled(boolean var1) {
      this.beanTreeNode.setRemoveInfectedConnectionsEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RemoveInfectedConnectionsEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }
}
