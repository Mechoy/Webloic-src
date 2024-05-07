package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.StringUtils;

public class VirtualDirectoryMappingBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private VirtualDirectoryMappingBean beanTreeNode;

   public VirtualDirectoryMappingBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (VirtualDirectoryMappingBean)var2;
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
      return this._getKeyValue(this.getUrlPatterns());
   }

   public void initKeyPropertyValue(String var1) {
      this.setUrlPatterns(StringUtils.splitCompletely(var1, ",", false));
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getLocalPath() {
      return this.beanTreeNode.getLocalPath();
   }

   public void setLocalPath(String var1) {
      this.beanTreeNode.setLocalPath(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LocalPath", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getUrlPatterns() {
      return this.beanTreeNode.getUrlPatterns();
   }

   public void setUrlPatterns(String[] var1) {
      this.beanTreeNode.setUrlPatterns(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UrlPatterns", (Object)null, (Object)null));
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
