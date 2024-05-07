package weblogic.wsee.wstx.wsat.v11.endpoint;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.endpoint.Coordinator;
import weblogic.wsee.wstx.wsat.v11.types.CoordinatorPortType;
import weblogic.wsee.wstx.wsat.v11.types.Notification;

@WebService(
   portName = "CoordinatorPort",
   serviceName = "WSAT11Service",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
   wsdlLocation = "/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsat.v11.types.CoordinatorPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@Addressing
public class CoordinatorPortImpl implements CoordinatorPortType {
   @Resource
   private WebServiceContext m_context;

   public void preparedOperation(Notification var1) {
      Coordinator var2 = this.getProxy();
      var2.preparedOperation(var1);
   }

   public void abortedOperation(Notification var1) {
      Coordinator var2 = this.getProxy();
      var2.abortedOperation(var1);
   }

   public void readOnlyOperation(Notification var1) {
      Coordinator var2 = this.getProxy();
      var2.readOnlyOperation(var1);
   }

   public void committedOperation(Notification var1) {
      Coordinator var2 = this.getProxy();
      var2.committedOperation(var1);
   }

   protected Coordinator<Notification> getProxy() {
      return new Coordinator(this.m_context, WSATVersion.v11);
   }
}
