package weblogic.wsee.addressing.policy.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class WSAUsingAddressingAssertion extends PolicyAssertion {
   public QName name;
   private boolean isRequired;

   public WSAUsingAddressingAssertion(QName var1, boolean var2) {
      this.name = WSAddressingConstants.WSAW_QNAME_10;
      this.isRequired = false;
      this.name = var1;
      this.isRequired = var2;
   }

   public WSAUsingAddressingAssertion() {
      this.name = WSAddressingConstants.WSAW_QNAME_10;
      this.isRequired = false;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public QName getName() {
      return this.name;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(this.getName(), var1, this.getName().getPrefix());
      if (this.isRequired()) {
         DOMUtils.addAttribute(var2, WSAddressingConstants.WSAW_ATT_QNAME, "true");
      }

      return var2;
   }

   public boolean isRequired() {
      return this.isRequired;
   }

   public void setRequired(boolean var1) {
      this.isRequired = var1;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      String var2 = var1.readUTF();
      if (WSAddressingConstants.WSAW_QNAME.getNamespaceURI().equals(var2)) {
         this.name = WSAddressingConstants.WSAW_QNAME;
      } else {
         this.name = WSAddressingConstants.WSAW_QNAME_10;
      }

      this.isRequired = var1.readBoolean();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.name.getNamespaceURI());
      var1.writeBoolean(this.isRequired);
   }
}
