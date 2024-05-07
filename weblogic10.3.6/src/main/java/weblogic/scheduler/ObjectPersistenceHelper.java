package weblogic.scheduler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import weblogic.jdbc.vendor.oracle.OracleThinBlob;
import weblogic.store.io.jdbc.JDBCHelper;

final class ObjectPersistenceHelper implements Blob, Serializable {
   private byte[] data;
   private static final int DEFAULT_BUF_SIZE = 1000;

   ObjectPersistenceHelper() {
      this.data = new byte[0];
   }

   ObjectPersistenceHelper(Serializable var1) throws IOException {
      this.data = getBytes(var1);
   }

   static byte[] getBytes(Serializable var0) throws IOException {
      ByteArrayOutputStream var1 = null;
      ObjectOutputStream var2 = null;

      byte[] var3;
      try {
         var1 = new ByteArrayOutputStream(1000);
         var2 = new ObjectOutputStream(var1);
         var2.writeObject(var0);
         var2.flush();
         var3 = var1.toByteArray();
      } finally {
         if (var2 != null) {
            var2.close();
         }

         if (var1 != null) {
            var1.close();
         }

      }

      return var3;
   }

   static Blob getBlob(Serializable var0) throws IOException {
      return new ObjectPersistenceHelper(var0);
   }

   static void writeToBlob(Blob var0, Serializable var1) throws SQLException, IOException {
      OutputStream var2 = null;

      try {
         if (var0 instanceof OracleThinBlob) {
            var2 = ((OracleThinBlob)var0).getBinaryOutputStream();
         } else {
            var2 = var0.setBinaryStream(1L);
         }

         byte[] var3 = getBytes(var1);
         var2.write(var3);
         var2.flush();
         var2.close();
         var2 = null;
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var10) {
         }

      }

   }

   static Object getObject(Blob var0) throws SQLException, IOException {
      assert var0 != null;

      return getObject(var0.getBytes(1L, (int)var0.length()));
   }

   static Object getObject(byte[] var0) throws SQLException, IOException {
      SchedulerObjectInputStream var1 = null;
      ByteArrayInputStream var2 = null;

      Object var3;
      try {
         var2 = new ByteArrayInputStream(var0);
         var1 = new SchedulerObjectInputStream(var2);
         var3 = var1.readObject();
      } catch (ClassNotFoundException var8) {
         throw new IOException(var8.getMessage());
      } finally {
         if (var1 != null) {
            var1.close();
         }

         if (var2 != null) {
            var2.close();
         }

      }

      return var3;
   }

   public long length() {
      return (long)this.data.length;
   }

   public byte[] getBytes(long var1, int var3) throws SQLException {
      if (var1 >= 0L && var1 <= (long)this.data.length) {
         if (var3 < 0) {
            throw new SQLException("length must be positive");
         } else {
            byte[] var4 = new byte[var3];
            System.arraycopy(this.data, (int)var1, var4, 0, var3);
            return var4;
         }
      } else {
         throw new SQLException("Position: " + var1 + " must be >=0 and less than Blob.length()");
      }
   }

   public InputStream getBinaryStream() {
      return new ByteArrayInputStream(this.data);
   }

   public long position(byte[] var1, long var2) throws SQLException {
      if (var2 >= 0L && var2 < (long)this.data.length) {
         for(int var4 = (int)var2; var4 < this.data.length; ++var4) {
            boolean var5 = true;

            for(int var6 = 0; var6 < var1.length; ++var6) {
               if (this.data[var4 + var6] != var1[var6]) {
                  var5 = false;
                  break;
               }
            }

            if (var5) {
               return (long)(var4 + 1);
            }
         }

         return -1L;
      } else {
         throw new SQLException("start must be >= 0 and < Blob.length()");
      }
   }

   public long position(Blob var1, long var2) throws SQLException {
      if (var1.length() > 2147483647L) {
         throw new SQLException("RowSets cannot read BLOBs greater than 2147483647 bytes.");
      } else {
         return this.position(var1.getBytes(0L, (int)var1.length()), var2);
      }
   }

   public int setBytes(long var1, byte[] var3) {
      return this.setBytes(var1, var3, 0, var3.length);
   }

   public int setBytes(long var1, byte[] var3, int var4, int var5) {
      this.ensureLength(var1 + (long)var5);
      System.arraycopy(var3, var4, this.data, (int)var1, var5);
      return var5;
   }

   public OutputStream setBinaryStream(final long var1) {
      return new OutputStream() {
         int pos = (int)var1 - 1;

         public void write(int var1x) {
            ObjectPersistenceHelper.this.ensureLength((long)(this.pos + 1));
            ObjectPersistenceHelper.this.data[this.pos++] = (byte)var1x;
         }

         public void write(byte[] var1x, int var2, int var3) {
            this.pos += ObjectPersistenceHelper.this.setBytes((long)this.pos, var1x, var2, var3);
         }
      };
   }

   public void free() throws SQLException {
      throw new SQLException("Java SE 6.0 method free is not supported");
   }

   public InputStream getBinaryStream(long var1, long var3) throws SQLException {
      throw new SQLException("Java SE 6.0 method getBinaryStream is not supported");
   }

   public void truncate(long var1) throws SQLException {
      if (var1 > (long)this.data.length) {
         throw new SQLException("truncate to length: " + var1 + " is larger than current size: " + this.data.length);
      } else {
         this.changeLength(var1, (int)var1);
      }
   }

   private void ensureLength(long var1) {
      if (var1 > (long)this.data.length) {
         this.changeLength(var1, this.data.length);
      }

   }

   private void changeLength(long var1, int var3) {
      byte[] var4 = this.data;
      this.data = new byte[(int)var1];
      System.arraycopy(var4, 0, this.data, 0, var3);
   }

   static boolean isOracleBlobRecord(Connection var0, ResultSet var1, int var2) throws SQLException {
      return JDBCHelper.isOracleBlobColumn(JDBCHelper.getDBMSType(var0.getMetaData(), (String[])null), var1, var2);
   }

   static boolean mustSelectForInsert(Connection var0) throws SQLException {
      return JDBCHelper.mustSelectForUpdateToInsertBinary(JDBCHelper.getDBMSType(var0.getMetaData(), (String[])null), var0.getMetaData());
   }

   static Object getObject(Connection var0, ResultSet var1, int var2) throws SQLException, IOException {
      return isOracleBlobRecord(var0, var1, var2) ? getObject(var1.getBlob(var2)) : getObject(var1.getBytes(var2));
   }

   private static class SchedulerObjectInputStream extends ObjectInputStream {
      private SchedulerObjectInputStream(InputStream var1) throws IOException {
         super(var1);
      }

      protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
         ClassLoader var2 = Thread.currentThread().getContextClassLoader();
         return Class.forName(var1.getName(), false, var2);
      }

      // $FF: synthetic method
      SchedulerObjectInputStream(InputStream var1, Object var2) throws IOException {
         this(var1);
      }
   }
}
