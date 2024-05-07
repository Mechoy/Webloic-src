package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class IssuedToken extends Token {
   private static final long serialVersionUID = 7431269518604532400L;
   public static final String ISSUED_TOKEN = "IssuedToken";
   private RequestSecurityTokenTemplate template;
   private RequireExternalReference requireExternalReference = null;

   public QName getName() {
      return new QName(this.getNamespace(), "IssuedToken", "sp");
   }

   protected void init(Element var1) throws PolicyException {
      super.init(var1);

      try {
         Element var2 = DOMUtils.getOptionalElementByTagNameNS(var1, this.getNamespace(), "RequestSecurityTokenTemplate");
         if (var2 != null) {
            this.template = new RequestSecurityTokenTemplate();
            this.template.initialize(var2);
         }

         Element var3 = DOMUtils.getOptionalElementByTagNameNS(var1, this.getNamespace(), "RequireExternalReference");
         if (var3 != null) {
            this.requireExternalReference = new RequireExternalReference();
         }

      } catch (DOMProcessingException var4) {
         throw new PolicyException(var4);
      }
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = super.serialize(var1);
      if (this.template != null) {
         this.template.serializeAssertion(var1, var2);
      }

      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      if (var1.readBoolean()) {
         this.template = new RequestSecurityTokenTemplate();
         this.template.readExternal(var1);
      }

      if (var1.readBoolean()) {
         this.requireExternalReference = new RequireExternalReference();
         this.requireExternalReference.readExternal(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this.template != null) {
         var1.writeBoolean(true);
         this.template.writeExternal(var1);
      } else {
         var1.writeBoolean(false);
      }

      if (this.requireExternalReference != null) {
         var1.writeBoolean(true);
         this.requireExternalReference.writeExternal(var1);
      } else {
         var1.writeBoolean(false);
      }

   }

   public RequireExternalReference getRequireExternalReference() {
      return this.requireExternalReference != null ? this.requireExternalReference : (RequireExternalReference)this.getNestedAssertion(RequireExternalReference.class);
   }

   public RequestSecurityTokenTemplate getRequestSecurityTokenTemplate() {
      return this.template;
   }
}
