package weblogic.jdbc.common.internal;

import java.io.Reader;

public final class ReaderContainer {
   Reader rdr;
   int block_size;

   public ReaderContainer(Reader var1, int var2) {
      this.rdr = var1;
      this.block_size = var2;
   }
}
