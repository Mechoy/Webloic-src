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

public class AnnotationInstanceBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private AnnotationInstanceBean beanTreeNode;
   private List membersDConfig = new ArrayList();
   private List arrayMembersDConfig = new ArrayList();
   private List nestedAnnotationsDConfig = new ArrayList();
   private List nestedAnnotationArraysDConfig = new ArrayList();

   public AnnotationInstanceBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (AnnotationInstanceBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("member/member-name")));
      var1.add(this.getParentXpath(this.applyNamespace("array-member/member-name")));
      var1.add(this.getParentXpath(this.applyNamespace("nested-annotation/member-name")));
      var1.add(this.getParentXpath(this.applyNamespace("nested-annotation-array/member-name")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("annotation-class-name"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setAnnotationClassName(var1[0].getText());
         if (debug) {
            Debug.say("inited with AnnotationClassName with " + var1[0].getText());
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
      String var6;
      String var7;
      int var11;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         MemberBean var9 = null;
         MemberBean[] var10 = this.beanTreeNode.getMembers();
         if (var10 == null) {
            this.beanTreeNode.createMember();
            var10 = this.beanTreeNode.getMembers();
         }

         var7 = this.lastElementOf(this.applyNamespace("member/member-name"));
         this.setKeyName(var7);
         var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(var11 = 0; var11 < var10.length; ++var11) {
            var9 = var10[var11];
            if (this.isMatch((DescriptorBean)var9, var1, var6)) {
               break;
            }

            var9 = null;
         }

         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createMember();
            var3 = true;
         }

         var4 = new MemberBeanDConfig(var1, (DescriptorBean)var9, var2);
         ((MemberBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("Members");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.membersDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         ArrayMemberBean var12 = null;
         ArrayMemberBean[] var13 = this.beanTreeNode.getArrayMembers();
         if (var13 == null) {
            this.beanTreeNode.createArrayMember();
            var13 = this.beanTreeNode.getArrayMembers();
         }

         var7 = this.lastElementOf(this.applyNamespace("array-member/member-name"));
         this.setKeyName(var7);
         var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(var11 = 0; var11 < var13.length; ++var11) {
            var12 = var13[var11];
            if (this.isMatch((DescriptorBean)var12, var1, var6)) {
               break;
            }

            var12 = null;
         }

         if (var12 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var12 = this.beanTreeNode.createArrayMember();
            var3 = true;
         }

         var4 = new ArrayMemberBeanDConfig(var1, (DescriptorBean)var12, var2);
         ((ArrayMemberBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("ArrayMembers");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.arrayMembersDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         NestedAnnotationBean var15 = null;
         NestedAnnotationBean[] var14 = this.beanTreeNode.getNestedAnnotations();
         if (var14 == null) {
            this.beanTreeNode.createNestedAnnotation();
            var14 = this.beanTreeNode.getNestedAnnotations();
         }

         var7 = this.lastElementOf(this.applyNamespace("nested-annotation/member-name"));
         this.setKeyName(var7);
         var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(var11 = 0; var11 < var14.length; ++var11) {
            var15 = var14[var11];
            if (this.isMatch((DescriptorBean)var15, var1, var6)) {
               break;
            }

            var15 = null;
         }

         if (var15 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var15 = this.beanTreeNode.createNestedAnnotation();
            var3 = true;
         }

         var4 = new NestedAnnotationBeanDConfig(var1, (DescriptorBean)var15, var2);
         ((NestedAnnotationBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("NestedAnnotations");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.nestedAnnotationsDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         NestedAnnotationArrayBean var17 = null;
         NestedAnnotationArrayBean[] var16 = this.beanTreeNode.getNestedAnnotationArrays();
         if (var16 == null) {
            this.beanTreeNode.createNestedAnnotationArray();
            var16 = this.beanTreeNode.getNestedAnnotationArrays();
         }

         var7 = this.lastElementOf(this.applyNamespace("nested-annotation-array/member-name"));
         this.setKeyName(var7);
         var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(var11 = 0; var11 < var16.length; ++var11) {
            var17 = var16[var11];
            if (this.isMatch((DescriptorBean)var17, var1, var6)) {
               break;
            }

            var17 = null;
         }

         if (var17 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var17 = this.beanTreeNode.createNestedAnnotationArray();
            var3 = true;
         }

         var4 = new NestedAnnotationArrayBeanDConfig(var1, (DescriptorBean)var17, var2);
         ((NestedAnnotationArrayBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("NestedAnnotationArrays");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.nestedAnnotationArraysDConfig.add(var4);
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var18 = 0; var18 < this.xpaths.length; ++var18) {
            Debug.say("xpaths[" + var18 + "]=" + this.xpaths[var18]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean((DConfigBean)var4);
         if (var3) {
            ((BasicDConfigBean)var4).setModified(true);

            Object var19;
            for(var19 = var4; ((BasicDConfigBean)var19).getParent() != null; var19 = ((BasicDConfigBean)var19).getParent()) {
            }

            ((BasicDConfigBeanRoot)var19).registerAsListener(((BasicDConfigBean)var4).getDescriptorBean());
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

   public MemberBeanDConfig[] getMembers() {
      return (MemberBeanDConfig[])((MemberBeanDConfig[])this.membersDConfig.toArray(new MemberBeanDConfig[0]));
   }

   void addMemberBean(MemberBeanDConfig var1) {
      this.addToList(this.membersDConfig, "MemberBean", var1);
   }

   void removeMemberBean(MemberBeanDConfig var1) {
      this.removeFromList(this.membersDConfig, "MemberBean", var1);
   }

   public ArrayMemberBeanDConfig[] getArrayMembers() {
      return (ArrayMemberBeanDConfig[])((ArrayMemberBeanDConfig[])this.arrayMembersDConfig.toArray(new ArrayMemberBeanDConfig[0]));
   }

   void addArrayMemberBean(ArrayMemberBeanDConfig var1) {
      this.addToList(this.arrayMembersDConfig, "ArrayMemberBean", var1);
   }

   void removeArrayMemberBean(ArrayMemberBeanDConfig var1) {
      this.removeFromList(this.arrayMembersDConfig, "ArrayMemberBean", var1);
   }

   public NestedAnnotationBeanDConfig[] getNestedAnnotations() {
      return (NestedAnnotationBeanDConfig[])((NestedAnnotationBeanDConfig[])this.nestedAnnotationsDConfig.toArray(new NestedAnnotationBeanDConfig[0]));
   }

   void addNestedAnnotationBean(NestedAnnotationBeanDConfig var1) {
      this.addToList(this.nestedAnnotationsDConfig, "NestedAnnotationBean", var1);
   }

   void removeNestedAnnotationBean(NestedAnnotationBeanDConfig var1) {
      this.removeFromList(this.nestedAnnotationsDConfig, "NestedAnnotationBean", var1);
   }

   public NestedAnnotationArrayBeanDConfig[] getNestedAnnotationArrays() {
      return (NestedAnnotationArrayBeanDConfig[])((NestedAnnotationArrayBeanDConfig[])this.nestedAnnotationArraysDConfig.toArray(new NestedAnnotationArrayBeanDConfig[0]));
   }

   void addNestedAnnotationArrayBean(NestedAnnotationArrayBeanDConfig var1) {
      this.addToList(this.nestedAnnotationArraysDConfig, "NestedAnnotationArrayBean", var1);
   }

   void removeNestedAnnotationArrayBean(NestedAnnotationArrayBeanDConfig var1) {
      this.removeFromList(this.nestedAnnotationArraysDConfig, "NestedAnnotationArrayBean", var1);
   }

   public String getShortDescription() {
      return this.beanTreeNode.getShortDescription();
   }
}
