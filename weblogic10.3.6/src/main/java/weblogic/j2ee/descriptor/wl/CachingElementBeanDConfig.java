package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class CachingElementBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private CachingElementBean beanTreeNode;

   public CachingElementBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (CachingElementBean)var2;
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

   public String getCmrField() {
      return this.beanTreeNode.getCmrField();
   }

   public void setCmrField(String var1) {
      this.beanTreeNode.setCmrField(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CmrField", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getGroupName() {
      return this.beanTreeNode.getGroupName();
   }

   public void setGroupName(String var1) {
      this.beanTreeNode.setGroupName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "GroupName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public CachingElementBean[] getCachingElements() {
      return this.beanTreeNode.getCachingElements();
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