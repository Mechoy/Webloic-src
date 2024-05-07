package weblogic.wsee.codec.soap12;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.codec.soap11.SoapDecoder;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Body;

public class Soap12Decoder extends SoapDecoder {
   Soap12Decoder(SOAPMessageContext var1, WsdlBindingMessage var2, WsMethod var3) {
      super(var1, var2, var3);
   }

   protected Soap12Body getSoapBody(WsdlBindingMessage var1) {
      return Soap12Body.narrow(var1);
   }

   protected Soap12BindingOperation getSoapBindingOperation(WsdlBindingOperation var1) {
      return Soap12BindingOperation.narrow(var1);
   }

   protected Soap12Binding getSoapBinding(WsdlBinding var1) {
      return Soap12Binding.narrow(var1);
   }
}
