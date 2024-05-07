package weblogic.jms.dotnet.t3.server.internal;

import java.io.IOException;
import java.rmi.RemoteException;
import weblogic.common.internal.PackageInfo;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.VersionInfo;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.dotnet.t3.server.JMSCSharp;
import weblogic.jms.dotnet.t3.server.spi.T3Connection;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionGoneListener;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandle;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.MsgAbbrevOutputStream;
import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.rjvm.RequestStream;
import weblogic.utils.io.ChunkedDataOutputStream;

public final class T3ConnectionImpl implements T3Connection, PeerGoneListener {
   private T3RJVM t3rjvm;
   private T3ConnectionHandleID id;
   private boolean closed = false;
   private boolean closedByClient = false;
   private PeerInfo peerInfo;
   private T3ConnectionGoneListener listener;

   public T3ConnectionImpl(T3RJVM var1, T3ConnectionHandleID var2, int var3, int var4, int var5, int var6, boolean var7) throws RemoteException {
      this.t3rjvm = var1;
      this.id = var2;
      this.peerInfo = new PeerInfo(var3, var4, var5, var6, var7, (PackageInfo[])null);
      String var8 = "" + this.peerInfo.getMajor() + "." + this.peerInfo.getMinor() + "." + this.peerInfo.getServicePack() + "." + this.peerInfo.getRollingPatch();
      if (!VersionInfo.theOne().compatible(var8)) {
         throw new IllegalArgumentException("Remote version is not compatible");
      }
   }

   public PeerInfo getPeerInfo() {
      return this.peerInfo;
   }

   public ChunkedDataOutputStream getRequestStream() throws IOException {
      this.check_close();
      MsgAbbrevOutputStream var1 = this.t3rjvm.getRJVM().getRequestStream(this.t3rjvm.getServerChannel());
      var1.writeByte(3);
      return (ChunkedDataOutputStream)var1;
   }

   public void send(ChunkedDataOutputStream var1) throws IOException {
      this.check_close();
      RequestStream var2 = (RequestStream)var1;

      try {
         var2.sendOneWay(100);
      } catch (RemoteException var5) {
         IOException var4 = new IOException(var5.toString());
         var4.initCause(var5.getCause());
         throw var4;
      }
   }

   public void setT3ConnectionGoneListener(T3ConnectionGoneListener var1) {
      boolean var2 = false;
      boolean var3 = false;
      synchronized(this) {
         var2 = this.closed;
         var3 = this.closedByClient;
         this.listener = var1;
      }

      if (var2 && !var3) {
         ((T3ConnectionHandle)var1).onPeerGone(new T3ConnectionGoneEventImpl(new IOException("Remote RJMV is closed already")));
      }

   }

   public void peerGone(PeerGoneEvent var1) {
      T3ConnectionGoneListener var2 = null;
      synchronized(this) {
         if (this.closed) {
            return;
         }

         this.closed = true;
         if (JMSDebug.JMSDotNetT3Server.isDebugEnabled()) {
            JMSDebug.JMSDotNetT3Server.debug("T3 connection [" + this.id.getValue() + "] is terminated");
         }

         try {
            this.t3rjvm.getRJVM().disconnect();
         } catch (Throwable var7) {
            if (JMSDebug.JMSDotNetT3Server.isDebugEnabled()) {
               JMSDebug.JMSDotNetT3Server.debug("Failed to disconnect RJVM.", var7);
            }
         }

         JMSCSharp.getInstance().remove_connection(this.id);
         var2 = this.listener;
         this.listener = null;
      }

      if (var2 != null) {
         try {
            var2.onPeerGone(new T3ConnectionGoneEventImpl(var1));
         } catch (Throwable var6) {
            if (JMSDebug.JMSDotNetT3Server.isDebugEnabled()) {
               JMSDebug.JMSDotNetT3Server.debug("Failed to notify listener. ", var6);
            }
         }
      }

   }

   public synchronized void shutdown() {
      if (!this.closed) {
         this.closedByClient = true;
         this.listener = null;
         this.peerGone((PeerGoneEvent)null);
      }
   }

   private synchronized void check_close() throws IOException {
      if (this.closed) {
         throw new IOException("T3 connection is closed");
      }
   }

   public synchronized boolean isClosed() {
      return this.closed;
   }

   public JVMID getRJVMId() {
      return this.t3rjvm.getRJVM().getID();
   }
}
