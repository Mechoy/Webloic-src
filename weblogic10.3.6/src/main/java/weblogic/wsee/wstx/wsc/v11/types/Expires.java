package weblogic.wsee.wstx.wsc.v11.types;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "",
   propOrder = {"value"}
)
@XmlRootElement(
   name = "Expires"
)
public class Expires {
   @XmlValue
   @XmlSchemaType(
      name = "unsignedInt"
   )
   protected long value;
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public long getValue() {
      return this.value;
   }

   public void setValue(long var1) {
      this.value = var1;
   }

   public Map<QName, String> getOtherAttributes() {
      return this.otherAttributes;
   }
}
