package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DistributedQueueBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DistributedQueueBean beanTreeNode;

   public DistributedQueueBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DistributedQueueBean)var2;
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

   public DistributedDestinationMemberBean[] getDistributedQueueMembers() {
      return this.beanTreeNode.getDistributedQueueMembers();
   }

   public int getForwardDelay() {
      return this.beanTreeNode.getForwardDelay();
   }

   public void setForwardDelay(int var1) {
      this.beanTreeNode.setForwardDelay(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ForwardDelay", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getResetDeliveryCountOnForward() {
      return this.beanTreeNode.getResetDeliveryCountOnForward();
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      this.beanTreeNode.setResetDeliveryCountOnForward(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResetDeliveryCountOnForward", (Object)null, (Object)null));
      this.setModified(true);
   }
}
