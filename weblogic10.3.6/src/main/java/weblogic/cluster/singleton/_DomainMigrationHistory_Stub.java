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
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;

public final class _DomainMigrationHistory_Stub extends Stub implements DomainMigrationHistory {
   private static String[] _type_ids = new String[]{"RMI:weblogic.cluster.singleton.DomainMigrationHistory:0000000000000000"};
   // $FF: synthetic field
   private static Class class$weblogic$cluster$singleton$MigrationData;
   // $FF: synthetic field
   private static Class array$Lweblogic$management$runtime$MigrationDataRuntimeMBean;
   // $FF: synthetic field
   private static Class array$Lweblogic$management$runtime$ServiceMigrationDataRuntimeMBean;

   public String[] _ids() {
      return _type_ids;
   }

   public final void update(MigrationData var1) throws RemoteException {
      try {
         InputStream var2 = null;

         try {
            OutputStream var3 = (OutputStream)this._request("update", true);
            var3.write_value(var1, class$weblogic$cluster$singleton$MigrationData == null ? (class$weblogic$cluster$singleton$MigrationData = class$("weblogic.cluster.singleton.MigrationData")) : class$weblogic$cluster$singleton$MigrationData);
            this._invoke(var3);
         } catch (ApplicationException var11) {
            var2 = (InputStream)var11.getInputStream();
            String var5 = var2.read_string();
            throw new UnexpectedException(var5);
         } catch (RemarshalException var12) {
            this.update(var1);
         } finally {
            this._releaseReply(var2);
         }

      } catch (SystemException var14) {
         throw Util.mapSystemException(var14);
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

   public final MigrationDataRuntimeMBean[] getMigrationDataRuntimes() throws RemoteException {
      try {
         InputStream var1 = null;

         MigrationDataRuntimeMBean[] var5;
         try {
            org.omg.CORBA.portable.OutputStream var2 = this._request("_get_migrationDataRuntimes", true);
            var1 = (InputStream)this._invoke(var2);
            MigrationDataRuntimeMBean[] var3 = (MigrationDataRuntimeMBean[])var1.read_value(array$Lweblogic$management$runtime$MigrationDataRuntimeMBean == null ? (array$Lweblogic$management$runtime$MigrationDataRuntimeMBean = class$("[Lweblogic.management.runtime.MigrationDataRuntimeMBean;")) : array$Lweblogic$management$runtime$MigrationDataRuntimeMBean);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getMigrationDataRuntimes();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes() throws RemoteException {
      try {
         InputStream var1 = null;

         ServiceMigrationDataRuntimeMBean[] var5;
         try {
            org.omg.CORBA.portable.OutputStream var2 = this._request("_get_serviceMigrationDataRuntimes", true);
            var1 = (InputStream)this._invoke(var2);
            ServiceMigrationDataRuntimeMBean[] var3 = (ServiceMigrationDataRuntimeMBean[])var1.read_value(array$Lweblogic$management$runtime$ServiceMigrationDataRuntimeMBean == null ? (array$Lweblogic$management$runtime$ServiceMigrationDataRuntimeMBean = class$("[Lweblogic.management.runtime.ServiceMigrationDataRuntimeMBean;")) : array$Lweblogic$management$runtime$ServiceMigrationDataRuntimeMBean);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getServiceMigrationDataRuntimes();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }
}
