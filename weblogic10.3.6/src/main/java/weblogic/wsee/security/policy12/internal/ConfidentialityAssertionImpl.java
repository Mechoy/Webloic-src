package weblogic.wsee.security.policy12.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.security.policy12.assertions.ContentEncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedParts;
import weblogic.wsee.security.policy12.assertions.Header;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wssp.ConfidentialityAssertion;
import weblogic.wsee.security.wssp.QNameExpr;

public class ConfidentialityAssertionImpl implements ConfidentialityAssertion {
   private boolean isEncryptedBodyRequired = false;
   private boolean isEncryptedBodyOptional = false;
   private boolean isEncryptedHeaderRequired = false;
   private List<String> encryptingElements = new ArrayList();
   private List<String> contentEncryptingElements = new ArrayList();
   private List<QNameExpr> encryptingParts = new ArrayList();
   private EncryptedElements encryptedElementsPolicy;
   private ContentEncryptedElements contentEncryptedElementsPolicy;
   private String xpathVersion;

   void init(ContentEncryptedElements var1) {
      this.contentEncryptedElementsPolicy = var1;
      String var2 = var1.getXPathVersion();
      if (var2 != null && var2.length() > 0) {
         this.xpathVersion = var2;
      }

      Iterator var3 = var1.getXPathExpressions().iterator();

      while(var3.hasNext()) {
         this.contentEncryptingElements.add(((XPath)var3.next()).getXPathExpr());
      }

   }

   void init(EncryptedElements var1) {
      this.encryptedElementsPolicy = var1;
      String var2 = var1.getXPathVersion();
      if (var2 != null && var2.length() > 0) {
         this.xpathVersion = var2;
      }

      Iterator var3 = var1.getXPathExpressions().iterator();

      while(var3.hasNext()) {
         this.encryptingElements.add(((XPath)var3.next()).getXPathExpr());
      }

   }

   void init(EncryptedParts var1) {
      if (var1.getBody() != null) {
         this.isEncryptedBodyRequired = true;
         this.isEncryptedBodyOptional = var1.isBodyOptional() || var1.getBody().isOptional() || var1.isOptional();
      }

      Iterator var2 = var1.getHeaders().iterator();

      while(var2.hasNext()) {
         Header var3 = (Header)var2.next();
         this.encryptingParts.add(new QNameExprImpl(var3.getHeaderName(), var3.getHeaderNamespaceUri(), var3.isOptional() || var1.isOptional()));
      }

   }

   public boolean isEncryptedBodyRequired() {
      return this.isEncryptedBodyRequired;
   }

   public boolean isEncryptedHeaderRequired() {
      return this.isEncryptedHeaderRequired;
   }

   public boolean isEncryptedBodyOptional() {
      return this.isEncryptedBodyOptional;
   }

   void setEncryptedHeaderRequired(boolean var1) {
      this.isEncryptedHeaderRequired = var1;
   }

   public List<String> getEncryptingElements() {
      return this.encryptingElements;
   }

   public List<String> getContentEncryptingElements() {
      return this.contentEncryptingElements;
   }

   public EncryptedElements getEncryptedElementsPolicy() {
      return this.encryptedElementsPolicy;
   }

   public ContentEncryptedElements getContentEncryptedElementsPolicy() {
      return this.contentEncryptedElementsPolicy;
   }

   public String getXPathVersion() {
      return this.xpathVersion;
   }

   public List<QNameExpr> getEncryptingParts() {
      return this.encryptingParts;
   }
}
