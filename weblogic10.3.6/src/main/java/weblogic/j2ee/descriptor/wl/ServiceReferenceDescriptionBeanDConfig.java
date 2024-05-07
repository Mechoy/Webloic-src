package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ServiceReferenceDescriptionBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ServiceReferenceDescriptionBean beanTreeNode;

   public ServiceReferenceDescriptionBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ServiceReferenceDescriptionBean)var2;
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
      return this.getServiceRefName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setServiceRefName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ServiceRefName: ");
      var1.append(this.beanTreeNode.getServiceRefName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getServiceRefName() {
      return this.beanTreeNode.getServiceRefName();
   }

   public void setServiceRefName(String var1) {
      this.beanTreeNode.setServiceRefName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ServiceRefName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getWsdlUrl() {
      return this.beanTreeNode.getWsdlUrl();
   }

   public void setWsdlUrl(String var1) {
      this.beanTreeNode.setWsdlUrl(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WsdlUrl", (Object)null, (Object)null));
      this.setModified(true);
   }

   public PropertyNamevalueBean[] getCallProperties() {
      return this.beanTreeNode.getCallProperties();
   }

   public PortInfoBean[] getPortInfos() {
      return this.beanTreeNode.getPortInfos();
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
