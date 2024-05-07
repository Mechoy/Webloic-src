package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class CompatibilityBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private CompatibilityBean beanTreeNode;

   public CompatibilityBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (CompatibilityBean)var2;
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

   public boolean isSerializeByteArrayToOracleBlob() {
      return this.beanTreeNode.isSerializeByteArrayToOracleBlob();
   }

   public void setSerializeByteArrayToOracleBlob(boolean var1) {
      this.beanTreeNode.setSerializeByteArrayToOracleBlob(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SerializeByteArrayToOracleBlob", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isSerializeCharArrayToBytes() {
      return this.beanTreeNode.isSerializeCharArrayToBytes();
   }

   public void setSerializeCharArrayToBytes(boolean var1) {
      this.beanTreeNode.setSerializeCharArrayToBytes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SerializeCharArrayToBytes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isAllowReadonlyCreateAndRemove() {
      return this.beanTreeNode.isAllowReadonlyCreateAndRemove();
   }

   public void setAllowReadonlyCreateAndRemove(boolean var1) {
      this.beanTreeNode.setAllowReadonlyCreateAndRemove(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AllowReadonlyCreateAndRemove", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDisableStringTrimming() {
      return this.beanTreeNode.isDisableStringTrimming();
   }

   public void setDisableStringTrimming(boolean var1) {
      this.beanTreeNode.setDisableStringTrimming(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DisableStringTrimming", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isFindersReturnNulls() {
      return this.beanTreeNode.isFindersReturnNulls();
   }

   public void setFindersReturnNulls(boolean var1) {
      this.beanTreeNode.setFindersReturnNulls(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FindersReturnNulls", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isLoadRelatedBeansFromDbInPostCreate() {
      return this.beanTreeNode.isLoadRelatedBeansFromDbInPostCreate();
   }

   public void setLoadRelatedBeansFromDbInPostCreate(boolean var1) {
      this.beanTreeNode.setLoadRelatedBeansFromDbInPostCreate(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoadRelatedBeansFromDbInPostCreate", (Object)null, (Object)null));
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
