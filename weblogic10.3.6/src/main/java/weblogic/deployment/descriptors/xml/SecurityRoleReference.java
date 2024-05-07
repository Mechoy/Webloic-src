package weblogic.deployment.descriptors.xml;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SecurityRoleReference extends weblogic.deployment.descriptors.SecurityRoleReference {
   String link;

   public SecurityRoleReference() {
      super("", "", (weblogic.deployment.descriptors.RoleDescriptor)null);
      this.setReferencedRole(new weblogic.deployment.descriptors.RoleDescriptor());
   }

   public SecurityRoleReference(Element var1) throws DOMProcessingException {
      super(DOMUtils.getOptionalValueByTagName(var1, "description"), DOMUtils.getValueByTagName(var1, "role-name"), (weblogic.deployment.descriptors.RoleDescriptor)null);
      this.link = DOMUtils.getValueByTagName(var1, "role-link");
   }

   public void resolveLinks(List var1) {
      Iterator var2 = var1.iterator();

      weblogic.deployment.descriptors.RoleDescriptor var3;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (weblogic.deployment.descriptors.RoleDescriptor)var2.next();
      } while(this.link == null || !this.link.equals(var3.getName()));

      this.setReferencedRole(var3);
   }

   public void setRoleLink(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.link = var1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getRoleLink() {
      return this.link;
   }
}
