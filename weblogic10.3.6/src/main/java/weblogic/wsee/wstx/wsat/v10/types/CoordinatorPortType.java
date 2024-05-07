package weblogic.wsee.wstx.wsat.v10.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "CoordinatorPortType",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface CoordinatorPortType {
   @WebMethod(
      operationName = "PreparedOperation",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/PreparedOperation"
   )
   @Oneway
   void preparedOperation(@WebParam(name = "Prepared",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "AbortedOperation",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/AbortedOperation"
   )
   @Oneway
   void abortedOperation(@WebParam(name = "Aborted",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "ReadOnlyOperation",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/ReadOnlyOperation"
   )
   @Oneway
   void readOnlyOperation(@WebParam(name = "ReadOnly",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "CommittedOperation",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/CommittedOperation"
   )
   @Oneway
   void committedOperation(@WebParam(name = "Committed",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "ReplayOperation",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wsat/ReplayOperation"
   )
   @Oneway
   void replayOperation(@WebParam(name = "Replay",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",partName = "parameters") Notification var1);
}
