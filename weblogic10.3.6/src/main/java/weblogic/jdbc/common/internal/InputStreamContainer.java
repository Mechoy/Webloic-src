package weblogic.jdbc.common.internal;

import java.io.InputStream;

public final class InputStreamContainer {
   InputStream is;
   int block_size;

   public InputStreamContainer(InputStream var1, int var2) {
      this.is = var1;
      this.block_size = var2;
   }
}
