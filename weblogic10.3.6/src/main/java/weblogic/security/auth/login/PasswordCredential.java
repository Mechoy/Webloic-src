package weblogic.security.auth.login;

public class PasswordCredential {
   private String password;
   private String username;

   public PasswordCredential(String var1, String var2) {
      this.username = var1;
      this.password = var2;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }
}
