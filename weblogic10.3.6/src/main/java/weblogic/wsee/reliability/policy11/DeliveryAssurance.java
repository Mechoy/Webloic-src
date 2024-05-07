package weblogic.wsee.reliability.policy11;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.xml.dom.DOMProcessingException;

public class DeliveryAssurance implements Externalizable {
   public static final long serialVersionUID = 1L;
   public static final QName NAME = new QName(WsrmConstants.RMVersion.latest().getPolicyNamespaceUri(), "DeliveryAssurance");
   private AtLeastOnce _atLeastOnce;
   private AtMostOnce _atMostOnce;
   private ExactlyOnce _exactlyOnce;
   private InOrder _inOrder;

   public AtLeastOnce getAtLeastOnce() {
      return this._atLeastOnce;
   }

   public void setAtLeastOnce(AtLeastOnce var1) {
      this._atLeastOnce = var1;
   }

   public AtMostOnce getAtMostOnce() {
      return this._atMostOnce;
   }

   public void setAtMostOnce(AtMostOnce var1) {
      this._atMostOnce = var1;
   }

   public ExactlyOnce getExactlyOnce() {
      return this._exactlyOnce;
   }

   public void setExactlyOnce(ExactlyOnce var1) {
      this._exactlyOnce = var1;
   }

   public InOrder getInOrder() {
      return this._inOrder;
   }

   public void setInOrder(InOrder var1) {
      this._inOrder = var1;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(NAME, var1);
      if (this._atLeastOnce != null) {
         var2.appendChild(this._atLeastOnce.serialize(var1));
      }

      if (this._atMostOnce != null) {
         var2.appendChild(this._atMostOnce.serialize(var1));
      }

      if (this._exactlyOnce != null) {
         var2.appendChild(this._exactlyOnce.serialize(var1));
      }

      if (this._inOrder != null) {
         var2.appendChild(this._inOrder.serialize(var1));
      }

      return var2;
   }

   protected void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, NAME.getLocalPart(), NAME.getNamespaceURI());
      if (PolicyHelper.hasWsp15NamespaceUri(var1)) {
         var3 = var2.addChild(var3, "Policy", "http://www.w3.org/ns/ws-policy");
      } else {
         var3 = var2.addChild(var3, "Policy", "http://schemas.xmlsoap.org/ws/2004/09/policy");
      }

      if (this._atLeastOnce != null) {
         this._atLeastOnce.write(var3, var2);
      }

      if (this._atMostOnce != null) {
         this._atMostOnce.write(var3, var2);
      }

      if (this._exactlyOnce != null) {
         this._exactlyOnce.write(var3, var2);
      }

      if (this._inOrder != null) {
         this._inOrder.write(var3, var2);
      }

   }

   protected void read(Element var1) throws DOMProcessingException, PolicyException {
      Element var2 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2004/09/policy", "Policy");
      if (var2 != null) {
         this.read(var2);
      } else {
         var2 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://www.w3.org/ns/ws-policy", "Policy");
         if (var2 != null) {
            this.read(var2);
         } else {
            Element var3 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, AtLeastOnce.NAME.getNamespaceURI(), AtLeastOnce.NAME.getLocalPart());
            if (var3 != null) {
               this._atLeastOnce = new AtLeastOnce();
               this._atLeastOnce.read(var3);
            }

            Element var4 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, AtMostOnce.NAME.getNamespaceURI(), AtMostOnce.NAME.getLocalPart());
            if (var4 != null) {
               this._atMostOnce = new AtMostOnce();
               this._atMostOnce.read(var4);
            }

            Element var5 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, ExactlyOnce.NAME.getNamespaceURI(), ExactlyOnce.NAME.getLocalPart());
            if (var5 != null) {
               this._exactlyOnce = new ExactlyOnce();
               this._exactlyOnce.read(var5);
            }

            Element var6 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, InOrder.NAME.getNamespaceURI(), InOrder.NAME.getLocalPart());
            if (var6 != null) {
               this._inOrder = new InOrder();
               this._inOrder.read(var6);
            }

            this.validate();
         }
      }
   }

   public void validate() throws PolicyException {
      boolean var1 = false;
      if (this._atLeastOnce != null && (this._atMostOnce != null || this._exactlyOnce != null)) {
         var1 = true;
      }

      if (this._atMostOnce != null && (this._atLeastOnce != null || this._exactlyOnce != null)) {
         var1 = true;
      }

      if (this._exactlyOnce != null && (this._atMostOnce != null || this._atLeastOnce != null)) {
         var1 = true;
      }

      if (var1) {
         throw new PolicyException("Only one of AtLeastOnce, AtMostOnce, and ExactlyOnce can be specified under DeliveryAssurance");
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DeliveryAssurance)) {
         return false;
      } else {
         DeliveryAssurance var2 = (DeliveryAssurance)var1;
         boolean var3 = this.compareObjects(this._atLeastOnce, var2._atLeastOnce);
         var3 &= this.compareObjects(this._atMostOnce, var2._atMostOnce);
         var3 &= this.compareObjects(this._exactlyOnce, var2._exactlyOnce);
         var3 &= this.compareObjects(this._inOrder, var2._inOrder);
         return var3;
      }
   }

   private boolean compareObjects(Object var1, Object var2) {
      if (var1 == null && var2 != null) {
         return false;
      } else if (var2 == null && var1 != null) {
         return false;
      } else if (var1 == null && var2 == null) {
         return true;
      } else {
         return var1 != null ? var1.equals(var2) : false;
      }
   }

   public int hashCode() {
      int var1 = NAME.hashCode();
      if (this._atLeastOnce != null) {
         var1 += this._atLeastOnce.hashCode();
      }

      if (this._atMostOnce != null) {
         var1 += this._atMostOnce.hashCode();
      }

      if (this._exactlyOnce != null) {
         var1 += this._exactlyOnce.hashCode();
      }

      if (this._inOrder != null) {
         var1 += this._inOrder.hashCode();
      }

      return var1;
   }

   public QName getName() {
      return NAME;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      if (var2 == 1) {
         this._atLeastOnce = new AtLeastOnce();
         this._atLeastOnce.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this._atMostOnce = new AtMostOnce();
         this._atMostOnce.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this._exactlyOnce = new ExactlyOnce();
         this._exactlyOnce.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this._inOrder = new InOrder();
         this._inOrder.readExternal(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (this._atLeastOnce != null) {
         var1.writeInt(1);
         this._atLeastOnce.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this._atMostOnce != null) {
         var1.writeInt(1);
         this._atMostOnce.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this._exactlyOnce != null) {
         var1.writeInt(1);
         this._exactlyOnce.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this._inOrder != null) {
         var1.writeInt(1);
         this._inOrder.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

   }
}
