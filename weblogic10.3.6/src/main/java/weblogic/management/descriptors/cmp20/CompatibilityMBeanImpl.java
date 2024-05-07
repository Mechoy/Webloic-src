package weblogic.management.descriptors.cmp20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class CompatibilityMBeanImpl extends XMLElementMBeanDelegate implements CompatibilityMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_loadRelatedBeansFromDbInPostCreate = false;
   private boolean loadRelatedBeansFromDbInPostCreate = false;
   private boolean isSet_encoding = false;
   private String encoding;
   private boolean isSet_byteArrayIsSerializedToOracleBlob = false;
   private boolean byteArrayIsSerializedToOracleBlob = false;
   private boolean isSet_version = false;
   private String version;
   private boolean isSet_allowReadonlyCreateAndRemove = false;
   private boolean allowReadonlyCreateAndRemove = false;

   public boolean getLoadRelatedBeansFromDbInPostCreate() {
      return this.loadRelatedBeansFromDbInPostCreate;
   }

   public void setLoadRelatedBeansFromDbInPostCreate(boolean var1) {
      boolean var2 = this.loadRelatedBeansFromDbInPostCreate;
      this.loadRelatedBeansFromDbInPostCreate = var1;
      this.isSet_loadRelatedBeansFromDbInPostCreate = true;
      this.checkChange("loadRelatedBeansFromDbInPostCreate", var2, this.loadRelatedBeansFromDbInPostCreate);
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setEncoding(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.encoding;
      this.encoding = var1;
      this.isSet_encoding = var1 != null;
      this.checkChange("encoding", var2, this.encoding);
   }

   public boolean getByteArrayIsSerializedToOracleBlob() {
      return this.byteArrayIsSerializedToOracleBlob;
   }

   public void setByteArrayIsSerializedToOracleBlob(boolean var1) {
      boolean var2 = this.byteArrayIsSerializedToOracleBlob;
      this.byteArrayIsSerializedToOracleBlob = var1;
      this.isSet_byteArrayIsSerializedToOracleBlob = true;
      this.checkChange("byteArrayIsSerializedToOracleBlob", var2, this.byteArrayIsSerializedToOracleBlob);
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.version;
      this.version = var1;
      this.isSet_version = var1 != null;
      this.checkChange("version", var2, this.version);
   }

   public boolean getAllowReadonlyCreateAndRemove() {
      return this.allowReadonlyCreateAndRemove;
   }

   public void setAllowReadonlyCreateAndRemove(boolean var1) {
      boolean var2 = this.allowReadonlyCreateAndRemove;
      this.allowReadonlyCreateAndRemove = var1;
      this.isSet_allowReadonlyCreateAndRemove = true;
      this.checkChange("allowReadonlyCreateAndRemove", var2, this.allowReadonlyCreateAndRemove);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<compatibility");
      var2.append(">\n");
      if (this.isSet_byteArrayIsSerializedToOracleBlob || this.getByteArrayIsSerializedToOracleBlob()) {
         var2.append(ToXML.indent(var1 + 2)).append("<byte-array-is-serialized-to-oracle-blob>").append(ToXML.capitalize(Boolean.valueOf(this.getByteArrayIsSerializedToOracleBlob()).toString())).append("</byte-array-is-serialized-to-oracle-blob>\n");
      }

      if (this.isSet_allowReadonlyCreateAndRemove || this.getAllowReadonlyCreateAndRemove()) {
         var2.append(ToXML.indent(var1 + 2)).append("<allow-readonly-create-and-remove>").append(ToXML.capitalize(Boolean.valueOf(this.getAllowReadonlyCreateAndRemove()).toString())).append("</allow-readonly-create-and-remove>\n");
      }

      if (this.isSet_loadRelatedBeansFromDbInPostCreate || this.getLoadRelatedBeansFromDbInPostCreate()) {
         var2.append(ToXML.indent(var1 + 2)).append("<load-related-beans-from-db-in-post-create>").append(ToXML.capitalize(Boolean.valueOf(this.getLoadRelatedBeansFromDbInPostCreate()).toString())).append("</load-related-beans-from-db-in-post-create>\n");
      }

      var2.append(ToXML.indent(var1)).append("</compatibility>\n");
      return var2.toString();
   }
}
