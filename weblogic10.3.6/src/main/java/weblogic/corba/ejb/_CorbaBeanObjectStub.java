package weblogic.corba.ejb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.ObjectImpl;

public class _CorbaBeanObjectStub extends ObjectImpl implements CorbaBeanObject {
   private static String[] __ids = new String[]{"IDL:weblogic/corba/CorbaBeanObject:1.0"};

   public String[] _ids() {
      return (String[])((String[])__ids.clone());
   }

   public EJBLocalHome getEJBLocalHome() throws EJBException {
      throw new UnsupportedOperationException();
   }

   public Object getPrimaryKey() throws EJBException {
      throw new UnsupportedOperationException();
   }

   public void remove() throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }

   public boolean isIdentical(EJBLocalObject var1) throws EJBException {
      throw new UnsupportedOperationException();
   }

   private void readObject(ObjectInputStream var1) throws IOException {
      String var2 = var1.readUTF();
      Object var3 = null;
      Object var4 = null;
      org.omg.CORBA.Object var5 = ORB.init((String[])var3, (Properties)var4).string_to_object(var2);
      Delegate var6 = ((ObjectImpl)var5)._get_delegate();
      this._set_delegate(var6);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      Object var2 = null;
      Object var3 = null;
      String var4 = ORB.init((String[])var2, (Properties)var3).object_to_string(this);
      var1.writeUTF(var4);
   }
}
