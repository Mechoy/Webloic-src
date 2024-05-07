package weblogic.servlet.security.internal;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.security.auth.callback.ContextHandlerCallback;
import weblogic.security.auth.callback.URLCallback;

class ServletCallbackHandler implements CallbackHandler {
   private final String username;
   private final Object password;
   private final HttpServletRequest request;
   private final HttpServletResponse response;

   ServletCallbackHandler(String var1, Object var2, HttpServletRequest var3, HttpServletResponse var4) {
      this.username = var1;
      this.password = var2;
      this.request = var3;
      this.response = var4;
   }

   public void handle(Callback[] var1) throws UnsupportedCallbackException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof NameCallback) {
               NameCallback var3 = (NameCallback)var1[var2];
               var3.setName(this.username);
            } else if (var1[var2] instanceof PasswordCallback) {
               PasswordCallback var6 = (PasswordCallback)var1[var2];
               var6.setPassword(((String)this.password).toCharArray());
            } else {
               String var4;
               if (var1[var2] instanceof TextInputCallback) {
                  TextInputCallback var7 = (TextInputCallback)((TextInputCallback)var1[var2]);
                  var4 = var7.getPrompt();
                  if (var4 != null) {
                     String var5 = this.request.getParameter(var4);
                     if (var5 == null) {
                        var5 = var7.getDefaultText();
                     }

                     var7.setText(var5);
                  }
               } else if (var1[var2] instanceof URLCallback) {
                  URLCallback var8 = (URLCallback)var1[var2];
                  var4 = this.request.getRequestURL().toString();
                  if (var4 != null) {
                     var8.setURL(var4);
                  } else {
                     var8.setURL(var8.getdefaultURL());
                  }
               } else {
                  if (!(var1[var2] instanceof ContextHandlerCallback)) {
                     throw new UnsupportedCallbackException(var1[var2], "Unrecognized Callback");
                  }

                  ContextHandlerCallback var9 = (ContextHandlerCallback)var1[var2];
                  var9.setContextHandler(WebAppSecurity.getContextHandler(this.request, this.response));
               }
            }
         }

      }
   }
}
