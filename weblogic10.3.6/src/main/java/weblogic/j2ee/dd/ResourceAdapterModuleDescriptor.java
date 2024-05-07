package weblogic.j2ee.dd;

import weblogic.application.ApplicationFileManager;
import weblogic.management.descriptors.application.ConnectorModuleMBean;
import weblogic.utils.io.XMLWriter;

public final class ResourceAdapterModuleDescriptor extends ModuleDescriptor implements ConnectorModuleMBean {
   private static final long serialVersionUID = -6519032324695892753L;

   public ResourceAdapterModuleDescriptor() {
   }

   public ResourceAdapterModuleDescriptor(String var1) {
      super(var1);
   }

   public void toXML(XMLWriter var1) {
      var1.println("<module>");
      var1.incrIndent();
      var1.println("<connector>" + this.getURI() + "</connector>");
      if (this.getAltDDURI() != null) {
         var1.println("<alt-dd>" + this.getAltDDURI() + "</alt-dd>");
      }

      var1.decrIndent();
      var1.println("</module>");
   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return "ConnectorComponent";
   }
}
