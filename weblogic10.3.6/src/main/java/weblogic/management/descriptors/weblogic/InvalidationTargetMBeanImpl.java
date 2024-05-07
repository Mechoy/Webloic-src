package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class InvalidationTargetMBeanImpl extends XMLElementMBeanDelegate implements InvalidationTargetMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbName = false;
   private String ejbName;

   public String getEJBName() {
      return this.ejbName;
   }

   public void setEJBName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbName;
      this.ejbName = var1;
      this.isSet_ejbName = var1 != null;
      this.checkChange("ejbName", var2, this.ejbName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<invalidation-target");
      var2.append(">\n");
      if (null != this.getEJBName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-name>").append(this.getEJBName()).append("</ejb-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</invalidation-target>\n");
      return var2.toString();
   }
}
