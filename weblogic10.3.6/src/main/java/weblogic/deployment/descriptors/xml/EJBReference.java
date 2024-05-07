package weblogic.deployment.descriptors.xml;

import org.w3c.dom.Element;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class EJBReference extends weblogic.deployment.descriptors.EJBReference {
   public EJBReference() {
      super((String)null, "", "", "", "", (String)null);
   }

   public EJBReference(Element var1) throws DOMProcessingException {
      super(DOMUtils.getOptionalValueByTagName(var1, "description"), DOMUtils.getValueByTagName(var1, "ejb-ref-name"), DOMUtils.getValueByTagName(var1, "ejb-ref-type"), DOMUtils.getValueByTagName(var1, "home"), DOMUtils.getValueByTagName(var1, "remote"), DOMUtils.getOptionalValueByTagName(var1, "ejb-link"));
   }
}
