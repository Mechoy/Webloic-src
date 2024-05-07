package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DeliveryFailureParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DeliveryFailureParamsBean beanTreeNode;

   public DeliveryFailureParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DeliveryFailureParamsBean)var2;
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

   public DestinationBean getErrorDestination() {
      return this.beanTreeNode.getErrorDestination();
   }

   public void setErrorDestination(DestinationBean var1) {
      this.beanTreeNode.setErrorDestination(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ErrorDestination", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getRedeliveryLimit() {
      return this.beanTreeNode.getRedeliveryLimit();
   }

   public void setRedeliveryLimit(int var1) {
      this.beanTreeNode.setRedeliveryLimit(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RedeliveryLimit", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getExpirationPolicy() {
      return this.beanTreeNode.getExpirationPolicy();
   }

   public void setExpirationPolicy(String var1) {
      this.beanTreeNode.setExpirationPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ExpirationPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getExpirationLoggingPolicy() {
      return this.beanTreeNode.getExpirationLoggingPolicy();
   }

   public void setExpirationLoggingPolicy(String var1) {
      this.beanTreeNode.setExpirationLoggingPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ExpirationLoggingPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public TemplateBean getTemplateBean() {
      return this.beanTreeNode.getTemplateBean();
   }
}
