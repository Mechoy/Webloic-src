package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ArrayMemberBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ArrayMemberBean beanTreeNode;

   public ArrayMemberBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ArrayMemberBean)var2;
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
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("member-name"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setMemberName(var1[0].getText());
         if (debug) {
            Debug.say("inited with MemberName with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("member-value"));
      if (var1 != null && var1.length > 0) {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getText();
         }

         this.beanTreeNode.setMemberValues(var2);
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

   public String[] getMemberValues() {
      return this.beanTreeNode.getMemberValues();
   }

   public void setMemberValues(String[] var1) {
      this.beanTreeNode.setMemberValues(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MemberValues", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getOverrideValues() {
      return this.beanTreeNode.getOverrideValues();
   }

   public void setOverrideValues(String[] var1) {
      this.beanTreeNode.setOverrideValues(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OverrideValues", (Object)null, (Object)null));
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

   public String[] getCleartextOverrideValues() {
      return this.beanTreeNode.getCleartextOverrideValues();
   }

   public void setCleartextOverrideValues(String[] var1) {
      this.beanTreeNode.setCleartextOverrideValues(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CleartextOverrideValues", (Object)null, (Object)null));
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
