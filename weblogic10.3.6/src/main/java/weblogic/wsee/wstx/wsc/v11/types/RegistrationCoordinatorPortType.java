package weblogic.wsee.wstx.wsc.v11.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "RegistrationCoordinatorPortType",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface RegistrationCoordinatorPortType {
   @WebMethod(
      operationName = "RegisterOperation",
      action = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06/Register"
   )
   @Oneway
   void registerOperation(@WebParam(name = "Register",targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",partName = "parameters") RegisterType var1);
}
