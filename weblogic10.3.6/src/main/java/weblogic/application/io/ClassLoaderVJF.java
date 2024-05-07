package weblogic.application.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.Iterators;
import weblogic.utils.jars.VirtualJarFile;

public final class ClassLoaderVJF implements VirtualJarFile {
   private final String uri;
   private final String prefix;
   private final GenericClassLoader cl;
   private final File[] rootFiles;

   public ClassLoaderVJF(String var1, GenericClassLoader var2, File[] var3) {
      this.uri = var1;
      this.prefix = var1 + "#";
      this.cl = var2;
      this.rootFiles = var3;
   }

   public String getName() {
      return this.uri;
   }

   public void close() throws IOException {
   }

   public URL getResource(String var1) {
      return this.cl.getResource(this.prefix + var1);
   }

   public ZipEntry getEntry(String var1) {
      URL var2 = this.getResource(var1);
      return var2 == null ? null : new URLZipEntry(var2);
   }

   public Iterator getEntries(String var1) throws IOException {
      ArrayList var2 = new ArrayList();
      Enumeration var3 = this.cl.getResources(this.prefix + var1);

      while(var3.hasMoreElements()) {
         var2.add(new URLZipEntry((URL)var3.nextElement()));
      }

      return var2.iterator();
   }

   public InputStream getInputStream(ZipEntry var1) throws IOException {
      URL var2 = ((URLZipEntry)var1).getURL();
      return var2.openStream();
   }

   public Manifest getManifest() throws IOException {
      throw new AssertionError("getManifest not supported");
   }

   public File[] getRootFiles() {
      return this.rootFiles;
   }

   public boolean isDirectory() {
      throw new AssertionError("isDirectory not supported");
   }

   public JarFile getJarFile() {
      throw new AssertionError("getJarFile not supported");
   }

   public File getDirectory() {
      throw new AssertionError("getDirectory not supported");
   }

   public Iterator entries() {
      try {
         return this.getEntries("/");
      } catch (IOException var2) {
         return Iterators.EMPTY_ITERATOR;
      }
   }

   private static class URLZipEntry extends ZipEntry {
      private final URL url;

      URLZipEntry(URL var1) {
         super(var1.toString());
         this.url = var1;
      }

      URL getURL() {
         return this.url;
      }
   }
}
