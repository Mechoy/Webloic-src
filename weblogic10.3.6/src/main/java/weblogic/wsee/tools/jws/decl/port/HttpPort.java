package weblogic.wsee.tools.jws.decl.port;

public class HttpPort extends Port implements PortDecl {
   static final String PROTOCOL = "http";

   public HttpPort() {
   }

   public HttpPort(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public String getProtocol() {
      return this.protocol;
   }

   public String getWsdlTransportNS() {
      return "http://schemas.xmlsoap.org/soap/http";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof HttpPort) ? false : super.equals(var1);
      }
   }
}
