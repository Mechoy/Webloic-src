package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.descriptor.DescriptorBean;

public class AnnotationManifestBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private AnnotationManifestBean beanTreeNode;
   private List annotatedClassesDConfig = new ArrayList();
   private List annotationDefinitionsDConfig = new ArrayList();
   private List enumDefinitionsDConfig = new ArrayList();

   public AnnotationManifestBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (AnnotationManifestBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   public AnnotationManifestBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (AnnotationManifestBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public AnnotationManifestBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(AnnotationManifestBean.class);
         this.beanTreeNode = (AnnotationManifestBean)this.beanTree;
         this.customInit();
      } catch (ConfigurationException var6) {
         throw new InvalidModuleException(var6.toString());
      }
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("annotation-manifest/annotated-class/annotated-class-name")));
      var1.add(this.getParentXpath(this.applyNamespace("annotation-manifest/annotation-definition/annotation-class-name")));
      var1.add(this.getParentXpath(this.applyNamespace("annotation-manifest/enum-definition/enum-class-name")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("annotation-manifest/version"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setVersion(var1[0].getText());
         if (debug) {
            Debug.say("inited with Version with " + var1[0].getText());
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
         AnnotatedClassBean var9 = null;
         AnnotatedClassBean[] var10 = this.beanTreeNode.getAnnotatedClasses();
         if (var10 == null) {
            this.beanTreeNode.createAnnotatedClass();
            var10 = this.beanTreeNode.getAnnotatedClasses();
         }

         var7 = this.lastElementOf(this.applyNamespace("annotation-manifest/annotated-class/annotated-class-name"));
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

            var9 = this.beanTreeNode.createAnnotatedClass();
            var3 = true;
         }

         var4 = new AnnotatedClassBeanDConfig(var1, (DescriptorBean)var9, var2);
         ((AnnotatedClassBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("AnnotatedClasses");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.annotatedClassesDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         AnnotationDefinitionBean var12 = null;
         AnnotationDefinitionBean[] var13 = this.beanTreeNode.getAnnotationDefinitions();
         if (var13 == null) {
            this.beanTreeNode.createAnnotationDefinition();
            var13 = this.beanTreeNode.getAnnotationDefinitions();
         }

         var7 = this.lastElementOf(this.applyNamespace("annotation-manifest/annotation-definition/annotation-class-name"));
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

            var12 = this.beanTreeNode.createAnnotationDefinition();
            var3 = true;
         }

         var4 = new AnnotationDefinitionBeanDConfig(var1, (DescriptorBean)var12, var2);
         ((AnnotationDefinitionBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("AnnotationDefinitions");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.annotationDefinitionsDConfig.add(var4);
      } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         EnumDefinitionBean var15 = null;
         EnumDefinitionBean[] var14 = this.beanTreeNode.getEnumDefinitions();
         if (var14 == null) {
            this.beanTreeNode.createEnumDefinition();
            var14 = this.beanTreeNode.getEnumDefinitions();
         }

         var7 = this.lastElementOf(this.applyNamespace("annotation-manifest/enum-definition/enum-class-name"));
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

            var15 = this.beanTreeNode.createEnumDefinition();
            var3 = true;
         }

         var4 = new EnumDefinitionBeanDConfig(var1, (DescriptorBean)var15, var2);
         ((EnumDefinitionBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!((BasicDConfigBean)var4).hasCustomInit()) {
            ((BasicDConfigBean)var4).setParentPropertyName("EnumDefinitions");
         }

         if (debug) {
            Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
         }

         this.enumDefinitionsDConfig.add(var4);
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

   public AnnotatedClassBeanDConfig[] getAnnotatedClasses() {
      return (AnnotatedClassBeanDConfig[])((AnnotatedClassBeanDConfig[])this.annotatedClassesDConfig.toArray(new AnnotatedClassBeanDConfig[0]));
   }

   void addAnnotatedClassBean(AnnotatedClassBeanDConfig var1) {
      this.addToList(this.annotatedClassesDConfig, "AnnotatedClassBean", var1);
   }

   void removeAnnotatedClassBean(AnnotatedClassBeanDConfig var1) {
      this.removeFromList(this.annotatedClassesDConfig, "AnnotatedClassBean", var1);
   }

   public AnnotationDefinitionBeanDConfig[] getAnnotationDefinitions() {
      return (AnnotationDefinitionBeanDConfig[])((AnnotationDefinitionBeanDConfig[])this.annotationDefinitionsDConfig.toArray(new AnnotationDefinitionBeanDConfig[0]));
   }

   void addAnnotationDefinitionBean(AnnotationDefinitionBeanDConfig var1) {
      this.addToList(this.annotationDefinitionsDConfig, "AnnotationDefinitionBean", var1);
   }

   void removeAnnotationDefinitionBean(AnnotationDefinitionBeanDConfig var1) {
      this.removeFromList(this.annotationDefinitionsDConfig, "AnnotationDefinitionBean", var1);
   }

   public EnumDefinitionBeanDConfig[] getEnumDefinitions() {
      return (EnumDefinitionBeanDConfig[])((EnumDefinitionBeanDConfig[])this.enumDefinitionsDConfig.toArray(new EnumDefinitionBeanDConfig[0]));
   }

   void addEnumDefinitionBean(EnumDefinitionBeanDConfig var1) {
      this.addToList(this.enumDefinitionsDConfig, "EnumDefinitionBean", var1);
   }

   void removeEnumDefinitionBean(EnumDefinitionBeanDConfig var1) {
      this.removeFromList(this.enumDefinitionsDConfig, "EnumDefinitionBean", var1);
   }

   public long getUpdateCount() {
      return this.beanTreeNode.getUpdateCount();
   }

   public void setUpdateCount(long var1) {
      this.beanTreeNode.setUpdateCount(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UpdateCount", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getVersion() {
      return this.beanTreeNode.getVersion();
   }

   public void setVersion(String var1) {
      this.beanTreeNode.setVersion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Version", (Object)null, (Object)null));
      this.setModified(true);
   }

   public DConfigBean[] getSecondaryDescriptors() {
      return super.getSecondaryDescriptors();
   }
}
