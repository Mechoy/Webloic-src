package weblogic.wsee.tools.jws.decl.port;

import weblogic.wsee.util.PathUtil;

public class HttpsPort extends Port implements PortDecl {
   static final String PROTOCOL = "https";

   public HttpsPort() {
   }

   public HttpsPort(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public String getProtocol() {
      return "https";
   }

   public String getWsdlTransportNS() {
      return "http://schemas.xmlsoap.org/soap/https";
   }

   public String getURI() {
      StringBuffer var1 = new StringBuffer(this.getProtocol());
      var1.append("://localhost:7002");
      var1.append(PathUtil.normalizePath(this.getContextPath()));
      var1.append(this.getServiceUri());
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof HttpsPort) ? false : super.equals(var1);
      }
   }
}
