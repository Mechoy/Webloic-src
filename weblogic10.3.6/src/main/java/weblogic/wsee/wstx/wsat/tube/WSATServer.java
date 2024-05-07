package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.message.HeaderList;

public interface WSATServer {
   void doHandleRequest(HeaderList var1, TransactionalAttribute var2);

   void doHandleResponse(TransactionalAttribute var1);

   void doHandleException(Throwable var1);
}
