package weblogic.wsee.wstx.wsc.v11.types;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(
   name = "RegistrationService_V11",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
   wsdlLocation = "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl"
)
public class RegistrationServiceV11 extends Service {
   private static final URL REGISTRATIONSERVICEV11_WSDL_LOCATION;
   private static final Logger logger = Logger.getLogger(RegistrationServiceV11.class.getName());

   public RegistrationServiceV11(URL var1, QName var2) {
      super(var1, var2);
   }

   public RegistrationServiceV11() {
      super(REGISTRATIONSERVICEV11_WSDL_LOCATION, new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationService_V11"));
   }

   @WebEndpoint(
      name = "RegistrationPort"
   )
   public RegistrationPortType getRegistrationPort() {
      return (RegistrationPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationPort"), RegistrationPortType.class);
   }

   @WebEndpoint(
      name = "RegistrationPort"
   )
   public RegistrationPortType getRegistrationPort(WebServiceFeature... var1) {
      return (RegistrationPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationPort"), RegistrationPortType.class, var1);
   }

   @WebEndpoint(
      name = "RegistrationCoordinatorPort"
   )
   public RegistrationCoordinatorPortType getRegistrationCoordinatorPort() {
      return (RegistrationCoordinatorPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationCoordinatorPort"), RegistrationCoordinatorPortType.class);
   }

   @WebEndpoint(
      name = "RegistrationCoordinatorPort"
   )
   public RegistrationCoordinatorPortType getRegistrationCoordinatorPort(WebServiceFeature... var1) {
      return (RegistrationCoordinatorPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationCoordinatorPort"), RegistrationCoordinatorPortType.class, var1);
   }

   @WebEndpoint(
      name = "RegistrationRequesterPort"
   )
   public RegistrationRequesterPortType getRegistrationRequesterPort() {
      return (RegistrationRequesterPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationRequesterPort"), RegistrationRequesterPortType.class);
   }

   @WebEndpoint(
      name = "RegistrationRequesterPort"
   )
   public RegistrationRequesterPortType getRegistrationRequesterPort(WebServiceFeature... var1) {
      return (RegistrationRequesterPortType)super.getPort(new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegistrationRequesterPort"), RegistrationRequesterPortType.class, var1);
   }

   static {
      URL var0 = null;

      try {
         URL var1 = RegistrationServiceV11.class.getResource(".");
         var0 = new URL(var1, "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl");
      } catch (MalformedURLException var2) {
         logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsc11/wstx-wscoor-1.1-wsdl-200702.wsdl', retrying as a local file");
         logger.warning(var2.getMessage());
      }

      REGISTRATIONSERVICEV11_WSDL_LOCATION = var0;
   }
}
