package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ResourceRefMBeanImpl extends XMLElementMBeanDelegate implements ResourceRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_resAuth = false;
   private String resAuth;
   private boolean isSet_resRefName = false;
   private String resRefName;
   private boolean isSet_resType = false;
   private String resType;

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.description;
      this.description = var1;
      this.isSet_description = var1 != null;
      this.checkChange("description", var2, this.description);
   }

   public String getResAuth() {
      return this.resAuth;
   }

   public void setResAuth(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resAuth;
      this.resAuth = var1;
      this.isSet_resAuth = var1 != null;
      this.checkChange("resAuth", var2, this.resAuth);
   }

   public String getResRefName() {
      return this.resRefName;
   }

   public void setResRefName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resRefName;
      this.resRefName = var1;
      this.isSet_resRefName = var1 != null;
      this.checkChange("resRefName", var2, this.resRefName);
   }

   public String getResType() {
      return this.resType;
   }

   public void setResType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resType;
      this.resType = var1;
      this.isSet_resType = var1 != null;
      this.checkChange("resType", var2, this.resType);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<resource-ref");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getResRefName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<res-ref-name>").append(this.getResRefName()).append("</res-ref-name>\n");
      }

      if (null != this.getResType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<res-type>").append(this.getResType()).append("</res-type>\n");
      }

      if (null != this.getResAuth()) {
         var2.append(ToXML.indent(var1 + 2)).append("<res-auth>").append(this.getResAuth()).append("</res-auth>\n");
      }

      var2.append(ToXML.indent(var1)).append("</resource-ref>\n");
      return var2.toString();
   }
}
