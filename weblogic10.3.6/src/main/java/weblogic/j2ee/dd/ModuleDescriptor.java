package weblogic.j2ee.dd;

import java.io.StringWriter;
import weblogic.application.ApplicationFileManager;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.application.ModuleMBean;
import weblogic.utils.io.XMLWriter;

public abstract class ModuleDescriptor extends XMLElementMBeanDelegate implements ModuleMBean {
   private String uri;
   private String altDDuri;

   public ModuleDescriptor() {
   }

   public ModuleDescriptor(String var1) {
      this();
      this.uri = var1;
      this.altDDuri = null;
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      String var2 = this.uri;
      this.uri = var1;
      this.checkChange("uri", var2, var1);
   }

   public String getModuleURI() {
      return this.uri;
   }

   public void setModuleURI(String var1) {
      String var2 = this.uri;
      this.uri = var1;
      this.checkChange("moduleURI", var2, var1);
   }

   public String getModuleKey() {
      return this.uri;
   }

   public String getName() {
      return this.uri;
   }

   public void setName(String var1) {
      this.setURI(var1);
   }

   public String getAltDDURI() {
      return this.altDDuri;
   }

   public void setAltDDURI(String var1) {
      String var2 = this.altDDuri;
      this.altDDuri = var1;
      this.checkChange("altDDURI", var2, var1);
   }

   public void register() throws ManagementException {
      super.register();
   }

   public void unregister() throws ManagementException {
      super.unregister();
   }

   public String toXML() {
      StringWriter var1 = new StringWriter();
      this.toXML(new XMLWriter(var1));
      return var1.toString();
   }

   public abstract void toXML(XMLWriter var1);

   public abstract String getAdminMBeanType(ApplicationFileManager var1);
}
