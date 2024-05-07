package weblogic.common.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import weblogic.utils.io.Replacer;
import weblogic.utils.io.Resolver;

public final class ReplacerObjectInputStream extends ObjectInputStream {
   private final Replacer replacer;
   private final Resolver resolver;

   public ReplacerObjectInputStream(InputStream var1, Replacer var2, Resolver var3) throws IOException {
      super(var1);
      this.enableResolveObject(var2 != null);
      this.replacer = var2;
      this.resolver = var3;
   }

   protected Object resolveObject(Object var1) throws IOException {
      Object var2 = this.replacer.resolveObject(var1);
      return var2;
   }

   protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
      if (this.resolver != null) {
         Class var2 = this.resolver.resolveClass(var1);
         if (var2 != null) {
            return var2;
         }
      }

      return super.resolveClass(var1);
   }

   protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
      return ProxyClassResolver.resolveProxyClass(var1);
   }
}
