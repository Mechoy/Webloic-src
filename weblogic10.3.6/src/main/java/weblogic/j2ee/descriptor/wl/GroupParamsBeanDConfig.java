package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class GroupParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private GroupParamsBean beanTreeNode;

   public GroupParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (GroupParamsBean)var2;
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
      return this.getSubDeploymentName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setSubDeploymentName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("SubDeploymentName: ");
      var1.append(this.beanTreeNode.getSubDeploymentName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getSubDeploymentName() {
      return this.beanTreeNode.getSubDeploymentName();
   }

   public void setSubDeploymentName(String var1) {
      this.beanTreeNode.setSubDeploymentName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SubDeploymentName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public DestinationBean getErrorDestination() {
      return this.beanTreeNode.getErrorDestination();
   }

   public void setErrorDestination(DestinationBean var1) {
      this.beanTreeNode.setErrorDestination(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ErrorDestination", (Object)null, (Object)null));
      this.setModified(true);
   }
}
