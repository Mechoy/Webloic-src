package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MembershipConstraintBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MembershipConstraintBean beanTreeNode;

   public MembershipConstraintBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MembershipConstraintBean)var2;
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
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("membership-rule"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setMembershipRule(var1[0].getText());
         if (debug) {
            Debug.say("inited with MembershipRule with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("member-name"));
      if (var1 != null && var1.length > 0) {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getText();
         }

         this.beanTreeNode.setMemberNames(var2);
      }

   }

   public boolean hasCustomInit() {
      return true;
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      return null;
   }

   public String keyPropertyValue() {
      return this.getMembershipRule();
   }

   public void initKeyPropertyValue(String var1) {
      this.setMembershipRule(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("MembershipRule: ");
      var1.append(this.beanTreeNode.getMembershipRule());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getMembershipRule() {
      return this.beanTreeNode.getMembershipRule();
   }

   public void setMembershipRule(String var1) {
      this.beanTreeNode.setMembershipRule(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MembershipRule", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getMemberNames() {
      return this.beanTreeNode.getMemberNames();
   }

   public void setMemberNames(String[] var1) {
      this.beanTreeNode.setMemberNames(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MemberNames", (Object)null, (Object)null));
      this.setModified(true);
   }
}
