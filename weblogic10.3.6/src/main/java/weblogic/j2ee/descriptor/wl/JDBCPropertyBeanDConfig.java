package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCPropertyBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCPropertyBean beanTreeNode;

   public JDBCPropertyBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCPropertyBean)var2;
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
      return this.getName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Name: ");
      var1.append(this.beanTreeNode.getName());
      var1.append("\n");
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

   public String getValue() {
      return this.beanTreeNode.getValue();
   }

   public void setValue(String var1) {
      this.beanTreeNode.setValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Value", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSysPropValue() {
      return this.beanTreeNode.getSysPropValue();
   }

   public void setSysPropValue(String var1) {
      this.beanTreeNode.setSysPropValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SysPropValue", (Object)null, (Object)null));
      this.setModified(true);
   }
}
