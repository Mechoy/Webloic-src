package weblogic.wsee.ws.dispatch.server;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.HeaderUtil;
import weblogic.wsee.ws.dispatch.Dispatcher;

public class OperationLookupHandler extends GenericHandler {
   public static final String OPERATION_NAME_PROPERTY = "weblogic.wsee.ws.server.OperationName";

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      AddressingHelper.validateAction(var2, true);
      QName var3 = this.getOperationName(var2);
      if (var3 == null) {
         AddressingHelper.checkFaultAction(var2);
         String var4 = "Failed to get operation name from incoming request";

         try {
            HeaderUtil.throwMustUnderstand(var1);
         } catch (SOAPFaultException var6) {
            this.throwMustUnderstand(var6, var4);
         }

         this.throwOperationNotFound(var4);
      }

      this.findBindingOperation(var2.getDispatcher(), var3, var1);
      this.findWsMethod(var2.getDispatcher(), var3);
      return true;
   }

   private void throwOperationNotFound(String var1) {
      QName var2 = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client", "SOAP-ENV");
      throw new SOAPFaultException(var2, var1, (String)null, (Detail)null);
   }

   private void throwMustUnderstand(SOAPFaultException var1, String var2) {
      String var3 = var1.getFaultString() + " " + var2;
      throw new SOAPFaultException(var1.getFaultCode(), var3, var1.getFaultActor(), var1.getDetail());
   }

   private QName getOperationName(WlMessageContext var1) {
      QName var2 = (QName)var1.getProperty("weblogic.wsee.ws.server.OperationName");
      if (var2 == null) {
         var2 = var1.getDispatcher().getOperationName();
         var1.setProperty("weblogic.wsee.ws.server.OperationName", var2);
      }

      return var2;
   }

   private void findWsMethod(Dispatcher var1, QName var2) {
      if (var1.getWsMethod() == null) {
         this.throwOperationNotFound("Unable to find method with name:" + var2 + " method available are -- " + var1.getWsPort().getEndpoint());
      }

   }

   private void findBindingOperation(Dispatcher var1, QName var2, MessageContext var3) {
      if (var1.getBindingOperation() == null) {
         String var4 = "Unable to find operation with name:" + var2 + " in the corresponding WSDL binding " + var1.getWsdlPort().getBinding();

         try {
            HeaderUtil.throwMustUnderstand(var3);
         } catch (SOAPFaultException var6) {
            this.throwMustUnderstand(var6, var4);
         }

         this.throwOperationNotFound(var4);
      }

   }
}
