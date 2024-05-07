package weblogic.xml.crypto.wss;

import javax.xml.namespace.QName;

public class WSSecurityException extends Exception {
   static final long serialVersionUID = 6394270481223271405L;
   private QName faultCode;

   public WSSecurityException() {
   }

   public WSSecurityException(Exception var1) {
      super(var1);
   }

   public WSSecurityException(String var1) {
      super(var1);
   }

   public WSSecurityException(String var1, Exception var2) {
      super(var1, var2);
   }

   public WSSecurityException(String var1, Object var2) {
      super(var1 + " " + var2);
   }

   public WSSecurityException(Exception var1, QName var2) {
      super(var1);
      this.faultCode = var2;
   }

   public WSSecurityException(String var1, QName var2) {
      super(var1);
      this.faultCode = var2;
   }

   public WSSecurityException(String var1, Exception var2, QName var3) {
      super(var1, var2);
      this.faultCode = var3;
   }

   public WSSecurityException(String var1, Object var2, QName var3) {
      super(var1 + " " + var2);
      this.faultCode = var3;
   }

   public QName getFaultCode() {
      return this.faultCode;
   }
}
