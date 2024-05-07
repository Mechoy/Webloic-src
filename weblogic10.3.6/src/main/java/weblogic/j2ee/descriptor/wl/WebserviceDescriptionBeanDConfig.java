package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WebserviceDescriptionBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WebserviceDescriptionBean beanTreeNode;

   public WebserviceDescriptionBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WebserviceDescriptionBean)var2;
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
      return this.getWebserviceDescriptionName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setWebserviceDescriptionName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("WebserviceDescriptionName: ");
      var1.append(this.beanTreeNode.getWebserviceDescriptionName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getWebserviceDescriptionName() {
      return this.beanTreeNode.getWebserviceDescriptionName();
   }

   public void setWebserviceDescriptionName(String var1) {
      this.beanTreeNode.setWebserviceDescriptionName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WebserviceDescriptionName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getWebserviceType() {
      return this.beanTreeNode.getWebserviceType();
   }

   public void setWebserviceType(String var1) {
      this.beanTreeNode.setWebserviceType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WebserviceType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getWsdlPublishFile() {
      return this.beanTreeNode.getWsdlPublishFile();
   }

   public void setWsdlPublishFile(String var1) {
      this.beanTreeNode.setWsdlPublishFile(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WsdlPublishFile", (Object)null, (Object)null));
      this.setModified(true);
   }

   public PortComponentBean[] getPortComponents() {
      return this.beanTreeNode.getPortComponents();
   }
}
