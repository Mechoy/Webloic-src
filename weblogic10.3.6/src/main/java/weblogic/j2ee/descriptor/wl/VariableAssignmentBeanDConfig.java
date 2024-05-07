package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class VariableAssignmentBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private VariableAssignmentBean beanTreeNode;

   public VariableAssignmentBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (VariableAssignmentBean)var2;
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

   public String getName() {
      return this.beanTreeNode.getName();
   }

   public void setName(String var1) {
      this.beanTreeNode.setName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Name", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getXpath() {
      return this.beanTreeNode.getXpath();
   }

   public void setXpath(String var1) {
      this.beanTreeNode.setXpath(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Xpath", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOrigin() {
      return this.beanTreeNode.getOrigin();
   }

   public void setOrigin(String var1) {
      this.beanTreeNode.setOrigin(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Origin", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOperation() {
      return this.beanTreeNode.getOperation();
   }

   public void setOperation(String var1) {
      this.beanTreeNode.setOperation(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Operation", (Object)null, (Object)null));
      this.setModified(true);
   }
}
