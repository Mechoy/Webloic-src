package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.management.descriptors.webapp.ServletMappingMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ServletMappingDescriptor extends BaseServletDescriptor implements ToXML, ServletMappingMBean {
   private static final long serialVersionUID = -2074799428020037595L;
   private static final String refErr = "servlet-mapping references a servlet that has not been defined";
   private static final String SERVLET_NAME = "servlet-name";
   private static final String URL_PATTERN = "url-pattern";
   private ServletMBean servlet;
   private String urlPattern;
   private String servletNameLink;

   public ServletMappingDescriptor() {
      this.servlet = null;
      this.urlPattern = "";
   }

   public ServletMappingDescriptor(WebAppDescriptor var1, ServletMappingMBean var2) {
      this(var2.getServlet(), var2.getURLPattern());
   }

   public ServletMappingDescriptor(ServletMBean var1, String var2) {
      this.servlet = var1;
      this.urlPattern = var2;
   }

   public ServletMappingDescriptor(WebAppDescriptor var1, String var2, String var3) {
      this.setServlet(var1, var2);
      this.urlPattern = var3;
   }

   public ServletMappingDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.urlPattern = DOMUtils.getValueByTagName(var2, "url-pattern");
      String var3 = DOMUtils.getValueByTagName(var2, "servlet-name");
      if (var1 != null) {
         this.setServlet(var1, var3);
      }

   }

   private void setServlet(WebAppDescriptor var1, String var2) {
      if (var1 == null) {
         HTTPLogger.logServletNotFound(var2);
      } else if (var2 == null) {
         HTTPLogger.logServletNotFound("null");
      } else {
         ServletMBean[] var3 = var1.getServlets();
         if (var3 == null) {
            HTTPLogger.logServletNotFound(var2);
            this.servletNameLink = var2;
         } else {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4] != null && var2.equals(var3[var4].getServletName())) {
                  this.servlet = var3[var4];
                  break;
               }
            }

            if (this.servlet == null) {
               this.servletNameLink = var2;
               HTTPLogger.logServletNotFound(var2);
            }
         }
      }
   }

   public ServletMBean getServlet() {
      return this.servlet;
   }

   public void setServlet(ServletMBean var1) {
      ServletMBean var2 = this.servlet;
      this.servlet = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("servlet", var2, var1);
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
      ServletMBean var2 = this.getServlet();
      String var3 = this.getURLPattern();
      if (var3 != null) {
         var3 = var3.trim();
         this.setURLPattern(var3);
      }

      if (var3 == null || var3.length() == 0) {
         this.addDescriptorError("NO_URL_PATTERN", var2 == null ? "" : var2.getServletName());
         var1 = false;
      }

      if (var2 == null) {
         this.addDescriptorError("NO_MAPPING_SERVLET_NAME", var3);
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
      ServletMBean var3 = this.getServlet();
      String var4 = var3 == null ? this.servletNameLink : var3.getServletName();
      var2 = var2 + this.indentStr(var1) + "<servlet-mapping>\n";
      var2 = var2 + this.indentStr(var1 + 2) + "<servlet-name>" + var4 + "</servlet-name>\n";
      var2 = var2 + this.indentStr(var1 + 2) + "<url-pattern>" + this.urlPattern + "</url-pattern>\n";
      var2 = var2 + this.indentStr(var1) + "</servlet-mapping>\n";
      return var2;
   }
}
