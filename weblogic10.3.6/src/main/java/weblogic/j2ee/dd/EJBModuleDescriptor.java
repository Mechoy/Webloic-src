package weblogic.j2ee.dd;

import weblogic.application.ApplicationFileManager;
import weblogic.management.descriptors.application.EJBModuleMBean;
import weblogic.utils.io.XMLWriter;

public final class EJBModuleDescriptor extends ModuleDescriptor implements EJBModuleMBean {
   private static final long serialVersionUID = -3810058314921396582L;

   public EJBModuleDescriptor() {
   }

   public EJBModuleDescriptor(String var1) {
      super(var1);
   }

   public void toXML(XMLWriter var1) {
      var1.println("<module>");
      var1.incrIndent();
      var1.println("<ejb>" + this.getURI() + "</ejb>");
      if (this.getAltDDURI() != null) {
         var1.println("<alt-dd>" + this.getAltDDURI() + "</alt-dd>");
      }

      var1.decrIndent();
      var1.println("</module>");
   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return "EJBComponent";
   }
}
