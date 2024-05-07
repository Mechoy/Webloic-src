package weblogic.jms.dotnet.t3.server;

import java.io.IOException;
import java.rmi.RemoteException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.dotnet.t3.server.internal.T3ConnectionHandleID;
import weblogic.jms.dotnet.t3.server.internal.T3ConnectionImpl;
import weblogic.jms.dotnet.t3.server.internal.T3RJVM;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandle;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandleFactory;
import weblogic.jms.dotnet.t3.server.spi.impl.T3ConnectionHandleFactoryImpl;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.RemoteInvokable;
import weblogic.rjvm.RemoteRequest;
import weblogic.rjvm.ReplyStream;
import weblogic.rmi.spi.InboundRequest;
import weblogic.utils.collections.NumericKeyHashMap;
import weblogic.utils.io.ChunkedDataInputStream;

public final class JMSCSharp implements RemoteInvokable {
   public static final byte REMOTE_INVOKE_HELLO_REQUEST = 1;
   public static final byte REMOTE_INVOKE_HELLO_RESPONSE = 2;
   public static final byte REMOTE_INVOKE_DISPATCH_REQUEST = 3;
   public static final byte REMOTE_INVOKE_FAILURE = 4;
   private static final byte REMOTE_INVOKE_FAILURE_UNKNOWN_OPCODE = 1;
   private static final byte REMOTE_INVOKE_FAILURE_DUPLICATE_RJVM = 2;
   private static final byte REMOTE_INVOKE_FAILURE_UNKNOWN_HANDLEID = 3;
   private static final int DEFAULT_SERVICE_ID = 0;
   private static JMSCSharp me = new JMSCSharp();
   private static T3ConnectionHandleFactory[] hFactories = new T3ConnectionHandleFactory[128];
   private final NumericKeyHashMap handles = new NumericKeyHashMap();
   private long handleID = 0L;

   private JMSCSharp() {
   }

   public static JMSCSharp getInstance() {
      return me;
   }

   public static synchronized void setT3ConnectionHandleFactory(int var0, T3ConnectionHandleFactory var1) {
      if (var0 <= 128 && var0 >= 0) {
         if (hFactories[var0] != null) {
            throw new IllegalArgumentException("Service id is used");
         } else {
            if (JMSDebug.JMSDotNetT3Server.isDebugEnabled()) {
               JMSDebug.JMSDotNetT3Server.debug("T3 service " + var0 + " is registered with " + var1);
            }

            hFactories[var0] = var1;
         }
      } else {
         throw new IllegalArgumentException("Service id must be in the range of 0 to 128");
      }
   }

   public void invoke(RemoteRequest var1) throws RemoteException {
      byte var2;
      try {
         var2 = var1.readByte();
      } catch (IOException var4) {
         throw new RemoteException("RemoteRequest: missing JMS .net client opcode");
      }

      switch (var2) {
         case 1:
            this.processHelloRequest(var1);
            break;
         case 3:
            this.dispatchRequest(var1);
            break;
         default:
            this.reportFailure((byte)1, var2);
      }

   }

   public void remove_connection(T3ConnectionHandleID var1) {
      synchronized(this.handles) {
         this.handles.remove(var1.getValue());
      }
   }

   private void reportFailure(byte var1, byte var2) {
      switch (var1) {
         case 1:
            System.out.println("REMOTE_INVOKE_FAILURE_UNKNOWN_OPCODE  " + var2);
            break;
         default:
            System.out.println("FailCode  " + var1 + " cmd " + var2);
      }

   }

   private void sendAndReportFailure(RemoteRequest var1, byte var2) {
      try {
         ReplyStream var3 = var1.getResponseStream();
         var3.writeByte(4);
         var3.writeByte(var2);
         var3.send();
      } catch (IOException var7) {
      } finally {
         this.reportFailure(var2, (byte)0);
      }

   }

   private void processHelloRequest(RemoteRequest var1) throws RemoteException {
      int var4;
      int var5;
      int var6;
      int var7;
      boolean var8;
      byte var9;
      try {
         var4 = var1.readInt();
         var5 = var1.readInt();
         var6 = var1.readInt();
         var7 = var1.readInt();
         var8 = var1.readBoolean();
         var9 = var1.readByte();
      } catch (IOException var15) {
         throw new RemoteException("RemoteRequest: T3 connection [hello] syntax error");
      }

      if (var9 <= 128 && var9 >= 0) {
         if (hFactories[var9] == null) {
            throw new RemoteException("T3 service " + var9 + " is empty");
         } else {
            T3RJVM var10 = this.getT3RJVM(var1);
            T3ConnectionHandleID var3 = this.getNextHandleID();
            T3ConnectionImpl var11 = new T3ConnectionImpl(var10, var3, var4, var5, var6, var7, var8);
            var10.getRJVM().addPeerGoneListener((T3ConnectionImpl)var11);
            T3ConnectionHandle var2 = hFactories[var9].createHandle(var11);
            ((T3ConnectionImpl)var11).setT3ConnectionGoneListener(var2);
            synchronized(this.handles) {
               if (!((T3ConnectionImpl)var11).isClosed()) {
                  var3.setHandle(var2);
                  var3.setConnection(var11);
                  this.handles.put(var3.getValue(), var3);
               }
            }

            if (JMSDebug.JMSDotNetT3Server.isDebugEnabled()) {
               JMSDebug.JMSDotNetT3Server.debug("T3 service id [" + var9 + "] hello request From " + var10.getRJVM() + ", assigned connection id " + var3.getValue());
            }

            try {
               ReplyStream var12 = var1.getResponseStream();
               var12.write(2);
               var12.writeLong(var3.getValue());
               var12.send();
            } catch (IOException var14) {
               throw new RemoteException("RemoteRequest: Failed to send [hello] response", var14);
            }
         }
      } else {
         throw new RemoteException("T3 service id should be in range of 0 to 128");
      }
   }

   private void dispatchRequest(RemoteRequest var1) throws RemoteException {
      long var3;
      try {
         var3 = var1.readLong();
      } catch (IOException var8) {
         throw new RemoteException(var8.toString(), var8);
      }

      T3ConnectionHandle var5;
      synchronized(this.handles) {
         T3ConnectionHandleID var2 = (T3ConnectionHandleID)this.handles.get(var3);
         if (var2 == null) {
            this.reportFailure((byte)3, (byte)0);
            return;
         }

         var5 = var2.getHandle();
      }

      var5.onMessage((ChunkedDataInputStream)var1);
   }

   private synchronized T3ConnectionHandleID getNextHandleID() {
      return new T3ConnectionHandleID(++this.handleID);
   }

   private T3RJVM getT3RJVM(RemoteRequest var1) throws RemoteException {
      JVMID var2 = new JVMID();

      try {
         var2.readExternal(var1);
      } catch (IOException var5) {
         throw new RemoteException("RemoteRequest: T3 connection [hello] syntax error", var5);
      } catch (ClassNotFoundException var6) {
         throw new RemoteException("RemoteRequest: Unknown JVMID syntax " + var6);
      }

      RJVM var3 = RJVMManager.getRJVMManager().findOrCreate(var2);
      ServerChannel var4 = ((InboundRequest)var1).getServerChannel();
      return new T3RJVM(var3, var4);
   }

   public int hashCode() {
      return 41;
   }

   static {
      setT3ConnectionHandleFactory(0, new T3ConnectionHandleFactoryImpl());
   }
}
