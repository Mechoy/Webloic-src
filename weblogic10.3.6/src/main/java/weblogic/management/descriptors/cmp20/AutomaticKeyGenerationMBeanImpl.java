package weblogic.management.descriptors.cmp20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class AutomaticKeyGenerationMBeanImpl extends XMLElementMBeanDelegate implements AutomaticKeyGenerationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_keyCacheSize = false;
   private int keyCacheSize;
   private boolean isSet_generatorName = false;
   private String generatorName;
   private boolean isSet_selectFirstSequenceKeyBeforeUpdate = false;
   private boolean selectFirstSequenceKeyBeforeUpdate;
   private boolean isSet_generatorType = false;
   private String generatorType;

   public int getKeyCacheSize() {
      return this.keyCacheSize;
   }

   public void setKeyCacheSize(int var1) {
      int var2 = this.keyCacheSize;
      this.keyCacheSize = var1;
      this.isSet_keyCacheSize = var1 != -1;
      this.checkChange("keyCacheSize", var2, this.keyCacheSize);
   }

   public String getGeneratorName() {
      return this.generatorName;
   }

   public void setGeneratorName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.generatorName;
      this.generatorName = var1;
      this.isSet_generatorName = var1 != null;
      this.checkChange("generatorName", var2, this.generatorName);
   }

   public boolean getSelectFirstSequenceKeyBeforeUpdate() {
      return this.selectFirstSequenceKeyBeforeUpdate;
   }

   public void setSelectFirstSequenceKeyBeforeUpdate(boolean var1) {
      boolean var2 = this.selectFirstSequenceKeyBeforeUpdate;
      this.selectFirstSequenceKeyBeforeUpdate = var1;
      this.isSet_selectFirstSequenceKeyBeforeUpdate = true;
      this.checkChange("selectFirstSequenceKeyBeforeUpdate", var2, this.selectFirstSequenceKeyBeforeUpdate);
   }

   public String getGeneratorType() {
      return this.generatorType;
   }

   public void setGeneratorType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.generatorType;
      this.generatorType = var1;
      this.isSet_generatorType = var1 != null;
      this.checkChange("generatorType", var2, this.generatorType);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<automatic-key-generation");
      var2.append(">\n");
      if (null != this.getGeneratorType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<generator-type>").append(this.getGeneratorType()).append("</generator-type>\n");
      }

      if (null != this.getGeneratorName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<generator-name>").append(this.getGeneratorName()).append("</generator-name>\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append("<key-cache-size>").append(this.getKeyCacheSize()).append("</key-cache-size>\n");
      var2.append(ToXML.indent(var1)).append("</automatic-key-generation>\n");
      return var2.toString();
   }
}
