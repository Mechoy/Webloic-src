package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ErrorPageMBean;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ErrorPageDescriptor extends BaseServletDescriptor implements ToXML, ErrorPageMBean {
   private static final long serialVersionUID = -1138390047099452104L;
   private static final String ERROR_CODE = "error-code";
   private static final String EXCEPTION_TYPE = "exception-type";
   private static final String LOCATION = "location";
   private String eCode;
   private String eType;
   private String location;

   public ErrorPageDescriptor() {
   }

   public ErrorPageDescriptor(ErrorPageMBean var1) {
      this(var1.getErrorCode(), var1.getExceptionType(), var1.getLocation());
   }

   public ErrorPageDescriptor(String var1, String var2, String var3) {
      this.eCode = var1;
      this.eType = var2;
      this.location = var3;
   }

   public ErrorPageDescriptor(Element var1) throws DOMProcessingException {
      this.eCode = DOMUtils.getOptionalValueByTagName(var1, "error-code");
      if (this.eCode == null) {
         this.eType = DOMUtils.getOptionalValueByTagName(var1, "exception-type");
      }

      this.location = DOMUtils.getValueByTagName(var1, "location");
   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      String var2 = this.getErrorCode();
      String var3 = this.getExceptionType();
      if (var2 != null && var2.length() > 0 && var3 != null && var3.length() > 0) {
         this.addDescriptorError("MULTIPLE_DEFINES_ERROR_PAGE", var2, var3);
         var1 = false;
      }

      if (var2 != null && var2.length() > 0) {
         this.eCode = var2.trim();

         try {
            Integer.parseInt(this.eCode);
         } catch (Exception var5) {
            this.addDescriptorError("INVALID_ERROR_CODE", this.eCode);
            var1 = false;
         }
      }

      String var4 = this.getLocation();
      if (var4 == null || var4.trim().length() == 0) {
         this.addDescriptorError("NO_ERROR_PAGE_LOCATION");
         var1 = false;
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public void setError(String var1) {
      try {
         var1 = var1.trim();
         Integer.parseInt(var1);
         this.setErrorCode(var1);
         this.setExceptionType((String)null);
      } catch (Exception var3) {
         this.setErrorCode((String)null);
         this.setExceptionType(var1);
      }

   }

   public String getError() {
      return this.eCode != null && this.eCode.length() > 0 ? this.eCode : this.eType;
   }

   public String getErrorCode() {
      return this.eCode;
   }

   public void setErrorCode(String var1) {
      String var2 = this.eCode;
      this.eCode = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("errorCode", var2, var1);
      }

   }

   public String getExceptionType() {
      return this.eType;
   }

   public void setExceptionType(String var1) {
      String var2 = this.eType;
      this.eType = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("exceptionType", var2, var1);
      }

   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String var1) {
      String var2 = this.location;
      this.location = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("location", var2, var1);
      }

   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<error-page>\n";
      var1 += 2;
      if (this.eCode != null) {
         var2 = var2 + this.indentStr(var1) + "<error-code>" + this.eCode + "</error-code>\n";
      } else {
         var2 = var2 + this.indentStr(var1) + "<exception-type>" + this.eType + "</exception-type>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<location>" + this.location + "</location>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</error-page>\n";
      return var2;
   }

   public static void main(String[] var0) throws Exception {
      ErrorPageDescriptor var1 = new ErrorPageDescriptor("401", (String)null, "/404.jsp");
      XMLWriter var2 = new XMLWriter(System.out);
      var1.toXML(var2);
      var2.flush();
   }
}
