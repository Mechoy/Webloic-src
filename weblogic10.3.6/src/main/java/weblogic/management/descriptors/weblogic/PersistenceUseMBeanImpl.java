package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PersistenceUseMBeanImpl extends XMLElementMBeanDelegate implements PersistenceUseMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_typeStorage = false;
   private String typeStorage;
   private boolean isSet_typeVersion = false;
   private String typeVersion;
   private boolean isSet_typeIdentifier = false;
   private String typeIdentifier;

   public String getTypeStorage() {
      return this.typeStorage;
   }

   public void setTypeStorage(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.typeStorage;
      this.typeStorage = var1;
      this.isSet_typeStorage = var1 != null;
      this.checkChange("typeStorage", var2, this.typeStorage);
   }

   public String getTypeVersion() {
      return this.typeVersion;
   }

   public void setTypeVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.typeVersion;
      this.typeVersion = var1;
      this.isSet_typeVersion = var1 != null;
      this.checkChange("typeVersion", var2, this.typeVersion);
   }

   public String getTypeIdentifier() {
      return this.typeIdentifier;
   }

   public void setTypeIdentifier(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.typeIdentifier;
      this.typeIdentifier = var1;
      this.isSet_typeIdentifier = var1 != null;
      this.checkChange("typeIdentifier", var2, this.typeIdentifier);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<persistence-use");
      var2.append(">\n");
      if (null != this.getTypeIdentifier()) {
         var2.append(ToXML.indent(var1 + 2)).append("<type-identifier>").append(this.getTypeIdentifier()).append("</type-identifier>\n");
      }

      if (null != this.getTypeVersion()) {
         var2.append(ToXML.indent(var1 + 2)).append("<type-version>").append(this.getTypeVersion()).append("</type-version>\n");
      }

      if (null != this.getTypeStorage()) {
         var2.append(ToXML.indent(var1 + 2)).append("<type-storage>").append(this.getTypeStorage()).append("</type-storage>\n");
      }

      var2.append(ToXML.indent(var1)).append("</persistence-use>\n");
      return var2.toString();
   }
}
