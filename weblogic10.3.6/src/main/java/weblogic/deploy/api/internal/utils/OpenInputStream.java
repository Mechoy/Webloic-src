package weblogic.deploy.api.internal.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OpenInputStream extends BufferedInputStream {
   public OpenInputStream(InputStream var1) {
      super(var1);
   }

   public OpenInputStream(InputStream var1, int var2) {
      super(var1, var2);
   }

   public void close() throws IOException {
      super.reset();
   }

   public void closeOut() throws IOException {
      super.close();
   }
}
