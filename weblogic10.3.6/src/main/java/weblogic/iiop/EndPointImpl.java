package weblogic.iiop;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInput;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.UNKNOWN;
import org.omg.SendingContext.RunTime;
import weblogic.common.internal.PeerInfo;
import weblogic.corba.cos.naming.NamingContextImpl;
import weblogic.corba.cos.naming.RootNamingContextImpl;
import weblogic.corba.cos.transactions.OTSHelper;
import weblogic.corba.cos.transactions.PropagationContextImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.ClientSecurityContext;
import weblogic.iiop.csi.CompoundSecMechList;
import weblogic.iiop.csi.SASServiceContext;
import weblogic.iiop.csi.SecurityContext;
import weblogic.kernel.Kernel;
import weblogic.protocol.Identity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.extensions.DisconnectEventImpl;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.NotImplementedException;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RequestTimeoutException;
import weblogic.rmi.extensions.ServerDisconnectEventImpl;
import weblogic.rmi.extensions.server.Collectable;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.ReplyOnError;
import weblogic.rmi.internal.ServerReference;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.OutboundRequest;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.internal.PropagationContext;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.SyncKeyTable;
import weblogic.utils.collections.NumericKeyHashMap;
import weblogic.utils.collections.NumericKeyHashtable;
import weblogic.utils.io.Chunk;
import weblogic.work.WorkManagerFactory;

public class EndPointImpl implements EndPoint {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static final boolean DEBUG = false;
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugCategory debugMarshal = Debug.getCategory("weblogic.iiop.marshal");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private static final DebugLogger debugIIOPMarshal = DebugLogger.getDebugLogger("DebugIIOPMarshal");
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected final boolean secure;
   protected final ConnectionManager conMan;
   protected final Connection c;
   private int bootstrapFlags = 0;
   private int negotiatedRequestId = 0;
   private Object bootstrapLock = new Object();
   private long nextClientContextId = 0L;
   private int nextRequestID = 1;
   private int pendingCount = 0;
   private final NumericKeyHashtable fragmentTable = new NumericKeyHashtable();
   private final NumericKeyHashtable securityContextTable = new NumericKeyHashtable();
   private final NumericKeyHashMap statefulClientContextIdTable = new NumericKeyHashMap();
   private final HashMap statefulClientContextTable = new HashMap();
   private final SyncKeyTable pendingResponses = new SyncKeyTable();
   private final long creationTime;
   private RunTime codebase = null;
   private HostID hostID;
   private final HashMap disconnectListeners = new HashMap();

   private final void p(String var1) {
      System.err.println("<EndPointImpl(" + Integer.toHexString(this.hashCode()) + ")>: " + var1);
   }

   private final void pfull(String var1) {
      this.p("@" + System.currentTimeMillis() + ", connected to: " + this.c.getConnectionKey().toString() + ": " + var1);
   }

   protected boolean isSecure(Connection var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p(" +++ connection isSecure : " + var1.isSecure());
      }

