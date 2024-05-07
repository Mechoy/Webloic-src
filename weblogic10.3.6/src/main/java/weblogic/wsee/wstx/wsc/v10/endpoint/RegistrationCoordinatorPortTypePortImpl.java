package weblogic.wsee.wstx.wsc.v10.endpoint;

import com.sun.xml.ws.developer.MemberSubmissionAddressing;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import weblogic.wsee.wstx.wsc.v10.types.RegisterType;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationCoordinatorPortType;

@WebService(
   portName = "RegistrationCoordinatorPortTypePort",
   serviceName = "RegistrationService_V10",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
   wsdlLocation = "/wsdls/wsc10/wscoor.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsc.v10.types.RegistrationCoordinatorPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@MemberSubmissionAddressing
public class RegistrationCoordinatorPortTypePortImpl implements RegistrationCoordinatorPortType {
   public void registerOperation(RegisterType var1) {
   }
}
