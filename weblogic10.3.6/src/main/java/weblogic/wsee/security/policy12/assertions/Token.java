package weblogic.wsee.security.policy12.assertions;

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

public abstract class Token extends NestedSecurityPolicy12Assertion {
   private static final long serialVersionUID = -4920883495980897630L;
   public static final String ISSUER = "Issuer";
   TokenInclusion tokenInclusion = null;
   protected Issuer issuer = null;
   protected String isserName = null;

   protected void init(Element var1) throws PolicyException {
      super.init(var1);
      String var2 = DOMUtils.getAttributeValueAsString(var1, new QName(this.getNamespace(), "IncludeToken", "sp"));
      if (var2 != null && var2.length() > 0) {
         this.tokenInclusion = new TokenInclusion(var2);
         this.tokenInclusion.setNamespace(var1.getNamespaceURI());
      }

      try {
         this.issuer = (Issuer)this.getNestedAssertion(Issuer.class);
         if (null == this.issuer) {
            Element var3 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702", "Issuer");
            if (var3 == null) {
               var3 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200512", "Issuer");
            }

            if (null != var3) {
               this.issuer = new Issuer();
               this.issuer.initAssertion(var3);
            }
         }

         IssuerName var6 = (IssuerName)this.getNestedAssertion(IssuerName.class);
         if (var6 != null) {
            this.isserName = var6.getIssuerName();
         }

         String var4 = PolicyHelper.getOptionalPolicyNamespaceUri(var1);
         if (null != var4) {
            this.setPolicyNamespaceUri(var4);
            this.setOptional(true);
         }

      } catch (Exception var5) {
         throw new PolicyException(var5);
      }
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = super.serialize(var1);
      if (this.tokenInclusion != null) {
         this.tokenInclusion.serialize(var2);
      }

      if (this.issuer != null) {
         this.issuer.serializeAssertion(var1, var2);
      }

      return var2;
   }

   public RequireDerivedKeys getRequireDerivedKeys() {
      return (RequireDerivedKeys)this.getNestedAssertion(RequireDerivedKeys.class);
   }

   public RequireImplicitDerivedKeys getRequireImplicitDerivedKeys() {
      return (RequireImplicitDerivedKeys)this.getNestedAssertion(RequireImplicitDerivedKeys.class);
   }

   public RequireExplicitDerivedKeys getRequireExplicitDerivedKeys() {
      return (RequireExplicitDerivedKeys)this.getNestedAssertion(RequireExplicitDerivedKeys.class);
   }

   public RequireInternalReference getRequireInternalReference() {
      return (RequireInternalReference)this.getNestedAssertion(RequireInternalReference.class);
   }

   public RequireExternalReference getRequireExternalReference() {
      return (RequireExternalReference)this.getNestedAssertion(RequireExternalReference.class);
   }

   public String getIssuer() {
      return this.issuer == null ? null : this.issuer.getIssuerUri();
   }

   public String getIssuerAddress() {
      return this.issuer == null ? null : this.issuer.getIssuerUri();
   }

   public String getIssuerName() {
      return this.isserName;
   }

   public TokenInclusion getTokenInclusion() {
      return this.tokenInclusion;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      if (var1.readBoolean()) {
         this.tokenInclusion = new TokenInclusion();
         this.tokenInclusion.readExternal(var1);
      }

      if (var1.readBoolean()) {
         this.issuer = new Issuer();
         this.issuer.readExternal(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this.tokenInclusion != null) {
         var1.writeBoolean(true);
         this.tokenInclusion.writeExternal(var1);
      } else {
         var1.writeBoolean(false);
      }

      if (this.issuer != null) {
         var1.writeBoolean(true);
         this.issuer.writeExternal(var1);
      } else {
         var1.writeBoolean(false);
      }

   }

   public static class TokenInclusion extends PolicyAssertion {
      public static final String TOKEN_INCLUSION = "IncludeToken";
      public static final String NEVER = "/IncludeToken/Never";
      public static final String ONCE = "/IncludeToken/Once";
      public static final String ALWAYS_TO_RECIPIENT = "/IncludeToken/AlwaysToRecipient";
      public static final String ALWAYS_TO_INITIATOR = "/IncludeToken/AlwaysToInitiator";
      public static final String ALWAYS = "/IncludeToken/Always";
      public static final String ALWAYS_TO_RECIPIENT2 = "/AlwaysToRecipient";
      private String inclusion = null;
      private String namespace;

      public TokenInclusion() {
      }

      public TokenInclusion(String var1) {
         this.inclusion = var1;
      }

      void setNamespace(String var1) {
         this.namespace = var1;
      }

      String getNamespace() {
         return this.namespace;
      }

      public QName getName() {
         return new QName(this.getNamespace(), "IncludeToken", "sp");
      }

      public String getInclusion() {
         if (this.inclusion == null) {
            this.inclusion = this.getNamespace() + "/IncludeToken/Always";
         }

         return this.inclusion;
      }

      public Element serialize(Document var1) throws PolicyException {
         return null;
      }

      public void serialize(Element var1) {
         DOMUtils.addPrefixedAttribute(var1, this.getName(), "sp", this.inclusion);
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this.inclusion = var1.readUTF();
         this.namespace = var1.readUTF();
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeUTF(this.getInclusion());
         var1.writeUTF(this.namespace);
      }
   }
}
