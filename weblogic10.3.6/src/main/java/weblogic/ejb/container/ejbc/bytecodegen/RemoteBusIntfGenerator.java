package weblogic.ejb.container.ejbc.bytecodegen;

import java.io.IOException;
import weblogic.ejb.container.ejbc.EJBCException;

class RemoteBusIntfGenerator implements Generator {
   private final Class<?> busIntf;
   private final String clsName;

   RemoteBusIntfGenerator(String var1, Class<?> var2) {
      this.clsName = BCUtil.binName(var1);
      this.busIntf = var2;
   }

   public Generator.Output generate() throws EJBCException {
      try {
         byte[] var1 = RemoteBusIntfClassAdapter.getRBIBytes(this.busIntf, this.clsName);
         return new ClassFileOutput(this.clsName, var1);
      } catch (IOException var2) {
         throw new EJBCException("Error reading the class file of " + this.busIntf, var2);
      }
   }
}
