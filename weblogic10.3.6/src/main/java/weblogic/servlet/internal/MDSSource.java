package weblogic.servlet.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import oracle.jsp.provider.JspResourceProvider;
import weblogic.utils.classloaders.Source;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public class MDSSource implements Source {
   private JspResourceProvider provider;
   private String requestURI;
   private String providerURI;
   private long lastModified;
   private InputStream inputStream;
   private Chunk head;
   private byte[] bytes = null;
   private long length = Long.MIN_VALUE;
   private boolean exists = false;

   public MDSSource(String var1, JspResourceProvider var2) {
      this.provider = var2;
      this.requestURI = var1;

      try {
         this.providerURI = this.provider.getProviderURI(this.requestURI);
         this.lastModified = this.provider.getLastModified(this.providerURI);
         this.inputStream = this.provider.fromStream(this.providerURI);
         this.exists = true;
      } catch (FileNotFoundException var4) {
      } catch (IOException var5) {
      }

   }

   public boolean exists() {
      return this.exists;
   }

   public String getProviderURI() {
      return this.providerURI;
   }

   public byte[] getBytes() throws IOException {
      if (this.bytes != null) {
         return this.bytes;
      } else {
         long var1 = this.length();
         if (var1 == Long.MIN_VALUE) {
            throw new IOException("Can't get bytes from stream, provider: " + this.provider + ", requestURI: " + this.requestURI + ", providerURI: " + this.providerURI + ", stream: " + this.inputStream);
         } else {
            this.bytes = new byte[(int)var1];
            int var3 = 0;

            for(Chunk var4 = this.head; var4 != null; var4 = var4.next) {
               System.arraycopy(var4.buf, 0, this.bytes, var3, var4.end);
               var3 += var4.end;
            }

            return this.bytes;
         }
      }
   }

   public URL getCodeSourceURL() {
      return this.getURL();
   }

   public InputStream getInputStream() throws IOException {
      return new UnsyncByteArrayInputStream(this.getBytes());
   }

   public URL getURL() {
      try {
         return new URL(this.requestURI);
      } catch (MalformedURLException var2) {
         return null;
      }
   }

   public long lastModified() {
      return this.lastModified;
   }

   public long length() {
      if (this.length == Long.MIN_VALUE) {
         this.head = Chunk.getChunk();

         try {
            this.length = (long)Chunk.chunkFully(this.head, this.inputStream);
         } catch (IOException var2) {
         }
      }

      return this.length;
   }
}
