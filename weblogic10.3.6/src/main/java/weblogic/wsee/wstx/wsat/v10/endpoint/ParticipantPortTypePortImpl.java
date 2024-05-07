package weblogic.wsee.wstx.wsat.v10.endpoint;

import com.sun.xml.ws.developer.MemberSubmissionAddressing;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.endpoint.Participant;
import weblogic.wsee.wstx.wsat.v10.types.Notification;
import weblogic.wsee.wstx.wsat.v10.types.ParticipantPortType;

@WebService(
   portName = "ParticipantPortTypePort",
   serviceName = "WSAT10Service",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
   wsdlLocation = "/wsdls/wsat10/wsat.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsat.v10.types.ParticipantPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@MemberSubmissionAddressing
public class ParticipantPortTypePortImpl implements ParticipantPortType {
   @Resource
   private WebServiceContext m_context;

   public void prepare(Notification var1) {
      Participant var2 = this.getProxy();
      var2.prepare(var1);
   }

   public void commit(Notification var1) {
      Participant var2 = this.getProxy();
      var2.commit(var1);
   }

   public void rollback(Notification var1) {
      Participant var2 = this.getProxy();
      var2.rollback(var1);
   }

   protected Participant<Notification> getProxy() {
      return new Participant(this.m_context, WSATVersion.v10);
   }

   public String toString() {
      return "v10ParticipantPortTypePortImpl hashcode:" + this.hashCode() + " getProxy():" + this.getProxy() + "m_context:" + this.m_context + "m_context.getMessageContext:" + this.m_context.getMessageContext();
   }
}
