package weblogic.nodemanager.mbean;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class TempFileReader extends FileReader {
   private File file;
   private boolean eof;
   private boolean closed;
   private static HashMap files = new HashMap();

   public TempFileReader(File var1) throws IOException {
      super(var1);
      this.file = var1;
      synchronized(files) {
         if (files.containsKey(var1)) {
            throw new IllegalArgumentException("A reader is already active for the file");
         } else {
            files.put(var1, this);
         }
      }
   }

   public int read() throws IOException {
      synchronized(this.lock) {
         if (this.closed) {
            throw new IOException("Stream is closed");
         } else if (this.eof) {
            return -1;
         } else {
            int var2 = super.read();
            if (var2 == -1) {
               this.eof = true;
               this.deleteFile();
            }

            return var2;
         }
      }
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      synchronized(this.lock) {
         if (this.closed) {
            throw new IOException("Stream is closed");
         } else if (this.eof) {
            return -1;
         } else {
            var3 = super.read(var1, var2, var3);
            if (var3 == -1) {
               this.eof = true;
               this.deleteFile();
            }

            return var3;
         }
      }
   }

   public int read(char[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public long skip(long var1) throws IOException {
      synchronized(this.lock) {
         if (this.closed) {
            throw new IOException("Stream is closed");
         } else if (this.eof) {
            return -1L;
         } else {
            var1 = super.skip(var1);
            if (var1 == -1L) {
               this.eof = true;
               this.deleteFile();
            }

            return var1;
         }
      }
   }

   public void close() {
      synchronized(this.lock) {
         this.closed = true;
         this.deleteFile();
      }
   }

   private void deleteFile() {
      try {
         super.close();
      } catch (IOException var2) {
      }

      this.file.delete();
   }

   static {
      Runtime.getRuntime().addShutdownHook(new ShutdownHook());
   }

   private static class ShutdownHook extends Thread {
      private ShutdownHook() {
      }

      public void run() {
         File var3;
         for(Iterator var1 = TempFileReader.files.entrySet().iterator(); var1.hasNext(); var3.delete()) {
            Map.Entry var2 = (Map.Entry)var1.next();
            var3 = (File)var2.getKey();
            FileReader var4 = (FileReader)var2.getValue();

            try {
               var4.close();
            } catch (IOException var6) {
            }
         }

      }

      // $FF: synthetic method
      ShutdownHook(Object var1) {
         this();
      }
   }
}
