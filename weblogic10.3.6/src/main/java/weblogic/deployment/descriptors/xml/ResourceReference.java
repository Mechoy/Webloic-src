package weblogic.deployment.descriptors.xml;

import org.w3c.dom.Element;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ResourceReference extends weblogic.deployment.descriptors.ResourceReference {
   public ResourceReference() {
      super("", "", "", "", "");
   }

   public ResourceReference(Element var1) throws DOMProcessingException {
      super(DOMUtils.getOptionalValueByTagName(var1, "description"), DOMUtils.getValueByTagName(var1, "res-ref-name"), DOMUtils.getValueByTagName(var1, "res-type"), DOMUtils.getValueByTagName(var1, "res-auth"), "");
   }
}
