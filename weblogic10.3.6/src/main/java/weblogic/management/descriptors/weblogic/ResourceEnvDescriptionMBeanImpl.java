package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ResourceEnvDescriptionMBeanImpl extends XMLElementMBeanDelegate implements ResourceEnvDescriptionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_resEnvRefName = false;
   private String resEnvRefName;
   private boolean isSet_jndiName = false;
   private String jndiName;

   public String getResEnvRefName() {
      return this.resEnvRefName;
   }

   public void setResEnvRefName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.resEnvRefName;
      this.resEnvRefName = var1;
      this.isSet_resEnvRefName = var1 != null;
      this.checkChange("resEnvRefName", var2, this.resEnvRefName);
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.jndiName;
      this.jndiName = var1;
      this.isSet_jndiName = var1 != null;
      this.checkChange("jndiName", var2, this.jndiName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<resource-env-description");
      var2.append(">\n");
      if (null != this.getResEnvRefName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<res-env-ref-name>").append(this.getResEnvRefName()).append("</res-env-ref-name>\n");
      }

      if (null != this.getJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jndi-name>").append(this.getJNDIName()).append("</jndi-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</resource-env-description>\n");
      return var2.toString();
   }
}
