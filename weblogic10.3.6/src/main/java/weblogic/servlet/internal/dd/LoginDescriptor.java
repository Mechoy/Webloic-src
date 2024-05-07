package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.LoginConfigMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class LoginDescriptor extends BaseServletDescriptor implements ToXML, LoginConfigMBean {
   private static final long serialVersionUID = -6175838651266572918L;
   private static final String AUTH_METHOD = "auth-method";
   private static final String REALM_NAME = "realm-name";
   private static final String FORM_LOGIN_CONFIG = "form-login-config";
   private static final String FORM_LOGIN_PAGE = "form-login-page";
   private static final String FORM_ERROR_PAGE = "form-error-page";
   public static final String AM_NONE = "";
   public static final String AM_BASIC = "BASIC";
   public static final String AM_DIGEST = "DIGEST";
   public static final String AM_FORM = "FORM";
   public static final String AM_CLIENT_CERT = "CLIENT-CERT";
   String authMethod;
   String realmName;
   String loginPage;
   String errorPage;

   public LoginDescriptor() {
   }

   public LoginDescriptor(LoginConfigMBean var1) {
      this.setAuthMethod(var1.getAuthMethod());
      this.setRealmName(var1.getRealmName());
      this.setLoginPage(var1.getLoginPage());
      this.setErrorPage(var1.getErrorPage());
   }

   public LoginDescriptor(Element var1) throws DOMProcessingException {
      this.realmName = DOMUtils.getOptionalValueByTagName(var1, "realm-name");
      this.authMethod = DOMUtils.getOptionalValueByTagName(var1, "auth-method");
      if (this.authMethod != null) {
         if ("BASIC".equalsIgnoreCase(this.authMethod)) {
            this.authMethod = "BASIC";
         } else if ("CLIENT-CERT".equalsIgnoreCase(this.authMethod)) {
            this.authMethod = "CLIENT-CERT";
         } else if ("DIGEST".equalsIgnoreCase(this.authMethod)) {
            this.authMethod = "DIGEST";
         } else if ("FORM".equalsIgnoreCase(this.authMethod)) {
            this.authMethod = "FORM";
         }
      }

      Element var2 = DOMUtils.getOptionalElementByTagName(var1, "form-login-config");
      if (var2 != null) {
         this.loginPage = DOMUtils.getValueByTagName(var2, "form-login-page");
         this.errorPage = DOMUtils.getValueByTagName(var2, "form-error-page");
         if (this.loginPage != null && !this.loginPage.startsWith("/")) {
            this.loginPage = "/" + this.loginPage;
         }

         if (this.errorPage != null && !this.errorPage.startsWith("/")) {
            this.errorPage = "/" + this.errorPage;
         }
      }

   }

   public String getAuthMethod() {
      return this.authMethod;
   }

   public void setAuthMethod(String var1) {
      String var2 = this.getAuthMethod();
      this.authMethod = var1;
      if (!comp(var2, this.getAuthMethod())) {
         this.firePropertyChange("authMethod", var2, this.getAuthMethod());
      }

   }

   public String getRealmName() {
      return this.realmName;
   }

   public void setRealmName(String var1) {
      String var2 = this.realmName;
      this.realmName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("realmName", var2, var1);
      }

   }

   public String getLoginPage() {
      return this.loginPage;
   }

   public void setLoginPage(String var1) {
      String var2 = this.loginPage;
      this.loginPage = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("loginPage", var2, var1);
      }

   }

   public String getErrorPage() {
      return this.errorPage;
   }

   public void setErrorPage(String var1) {
      String var2 = this.errorPage;
      this.errorPage = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("errorPage", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.loginPage != null) {
         this.loginPage = this.loginPage.trim();
      }

      if (this.errorPage != null) {
         this.errorPage = this.errorPage.trim();
      }

      if (this.loginPage != null && this.loginPage.length() == 0) {
         this.addDescriptorError("NO_LOGIN_PAGE");
         var1 = false;
      }

      if (this.errorPage != null && this.errorPage.length() == 0) {
         this.addDescriptorError("NO_LOGIN_ERROR_PAGE");
         var1 = false;
      }

      if (var1) {
         if (this.loginPage != null && this.errorPage == null) {
            this.addDescriptorError("NO_LOGIN_ERROR_PAGE");
            var1 = false;
         } else if (this.errorPage != null && this.loginPage == null) {
            this.addDescriptorError("NO_LOGIN_PAGE");
            var1 = false;
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
      String var3 = this.getAuthMethod();
      if (var3 == null && this.realmName == null && this.loginPage == null && this.errorPage == null) {
         return var2;
      } else {
         var2 = var2 + this.indentStr(var1) + "<login-config>\n";
         var1 += 2;
         if (var3 != null) {
            var2 = var2 + this.indentStr(var1) + "<auth-method>" + var3 + "</auth-method>\n";
         }

         if (this.realmName != null) {
            var2 = var2 + this.indentStr(var1) + "<realm-name>" + this.realmName + "</realm-name>\n";
         }

         if (this.authMethod == "FORM" && this.loginPage != null && this.errorPage != null) {
            var2 = var2 + this.indentStr(var1) + "<form-login-config>\n";
            var1 += 2;
            var2 = var2 + this.indentStr(var1) + "<form-login-page>" + this.loginPage + "</form-login-page>\n";
            var2 = var2 + this.indentStr(var1) + "<form-error-page>" + this.errorPage + "</form-error-page>\n";
            var1 -= 2;
            var2 = var2 + this.indentStr(var1) + "</form-login-config>\n";
         }

         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</login-config>\n";
         return var2;
      }
   }

   public String toString() {
      return "LoginDescriptor(method=" + this.getAuthMethod() + ", realm=" + this.realmName + ", login=" + this.loginPage + ", error=" + this.errorPage + ")";
   }
}
