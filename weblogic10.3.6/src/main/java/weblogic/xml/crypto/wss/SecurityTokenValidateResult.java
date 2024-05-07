package weblogic.xml.crypto.wss;

import java.security.cert.X509Certificate;

public class SecurityTokenValidateResult {
   private boolean status;
   private String msg;
   private X509Certificate cert;
   private boolean flag;
   private boolean defferedValidation;

   public SecurityTokenValidateResult(boolean var1) {
      this(var1, (String)null);
   }

   public SecurityTokenValidateResult(boolean var1, String var2) {
      this.flag = false;
      this.defferedValidation = false;
      this.status = var1;
      this.msg = var2;
      this.flag = true;
   }

   public SecurityTokenValidateResult(boolean var1, X509Certificate var2) {
      this.flag = false;
      this.defferedValidation = false;
      this.status = var1;
      this.cert = var2;
      this.flag = false;
   }

   public boolean status() {
      return this.status;
   }

   public String getMsg() {
      if (!this.flag) {
         if (this.cert != null) {
            this.msg = this.cert.toString();
         }

         this.flag = true;
      }

      return this.msg;
   }

   public boolean isDefferedValidation() {
      return this.defferedValidation;
   }

   public void setDefferedValidation(boolean var1) {
      this.defferedValidation = var1;
   }

   public String toString() {
      String var1 = super.toString();
      if (this.defferedValidation) {
         var1 = var1 + "[defferedValidation]";
      } else {
         var1 = var1 + "[status: " + this.status + "]" + "[msg " + this.getMsg() + "]";
      }

      return var1;
   }
}
