package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JMSConnectionFactoryBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JMSConnectionFactoryBean beanTreeNode;

   public JMSConnectionFactoryBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JMSConnectionFactoryBean)var2;
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
      return this.getName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Name: ");
      var1.append(this.beanTreeNode.getName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getName() {
      return this.beanTreeNode.getName();
   }

   public void setName(String var1) {
      this.beanTreeNode.setName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Name", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getJNDIName() {
      return this.beanTreeNode.getJNDIName();
   }

   public void setJNDIName(String var1) {
      this.beanTreeNode.setJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLocalJNDIName() {
      return this.beanTreeNode.getLocalJNDIName();
   }

   public void setLocalJNDIName(String var1) {
      this.beanTreeNode.setLocalJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LocalJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public DefaultDeliveryParamsBean getDefaultDeliveryParams() {
      return this.beanTreeNode.getDefaultDeliveryParams();
   }

   public ClientParamsBean getClientParams() {
      return this.beanTreeNode.getClientParams();
   }

   public TransactionParamsBean getTransactionParams() {
      return this.beanTreeNode.getTransactionParams();
   }

   public FlowControlParamsBean getFlowControlParams() {
      return this.beanTreeNode.getFlowControlParams();
   }

   public LoadBalancingParamsBean getLoadBalancingParams() {
      return this.beanTreeNode.getLoadBalancingParams();
   }

   public SecurityParamsBean getSecurityParams() {
      return this.beanTreeNode.getSecurityParams();
   }
}
