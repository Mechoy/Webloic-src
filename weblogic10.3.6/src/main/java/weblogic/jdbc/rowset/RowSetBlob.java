package weblogic.jdbc.rowset;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import weblogic.utils.StackTraceUtils;

public final class RowSetBlob extends RowSetLob implements Blob, Serializable {
   private static final long serialVersionUID = -2526507456741346804L;
   private byte[] data;

   public RowSetBlob() {
      this(new byte[0]);
   }

   public RowSetBlob(byte[] var1) {
      this.data = var1;
   }

   public RowSetBlob(Blob var1) throws SQLException {
      if (var1.length() > 2147483647L) {
         throw new SQLException("RowSets cannot read BLOBs greater than 2147483647 bytes.");
      } else {
         int var2 = (int)var1.length();
         this.data = new byte[var2];
         BufferedInputStream var3 = null;

         try {
            var3 = new BufferedInputStream(var1.getBinaryStream());
            int var4 = 0;

            int var16;
            for(boolean var5 = false; (var16 = var3.read(this.data, var4, var2 - var4)) > 0; var4 += var16) {
            }
         } catch (IOException var14) {
            throw new SQLException("Error reading BLOB data: " + StackTraceUtils.throwable2StackTrace(var14));
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Exception var13) {
               }
            }

         }

      }
   }

   public String toString() {
      return "<BLOB length: " + this.data.length + ">";
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

   public OutputStream setBinaryStream(final long var1) throws SQLException {
      return new OutputStream() {
         int pos = (int)var1 - 1;

         public void write(int var1x) {
            RowSetBlob.this.ensureLength((long)(this.pos + 1));
            RowSetBlob.this.data[this.pos++] = (byte)var1x;
         }

         public void write(byte[] var1x, int var2, int var3) {
            this.pos += RowSetBlob.this.setBytes((long)this.pos, var1x, var2, var3);
         }
      };
   }

   public void truncate(long var1) throws SQLException {
      if (var1 > (long)this.data.length) {
         throw new SQLException("truncate to length: " + var1 + " is larger than current size: " + this.data.length);
      } else {
         this.changeLength(var1, (int)var1);
      }
   }

   byte[] getData() {
      return this.data;
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

   protected Object update(Connection var1, ResultSet var2, int var3, RowSetLob.UpdateHelper var4) throws SQLException {
      return var4.update(var1, var2.getBlob(var3), this.data);
   }

   public void free() throws SQLException {
      this.data = null;
   }

   public InputStream getBinaryStream(long var1, long var3) throws SQLException {
      return new ByteArrayInputStream(this.data, (int)var1, (int)var3);
   }
}
