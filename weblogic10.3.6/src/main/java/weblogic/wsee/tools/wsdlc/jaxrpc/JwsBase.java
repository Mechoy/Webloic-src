package weblogic.wsee.tools.wsdlc.jaxrpc;

import weblogic.wsee.util.jspgen.JspGenBase;

public abstract class JwsBase extends JspGenBase {
   protected JwsGenInfo jws;

   void setJwsGenInfo(JwsGenInfo var1) {
      this.jws = var1;
   }

   public JwsGenInfo getJwsGenInfo() {
      return this.jws;
   }

   public void setup(Object var1) {
   }
}
