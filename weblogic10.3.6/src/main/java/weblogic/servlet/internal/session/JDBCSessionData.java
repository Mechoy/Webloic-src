package weblogic.servlet.internal.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.j2ee.ApplicationManager;
import weblogic.logging.Loggable;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

class JDBCSessionData extends SessionData implements HttpSession {
   private static final long serialVersionUID = 7715646887169061115L;
   private static final boolean PERSIST_APPVERSION = !Boolean.getBoolean("weblogic.servlet.session.PersistentBackCompatibility");
   private static final Driver driver = new weblogic.jdbc.pool.Driver();
   private final String logContext;
   private final String contextName;
   private final String fullCtxName;
   private final Properties jdbcProps;
   protected final JDBCSessionContext jdbcCtx;
   private final DataSource dataSource;
   protected transient long dbLAT;
   private transient long triggerLAT;
   private transient boolean isCacheStale = false;

   public JDBCSessionData(String var1, SessionContext var2, DataSource var3, Properties var4, boolean var5) {
      super(var1, var2, var5);
      this.dataSource = var3;
      this.jdbcProps = var4;
      this.logContext = var2.getServletContext().getLogContext();
      this.jdbcCtx = (JDBCSessionContext)var2;
      this.contextName = this.getContextName();
      this.fullCtxName = var2.getServletContext().getFullCtxName();
   }

   static JDBCSessionData newSession(String var0, SessionContext var1, DataSource var2, Properties var3) {
      JDBCSessionData var4 = null;

      try {
         var4 = new JDBCSessionData(var0, var1, var2, var3, true);
         var4.dbCreate();
         var1.getServletContext().getEventsManager().notifySessionLifetimeEvent(var4, true);
         return var4;
      } catch (SQLException var6) {
         HTTPSessionLogger.logUnexpectedError(var1.getServletContext().getLogContext(), var6);
         return null;
      }
   }

   static JDBCSessionData getFromDB(String var0, SessionContext var1, DataSource var2, Properties var3) {
      JDBCSessionData var4 = null;

      try {
         var4 = dbRefresh(var0, var1, var2, var3);
         return var4;
      } catch (SessionNotFoundException var6) {
         return null;
      } catch (SQLException var7) {
         HTTPSessionLogger.logUnexpectedError(var1.getServletContext().getLogContext(), var7);
         return null;
      }
   }

   static String[] getSessionIds(DataSource var0, Properties var1, WebAppServletContext var2, String var3) {
      Connection var4 = null;
      PreparedStatement var5 = null;
      ResultSet var6 = null;
      ArrayList var7 = new ArrayList();

      label55: {
         String[] var9;
         try {
            var4 = getConnection(var0, var1);
            var5 = var4.prepareStatement(var3);
            var5.setString(1, var2.getName());
            var5.setString(2, var2.getFullCtxName());
            var6 = var5.executeQuery();

            while(true) {
               if (!var6.next()) {
                  break label55;
               }

               var7.add(var6.getString(1));
            }
         } catch (SQLException var14) {
            HTTPSessionLogger.logUnexpectedError(var2.getLogContext(), var14);
            var9 = new String[0];
         } finally {
            closeDBResources(var6, var5, var4);
         }

         return var9;
      }

      String[] var8 = new String[var7.size()];
      return (String[])((String[])var7.toArray(var8));
   }

   static String[] getWlCtxPaths(DataSource var0, Properties var1, WebAppServletContext var2, String var3, String var4) {
      Connection var5 = null;
      PreparedStatement var6 = null;
      ResultSet var7 = null;
      ArrayList var8 = new ArrayList();

      label55: {
         String[] var10;
         try {
            var5 = getConnection(var0, var1);
            var6 = var5.prepareStatement(var3);
            var6.setString(1, var4);
            var7 = var6.executeQuery();

            while(true) {
               if (!var7.next()) {
                  break label55;
               }

               var8.add(var7.getString(1));
            }
         } catch (SQLException var15) {
            HTTPSessionLogger.logUnexpectedError(var2.getLogContext(), var15);
            var10 = new String[0];
         } finally {
            closeDBResources(var7, var6, var5);
         }

         return var10;
      }

      String[] var9 = new String[var8.size()];
      return (String[])((String[])var8.toArray(var9));
   }

