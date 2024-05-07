package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import java.io.DataInputStream;
import java.io.IOException;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.NO_RESOURCES;
import org.omg.CORBA.NO_RESPONSE;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.TRANSIENT;
import org.omg.CORBA.UNKNOWN;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.MessageHeaderUtils;
import weblogic.tgiop.TGIOPConnection;
import weblogic.utils.io.Chunk;
import weblogic.wtc.gwt.MethodParameters;

public final class TGIOPUtil {
   public static final int TPTCMPASSTHRU = 1;
   public static final int TPSELF = 1;
   public static final int TPBIND = 1;

   public static void routeSetHost(tfmh var0, String var1, int var2, short var3, int var4) throws TPException {
      boolean var6 = ntrace.isTraceEnabled(8);
      if (var6) {
         ntrace.doTrace("[/TGIOPUtil/routeSetHost/0");
      }

      if (var4 != 0) {
         if (var6) {
            ntrace.doTrace("*]/TGIOPUtil/routeSetHost/10");
         }

         throw new TPException(4);
      } else {
         var0.callout = null;
         RouteTcb var7;
         if (var0.route != null) {
            var7 = (RouteTcb)var0.route.body;
            if (var0.tdom_vals != null) {
               TdomValsTcb var8 = (TdomValsTcb)var0.tdom_vals.body;
               var7.setFlags((short)(var7.getFlags() | 1));
            }

            if (var6) {
               ntrace.doTrace("]/TGIOPUtil/routeSetHost/20");
            }

         } else {
            var0.route = new tcm((short)9, new RouteTcb(var2));
            var7 = (RouteTcb)var0.route.body;
            var7.setPort(var3);
            if (var0.tdom_vals != null) {
               var7.setFlags((short)(var7.getFlags() | 1));
            }

            if (var1 == null) {
               var7.setSvcTmidInd((short)1);
            } else {
               var7.setHost(var1);
               var7.setHostLen(var2);
               int var5 = (var2 - 1) / 4 + 1;
               var7.setSvcTmidInd((short)var5);
            }

            if (var6) {
               ntrace.doTrace("]/TGIOPUtil/routeSetHost/30");
            }

         }
      }
   }

   public static final int getShortBigEndian(byte[] var0, int var1) {
      int var2 = var0[var1] << 8 & '\uff00';
      int var3 = var0[var1 + 1] & 255;
      return var2 | var3;
   }

   public static final int getShortLittleEndian(byte[] var0, int var1) {
      int var2 = var0[var1 + 1] << 8 & '\uff00';
      int var3 = var0[var1] & 255;
      return var2 | var3;
   }

   public static final int extractLong(DataInputStream var0, int var1) throws IOException {
      byte[] var3 = new byte[4];
      var0.readFully(var3);
      int var2;
      if (var1 == 0) {
         var2 = MessageHeaderUtils.getIntBigEndian(var3, 0);
      } else {
         var2 = MessageHeaderUtils.getIntLittleEndian(var3, 0);
      }

      return var2;
   }

   public static final short extractShort(DataInputStream var0, int var1) throws IOException {
      byte[] var3 = new byte[2];
      var0.readFully(var3);
      short var2;
      if (var1 == 0) {
         var2 = (short)getShortBigEndian(var3, 0);
      } else {
         var2 = (short)getShortLittleEndian(var3, 0);
      }

      return var2;
   }

