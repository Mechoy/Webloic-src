package weblogic.wsee.jaxws.client.async;

import com.sun.xml.ws.api.message.Packet;
import javax.xml.ws.EndpointReference;

public interface AsyncResponseEndpoint {
   void addResponseProcessingCompletionListener(ResponseProcessingCompletionListener var1);

   void removeResponseProcessingCompletionListener(ResponseProcessingCompletionListener var1);

   EndpointReference getEndpointReference();

   public interface ResponseProcessingCompletionListener {
      Packet responseProcessingComplete(Packet var1);

      Packet responseProcessingFailed(Packet var1, Throwable var2);
   }
}
