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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "RegisterType",
   propOrder = {"protocolIdentifier", "participantProtocolService", "any"}
)
public class RegisterType {
   @XmlElement(
      name = "ProtocolIdentifier",
      required = true
   )
   @XmlSchemaType(
      name = "anyURI"
   )
   protected String protocolIdentifier;
   @XmlElement(
      name = "ParticipantProtocolService",
      required = true
   )
   protected W3CEndpointReference participantProtocolService;
   @XmlAnyElement(
      lax = true
   )
   protected List<Object> any;
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public String getProtocolIdentifier() {
      return this.protocolIdentifier;
   }

   public void setProtocolIdentifier(String var1) {
      this.protocolIdentifier = var1;
   }

   public W3CEndpointReference getParticipantProtocolService() {
      return this.participantProtocolService;
   }

   public void setParticipantProtocolService(W3CEndpointReference var1) {
      this.participantProtocolService = var1;
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
