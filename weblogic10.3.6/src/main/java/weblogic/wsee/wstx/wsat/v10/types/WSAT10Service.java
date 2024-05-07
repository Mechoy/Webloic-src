package weblogic.wsee.wstx.wsat.v10.types;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(
   name = "WSAT10Service",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
   wsdlLocation = "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsat10/wsat.wsdl"
)
public class WSAT10Service extends Service {
   private static final URL WSAT10SERVICE_WSDL_LOCATION;
   private static final Logger logger = Logger.getLogger(WSAT10Service.class.getName());

   public WSAT10Service(URL var1, QName var2) {
      super(var1, var2);
   }

   public WSAT10Service() {
      super(WSAT10SERVICE_WSDL_LOCATION, new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "WSAT10Service"));
   }

   @WebEndpoint(
      name = "CoordinatorPortTypePort"
   )
   public CoordinatorPortType getCoordinatorPortTypePort() {
      return (CoordinatorPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "CoordinatorPortTypePort"), CoordinatorPortType.class);
   }

   @WebEndpoint(
      name = "CoordinatorPortTypePort"
   )
   public CoordinatorPortType getCoordinatorPortTypePort(WebServiceFeature... var1) {
      return (CoordinatorPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "CoordinatorPortTypePort"), CoordinatorPortType.class, var1);
   }

   @WebEndpoint(
      name = "ParticipantPortTypePort"
   )
   public ParticipantPortType getParticipantPortTypePort() {
      return (ParticipantPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "ParticipantPortTypePort"), ParticipantPortType.class);
   }

   @WebEndpoint(
      name = "ParticipantPortTypePort"
   )
   public ParticipantPortType getParticipantPortTypePort(WebServiceFeature... var1) {
      return (ParticipantPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "ParticipantPortTypePort"), ParticipantPortType.class, var1);
   }

   static {
      URL var0 = null;

      try {
         URL var1 = WSAT10Service.class.getResource(".");
         var0 = new URL(var1, "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsat10/wsat.wsdl");
      } catch (MalformedURLException var2) {
         logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsat10/wsat.wsdl', retrying as a local file");
         logger.warning(var2.getMessage());
      }

      WSAT10SERVICE_WSDL_LOCATION = var0;
   }
}
