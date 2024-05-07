package weblogic.xml.security.utils;

import javax.xml.namespace.QName;

public abstract class AbstractXMLReader implements XMLReader {
   protected static final QNameFactory qnameFactory = QNameCache.getInstance();
   private static final String DEFAULT_NAMESPACE = "";

   public QName getQNameAttribute(String var1, String var2) throws IllegalStateException {
      String var3 = this.getAttribute(var1, var2);
      return var3 == null ? null : this.getQName(var3);
   }

   protected QName getQName(String var1) {
      int var2 = var1.indexOf(":");
      return var2 < 0 ? qnameFactory.getQName(this.getNamespaceURI(""), var1) : qnameFactory.getQName(this.getNamespaceURI(var1.substring(0, var2)), var1.substring(var2 + 1, var1.length()));
   }

   public abstract String getText() throws IllegalStateException;

   public QName getQNameText() throws IllegalStateException {
      return this.getQName(this.getText());
   }
}
