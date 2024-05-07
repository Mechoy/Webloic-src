package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.FilterMBean;
import weblogic.management.descriptors.webapp.FilterMappingMBean;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class FilterMappingDescriptor extends BaseServletDescriptor implements ToXML, FilterMappingMBean {
   private static final long serialVersionUID = 3697699795239081065L;
   private static final String FILTER_NAME = "filter-name";
   private static final String URL_PATTERN = "url-pattern";
   private static final String SERVLET_NAME = "servlet-name";
   private FilterMBean filter;
   private String urlPattern;
   private ServletMBean servlet;
   private String servletNameLink;

   public FilterMappingDescriptor() {
   }

   public FilterMappingDescriptor(FilterMappingMBean var1) {
      this.setFilter(var1.getFilter());
      this.setUrlPattern(var1.getUrlPattern());
      this.setServlet(var1.getServlet());
   }

   public FilterMappingDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      String var3 = DOMUtils.getValueByTagName(var2, "filter-name");
      FilterMBean[] var4 = var1.getFilters();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getFilterName().equals(var3)) {
               this.filter = var4[var5];
            }
         }
      }

      this.urlPattern = DOMUtils.getOptionalValueByTagName(var2, "url-pattern");
      String var8 = DOMUtils.getOptionalValueByTagName(var2, "servlet-name");
      ServletMBean[] var6 = var1.getServlets();
      if (var6 != null) {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7].getServletName().equals(var8)) {
               this.servlet = var6[var7];
            }
         }
      } else {
         this.servletNameLink = var8;
      }

      if (this.urlPattern != null && this.urlPattern.length() > 0 && (this.servlet != null || this.servletNameLink != null)) {
         this.addDescriptorError("MULTIPLE_DEFINES_FILTER_MAPPING", var3);
      }

      if ((this.urlPattern == null || this.urlPattern.length() == 0) && this.servlet == null && this.servletNameLink == null) {
         this.addDescriptorError("NO_FILTER_MAPPING_DEF", var3);
      }

   }

   public FilterMBean getFilter() {
      return this.filter;
   }

   public void setFilter(FilterMBean var1) {
      FilterMBean var2 = this.filter;
      this.filter = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("filter", var2, var1);
      }

   }

   public ServletMBean getServlet() {
      return this.servlet;
   }

   public void setServlet(ServletMBean var1) {
      ServletMBean var2 = this.servlet;
      this.servlet = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("servet", var2, var1);
      }

   }

   public String getUrlPattern() {
      return this.urlPattern;
   }

   public void setUrlPattern(String var1) {
      String var2 = this.urlPattern;
      this.urlPattern = var1;
      if (!comp(var2, this.urlPattern)) {
         this.firePropertyChange("urlPattern", var2, this.urlPattern);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.getFilter() == null) {
         this.addDescriptorError("NO_FILTER_NAME");
         var1 = false;
      }

      ServletMBean var2 = this.getServlet();
      String var3 = this.getUrlPattern();
      if ((var2 != null || this.servletNameLink != null) && var3 != null && var3.length() > 0) {
         this.addDescriptorError("MULTIPLE_DEFINES_FILTER_MAPPING", var2.getServletName());
         var1 = false;
      }

      if (var2 == null && this.servletNameLink == null && (var3 == null || var3.length() == 0)) {
         this.addDescriptorError("NO_FILTER_MAPPING_DEF");
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
      var2 = var2 + this.indentStr(var1) + "<filter-mapping>\n";
      var1 += 2;
      FilterMBean var3 = this.getFilter();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<filter-name>" + var3.getFilterName() + "</filter-name>\n";
      }

      ServletMBean var4 = this.getServlet();
      if (var4 != null) {
         String var5 = var4.getServletName();
         var2 = var2 + this.indentStr(var1) + "<servlet-name>" + var5 + "</servlet-name>\n";
      } else if (this.servletNameLink != null) {
         var2 = var2 + this.indentStr(var1) + "<servlet-name>" + this.servletNameLink + "</servlet-name>\n";
      } else {
         var2 = var2 + this.indentStr(var1) + "<url-pattern>" + this.getUrlPattern() + "</url-pattern>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</filter-mapping>\n";
      return var2;
   }
}
