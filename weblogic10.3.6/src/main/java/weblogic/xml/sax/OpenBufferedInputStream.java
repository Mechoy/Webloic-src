package weblogic.xml.sax;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OpenBufferedInputStream extends BufferedInputStream {
   private boolean allowClose = false;

   public OpenBufferedInputStream(InputStream var1) {
      super(var1);
   }

   public OpenBufferedInputStream(InputStream var1, int var2) {
      super(var1, var2);
   }

   public void allowClose(boolean var1) {
      this.allowClose = var1;
   }

   public void close() throws IOException {
      if (this.allowClose) {
         super.close();
      }

   }
}
