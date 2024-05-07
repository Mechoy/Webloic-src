package weblogic.servlet.internal.dd;

import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.UserDataConstraintMBean;

public final class UserDataConstraint extends BaseServletDescriptor implements UserDataConstraintMBean {
   private static final long serialVersionUID = 4772501842036136387L;
   public static final String NONE = "NONE";
   public static final String INTEGRAL = "INTEGRAL";
   public static final String CONFIDENTIAL = "CONFIDENTIAL";
   String description;
   String transportGuarantee;

   public UserDataConstraint() {
   }

   public UserDataConstraint(UserDataConstraintMBean var1) {
      if (var1 != null) {
         this.setDescription(var1.getDescription());
         this.setTransportGuarantee(var1.getTransportGuarantee());
      } else {
         this.transportGuarantee = "NONE";
      }

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

   public String getTransportGuarantee() {
      return this.transportGuarantee;
   }

   public void setTransportGuarantee(String var1) {
      String var2 = this.transportGuarantee;
      this.transportGuarantee = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("transportGuarantee", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toString() {
      String var1 = "AuthConstraintDescriptor(";
      var1 = var1 + "guarantee=" + this.transportGuarantee;
      var1 = var1 + ")";
      return var1;
   }

   public String toXML(int var1) {
      String var2 = "";
      String var3 = "NONE";
      if ("INTEGRAL".equals(this.getTransportGuarantee())) {
         var3 = "INTEGRAL";
      } else if ("CONFIDENTIAL".equals(this.getTransportGuarantee())) {
         var3 = "CONFIDENTIAL";
      }

      var2 = var2 + this.indentStr(var1) + "<user-data-constraint>\n";
      var1 += 2;
      if (this.description != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + this.description + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<transport-guarantee>" + var3 + "</transport-guarantee>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</user-data-constraint>\n";
      return var2;
   }
}
