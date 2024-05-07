package weblogic.transaction.internal;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import java.util.Map;
import javax.transaction.xa.Xid;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;
import weblogic.security.acl.internal.AuthenticatedUser;

public final class SubCoordinatorImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$weblogic$transaction$internal$NotificationListener;
   // $FF: synthetic field
   private static Class array$Ljavax$transaction$xa$Xid;
   // $FF: synthetic field
   private static Class class$java$lang$String;
   // $FF: synthetic field
   private static Class class$weblogic$transaction$internal$PropagationContext;
   // $FF: synthetic field
   private static Class array$Ljava$lang$String;
   // $FF: synthetic field
   private static Class class$java$util$Map;
   // $FF: synthetic field
   private static Class class$weblogic$transaction$internal$Notification;
   // $FF: synthetic field
   private static Class class$javax$transaction$xa$Xid;
   // $FF: synthetic field
   private static Class class$weblogic$security$acl$internal$AuthenticatedUser;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      String[] var18;
      AuthenticatedUser var23;
      String[] var27;
      Map var29;
      Xid var81;
      String var84;
      NotificationListener var85;
      String var93;
      String[] var94;
      Xid[] var96;
      int var97;
      boolean var98;
      boolean var99;
      AuthenticatedUser var100;
      Map var101;
      switch (var1) {
         case 0:
            Object var82;
            try {
               MsgInput var7 = var2.getMsgInput();
               var85 = (NotificationListener)var7.readObject(class$weblogic$transaction$internal$NotificationListener == null ? (class$weblogic$transaction$internal$NotificationListener = class$("weblogic.transaction.internal.NotificationListener")) : class$weblogic$transaction$internal$NotificationListener);
               var82 = (Object)var7.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var79) {
               throw new UnmarshalException("error unmarshalling arguments", var79);
            } catch (ClassNotFoundException var80) {
               throw new UnmarshalException("error unmarshalling arguments", var80);
            }

            ((NotificationBroadcaster)var4).addNotificationListener(var85, var82);
            this.associateResponseData(var2, var3);
            break;
         case 1:
            try {
               MsgInput var6 = var2.getMsgInput();
               var81 = (Xid)var6.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
            } catch (IOException var77) {
               throw new UnmarshalException("error unmarshalling arguments", var77);
            } catch (ClassNotFoundException var78) {
               throw new UnmarshalException("error unmarshalling arguments", var78);
            }

            ((SubCoordinatorOneway3)var4).forceLocalCommit(var81);
            this.associateResponseData(var2, var3);
            break;
         case 2:
            try {
               MsgInput var8 = var2.getMsgInput();
               var81 = (Xid)var8.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
            } catch (IOException var75) {
               throw new UnmarshalException("error unmarshalling arguments", var75);
            } catch (ClassNotFoundException var76) {
               throw new UnmarshalException("error unmarshalling arguments", var76);
            }

            ((SubCoordinatorOneway3)var4).forceLocalRollback(var81);
            this.associateResponseData(var2, var3);
            break;
         case 3:
            try {
               MsgInput var9 = var2.getMsgInput();
               var84 = (String)var9.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var73) {
               throw new UnmarshalException("error unmarshalling arguments", var73);
            } catch (ClassNotFoundException var74) {
               throw new UnmarshalException("error unmarshalling arguments", var74);
            }

            Map var87 = ((SubCoordinatorRM)var4).getProperties(var84);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var87, class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
               break;
            } catch (IOException var72) {
               throw new MarshalException("error marshalling return", var72);
            }
         case 4:
            try {
               MsgInput var10 = var2.getMsgInput();
               var84 = (String)var10.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var70) {
               throw new UnmarshalException("error unmarshalling arguments", var70);
            } catch (ClassNotFoundException var71) {
               throw new UnmarshalException("error unmarshalling arguments", var71);
            }

            Map var91 = ((SubCoordinator3)var4).getSubCoordinatorInfo(var84);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var91, class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
               break;
            } catch (IOException var69) {
               throw new MarshalException("error marshalling return", var69);
            }
         case 5:
            Notification var86;
            Object var90;
            try {
               MsgInput var12 = var2.getMsgInput();
               var86 = (Notification)var12.readObject(class$weblogic$transaction$internal$Notification == null ? (class$weblogic$transaction$internal$Notification = class$("weblogic.transaction.internal.Notification")) : class$weblogic$transaction$internal$Notification);
               var90 = (Object)var12.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var67) {
               throw new UnmarshalException("error unmarshalling arguments", var67);
            } catch (ClassNotFoundException var68) {
               throw new UnmarshalException("error unmarshalling arguments", var68);
            }

            ((NotificationListener)var4).handleNotification(var86, var90);
            this.associateResponseData(var2, var3);
            break;
         case 6:
            boolean var89;
            String var92;
            try {
               MsgInput var14 = var2.getMsgInput();
               var81 = (Xid)var14.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var89 = var14.readBoolean();
               var92 = (String)var14.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var65) {
               throw new UnmarshalException("error unmarshalling arguments", var65);
            } catch (ClassNotFoundException var66) {
               throw new UnmarshalException("error unmarshalling arguments", var66);
            }

            ((SubCoordinator2)var4).nonXAResourceCommit(var81, var89, var92);
            this.associateResponseData(var2, var3);
            break;
         case 7:
            String var88;
            try {
               MsgInput var13 = var2.getMsgInput();
               var84 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var88 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var63) {
               throw new UnmarshalException("error unmarshalling arguments", var63);
            } catch (ClassNotFoundException var64) {
               throw new UnmarshalException("error unmarshalling arguments", var64);
            }

            var96 = ((SubCoordinator)var4).recover(var84, var88);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var96, array$Ljavax$transaction$xa$Xid == null ? (array$Ljavax$transaction$xa$Xid = class$("[Ljavax.transaction.xa.Xid;")) : array$Ljavax$transaction$xa$Xid);
               break;
            } catch (IOException var62) {
               throw new MarshalException("error marshalling return", var62);
            }
         case 8:
            try {
               MsgInput var11 = var2.getMsgInput();
               var85 = (NotificationListener)var11.readObject(class$weblogic$transaction$internal$NotificationListener == null ? (class$weblogic$transaction$internal$NotificationListener = class$("weblogic.transaction.internal.NotificationListener")) : class$weblogic$transaction$internal$NotificationListener);
            } catch (IOException var60) {
               throw new UnmarshalException("error unmarshalling arguments", var60);
            } catch (ClassNotFoundException var61) {
               throw new UnmarshalException("error unmarshalling arguments", var61);
            }

            ((NotificationBroadcaster)var4).removeNotificationListener(var85);
            this.associateResponseData(var2, var3);
            break;
         case 9:
            try {
               MsgInput var16 = var2.getMsgInput();
               var84 = (String)var16.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var96 = (Xid[])var16.readObject(array$Ljavax$transaction$xa$Xid == null ? (array$Ljavax$transaction$xa$Xid = class$("[Ljavax.transaction.xa.Xid;")) : array$Ljavax$transaction$xa$Xid);
            } catch (IOException var58) {
               throw new UnmarshalException("error unmarshalling arguments", var58);
            } catch (ClassNotFoundException var59) {
               throw new UnmarshalException("error unmarshalling arguments", var59);
            }

            ((SubCoordinator)var4).rollback(var84, var96);
            this.associateResponseData(var2, var3);
            break;
         case 10:
            try {
               MsgInput var20 = var2.getMsgInput();
               var81 = (Xid)var20.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var94 = (String[])var20.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var99 = var20.readBoolean();
               var98 = var20.readBoolean();
            } catch (IOException var56) {
               throw new UnmarshalException("error unmarshalling arguments", var56);
            } catch (ClassNotFoundException var57) {
               throw new UnmarshalException("error unmarshalling arguments", var57);
            }

            ((SubCoordinatorOneway)var4).startCommit(var81, var93, var94, var99, var98);
            this.associateResponseData(var2, var3);
            break;
         case 11:
            try {
               MsgInput var22 = var2.getMsgInput();
               var81 = (Xid)var22.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var22.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var94 = (String[])var22.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var99 = var22.readBoolean();
               var98 = var22.readBoolean();
               var100 = (AuthenticatedUser)var22.readObject(class$weblogic$security$acl$internal$AuthenticatedUser == null ? (class$weblogic$security$acl$internal$AuthenticatedUser = class$("weblogic.security.acl.internal.AuthenticatedUser")) : class$weblogic$security$acl$internal$AuthenticatedUser);
            } catch (IOException var54) {
               throw new UnmarshalException("error unmarshalling arguments", var54);
            } catch (ClassNotFoundException var55) {
               throw new UnmarshalException("error unmarshalling arguments", var55);
            }

            ((SubCoordinatorOneway2)var4).startCommit(var81, var93, var94, var99, var98, var100);
            this.associateResponseData(var2, var3);
            break;
         case 12:
            try {
               MsgInput var24 = var2.getMsgInput();
               var81 = (Xid)var24.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var24.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var94 = (String[])var24.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var99 = var24.readBoolean();
               var98 = var24.readBoolean();
               var100 = (AuthenticatedUser)var24.readObject(class$weblogic$security$acl$internal$AuthenticatedUser == null ? (class$weblogic$security$acl$internal$AuthenticatedUser = class$("weblogic.security.acl.internal.AuthenticatedUser")) : class$weblogic$security$acl$internal$AuthenticatedUser);
               var101 = (Map)var24.readObject(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
            } catch (IOException var52) {
               throw new UnmarshalException("error unmarshalling arguments", var52);
            } catch (ClassNotFoundException var53) {
               throw new UnmarshalException("error unmarshalling arguments", var53);
            }

            ((SubCoordinatorOneway5)var4).startCommit(var81, var93, var94, var99, var98, var100, var101);
            this.associateResponseData(var2, var3);
            break;
         case 13:
            PropagationContext var83;
            int var95;
            try {
               MsgInput var17 = var2.getMsgInput();
               var83 = (PropagationContext)var17.readObject(class$weblogic$transaction$internal$PropagationContext == null ? (class$weblogic$transaction$internal$PropagationContext = class$("weblogic.transaction.internal.PropagationContext")) : class$weblogic$transaction$internal$PropagationContext);
               var95 = var17.readInt();
            } catch (IOException var50) {
               throw new UnmarshalException("error unmarshalling arguments", var50);
            } catch (ClassNotFoundException var51) {
               throw new UnmarshalException("error unmarshalling arguments", var51);
            }

            ((SubCoordinatorOneway)var4).startPrePrepareAndChain(var83, var95);
            this.associateResponseData(var2, var3);
            break;
         case 14:
            try {
               MsgInput var21 = var2.getMsgInput();
               var81 = (Xid)var21.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var21.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var21.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var97 = var21.readInt();
            } catch (IOException var48) {
               throw new UnmarshalException("error unmarshalling arguments", var48);
            } catch (ClassNotFoundException var49) {
               throw new UnmarshalException("error unmarshalling arguments", var49);
            }

            ((SubCoordinatorOneway)var4).startPrepare(var81, var93, var18, var97);
            this.associateResponseData(var2, var3);
            break;
         case 15:
            try {
               MsgInput var25 = var2.getMsgInput();
               var81 = (Xid)var25.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var25.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var25.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var97 = var25.readInt();
               var101 = (Map)var25.readObject(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
            } catch (IOException var46) {
               throw new UnmarshalException("error unmarshalling arguments", var46);
            } catch (ClassNotFoundException var47) {
               throw new UnmarshalException("error unmarshalling arguments", var47);
            }

            ((SubCoordinatorOneway5)var4).startPrepare(var81, var93, var18, var97, var101);
            this.associateResponseData(var2, var3);
            break;
         case 16:
            try {
               MsgInput var19 = var2.getMsgInput();
               var81 = (Xid)var19.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var19.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var19.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var44) {
               throw new UnmarshalException("error unmarshalling arguments", var44);
            } catch (ClassNotFoundException var45) {
               throw new UnmarshalException("error unmarshalling arguments", var45);
            }

            ((SubCoordinatorOneway)var4).startRollback(var81, var93, var18);
            this.associateResponseData(var2, var3);
            break;
         case 17:
            try {
               MsgInput var26 = var2.getMsgInput();
               var81 = (Xid)var26.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var26.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var26.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var23 = (AuthenticatedUser)var26.readObject(class$weblogic$security$acl$internal$AuthenticatedUser == null ? (class$weblogic$security$acl$internal$AuthenticatedUser = class$("weblogic.security.acl.internal.AuthenticatedUser")) : class$weblogic$security$acl$internal$AuthenticatedUser);
            } catch (IOException var42) {
               throw new UnmarshalException("error unmarshalling arguments", var42);
            } catch (ClassNotFoundException var43) {
               throw new UnmarshalException("error unmarshalling arguments", var43);
            }

            ((SubCoordinatorOneway2)var4).startRollback(var81, var93, var18, var23);
            this.associateResponseData(var2, var3);
            break;
         case 18:
            try {
               MsgInput var28 = var2.getMsgInput();
               var81 = (Xid)var28.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var28.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var28.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var23 = (AuthenticatedUser)var28.readObject(class$weblogic$security$acl$internal$AuthenticatedUser == null ? (class$weblogic$security$acl$internal$AuthenticatedUser = class$("weblogic.security.acl.internal.AuthenticatedUser")) : class$weblogic$security$acl$internal$AuthenticatedUser);
               var27 = (String[])var28.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var40) {
               throw new UnmarshalException("error unmarshalling arguments", var40);
            } catch (ClassNotFoundException var41) {
               throw new UnmarshalException("error unmarshalling arguments", var41);
            }

            ((SubCoordinatorOneway4)var4).startRollback(var81, var93, var18, var23, var27);
            this.associateResponseData(var2, var3);
            break;
         case 19:
            try {
               MsgInput var30 = var2.getMsgInput();
               var81 = (Xid)var30.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var30.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var30.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var23 = (AuthenticatedUser)var30.readObject(class$weblogic$security$acl$internal$AuthenticatedUser == null ? (class$weblogic$security$acl$internal$AuthenticatedUser = class$("weblogic.security.acl.internal.AuthenticatedUser")) : class$weblogic$security$acl$internal$AuthenticatedUser);
               var27 = (String[])var30.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var29 = (Map)var30.readObject(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
            } catch (IOException var38) {
               throw new UnmarshalException("error unmarshalling arguments", var38);
            } catch (ClassNotFoundException var39) {
               throw new UnmarshalException("error unmarshalling arguments", var39);
            }

            ((SubCoordinatorOneway5)var4).startRollback(var81, var93, var18, var23, var27, var29);
            this.associateResponseData(var2, var3);
            break;
         case 20:
            boolean var31;
            try {
               MsgInput var32 = var2.getMsgInput();
               var81 = (Xid)var32.readObject(class$javax$transaction$xa$Xid == null ? (class$javax$transaction$xa$Xid = class$("javax.transaction.xa.Xid")) : class$javax$transaction$xa$Xid);
               var93 = (String)var32.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var18 = (String[])var32.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var23 = (AuthenticatedUser)var32.readObject(class$weblogic$security$acl$internal$AuthenticatedUser == null ? (class$weblogic$security$acl$internal$AuthenticatedUser = class$("weblogic.security.acl.internal.AuthenticatedUser")) : class$weblogic$security$acl$internal$AuthenticatedUser);
               var27 = (String[])var32.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               var29 = (Map)var32.readObject(class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map);
               var31 = var32.readBoolean();
            } catch (IOException var36) {
               throw new UnmarshalException("error unmarshalling arguments", var36);
            } catch (ClassNotFoundException var37) {
               throw new UnmarshalException("error unmarshalling arguments", var37);
            }

            ((SubCoordinatorOneway6)var4).startRollback(var81, var93, var18, var23, var27, var29, var31);
            this.associateResponseData(var2, var3);
            break;
         case 21:
            Xid[] var5;
            try {
               MsgInput var15 = var2.getMsgInput();
               var5 = (Xid[])var15.readObject(array$Ljavax$transaction$xa$Xid == null ? (array$Ljavax$transaction$xa$Xid = class$("[Ljavax.transaction.xa.Xid;")) : array$Ljavax$transaction$xa$Xid);
            } catch (IOException var34) {
               throw new UnmarshalException("error unmarshalling arguments", var34);
            } catch (ClassNotFoundException var35) {
               throw new UnmarshalException("error unmarshalling arguments", var35);
            }

            ((SubCoordinatorOneway)var4).startRollback(var5);
            this.associateResponseData(var2, var3);
            break;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }

      return var3;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public Object invoke(int var1, Object[] var2, Object var3) throws Exception {
      switch (var1) {
         case 0:
            ((NotificationBroadcaster)var3).addNotificationListener((NotificationListener)var2[0], (Object)var2[1]);
            return null;
         case 1:
            ((SubCoordinatorOneway3)var3).forceLocalCommit((Xid)var2[0]);
            return null;
         case 2:
            ((SubCoordinatorOneway3)var3).forceLocalRollback((Xid)var2[0]);
            return null;
         case 3:
            return ((SubCoordinatorRM)var3).getProperties((String)var2[0]);
         case 4:
            return ((SubCoordinator3)var3).getSubCoordinatorInfo((String)var2[0]);
         case 5:
            ((NotificationListener)var3).handleNotification((Notification)var2[0], (Object)var2[1]);
            return null;
         case 6:
            ((SubCoordinator2)var3).nonXAResourceCommit((Xid)var2[0], (Boolean)var2[1], (String)var2[2]);
            return null;
         case 7:
            return ((SubCoordinator)var3).recover((String)var2[0], (String)var2[1]);
         case 8:
            ((NotificationBroadcaster)var3).removeNotificationListener((NotificationListener)var2[0]);
            return null;
         case 9:
            ((SubCoordinator)var3).rollback((String)var2[0], (Xid[])var2[1]);
            return null;
         case 10:
            ((SubCoordinatorOneway)var3).startCommit((Xid)var2[0], (String)var2[1], (String[])var2[2], (Boolean)var2[3], (Boolean)var2[4]);
            return null;
         case 11:
            ((SubCoordinatorOneway2)var3).startCommit((Xid)var2[0], (String)var2[1], (String[])var2[2], (Boolean)var2[3], (Boolean)var2[4], (AuthenticatedUser)var2[5]);
            return null;
         case 12:
            ((SubCoordinatorOneway5)var3).startCommit((Xid)var2[0], (String)var2[1], (String[])var2[2], (Boolean)var2[3], (Boolean)var2[4], (AuthenticatedUser)var2[5], (Map)var2[6]);
            return null;
         case 13:
            ((SubCoordinatorOneway)var3).startPrePrepareAndChain((PropagationContext)var2[0], (Integer)var2[1]);
            return null;
         case 14:
            ((SubCoordinatorOneway)var3).startPrepare((Xid)var2[0], (String)var2[1], (String[])var2[2], (Integer)var2[3]);
            return null;
         case 15:
            ((SubCoordinatorOneway5)var3).startPrepare((Xid)var2[0], (String)var2[1], (String[])var2[2], (Integer)var2[3], (Map)var2[4]);
            return null;
         case 16:
            ((SubCoordinatorOneway)var3).startRollback((Xid)var2[0], (String)var2[1], (String[])var2[2]);
            return null;
         case 17:
            ((SubCoordinatorOneway2)var3).startRollback((Xid)var2[0], (String)var2[1], (String[])var2[2], (AuthenticatedUser)var2[3]);
            return null;
         case 18:
            ((SubCoordinatorOneway4)var3).startRollback((Xid)var2[0], (String)var2[1], (String[])var2[2], (AuthenticatedUser)var2[3], (String[])var2[4]);
            return null;
         case 19:
            ((SubCoordinatorOneway5)var3).startRollback((Xid)var2[0], (String)var2[1], (String[])var2[2], (AuthenticatedUser)var2[3], (String[])var2[4], (Map)var2[5]);
            return null;
         case 20:
            ((SubCoordinatorOneway6)var3).startRollback((Xid)var2[0], (String)var2[1], (String[])var2[2], (AuthenticatedUser)var2[3], (String[])var2[4], (Map)var2[5], (Boolean)var2[6]);
            return null;
         case 21:
            ((SubCoordinatorOneway)var3).startRollback((Xid[])var2[0]);
            return null;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
