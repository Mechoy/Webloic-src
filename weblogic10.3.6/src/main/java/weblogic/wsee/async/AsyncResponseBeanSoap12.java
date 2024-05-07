package weblogic.wsee.async;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import weblogic.jws.Binding;
import weblogic.jws.WLDeployment;
import weblogic.jws.WLHttpTransport;
import weblogic.jws.WLHttpsTransport;
import weblogic.jws.WLJmsTransport;

@WebService(
   name = "AsyncResponseServiceSoap12PortType",
   serviceName = "AsyncResponseServiceSoap12",
   targetNamespace = "http://www.bea.com/async/AsyncResponseServiceSoap12"
)
@SOAPBinding(
   style = Style.DOCUMENT,
   use = Use.LITERAL
)
@WLHttpTransport(
   portName = "AsyncResponseServiceSoap12",
   contextPath = "_async",
   serviceUri = "AsyncResponseServiceSoap12"
)
@WLHttpsTransport(
   portName = "AsyncResponseServiceSoap12Https",
   contextPath = "_async",
   serviceUri = "AsyncResponseServiceSoap12Https"
)
@WLJmsTransport(
   portName = "AsyncResponseServiceSoap12Jms",
   contextPath = "_async",
   serviceUri = "AsyncResponseServiceSoap12Jms"
)
@WLDeployment(
   deploymentListener = {"weblogic.wsee.async.AsyncResponseServiceDeploymentListener"}
)
@Binding(Binding.Type.SOAP12)
public class AsyncResponseBeanSoap12 extends AsyncResponseBean {
}
