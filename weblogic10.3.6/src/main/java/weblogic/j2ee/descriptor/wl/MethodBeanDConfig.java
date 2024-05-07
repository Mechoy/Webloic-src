package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MethodBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MethodBean beanTreeNode;

   public MethodBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MethodBean)var2;
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

   public String getDescription() {
      return this.beanTreeNode.getDescription();
   }

   public void setDescription(String var1) {
      this.beanTreeNode.setDescription(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Description", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getEjbName() {
      return this.beanTreeNode.getEjbName();
   }

   public void setEjbName(String var1) {
      this.beanTreeNode.setEjbName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EjbName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMethodIntf() {
      return this.beanTreeNode.getMethodIntf();
   }

   public void setMethodIntf(String var1) {
      this.beanTreeNode.setMethodIntf(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MethodIntf", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMethodName() {
      return this.beanTreeNode.getMethodName();
   }

   public void setMethodName(String var1) {
      this.beanTreeNode.setMethodName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MethodName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MethodParamsBean getMethodParams() {
      return this.beanTreeNode.getMethodParams();
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
