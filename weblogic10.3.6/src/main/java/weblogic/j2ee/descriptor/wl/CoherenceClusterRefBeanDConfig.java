package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class CoherenceClusterRefBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private CoherenceClusterRefBean beanTreeNode;

   public CoherenceClusterRefBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (CoherenceClusterRefBean)var2;
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
      return this.getCoherenceClusterName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setCoherenceClusterName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("CoherenceClusterName: ");
      var1.append(this.beanTreeNode.getCoherenceClusterName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getCoherenceClusterName() {
      return this.beanTreeNode.getCoherenceClusterName();
   }

   public void setCoherenceClusterName(String var1) {
      this.beanTreeNode.setCoherenceClusterName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CoherenceClusterName", (Object)null, (Object)null));
      this.setModified(true);
   }
}
