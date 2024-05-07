package weblogic.deployment;

import java.io.DataInputStream;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import weblogic.corba.j2ee.naming.ORBHelper;

public class ORBObjectFactory implements ObjectFactory {
   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
      try {
         return Class.forName((new DataInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/services/org.omg.CORBA.ORB"))).readLine()).newInstance();
      } catch (Exception var6) {
         return ORBHelper.getORBHelper().getLocalORB();
      }
   }
}
