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

public class NestedAnnotationArrayBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private NestedAnnotationArrayBean beanTreeNode;
   private List annotationsDConfig = new ArrayList();

   public NestedAnnotationArrayBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (NestedAnnotationArrayBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("annotation/annotation-class-name")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      if (debug) {
         Debug.say("Creating child DCB for <" + var1.getXpath() + ">");
      }

      boolean var3 = false;
      AnnotationInstanceBeanDConfig var4 = null;
      String var5 = var1.getXpath();
      int var8 = 0;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         AnnotationInstanceBean var9 = null;
         AnnotationInstanceBean[] var10 = this.beanTreeNode.getAnnotations();
         if (var10 == null) {
            this.beanTreeNode.createAnnotation();
            var10 = this.beanTreeNode.getAnnotations();
         }

         String var7 = this.lastElementOf(this.applyNamespace("annotation/annotation-class-name"));
         this.setKeyName(var7);
         String var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(int var11 = 0; var11 < var10.length; ++var11) {
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
         if (!var4.hasCustomInit()) {
            var4.setParentPropertyName("Annotations");
         }

         if (debug) {
            Debug.say("dcb dump: " + var4.toString());
         }

         this.annotationsDConfig.add(var4);
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var12 = 0; var12 < this.xpaths.length; ++var12) {
            Debug.say("xpaths[" + var12 + "]=" + this.xpaths[var12]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean(var4);
         if (var3) {
            var4.setModified(true);

            Object var13;
            for(var13 = var4; ((BasicDConfigBean)var13).getParent() != null; var13 = ((BasicDConfigBean)var13).getParent()) {
            }

            ((BasicDConfigBeanRoot)var13).registerAsListener(var4.getDescriptorBean());
         }

         this.processDCB(var4, var3);
      }

      return var4;
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

   public AnnotationInstanceBeanDConfig[] getAnnotations() {
      return (AnnotationInstanceBeanDConfig[])((AnnotationInstanceBeanDConfig[])this.annotationsDConfig.toArray(new AnnotationInstanceBeanDConfig[0]));
   }

   void addAnnotationInstanceBean(AnnotationInstanceBeanDConfig var1) {
      this.addToList(this.annotationsDConfig, "AnnotationInstanceBean", var1);
   }

   void removeAnnotationInstanceBean(AnnotationInstanceBeanDConfig var1) {
      this.removeFromList(this.annotationsDConfig, "AnnotationInstanceBean", var1);
   }
}
