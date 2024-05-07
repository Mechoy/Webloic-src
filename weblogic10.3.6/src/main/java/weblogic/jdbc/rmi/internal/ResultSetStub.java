package weblogic.jdbc.rmi.internal;

import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderBlockGetterImpl;
import weblogic.jdbc.common.internal.ReaderHandler;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.RemoteHelper;

public class ResultSetStub extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = 3023484301546933309L;
   ResultSet remoteRs;
   private RmiDriverSettings rmiSettings;
   private transient ResultSetRowCache currRowCache;
   private transient BlockGetter bg = null;
   private transient ReaderBlockGetter rbg = null;

   public ResultSetStub() {
   }

   public ResultSetStub(ResultSet var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(ResultSet var1, RmiDriverSettings var2) {
      this.remoteRs = var1;
      this.rmiSettings = var2;
   }

   public Object readResolve() throws ObjectStreamException {
      ResultSetStub var1 = null;

      try {
         var1 = (ResultSetStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ResultSetStub", this.remoteRs, false);
         var1.init(this.remoteRs, this.rmiSettings);
         return (java.sql.ResultSet)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return var1;
      }
   }

   public Object getStub() {
      if (this.rmiSettings.isVerbose()) {
         JdbcDebug.JDBCRMIInternal.debug("getStub remoteRs: " + this.remoteRs);
      }

      return this.remoteRs;
   }

   public boolean isRowCaching() throws SQLException {
      if (RemoteHelper.isCollocated(this.remoteRs)) {
         this.rmiSettings.setRowCacheSize(0);
         return false;
      } else {
         return this.remoteRs.isRowCaching();
      }
   }

   public int getRowCacheSize() {
      return this.rmiSettings.getRowCacheSize();
   }

   public ResultSetRowCache getNextRowCache() throws SQLException {
      return this.remoteRs.getNextRowCache();
   }

   public ResultSetMetaDataCache getMetaDataCache() throws SQLException {
      return this.remoteRs.getMetaDataCache();
   }

   public java.sql.ResultSetMetaData getMetaData() throws SQLException {
      java.sql.ResultSetMetaData var1 = this.remoteRs.getMetaData();
      return var1 == null ? null : new ResultSetMetaDataImpl(var1);
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      BlockGetter var5 = this.remoteRs.getBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 1);
      if (var3 == -1) {
         return null;
      } else {
         InputStreamHandler var4 = new InputStreamHandler();
         var4.setBlockGetter(var5, var3);
         return var4;
      }
   }

   public InputStream getAsciiStream(String var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      BlockGetter var5 = this.remoteRs.getBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 1);
      if (var3 == -1) {
         return null;
      } else {
         InputStreamHandler var4 = new InputStreamHandler();
         var4.setBlockGetter(var5, var3);
         return var4;
      }
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getUnicodeStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      BlockGetter var5 = this.remoteRs.getBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 2);
      if (var3 == -1) {
         return null;
      } else {
         InputStreamHandler var4 = new InputStreamHandler();
         var4.setBlockGetter(var5, var3);
         return var4;
      }
   }

   public InputStream getUnicodeStream(String var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      BlockGetter var5 = this.remoteRs.getBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 2);
      if (var3 == -1) {
         return null;
      } else {
         InputStreamHandler var4 = new InputStreamHandler();
         var4.setBlockGetter(var5, var3);
         return var4;
      }
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      BlockGetter var5 = this.remoteRs.getBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 3);
      if (var3 == -1) {
         return null;
      } else {
         InputStreamHandler var4 = new InputStreamHandler();
         var4.setBlockGetter(var5, var3);
         return var4;
      }
   }

   public InputStream getBinaryStream(String var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      BlockGetter var5 = this.remoteRs.getBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 3);
      if (var3 == -1) {
         return null;
      } else {
         InputStreamHandler var4 = new InputStreamHandler();
         var4.setBlockGetter(var5, var3);
         return var4;
      }
   }

   public Reader getCharacterStream(int var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      ReaderBlockGetter var5 = this.remoteRs.getReaderBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 4);
      if (var3 == -1) {
         return null;
      } else {
         ReaderHandler var4 = new ReaderHandler();
         var4.setReaderBlockGetter(var5, var3);
         return var4;
      }
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      ReaderBlockGetter var5 = this.remoteRs.getReaderBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 4);
      if (var3 == -1) {
         return null;
      } else {
         ReaderHandler var4 = new ReaderHandler();
         var4.setReaderBlockGetter(var5, var3);
         return var4;
      }
   }

   public void close() throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var1 = "time=" + System.currentTimeMillis() + " : close";
         JdbcDebug.JDBCRMIInternal.debug(var1);
      }

      try {
         this.remoteRs.close();
      } finally {
         synchronized(this) {
            if (this.bg != null) {
               this.bg.close();
               this.bg = null;
            }

            if (this.rbg != null) {
               this.rbg.close();
               this.rbg = null;
            }

         }
      }

   }

   public void updateNClob(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateNClob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateNClob(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateNClob(var1, this.rbg, var6);
      }

   }

   public void updateNClob(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateNClob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateNClob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateNClob(var1, this.rbg, var8, var3);
      }

   }

   public void updateNClob(String var1, Reader var2) throws SQLException {
      this.updateNClob(this.remoteRs.findColumn(var1), var2);
   }

   public void updateNClob(String var1, Reader var2, long var3) throws SQLException {
      this.updateNClob(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateClob(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateClob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateClob(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateClob(var1, this.rbg, var6);
      }

   }

   public void updateClob(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateClob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateClob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateClob(var1, this.rbg, var8, var3);
      }

   }

   public void updateClob(String var1, Reader var2) throws SQLException {
      this.updateClob(this.remoteRs.findColumn(var1), var2);
   }

   public void updateClob(String var1, Reader var2, long var3) throws SQLException {
      this.updateClob(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateCharacterStream(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateCharacterStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateCharacterStream(var1, this.rbg, var6);
      }

   }

   public void updateCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : updateCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      if (var2 == null) {
         this.remoteRs.updateCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var7 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateCharacterStream(var1, this.rbg, var7, var3);
      }

   }

   public void updateCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateCharacterStream(var1, this.rbg, var8, var3);
      }

   }

   public void updateCharacterStream(String var1, Reader var2) throws SQLException {
      this.updateCharacterStream(this.remoteRs.findColumn(var1), var2);
   }

   public void updateCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.updateCharacterStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.updateCharacterStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateNCharacterStream(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateNCharacterStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateNCharacterStream(var1, this.rbg, var6);
      }

   }

   public void updateNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateNCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateNCharacterStream(var1, this.rbg, var8, var3);
      }

   }

   public void updateNCharacterStream(String var1, Reader var2) throws SQLException {
      this.updateNCharacterStream(this.remoteRs.findColumn(var1), var2);
   }

   public void updateNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.updateNCharacterStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateAsciiStream(int var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateAsciiStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateAsciiStream(var1, this.bg, var6);
      }

   }

   public void updateAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : updateAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      if (var2 == null) {
         this.remoteRs.updateAsciiStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var7 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateAsciiStream(var1, this.bg, var7, var3);
      }

   }

   public void updateAsciiStream(int var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateAsciiStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateAsciiStream(var1, this.bg, var8, var3);
      }

   }

   public void updateAsciiStream(String var1, InputStream var2) throws SQLException {
      this.updateAsciiStream(this.remoteRs.findColumn(var1), var2);
   }

   public void updateAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      this.updateAsciiStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateAsciiStream(String var1, InputStream var2, long var3) throws SQLException {
      this.updateAsciiStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateBinaryStream(int var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateBinaryStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateBinaryStream(var1, this.bg, var6);
      }

   }

   public void updateBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : updateBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      if (var2 == null) {
         this.remoteRs.updateBinaryStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var7 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateBinaryStream(var1, this.bg, var7, var3);
      }

   }

   public void updateBinaryStream(int var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateBinaryStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateBinaryStream(var1, this.bg, var8, var3);
      }

   }

   public void updateBinaryStream(String var1, InputStream var2) throws SQLException {
      this.updateBinaryStream(this.remoteRs.findColumn(var1), var2);
   }

   public void updateBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      this.updateBinaryStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateBinaryStream(String var1, InputStream var2, long var3) throws SQLException {
      this.updateBinaryStream(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateBlob(int var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : updateBlob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.remoteRs.updateBlob(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateBlob(var1, this.bg, var6);
      }

   }

   public void updateBlob(int var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : updateBlob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.remoteRs.updateBlob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.remoteRs.updateBlob(var1, this.bg, var8, var3);
      }

   }

   public void updateBlob(String var1, InputStream var2) throws SQLException {
      this.updateBlob(this.remoteRs.findColumn(var1), var2);
   }

   public void updateBlob(String var1, InputStream var2, long var3) throws SQLException {
      this.updateBlob(this.remoteRs.findColumn(var1), var2, var3);
   }

   public void updateObject(int var1, Object var2) throws SQLException {
      if (var2 == null) {
         this.remoteRs.updateObject(var1, var2);
      } else {
         String var4;
         int var10;
         if (var2 instanceof Reader) {
            Reader var3 = (Reader)var2;
            if (this.rmiSettings.isVerbose()) {
               var4 = "time=" + System.currentTimeMillis() + " : updateObject";
               JdbcDebug.JDBCRMIInternal.debug(var4);
            }

            synchronized(this) {
               if (this.rbg == null) {
                  this.rbg = new ReaderBlockGetterImpl();
               }
            }

            var10 = this.rbg.register(var3, this.rmiSettings.getChunkSize());
            this.remoteRs.updateObject(var1, this.rbg, var10);
         } else if (var2 instanceof InputStream) {
            InputStream var9 = (InputStream)var2;
            if (this.rmiSettings.isVerbose()) {
               var4 = "time=" + System.currentTimeMillis() + " : updateObject";
               JdbcDebug.JDBCRMIInternal.debug(var4);
            }

            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            var10 = this.bg.register(var9, this.rmiSettings.getChunkSize());
            this.remoteRs.updateObject(var1, this.bg, var10);
         } else {
            this.remoteRs.updateObject(var1, var2);
         }
      }

   }

   public void updateObject(int var1, Object var2, int var3) throws SQLException {
      if (var2 == null) {
         this.remoteRs.updateObject(var1, (Object)var2, var3);
      } else {
         String var5;
         int var11;
         if (var2 instanceof Reader) {
            Reader var4 = (Reader)var2;
            if (this.rmiSettings.isVerbose()) {
               var5 = "time=" + System.currentTimeMillis() + " : updateObject";
               JdbcDebug.JDBCRMIInternal.debug(var5);
            }

            synchronized(this) {
               if (this.rbg == null) {
                  this.rbg = new ReaderBlockGetterImpl();
               }
            }

            var11 = this.rbg.register(var4, this.rmiSettings.getChunkSize());
            this.remoteRs.updateObject(var1, this.rbg, var11, var3);
         } else if (var2 instanceof InputStream) {
            InputStream var10 = (InputStream)var2;
            if (this.rmiSettings.isVerbose()) {
               var5 = "time=" + System.currentTimeMillis() + " : updateObject";
               JdbcDebug.JDBCRMIInternal.debug(var5);
            }

            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            var11 = this.bg.register(var10, this.rmiSettings.getChunkSize());
            this.remoteRs.updateObject(var1, this.bg, var11, var3);
         } else {
            this.remoteRs.updateObject(var1, (Object)var2, var3);
         }
      }

   }

   public void updateObject(String var1, Object var2) throws SQLException {
      if (var2 == null) {
         this.remoteRs.updateObject(var1, var2);
      } else if (!(var2 instanceof Reader) && !(var2 instanceof InputStream)) {
         this.remoteRs.updateObject(var1, var2);
      } else {
         this.updateObject(this.remoteRs.findColumn(var1), var2);
      }

   }

   public void updateObject(String var1, Object var2, int var3) throws SQLException {
      if (var2 == null) {
         this.remoteRs.updateObject(var1, var2, var3);
      } else if (!(var2 instanceof Reader) && !(var2 instanceof InputStream)) {
         this.remoteRs.updateObject(var1, var2, var3);
      } else {
         this.updateObject(this.remoteRs.findColumn(var1), var2, var3);
      }

   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      ReaderBlockGetter var5 = this.remoteRs.getReaderBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 5);
      if (var3 == -1) {
         return null;
      } else {
         ReaderHandler var4 = new ReaderHandler();
         var4.setReaderBlockGetter(var5, var3);
         return var4;
      }
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : getNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      ReaderBlockGetter var5 = this.remoteRs.getReaderBlockGetter();
      int var3 = this.remoteRs.registerStream(var1, 5);
      if (var3 == -1) {
         return null;
      } else {
         ReaderHandler var4 = new ReaderHandler();
         var4.setReaderBlockGetter(var5, var3);
         return var4;
      }
   }
}
