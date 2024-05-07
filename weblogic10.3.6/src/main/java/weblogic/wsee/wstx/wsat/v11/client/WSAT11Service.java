package weblogic.wsee.wstx.wsat.v11.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsat.v11.types.CoordinatorPortType;
import weblogic.wsee.wstx.wsat.v11.types.ParticipantPortType;

@WebServiceClient(
   name = "WSAT11Service",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
   wsdlLocation = "wstx-wsat-1.1-wsdl-200702.wsdl"
)
public class WSAT11Service extends Service {
   private static final String WSDL = "/META-INF/wstx/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl";
   private static final URL WSAT11SERVICE_WSDL_LOCATION = WSAT11Service.class.getResource("/META-INF/wstx/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl");

   public WSAT11Service(URL var1, QName var2) {
      super(var1, var2);
   }

   public WSAT11Service() {
      super(WSAT11SERVICE_WSDL_LOCATION, new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "WSAT11Service"));
   }

   @WebEndpoint(
      name = "CoordinatorPort"
   )
   public CoordinatorPortType getCoordinatorPort(EndpointReference var1, WebServiceFeature... var2) {
      return (CoordinatorPortType)super.getPort(var1, CoordinatorPortType.class, var2);
   }

   @WebEndpoint(
      name = "ParticipantPort"
   )
   public ParticipantPortType getParticipantPort(EndpointReference var1, WebServiceFeature... var2) {
      return (ParticipantPortType)super.getPort(var1, ParticipantPortType.class, var2);
   }
}
