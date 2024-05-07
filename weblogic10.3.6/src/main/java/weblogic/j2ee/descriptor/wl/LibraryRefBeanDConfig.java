package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class LibraryRefBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private LibraryRefBean beanTreeNode;

   public LibraryRefBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (LibraryRefBean)var2;
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

   public String getLibraryName() {
      return this.beanTreeNode.getLibraryName();
   }

   public void setLibraryName(String var1) {
      this.beanTreeNode.setLibraryName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LibraryName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSpecificationVersion() {
      return this.beanTreeNode.getSpecificationVersion();
   }

   public void setSpecificationVersion(String var1) {
      this.beanTreeNode.setSpecificationVersion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SpecificationVersion", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getImplementationVersion() {
      return this.beanTreeNode.getImplementationVersion();
   }

   public void setImplementationVersion(String var1) {
      this.beanTreeNode.setImplementationVersion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ImplementationVersion", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getExactMatch() {
      return this.beanTreeNode.getExactMatch();
   }

   public void setExactMatch(boolean var1) {
      this.beanTreeNode.setExactMatch(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ExactMatch", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getContextRoot() {
      return this.beanTreeNode.getContextRoot();
   }

   public void setContextRoot(String var1) {
      this.beanTreeNode.setContextRoot(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ContextRoot", (Object)null, (Object)null));
      this.setModified(true);
   }
}
