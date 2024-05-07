package weblogic.jms.dotnet.transport.t3client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.StringTokenizer;
import weblogic.jms.dotnet.transport.Transport;

public class T3Connection {
   private final T3ConnectionLock lock = new T3ConnectionLock();
   private static final boolean DEBUG = false;
   private static final SecureRandom RANDOM = new SecureRandom();
   static final byte T3_CONNECTION_DISPATCHER_ID_DEFAULT = 0;
   static final byte T3_CONNECTION_DISPATCHER_ID_TRANSPORT = 1;
   private static final byte REMOTE_INVOKE_HELLO_REQUEST = 1;
   private static final byte REMOTE_INVOKE_HELLO_RESPONSE = 2;
   private static final byte REMOTE_INVOKE_DISPATCH_REQUEST = 3;
   private static final byte REMOTE_INVOKE_FAILURE = 4;
   private static final int INIT = 0;
   private static final int LOGIN = 1;
   private static final int HELLO = 2;
   private static final int READY = 3;
   private static final int CLOSED = 4;
   private static final int CLIENT_RAWADDRESS = 0;
   private final Socket socket;
   private final OutputStream output;
   private final InputStream input;
   private final long transportId;
   private final T3PeerInfo lPeerInfo;
   private final T3PeerInfo rPeerInfo;
   private final T3JVMID lJvmId;
   private final T3JVMID rJvmId;
   private long scratchID;
   private final byte serviceId;
   private static final boolean SEQUENCE_ENABLED = false;
   private int nextSendSequenceId = 0;
   private int nextExpectedSequenceId = 0;
   private int state;
   private boolean inProgress = false;
   private final T3Header T3HEADER_IDENTIFY;
   private final T3Header T3HEADER_ONEWAY;
   private final T3Header T3HEADER_TWOWAY;
   private int backLog;
   private boolean hasWaiter;
   private MarshalWriterImpl first;
   private MarshalWriterImpl last;

   T3Connection(String var1, int var2, T3PeerInfo var3, byte var4) throws Exception {
      this.T3HEADER_IDENTIFY = new T3Header(T3.PROTOCOL_IDENTIFY_REQUEST_NO_PEERINFO, T3.PROTOCOL_QOS_ANY, T3.PROTOCOL_FLAG_JVMID, T3.PROTOCOL_RESPONSEID, T3.PROTOCOL_INVOKEID_NONE);
      this.T3HEADER_ONEWAY = new T3Header(T3.PROTOCOL_ONE_WAY, T3.PROTOCOL_QOS_ANY, T3.PROTOCOL_FLAG_NONE, T3.PROTOCOL_RESPONSEID_NONE, T3.PROTOCOL_INVOKEID_JMSCSHARP_SERVICE);
      this.T3HEADER_TWOWAY = new T3Header(T3.PROTOCOL_REQUEST, T3.PROTOCOL_QOS_ANY, T3.PROTOCOL_FLAG_NONE, T3.PROTOCOL_RESPONSEID, T3.PROTOCOL_INVOKEID_JMSCSHARP_SERVICE);
      this.scratchID = RANDOM.nextLong();
      this.lJvmId = new T3JVMID((byte)1, this.scratchID, var1, 0, (int[])null);
      this.rJvmId = new T3JVMID((byte)1, 0L, var1, 0, new int[]{var2, -1, -1, -1, -1, -1, -1});
      this.serviceId = var4;
      if (var3 == null) {
         this.lPeerInfo = T3PeerInfo.defaultPeerInfo;
      } else {
         this.lPeerInfo = var3;
      }

      this.state = 0;
      this.inProgress = false;
      this.socket = new Socket(var1, var2);
      this.output = this.socket.getOutputStream();
      this.input = this.socket.getInputStream();
      this.state = 1;
      this.inProgress = true;
      MarshalWriterImpl var5 = createLoginRequest(this.lPeerInfo);
      this.sendSocket(var5);
      this.rPeerInfo = processLoginResponse(this.input);
      MarshalWriterImpl var6 = createIdentifyRequest(this.lPeerInfo, this.lJvmId, this.rJvmId, this.T3HEADER_IDENTIFY);
      this.sendSocket(var6);
      processIdentifyResponse(this.input);
      this.state = 2;
      MarshalWriterImpl var7 = createHelloRequest(this.lPeerInfo, this.lJvmId, var4, this.T3HEADER_TWOWAY);
      this.sendSocket(var7);
      this.transportId = processHelloResponse(this.input);
      this.state = 3;
      this.inProgress = false;
   }

   void sendSocket(MarshalWriterImpl var1) throws IOException {
      var1.copyTo(this.output);
   }

   T3PeerInfo getLocalPeerInfo() {
      return this.lPeerInfo;
   }

   T3PeerInfo getRemotePeerInfo() {
      synchronized(this.lock) {
         return this.rPeerInfo;
      }
   }

