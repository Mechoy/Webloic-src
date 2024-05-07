package weblogic.wsee.tools.jws.decl.port;

public interface PortDecl {
   String getURI();

   String getServiceUri();

   String getContextPath();

   String getPortName();

   String getWsdlTransportNS();

   String getProtocol();

   void setProtocol(String var1);

   String getNormalizedPath();
}
