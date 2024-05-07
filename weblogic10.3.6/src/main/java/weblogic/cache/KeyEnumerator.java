package weblogic.cache;

import java.util.StringTokenizer;

public class KeyEnumerator {
   private StringTokenizer keyTokenizer;
   private KeyParser keyParser;

   public KeyEnumerator(String var1) {
      this.keyTokenizer = new StringTokenizer(var1, ",");
   }

   public boolean hasMoreKeys() {
      return this.keyTokenizer.hasMoreTokens();
   }

   public String getNextKey() {
      this.keyParser = new KeyParser(this.keyTokenizer.nextToken());
      return this.keyParser.getKey();
   }

   public String getKeyScope() {
      return this.keyParser.getKeyScope();
   }
}
