package weblogic.wsee.wstx.wsat.v11.types;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(
   name = "WSAT11Service",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
   wsdlLocation = "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl"
)
public class WSAT11Service extends Service {
   private static final URL WSAT11SERVICE_WSDL_LOCATION;
   private static final Logger logger = Logger.getLogger(WSAT11Service.class.getName());

   public WSAT11Service(URL var1, QName var2) {
      super(var1, var2);
   }

   public WSAT11Service() {
      super(WSAT11SERVICE_WSDL_LOCATION, new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "WSAT11Service"));
   }

   @WebEndpoint(
      name = "CoordinatorPort"
   )
   public CoordinatorPortType getCoordinatorPort() {
      return (CoordinatorPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "CoordinatorPort"), CoordinatorPortType.class);
   }

   @WebEndpoint(
      name = "CoordinatorPort"
   )
   public CoordinatorPortType getCoordinatorPort(WebServiceFeature... var1) {
      return (CoordinatorPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "CoordinatorPort"), CoordinatorPortType.class, var1);
   }

   @WebEndpoint(
      name = "ParticipantPort"
   )
   public ParticipantPortType getParticipantPort() {
      return (ParticipantPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "ParticipantPort"), ParticipantPortType.class);
   }

   @WebEndpoint(
      name = "ParticipantPort"
   )
   public ParticipantPortType getParticipantPort(WebServiceFeature... var1) {
      return (ParticipantPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "ParticipantPort"), ParticipantPortType.class, var1);
   }

   static {
      URL var0 = null;

      try {
         URL var1 = WSAT11Service.class.getResource(".");
         var0 = new URL(var1, "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl");
      } catch (MalformedURLException var2) {
         logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl', retrying as a local file");
         logger.warning(var2.getMessage());
      }

      WSAT11SERVICE_WSDL_LOCATION = var0;
   }
}
