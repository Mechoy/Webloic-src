package weblogic.deploy.service.datatransferhandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import weblogic.deploy.service.DataStream;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;

class DataStreamFactory {
   public static final int UNKNOWN_LENGTH = -1;
   public static final String UNKNOWN_LOCATION = "unknwown";

   static DataStream createDataStream(final String var0, final InputStream var1, final boolean var2) {
      return new DataStream() {
         public InputStream getInputStream() throws IOException {
            return var1;
         }

         public void close() {
         }

         public String getName() {
            return var0;
         }

         public boolean isZip() {
            return var2;
         }
      };
   }

   static FileDataStream createFileDataStream(String var0, boolean var1) {
      return createFileDataStream((String)null, (String)var0, var1);
   }

   static FileDataStream createFileDataStream(String var0, String var1, boolean var2) {
      return createFileDataStream(var0, new File(var1), var2);
   }

   static FileDataStream createFileDataStream(File var0, boolean var1) {
      return createFileDataStream((String)null, (File)var0, var1);
   }

   static FileDataStream createFileDataStream(final String var0, final File var1, final boolean var2) {
      return new FileDataStream() {
         public String getName() {
            return var0 == null ? var1.getName() : var0;
         }

         public File getFile() {
            return var1;
         }

         public int getLength() throws IOException {
            this.validateFile();
            return (int)var1.length();
         }

         public boolean isZip() {
            return var2;
         }

         public InputStream getInputStream() throws IOException {
            this.validateFile();
            return new FileInputStream(var1);
         }

         public void close() {
         }

         private void validateFile() throws IOException {
            if (!var1.exists()) {
               throw new IOException("File '" + var1.getAbsolutePath() + "' does not exist");
            }
         }
      };
   }

   static MultiDataStream createMultiDataStream() {
      return new MultiDataStreamImpl();
   }
}
