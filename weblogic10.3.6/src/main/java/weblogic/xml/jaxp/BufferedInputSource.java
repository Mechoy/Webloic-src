package weblogic.xml.jaxp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import org.xml.sax.InputSource;

public class BufferedInputSource extends InputSource {
   private InputSource source;
   private InputStream is;
   private Reader reader;

   public BufferedInputSource(InputSource var1) {
      this.source = var1;
   }

   public InputStream getByteStream() {
      Object var1 = this.source.getByteStream();
      if (var1 != null) {
         return (InputStream)var1;
      } else {
         if (var1 != null && !((InputStream)var1).markSupported()) {
            var1 = new BufferedInputStream((InputStream)var1);
            this.is = (InputStream)var1;
         }

         return (InputStream)var1;
      }
   }

   public Reader getCharacterStream() {
      if (this.reader != null) {
         return this.reader;
      } else {
         Object var1 = this.source.getCharacterStream();
         if (var1 != null && !((Reader)var1).markSupported()) {
            var1 = new BufferedReader((Reader)var1);
            this.reader = (Reader)var1;
         }

         return (Reader)var1;
      }
   }

   public String getEncoding() {
      return this.source.getEncoding();
   }

   public String getPublicId() {
      return this.source.getPublicId();
   }

   public String getSystemId() {
      return this.source.getSystemId();
   }

   public void setByteStream(InputStream var1) {
      this.source.setByteStream(var1);
   }

   public void setCharacterStream(Reader var1) {
      this.source.setCharacterStream(var1);
   }

   public void setEncoding(String var1) {
      this.source.setEncoding(var1);
   }

   public void setPublicId(String var1) {
      this.source.setPublicId(var1);
   }

   public void setSystemId(String var1) {
      this.source.setSystemId(var1);
   }
}
