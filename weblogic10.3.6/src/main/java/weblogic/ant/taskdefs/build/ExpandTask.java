package weblogic.ant.taskdefs.build;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public final class ExpandTask extends Task {
   private String src;
   private String dest = ".";

   public void setSrc(String var1) {
      this.src = var1;
   }

   public void setDest(String var1) {
      this.dest = var1;
   }

   public void execute() throws BuildException {
      ZipInputStream var1 = null;
      ByteBuffer var2 = ByteBuffer.allocate(3000000);
      File var3 = new File(this.dest);

      try {
         var1 = new ZipInputStream(new BufferedInputStream(new FileInputStream(this.src), 512));

         while(true) {
            while(true) {
               ZipEntry var4 = var1.getNextEntry();
               if (var4 == null) {
                  return;
               }

               File var5 = new File(var3, var4.getName());
               if (var4.isDirectory()) {
                  ensureDir(var5);
               } else {
                  ensureDir(var5.getParentFile());
                  byte[] var6 = var2.array();
                  int var7 = var6.length;
                  int var8 = 0;

                  while(true) {
                     int var9 = var1.read(var6, var8, var7);
                     if (var9 == -1) {
                        var1.closeEntry();
                        var2.limit(var8);
                        FileChannel var19 = (new RandomAccessFile(var5, "rw")).getChannel();
                        var19.map(MapMode.READ_WRITE, 0L, (long)var8).put(var2);
                        var19.close();
                        var2.clear();
                        break;
                     }

                     var8 += var9;
                     var7 -= var9;
                     if (var7 == 0) {
                        var2 = ByteBuffer.allocate(2 * var8);
                        System.out.println("expanding from " + var6.length + " to " + var2.capacity() + " for " + var5);
                        var2.put(var6, 0, var8);
                        var6 = var2.array();
                        var7 = var6.length - var8;
                     }
                  }
               }
            }
         }
      } catch (IOException var17) {
         throw new BuildException(var17);
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var16) {
         }

      }
   }

   private static void ensureDir(File var0) throws IOException {
      if (var0 != null) {
         if (!var0.exists() || !var0.isDirectory()) {
            if (!var0.mkdirs()) {
               throw new IOException("failed to create directory " + var0.getPath());
            }
         }
      }
   }

   public static void main(String[] var0) throws Exception {
      ExpandTask var1 = new ExpandTask();
      var1.setDest(".");
      var1.setSrc(var0[0]);
      var1.execute();
   }
}
