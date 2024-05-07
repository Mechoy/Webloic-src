package weblogic.wsee.wstx.wsat.v10.endpoint;

import com.sun.xml.ws.developer.MemberSubmissionAddressing;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.endpoint.Coordinator;
import weblogic.wsee.wstx.wsat.v10.types.CoordinatorPortType;
import weblogic.wsee.wstx.wsat.v10.types.Notification;

@WebService(
   portName = "CoordinatorPortTypePort",
   serviceName = "WSAT10Service",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
   wsdlLocation = "/wsdls/wsat10/wsat.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsat.v10.types.CoordinatorPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@MemberSubmissionAddressing
public class CoordinatorPortTypePortImpl implements CoordinatorPortType {
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

   public void replayOperation(Notification var1) {
      Coordinator var2 = this.getProxy();
      var2.replayOperation(var1);
   }

   protected Coordinator<Notification> getProxy() {
      Coordinator var1 = new Coordinator(this.m_context, WSATVersion.v10);
      return var1;
   }
}
