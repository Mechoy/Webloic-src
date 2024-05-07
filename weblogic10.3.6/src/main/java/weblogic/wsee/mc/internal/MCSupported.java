package weblogic.wsee.mc.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public class MCSupported extends PolicyAssertion {
   private static final long serialVersionUID = -4920168888986897631L;
   public static final QName MC_QNAME;
   Boolean optional = null;

   public boolean getOptional() {
      return null == this.optional ? true : this.optional;
   }

   public boolean isOptional() {
      return this.getOptional();
   }

   public void setOptional(boolean var1) {
      this.optional = new Boolean(var1);
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(this.getName(), var1, McConstants.McVersion.MC_11.getPrefix());
      if (this.optional != null) {
         PolicyHelper.addOptionalAttribute(var2, this.optional, this.getPolicyNamespaceUri());
      }

      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      if (var1.readBoolean()) {
         this.optional = new Boolean(var1.readBoolean());
      } else {
         this.optional = null;
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this.optional != null) {
         var1.writeBoolean(true);
         var1.writeBoolean(this.optional);
      } else {
         var1.writeBoolean(false);
      }

   }

   public QName getName() {
      return MC_QNAME;
   }

   static {
      MC_QNAME = new QName(McConstants.McVersion.MC_11.getNamespaceUri(), McConstants.Element.MC_SUPPORTED.getElementName(), McConstants.McVersion.MC_11.getPrefix());
   }
}
