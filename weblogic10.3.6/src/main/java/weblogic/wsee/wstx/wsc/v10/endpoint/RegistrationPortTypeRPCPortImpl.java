package weblogic.wsee.wstx.wsc.v10.endpoint;

import com.sun.xml.ws.developer.MemberSubmissionAddressing;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.XmlTypeAdapter;
import weblogic.wsee.wstx.wsc.v10.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.types.RegisterType;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationPortTypeRPC;

@WebService(
   portName = "RegistrationPortTypeRPCPort",
   serviceName = "RegistrationService_V10",
   targetNamespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
   wsdlLocation = "/wsdls/wsc10/wscoor.wsdl",
   endpointInterface = "weblogic.wsee.wstx.wsc.v10.types.RegistrationPortTypeRPC"
)
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@MemberSubmissionAddressing
public class RegistrationPortTypeRPCPortImpl implements RegistrationPortTypeRPC {
   @Resource
   private WebServiceContext m_context;

   public RegisterResponseType registerOperation(RegisterType var1) {
      RegistrationProxyImpl var2 = this.getProxy();
      BaseRegisterResponseType var3 = var2.registerOperation(XmlTypeAdapter.adapt(var1));
      return (RegisterResponseType)var3.getDelegate();
   }

   protected RegistrationProxyImpl getProxy() {
      return new RegistrationProxyImpl(this.m_context);
   }
}
