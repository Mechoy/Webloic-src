package weblogic.cache;

import weblogic.utils.NestedException;

public class CacheException extends NestedException {
   public CacheException(String var1) {
      super(var1);
   }

   public CacheException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
