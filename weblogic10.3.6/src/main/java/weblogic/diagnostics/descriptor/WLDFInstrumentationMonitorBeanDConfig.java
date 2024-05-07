package weblogic.diagnostics.descriptor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WLDFInstrumentationMonitorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WLDFInstrumentationMonitorBean beanTreeNode;

   public WLDFInstrumentationMonitorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WLDFInstrumentationMonitorBean)var2;
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

   public String getDescription() {
      return this.beanTreeNode.getDescription();
   }

   public void setDescription(String var1) {
      this.beanTreeNode.setDescription(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Description", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isEnabled() {
      return this.beanTreeNode.isEnabled();
   }

   public void setEnabled(boolean var1) {
      this.beanTreeNode.setEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Enabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDyeMask() {
      return this.beanTreeNode.getDyeMask();
   }

   public void setDyeMask(String var1) {
      this.beanTreeNode.setDyeMask(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DyeMask", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDyeFilteringEnabled() {
      return this.beanTreeNode.isDyeFilteringEnabled();
   }

   public void setDyeFilteringEnabled(boolean var1) {
      this.beanTreeNode.setDyeFilteringEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DyeFilteringEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getProperties() {
      return this.beanTreeNode.getProperties();
   }

   public void setProperties(String var1) {
      this.beanTreeNode.setProperties(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Properties", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getActions() {
      return this.beanTreeNode.getActions();
   }

   public void setActions(String[] var1) {
      this.beanTreeNode.setActions(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Actions", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLocationType() {
      return this.beanTreeNode.getLocationType();
   }

   public void setLocationType(String var1) {
      this.beanTreeNode.setLocationType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LocationType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPointcut() {
      return this.beanTreeNode.getPointcut();
   }

   public void setPointcut(String var1) {
      this.beanTreeNode.setPointcut(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Pointcut", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getIncludes() {
      return this.beanTreeNode.getIncludes();
   }

   public void setIncludes(String[] var1) {
      this.beanTreeNode.setIncludes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Includes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getExcludes() {
      return this.beanTreeNode.getExcludes();
   }

   public void setExcludes(String[] var1) {
      this.beanTreeNode.setExcludes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Excludes", (Object)null, (Object)null));
      this.setModified(true);
   }
}
