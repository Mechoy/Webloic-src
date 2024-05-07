package weblogic.application.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Enumeration;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.classloaders.URLSource;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.SingleItemEnumeration;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public final class MergedDescriptorFinder extends URLStreamHandler implements ClassFinder {
   private static final String PROTOCOL_NAME = "merged-descriptor";
   private final Map<String, DescriptorBean> descriptors;

   public MergedDescriptorFinder(Map<String, DescriptorBean> var1) {
      this.descriptors = var1;
   }

   public Source getSource(String var1) {
      DescriptorBean var2 = (DescriptorBean)this.descriptors.get(var1);
      if (var2 != null) {
         try {
            return new URLSource(new URL("merged-descriptor", (String)null, -1, var1, this));
         } catch (IOException var4) {
         }
      }

      return null;
   }

   public Enumeration getSources(String var1) {
      Source var2 = this.getSource(var1);
      return (Enumeration)(var2 == null ? new EmptyEnumerator() : new SingleItemEnumeration(var2));
   }

   public Source getClassSource(String var1) {
      return null;
   }

   public String getClassPath() {
      return "";
   }

   public ClassFinder getManifestFinder() {
      return NullClassFinder.NULL_FINDER;
   }

   public Enumeration entries() {
      return EmptyEnumerator.EMPTY;
   }

   public void close() {
      this.descriptors.clear();
   }

   protected URLConnection openConnection(URL var1) throws IOException {
      DescriptorBean var2 = (DescriptorBean)this.descriptors.get(var1.getFile());
      if (var2 == null) {
         throw new IOException("Merged descriptor not found " + var1.getFile());
      } else {
         return new DescriptorURLConnection(var1, var2);
      }
   }

   protected String toExternalForm(URL var1) {
      return "merged-descriptor:/" + var1.getFile();
   }

   static class DescriptorURLConnection extends URLConnection {
      public static final EditableDescriptorManager descMgr = new EditableDescriptorManager();
      private DescriptorBean descriptor;

      public DescriptorURLConnection(URL var1, DescriptorBean var2) {
         super(var1);
         this.descriptor = var2;
      }

      public void connect() throws IOException {
      }

      public InputStream getInputStream() throws IOException {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         descMgr.writeDescriptorAsXML(this.descriptor.getDescriptor(), var1);
         return new UnsyncByteArrayInputStream(var1.toByteArray());
      }
   }
}
