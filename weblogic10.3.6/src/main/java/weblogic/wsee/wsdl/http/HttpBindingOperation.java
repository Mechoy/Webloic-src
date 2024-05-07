package weblogic.wsee.wsdl.http;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;

public class HttpBindingOperation implements WsdlExtension {
   public static final String KEY = "HTTP-binding-operation";
   private String location;

   public String getKey() {
      return "HTTP-binding-operation";
   }

   public String getLocation() {
      return this.location;
   }

   public static HttpBindingOperation narrow(WsdlBindingOperation var0) {
      return (HttpBindingOperation)var0.getExtension("HTTP-binding-operation");
   }

   public void parse(Element var1) {
      this.location = WsdlReader.getAttribute(var1, (String)null, "location");
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "operation", "http://schemas.xmlsoap.org/wsdl/http/");
      if (this.location != null) {
         var2.setAttribute(var3, "location", (String)null, (String)this.location);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("location", this.location);
      var1.end();
   }

   public static HttpBindingOperation attach(WsdlBindingOperation var0) {
      HttpBindingOperation var1 = new HttpBindingOperation();
      var0.putExtension(var1);
      return var1;
   }
}
