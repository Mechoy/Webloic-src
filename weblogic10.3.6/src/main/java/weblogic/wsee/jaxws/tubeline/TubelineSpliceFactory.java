package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

public interface TubelineSpliceFactory extends TubeFactory {
   void createSplice(ClientDispatchFactory var1, ClientTubeAssemblerContext var2);

   void createSplice(DispatchFactory var1, ServerTubeAssemblerContext var2);

   public interface ClientDispatchFactory extends DispatchFactory {
      <T> Dispatch<T> createDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3);

      <T> Dispatch<T> createPostSpliceDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3);
   }

   public interface DispatchFactory {
      <T> Dispatch<T> createDispatch(Class<T> var1, Service.Mode var2);

      <T> Dispatch<T> createResponseDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3);
   }
}
