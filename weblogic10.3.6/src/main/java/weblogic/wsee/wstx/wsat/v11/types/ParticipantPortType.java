package weblogic.wsee.wstx.wsat.v11.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "ParticipantPortType",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface ParticipantPortType {
   @WebMethod(
      operationName = "PrepareOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Prepare"
   )
   @Oneway
   void prepareOperation(@WebParam(name = "Prepare",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "CommitOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Commit"
   )
   @Oneway
   void commitOperation(@WebParam(name = "Commit",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "RollbackOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Rollback"
   )
   @Oneway
   void rollbackOperation(@WebParam(name = "Rollback",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);
}
