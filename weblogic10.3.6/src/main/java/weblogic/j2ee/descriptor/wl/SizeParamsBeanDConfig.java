package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class SizeParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SizeParamsBean beanTreeNode;

   public SizeParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SizeParamsBean)var2;
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

   public int getInitialCapacity() {
      return this.beanTreeNode.getInitialCapacity();
   }

   public void setInitialCapacity(int var1) {
      this.beanTreeNode.setInitialCapacity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitialCapacity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxCapacity() {
      return this.beanTreeNode.getMaxCapacity();
   }

   public void setMaxCapacity(int var1) {
      this.beanTreeNode.setMaxCapacity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxCapacity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCapacityIncrement() {
      return this.beanTreeNode.getCapacityIncrement();
   }

   public void setCapacityIncrement(int var1) {
      this.beanTreeNode.setCapacityIncrement(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CapacityIncrement", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isShrinkingEnabled() {
      return this.beanTreeNode.isShrinkingEnabled();
   }

   public void setShrinkingEnabled(boolean var1) {
      this.beanTreeNode.setShrinkingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ShrinkingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getShrinkPeriodMinutes() {
      return this.beanTreeNode.getShrinkPeriodMinutes();
   }

   public void setShrinkPeriodMinutes(int var1) {
      this.beanTreeNode.setShrinkPeriodMinutes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ShrinkPeriodMinutes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getShrinkFrequencySeconds() {
      return this.beanTreeNode.getShrinkFrequencySeconds();
   }

   public void setShrinkFrequencySeconds(int var1) {
      this.beanTreeNode.setShrinkFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ShrinkFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getHighestNumWaiters() {
      return this.beanTreeNode.getHighestNumWaiters();
   }

   public void setHighestNumWaiters(int var1) {
      this.beanTreeNode.setHighestNumWaiters(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HighestNumWaiters", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getHighestNumUnavailable() {
      return this.beanTreeNode.getHighestNumUnavailable();
   }

   public void setHighestNumUnavailable(int var1) {
      this.beanTreeNode.setHighestNumUnavailable(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HighestNumUnavailable", (Object)null, (Object)null));
      this.setModified(true);
   }
}
