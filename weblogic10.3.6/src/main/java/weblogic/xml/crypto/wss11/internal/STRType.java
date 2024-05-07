package weblogic.xml.crypto.wss11.internal;

import javax.xml.namespace.QName;

public class STRType {
   private QName topLevelElement;
   private String valueType;
   private String tokenType;

   public STRType(QName var1) {
      this.topLevelElement = null;
      this.valueType = null;
      this.tokenType = null;
      this.topLevelElement = var1;
      if (var1 == null) {
         throw new IllegalArgumentException("QName topLevelElement can't be null.");
      }
   }

   public STRType(QName var1, String var2) {
      this(var1);
      this.valueType = var2;
   }

   public STRType(QName var1, String var2, String var3) {
      this(var1, var2);
      this.tokenType = var3;
   }

   public QName getTopLevelElement() {
      return this.topLevelElement;
   }

   public String getValueType() {
      return this.valueType;
   }

   public String getTokenType() {
      return this.tokenType;
   }

   public void setTokenType(String var1) {
      this.tokenType = var1;
   }
}
