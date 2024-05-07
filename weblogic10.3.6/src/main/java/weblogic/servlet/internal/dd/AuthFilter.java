package weblogic.servlet.internal.dd;

import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.AuthFilterMBean;

public class AuthFilter extends BaseServletDescriptor implements AuthFilterMBean {
   String filter;

   public AuthFilter() {
   }

   public AuthFilter(String var1) {
      this.filter = var1;
   }

   public AuthFilter(AuthFilterMBean var1) {
      if (var1 != null) {
         this.filter = var1.getFilter();
      }

   }

   public String getFilter() {
      return this.filter;
   }

   public void setFilter(String var1) {
      String var2 = this.filter;
      this.filter = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("filter", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      return this.filter == null ? "" : "<auth-filter>" + this.filter + "</auth-filter>";
   }
}
