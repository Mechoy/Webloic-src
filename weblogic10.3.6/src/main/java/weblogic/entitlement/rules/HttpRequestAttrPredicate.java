package weblogic.entitlement.rules;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;

public abstract class HttpRequestAttrPredicate extends BasePredicate {
   private static final String VERSION = "1.0";
   protected static final PredicateArgument ATTR_NAME_ARGUMENT = new StringPredicateArgument("HttpRequestAttrPredicateAttributeArgumentName", "HttpRequestAttrPredicateAttributeArgumentDescription", (String)null) {
      public void validateValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
         super.validateValue(var1, var2);
         String var3 = (String)var1;
         if (!var3.startsWith("Attribute.") && !var3.startsWith("Header.") && !var3.startsWith("Parameter.") && !var3.equals("Method") && !var3.equals("RequestURI") && !var3.equals("QueryString") && !var3.equals("Protocol") && !var3.equals("LocalPort") && !var3.equals("RemotePort") && !var3.equals("RemoteHost") && !var3.startsWith("Session.Attribute.")) {
            String var4 = (new PredicateTextFormatter(var2)).getInvalidAttributeNameMessage(var3);
            throw new IllegalPredicateArgumentException(var4);
         }
      }
   };
   private String attrName;

   public HttpRequestAttrPredicate(String var1, String var2) {
      super(var1, var2);
   }

   protected String getAttributeName() {
      return this.attrName;
   }

   protected void setAttributeName(String var1) throws IllegalPredicateArgumentException {
      ATTR_NAME_ARGUMENT.validateValue(var1, (Locale)null);
      this.attrName = var1;
   }

   protected Object getAttribute(ContextHandler var1) {
      if (var1 == null) {
         return null;
      } else {
         Object var2 = null;
         HttpServletRequest var3 = (HttpServletRequest)var1.getValue("com.bea.contextelement.servlet.HttpServletRequest");
         if (var3 != null) {
            if (this.attrName.startsWith("Attribute.")) {
               var2 = var3.getAttribute(this.attrName.substring("Attribute.".length()));
            } else if (this.attrName.startsWith("Header.")) {
               var2 = var3.getHeader(this.attrName.substring("Header.".length()));
            } else if (this.attrName.startsWith("Parameter.")) {
               var2 = var3.getParameter(this.attrName.substring("Parameter.".length()));
            } else if (this.attrName.equals("Method")) {
               var2 = var3.getMethod();
            } else if (this.attrName.equals("RequestURI")) {
               var2 = var3.getRequestURI();
            } else if (this.attrName.equals("QueryString")) {
               var2 = var3.getQueryString();
            } else if (this.attrName.equals("Protocol")) {
               var2 = var3.getProtocol();
            } else if (this.attrName.equals("LocalPort")) {
               var2 = new Integer(var3.getLocalPort());
            } else if (this.attrName.equals("RemotePort")) {
               var2 = new Integer(var3.getRemotePort());
            } else if (this.attrName.equals("RemoteHost")) {
               var2 = var3.getRemoteHost();
            } else {
               if (!this.attrName.startsWith("Session.Attribute.")) {
                  throw new IllegalArgumentException("Not a supported attribute: " + this.attrName);
               }

               HttpSession var4 = var3.getSession();
               if (var4 != null) {
                  var2 = var4.getAttribute(this.attrName.substring("Session.Attribute.".length()));
               }
            }
         }

         return var2;
      }
   }

   public boolean isSupportedResource(String var1) {
      return var1.startsWith("type=<url>");
   }

   public String getVersion() {
      return "1.0";
   }
}
