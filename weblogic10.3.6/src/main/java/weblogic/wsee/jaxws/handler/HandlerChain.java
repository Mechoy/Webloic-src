package weblogic.wsee.jaxws.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;

class HandlerChain {
   private final String serviceNamePattern;
   private final String portNamePattern;
   private final List<String> protocolBindings;

   HandlerChain(String var1, String var2, String var3) {
      this.serviceNamePattern = var1;
      this.portNamePattern = var2;
      if (var3 == null) {
         this.protocolBindings = null;
      } else {
         this.protocolBindings = Arrays.asList(var3.split("\\s"));
      }

   }

   private static boolean isMatch(QName var0, String var1) {
      if (var0 != null && var1 != null) {
         QName var2 = QName.valueOf(var1);
         if (!var2.getNamespaceURI().equals(var0.getNamespaceURI()) && !var2.getNamespaceURI().equals("")) {
            return false;
         } else {
            String var3 = var2.getLocalPart().replaceAll("\\*", ".*");
            return Pattern.matches(var3, var0.getLocalPart());
         }
      } else {
         return true;
      }
   }

   private static boolean isMatch(String var0, List<String> var1) {
      if (var0 != null && var1 != null) {
         Iterator var2 = var1.iterator();

         String var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (String)var2.next();
         } while(!BindingIdTranslator.translate(var3).equals(var0));

         return true;
      } else {
         return true;
      }
   }

   public boolean isMatch(QName var1, QName var2, String var3) {
      if (!isMatch(var1, this.serviceNamePattern)) {
         return false;
      } else if (!isMatch(var2, this.portNamePattern)) {
         return false;
      } else {
         return isMatch(var3, this.protocolBindings);
      }
   }

   public Set<String> getApplicableProtocols() {
      HashSet var1 = new HashSet();
      if (this.protocolBindings == null) {
         var1.add("http://www.w3.org/2004/08/wsdl/http");
         var1.add("http://schemas.xmlsoap.org/wsdl/soap/http");
         var1.add("http://www.w3.org/2003/05/soap/bindings/HTTP/");
         var1.add("http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true");
         var1.add("http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true");
      } else {
         Iterator var2 = this.protocolBindings.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.add(BindingIdTranslator.translate(var3));
         }
      }

      return var1;
   }
}
