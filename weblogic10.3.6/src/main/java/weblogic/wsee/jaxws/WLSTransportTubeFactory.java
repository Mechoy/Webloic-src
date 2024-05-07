package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.TransportTube;
import com.sun.xml.ws.api.pipe.TransportTubeFactory;
import weblogic.wsee.jaxws.transport.http.client.HttpTransportPipe;

public class WLSTransportTubeFactory extends TransportTubeFactory {
   public TransportTube doCreate(ClientTubeAssemblerContext var1) {
      return this.createDefault(var1);
   }

   public TransportTube createHttpTransport(ClientTubeAssemblerContext var1) {
      return new HttpTransportPipe(var1.getAddress(), var1.getCodec(), var1.getBinding());
   }
}
