package weblogic.xml.security.signature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import weblogic.xml.security.transforms.NodeTransform;
import weblogic.xml.security.transforms.OctetTransform;
import weblogic.xml.security.transforms.Transform;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;

public class TransformUtils {
   private static final XMLInputStreamFactory inFactory = XMLInputStreamFactory.newInstance();

   public static void process(InputStream is, Transform t) throws IOException {
      if (t instanceof OctetTransform) {
         process(is, (OctetTransform)t);
      } else {
         if (!(t instanceof NodeTransform)) {
            throw new AssertionError("Unknown Transform type : " + t.getClass());
         }

         process(is, (NodeTransform)t);
      }

   }

   public static void process(InputStream is, OctetTransform t) throws IOException {
      byte[] buf = new byte[4096];
      OutputStream os = t.getOutputStream();
      int nread = false;

      int nread;
      while((nread = is.read(buf, 0, buf.length)) != -1) {
         os.write(buf, 0, nread);
      }

   }

   public static void process(InputStream is, NodeTransform t) throws IOException {
      XMLInputStream xmlIn = inFactory.newInputStream(is);
      XMLOutputStream xmlOut = t.getXMLOutputStream();

      while(xmlIn.hasNext()) {
         xmlOut.add(xmlIn.next());
      }

      xmlOut.close();
   }
}
