package weblogic.aspects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.EmptyEnumerator;

public class AspectSystemClassFinder implements ClassFinder {
   private AspectSystem system;

   public AspectSystemClassFinder(AspectSystem var1) {
      this.system = var1;
   }

   public Source getSource(String var1) {
      return null;
   }

   public Enumeration getSources(String var1) {
      Vector var2 = new Vector(0);
      return var2.elements();
   }

   public Source getClassSource(String var1) {
      byte[] var2 = (byte[])((byte[])this.system.getAllSources().get(var1));
      return var2 == null ? null : new AspectSystemSource(var2);
   }

   public String getClassPath() {
      return "";
   }

   public ClassFinder getManifestFinder() {
      return null;
   }

   public Enumeration entries() {
      return EmptyEnumerator.EMPTY;
   }

   public void close() {
   }

   private static class AspectSystemSource implements Source {
      private byte[] bytes;
      private long lastModified = System.currentTimeMillis();

      public AspectSystemSource(byte[] var1) {
         this.bytes = var1;
      }

      public InputStream getInputStream() throws IOException {
         return new ByteArrayInputStream(this.bytes);
      }

      public URL getURL() {
         return null;
      }

      public URL getCodeSourceURL() {
         return null;
      }

      public byte[] getBytes() {
         return this.bytes;
      }

      public long lastModified() {
         return this.lastModified;
      }

      public long length() {
         return (long)this.bytes.length;
      }
   }
}
