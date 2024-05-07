package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.management.descriptors.webappext.ServletDescriptorMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class WLServletDescriptor extends BaseServletDescriptor implements ServletDescriptorMBean {
   private static final String SERVLET_DESCRIPTOR = "servlet-descriptor";
   private static final String SERVLET_NAME = "servlet-name";
   private static final String RUN_AS_PRINCIPAL_NAME = "run-as-principal-name";
   private static final String INIT_AS_PRINCIPAL_NAME = "init-as-principal-name";
   private static final String DESTROY_AS_PRINCIPAL_NAME = "destroy-as-principal-name";
   private static final String DISPATCH_POLICY = "dispatch-policy";
   private String servletName = null;
   private String runAsPrincipalName = null;
   private String initAsPrincipalName = null;
   private String destroyAsPrincipalName = null;
   private String dispatchPolicy = null;
   private WebAppDescriptor wad = null;
   private ServletMBean servlet = null;

   public WLServletDescriptor() {
   }

   public WLServletDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.wad = var1;
      this.setServletName(DOMUtils.getValueByTagName(var2, "servlet-name"));
      this.setRunAsPrincipalName(DOMUtils.getOptionalValueByTagName(var2, "run-as-principal-name"));
      this.setInitAsPrincipalName(DOMUtils.getOptionalValueByTagName(var2, "init-as-principal-name"));
      this.setDestroyAsPrincipalName(DOMUtils.getOptionalValueByTagName(var2, "destroy-as-principal-name"));
      this.setDispatchPolicy(DOMUtils.getOptionalValueByTagName(var2, "dispatch-policy"));
   }

   public WLServletDescriptor(WebAppDescriptor var1, String var2) throws DOMProcessingException {
      this.wad = var1;
      this.setServletName(var2);
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
            this.servlet.setServletDescriptor(this);
         }
      }
   }

   public String getServletName() {
      return this.servlet != null ? this.servlet.getServletName() : this.servletName;
   }

   public void setServlet(ServletMBean var1) {
      this.servlet = var1;
      this.servletName = var1.getServletName();
      var1.setServletDescriptor(this);
   }

   public ServletMBean getServlet() {
      return this.servlet;
   }

   public void setRunAsPrincipalName(String var1) {
      String var2 = this.runAsPrincipalName;
      this.runAsPrincipalName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("runAsPrincipalName", var2, var1);
      }

   }

   public String getRunAsPrincipalName() {
      return this.runAsPrincipalName;
   }

   public void setInitAsPrincipalName(String var1) {
      String var2 = this.initAsPrincipalName;
      this.initAsPrincipalName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("initAsPrincipalName", var2, var1);
      }

   }

   public String getInitAsPrincipalName() {
      return this.initAsPrincipalName;
   }

   public void setDestroyAsPrincipalName(String var1) {
      String var2 = this.destroyAsPrincipalName;
      this.destroyAsPrincipalName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("destroyAsPrincipalName", var2, var1);
      }

   }

   public String getDestroyAsPrincipalName() {
      return this.destroyAsPrincipalName;
   }

   public void setDispatchPolicy(String var1) {
      String var2 = this.dispatchPolicy;
      this.dispatchPolicy = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("dispatchPolicy", var2, var1);
      }

   }

   public String getDispatchPolicy() {
      return this.dispatchPolicy;
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
      var2 = var2 + this.indentStr(var1) + "<" + "servlet-descriptor" + ">\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<" + "servlet-name" + ">" + this.getServletName() + "</" + "servlet-name" + ">\n";
      if (this.getRunAsPrincipalName() != null) {
         var2 = var2 + this.indentStr(var1) + "<" + "run-as-principal-name" + ">" + this.getRunAsPrincipalName() + "</" + "run-as-principal-name" + ">\n";
      }

      if (this.getInitAsPrincipalName() != null) {
         var2 = var2 + this.indentStr(var1) + "<" + "init-as-principal-name" + ">" + this.getInitAsPrincipalName() + "</" + "init-as-principal-name" + ">\n";
      }

      if (this.getDestroyAsPrincipalName() != null) {
         var2 = var2 + this.indentStr(var1) + "<" + "destroy-as-principal-name" + ">" + this.getDestroyAsPrincipalName() + "</" + "destroy-as-principal-name" + ">\n";
      }

      if (this.getDispatchPolicy() != null) {
         var2 = var2 + this.indentStr(var1) + "<" + "dispatch-policy" + ">" + this.getDispatchPolicy() + "</" + "dispatch-policy" + ">\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</" + "servlet-descriptor" + ">\n";
      return var2;
   }
}
