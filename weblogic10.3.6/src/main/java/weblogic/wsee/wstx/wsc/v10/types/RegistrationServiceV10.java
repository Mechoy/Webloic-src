package weblogic.wsee.wstx.wsc.v10.types;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(
   name = "RegistrationService_V10",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
   wsdlLocation = "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsc10/wscoor.wsdl"
)
public class RegistrationServiceV10 extends Service {
   private static final URL REGISTRATIONSERVICEV10_WSDL_LOCATION;
   private static final Logger logger = Logger.getLogger(RegistrationServiceV10.class.getName());

   public RegistrationServiceV10(URL var1, QName var2) {
      super(var1, var2);
   }

   public RegistrationServiceV10() {
      super(REGISTRATIONSERVICEV10_WSDL_LOCATION, new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationService_V10"));
   }

   @WebEndpoint(
      name = "RegistrationRequesterPortTypePort"
   )
   public RegistrationRequesterPortType getRegistrationRequesterPortTypePort() {
      return (RegistrationRequesterPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationRequesterPortTypePort"), RegistrationRequesterPortType.class);
   }

   @WebEndpoint(
      name = "RegistrationRequesterPortTypePort"
   )
   public RegistrationRequesterPortType getRegistrationRequesterPortTypePort(WebServiceFeature... var1) {
      return (RegistrationRequesterPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationRequesterPortTypePort"), RegistrationRequesterPortType.class, var1);
   }

   @WebEndpoint(
      name = "RegistrationPortTypeRPCPort"
   )
   public RegistrationPortTypeRPC getRegistrationPortTypeRPCPort() {
      return (RegistrationPortTypeRPC)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationPortTypeRPCPort"), RegistrationPortTypeRPC.class);
   }

   @WebEndpoint(
      name = "RegistrationPortTypeRPCPort"
   )
   public RegistrationPortTypeRPC getRegistrationPortTypeRPCPort(WebServiceFeature... var1) {
      return (RegistrationPortTypeRPC)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationPortTypeRPCPort"), RegistrationPortTypeRPC.class, var1);
   }

   @WebEndpoint(
      name = "RegistrationCoordinatorPortTypePort"
   )
   public RegistrationCoordinatorPortType getRegistrationCoordinatorPortTypePort() {
      return (RegistrationCoordinatorPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationCoordinatorPortTypePort"), RegistrationCoordinatorPortType.class);
   }

   @WebEndpoint(
      name = "RegistrationCoordinatorPortTypePort"
   )
   public RegistrationCoordinatorPortType getRegistrationCoordinatorPortTypePort(WebServiceFeature... var1) {
      return (RegistrationCoordinatorPortType)super.getPort(new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegistrationCoordinatorPortTypePort"), RegistrationCoordinatorPortType.class, var1);
   }

   static {
      URL var0 = null;

      try {
         URL var1 = RegistrationServiceV10.class.getResource(".");
         var0 = new URL(var1, "file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsc10/wscoor.wsdl");
      } catch (MalformedURLException var2) {
         logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/weblogic/dev/src1036/wls/modules/wsee/src/weblogic/wsee/wstx/WEB-INF/wsdls/wsc10/wscoor.wsdl', retrying as a local file");
         logger.warning(var2.getMessage());
      }

      REGISTRATIONSERVICEV10_WSDL_LOCATION = var0;
   }
}
