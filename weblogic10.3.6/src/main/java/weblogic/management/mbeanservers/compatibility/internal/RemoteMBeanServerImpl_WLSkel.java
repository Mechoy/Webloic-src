package weblogic.management.mbeanservers.compatibility.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.loading.ClassLoaderRepository;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class RemoteMBeanServerImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$java$util$Set;
   // $FF: synthetic field
   private static Class class$java$io$ObjectInputStream;
   // $FF: synthetic field
   private static Class class$java$lang$ClassLoader;
   // $FF: synthetic field
   private static Class class$javax$management$AttributeList;
   // $FF: synthetic field
   private static Class class$javax$management$MBeanInfo;
   // $FF: synthetic field
   private static Class array$Ljava$lang$Object;
   // $FF: synthetic field
   private static Class class$weblogic$management$MBeanHome;
   // $FF: synthetic field
   private static Class class$javax$management$Attribute;
   // $FF: synthetic field
   private static Class class$java$lang$String;
   // $FF: synthetic field
   private static Class array$B;
   // $FF: synthetic field
   private static Class class$javax$management$loading$ClassLoaderRepository;
   // $FF: synthetic field
   private static Class class$javax$management$ObjectInstance;
   // $FF: synthetic field
   private static Class class$javax$management$QueryExp;
   // $FF: synthetic field
   private static Class class$javax$management$NotificationFilter;
   // $FF: synthetic field
   private static Class class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class array$Ljava$lang$String;
   // $FF: synthetic field
   private static Class class$java$lang$Integer;
   // $FF: synthetic field
   private static Class class$javax$management$NotificationListener;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      ObjectName var5;
      byte[] var136;
      NotificationFilter var137;
      ObjectName var139;
      ObjectName var142;
      String var143;
      Object var146;
      ObjectInputStream var155;
      ObjectInstance var156;
      String var164;
      ObjectName var166;
      Object[] var168;
      ObjectName var174;
      NotificationListener var175;
      QueryExp var177;
      boolean var178;
      Object var181;
      switch (var1) {
         case 0:
            NotificationListener var144;
            try {
               MsgInput var9 = var2.getMsgInput();
               var5 = (ObjectName)var9.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var144 = (NotificationListener)var9.readObject(class$javax$management$NotificationListener == null ? (class$javax$management$NotificationListener = class$("javax.management.NotificationListener")) : class$javax$management$NotificationListener);
               var137 = (NotificationFilter)var9.readObject(class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
               var146 = (Object)var9.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var132) {
               throw new UnmarshalException("error unmarshalling arguments", var132);
            } catch (ClassNotFoundException var133) {
               throw new UnmarshalException("error unmarshalling arguments", var133);
            }

            ((MBeanServer)var4).addNotificationListener(var5, var144, var137, var146);
            this.associateResponseData(var2, var3);
            break;
         case 1:
            try {
               MsgInput var10 = var2.getMsgInput();
               var5 = (ObjectName)var10.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var139 = (ObjectName)var10.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var137 = (NotificationFilter)var10.readObject(class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
               var146 = (Object)var10.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var130) {
               throw new UnmarshalException("error unmarshalling arguments", var130);
            } catch (ClassNotFoundException var131) {
               throw new UnmarshalException("error unmarshalling arguments", var131);
            }

            ((MBeanServer)var4).addNotificationListener(var5, var139, var137, var146);
            this.associateResponseData(var2, var3);
            break;
         case 2:
            try {
               MsgInput var7 = var2.getMsgInput();
               var143 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var139 = (ObjectName)var7.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var128) {
               throw new UnmarshalException("error unmarshalling arguments", var128);
            } catch (ClassNotFoundException var129) {
               throw new UnmarshalException("error unmarshalling arguments", var129);
            }

            ObjectInstance var145 = ((MBeanServer)var4).createMBean(var143, var139);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var145, class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
               break;
            } catch (IOException var127) {
               throw new MarshalException("error marshalling return", var127);
            }
         case 3:
            try {
               MsgInput var11 = var2.getMsgInput();
               var143 = (String)var11.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var139 = (ObjectName)var11.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var142 = (ObjectName)var11.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var125) {
               throw new UnmarshalException("error unmarshalling arguments", var125);
            } catch (ClassNotFoundException var126) {
               throw new UnmarshalException("error unmarshalling arguments", var126);
            }

            ObjectInstance var153 = ((MBeanServer)var4).createMBean(var143, var139, var142);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var153, class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
               break;
            } catch (IOException var124) {
               throw new MarshalException("error marshalling return", var124);
            }
         case 4:
            Object[] var151;
            String[] var154;
            try {
               MsgInput var14 = var2.getMsgInput();
               var143 = (String)var14.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var139 = (ObjectName)var14.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var142 = (ObjectName)var14.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var151 = (Object[])var14.readObject(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
               var154 = (String[])var14.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var122) {
               throw new UnmarshalException("error unmarshalling arguments", var122);
            } catch (ClassNotFoundException var123) {
               throw new UnmarshalException("error unmarshalling arguments", var123);
            }

            var156 = ((MBeanServer)var4).createMBean(var143, var139, var142, var151, var154);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var156, class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
               break;
            } catch (IOException var121) {
               throw new MarshalException("error marshalling return", var121);
            }
         case 5:
            Object[] var141;
            String[] var150;
            try {
               MsgInput var13 = var2.getMsgInput();
               var143 = (String)var13.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var139 = (ObjectName)var13.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var141 = (Object[])var13.readObject(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
               var150 = (String[])var13.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var119) {
               throw new UnmarshalException("error unmarshalling arguments", var119);
            } catch (ClassNotFoundException var120) {
               throw new UnmarshalException("error unmarshalling arguments", var120);
            }

            var156 = ((MBeanServer)var4).createMBean(var143, var139, var141, var150);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var156, class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
               break;
            } catch (IOException var118) {
               throw new MarshalException("error marshalling return", var118);
            }
         case 6:
            byte[] var140;
            try {
               MsgInput var12 = var2.getMsgInput();
               var143 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var139 = (ObjectName)var12.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var140 = (byte[])var12.readObject(array$B == null ? (array$B = class$("[B")) : array$B);
            } catch (IOException var116) {
               throw new UnmarshalException("error unmarshalling arguments", var116);
            } catch (ClassNotFoundException var117) {
               throw new UnmarshalException("error unmarshalling arguments", var117);
            }

            var155 = ((MBeanServer)var4).deserialize(var143, var139, var140);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var155, class$java$io$ObjectInputStream == null ? (class$java$io$ObjectInputStream = class$("java.io.ObjectInputStream")) : class$java$io$ObjectInputStream);
               break;
            } catch (IOException var115) {
               throw new MarshalException("error marshalling return", var115);
            }
         case 7:
            try {
               MsgInput var8 = var2.getMsgInput();
               var143 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var136 = (byte[])var8.readObject(array$B == null ? (array$B = class$("[B")) : array$B);
            } catch (IOException var113) {
               throw new UnmarshalException("error unmarshalling arguments", var113);
            } catch (ClassNotFoundException var114) {
               throw new UnmarshalException("error unmarshalling arguments", var114);
            }

            var155 = ((MBeanServer)var4).deserialize(var143, var136);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var155, class$java$io$ObjectInputStream == null ? (class$java$io$ObjectInputStream = class$("java.io.ObjectInputStream")) : class$java$io$ObjectInputStream);
               break;
            } catch (IOException var112) {
               throw new MarshalException("error marshalling return", var112);
            }
         case 8:
            try {
               MsgInput var15 = var2.getMsgInput();
               var5 = (ObjectName)var15.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var136 = (byte[])var15.readObject(array$B == null ? (array$B = class$("[B")) : array$B);
            } catch (IOException var110) {
               throw new UnmarshalException("error unmarshalling arguments", var110);
            } catch (ClassNotFoundException var111) {
               throw new UnmarshalException("error unmarshalling arguments", var111);
            }

            ObjectInputStream var157 = ((MBeanServer)var4).deserialize(var5, var136);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var157, class$java$io$ObjectInputStream == null ? (class$java$io$ObjectInputStream = class$("java.io.ObjectInputStream")) : class$java$io$ObjectInputStream);
               break;
            } catch (IOException var109) {
               throw new MarshalException("error marshalling return", var109);
            }
         case 9:
            String var135;
            try {
               MsgInput var16 = var2.getMsgInput();
               var5 = (ObjectName)var16.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var135 = (String)var16.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var107) {
               throw new UnmarshalException("error unmarshalling arguments", var107);
            } catch (ClassNotFoundException var108) {
               throw new UnmarshalException("error unmarshalling arguments", var108);
            }

            Object var158 = ((MBeanServer)var4).getAttribute(var5, var135);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var158, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var106) {
               throw new MarshalException("error marshalling return", var106);
            }
         case 10:
            String[] var134;
            try {
               MsgInput var17 = var2.getMsgInput();
               var5 = (ObjectName)var17.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var134 = (String[])var17.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var104) {
               throw new UnmarshalException("error unmarshalling arguments", var104);
            } catch (ClassNotFoundException var105) {
               throw new UnmarshalException("error unmarshalling arguments", var105);
            }

            AttributeList var160 = ((MBeanServer)var4).getAttributes(var5, var134);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var160, class$javax$management$AttributeList == null ? (class$javax$management$AttributeList = class$("javax.management.AttributeList")) : class$javax$management$AttributeList);
               break;
            } catch (IOException var103) {
               throw new MarshalException("error marshalling return", var103);
            }
         case 11:
            try {
               MsgInput var6 = var2.getMsgInput();
               var5 = (ObjectName)var6.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var101) {
               throw new UnmarshalException("error unmarshalling arguments", var101);
            } catch (ClassNotFoundException var102) {
               throw new UnmarshalException("error unmarshalling arguments", var102);
            }

            ClassLoader var159 = ((MBeanServer)var4).getClassLoader(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var159, class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader);
               break;
            } catch (IOException var100) {
               throw new MarshalException("error marshalling return", var100);
            }
         case 12:
            try {
               MsgInput var18 = var2.getMsgInput();
               var5 = (ObjectName)var18.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var98) {
               throw new UnmarshalException("error unmarshalling arguments", var98);
            } catch (ClassNotFoundException var99) {
               throw new UnmarshalException("error unmarshalling arguments", var99);
            }

            ClassLoader var161 = ((MBeanServer)var4).getClassLoaderFor(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var161, class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader);
               break;
            } catch (IOException var97) {
               throw new MarshalException("error marshalling return", var97);
            }
         case 13:
            ClassLoaderRepository var152 = ((MBeanServer)var4).getClassLoaderRepository();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var152, class$javax$management$loading$ClassLoaderRepository == null ? (class$javax$management$loading$ClassLoaderRepository = class$("javax.management.loading.ClassLoaderRepository")) : class$javax$management$loading$ClassLoaderRepository);
               break;
            } catch (IOException var96) {
               throw new MarshalException("error marshalling return", var96);
            }
         case 14:
            var143 = ((MBeanServer)var4).getDefaultDomain();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var143, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var95) {
               throw new MarshalException("error marshalling return", var95);
            }
         case 15:
            String[] var149 = ((MBeanServer)var4).getDomains();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var149, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
               break;
            } catch (IOException var94) {
               throw new MarshalException("error marshalling return", var94);
            }
         case 16:
            Integer var148 = ((MBeanServer)var4).getMBeanCount();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var148, class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer);
               break;
            } catch (IOException var93) {
               throw new MarshalException("error marshalling return", var93);
            }
         case 17:
            MBeanHome var147 = ((RemoteMBeanServer)var4).getMBeanHome();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var147, class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome);
               break;
            } catch (IOException var92) {
               throw new MarshalException("error marshalling return", var92);
            }
         case 18:
            try {
               MsgInput var19 = var2.getMsgInput();
               var5 = (ObjectName)var19.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var90) {
               throw new UnmarshalException("error unmarshalling arguments", var90);
            } catch (ClassNotFoundException var91) {
               throw new UnmarshalException("error unmarshalling arguments", var91);
            }

            MBeanInfo var162 = ((MBeanServer)var4).getMBeanInfo(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var162, class$javax$management$MBeanInfo == null ? (class$javax$management$MBeanInfo = class$("javax.management.MBeanInfo")) : class$javax$management$MBeanInfo);
               break;
            } catch (IOException var89) {
               throw new MarshalException("error marshalling return", var89);
            }
         case 19:
            try {
               MsgInput var20 = var2.getMsgInput();
               var5 = (ObjectName)var20.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var87) {
               throw new UnmarshalException("error unmarshalling arguments", var87);
            } catch (ClassNotFoundException var88) {
               throw new UnmarshalException("error unmarshalling arguments", var88);
            }

            ObjectInstance var163 = ((MBeanServer)var4).getObjectInstance(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var163, class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
               break;
            } catch (IOException var86) {
               throw new MarshalException("error marshalling return", var86);
            }
         case 20:
            var143 = ((RemoteMBeanServer)var4).getServerName();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var143, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var85) {
               throw new MarshalException("error marshalling return", var85);
            }
         case 21:
            try {
               MsgInput var21 = var2.getMsgInput();
               var143 = (String)var21.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var83) {
               throw new UnmarshalException("error unmarshalling arguments", var83);
            } catch (ClassNotFoundException var84) {
               throw new UnmarshalException("error unmarshalling arguments", var84);
            }

            Object var167 = ((MBeanServer)var4).instantiate(var143);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var167, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var82) {
               throw new MarshalException("error marshalling return", var82);
            }
         case 22:
            try {
               MsgInput var23 = var2.getMsgInput();
               var143 = (String)var23.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var166 = (ObjectName)var23.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var80) {
               throw new UnmarshalException("error unmarshalling arguments", var80);
            } catch (ClassNotFoundException var81) {
               throw new UnmarshalException("error unmarshalling arguments", var81);
            }

            Object var171 = ((MBeanServer)var4).instantiate(var143, var166);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var171, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var79) {
               throw new MarshalException("error marshalling return", var79);
            }
         case 23:
            String[] var170;
            try {
               MsgInput var26 = var2.getMsgInput();
               var143 = (String)var26.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var166 = (ObjectName)var26.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var168 = (Object[])var26.readObject(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
               var170 = (String[])var26.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var77) {
               throw new UnmarshalException("error unmarshalling arguments", var77);
            } catch (ClassNotFoundException var78) {
               throw new UnmarshalException("error unmarshalling arguments", var78);
            }

            var181 = ((MBeanServer)var4).instantiate(var143, var166, var168, var170);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var181, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var76) {
               throw new MarshalException("error marshalling return", var76);
            }
         case 24:
            Object[] var165;
            String[] var169;
            try {
               MsgInput var25 = var2.getMsgInput();
               var143 = (String)var25.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var165 = (Object[])var25.readObject(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
               var169 = (String[])var25.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var74) {
               throw new UnmarshalException("error unmarshalling arguments", var74);
            } catch (ClassNotFoundException var75) {
               throw new UnmarshalException("error unmarshalling arguments", var75);
            }

            var181 = ((MBeanServer)var4).instantiate(var143, var165, var169);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var181, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var73) {
               throw new MarshalException("error marshalling return", var73);
            }
         case 25:
            String[] var180;
            try {
               MsgInput var28 = var2.getMsgInput();
               var5 = (ObjectName)var28.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var164 = (String)var28.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var168 = (Object[])var28.readObject(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
               var180 = (String[])var28.readObject(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            } catch (IOException var71) {
               throw new UnmarshalException("error unmarshalling arguments", var71);
            } catch (ClassNotFoundException var72) {
               throw new UnmarshalException("error unmarshalling arguments", var72);
            }

            Object var176 = ((MBeanServer)var4).invoke(var5, var164, var168, var180);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var176, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var70) {
               throw new MarshalException("error marshalling return", var70);
            }
         case 26:
            try {
               MsgInput var24 = var2.getMsgInput();
               var5 = (ObjectName)var24.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var164 = (String)var24.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var68) {
               throw new UnmarshalException("error unmarshalling arguments", var68);
            } catch (ClassNotFoundException var69) {
               throw new UnmarshalException("error unmarshalling arguments", var69);
            }

            var178 = ((MBeanServer)var4).isInstanceOf(var5, var164);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var178);
               break;
            } catch (IOException var67) {
               throw new MarshalException("error marshalling return", var67);
            }
         case 27:
            try {
               MsgInput var22 = var2.getMsgInput();
               var5 = (ObjectName)var22.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var65) {
               throw new UnmarshalException("error unmarshalling arguments", var65);
            } catch (ClassNotFoundException var66) {
               throw new UnmarshalException("error unmarshalling arguments", var66);
            }

            var178 = ((MBeanServer)var4).isRegistered(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeBoolean(var178);
               break;
            } catch (IOException var64) {
               throw new MarshalException("error marshalling return", var64);
            }
         case 28:
            try {
               MsgInput var29 = var2.getMsgInput();
               var5 = (ObjectName)var29.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var177 = (QueryExp)var29.readObject(class$javax$management$QueryExp == null ? (class$javax$management$QueryExp = class$("javax.management.QueryExp")) : class$javax$management$QueryExp);
            } catch (IOException var62) {
               throw new UnmarshalException("error unmarshalling arguments", var62);
            } catch (ClassNotFoundException var63) {
               throw new UnmarshalException("error unmarshalling arguments", var63);
            }

            Set var179 = ((MBeanServer)var4).queryMBeans(var5, var177);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var179, class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
               break;
            } catch (IOException var61) {
               throw new MarshalException("error marshalling return", var61);
            }
         case 29:
            try {
               MsgInput var30 = var2.getMsgInput();
               var5 = (ObjectName)var30.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var177 = (QueryExp)var30.readObject(class$javax$management$QueryExp == null ? (class$javax$management$QueryExp = class$("javax.management.QueryExp")) : class$javax$management$QueryExp);
            } catch (IOException var59) {
               throw new UnmarshalException("error unmarshalling arguments", var59);
            } catch (ClassNotFoundException var60) {
               throw new UnmarshalException("error unmarshalling arguments", var60);
            }

            Set var182 = ((MBeanServer)var4).queryNames(var5, var177);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var182, class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
               break;
            } catch (IOException var58) {
               throw new MarshalException("error marshalling return", var58);
            }
         case 30:
            Object var138;
            try {
               MsgInput var31 = var2.getMsgInput();
               var138 = (Object)var31.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               var174 = (ObjectName)var31.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var56) {
               throw new UnmarshalException("error unmarshalling arguments", var56);
            } catch (ClassNotFoundException var57) {
               throw new UnmarshalException("error unmarshalling arguments", var57);
            }

            ObjectInstance var183 = ((MBeanServer)var4).registerMBean(var138, var174);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var183, class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
               break;
            } catch (IOException var55) {
               throw new MarshalException("error marshalling return", var55);
            }
         case 31:
            try {
               MsgInput var32 = var2.getMsgInput();
               var5 = (ObjectName)var32.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var175 = (NotificationListener)var32.readObject(class$javax$management$NotificationListener == null ? (class$javax$management$NotificationListener = class$("javax.management.NotificationListener")) : class$javax$management$NotificationListener);
            } catch (IOException var53) {
               throw new UnmarshalException("error unmarshalling arguments", var53);
            } catch (ClassNotFoundException var54) {
               throw new UnmarshalException("error unmarshalling arguments", var54);
            }

            ((MBeanServer)var4).removeNotificationListener(var5, var175);
            this.associateResponseData(var2, var3);
            break;
         case 32:
            NotificationFilter var184;
            Object var186;
            try {
               MsgInput var35 = var2.getMsgInput();
               var5 = (ObjectName)var35.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var175 = (NotificationListener)var35.readObject(class$javax$management$NotificationListener == null ? (class$javax$management$NotificationListener = class$("javax.management.NotificationListener")) : class$javax$management$NotificationListener);
               var184 = (NotificationFilter)var35.readObject(class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
               var186 = (Object)var35.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var51) {
               throw new UnmarshalException("error unmarshalling arguments", var51);
            } catch (ClassNotFoundException var52) {
               throw new UnmarshalException("error unmarshalling arguments", var52);
            }

            ((MBeanServer)var4).removeNotificationListener(var5, var175, var184, var186);
            this.associateResponseData(var2, var3);
            break;
         case 33:
            try {
               MsgInput var33 = var2.getMsgInput();
               var5 = (ObjectName)var33.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var174 = (ObjectName)var33.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var49) {
               throw new UnmarshalException("error unmarshalling arguments", var49);
            } catch (ClassNotFoundException var50) {
               throw new UnmarshalException("error unmarshalling arguments", var50);
            }

            ((MBeanServer)var4).removeNotificationListener(var5, var174);
            this.associateResponseData(var2, var3);
            break;
         case 34:
            NotificationFilter var185;
            Object var187;
            try {
               MsgInput var37 = var2.getMsgInput();
               var5 = (ObjectName)var37.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var174 = (ObjectName)var37.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var185 = (NotificationFilter)var37.readObject(class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
               var187 = (Object)var37.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var47) {
               throw new UnmarshalException("error unmarshalling arguments", var47);
            } catch (ClassNotFoundException var48) {
               throw new UnmarshalException("error unmarshalling arguments", var48);
            }

            ((MBeanServer)var4).removeNotificationListener(var5, var174, var185, var187);
            this.associateResponseData(var2, var3);
            break;
         case 35:
            Attribute var173;
            try {
               MsgInput var34 = var2.getMsgInput();
               var5 = (ObjectName)var34.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var173 = (Attribute)var34.readObject(class$javax$management$Attribute == null ? (class$javax$management$Attribute = class$("javax.management.Attribute")) : class$javax$management$Attribute);
            } catch (IOException var45) {
               throw new UnmarshalException("error unmarshalling arguments", var45);
            } catch (ClassNotFoundException var46) {
               throw new UnmarshalException("error unmarshalling arguments", var46);
            }

            ((MBeanServer)var4).setAttribute(var5, var173);
            this.associateResponseData(var2, var3);
            break;
         case 36:
            AttributeList var172;
            try {
               MsgInput var36 = var2.getMsgInput();
               var5 = (ObjectName)var36.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
               var172 = (AttributeList)var36.readObject(class$javax$management$AttributeList == null ? (class$javax$management$AttributeList = class$("javax.management.AttributeList")) : class$javax$management$AttributeList);
            } catch (IOException var43) {
               throw new UnmarshalException("error unmarshalling arguments", var43);
            } catch (ClassNotFoundException var44) {
               throw new UnmarshalException("error unmarshalling arguments", var44);
            }

            AttributeList var38 = ((MBeanServer)var4).setAttributes(var5, var172);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var38, class$javax$management$AttributeList == null ? (class$javax$management$AttributeList = class$("javax.management.AttributeList")) : class$javax$management$AttributeList);
               break;
            } catch (IOException var42) {
               throw new MarshalException("error marshalling return", var42);
            }
         case 37:
            try {
               MsgInput var27 = var2.getMsgInput();
               var5 = (ObjectName)var27.readObject(class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            } catch (IOException var40) {
               throw new UnmarshalException("error unmarshalling arguments", var40);
            } catch (ClassNotFoundException var41) {
               throw new UnmarshalException("error unmarshalling arguments", var41);
            }

            ((MBeanServer)var4).unregisterMBean(var5);
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
            ((MBeanServer)var3).addNotificationListener((ObjectName)var2[0], (NotificationListener)var2[1], (NotificationFilter)var2[2], (Object)var2[3]);
            return null;
         case 1:
            ((MBeanServer)var3).addNotificationListener((ObjectName)var2[0], (ObjectName)var2[1], (NotificationFilter)var2[2], (Object)var2[3]);
            return null;
         case 2:
            return ((MBeanServer)var3).createMBean((String)var2[0], (ObjectName)var2[1]);
         case 3:
            return ((MBeanServer)var3).createMBean((String)var2[0], (ObjectName)var2[1], (ObjectName)var2[2]);
         case 4:
            return ((MBeanServer)var3).createMBean((String)var2[0], (ObjectName)var2[1], (ObjectName)var2[2], (Object[])var2[3], (String[])var2[4]);
         case 5:
            return ((MBeanServer)var3).createMBean((String)var2[0], (ObjectName)var2[1], (Object[])var2[2], (String[])var2[3]);
         case 6:
            return ((MBeanServer)var3).deserialize((String)var2[0], (ObjectName)var2[1], (byte[])var2[2]);
         case 7:
            return ((MBeanServer)var3).deserialize((String)var2[0], (byte[])var2[1]);
         case 8:
            return ((MBeanServer)var3).deserialize((ObjectName)var2[0], (byte[])var2[1]);
         case 9:
            return ((MBeanServer)var3).getAttribute((ObjectName)var2[0], (String)var2[1]);
         case 10:
            return ((MBeanServer)var3).getAttributes((ObjectName)var2[0], (String[])var2[1]);
         case 11:
            return ((MBeanServer)var3).getClassLoader((ObjectName)var2[0]);
         case 12:
            return ((MBeanServer)var3).getClassLoaderFor((ObjectName)var2[0]);
         case 13:
            return ((MBeanServer)var3).getClassLoaderRepository();
         case 14:
            return ((MBeanServer)var3).getDefaultDomain();
         case 15:
            return ((MBeanServer)var3).getDomains();
         case 16:
            return ((MBeanServer)var3).getMBeanCount();
         case 17:
            return ((RemoteMBeanServer)var3).getMBeanHome();
         case 18:
            return ((MBeanServer)var3).getMBeanInfo((ObjectName)var2[0]);
         case 19:
            return ((MBeanServer)var3).getObjectInstance((ObjectName)var2[0]);
         case 20:
            return ((RemoteMBeanServer)var3).getServerName();
         case 21:
            return ((MBeanServer)var3).instantiate((String)var2[0]);
         case 22:
            return ((MBeanServer)var3).instantiate((String)var2[0], (ObjectName)var2[1]);
         case 23:
            return ((MBeanServer)var3).instantiate((String)var2[0], (ObjectName)var2[1], (Object[])var2[2], (String[])var2[3]);
         case 24:
            return ((MBeanServer)var3).instantiate((String)var2[0], (Object[])var2[1], (String[])var2[2]);
         case 25:
            return ((MBeanServer)var3).invoke((ObjectName)var2[0], (String)var2[1], (Object[])var2[2], (String[])var2[3]);
         case 26:
            return new Boolean(((MBeanServer)var3).isInstanceOf((ObjectName)var2[0], (String)var2[1]));
         case 27:
            return new Boolean(((MBeanServer)var3).isRegistered((ObjectName)var2[0]));
         case 28:
            return ((MBeanServer)var3).queryMBeans((ObjectName)var2[0], (QueryExp)var2[1]);
         case 29:
            return ((MBeanServer)var3).queryNames((ObjectName)var2[0], (QueryExp)var2[1]);
         case 30:
            return ((MBeanServer)var3).registerMBean((Object)var2[0], (ObjectName)var2[1]);
         case 31:
            ((MBeanServer)var3).removeNotificationListener((ObjectName)var2[0], (NotificationListener)var2[1]);
            return null;
         case 32:
            ((MBeanServer)var3).removeNotificationListener((ObjectName)var2[0], (NotificationListener)var2[1], (NotificationFilter)var2[2], (Object)var2[3]);
            return null;
         case 33:
            ((MBeanServer)var3).removeNotificationListener((ObjectName)var2[0], (ObjectName)var2[1]);
            return null;
         case 34:
            ((MBeanServer)var3).removeNotificationListener((ObjectName)var2[0], (ObjectName)var2[1], (NotificationFilter)var2[2], (Object)var2[3]);
            return null;
         case 35:
            ((MBeanServer)var3).setAttribute((ObjectName)var2[0], (Attribute)var2[1]);
            return null;
         case 36:
            return ((MBeanServer)var3).setAttributes((ObjectName)var2[0], (AttributeList)var2[1]);
         case 37:
            ((MBeanServer)var3).unregisterMBean((ObjectName)var2[0]);
            return null;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
