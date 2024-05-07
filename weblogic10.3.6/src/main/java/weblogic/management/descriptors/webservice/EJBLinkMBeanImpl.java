package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBLinkMBeanImpl extends XMLElementMBeanDelegate implements EJBLinkMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_path = false;
   private String path;

   public String getPath() {
      return this.path;
   }

   public void setPath(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.path;
      this.path = var1;
      this.isSet_path = var1 != null;
      this.checkChange("path", var2, this.path);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb-link");
      if (this.isSet_path) {
         var2.append(" path=\"").append(String.valueOf(this.getPath())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</ejb-link>\n");
      return var2.toString();
   }
}