   static Connection getConnection(DataSource var0, Properties var1) throws SQLException {
      Connection var2 = null;

      try {
         if (var0 != null) {
            var2 = var0.getConnection();
         } else {
            var2 = driver.connect("jdbc:weblogic:pool", var1);
         }

         var2.setAutoCommit(true);
         return var2;
      } catch (SQLException var6) {
         try {
            var2.close();
         } catch (Exception var5) {
         }

         throw var6;
      }
   }

   protected void dbCreate() throws SQLException {
      if (this.isDebugEnabled()) {
         Loggable var1 = HTTPSessionLogger.logPerformOperationLoggable("dbCreate()", this.id, this.getContextPath());
         DEBUG_SESSIONS.debug(var1.getMessage());
      }

      Connection var10 = null;
      PreparedStatement var2 = null;

      try {
         if (var10 == null) {
            var10 = getConnection(this.dataSource, this.jdbcProps);
         }

         var2 = var10.prepareStatement(this.jdbcCtx.getInsertQuery());
         this.addCreateStatement(var2);
         if (var2.executeUpdate() != 1) {
            throw new SQLException("Failed to insert record for session " + this.id + " in the database");
         }

         this.dbLAT = this.accessTime;
      } catch (SQLException var8) {
         this.setValid(false);
         throw var8;
      } finally {
         this.setModified(false);
         closeDBResources((ResultSet)null, var2, var10);
      }

   }

   protected void addCreateStatement(PreparedStatement var1) throws SQLException {
      int var2 = 0;
      ++var2;
      var1.setString(var2, this.id);
      ++var2;
      var1.setString(var2, PERSIST_APPVERSION ? this.fullCtxName : this.contextName);
      ++var2;
      var1.setString(var2, this.isNew() ? "1" : "0");
      ++var2;
      var1.setLong(var2, this.creationTime);

      try {
         this.initVersionAttrsIfNeeded();
         byte[] var3 = this.serializeAttributes();
         ++var2;
         var1.setBinaryStream(var2, new ByteArrayInputStream(var3), var3.length);
      } catch (IOException var4) {
         this.setValid(false);
         throw new SQLException("Could not serialize attributes to create session " + this.id + ":\n" + StackTraceUtils.throwable2StackTrace(var4));
      }

      ++var2;
      var1.setString(var2, this.isValid() ? "1" : "0");
      ++var2;
      var1.setLong(var2, this.accessTime);
      ++var2;
      var1.setInt(var2, this.maxInactiveInterval);
   }

   protected void addFirstUpdateStatement(PreparedStatement var1) throws SQLException, IOException {
      byte[] var2 = this.serializeAttributes();
      var1.setBinaryStream(1, new ByteArrayInputStream(var2), var2.length);
      var1.setString(2, this.isNew() ? "1" : "0");
      var1.setString(3, this.isValid() ? "1" : "0");
      var1.setLong(4, this.accessTime);
      var1.setInt(5, this.maxInactiveInterval);
   }

   protected void addTimeSensitiveUpdateStatement(PreparedStatement var1) throws SQLException {
      var1.setString(6, this.id);
      var1.setString(7, this.contextName);
      var1.setString(8, this.fullCtxName);
      var1.setLong(9, this.dbLAT);
      var1.setLong(10, this.triggerLAT);
   }

