package weblogic.wsee.wstx;

import javax.transaction.Synchronization;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import javax.xml.ws.EndpointReference;
import weblogic.wsee.wstx.wsat.WSATException;

public interface TransactionServices {
   byte[] getGlobalTransactionId();

   byte[] enlistResource(XAResource var1, Xid var2) throws WSATException;

   void registerSynchronization(Synchronization var1, Xid var2) throws WSATException;

   int getExpires();

   Xid importTransaction(int var1, byte[] var2) throws WSATException;

   String prepare(byte[] var1) throws WSATException;

   void commit(byte[] var1) throws WSATException;

   void rollback(byte[] var1) throws WSATException;

   void replayCompletion(String var1, XAResource var2) throws WSATException;

   EndpointReference getParentReference(Xid var1);
}