   T3JVMID getLocalJVMID() {
      return this.lJvmId;
   }

   T3JVMID getRemoteJVMID() {
      return this.rJvmId;
   }

   void close() {
      synchronized(this.lock) {
         if (this.state == 4) {
            return;
         }

         this.state = 4;
      }

      System.out.println("Debug: Closing T3 Conn " + this);

      try {
         this.socket.close();
      } catch (Exception var3) {
      }

   }

   MarshalReaderImpl receiveOneWay(Transport var1) throws Exception {
      synchronized(this.lock) {
         if (this.state != 3 || this.inProgress) {
            throw new Exception("Connection failure, state = " + this.getStateAsString());
         }
      }

      T3Message var2 = processResponse(this.input, false, var1);
      MarshalReaderImpl var3 = var2.getPayload();

      try {
         if (var3 != null && var2.getBodyLength() >= 5) {
            byte var4 = var3.readByte();
            if (var4 != 3) {
               throw new Exception("Unknown connection opcode " + var4);
            } else {
               return var3;
            }
         } else {
            throw new Exception("Unknown connection message syntax");
         }
      } catch (Exception var5) {
         var2.cleanup();
         throw var5;
      }
   }

   private static T3Message processResponse(InputStream var0, boolean var1) throws Exception {
      return processResponse(var0, var1, (Transport)null);
   }

   private static T3Message processResponse(InputStream var0, boolean var1, Transport var2) throws Exception {
      T3Message var3 = T3Message.readT3Message(var0, var2);
      return var3;
   }

   static MarshalWriterImpl createOneWay(MarshalWriterImpl var0) {
      if (var0 == null) {
         var0 = new MarshalWriterImpl();
      }

      var0.skip(19);
      var0.skip(9);
      return var0;
   }

   void sendOneWay(MarshalWriterImpl var1) throws Exception {
      MarshalWriterImpl var2 = null;
      synchronized(this.lock) {
         if (this.state != 3 || this.inProgress) {
            var1.closeInternal();
            throw new Exception("Connection is not ready");
         }

         ++this.backLog;
         var1.setNext((MarshalWriterImpl)null);
         if (this.last != null) {
            this.last.setNext(var1);
            this.last = var1;
            if (this.backLog > 32) {
               this.hasWaiter = true;

               try {
                  this.lock.wait();
               } catch (InterruptedException var17) {
               }
            }

            return;
         }

         this.first = var1;
         this.last = var1;
         var2 = var1;
      }

      while(true) {
         int var3 = var2.getPosition();
         var2.setPosition(19);
         var2.write(3);
         var2.writeLong(this.transportId);
         var2.setPosition(var3);
         T3Message var4 = new T3Message(this.T3HEADER_ONEWAY, T3Abbrev.NULL_ABBREVS);
         var4.write(var2);
         boolean var15 = false;

         try {
            var15 = true;
            this.sendSocket(var2);
            var15 = false;
         } finally {
            if (var15) {
               var2.closeInternal();
               synchronized(this.lock) {
                  --this.backLog;
                  this.first = this.first.getNext();
                  if (this.first == null) {
                     this.last = null;
                     if (this.hasWaiter) {
                        this.lock.notifyAll();
                     }

                     return;
                  }

                  var2 = this.first;
               }
            }
         }

         var2.closeInternal();
         synchronized(this.lock) {
            --this.backLog;
            this.first = this.first.getNext();
            if (this.first == null) {
               this.last = null;
               if (this.hasWaiter) {
                  this.lock.notifyAll();
               }

               return;
            }

            var2 = this.first;
         }
      }
   }

   private static MarshalWriterImpl createLoginRequest(T3PeerInfo var0) {
      MarshalWriterImpl var1 = new MarshalWriterImpl();
      String var2 = T3.PROTOCOL_NAME + " " + var0.getVersion() + T3.PROTOCOL_DELIMITER + T3.PROTOCOL_ABBV + T3.PROTOCOL_ABBV_DELIMITER + T3.PROTOCOL_ABBV_SIZE + T3.PROTOCOL_DELIMITER + T3.PROTOCOL_HDR + T3.PROTOCOL_HDR_DELIMITER + T3.PROTOCOL_HDR_SIZE + T3.PROTOCOL_DELIMITER + T3.PROTOCOL_DELIMITER;
      int var3 = var2.length();
      byte[] var4 = new byte[var3];
      var2.getBytes(0, var3, var4, 0);
      var1.write(var4, 0, var4.length);
      return var1;
   }