      return var1.isSecure();
   }

   public boolean supportsForwarding() {
      return true;
   }

   public final Connection getConnection() {
      return this.c;
   }

   public final void setCodeSets(int var1, int var2) {
      this.c.setCodeSets(var1, var2);
   }

   public final int getWcharCodeSet() {
      return this.c.getWcharCodeSet();
   }

   public final int getCharCodeSet() {
      return this.c.getCharCodeSet();
   }

   public final boolean getFlag(int var1) {
      return this.c.getFlag(var1);
   }

   public final void setFlag(int var1) {
      this.c.setFlag(var1);
   }

   public final RunTime getRemoteCodeBase() {
      if (this.codebase == null && this.c.getRemoteCodeBase() != null && !this.c.getRemoteCodeBase().getProfile().getObjectKey().isLocalKey()) {
         try {
            IIOPReplacer.getIIOPReplacer();
            this.codebase = (RunTime)PortableRemoteObject.narrow(IIOPReplacer.resolveObject(this.c.getRemoteCodeBase()), RunTime.class);
         } catch (ClassCastException var2) {
            IIOPLogger.logBadRuntime(var2);
         } catch (IOException var3) {
            IIOPLogger.logBadRuntime(var3);
         }
      }

      return this.codebase;
   }

   public final void setRemoteCodeBase(IOR var1) {
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport(this + " setting remote codebase to " + var1);
      }

      this.c.setRemoteCodeBase(var1);
   }

   public PeerInfo getPeerInfo() {
      return this.c.getPeerInfo();
   }

   public void setPeerInfo(PeerInfo var1) {
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("peer is " + var1);
      }

      this.c.setPeerInfo(var1);
   }

   public EndPointImpl(Connection var1, ConnectionManager var2) {
      this.c = var1;
      this.secure = this.isSecure(var1);
      this.conMan = var2;
      this.creationTime = System.currentTimeMillis();
   }

   public final ConnectionManager getConnectionManager() {
      return this.conMan;
   }

   public SequencedRequestMessage getPendingResponse(int var1) {
      return (SequencedRequestMessage)this.pendingResponses.get(var1);
   }

   public SequencedRequestMessage removePendingResponse(int var1) {
      return (SequencedRequestMessage)this.pendingResponses.remove(var1);
   }

   public void registerPendingResponse(SequencedRequestMessage var1) {
      this.pendingResponses.put(var1);
   }

   private final Message createMsgFromStream(Chunk var1) throws IOException {
      IIOPInputStream var2 = new IIOPInputStream(var1, this.isSecure(), this);
      if (Kernel.getDebug().getDebugIIOP() || debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) {
         IIOPLogger.logDebugMarshal("received [" + this.getServerChannel().getProtocol() + "] from " + this.getConnection().getConnectionKey() + " on " + this.getServerChannel() + "\n" + var2.dumpBuf());
      }

      MessageHeader var3 = new MessageHeader(var2);
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("received " + var3.getMsgTypeAsString() + " message");
      }

      if (var3.getMinorVersion() > this.c.getMinorVersion()) {
         this.c.setMinorVersion(var3.getMinorVersion());
      }

      var2.setSupportsJDK13Chunking(var3.getMinorVersion() < 2);
      switch (var3.getMsgType()) {
         case 0:
            return new RequestMessage(this, var3, var2);
         case 1:
            return new ReplyMessage(this, var3, var2);
         case 2:
            return new CancelRequestMessage(this, var3, var2);
         case 3:
            return new LocateRequestMessage(this, var3, var2);
         case 4:
            return new LocateReplyMessage(this, var3, var2);
         case 5:
            return new CloseConnectionMessage(this, var3, var2);
         case 6:
            return new MessageErrorMessage(this, var3, var2);
         case 7:
            return new FragmentMessage(this, var3, var2);
         default:
            IIOPLogger.logGarbageMessage();
            throw new UnmarshalException("Recieved unknown message type: " + var3.getMsgType());
      }
   }

   public final void dispatch(Chunk var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.pfull("dispatch()");
      }

      Message var2 = null;

      try {
         var2 = this.createMsgFromStream(var1);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("Message type: " + var2.getMsgType());
         }

         this.handleMessage(var2);
      } catch (IOException var5) {
         this.handleProtocolException(var2, var5);
      } catch (SystemException var6) {
         this.handleProtocolException(var2, var6);
      } catch (RuntimeException var7) {
         this.handleProtocolException(var2, var7);
      } catch (Throwable var8) {
         UNKNOWN var4 = new UNKNOWN("Unhandled error: " + var8.getMessage());
         var4.initCause(var8);
         this.handleProtocolException(var2, var4);
      }

   }

   public void handleProtocolException(Message var1, Throwable var2) {
      if (debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) {
         IIOPLogger.logDebugMarshalError("got protocol exception", var2);
      }

      if (var1 == null) {
         this.gotExceptionReceiving(var2);
      } else {
         if (var1.isFragmented() || var1.getMsgType() == 7) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               this.p("message fragment error");
            }

            var1 = this.purgeFragment(var1);
         }

         switch (var1.getMsgType()) {
            case 0:
               this.replyErrorToOutstandingRequest((RequestMessage)var1, var2);
               return;
            case 1:
               this.notifySender((ReplyMessage)var1, var2);
               return;
            case 2:
            case 5:
            case 6:
            case 7:
            default:
               this.gotExceptionReceiving(var2);
               return;
            case 3:
            case 4:
               new ConnectionShutdownHandler(this.c, var2);
         }
      }
   }

   private final void replyErrorToOutstandingRequest(RequestMessage var1, Throwable var2) {
      ServiceContextList var3 = null;

      try {
         var3 = var1.getOutboundServiceContexts();
      } catch (Throwable var6) {
      }

      ReplyMessage var4 = new ReplyMessage(this, var1, var3, 0);
      OutboundResponseImpl var5 = new OutboundResponseImpl(this, var4);
      if (!(var2 instanceof SystemException) && !(var2 instanceof RemoteException)) {
         if (var2 instanceof Exception) {
            new ReplyOnError(var5, new MarshalException("GIOP protocol error", (Exception)var2));
         } else {
            new ReplyOnError(var5, new MarshalException("GIOP protocol error: " + var2.getMessage()));
         }
      } else {
         new ReplyOnError(var5, var2);
      }

   }

   private final OBJECT_NOT_EXIST replyNoSuchObject(String var1) {
      return new OBJECT_NOT_EXIST(var1, 1330446337, CompletionStatus.COMPLETED_NO);
   }

   final void notifySender(ReplyMessage var1, Throwable var2) {
      int var3 = var1.getRequestID();
      SequencedRequestMessage var4 = this.removePendingResponse(var3);
      if (var4 != null) {
         UnmarshalException var5 = new UnmarshalException("GIOP protocol failure");
         var5.detail = var2;
         var1.setThrowable(var5);
         var4.notify((Message)var1);
      }
   }

   private final void handleMessage(Message var1) throws IOException {
      if (var1.isFragmented()) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("message is fragmented");
         }

         this.collectFragment(var1);
      } else {
         this.processMessage(var1);
      }
   }

   final void processMessage(Message var1) throws IOException {
      switch (var1.getMsgType()) {
         case 0:
            this.handleIncomingRequest(var1);
            return;
         case 1:
            if (this.negotiatedRequestId >= 0) {
               synchronized(this.bootstrapLock) {
                  if (this.negotiatedRequestId >= 0 && this.negotiatedRequestId == var1.getRequestID()) {
                     this.setFlag(this.bootstrapFlags);
                     this.negotiatedRequestId = -1;
                  }
               }
            }

            this.handleIncomingResponse(var1);
            return;
         case 2:
            return;
         case 3:
            this.handleLocateRequest(var1);
            return;
         case 4:
            this.handleLocateReply(var1);
            return;
         case 5:
            this.handleCloseConnection(var1);
            return;
         case 6:
            if (Kernel.getDebug().getDebugIIOP()) {
               this.p("handling message error");
            }

            this.handleMessageError(var1);
            return;
         case 7:
            this.processFragment(var1);
            return;
         default:
            if (Kernel.getDebug().getDebugIIOP()) {
               this.p("handling unknown message");
            }

            this.gotExceptionReceiving(Utils.mapToCORBAException(new UnmarshalException("Unkown message type: " + var1.getMsgType())));
      }
   }

   final void gotExceptionReceiving(Throwable var1) {
      this.conMan.gotExceptionReceiving(this.c, var1);
   }

   private final void handleMessageError(Message var1) {
      new ConnectionShutdownHandler(this.c, new EOFException("GIOP protocol error"));
   }

   final void handleCloseConnection(Message var1) {
      new ConnectionShutdownHandler(this.c, new EOFException("Connection closed by peer"), false);
   }

   final void collectFragment(Message var1) throws IOException {
      int var3 = var1.getRequestID();
      ArrayList var4 = null;
      synchronized(this.fragmentTable) {
         var4 = (ArrayList)this.fragmentTable.get((long)var3);
         if (var4 == null) {
            if (var1.getMsgType() == 7) {
               throw new UnmarshalException("Message Fragment out of order");
            }

            var4 = new ArrayList();
            this.fragmentTable.put((long)var3, var4);
         } else if (var1.getMsgType() != 7) {
            throw new UnmarshalException("Message Fragment out of order");
         }
      }

      var4.add(var1);
      if (debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) {
         IIOPLogger.logDebugMarshal("collected fragment " + var4.size() + " for request " + var1.getRequestID());
      }

   }

   final void processFragment(Message var1) throws IOException {
      ArrayList var2 = (ArrayList)this.fragmentTable.remove((long)var1.getRequestID());
      var2.add(var1);
      var1 = (Message)var2.remove(0);
      IIOPInputStream var3 = var1.getInputStream();

      while(!var2.isEmpty()) {
         Message var4 = (Message)var2.remove(0);
         var3.addChunks(var4.getInputStream());
      }

      var1.flush();
      this.processMessage(var1);
   }

   final Message purgeFragment(Message var1) {
      ArrayList var2 = (ArrayList)this.fragmentTable.remove((long)var1.getRequestID());
      if (var2 == null) {
         return var1;
      } else {
         var1 = (Message)var2.remove(0);
         IIOPInputStream var3 = var1.getInputStream();

         try {
            while(!var2.isEmpty()) {
               Message var4 = (Message)var2.remove(0);
               var3.addChunks(var4.getInputStream());
            }
         } catch (Throwable var5) {
         }

         return var1;
      }
   }

   final void handleIncomingRequest(Message var1) throws IOException {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.pfull("handleIncomingRequest");
      }

      RequestMessage var2 = (RequestMessage)var1;
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("handleIncomingRequest:  GIOP 1." + var2.getMinorVersion() + " (Connection GIOP 1." + this.getMinorVersion());
      }

      ServiceContext var3 = var2.getServiceContext(15);
      SASServiceContext var4;
      if ((var3 = var2.getServiceContext(15)) != null) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("handling SAS request");
         }

         var4 = (SASServiceContext)var3;
         boolean var5 = var4.handleSASRequest(var2, this);
         if (var5) {
            return;
         }
      }

      var4 = null;

      InboundRequestImpl var11;
      ServerReference var12;
      try {
         var12 = null;
         ObjectKey var6 = var2.getObjectKey();
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("ObjectKey = " + var6);
         }

         if (var6.isWLSKey()) {
            if (!var6.isLocalKey() && var6.getObjectID() > 256) {
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  this.p("Request throws OBJECT_NOT_EXIST: " + var6);
               }

               throw this.replyNoSuchObject("Transient reference expired.");
            }

            var11 = new InboundRequestImpl(this, var2);
         } else {
            if (!var6.isNamingKey()) {
               IOR var13;
               if ((var13 = var6.getInitialReference()) != null) {
                  this.handleInitialReference(var2, var13);
                  return;
               }

               if (var6.isBootstrapKey()) {
                  this.handleBootstrapRequest(var2);
                  return;
               }

               if (var6.getTarget() == null) {
                  this.routeRequest(var2);
                  return;
               }

               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  this.p("Request gets object not found");
               }

               throw this.replyNoSuchObject("Object not exported");
            }

            if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("REQUEST: NameService");
            }

            if (this.supportsForwarding()) {
               this.handleNamingRequest(var2);
               return;
            }

            var11 = new InboundRequestImpl(this, var2, 8);
         }
      } catch (RemoteException var10) {
         throw this.replyNoSuchObject("Object not exported");
      }

      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("REQUEST(" + var2.getRequestID() + "): for object " + var11.getObjectID() + ", " + var2.getOperationName() + "()");
      }

      try {
         var12 = var11.getServerReference();
         if (var12 instanceof Collectable) {
            ((Collectable)var12).renewLease();
         }

         RuntimeMethodDescriptor var14 = var11.getRuntimeMethodDescriptor(var12.getDescriptor());
         if (var14 == null) {
            String var15 = var2.getOperationName();
            IIOPLogger.logMethodParseFailure(var15);
            if (!var11.isOneWay()) {
               new ReplyOnError(var11.getOutboundResponse(), new BAD_OPERATION("Could not dispatch to method name: " + var15, 0, CompletionStatus.COMPLETED_NO));
            }

            return;
         }

         if (var14.requiresTransaction()) {
            PropagationContextImpl var7 = (PropagationContextImpl)var2.getServiceContext(0);
            if (var7 != null && (var7.isNull() || IIOPService.txMechanism == 0)) {
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  this.p("null tx when one was required or not allowed.");
               }

               new ReplyOnError(var11.getOutboundResponse(), new UnmarshalException("A transaction is required or not allowed"));
               return;
            }
         }

         var11.registerAsPending();
         var12.dispatch(var11);
         if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("REQUEST(" + var2.getRequestID() + "): dispatched");
         }
      } catch (NoSuchObjectException var8) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            var8.printStackTrace();
         }

         if (!var11.isOneWay()) {
            new ReplyOnError(var11.getOutboundResponse(), this.replyNoSuchObject("No such oid: " + var11.getObjectID()));
         }
      } catch (RemoteException var9) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            var9.printStackTrace();
         }

         if (!var11.isOneWay()) {
            new ReplyOnError(var11.getOutboundResponse(), new INV_OBJREF("Could not dispatch to oid: " + var11.getObjectID(), 0, CompletionStatus.COMPLETED_NO));
         }
      }

   }

   final void routeRequest(RequestMessage var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.pfull("routeRequest()");
      }

      ObjectKey var2 = var1.getObjectKey();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("routeRequest(): route based on: " + var2);
      }

      Identity var3 = var2.getTransientIdentity();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("routeRequest(): identity = " + var3);
      }

      EndPoint var4 = EndPointManager.getRoute(var3);
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("got route... " + var4);
      }

      if (var4 == null) {
         throw this.replyNoSuchObject("Object not exported");
      } else {
         new MessageForwardingHandler(var4, var1, var3, this.getConnection().getConnectionKey());
      }
   }

   final void handleBootstrapRequest(RequestMessage var1) throws IOException {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("handleBoostrapRequest");
      }

      ReplyMessage var2 = new ReplyMessage(this, var1, var1.getOutboundServiceContexts(), 0);
      IIOPOutputStream var3 = var2.getOutputStream();
      var2.write(var3);
      String var4 = var1.getOperationName();
      if (var4.equals("get")) {
         String var5 = var1.getInputStream().read_string();
         if (var5.equals("NameService")) {
            ClusterServices var6 = ClusterServices.getServices();
            IOR var7 = null;
            if (this.supportsForwarding() && var6.getLocationForwardPolicy() != 0) {
               var7 = NamingContextImpl.getBootstrapIOR(var6.getNextMember());
            } else {
               var7 = RootNamingContextImpl.getRootNamingContext().getIOR();
            }

            var7.write(var3);
            if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("LOCATION_FORWARD(" + var1.getRequestID() + "): to " + var7);
            }
         } else {
            IOR var9 = InitialReferences.getInitialReference(var5);
            if (var9 == null) {
               throw this.replyNoSuchObject("Object not exported");
            }

            var9.write(var3);
         }
      } else if (var4.equals("list")) {
         String[] var8 = InitialReferences.getServiceList();
         var3.write_long(var8.length);

         for(int var10 = 0; var10 < var8.length; ++var10) {
            var3.write_string(var8[var10]);
         }
      }

      new ReplyHandler(this, var2);
   }

   private final void handleNamingRequest(RequestMessage var1) throws IOException {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("handleNamingRequest");
      }

      ClusterServices var2 = ClusterServices.getServices();
      IOR var3 = null;
      if (var2 != null && ClusterServices.getServices().getLocationForwardPolicy() != 0) {
         var3 = NamingContextImpl.getBootstrapIOR(var2.getNextMember());
      } else {
         var3 = RootNamingContextImpl.getRootNamingContext().getIOR();
      }

      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("LOCATION_FORWARD(" + var1.getRequestID() + "): to " + var3);
      }

      ReplyMessage var4 = new ReplyMessage(this, var1, var1.getOutboundServiceContexts(), var3);
      IIOPOutputStream var5 = var4.getOutputStream();
      var4.write(var5);
      new ReplyHandler(this, var4);
   }

   final void handleInitialReference(RequestMessage var1, IOR var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("handleInitialReference: LOCATION_FORWARD to: " + var2);
      }

      ReplyMessage var3 = new ReplyMessage(this, var1, var1.getOutboundServiceContexts(), var2);
      IIOPOutputStream var4 = var3.getOutputStream();
      var3.write(var4);
      new ReplyHandler(this, var3);
   }

   final void handleIncomingResponse(Message var1) throws IOException {
      ReplyMessage var2 = (ReplyMessage)var1;
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("handleIncomingResponse():  GIOP 1." + var2.getMinorVersion() + " (Connection GIOP 1." + this.getMinorVersion());
      }

      if (var2.getMinorVersion() < this.getMinorVersion()) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("dropping to GIOP 1." + var2.getMinorVersion() + " for " + this);
         }

         this.c.setMinorVersion(var2.getMinorVersion());
      }

      ForwardingContext var3 = (ForwardingContext)var2.getServiceContext(1111834889);
      if (var3 != null) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("forwarding response to: " + var3);
         }

         EndPoint var7 = EndPointManager.getForwardingDestination(var3.getConnectionKey());
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("got destination: " + var7);
         }

         new MessageForwardingHandler(var7, var2, (Identity)null, (ConnectionKey)null);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("done!");
         }

      } else {
         int var4 = var2.getRequestID();
         if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("REPLY(" + var4 + "): " + var2.getStatusAsString());
         }

         SequencedRequestMessage var5 = this.getPendingResponse(var4);
         PropagationContextImpl var6;
         if (var2.getServiceContext(0) == null && var5 != null && (var6 = (PropagationContextImpl)var5.getServiceContext(0)) != null) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               this.p("handleIncomingResponse(): adding tx context to reply");
            }

            var2.addServiceContext(var6);
         }

         var5.notify((Message)var2);
         this.removePendingResponse(var4);
      }
   }

   final void handleLocateReply(Message var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("handleLocateReply");
      }

      LocateReplyMessage var2 = (LocateReplyMessage)var1;
      int var3 = var2.getRequestID();
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("LOCATE_REPLY(" + var3 + ")");
      }

      LocateRequestMessage var4 = (LocateRequestMessage)this.removePendingResponse(var3);
      var4.notify(var2);
   }

   final void handleLocateRequest(Message var1) throws IOException {
      LocateRequestMessage var2 = (LocateRequestMessage)var1;
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("LOCATE_REQUEST(" + var2.getRequestID() + ")");
      }

      ObjectKey var3 = var2.getObjectKey();
      LocateReplyMessage var4;
      IOR var5;
      if (var3.isWLSKey()) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("got this one... " + var3.getObjectID() + " interface = " + var3.getInterfaceName() + " oid = " + var3.getObjectID());
         }

         var5 = null;
         if (!var3.isLocalKey()) {
            throw new AssertionError("LocateRequest for non-local object");
         }

         var5 = new IOR(var3.getInterfaceName(), var3);
         var4 = new LocateReplyMessage(var2, var5, 1);
      } else if (var3.isBootstrapKey()) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            this.p("got NameService request");
         }

         var5 = RootNamingContextImpl.getRootNamingContext().getIOR();
         var4 = new LocateReplyMessage(var2, var5, 2);
      } else {
         var5 = var3.getInitialReference();
         if (var5 != null) {
            var4 = new LocateReplyMessage(var2, var5, 2);
         } else {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               this.p("don't have this one... " + var3.getObjectID());
            }

            var4 = new LocateReplyMessage(var2, 0);
         }
      }

      var4.write(var4.getOutputStream());
      new ReplyHandler(this, var4);
   }

   public final void send(IIOPOutputStream var1) throws RemoteException {
      if (Kernel.getDebug().getDebugIIOP() || debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) {
         IIOPLogger.logDebugMarshal("send [" + this.getServerChannel().getProtocol() + "] to " + this.getConnection().getConnectionKey() + " on " + this.getServerChannel());
      }

      try {
         this.c.send(var1);
      } catch (IOException var3) {
         if (Kernel.getDebug().getDebugIIOP() || debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) {
            IIOPLogger.logSendFailure(var3);
         }

         this.conMan.handleExceptionSending(this.c, var3);
         throw new MarshalException("IOException while sending", var3);
      }
   }

   public final Message sendReceive(SequencedRequestMessage var1, int var2) throws RemoteException {
      this.registerPendingResponse(var1);
      if (var2 != 0 && this.negotiatedRequestId >= 0) {
         synchronized(this.bootstrapLock) {
            if (this.negotiatedRequestId == 0) {
               this.negotiatedRequestId = var1.getRequestID();
               this.bootstrapFlags = var2;
            }
         }
      }

      this.send(var1.getOutputStream());

      try {
         var1.waitForData();
      } catch (RequestTimeoutException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new UnmarshalException("Exception waiting for response", var6);
      } catch (Throwable var7) {
         throw new UnmarshalException("Throwable waiting for response (" + var7.getClass().getName() + ") " + var7.getMessage());
      }

      return var1.getReply();
   }

   public final Message sendReceive(SequencedRequestMessage var1) throws RemoteException {
      return this.sendReceive(var1, 0);
   }

   public final void sendDeferred(SequencedRequestMessage var1, int var2) throws RemoteException {
      this.registerPendingResponse(var1);
      if (var2 != 0 && this.negotiatedRequestId >= 0) {
         synchronized(this.bootstrapLock) {
            if (this.negotiatedRequestId == 0) {
               this.negotiatedRequestId = var1.getRequestID();
               this.bootstrapFlags = var2;
            }
         }
      }

      this.send(var1.getOutputStream());
   }

   public final void sendDeferred(SequencedRequestMessage var1) throws RemoteException {
      this.sendDeferred(var1, 0);
   }

   public boolean hasPendingResponses() {
      return this.pendingResponses.size() > 0 || this.pendingCount > 0;
   }

   public final synchronized void cleanupPendingResponses(Throwable var1) {
      Enumeration var2 = this.pendingResponses.elements();

      while(var2.hasMoreElements()) {
         ((SequencedRequestMessage)var2.nextElement()).notify(var1);
      }

      this.deliverHeartbeatMonitorListenerException(var1 instanceof Exception ? (Exception)var1 : new Exception(var1));
   }

   public final boolean isSecure() {
      return this.secure;
   }

   public final int getMinorVersion() {
      return this.c.getMinorVersion();
   }

   public void setMessageServiceContext(Message var1, ServiceContext var2) {
      if (var2 != null && this.getMinorVersion() > 0) {
         var1.addServiceContext(var2);
      }

   }

   public ServiceContext getMessageServiceContext(Message var1, int var2) {
      return var1.getServiceContext(var2);
   }

   public AuthenticatedSubject getSubject(RequestMessage var1) {
      ServiceContext var2 = null;
      if ((var2 = var1.getServiceContext(1111834882)) != null) {
         if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
            IIOPLogger.logDebugSecurity("getting user " + ((VendorInfoSecurity)var2).getUser() + " from vendor service context");
         }

         AuthenticatedUser var4 = ((VendorInfoSecurity)var2).getUser();
         return var4 == null ? SubjectUtils.getAnonymousSubject() : SecurityServiceManager.getASFromAUInServerOrClient(var4);
      } else {
         AuthenticatedSubject var3;
         if ((var2 = var1.getServiceContext(15)) != null) {
            var3 = ((SASServiceContext)var2).getSubject();
            if (var3 == null) {
               return SubjectUtils.getAnonymousSubject();
            } else {
               if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                  IIOPLogger.logDebugSecurity("getting subject " + var3 + " from CSIv2 service context");
               }

               return var3;
            }
         } else {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               this.p("getting user: " + this.c.getUser() + " from connection");
            }

            var3 = this.c.getUser();
            return var3 == null ? SubjectUtils.getAnonymousSubject() : var3;
         }
      }
   }

   public void setSubject(RequestMessage var1, AuthenticatedSubject var2) {
      if (var2 == null) {
         var2 = SubjectUtils.getAnonymousSubject();
      }

      if (SecurityServiceManager.isKernelIdentity(var2)) {
         if (this.getPeerInfo() != null && Kernel.isServer()) {
            var2 = SecurityServiceManager.sendASToWire(var2);
         } else {
            var2 = SubjectUtils.getAnonymousSubject();
         }
      }

      if (!SubjectUtils.isUserAnonymous(var2) || this.getPeerInfo() == null) {
         ClientSecurityContext var3 = this.getClientContext(var2);
         if (var3 == null || Kernel.isServer() && var3.needCredentials() && this.getPeerInfo() != null && getPasswordCredentials(var2) != null) {
            CompoundSecMechList var4 = var1.getMechanismListForRequest();
            if (var4 != null && var4.useSAS()) {
               if (SubjectUtils.isUserAnonymous(var2)) {
                  if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                     IIOPLogger.logDebugSecurity("making anonymous outbound call using CSIv2 for " + var2);
                  }

                  this.putSASClientContext(var1, var4, var2, false);
                  return;
               }

               PasswordCredential var5 = getPasswordCredentials(var2);
               if (var5 != null && (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled())) {
                  IIOPLogger.logDebugSecurity("found password credentials for " + var2 + " using CSIv2");
               }

               if (var5 != null || this.getPeerInfo() == null || !Kernel.isServer()) {
                  this.putSASClientContext(var1, var4, var2, var5 == null);
                  return;
               }
            } else if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
               IIOPLogger.logDebugSecurity("target does not support CSIv2");
            }

            if (var2 != null) {
               if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
                  IIOPLogger.logDebugSecurity("adding outbound vendor security context for " + var2);
               }

               synchronized(this.statefulClientContextTable) {
                  ClientSecurityContext var6 = new ClientSecurityContext(var2);
                  this.statefulClientContextTable.put(var2, var6);
                  this.setMessageServiceContext(var1, var6.getMessageInContext());
               }
            }
         } else {
            if (debugSecurity.isEnabled() || debugIIOPSecurity.isDebugEnabled()) {
               IIOPLogger.logDebugSecurity("adding client security context for " + var2);
            }

            var1.addServiceContext(var3.getMessageInContext());
         }

      }
   }

   private static final PasswordCredential getPasswordCredentials(AuthenticatedSubject var0) {
      PasswordCredential var1 = null;
      Set var3 = var0.getPrivateCredentials(kernelId, PasswordCredential.class);
      Iterator var4 = var3.iterator();
      if (var4.hasNext()) {
         var1 = (PasswordCredential)var4.next();
      }

      return var1;
   }

   public Object getInboundRequestTxContext(RequestMessage var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("getInboundRequestTxContext()");
      }

      final ServiceContext var2 = null;
      if ((var2 = var1.getServiceContext(1111834881)) != null) {
         return ((VendorInfoTx)var2).getTxContext();
      } else if ((var2 = var1.getServiceContext(0)) != null) {
         if (!Kernel.isServer()) {
            return var2;
         } else {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               this.p("getInboundRequestTxContext(" + var2 + ")");
            }

            try {
               Object var4 = SecurityServiceManager.runAs(kernelId, (AuthenticatedSubject)var1.getSubject(), new PrivilegedExceptionAction() {
                  public Object run() throws Exception {
                     return OTSHelper.importTransaction((PropagationContextImpl)var2, 0);
                  }
               });
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  this.p("getInboundResponseTxContext(): " + var4);
               }

               return var4;
            } catch (PrivilegedActionException var5) {
               IIOPLogger.logOTSError("JTA error while importing transaction", (XAException)var5.getException());
               throw new TRANSACTION_ROLLEDBACK(var5.getException().getMessage());
            }
         }
      } else {
         return this.c.getTxContext();
      }
   }

   public void setOutboundResponseTxContext(ReplyMessage var1, Object var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("setOutboundResponseTxContext(" + (var2 == null ? null : var2.getClass().getName()) + ")");
      }

      if (var2 != null && this.c.getTxContext() == null) {
         if (var2 instanceof PropagationContext) {
            try {
               if (IIOPService.txMechanism == 2) {
                  PropagationContext var3 = (PropagationContext)var2;
                  var3.getTransaction().setProperty("weblogic.transaction.protocol", "iiop");
                  this.setMessageServiceContext(var1, new VendorInfoTx(var3));
               } else {
                  this.setMessageServiceContext(var1, OTSHelper.exportTransaction((PropagationContext)((PropagationContext)var2), 1));
               }
            } catch (Throwable var4) {
               IIOPLogger.logOTSError("JTA error while exporting transaction", var4);
               throw new TRANSACTION_ROLLEDBACK(var4.getMessage());
            }
         } else if (var2 instanceof org.omg.CosTransactions.PropagationContext) {
            this.setMessageServiceContext(var1, new PropagationContextImpl((org.omg.CosTransactions.PropagationContext)var2));
         }
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("setOutboundResponseTxContext(): " + var2);
      }

   }

   public void setOutboundRequestTxContext(RequestMessage var1, Object var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("setOutboundRequestTxContext(" + (var2 == null ? null : var2.getClass().getName()) + ")");
      }

      if (var2 != null) {
         if (var2 instanceof PropagationContext) {
            try {
               if (IIOPService.txMechanism == 2) {
                  PropagationContext var3 = (PropagationContext)var2;
                  var3.getTransaction().setProperty("weblogic.transaction.protocol", "iiop");
                  this.setMessageServiceContext(var1, new VendorInfoTx(var3));
               } else {
                  this.setMessageServiceContext(var1, OTSHelper.exportTransaction((PropagationContext)((PropagationContext)var2), 0));
               }
            } catch (Throwable var4) {
               IIOPLogger.logOTSError("JTA error while exporting transaction", var4);
               throw new TRANSACTION_ROLLEDBACK(var4.getMessage());
            }
         } else if (var2 instanceof org.omg.CosTransactions.PropagationContext) {
            this.setMessageServiceContext(var1, new PropagationContextImpl((org.omg.CosTransactions.PropagationContext)var2));
         }
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("setOutboundRequestTxContext(): " + var2);
      }

   }

   public Object getInboundResponseTxContext(ReplyMessage var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("getInboundResponseTxContext()");
      }

      ServiceContext var2 = null;
      if ((var2 = var1.getServiceContext(1111834881)) != null) {
         return ((VendorInfoTx)var2).getTxContext();
      } else if ((var2 = var1.getServiceContext(0)) != null) {
         if (!Kernel.isServer()) {
            return var2;
         } else {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               this.p("getInboundResponseTxContext(" + var2 + ")");
            }

            try {
               Transaction var3 = OTSHelper.importTransaction((PropagationContextImpl)var2, 1);
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  this.p("getInboundResponseTxContext(): " + var3);
               }

               return var3;
            } catch (XAException var4) {
               IIOPLogger.logOTSError("JTA error while importing transaction", var4);
               throw new TRANSACTION_ROLLEDBACK(var4.getMessage());
            }
         }
      } else {
         return null;
      }
   }

   public final void putSASClientContext(RequestMessage var1, CompoundSecMechList var2, AuthenticatedSubject var3, boolean var4) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         this.p("putSASClientContext(" + var3 + "): adding SAS service context");
      }

      synchronized(this.statefulClientContextTable) {
         ClientSecurityContext var6 = (ClientSecurityContext)this.statefulClientContextTable.get(var3);
         if (var6 == null || var6.needCredentials() && getPasswordCredentials(var3) != null) {
            var6 = (new SASServiceContext(var2, var3, this)).getClientContext();
            var6.setNeedCredentials(var4);
            this.statefulClientContextTable.put(var3, var6);
            this.statefulClientContextIdTable.put(var6.getClientContextId(), var3);
         }

         var1.addServiceContext(var6.getMessageInContext());
      }
   }

   public final void removeSASClientContext(long var1) {
      synchronized(this.statefulClientContextTable) {
         AuthenticatedSubject var4 = (AuthenticatedSubject)this.statefulClientContextIdTable.remove(var1);
         if (var4 != null) {
            this.statefulClientContextTable.remove(var4);
         }

      }
   }

   public final void establishSASClientContext(long var1) {
      synchronized(this.statefulClientContextTable) {
         ClientSecurityContext var4 = (ClientSecurityContext)this.statefulClientContextTable.get(this.statefulClientContextIdTable.get(var1));
         if (var4 != null) {
            var4.contextEstablished();
         }

      }
   }

   public final ClientSecurityContext getClientContext(AuthenticatedSubject var1) {
      synchronized(this.statefulClientContextTable) {
         return (ClientSecurityContext)this.statefulClientContextTable.get(var1);
      }
   }

   public final synchronized long getNextClientContextId() {
      return ++this.nextClientContextId;
   }

   public synchronized int getNextRequestID() {
      return ++this.nextRequestID;
   }

   public synchronized void incrementPendingRequests() {
      ++this.pendingCount;
   }

   public synchronized void decrementPendingRequests() {
      --this.pendingCount;
   }

   public final SecurityContext putSecurityContext(long var1, SecurityContext var3) {
      return (SecurityContext)this.securityContextTable.put(var1, var3);
   }

   public final SecurityContext getSecurityContext(long var1) {
      return (SecurityContext)this.securityContextTable.get(var1);
   }

   public final SecurityContext removeSecurityContext(long var1) {
      return (SecurityContext)this.securityContextTable.remove(var1);
   }

   public HostID getHostID() {
      if (this.hostID == null) {
         ConnectionKey var1 = this.getConnection().getConnectionKey();
         this.hostID = new HostIDImpl(var1.getAddress(), var1.getPort());
      }

      return this.hostID;
   }

   public Channel getRemoteChannel() {
      return this.getConnection().getRemoteChannel();
   }

   public ServerChannel getServerChannel() {
      return this.getConnection().getChannel();
   }

   public boolean isDead() {
      return this.c == null || this.c.isDead();
   }

   public boolean isUnresponsive() {
      return this.isDead();
   }

   public OutboundRequest getOutboundRequest(RemoteReference var1, RuntimeMethodDescriptor var2, String var3) throws IOException {
      return null;
   }

   public OutboundRequest getOutboundRequest(RemoteReference var1, RuntimeMethodDescriptor var2, String var3, Protocol var4) throws IOException {
      return null;
   }

   public String getClusterURL(ObjectInput var1) {
      return null;
   }

   public boolean addDisconnectListener(Remote var1, DisconnectListener var2) {
      synchronized(this.disconnectListeners) {
         HeartbeatKey var4 = new HeartbeatKey(var1, var2);
         this.disconnectListeners.put(var4, var2);
         this.getConnection().setHeartbeatStub(var1);
         return true;
      }
   }

   public boolean removeDisconnectListener(Remote var1, DisconnectListener var2) {
      synchronized(this.disconnectListeners) {
         HeartbeatKey var4 = new HeartbeatKey(var1, var2);
         this.disconnectListeners.remove(var4);
         if (this.disconnectListeners.isEmpty()) {
            this.getConnection().setHeartbeatStub((Remote)null);
         }

         return true;
      }
   }

   public void disconnect() {
      throw new NotImplementedException("disconnect not implemented in IIOP");
   }

   public long getCreationTime() {
      return this.creationTime;
   }

   private void deliverHeartbeatMonitorListenerException(Exception var1) {
      synchronized(this.disconnectListeners) {
         Iterator var3 = this.disconnectListeners.values().iterator();

         while(var3.hasNext()) {
            DisconnectListener var4 = (DisconnectListener)var3.next();
            WorkManagerFactory.getInstance().getSystem().schedule(new HeartbeatMonitorExceptionHandler(var4, var1));
         }

         this.disconnectListeners.clear();
      }
   }

   private static class HeartbeatKey {
      Remote stub;
      DisconnectListener l;
      int hashCode;

      public HeartbeatKey(Remote var1, DisconnectListener var2) {
         this.stub = var1;
         this.l = var2;
         if (this.stub != null && this.l != null) {
            this.hashCode = this.stub.hashCode() ^ this.l.hashCode();
         } else {
            this.hashCode = super.hashCode();
         }

      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof HeartbeatKey)) {
            return false;
         } else {
            HeartbeatKey var2 = (HeartbeatKey)var1;
            return this.stub == var2.stub && this.l == var2.l;
         }
      }

      public int hashCode() {
         return this.hashCode;
      }
   }

   private class HeartbeatMonitorExceptionHandler implements Runnable {
      private DisconnectListener l;
      private Exception e;

      HeartbeatMonitorExceptionHandler(DisconnectListener var2, Exception var3) {
         this.l = var2;
         this.e = var3;
      }

      public final void run() {
         if (EndPointImpl.this.getRemoteCodeBase() != null) {
            ServerIdentity var1 = EndPointImpl.this.getConnection().getRemoteCodeBase().getProfile().getObjectKey().getTarget();
            if (var1 != null && !var1.isClient()) {
               this.l.onDisconnect(new ServerDisconnectEventImpl(this.e, var1.getServerName()));
               return;
            }
         }

         this.l.onDisconnect(new DisconnectEventImpl(this.e));
      }
   }
}
