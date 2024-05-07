package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class MemberConstraintBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private MemberConstraintBean beanTreeNode;

   public MemberConstraintBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (MemberConstraintBean)var2;
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
      DDBean[] var1 = this.getDDBean().getChildBean(this.applyNamespace("constraint-type"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setConstraintType(var1[0].getText());
         if (debug) {
            Debug.say("inited with ConstraintType with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("max-length"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setMaxLength(var1[0].getText());
         if (debug) {
            Debug.say("inited with MaxLength with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("min-value"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setMinValue(var1[0].getText());
         if (debug) {
            Debug.say("inited with MinValue with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("max-value"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setMaxValue(var1[0].getText());
         if (debug) {
            Debug.say("inited with MaxValue with " + var1[0].getText());
         }
      }

      var1 = this.getDDBean().getChildBean(this.applyNamespace("scale"));
      if (var1 != null && var1.length > 0) {
         this.beanTreeNode.setScale(Integer.valueOf(var1[0].getText()));
         if (debug) {
            Debug.say("inited with Scale with " + var1[0].getText());
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
      return this.getConstraintType();
   }

   public void initKeyPropertyValue(String var1) {
      this.setConstraintType(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ConstraintType: ");
      var1.append(this.beanTreeNode.getConstraintType());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getConstraintType() {
      return this.beanTreeNode.getConstraintType();
   }

   public void setConstraintType(String var1) {
      this.beanTreeNode.setConstraintType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConstraintType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMaxLength() {
      return this.beanTreeNode.getMaxLength();
   }

   public void setMaxLength(String var1) {
      this.beanTreeNode.setMaxLength(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxLength", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMinValue() {
      return this.beanTreeNode.getMinValue();
   }

   public void setMinValue(String var1) {
      this.beanTreeNode.setMinValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MinValue", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMaxValue() {
      return this.beanTreeNode.getMaxValue();
   }

   public void setMaxValue(String var1) {
      this.beanTreeNode.setMaxValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxValue", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getScale() {
      return this.beanTreeNode.getScale();
   }

   public void setScale(int var1) {
      this.beanTreeNode.setScale(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Scale", (Object)null, (Object)null));
      this.setModified(true);
   }
}
