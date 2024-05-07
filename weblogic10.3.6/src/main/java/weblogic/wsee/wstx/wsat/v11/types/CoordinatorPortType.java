package weblogic.wsee.wstx.wsat.v11.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "CoordinatorPortType",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface CoordinatorPortType {
   @WebMethod(
      operationName = "PreparedOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Prepared"
   )
   @Oneway
   void preparedOperation(@WebParam(name = "Prepared",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "AbortedOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Aborted"
   )
   @Oneway
   void abortedOperation(@WebParam(name = "Aborted",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "ReadOnlyOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/ReadOnly"
   )
   @Oneway
   void readOnlyOperation(@WebParam(name = "ReadOnly",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);

   @WebMethod(
      operationName = "CommittedOperation",
      action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Committed"
   )
   @Oneway
   void committedOperation(@WebParam(name = "Committed",targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",partName = "parameters") Notification var1);
}
