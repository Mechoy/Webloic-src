package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCRouteEntry;
import com.bea.core.jatmi.intf.TCRouteService;
import java.util.ArrayList;
import javax.transaction.xa.Xid;
import weblogic.management.ManagementException;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TypedBuffer;
import weblogic.wtc.jatmi.gwatmi;

public final class WlsRouteService implements TCRouteService {
   private WTCService _svc;
   private OatmialServices _tos = null;

   public WlsRouteService() {
      try {
         this._svc = WTCService.getService();
      } catch (ManagementException var2) {
      }

   }

   public void shutdown(int var1) {
      this._svc = null;
      this._tos = null;
   }

   public ArrayList[] selectTargetRoutes(String var1, TypedBuffer var2, Xid var3, int var4) throws TPException {
      boolean var5 = ntrace.isTraceEnabled(2);
      TDMImport var6 = null;
      TDMRemote var8 = null;
      gwatmi var9 = null;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = false;
      if (var5) {
         ntrace.doTrace("[/WTCRouteManager/selectTargetRotes/" + var1);
      }

      var6 = this._svc.getImport(var1, var3);
      TDMRemote[] var7;
      if ((var7 = var6.getRemoteAccessPointObjectList()) != null && var7.length != 0) {
         ArrayList var15 = new ArrayList(var7.length);

         for(int var13 = 0; var13 < var7.length; ++var13) {
            String var14 = var7[var13].getConnectionPolicy();
            if (var11 || var14 != null && !var14.equals("ON_DEMAND")) {
               var10 = false;
            } else {
               var10 = true;
            }

            if ((var9 = var7[var13].getTsession(var10)) != null) {
               var8 = var7[var13];
               if (var8 != null && var3 != null) {
                  if (this._tos == null) {
                     this._tos = WTCService.getOatmialServices();
                  }

                  this._tos.addOutboundRdomToXid(var3, var8);
               }

               if (var10) {
                  var11 = true;
               }

               TCRouteEntry var16 = new TCRouteEntry(var9, var6.getRemoteName());
               var15.add(var16);
            } else if (var7[var13].getTimedOut()) {
               var12 = true;
            }
         }

         if (var15.size() == 0) {
            if (var12) {
               if (var5) {
                  ntrace.doTrace("*]/WTCRouteManager/selectTargetRotes/20.1/");
               }

               throw new TPException(13, "Connection establishment timed out");
            } else {
               if (var5) {
                  ntrace.doTrace("*]/WTCRouteManager/selectTargetRotes/20.2/");
               }

               throw new TPException(6, "Could not get a Tuxedo session");
            }
         } else {
            ArrayList[] var17 = new ArrayList[]{var15};
            if (var5) {
               ntrace.doTrace("]/WTCRouteManager/selectTargetRotes/30/" + var9);
            }

            return var17;
         }
      } else {
         if (var5) {
            ntrace.doTrace("*]/WTCRouteManager/selectTargetRotes/10/");
         }

         throw new TPException(6, "Could not find remote accesss point");
      }
   }
}
