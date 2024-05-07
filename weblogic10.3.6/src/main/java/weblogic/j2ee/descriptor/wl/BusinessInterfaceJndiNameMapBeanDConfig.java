package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class BusinessInterfaceJndiNameMapBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private BusinessInterfaceJndiNameMapBean beanTreeNode;

   public BusinessInterfaceJndiNameMapBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (BusinessInterfaceJndiNameMapBean)var2;
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
      return this.getBusinessRemote();
   }

   public void initKeyPropertyValue(String var1) {
      this.setBusinessRemote(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("BusinessRemote: ");
      var1.append(this.beanTreeNode.getBusinessRemote());
      var1.append("\n");
      var1.append("JNDIName: ");
      var1.append(this.beanTreeNode.getJNDIName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getBusinessRemote() {
      return this.beanTreeNode.getBusinessRemote();
   }

   public void setBusinessRemote(String var1) {
      this.beanTreeNode.setBusinessRemote(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BusinessRemote", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getJNDIName() {
      return this.beanTreeNode.getJNDIName();
   }

   public void setJNDIName(String var1) {
      this.beanTreeNode.setJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }
}
