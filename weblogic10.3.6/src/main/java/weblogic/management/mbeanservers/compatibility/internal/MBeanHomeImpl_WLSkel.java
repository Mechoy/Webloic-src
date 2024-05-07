package weblogic.management.mbeanservers.compatibility.internal;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import java.util.Set;
import javax.management.ObjectName;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class MBeanHomeImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$weblogic$management$RemoteMBeanServer;
   // $FF: synthetic field
   private static Class class$weblogic$management$configuration$DomainMBean;
   // $FF: synthetic field
   private static Class class$weblogic$management$WebLogicMBean;
   // $FF: synthetic field
   private static Class class$weblogic$management$configuration$ConfigurationMBean;
   // $FF: synthetic field
   private static Class class$weblogic$management$MBeanHome;
   // $FF: synthetic field
   private static Class class$java$util$Set;
   // $FF: synthetic field
   private static Class class$java$lang$Class;
   // $FF: synthetic field
   private static Class class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class class$java$lang$String;
   // $FF: synthetic field
   private static Class class$weblogic$management$runtime$RuntimeMBean;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      String var5;
      ObjectName var82;
      String var84;
      String var87;
      String var91;
      String var96;
      WebLogicMBean var103;
      String var104;
      switch (var1) {
         case 0:
            String var85;
            MBeanHome var92;
            try {
               MsgInput var8 = var2.getMsgInput();
               var92 = (MBeanHome)var8.readObject(class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome);
               var84 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var85 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var80) {
               throw new UnmarshalException("error unmarshalling arguments", var80);
            } catch (ClassNotFoundException var81) {
               throw new UnmarshalException("error unmarshalling arguments", var81);
            }

            ((MBeanHome)var4).addManagedHome(var92, var84, var85);
            this.associateResponseData(var2, var3);
            break;
         case 1:
            try {
               MsgInput var7 = var2.getMsgInput();
               var5 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var84 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var78) {
               throw new UnmarshalException("error unmarshalling arguments", var78);
            } catch (ClassNotFoundException var79) {
               throw new UnmarshalException("error unmarshalling arguments", var79);
            }

            WebLogicMBean var89 = ((MBeanHome)var4).createAdminMBean(var5, var84);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var89, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var77) {
               throw new MarshalException("error marshalling return", var77);
            }
         case 2:
            try {
               MsgInput var10 = var2.getMsgInput();
               var5 = (String)var10.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var84 = (String)var10.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (String)var10.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var75) {
               throw new UnmarshalException("error unmarshalling arguments", var75);
            } catch (ClassNotFoundException var76) {
               throw new UnmarshalException("error unmarshalling arguments", var76);
            }

            WebLogicMBean var94 = ((MBeanHome)var4).createAdminMBean(var5, var84, var87);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var94, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var74) {
               throw new MarshalException("error marshalling return", var74);
            }
         case 3:
            ConfigurationMBean var93;
            try {
               MsgInput var12 = var2.getMsgInput();
               var5 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var84 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var87 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var93 = (ConfigurationMBean)var12.readObject(class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
            } catch (IOException var72) {
               throw new UnmarshalException("error unmarshalling arguments", var72);
            } catch (ClassNotFoundException var73) {
               throw new UnmarshalException("error unmarshalling arguments", var73);
            }

            WebLogicMBean var95 = ((MBeanHome)var4).createAdminMBean(var5, var84, var87, var93);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var95, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var71) {
               throw new MarshalException("error marshalling return", var71);
            }
         case 4:
            try {
               MsgInput var6 = var2.getMsgInput();
               var82 = (ObjectName)var6.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var69) {
               throw new UnmarshalException("error unmarshalling arguments", var69);
            } catch (ClassNotFoundException var70) {
               throw new UnmarshalException("error unmarshalling arguments", var70);
            }

            ((MBeanHome)var4).deleteMBean(var82);
            this.associateResponseData(var2, var3);
            break;
         case 5:
            WebLogicMBean var90;
            try {
               MsgInput var9 = var2.getMsgInput();
               var90 = (WebLogicMBean)var9.readObject(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            } catch (IOException var67) {
               throw new UnmarshalException("error unmarshalling arguments", var67);
            } catch (ClassNotFoundException var68) {
               throw new UnmarshalException("error unmarshalling arguments", var68);
            }

            ((MBeanHome)var4).deleteMBean(var90);
            this.associateResponseData(var2, var3);
            break;
         case 6:
            DomainMBean var88 = ((MBeanHome)var4).getActiveDomain();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var88, class$weblogic$management$configuration$DomainMBean == null ? (class$weblogic$management$configuration$DomainMBean = class$("weblogic.management.configuration.DomainMBean")) : class$weblogic$management$configuration$DomainMBean);
               break;
            } catch (IOException var66) {
               throw new MarshalException("error marshalling return", var66);
            }
         case 7:
            try {
               MsgInput var13 = var2.getMsgInput();
               var5 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var91 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var64) {
               throw new UnmarshalException("error unmarshalling arguments", var64);
            } catch (ClassNotFoundException var65) {
               throw new UnmarshalException("error unmarshalling arguments", var65);
            }

            ConfigurationMBean var100 = ((MBeanHome)var4).getAdminMBean(var5, var91);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var100, class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
               break;
            } catch (IOException var63) {
               throw new MarshalException("error marshalling return", var63);
            }
         case 8:
            try {
               MsgInput var15 = var2.getMsgInput();
               var5 = (String)var15.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var91 = (String)var15.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var96 = (String)var15.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var61) {
               throw new UnmarshalException("error unmarshalling arguments", var61);
            } catch (ClassNotFoundException var62) {
               throw new UnmarshalException("error unmarshalling arguments", var62);
            }

            ConfigurationMBean var99 = ((MBeanHome)var4).getAdminMBean(var5, var91, var96);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var99, class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
               break;
            } catch (IOException var60) {
               throw new MarshalException("error marshalling return", var60);
            }
         case 9:
            Set var86 = ((MBeanHome)var4).getAllMBeans();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var86, class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
               break;
            } catch (IOException var59) {
               throw new MarshalException("error marshalling return", var59);
            }
         case 10:
            try {
               MsgInput var11 = var2.getMsgInput();
               var5 = (String)var11.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var57) {
               throw new UnmarshalException("error unmarshalling arguments", var57);
            } catch (ClassNotFoundException var58) {
               throw new UnmarshalException("error unmarshalling arguments", var58);
            }

            Set var98 = ((MBeanHome)var4).getAllMBeans(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var98, class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
               break;
            } catch (IOException var56) {
               throw new MarshalException("error marshalling return", var56);
            }
         case 11:
            try {
               MsgInput var16 = var2.getMsgInput();
               var5 = (String)var16.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var96 = (String)var16.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var54) {
               throw new UnmarshalException("error unmarshalling arguments", var54);
            } catch (ClassNotFoundException var55) {
               throw new UnmarshalException("error unmarshalling arguments", var55);
            }

            ConfigurationMBean var101 = ((MBeanHome)var4).getConfigurationMBean(var5, var96);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var101, class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
               break;
            } catch (IOException var53) {
               throw new MarshalException("error marshalling return", var53);
            }
         case 12:
            var5 = ((MBeanHome)var4).getDomainName();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var5, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var52) {
               throw new MarshalException("error marshalling return", var52);
            }
         case 13:
            Class var97;
            try {
               MsgInput var17 = var2.getMsgInput();
               var5 = (String)var17.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var97 = (Class)var17.readObject(class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class);
            } catch (IOException var50) {
               throw new UnmarshalException("error unmarshalling arguments", var50);
            } catch (ClassNotFoundException var51) {
               throw new UnmarshalException("error unmarshalling arguments", var51);
            }

            WebLogicMBean var102 = ((MBeanHome)var4).getMBean(var5, var97);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var102, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var49) {
               throw new MarshalException("error marshalling return", var49);
            }
         case 14:
            try {
               MsgInput var18 = var2.getMsgInput();
               var5 = (String)var18.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var96 = (String)var18.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var47) {
               throw new UnmarshalException("error unmarshalling arguments", var47);
            } catch (ClassNotFoundException var48) {
               throw new UnmarshalException("error unmarshalling arguments", var48);
            }

            var103 = ((MBeanHome)var4).getMBean(var5, var96);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var103, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var46) {
               throw new MarshalException("error marshalling return", var46);
            }
         case 15:
            try {
               MsgInput var20 = var2.getMsgInput();
               var5 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var96 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var104 = (String)var20.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var44) {
               throw new UnmarshalException("error unmarshalling arguments", var44);
            } catch (ClassNotFoundException var45) {
               throw new UnmarshalException("error unmarshalling arguments", var45);
            }

            WebLogicMBean var107 = ((MBeanHome)var4).getMBean(var5, var96, var104);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var107, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var43) {
               throw new MarshalException("error marshalling return", var43);
            }
         case 16:
            String var106;
            try {
               MsgInput var22 = var2.getMsgInput();
               var5 = (String)var22.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var96 = (String)var22.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var104 = (String)var22.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var106 = (String)var22.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var41) {
               throw new UnmarshalException("error unmarshalling arguments", var41);
            } catch (ClassNotFoundException var42) {
               throw new UnmarshalException("error unmarshalling arguments", var42);
            }

            WebLogicMBean var109 = ((MBeanHome)var4).getMBean(var5, var96, var104, var106);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var109, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var40) {
               throw new MarshalException("error marshalling return", var40);
            }
         case 17:
            try {
               MsgInput var14 = var2.getMsgInput();
               var82 = (ObjectName)var14.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var38) {
               throw new UnmarshalException("error unmarshalling arguments", var38);
            } catch (ClassNotFoundException var39) {
               throw new UnmarshalException("error unmarshalling arguments", var39);
            }

            var103 = ((MBeanHome)var4).getMBean(var82);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var103, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
               break;
            } catch (IOException var37) {
               throw new MarshalException("error marshalling return", var37);
            }
         case 18:
            RemoteMBeanServer var83 = ((MBeanHome)var4).getMBeanServer();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var83, class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer);
               break;
            } catch (IOException var36) {
               throw new MarshalException("error marshalling return", var36);
            }
         case 19:
            try {
               MsgInput var19 = var2.getMsgInput();
               var5 = (String)var19.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var34) {
               throw new UnmarshalException("error unmarshalling arguments", var34);
            } catch (ClassNotFoundException var35) {
               throw new UnmarshalException("error unmarshalling arguments", var35);
            }

            Set var105 = ((MBeanHome)var4).getMBeansByType(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var105, class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
               break;
            } catch (IOException var33) {
               throw new MarshalException("error marshalling return", var33);
            }
         case 20:
            try {
               MsgInput var21 = var2.getMsgInput();
               var82 = (ObjectName)var21.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var31) {
               throw new UnmarshalException("error unmarshalling arguments", var31);
            } catch (ClassNotFoundException var32) {
               throw new UnmarshalException("error unmarshalling arguments", var32);
            }

            Object var108 = ((MBeanHome)var4).getProxy(var82);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var108, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var30) {
               throw new MarshalException("error marshalling return", var30);
            }
         case 21:
            String var23;
            try {
               MsgInput var24 = var2.getMsgInput();
               var5 = (String)var24.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var23 = (String)var24.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var28) {
               throw new UnmarshalException("error unmarshalling arguments", var28);
            } catch (ClassNotFoundException var29) {
               throw new UnmarshalException("error unmarshalling arguments", var29);
            }

            RuntimeMBean var25 = ((MBeanHome)var4).getRuntimeMBean(var5, var23);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var25, class$weblogic$management$runtime$RuntimeMBean == null ? (class$weblogic$management$runtime$RuntimeMBean = class$("weblogic.management.runtime.RuntimeMBean")) : class$weblogic$management$runtime$RuntimeMBean);
               break;
            } catch (IOException var27) {
               throw new MarshalException("error marshalling return", var27);
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
            ((MBeanHome)var3).addManagedHome((MBeanHome)var2[0], (String)var2[1], (String)var2[2]);
            return null;
         case 1:
            return ((MBeanHome)var3).createAdminMBean((String)var2[0], (String)var2[1]);
         case 2:
            return ((MBeanHome)var3).createAdminMBean((String)var2[0], (String)var2[1], (String)var2[2]);
         case 3:
            return ((MBeanHome)var3).createAdminMBean((String)var2[0], (String)var2[1], (String)var2[2], (ConfigurationMBean)var2[3]);
         case 4:
            ((MBeanHome)var3).deleteMBean((ObjectName)var2[0]);
            return null;
         case 5:
            ((MBeanHome)var3).deleteMBean((WebLogicMBean)var2[0]);
            return null;
         case 6:
            return ((MBeanHome)var3).getActiveDomain();
         case 7:
            return ((MBeanHome)var3).getAdminMBean((String)var2[0], (String)var2[1]);
         case 8:
            return ((MBeanHome)var3).getAdminMBean((String)var2[0], (String)var2[1], (String)var2[2]);
         case 9:
            return ((MBeanHome)var3).getAllMBeans();
         case 10:
            return ((MBeanHome)var3).getAllMBeans((String)var2[0]);
         case 11:
            return ((MBeanHome)var3).getConfigurationMBean((String)var2[0], (String)var2[1]);
         case 12:
            return ((MBeanHome)var3).getDomainName();
         case 13:
            return ((MBeanHome)var3).getMBean((String)var2[0], (Class)var2[1]);
         case 14:
            return ((MBeanHome)var3).getMBean((String)var2[0], (String)var2[1]);
         case 15:
            return ((MBeanHome)var3).getMBean((String)var2[0], (String)var2[1], (String)var2[2]);
         case 16:
            return ((MBeanHome)var3).getMBean((String)var2[0], (String)var2[1], (String)var2[2], (String)var2[3]);
         case 17:
            return ((MBeanHome)var3).getMBean((ObjectName)var2[0]);
         case 18:
            return ((MBeanHome)var3).getMBeanServer();
         case 19:
            return ((MBeanHome)var3).getMBeansByType((String)var2[0]);
         case 20:
            return ((MBeanHome)var3).getProxy((ObjectName)var2[0]);
         case 21:
            return ((MBeanHome)var3).getRuntimeMBean((String)var2[0], (String)var2[1]);
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
