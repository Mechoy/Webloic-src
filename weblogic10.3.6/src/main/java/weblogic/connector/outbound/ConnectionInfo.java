package weblogic.connector.outbound;

import com.bea.connector.diagnostic.ManagedConnectionType;
import com.bea.connector.diagnostic.TransactionInfoType;
import com.bea.connector.diagnostic.TransactionType;
import java.security.AccessController;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.resource.spi.DissociatableManagedConnection;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ValidatingManagedConnectionFactory;
import javax.transaction.SystemException;
import weblogic.common.ResourceException;
import weblogic.common.resourcepool.PooledResource;
import weblogic.common.resourcepool.PooledResourceInfo;
import weblogic.common.resourcepool.ResourceCleanupHandler;
import weblogic.connector.common.ConnectorDiagnosticImageSource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.Utils;
import weblogic.connector.external.TrackableConnection;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.connector.transaction.outbound.LocalTxConnectionHandler;
import weblogic.connector.transaction.outbound.TxConnectionHandler;
import weblogic.connector.transaction.outbound.XATxConnectionHandler;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.Transaction;

public class ConnectionInfo implements PooledResource, TrackableConnection {
   ConnectionHandler connectionHandler;
   private long creationTime = 0L;
   private boolean connectionUsed = false;
   private boolean enabled = true;
   private int sharingCount = 0;
   private long lastUsedTime = 0L;
   private int pushCount = 0;
   private String allocationCallStack;
   private boolean shareable;
   private static final String CLASS_NAME = "weblogic.connector.outbound.ConnectionInfo";
   private long creationDurationTime = 0L;
   private long reserveDurationTime = 0L;
   private long reserveTime = 0L;
   boolean hasError = false;
   PooledResourceInfo prInfo = null;
   private boolean destroyAfterRelease;

   ConnectionInfo() {
   }

   public synchronized void incrementSharingCount() {
      if (Debug.verbose) {
         Debug.enter(this, "incrementSharingCount() of " + this + "; old sharingCount:" + this.sharingCount);
      }

      ++this.sharingCount;
      if (Debug.verbose) {
         Debug.exit(this, "incrementSharingCount() of " + this + "; new sharingCount:" + this.sharingCount);
      }

   }

   public synchronized void resetSharingCount() {
      if (Debug.verbose) {
         Debug.enter(this, "resetSharingCount() of " + this + "; old sharingCount:" + this.sharingCount);
      }

      this.sharingCount = 0;
      if (Debug.verbose) {
         Debug.exit(this, "resetSharingCount() of " + this + "; new sharingCount:" + this.sharingCount);
      }

   }

   public synchronized void decrementSharingCount() {
      if (Debug.verbose) {
         Debug.enter(this, "decrementSharingCount() of " + this + "; old sharingCount:" + this.sharingCount);
      }

      --this.sharingCount;
      if (Debug.verbose) {
         Debug.exit(this, "decrementSharingCount() of " + this + "; new sharingCount:" + this.sharingCount);
      }

   }

   public void initialize() {
      this.creationTime = System.currentTimeMillis();
   }

   public void setup() {
   }

   public void enable() {
      this.enabled = true;
   }

   public PooledResourceInfo getPooledResourceInfo() {
      return this.prInfo;
   }

   public void setPooledResourceInfo(PooledResourceInfo var1) {
      this.prInfo = var1;
   }

   public void disable() {
      this.enabled = false;
   }

   public void connectionClosed() {
      if (Debug.verbose) {
         Debug.enter(this, "connectionClosed()");
      }

      Utils.startManagement();
      int var1 = this.decrementPushCount();

      try {
         if (var1 <= 0) {
            this.connectionHandler.dissociateHandles();
         }
      } finally {
         Utils.stopManagement();
         if (Debug.verbose) {
            Debug.exit(this, "connectionClosed()");
         }

      }

   }

   public void cleanup() throws ResourceException {
      if (Debug.verbose) {
         Debug.enter(this, "cleanup()");
      }

      this.sharingCount = 0;

      try {
         this.connectionHandler.cleanup();
      } catch (javax.resource.ResourceException var6) {
         throw new ResourceException(var6);
      } finally {
         if (Debug.verbose) {
            Debug.exit(this, "cleanup()");
         }

      }

   }

   public void destroy() {
      if (Debug.verbose) {
         Debug.enter(this, "destroy()");
      }

      Utils.startManagement();

      try {
         this.connectionHandler.destroy();
      } finally {
         Utils.stopManagement();
         if (Debug.verbose) {
            Debug.exit(this, "destroy()");
         }

      }

   }

