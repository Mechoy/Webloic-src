package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.PreprocessorMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class PreprocessorDescriptor extends BaseServletDescriptor implements ToXML, PreprocessorMBean {
   private static final String PREPROCESSOR_NAME = "preprocessor-name";
   private static final String PREPROCESSOR_CLASS = "preprocessor-class";
   private static boolean debug = false;
   private String preprocessorName;
   private String preprocessorClass;

   public PreprocessorDescriptor() {
   }

   public PreprocessorDescriptor(PreprocessorMBean var1) {
      this.setPreprocessorName(var1.getPreprocessorName());
      this.setPreprocessorClass(var1.getPreprocessorClass());
   }

   public PreprocessorDescriptor(Element var1) throws DOMProcessingException {
      this.preprocessorName = DOMUtils.getValueByTagName(var1, "preprocessor-name");
      this.preprocessorClass = DOMUtils.getValueByTagName(var1, "preprocessor-class");
      if (this.preprocessorClass == null) {
         throw new DOMProcessingException("Preprocessor node does not contain preprocessor-class node");
      }
   }

   public String getPreprocessorName() {
      return this.preprocessorName;
   }

   public void setPreprocessorName(String var1) {
      String var2 = this.preprocessorName;
      this.preprocessorName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("preprocessorName", var2, var1);
      }

   }

   public String getPreprocessorClass() {
      return this.preprocessorClass;
   }

   public void setPreprocessorClass(String var1) {
      String var2 = this.preprocessorClass;
      this.preprocessorClass = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("preprocessorClass", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      String var2 = this.getPreprocessorName();
      if (var2 != null && (var2 = var2.trim()).length() != 0) {
         this.setPreprocessorName(var2);
      } else {
         this.addDescriptorError("NO_PREPROCESSOR_NAME");
         var1 = false;
      }

      var2 = this.getPreprocessorClass();
      if (var2 == null) {
         this.addDescriptorError("NO_PREPROCESSOR_CLASS", this.getPreprocessorName() == null ? "" : this.getPreprocessorName());
         var1 = false;
      }

      if (var2 != null) {
         var2 = var2.trim();
         this.setPreprocessorClass(var2);
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.indentStr(var1));
      var2.append("<preprocessor>\n");
      var2.append(this.indentStr(var1 + 2));
      var2.append("<preprocessor-name>");
      var2.append(this.getPreprocessorName());
      var2.append("</preprocessor-name>\n");
      var2.append(this.indentStr(var1 + 2));
      var2.append("<preprocessor-class>");
      var2.append(this.getPreprocessorClass());
      var2.append("</preprocessor-class>\n");
      var2.append(this.indentStr(var1));
      var2.append("</preprocessor>");
      if (debug) {
         System.out.println(var2);
      }

      return var2.toString();
   }
}
