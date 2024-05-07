package weblogic.iiop;

import java.rmi.RemoteException;
import org.omg.SendingContext.RunTime;
import weblogic.common.internal.PeerInfo;
import weblogic.iiop.csi.SecurityContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.utils.io.Chunk;

public interface EndPoint extends weblogic.rmi.spi.EndPoint {
   int getNextRequestID();

   void dispatch(Chunk var1);

   void send(IIOPOutputStream var1) throws RemoteException;

   Message sendReceive(SequencedRequestMessage var1, int var2) throws RemoteException;

   void sendDeferred(SequencedRequestMessage var1, int var2) throws RemoteException;

   Message sendReceive(SequencedRequestMessage var1) throws RemoteException;

   void sendDeferred(SequencedRequestMessage var1) throws RemoteException;

   void cleanupPendingResponses(Throwable var1);

   SequencedRequestMessage getPendingResponse(int var1);

   SequencedRequestMessage removePendingResponse(int var1);

   void registerPendingResponse(SequencedRequestMessage var1);

   boolean hasPendingResponses();

   void incrementPendingRequests();

   void decrementPendingRequests();

   void handleProtocolException(Message var1, Throwable var2);

   boolean isSecure();

   boolean supportsForwarding();

   int getMinorVersion();

   Connection getConnection();

   void setCodeSets(int var1, int var2);

   int getWcharCodeSet();

   int getCharCodeSet();

   RunTime getRemoteCodeBase();

   void setRemoteCodeBase(IOR var1);

   PeerInfo getPeerInfo();

   void setPeerInfo(PeerInfo var1);

   void setFlag(int var1);

   boolean getFlag(int var1);

   AuthenticatedSubject getSubject(RequestMessage var1);

   void setSubject(RequestMessage var1, AuthenticatedSubject var2);

   void setMessageServiceContext(Message var1, ServiceContext var2);

   ServiceContext getMessageServiceContext(Message var1, int var2);

   Object getInboundRequestTxContext(RequestMessage var1);

   void setOutboundResponseTxContext(ReplyMessage var1, Object var2);

   void setOutboundRequestTxContext(RequestMessage var1, Object var2);

   Object getInboundResponseTxContext(ReplyMessage var1);

   void removeSASClientContext(long var1);

   void establishSASClientContext(long var1);

   long getNextClientContextId();

   SecurityContext putSecurityContext(long var1, SecurityContext var3);

   SecurityContext getSecurityContext(long var1);

   SecurityContext removeSecurityContext(long var1);
}
