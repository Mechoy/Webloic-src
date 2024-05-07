package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ResourceDescriptionMBeanImpl extends XMLElementMBeanDelegate implements ResourceDescriptionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_resRefName = false;
   private String resRefName;
   private boolean isSet_jndiName = false;
   private String jndiName;

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
      var2.append(ToXML.indent(var1)).append("<resource-description");
      var2.append(">\n");
      if (null != this.getResRefName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<res-ref-name>").append(this.getResRefName()).append("</res-ref-name>\n");
      }

      if (null != this.getJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jndi-name>").append(this.getJNDIName()).append("</jndi-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</resource-description>\n");
      return var2.toString();
   }
}
