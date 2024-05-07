package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DefaultPersistentStoreBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DefaultPersistentStoreBean beanTreeNode;

   public DefaultPersistentStoreBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DefaultPersistentStoreBean)var2;
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

   public String getNotes() {
      return this.beanTreeNode.getNotes();
   }

   public void setNotes(String var1) {
      this.beanTreeNode.setNotes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Notes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDirectoryPath() {
      return this.beanTreeNode.getDirectoryPath();
   }

   public void setDirectoryPath(String var1) {
      this.beanTreeNode.setDirectoryPath(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DirectoryPath", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSynchronousWritePolicy() {
      return this.beanTreeNode.getSynchronousWritePolicy();
   }

   public void setSynchronousWritePolicy(String var1) {
      this.beanTreeNode.setSynchronousWritePolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SynchronousWritePolicy", (Object)null, (Object)null));
      this.setModified(true);
   }
}
