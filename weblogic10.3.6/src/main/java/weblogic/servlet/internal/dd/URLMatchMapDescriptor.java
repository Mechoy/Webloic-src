package weblogic.servlet.internal.dd;

import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.URLMatchMapMBean;

public class URLMatchMapDescriptor extends BaseServletDescriptor implements ToXML, URLMatchMapMBean {
   private static final String URL_MATCH_MAP = "url-match-map";
   private String mapClass;

   public URLMatchMapDescriptor() {
   }

   public URLMatchMapDescriptor(String var1) {
      this.mapClass = var1;
   }

   public URLMatchMapDescriptor(URLMatchMapMBean var1) {
      if (var1 != null) {
         this.mapClass = var1.getClassName();
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      String var2 = this.getClassName();
      if (var2 == null || var2.length() == 0) {
         this.addDescriptorError("NO_URL_MATCH_MAP_CLASS");
         var1 = false;
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String getClassName() {
      return this.mapClass;
   }

   public void setClassName(String var1) {
      String var2 = this.mapClass;
      this.mapClass = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("className", var2, var1);
      }

   }

   public String toXML(int var1) {
      if (this.mapClass != null) {
         this.mapClass = this.mapClass.trim();
         if (this.mapClass.length() == 0) {
            this.mapClass = null;
         }
      }

      if (this.mapClass != null) {
         String var2 = "";
         var2 = var2 + this.indentStr(var1) + "<" + "url-match-map" + ">\n";
         var1 += 2;
         var2 = var2 + this.indentStr(var1) + this.mapClass + "\n";
         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</" + "url-match-map" + ">\n";
         return var2;
      } else {
         return "";
      }
   }
}
