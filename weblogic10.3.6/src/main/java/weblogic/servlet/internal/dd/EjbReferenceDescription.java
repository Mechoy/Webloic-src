package weblogic.servlet.internal.dd;

import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.EjbRefMBean;
import weblogic.management.descriptors.webappext.EjbReferenceDescriptionMBean;

public final class EjbReferenceDescription extends BaseServletDescriptor implements EjbReferenceDescriptionMBean {
   private static final long serialVersionUID = -4288451935850905034L;
   private static String refErr = "Can't define ejb-reference-description in weblogic.xml because web.xml has no matching ejb-ref";
   EjbRefMBean ref;
   String jndiName;

   public EjbReferenceDescription() {
   }

   public EjbReferenceDescription(EjbReferenceDescriptionMBean var1) {
      this.setEjbReference(var1.getEjbReference());
      this.setJndiName(var1.getJndiName());
   }

   public void setEjbReference(EjbRefMBean var1) {
      EjbRefMBean var2 = this.ref;
      this.ref = var1;
      if (!comp(var2, this.ref)) {
         this.firePropertyChange("ejbReference", var2, this.ref);
      }

   }

   public EjbRefMBean getEjbReference() {
      return this.ref;
   }

   public void setJndiName(String var1) {
      String var2 = this.jndiName;
      this.jndiName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("jndiName", var2, var1);
      }

   }

   public String getJndiName() {
      return this.jndiName != null ? this.jndiName : this.getEjbReference().getEJBRefName();
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      return "NYI";
   }

   public String toString() {
      return "EjbReferenceDescription(" + this.ref + "," + this.jndiName + ")";
   }
}
