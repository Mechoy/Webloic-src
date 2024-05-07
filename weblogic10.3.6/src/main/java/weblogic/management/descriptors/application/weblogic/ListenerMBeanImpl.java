package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ListenerMBeanImpl extends XMLElementMBeanDelegate implements ListenerMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_runAsPrincipalName = false;
   private String runAsPrincipalName;
   private boolean isSet_listenerUri = false;
   private String listenerUri;
   private boolean isSet_listenerClass = false;
   private String listenerClass;

   public String getRunAsPrincipalName() {
      return this.runAsPrincipalName;
   }

   public void setRunAsPrincipalName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.runAsPrincipalName;
      this.runAsPrincipalName = var1;
      this.isSet_runAsPrincipalName = var1 != null;
      this.checkChange("runAsPrincipalName", var2, this.runAsPrincipalName);
   }

   public String getListenerUri() {
      return this.listenerUri;
   }

   public void setListenerUri(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.listenerUri;
      this.listenerUri = var1;
      this.isSet_listenerUri = var1 != null;
      this.checkChange("listenerUri", var2, this.listenerUri);
   }

   public String getListenerClass() {
      return this.listenerClass;
   }

   public void setListenerClass(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.listenerClass;
      this.listenerClass = var1;
      this.isSet_listenerClass = var1 != null;
      this.checkChange("listenerClass", var2, this.listenerClass);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<listener");
      var2.append(">\n");
      if (null != this.getListenerClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<listener-class>").append(this.getListenerClass()).append("</listener-class>\n");
      }

      if (null != this.getListenerUri()) {
         var2.append(ToXML.indent(var1 + 2)).append("<listener-uri>").append(this.getListenerUri()).append("</listener-uri>\n");
      }

      if (null != this.getRunAsPrincipalName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<run-as-principal-name>").append(this.getRunAsPrincipalName()).append("</run-as-principal-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</listener>\n");
      return var2.toString();
   }
}
