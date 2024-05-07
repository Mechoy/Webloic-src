package weblogic.wsee.security.policy12.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.security.policy12.assertions.Header;
import weblogic.wsee.security.policy12.assertions.SignedElements;
import weblogic.wsee.security.policy12.assertions.SignedParts;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wssp.IntegrityAssertion;
import weblogic.wsee.security.wssp.QNameExpr;

public class IntegrityAssertionImpl implements IntegrityAssertion {
   private boolean isSignedBodyRequired = false;
   private boolean isSignedBodyOptional = false;
   private List<QNameExpr> signingParts = new ArrayList();
   private List<String> signingElements = new ArrayList();
   private SignedElements signedElementsPolicy;
   private String xpathVersion = "1.0";
   private static final String WSA_2004_08 = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
   private static final String WSA_10 = "http://www.w3.org/2005/08/addressing";

   void init(SignedElements var1) {
      this.signedElementsPolicy = var1;
      String var2 = var1.getXPathVersion();
      if (var2 != null && var2.length() > 0) {
         this.xpathVersion = var2;
      }

      Iterator var3 = var1.getXPathExpressions().iterator();

      while(var3.hasNext()) {
         this.signingElements.add(((XPath)var3.next()).getXPathExpr());
      }

   }

   void init(SignedParts var1) {
      if (var1.getBody() != null) {
         this.isSignedBodyRequired = true;
         this.isSignedBodyOptional = var1.isBodyOptional() || var1.getBody().isOptional() || var1.isOptional();
      }

      Iterator var2 = var1.getHeaders().iterator();

      while(var2.hasNext()) {
         Header var3 = (Header)var2.next();
         this.signingParts.add(new QNameExprImpl(var3.getHeaderName(), var3.getHeaderNamespaceUri(), var3.isOptional()));
      }

   }

   public boolean isSignedBodyRequired() {
      return this.isSignedBodyRequired;
   }

   public boolean isSignedBodyOptional() {
      return this.isSignedBodyOptional;
   }

   public List<String> getSigningElements() {
      return this.signingElements;
   }

   public String getXPathVersion() {
      return this.xpathVersion;
   }

   public List<QNameExpr> getSigningParts() {
      return this.signingParts;
   }

   public SignedElements getSignedElementsPolicy() {
      return this.signedElementsPolicy;
   }

   public boolean isSignedWsaHeadersRequired() {
      Iterator var1 = this.signingParts.iterator();

      String var3;
      do {
         do {
            if (!var1.hasNext()) {
               return false;
            }

            QNameExpr var2 = (QNameExpr)var1.next();
            var3 = var2.getNamespaceUri();
         } while(var3 == null);
      } while(!var3.equals("http://schemas.xmlsoap.org/ws/2004/08/addressing") && !var3.equals("http://www.w3.org/2005/08/addressing"));

      return true;
   }
}
