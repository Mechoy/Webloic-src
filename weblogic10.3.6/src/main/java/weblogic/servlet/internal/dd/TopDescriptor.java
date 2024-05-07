package weblogic.servlet.internal.dd;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.utils.io.XMLWriter;

public final class TopDescriptor extends BaseServletDescriptor implements TopLevelDescriptorMBean {
   private WebAppDescriptor wad;
   private WLWebAppDescriptor wl;
   private String loadPath;

   public TopDescriptor() {
      this.wad = new WebAppDescriptor();
      this.wl = new WLWebAppDescriptor();
   }

   public TopDescriptor(WebAppDescriptor var1, WLWebAppDescriptor var2) {
      this.wad = var1;
      this.wl = var2;
   }

   public void _setLoadPath(String var1) {
      this.loadPath = var1;
   }

   public String _getLoadPath() {
      return this.loadPath;
   }

   public void setStandard(WebAppDescriptor var1) {
      this.wad = var1;
   }

   public WebAppDescriptor getStandard() {
      return this.wad;
   }

   public void setWeblogic(WLWebAppDescriptor var1) {
      this.wl = var1;
   }

   public WLWebAppDescriptor getWeblogic() {
      return this.wl;
   }

   public void toXML(XMLWriter var1) {
      this.wad.toXML(var1);
      this.wl.toXML(var1);
   }

   public String toXML(int var1) {
      StringWriter var2 = new StringWriter();
      XMLWriter var3 = new XMLWriter(var2);
      this.toXML(var3);
      var3.flush();
      return var2.toString();
   }

   public void register() throws ManagementException {
   }

   public void usePersistenceDestination(String var1) {
      this._setLoadPath(var1);
   }

   public void validate() throws DescriptorValidationException {
      this.wad.validate();
      this.wl.validate();
   }

   public void persist(Properties var1) throws IOException {
   }

   public void persist() throws IOException {
   }
}
