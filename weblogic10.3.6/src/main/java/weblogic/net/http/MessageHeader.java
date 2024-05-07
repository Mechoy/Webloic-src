package weblogic.net.http;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class MessageHeader {
   private static final byte[] HTTP_PROTOCOL_BYTES = new byte[]{72, 84, 84, 80, 47, 49, 46};
   private static final int HTTP_PROTOCOL_LEN = 7;
   private String[] keys;
   private String[] vals;
   private int nkeys;
   private byte[] byteBuffer = new byte[7];

   public MessageHeader() {
      this.grow();
   }

   public MessageHeader(InputStream var1) throws IOException {
      this.parseHeader(var1);
   }

   public String findValue(String var1) {
      int var2;
      if (var1 == null) {
         var2 = this.nkeys;

         while(true) {
            --var2;
            if (var2 < 0) {
               break;
            }

            if (this.keys[var2] == null) {
               return this.vals[var2];
            }
         }
      } else {
         var2 = this.nkeys;

         while(true) {
            --var2;
            if (var2 < 0) {
               break;
            }

            if (var1.equalsIgnoreCase(this.keys[var2])) {
               return this.vals[var2];
            }
         }
      }

      return null;
   }

   public String getKey(int var1) {
      return var1 >= 0 && var1 < this.nkeys ? this.keys[var1] : null;
   }

   public String getValue(int var1) {
      return var1 >= 0 && var1 < this.nkeys ? this.vals[var1] : null;
   }

   public void print(HttpOutputStream var1) throws IOException {
      for(int var2 = 0; var2 < this.nkeys; ++var2) {
         if (this.keys[var2] != null) {
            var1.print(this.keys[var2]);
            if (this.vals[var2] != null) {
               var1.print(": ");
               var1.print(this.vals[var2]);
            }

            var1.print("\r\n");
         }
      }

      var1.print("\r\n");
   }

   public void print(PrintStream var1) {
      for(int var2 = 0; var2 < this.nkeys; ++var2) {
         if (this.keys[var2] != null) {
            var1.print(this.keys[var2] + (this.vals[var2] != null ? ": " + this.vals[var2] : "") + "\r\n");
         }
      }

      var1.print("\r\n");
      var1.flush();
   }

   public void add(String var1, String var2) {
      this.grow();
      this.keys[this.nkeys] = var1;
      this.vals[this.nkeys] = var2;
      ++this.nkeys;
   }

   public void prepend(String var1, String var2) {
      this.grow();

      for(int var3 = this.nkeys; var3 > 0; --var3) {
         this.keys[var3] = this.keys[var3 - 1];
         this.vals[var3] = this.vals[var3 - 1];
      }

      this.keys[0] = var1;
      this.vals[0] = var2;
      ++this.nkeys;
   }

   public void set(int var1, String var2, String var3) {
      this.grow();
      if (var1 >= 0) {
         if (var1 > this.nkeys) {
            this.add(var2, var3);
         } else {
            this.keys[var1] = var2;
            this.vals[var1] = var3;
         }

      }
   }

   private void grow() {
      if (this.keys == null || this.nkeys >= this.keys.length) {
         String[] var1 = new String[this.nkeys + 4];
         String[] var2 = new String[this.nkeys + 4];
         if (this.keys != null) {
            System.arraycopy(this.keys, 0, var1, 0, this.nkeys);
         }

         if (this.vals != null) {
            System.arraycopy(this.vals, 0, var2, 0, this.nkeys);
         }

         this.keys = var1;
         this.vals = var2;
      }

   }

   public void set(String var1, String var2) {
      int var3 = this.nkeys;

      do {
         --var3;
         if (var3 < 0) {
            this.add(var1, var2);
            return;
         }
      } while(!var1.equalsIgnoreCase(this.keys[var3]));

      this.vals[var3] = var2;
   }

   public void setIfNotSet(String var1, String var2) {
      if (this.findValue(var1) == null) {
         this.add(var1, var2);
      }

   }

   public void parseHeader(InputStream var1) throws IOException {
      this.nkeys = 0;
      if (var1 != null && this.isHTTP(var1)) {
         char[] var2 = new char[10];
         int var3 = var1.read();
         if (var3 < 0) {
            throw new IOException("Response contained no data");
         } else {
            String var9;
            String var10;
            for(; var3 != 10 && var3 != 13 && var3 >= 0; this.add(var10, var9)) {
               int var4 = 0;
               int var5 = -1;
               boolean var7 = var3 > 32;
               var2[var4++] = (char)var3;

               label105:
               while(true) {
                  int var6;
                  if ((var6 = var1.read()) < 0) {
                     var3 = -1;
                     break;
                  }

                  switch (var6) {
                     case 9:
                        var6 = 32;
                     case 32:
                        var7 = false;
                        break;
                     case 10:
                     case 13:
                        var3 = var1.read();
                        if (var6 == 13 && var3 == 10) {
                           var3 = var1.read();
                           if (var3 == 13) {
                              var3 = var1.read();
                           }
                        }

                        if (var3 == 10 || var3 == 13 || var3 > 32) {
                           break label105;
                        }

                        var6 = 32;
                        break;
                     case 58:
                        if (var7 && var4 > 0) {
                           var5 = var4;
                        }

                        var7 = false;
                  }

                  if (var4 >= var2.length) {
                     char[] var8 = new char[var2.length * 2];
                     System.arraycopy(var2, 0, var8, 0, var4);
                     var2 = var8;
                  }

                  var2[var4++] = (char)var6;
               }

               while(var4 > 0 && var2[var4 - 1] <= ' ') {
                  --var4;
               }

               if (var5 <= 0) {
                  var10 = null;
                  var5 = 0;
               } else {
                  var10 = String.copyValueOf(var2, 0, var5);
                  if (var5 < var4 && var2[var5] == ':') {
                     ++var5;
                  }

                  while(var5 < var4 && var2[var5] <= ' ') {
                     ++var5;
                  }
               }

               if (var5 >= var4) {
                  var9 = new String();
               } else {
                  var9 = String.copyValueOf(var2, var5, var4 - var5);
               }
            }

         }
      }
   }

   private boolean isHTTP(InputStream var1) throws IOException {
      if (!var1.markSupported()) {
         return true;
      } else {
         var1.mark(7);

         boolean var2;
         try {
            if (7 != var1.read(this.byteBuffer, 0, 7)) {
               throw new EOFException("Response contained no data");
            }

            var2 = Arrays.equals(HTTP_PROTOCOL_BYTES, this.byteBuffer);
         } finally {
            var1.reset();
         }

         return var2;
      }
   }

   public String toString() {
      String var1 = super.toString();

      for(int var2 = 0; var2 < this.keys.length; ++var2) {
         var1 = var1 + "{" + this.keys[var2] + ": " + this.vals[var2] + "}";
      }

      return var1;
   }

   public synchronized Map getHeaders() {
      HashMap var1 = new HashMap();
      int var2 = this.nkeys;

      while(true) {
         --var2;
         Object var3;
         if (var2 < 0) {
            Set var6 = var1.keySet();
            Iterator var5 = var6.iterator();

            while(var5.hasNext()) {
               var3 = var5.next();
               List var4 = (List)var1.get(var3);
               var1.put(var3, Collections.unmodifiableList(var4));
            }

            return Collections.unmodifiableMap(var1);
         }

         var3 = var1.get(this.keys[var2]);
         if (var3 == null) {
            var3 = new ArrayList();
            var1.put(this.keys[var2], var3);
         }

         ((List)var3).add(this.vals[var2]);
      }
   }
}
