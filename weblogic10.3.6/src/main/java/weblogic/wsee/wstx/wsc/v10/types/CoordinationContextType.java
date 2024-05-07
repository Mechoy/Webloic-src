package weblogic.wsee.wstx.wsc.v10.types;

import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "CoordinationContextType",
   propOrder = {"identifier", "expires", "coordinationType", "registrationService"}
)
@XmlSeeAlso({CreateCoordinationContextType.CurrentContext.class, CoordinationContext.class})
public class CoordinationContextType {
   @XmlElement(
      name = "Identifier",
      required = true
   )
   protected Identifier identifier;
   @XmlElement(
      name = "Expires"
   )
   protected Expires expires;
   @XmlElement(
      name = "CoordinationType",
      required = true
   )
   @XmlSchemaType(
      name = "anyURI"
   )
   protected String coordinationType;
   @XmlElement(
      name = "RegistrationService",
      required = true
   )
   protected MemberSubmissionEndpointReference registrationService;
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public Identifier getIdentifier() {
      return this.identifier;
   }

   public void setIdentifier(Identifier var1) {
      this.identifier = var1;
   }

   public Expires getExpires() {
      return this.expires;
   }

   public void setExpires(Expires var1) {
      this.expires = var1;
   }

   public String getCoordinationType() {
      return this.coordinationType;
   }

   public void setCoordinationType(String var1) {
      this.coordinationType = var1;
   }

   public MemberSubmissionEndpointReference getRegistrationService() {
      return this.registrationService;
   }

   public void setRegistrationService(MemberSubmissionEndpointReference var1) {
      this.registrationService = var1;
   }

   public Map<QName, String> getOtherAttributes() {
      return this.otherAttributes;
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(
      name = "",
      propOrder = {"value"}
   )
   public static class Identifier {
      @XmlValue
      @XmlSchemaType(
         name = "anyURI"
      )
      protected String value;
      @XmlAnyAttribute
      private Map<QName, String> otherAttributes = new HashMap();

      public String getValue() {
         return this.value;
      }

      public void setValue(String var1) {
         this.value = var1;
      }

      public Map<QName, String> getOtherAttributes() {
         return this.otherAttributes;
      }
   }
}
