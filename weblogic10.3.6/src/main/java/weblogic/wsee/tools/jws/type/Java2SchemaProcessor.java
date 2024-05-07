package weblogic.wsee.tools.jws.type;

import java.util.Iterator;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.CallbackServiceDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class Java2SchemaProcessor extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getCowReader() == null) {
         Java2SchemaDriver var2 = new Java2SchemaDriver(var1);
         var2.drive(var1.getWebService());
         CallbackServiceDecl var3 = var1.getWebService().getCallbackService();
         if (var3 != null) {
            var2.drive(var3);
         }

      }
   }

   private class Java2SchemaDriver {
      SOAPBindingProcessor doclitBare;
      SOAPBindingProcessor doclitWrap;
      SOAPBindingProcessor rpc;

      public Java2SchemaDriver(JAXRPCWebServiceInfo var2) {
         this.doclitBare = new DocLiteralBareProcessor(var2);
         this.doclitWrap = new DocLiteralWrapProcessor(var2);
         this.rpc = new RpcProcessor(var2);
      }

      private void drive(WebServiceSEIDecl var1) throws WsBuildException {
         Iterator var2 = var1.getWebMethods();

         while(var2.hasNext()) {
            WebMethodDecl var3 = (WebMethodDecl)var2.next();
            if (var3.getSoapBinding().isDocLiteralWrapped()) {
               this.doclitWrap.processMethod(var3);
            } else if (var3.getSoapBinding().isDocLiteralBare()) {
               this.doclitBare.processMethod(var3);
            } else {
               this.rpc.processMethod(var3);
            }
         }

      }
   }
}
