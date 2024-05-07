package weblogic.wsee.wstx.wsc.v11.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "CreateCoordinationContextResponseType",
   propOrder = {"coordinationContext", "any"}
)
public class CreateCoordinationContextResponseType {
   @XmlElement(
      name = "CoordinationContext",
      required = true
   )
   protected CoordinationContext coordinationContext;
   @XmlAnyElement(
      lax = true
   )
   protected List<Object> any;
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public CoordinationContext getCoordinationContext() {
      return this.coordinationContext;
   }

   public void setCoordinationContext(CoordinationContext var1) {
      this.coordinationContext = var1;
   }

   public List<Object> getAny() {
      if (this.any == null) {
         this.any = new ArrayList();
      }

      return this.any;
   }

   public Map<QName, String> getOtherAttributes() {
      return this.otherAttributes;
   }
}
