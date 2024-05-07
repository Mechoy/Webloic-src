package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StartupMBeanImpl extends XMLElementMBeanDelegate implements StartupMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_startupUri = false;
   private String startupUri;
   private boolean isSet_startupClass = false;
   private String startupClass;

   public String getStartupUri() {
      return this.startupUri;
   }

   public void setStartupUri(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.startupUri;
      this.startupUri = var1;
      this.isSet_startupUri = var1 != null;
      this.checkChange("startupUri", var2, this.startupUri);
   }

   public String getStartupClass() {
      return this.startupClass;
   }

   public void setStartupClass(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.startupClass;
      this.startupClass = var1;
      this.isSet_startupClass = var1 != null;
      this.checkChange("startupClass", var2, this.startupClass);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<startup");
      var2.append(">\n");
      if (null != this.getStartupClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<startup-class>").append(this.getStartupClass()).append("</startup-class>\n");
      }

      if (null != this.getStartupUri()) {
         var2.append(ToXML.indent(var1 + 2)).append("<startup-uri>").append(this.getStartupUri()).append("</startup-uri>\n");
      }

      var2.append(ToXML.indent(var1)).append("</startup>\n");
      return var2.toString();
   }
}
