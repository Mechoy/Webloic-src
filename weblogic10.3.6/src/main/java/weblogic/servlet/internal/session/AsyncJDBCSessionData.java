package weblogic.servlet.internal.session;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import weblogic.utils.StackTraceUtils;

public class AsyncJDBCSessionData extends JDBCSessionData {
   private static final long serialVersionUID = 7506555655775346941L;
   public static final int SHOULD_BATCH = 0;
   public static final int CREATE = 1;
   public static final int UPDATE = 2;
   public static final int REMOVE = 3;
   private AsyncJDBCSessionContext jdbcCtx;
   private boolean blockingFlushCall = false;
   private boolean shouldBatch = false;
   private boolean shouldCreate = false;
   private int state;

   public AsyncJDBCSessionData(String var1, SessionContext var2, DataSource var3, Properties var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
      this.jdbcCtx = (AsyncJDBCSessionContext)var2;
      this.state = 0;
      this.shouldBatch = true;
   }

   static JDBCSessionData newSession(String var0, SessionContext var1, DataSource var2, Properties var3) {
      AsyncJDBCSessionData var4 = new AsyncJDBCSessionData(var0, var1, var2, var3, true);
      var4.shouldCreate = true;
      var1.getServletContext().getEventsManager().notifySessionLifetimeEvent(var4, true);
      return var4;
   }

   void syncSession() {
      super.syncSession();
      if (this.blockingFlushCall) {
         this.jdbcCtx.getPersistenceManager().blockingFlush();
         this.blockingFlushCall = false;
      }

   }

   public int getState() {
      return this.state;
   }

   protected synchronized void dbCreate() {
      this.state = 1;
      this.jdbcCtx.getPersistenceManager().update(this);
      this.shouldBatch = false;
   }

   protected synchronized void dbUpdate() {
      if (this.shouldBatch) {
         if (this.shouldCreate) {
            this.shouldCreate = false;
            this.state = 1;
         } else {
            this.state = 2;
         }

         this.jdbcCtx.getPersistenceManager().update(this);
         this.shouldBatch = false;
      }

   }

   protected synchronized void dbRemove() {
      this.state = 3;
      if (this.shouldBatch) {
         this.shouldBatch = false;
         this.jdbcCtx.getPersistenceManager().update(this);
      }

   }

   private void commit() {
      this.state = 0;
      this.shouldBatch = true;
   }

   public synchronized void addStatements(PreparedStatement var1) throws SQLException {
      if (this.state == 0) {
         throw new AssertionError("This should never happen, and state is " + this.state);
      } else {
         if (this.state == 1) {
            this.addCreateStatement(var1);
         } else if (this.state == 2) {
            try {
               this.addFirstUpdateStatement(var1);
            } catch (IOException var3) {
               throw new SQLException("Could not serialize attributes to update session " + this.id + ":\n" + StackTraceUtils.throwable2StackTrace(var3));
            }

            this.addTimeSensitiveUpdateStatement(var1);
         } else if (this.state == 3) {
            this.addRemoveStatement(var1);
         }

         this.dbLAT = this.accessTime;
         this.commit();
      }
   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      super.removeInternalAttribute(var1);
      if (var1.equals("weblogic.authuser")) {
         this.blockingFlushCall = true;
      }

   }
}
