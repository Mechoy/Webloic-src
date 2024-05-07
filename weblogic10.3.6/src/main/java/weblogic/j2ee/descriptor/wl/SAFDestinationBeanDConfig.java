package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class SAFDestinationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SAFDestinationBean beanTreeNode;

   public SAFDestinationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SAFDestinationBean)var2;
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

   public String getRemoteJNDIName() {
      return this.beanTreeNode.getRemoteJNDIName();
   }

   public void setRemoteJNDIName(String var1) {
      this.beanTreeNode.setRemoteJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RemoteJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLocalJNDIName() {
      return this.beanTreeNode.getLocalJNDIName();
   }

   public void setLocalJNDIName(String var1) {
      this.beanTreeNode.setLocalJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LocalJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentQos() {
      return this.beanTreeNode.getPersistentQos();
   }

   public void setPersistentQos(String var1) {
      this.beanTreeNode.setPersistentQos(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentQos", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getNonPersistentQos() {
      return this.beanTreeNode.getNonPersistentQos();
   }

   public void setNonPersistentQos(String var1) {
      this.beanTreeNode.setNonPersistentQos(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NonPersistentQos", (Object)null, (Object)null));
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
