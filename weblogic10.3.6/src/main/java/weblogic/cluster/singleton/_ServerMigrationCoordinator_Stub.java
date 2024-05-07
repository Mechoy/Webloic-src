package weblogic.cluster.singleton;

import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

public final class _ServerMigrationCoordinator_Stub extends Stub implements ServerMigrationCoordinator {
   private static String[] _type_ids = new String[]{"RMI:weblogic.cluster.singleton.ServerMigrationCoordinator:0000000000000000"};
   // $FF: synthetic field
   private static Class class$weblogic$cluster$singleton$ServerMigrationException;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public String[] _ids() {
      return _type_ids;
   }

   public final void migrate(String var1, String var2, String var3, boolean var4, boolean var5) throws RemoteException {
      try {
         InputStream var6 = null;

         try {
            OutputStream var7 = (OutputStream)this._request("migrate", true);
            var7.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var7.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var7.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var7.write_boolean(var4);
            var7.write_boolean(var5);
            this._invoke(var7);
         } catch (ApplicationException var15) {
            var6 = (InputStream)var15.getInputStream();
            String var9 = var6.read_string();
            if (var9.equals("IDL:weblogic/cluster/singleton/ServerMigrationEx:1.0")) {
               throw (ServerMigrationException)((ServerMigrationException)var6.read_value(class$weblogic$cluster$singleton$ServerMigrationException == null ? (class$weblogic$cluster$singleton$ServerMigrationException = class$("weblogic.cluster.singleton.ServerMigrationException")) : class$weblogic$cluster$singleton$ServerMigrationException));
            }

            throw new UnexpectedException(var9);
         } catch (RemarshalException var16) {
            this.migrate(var1, var2, var3, var4, var5);
         } finally {
            this._releaseReply(var6);
         }

      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
