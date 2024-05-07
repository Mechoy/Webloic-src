package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class LibraryContextRootOverrideBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private LibraryContextRootOverrideBean beanTreeNode;

   public LibraryContextRootOverrideBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (LibraryContextRootOverrideBean)var2;
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
      return this.getContextRoot();
   }

   public void initKeyPropertyValue(String var1) {
      this.setContextRoot(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ContextRoot: ");
      var1.append(this.beanTreeNode.getContextRoot());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getContextRoot() {
      return this.beanTreeNode.getContextRoot();
   }

   public void setContextRoot(String var1) {
      this.beanTreeNode.setContextRoot(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ContextRoot", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOverrideValue() {
      return this.beanTreeNode.getOverrideValue();
   }

   public void setOverrideValue(String var1) {
      this.beanTreeNode.setOverrideValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OverrideValue", (Object)null, (Object)null));
      this.setModified(true);
   }
}
