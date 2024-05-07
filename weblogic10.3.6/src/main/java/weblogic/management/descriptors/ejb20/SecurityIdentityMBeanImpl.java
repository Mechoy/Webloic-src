package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SecurityIdentityMBeanImpl extends XMLElementMBeanDelegate implements SecurityIdentityMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_useCallerIdentity = false;
   private boolean useCallerIdentity;
   private boolean isSet_runAs = false;
   private RunAsMBean runAs;
   private boolean isSet_description = false;
   private String description;

   public boolean getUseCallerIdentity() {
      return this.useCallerIdentity;
   }

   public void setUseCallerIdentity(boolean var1) {
      boolean var2 = this.useCallerIdentity;
      this.useCallerIdentity = var1;
      this.isSet_useCallerIdentity = true;
      this.checkChange("useCallerIdentity", var2, this.useCallerIdentity);
   }

   public RunAsMBean getRunAs() {
      return this.runAs;
   }

   public void setRunAs(RunAsMBean var1) {
      RunAsMBean var2 = this.runAs;
      this.runAs = var1;
      this.isSet_runAs = var1 != null;
      this.checkChange("runAs", var2, this.runAs);
   }

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<security-identity");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getRunAs()) {
         var2.append(this.getRunAs().toXML(var1 + 2)).append("\n");
      } else if (this.isSet_useCallerIdentity) {
         var2.append(ToXML.indent(var1 + 2)).append(this.getUseCallerIdentity() ? "<use-caller-identity/>\n" : "");
      }

      var2.append(ToXML.indent(var1)).append("</security-identity>\n");
      return var2.toString();
   }
}
