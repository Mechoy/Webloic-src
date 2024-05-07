package weblogic.wsee.tools.wsdlc.jaxws;

import com.sun.tools.ws.processor.model.Port;
import com.sun.tools.ws.processor.model.Service;
import com.sun.tools.ws.processor.model.java.JavaMethod;
import com.sun.tools.ws.processor.model.java.JavaParameter;
import java.util.Iterator;
import weblogic.wsee.util.jspgen.JspGenBase;

abstract class JwsBase extends JspGenBase {
   protected Service service;
   protected Port port;
   protected String wsdlLocation;
   protected String bindingType;

   void setBindingType(String var1) {
      this.bindingType = var1;
   }

   void setWsdlLocation(String var1) {
      this.wsdlLocation = var1;
   }

   void setService(Service var1) {
      this.service = var1;
   }

   void setPort(Port var1) {
      this.port = var1;
   }

   public void setup(Object var1) {
   }

   static String getThrowsClause(JavaMethod var0) {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = var0.getExceptions();
      if (var2.hasNext()) {
         var1.append("throws ");
      }

      while(var2.hasNext()) {
         var1.append(var2.next());
         if (var2.hasNext()) {
            var1.append(',');
         }
      }

      return var1.toString();
   }

   static String getArgumentString(JavaMethod var0) {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = var0.getParametersList().iterator();

      while(var2.hasNext()) {
         JavaParameter var3 = (JavaParameter)var2.next();
         if (var3.isHolder()) {
            var1.append("javax.xml.ws.Holder<").append(var3.getType().getName()).append(">");
         } else {
            var1.append(var3.getType().getName());
         }

         var1.append(" ");
         var1.append(var3.getName());
         if (var2.hasNext()) {
            var1.append(',');
         }
      }

      return var1.toString();
   }
}
