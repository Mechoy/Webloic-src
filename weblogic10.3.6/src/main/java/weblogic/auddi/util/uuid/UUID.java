package weblogic.auddi.util.uuid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import weblogic.auddi.util.Logger;

public class UUID {
   public static String uuidGen() {
      try {
         String var0 = nextUUID1();
         return var0 == null ? nextUUIDJ() : var0;
      } catch (IOException var1) {
         return nextUUIDJ();
      }
   }

   private static String nextUUIDJ() {
      return (new JUUID()).toString();
   }

   private static String nextUUID1() throws IOException {
      String[] var0 = execute("uuidgen.exe");
      return var0 != null ? var0[0] : null;
   }

   private static native String nextUUID0();

   private static void waitFor(Process var0) {
      try {
         var0.waitFor();
      } catch (InterruptedException var2) {
         Logger.error("ERROR In Process : ");
         var2.printStackTrace();
      }

   }

   private static String[] execute(String var0) throws IOException {
      Process var1 = Runtime.getRuntime().exec(var0);
      waitFor(var1);
      InputStream var2 = var1.getInputStream();
      BufferedReader var3 = new BufferedReader(new InputStreamReader(var2));
      Vector var5 = new Vector();

      String var4;
      while((var4 = var3.readLine()) != null) {
         var5.addElement(var4.trim());
      }

      if (var5 != null) {
         String[] var6 = new String[var5.size()];
         var5.copyInto(var6);
         return var6;
      } else {
         return null;
      }
   }

   public static void main(String[] var0) throws IOException {
      for(int var1 = 0; var1 < Integer.parseInt(var0[0]); ++var1) {
         Logger.error(uuidGen());
      }

   }
}
