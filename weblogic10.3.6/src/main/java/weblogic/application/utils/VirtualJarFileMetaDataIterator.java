package weblogic.application.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import org.apache.openjpa.lib.meta.MetaDataFilter;
import org.apache.openjpa.lib.meta.MetaDataIterator;
import weblogic.utils.jars.VirtualJarFile;

public class VirtualJarFileMetaDataIterator implements MetaDataIterator, MetaDataFilter.Resource {
   private final VirtualJarFile _vjf;
   private final MetaDataFilter _filter;
   private final Iterator _entries;
   private ZipEntry _entry = null;
   private ZipEntry _last = null;

   public VirtualJarFileMetaDataIterator(VirtualJarFile var1, MetaDataFilter var2) {
      this._vjf = var1;
      this._filter = var2;
      this._entries = this._vjf == null ? null : this._vjf.entries();
   }

   public boolean hasNext() throws IOException {
      if (this._entries == null) {
         return false;
      } else {
         while(this._entry == null && this._entries.hasNext()) {
            this._entry = (ZipEntry)this._entries.next();
            if (this._filter != null && !this._filter.matches(this)) {
               this._entry = null;
            }
         }

         return this._entry != null;
      }
   }

   public Object next() throws IOException {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         String var1 = this._entry.getName();
         this._last = this._entry;
         this._entry = null;
         return var1;
      }
   }

   public InputStream getInputStream() throws IOException {
      if (this._last == null) {
         throw new IllegalStateException();
      } else {
         return this._vjf.getInputStream(this._last);
      }
   }

   public File getFile() {
      if (this._last == null) {
         throw new IllegalStateException();
      } else {
         return null;
      }
   }

   public void close() {
   }

   public String getName() {
      return this._entry.getName();
   }

   public byte[] getContent() throws IOException {
      long var1 = this._entry.getSize();
      if (var1 == 0L) {
         return new byte[0];
      } else {
         InputStream var3 = this._vjf.getInputStream(this._entry);
         ByteArrayOutputStream var5 = new ByteArrayOutputStream();
         byte[] var6 = new byte[1024];
         byte[] var4;
         int var7;
         if (var1 >= 0L) {
            boolean var8 = false;

            while((long)(var7 = var3.read(var6)) < var1) {
               var1 -= (long)var7;
               var5.write(var6, 0, var7);
            }

            var5.write(var6, 0, var7);
            var4 = var5.toByteArray();
         } else {
            while((var7 = var3.read(var6)) != -1) {
               var5.write(var6, 0, var7);
            }

            var4 = var5.toByteArray();
         }

         var5.close();
         var3.close();
         return var4;
      }
   }
}
