package weblogic.ejb.container.cmp.rdbms;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public final class RDBMSObjectInputStream extends ObjectInputStream {
   private ClassLoader classLoader = null;

   public RDBMSObjectInputStream(InputStream var1, ClassLoader var2) throws IOException {
      super(var1);
      this.classLoader = var2;
   }

   protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
      return Class.forName(var1.getName(), false, this.classLoader);
   }
}