   public int test() throws ResourceException {
      Utils.startManagement();

      byte var1;
      try {
         ManagedConnectionFactory var2 = this.connectionHandler.getPool().getManagedConnectionFactory();
         if (!(var2 instanceof ValidatingManagedConnectionFactory)) {
            String var16 = Debug.getExceptionMCFNotImplementValidatingMCF();
            if (Debug.isConnectionsEnabled()) {
               Debug.connections(var16);
            }

            throw new ResourceException(var16);
         }

         HashSet var3 = new HashSet();
         Set var4 = null;
         var3.add(this.connectionHandler.getManagedConnection());
         AuthenticatedSubject var5 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         String var7;
         try {
            var4 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().getInvalidConnections((ValidatingManagedConnectionFactory)var2, var3, var5);
         } catch (javax.resource.ResourceException var13) {
            var7 = Debug.getExceptionTestResourceException(this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var13, var5));
            if (Debug.isConnectionsEnabled()) {
               Debug.connections("Connection test failed: " + var7, var13);
            }

            throw new ResourceException(var7, var13);
         } catch (Throwable var14) {
            var7 = Debug.getExceptionTestNonResourceException(this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var14, var5));
            if (Debug.isConnectionsEnabled()) {
               Debug.connections("Connection test failed: " + var7, var14);
            }

            throw new ResourceException(var7, var14);
         }

