package weblogic.wsee.wstx.internal;

import java.util.Locale;
import javax.transaction.xa.XAException;

class JTAHelper {
   static void throwXAException(int var0, String var1) throws XAException {
      weblogic.transaction.XAException var2 = new weblogic.transaction.XAException(var0, xaErrorCodeToString(var0) + ".  " + var1, (Throwable)null);
      throw var2;
   }

   static void throwXAException(int var0, String var1, Throwable var2) throws XAException {
      weblogic.transaction.XAException var3 = new weblogic.transaction.XAException(var0, xaErrorCodeToString(var0) + ".  " + var1, var2);
      throw var3;
   }

   static String xaErrorCodeToString(int var0) {
      return xaErrorCodeToString(var0, true);
   }

   static String xaErrorCodeToString(int var0, boolean var1) {
      StringBuffer var2 = new StringBuffer(10);
      switch (var0) {
         case -9:
            var2.append("XAER_OUTSIDE");
            if (var1) {
               var2.append(" : The resource manager is doing work outside global transaction");
            }

            return var2.toString();
         case -8:
            var2.append("XAER_DUPID");
            if (var1) {
               var2.append(" : The XID already exists");
            }

            return var2.toString();
         case -7:
            var2.append("XAER_RMFAIL");
            if (var1) {
               var2.append(" : Resource manager is unavailable");
            }

            return var2.toString();
         case -6:
            var2.append("XAER_PROTO");
            if (var1) {
               var2.append(" : Routine was invoked in an inproper context");
            }

            return var2.toString();
         case -5:
            var2.append("XAER_INVAL");
            if (var1) {
               var2.append(" : Invalid arguments were given");
            }

            return var2.toString();
         case -4:
            var2.append("XAER_NOTA");
            if (var1) {
               var2.append(" : The XID is not valid");
            }

            return var2.toString();
         case -3:
            var2.append("XAER_RMERR");
            if (var1) {
               var2.append(" : A resource manager error has occured in the transaction branch");
            }

            return var2.toString();
         case -2:
            var2.append("XAER_ASYNC");
            if (var1) {
               var2.append(" : Asynchronous operation already outstanding");
            }

            return var2.toString();
         case 0:
            return "XA_OK";
         case 3:
            return "XA_RDONLY";
         case 5:
            var2.append("XA_HEURMIX");
            if (var1) {
               var2.append(" : The transaction branch has been heuristically committed and rolled back");
            }

            return var2.toString();
         case 6:
            var2.append("XA_HEURRB");
            if (var1) {
               var2.append(" : The transaction branch has been heuristically rolled back");
            }

            return var2.toString();
         case 7:
            var2.append("XA_HEURCOM");
            if (var1) {
               var2.append(" : The transaction branch has been heuristically committed");
            }

            return var2.toString();
         case 8:
            var2.append("XA_HEURHAZ");
            if (var1) {
               var2.append(" : The transaction branch may have been heuristically completed");
            }

            return var2.toString();
         case 100:
            var2.append("XA_RBROLLBACK");
            if (var1) {
               var2.append(" : Rollback was caused by unspecified reason");
            }

            return var2.toString();
         case 101:
            var2.append("XA_RBCOMMFAIL");
            if (var1) {
               var2.append(" : Rollback was caused by communication failure");
            }

            return var2.toString();
         case 102:
            var2.append("XA_RBDEADLOCK");
            if (var1) {
               var2.append(" : A deadlock was detected");
            }

            return var2.toString();
         case 103:
            var2.append("XA_RBINTEGRITY");
            if (var1) {
               var2.append(" : A condition that violates the integrity of the resource was detected");
            }

            return var2.toString();
         case 104:
            var2.append("XA_RBOTHER");
            if (var1) {
               var2.append(" : The resource manager rolled back the transaction branch for a reason not on this list");
            }

            return var2.toString();
         case 105:
            var2.append("XA_RBPROTO");
            if (var1) {
               var2.append(" : A protocol error occured in the resource manager");
            }

            return var2.toString();
         case 106:
            var2.append("XA_RBTIMEOUT");
            if (var1) {
               var2.append(" : A transaction branch took too long");
            }

            return var2.toString();
         case 107:
            var2.append("XA_RBTRANSIENT");
            if (var1) {
               var2.append(" : May retry the transaction branch");
            }

            return var2.toString();
         default:
            return Integer.toHexString(var0).toUpperCase(Locale.ENGLISH);
      }
   }
}
