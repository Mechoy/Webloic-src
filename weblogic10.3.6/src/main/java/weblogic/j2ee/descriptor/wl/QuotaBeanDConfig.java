package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class QuotaBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private QuotaBean beanTreeNode;

   public QuotaBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (QuotaBean)var2;
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

   public String getPolicy() {
      return this.beanTreeNode.getPolicy();
   }

   public void setPolicy(String var1) {
      this.beanTreeNode.setPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Policy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isShared() {
      return this.beanTreeNode.isShared();
   }

   public void setShared(boolean var1) {
      this.beanTreeNode.setShared(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Shared", (Object)null, (Object)null));
      this.setModified(true);
   }
}
