package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class AutomaticKeyGenerationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private AutomaticKeyGenerationBean beanTreeNode;

   public AutomaticKeyGenerationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (AutomaticKeyGenerationBean)var2;
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

   public String getGeneratorType() {
      return this.beanTreeNode.getGeneratorType();
   }

   public void setGeneratorType(String var1) {
      this.beanTreeNode.setGeneratorType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "GeneratorType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getGeneratorName() {
      return this.beanTreeNode.getGeneratorName();
   }

   public void setGeneratorName(String var1) {
      this.beanTreeNode.setGeneratorName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "GeneratorName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getKeyCacheSize() {
      return this.beanTreeNode.getKeyCacheSize();
   }

   public void setKeyCacheSize(int var1) {
      this.beanTreeNode.setKeyCacheSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeyCacheSize", (Object)null, (Object)null));
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

   public boolean getSelectFirstSequenceKeyBeforeUpdate() {
      return this.beanTreeNode.getSelectFirstSequenceKeyBeforeUpdate();
   }

   public void setSelectFirstSequenceKeyBeforeUpdate(boolean var1) {
      this.beanTreeNode.setSelectFirstSequenceKeyBeforeUpdate(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SelectFirstSequenceKeyBeforeUpdate", (Object)null, (Object)null));
      this.setModified(true);
   }
}
