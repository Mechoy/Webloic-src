package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MemberBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MemberBean beanTreeNode;

   public MemberBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MemberBean)var2;
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
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("member-value"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setMemberValue(var1[0].getText());
         if (debug) {
            Debug.say("inited with MemberValue with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("requires-encryption"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setRequiresEncryption(Boolean.valueOf(var1[0].getText()));
         if (debug) {
            Debug.say("inited with RequiresEncryption with " + var1[0].getText());
         }
      }

   }

   public boolean hasCustomInit() {
      return true;
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      return null;
   }

   public String keyPropertyValue() {
      return this.getMemberName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setMemberName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("MemberName: ");
      var1.append(this.beanTreeNode.getMemberName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getMemberName() {
      return this.beanTreeNode.getMemberName();
   }

   public void setMemberName(String var1) {
      this.beanTreeNode.setMemberName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MemberName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMemberValue() {
      return this.beanTreeNode.getMemberValue();
   }

   public void setMemberValue(String var1) {
      this.beanTreeNode.setMemberValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MemberValue", (Object)null, (Object)null));
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

   public boolean getRequiresEncryption() {
      return this.beanTreeNode.getRequiresEncryption();
   }

   public void setRequiresEncryption(boolean var1) {
      this.beanTreeNode.setRequiresEncryption(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RequiresEncryption", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCleartextOverrideValue() {
      return this.beanTreeNode.getCleartextOverrideValue();
   }

   public void setCleartextOverrideValue(String var1) {
      this.beanTreeNode.setCleartextOverrideValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CleartextOverrideValue", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSecuredOverrideValue() {
      return this.beanTreeNode.getSecuredOverrideValue();
   }

   public void setSecuredOverrideValue(String var1) {
      this.beanTreeNode.setSecuredOverrideValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SecuredOverrideValue", (Object)null, (Object)null));
      this.setModified(true);
   }

   public byte[] getSecuredOverrideValueEncrypted() {
      return this.beanTreeNode.getSecuredOverrideValueEncrypted();
   }

   public void setSecuredOverrideValueEncrypted(byte[] var1) {
      this.beanTreeNode.setSecuredOverrideValueEncrypted(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SecuredOverrideValueEncrypted", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getShortDescription() {
      return this.beanTreeNode.getShortDescription();
   }
}
