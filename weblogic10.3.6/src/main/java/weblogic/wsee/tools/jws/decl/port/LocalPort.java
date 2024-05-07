package weblogic.wsee.tools.jws.decl.port;

public class LocalPort extends Port implements PortDecl {
   private static final String LOCAL_TRANSPORT = "http://www.bea.com/soap/transport/local";
   static final String PROTOCOL = "local";

   public LocalPort() {
   }

   public LocalPort(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public String getProtocol() {
      return "local";
   }

   public String getWsdlTransportNS() {
      return "http://www.bea.com/soap/transport/local";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof LocalPort) ? false : super.equals(var1);
      }
   }
}
