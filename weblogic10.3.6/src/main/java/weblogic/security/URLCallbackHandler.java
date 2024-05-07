package weblogic.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import weblogic.security.auth.callback.URLCallback;

public class URLCallbackHandler implements CallbackHandler {
   private String url;
   private String username;
   private byte[] password;

   /** @deprecated */
   public URLCallbackHandler(String var1, String var2) {
      this(var1, var2.getBytes());
   }

   public URLCallbackHandler(String var1, byte[] var2) {
      this.url = null;
      this.username = var1;
      this.password = var2;
   }

   /** @deprecated */
   public URLCallbackHandler(String var1, String var2, String var3) {
      this(var1, var2.getBytes(), var3);
   }

   public URLCallbackHandler(String var1, byte[] var2, String var3) {
      this.url = var3;
      this.username = var1;
      this.password = var2;
   }

   public void handle(Callback[] var1) throws UnsupportedCallbackException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] instanceof NameCallback) {
            NameCallback var3 = (NameCallback)var1[var2];
            var3.setName(this.username);
         } else if (var1[var2] instanceof PasswordCallback) {
            PasswordCallback var4 = (PasswordCallback)var1[var2];
            if (this.password != null) {
               var4.setPassword((new String(this.password)).toCharArray());
            }
         } else {
            if (!(var1[var2] instanceof URLCallback)) {
               throw new UnsupportedCallbackException(var1[var2], "Unrecognized Callback");
            }

            URLCallback var5 = (URLCallback)var1[var2];
            if (this.url != null) {
               var5.setURL(this.url);
            } else {
               var5.setURL(var5.getdefaultURL());
            }
         }
      }

   }
}
