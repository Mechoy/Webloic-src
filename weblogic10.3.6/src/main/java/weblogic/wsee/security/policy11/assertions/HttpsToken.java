package weblogic.wsee.security.policy11.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;

public class HttpsToken extends weblogic.wsee.security.policy12.assertions.HttpsToken {
   private static final long serialVersionUID = 1335615639315668760L;
   public static final QName REQ_CLIENT_CERT = new QName("RequireClientCertificate");
   boolean isClientCertRequired = false;

   protected void init(Element var1) throws PolicyException {
      super.init(var1);
      Boolean var2 = DOMUtils.getAttributeValueAsBoolean(var1, REQ_CLIENT_CERT);
      if (var2 != null) {
         this.isClientCertRequired = var2;
      }

   }

   public boolean isClientCertRequired() {
      return this.isClientCertRequired;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = super.serialize(var1);
      DOMUtils.addAttribute(var2, REQ_CLIENT_CERT, String.valueOf(this.isClientCertRequired));
      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.isClientCertRequired = var1.readBoolean();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeBoolean(this.isClientCertRequired);
   }
}
