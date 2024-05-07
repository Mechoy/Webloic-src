package weblogic.wsee.wstx.wsc.v10.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "RegistrationRequesterPortType",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface RegistrationRequesterPortType {
   @WebMethod(
      operationName = "RegisterResponse",
      action = "http://schemas.xmlsoap.org/ws/2004/10/wscoor/RegisterResponse"
   )
   @Oneway
   void registerResponse(@WebParam(name = "RegisterResponse",targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",partName = "parameters") RegisterResponseType var1);
}
