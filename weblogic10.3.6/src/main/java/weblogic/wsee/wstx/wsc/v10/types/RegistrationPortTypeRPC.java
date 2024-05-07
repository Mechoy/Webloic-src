package weblogic.wsee.wstx.wsc.v10.types;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "RegistrationPortTypeRPC",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface RegistrationPortTypeRPC {
   @WebMethod(
      operationName = "RegisterOperation",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wscoor/RegisterOperation"
   )
   @WebResult(
      name = "RegisterResponse",
      targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
      partName = "parameters"
   )
   RegisterResponseType registerOperation(@WebParam(name = "Register",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",partName = "parameters") RegisterType var1);
}
