package weblogic.wsee.wstx.wsc.v11.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsc.v11.types.RegistrationCoordinatorPortType;
import weblogic.wsee.wstx.wsc.v11.types.RegistrationPortType;
import weblogic.wsee.wstx.wsc.v11.types.RegistrationRequesterPortType;

@WebServiceClient(
   name = "RegistrationService_V11",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
   wsdlLocation = "wstx-wscoor-1.1-wsdl-200702.wsdl"
)
public class RegistrationServiceV11 extends Service {
   private static final String WSDL = "/META-INF/wstx/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl";
   private static final URL REGISTRATIONSERVICEV11_WSDL_LOCATION = RegistrationServiceV11.class.getResource("/META-INF/wstx/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl");

   public RegistrationServiceV11(URL var1, QName var2) {
      super(var1, var2);
   }

   public RegistrationServiceV11() {
      super(REGISTRATIONSERVICEV11_WSDL_LOCATION, new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationService_V11"));
   }

   @WebEndpoint(
      name = "RegistrationPort"
   )
   public RegistrationPortType getRegistrationPort(EndpointReference var1, WebServiceFeature... var2) {
      return (RegistrationPortType)super.getPort(var1, RegistrationPortType.class, var2);
   }

   @WebEndpoint(
      name = "RegistrationRequesterPort"
   )
   public RegistrationRequesterPortType getRegistrationRequesterPort(EndpointReference var1, WebServiceFeature... var2) {
      return (RegistrationRequesterPortType)super.getPort(var1, RegistrationRequesterPortType.class, var2);
   }

   @WebEndpoint(
      name = "RegistrationCoordinatorPort"
   )
   public RegistrationCoordinatorPortType getRegistrationCoordinatorPort(EndpointReference var1, WebServiceFeature... var2) {
      return (RegistrationCoordinatorPortType)super.getPort(var1, RegistrationCoordinatorPortType.class, var2);
   }
}
