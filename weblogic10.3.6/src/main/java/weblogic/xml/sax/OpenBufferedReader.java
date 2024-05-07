package weblogic.xml.sax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class OpenBufferedReader extends BufferedReader {
   private boolean allowClose = false;
   private long nChar;

   public OpenBufferedReader(Reader var1) {
      super(var1);
   }

   public OpenBufferedReader(Reader var1, int var2) {
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

   public int read() throws IOException {
      ++this.nChar;
      return super.read();
   }

   public long getNChar() {
      return this.nChar;
   }
}
