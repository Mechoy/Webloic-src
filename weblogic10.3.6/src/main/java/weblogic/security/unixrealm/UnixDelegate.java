package weblogic.security.unixrealm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import weblogic.logging.LogOutputStream;
import weblogic.security.utils.Factory;

final class UnixDelegate {
   private Runtime runtime = Runtime.getRuntime();
   private String program;
   private Process proc;
   private BufferedReader in;
   private PrintWriter out;
   private int chatCount;
   private int maxChats;
   private UnixRealm owner;
   private LogOutputStream log;

   UnixDelegate(String var1, int var2, UnixRealm var3) {
      this.program = var1;
      this.maxChats = var2;
      this.owner = var3;
      this.log = var3.log;
   }

   Chat chat(String[] var1) throws IOException {
      if (this.proc == null || this.chatCount > this.maxChats) {
         if (this.proc != null) {
            if (this.log != null) {
               this.log.debug("killing old subprocess");
            }

            this.close();
         }

         if (this.log != null) {
            this.log.debug("starting new subprocess");
         }

         this.proc = this.runtime.exec(this.program);
         this.in = new BufferedReader(new InputStreamReader(this.proc.getInputStream()));
         this.out = new PrintWriter(this.proc.getOutputStream());
      }

      if (this.log != null) {
         StringBuffer var2 = new StringBuffer();
         if (var1.length > 0) {
            for(int var3 = 0; var3 < var1.length - 1; ++var3) {
               var2.append("\"" + var1[var3] + "\", ");
            }

            var2.append("\"" + var1[var1.length - 1] + "\"");
         }

         this.log.debug("chat: sending {" + var2 + "}");
      }

      for(int var4 = 0; var4 < var1.length; ++var4) {
         this.out.println(var1[var4]);
      }

      if (this.out.checkError()) {
         throw new SubprocessException("subprocess failed");
      } else {
         ++this.chatCount;
         return new Chat();
      }
   }

   void close() {
      this.out.close();

      try {
         this.in.close();
      } catch (IOException var2) {
      }

      this.proc.destroy();
      this.out = null;
      this.in = null;
      this.proc = null;
      this.chatCount = 0;
   }

   protected void finalize() {
      this.close();
   }

   static class DFactory implements Factory {
      String program;
      int maxChats;
      UnixRealm owner;

      DFactory(String var1, int var2, UnixRealm var3) {
         this.program = var1;
         this.maxChats = var2;
         this.owner = var3;
      }

      public Object newInstance() throws InvocationTargetException {
         if (this.owner.log != null) {
            this.owner.log.debug("new instance");
         }

         return new UnixDelegate(this.program, this.maxChats, this.owner);
      }

      public void destroyInstance(Object var1) {
         if (this.owner.log != null) {
            this.owner.log.debug("destroy instance");
         }

         ((UnixDelegate)var1).close();
      }
   }

   class Chat {
      String read() throws IOException {
         String var1 = UnixDelegate.this.in.readLine();
         if (UnixDelegate.this.log != null) {
            UnixDelegate.this.log.debug("read \"" + var1 + "\"");
         }

         return var1;
      }

      boolean expect(String var1) throws IOException {
         String var2 = this.read();
         boolean var3 = var2.equals(var1);
         if (UnixDelegate.this.log != null) {
            UnixDelegate.this.log.debug("expected \"" + var1 + "\": " + (var3 ? "matched" : "no match"));
         }

         return var3;
      }

      void require(String var1) throws IOException {
         String var2 = this.read();
         if (UnixDelegate.this.log != null) {
            UnixDelegate.this.log.debug("requiring \"" + var1 + "\"");
         }

         if (!var2.equals(var1)) {
            throw new SubprocessException("request failed: " + var2);
         }
      }
   }
}
