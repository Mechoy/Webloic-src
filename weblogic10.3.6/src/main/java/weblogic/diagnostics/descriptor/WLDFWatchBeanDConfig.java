package weblogic.diagnostics.descriptor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WLDFWatchBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WLDFWatchBean beanTreeNode;

   public WLDFWatchBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WLDFWatchBean)var2;
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

   public String getRuleType() {
      return this.beanTreeNode.getRuleType();
   }

   public void setRuleType(String var1) {
      this.beanTreeNode.setRuleType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RuleType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRuleExpression() {
      return this.beanTreeNode.getRuleExpression();
   }

   public void setRuleExpression(String var1) {
      this.beanTreeNode.setRuleExpression(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RuleExpression", (Object)null, (Object)null));
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

   public String getAlarmType() {
      return this.beanTreeNode.getAlarmType();
   }

   public void setAlarmType(String var1) {
      this.beanTreeNode.setAlarmType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AlarmType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getAlarmResetPeriod() {
      return this.beanTreeNode.getAlarmResetPeriod();
   }

   public void setAlarmResetPeriod(int var1) {
      this.beanTreeNode.setAlarmResetPeriod(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AlarmResetPeriod", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WLDFNotificationBean[] getNotifications() {
      return this.beanTreeNode.getNotifications();
   }

   public void setNotifications(WLDFNotificationBean[] var1) {
      this.beanTreeNode.setNotifications(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Notifications", (Object)null, (Object)null));
      this.setModified(true);
   }
}
