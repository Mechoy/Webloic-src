package weblogic.wtc.wls;

import com.bea.core.jatmi.intf.TCAppKey;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import weblogic.wtc.jatmi.AppKey;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.UserRec;

public final class WlsAppKeyDelegate implements TCAppKey {
   private AppKey _app_key;

   public WlsAppKeyDelegate(AppKey var1) {
      this._app_key = var1;
   }

   public void init(String var1, boolean var2, int var3) throws TPException {
      if (this._app_key != null) {
         this._app_key.init(var1, var2, var3);
      }

   }

   public void uninit() throws TPException {
      if (this._app_key != null) {
         this._app_key.uninit();
      }

   }

   public UserRec getTuxedoUserRecord(TCAuthenticatedUser var1) {
      UserRec var2 = null;
      if (this._app_key != null) {
         WlsAuthenticatedUser var3 = (WlsAuthenticatedUser)var1;
         var2 = this._app_key.getTuxedoUserRecord(var3.getWlsSubject());
      }

      return var2;
   }

   public void doCache(boolean var1) {
   }

   public boolean isCached() {
      return false;
   }
}
