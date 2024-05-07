package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class SAFImportedDestinationsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SAFImportedDestinationsBean beanTreeNode;

   public SAFImportedDestinationsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SAFImportedDestinationsBean)var2;
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
      var1.append("UnitOfOrderRouting: ");
      var1.append(this.beanTreeNode.getUnitOfOrderRouting());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public SAFQueueBean[] getSAFQueues() {
      return this.beanTreeNode.getSAFQueues();
   }

   public SAFTopicBean[] getSAFTopics() {
      return this.beanTreeNode.getSAFTopics();
   }

   public String getJNDIPrefix() {
      return this.beanTreeNode.getJNDIPrefix();
   }

   public void setJNDIPrefix(String var1) {
      this.beanTreeNode.setJNDIPrefix(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIPrefix", (Object)null, (Object)null));
      this.setModified(true);
   }

   public SAFRemoteContextBean getSAFRemoteContext() {
      return this.beanTreeNode.getSAFRemoteContext();
   }

   public void setSAFRemoteContext(SAFRemoteContextBean var1) {
      this.beanTreeNode.setSAFRemoteContext(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SAFRemoteContext", (Object)null, (Object)null));
      this.setModified(true);
   }

   public SAFErrorHandlingBean getSAFErrorHandling() {
      return this.beanTreeNode.getSAFErrorHandling();
   }

   public void setSAFErrorHandling(SAFErrorHandlingBean var1) {
      this.beanTreeNode.setSAFErrorHandling(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SAFErrorHandling", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getTimeToLiveDefault() {
      return this.beanTreeNode.getTimeToLiveDefault();
   }

   public void setTimeToLiveDefault(long var1) {
      this.beanTreeNode.setTimeToLiveDefault(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TimeToLiveDefault", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUseSAFTimeToLiveDefault() {
      return this.beanTreeNode.isUseSAFTimeToLiveDefault();
   }

   public void setUseSAFTimeToLiveDefault(boolean var1) {
      this.beanTreeNode.setUseSAFTimeToLiveDefault(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseSAFTimeToLiveDefault", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUnitOfOrderRouting() {
      return this.beanTreeNode.getUnitOfOrderRouting();
   }

   public void setUnitOfOrderRouting(String var1) {
      this.beanTreeNode.setUnitOfOrderRouting(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UnitOfOrderRouting", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MessageLoggingParamsBean getMessageLoggingParams() {
      return this.beanTreeNode.getMessageLoggingParams();
   }
}
