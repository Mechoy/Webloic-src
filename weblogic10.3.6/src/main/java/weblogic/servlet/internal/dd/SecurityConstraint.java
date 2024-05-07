package weblogic.servlet.internal.dd;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.AuthConstraintMBean;
import weblogic.management.descriptors.webapp.SecurityConstraintMBean;
import weblogic.management.descriptors.webapp.SecurityRoleMBean;
import weblogic.management.descriptors.webapp.UserDataConstraintMBean;
import weblogic.management.descriptors.webapp.WebResourceCollectionMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SecurityConstraint extends BaseServletDescriptor implements ToXML, SecurityConstraintMBean {
   private static final long serialVersionUID = 2835803302540822938L;
   private static final String WEB_RESOURCE_COLLECTION = "web-resource-collection";
   private static final String AUTH_CONSTRAINT = "auth-constraint";
   private static final String USER_DATA_CONSTRAINT = "user-data-constraint";
   private static final String TRANSPORT_GUARANTEE = "transport-guarantee";
   private static final String DISPLAY_NAME = "display-name";
   private static final String DESCRIPTION = "description";
   public static final int TG_NONE = 0;
   public static final int TG_INTEGRAL = 1;
   public static final int TG_CONFIDENTIAL = 2;
   private String displayName;
   private WebResourceCollectionMBean[] webResources;
   private String[] methods = null;
   private AuthConstraintDescriptor authConstraint;
   private UserDataConstraintMBean userDataConstraint;

   public SecurityConstraint() {
   }

   public SecurityConstraint(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.displayName = DOMUtils.getOptionalValueByTagName(var2, "display-name");
      List var3 = DOMUtils.getOptionalElementsByTagName(var2, "web-resource-collection");
      if (var3 != null) {
         Iterator var5 = var3.iterator();
         this.webResources = new WebResourceDescriptor[var3.size()];

         Element var7;
         for(int var6 = 0; var5.hasNext(); this.webResources[var6++] = new WebResourceDescriptor(var7)) {
            var7 = (Element)var5.next();
         }
      }

      Element var4 = DOMUtils.getOptionalElementByTagName(var2, "user-data-constraint");
      if (var4 != null) {
         String var8 = DOMUtils.getOptionalValueByTagName(var4, "description");
         String var9 = DOMUtils.getValueByTagName(var4, "transport-guarantee");
         if (var9 == null) {
            throw new DOMProcessingException("You must specify transport-guarantee inside user-data-constraint element.");
         }

         if ("INTEGRAL".equalsIgnoreCase(var9)) {
            this.userDataConstraint = new UserDataConstraint();
            this.userDataConstraint.setDescription(var8);
            this.userDataConstraint.setTransportGuarantee("INTEGRAL");
         } else if ("CONFIDENTIAL".equalsIgnoreCase(var9)) {
            this.userDataConstraint = new UserDataConstraint();
            this.userDataConstraint.setDescription(var8);
            this.userDataConstraint.setTransportGuarantee("CONFIDENTIAL");
         } else {
            this.userDataConstraint = new UserDataConstraint();
            this.userDataConstraint.setDescription(var8);
            this.userDataConstraint.setTransportGuarantee("NONE");
         }
      }

      var4 = DOMUtils.getOptionalElementByTagName(var2, "auth-constraint");
      if (var4 != null) {
         this.authConstraint = new AuthConstraintDescriptor(var1, var4);
      }

   }

   public SecurityConstraint(SecurityConstraintMBean var1) {
      this.setDisplayName(var1.getDisplayName());
      this.setWebResourceCollection(var1.getWebResourceCollection());
      this.setAuthConstraint(var1.getAuthConstraint());
      this.setUserDataConstraint(new UserDataConstraint(var1.getUserDataConstraint()));
   }

   public String[] getHttpMethods() {
      return this.methods;
   }

   public void setHttpMethods(String[] var1) {
      String[] var2 = this.methods;
      this.methods = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("httpMethods", var2, var1);
      }

   }

   public boolean hasRoleConstraint() {
      if (this.authConstraint == null) {
         return false;
      } else {
         SecurityRoleMBean[] var1 = this.authConstraint.getRoles();
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (var1[var2].getRoleName().equals("*")) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public int getTransportGuarantee() {
      if (this.userDataConstraint == null) {
         return 0;
      } else {
         String var1 = this.userDataConstraint.getTransportGuarantee();
         if ("INTEGRAL".equalsIgnoreCase(var1)) {
            return 1;
         } else {
            return "CONFIDENTIAL".equalsIgnoreCase(var1) ? 2 : 0;
         }
      }
   }

   public void setTransportGuarantee(int var1) {
      int var2 = this.getTransportGuarantee();
      switch (var1) {
         case 0:
         default:
            this.userDataConstraint.setTransportGuarantee("NONE");
            break;
         case 1:
            this.userDataConstraint.setTransportGuarantee("INTEGRAL");
            break;
         case 2:
            this.userDataConstraint.setTransportGuarantee("CONFIDENTIAL");
      }

      if (var2 != this.getTransportGuarantee()) {
         this.firePropertyChange("transportGuarantee", new Integer(this.getTransportGuarantee()), new Integer(var1));
      }

   }

   public WebResourceCollectionMBean[] getWebResourceCollection() {
      return this.webResources;
   }

   public void setWebResourceCollection(WebResourceCollectionMBean[] var1) {
      WebResourceCollectionMBean[] var2 = this.webResources;
      if (var1 != null && var1.length != 0) {
         WebResourceCollectionMBean[] var3 = new WebResourceCollectionMBean[var1.length];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3[var4] = var1[var4];
         }

         this.webResources = var3;
      } else {
         this.webResources = new WebResourceDescriptor[0];
      }

      if (!comp(var2, var1)) {
         this.firePropertyChange("webResourceCollection", var2, var1);
      }

   }

   public void addWebResourceCollection(WebResourceCollectionMBean var1) {
      WebResourceCollectionMBean[] var2 = this.getWebResourceCollection();
      if (var2 == null) {
         var2 = new WebResourceCollectionMBean[]{var1};
         this.setWebResourceCollection(var2);
      } else {
         WebResourceCollectionMBean[] var3 = new WebResourceCollectionMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setWebResourceCollection(var3);
      }
   }

   public void removeWebResourceCollection(WebResourceCollectionMBean var1) {
      WebResourceCollectionMBean[] var2 = this.getWebResourceCollection();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            WebResourceCollectionMBean[] var5 = new WebResourceCollectionMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setWebResourceCollection(var5);
         }

      }
   }

   public AuthConstraintMBean getAuthConstraint() {
      return this.authConstraint;
   }

   public void setAuthConstraint(AuthConstraintMBean var1) {
      AuthConstraintDescriptor var2 = this.authConstraint;
      this.authConstraint = (AuthConstraintDescriptor)var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("authConstraint", var2, var1);
      }

   }

   public UserDataConstraintMBean getUserDataConstraint() {
      return this.userDataConstraint;
   }

   public void setUserDataConstraint(UserDataConstraintMBean var1) {
      UserDataConstraintMBean var2 = this.userDataConstraint;
      this.userDataConstraint = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("userDataConstraint", var2, var1);
      }

   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      String var2 = this.displayName;
      this.displayName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("displayName", var2, var1);
      }

   }

   public String getDisplayString() {
      String var1 = this.getDisplayName();
      if (var1 == null || (var1 = var1.trim()).length() == 0) {
         WebResourceCollectionMBean[] var2 = this.getWebResourceCollection();

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            var1 = var2[var3].getResourceName();
            if (var1 != null && (var1 = var1.trim()).length() > 0) {
               break;
            }
         }
      }

      return var1;
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      if (this.webResources == null || this.webResources.length == 0) {
         this.addDescriptorError("NO_WEB_RESOURCE");
         throw new DescriptorValidationException();
      }
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<security-constraint>\n";
      var1 += 2;
      if (this.getDisplayName() != null) {
         var2 = var2 + this.indentStr(var1) + "<display-name>" + this.getDisplayName() + "</display-name>\n";
      }

      if (this.webResources != null) {
         for(int var3 = 0; var3 < this.webResources.length; ++var3) {
            var2 = var2 + this.webResources[var3].toXML(var1);
         }
      }

      if (this.authConstraint != null) {
         var2 = var2 + this.authConstraint.toXML(var1);
      }

      if (this.userDataConstraint != null) {
         var2 = var2 + this.userDataConstraint.toXML(var1);
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</security-constraint>\n";
      return var2;
   }

   public String toString() {
      String var1 = "SecurityConstraint(";
      var1 = var1 + "display=" + this.displayName + ",";
      String[] var2 = this.getHttpMethods();
      if (var2 == null) {
         var1 = var1 + "methods=null,";
      } else {
         String var3 = "{";

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3 = var3 + var2[var4];
            if (var4 == var2.length - 1) {
               var3 = var3 + "}";
            } else {
               var3 = var3 + ",";
            }
         }

         var1 = var1 + "methods=" + var3 + ",";
      }

      var1 = var1 + "guarantee=" + this.getTransportGuarantee() + ",";
      var1 = var1 + "constrained=" + this.hasRoleConstraint() + ",";
      var1 = var1 + "auth=" + this.authConstraint + ",";
      WebResourceCollectionMBean[] var6 = this.getWebResourceCollection();
      if (var6 == null) {
         var1 = var1 + "wrc=null,";
      } else {
         String var7 = "{";

         for(int var5 = 0; var5 < var6.length; ++var5) {
            var7 = var7 + var6[var5];
            if (var5 == var6.length - 1) {
               var7 = var7 + "}";
            } else {
               var7 = var7 + ",";
            }
         }

         var1 = var1 + "wrc=" + var7 + ",";
      }

      var1 = var1 + "auth=" + this.getAuthConstraint() + ",";
      var1 = var1 + "udc=" + this.getUserDataConstraint();
      var1 = var1 + ")";
      return var1;
   }
}
