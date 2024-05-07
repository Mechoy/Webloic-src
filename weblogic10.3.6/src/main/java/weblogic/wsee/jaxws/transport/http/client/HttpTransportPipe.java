package weblogic.wsee.jaxws.transport.http.client;

import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Codec;
import java.util.List;
import java.util.Map;

public class HttpTransportPipe extends com.sun.xml.ws.transport.http.client.HttpTransportPipe {
   public HttpTransportPipe(EndpointAddress var1, Codec var2, WSBinding var3) {
      super(var1, var2, var3);
   }

   protected HttpClientTransport getTransport(Packet var1, Map<String, List<String>> var2) {
      return new HttpClientTransport(var1, var2);
   }
}
