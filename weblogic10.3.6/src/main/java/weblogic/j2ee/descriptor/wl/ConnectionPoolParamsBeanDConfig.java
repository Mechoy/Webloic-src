package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ConnectionPoolParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ConnectionPoolParamsBean beanTreeNode;

   public ConnectionPoolParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ConnectionPoolParamsBean)var2;
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

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.beanTreeNode.getConnectionCreationRetryFrequencySeconds();
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) {
      this.beanTreeNode.setConnectionCreationRetryFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionCreationRetryFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getConnectionReserveTimeoutSeconds() {
      return this.beanTreeNode.getConnectionReserveTimeoutSeconds();
   }

   public void setConnectionReserveTimeoutSeconds(int var1) {
      this.beanTreeNode.setConnectionReserveTimeoutSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionReserveTimeoutSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getTestFrequencySeconds() {
      return this.beanTreeNode.getTestFrequencySeconds();
   }

   public void setTestFrequencySeconds(int var1) {
      this.beanTreeNode.setTestFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTestConnectionsOnCreate() {
      return this.beanTreeNode.isTestConnectionsOnCreate();
   }

   public void setTestConnectionsOnCreate(boolean var1) {
      this.beanTreeNode.setTestConnectionsOnCreate(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestConnectionsOnCreate", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTestConnectionsOnRelease() {
      return this.beanTreeNode.isTestConnectionsOnRelease();
   }

   public void setTestConnectionsOnRelease(boolean var1) {
      this.beanTreeNode.setTestConnectionsOnRelease(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestConnectionsOnRelease", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTestConnectionsOnReserve() {
      return this.beanTreeNode.isTestConnectionsOnReserve();
   }

   public void setTestConnectionsOnReserve(boolean var1) {
      this.beanTreeNode.setTestConnectionsOnReserve(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestConnectionsOnReserve", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getProfileHarvestFrequencySeconds() {
      return this.beanTreeNode.getProfileHarvestFrequencySeconds();
   }

   public void setProfileHarvestFrequencySeconds(int var1) {
      this.beanTreeNode.setProfileHarvestFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ProfileHarvestFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIgnoreInUseConnectionsEnabled() {
      return this.beanTreeNode.isIgnoreInUseConnectionsEnabled();
   }

   public void setIgnoreInUseConnectionsEnabled(boolean var1) {
      this.beanTreeNode.setIgnoreInUseConnectionsEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IgnoreInUseConnectionsEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }
}
