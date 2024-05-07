package weblogic.common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.common.internal.BootServicesStub;
import weblogic.common.internal.T3ClientParams;
import weblogic.kernel.Kernel;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.MsgAbbrevOutputStream;
import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.Response;
import weblogic.rjvm.ServerURL;
import weblogic.rmi.ConnectIOException;
import weblogic.rmi.MarshalException;
import weblogic.rmi.RemoteException;
import weblogic.rmi.UnmarshalException;
import weblogic.rmi.utils.Utilities;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.AssertionError;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.URLClassFinder;

public final class T3Client implements PeerGoneListener, ClientCallback {
   public static final int DISCONNECT_TIMEOUT_DEFAULT = -2;
   public static final int DISCONNECT_TIMEOUT_NEVER = -1;
   private T3Connection connection;
   private RJVM rjvm;
   private T3ClientParams cm;
   private int idleCallbackID;
   private Thread loginThread;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public T3ServicesDef services;
   private String workspace;
   private Vector disconnectListeners;
   private boolean disconnectEventSent;
   public static final int INVALID_CALLBACK_ID = -1;
   private ClassLoader loader;

   public T3ServicesDef getT3Services() {
      return this.services;
   }

   public RJVM private_getRJVM() {
      return this.rjvm;
   }

   public boolean isConnected() {
      return this.cm != null;
   }

   public T3Connection getT3Connection() {
      return this.connection;
   }

   public boolean timeTraceEnabled() {
      return false;
   }

   public boolean timeTraceEnable(PrintStream var1) {
      return false;
   }

   public T3Client(T3Connection var1, String var2) {
      this.loginThread = null;
      this.disconnectListeners = new Vector();
      this.disconnectEventSent = false;
      Kernel.ensureInitialized();
      this.connection = var1;
      this.workspace = var2;
      this.services = new T3ClientServices(this);
   }

   public T3Client(T3Connection var1) {
      this((T3Connection)var1, (String)null);
   }

   public T3Client(String var1, String var2, UserInfo var3) throws UnknownHostException, T3Exception, IOException {
      this(new T3Connection(var1, var3), var2);
   }

   public T3Client(String var1, String var2) throws UnknownHostException, T3Exception, IOException {
      this(var1, var2, (UserInfo)null);
   }

   public T3Client(String var1) throws UnknownHostException, T3Exception, IOException {
      this(var1, (String)null, (UserInfo)null);
   }

   public T3Client(String var1, UserInfo var2) throws UnknownHostException, T3Exception, IOException {
      this(var1, (String)null, var2);
   }

   public synchronized T3Client connect() throws IOException, T3Exception, T3ExecuteException, SecurityException {
      if (this.connection == null) {
         throw new T3Exception("Improperly initalized.");
      } else {
         Protocol var1 = ProtocolManager.getProtocolByName(this.connection.getProtocol());
         if (var1.isUnknown()) {
            throw new MalformedURLException("Unknown protocol: '" + this.connection.getProtocol() + "'");
         } else {
            this.rjvm = (new ServerURL(this.connection.getURL())).findOrCreateRJVM();
            this.rjvm.addPeerGoneListener(this);
            this.idleCallbackID = this.registerCallback(this);
            this.cm = null;

            try {
               BootServicesStub var2 = new BootServicesStub(this.rjvm, var1);
               this.cm = var2.findOrCreateClientContext(this.workspace, this.connection.getUser(), this.idleCallbackID);
               if (this.connection.getUser() instanceof AuthenticatedUser && SecurityServiceManager.isKernelIdentity(SecurityServiceManager.getASFromAU((AuthenticatedUser)((AuthenticatedUser)this.connection.getUser())))) {
                  this.cm.user = (AuthenticatedUser)((AuthenticatedUser)this.connection.getUser());
               }

               this.loginThread = Thread.currentThread();
               AuthenticatedSubject var3 = SecurityServiceManager.getASFromAU(this.cm.user);
               SecurityServiceManager.pushSubject(kernelId, var3);
            } finally {
               if (this.cm == null) {
                  this.shutdown(true);
               }

            }

            if (this.cm == null) {
               throw new T3Exception("Error attempting to initialize.");
            } else {
               if (this.cm.verbose) {
                  System.out.println("New WSID: " + this.cm.wsID);
                  System.out.println("Connected as user=" + this.cm.user);
               }

               return this;
            }
         }
      }
   }

