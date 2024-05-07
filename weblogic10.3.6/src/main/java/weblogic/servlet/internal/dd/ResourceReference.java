package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ResourceRefMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ResourceReference extends BaseServletDescriptor implements ResourceRefMBean {
   private static final long serialVersionUID = -4930785984109869909L;
   String refName;
   String refType;
   String auth;
   String sharingScope;
   String description;

   public ResourceReference() {
   }

   public ResourceReference(String var1, String var2, String var3, String var4) {
      this.setRefName(var2);
      this.setRefType(var3);
      this.setAuth(var4);
      this.setDescription(var1);
   }

   public ResourceReference(ResourceRefMBean var1) {
      this.setDescription(var1.getDescription());
      this.setRefName(var1.getRefName());
      this.setRefType(var1.getRefType());
      this.setAuth(var1.getAuth());
      this.setSharingScope(var1.getSharingScope());
   }

   public ResourceReference(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setRefName(DOMUtils.getValueByTagName(var1, "res-ref-name"));
      this.setRefType(DOMUtils.getValueByTagName(var1, "res-type"));
      this.setAuth(DOMUtils.getValueByTagName(var1, "res-auth"));
      this.setSharingScope(DOMUtils.getOptionalValueByTagName(var1, "res-sharing-scope"));
   }

   public String getRefName() {
      return this.refName;
   }

   public void setRefName(String var1) {
      String var2 = this.refName;
      this.refName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("refName", var2, var1);
      }

   }

   public String getRefType() {
      return this.refType;
   }

   public void setRefType(String var1) {
      String var2 = this.refType;
      this.refType = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("refType", var2, var1);
      }

   }

   public String getAuth() {
      return this.auth;
   }

   public void setAuth(String var1) {
      String var2 = this.auth;
      this.auth = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("auth", var2, var1);
      }

   }

   public String getSharingScope() {
      return this.sharingScope;
   }

   public void setSharingScope(String var1) {
      String var2 = this.sharingScope;
      this.sharingScope = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("sharingScope", var2, var1);
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

   public String toString() {
      return this.getRefName();
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<resource-ref>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<res-ref-name>" + this.getRefName() + "</res-ref-name>\n";

      try {
         var2 = var2 + this.indentStr(var1) + "<res-type>" + this.getRefType() + "</res-type>\n";
      } catch (Exception var5) {
      }

      var2 = var2 + this.indentStr(var1) + "<res-auth>" + this.getAuth() + "</res-auth>\n";
      if (this.sharingScope != null) {
         var2 = var2 + this.indentStr(var1) + "<res-sharing-scope>" + this.getSharingScope() + "</res-sharing-scope>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</resource-ref>\n";
      return var2;
   }
}
