package weblogic.j2ee.dd;

import weblogic.application.ApplicationFileManager;
import weblogic.management.descriptors.application.JavaModuleMBean;
import weblogic.utils.io.XMLWriter;

public final class JavaModuleDescriptor extends ModuleDescriptor implements JavaModuleMBean {
   private static final long serialVersionUID = -5838871575860088884L;

   public JavaModuleDescriptor() {
   }

   public JavaModuleDescriptor(String var1) {
      super(var1);
   }

   public void toXML(XMLWriter var1) {
      var1.println("<module>");
      var1.incrIndent();
      var1.println("<java>" + this.getURI() + "</java>");
      if (this.getAltDDURI() != null) {
         var1.println("<alt-dd>" + this.getAltDDURI() + "</alt-dd>");
      }

      var1.decrIndent();
      var1.println("</module>");
   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return "AppClientComponent";
   }
}