   public void addDisconnectListener(DisconnectListener var1) {
      this.disconnectListeners.addElement(var1);
   }

   public void removeDisconnectListener(DisconnectListener var1) {
      this.disconnectListeners.removeElement(var1);
   }

   private synchronized void disconnectOccurred(DisconnectEvent var1) {
      if (!this.disconnectEventSent) {
         this.disconnectEventSent = true;
         Enumeration var2 = this.disconnectListeners.elements();

         while(var2.hasMoreElements()) {
            ((DisconnectListener)var2.nextElement()).disconnectOccurred(var1);
         }
      }

   }

   private void shutdown(boolean var1) {
      this.cm = null;
      if (var1 && this.rjvm != null) {
         this.rjvm.removePeerGoneListener(this);
      }

      this.unregisterCallback(this.idleCallbackID);
      this.rjvm = null;
   }

   public synchronized T3Client disconnect() throws IOException, T3Exception {
      if (!this.isConnected()) {
         System.out.println("Ignoring request to disconnect client that is already disconnected.");
         return this;
      } else {
         this.sendOneWay("XZZdisconnectZZX", (Object)null);
         this.shutdown(true);
         this.disconnectOccurred(new DisconnectEvent(this, "clean disconnect"));
         if (Thread.currentThread() != this.loginThread) {
            System.out.println("Warning: T3Client.disconnect() called in a different thread than the one connect was called in");
            return this;
         } else {
            SecurityServiceManager.popSubject(kernelId);
            return this;
         }
      }
   }

   public void peerGone(PeerGoneEvent var1) {
      this.shutdown(false);
      this.disconnectOccurred(new DisconnectEvent(this, "connection to peer went down"));
   }

   public void dispatch(Throwable var1, Object var2) {
      this.shutdown(true);
      this.disconnectOccurred(new DisconnectEvent(this, "idle time out from Server"));
   }

   public int getHardDisconnectTimeoutMins() {
      return !this.isConnected() ? 0 : this.cm.hardDisconnectTimeoutMins;
   }

   public synchronized T3Client setHardDisconnectTimeoutMins(int var1) throws RemoteException, T3Exception {
      if (!this.isConnected()) {
         throw new T3Exception("T3Client not connected");
      } else {
         T3ClientParams var10001 = this.cm;
         if (var1 < -2) {
            throw new T3Exception("Invalid timeout value: " + var1);
         } else {
            this.cm.rcc.setHardDisconnectTimeoutMins(var1);
            this.cm.hardDisconnectTimeoutMins = var1;
            return this;
         }
      }
   }

   public int getSoftDisconnectTimeoutMins() {
      return !this.isConnected() ? 0 : this.cm.softDisconnectTimeoutMins;
   }

   public synchronized T3Client setSoftDisconnectTimeoutMins(int var1) throws RemoteException, T3Exception {
      if (!this.isConnected()) {
         throw new T3Exception("T3Client not connected");
      } else {
         T3ClientParams var10001 = this.cm;
         if (var1 < -2) {
            throw new T3Exception("Invalid timeout value: " + var1);
         } else {
            this.cm.rcc.setSoftDisconnectTimeoutMins(var1);
            this.cm.softDisconnectTimeoutMins = var1;
            return this;
         }
      }
   }

   public int getIdleDisconnectTimeoutMins() {
      return !this.isConnected() ? 0 : this.cm.idleSoftDisconnectTimeoutMins;
   }

   public synchronized T3Client setIdleDisconnectTimeoutMins(int var1) throws RemoteException, T3Exception {
      if (!this.isConnected()) {
         throw new T3Exception("T3Client not connected");
      } else {
         T3ClientParams var10001 = this.cm;
         if (var1 < -2) {
            throw new T3Exception("Invalid timeout value: " + var1);
         } else {
            this.cm.rcc.setIdleDisconnectTimeoutMins(var1);
            this.cm.idleSoftDisconnectTimeoutMins = var1;
            return this;
         }
      }
   }

