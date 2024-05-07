package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.descriptor.DescriptorBean;

public class SimpleTypeDefinitionBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SimpleTypeDefinitionBean beanTreeNode;
   private MemberConstraintBeanDConfig constraintDConfig;

   public SimpleTypeDefinitionBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SimpleTypeDefinitionBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.applyNamespace("constraint"));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("base-type"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setBaseType(var1[0].getText());
         if (debug) {
            Debug.say("inited with BaseType with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("requires-encryption"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setRequiresEncryption(Boolean.valueOf(var1[0].getText()));
         if (debug) {
            Debug.say("inited with RequiresEncryption with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("default-value"));
      if (var1 != null && var1.length > 0) {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getText();
         }

         this.beanTreeNode.setDefaultValue(var2);
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
      MemberConstraintBeanDConfig var4 = null;
      String var5 = var1.getXpath();
      int var8 = 0;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         MemberConstraintBean var9 = this.beanTreeNode.getConstraint();
         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createConstraint();
            var3 = true;
         }

         this.constraintDConfig = new MemberConstraintBeanDConfig(var1, (DescriptorBean)var9, var2);
         var4 = this.constraintDConfig;
         if (!var4.hasCustomInit()) {
            var4.setParentPropertyName("Constraint");
         }
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var10 = 0; var10 < this.xpaths.length; ++var10) {
            Debug.say("xpaths[" + var10 + "]=" + this.xpaths[var10]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean(var4);
         if (var3) {
            var4.setModified(true);

            Object var11;
            for(var11 = var4; ((BasicDConfigBean)var11).getParent() != null; var11 = ((BasicDConfigBean)var11).getParent()) {
            }

            ((BasicDConfigBeanRoot)var11).registerAsListener(var4.getDescriptorBean());
         }

         this.processDCB(var4, var3);
      }

      return var4;
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

   public String getBaseType() {
      return this.beanTreeNode.getBaseType();
   }

   public void setBaseType(String var1) {
      this.beanTreeNode.setBaseType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BaseType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MemberConstraintBeanDConfig getConstraint() {
      return this.constraintDConfig;
   }

   public void setConstraint(MemberConstraintBeanDConfig var1) {
      this.constraintDConfig = var1;
      this.firePropertyChange(new PropertyChangeEvent(this, "Constraint", (Object)null, (Object)null));
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

   public String[] getDefaultValue() {
      return this.beanTreeNode.getDefaultValue();
   }

   public void setDefaultValue(String[] var1) {
      this.beanTreeNode.setDefaultValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultValue", (Object)null, (Object)null));
      this.setModified(true);
   }
}
