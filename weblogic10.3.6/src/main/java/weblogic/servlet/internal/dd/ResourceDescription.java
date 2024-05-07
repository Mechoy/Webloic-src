package weblogic.servlet.internal.dd;

import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ResourceRefMBean;
import weblogic.management.descriptors.webappext.ResourceDescriptionMBean;

public final class ResourceDescription extends BaseServletDescriptor implements ResourceDescriptionMBean {
   private static final long serialVersionUID = 1215314657449753272L;
   private static String refErr = "Can't define resource-description in weblogic.xml because web.xml has no matching resource-ref";
   private ResourceRefMBean ref;
   private String jndiName;

   public ResourceDescription() {
   }

   public ResourceDescription(ResourceDescriptionMBean var1) {
      this.jndiName = var1.getJndiName();
      this.ref = var1.getResourceReference();
   }

   public void setResourceReferenceName(String var1) {
      String var2 = this.getResourceReferenceName();
      this.ref.setRefName(var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("resourceReferenceName", var2, var1);
      }

   }

   public String getResourceReferenceName() {
      return this.ref == null ? null : this.ref.getRefName();
   }

   public void setResourceReference(ResourceRefMBean var1) {
      ResourceRefMBean var2 = this.ref;
      this.ref = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("resourceReference", var2, var1);
      }

   }

   public ResourceRefMBean getResourceReference() {
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
      return this.jndiName;
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<resource-description>\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<res-ref-name>" + this.getResourceReferenceName() + "</res-ref-name>\n";
      var2 = var2 + this.indentStr(var1) + "<jndi-name>" + this.getJndiName() + "</jndi-name>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</resource-description>\n";
      return var2;
   }
}
