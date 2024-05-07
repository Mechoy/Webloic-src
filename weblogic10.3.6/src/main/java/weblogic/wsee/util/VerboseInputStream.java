package weblogic.wsee.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class VerboseInputStream extends BufferedInputStream {
   public VerboseInputStream(InputStream var1) {
      super(var1);
      Verbose.say("** S T A R T  I N P U T S T R E A M **");
   }

   public int read() throws IOException {
      int var1 = super.read();
      if (var1 >= 0) {
         Verbose.getOut().write(var1);
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = super.read(var1, var2, var3);
      if (var4 >= 0) {
         Verbose.getOut().write(var1, var2, var4);
      }

      return var4;
   }

   public void close() throws IOException {
      super.close();
      Verbose.getOut().flush();
      Verbose.say("** E N D  I N P U T S T R E A M **");
   }
}
