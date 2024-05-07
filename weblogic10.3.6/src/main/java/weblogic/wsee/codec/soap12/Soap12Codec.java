package weblogic.wsee.codec.soap12;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.codec.soap11.SoapCodec;
import weblogic.wsee.jws.wlw.JwsSoapFaultHelper;
import weblogic.wsee.jws.wlw.SoapFaultException;
import weblogic.wsee.jws.wlw.UnRecognizedFaultException;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapFault;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Fault;

public class Soap12Codec extends SoapCodec {
   protected Soap12Encoder createEncoder(SOAPMessage var1, WsdlBindingMessage var2, WsMethod var3, MessageContext var4) {
      return new Soap12Encoder(var1, var2, var3, var4);
   }

   protected Soap12Decoder createDecoder(SOAPMessageContext var1, WsdlBindingMessage var2, WsMethod var3) {
      return new Soap12Decoder(var1, var2, var3);
   }

   public MessageContext createContext() {
      return new SoapMessageContext(true);
   }

   protected SoapFault getSoapFault(WsdlBindingMessage var1) {
      return Soap12Fault.narrow(var1);
   }

   protected SoapBinding getSoapBinding(WsdlBinding var1) {
      return Soap12Binding.narrow(var1);
   }

   protected SoapBindingOperation getSoapBindingOperation(WsdlBindingOperation var1) {
      return Soap12BindingOperation.narrow(var1);
   }

   protected void fillFault(SOAPFault var1, SoapFaultException var2) throws SOAPException {
      JwsSoapFaultHelper.fillFault(var1, 2, var2);
   }

   protected SoapFaultException createExceptionFromFault(SOAPFault var1) throws UnRecognizedFaultException {
      return JwsSoapFaultHelper.createExceptionFromFault(var1, 2);
   }
}
