package weblogic.webservice.encoding;

import java.awt.Image;
import java.awt.Panel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import javax.xml.rpc.JAXRPCException;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.xml.schema.types.XSDBase64Binary;

/** @deprecated */
public class ImageCodec extends AttachmentCodec {
   protected Object deserializeContent(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            String var2 = (String)var1;
            byte[] var3 = XSDBase64Binary.convertXml(var2);
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            ObjectInputStream var5 = new ObjectInputStream(var4);
            int var6 = var5.readInt();
            int var7 = var5.readInt();
            int[] var8 = (int[])((int[])var5.readObject());
            MemoryImageSource var9 = new MemoryImageSource(var6, var7, var8, 0, var6);
            Panel var10 = new Panel();
            Image var11 = var10.createImage(var9);
            return var11;
         } catch (ClassNotFoundException var12) {
            throw new JAXRPCException(var12);
         } catch (OptionalDataException var13) {
            throw new JAXRPCException(var13);
         } catch (IOException var14) {
            throw new JAXRPCException(var14);
         }
      }
   }

   protected String getContentType() {
      return "text/xml";
   }

   protected Object serializeContent(Object var1) {
      Image var2 = null;
      if (var1 instanceof Image) {
         var2 = (Image)var1;
         PixelGrabber var3 = new PixelGrabber(var2, 0, 0, -1, -1, true);

         try {
            var3.grabPixels();
         } catch (InterruptedException var10) {
         }

         int[] var4 = (int[])((int[])var3.getPixels());
         int var5 = var3.getWidth();
         int var6 = var3.getHeight();
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();

         try {
            ObjectOutputStream var8 = new ObjectOutputStream(var7);
            var8.writeInt(var5);
            var8.writeInt(var6);
            var8.writeObject(var4);
         } catch (IOException var9) {
            throw new JAXRPCException("failed to serialize:" + var9, var9);
         }

         BASE64Encoder var11 = new BASE64Encoder();
         return var11.encodeBuffer(var7.toByteArray());
      } else {
         throw new JAXRPCException("Dont know how to serialize:" + var1);
      }
   }
}