   protected void dbUpdate() throws SQLException {
      if (this.isDebugEnabled()) {
         Loggable var1 = HTTPSessionLogger.logPerformOperationLoggable("dbUpdate() lat=" + this.accessTime + " ", this.id, this.getContextPath());
         DEBUG_SESSIONS.debug(var1.getMessage());
      }

      Connection var19 = null;
      PreparedStatement var2 = null;

      try {
         boolean var3 = false;
         if (this.isModified()) {
            var19 = getConnection(this.dataSource, this.jdbcProps);
            var2 = var19.prepareStatement(this.jdbcCtx.getUpdateQuery());

            try {
               this.addFirstUpdateStatement(var2);
               boolean var4 = false;
               int var20;
               synchronized(this) {
                  this.addTimeSensitiveUpdateStatement(var2);
                  var20 = var2.executeUpdate();
                  if (var20 > 0) {
                     this.dbLAT = this.accessTime;
                     this.triggerLAT = 0L;
                  }
               }

               if (var20 == 0) {
                  if (!this.isValid()) {
                     throw new SQLException("Failed to update database record for session " + this.id + " for contextName = " + this.contextName);
                  }

                  try {
                     this.dbCreate();
                  } catch (SQLException var14) {
                     HTTPSessionLogger.logJDBCSessionConcurrentModification(this.id + " ctx:" + this.contextName + " dblat:" + this.dbLAT + " triggerLAT:" + this.triggerLAT, var14);
                     throw var14;
                  }
               }
            } catch (IOException var16) {
               throw new SQLException("Could not serialize attributes to update session " + this.id + ":\n" + StackTraceUtils.throwable2StackTrace(var16));
            }
         } else {
            this.jdbcCtx.updateLAT(this, this.contextName, this.fullCtxName);
         }
      } catch (SQLException var17) {
         this.setValid(false);
         throw var17;
      } finally {
         this.setModified(false);
         closeDBResources((ResultSet)null, var2, var19);
      }

   }

   protected static String getJDBCContextName(SessionContext var0) {
      return var0.getConfigMgr().isSessionSharingEnabled() ? var0.getServletContext().getApplicationId() : var0.getServletContext().getName();
   }

   protected static JDBCSessionData dbRefresh(String var0, SessionContext var1, DataSource var2, Properties var3) throws SQLException, SessionNotFoundException {
      Connection var4 = null;
      PreparedStatement var5 = null;
      ResultSet var6 = null;
      JDBCSessionData var7 = null;
      JDBCSessionContext var8 = (JDBCSessionContext)var1;
      String var9 = var1.getServletContext().getName();
      if (DEBUG_SESSIONS.isDebugEnabled()) {
         DEBUG_SESSIONS.debug("Looking in database for " + var0 + " with contextPath: " + var9);
      }

      try {
         var4 = getConnection(var2, var3);
         var5 = var4.prepareStatement(var8.getSelectQuery());
         var5.setString(1, var0);
         var5.setString(2, var9);
         var5.setString(3, var1.getServletContext().getFullCtxName());
         var6 = var5.executeQuery();
         String var10 = null;
         if (!var6.next()) {
            throw new SessionNotFoundException();
         } else {
            var10 = var6.getString(1);
            if (var6.wasNull()) {
               throw new SQLException("Failed to read session " + var0 + " from db");
            } else {
               var7 = var8.createNewData(var0, var1, var2, var3, false);
               var7.isNew = var10.equals("1");
               var7.creationTime = var6.getLong(2);
               InputStream var11 = var6.getBinaryStream(3);
               ByteArrayOutputStream var12 = new ByteArrayOutputStream();
               boolean var13 = false;
               Object var14 = null;

               byte[] var31;
               try {
                  int var30;
                  while((var30 = var11.read()) != -1) {
                     var12.write(var30);
                  }

                  var31 = var12.toByteArray();
               } catch (Exception var26) {
                  throw new SQLException("JDBCSessionData dbRefresh() fails getting session data: " + var26.getMessage());
               }

               var7.setValid(var6.getString(4).equals("1"));
               var7.accessTime = var6.getLong(5);
               var7.maxInactiveInterval = var6.getInt(6);
               var7.dbLAT = var7.accessTime;

               try {
                  var7.deSerializeAttributes(var31, var7);
                  if (var1.isDebugEnabled()) {
                     Loggable var15 = HTTPSessionLogger.logPerformOperationLoggable("dbRefresh() gets version=" + var7.getInternalAttribute("weblogic.versionId"), var0, var9);
                     DEBUG_SESSIONS.debug(var15.getMessage());
                  }
               } catch (IOException var27) {
                  boolean var16 = var7.isValid();
                  var7.remove();
                  long var17 = System.currentTimeMillis() - var7.getLAT();
                  if (var16 && (var7.maxInactiveInterval < 0 || var17 <= (long)(var7.maxInactiveInterval * 1000))) {
                     throw new SQLException("Could not deserialize attributes after reading session " + var0 + ":\n" + StackTraceUtils.throwable2StackTrace(var27));
                  }

                  Object var19 = null;
                  return (JDBCSessionData)var19;
               }

               JDBCSessionData var32 = var7;
               return var32;
            }
         }
      } catch (SQLException var28) {
         if (var7 != null) {
            var7.setValid(false);
         }

         throw var28;
      } finally {
         if (var7 != null) {
            var7.setModified(false);
         }

         closeDBResources(var6, var5, var4);
      }
   }

