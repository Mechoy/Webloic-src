package weblogic.security.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPCache;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import weblogic.security.shared.LoggerWrapper;

public final class LDAPConnectionHelper {
   private LDAPConnection conn;
   private Pool pool;
   private boolean isWrite;
   private static final String OBJECTCLASS_ATTR = "objectclass";
   private static LoggerWrapper log = LoggerWrapper.getInstance("DebugEmbeddedLDAP");

   private boolean isDebug() {
      return log.isDebugEnabled();
   }

   private void _debug(String var1) {
      if (log.isDebugEnabled()) {
         log.debug(var1);
      }

   }

   private void debug(String var1, String var2) {
      this._debug("LDAPConnectionHelper." + var1 + ": " + var2);
   }

   public LDAPConnectionHelper(Pool var1, boolean var2, LoggerWrapper var3) {
      String var4 = "constructor";
      if (var3 != null) {
         log = var3;
      }

      this.pool = var1;
      this.isWrite = var2;

      try {
         this.conn = (LDAPConnection)var1.getInstance();
         if (this.isDebug()) {
            this.debug(var4, "conn:" + this.conn);
         }

      } catch (InvocationTargetException var6) {
         throw new RuntimeException(var6);
      }
   }

   public void setReadOnly() {
      this.isWrite = false;
   }

   public LDAPConnection getConnection() {
      return this.conn;
   }

   public void done() {
      String var1 = "done";
      if (this.conn != null) {
         if (this.isDebug()) {
            this.debug(var1, "conn:" + this.conn);
         }

         if (this.isWrite) {
            LDAPCache var2 = this.conn.getCache();
            if (var2 != null) {
               var2.flushEntries((String)null, 0);
            }
         }

         this.pool.returnInstance(this.conn);
         this.conn = null;
      }
   }

   public void error() {
      String var1 = "error";
      if (this.conn != null) {
         if (this.isDebug()) {
            this.debug(var1, "conn:" + this.conn);
         }

         try {
            this.conn.disconnect();
         } catch (Exception var3) {
         }

         this.conn = null;
      }
   }

   public boolean ensureDirectoryExists(String var1, String var2, String var3, String var4) throws LDAPException {
      String var5 = "ensureDirectoryExists";
      if (this.isDebug()) {
         this.debug(var5, "dn=\"" + var1 + "\"" + ", objectClass=" + var2 + ", leafAttr=" + var3 + ", leafValue=" + var4);
      }

      try {
         String[] var6 = new String[]{"1.1"};
         this.conn.read(var1, var6);
         if (this.isDebug()) {
            this.debug(var5, "directory already exists");
         }

         return false;
      } catch (LDAPException var12) {
         if (var12.getLDAPResultCode() == 32) {
            String[] var7 = new String[]{"top", var2};
            LDAPAttributeSet var8 = new LDAPAttributeSet();
            var8.add(new LDAPAttribute("objectclass", var7));
            var8.add(new LDAPAttribute(var3, var4));
            LDAPEntry var9 = new LDAPEntry(var1, var8);

            try {
               this.conn.add(var9);
            } catch (LDAPException var11) {
               if (var11.getLDAPResultCode() == 68) {
                  if (this.isDebug()) {
                     this.debug(var5, "directory already exists or just created by other component");
                  }

                  return false;
               }

               throw var11;
            }

            if (this.isDebug()) {
               this.debug(var5, "created directory");
            }

            return true;
         } else {
            throw var12;
         }
      }
   }

   private static String escapeSearchFilterStringAttr(String var0, boolean var1) {
      int var2 = var0.length();
      StringBuffer var3 = new StringBuffer(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         char var5 = var0.charAt(var4);
         switch (var5) {
            case '\u0000':
               var3.append("\\00");
               break;
            case '(':
               var3.append("\\28");
               break;
            case ')':
               var3.append("\\29");
               break;
            case '*':
               if (var1) {
                  var3.append("\\2a");
               } else {
                  var3.append(var5);
               }
               break;
            case '\\':
               var3.append("\\5c");
               break;
            default:
               var3.append(var5);
         }
      }

      return var3.toString();
   }

   public static String escapeSearchFilterLiteralStringAttr(String var0) {
      return escapeSearchFilterStringAttr(var0, true);
   }

   public static String escapeSearchFilterWildcardStringAttr(String var0) {
      return escapeSearchFilterStringAttr(var0, false);
   }

   public static String escapeDNAttr(String var0) {
      return var0;
   }

   public static byte[] getSingletonByteArrayValue(LDAPEntry var0, String var1) {
      LDAPAttribute var2 = var0.getAttribute(var1);
      if (var2 == null) {
         return null;
      } else {
         Enumeration var3 = var2.getByteValues();
         return var3 != null && var3.hasMoreElements() ? (byte[])((byte[])var3.nextElement()) : null;
      }
   }

   public static String getSingletonStringValue(LDAPEntry var0, String var1) {
      LDAPAttribute var2 = var0.getAttribute(var1);
      if (var2 == null) {
         return null;
      } else {
         Enumeration var3 = var2.getStringValues();
         return var3 != null && var3.hasMoreElements() ? (String)var3.nextElement() : null;
      }
   }
}
