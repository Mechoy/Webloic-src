package weblogic.wtc.corba.internal;

import com.bea.core.jatmi.common.ntrace;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.wtc.gwt.TuxedoCorbaConnection;
import weblogic.wtc.gwt.TuxedoCorbaConnectionFactory;
import weblogic.wtc.jatmi.BEAObjectKey;
import weblogic.wtc.jatmi.CallDescriptor;
import weblogic.wtc.jatmi.TPException;

public final class ORBSocket extends Socket {
   private boolean dummyTcpNoDelay;
   private boolean dummyKeepAlive;
   private int dummyRecvBufSize;
   private int dummySendBufSize;
   private boolean dummyLingerEnabled;
   private int dummyLingerValue;
   private int dummyPort;
   private InetAddress dummyInetAddress;
   private int dummyTimeout;
   private ORBSocketInputStream inputStream;
   private ORBSocketOutputStream outputStream;
   private TuxedoCorbaConnection tuxConnection;
   private HashMap outstandingReqMap;

   public ORBSocket(String var1, int var2) throws UnknownHostException, IOException {
      this(InetAddress.getByName(var1), var2, true);
   }

   public ORBSocket(InetAddress var1, int var2) throws IOException {
      this(var1, var2, true);
   }

   private ORBSocket(InetAddress var1, int var2, boolean var3) throws IOException {
      boolean var4 = ntrace.isTraceEnabled(8);
      if (var4) {
         ntrace.doTrace("[/ORBSocket//" + var1 + "/" + var2 + "/" + var3);
      }

      this.dummyTcpNoDelay = false;
      this.dummyKeepAlive = false;
      this.dummyRecvBufSize = 2048;
      this.dummySendBufSize = 2048;
      this.dummyLingerEnabled = false;
      this.dummyLingerValue = 0;
      this.dummyInetAddress = var1;
      this.dummyPort = var2;
      this.dummyTimeout = 0;

      TuxedoCorbaConnectionFactory var5;
      try {
         InitialContext var7 = new InitialContext();
         var5 = (TuxedoCorbaConnectionFactory)var7.lookup("tuxedo.services.TuxedoCorbaConnection");
      } catch (NamingException var10) {
         throw new IOException("Could not get TuxedoCorbaConnectionFactory : " + var10);
      } catch (Exception var11) {
         throw new IOException("Could not get TuxedoCorbaConnectionFactory : " + var11);
      }

      try {
         this.tuxConnection = var5.getTuxedoCorbaConnection();
      } catch (TPException var8) {
         throw new IOException("Could not get TuxedoCorbaConnection : " + var8);
      } catch (Exception var9) {
         throw new IOException("Could not get TuxedoCorbaConnection : " + var9);
      }

      this.inputStream = new ORBSocketInputStream(this, this.tuxConnection);
      this.outputStream = new ORBSocketOutputStream(this, this.tuxConnection);
      this.outstandingReqMap = new HashMap();
      if (var4) {
         ntrace.doTrace("[/ORBSocket//50");
      }

   }

   public InetAddress getInetAddress() {
      return this.dummyInetAddress;
   }

   public InetAddress getLocalAddress() {
      try {
         return InetAddress.getLocalHost();
      } catch (UnknownHostException var2) {
         return null;
      }
   }

   public int getPort() {
      return this.dummyPort;
   }

   public int getLocalPort() {
      return 0;
   }

   public InputStream getInputStream() throws IOException {
      return this.inputStream;
   }

   public OutputStream getOutputStream() throws IOException {
      return this.outputStream;
   }

   public void setTcpNoDelay(boolean var1) throws SocketException {
      this.dummyTcpNoDelay = var1;
   }

   public boolean getTcpNoDelay() throws SocketException {
      return this.dummyTcpNoDelay;
   }

   public void setSoLinger(boolean var1, int var2) throws SocketException {
      this.dummyLingerEnabled = var1;
      this.dummyLingerValue = var2;
   }

   public int getSoLinger() throws SocketException {
      return this.dummyLingerEnabled ? this.dummyLingerValue : -1;
   }

   public synchronized void setSoTimeout(int var1) throws SocketException {
      this.dummyTimeout = var1;
   }

   public synchronized int getSoTimeout() throws SocketException {
      return this.dummyTimeout;
   }

