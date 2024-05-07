package weblogic.j2eeclient.java;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import weblogic.j2eeclient.Main;

public final class javaURLContextFactory implements ObjectFactory {
   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
      return Main.getJavaContext();
   }
}
