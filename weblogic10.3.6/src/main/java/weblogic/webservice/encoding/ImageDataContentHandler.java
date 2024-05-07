package weblogic.webservice.encoding;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/** @deprecated */
public class ImageDataContentHandler implements DataContentHandler {
   private static final String IMAGE_GIF = "image/gif";
   private static final String IMAGE_JPEG = "image/jpeg";

   public DataFlavor[] getTransferDataFlavors() {
      DataFlavor[] var1 = new DataFlavor[]{new ActivationDataFlavor(byte[].class, "image/gif", "image/gif"), new ActivationDataFlavor(byte[].class, "image/jpeg", "image/jpeg")};
      return var1;
   }

   public Object getTransferData(DataFlavor var1, DataSource var2) throws UnsupportedFlavorException, IOException {
      if (!var1.isMimeTypeEqual("image/gif") && !var1.isMimeTypeEqual("image/jpeg")) {
         throw new UnsupportedFlavorException(var1);
      } else if (var1.getRepresentationClass().isAssignableFrom(Byte.TYPE)) {
         InputStream var3 = var2.getInputStream();
         return getByteArrayFromInputStream(var3);
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   public Object getContent(DataSource var1) throws IOException {
      InputStream var2 = var1.getInputStream();
      return getByteArrayFromInputStream(var2);
   }

   public void writeTo(Object var1, String var2, OutputStream var3) throws IOException {
      MimeType var4 = null;

      try {
         var4 = new MimeType(var2);
         if (!var4.match("image/gif") && !var4.match("image/jpeg")) {
            throw new IOException("MimeType should be image/gif or image/jpeg, and " + var2 + "is not suported.");
         }
      } catch (MimeTypeParseException var7) {
         throw new IOException(var7.toString());
      }

      if (var1 instanceof byte[]) {
         var3.write((byte[])((byte[])var1));
         var3.flush();
      } else if (var1 instanceof InputStream) {
         InputStream var5 = (InputStream)var1;
         this.write(var5, var3);
         var5.close();
         var3.close();
      } else {
         if (!(var1 instanceof FileDataSource)) {
            throw new IOException(var1.getClass().getName() + ":is not supported.");
         }

         FileDataSource var8 = (FileDataSource)var1;
         InputStream var6 = var8.getInputStream();
         this.write(var6, var3);
         var6.close();
         var3.close();
      }

   }

   private void write(InputStream var1, OutputStream var2) throws IOException {
      byte[] var4 = new byte[2048];

      int var3;
      while((var3 = var1.read(var4)) != -1) {
         var2.write(var4, 0, var3);
      }

   }

   private static byte[] getByteArrayFromInputStream(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      byte[] var2 = new byte[2048];

      int var3;
      while((var3 = var0.read(var2)) != -1) {
         var1.write(var2, 0, var3);
      }

      return var1.toByteArray();
   }
}
