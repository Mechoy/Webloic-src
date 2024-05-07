package weblogic.jndi.internal;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.event.NamingListener;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class RootNamingNode_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$javax$naming$NameParser;
   // $FF: synthetic field
   private static Class class$javax$naming$Name;
   // $FF: synthetic field
   private static Class class$javax$naming$event$NamingListener;
   // $FF: synthetic field
   private static Class class$javax$naming$NamingEnumeration;
   // $FF: synthetic field
   private static Class class$weblogic$jndi$internal$NamingNode;
   // $FF: synthetic field
   private static Class class$java$util$List;
   // $FF: synthetic field
   private static Class class$javax$naming$Context;
   // $FF: synthetic field
   private static Class class$java$lang$String;
   // $FF: synthetic field
   private static Class class$java$util$Hashtable;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      String var5;
      Object var13;
      Hashtable var23;
      NamingListener var74;
      Hashtable var77;
      Hashtable var81;
      String var86;
      Hashtable var87;
      Hashtable var92;
      Object var93;
      switch (var1) {
         case 0:
            int var76;
            NamingListener var80;
            try {
               MsgInput var9 = var2.getMsgInput();
               var5 = (String)var9.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var76 = var9.readInt();
               var80 = (NamingListener)var9.readObject(class$javax$naming$event$NamingListener == null ? (class$javax$naming$event$NamingListener = class$("javax.naming.event.NamingListener")) : class$javax$naming$event$NamingListener);
               var81 = (Hashtable)var9.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var72) {
               throw new UnmarshalException("error unmarshalling arguments", var72);
            } catch (ClassNotFoundException var73) {
               throw new UnmarshalException("error unmarshalling arguments", var73);
            }

            ((NamingNode)var4).addNamingListener(var5, var76, var80, var81);
            this.associateResponseData(var2, var3);
            break;
         case 1:
            try {
               MsgInput var6 = var2.getMsgInput();
               var74 = (NamingListener)var6.readObject(class$javax$naming$event$NamingListener == null ? (class$javax$naming$event$NamingListener = class$("javax.naming.event.NamingListener")) : class$javax$naming$event$NamingListener);
            } catch (IOException var70) {
               throw new UnmarshalException("error unmarshalling arguments", var70);
            } catch (ClassNotFoundException var71) {
               throw new UnmarshalException("error unmarshalling arguments", var71);
            }

            ((NamingNode)var4).addOneLevelScopeNamingListener(var74);
            this.associateResponseData(var2, var3);
            break;
         case 2:
            Object var78;
            try {
               MsgInput var10 = var2.getMsgInput();
               var5 = (String)var10.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var78 = (Object)var10.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var81 = (Hashtable)var10.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var68) {
               throw new UnmarshalException("error unmarshalling arguments", var68);
            } catch (ClassNotFoundException var69) {
               throw new UnmarshalException("error unmarshalling arguments", var69);
            }

            ((NamingNode)var4).bind(var5, var78, var81);
            this.associateResponseData(var2, var3);
            break;
         case 3:
            try {
               MsgInput var8 = var2.getMsgInput();
               var5 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var77 = (Hashtable)var8.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var66) {
               throw new UnmarshalException("error unmarshalling arguments", var66);
            } catch (ClassNotFoundException var67) {
               throw new UnmarshalException("error unmarshalling arguments", var67);
            }

            Context var84 = ((NamingNode)var4).createSubcontext(var5, var77);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var84, class$javax$naming$Context == null ? (class$javax$naming$Context = class$("javax.naming.Context")) : class$javax$naming$Context);
               break;
            } catch (IOException var65) {
               throw new MarshalException("error marshalling return", var65);
            }
         case 4:
            try {
               MsgInput var11 = var2.getMsgInput();
               var5 = (String)var11.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var77 = (Hashtable)var11.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var63) {
               throw new UnmarshalException("error unmarshalling arguments", var63);
            } catch (ClassNotFoundException var64) {
               throw new UnmarshalException("error unmarshalling arguments", var64);
            }

            ((NamingNode)var4).destroySubcontext(var5, var77);
            this.associateResponseData(var2, var3);
            break;
         case 5:
            Hashtable var83;
            try {
               MsgInput var7 = var2.getMsgInput();
               var83 = (Hashtable)var7.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var61) {
               throw new UnmarshalException("error unmarshalling arguments", var61);
            } catch (ClassNotFoundException var62) {
               throw new UnmarshalException("error unmarshalling arguments", var62);
            }

            Context var85 = ((NamingNode)var4).getContext(var83);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var85, class$javax$naming$Context == null ? (class$javax$naming$Context = class$("javax.naming.Context")) : class$javax$naming$Context);
               break;
            } catch (IOException var60) {
               throw new MarshalException("error marshalling return", var60);
            }
         case 6:
            var5 = ((NamingNode)var4).getNameInNamespace();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var5, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var59) {
               throw new MarshalException("error marshalling return", var59);
            }
         case 7:
            try {
               MsgInput var12 = var2.getMsgInput();
               var5 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var57) {
               throw new UnmarshalException("error unmarshalling arguments", var57);
            } catch (ClassNotFoundException var58) {
               throw new UnmarshalException("error unmarshalling arguments", var58);
            }

            var86 = ((NamingNode)var4).getNameInNamespace(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var86, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var56) {
               throw new MarshalException("error marshalling return", var56);
            }
         case 8:
            try {
               MsgInput var14 = var2.getMsgInput();
               var5 = (String)var14.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (Hashtable)var14.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var54) {
               throw new UnmarshalException("error unmarshalling arguments", var54);
            } catch (ClassNotFoundException var55) {
               throw new UnmarshalException("error unmarshalling arguments", var55);
            }

            NameParser var88 = ((NamingNode)var4).getNameParser(var5, var87);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var88, class$javax$naming$NameParser == null ? (class$javax$naming$NameParser = class$("javax.naming.NameParser")) : class$javax$naming$NameParser);
               break;
            } catch (IOException var53) {
               throw new MarshalException("error marshalling return", var53);
            }
         case 9:
            List var82 = ((NamingNode)var4).getOneLevelScopeNamingListeners();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var82, class$java$util$List == null ? (class$java$util$List = class$("java.util.List")) : class$java$util$List);
               break;
            } catch (IOException var52) {
               throw new MarshalException("error marshalling return", var52);
            }
         case 10:
            NamingNode var79 = ((NamingNode)var4).getParent();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var79, class$weblogic$jndi$internal$NamingNode == null ? (class$weblogic$jndi$internal$NamingNode = class$("weblogic.jndi.internal.NamingNode")) : class$weblogic$jndi$internal$NamingNode);
               break;
            } catch (IOException var51) {
               throw new MarshalException("error marshalling return", var51);
            }
         case 11:
            try {
               MsgInput var15 = var2.getMsgInput();
               var5 = (String)var15.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (Hashtable)var15.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var49) {
               throw new UnmarshalException("error unmarshalling arguments", var49);
            } catch (ClassNotFoundException var50) {
               throw new UnmarshalException("error unmarshalling arguments", var50);
            }

            NamingEnumeration var89 = ((NamingNode)var4).list(var5, var87);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var89, class$javax$naming$NamingEnumeration == null ? (class$javax$naming$NamingEnumeration = class$("javax.naming.NamingEnumeration")) : class$javax$naming$NamingEnumeration);
               break;
            } catch (IOException var48) {
               throw new MarshalException("error marshalling return", var48);
            }
         case 12:
            try {
               MsgInput var16 = var2.getMsgInput();
               var5 = (String)var16.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (Hashtable)var16.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var46) {
               throw new UnmarshalException("error unmarshalling arguments", var46);
            } catch (ClassNotFoundException var47) {
               throw new UnmarshalException("error unmarshalling arguments", var47);
            }

            NamingEnumeration var90 = ((NamingNode)var4).listBindings(var5, var87);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var90, class$javax$naming$NamingEnumeration == null ? (class$javax$naming$NamingEnumeration = class$("javax.naming.NamingEnumeration")) : class$javax$naming$NamingEnumeration);
               break;
            } catch (IOException var45) {
               throw new MarshalException("error marshalling return", var45);
            }
         case 13:
            try {
               MsgInput var17 = var2.getMsgInput();
               var5 = (String)var17.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (Hashtable)var17.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var43) {
               throw new UnmarshalException("error unmarshalling arguments", var43);
            } catch (ClassNotFoundException var44) {
               throw new UnmarshalException("error unmarshalling arguments", var44);
            }

            Object var91 = ((NamingNode)var4).lookup(var5, var87);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var91, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var42) {
               throw new MarshalException("error marshalling return", var42);
            }
         case 14:
            try {
               MsgInput var18 = var2.getMsgInput();
               var5 = (String)var18.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (Hashtable)var18.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var40) {
               throw new UnmarshalException("error unmarshalling arguments", var40);
            } catch (ClassNotFoundException var41) {
               throw new UnmarshalException("error unmarshalling arguments", var41);
            }

            var93 = ((NamingNode)var4).lookupLink(var5, var87);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var93, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var39) {
               throw new MarshalException("error marshalling return", var39);
            }
         case 15:
            Hashtable var94;
            try {
               MsgInput var21 = var2.getMsgInput();
               var5 = (String)var21.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var13 = (Object)var21.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var93 = (Object)var21.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var94 = (Hashtable)var21.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var37) {
               throw new UnmarshalException("error unmarshalling arguments", var37);
            } catch (ClassNotFoundException var38) {
               throw new UnmarshalException("error unmarshalling arguments", var38);
            }

            ((NamingNode)var4).rebind(var5, var13, var93, var94);
            this.associateResponseData(var2, var3);
            break;
         case 16:
            try {
               MsgInput var20 = var2.getMsgInput();
               var5 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var13 = (Object)var20.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var92 = (Hashtable)var20.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var35) {
               throw new UnmarshalException("error unmarshalling arguments", var35);
            } catch (ClassNotFoundException var36) {
               throw new UnmarshalException("error unmarshalling arguments", var36);
            }

            ((NamingNode)var4).rebind(var5, var13, var92);
            this.associateResponseData(var2, var3);
            break;
         case 17:
            Name var75;
            try {
               MsgInput var22 = var2.getMsgInput();
               var75 = (Name)var22.readObject(class$javax$naming$Name == null ? (class$javax$naming$Name = class$("javax.naming.Name")) : class$javax$naming$Name);
               var13 = (Object)var22.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var92 = (Hashtable)var22.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var33) {
               throw new UnmarshalException("error unmarshalling arguments", var33);
            } catch (ClassNotFoundException var34) {
               throw new UnmarshalException("error unmarshalling arguments", var34);
            }

            ((NamingNode)var4).rebind(var75, var13, var92);
            this.associateResponseData(var2, var3);
            break;
         case 18:
            try {
               MsgInput var19 = var2.getMsgInput();
               var74 = (NamingListener)var19.readObject(class$javax$naming$event$NamingListener == null ? (class$javax$naming$event$NamingListener = class$("javax.naming.event.NamingListener")) : class$javax$naming$event$NamingListener);
               var87 = (Hashtable)var19.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var31) {
               throw new UnmarshalException("error unmarshalling arguments", var31);
            } catch (ClassNotFoundException var32) {
               throw new UnmarshalException("error unmarshalling arguments", var32);
            }

            ((NamingNode)var4).removeNamingListener(var74, var87);
            this.associateResponseData(var2, var3);
            break;
         case 19:
            try {
               MsgInput var24 = var2.getMsgInput();
               var5 = (String)var24.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var86 = (String)var24.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var23 = (Hashtable)var24.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var29) {
               throw new UnmarshalException("error unmarshalling arguments", var29);
            } catch (ClassNotFoundException var30) {
               throw new UnmarshalException("error unmarshalling arguments", var30);
            }

            ((NamingNode)var4).rename(var5, var86, var23);
            this.associateResponseData(var2, var3);
            break;
         case 20:
            try {
               MsgInput var25 = var2.getMsgInput();
               var5 = (String)var25.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var13 = (Object)var25.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var23 = (Hashtable)var25.readObject(class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable);
            } catch (IOException var27) {
               throw new UnmarshalException("error unmarshalling arguments", var27);
            } catch (ClassNotFoundException var28) {
               throw new UnmarshalException("error unmarshalling arguments", var28);
            }

            ((NamingNode)var4).unbind(var5, var13, var23);
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
            ((NamingNode)var3).addNamingListener((String)var2[0], (Integer)var2[1], (NamingListener)var2[2], (Hashtable)var2[3]);
            return null;
         case 1:
            ((NamingNode)var3).addOneLevelScopeNamingListener((NamingListener)var2[0]);
            return null;
         case 2:
            ((NamingNode)var3).bind((String)var2[0], (Object)var2[1], (Hashtable)var2[2]);
            return null;
         case 3:
            return ((NamingNode)var3).createSubcontext((String)var2[0], (Hashtable)var2[1]);
         case 4:
            ((NamingNode)var3).destroySubcontext((String)var2[0], (Hashtable)var2[1]);
            return null;
         case 5:
            return ((NamingNode)var3).getContext((Hashtable)var2[0]);
         case 6:
            return ((NamingNode)var3).getNameInNamespace();
         case 7:
            return ((NamingNode)var3).getNameInNamespace((String)var2[0]);
         case 8:
            return ((NamingNode)var3).getNameParser((String)var2[0], (Hashtable)var2[1]);
         case 9:
            return ((NamingNode)var3).getOneLevelScopeNamingListeners();
         case 10:
            return ((NamingNode)var3).getParent();
         case 11:
            return ((NamingNode)var3).list((String)var2[0], (Hashtable)var2[1]);
         case 12:
            return ((NamingNode)var3).listBindings((String)var2[0], (Hashtable)var2[1]);
         case 13:
            return ((NamingNode)var3).lookup((String)var2[0], (Hashtable)var2[1]);
         case 14:
            return ((NamingNode)var3).lookupLink((String)var2[0], (Hashtable)var2[1]);
         case 15:
            ((NamingNode)var3).rebind((String)var2[0], (Object)var2[1], (Object)var2[2], (Hashtable)var2[3]);
            return null;
         case 16:
            ((NamingNode)var3).rebind((String)var2[0], (Object)var2[1], (Hashtable)var2[2]);
            return null;
         case 17:
            ((NamingNode)var3).rebind((Name)var2[0], (Object)var2[1], (Hashtable)var2[2]);
            return null;
         case 18:
            ((NamingNode)var3).removeNamingListener((NamingListener)var2[0], (Hashtable)var2[1]);
            return null;
         case 19:
            ((NamingNode)var3).rename((String)var2[0], (String)var2[1], (Hashtable)var2[2]);
            return null;
         case 20:
            ((NamingNode)var3).unbind((String)var2[0], (Object)var2[1], (Hashtable)var2[2]);
            return null;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
