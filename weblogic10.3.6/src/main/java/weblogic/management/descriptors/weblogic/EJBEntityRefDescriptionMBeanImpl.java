package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBEntityRefDescriptionMBeanImpl extends XMLElementMBeanDelegate implements EJBEntityRefDescriptionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_remoteEJBName = false;
   private String remoteEJBName;
   private boolean isSet_jndiName = false;
   private String jndiName;

   public String getRemoteEJBName() {
      return this.remoteEJBName;
   }

   public void setRemoteEJBName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.remoteEJBName;
      this.remoteEJBName = var1;
      this.isSet_remoteEJBName = var1 != null;
      this.checkChange("remoteEJBName", var2, this.remoteEJBName);
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
      var2.append(ToXML.indent(var1)).append("<ejb-entity-ref-description");
      var2.append(">\n");
      if (null != this.getRemoteEJBName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<remote-ejb-name>").append(this.getRemoteEJBName()).append("</remote-ejb-name>\n");
      }

      if (null != this.getJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jndi-name>").append(this.getJNDIName()).append("</jndi-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb-entity-ref-description>\n");
      return var2.toString();
   }
}
