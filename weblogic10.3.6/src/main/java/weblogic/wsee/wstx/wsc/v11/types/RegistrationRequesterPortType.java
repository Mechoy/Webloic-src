package weblogic.wsee.wstx.wsc.v11.types;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
   name = "RegistrationRequesterPortType",
   targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06"
)
@SOAPBinding(
   parameterStyle = ParameterStyle.BARE
)
@XmlSeeAlso({ObjectFactory.class})
public interface RegistrationRequesterPortType {
   @WebMethod(
      operationName = "RegisterResponse",
      action = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06/RegisterResponse"
   )
   @Oneway
   void registerResponse(@WebParam(name = "RegisterResponse",targetNamespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",partName = "parameters") RegisterResponseType var1);
}
