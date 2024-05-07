package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.management.descriptors.webappext.InitAsMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class InitAsDescriptor extends BaseServletDescriptor implements InitAsMBean {
   private String servletName;
   private String principalName;
   private ServletMBean servlet;
   private WebAppDescriptor wad;

   public InitAsDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.wad = var1;
      this.setServletName(DOMUtils.getValueByTagName(var2, "servlet-name"));
      this.setPrincipalName(DOMUtils.getValueByTagName(var2, "principal-name"));
   }

   public void setServletName(String var1) throws DOMProcessingException {
      String var2 = this.servletName;
      this.servletName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("servletName", var2, var1);
      }

      ServletMBean[] var3 = this.wad.getServlets();
      if (var3 == null) {
         HTTPLogger.logServletNotFound(var1);
         throw new DOMProcessingException("Servlet with name " + var1 + " not defined in web.xml");
      } else {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] != null && var1.equals(var3[var4].getServletName())) {
               this.servlet = var3[var4];
               break;
            }
         }

         if (this.servlet == null) {
            HTTPLogger.logServletNotFound(var1);
            throw new DOMProcessingException("Servlet with name " + var1 + " not defined in web.xml");
         } else {
            this.setIdentity();
         }
      }
   }

   public String getServletName() {
      return this.servletName;
   }

   public void setServlet(ServletMBean var1) {
      this.servlet = var1;
      this.servletName = var1.getName();
      this.setIdentity();
   }

   public ServletMBean getServlet() {
      return this.servlet;
   }

   public void setPrincipalName(String var1) {
      String var2 = this.principalName;
      this.principalName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("principalName", var2, var1);
      }

      this.setIdentity();
   }

   public String getPrincipalName() {
      return this.principalName;
   }

   public void setIdentity() {
      if (this.servlet != null && this.principalName != null) {
         this.servlet.setInitAs(this.principalName);
      }

   }

   public String toString() {
      return this.getServletName();
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<init-as>\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<servlet-name>" + this.getServletName() + "</servlet-name>\n";
      var2 = var2 + this.indentStr(var1) + "<principal-name>" + this.getPrincipalName() + "</principal-name>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</init-as>\n";
      return var2;
   }
}
