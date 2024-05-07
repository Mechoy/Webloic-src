package weblogic.deploy.service.datatransferhandlers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.AccessController;
import java.util.Locale;
import weblogic.deploy.common.Debug;
import weblogic.deploy.common.DeploymentConstants;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;
import weblogic.management.DomainDir;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class MultipartParser implements DeploymentConstants {
   private static final int DEFAULT_MAX_POST_SIZE = 1073741824;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private HttpURLConnection connection;
   private File dir;
   private int maxSize;
   private MultiDataStream streams;

   public MultipartParser(HttpURLConnection var1, String var2) throws IOException {
      this(var1, var2, 1073741824);
   }

   public MultipartParser(HttpURLConnection var1, String var2, int var3) throws IOException {
      this.streams = null;
      if (var1 == null) {
         throw new IllegalArgumentException("Connection cannot be null");
      } else if (var3 <= 0) {
         throw new IllegalArgumentException("maxPostSize must be positive");
      } else {
         this.connection = var1;
         this.maxSize = var3;
         String var4 = DomainDir.getTempDirForServer(ManagementService.getPropertyService(kernelId).getServerName());
         this.dir = var2 != null ? new File(var4, var2) : new File(var4);
         boolean var5 = this.dir.exists();
         if (!var5) {
            var5 = this.dir.mkdirs();
         }

         if (!var5) {
            throw new IllegalArgumentException("Could not create dir: " + this.dir);
         } else if (!this.dir.isDirectory()) {
            throw new IllegalArgumentException("Not a dir: " + this.dir);
         } else if (!this.dir.canWrite()) {
            throw new IllegalArgumentException("Not writable: " + this.dir);
         } else {
            this.parseResponse();
         }
      }
   }

   MultiDataStream getMultiDataStream() {
      return this.streams;
   }

   private void parseResponse() throws IOException {
      int var1 = this.connection.getContentLength();
      if (var1 < 0) {
         throw new IOException("Posted content doesn't set it's Content-Length");
      } else if (var1 > this.maxSize) {
         throw new IOException("Posted content exceeds max post size");
      } else {
         String var2 = this.connection.getContentType();
         if (var2 != null && var2.toLowerCase(Locale.US).startsWith("multipart/mixed")) {
            String var3 = this.connection.getHeaderField("files_header");
            if (var3 != null && var3.length() != 0) {
               MultipartHelper.FileInfo[] var4 = MultipartHelper.parseFilesHeader(var3);
               if (var4 != null && var4.length != 0) {
                  this.streams = this.readFiles(var4, this.connection.getInputStream());
               } else {
                  throw new IllegalArgumentException("No files to be read");
               }
            } else {
               throw new IllegalArgumentException("Invalid files_header value");
            }
         } else {
            throw new IOException("Posted content type isn't multipart/x-mixed-replace");
         }
      }
   }

   private MultiDataStream readFiles(MultipartHelper.FileInfo[] var1, InputStream var2) throws IOException {
      MultiDataStream var3 = DataStreamFactory.createMultiDataStream();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var3.addDataStream(this.readFile(var1[var4], var2));
      }

      return var3;
   }

   private FileDataStream readFile(MultipartHelper.FileInfo var1, InputStream var2) throws IOException {
      BufferedOutputStream var3 = null;
      int var4 = (int)var1.getSize();
      if (Debug.isServiceHttpDebugEnabled()) {
         Debug.serviceDebug(" Reading file '" + var1.getName() + "' with length : " + var4);
      }

      String var5 = var1.isZip() ? ".jar" : ".txt";
      File var6 = File.createTempFile("wl_comp", var5, this.dir);
      File var7 = var6.getParentFile();
      if (var7 != null) {
         boolean var8 = var7.exists();
         if (!var8) {
            var8 = var7.mkdirs();
         }

         if (!var8) {
            throw new IOException("Cannot create parent dir for '" + var6.getAbsolutePath());
         }
      }

      try {
         var3 = new BufferedOutputStream(new FileOutputStream(var6), 8192);
         byte[] var21 = new byte[8192];
         int var10 = var4 < var21.length ? var4 : var21.length;

         int var12;
         for(int var11 = 0; var11 != var4; var10 = var12 < var21.length ? var12 : var21.length) {
            int var9 = var2.read(var21, 0, var10);
            if (var9 == -1) {
               throw new IOException("Reached EOF");
            }

            var11 += var9;
            var3.write(var21, 0, var9);
            var12 = var4 - var11;
         }

         var3.flush();
         return createDataStream(var1, var6);
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var19) {
            }
         }

      }
   }

   private static FileDataStream createDataStream(final MultipartHelper.FileInfo var0, final File var1) {
      return new FileDataStream() {
         public String getName() {
            String var1x = var0.getName();
            return var1x != null && var1x.length() != 0 ? var1x : var1.getName();
         }

         public File getFile() {
            return var1;
         }

         public int getLength() throws IOException {
            this.validateFile();
            return (int)var1.length();
         }

         public boolean isZip() {
            return var0.isZip();
         }

         public InputStream getInputStream() throws IOException {
            this.validateFile();
            return new FileInputStream(var1);
         }

         public void close() {
            boolean var1x = var1.delete();
            if (Debug.isServiceHttpDebugEnabled()) {
               if (var1x) {
                  Debug.serviceDebug("Successfully deleted temp file : " + var1.getAbsolutePath());
               } else {
                  Debug.serviceDebug("Could not delete temp file : " + var1.getAbsolutePath());
               }
            }

         }

         private void validateFile() throws IOException {
            if (!var1.exists()) {
               throw new IOException("File '" + var1.getAbsolutePath() + "' does not exist");
            }
         }
      };
   }
}
