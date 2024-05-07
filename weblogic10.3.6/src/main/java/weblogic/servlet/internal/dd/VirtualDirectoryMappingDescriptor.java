package weblogic.servlet.internal.dd;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.VirtualDirectoryMappingMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class VirtualDirectoryMappingDescriptor extends BaseServletDescriptor implements VirtualDirectoryMappingMBean, ToXML {
   private static final String VIRTUAL_DIRECTORY_MAPPING = "virtual-directory-mapping";
   private static final String LOCAL_PATH = "local-path";
   private static final String URL_PATTERN = "url-pattern";
   private String localPath;
   private String[] urlPatterns;
   private static String refErr = "Invalid virtual-directory-mapping in weblogic.xml";

   public VirtualDirectoryMappingDescriptor() {
      this.localPath = null;
      this.urlPatterns = null;
      this.localPath = null;
      this.urlPatterns = null;
   }

   public VirtualDirectoryMappingDescriptor(String var1, String[] var2) throws DOMProcessingException {
      this.localPath = null;
      this.urlPatterns = null;
      this.localPath = var1;
      this.urlPatterns = var2;
   }

   public VirtualDirectoryMappingDescriptor(VirtualDirectoryMappingMBean var1) throws DOMProcessingException {
      this(var1.getLocalPath(), var1.getURLPatterns());
   }

   public VirtualDirectoryMappingDescriptor(Element var1) throws DOMProcessingException {
      this.localPath = null;
      this.urlPatterns = null;
      this.setLocalPath(DOMUtils.getOptionalValueByTagName(var1, "local-path"));
      List var2 = DOMUtils.getValuesByTagName(var1, "url-pattern");
      if (var2 == null) {
         this.setURLPatterns(new String[0]);
      } else {
         Iterator var3 = var2.iterator();
         String[] var4 = new String[var2.size()];
         var2.toArray(var4);
         this.setURLPatterns(var4);
      }

   }

   public String getLocalPath() {
      return this.localPath;
   }

   public void setLocalPath(String var1) {
      String var2 = this.localPath;
      this.localPath = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("localPath", var2, var1);
      }

   }

   public String[] getURLPatterns() {
      return this.urlPatterns;
   }

   public void setURLPatterns(String[] var1) {
      String[] var2 = this.urlPatterns;
      this.urlPatterns = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("urlPatterns", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.localPath == null) {
         this.addDescriptorError("local-path is not set in virtual-directory-mapping");
         var1 = false;
      }

      if (this.urlPatterns != null) {
         for(int var2 = 0; var2 < this.urlPatterns.length; ++var2) {
            if (this.urlPatterns[var2] == null) {
               this.addDescriptorError("url-pattern is null in virtual-directory-mapping");
               var1 = false;
            }
         }
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
      var2 = var2 + this.indentStr(var1) + "<" + "virtual-directory-mapping" + ">\n";
      var1 += 2;
      if (this.localPath != null) {
         var2 = var2 + this.indentStr(var1) + "<" + "local-path" + ">" + this.localPath + "</" + "local-path" + ">\n";
      }

      if (this.urlPatterns != null) {
         for(int var3 = 0; var3 < this.urlPatterns.length; ++var3) {
            var2 = var2 + this.indentStr(var1) + "<" + "url-pattern" + ">" + this.urlPatterns[var3] + "</" + "url-pattern" + ">\n";
         }
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</" + "virtual-directory-mapping" + ">\n";
      return var2;
   }
}
