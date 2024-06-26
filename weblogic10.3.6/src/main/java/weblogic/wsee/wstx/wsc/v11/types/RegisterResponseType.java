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
import javax.xml.ws.wsaddressing.W3CEndpointReference;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "RegisterResponseType",
   propOrder = {"coordinatorProtocolService", "any"}
)
public class RegisterResponseType {
   @XmlElement(
      name = "CoordinatorProtocolService",
      required = true
   )
   protected W3CEndpointReference coordinatorProtocolService;
   @XmlAnyElement(
      lax = true
   )
   protected List<Object> any;
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public W3CEndpointReference getCoordinatorProtocolService() {
      return this.coordinatorProtocolService;
   }

   public void setCoordinatorProtocolService(W3CEndpointReference var1) {
      this.coordinatorProtocolService = var1;
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
