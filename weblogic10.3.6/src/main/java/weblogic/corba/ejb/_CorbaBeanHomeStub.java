package weblogic.corba.ejb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

public class _CorbaBeanHomeStub extends ObjectImpl implements CorbaBeanHome {
   private static String[] __ids = new String[]{"IDL:weblogic/corba/CorbaBeanHome:1.0"};

   public void remove(Object var1) throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }

   public CorbaBeanObject create() throws CreateException {
      InputStream var1 = null;

      CorbaBeanObject var3;
      try {
         OutputStream var2 = this._request("create", true);
         var1 = this._invoke(var2);
         var3 = CorbaBeanObjectHelper.read(var1);
         CorbaBeanObject var4 = var3;
         return var4;
      } catch (ApplicationException var9) {
         var1 = var9.getInputStream();
         String var12 = var9.getId();
         if (var12.equals("IDL:javax/ejb/CreateException:1.0")) {
            throw CreateExceptionHelper.read(var1);
         }

         throw new MARSHAL(var12);
      } catch (RemarshalException var10) {
         var3 = this.create();
      } finally {
         this._releaseReply(var1);
      }

      return var3;
   }

   public String[] _ids() {
      return (String[])((String[])__ids.clone());
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
