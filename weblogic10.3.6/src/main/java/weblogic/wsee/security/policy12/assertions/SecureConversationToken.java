package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;

public class SecureConversationToken extends Token {
   public static final String SECURE_CONVERSATION_TOKEN = "SecureConversationToken";

   protected void init(Element var1) throws PolicyException {
      super.init(var1);
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = super.serialize(var1);
      return this.issuer != null ? this.issuer.serialize(var1) : var2;
   }

   public QName getName() {
      return new QName(this.getNamespace(), "SecureConversationToken", "sp");
   }

   public boolean isSC200502SecurityContextToken() {
      return (SC200502SecurityContextToken)this.getNestedAssertion(SC200502SecurityContextToken.class) != null;
   }

   public boolean isSC13SecurityContextToken() {
      return (SC200502SecurityContextToken)this.getNestedAssertion(SC200502SecurityContextToken.class) == null;
   }

   public RequireExternalUriReference getRequireExternalUriReference() {
      return (RequireExternalUriReference)this.getNestedAssertion(RequireExternalUriReference.class);
   }

   public BootstrapPolicy getBootstrapPolicy() {
      return (BootstrapPolicy)this.getNestedAssertion(BootstrapPolicy.class);
   }

   public NormalizedExpression getNormalizedBootstrapPolicy() {
      BootstrapPolicy var1 = this.getBootstrapPolicy();
      return var1 != null ? var1.getNestedPolicy() : null;
   }
}
