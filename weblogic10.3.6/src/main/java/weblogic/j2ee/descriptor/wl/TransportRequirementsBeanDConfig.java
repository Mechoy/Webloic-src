package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class TransportRequirementsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private TransportRequirementsBean beanTreeNode;

   public TransportRequirementsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (TransportRequirementsBean)var2;
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

   public String getIntegrity() {
      return this.beanTreeNode.getIntegrity();
   }

   public void setIntegrity(String var1) {
      this.beanTreeNode.setIntegrity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Integrity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConfidentiality() {
      return this.beanTreeNode.getConfidentiality();
   }

   public void setConfidentiality(String var1) {
      this.beanTreeNode.setConfidentiality(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Confidentiality", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getClientCertAuthentication() {
      return this.beanTreeNode.getClientCertAuthentication();
   }

   public void setClientCertAuthentication(String var1) {
      this.beanTreeNode.setClientCertAuthentication(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClientCertAuthentication", (Object)null, (Object)null));
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
