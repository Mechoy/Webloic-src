package weblogic.servlet.internal.dd;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.SecurityRoleMBean;
import weblogic.management.descriptors.webappext.SecurityRoleAssignmentMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SecurityRoleAssignment extends BaseServletDescriptor implements SecurityRoleAssignmentMBean {
   private static final long serialVersionUID = -3874048726826475026L;
   private final String SECURITY_ROLE_ASSIGNMENT = "security-role-assignment";
   private final String ROLE_NAME = "role-name";
   private final String PRINCIPAL_NAME = "principal-name";
   private final String GLOBAL_ROLE = "global-role";
   private final String EXTERNALLY_DEFINED = "externally-defined";
   private SecurityRoleMBean role = null;
   private String[] principal = null;
   private boolean externallyDefined = false;
   private static String refErr = "Can't define security-role-assignment in weblogic.xml because web.xml has no matching security-role";

   public SecurityRoleAssignment() {
   }

   public SecurityRoleAssignment(WebAppDescriptor var1, SecurityRoleAssignmentMBean var2) throws DOMProcessingException {
      this.setRole(var1, var2.getRole().getRoleName());
      this.setPrincipalNames(var2.getPrincipalNames());
      this.setExternallyDefined(var2.isExternallyDefined());
   }

   public SecurityRoleAssignment(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      String var3 = DOMUtils.getValueByTagName(var2, "role-name");
      if (var3 == null) {
         throw new DOMProcessingException("You must specify a role-name element within security-role-assignment");
      } else {
         this.setRole(var1, var3);
         String var4 = DOMUtils.getOptionalValueByTagName(var2, "externally-defined");
         if (var4 == null) {
            var4 = DOMUtils.getOptionalValueByTagName(var2, "global-role");
         }

         if (var4 == null) {
            this.externallyDefined = false;
         } else if (!var4.equals("") && !var4.equalsIgnoreCase("true")) {
            this.externallyDefined = false;
         } else {
            this.externallyDefined = true;
         }

         List var5 = DOMUtils.getOptionalElementsByTagName(var2, "principal-name");
         if (var5 != null && var5.size() >= 1) {
            Iterator var6 = var5.iterator();
            String[] var7 = new String[var5.size()];

            for(int var8 = 0; var6.hasNext(); ++var8) {
               Element var9 = (Element)var6.next();
               var7[var8] = DOMUtils.getTextData(var9);
            }

            this.setPrincipalNames(var7);
         } else {
            this.setPrincipalNames(new String[0]);
            if (!this.externallyDefined) {
               throw new DOMProcessingException("Neither principal-names nor  externally-defined element specified for security-role-assignment");
            }
         }

      }
   }

   private void setRole(WebAppDescriptor var1, String var2) throws DOMProcessingException {
      SecurityRoleMBean[] var3 = var1.getSecurityRoles();
      if (var3 == null) {
         HTTPLogger.logBadSecurityRoleInSRA(var2);
      }

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getRoleName().equals(var2)) {
            this.role = var3[var4];
            break;
         }
      }

      if (this.role == null) {
         HTTPLogger.logBadSecurityRoleInSRA(var2);
      }

   }

   public void setRole(SecurityRoleMBean var1) {
      if (this.role == null) {
         HTTPLogger.logBadSecurityRoleInSRA(this.getName());
      }

      SecurityRoleMBean var2 = this.role;
      this.role = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("role", var2, var1);
      }

   }

   public SecurityRoleMBean getRole() {
      return this.role;
   }

   public void setPrincipalNames(String[] var1) {
      String[] var2 = this.principal;
      this.principal = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("principalNames", var2, var1);
      }

   }

   public String[] getPrincipalNames() {
      return this.principal;
   }

   public void addPrincipalName(String var1) {
      String[] var2 = this.getPrincipalNames();
      if (var2 == null) {
         var2 = new String[]{var1};
         this.setPrincipalNames(var2);
      } else {
         String[] var3 = new String[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setPrincipalNames(var3);
      }
   }

   public void removePrincipalName(String var1) {
      String[] var2 = this.getPrincipalNames();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            String[] var5 = new String[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setPrincipalNames(var5);
         }

      }
   }

   public void setExternallyDefined(boolean var1) {
      boolean var2 = this.externallyDefined;
      this.externallyDefined = var1;
      if (var2 != var1) {
         this.firePropertyChange("externallyDefined", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isExternallyDefined() {
      return this.externallyDefined;
   }

   public boolean isGlobalRole() {
      return this.isExternallyDefined();
   }

   public void setGlobalRole(boolean var1) {
      this.setExternallyDefined(var1);
   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      if (this.role == null || this.role.getRoleName() == null) {
         this.addDescriptorError("role-name is not set in security-role-assignment");
         var1 = false;
      }

      var1 = this.hasEitherPrincipalOrExternal();
      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   private boolean hasEitherPrincipalOrExternal() {
      boolean var1 = false;
      if ((this.principal == null || this.principal.length == 0) && this.externallyDefined) {
         var1 = true;
      }

      if (this.principal != null && this.principal.length != 0 && !this.externallyDefined) {
         var1 = true;
      }

      if (this.principal != null && this.principal.length != 0 && this.externallyDefined) {
         var1 = false;
      }

      if ((this.principal == null || this.principal.length == 0) && !this.externallyDefined) {
         var1 = false;
      }

      return var1;
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<" + "security-role-assignment" + ">\n";
      var1 += 2;
      if (this.role != null) {
         var2 = var2 + this.indentStr(var1) + "<" + "role-name" + ">" + this.role.getRoleName() + "</" + "role-name" + ">\n";
      }

      if (this.principal != null) {
         for(int var3 = 0; var3 < this.principal.length; ++var3) {
            var2 = var2 + this.indentStr(var1) + "<" + "principal-name" + ">" + this.principal[var3] + "</" + "principal-name" + ">\n";
         }
      }

      if (this.externallyDefined) {
         var2 = var2 + this.indentStr(var1) + "<" + "externally-defined" + "/>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</" + "security-role-assignment" + ">\n";
      return var2;
   }
}
