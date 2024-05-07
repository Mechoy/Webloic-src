package weblogic.wsee.wsdl.http;

import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;

public final class HttpAddress implements WsdlExtension {
   private static final String KEY = "HTTP-address";
   private String location;
   private static final boolean verbose = Verbose.isVerbose(HttpAddress.class);

   public HttpAddress(String var1) {
      this.location = var1;
   }

   HttpAddress() {
   }

   public String getKey() {
      return "HTTP-address";
   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String var1) {
      this.location = var1;
   }

   public static HttpAddress narrow(WsdlPort var0) {
      return (HttpAddress)var0.getExtension("HTTP-address");
   }

   public void parse(Element var1, WsdlPort var2) throws WsdlException {
      this.location = WsdlReader.getMustAttribute(var1, (String)null, "location");
      int var3 = this.location.indexOf(":");
      if (var2 instanceof WsdlPortBuilder) {
         WsdlPortBuilder var4 = (WsdlPortBuilder)var2;
         if (var3 != -1) {
            var4.setTransport(this.location.substring(0, var3));
         } else {
            var4.setTransport("http");
         }
      }

      if (verbose) {
         Verbose.log((Object)("Endpoint location :" + this.location));
      }

   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "address", "http://schemas.xmlsoap.org/wsdl/http/");
      String var4 = var2.getEndpointURL(this.location);
      if (var4 != null) {
         var2.setAttribute(var3, "location", (String)null, (String)var4);
      } else {
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

   public static HttpAddress attach(WsdlPort var0) {
      HttpAddress var1 = new HttpAddress();
      var0.putExtension(var1);
      if (var0 instanceof WsdlPortBuilder) {
         ((WsdlPortBuilder)var0).setTransport("http");
      }

      return var1;
   }
}