   boolean isCacheStale() {
      if (!this.isCacheStale) {
         this.isCacheStale = this.checkCacheStale();
      }

      return this.isCacheStale;
   }

   boolean checkCacheStale() {
      if (this.isDebugEnabled()) {
         Loggable var1 = HTTPSessionLogger.logPerformOperationLoggable("isCacheStale()", this.id, this.getContextPath());
         DEBUG_SESSIONS.debug(var1.getMessage());
      }

      Connection var16 = null;
      PreparedStatement var2 = null;
      ResultSet var3 = null;
      long var4 = this.dbLAT;
      if (this.triggerLAT > this.dbLAT) {
         var4 = this.triggerLAT;
      }

      boolean var7;
      try {
         var16 = getConnection(this.dataSource, this.jdbcProps);
         var2 = var16.prepareStatement(this.jdbcCtx.getSelectLATQuery());
         var2.setString(1, this.id);
         var2.setString(2, this.contextName);
         var2.setString(3, this.fullCtxName);
         synchronized(this) {
            var3 = var2.executeQuery();
            if (var3.next()) {
               this.dbLAT = var3.getLong(1);
            }

            return var4 < this.dbLAT;
         }
      } catch (SQLException var14) {
         HTTPSessionLogger.logUnexpectedError(this.logContext, var14);
         var7 = true;
      } finally {
         closeDBResources(var3, var2, var16);
      }

      return var7;
   }

   protected void dbRemove() {
      if (this.isDebugEnabled()) {
         Loggable var1 = HTTPSessionLogger.logPerformOperationLoggable("dbRemove()", this.id, this.getContextPath());
         DEBUG_SESSIONS.debug(var1.getMessage());
      }

      Connection var10 = null;
      PreparedStatement var2 = null;

      try {
         var10 = getConnection(this.dataSource, this.jdbcProps);
         var2 = var10.prepareStatement(this.jdbcCtx.getDeleteQuery());
         this.addRemoveStatement(var2);
         var2.executeUpdate();
      } catch (SQLException var8) {
         HTTPSessionLogger.logUnexpectedError(this.logContext, var8);
      } finally {
         closeDBResources((ResultSet)null, var2, var10);
      }

   }

   protected void addRemoveStatement(PreparedStatement var1) throws SQLException {
      var1.setString(1, this.id);
      var1.setString(2, this.contextName);
      var1.setString(3, this.fullCtxName);
   }

   static int getTotalSessionsCount(DataSource var0, Properties var1, WebAppServletContext var2, String var3) {
      Connection var4 = null;
      PreparedStatement var5 = null;
      ResultSet var6 = null;

      try {
         var4 = getConnection(var0, var1);
         var5 = var4.prepareStatement(var3);
         var5.setString(1, var2.getName());
         var5.setString(2, var2.getFullCtxName());
         var6 = var5.executeQuery();
         int var7 = 0;
         if (var6.next()) {
            var7 = var6.getInt(1);
         }

         int var8 = var7;
         return var8;
      } catch (SQLException var13) {
         HTTPSessionLogger.logUnexpectedError(var2.getLogContext(), var13);
      } finally {
         closeDBResources(var6, var5, var4);
      }

      return 0;
   }

