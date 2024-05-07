package weblogic.wsee.security.wst.binding;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.xml.dom.marshal.MarshalException;

public class AppliesTo extends TrustDOMStructure {
   private static final long serialVersionUID = -1666518078887214439L;
   public static final String NAME = "AppliesTo";
   private EndpointReference endpointReference;
   private Element ele;

   public AppliesTo() {
      this.initNamespaceAndPrefix((String)null);
   }

   public AppliesTo(String var1) {
      this.initNamespaceAndPrefix(var1);
   }

   private void initNamespaceAndPrefix(String var1) {
      if (var1 == null) {
         this.namespaceUri = "http://schemas.xmlsoap.org/ws/2004/09/policy";
         this.prefix = "wsp";
      } else {
         this.namespaceUri = var1;
         if ("http://www.w3.org/ns/ws-policy".equals(this.namespaceUri)) {
            this.prefix = "wsp15";
         } else {
            this.prefix = "wsp";
         }
      }

   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.endpointReference != null) {
         this.endpointReference.marshal(var1, (Node)null, var2);
      } else {
         String var3 = createNamespacePrefix(var2, this.ele.getNamespaceURI(), this.ele.getPrefix());
         String var4 = this.ele.getLocalName();
         if (var4 == null) {
            throw new MarshalException("currently only qualified element is supported as the appliedto element");
         }

         Element var5 = createElement(var1, new QName(this.ele.getNamespaceURI(), var4), var3);
         var5.setTextContent(this.ele.getTextContent());
         var1.appendChild(var5);
      }

   }

   public String getEndpointReference() {
      return this.endpointReference == null ? null : this.endpointReference.getEndpointAddress();
   }

   public Element getElement() {
      return this.ele;
   }

   public void setElement(Element var1) {
      this.ele = var1;
   }

   public void setEndpointReference(String var1, String var2) {
      this.endpointReference = new EndpointReference(var1);
      this.endpointReference.setEndpointAddress(var2);
   }

   public String getName() {
      return "AppliesTo";
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getElementByTagName(var1, "EndpointReference", true);
      if (var2 != null) {
         this.endpointReference = new EndpointReference(var2.getNamespaceURI());
         this.endpointReference.unmarshal(var2);
      } else {
         this.ele = getFirstElement(var1);
      }

   }

   private static final class EndpointReference extends TrustDOMStructure {
      public static final String NAME = "EndpointReference";
      private weblogic.wsee.addressing.EndpointReference _endpointReference;

      public EndpointReference() {
         this.initNamespaceAndPrefix((String)null);
      }

      public EndpointReference(String var1) {
         this.initNamespaceAndPrefix(var1);
      }

      String getEndpointAddress() {
         return this._endpointReference.getAddress();
      }

      void setEndpointAddress(String var1) {
         this._endpointReference = new weblogic.wsee.addressing.EndpointReference();
         this._endpointReference.setNamespaceURI(this.namespaceUri);
         this._endpointReference.setAddress(var1);
      }

      private void initNamespaceAndPrefix(String var1) {
         if (var1 == null) {
            this.namespaceUri = "http://www.w3.org/2005/08/addressing";
         } else {
            this.namespaceUri = var1;
         }

         this.prefix = "wsa";
      }

      public void marshalContents(Element var1, Map var2) {
         assert this._endpointReference != null;

         this._endpointReference.write(var1);
      }

      public String getName() {
         return "EndpointReference";
      }

      public void unmarshalContents(Element var1) throws MarshalException {
         try {
            this._endpointReference = new weblogic.wsee.addressing.EndpointReference();
            this._endpointReference.read(var1);
            this.namespaceUri = this._endpointReference.getNamespaceURI();
         } catch (MsgHeaderException var3) {
            throw new MarshalException(var3);
         }
      }
   }
}
