package weblogic.xml.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import org.xml.sax.InputSource;

public class InputSourceUtil {
   public static byte[] getInputByteData(InputSource var0) throws IOException {
      InputStream var1 = null;
      Object var2 = null;
      var1 = var0.getByteStream();
      if (var1 == null) {
         return null;
      } else {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         byte[] var4 = new byte[1024];
         boolean var5 = false;

         byte[] var10;
         try {
            int var11;
            while((var11 = var1.read(var4)) != -1) {
               var3.write(var4, 0, var11);
            }

            var10 = var3.toByteArray();
         } finally {
            if (var1 != null) {
               var1.close();
            }

            if (var3 != null) {
               var3.close();
            }

         }

         return var10;
      }
   }

   public static char[] getInputCharData(InputSource var0) throws IOException {
      Reader var1 = null;
      Object var2 = null;
      var1 = var0.getCharacterStream();
      if (var1 == null) {
         return null;
      } else {
         CharArrayWriter var3 = new CharArrayWriter();
         char[] var4 = new char[1024];
         boolean var5 = false;

         char[] var10;
         try {
            int var11;
            while((var11 = var1.read(var4)) != -1) {
               var3.write(var4, 0, var11);
            }

            var10 = var3.toCharArray();
         } finally {
            if (var1 != null) {
               var1.close();
            }

            if (var3 != null) {
               var3.close();
            }

         }

         return var10;
      }
   }

   public static byte[] forceGetInputByteData(InputSource var0) throws IOException, UnsupportedEncodingException {
      byte[] var1 = getInputByteData(var0);
      if (var1 == null) {
         char[] var2 = getInputCharData(var0);
         if (var2 != null) {
            var1 = (new String(var2)).getBytes(var0.getEncoding());
         }
      }

      return var1;
   }

   public static boolean isEqual(InputSource var0, InputSource var1) throws IOException, IllegalArgumentException {
      boolean var4;
      try {
         byte[] var2 = var0 != null ? forceGetInputByteData(var0) : null;
         byte[] var3 = var1 != null ? forceGetInputByteData(var1) : null;
         var4 = Arrays.equals(var2, var3);
      } finally {
         resetInputSource(var0);
         resetInputSource(var1);
      }

      return var4;
   }

   public static void resetInputSource(InputSource var0) throws IOException, IllegalArgumentException {
      if (var0 != null) {
         InputStream var1 = var0.getByteStream();
         Reader var2 = var0.getCharacterStream();
         if (var1 != null) {
            if (!(var1 instanceof ByteArrayInputStream)) {
               throw new IllegalArgumentException("no byte array input stream in input source: " + var1.getClass().getName());
            }

            var1.reset();
         } else if (var2 != null) {
            if (!(var2 instanceof CharArrayReader)) {
               throw new IllegalArgumentException("no char array reader stream in input source: " + var2.getClass().getName());
            }

            var2.reset();
         }

      }
   }

   public static void transformInputSource(InputSource var0) throws IOException {
      if (var0 != null) {
         InputStream var1 = var0.getByteStream();
         Reader var2 = var0.getCharacterStream();
         if (var1 != null) {
            if (!(var1 instanceof ByteArrayInputStream)) {
               byte[] var3 = getInputByteData(var0);
               ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
               var0.setByteStream(var4);
            }
         } else if (var2 != null && !(var2 instanceof CharArrayReader2)) {
            char[] var6 = getInputCharData(var0);
            CharArrayReader2 var5 = new CharArrayReader2(var6);
            var0.setCharacterStream(var5);
         }

      }
   }

   private static class CharArrayReader2 extends CharArrayReader {
      public CharArrayReader2(char[] var1) {
         super(var1);
      }

      public CharArrayReader2(char[] var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      public void close() {
      }

      public void forceClose() {
         super.close();
      }
   }
}
