package weblogic.deployment.descriptors.xml;

import org.w3c.dom.Element;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class EnvironmentEntry extends weblogic.deployment.descriptors.EnvironmentEntry {
   public EnvironmentEntry() {
   }

   public EnvironmentEntry(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setName(DOMUtils.getValueByTagName(var1, "env-entry-name"));
      this.setValue(DOMUtils.getOptionalValueByTagName(var1, "env-entry-value"));
      this.setType(DOMUtils.getValueByTagName(var1, "env-entry-type"));
   }
}
