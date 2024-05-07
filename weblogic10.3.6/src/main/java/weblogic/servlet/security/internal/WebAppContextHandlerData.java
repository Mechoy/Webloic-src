package weblogic.servlet.security.internal;

import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;
import weblogic.security.jacc.PolicyContextHandlerData;

public class WebAppContextHandlerData implements PolicyContextHandlerData {
   public static final String HTTP_REQUEST_KEY = "javax.servlet.http.HttpServletRequest";
   private static final String[] keys = new String[]{"javax.servlet.http.HttpServletRequest"};
   private final HttpServletRequest request;

   public WebAppContextHandlerData(HttpServletRequest var1) {
      this.request = var1;
   }

   public Object getContext(String var1) throws PolicyContextException {
      return var1 != null && var1.equals("javax.servlet.http.HttpServletRequest") ? this.request : null;
   }

   public static String[] getKeys() {
      return keys;
   }
}
