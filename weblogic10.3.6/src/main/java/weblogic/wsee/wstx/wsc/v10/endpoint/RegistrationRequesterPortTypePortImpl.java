package weblogic.wsee.wstx.wsc.v10.endpoint;

import com.sun.xml.ws.developer.MemberSubmissionAddressing;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsc.v10.XmlTypeAdapter;
import weblogic.wsee.wstx.wsc.v10.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationRequesterPortType;

@WebService(
   portName = "RegistrationRequesterPortTypePort",
   serviceName = "RegistrationService_V10",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
   wsdlLocation = "/wsdls/wsc10/wscoor.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsc.v10.types.RegistrationRequesterPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@MemberSubmissionAddressing
public class RegistrationRequesterPortTypePortImpl implements RegistrationRequesterPortType {
   @Resource
   private WebServiceContext m_context;

   public void registerResponse(RegisterResponseType var1) {
      RegistrationRequesterImpl var2 = new RegistrationRequesterImpl(this.m_context);
      var2.registerResponse(XmlTypeAdapter.adapt(var1));
   }
}
