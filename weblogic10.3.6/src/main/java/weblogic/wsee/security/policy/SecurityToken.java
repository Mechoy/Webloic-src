package weblogic.wsee.security.policy;

import java.util.List;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;

public class SecurityToken {
   private Node claims;
   private String tokenIssuer;
   private String issuerName;
   private String tokenTypeUri;
   private boolean includeInMessage;
   private String derivedFromTokenType;
   private boolean includeDerivedFromInMessage = true;
   private NormalizedExpression bootstrapPolicy = null;
   private List strTypes = null;
   private EncryptionMethod encMethod = null;
   private EncryptionMethod keyWrapMethod = null;
   private boolean optional = false;
   private List strTypesForDKBaseToken = null;

   public SecurityToken() {
   }

   public SecurityToken(Node var1, String var2, String var3, boolean var4) {
      this.claims = var1;
      this.tokenIssuer = var2;
      this.tokenTypeUri = var3;
      this.includeInMessage = var4;
   }

   public Node getClaims() {
      return this.claims;
   }

   public void setClaims(Node var1) {
      this.claims = var1;
   }

   public String getTokenIssuer() {
      return this.tokenIssuer;
   }

   public void setTokenIssuer(String var1) {
      this.tokenIssuer = var1;
   }

   public String getIssuerName() {
      return this.issuerName;
   }

   public void setIssuerName(String var1) {
      this.issuerName = var1;
   }

   public String getTokenTypeUri() {
      return this.tokenTypeUri;
   }

   public void setTokenTypeUri(String var1) {
      this.tokenTypeUri = var1;
   }

   public void setIncludeInMessage(boolean var1) {
      this.includeInMessage = var1;
   }

   public boolean isIncludeInMessage() {
      return this.includeInMessage;
   }

   public void setDerivedFromTokenType(String var1) {
      this.derivedFromTokenType = var1;
   }

   public String getDerivedFromTokenType() {
      return this.derivedFromTokenType;
   }

   public void setIncludeDerivedFromInMessage(boolean var1) {
      this.includeDerivedFromInMessage = var1;
   }

   public boolean isIncludeDerivedFromInMessage() {
      return this.includeDerivedFromInMessage;
   }

   public void setBootstrapPolicy(NormalizedExpression var1) {
      this.bootstrapPolicy = var1;
   }

   public NormalizedExpression getBootstrapPolicy() {
      return this.bootstrapPolicy;
   }

   public List getStrTypes() {
      return this.strTypes;
   }

   public void setStrTypes(List var1) {
      this.strTypes = var1;
   }

   public void setEncryptionMethod(EncryptionMethod var1) {
      this.encMethod = var1;
   }

   public EncryptionMethod getEncryptionMethod() {
      return this.encMethod;
   }

   public void setKeyWrapMethod(EncryptionMethod var1) {
      this.keyWrapMethod = var1;
   }

   public EncryptionMethod getKeyWrapMethod() {
      return this.keyWrapMethod;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public void setOptional(boolean var1) {
      this.optional = var1;
   }

   public List getStrTypesForDKBaseToken() {
      return this.strTypesForDKBaseToken;
   }

   public void setStrTypesForDKBaseToken(List var1) {
      this.strTypesForDKBaseToken = var1;
   }

   public String toString() {
      return "SecurityToken[ " + (this.tokenIssuer != null ? this.tokenIssuer : this.issuerName) + ",type=" + this.tokenTypeUri + ",claims=" + this.claims + "]";
   }
}