   public static void calloutSet(tfmh var0, Objinfo var1, Objrecv var2, int var3) throws TPException {
      String var4 = null;
      int var5 = 1;
      int var6 = 0;
      boolean var7 = ntrace.isTraceEnabled(8);
      if (var7) {
         ntrace.doTrace("[/TGIOPUtil/calloutSet/0");
      }

      if (var3 == 0 && var1 != null) {
         if (var0.callout != null) {
            if (var7) {
               ntrace.doTrace("]/TGIOPUtil/calloutSet/20");
            }

         } else {
            var0.callout = new tcm((short)16, new CalloutTcb());
            CalloutTcb var8 = (CalloutTcb)var0.callout.body;
            if (var0.tdom_vals != null) {
               var8.setFlags(var8.getFlags() | 1);
            }

            String var9 = var8.getSrc().getDomain();
            var8.setSrc(new ClientInfo(var1.getSendSrcCltinfo()));
            var8.getSrc().setVersion(1);
            var8.getSrc().setDomain(var9);
            var9 = var8.getDest().getDomain();
            var8.setDest(new ClientInfo(var1.getCltinfo()));
            var8.getDest().setVersion(1);
            var8.getDest().setDomain(var9);
            var8.setConnGen(var1.getConnGen());
            var8.setConnId(var1.getConnId());
            if (var2 != null) {
               var4 = var2.getHost();
               var5 = var4.length();
               var6 = var2.getPort();
            }

            if (var4 != null) {
               var8.setHostlen(var5);
               var8.setHost(new String(var4));
            } else {
               var8.setHostlen(1);
               var8.setHost(new String(""));
            }

            var8.setPort(var6);
            var0.route = null;
            if (var7) {
               ntrace.doTrace("]/TGIOPUtil/calloutSet/40");
            }

         }
      } else {
         if (var7) {
            ntrace.doTrace("*]/TGIOPUtil/calloutSet/10");
         }

         throw new TPException(4);
      }
   }

   public static void injectMsgIntoRMI(tfmh var0, MethodParameters var1) throws IOException {
      Chunk var3;
      Chunk var4 = var3 = Chunk.getChunk();
      boolean var5 = ntrace.isTraceEnabled(8);
      if (var5) {
         ntrace.doTrace("[/TGIOPUtil/injectMsgIntoRMI/0/" + var0 + "/" + var1);
      }

      TypedTGIOP var2 = (TypedTGIOP)((UserTcb)var0.user.body).user_data;
      int var6 = var2.send_size;
      if (var5) {
         ntrace.doTrace("/TGIOPUtil/injectMsgIntoRMI/10/remaining = " + var6);
      }

      int var8;
      for(int var7 = 0; var6 > 0; var6 -= var8) {
         if (var3.end == Chunk.CHUNK_SIZE) {
            var3.next = Chunk.getChunk();
            var3 = var3.next;
         }

         var8 = Math.min(var6, Chunk.CHUNK_SIZE - var3.end);
         System.arraycopy(var2.tgiop, var7, var3.buf, var3.end, var8);
         var7 += var8;
         var3.end += var8;
      }

      if (var5) {
         ntrace.doTrace("/TGIOPUtil/injectMsgIntoRMI/20");
      }

      TGIOPConnection var9 = new TGIOPConnection(var1);
      ConnectionManager.getConnectionManager().dispatch(var9, var4);
      if (var5) {
         ntrace.doTrace("]/TGIOPUtil/injectMsgIntoRMI/30");
      }

   }

   public static Exception mapTPError(int var0) {
      Object var1;
      switch (var0) {
         case 1:
            var1 = new TRANSACTION_ROLLEDBACK();
            break;
         case 2:
         case 4:
         case 9:
         case 11:
         case 12:
         case 16:
         case 19:
         case 20:
         case 21:
         case 22:
            var1 = new INTERNAL();
            break;
         case 3:
         case 7:
         case 15:
            var1 = new TRANSIENT();
            break;
         case 5:
            var1 = new NO_RESOURCES();
            break;
         case 6:
            var1 = new NO_IMPLEMENT();
            break;
         case 8:
            var1 = new NO_PERMISSION();
            break;
         case 10:
            var1 = new OBJ_ADAPTER();
            break;
         case 13:
            var1 = new NO_RESPONSE();
            break;
         case 14:
            var1 = new INVALID_TRANSACTION();
            break;
         case 17:
         case 18:
            var1 = new MARSHAL();
            break;
         case 23:
            var1 = new INITIALIZE();
            break;
         default:
            var1 = new UNKNOWN();
      }

      return (Exception)var1;
   }
}
