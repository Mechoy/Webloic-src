package weblogic.security.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class InputStreamsCloner {
   private InputStreamCloner[] cloners = null;
   private int hashcode = 0;

   public InputStreamsCloner(InputStream[] var1) {
      this.cloners = new InputStreamCloner[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.cloners[var2] = new InputStreamCloner(var1[var2]);
      }

   }

   public InputStream[] cloneStreams() throws IOException {
      InputStream[] var1 = new InputStream[this.cloners.length];

      for(int var2 = 0; var2 < this.cloners.length; ++var2) {
         var1[var2] = this.cloners[var2].cloneStream();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      return this == var1 || var1 != null && var1 instanceof InputStreamsCloner && Arrays.equals(this.cloners, ((InputStreamsCloner)var1).cloners);
   }

   public int hashCode() {
      if (this.hashcode == 0) {
         int var1 = 1;

         for(int var2 = 0; var2 < this.cloners.length; ++var2) {
            var1 = var1 * 31 + this.cloners[var2].hashCode();
         }

         this.hashcode = var1;
      }

      return this.hashcode;
   }

   public int size() {
      return this.cloners.length;
   }
}
