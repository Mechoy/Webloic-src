package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBReferenceDescriptionMBeanImpl extends XMLElementMBeanDelegate implements EJBReferenceDescriptionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbRefName = false;
   private String ejbRefName;
   private boolean isSet_jndiName = false;
   private String jndiName;

   public String getEJBRefName() {
      return this.ejbRefName;
   }

   public void setEJBRefName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbRefName;
      this.ejbRefName = var1;
      this.isSet_ejbRefName = var1 != null;
      this.checkChange("ejbRefName", var2, this.ejbRefName);
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
      var2.append(ToXML.indent(var1)).append("<ejb-reference-description");
      var2.append(">\n");
      if (null != this.getEJBRefName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-ref-name>").append(this.getEJBRefName()).append("</ejb-ref-name>\n");
      }

      if (null != this.getJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jndi-name>").append(this.getJNDIName()).append("</jndi-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb-reference-description>\n");
      return var2.toString();
   }
}
