package weblogic.xml.crypto.wss.policy;

public class UNTClaims implements Claims {
   private String subjectName;
   private boolean usePassword;
   private String passwordType;

   public UNTClaims(String var1, boolean var2, String var3) {
      this.subjectName = var1;
      this.usePassword = var2;
      this.passwordType = var3;
   }

   public String getSubjectName() {
      return this.subjectName;
   }

   public boolean isUsePassword() {
      return this.usePassword;
   }

   public String getPasswordType() {
      return this.passwordType;
   }
}
