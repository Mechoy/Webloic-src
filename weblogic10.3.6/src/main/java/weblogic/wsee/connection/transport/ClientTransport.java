package weblogic.wsee.connection.transport;

import java.io.IOException;
import weblogic.wsee.connection.ResponseListener;

public interface ClientTransport extends Transport {
   void connect(String var1, TransportInfo var2) throws IOException;

   void setResponseListener(ResponseListener var1);

   boolean isBlocking();

   void setConnectionTimeout(int var1);

   void setReadTimeout(int var1);
}
