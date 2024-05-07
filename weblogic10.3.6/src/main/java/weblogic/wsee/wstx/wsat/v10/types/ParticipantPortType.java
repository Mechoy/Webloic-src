package weblogic.wsee.wstx.wsat.v10.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "ParticipantPortType",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface ParticipantPortType {
   @WebMethod(
      operationName = "Prepare",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/Prepare"
   )
   @Oneway
   void prepare(@WebParam(name = "Prepare",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "Commit",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/Commit"
   )
   @Oneway
   void commit(@WebParam(name = "Commit",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "Rollback",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/Rollback"
   )
   @Oneway
   void rollback(@WebParam(name = "Rollback",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);
}
