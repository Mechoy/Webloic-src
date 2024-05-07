package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ModuleDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ModuleDescriptorBean beanTreeNode;

   public ModuleDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ModuleDescriptorBean)var2;
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
      return this.getUri();
   }

   public void initKeyPropertyValue(String var1) {
      this.setUri(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Uri: ");
      var1.append(this.beanTreeNode.getUri());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getRootElement() {
      return this.beanTreeNode.getRootElement();
   }

   public void setRootElement(String var1) {
      this.beanTreeNode.setRootElement(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RootElement", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUri() {
      return this.beanTreeNode.getUri();
   }

   public void setUri(String var1) {
      this.beanTreeNode.setUri(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Uri", (Object)null, (Object)null));
      this.setModified(true);
   }

   public VariableAssignmentBean[] getVariableAssignments() {
      return this.beanTreeNode.getVariableAssignments();
   }

   public String getHashCode() {
      return this.beanTreeNode.getHashCode();
   }

   public void setHashCode(String var1) {
      this.beanTreeNode.setHashCode(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HashCode", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isExternal() {
      return this.beanTreeNode.isExternal();
   }

   public void setExternal(boolean var1) {
      this.beanTreeNode.setExternal(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "External", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isChanged() {
      return this.beanTreeNode.isChanged();
   }

   public void setChanged(boolean var1) {
      this.beanTreeNode.setChanged(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Changed", (Object)null, (Object)null));
      this.setModified(true);
   }
}
