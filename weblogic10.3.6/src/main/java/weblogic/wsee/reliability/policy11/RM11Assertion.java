package weblogic.wsee.reliability.policy11;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.xml.dom.DOMProcessingException;

public class RM11Assertion extends PolicyAssertion implements Externalizable {
   private static final long serialVersionUID = 1L;
   public static final QName NAME = new QName(WsrmConstants.RMVersion.latest().getPolicyNamespaceUri(), "RMAssertion");
   private SequenceSTR _sequenceSTR;
   private SequenceTransportSecurity _sequenceTransportSecurity;
   private DeliveryAssurance _deliveryAssurance;

   public SequenceSTR getSequenceSTR() {
      return this._sequenceSTR;
   }

   public void setSequenceSTR(SequenceSTR var1) {
      this._sequenceSTR = var1;
   }

   public SequenceTransportSecurity getSequenceTransportSecurity() {
      return this._sequenceTransportSecurity;
   }

   public void setSequenceTransportSecurity(SequenceTransportSecurity var1) {
      this._sequenceTransportSecurity = var1;
   }

   public DeliveryAssurance getDeliveryAssurance() {
      return this._deliveryAssurance;
   }

   public void setDeliveryAssurance(DeliveryAssurance var1) {
      this._deliveryAssurance = var1;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(NAME, var1);
      if (this.optional) {
         PolicyHelper.addOptionalAttribute(var2, this.getPolicyNamespaceUri());
      }

      if (this._sequenceSTR != null) {
         var2.appendChild(this._sequenceSTR.serialize(var1));
      }

      if (this._sequenceTransportSecurity != null) {
         var2.appendChild(this._sequenceTransportSecurity.serialize(var1));
      }

      if (this._deliveryAssurance != null) {
         var2.appendChild(this._deliveryAssurance.serialize(var1));
      }

      return var2;
   }

   protected void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, NAME.getLocalPart(), NAME.getNamespaceURI());
      if (this.optional) {
         PolicyHelper.addOptionalAttribute(var3, (String)null);
      }

      if (PolicyHelper.hasWsp15NamespaceUri(var1)) {
         var3 = var2.addChild(var3, "Policy", "http://www.w3.org/ns/ws-policy");
      } else {
         var3 = var2.addChild(var3, "Policy", "http://schemas.xmlsoap.org/ws/2004/09/policy");
      }

      if (this._sequenceSTR != null) {
         this._sequenceSTR.write(var3, var2);
      }

      if (this._sequenceTransportSecurity != null) {
         this._sequenceTransportSecurity.write(var3, var2);
      }

      if (this._deliveryAssurance != null) {
         this._deliveryAssurance.write(var3, var2);
      }

   }

   public void read(Element var1) throws DOMProcessingException, PolicyException {
      Element var2 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2004/09/policy", "Policy");
      if (var2 != null) {
         this.read(var2);
      } else {
         var2 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://www.w3.org/ns/ws-policy", "Policy");
         if (var2 != null) {
            this.read(var2);
         } else {
            Element var3 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, SequenceSTR.NAME.getNamespaceURI(), SequenceSTR.NAME.getLocalPart());
            if (var3 != null) {
               this._sequenceSTR = new SequenceSTR();
               this._sequenceSTR.read(var3);
            }

            Element var4 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, SequenceTransportSecurity.NAME.getNamespaceURI(), SequenceTransportSecurity.NAME.getLocalPart());
            if (var4 != null) {
               this._sequenceTransportSecurity = new SequenceTransportSecurity();
               this._sequenceTransportSecurity.read(var4);
            }

            Element var5 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, DeliveryAssurance.NAME.getNamespaceURI(), DeliveryAssurance.NAME.getLocalPart());
            if (var5 != null) {
               this._deliveryAssurance = new DeliveryAssurance();
               this._deliveryAssurance.read(var5);
            }

            this.validate();
         }
      }
   }

   public void validate() throws PolicyException {
      if (this._sequenceSTR != null && this._sequenceTransportSecurity != null) {
         throw new PolicyException("Only one of SequenceSTR and SequenceTransportSecurity can be specified under RMAssertion");
      } else {
         if (this._deliveryAssurance != null) {
            this._deliveryAssurance.validate();
         }

      }
   }

   private boolean checkEqual(Object var1, Object var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var2 == null ? false : var2.equals(var1);
      }
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof RM11Assertion) {
         RM11Assertion var2 = (RM11Assertion)var1;
         if (!this.checkEqual(this._sequenceSTR, var2._sequenceSTR)) {
            return false;
         } else if (!this.checkEqual(this._sequenceTransportSecurity, var2._sequenceTransportSecurity)) {
            return false;
         } else {
            return this.checkEqual(this._deliveryAssurance, var2._deliveryAssurance);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return NAME.hashCode() + (this._sequenceSTR != null ? this._sequenceSTR.hashCode() : 0) + (this._sequenceTransportSecurity != null ? this._sequenceTransportSecurity.hashCode() : 0) + (this._deliveryAssurance != null ? this._deliveryAssurance.hashCode() : 0);
   }

   public QName getName() {
      return NAME;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      int var2 = var1.readInt();
      if (var2 == 1) {
         this._sequenceSTR = new SequenceSTR();
         this._sequenceSTR.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this._sequenceTransportSecurity = new SequenceTransportSecurity();
         this._sequenceTransportSecurity.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this._deliveryAssurance = new DeliveryAssurance();
         this._deliveryAssurance.readExternal(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this._sequenceSTR != null) {
         var1.writeInt(1);
         this._sequenceSTR.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this._sequenceTransportSecurity != null) {
         var1.writeInt(1);
         this._sequenceTransportSecurity.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this._deliveryAssurance != null) {
         var1.writeInt(1);
         this._deliveryAssurance.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

   }
}
