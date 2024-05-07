package weblogic.security.auth.callback;

import javax.security.auth.callback.Callback;

public class URLCallback implements Callback {
   private String prompt;
   private String defaultURL;
   private String inputURL;

   public URLCallback(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.prompt = var1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public URLCallback(String var1, String var2) {
      if (var1 != null && var1.length() != 0 && var2 != null && var2.length() != 0) {
         this.prompt = var1;
         this.defaultURL = var2;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getPrompt() {
      return this.prompt;
   }

   public String getdefaultURL() {
      return this.defaultURL;
   }

   public void setURL(String var1) {
      this.inputURL = var1;
   }

   public String getURL() {
      return this.inputURL;
   }
}
