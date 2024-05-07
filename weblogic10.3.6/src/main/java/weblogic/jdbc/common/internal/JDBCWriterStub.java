package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import weblogic.rmi.extensions.server.StubDelegateInfo;

public class JDBCWriterStub extends Writer implements JDBCWriter, Serializable, StubDelegateInfo {
   JDBCWriter remoteWtr;
   boolean verbose = false;
   char[] chars = null;
   private int currpos = 0;
   boolean closed = false;
   int block_size;

   public JDBCWriterStub(JDBCWriter var1, boolean var2, int var3) {
      this.remoteWtr = var1;
      this.block_size = var3;
      this.verbose = var2;
   }

   public void close() throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else {
         this.remoteWtr.close();
         this.closed = true;
      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else {
         if (this.chars != null && this.currpos > 0) {
            if (this.currpos >= this.block_size) {
               this.writeBlock(this.chars);
               this.remoteWtr.flush();
               this.currpos = 0;
            } else {
               char[] var1 = new char[this.currpos];
               System.arraycopy(this.chars, 0, var1, 0, var1.length);
               this.writeBlock(var1);
               this.remoteWtr.flush();
               this.currpos = 0;
            }
         } else {
            this.remoteWtr.flush();
         }

      }
   }

   public void writeBlock(char[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else {
         this.remoteWtr.writeBlock(var1);
      }
   }

   public void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR OutputStream has been closed and cannot be used");
      } else {
         if (this.chars == null) {
            this.chars = new char[this.block_size];
         }

         if (this.currpos >= this.block_size) {
            this.flush();
         }

         this.chars[this.currpos++] = (char)var1;
      }
   }

   public void write(String var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else if (var1 == null) {
         throw new IOException("ERROR cannot accept null input");
      } else {
         char[] var2 = var1.toCharArray();

         try {
            this.write((char[])var2, 0, var2.length);
         } catch (IOException var4) {
            throw new IOException(var4.getMessage());
         }
      }
   }

   public void write(String var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else if (var1 == null) {
         throw new IOException("ERROR cannot accept null input");
      } else if (var2 >= var3) {
         throw new IOException("ERROR offset (" + var2 + ") cannot exceed length " + var3);
      } else if (var3 > var1.length()) {
         throw new IOException("ERROR offset (" + var3 + ") cannot exceed input length" + var1.length());
      } else {
         char[] var4 = var1.toCharArray();

         try {
            this.write(var4, var2, var3);
         } catch (IOException var6) {
            throw new IOException(var6.getMessage());
         }
      }
   }

   public void write(char[] var1) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else if (var1 == null) {
         throw new IOException("ERROR cannot accept null input");
      } else {
         try {
            this.write((char[])var1, 0, var1.length);
         } catch (IOException var3) {
            throw new IOException(var3.getMessage());
         }
      }
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("ERROR Writer has been closed and cannot be used");
      } else if (var1 != null && var2 >= 0 && var2 <= var3 && var3 >= 0 && var3 <= var1.length) {
         if (var2 == 0 && var3 == var1.length) {
            this.writeBlock(var1);
         } else if (var3 != 1 && var1.length != 1) {
            if (var2 > 0 || var3 < var1.length) {
               int var4 = var3 - var2;
               char[] var5 = new char[var4];
               System.arraycopy(var1, var2, var5, 0, var4);
               this.writeBlock(var5);
            }
         } else {
            this.write(var1[var2]);
         }

      } else {
         throw new IOException("ERROR parameters are incorrect - no data or offset or length are incorrct");
      }
   }

   public Object getStubDelegate() {
      return this.remoteWtr;
   }
}
