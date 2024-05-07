package weblogic.diagnostics.descriptor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WLDFWatchNotificationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WLDFWatchNotificationBean beanTreeNode;

   public WLDFWatchNotificationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WLDFWatchNotificationBean)var2;
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

   public boolean isEnabled() {
      return this.beanTreeNode.isEnabled();
   }

   public void setEnabled(boolean var1) {
      this.beanTreeNode.setEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Enabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSeverity() {
      return this.beanTreeNode.getSeverity();
   }

   public void setSeverity(String var1) {
      this.beanTreeNode.setSeverity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Severity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLogWatchSeverity() {
      return this.beanTreeNode.getLogWatchSeverity();
   }

   public void setLogWatchSeverity(String var1) {
      this.beanTreeNode.setLogWatchSeverity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LogWatchSeverity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WLDFWatchBean[] getWatches() {
      return this.beanTreeNode.getWatches();
   }

   public WLDFNotificationBean[] getNotifications() {
      return this.beanTreeNode.getNotifications();
   }

   public WLDFImageNotificationBean[] getImageNotifications() {
      return this.beanTreeNode.getImageNotifications();
   }

   public WLDFJMSNotificationBean[] getJMSNotifications() {
      return this.beanTreeNode.getJMSNotifications();
   }

   public WLDFJMXNotificationBean[] getJMXNotifications() {
      return this.beanTreeNode.getJMXNotifications();
   }

   public WLDFSMTPNotificationBean[] getSMTPNotifications() {
      return this.beanTreeNode.getSMTPNotifications();
   }

   public WLDFSNMPNotificationBean[] getSNMPNotifications() {
      return this.beanTreeNode.getSNMPNotifications();
   }
}