   public synchronized void setSendBufferSize(int var1) throws SocketException {
   }

   public synchronized int getSendBufferSize() throws SocketException {
      return this.dummySendBufSize;
   }

   public synchronized void setReceiveBufferSize(int var1) throws SocketException {
      this.dummyRecvBufSize = var1;
   }

   public synchronized int getReceiveBufferSize() throws SocketException {
      return this.dummyRecvBufSize;
   }

   public void setKeepAlive(boolean var1) throws SocketException {
      this.dummyKeepAlive = var1;
   }

   public boolean getKeepAlive() throws SocketException {
      return this.dummyKeepAlive;
   }

   public synchronized void close() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocket/close/");
      }

      try {
         this.inputStream.close();
         this.outputStream.close();
         this.tuxConnection.tpterm();
      } catch (Exception var3) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocket/close/10/IOException" + var3);
            var3.printStackTrace();
         }

         throw new IOException("Could not close ORBSocket : " + var3);
      }

      if (var1) {
         ntrace.doTrace("[/ORBSocket/close/50");
      }

   }

   public void shutdownInput() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocket/shutdownInput/");
      }

      try {
         this.inputStream.close();
      } catch (Exception var3) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocket/shutdownInput/10/IOException" + var3);
         }

         throw new IOException("Could not close ORBSocketInputStream : " + var3);
      }

      if (var1) {
         ntrace.doTrace("[/ORBSocket/shutdownInput/50");
      }

   }

   public void shutdownOutput() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocket/shutdownOutput/");
      }

      try {
         this.outputStream.close();
      } catch (Exception var3) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocket/shutdownOutput/10/IOException" + var3);
         }

         throw new IOException("Could not close ORBSocketOutputStream : " + var3);
      }

      if (var1) {
         ntrace.doTrace("[/ORBSocket/shutdownOutput/50");
      }

   }

   public void addOutstandingRequest(CallDescriptor var1, BEAObjectKey var2) {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORBSocket/addOutstandingRequest/" + var1 + "/" + var2);
      }

      synchronized(this.outstandingReqMap) {
         this.outstandingReqMap.put(var1, var2);
      }

      if (var3) {
         ntrace.doTrace("[/ORBSocket/addOutstandingRequest/50");
      }

   }

   public BEAObjectKey removeOutstandingRequest(CallDescriptor var1) {
      boolean var2 = ntrace.isTraceEnabled(8);
      if (var2) {
         ntrace.doTrace("[/ORBSocket/removeOutstandingRequest/" + var1);
      }

      BEAObjectKey var3 = null;
      synchronized(this.outstandingReqMap) {
         var3 = (BEAObjectKey)this.outstandingReqMap.remove(var1);
         if (var3 == null && var1 == null && !this.outstandingReqMap.isEmpty()) {
            Set var5 = this.outstandingReqMap.keySet();
            Iterator var6 = var5.iterator();
            var3 = (BEAObjectKey)this.outstandingReqMap.remove((CallDescriptor)var6.next());
         }
      }

      if (var2) {
         ntrace.doTrace("[/ORBSocket/removeOutstandingRequest/50/" + var3);
      }

      return var3;
   }

   public CallDescriptor getOriginalCallDescriptor(CallDescriptor var1) {
      boolean var2 = ntrace.isTraceEnabled(8);
      if (var2) {
         ntrace.doTrace("[/ORBSocket/getOriginalCallDescriptor/" + var1);
      }

      CallDescriptor var3 = null;
      synchronized(this.outstandingReqMap) {
         if (this.outstandingReqMap.containsKey(var1)) {
            Set var5 = this.outstandingReqMap.entrySet();
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               Map.Entry var7 = (Map.Entry)var6.next();
               CallDescriptor var8 = (CallDescriptor)var7.getKey();
               if (var8.equals(var1)) {
                  var3 = var8;
                  break;
               }
            }
         }
      }

      if (var2) {
         ntrace.doTrace("[/ORBSocket/getOriginalCallDescriptor/50/" + var3);
      }

      return var3;
   }

   public String toString() {
      return "Socket[addr=" + this.dummyInetAddress + ",port=" + this.dummyPort + "]";
   }
}
