package weblogic.jdbc.rowset;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringBufferInputStream;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public class RowSetClob extends RowSetLob implements NClob, Serializable {
   private static final long serialVersionUID = -2055191804143683989L;
   protected char[] data;

   public RowSetClob() {
      this(new char[0]);
   }

   public RowSetClob(String var1) {
      this(var1.toCharArray());
   }

   public RowSetClob(char[] var1) {
      this.data = var1;
   }

   public RowSetClob(Clob var1) throws SQLException {
      if (var1.length() > 2147483647L) {
         throw new SQLException("RowSets cannot read CLOBs greater than 2147483647 bytes.");
      } else {
         int var2 = (int)var1.length();
         this.data = new char[var2];
         BufferedReader var3 = null;

         try {
            var3 = new BufferedReader(var1.getCharacterStream());
            int var4 = 0;

            int var16;
            for(boolean var5 = false; (var16 = var3.read(this.data, var4, var2 - var4)) > 0; var4 += var16) {
            }
         } catch (IOException var14) {
            throw new SQLException("Error reading CLOB data: " + StackTraceUtils.throwable2StackTrace(var14));
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
      return "<CLOB length: " + this.data.length + ">";
   }

   private void dumpData() {
      Debug.say("** <CLOB> length:" + this.data.length + " data: " + new String(this.data, 0, this.data.length));
   }

   char[] getData() {
      return this.data;
   }

   private void checkOffset(long var1) throws SQLException {
      if (var1 < 0L || var1 > (long)this.data.length) {
         throw new SQLException("Offset : " + var1 + " is not valid since the data length is: " + this.data.length);
      }
   }

   public long length() {
      return (long)this.data.length;
   }

   public InputStream getAsciiStream() {
      return new StringBufferInputStream(new String(this.data));
   }

   public void truncate(long var1) throws SQLException {
      if (var1 > (long)this.data.length) {
         throw new SQLException("truncate to length:" + var1 + " is larger than current size: " + this.data.length);
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
      char[] var4 = this.data;
      this.data = new char[(int)var1];
      System.arraycopy(var4, 0, this.data, 0, var3);
   }

   public String getSubString(long var1, int var3) throws SQLException {
      this.checkOffset(var1);
      int var4 = (int)var1;
      return new String(this.data, var4, Math.min(var3, this.data.length - var4));
   }

   public Reader getCharacterStream() throws SQLException {
      return new CharArrayReader(this.data);
   }

   public int setString(long var1, String var3) {
      return this.setString(var1, var3, 0, var3.length());
   }

   public int setString(long var1, String var3, int var4, int var5) {
      this.ensureLength(var1 + (long)var5);
      var3.getChars(var4, var5, this.data, (int)var1);
      return var5;
   }

   public long position(String var1, long var2) throws SQLException {
      this.checkOffset(var2);
      char[] var4 = var1.toCharArray();

      for(int var5 = (int)var2; var5 < this.data.length; ++var5) {
         boolean var6 = true;

         for(int var7 = 0; var7 < var4.length; ++var7) {
            if (this.data[var5 + var7] != var4[var7]) {
               var6 = false;
               break;
            }
         }

         if (var6) {
            return (long)(var5 + 1);
         }
      }

      return -1L;
   }

   public long position(Clob var1, long var2) throws SQLException {
      return this.position(var1.getSubString(0L, (int)var1.length()), var2);
   }

   public OutputStream setAsciiStream(final long var1) throws SQLException {
      return new OutputStream() {
         int pos = (int)var1 - 1;

         public void write(int var1x) {
            RowSetClob.this.ensureLength((long)(this.pos + 1));
            RowSetClob.this.data[this.pos++] = (char)var1x;
         }

         public void write(byte[] var1x, int var2, int var3) {
            RowSetClob.this.ensureLength((long)(this.pos + var3));

            while(var3 > 0) {
               RowSetClob.this.data[this.pos++] = (char)var1x[var2++];
               --var3;
            }

         }
      };
   }

   public Writer setCharacterStream(final long var1) throws SQLException {
      return new Writer() {
         int pos = (int)var1 - 1;

         public void write(char[] var1x, int var2, int var3) throws IOException {
            RowSetClob.this.ensureLength((long)(this.pos + var3));
            System.arraycopy(var1x, var2, RowSetClob.this.data, this.pos, var3);
            this.pos += var3;
         }

         public void flush() throws IOException {
         }

         public void close() throws IOException {
         }

         public void write(String var1x, int var2, int var3) throws IOException {
            RowSetClob.this.ensureLength((long)(this.pos + var3));
            var1x.getChars(var2, var3, RowSetClob.this.data, this.pos);
            this.pos += var3;
         }
      };
   }

   protected Object update(Connection var1, ResultSet var2, int var3, RowSetLob.UpdateHelper var4) throws SQLException {
      return var4.update(var1, var2.getClob(var3), this.data);
   }

   public void free() throws SQLException {
      this.data = null;
   }

   public Reader getCharacterStream(long var1, long var3) throws SQLException {
      return new CharArrayReader(this.data, (int)var1, (int)var3);
   }
}
