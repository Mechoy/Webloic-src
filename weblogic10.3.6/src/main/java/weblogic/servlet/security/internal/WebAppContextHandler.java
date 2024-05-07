package weblogic.servlet.security.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;

public class WebAppContextHandler implements ContextHandler {
   public static final String HTTP_SERVLET_REQUEST = "HttpServletRequest";
   public static final String HTTP_SERVLET_RESPONSE = "HttpServletResponse";
   private int SIZE = 4;
   private static final String[] names = new String[]{"com.bea.contextelement.servlet.HttpServletRequest", "com.bea.contextelement.servlet.HttpServletResponse", "HttpServletRequest", "HttpServletResponse"};
   private Object[] values = null;
   private ContextElement requestElement = null;
   private ContextElement responseElement = null;
   private ContextElement requestElementDeprecated = null;
   private ContextElement responseElementDeprecated = null;

   public WebAppContextHandler(HttpServletRequest var1, HttpServletResponse var2) {
      this.values = new Object[this.SIZE];
      this.values[0] = var1;
      this.values[1] = var2;
      this.values[2] = var1;
      this.values[3] = var2;
      this.requestElement = new ContextElement("com.bea.contextelement.servlet.HttpServletRequest", var1);
      this.responseElement = new ContextElement("com.bea.contextelement.servlet.HttpServletResponse", var2);
      this.requestElementDeprecated = new ContextElement("HttpServletRequest", var1);
      this.responseElementDeprecated = new ContextElement("HttpServletResponse", var2);
   }

   public int size() {
      return this.SIZE;
   }

   public String[] getNames() {
      return names;
   }

   public Object getValue(String var1) {
      if (var1 == null) {
         return null;
      } else if (var1.equals("com.bea.contextelement.servlet.HttpServletRequest")) {
         return this.values[0];
      } else if (var1.equals("com.bea.contextelement.servlet.HttpServletResponse")) {
         return this.values[1];
      } else if (var1.equals("HttpServletRequest")) {
         return this.values[2];
      } else {
         return var1.equals("HttpServletResponse") ? this.values[3] : null;
      }
   }

   private int indexOf(String var1) {
      if (var1 == null) {
         return -1;
      } else if (var1.equals("com.bea.contextelement.servlet.HttpServletRequest")) {
         return 0;
      } else if (var1.equals("com.bea.contextelement.servlet.HttpServletResponse")) {
         return 1;
      } else if (var1.equals("HttpServletRequest")) {
         return 2;
      } else {
         return var1.equals("HttpServletResponse") ? 3 : -1;
      }
   }

   public ContextElement[] getValues(String[] var1) {
      if (var1 != null && var1.length >= 1) {
         boolean var2 = false;
         boolean var3 = false;

         for(int var4 = 0; var4 < var1.length; ++var4) {
            int var5 = this.indexOf(var1[var4]);
            if (var5 != 0 && var5 != 2) {
               if (var5 == 1 || var5 == 3) {
                  var3 = true;
               }
            } else {
               var2 = true;
            }
         }

         ContextElement[] var6 = null;
         if (var2 && var3) {
            var6 = new ContextElement[]{this.requestElement, this.responseElement, this.requestElementDeprecated, this.responseElementDeprecated};
         } else if (var2 && !var3) {
            var6 = new ContextElement[]{this.requestElement, this.requestElementDeprecated};
         } else if (!var2 && var3) {
            var6 = new ContextElement[]{this.responseElement, null};
            var6[0] = this.responseElementDeprecated;
         }

         return var6;
      } else {
         return null;
      }
   }
}
