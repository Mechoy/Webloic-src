package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class PersistenceUseBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private PersistenceUseBean beanTreeNode;

   public PersistenceUseBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (PersistenceUseBean)var2;
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

   public String getTypeIdentifier() {
      return this.beanTreeNode.getTypeIdentifier();
   }

   public void setTypeIdentifier(String var1) {
      this.beanTreeNode.setTypeIdentifier(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TypeIdentifier", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTypeVersion() {
      return this.beanTreeNode.getTypeVersion();
   }

   public void setTypeVersion(String var1) {
      this.beanTreeNode.setTypeVersion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TypeVersion", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTypeStorage() {
      return this.beanTreeNode.getTypeStorage();
   }

   public void setTypeStorage(String var1) {
      this.beanTreeNode.setTypeStorage(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TypeStorage", (Object)null, (Object)null));
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