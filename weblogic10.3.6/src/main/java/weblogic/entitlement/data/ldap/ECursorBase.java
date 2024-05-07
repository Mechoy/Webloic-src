package weblogic.entitlement.data.ldap;

import java.util.Properties;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import weblogic.entitlement.data.BaseResource;
import weblogic.security.shared.LoggerWrapper;

abstract class ECursorBase {
   private static int cursorId = 1;
   private String cursorName;
   private LDAPConnection conn;
   private LDAPSearchResults results;
   private LDAPEntry currEntry;
   private int currEntryNum;
   private int maximumToReturn;
   protected EData data;
   protected LoggerWrapper traceLogger;

   public ECursorBase(String var1, LDAPConnection var2, LDAPSearchResults var3, int var4, EData var5, LoggerWrapper var6) {
      this.traceLogger = var6;
      this.data = var5;
      this.conn = var2;
      this.results = var3;
      this.maximumToReturn = var4;
      this.currEntry = null;
      this.currEntryNum = 0;
      this.cursorName = var1 + this.hashCode() + this.getNextCursorId();
      if (var3 != null) {
         if (var6 != null && var6.isDebugEnabled()) {
            var6.debug("ECursorBase has more elements: " + var3.hasMoreElements());
         }

         if (!var3.hasMoreElements()) {
            this.conn = null;
            if (var2 != null) {
               EData.releaseConnection(var2);
            }
         } else {
            this.advance();
         }
      }

   }

   public abstract Properties getCurrentProperties();

   public void advance() {
      this.advance(true);
   }

   protected void advance(boolean var1) {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("advance(" + var1 + ")");
      }

      this.currEntry = null;

      try {
         if (this.results == null || !this.results.hasMoreElements() || this.maximumToReturn != 0 && this.currEntryNum >= this.maximumToReturn) {
            this.close();
         } else {
            this.currEntry = this.results.next();
            if (var1) {
               ++this.currEntryNum;
            }
         }
      } catch (LDAPException var3) {
         if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
            this.traceLogger.debug("LDAPException while trying to advance cursor");
         }

         this.close();
         EData var10000 = this.data;
         EData.checkStorageException(var3);
      }

   }

   public void close() {
      if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
         this.traceLogger.debug("close");
      }

      boolean var6 = false;

      EData var10000;
      LDAPConnection var9;
      label154: {
         try {
            try {
               var6 = true;
               if (this.results != null) {
                  if (this.conn != null) {
                     if (this.traceLogger != null) {
                        this.traceLogger.debug("ECursorBase abandoning search results");
                     }

                     LDAPSearchResults var1 = this.results;
                     this.results = null;
                     this.conn.abandon(var1);
                     var6 = false;
                  } else {
                     var6 = false;
                  }
               } else {
                  var6 = false;
               }
               break label154;
            } catch (LDAPException var7) {
               if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
                  this.traceLogger.debug("LDAPException while trying to abandon results");
               }
            }

            var10000 = this.data;
            EData.checkStorageException(var7);
            var6 = false;
         } finally {
            if (var6) {
               if (this.conn != null) {
                  if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
                     this.traceLogger.debug("ECursorBase releasing connection");
                  }

                  LDAPConnection var3 = this.conn;
                  this.conn = null;
                  var10000 = this.data;
                  EData.releaseConnection(var3);
               }

            }
         }

         if (this.conn != null) {
            if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
               this.traceLogger.debug("ECursorBase releasing connection");
            }

            var9 = this.conn;
            this.conn = null;
            var10000 = this.data;
            EData.releaseConnection(var9);
         }

         return;
      }

      if (this.conn != null) {
         if (this.traceLogger != null && this.traceLogger.isDebugEnabled()) {
            this.traceLogger.debug("ECursorBase releasing connection");
         }

         var9 = this.conn;
         this.conn = null;
         var10000 = this.data;
         EData.releaseConnection(var9);
      }

   }

   public boolean haveCurrent() {
      return this.currEntry != null;
   }

   public String getCursorName() {
      return this.cursorName;
   }

   protected LDAPEntry getCurrentEntry() {
      return this.currEntry;
   }

   protected String getEntitlement(BaseResource var1) {
      String var2 = var1.getEntitlement();
      return var2 != null ? var2 : "";
   }

   private synchronized int getNextCursorId() {
      return cursorId++;
   }
}
