package weblogic.wsee.jaxrpc;

import javax.xml.rpc.Call;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLSOAPFactory;

public final class JAXRPCContext {
   private static final boolean verbose = Verbose.isVerbose(JAXRPCContext.class);
   private static final SOAPFactory factory = WLSOAPFactory.createSOAPFactory();
   private Call call;

   JAXRPCContext(Call var1) {
      this.call = var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   public void setEndpointAddress(String var1) {
      this.call.setProperty("javax.xml.rpc.service.endpoint.address", var1);
   }

   public SOAPElement createElement(String var1, String var2, String var3) throws SOAPException {
      return factory.createElement(var1, var2, var3);
   }
}
