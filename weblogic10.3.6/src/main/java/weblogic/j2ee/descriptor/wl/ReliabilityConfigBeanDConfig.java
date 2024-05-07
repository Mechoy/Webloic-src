package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ReliabilityConfigBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ReliabilityConfigBean beanTreeNode;

   public ReliabilityConfigBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ReliabilityConfigBean)var2;
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

   public boolean isCustomized() {
      return this.beanTreeNode.isCustomized();
   }

   public void setCustomized(boolean var1) {
      this.beanTreeNode.setCustomized(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Customized", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getInactivityTimeout() {
      return this.beanTreeNode.getInactivityTimeout();
   }

   public void setInactivityTimeout(String var1) {
      this.beanTreeNode.setInactivityTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InactivityTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getBaseRetransmissionInterval() {
      return this.beanTreeNode.getBaseRetransmissionInterval();
   }

   public void setBaseRetransmissionInterval(String var1) {
      this.beanTreeNode.setBaseRetransmissionInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BaseRetransmissionInterval", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getRetransmissionExponentialBackoff() {
      return this.beanTreeNode.getRetransmissionExponentialBackoff();
   }

   public void setRetransmissionExponentialBackoff(boolean var1) {
      this.beanTreeNode.setRetransmissionExponentialBackoff(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RetransmissionExponentialBackoff", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getNonBufferedSource() {
      return this.beanTreeNode.getNonBufferedSource();
   }

   public void setNonBufferedSource(boolean var1) {
      this.beanTreeNode.setNonBufferedSource(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NonBufferedSource", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getAcknowledgementInterval() {
      return this.beanTreeNode.getAcknowledgementInterval();
   }

   public void setAcknowledgementInterval(String var1) {
      this.beanTreeNode.setAcknowledgementInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AcknowledgementInterval", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSequenceExpiration() {
      return this.beanTreeNode.getSequenceExpiration();
   }

   public void setSequenceExpiration(String var1) {
      this.beanTreeNode.setSequenceExpiration(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SequenceExpiration", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getBufferRetryCount() {
      return this.beanTreeNode.getBufferRetryCount();
   }

   public void setBufferRetryCount(int var1) {
      this.beanTreeNode.setBufferRetryCount(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BufferRetryCount", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getBufferRetryDelay() {
      return this.beanTreeNode.getBufferRetryDelay();
   }

   public void setBufferRetryDelay(String var1) {
      this.beanTreeNode.setBufferRetryDelay(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BufferRetryDelay", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getNonBufferedDestination() {
      return this.beanTreeNode.getNonBufferedDestination();
   }

   public void setNonBufferedDestination(boolean var1) {
      this.beanTreeNode.setNonBufferedDestination(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NonBufferedDestination", (Object)null, (Object)null));
      this.setModified(true);
   }
}
