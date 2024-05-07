package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ShutdownMBeanImpl extends XMLElementMBeanDelegate implements ShutdownMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_shutdownClass = false;
   private String shutdownClass;
   private boolean isSet_shutdownUri = false;
   private String shutdownUri;

   public String getShutdownClass() {
      return this.shutdownClass;
   }

   public void setShutdownClass(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.shutdownClass;
      this.shutdownClass = var1;
      this.isSet_shutdownClass = var1 != null;
      this.checkChange("shutdownClass", var2, this.shutdownClass);
   }

   public String getShutdownUri() {
      return this.shutdownUri;
   }

   public void setShutdownUri(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.shutdownUri;
      this.shutdownUri = var1;
      this.isSet_shutdownUri = var1 != null;
      this.checkChange("shutdownUri", var2, this.shutdownUri);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<shutdown");
      var2.append(">\n");
      if (null != this.getShutdownClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<shutdown-class>").append(this.getShutdownClass()).append("</shutdown-class>\n");
      }

      if (null != this.getShutdownUri()) {
         var2.append(ToXML.indent(var1 + 2)).append("<shutdown-uri>").append(this.getShutdownUri()).append("</shutdown-uri>\n");
      }

      var2.append(ToXML.indent(var1)).append("</shutdown>\n");
      return var2.toString();
   }
}
