package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class PreparedStatementBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private PreparedStatementBean beanTreeNode;

   public PreparedStatementBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (PreparedStatementBean)var2;
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

   public boolean isProfilingEnabled() {
      return this.beanTreeNode.isProfilingEnabled();
   }

   public void setProfilingEnabled(boolean var1) {
      this.beanTreeNode.setProfilingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ProfilingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCacheProfilingThreshold() {
      return this.beanTreeNode.getCacheProfilingThreshold();
   }

   public void setCacheProfilingThreshold(int var1) {
      this.beanTreeNode.setCacheProfilingThreshold(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CacheProfilingThreshold", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCacheSize() {
      return this.beanTreeNode.getCacheSize();
   }

   public void setCacheSize(int var1) {
      this.beanTreeNode.setCacheSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CacheSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isParameterLoggingEnabled() {
      return this.beanTreeNode.isParameterLoggingEnabled();
   }

   public void setParameterLoggingEnabled(boolean var1) {
      this.beanTreeNode.setParameterLoggingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ParameterLoggingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxParameterLength() {
      return this.beanTreeNode.getMaxParameterLength();
   }

   public void setMaxParameterLength(int var1) {
      this.beanTreeNode.setMaxParameterLength(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxParameterLength", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCacheType() {
      return this.beanTreeNode.getCacheType();
   }

   public void setCacheType(int var1) {
      this.beanTreeNode.setCacheType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CacheType", (Object)null, (Object)null));
      this.setModified(true);
   }
}
