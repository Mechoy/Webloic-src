package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ThresholdParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ThresholdParamsBean beanTreeNode;

   public ThresholdParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ThresholdParamsBean)var2;
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

   public long getBytesHigh() {
      return this.beanTreeNode.getBytesHigh();
   }

   public void setBytesHigh(long var1) {
      this.beanTreeNode.setBytesHigh(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BytesHigh", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getBytesLow() {
      return this.beanTreeNode.getBytesLow();
   }

   public void setBytesLow(long var1) {
      this.beanTreeNode.setBytesLow(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BytesLow", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getMessagesHigh() {
      return this.beanTreeNode.getMessagesHigh();
   }

   public void setMessagesHigh(long var1) {
      this.beanTreeNode.setMessagesHigh(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessagesHigh", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getMessagesLow() {
      return this.beanTreeNode.getMessagesLow();
   }

   public void setMessagesLow(long var1) {
      this.beanTreeNode.setMessagesLow(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MessagesLow", (Object)null, (Object)null));
      this.setModified(true);
   }

   public TemplateBean getTemplateBean() {
      return this.beanTreeNode.getTemplateBean();
   }
}
