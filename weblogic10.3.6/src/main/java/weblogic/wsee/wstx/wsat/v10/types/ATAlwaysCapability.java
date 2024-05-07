package weblogic.wsee.wstx.wsat.v10.types;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = ""
)
@XmlRootElement(
   name = "ATAlwaysCapability"
)
public class ATAlwaysCapability {
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public Map<QName, String> getOtherAttributes() {
      return this.otherAttributes;
   }
}
