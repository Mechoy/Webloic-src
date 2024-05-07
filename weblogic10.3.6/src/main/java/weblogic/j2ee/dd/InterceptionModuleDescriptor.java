package weblogic.j2ee.dd;

import weblogic.application.ApplicationFileManager;
import weblogic.management.descriptors.application.InterceptionModuleMBean;
import weblogic.utils.io.XMLWriter;

public final class InterceptionModuleDescriptor extends ModuleDescriptor implements InterceptionModuleMBean {
   public InterceptionModuleDescriptor() {
   }

   public InterceptionModuleDescriptor(String var1) {
      super(var1);
   }

   public void toXML(XMLWriter var1) {
      var1.println("<module>");
      var1.incrIndent();
      var1.println("<jms>" + this.getURI() + "</jms>");
      if (this.getAltDDURI() != null) {
         var1.println("<alt-dd>" + this.getAltDDURI() + "</alt-uri>");
      }

      var1.decrIndent();
      var1.println("</module>");
   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return null;
   }
}