   private byte[] serializeAttributes() throws IOException {
      Object var1 = null;
      UnsyncByteArrayOutputStream var2 = new UnsyncByteArrayOutputStream();
      WLObjectOutputStream var3 = new WLObjectOutputStream(var2);
      var3.setReplacer(RemoteObjectReplacer.getReplacer());
      var3.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
      var3.writeObject(convertToHashtable(this.attributes));
      var3.writeObject(convertToHashtable(this.internalAttributes));
      var3.flush();
      byte[] var4 = var2.toRawBytes();
      return var4;
   }

   private void deSerializeAttributes(byte[] var1, JDBCSessionData var2) throws IOException {
      if (var1 != null && var1.length >= 2) {
         try {
            UnsyncByteArrayInputStream var3 = new UnsyncByteArrayInputStream(var1);
            if (var1[0] == -84 && var1[1] == -19) {
               SessionMigrationObjectInputStream var9 = new SessionMigrationObjectInputStream(var3);
               var2.attributes = convertToConcurrentHashMap(var9.readObject());

               try {
                  var2.internalAttributes = convertToConcurrentHashMap(var9.readObject());
               } catch (StreamCorruptedException var7) {
                  this.internalAttributes = null;
                  if (this.isDebugEnabled()) {
                     DEBUG_SESSIONS.debug("Ignoring the StreamCorruptedException " + var7.getMessage());
                  }
               }
            } else {
               WLObjectInputStream var4 = new WLObjectInputStream(var3);
               var4.setReplacer(RemoteObjectReplacer.getReplacer());
               var2.attributes = convertToConcurrentHashMap(var4.readObject());

               try {
                  var2.internalAttributes = convertToConcurrentHashMap(var4.readObject());
               } catch (StreamCorruptedException var6) {
                  this.internalAttributes = null;
                  if (this.isDebugEnabled()) {
                     DEBUG_SESSIONS.debug("Ignoring the StreamCorruptedException " + var6.getMessage());
                  }
               }
            }

         } catch (ClassNotFoundException var8) {
            throw new IOException("Exception deserializing attributes:" + StackTraceUtils.throwable2StackTrace(var8));
         }
      }
   }

   void setTriggerLAT(long var1) {
      this.triggerLAT = var1;
   }

   void remove(boolean var1) {
      super.remove(var1);
      this.dbRemove();
      this.setValid(false);
   }

   void syncSession() {
      if (this.isValid()) {
         super.syncSession();

         try {
            this.dbUpdate();
         } catch (SQLException var2) {
            HTTPSessionLogger.logUnexpectedError(this.logContext, var2);
         }

      }
   }

   public synchronized void setAttribute(String var1, Object var2, boolean var3) throws IllegalStateException, IllegalArgumentException {
      super.setAttribute(var1, var2, var3);
      this.setModified(true);
   }

   protected void removeAttribute(String var1, boolean var2) throws IllegalStateException {
      super.removeAttribute(var1, var2);
      this.setModified(true);
   }

   public synchronized void setInternalAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException {
      super.setInternalAttribute(var1, var2);
      this.setModified(true);
   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      super.removeInternalAttribute(var1);
      this.setModified(true);
   }

   protected void logTransientAttributeError(String var1) {
      HTTPSessionLogger.logTransientJDBCAttributeError(this.logContext, var1, this.getId());
   }

   private static void closeDBResources(ResultSet var0, PreparedStatement var1, Connection var2) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Exception var6) {
      }

      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (Exception var5) {
      }

      try {
         if (var2 != null) {
            var2.close();
         }
      } catch (Exception var4) {
      }

   }

   private static class SessionMigrationObjectInputStream extends ObjectInputStream {
      public SessionMigrationObjectInputStream() throws IOException, SecurityException {
      }

      public SessionMigrationObjectInputStream(UnsyncByteArrayInputStream var1) throws IOException, StreamCorruptedException {
         super(var1);
      }

      public Class resolveClass(ObjectStreamClass var1) throws ClassNotFoundException {
         return ApplicationManager.loadClass(var1.getName(), (String)null);
      }
   }
}
