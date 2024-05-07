package weblogic.wsee.security.policy12.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import weblogic.wsee.security.policy12.assertions.Header;
import weblogic.wsee.security.policy12.assertions.RequiredElements;
import weblogic.wsee.security.policy12.assertions.RequiredParts;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wssp.ProtectionAssertion;
import weblogic.wsee.security.wssp.QNameExpr;

public class ProtectionAssertionImpl implements ProtectionAssertion {
   List<String> requiredElements = new LinkedList();
   private RequiredElements requiredElementsPolicy = null;
   private List<QNameExpr> requiredParts = new ArrayList();
   private RequiredParts requiredPartsPolicy = null;
   String xpathVersion = "1.0";

   ProtectionAssertionImpl(RequiredElements var1) {
      this.requiredElementsPolicy = var1;
      String var2 = var1.getXPathVersion();
      if (var2 != null && var2.length() > 0) {
         this.xpathVersion = var2;
      }

      Set var3 = var1.getXPathExpressions();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         this.requiredElements.add(((XPath)var4.next()).getXPathExpr());
      }

   }

   public ProtectionAssertionImpl(RequiredParts var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null Required Parts found");
      } else {
         this.requiredPartsPolicy = var1;
         Iterator var2 = var1.getHeaders().iterator();

         while(var2.hasNext()) {
            Header var3 = (Header)var2.next();
            this.requiredParts.add(new QNameExprImpl(var3.getHeaderName(), var3.getHeaderNamespaceUri(), var3.isOptional() || var1.isOptional()));
         }

      }
   }

   public List<String> getRequiredElements() {
      return this.requiredElements;
   }

   public RequiredElements getRequiredElementsPolicy() {
      return this.requiredElementsPolicy;
   }

   public String getXPathVersion() {
      return this.xpathVersion;
   }

   public List<QNameExpr> getRequiredParts() {
      return this.requiredParts;
   }

   public RequiredParts getRequiredPartsPolicy() {
      return this.requiredPartsPolicy;
   }

   public boolean hasRequiredPartsPolicy() {
      return this.requiredPartsPolicy != null;
   }
}
