package weblogic.servlet.internal.session;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import weblogic.cache.utils.BubblingCache;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.HTTPDebugLogger;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;

public final class FileSessionContext extends SessionContext {
   private static final String ROOT;
   private File sessionDir = null;
   private int sessionDirLen = 0;
   private static final Object dirTreeLock;
   private BubblingCache cachedSessions = null;
   private int fileSessionsCacheSize = 256;
   protected static final String[][] RESERVED;
   protected static final boolean WRITE_VERSIONS;
   private static final int PATH_LIM = 4;

   public FileSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);

      String var4;
      try {
         String var3 = var1.getTempPath();
         var4 = this.configMgr.getPersistentStoreDir();
         var4 = var4.replace('/', File.separatorChar);
         if (!isAbsolute(var4)) {
            File var5 = new File(DomainDir.getDataDirForServer(var1.getServer().getServerName()), ROOT + var3);
            if (!var5.exists() && !var5.mkdirs()) {
               HTTPLogger.logUnableToMakeDirectory(var1.getLogContext(), var5.getAbsolutePath());
            }

            this.sessionDir = new File(var5, var4);
         } else {
            String var13 = var1.getName();
            if (var13 != null && var13.length() > 0) {
               var4 = var4 + File.separatorChar + var13;
            }

            this.sessionDir = new File(var4);
         }
      } catch (Exception var9) {
         throw new Error(var9.toString());
      }

      if (this.configMgr.isCleanupSessionFilesEnabled()) {
         this.deleteSessionTree();
      }

      if (this.isDebugEnabled()) {
         Loggable var11 = HTTPSessionLogger.logPersistenceLoggable(this.sessionDir.getAbsolutePath());
         DEBUG_SESSIONS.debug(var11.getMessage());
      }

      boolean var12 = false;

      for(int var14 = 0; var14 < 10; ++var14) {
         synchronized(dirTreeLock) {
            if (this.sessionDir.mkdirs() || this.sessionDir.isDirectory()) {
               var12 = true;
               break;
            }

            try {
               Thread.sleep(500L);
            } catch (Exception var8) {
            }
         }
      }

      if (!var12) {
         throw new RuntimeException("Cannot make directory: " + this.sessionDir.getAbsolutePath());
      } else {
         var4 = this.sessionDir.getAbsolutePath();
         if (var4.endsWith(File.separator)) {
            this.sessionDirLen = var4.length();
         } else {
            this.sessionDirLen = var4.length() + 1;
         }

         this.fileSessionsCacheSize = this.configMgr.getCacheSize();
         if (this.fileSessionsCacheSize > 0) {
            this.cachedSessions = new BubblingCache(this.fileSessionsCacheSize);
         }

      }
   }

   static boolean containsReservedKeywords(String var0) {
      String[] var1 = getStorageDirs(var0.toUpperCase());
      int var4;
      if (4 <= RESERVED.length) {
         for(int var2 = 0; var2 < var1.length - 1; ++var2) {
            String var3 = var1[var2];

            for(var4 = 0; var4 < RESERVED[4].length; ++var4) {
               if (RESERVED[4][var4].equals(var3)) {
                  return true;
               }
            }
         }
      }

      String var5 = var1[var1.length - 1];
      int var6 = var5.length();
      if (var6 <= RESERVED.length) {
         for(var4 = 0; var4 < RESERVED[var6].length; ++var4) {
            if (RESERVED[var6][var4].equals(var5)) {
               return true;
            }
         }
      }

      return false;
   }

   protected void invalidateOrphanedSessions() {
      Set var1 = this.getServletContext().getServer().getSessionLogin().getAllIds();
      if (!var1.isEmpty()) {
         String[] var2 = this.getIdsInternal();

         int var3;
         for(var3 = 0; var3 < var2.length; ++var3) {
            var1.remove(var2[var3]);
         }

         if (var3 != 0) {
            String var4 = null;
            Iterator var5 = var1.iterator();

            while(var5.hasNext()) {
               var4 = (String)var5.next();
               this.getServletContext().getServer().getSessionLogin().unregister(var4, this.getServletContext().getContextPath());
            }

         }
      }
   }

   private static boolean isAbsolute(String var0) {
      String var1 = System.getProperty("os.name");
      if (var1 != null) {
         var1 = var1.toLowerCase(Locale.ENGLISH);
         if (var1.indexOf("windows") >= 0 && var0.length() > 2 && Character.isLetter(var0.charAt(0)) && var0.charAt(1) == ':') {
            return true;
         }
      }

      return var0.length() > 0 && var0.charAt(0) == File.separatorChar;
   }

   public String getPersistentStoreType() {
      return "file";
   }

   private void deleteSessionTree() {
      if (this.isDebugEnabled()) {
         Loggable var1 = HTTPSessionLogger.logDeleteDirectoryLoggable(this.sessionDir.getAbsolutePath());
         DEBUG_SESSIONS.debug(var1.getMessage());
      }

      FileEnumerator var4 = new FileEnumerator(this, true, false);
      var4.recurse(this.sessionDir);
      File[] var2 = var4.getSessionFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].delete()) {
            HTTPSessionLogger.logUnableToDelete(var2[var3].getAbsolutePath());
         }
      }

   }

   private void makeStorageDir(String var1) {
      boolean var2 = false;
      File var3 = this.getSessionPath(RSID.getID(var1));
      var3 = new File(var3.getParent());
      if (!var3.isDirectory()) {
         for(int var4 = 0; var4 < 10; ++var4) {
            synchronized(dirTreeLock) {
               if (var3.mkdirs() || var3.isDirectory()) {
                  var2 = true;
                  break;
               }
            }

            try {
               Thread.sleep(500L);
            } catch (Exception var7) {
            }
         }

         if (!var2) {
            throw new RuntimeException("Cannot make directory: " + var3.getAbsolutePath());
         }
      }
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      FileSessionData var4 = new FileSessionData(var1, this, true);
      SessionData.checkSpecial(var2, var4);
      var4.incrementActiveRequestCount();
      this.addSession(var4.id, var4);
      this.makeStorageDir(var4.id);
      this.incrementOpenSessionsCount();
      var4.setMonitoringId();
      return var4;
   }

   private String path2session(File var1) {
      String var2 = var1.getAbsolutePath().substring(this.sessionDirLen);
      StringBuilder var3 = new StringBuilder();
      int var4 = var2.length();

      for(int var5 = 0; var5 < var4; ++var5) {
         char var6 = var2.charAt(var5);
         if (var6 != File.separatorChar) {
            var3.append(var6);
         }
      }

      return var3.toString();
   }

   private String session2path(String var1) {
      StringBuilder var2 = new StringBuilder();
      String[] var3 = getStorageDirs(var1);
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length - 1; ++var4) {
            var2.append(var3[var4]);
            var2.append(File.separatorChar);
         }

         var2.append(var3[var3.length - 1]);
      }

      return var2.toString();
   }

   static String[] getStorageDirs(String var0) {
      int var1 = var0.length();
      int var2 = var1 / 4;
      if (var1 % 4 > 0) {
         ++var2;
      }

      String[] var3 = new String[var2];
      int var4 = 0;

      for(int var5 = 0; var4 < var1 - 4; ++var5) {
         var3[var5] = var0.substring(var4, var4 + 4);
         var4 += 4;
      }

      var3[var2 - 1] = var0.substring(var4);
      return var3;
   }

   public File getSessionPath(String var1) {
      return new File(this.sessionDir, this.session2path(var1));
   }

   public void sync(HttpSession var1) {
      FileSessionData var2 = (FileSessionData)var1;
      if (var2.isValid()) {
         var2.syncSession();
         synchronized(var2) {
            var2.decrementActiveRequestCount();
            if (!var2.sessionInUse() && var2.isValid()) {
               var2.notifyAboutToPassivate(new HttpSessionEvent(var2));
               String var4 = var2.id;
               if (this.cachedSessions != null) {
                  this.cachedSessions.put(var2.id, var1);
               }

               DataOutputStream var5 = null;
               File var6 = null;

               try {
                  var6 = this.getSessionPath(var4);
                  synchronized(dirTreeLock) {
                     this.makeStorageDir(var4);
                     var5 = new DataOutputStream(new FileOutputStream(var6));
                     WLObjectOutputStream var8 = new WLObjectOutputStream(var5);
                     var8.setReplacer(RemoteObjectReplacer.getReplacer());
                     var8.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
                     if (WRITE_VERSIONS) {
                        this.writeVersionInfo((WebAppServletContext)var2.getServletContext(), var8);
                     }

                     var8.writeObject(var2);
                     var8.flush();
                     var5.writeLong(var2.getLAT());
                     var8.close();
                     var5.close();
                  }

                  if (var2.transientAttributes != null) {
                     this.transientData.put(var4, var2.transientAttributes);
                  }

                  var5 = null;
                  if (this.isDebugEnabled()) {
                     Loggable var7 = HTTPSessionLogger.logPickledSessionLoggable(var4, var6.getAbsolutePath());
                     DEBUG_SESSIONS.debug(var7.getMessage());
                  }
               } catch (ThreadDeath var24) {
                  throw var24;
               } catch (Throwable var25) {
                  HTTPSessionLogger.logErrorSavingSessionData(var25);
                  if (var6 != null) {
                     var6.delete();
                  }
               } finally {
                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (Exception var22) {
                     }
                  }

               }

               this.removeSession(var4);
            }
         }
      }
   }

   public void destroy(boolean var1, boolean var2) {
      super.destroy(var2);
      if (this.configMgr.isCleanupSessionFilesEnabled()) {
         this.deleteSessionTree();
      }

   }

   private void writeVersionInfo(WebAppServletContext var1, ObjectOutputStream var2) throws IOException {
      if (var1.getVersionId() == null) {
         var2.writeObject((Object)null);
      } else {
         var2.writeObject(new AppVersionInfo(var1.getVersionId()));
      }

   }

   private File getParent(File var1) {
      String var2 = var1.getParent();
      if (var2 == null) {
         return null;
      } else {
         File var3 = new File(var2);
         var2 = var3.getAbsolutePath();
         return var2.length() <= this.sessionDirLen ? null : var3;
      }
   }

   private void delTree(File var1) {
      synchronized(dirTreeLock) {
         if (var1 != null && var1.exists()) {
            do {
               String[] var3 = var1.list();
               if (var3 != null && var3.length > 0) {
                  return;
               }

               if (!var1.delete()) {
                  return;
               }
            } while((var1 = this.getParent(var1)) != null);

         }
      }
   }

   boolean invalidateSession(SessionData var1, boolean var2, boolean var3) {
      if (var1 == null) {
         return false;
      } else {
         String var4 = var1.id;
         File var5 = this.getSessionPath(var4);
         this.removeSession(var4);
         if (this.cachedSessions != null) {
            this.cachedSessions.remove(var1.id);
         }

         this.transientData.remove(var4);
         var1.remove(var3);
         var1.setValid(false);
         this.decrementOpenSessionsCount();

         try {
            boolean var6 = false;

            for(int var7 = var2 ? 1 : 3; var7 > 0; --var7) {
               if (!var5.exists()) {
                  if (this.isDebugEnabled()) {
                     Loggable var8 = HTTPSessionLogger.logNotInvalidatedLoggable(var5.getAbsolutePath());
                     DEBUG_SESSIONS.debug(var8.getMessage());
                  }

                  return false;
               }

               if (var5.delete()) {
                  var6 = true;
                  break;
               }

               if (var7 > 1) {
                  if (this.isDebugEnabled()) {
                     DEBUG_SESSIONS.debug("Retry deleting session persistent file : " + var5.getAbsolutePath());
                  }

                  try {
                     Thread.sleep((long)(50 + (int)(Math.random() * 50.0)));
                  } catch (Exception var9) {
                  }
               }
            }

            if (!var6) {
               HTTPSessionLogger.logUnableToDelete(var5.getAbsolutePath());
            } else if (this.isDebugEnabled()) {
               Loggable var11 = HTTPSessionLogger.logDeletedFileLoggable(var5.getAbsolutePath());
               DEBUG_SESSIONS.debug(var11.getMessage());
            }

            this.delTree(this.getParent(var5));
            SessionData.invalidateProcessedSession(var1);
         } catch (Throwable var10) {
            HTTPSessionLogger.logTestFailure(var4, var10);
            if (var5 != null) {
               var5.delete();
               this.delTree(this.getParent(var5));
            }
         }

         return true;
      }
   }

   private FileSessionData loadSession(FileInputStream var1, File var2, String var3, boolean var4) {
      if (!var2.canRead()) {
         this.removeSession(var3);
         return null;
      } else {
         synchronized(dirTreeLock) {
            if (!var2.canRead()) {
               this.removeSession(var3);
               return null;
            } else {
               FileSessionData var9;
               try {
                  if (var1 == null) {
                     var1 = new FileInputStream(var2);
                  }

                  WLObjectInputStream var6 = new WLObjectInputStream(var1);
                  var6.setReplacer(RemoteObjectReplacer.getReplacer());
                  Object var8 = var6.readObject();
                  FileSessionData var7;
                  if (var8 != null && !(var8 instanceof AppVersionInfo)) {
                     var7 = (FileSessionData)var8;
                  } else {
                     var7 = (FileSessionData)var6.readObject();
                  }

                  var7.id = var3;
                  var7.setSessionContext(this);
                  var7.setModified(false);
                  var6.close();
                  var1.close();
                  var7.transientAttributes = (Hashtable)this.transientData.get(var3);
                  var1 = null;
                  if (var4 && !var7.isValidForceCheck()) {
                     var9 = null;
                     return var9;
                  }

                  var9 = var7;
               } catch (FileNotFoundException var30) {
                  this.logLoadingErrorForDebug(var30);
                  return null;
               } catch (EOFException var31) {
                  this.logLoadingErrorForDebug(var31);
                  return null;
               } catch (IOException var32) {
                  this.logLoadingErrorForDebug(var32);
                  return null;
               } catch (ClassNotFoundException var33) {
                  this.logLoadingErrorForDebug("Probably the classfiles have changed for a new version", var33);
                  return null;
               } catch (Throwable var34) {
                  HTTPSessionLogger.logErrorLoadingSessionData(var3, var34);
                  return null;
               } finally {
                  if (var1 != null) {
                     try {
                        var1.close();
                     } catch (Exception var29) {
                     }
                  }

               }

               return var9;
            }
         }
      }
   }

   private final void logLoadingErrorForDebug(Exception var1) {
      this.logLoadingErrorForDebug((String)null, var1);
   }

   private final void logLoadingErrorForDebug(String var1, Exception var2) {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(var1 != null ? var1 : "Failed to load session from file. It could be interfered by other server which is pointing to the same persistent file system.", var2);
      }

   }

   public String[] getIdsInternal() {
      FileEnumerator var1 = new FileEnumerator(this, false, false);
      synchronized(dirTreeLock) {
         var1.recurse(this.sessionDir);
      }

      return var1.getSessionIds();
   }

   void unregisterExpiredSessions(ArrayList var1) {
   }

   public int getCurrOpenSessionsCount() {
      FileEnumerator var1 = new FileEnumerator(this, false, true);
      var1.recurse(this.sessionDir);
      return var1.getCount();
   }

   private boolean isCacheStale(SessionData var1) {
      File var2 = this.getSessionPath(var1.id);
      if (!var2.exists()) {
         if (this.isDebugEnabled()) {
            Loggable var23 = HTTPSessionLogger.logNotInvalidatedLoggable(var2.getAbsolutePath());
            DEBUG_SESSIONS.debug(var23.getMessage());
         }

         return true;
      } else {
         RandomAccessFile var3 = null;
         synchronized(dirTreeLock) {
            boolean var7;
            try {
               var3 = new RandomAccessFile(var2, "r");
               var3.seek(var2.length() - 8L);
               long var5 = var3.readLong();
               var3.seek(0L);
               var3.close();
               var3 = null;
               if (var5 > var1.getLAT()) {
                  return true;
               }

               var7 = false;
            } catch (Throwable var20) {
               if (this.isDebugEnabled()) {
                  Loggable var6 = HTTPSessionLogger.logTestFailureLoggable(var1.id, var20);
                  DEBUG_SESSIONS.debug(var6.getMessage());
               }

               var2.delete();
               this.delTree(this.getParent(var2));
               return true;
            } finally {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Exception var19) {
                  }
               }

               var3 = null;
            }

            return var7;
         }
      }
   }

   public SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      var1 = RSID.getID(var1);
      FileSessionData var4 = (FileSessionData)this.getOpenSession(var1);
      if (var4 == null && this.cachedSessions != null) {
         var4 = (FileSessionData)this.cachedSessions.get(var1);
         if (var4 != null) {
            if (!var4.sessionInUse() && this.isCacheStale(var4)) {
               this.cachedSessions.remove(var4.id);
               var4 = null;
            } else {
               this.addSession(var4.id, var4);
            }
         }
      }

      if (var4 != null) {
         if (var2 != null && var3 != null) {
            synchronized(var4) {
               if (!var4.isValidForceCheck()) {
                  if (this.cachedSessions != null) {
                     this.cachedSessions.remove(var4.id);
                  }

                  this.removeSession(var4.id);
                  return null;
               }

               var4.incrementActiveRequestCount();
            }
         }

         return var4;
      } else {
         File var5 = this.getSessionPath(var1);
         if (this.isDebugEnabled()) {
            Loggable var6 = HTTPSessionLogger.logSessionPathLoggable(var1, var5.getAbsolutePath(), var5.exists());
            DEBUG_SESSIONS.debug(var6.getMessage());
         }

         var4 = this.loadSession((FileInputStream)null, var5, var1, var2 != null && var3 != null);
         if (var4 != null && var2 != null && var3 != null) {
            var4.reinitRuntimeMBean();
            var4.updateVersionIfNeeded(this);
            var4.getContext().addSession(var1, var4);
            var4.incrementActiveRequestCount();
            var4.notifyActivated(new HttpSessionEvent(var4));
         }

         return var4;
      }
   }

   public int getNonPersistedSessionCount() {
      return 0;
   }

   public String lookupAppVersionIdForSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      if (var1 == null) {
         return null;
      } else {
         var1 = RSID.getID(var1);
         if (this.cachedSessions != null && !this.cachedSessions.isEmpty()) {
            SessionData var4 = (SessionData)this.cachedSessions.get(var1);
            if (var4 != null && var4.getServletContext() != null) {
               return ((WebAppServletContext)var4.getServletContext()).getVersionId();
            }
         }

         File var33 = this.getSessionPath(var1);
         if (var33 != null && var33.exists() && !var33.isDirectory()) {
            FileInputStream var5 = null;
            String var6 = null;

            try {
               var5 = new FileInputStream(var33);
               WLObjectInputStream var7 = new WLObjectInputStream(var5);
               var7.setReplacer(RemoteObjectReplacer.getReplacer());
               Object var8 = var7.readObject();
               if (var8 != null && var8 instanceof AppVersionInfo) {
                  var6 = ((AppVersionInfo)var8).getVersionInfo();
               }

               if (HTTPDebugLogger.isEnabled()) {
                  StringBuilder var9 = new StringBuilder("got version id \"");
                  var9.append(var6).append("\" for session id \"").append(var1).append("\" from file \"").append(var33).append("\"");
                  HTTPDebugLogger.debug(var9.toString());
               }
            } catch (FileNotFoundException var27) {
               this.logLoadingErrorForDebug(var27);
            } catch (EOFException var28) {
               this.logLoadingErrorForDebug(var28);
            } catch (IOException var29) {
               this.logLoadingErrorForDebug(var29);
            } catch (ClassNotFoundException var30) {
               this.logLoadingErrorForDebug(var30);
            } catch (Exception var31) {
               this.logLoadingErrorForDebug(var31);
            } finally {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var26) {
                  }
               }

            }

            return var6;
         } else {
            return null;
         }
      }
   }

   static {
      ROOT = "webapps" + File.separator;
      dirTreeLock = new Object();
      RESERVED = new String[][]{new String[0], new String[0], new String[0], {"PRN"}, {"LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"}, new String[0], new String[0]};
      WRITE_VERSIONS = !Boolean.getBoolean("weblogic.servlet.session.PersistentBackCompatibility");
   }

   private class FileEnumerator {
      private final ArrayList list;
      private final ArrayList sessionIds;
      private FileSessionContext ctxt;
      private boolean listFiles = false;
      private boolean justCount = false;
      private int count = 0;

      FileEnumerator(FileSessionContext var2, boolean var3, boolean var4) {
         this.ctxt = var2;
         this.list = new ArrayList();
         this.listFiles = var3;
         this.justCount = var4;
         this.sessionIds = new ArrayList();
      }

      public void recurse(File var1) {
         String[] var2 = var1.list();
         if (var2 != null && var2.length != 0) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               File var4 = new File(var1, var2[var3]);
               if (var4.isDirectory()) {
                  this.recurse(var4);
                  if (!this.justCount && this.listFiles) {
                     this.list.add(var4);
                  }
               } else if (this.justCount) {
                  ++this.count;
               } else if (this.listFiles) {
                  this.list.add(var4);
               } else {
                  String var5 = this.ctxt.path2session(var4);
                  this.list.add(var5);
                  this.sessionIds.add(var5);
               }
            }

         }
      }

      public int getCount() {
         return this.count;
      }

      public File[] getSessionFiles() {
         File[] var1 = new File[this.list.size()];
         return (File[])((File[])this.list.toArray(var1));
      }

      public String[] getSessionIds() {
         String[] var1 = new String[this.sessionIds.size()];
         return (String[])((String[])this.sessionIds.toArray(var1));
      }
   }

   private static final class AppVersionInfo implements Serializable {
      private final String versionId;

      AppVersionInfo(String var1) {
         this.versionId = var1;
      }

      public String getVersionInfo() {
         return this.versionId;
      }
   }
}
