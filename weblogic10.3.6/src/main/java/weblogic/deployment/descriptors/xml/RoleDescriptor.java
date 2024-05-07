package weblogic.deployment.descriptors.xml;

import org.w3c.dom.Element;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class RoleDescriptor extends weblogic.deployment.descriptors.RoleDescriptor {
   public RoleDescriptor() {
      super("", "");
   }

   public RoleDescriptor(Element var1) throws DOMProcessingException {
      super(DOMUtils.getOptionalValueByTagName(var1, "description"), DOMUtils.getValueByTagName(var1, "role-name"));

      try {
         this.addSecurityPrincipals(DOMUtils.getValuesByTagName(var1, "principal-name"));
      } catch (DOMProcessingException var3) {
      }

   }
}
