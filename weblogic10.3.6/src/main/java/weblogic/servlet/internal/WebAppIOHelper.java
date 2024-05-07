package weblogic.servlet.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.DescriptorCache;

abstract class WebAppIOHelper implements DescriptorCache.IOHelper {
   protected final War.ResourceLocation resLocation;

   WebAppIOHelper(War.ResourceLocation var1) {
      this.resLocation = var1;
   }

   public final Object parseXML(InputStream var1) throws IOException, XMLStreamException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(WebAppIOHelper.class.getClassLoader());

      Object var5;
      try {
         XMLInputFactory var3 = XMLInputFactory.newInstance();
         XMLStreamReader var4 = var3.createXMLStreamReader(var1);
         var5 = this.parseXMLInternal(var4);
      } finally {
         Thread.currentThread().setContextClassLoader(var2);
      }

      return var5;
   }

   protected abstract Object parseXMLInternal(XMLStreamReader var1) throws IOException, XMLStreamException;

   public InputStream openInputStream() throws IOException {
      return this.resLocation.getInputStream();
   }

   public Object readCachedBean(File var1) throws IOException {
      ObjectInputStream var2 = null;

      Object var3;
      try {
         var2 = new ObjectInputStream(new FileInputStream(var1));
         var3 = var2.readObject();
      } catch (ClassNotFoundException var12) {
         throw (IOException)(new IOException(var12.getMessage())).initCause(var12);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var11) {
            }
         }

      }

      return var3;
   }

   public boolean useCaching() {
      return true;
   }
}
