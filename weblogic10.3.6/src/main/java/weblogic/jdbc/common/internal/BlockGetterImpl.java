package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;
import weblogic.jdbc.JDBCLogger;
import weblogic.rmi.server.UnicastRemoteObject;

public class BlockGetterImpl implements BlockGetter {
   private int lastId = 0;
   private final Map streams = new HashMap();

   public int register(InputStream var1, int var2) {
      InputStreamContainer var3 = new InputStreamContainer(var1, var2);
      synchronized(this) {
         ++this.lastId;
         this.streams.put(new Integer(this.lastId), var3);
         return this.lastId;
      }
   }

   public InputStream getStream(int var1) {
      InputStreamContainer var2 = null;
      InputStream var3 = null;
      synchronized(this) {
         var2 = (InputStreamContainer)this.streams.get(new Integer(var1));
      }

      if (var2 != null) {
         var3 = var2.is;
      }

      return var3;
   }

   public byte[] getBlock(int var1) {
      InputStreamContainer var2 = null;
      synchronized(this) {
         var2 = (InputStreamContainer)this.streams.get(new Integer(var1));
      }

      if (var2 == null) {
         return null;
      } else {
         synchronized(var2) {
            byte[] var4 = new byte[var2.block_size];
            int var5 = -1;

            try {
               var5 = var2.is.read(var4);
            } catch (IOException var8) {
               JDBCLogger.logStackTrace(var8);
            }

            if (var5 < 0) {
               return null;
            } else if (var5 >= var2.block_size) {
               return var4;
            } else {
               byte[] var6 = new byte[var5];
               System.arraycopy(var4, 0, var6, 0, var6.length);
               return var6;
            }
         }
      }
   }

   public boolean markSupported(int var1) {
      InputStream var2 = this.getStream(var1);
      return var2.markSupported();
   }

   public void mark(int var1, int var2) {
      InputStream var3 = this.getStream(var1);
      var3.mark(var2);
   }

   public int available(int var1) throws IOException {
      InputStream var2 = this.getStream(var1);
      return var2.available();
   }

   public void reset(int var1) throws IOException {
      InputStream var2 = this.getStream(var1);
      var2.reset();
   }

   public void close(int var1) {
      InputStreamContainer var2 = null;
      synchronized(this) {
         var2 = (InputStreamContainer)this.streams.remove(new Integer(var1));
      }

      try {
         if (var2 != null) {
            synchronized(var2) {
               var2.is.close();
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
