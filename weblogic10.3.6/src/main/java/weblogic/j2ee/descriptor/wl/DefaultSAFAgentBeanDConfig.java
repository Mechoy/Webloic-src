package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DefaultSAFAgentBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DefaultSAFAgentBean beanTreeNode;

   public DefaultSAFAgentBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DefaultSAFAgentBean)var2;
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

   public String getNotes() {
      return this.beanTreeNode.getNotes();
   }

   public void setNotes(String var1) {
      this.beanTreeNode.setNotes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Notes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getBytesMaximum() {
      return this.beanTreeNode.getBytesMaximum();
   }

   public void setBytesMaximum(long var1) {
      this.beanTreeNode.setBytesMaximum(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BytesMaximum", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getMessagesMaximum() {
      return this.beanTreeNode.getMessagesMaximum();
   }

   public void setMessagesMaximum(long var1) {
      this.beanTreeNode.setMessagesMaximum(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessagesMaximum", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaximumMessageSize() {
      return this.beanTreeNode.getMaximumMessageSize();
   }

   public void setMaximumMessageSize(int var1) {
      this.beanTreeNode.setMaximumMessageSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaximumMessageSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getDefaultRetryDelayBase() {
      return this.beanTreeNode.getDefaultRetryDelayBase();
   }

   public void setDefaultRetryDelayBase(long var1) {
      this.beanTreeNode.setDefaultRetryDelayBase(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultRetryDelayBase", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getDefaultRetryDelayMaximum() {
      return this.beanTreeNode.getDefaultRetryDelayMaximum();
   }

   public void setDefaultRetryDelayMaximum(long var1) {
      this.beanTreeNode.setDefaultRetryDelayMaximum(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultRetryDelayMaximum", (Object)null, (Object)null));
      this.setModified(true);
   }

   public double getDefaultRetryDelayMultiplier() {
      return this.beanTreeNode.getDefaultRetryDelayMultiplier();
   }

   public void setDefaultRetryDelayMultiplier(double var1) {
      this.beanTreeNode.setDefaultRetryDelayMultiplier(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultRetryDelayMultiplier", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getWindowSize() {
      return this.beanTreeNode.getWindowSize();
   }

   public void setWindowSize(int var1) {
      this.beanTreeNode.setWindowSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WindowSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isLoggingEnabled() {
      return this.beanTreeNode.isLoggingEnabled();
   }

   public void setLoggingEnabled(boolean var1) {
      this.beanTreeNode.setLoggingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoggingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getDefaultTimeToLive() {
      return this.beanTreeNode.getDefaultTimeToLive();
   }

   public void setDefaultTimeToLive(long var1) {
      this.beanTreeNode.setDefaultTimeToLive(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultTimeToLive", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getMessageBufferSize() {
      return this.beanTreeNode.getMessageBufferSize();
   }

   public void setMessageBufferSize(long var1) {
      this.beanTreeNode.setMessageBufferSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessageBufferSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPagingDirectory() {
      return this.beanTreeNode.getPagingDirectory();
   }

   public void setPagingDirectory(String var1) {
      this.beanTreeNode.setPagingDirectory(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PagingDirectory", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getWindowInterval() {
      return this.beanTreeNode.getWindowInterval();
   }

   public void setWindowInterval(long var1) {
      this.beanTreeNode.setWindowInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WindowInterval", (Object)null, (Object)null));
      this.setModified(true);
   }
}
