package weblogic.servlet.cluster.wan;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.security.AccessController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.sql.DataSource;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.subject.SubjectManager;
import weblogic.servlet.cluster.WANReplicationDetailsDebugLogger;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public class PersistenceServiceImpl implements PersistenceServiceInternal {
   private final String insertQuery;
   private final String updateQuery;
   private final String deleteQuery;
   private final PersistenceServiceInternal localService;
   static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int QUERY_TIMEOUT = 30;
   private final DataSource dataSource;

   public PersistenceServiceImpl(DataSource var1) {
      this.dataSource = var1;
      String var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getCluster().getWANSessionPersistenceTableName();
      this.insertQuery = " INSERT INTO " + var2 + " (wl_id, wl_create_time, wl_context_path, " + "wl_max_inactive_interval, wl_access_time, wl_version,  " + "wl_session_attribute_key, wl_session_attribute_value, wl_internal_attribute) " + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
      this.updateQuery = " UPDATE " + var2 + " set wl_access_time " + " = ?, wl_session_attribute_value = ?, wl_version = ? where wl_id = ? " + "AND wl_context_path = ? AND wl_session_attribute_key = ? AND " + "wl_internal_attribute = ? AND (? > (select max(wl_version) from " + var2 + " where wl_id = ? AND wl_context_path = ? AND wl_session_attribute_key = ?))";
      this.deleteQuery = "DELETE FROM " + var2 + " WHERE WL_ID = ? AND WL_CONTEXT_PATH = ? ";
      this.localService = new LocalPersistenceServiceInternal(this);
   }

   public PersistenceServiceInternal getLocalService() {
      return this.localService;
   }

   public void localPersistState(BatchedSessionState var1) throws ServiceUnavailableException {
      this.persistState(var1, false);
   }

   public void persistState(BatchedSessionState var1) throws ServiceUnavailableException {
      this.persistState(var1, false);
   }

   public void persistState(BatchedSessionState var1, boolean var2) throws ServiceUnavailableException {
      if (var2) {
         this.verifyCaller();
      }

      Update[] var3 = var1.getUpdates();
      int var4 = var3.length;
      if (var4 != 0) {
         Connection var5 = null;

         try {
            var5 = this.dataSource.getConnection();
         } catch (SQLException var16) {
            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Database unavailable", var16);
            }

            throw new ServiceUnavailableException("Database unavailabe");
         }

         try {
            SessionDiff[] var6 = new SessionDiff[var4];
            PreparedStatement var7 = null;
            var7 = this.createSessionsInDB(var5, var7, var4, var3, var6, var1);
            this.updateSession(var5, var7, var4, var6, var3, var1);
            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Persisted " + var3.length + " sessions to the database");
            }
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (SQLException var15) {
               }
            }

         }

      }
   }

   private PreparedStatement createSessionsInDB(Connection var1, PreparedStatement var2, int var3, Update[] var4, SessionDiff[] var5, BatchedSessionState var6) {
      try {
         var2 = var1.prepareStatement(this.insertQuery);

         for(int var7 = 0; var7 < var3; ++var7) {
            try {
               var5[var7] = var4[var7].getChange();
               HashMap var8 = var5[var7].getNewAttributes();
               this.insertAttributes(var8, var2, var4[var7], false, var5[var7]);
               var8 = var5[var7].getNewInternalAttributes();
               this.insertAttributes(var8, var2, var4[var7], true, var5[var7]);
            } catch (IOException var14) {
               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Failed to serialize new attributes for user session identified by " + var4[var7].getSessionID(), var14);
               }
            }
         }

         var2.executeBatch();
      } catch (SQLException var15) {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Failed while making bulk  insert. We will automatically attempt to fix this ", var15);
         }

         this.makeIndividualInsertCalls(var1, var6, var5);
      } finally {
         this.closeStatement(var2);
      }

      return var2;
   }

   private void updateSession(Connection var1, PreparedStatement var2, int var3, SessionDiff[] var4, Update[] var5, BatchedSessionState var6) {
      try {
         var2 = var1.prepareStatement(this.updateQuery);
         setQueryTimeout(var2, 30);

         for(int var7 = 0; var7 < var3; ++var7) {
            try {
               if (var4[var7] == null) {
                  var4[var7] = var5[var7].getChange();
               }

               HashMap var8 = var4[var7].getUpdateAttributes();
               this.updateAttributes(var8, var2, var5[var7], false, var4[var7]);
               var8 = var4[var7].getUpdateInternalAttributes();
               this.updateAttributes(var8, var2, var5[var7], true, var4[var7]);
            } catch (IOException var14) {
               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Failed to serialize updated attributes for user session identified by " + var5[var7].getSessionID(), var14);
               }
            }
         }

         var2.executeBatch();
      } catch (SQLException var15) {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Failed while making bulk  update. We will automatically attempt to fix this ", var15);
         }

         this.makeIndividualUpdateCalls(var1, var6, var4);
      } finally {
         this.closeStatement(var2);
      }

   }

   private void closeStatement(PreparedStatement var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
         }
      }

   }

   private void makeIndividualUpdateCalls(Connection var1, BatchedSessionState var2, SessionDiff[] var3) {
      Update[] var5 = var2.getUpdates();
      int var6 = var5.length;

      PreparedStatement var4;
      try {
         var4 = var1.prepareStatement(this.updateQuery);
      } catch (SQLException var12) {
         throw new AssertionError("Unexpected exception" + var12.toString());
      }

      try {
         for(int var7 = 0; var7 < var6; ++var7) {
            if (var3[var7] == null) {
               var3[var7] = var5[var7].getChange();
            }

            this.makeIndividualUpdateCall(var3[var7].getUpdateAttributes(), var5[var7], var3[var7], var4, false);
            this.makeIndividualUpdateCall(var3[var7].getUpdateInternalAttributes(), var5[var7], var3[var7], var4, true);
         }
      } finally {
         this.closeStatement(var4);
      }

   }

   private void makeIndividualUpdateCall(HashMap var1, Update var2, SessionDiff var3, PreparedStatement var4, boolean var5) {
      Iterator var6 = var1.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();

         try {
            var4.setLong(1, var2.getLastAccessTime());
            byte[] var8 = var3.getBytesForDB(var1.get(var7));
            var4.setBytes(2, var8);
            var4.setInt(3, var3.getVersionCount());
            var4.setString(4, var2.getSessionID());
            var4.setString(5, var2.getContextPath());
            var4.setString(6, var7);
            var4.setInt(7, var5 ? 1 : 0);
            var4.setInt(8, var3.getVersionCount());
            var4.setString(9, var2.getSessionID());
            var4.setString(10, var2.getContextPath());
            var4.setString(11, var7);
            setQueryTimeout(var4, 30);
            var4.execute();
         } catch (SQLException var9) {
            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Failed while making individual update call for user with session id " + var2.getSessionID(), var9);
            }
         } catch (IOException var10) {
            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Failed while making individual update call for user with session id " + var2.getSessionID(), var10);
            }
         }
      }

   }

   private void updateAttributes(HashMap var1, PreparedStatement var2, Update var3, boolean var4, SessionDiff var5) throws SQLException, IOException {
      Iterator var6 = var1.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         var2.setLong(1, var3.getLastAccessTime());
         byte[] var8 = var5.getBytesForDB(var1.get(var7));
         var2.setBytes(2, var8);
         var2.setInt(3, var5.getVersionCount());
         var2.setString(4, var3.getSessionID());
         var2.setString(5, var3.getContextPath());
         var2.setString(6, var7);
         var2.setInt(7, var4 ? 1 : 0);
         var2.setInt(8, var5.getVersionCount());
         var2.setString(9, var3.getSessionID());
         var2.setString(10, var3.getContextPath());
         var2.setString(11, var7);
         var2.addBatch();
      }

   }

   public void localInvalidateSessions(Set var1) throws RemoteException {
      this.invalidateSessions(var1, false);
   }

   public void invalidateSessions(Set var1) throws RemoteException {
      this.invalidateSessions(var1, true);
   }

   private void invalidateSessions(Set var1, boolean var2) throws RemoteException {
      if (var2) {
         this.verifyCaller();
      }

      Connection var3 = null;

      try {
         var3 = this.dataSource.getConnection();
      } catch (SQLException var17) {
         throw new ServiceUnavailableException("Database unavailabe");
      }

      PreparedStatement var4 = null;

      try {
         var4 = var3.prepareStatement(this.deleteQuery);
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            Invalidate var6 = (Invalidate)var5.next();
            var4.setString(1, var6.getSessionID());
            var4.setString(2, var6.getContextPath());
            var4.addBatch();
         }

         var4.executeBatch();
      } catch (SQLException var18) {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Failed to invalidate some  sessions. The server will perform auto recovery", var18);
         }

         this.makeIndividualDeleteCalls(var3, var1);
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (SQLException var16) {
            }
         }

         this.closeStatement(var4);
      }

   }

   private void insertAttributes(HashMap var1, PreparedStatement var2, Update var3, boolean var4, SessionDiff var5) throws SQLException, IOException {
      Iterator var6 = var1.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         var2.setString(1, var3.getSessionID());
         var2.setLong(2, var3.getCreationTime());
         var2.setString(3, var3.getContextPath());
         var2.setInt(4, var3.getMaxInactiveTime());
         var2.setLong(5, var3.getLastAccessTime());
         var2.setInt(6, var5.getVersionCount());
         var2.setString(7, var7);
         byte[] var8 = var5.getBytesForDB(var1.get(var7));
         var2.setBinaryStream(8, new UnsyncByteArrayInputStream(var8), var8.length);
         var2.setInt(9, var4 ? 1 : 0);
         var2.addBatch();
      }

   }

   private void makeIndividualDeleteCalls(Connection var1, Set var2) {
      PreparedStatement var3 = null;

      try {
         var3 = var1.prepareStatement(this.deleteQuery);
      } catch (SQLException var12) {
         throw new AssertionError("Unexpected exception" + var12.toString());
      }

      try {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Invalidate var5 = (Invalidate)var4.next();

            try {
               var3.setString(0, var5.getSessionID());
               var3.setString(1, var5.getContextPath());
               var3.execute();
            } catch (SQLException var13) {
               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Failed to invalidate  session " + var5.getSessionID(), var13);
               }
            }
         }
      } finally {
         this.closeStatement(var3);
      }

   }

   private void makeIndividualInsertCall(HashMap var1, Update var2, SessionDiff var3, PreparedStatement var4, boolean var5) {
      Iterator var6 = var1.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();

         try {
            var4.setString(1, var2.getSessionID());
            var4.setLong(2, var2.getCreationTime());
            var4.setString(3, var2.getContextPath());
            var4.setInt(4, var2.getMaxInactiveTime());
            var4.setLong(5, var2.getLastAccessTime());
            var4.setInt(6, var3.getVersionCount());
            var4.setString(7, var7);
            byte[] var8 = var3.getBytesForDB(var1.get(var7));
            var4.setBinaryStream(8, new UnsyncByteArrayInputStream(var8), var8.length);
            var4.setInt(9, var5 ? 1 : 0);
            var4.execute();
         } catch (SQLException var9) {
            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Failed during an individual insert calls to the database. This is normal as the remote  call is idempotent", var9);
            }
         } catch (IOException var10) {
            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Failed during an individual insert calls to the database. This is normal as the remote  call is idempotent", var10);
            }
         }
      }

   }

   private void makeIndividualInsertCalls(Connection var1, BatchedSessionState var2, SessionDiff[] var3) {
      Update[] var5 = var2.getUpdates();
      int var6 = var5.length;

      PreparedStatement var4;
      try {
         var4 = var1.prepareStatement(this.insertQuery);
      } catch (SQLException var12) {
         throw new AssertionError("Unexpected exception" + var12.toString());
      }

      try {
         for(int var7 = 0; var7 < var6; ++var7) {
            if (var3[var7] == null) {
               var3[var7] = var5[var7].getChange();
            }

            this.makeIndividualInsertCall(var3[var7].getNewAttributes(), var5[var7], var3[var7], var4, false);
            this.makeIndividualInsertCall(var3[var7].getNewInternalAttributes(), var5[var7], var3[var7], var4, true);
         }
      } finally {
         this.closeStatement(var4);
      }

   }

   private static void setQueryTimeout(PreparedStatement var0, int var1) {
      try {
         var0.setQueryTimeout(var1);
      } catch (SQLException var3) {
      }

   }

   private void verifyCaller() {
      try {
         HostID var1 = ServerHelper.getClientEndPoint().getHostID();
         AuthenticatedSubject var2 = (AuthenticatedSubject)SubjectManager.getSubjectManager().getCurrentSubject(KERNEL_ID);
         if (var2 == null) {
            throw new SecurityException("Null user is not permitted to perform WAN session replication operations");
         } else {
            int var3 = RemoteDomainSecurityHelper.acceptRemoteDomainCall(var1, var2);
            if (var3 == 1) {
               throw new SecurityException("user " + var2.getName() + " is not " + "permitted to perform WAN session replication operations");
            }
         }
      } catch (ServerNotActiveException var4) {
         throw new SecurityException("operation not permitted");
      }
   }

   private final class LocalPersistenceServiceInternal implements PersistenceServiceInternal {
      PersistenceServiceImpl persistenceService;

      private LocalPersistenceServiceInternal(PersistenceServiceImpl var2) {
         this.persistenceService = var2;
      }

      public void invalidateSessions(Set var1) throws RemoteException {
         this.persistenceService.localInvalidateSessions(var1);
      }

      public void persistState(BatchedSessionState var1) throws ServiceUnavailableException, RemoteException {
         this.persistenceService.localPersistState(var1);
      }

      // $FF: synthetic method
      LocalPersistenceServiceInternal(PersistenceServiceImpl var2, Object var3) {
         this(var2);
      }
   }
}
