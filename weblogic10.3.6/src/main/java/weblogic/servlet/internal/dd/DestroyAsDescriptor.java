package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.management.descriptors.webappext.DestroyAsMBean;
import weblogic.xml.dom.DOMProcessingException;

public final class DestroyAsDescriptor extends InitAsDescriptor implements DestroyAsMBean {
   public DestroyAsDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      super(var1, var2);
   }

   public void setIdentity() {
      ServletMBean var1 = this.getServlet();
      String var2 = this.getPrincipalName();
      if (var1 != null && var2 != null) {
         var1.setDestroyAs(var2);
      }

   }

   public String toString() {
      return this.getServletName();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<destroy-as>\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<servlet-name>" + this.getServletName() + "</servlet-name>\n";
      var2 = var2 + this.indentStr(var1) + "<principal-name>" + this.getPrincipalName() + "</principal-name>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</destroy-as>\n";
      return var2;
   }
}
