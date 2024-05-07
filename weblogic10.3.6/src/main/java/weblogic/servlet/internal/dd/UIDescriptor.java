package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.UIMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class UIDescriptor extends BaseServletDescriptor implements UIMBean {
   private static final long serialVersionUID = 6939835176059638336L;
   public static final String DESCRIPTION = "description";
   public static final String DISPLAY_NAME = "display-name";
   public static final String LARGE_ICON = "large-icon";
   public static final String SMALL_ICON = "small-icon";
   public static final String ICON = "icon";
   private String description;
   private String displayName;
   private String largeIconFileName;
   private String smallIconFileName;

   public UIDescriptor() {
      this((String)null, (String)null, (String)null, (String)null);
   }

   public UIDescriptor(String var1, String var2, String var3, String var4) {
      this.description = var1;
      this.displayName = var2;
      this.largeIconFileName = var3;
      this.smallIconFileName = var4;
   }

   public UIDescriptor(UIMBean var1) {
      this(var1.getDescription(), var1.getDisplayName(), var1.getLargeIconFileName(), var1.getSmallIconFileName());
   }

   public UIDescriptor(Element var1) throws DOMProcessingException {
      this.description = DOMUtils.getOptionalValueByTagName(var1, "description");
      this.displayName = DOMUtils.getOptionalValueByTagName(var1, "display-name");
      this.largeIconFileName = DOMUtils.getOptionalValueByTagName(var1, "large-icon");
      this.smallIconFileName = DOMUtils.getOptionalValueByTagName(var1, "small-icon");
      if (this.largeIconFileName == null && this.smallIconFileName == null) {
         Element var2 = DOMUtils.getOptionalElementByTagName(var1, "icon");
         if (var2 != null) {
            this.largeIconFileName = DOMUtils.getOptionalValueByTagName(var2, "large-icon");
            this.smallIconFileName = DOMUtils.getOptionalValueByTagName(var2, "small-icon");
         }
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      String var2 = this.displayName;
      this.displayName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("displayName", var2, var1);
      }

   }

   public String getLargeIconFileName() {
      return this.largeIconFileName;
   }

   public void setLargeIconFileName(String var1) {
      String var2 = this.largeIconFileName;
      this.largeIconFileName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("largeIconFileName", var2, var1);
      }

   }

   public String getSmallIconFileName() {
      return this.smallIconFileName;
   }

   public void setSmallIconFileName(String var1) {
      String var2 = this.smallIconFileName;
      this.smallIconFileName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("smallIconFileName", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      String var3 = this.getSmallIconFileName();
      String var4 = this.getLargeIconFileName();
      if (var3 != null || var4 != null) {
         var2 = var2 + this.indentStr(var1) + "<icon>\n";
         if (var3 != null) {
            var2 = var2 + this.indentStr(var1 + 2) + "<small-icon>" + var3 + "</small-icon>\n";
         }

         if (var4 != null) {
            var2 = var2 + this.indentStr(var1 + 2) + "<large-icon>" + var4 + "</large-icon>\n";
         }

         var2 = var2 + this.indentStr(var1) + "</icon>\n";
      }

      if (this.getDisplayName() != null) {
         var2 = var2 + this.indentStr(var1) + "<display-name>" + this.getDisplayName() + "</display-name>\n";
      }

      if (this.getDescription() != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + this.getDescription() + "</description>\n";
      }

      return var2;
   }
}
