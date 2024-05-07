package weblogic.wsee.wstx.wsc.v10.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationCoordinatorPortType;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationPortTypeRPC;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationRequesterPortType;

@WebServiceClient(
   name = "RegistrationService_V10",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
   wsdlLocation = "wscoor.wsdl"
)
public class RegistrationServiceV10 extends Service {
   private static final String WSDL = "/META-INF/wstx/wsdls/wsc10/wscoor.wsdl";
   private static final URL REGISTRATIONSERVICEV10_WSDL_LOCATION = RegistrationServiceV10.class.getResource("/META-INF/wstx/wsdls/wsc10/wscoor.wsdl");

   public RegistrationServiceV10(URL var1, QName var2) {
      super(var1, var2);
   }

   public RegistrationServiceV10() {
      super(REGISTRATIONSERVICEV10_WSDL_LOCATION, new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationService_V10"));
   }

   @WebEndpoint(
      name = "RegistrationRequesterPortTypePort"
   )
   public RegistrationRequesterPortType getRegistrationRequesterPortTypePort(EndpointReference var1, WebServiceFeature... var2) {
      return (RegistrationRequesterPortType)super.getPort(var1, RegistrationRequesterPortType.class, var2);
   }

   @WebEndpoint(
      name = "RegistrationPortTypeRPCPort"
   )
   public RegistrationPortTypeRPC getRegistrationPortTypeRPCPort(EndpointReference var1, WebServiceFeature... var2) {
      return (RegistrationPortTypeRPC)super.getPort(var1, RegistrationPortTypeRPC.class, var2);
   }

   @WebEndpoint(
      name = "RegistrationCoordinatorPortTypePort"
   )
   public RegistrationCoordinatorPortType getRegistrationCoordinatorPortTypePort(EndpointReference var1, WebServiceFeature... var2) {
      return (RegistrationCoordinatorPortType)super.getPort(var1, RegistrationCoordinatorPortType.class, var2);
   }
}
