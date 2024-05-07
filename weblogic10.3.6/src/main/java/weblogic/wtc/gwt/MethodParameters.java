package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wtc.jatmi.InvokeInfo;
import weblogic.wtc.jatmi.Objrecv;
import weblogic.wtc.jatmi.dsession;
import weblogic.wtc.jatmi.gwatmi;
import weblogic.wtc.wls.WlsAuthenticatedUser;

public final class MethodParameters {
   private Objrecv myObjrecv;
   private ServiceParameters mySvcParms;
   private Object[] myTxInfo;
   private dsession myDsession;
   private int myGIOPRequestID;
   private boolean _debug = false;

   public MethodParameters(ServiceParameters var1, Objrecv var2, Object[] var3, dsession var4) {
      this.mySvcParms = var1;
      this.myObjrecv = var2;
      this.myTxInfo = var3;
      this.myDsession = var4;
      this.myGIOPRequestID = 0;
   }

   public MethodParameters(int var1, dsession var2) {
      this.mySvcParms = null;
      this.myObjrecv = null;
      this.myTxInfo = null;
      this.myGIOPRequestID = var1;
      this.myDsession = var2;
   }

   public InvokeInfo get_invokeInfo() {
      return this.mySvcParms.get_invokeInfo();
   }

   public gwatmi get_gwatmi() {
      return this.myDsession;
   }

   public Objrecv getObjrecv() {
      return this.myObjrecv;
   }

   public ServiceParameters getServiceParameters() {
      return this.mySvcParms;
   }

   public Object[] getTxInfo() {
      return this.myTxInfo;
   }

   public AuthenticatedSubject getAuthenticatedSubject() {
      if (this._debug) {
         ntrace.doTrace("[/MethodParameters/getAuthenticatedSubject()");
      }

      TCAuthenticatedUser var1 = this.mySvcParms.getInvokeInfo().getUser();
      if (var1 instanceof WlsAuthenticatedUser) {
         WlsAuthenticatedUser var2 = (WlsAuthenticatedUser)var1;
         if (this._debug) {
            ntrace.doTrace("]/MethodParameters/getAuthenticatedSubject(10)/return " + var2.getWlsSubject());
         }

         return var2.getWlsSubject();
      } else {
         if (this._debug) {
            ntrace.doTrace("]/MethodParameters/getAuthenticatedSubject(20)/return null");
         }

         return null;
      }
   }

   public int getGIOPRequestID() {
      return this.myGIOPRequestID;
   }

   public void setGIOPRequestID(int var1) {
      this.myGIOPRequestID = var1;
   }

   public String toString() {
      return "\nMethodParameters\n  myObjrecv = " + this.myObjrecv + "\n" + "  mySvcParms = " + this.mySvcParms + "\n" + "  myTxInfo = " + this.myTxInfo + "\n" + "  myDsession = " + this.myDsession + "\n" + "  myGIOPRequestID = " + this.myGIOPRequestID + "\n";
   }
}
