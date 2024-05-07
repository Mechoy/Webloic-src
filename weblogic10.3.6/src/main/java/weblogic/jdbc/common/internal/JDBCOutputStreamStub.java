package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import weblogic.rmi.extensions.server.StubDelegateInfo;

public class JDBCOutputStreamStub extends OutputStream implements JDBCOutputStream, Serializable, StubDelegateInfo {
   JDBCOutputStream remoteOs;
   boolean verbose = false;
   byte[] bytes = null;
   private int currpos = 0;
   boolean closed = false;
   int block_size;

   public JDBCOutputStreamStub(JDBCOutputStream var1, boolean var2, int var3) {
      this.remoteOs = var1;
      this.verbose = var2;
      this.block_size = var3;
   }

   public void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         if (this.bytes == null) {
            this.bytes = new byte[this.block_size];
         }

         if (this.currpos >= this.block_size) {
            this.flush();
         }

         this.bytes[this.currpos++] = (byte)var1;
      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         if (this.bytes != null && this.currpos > 0) {
            if (this.currpos >= this.block_size) {
               this.writeBlock(this.bytes);
               this.currpos = 0;
               this.remoteOs.flush();
            } else {
               byte[] var1 = new byte[this.currpos];
               System.arraycopy(this.bytes, 0, var1, 0, var1.length);
               this.writeBlock(var1);
               this.currpos = 0;
               this.remoteOs.flush();
            }
         } else {
            this.remoteOs.flush();
         }

      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Stream has been closed and cannot be used");
      } else if (var1 != null && var2 >= 0 && var2 <= var3 && var3 >= 0 && var3 <= var1.length) {
         if (var2 == 0 && var3 == var1.length) {
            this.writeBlock(var1);
         } else if (var3 != 1 && var1.length != 1) {
            if (var2 > 0 || var3 < var1.length) {
               int var4 = var3 - var2;
               byte[] var5 = new byte[var4];
               System.arraycopy(var1, var2, var5, 0, var4);
               this.writeBlock(var5);
            }
         } else {
            this.write(var1[var2]);
         }

      } else {
         throw new IOException("ERROR parameters are incorrect - no data or offset or length are incorrect");
      }
   }

   public void write(byte[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR stream has been closed and cannot be used");
      } else if (var1 == null) {
         throw new IOException("ERROR cannot accept null input");
      } else {
         try {
            this.write(var1, 0, var1.length);
         } catch (IOException var3) {
            throw new IOException(var3.getMessage());
         }
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.remoteOs.close();
         this.closed = true;
      }
   }

   public void writeBlock(byte[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         this.remoteOs.writeBlock(var1);
      }
   }

   public Object getStubDelegate() {
      return this.remoteOs;
   }
}
