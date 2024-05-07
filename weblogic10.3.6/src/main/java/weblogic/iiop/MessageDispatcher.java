package weblogic.iiop;

import java.io.IOException;
import weblogic.utils.io.Chunk;

public interface MessageDispatcher {
   void dispatch(Connection var1, Chunk var2);

   void gotExceptionReceiving(Connection var1, Throwable var2);

   void gotExceptionSending(Connection var1, IOException var2);
}
