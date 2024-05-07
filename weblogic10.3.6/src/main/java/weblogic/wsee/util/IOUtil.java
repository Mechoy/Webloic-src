package weblogic.wsee.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class IOUtil {
   private static final boolean verbose = Verbose.isVerbose(IOUtil.class);

   public static String toString(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("null InputStream");
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         int var2;
         while((var2 = var0.read()) != -1) {
            var1.write(var2);
         }

         var1.flush();
         return new String(var1.toByteArray());
      }
   }

   public static String toString(Reader var0) throws IOException {
      StringBuilder var1 = new StringBuilder();

      int var2;
      while((var2 = var0.read()) != -1) {
         var1.append(var2);
      }

      return var1.toString();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   public static OutputStream createEncodedFileOutputStream(File var0, String var1) throws FileNotFoundException, UnsupportedEncodingException {
      FileOutputStream var2;
      try {
         var2 = new FileOutputStream(var0);
      } catch (FileNotFoundException var8) {
         throw var8;
      }

      try {
         PrintStream var3 = var1 == null ? new PrintStream(var2, true) : new PrintStream(var2, true, var1);
         return var3;
      } catch (UnsupportedEncodingException var7) {
         try {
            var2.close();
         } catch (IOException var6) {
            var6.printStackTrace();
         }

         throw var7;
      }
   }

   public static Writer createEncodedFileWriter(File var0, String var1) throws FileNotFoundException, UnsupportedEncodingException {
      FileOutputStream var2;
      try {
         var2 = new FileOutputStream(var0);
      } catch (FileNotFoundException var8) {
         throw var8;
      }

      try {
         OutputStreamWriter var3 = var1 == null ? new OutputStreamWriter(var2) : new OutputStreamWriter(var2, var1);
         return var3;
      } catch (UnsupportedEncodingException var7) {
         try {
            var2.close();
         } catch (IOException var6) {
            var6.printStackTrace();
         }

         throw var7;
      }
   }
}
