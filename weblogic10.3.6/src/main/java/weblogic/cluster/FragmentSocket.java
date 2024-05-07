package weblogic.cluster;

import java.io.IOException;
import java.io.InterruptedIOException;

public interface FragmentSocket {
   int MAX_MULTICAST_BUF_SIZE = 65536;

   void start() throws IOException;

   void send(byte[] var1, int var2) throws IOException;

   int receive(byte[] var1) throws InterruptedIOException, IOException;

   void shutdown();

   long getFragmentsSentCount();

   long getFragmentsReceivedCount();

   void setPacketDelay(long var1);

   void shutdownPermanent();
}
