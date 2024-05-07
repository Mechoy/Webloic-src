package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ListenerBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ListenerBean beanTreeNode;

   public ListenerBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ListenerBean)var2;
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

   public String getListenerClass() {
      return this.beanTreeNode.getListenerClass();
   }

   public void setListenerClass(String var1) {
      this.beanTreeNode.setListenerClass(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ListenerClass", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getListenerUri() {
      return this.beanTreeNode.getListenerUri();
   }

   public void setListenerUri(String var1) {
      this.beanTreeNode.setListenerUri(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ListenerUri", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRunAsPrincipalName() {
      return this.beanTreeNode.getRunAsPrincipalName();
   }

   public void setRunAsPrincipalName(String var1) {
      this.beanTreeNode.setRunAsPrincipalName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RunAsPrincipalName", (Object)null, (Object)null));
      this.setModified(true);
   }
}
