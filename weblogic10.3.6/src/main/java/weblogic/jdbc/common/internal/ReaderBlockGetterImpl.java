package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.Reader;
import java.rmi.NoSuchObjectException;
import java.util.Hashtable;
import weblogic.jdbc.JDBCLogger;
import weblogic.rmi.server.UnicastRemoteObject;

public class ReaderBlockGetterImpl implements ReaderBlockGetter {
   private int lastId = 0;
   private int block_size = 1024;
   private Hashtable readers = new Hashtable();

   public int register(Reader var1, int var2) {
      ReaderContainer var3 = new ReaderContainer(var1, var2);
      synchronized(this) {
         ++this.lastId;
         this.readers.put("" + this.lastId, var3);
         return this.lastId;
      }
   }

   public Reader getReader(int var1) {
      ReaderContainer var2 = null;
      Reader var3 = null;
      synchronized(this) {
         var2 = (ReaderContainer)this.readers.get("" + var1);
      }

      if (var2 != null) {
         var3 = var2.rdr;
      }

      return var3;
   }

   public char[] getBlock(int var1) {
      ReaderContainer var2 = null;
      synchronized(this) {
         var2 = (ReaderContainer)this.readers.get("" + var1);
      }

      if (var2 == null) {
         return null;
      } else {
         synchronized(var2) {
            char[] var4 = new char[var2.block_size];
            int var5 = -1;

            try {
               var5 = var2.rdr.read(var4);
            } catch (IOException var8) {
               JDBCLogger.logStackTrace(var8);
            }

            if (var5 < 0) {
               return null;
            } else if (var5 >= var2.block_size) {
               return var4;
            } else {
               char[] var6 = new char[var5];
               System.arraycopy(var4, 0, var6, 0, var6.length);
               return var6;
            }
         }
      }
   }

   public int getBlockSize() {
      return this.block_size;
   }

   public boolean markSupported(int var1) {
      Reader var2 = this.getReader(var1);
      return var2.markSupported();
   }

   public void mark(int var1, int var2) throws IOException {
      Reader var3 = this.getReader(var1);
      var3.mark(var2);
   }

   public boolean ready(int var1) throws IOException {
      Reader var2 = this.getReader(var1);
      return var2.ready();
   }

   public void reset(int var1) throws IOException {
      Reader var2 = this.getReader(var1);
      var2.reset();
   }

   public void close(int var1) {
      ReaderContainer var2 = null;
      synchronized(this) {
         var2 = (ReaderContainer)this.readers.remove("" + var1);
      }

      try {
         if (var2 != null) {
            synchronized(var2) {
               var2.rdr.close();
            }
         }
      } catch (IOException var7) {
      }

   }

   public void close() {
      try {
         UnicastRemoteObject.unexportObject(this, true);
      } catch (NoSuchObjectException var2) {
      }

   }
}
