package weblogic.xml.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import javax.xml.transform.Source;
import org.xml.sax.InputSource;
import weblogic.xml.babel.reader.XmlReader;

public final class InputFormats {
   public static Reader resolveSystemID(String var0) throws IOException {
      try {
         FileInputStream var4 = new FileInputStream(var0);
         return XmlReader.createReader(var4);
      } catch (IOException var3) {
         URL var1 = new URL(var0);
         BufferedInputStream var2 = new BufferedInputStream(var1.openStream());
         return XmlReader.createReader(var2);
      }
   }

   public static Reader resolveInputSource(InputSource var0) throws IOException {
      if (var0.getCharacterStream() != null) {
         return var0.getCharacterStream();
      } else if (var0.getByteStream() != null) {
         return XmlReader.createReader(var0.getByteStream());
      } else if (var0.getSystemId() != null) {
         return resolveSystemID(var0.getSystemId());
      } else {
         throw new IOException("Unable to resolve input source.");
      }
   }

   public static Reader resolveSource(Source var0) throws IOException {
      return resolveSystemID(var0.getSystemId());
   }
}
