package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.descriptor.DescriptorBean;

public class AnnotationDefinitionBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private AnnotationDefinitionBean beanTreeNode;
   private MembershipConstraintBeanDConfig membershipConstraintDConfig;
   private List memberDefinitionsDConfig = new ArrayList();

   public AnnotationDefinitionBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (AnnotationDefinitionBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.applyNamespace("membership-constraint"));
      var1.add(this.getParentXpath(this.applyNamespace("member-definition/member-name")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("allowed-on-declaration"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setAllowedOnDeclaration(Boolean.valueOf(var1[0].getText()));
         if (debug) {
            Debug.say("inited with AllowedOnDeclaration with " + var1[0].getText());
         }
      }

   }

   public boolean hasCustomInit() {
      return true;
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      if (debug) {
         Debug.say("Creating child DCB for <" + var1.getXpath() + ">");
      }

      boolean var3 = false;
      Object var4 = null;
      String var5 = var1.getXpath();
      int var8 = 0;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         MembershipConstraintBean var9 = this.beanTreeNode.getMembershipConstraint();
         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createMembershipConstraint();
            var3 = true;
         }

         this.membershipConstraintDConfig = new MembershipConstraintBeanDConfig(var1, (DescriptorBean)var9, var2);
         var4 = this.membershipConstraintDConfig;
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("MembershipConstraint");
         }
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         MemberDefinitionBean var12 = null;
         MemberDefinitionBean[] var10 = this.beanTreeNode.getMemberDefinitions();
         if (var10 == null) {
            this.beanTreeNode.createMemberDefinition();
            var10 = this.beanTreeNode.getMemberDefinitions();
         }

         String var7 = this.lastElementOf(this.applyNamespace("member-definition/member-name"));
         this.setKeyName(var7);
         String var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(int var11 = 0; var11 < var10.length; ++var11) {
            var12 = var10[var11];
            if (this.isMatch((DescriptorBean)var12, var1, var6)) {
               break;
            }

            var12 = null;
         }

         if (var12 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var12 = this.beanTreeNode.createMemberDefinition();
            var3 = true;
         }

         var4 = new MemberDefinitionBeanDConfig(var1, (DescriptorBean)var12, var2);
         ((MemberDefinitionBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("MemberDefinitions");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.memberDefinitionsDConfig.add(var4);
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var13 = 0; var13 < this.xpaths.length; ++var13) {
            Debug.say("xpaths[" + var13 + "]=" + this.xpaths[var13]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean((DConfigBean)var4);
         if (var3) {
            ((BasicDConfigBean)var4).setModified(true);

            Object var14;
            for(var14 = var4; ((BasicDConfigBean)var14).getParent() != null; var14 = ((BasicDConfigBean)var14).getParent()) {
            }

            ((BasicDConfigBeanRoot)var14).registerAsListener(((BasicDConfigBean)var4).getDescriptorBean());
         }

         this.processDCB((BasicDConfigBean)var4, var3);
      }

      return (DConfigBean)var4;
   }

   public String keyPropertyValue() {
      return this.getAnnotationClassName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setAnnotationClassName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("AnnotationClassName: ");
      var1.append(this.beanTreeNode.getAnnotationClassName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getAnnotationClassName() {
      return this.beanTreeNode.getAnnotationClassName();
   }

   public void setAnnotationClassName(String var1) {
      this.beanTreeNode.setAnnotationClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AnnotationClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MembershipConstraintBeanDConfig getMembershipConstraint() {
      return this.membershipConstraintDConfig;
   }

   public void setMembershipConstraint(MembershipConstraintBeanDConfig var1) {
      this.membershipConstraintDConfig = var1;
      this.firePropertyChange(new PropertyChangeEvent(this, "MembershipConstraint", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getAllowedOnDeclaration() {
      return this.beanTreeNode.getAllowedOnDeclaration();
   }

   public void setAllowedOnDeclaration(boolean var1) {
      this.beanTreeNode.setAllowedOnDeclaration(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AllowedOnDeclaration", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MemberDefinitionBeanDConfig[] getMemberDefinitions() {
      return (MemberDefinitionBeanDConfig[])((MemberDefinitionBeanDConfig[])this.memberDefinitionsDConfig.toArray(new MemberDefinitionBeanDConfig[0]));
   }

   void addMemberDefinitionBean(MemberDefinitionBeanDConfig var1) {
      this.addToList(this.memberDefinitionsDConfig, "MemberDefinitionBean", var1);
   }

   void removeMemberDefinitionBean(MemberDefinitionBeanDConfig var1) {
      this.removeFromList(this.memberDefinitionsDConfig, "MemberDefinitionBean", var1);
   }
}