   public String getServerName() {
      return !this.isConnected() ? "" : this.cm.serverName;
   }

   public boolean getVerbose() {
      return !this.isConnected() ? false : this.cm.verbose;
   }

   public synchronized T3Client setVerbose(boolean var1) throws RemoteException {
      if (!this.isConnected()) {
         throw new ConnectIOException("T3Client not connected");
      } else {
         this.cm.rcc.setVerbose(var1);
         this.cm.verbose = var1;
         return this;
      }
   }

   public String toString() {
      return super.toString() + " - connection: '" + this.getT3Connection() + "', wsid: '" + (this.cm != null ? this.cm.wsID : "unconnected") + "'";
   }

   public void sendOneWay(String var1, Object var2) throws RemoteException {
      if (this.rjvm == null) {
         throw new ConnectIOException("T3Client not connected");
      } else {
         MsgAbbrevOutputStream var3 = null;

         try {
            var3 = this.rjvm.getRequestStream((ServerChannel)null);
            var3.writeAbbrevString(var1);
            var3.writeObjectWL(var2);
         } catch (IOException var6) {
            throw new MarshalException("Failed to marshal arguments", var6);
         }

         try {
            var3.sendOneWay(this.cm.ccID);
         } catch (java.rmi.RemoteException var5) {
            throw (RemoteException)Utilities.theOtherException(var5);
         }
      }
   }

   public Response sendRecvAsync(String var1, Object var2) throws RemoteException {
      if (this.rjvm == null) {
         throw new ConnectIOException("T3Client not connected");
      } else {
         MsgAbbrevOutputStream var3 = null;

         try {
            var3 = this.rjvm.getRequestStream((ServerChannel)null);
            var3.writeAbbrevString(var1);
            var3.writeObjectWL(var2);
         } catch (IOException var6) {
            throw new MarshalException("Failed to marshal arguments", var6);
         }

         try {
            return var3.sendRecv(this.cm.ccID);
         } catch (java.rmi.RemoteException var5) {
            throw (RemoteException)Utilities.theOtherException(var5);
         }
      }
   }

   public Object sendRecv(String var1, Object var2) throws T3Exception {
      Response var3 = this.sendRecvAsync(var1, var2);
      Throwable var4 = var3.getThrowable();
      if (var4 != null) {
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         if (var4 instanceof T3Exception) {
            throw (T3Exception)var4;
         }

         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         if (var4 instanceof Error) {
            throw (Error)var4;
         }

         if (var4 instanceof Exception) {
            throw new T3Exception("Exception in sendRecv", var4);
         }
      }

      WLObjectInput var5 = null;

      Object var6;
      try {
         var5 = var3.getMsg();
         var6 = var5.readObjectWL();
      } catch (IOException var16) {
         throw new UnmarshalException("reading payload failed: ", var16);
      } catch (ClassNotFoundException var17) {
         throw new UnmarshalException("reading payload failed: ", var17);
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var15) {
               throw new AssertionError(var15);
            }
         }

      }

      return var6;
   }

   private int registerCallback(ClientCallback var1) {
      if (this.rjvm == null) {
         return -1;
      } else {
         CallbackDispatcher var2 = new CallbackDispatcher(var1);
         int var3 = System.identityHashCode(var2);
         RJVMManager.getLocalRJVM().getFinder().put(var3, var2);
         return var3;
      }
   }

   private void unregisterCallback(int var1) {
      RJVMManager.getLocalRJVM().getFinder().remove(var1);
   }

   public Class loadClass(String var1) throws ClassNotFoundException {
      if (this.rjvm == null) {
         try {
            this.connect();
         } catch (Exception var4) {
            throw new ClassNotFoundException("Connection failure while trying to load class: '" + var1 + "'");
         }
      }

      if (this.loader == null) {
         String var2 = ChannelHelper.createCodebaseURL(this.rjvm.getID());
         URLClassFinder var3 = new URLClassFinder(var2);
         this.loader = new GenericClassLoader(var3);
      }

      return this.loader.loadClass(var1);
   }
}
