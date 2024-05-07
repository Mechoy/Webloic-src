package weblogic.diagnostics.descriptor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WLDFJMSNotificationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WLDFJMSNotificationBean beanTreeNode;

   public WLDFJMSNotificationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WLDFJMSNotificationBean)var2;
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

   public String getDestinationJNDIName() {
      return this.beanTreeNode.getDestinationJNDIName();
   }

   public void setDestinationJNDIName(String var1) {
      this.beanTreeNode.setDestinationJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DestinationJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionFactoryJNDIName() {
      return this.beanTreeNode.getConnectionFactoryJNDIName();
   }

   public void setConnectionFactoryJNDIName(String var1) {
      this.beanTreeNode.setConnectionFactoryJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionFactoryJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }
}