   private static T3PeerInfo processLoginResponse(InputStream var0) throws Exception {
      String var1 = readLine(var0);
      StringTokenizer var2 = new StringTokenizer(var1, ":");
      if (var2.countTokens() < 2) {
         throw new Exception("Unknown Login response  " + var1);
      } else {
         String var3 = var2.nextToken();
         if (!var3.equalsIgnoreCase(T3.PROTOCOL_REPLY_OK)) {
            throw new Exception("Unknown Login response  " + var1);
         } else {
            var3 = var2.nextToken();
            T3PeerInfo var4 = new T3PeerInfo(var3);

            String var5;
            while((var5 = readLine(var0)) != null && var5.length() != 0) {
               checkConnectionParams(var5);
            }

            return var4;
         }
      }
   }

   private static MarshalWriterImpl createIdentifyRequest(T3PeerInfo var0, T3JVMID var1, T3JVMID var2, T3Header var3) throws Exception {
      MarshalWriterImpl var4 = new MarshalWriterImpl();
      var4.skip(19);
      var4.writeInt(T3.PROTOCOL_HEARTBEAT_DISABLE);
      var4.writeInt(0);
      var0.write(var4);
      T3Abbrev[] var5 = new T3Abbrev[]{new T3Abbrev(255, (T3JVMID)null), new T3Abbrev(256, var2), new T3Abbrev(256, var1)};
      T3Message var6 = new T3Message(var3, var5);
      var6.write(var4);
      return var4;
   }

   private static void processIdentifyResponse(InputStream var0) throws Exception {
      T3Message var1 = processResponse(var0, false);

      try {
         T3Header var2 = var1.getMessageHeader();
         if (var2.getCommand() != T3.PROTOCOL_IDENTIFY_RESPONSE_NO_PEERINFO) {
            throw new Exception("Unknown identify response " + var2.getCommand());
         }
      } finally {
         var1.cleanup();
      }

   }

   private static MarshalWriterImpl createHelloRequest(T3PeerInfo var0, T3JVMID var1, byte var2, T3Header var3) throws Exception {
      MarshalWriterImpl var4 = new MarshalWriterImpl();
      var4.skip(19);
      var4.write(1);
      var0.write(var4);
      var4.write(var2);
      var4.write(var1.getFlags());
      var4.writeLong(var1.getDifferentiator());
      var4.writeUTF(var1.getHostAddress());
      var4.writeInt(var1.getRawAddress());
      var4.writeInt(0);
      T3Message var5 = new T3Message(var3, T3Abbrev.NULL_ABBREVS);
      var5.write(var4);
      return var4;
   }

   private static long processHelloResponse(InputStream var0) throws Exception {
      T3Message var1 = processResponse(var0, false);
      MarshalReaderImpl var2 = var1.getPayload();

      long var6;
      try {
         byte var3 = var2.readByte();
         if (var3 != 2) {
            throw new Exception("Expect hello response but got " + var1.getMessageHeader().getCommand() + "," + var3);
         }

         long var4 = var2.readLong();
         var6 = var4;
      } finally {
         var1.cleanup();
      }

      return var6;
   }

   private static void checkConnectionParams(String var0) throws Exception {
      int var1;
      if (var0.charAt(0) == T3.PROTOCOL_ABBV.charAt(0) && var0.charAt(1) == T3.PROTOCOL_ABBV.charAt(1) && var0.charAt(2) == ':') {
         var1 = Integer.parseInt(var0.substring(3, var0.length()));
      } else {
         if (var0.charAt(0) != T3.PROTOCOL_HDR.charAt(0) || var0.charAt(1) != T3.PROTOCOL_HDR.charAt(1) || var0.charAt(2) != ':') {
            throw new Exception("Unknown connection parameters " + var0);
         }

         var1 = Integer.parseInt(var0.substring(3, var0.length()));
      }

   }

   private static String readLine(InputStream var0) throws IOException {
      char[] var1 = new char[128];
      int var2 = 0;
      int var3 = var0.read();

      while(true) {
         if (var3 == -1) {
            if (var2 == 0) {
               return null;
            }
            break;
         }

         if (var3 == 10) {
            if (var2 > 0 && var1[var2 - 1] == '\r') {
               --var2;
            }
            break;
         }

         if (var2 == var1.length) {
            char[] var4 = var1;
            var1 = new char[var2 + 128];
            System.arraycopy(var4, 0, var1, 0, var2);
         }

         var1[var2++] = (char)var3;
         var3 = var0.read();
      }

      return String.copyValueOf(var1, 0, var2);
   }

   public String getStateAsString() {
      synchronized(this.lock) {
         switch (this.state) {
            case 0:
               return "INIT";
            case 1:
               return "LOGIN";
            case 2:
               return "HELLO";
            case 3:
               return "READY";
            case 4:
               return "CLOSED";
            default:
               return "UNKNOWN";
         }
      }
   }

   public long getScratchID() {
      return this.scratchID;
   }

   public String toString() {
      return this.getStateAsString() + " " + (this.inProgress ? "In Progress" : "done");
   }
}
