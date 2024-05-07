package weblogic.wsee.security.wssp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.security.policy12.assertions.AbstractSupportingTokens;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedParts;
import weblogic.wsee.security.policy12.assertions.Header;
import weblogic.wsee.security.policy12.assertions.SignedElements;
import weblogic.wsee.security.policy12.assertions.SignedParts;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.policy12.internal.QNameExprImpl;

public interface SupportingTokensAssertion {
   boolean hasSupportingTokens();

   List<TokenAssertion> getSupportingTokens();

   SecurityInfo getSecurityInfoOfSupportingTokens();

   boolean hasSignedSupportingTokens();

   List<TokenAssertion> getSignedSupportingTokens();

   SecurityInfo getSecurityInfoOfSignedSupportingTokens();

   boolean hasSignedEncryptedSupportingTokens();

   List<TokenAssertion> getSignedEncryptedSupportingTokens();

   SecurityInfo getSecurityInfoOfSignedEncryptedSupportingTokens();

   boolean hasEndorsingSupportingTokens();

   List<TokenAssertion> getEndorsingSupportingTokens();

   SecurityInfo getSecurityInfoOfEndorsingSupportingTokens();

   boolean hasSignedEndorsingSupportingTokens();

   List<TokenAssertion> getSignedEndorsingSupportingTokens();

   SecurityInfo getSecurityInfoOfSignedEndorsingSupportingTokens();

   boolean hasEncryptedSupportingTokens();

   List<TokenAssertion> getEncryptedSupportingTokens();

   SecurityInfo getSecurityInfoOfEncryptedSupportingTokens();

   /** @deprecated */
   boolean isEncryptedBodyRequired();

   /** @deprecated */
   boolean isEncryptedBodyOptional();

   public static class SecurityInfo {
      private boolean isSignedBodyRequired = false;
      private boolean isSignedBodyOptional = false;
      private boolean isEncryptedBodyRequired = false;
      private boolean isEncryptedBodyOptional = false;
      private String signedXPathVersion = null;
      private String encryptedXPathVersion = null;
      private List<QNameExpr> signedParts = new ArrayList();
      private List<XPath> signedElements = new ArrayList();
      private List<QNameExpr> encryptedParts = new ArrayList();
      private List<XPath> encryptedElements = new ArrayList();

      public boolean isSignedBodyRequired() {
         return this.isSignedBodyRequired;
      }

      public boolean isSignedBodyOptional() {
         return this.isSignedBodyOptional;
      }

      public boolean isEncryptedBodyRequired() {
         return this.isEncryptedBodyRequired;
      }

      public boolean isEncryptedBodyOptional() {
         return this.isEncryptedBodyOptional;
      }

      public String getSignedXPathVersion() {
         return this.signedXPathVersion;
      }

      public String getEncryptedXPathVersion() {
         return this.encryptedXPathVersion;
      }

      public List<QNameExpr> getSignedParts() {
         return this.signedParts;
      }

      public List<XPath> getSignedElements() {
         return this.signedElements;
      }

      public List<QNameExpr> getEncryptedParts() {
         return this.encryptedParts;
      }

      public List<XPath> getEncryptedElements() {
         return this.encryptedElements;
      }

      public void init(AbstractSupportingTokens var1) {
         if (var1 != null) {
            SignedParts var2 = var1.getSignedParts();
            if (var2 != null) {
               if (var2.getBody() != null) {
                  this.isSignedBodyRequired = true;
                  this.isSignedBodyOptional = var2.isBodyOptional() || var2.getBody().isOptional() || var2.isOptional();
               }

               Iterator var3 = var2.getHeaders().iterator();

               while(var3.hasNext()) {
                  Header var4 = (Header)var3.next();
                  this.signedParts.add(new QNameExprImpl(var4.getHeaderName(), var4.getHeaderNamespaceUri(), var4.isOptional() || var2.isOptional()));
               }
            }

            SignedElements var7 = var1.getSignedElements();
            if (var7 != null) {
               this.signedElements.addAll(var7.getXPathExpressions());
               this.signedXPathVersion = var7.getXPathVersion();
            }

            EncryptedParts var8 = var1.getEncryptedParts();
            if (var8 != null) {
               if (var8.getBody() != null) {
                  this.isEncryptedBodyRequired = true;
                  this.isEncryptedBodyOptional = var8.isBodyOptional() || var8.getBody().isOptional() || var8.isOptional();
               }

               Iterator var5 = var8.getHeaders().iterator();

               while(var5.hasNext()) {
                  Header var6 = (Header)var5.next();
                  this.encryptedParts.add(new QNameExprImpl(var6.getHeaderName(), var6.getHeaderNamespaceUri(), var6.isOptional() || var8.isOptional()));
               }
            }

            EncryptedElements var9 = var1.getEncryptedElements();
            if (var9 != null) {
               this.encryptedElements.addAll(var9.getXPathExpressions());
               this.encryptedXPathVersion = var9.getXPathVersion();
            }

         }
      }
   }
}
