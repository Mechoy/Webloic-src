package weblogic.wsee.ws.dispatch;

import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public abstract class Dispatcher {
   public abstract QName getOperationName();

   public abstract QName getPortName();

   public abstract QName getServiceName();

   public abstract WsdlBindingOperation getBindingOperation();

   public abstract Connection getConnection();

   public abstract WlMessageContext getContext();

   public abstract Map getInParams();

   public abstract void setInParams(Map var1);

   public abstract Map getOutParams();

   public abstract void setOutParams(Map var1);

   public abstract WsMethod getWsMethod();

   public abstract WsdlOperation getOperation();

   public abstract WsPort getWsPort();

   public abstract void setWsPort(WsPort var1);

   public abstract WsdlPort getWsdlPort();

   public abstract boolean isSOAP12();
}
