package weblogic.wsee.ws;

import java.util.Iterator;
import weblogic.wsee.wsdl.WsdlPortType;

public abstract class WsEndpoint {
   public abstract WsService getService();

   public abstract WsdlPortType getPortType();

   public abstract Class getJwsClass();

   public abstract WsMethod getMethod(String var1);

   public abstract Iterator getMethods();

   public abstract Class getEndpointInterface();
}
