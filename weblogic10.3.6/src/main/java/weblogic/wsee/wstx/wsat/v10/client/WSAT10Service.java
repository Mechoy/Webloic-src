package weblogic.wsee.wstx.wsat.v10.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsat.v10.types.CoordinatorPortType;
import weblogic.wsee.wstx.wsat.v10.types.ParticipantPortType;

@WebServiceClient(
   name = "WSAT10Service",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
   wsdlLocation = "wsat.wsdl"
)
public class WSAT10Service extends Service {
   private static final String WSDL = "/META-INF/wstx/wsdls/wsat10/wsat.wsdl";
   private static final URL WSAT10SERVICE_WSDL_LOCATION = WSAT10Service.class.getResource("/META-INF/wstx/wsdls/wsat10/wsat.wsdl");

   public WSAT10Service(URL var1, QName var2) {
      super(var1, var2);
   }

   public WSAT10Service() {
      super(WSAT10SERVICE_WSDL_LOCATION, new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "WSAT10Service"));
   }

   @WebEndpoint(
      name = "CoordinatorPortTypePort"
   )
   public CoordinatorPortType getCoordinatorPortTypePort(EndpointReference var1, WebServiceFeature... var2) {
      return (CoordinatorPortType)super.getPort(var1, CoordinatorPortType.class, var2);
   }

   @WebEndpoint(
      name = "ParticipantPortTypePort"
   )
   public ParticipantPortType getParticipantPortTypePort(EndpointReference var1, WebServiceFeature... var2) {
      return (ParticipantPortType)super.getPort(var1, ParticipantPortType.class, var2);
   }
}
