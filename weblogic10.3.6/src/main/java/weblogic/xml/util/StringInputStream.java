package weblogic.xml.util;

import java.io.IOException;
import java.io.InputStream;

public class StringInputStream extends InputStream {
   int size;
   int index;
   String buf;

   public StringInputStream(String var1) {
      this.size = var1.length();
      this.index = 0;
      this.buf = var1;
   }

   public int read() throws IOException {
      return this.index < this.size ? this.buf.charAt(this.index++) : -1;
   }
}
