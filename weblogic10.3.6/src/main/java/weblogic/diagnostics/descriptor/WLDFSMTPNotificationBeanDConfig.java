package weblogic.diagnostics.descriptor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WLDFSMTPNotificationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WLDFSMTPNotificationBean beanTreeNode;

   public WLDFSMTPNotificationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WLDFSMTPNotificationBean)var2;
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

   public String getMailSessionJNDIName() {
      return this.beanTreeNode.getMailSessionJNDIName();
   }

   public void setMailSessionJNDIName(String var1) {
      this.beanTreeNode.setMailSessionJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MailSessionJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSubject() {
      return this.beanTreeNode.getSubject();
   }

   public void setSubject(String var1) {
      this.beanTreeNode.setSubject(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Subject", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getBody() {
      return this.beanTreeNode.getBody();
   }

   public void setBody(String var1) {
      this.beanTreeNode.setBody(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Body", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getRecipients() {
      return this.beanTreeNode.getRecipients();
   }

   public void setRecipients(String[] var1) {
      this.beanTreeNode.setRecipients(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Recipients", (Object)null, (Object)null));
      this.setModified(true);
   }
}
