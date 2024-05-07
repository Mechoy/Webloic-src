package weblogic.management.internal;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.management.configuration.ListResults;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class RemoteRealmManagerImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$weblogic$management$configuration$ListResults;
   // $FF: synthetic field
   private static Class array$Ljava$lang$String;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      String var5;
      ListResults var69;
      ListResults var77;
      switch (var1) {
         case 0:
            try {
               MsgInput var6 = var2.getMsgInput();
               var5 = (String)var6.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var67) {
               throw new UnmarshalException("error unmarshalling arguments", var67);
            } catch (ClassNotFoundException var68) {
               throw new UnmarshalException("error unmarshalling arguments", var68);
            }

            boolean var72 = ((RemoteRealmManager)var4).aclExists(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var72);
               break;
            } catch (IOException var66) {
               throw new MarshalException("error marshalling return", var66);
            }
         case 1:
            String var71;
            try {
               MsgInput var8 = var2.getMsgInput();
               var5 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var71 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var64) {
               throw new UnmarshalException("error unmarshalling arguments", var64);
            } catch (ClassNotFoundException var65) {
               throw new UnmarshalException("error unmarshalling arguments", var65);
            }

            ((RemoteRealmManager)var4).addMember(var5, var71);
            this.associateResponseData(var2, var3);
            break;
         case 2:
            Object var70;
            Object var73;
            try {
               MsgInput var10 = var2.getMsgInput();
               var5 = (String)var10.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var70 = (Object)var10.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var73 = (Object)var10.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var62) {
               throw new UnmarshalException("error unmarshalling arguments", var62);
            } catch (ClassNotFoundException var63) {
               throw new UnmarshalException("error unmarshalling arguments", var63);
            }

            boolean var76 = ((RemoteRealmManager)var4).changeCredential(var5, var70, var73);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var76);
               break;
            } catch (IOException var61) {
               throw new MarshalException("error marshalling return", var61);
            }
         case 3:
            try {
               MsgInput var7 = var2.getMsgInput();
               var5 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var59) {
               throw new UnmarshalException("error unmarshalling arguments", var59);
            } catch (ClassNotFoundException var60) {
               throw new UnmarshalException("error unmarshalling arguments", var60);
            }

            ((RemoteRealmManager)var4).createAcl(var5);
            this.associateResponseData(var2, var3);
            break;
         case 4:
            try {
               MsgInput var9 = var2.getMsgInput();
               var5 = (String)var9.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var57) {
               throw new UnmarshalException("error unmarshalling arguments", var57);
            } catch (ClassNotFoundException var58) {
               throw new UnmarshalException("error unmarshalling arguments", var58);
            }

            ((RemoteRealmManager)var4).createGroup(var5);
            this.associateResponseData(var2, var3);
            break;
         case 5:
            Object var75;
            try {
               MsgInput var12 = var2.getMsgInput();
               var5 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var75 = (Object)var12.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var55) {
               throw new UnmarshalException("error unmarshalling arguments", var55);
            } catch (ClassNotFoundException var56) {
               throw new UnmarshalException("error unmarshalling arguments", var56);
            }

            ((RemoteRealmManager)var4).createUser(var5, var75);
            this.associateResponseData(var2, var3);
            break;
         case 6:
            String var74;
            try {
               MsgInput var13 = var2.getMsgInput();
               var5 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var74 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var53) {
               throw new UnmarshalException("error unmarshalling arguments", var53);
            } catch (ClassNotFoundException var54) {
               throw new UnmarshalException("error unmarshalling arguments", var54);
            }

            var77 = ((RemoteRealmManager)var4).getGrantees(var5, var74);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var77, class$weblogic$management$configuration$ListResults == null ? (class$weblogic$management$configuration$ListResults = class$("weblogic.management.configuration.ListResults")) : class$weblogic$management$configuration$ListResults);
               break;
            } catch (IOException var52) {
               throw new MarshalException("error marshalling return", var52);
            }
         case 7:
            try {
               MsgInput var11 = var2.getMsgInput();
               var5 = (String)var11.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var50) {
               throw new UnmarshalException("error unmarshalling arguments", var50);
            } catch (ClassNotFoundException var51) {
               throw new UnmarshalException("error unmarshalling arguments", var51);
            }

            var77 = ((RemoteRealmManager)var4).getMembers(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var77, class$weblogic$management$configuration$ListResults == null ? (class$weblogic$management$configuration$ListResults = class$("weblogic.management.configuration.ListResults")) : class$weblogic$management$configuration$ListResults);
               break;
            } catch (IOException var49) {
               throw new MarshalException("error marshalling return", var49);
            }
         case 8:
            try {
               MsgInput var14 = var2.getMsgInput();
               var5 = (String)var14.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var47) {
               throw new UnmarshalException("error unmarshalling arguments", var47);
            } catch (ClassNotFoundException var48) {
               throw new UnmarshalException("error unmarshalling arguments", var48);
            }

            String[] var79 = ((RemoteRealmManager)var4).getPermissions(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var79, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               break;
            } catch (IOException var46) {
               throw new MarshalException("error marshalling return", var46);
            }
         case 9:
            String var78;
            String var81;
            try {
               MsgInput var17 = var2.getMsgInput();
               var5 = (String)var17.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var78 = (String)var17.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var81 = (String)var17.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var44) {
               throw new UnmarshalException("error unmarshalling arguments", var44);
            } catch (ClassNotFoundException var45) {
               throw new UnmarshalException("error unmarshalling arguments", var45);
            }

            ((RemoteRealmManager)var4).grantPermission(var5, var78, var81);
            this.associateResponseData(var2, var3);
            break;
         case 10:
            try {
               MsgInput var15 = var2.getMsgInput();
               var5 = (String)var15.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var42) {
               throw new UnmarshalException("error unmarshalling arguments", var42);
            } catch (ClassNotFoundException var43) {
               throw new UnmarshalException("error unmarshalling arguments", var43);
            }

            boolean var80 = ((RemoteRealmManager)var4).groupExists(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var80);
               break;
            } catch (IOException var41) {
               throw new MarshalException("error marshalling return", var41);
            }
         case 11:
            var69 = ((RemoteRealmManager)var4).listAcls();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var69, class$weblogic$management$configuration$ListResults == null ? (class$weblogic$management$configuration$ListResults = class$("weblogic.management.configuration.ListResults")) : class$weblogic$management$configuration$ListResults);
               break;
            } catch (IOException var40) {
               throw new MarshalException("error marshalling return", var40);
            }
         case 12:
            var69 = ((RemoteRealmManager)var4).listGroups();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var69, class$weblogic$management$configuration$ListResults == null ? (class$weblogic$management$configuration$ListResults = class$("weblogic.management.configuration.ListResults")) : class$weblogic$management$configuration$ListResults);
               break;
            } catch (IOException var39) {
               throw new MarshalException("error marshalling return", var39);
            }
         case 13:
            var69 = ((RemoteRealmManager)var4).listUsers();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var69, class$weblogic$management$configuration$ListResults == null ? (class$weblogic$management$configuration$ListResults = class$("weblogic.management.configuration.ListResults")) : class$weblogic$management$configuration$ListResults);
               break;
            } catch (IOException var38) {
               throw new MarshalException("error marshalling return", var38);
            }
         case 14:
            try {
               MsgInput var16 = var2.getMsgInput();
               var5 = (String)var16.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var36) {
               throw new UnmarshalException("error unmarshalling arguments", var36);
            } catch (ClassNotFoundException var37) {
               throw new UnmarshalException("error unmarshalling arguments", var37);
            }

            ((RemoteRealmManager)var4).removeAcl(var5);
            this.associateResponseData(var2, var3);
            break;
         case 15:
            try {
               MsgInput var18 = var2.getMsgInput();
               var5 = (String)var18.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var34) {
               throw new UnmarshalException("error unmarshalling arguments", var34);
            } catch (ClassNotFoundException var35) {
               throw new UnmarshalException("error unmarshalling arguments", var35);
            }

            ((RemoteRealmManager)var4).removeGroup(var5);
            this.associateResponseData(var2, var3);
            break;
         case 16:
            String var82;
            try {
               MsgInput var20 = var2.getMsgInput();
               var5 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var82 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var32) {
               throw new UnmarshalException("error unmarshalling arguments", var32);
            } catch (ClassNotFoundException var33) {
               throw new UnmarshalException("error unmarshalling arguments", var33);
            }

            ((RemoteRealmManager)var4).removeMember(var5, var82);
            this.associateResponseData(var2, var3);
            break;
         case 17:
            try {
               MsgInput var19 = var2.getMsgInput();
               var5 = (String)var19.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var30) {
               throw new UnmarshalException("error unmarshalling arguments", var30);
            } catch (ClassNotFoundException var31) {
               throw new UnmarshalException("error unmarshalling arguments", var31);
            }

            ((RemoteRealmManager)var4).removeUser(var5);
            this.associateResponseData(var2, var3);
            break;
         case 18:
            String var83;
            String var84;
            try {
               MsgInput var23 = var2.getMsgInput();
               var5 = (String)var23.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var83 = (String)var23.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var84 = (String)var23.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var28) {
               throw new UnmarshalException("error unmarshalling arguments", var28);
            } catch (ClassNotFoundException var29) {
               throw new UnmarshalException("error unmarshalling arguments", var29);
            }

            ((RemoteRealmManager)var4).revokePermission(var5, var83, var84);
            this.associateResponseData(var2, var3);
            break;
         case 19:
            try {
               MsgInput var21 = var2.getMsgInput();
               var5 = (String)var21.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var26) {
               throw new UnmarshalException("error unmarshalling arguments", var26);
            } catch (ClassNotFoundException var27) {
               throw new UnmarshalException("error unmarshalling arguments", var27);
            }

            boolean var22 = ((RemoteRealmManager)var4).userExists(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var22);
               break;
            } catch (IOException var25) {
               throw new MarshalException("error marshalling return", var25);
            }
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
            return new Boolean(((RemoteRealmManager)var3).aclExists((String)var2[0]));
         case 1:
            ((RemoteRealmManager)var3).addMember((String)var2[0], (String)var2[1]);
            return null;
         case 2:
            return new Boolean(((RemoteRealmManager)var3).changeCredential((String)var2[0], (Object)var2[1], (Object)var2[2]));
         case 3:
            ((RemoteRealmManager)var3).createAcl((String)var2[0]);
            return null;
         case 4:
            ((RemoteRealmManager)var3).createGroup((String)var2[0]);
            return null;
         case 5:
            ((RemoteRealmManager)var3).createUser((String)var2[0], (Object)var2[1]);
            return null;
         case 6:
            return ((RemoteRealmManager)var3).getGrantees((String)var2[0], (String)var2[1]);
         case 7:
            return ((RemoteRealmManager)var3).getMembers((String)var2[0]);
         case 8:
            return ((RemoteRealmManager)var3).getPermissions((String)var2[0]);
         case 9:
            ((RemoteRealmManager)var3).grantPermission((String)var2[0], (String)var2[1], (String)var2[2]);
            return null;
         case 10:
            return new Boolean(((RemoteRealmManager)var3).groupExists((String)var2[0]));
         case 11:
            return ((RemoteRealmManager)var3).listAcls();
         case 12:
            return ((RemoteRealmManager)var3).listGroups();
         case 13:
            return ((RemoteRealmManager)var3).listUsers();
         case 14:
            ((RemoteRealmManager)var3).removeAcl((String)var2[0]);
            return null;
         case 15:
            ((RemoteRealmManager)var3).removeGroup((String)var2[0]);
            return null;
         case 16:
            ((RemoteRealmManager)var3).removeMember((String)var2[0], (String)var2[1]);
            return null;
         case 17:
            ((RemoteRealmManager)var3).removeUser((String)var2[0]);
            return null;
         case 18:
            ((RemoteRealmManager)var3).revokePermission((String)var2[0], (String)var2[1], (String)var2[2]);
            return null;
         case 19:
            return new Boolean(((RemoteRealmManager)var3).userExists((String)var2[0]));
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
