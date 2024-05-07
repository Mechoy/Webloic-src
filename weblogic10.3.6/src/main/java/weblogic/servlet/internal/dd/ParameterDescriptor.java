package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ParameterMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ParameterDescriptor extends BaseServletDescriptor implements ToXML, ParameterMBean {
   private static final long serialVersionUID = 4406607312692360385L;
   private static final String PARAM_NAME = "param-name";
   private static final String PARAM_VALUE = "param-value";
   private static final String DESCRIPTION = "description";
   private String description;
   private String paramName;
   private String paramValue;

   public ParameterDescriptor() {
      this((String)null, (String)null, (String)null);
   }

   public ParameterDescriptor(ParameterMBean var1) {
      this(var1.getParamName(), var1.getParamValue(), var1.getDescription());
   }

   public ParameterDescriptor(String var1, String var2, String var3) {
      this.description = var3;
      this.paramName = var1;
      this.paramValue = var2;
   }

   public ParameterDescriptor(Element var1) throws DOMProcessingException {
      this.description = DOMUtils.getOptionalValueByTagName(var1, "description");
      this.paramName = DOMUtils.getValueByTagName(var1, "param-name");
      this.paramValue = DOMUtils.getValueByTagName(var1, "param-value");
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

   public String getParamName() {
      return this.paramName;
   }

   public void setParamName(String var1) {
      String var2 = this.paramName;
      this.paramName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("paramName", var2, var1);
      }

   }

   public String getParamValue() {
      return this.paramValue;
   }

   public void setParamValue(String var1) {
      String var2 = this.paramValue;
      this.paramValue = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("paramValue", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.paramName == null || this.paramName.length() == 0) {
         this.addDescriptorError("NO_PARAM_NAME", this.paramValue);
         var1 = false;
      }

      if (this.paramValue == null) {
         this.addDescriptorError("NO_PARAM_VALUE", this.paramName);
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
      var2 = var2 + this.indentStr(var1) + "<context-param>\n";
      var1 += 2;
      String var3 = this.paramValue;
      if (var3 == null) {
         var3 = "";
      }

      var2 = var2 + this.indentStr(var1) + "<param-name>" + this.paramName + "</param-name>\n";
      var2 = var2 + this.indentStr(var1) + "<param-value>" + var3 + "</param-value>\n";
      if (this.description != null && this.description.length() > 0) {
         var2 = var2 + this.indentStr(var1) + "<description>" + this.description + "</description>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</context-param>\n";
      return var2;
   }
}
