package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.utils.encoders.BASE64Encoder;

public class UserInfo {
   private byte[] salt;
   private String hash;
   private boolean saveNeeded;
   public static final String USERNAME_PROP = "username";
   public static final String PASSWORD_PROP = "password";
   public static final String HASHED_PROP = "hashed";
   private static final String HASH_ALGORITHM = "SHA-256";
   private static final String OLD_HASH_ALGORITHM = "SHA";
   private static final String HASH_ALGORITHM_TAG = "{Algorithm=SHA-256}";
   private static final String HEADER = "Node manager user information";
   private long timestamp = -1L;
   private File userFile;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public UserInfo(byte[] var1) {
      if (var1 != null && var1.length != 0) {
         this.salt = new byte[var1.length];
         System.arraycopy(var1, 0, this.salt, 0, var1.length);
      }

   }

   public UserInfo(DomainDir var1) throws IOException {
      File var2 = var1.getSaltFile();
      if (!var2.exists()) {
         var2 = var1.getOldSaltFile();
         if (!var2.exists()) {
            throw new FileNotFoundException(nmText.getDomainSaltFileNotFound());
         }
      }

      this.salt = loadSalt(var2);
   }

   public synchronized boolean verify(String var1, String var2) {
      if (this.hash == null) {
         throw new IllegalStateException("Must set username and password first");
      } else {
         if (this.timestamp <= this.userFile.lastModified() && this.userFile.canRead()) {
            try {
               this.load(this.userFile);
            } catch (IOException var4) {
            }
         }

         String var3 = null;
         if (this.hash.startsWith("{Algorithm=SHA-256}")) {
            var3 = "{Algorithm=SHA-256}" + hash("SHA-256", this.salt, var1, var2);
         } else {
            var3 = hash("SHA", this.salt, var1, var2);
         }

         return this.hash.equals(var3);
      }
   }

   public synchronized void set(String var1, String var2) {
      this.hash = "{Algorithm=SHA-256}" + hash("SHA-256", this.salt, var1, var2);
   }

   String getHash() {
      return this.hash;
   }

   public synchronized void load(File var1) throws IOException {
      if (this.timestamp <= 0L || this.timestamp < var1.lastModified()) {
         this.userFile = var1;
         Properties var2 = new Properties();
         RandomAccessFile var3 = new RandomAccessFile(var1, "r");
         FileChannel var4 = var3.getChannel();
         FileLock var5 = null;

         try {
            var5 = var4.lock(0L, var1.length(), true);
            RandomAccessFileInputStream var6 = new RandomAccessFileInputStream(var3);

            try {
               var2.load(var6);
            } finally {
               var6.close();
               if (var5 != null) {
                  var5.release();
               }

            }
         } finally {
            var4.close();
            var3.close();
         }

         String var17 = var2.getProperty("username");
         String var7 = var2.getProperty("password");
         if (var17 != null && var7 != null) {
            this.set(var17, var7);
            this.saveNeeded = true;
         } else {
            this.hash = var2.getProperty("hashed");
            if (this.hash == null) {
               throw new IllegalStateException(nmText.credentialsFileEmpty());
            }

            this.saveNeeded = false;
         }

         this.timestamp = var1.lastModified();
      }
   }

   public synchronized boolean saveNeeded() {
      return this.saveNeeded;
   }

   public synchronized void save(File var1) throws IOException {
      Properties var2 = new Properties();
      var2.setProperty("hashed", this.hash);
      RandomAccessFile var3 = new RandomAccessFile(var1, "rws");
      FileChannel var4 = var3.getChannel();

      try {
         FileLock var5 = var4.lock(0L, var1.length(), false);
         RandomAccessFileOutputStream var6 = new RandomAccessFileOutputStream(var3);

         try {
            var2.store(new RandomAccessFileOutputStream(var3), "Node manager user information");
         } finally {
            var6.close();
            if (var5 != null) {
               var5.release();
            }

         }
      } finally {
         var4.close();
         var3.close();
      }

      this.timestamp = var1.lastModified();
      this.userFile = var1;
   }

   private static String hash(String var0, byte[] var1, String var2, String var3) {
      MessageDigest var4;
      try {
         var4 = MessageDigest.getInstance(var0);
      } catch (NoSuchAlgorithmException var6) {
         throw (InternalError)(new InternalError(var0 + " digest algorithm not found")).initCause(var6);
      }

      var4.update(var1);
      var4.update(var2.getBytes());
      var4.update(var3.getBytes());
      return (new BASE64Encoder()).encodeBuffer(var4.digest());
   }

   private static byte[] loadSalt(File var0) throws IOException {
      try {
         return SerializedSystemIni.getSalt(var0.getPath());
      } catch (RuntimeException var2) {
         throw (IOException)(new IOException(var2.getMessage())).initCause(var2);
      }
   }

   private static class RandomAccessFileOutputStream extends OutputStream {
      RandomAccessFile raf;

      RandomAccessFileOutputStream(RandomAccessFile var1) {
         this.raf = var1;
      }

      public void write(int var1) throws IOException {
         this.raf.write(var1);
      }
   }

   private static class RandomAccessFileInputStream extends InputStream {
      RandomAccessFile raf;

      RandomAccessFileInputStream(RandomAccessFile var1) {
         this.raf = var1;
      }

      public int read() throws IOException {
         return this.raf.read();
      }
   }
}
