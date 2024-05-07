package weblogic.wsee.addressing;

import java.io.Serializable;
import java.util.Iterator;
import org.w3c.dom.Element;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class EndpointReference implements Serializable {
   static final long serialVersionUID = 2515772824816511305L;
   private String address;
   private String portType;
   private String serviceName;
   private String portName;
   private String namespaceURI = "http://www.w3.org/2005/08/addressing";
   private PolicyStatement policy;
   private MsgHeaders refProperties = new FreeStandingMsgHeaders();
   private MsgHeaders refParameters = new FreeStandingMsgHeaders();
   private MetadataHeaders metadata = new MetadataHeaders();
   private transient Element endptElement;

   public EndpointReference() {
   }

   public EndpointReference(String var1) {
      this.address = var1;
   }

   public EndpointReference(String var1, String var2) {
      this.address = var1;
      this.portType = var2;
   }

   public String getPortType() {
      return this.portType;
   }

   public void setPortType(String var1) {
      this.portType = var1;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public String getPortName() {
      return this.portName;
   }

   public void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public void setServiceName(String var1, String var2) {
      this.serviceName = var1;
      this.portName = var2;
   }

   public String getAddress() {
      return this.address;
   }

   public void setAddress(String var1) {
      this.address = var1;
   }

   public void setNamespaceURI(String var1) {
      this.namespaceURI = var1;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public void setPolicy(PolicyStatement var1) {
      this.policy = var1;
   }

   public PolicyStatement getPolicy() {
      return this.policy;
   }

   public MsgHeaders getReferenceProperties() {
      return this.refProperties;
   }

   public void setReferenceProperties(FreeStandingMsgHeaders var1) {
      this.refProperties = var1;
   }

   public MsgHeaders getReferenceParameters() {
      return this.refParameters;
   }

   public void setReferenceParameters(FreeStandingMsgHeaders var1) {
      this.refParameters = var1;
   }

   public MetadataHeaders getMetadata() {
      return this.metadata;
   }

   public void setMetadata(MetadataHeaders var1) {
      this.metadata = var1;
   }

   public Element getEndptElement() {
      return this.endptElement;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      var1.writeField("namespace", this.namespaceURI);
      var1.writeField("address", this.address);
      var1.writeField("portType", this.portType);
      var1.writeField("portName", this.portName);
      var1.writeField("serviceName", this.serviceName);
      var1.writeField("refProperties", this.refProperties);
      var1.writeField("refParameters", this.refParameters);
      var1.writeField("metadata", this.metadata);
      var1.writeField("policy", this.policy);
      return var1.toString();
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.address = DOMUtils.getOptionalValueByTagNameNS(var1, this.namespaceURI, "Address");
         if (this.address == null || this.address.length() == 0) {
            this.address = DOMUtils.getOptionalValueByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2004/08/addressing", "Address");
            this.namespaceURI = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
         }

         if (this.address != null && this.address.length() != 0) {
            Element var2 = DOMUtils.getOptionalElementByTagNameNS(var1, this.namespaceURI, "ServiceName");
            if (var2 != null) {
               this.serviceName = DOMUtils.getTextData(var2);
               this.portName = var2.getAttribute("PortName");
            }

            this.portType = DOMUtils.getOptionalValueByTagNameNS(var1, this.namespaceURI, "PortType");
            Element var3 = DOMUtils.getOptionalElementByTagNameNS(var1, this.namespaceURI, "ReferenceProperties");
            if (var3 != null) {
               SoapMsgHeaders var4 = new SoapMsgHeaders(var3);
               Iterator var5 = var4.listHeaders();

               while(var5.hasNext()) {
                  MsgHeader var6 = (MsgHeader)var5.next();
                  this.refProperties.addHeader(var6);
               }
            }

            boolean var12 = this.isWSA10();
            Element var13 = DOMUtils.getOptionalElementByTagNameNS(var1, this.namespaceURI, "ReferenceParameters");
            if (var13 != null) {
               SoapMsgHeaders var14 = new SoapMsgHeaders(var13);
               Iterator var7 = var14.listHeaders();

               while(var7.hasNext()) {
                  MsgHeader var8 = (MsgHeader)var7.next();
                  var8.setRefParam(var12);
                  this.refParameters.addHeader(var8);
               }
            }

            Element var15;
            if (var12) {
               var15 = DOMUtils.getOptionalElementByTagNameNS(var1, this.namespaceURI, "Metadata");
               if (var15 != null) {
                  SoapMsgHeaders var16 = new SoapMsgHeaders(var15);
                  Iterator var17 = var16.listHeaders();

                  while(var17.hasNext()) {
                     MsgHeader var9 = (MsgHeader)var17.next();
                     this.metadata.addHeader(var9);
                  }
               }
            }

            var15 = DOMUtils.getOptionalElementByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2002/12/policy", "Policy");
            if (var15 != null) {
               this.policy = PolicyFinder.readPolicyStatementFromNode(var15);
            }

            this.endptElement = var1;
         } else {
            throw new MsgHeaderException("Empty address is not allowed.");
         }
      } catch (DOMProcessingException var10) {
         throw new MsgHeaderException("Could not parse endpoint reference", var10);
      } catch (PolicyException var11) {
         throw new MsgHeaderException("Could not read policy", var11);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      if (var1.getPrefix() == null || !var1.getPrefix().equals("wsa")) {
         DOMUtils.addNamespaceDeclaration(var1, "wsa", this.namespaceURI);
      }

      DOMUtils.addValueNS(var1, this.namespaceURI, "wsa:Address", this.address);
      Element var2;
      if (this.serviceName != null) {
         var2 = DOMUtils.addValueNS(var1, this.namespaceURI, "wsa:ServiceName", this.serviceName);
         if (this.portName != null) {
            var2.setAttribute("PortName", this.portName);
         }
      }

      if (this.portType != null) {
         DOMUtils.addValueNS(var1, this.namespaceURI, "wsa:PortType", this.portType);
      }

      Iterator var3;
      MsgHeader var4;
      if (!this.refProperties.isEmpty()) {
         var2 = var1.getOwnerDocument().createElementNS(this.namespaceURI, "wsa:ReferenceProperties");
         var3 = this.refProperties.listHeaders();

         while(var3.hasNext()) {
            var4 = (MsgHeader)var3.next();
            var4.writeToParent(var2);
         }

         var1.appendChild(var2);
      }

      if (!this.refParameters.isEmpty()) {
         var2 = var1.getOwnerDocument().createElementNS(this.namespaceURI, "wsa:ReferenceParameters");
         var3 = this.refParameters.listHeaders();

         while(var3.hasNext()) {
            var4 = (MsgHeader)var3.next();
            var4.writeToParent(var2);
         }

         var1.appendChild(var2);
      }

      if (this.metadata != null && !this.metadata.isEmpty() && this.isWSA10()) {
         var2 = var1.getOwnerDocument().createElementNS(this.namespaceURI, "wsa:Metadata");
         var3 = this.metadata.listHeaders();

         while(var3.hasNext()) {
            var4 = (MsgHeader)var3.next();
            var4.writeToParent(var2);
         }

         var1.appendChild(var2);
      }

      if (this.policy != null) {
         try {
            var2 = this.policy.toXML();
            var1.appendChild(var2);
         } catch (PolicyException var5) {
            throw new MsgHeaderException("Unable to read policy", var5);
         }
      }

   }

   private boolean isWSA10() {
      return "http://www.w3.org/2005/08/addressing".equals(this.namespaceURI);
   }
}
