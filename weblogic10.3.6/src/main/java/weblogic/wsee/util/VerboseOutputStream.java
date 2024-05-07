package weblogic.wsee.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.utils.io.Chunk;

public class VerboseOutputStream extends BufferedOutputStream {
   public VerboseOutputStream(OutputStream var1) {
      super(var1, Chunk.CHUNK_SIZE);
      Verbose.say("** S T A R T   R E S P O N S E  O U T P U T S T R E A M **");
      Verbose.say("");
   }

   public void write(int var1) throws IOException {
      Verbose.getOut().write(var1);
      super.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      Verbose.getOut().write(var1, var2, var3);
      super.write(var1, var2, var3);
   }

   public void close() throws IOException {
      super.close();
      Verbose.say("\n     ** E N D  R E S P O N S E  O U T P U T S T R E A M **");
   }
}
