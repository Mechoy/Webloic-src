package weblogic.wsee.wstx.wsc.v11.endpoint;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.v11.XmlTypeAdapter;
import weblogic.wsee.wstx.wsc.v11.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v11.types.RegisterType;
import weblogic.wsee.wstx.wsc.v11.types.RegistrationPortType;

@WebService(
   portName = "RegistrationPort",
   serviceName = "RegistrationService_V11",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
   wsdlLocation = "/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsc.v11.types.RegistrationPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@Addressing
public class RegistrationPortImpl implements RegistrationPortType {
   @Resource
   private WebServiceContext m_context;

   public RegisterResponseType registerOperation(RegisterType var1) {
      this.m_context.getMessageContext().put("javax.xml.ws.soap.http.soapaction.use", true);
      this.m_context.getMessageContext().put("javax.xml.ws.soap.http.soapaction.uri", "http://docs.oasis-open.org/ws-tx/wscoor/2006/06/RegisterResponse");
      RegistrationProxyImpl var2 = this.getProxy();
      BaseRegisterResponseType var3 = var2.registerOperation(XmlTypeAdapter.adapt(var1));
      return (RegisterResponseType)var3.getDelegate();
   }

   protected RegistrationProxyImpl getProxy() {
      return new RegistrationProxyImpl(this.m_context);
   }
}