         if (var4 != null && !var4.isEmpty()) {
            var1 = -1;
            this.hasError = true;
            if (Debug.isConnectionsEnabled()) {
               Debug.connections("Managed Connection " + this.connectionHandler.getManagedConnection() + " is not valid as reported by Adapter");
            }
         } else {
            var1 = 0;
         }
      } finally {
         Utils.stopManagement();
      }

      return var1;
   }

   public Object createConnectionHandle(SecurityContext var1) throws javax.resource.ResourceException {
      Object var2 = this.connectionHandler.createConnectionHandle(var1);
      if (var2 != null) {
         this.incrementSharingCount();
      }

      return var2;
   }

   static ConnectionInfo createConnectionInfo(ConnectionPool var0, String var1, ManagedConnection var2, SecurityContext var3) throws javax.resource.ResourceException {
      if (Debug.verbose) {
         Debug.enter("weblogic.connector.outbound.ConnectionInfo", "createConnectioInfo() for pool '" + var0.getName() + "' creating connection info/handler with " + var1 + " transaction support");
      }

      ConnectionInfo var4 = new ConnectionInfo();
      Object var5 = null;

      try {
         if (var1.equals("NoTransaction")) {
            var5 = new NoTxConnectionHandler(var2, var0, var3, var4);
         } else if (var1.equals("LocalTransaction")) {
            var5 = new LocalTxConnectionHandler(var2, var0, var3, var4);
         } else if (var1.equals("XATransaction")) {
            var5 = new XATxConnectionHandler(var2, var0, var3, var4);
         } else {
            String var6 = "Illegal value of transSupport passed to ConnectionHandlerImpl.createConnectionHandler(): " + var1;
            Debug.println("weblogic.connector.outbound.ConnectionInfo", ".createConnectionInfo() " + var6);
            Debug.throwAssertionError(var6);
         }

         var4.setConnectionHandler((ConnectionHandler)var5);
      } catch (javax.resource.ResourceException var11) {
         Debug.println("weblogic.connector.outbound.ConnectionInfo", ".createConnectionInfo() Failed to create " + var1 + " Connection Handler");
         throw var11;
      } finally {
         if (Debug.verbose) {
            Debug.exit("weblogic.connector.outbound.ConnectionInfo", "createConnectionInfo()");
         }

      }

      return var4;
   }

   void associateConnectionHandle(Object var1) throws javax.resource.ResourceException {
      this.connectionHandler.associateConnectionHandle(var1);
   }

   public void setUsed(boolean var1) {
      this.connectionUsed = var1;
   }

   public void setResourceCleanupHandler(ResourceCleanupHandler var1) {
   }

   public void setConnectionHandler(ConnectionHandler var1) {
      this.connectionHandler = var1;
   }

   public void setLastUsedTime(long var1) {
      this.lastUsedTime = var1;
   }

   public void setAllocationCallStack(String var1) {
      this.allocationCallStack = var1;
   }

   void setShareable(boolean var1) {
      this.shareable = var1;
   }

   public boolean isLocalTransactionInProgress() {
      boolean var1;
      if (this.connectionHandler instanceof TxConnectionHandler) {
         var1 = ((TxConnectionHandler)this.connectionHandler).isLocalTransactionInProgress();
      } else {
         var1 = false;
      }

      return var1;
   }

   public synchronized int getSharingCount() {
      return this.sharingCount;
   }

   public long getCreationTime() throws ResourceException {
      return this.creationTime;
   }

   public boolean getUsed() {
      return this.connectionUsed;
   }

   public ResourceCleanupHandler getResourceCleanupHandler() {
      return this.connectionHandler;
   }

   public ConnectionHandler getConnectionHandler() {
      return this.connectionHandler;
   }

   public long getLastUsedTime() {
      return this.lastUsedTime;
   }

   public String getLastUsageString() {
      String var1;
      if (this.lastUsedTime > 0L) {
         Date var2 = new Date(this.lastUsedTime);
         var1 = var2.toString();
      } else {
         var1 = Debug.getStringNever();
      }

      return var1;
   }

   public String getAllocationCallStack() {
      return this.allocationCallStack;
   }

   public boolean isInTransaction() {
      return this.connectionHandler.isInTransaction();
   }

   public boolean isBeingShared() {
      return this.sharingCount > 1;
   }

   public ManagedConnectionType getXMLBean(ConnectorDiagnosticImageSource var1) {
      ManagedConnection var2 = this.connectionHandler.getManagedConnection();
      ManagedConnectionType var3 = ManagedConnectionType.Factory.newInstance();
      var3.setHashcode(var2.hashCode());
      var3.setId(var2.toString());
      boolean var4 = var1 != null ? var1.timedout() : false;
      if (var4) {
         return var3;
      } else {
         TransactionInfoType var6;
         if (this.connectionHandler instanceof TxConnectionHandler) {
            Transaction var5 = (Transaction)((TxConnectionHandler)this.connectionHandler).getTransaction();
            if (var5 != null) {
               var6 = var3.getTransactionInfo();
               if (var6 == null) {
                  var6 = var3.addNewTransactionInfo();
               }

               String var7;
               try {
                  var7 = String.valueOf(var5.getStatus());
               } catch (SystemException var9) {
                  var7 = "Not Available";
               }

               TransactionType var8 = var6.addNewTransaction();
               var8.setId(var5.getXid().toString());
               var8.setStatus(var7);
               var8.setTransactionType("XATransaction");
               var8.setEnlistmentTime("Not Available");
            }
         }

         if (this.connectionHandler instanceof LocalTxConnectionHandler) {
            LocalTransaction var10 = ((LocalTxConnectionHandler)this.connectionHandler).getLocalTransaction();
            if (var10 != null) {
               var6 = var3.getTransactionInfo();
               if (var6 == null) {
                  var6 = var3.addNewTransactionInfo();
               }

               TransactionType var11 = var6.addNewTransaction();
               var11.setId(var10.toString());
               var11.setStatus("Started");
               var11.setTransactionType("LocalTransaction");
               var11.setEnlistmentTime("Not Available");
            }
         }

         return var3;
      }
   }

   boolean isShareable() {
      return this.shareable;
   }

   public String toString() {
      return "ConnectionInfo@" + this.hashCode() + " of pool " + this.connectionHandler.getPoolName();
   }

   public long getCreationDurationTime() {
      return this.creationDurationTime;
   }

   public long getReserveDurationTime() {
      return this.reserveDurationTime;
   }

   public long getReserveTime() {
      return this.reserveTime;
   }

   public void setCreationDurationTime(long var1) {
      this.creationDurationTime = var1;
   }

   public void setReserveDurationTime(long var1) {
      this.reserveDurationTime = var1;
   }

   public void setReserveTime(long var1) {
      this.reserveTime = var1;
   }

   public String getTransactionId() {
      String var1 = "";
      if (this.connectionHandler instanceof TxConnectionHandler) {
         Transaction var2 = (Transaction)((TxConnectionHandler)this.connectionHandler).getTransaction();
         if (var2 != null) {
            var1 = var2.getXid().toString();
         }
      }

      return var1;
   }

   public boolean hasError() {
      return this.hasError;
   }

   protected void pushConnection() {
      ManagedConnection var1 = this.getConnectionHandler().getManagedConnection();
      if (!this.getConnectionHandler().getPool().getCanUseProxy() && var1 instanceof DissociatableManagedConnection) {
         MethodInvocationHelper.pushConnectionObject(this);
         this.incrementPushCount();
      }

   }

   private int incrementPushCount() {
      synchronized(this) {
         if (Debug.verbose) {
            Debug.println("incrementPushCount(): increase from " + this.pushCount + " to " + (this.pushCount + 1));
         }

         return ++this.pushCount;
      }
   }

   private int decrementPushCount() {
      synchronized(this) {
         if (Debug.verbose) {
            Debug.println("decrementPushCount(): decrease from " + this.pushCount + " to " + (this.pushCount - 1));
         }

         return --this.pushCount;
      }
   }

   public int getPushCount() {
      return this.pushCount;
   }

   public String getGroupId() {
      return null;
   }

   public boolean needDestroyAfterRelease() {
      return this.destroyAfterRelease || this.hasError || this.connectionHandler != null && this.connectionHandler.shouldBeDiscard();
   }

   public void setDestroyAfterRelease() {
      this.destroyAfterRelease = true;
   }
}
