package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.MimeMappingMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class MimeMappingDescriptor extends BaseServletDescriptor implements ToXML, MimeMappingMBean {
   private static final long serialVersionUID = -7123219540911766475L;
   private static final String EXTENSION = "extension";
   private static final String MIME_TYPE = "mime-type";
   private String extension;
   private String mimeType;

   public MimeMappingDescriptor() {
      this("", "");
   }

   public MimeMappingDescriptor(MimeMappingMBean var1) {
      this(var1.getExtension(), var1.getMimeType());
   }

   public MimeMappingDescriptor(String var1, String var2) {
      this.extension = var1;
      this.mimeType = var2;
   }

   public MimeMappingDescriptor(Element var1) throws DOMProcessingException {
      this.extension = DOMUtils.getValueByTagName(var1, "extension");
      this.mimeType = DOMUtils.getValueByTagName(var1, "mime-type");
   }

   public String getExtension() {
      return this.extension;
   }

   public void setExtension(String var1) {
      String var2 = this.extension;
      this.extension = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("extension", var2, var1);
      }

   }

   public String getMimeType() {
      return this.mimeType;
   }

   public void setMimeType(String var1) {
      String var2 = this.mimeType;
      this.mimeType = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("mimeType", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.extension == null || (this.extension = this.extension.trim()).length() == 0) {
         this.addDescriptorError("NO_MIME_EXTENSION", this.mimeType);
         var1 = false;
      }

      if (this.mimeType == null || (this.mimeType = this.mimeType.trim()).length() == 0) {
         this.addDescriptorError("NO_MIME_TYPE", this.extension);
         var1 = false;
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<mime-mapping>\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<extension>" + this.extension + "</extension>\n";
      var2 = var2 + this.indentStr(var1) + "<mime-type>" + this.mimeType + "</mime-type>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</mime-mapping>\n";
      return var2;
   }
}
