package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import java.sql.Connection;
import java.sql.Wrapper;
import java.util.Properties;
import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import oracle.ucp.ConnectionLabelingCallback;
import oracle.ucp.jdbc.ConnectionInitializationCallback;
import weblogic.jdbc.extensions.WLDataSource;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class RmiDataSource_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$sql$Connection;
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$java$util$Properties;
   // $FF: synthetic field
   private static Class class$oracle$ucp$ConnectionLabelingCallback;
   // $FF: synthetic field
   private static Class class$oracle$ucp$jdbc$ConnectionInitializationCallback;
   // $FF: synthetic field
   private static Class class$java$lang$Class;
   // $FF: synthetic field
   private static Class class$java$io$PrintWriter;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      Class var5;
      int var42;
      PrintWriter var43;
      String var45;
      String var48;
      Connection var49;
      switch (var1) {
         case 0:
            Connection var50 = ((DataSource)var4).getConnection();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var50, class$java$sql$Connection == null ? (class$java$sql$Connection = class$("java.sql.Connection")) : class$java$sql$Connection);
               break;
            } catch (IOException var41) {
               throw new MarshalException("error marshalling return", var41);
            }
         case 1:
            try {
               MsgInput var7 = var2.getMsgInput();
               var48 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var45 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var39) {
               throw new UnmarshalException("error unmarshalling arguments", var39);
            } catch (ClassNotFoundException var40) {
               throw new UnmarshalException("error unmarshalling arguments", var40);
            }

            var49 = ((DataSource)var4).getConnection(var48, var45);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var49, class$java$sql$Connection == null ? (class$java$sql$Connection = class$("java.sql.Connection")) : class$java$sql$Connection);
               break;
            } catch (IOException var38) {
               throw new MarshalException("error marshalling return", var38);
            }
         case 2:
            Properties var51;
            try {
               MsgInput var9 = var2.getMsgInput();
               var48 = (String)var9.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var45 = (String)var9.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var51 = (Properties)var9.readObject(class$java$util$Properties == null ? (class$java$util$Properties = class$("java.util.Properties")) : class$java$util$Properties);
            } catch (IOException var36) {
               throw new UnmarshalException("error unmarshalling arguments", var36);
            } catch (ClassNotFoundException var37) {
               throw new UnmarshalException("error unmarshalling arguments", var37);
            }

            Connection var53 = ((WLDataSource)var4).getConnection(var48, var45, var51);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var53, class$java$sql$Connection == null ? (class$java$sql$Connection = class$("java.sql.Connection")) : class$java$sql$Connection);
               break;
            } catch (IOException var35) {
               throw new MarshalException("error marshalling return", var35);
            }
         case 3:
            Properties var47;
            try {
               MsgInput var6 = var2.getMsgInput();
               var47 = (Properties)var6.readObject(class$java$util$Properties == null ? (class$java$util$Properties = class$("java.util.Properties")) : class$java$util$Properties);
            } catch (IOException var33) {
               throw new UnmarshalException("error unmarshalling arguments", var33);
            } catch (ClassNotFoundException var34) {
               throw new UnmarshalException("error unmarshalling arguments", var34);
            }

            var49 = ((WLDataSource)var4).getConnection(var47);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var49, class$java$sql$Connection == null ? (class$java$sql$Connection = class$("java.sql.Connection")) : class$java$sql$Connection);
               break;
            } catch (IOException var32) {
               throw new MarshalException("error marshalling return", var32);
            }
         case 4:
            var43 = ((CommonDataSource)var4).getLogWriter();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var43, class$java$io$PrintWriter == null ? (class$java$io$PrintWriter = class$("java.io.PrintWriter")) : class$java$io$PrintWriter);
               break;
            } catch (IOException var31) {
               throw new MarshalException("error marshalling return", var31);
            }
         case 5:
            var42 = ((CommonDataSource)var4).getLoginTimeout();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeInt(var42);
               break;
            } catch (IOException var30) {
               throw new MarshalException("error marshalling return", var30);
            }
         case 6:
            try {
               MsgInput var8 = var2.getMsgInput();
               var5 = (Class)var8.readObject(class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class);
            } catch (IOException var28) {
               throw new UnmarshalException("error unmarshalling arguments", var28);
            } catch (ClassNotFoundException var29) {
               throw new UnmarshalException("error unmarshalling arguments", var29);
            }

            boolean var52 = ((Wrapper)var4).isWrapperFor(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var52);
               break;
            } catch (IOException var27) {
               throw new MarshalException("error marshalling return", var27);
            }
         case 7:
            ConnectionInitializationCallback var46;
            try {
               MsgInput var10 = var2.getMsgInput();
               var46 = (ConnectionInitializationCallback)var10.readObject(class$oracle$ucp$jdbc$ConnectionInitializationCallback == null ? (class$oracle$ucp$jdbc$ConnectionInitializationCallback = class$("oracle.ucp.jdbc.ConnectionInitializationCallback")) : class$oracle$ucp$jdbc$ConnectionInitializationCallback);
            } catch (IOException var25) {
               throw new UnmarshalException("error unmarshalling arguments", var25);
            } catch (ClassNotFoundException var26) {
               throw new UnmarshalException("error unmarshalling arguments", var26);
            }

            ((WLDataSource)var4).registerConnectionInitializationCallback(var46);
            this.associateResponseData(var2, var3);
            break;
         case 8:
            ConnectionLabelingCallback var44;
            try {
               MsgInput var11 = var2.getMsgInput();
               var44 = (ConnectionLabelingCallback)var11.readObject(class$oracle$ucp$ConnectionLabelingCallback == null ? (class$oracle$ucp$ConnectionLabelingCallback = class$("oracle.ucp.ConnectionLabelingCallback")) : class$oracle$ucp$ConnectionLabelingCallback);
            } catch (IOException var23) {
               throw new UnmarshalException("error unmarshalling arguments", var23);
            } catch (ClassNotFoundException var24) {
               throw new UnmarshalException("error unmarshalling arguments", var24);
            }

            ((WLDataSource)var4).registerConnectionLabelingCallback(var44);
            this.associateResponseData(var2, var3);
            break;
         case 9:
            ((WLDataSource)var4).removeConnectionLabelingCallback();
            this.associateResponseData(var2, var3);
            break;
         case 10:
            try {
               MsgInput var12 = var2.getMsgInput();
               var43 = (PrintWriter)var12.readObject(class$java$io$PrintWriter == null ? (class$java$io$PrintWriter = class$("java.io.PrintWriter")) : class$java$io$PrintWriter);
            } catch (IOException var21) {
               throw new UnmarshalException("error unmarshalling arguments", var21);
            } catch (ClassNotFoundException var22) {
               throw new UnmarshalException("error unmarshalling arguments", var22);
            }

            ((CommonDataSource)var4).setLogWriter(var43);
            this.associateResponseData(var2, var3);
            break;
         case 11:
            try {
               MsgInput var13 = var2.getMsgInput();
               var42 = var13.readInt();
            } catch (IOException var20) {
               throw new UnmarshalException("error unmarshalling arguments", var20);
            }

            ((CommonDataSource)var4).setLoginTimeout(var42);
            this.associateResponseData(var2, var3);
            break;
         case 12:
            ((WLDataSource)var4).unregisterConnectionInitializationCallback();
            this.associateResponseData(var2, var3);
            break;
         case 13:
            try {
               MsgInput var14 = var2.getMsgInput();
               var5 = (Class)var14.readObject(class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class);
            } catch (IOException var18) {
               throw new UnmarshalException("error unmarshalling arguments", var18);
            } catch (ClassNotFoundException var19) {
               throw new UnmarshalException("error unmarshalling arguments", var19);
            }

            Object var15 = ((Wrapper)var4).unwrap(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var15, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var17) {
               throw new MarshalException("error marshalling return", var17);
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
            return ((DataSource)var3).getConnection();
         case 1:
            return ((DataSource)var3).getConnection((String)var2[0], (String)var2[1]);
         case 2:
            return ((WLDataSource)var3).getConnection((String)var2[0], (String)var2[1], (Properties)var2[2]);
         case 3:
            return ((WLDataSource)var3).getConnection((Properties)var2[0]);
         case 4:
            return ((CommonDataSource)var3).getLogWriter();
         case 5:
            return new Integer(((CommonDataSource)var3).getLoginTimeout());
         case 6:
            return new Boolean(((Wrapper)var3).isWrapperFor((Class)var2[0]));
         case 7:
            ((WLDataSource)var3).registerConnectionInitializationCallback((ConnectionInitializationCallback)var2[0]);
            return null;
         case 8:
            ((WLDataSource)var3).registerConnectionLabelingCallback((ConnectionLabelingCallback)var2[0]);
            return null;
         case 9:
            ((WLDataSource)var3).removeConnectionLabelingCallback();
            return null;
         case 10:
            ((CommonDataSource)var3).setLogWriter((PrintWriter)var2[0]);
            return null;
         case 11:
            ((CommonDataSource)var3).setLoginTimeout((Integer)var2[0]);
            return null;
         case 12:
            ((WLDataSource)var3).unregisterConnectionInitializationCallback();
            return null;
         case 13:
            return ((Wrapper)var3).unwrap((Class)var2[0]);
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
