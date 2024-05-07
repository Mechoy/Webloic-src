package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.server.WSEndpoint;

public interface EndpointCreationInterceptor {
   void postCreateEndpoint(WSEndpoint var1);
}
