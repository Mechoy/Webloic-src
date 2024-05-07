package weblogic.wsee.wstx.wsat.v11.endpoint;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.endpoint.Participant;
import weblogic.wsee.wstx.wsat.v11.types.Notification;
import weblogic.wsee.wstx.wsat.v11.types.ParticipantPortType;

@WebService(
   portName = "ParticipantPort",
   serviceName = "WSAT11Service",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
   wsdlLocation = "/wsdls/wsat11/wstx-wsat-1.1-wsdl-200702.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsat.v11.types.ParticipantPortType"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@Addressing
public class ParticipantPortImpl implements ParticipantPortType {
   @Resource
   private WebServiceContext m_context;

   public void prepareOperation(Notification var1) {
      Participant var2 = this.getPoxy();
      var2.prepare(var1);
   }

   public void commitOperation(Notification var1) {
      Participant var2 = this.getPoxy();
      var2.commit(var1);
   }

   public void rollbackOperation(Notification var1) {
      Participant var2 = this.getPoxy();
      var2.rollback(var1);
   }

   protected Participant<Notification> getPoxy() {
      return new Participant(this.m_context, WSATVersion.v11);
   }
}
