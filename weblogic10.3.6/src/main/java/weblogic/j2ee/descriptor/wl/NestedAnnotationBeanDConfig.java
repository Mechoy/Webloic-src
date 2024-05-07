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

public class NestedAnnotationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private NestedAnnotationBean beanTreeNode;
   private AnnotationInstanceBeanDConfig annotationDConfig;

   public NestedAnnotationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (NestedAnnotationBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.applyNamespace("annotation"));
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
         AnnotationInstanceBean var9 = this.beanTreeNode.getAnnotation();
         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createAnnotation();
            var3 = true;
         }

         this.annotationDConfig = new AnnotationInstanceBeanDConfig(var1, (DescriptorBean)var9, var2);
         var4 = this.annotationDConfig;
         if (!var4.hasCustomInit()) {
            var4.setParentPropertyName("Annotation");
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

   public AnnotationInstanceBeanDConfig getAnnotation() {
      return this.annotationDConfig;
   }

   public void setAnnotation(AnnotationInstanceBeanDConfig var1) {
      this.annotationDConfig = var1;
      this.firePropertyChange(new PropertyChangeEvent(this, "Annotation", (Object)null, (Object)null));
      this.setModified(true);
   }
}
