package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class FlowControlParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private FlowControlParamsBean beanTreeNode;

   public FlowControlParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (FlowControlParamsBean)var2;
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

   public int getFlowMinimum() {
      return this.beanTreeNode.getFlowMinimum();
   }

   public void setFlowMinimum(int var1) {
      this.beanTreeNode.setFlowMinimum(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FlowMinimum", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getFlowMaximum() {
      return this.beanTreeNode.getFlowMaximum();
   }

   public void setFlowMaximum(int var1) {
      this.beanTreeNode.setFlowMaximum(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FlowMaximum", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getFlowInterval() {
      return this.beanTreeNode.getFlowInterval();
   }

   public void setFlowInterval(int var1) {
      this.beanTreeNode.setFlowInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FlowInterval", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getFlowSteps() {
      return this.beanTreeNode.getFlowSteps();
   }

   public void setFlowSteps(int var1) {
      this.beanTreeNode.setFlowSteps(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FlowSteps", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isFlowControlEnabled() {
      return this.beanTreeNode.isFlowControlEnabled();
   }

   public void setFlowControlEnabled(boolean var1) {
      this.beanTreeNode.setFlowControlEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FlowControlEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOneWaySendMode() {
      return this.beanTreeNode.getOneWaySendMode();
   }

   public void setOneWaySendMode(String var1) {
      this.beanTreeNode.setOneWaySendMode(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OneWaySendMode", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getOneWaySendWindowSize() {
      return this.beanTreeNode.getOneWaySendWindowSize();
   }

   public void setOneWaySendWindowSize(int var1) {
      this.beanTreeNode.setOneWaySendWindowSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OneWaySendWindowSize", (Object)null, (Object)null));
      this.setModified(true);
   }
}
