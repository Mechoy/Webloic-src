package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.PreprocessorMBean;
import weblogic.management.descriptors.webappext.PreprocessorMappingMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class PreprocessorMappingDescriptor extends BaseServletDescriptor implements PreprocessorMappingMBean, ToXML {
   private static final String PREPROCESSOR_NAME = "preprocessor-name";
   private static final String URL_PATTERN = "url-pattern";
   private PreprocessorMBean preprocessor;
   private String urlPattern;
   private static boolean debug = true;

   public PreprocessorMappingDescriptor() {
      this.preprocessor = null;
      this.urlPattern = "";
   }

   public PreprocessorMappingDescriptor(WLWebAppDescriptor var1, PreprocessorMappingMBean var2) {
      this(var2.getPreprocessor(), var2.getURLPattern());
   }

   public PreprocessorMappingDescriptor(PreprocessorMBean var1, String var2) {
      this.preprocessor = var1;
      this.urlPattern = var2;
   }

   public PreprocessorMappingDescriptor(WLWebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.urlPattern = DOMUtils.getValueByTagName(var2, "url-pattern");
      String var3 = DOMUtils.getValueByTagName(var2, "preprocessor-name");
      if (var1 != null) {
         this.setPreprocessor(var1, var3);
      }

   }

   public void setPreprocessor(WLWebAppDescriptor var1, String var2) {
      if (var1 == null) {
         HTTPLogger.logPreprocessorNotFound(var2);
      } else if (var2 == null) {
         HTTPLogger.logPreprocessorNotFound("null");
      } else {
         PreprocessorMBean[] var3 = var1.getPreprocessors();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4] != null && var2.equals(var3[var4].getPreprocessorName())) {
                  this.preprocessor = var3[var4];
                  break;
               }
            }

            if (this.preprocessor == null) {
               HTTPLogger.logPreprocessorNotFound(var2);
            }
         }
      }
   }

   public PreprocessorMBean getPreprocessor() {
      return this.preprocessor;
   }

   public void setPreprocessor(PreprocessorMBean var1) {
      PreprocessorMBean var2 = this.preprocessor;
      this.preprocessor = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("preprocessor", var2, var1);
      }

   }

   public String getURLPattern() {
      return this.urlPattern;
   }

   public void setURLPattern(String var1) {
      String var2 = this.urlPattern;
      this.urlPattern = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("urlPattern", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      PreprocessorMBean var2 = this.getPreprocessor();
      String var3 = this.getURLPattern();
      if (var3 != null) {
         var3 = var3.trim();
         this.setURLPattern(var3);
      }

      if (var3 == null || var3.length() == 0) {
         this.addDescriptorError("NO_PREPROCESSOR_URL_PATTERN", var2 == null ? "" : var2.getPreprocessorName());
         var1 = false;
      }

      if (var2 == null) {
         this.addDescriptorError("NO_MAPPING_PREPROCESSOR_NAME", var3);
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
      StringBuffer var2 = new StringBuffer("");
      PreprocessorMBean var3 = this.getPreprocessor();
      String var4 = var3 == null ? "" : var3.getPreprocessorName();
      var2.append(this.indentStr(var1));
      var2.append(this.indentStr(var1 + 2));
      var2.append("<preprocessor-mapping>\n");
      var2.append(this.indentStr(var1 + 2));
      var2.append("<preprocessor-name>");
      var2.append(var4);
      var2.append("</preprocessor-name>\n");
      var2.append(this.indentStr(var1 + 2));
      var2.append("<url-pattern>");
      var2.append(this.urlPattern);
      var2.append("</url-pattern>\n");
      var2.append(this.indentStr(var1));
      var2.append("</preprocessor-mapping>\n");
      if (debug) {
         System.err.println(var2);
      }

      return var2.toString();
   }
}
