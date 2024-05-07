package weblogic.wsee.wstx.wsc.v11.endpoint;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import weblogic.wsee.wstx.wsc.v11.XmlTypeAdapter;
import weblogic.wsee.wstx.wsc.v11.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v11.types.RegistrationRequesterPortType;

@WebService(
   portName = "RegistrationRequesterPort",
   serviceName = "RegistrationService_V11",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
   wsdlLocation = "/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsc.v11.types.RegistrationRequesterPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@Addressing
public class RegistrationRequesterPortImpl implements RegistrationRequesterPortType {
   @Resource
   private WebServiceContext m_context;

   public void registerResponse(RegisterResponseType var1) {
      RegistrationRequesterImpl var2 = new RegistrationRequesterImpl(this.m_context);
      var2.registerResponse(XmlTypeAdapter.adapt(var1));
   }
}
