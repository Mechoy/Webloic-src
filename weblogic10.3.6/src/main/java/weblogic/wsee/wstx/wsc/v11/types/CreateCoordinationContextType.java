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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "CreateCoordinationContextType",
   propOrder = {"expires", "currentContext", "coordinationType", "any"}
)
public class CreateCoordinationContextType {
   @XmlElement(
      name = "Expires"
   )
   protected Expires expires;
   @XmlElement(
      name = "CurrentContext"
   )
   protected CurrentContext currentContext;
   @XmlElement(
      name = "CoordinationType",
      required = true
   )
   @XmlSchemaType(
      name = "anyURI"
   )
   protected String coordinationType;
   @XmlAnyElement(
      lax = true
   )
   protected List<Object> any;
   @XmlAnyAttribute
   private Map<QName, String> otherAttributes = new HashMap();

   public Expires getExpires() {
      return this.expires;
   }

   public void setExpires(Expires var1) {
      this.expires = var1;
   }

   public CurrentContext getCurrentContext() {
      return this.currentContext;
   }

   public void setCurrentContext(CurrentContext var1) {
      this.currentContext = var1;
   }

   public String getCoordinationType() {
      return this.coordinationType;
   }

   public void setCoordinationType(String var1) {
      this.coordinationType = var1;
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

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(
      name = "",
      propOrder = {"any"}
   )
   public static class CurrentContext extends CoordinationContextType {
      @XmlAnyElement(
         lax = true
      )
      protected List<Object> any;

      public List<Object> getAny() {
         if (this.any == null) {
            this.any = new ArrayList();
         }

         return this.any;
      }
   }
}
