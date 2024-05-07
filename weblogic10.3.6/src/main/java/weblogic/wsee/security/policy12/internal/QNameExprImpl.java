package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.wssp.QNameExpr;

public class QNameExprImpl implements QNameExpr {
   private String localName;
   private String namespaceUri;
   private boolean optional;

   public QNameExprImpl(String var1, String var2, boolean var3) {
      this.localName = var1;
      this.namespaceUri = var2;
      this.optional = var3;
   }

   QNameExprImpl(String var1, String var2) {
      this.localName = var1;
      this.namespaceUri = var2;
   }

   public String getLocalName() {
      return this.localName;
   }

   public String getNamespaceUri() {
      return this.namespaceUri;
   }

   public boolean isOptional() {
      return this.optional;
   }
}
