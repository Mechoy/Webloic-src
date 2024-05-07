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

public class MemberDefinitionBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MemberDefinitionBean beanTreeNode;
   private EnumRefBeanDConfig enumRefDConfig;
   private SimpleTypeDefinitionBeanDConfig simpleTypeDefinitionDConfig;

   public MemberDefinitionBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MemberDefinitionBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.applyNamespace("enum-ref"));
      var1.add(this.applyNamespace("simple-type-definition"));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("is-array"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setIsArray(Boolean.valueOf(var1[0].getText()));
         if (debug) {
            Debug.say("inited with IsArray with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("is-required"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setIsRequired(Boolean.valueOf(var1[0].getText()));
         if (debug) {
            Debug.say("inited with IsRequired with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("annotation-ref"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setAnnotationRef(var1[0].getText());
         if (debug) {
            Debug.say("inited with AnnotationRef with " + var1[0].getText());
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
         EnumRefBean var9 = this.beanTreeNode.getEnumRef();
         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createEnumRef();
            var3 = true;
         }

         this.enumRefDConfig = new EnumRefBeanDConfig(var1, (DescriptorBean)var9, var2);
         var4 = this.enumRefDConfig;
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("EnumRef");
         }
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         SimpleTypeDefinitionBean var10 = this.beanTreeNode.getSimpleTypeDefinition();
         if (var10 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var10 = this.beanTreeNode.createSimpleTypeDefinition();
            var3 = true;
         }

         this.simpleTypeDefinitionDConfig = new SimpleTypeDefinitionBeanDConfig(var1, (DescriptorBean)var10, var2);
         var4 = this.simpleTypeDefinitionDConfig;
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("SimpleTypeDefinition");
         }
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var11 = 0; var11 < this.xpaths.length; ++var11) {
            Debug.say("xpaths[" + var11 + "]=" + this.xpaths[var11]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean((DConfigBean)var4);
         if (var3) {
            ((BasicDConfigBean)var4).setModified(true);

            Object var12;
            for(var12 = var4; ((BasicDConfigBean)var12).getParent() != null; var12 = ((BasicDConfigBean)var12).getParent()) {
            }

            ((BasicDConfigBeanRoot)var12).registerAsListener(((BasicDConfigBean)var4).getDescriptorBean());
         }

         this.processDCB((BasicDConfigBean)var4, var3);
      }

      return (DConfigBean)var4;
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

   public boolean getIsArray() {
      return this.beanTreeNode.getIsArray();
   }

   public void setIsArray(boolean var1) {
      this.beanTreeNode.setIsArray(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IsArray", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getIsRequired() {
      return this.beanTreeNode.getIsRequired();
   }

   public void setIsRequired(boolean var1) {
      this.beanTreeNode.setIsRequired(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IsRequired", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getAnnotationRef() {
      return this.beanTreeNode.getAnnotationRef();
   }

   public void setAnnotationRef(String var1) {
      this.beanTreeNode.setAnnotationRef(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AnnotationRef", (Object)null, (Object)null));
      this.setModified(true);
   }

   public EnumRefBeanDConfig getEnumRef() {
      return this.enumRefDConfig;
   }

   public void setEnumRef(EnumRefBeanDConfig var1) {
      this.enumRefDConfig = var1;
      this.firePropertyChange(new PropertyChangeEvent(this, "EnumRef", (Object)null, (Object)null));
      this.setModified(true);
   }

   public SimpleTypeDefinitionBeanDConfig getSimpleTypeDefinition() {
      return this.simpleTypeDefinitionDConfig;
   }

   public void setSimpleTypeDefinition(SimpleTypeDefinitionBeanDConfig var1) {
      this.simpleTypeDefinitionDConfig = var1;
      this.firePropertyChange(new PropertyChangeEvent(this, "SimpleTypeDefinition", (Object)null, (Object)null));
      this.setModified(true);
   }
}
