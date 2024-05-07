package weblogic.wsee.tools.source;

import weblogic.wsee.wsdl.WsdlPort;

public class JsPort {
   private WsdlPort wsdlPort;
   private JsClass endpoint;

   public JsPort(WsdlPort var1, JsClass var2) {
      this.wsdlPort = var1;
      this.endpoint = var2;
   }

   public WsdlPort getWsdlPort() {
      return this.wsdlPort;
   }

   public JsClass getEndpoint() {
      return this.endpoint;
   }
}
