package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.AuthConstraintMBean;
import weblogic.management.descriptors.webapp.SecurityRoleMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class AuthConstraintDescriptor extends BaseServletDescriptor implements ToXML, AuthConstraintMBean {
   private static final long serialVersionUID = -6323007461514307324L;
   private static final String ROLE_NAME = "role-name";
   private static final String DESCRIPTION = "description";
   private String description;
   private SecurityRoleMBean[] roles;

   public AuthConstraintDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.description = DOMUtils.getOptionalValueByTagName(var2, "description");
      List var3 = DOMUtils.getOptionalElementsByTagName(var2, "role-name");
      if (var3 != null && !var3.isEmpty()) {
         List var4 = DOMUtils.getTextDataValues(var3);
         if (var4 != null && !var4.isEmpty()) {
            SecurityRoleMBean[] var5 = var1.getSecurityRoles();
            Hashtable var6 = new Hashtable();

            for(int var7 = 0; var7 < var5.length; ++var7) {
               var6.put(var5[var7].getRoleName(), var5[var7]);
            }

            ArrayList var12 = new ArrayList();
            Iterator var8 = var4.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               if (!var9.equals("*")) {
                  SecurityRoleMBean var10 = (SecurityRoleMBean)var6.get(var9);
                  if (var10 == null) {
                     HTTPLogger.logBadSecurityRoleInAC(var9);
                  } else {
                     var12.add(var10);
                  }
               } else {
                  SecurityRoleDescriptor var11 = new SecurityRoleDescriptor();
                  var11.setRoleName("*");
                  var12.add(var11);
               }
            }

            this.roles = (SecurityRoleMBean[])((SecurityRoleMBean[])var12.toArray(new SecurityRoleMBean[0]));
         }
      }

   }

   public AuthConstraintDescriptor() {
      this.roles = new SecurityRoleMBean[0];
   }

   public AuthConstraintDescriptor(AuthConstraintMBean var1) {
      this.setDescription(var1.getDescription());
      this.setRoles(var1.getRoles());
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      SecurityRoleMBean[] var1 = this.getRoles();
      if (var1 == null || var1.length == 0) {
         this.addDescriptorError("NO_ROLE_NAMES");
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public void setRoles(SecurityRoleMBean[] var1) {
      SecurityRoleMBean[] var2 = this.roles;
      this.roles = var1;
      if (!comp(var2, this.roles)) {
         this.firePropertyChange("roles", var2, this.roles);
      }

   }

   public SecurityRoleMBean[] getRoles() {
      return this.roles;
   }

   public void addRole(SecurityRoleMBean var1) {
      SecurityRoleMBean[] var2 = this.getRoles();
      if (var2 == null) {
         var2 = new SecurityRoleMBean[]{var1};
         this.setRoles(var2);
      } else {
         SecurityRoleMBean[] var3 = new SecurityRoleMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setRoles(var3);
      }
   }

   public void removeRole(SecurityRoleMBean var1) {
      SecurityRoleMBean[] var2 = this.getRoles();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            SecurityRoleMBean[] var5 = new SecurityRoleMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setRoles(var5);
         }

      }
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<auth-constraint>\n";
      var1 += 2;
      if (this.description != null && this.description.length() > 0) {
         var2 = var2 + this.indentStr(var1) + "<description>" + this.description + "</description>\n";
      }

      for(int var3 = 0; this.roles != null && var3 < this.roles.length; ++var3) {
         if (this.roles[var3] != null) {
            String var4 = this.roles[var3].getRoleName();
            var2 = var2 + this.indentStr(var1) + "<role-name>" + var4 + "</role-name>\n";
         }
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</auth-constraint>\n";
      return var2;
   }

   public String toString() {
      String var1 = "AuthConstraintDescriptor(";
      String var2 = "{";

      for(int var3 = 0; var3 < this.roles.length; ++var3) {
         var2 = var2 + this.roles[var3].getRoleName();
         if (var3 == this.roles.length - 1) {
            var2 = var2 + "}";
         } else {
            var2 = var2 + ",";
         }
      }

      var1 = var1 + "roles=" + var2 + ",";
      var1 = var1 + ")";
      return var1;
   }
}
