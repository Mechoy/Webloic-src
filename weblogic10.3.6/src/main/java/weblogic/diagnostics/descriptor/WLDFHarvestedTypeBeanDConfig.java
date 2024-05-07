package weblogic.diagnostics.descriptor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WLDFHarvestedTypeBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WLDFHarvestedTypeBean beanTreeNode;

   public WLDFHarvestedTypeBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WLDFHarvestedTypeBean)var2;
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

   public String getName() {
      return this.beanTreeNode.getName();
   }

   public void setName(String var1) {
      this.beanTreeNode.setName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Name", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isEnabled() {
      return this.beanTreeNode.isEnabled();
   }

   public void setEnabled(boolean var1) {
      this.beanTreeNode.setEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Enabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isKnownType() {
      return this.beanTreeNode.isKnownType();
   }

   public void setKnownType(boolean var1) {
      this.beanTreeNode.setKnownType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KnownType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getHarvestedAttributes() {
      return this.beanTreeNode.getHarvestedAttributes();
   }

   public void setHarvestedAttributes(String[] var1) {
      this.beanTreeNode.setHarvestedAttributes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HarvestedAttributes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getHarvestedInstances() {
      return this.beanTreeNode.getHarvestedInstances();
   }

   public void setHarvestedInstances(String[] var1) {
      this.beanTreeNode.setHarvestedInstances(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HarvestedInstances", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getNamespace() {
      return this.beanTreeNode.getNamespace();
   }

   public void setNamespace(String var1) {
      this.beanTreeNode.setNamespace(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Namespace", (Object)null, (Object)null));
      this.setModified(true);
   }
}
