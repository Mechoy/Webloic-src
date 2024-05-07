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

public class AnnotatedClassBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private AnnotatedClassBean beanTreeNode;
   private List annotationsDConfig = new ArrayList();
   private List fieldsDConfig = new ArrayList();
   private List methodsDConfig = new ArrayList();

   public AnnotatedClassBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (AnnotatedClassBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("annotation/annotation-class-name")));
      var1.add(this.getParentXpath(this.applyNamespace("field/field-name")));
      var1.add(this.getParentXpath(this.applyNamespace("method/method-key")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("annotated-class-name"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setAnnotatedClassName(var1[0].getText());
         if (debug) {
            Debug.say("inited with AnnotatedClassName with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("component-type"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setComponentType(var1[0].getText());
         if (debug) {
            Debug.say("inited with ComponentType with " + var1[0].getText());
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
         AnnotationInstanceBean var9 = null;
         AnnotationInstanceBean[] var10 = this.beanTreeNode.getAnnotations();
         if (var10 == null) {
            this.beanTreeNode.createAnnotation();
            var10 = this.beanTreeNode.getAnnotations();
         }

         var7 = this.lastElementOf(this.applyNamespace("annotation/annotation-class-name"));
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

            var9 = this.beanTreeNode.createAnnotation();
            var3 = true;
         }

         var4 = new AnnotationInstanceBeanDConfig(var1, (DescriptorBean)var9, var2);
         ((AnnotationInstanceBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("Annotations");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.annotationsDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         AnnotatedFieldBean var12 = null;
         AnnotatedFieldBean[] var13 = this.beanTreeNode.getFields();
         if (var13 == null) {
            this.beanTreeNode.createField();
            var13 = this.beanTreeNode.getFields();
         }

         var7 = this.lastElementOf(this.applyNamespace("field/field-name"));
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

            var12 = this.beanTreeNode.createField();
            var3 = true;
         }

         var4 = new AnnotatedFieldBeanDConfig(var1, (DescriptorBean)var12, var2);
         ((AnnotatedFieldBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("Fields");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.fieldsDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         AnnotatedMethodBean var15 = null;
         AnnotatedMethodBean[] var14 = this.beanTreeNode.getMethods();
         if (var14 == null) {
            this.beanTreeNode.createMethod();
            var14 = this.beanTreeNode.getMethods();
         }

         var7 = this.lastElementOf(this.applyNamespace("method/method-key"));
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

            var15 = this.beanTreeNode.createMethod();
            var3 = true;
         }

         var4 = new AnnotatedMethodBeanDConfig(var1, (DescriptorBean)var15, var2);
         ((AnnotatedMethodBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("Methods");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.methodsDConfig.add(var4);
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var16 = 0; var16 < this.xpaths.length; ++var16) {
            Debug.say("xpaths[" + var16 + "]=" + this.xpaths[var16]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean((DConfigBean)var4);
         if (var3) {
            ((BasicDConfigBean)var4).setModified(true);

            Object var17;
            for(var17 = var4; ((BasicDConfigBean)var17).getParent() != null; var17 = ((BasicDConfigBean)var17).getParent()) {
            }

            ((BasicDConfigBeanRoot)var17).registerAsListener(((BasicDConfigBean)var4).getDescriptorBean());
         }

         this.processDCB((BasicDConfigBean)var4, var3);
      }

      return (DConfigBean)var4;
   }

   public String keyPropertyValue() {
      return this.getAnnotatedClassName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setAnnotatedClassName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("AnnotatedClassName: ");
      var1.append(this.beanTreeNode.getAnnotatedClassName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getAnnotatedClassName() {
      return this.beanTreeNode.getAnnotatedClassName();
   }

   public void setAnnotatedClassName(String var1) {
      this.beanTreeNode.setAnnotatedClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AnnotatedClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getComponentType() {
      return this.beanTreeNode.getComponentType();
   }

   public void setComponentType(String var1) {
      this.beanTreeNode.setComponentType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ComponentType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public AnnotationInstanceBeanDConfig[] getAnnotations() {
      return (AnnotationInstanceBeanDConfig[])((AnnotationInstanceBeanDConfig[])this.annotationsDConfig.toArray(new AnnotationInstanceBeanDConfig[0]));
   }

   void addAnnotationInstanceBean(AnnotationInstanceBeanDConfig var1) {
      this.addToList(this.annotationsDConfig, "AnnotationInstanceBean", var1);
   }

   void removeAnnotationInstanceBean(AnnotationInstanceBeanDConfig var1) {
      this.removeFromList(this.annotationsDConfig, "AnnotationInstanceBean", var1);
   }

   public AnnotatedFieldBeanDConfig[] getFields() {
      return (AnnotatedFieldBeanDConfig[])((AnnotatedFieldBeanDConfig[])this.fieldsDConfig.toArray(new AnnotatedFieldBeanDConfig[0]));
   }

   void addAnnotatedFieldBean(AnnotatedFieldBeanDConfig var1) {
      this.addToList(this.fieldsDConfig, "AnnotatedFieldBean", var1);
   }

   void removeAnnotatedFieldBean(AnnotatedFieldBeanDConfig var1) {
      this.removeFromList(this.fieldsDConfig, "AnnotatedFieldBean", var1);
   }

   public AnnotatedMethodBeanDConfig[] getMethods() {
      return (AnnotatedMethodBeanDConfig[])((AnnotatedMethodBeanDConfig[])this.methodsDConfig.toArray(new AnnotatedMethodBeanDConfig[0]));
   }

   void addAnnotatedMethodBean(AnnotatedMethodBeanDConfig var1) {
      this.addToList(this.methodsDConfig, "AnnotatedMethodBean", var1);
   }

   void removeAnnotatedMethodBean(AnnotatedMethodBeanDConfig var1) {
      this.removeFromList(this.methodsDConfig, "AnnotatedMethodBean", var1);
   }

   public String getShortDescription() {
      return this.beanTreeNode.getShortDescription();
   }
}
