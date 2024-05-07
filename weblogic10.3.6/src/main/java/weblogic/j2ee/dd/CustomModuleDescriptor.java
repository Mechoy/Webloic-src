package weblogic.j2ee.dd;

import weblogic.application.ApplicationFileManager;
import weblogic.management.descriptors.application.CustomModuleMBean;
import weblogic.utils.io.XMLWriter;

public final class CustomModuleDescriptor extends ModuleDescriptor implements CustomModuleMBean {
   private String providerName;

   public CustomModuleDescriptor() {
   }

   public CustomModuleDescriptor(String var1) {
      super(var1);
   }

   public String getProviderName() {
      return this.providerName;
   }

   public void setProviderName(String var1) {
      this.providerName = var1;
   }

   public void toXML(XMLWriter var1) {
      throw new AssertionError("should be removed soon...");
   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return null;
   }
}
