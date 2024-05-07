package weblogic.jndi.internal;

import weblogic.jndi.Environment;
import weblogic.kernel.ThreadLocalStack;

public final class ThreadEnvironment {
   private static final ThreadLocalStack threadEnvironment = new ThreadLocalStack(true);

   public static void push(Environment var0) {
      threadEnvironment.push(var0);
   }

   public static Environment pop() {
      return (Environment)threadEnvironment.popAndPeek();
   }

   public static Environment get() {
      return (Environment)threadEnvironment.get();
   }
}
