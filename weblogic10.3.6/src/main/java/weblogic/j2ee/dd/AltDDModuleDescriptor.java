package weblogic.j2ee.dd;

import weblogic.application.ApplicationFileManager;
import weblogic.management.descriptors.application.AltDDModuleDescriptorMBean;
import weblogic.utils.io.XMLWriter;

public final class AltDDModuleDescriptor extends ModuleDescriptor implements AltDDModuleDescriptorMBean {
   private static final long serialVersionUID = 9003680643645690903L;

   public AltDDModuleDescriptor() {
   }

   public AltDDModuleDescriptor(String var1) {
      super(var1);
   }

   public void toXML(XMLWriter var1) {
      if (this.getURI() != null) {
         var1.println("<alt-dd>");
         var1.incrIndent();
         var1.println(this.getURI());
         var1.decrIndent();
         var1.println("</alt-dd>");
      }

   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return null;
   }
}
