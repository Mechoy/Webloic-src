package weblogic.wsee.wstx.wsat.policy;

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

public class ATAssertion extends PolicyAssertion {
   private QName qname;
   private boolean isOptinalSet;

   public void setOptional(boolean var1) {
      super.setOptional(var1);
      this.isOptinalSet = true;
   }

   public Element serialize(Document var1) throws PolicyException {
      QName var2 = this.getName();
      Element var3 = DOMUtils.createElement(var2, var1, var2.getPrefix());
      if (this.optional || this.isOptinalSet) {
         PolicyHelper.addOptionalAttribute(var3, this.optional, this.getPolicyNamespaceUri());
      }

      return var3;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.isOptinalSet = var1.readBoolean();
      this.qname = (QName)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeBoolean(this.isOptinalSet);
      var1.writeObject(this.getName());
   }

   public QName getName() {
      return this.qname == null ? ATPolicyConstants.DEFAULT_AT_QNAME : this.qname;
   }

   public void setName(QName var1) {
      this.qname = var1;
   }
}
