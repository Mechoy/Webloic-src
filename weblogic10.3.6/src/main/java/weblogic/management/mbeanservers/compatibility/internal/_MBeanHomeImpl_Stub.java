package weblogic.management.mbeanservers.compatibility.internal;

import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.ObjectName;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA_2_3.portable.InputStream;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.runtime.RuntimeMBean;

public final class _MBeanHomeImpl_Stub extends Stub implements MBeanHome {
   // $FF: synthetic field
   private static Class class$javax$management$MBeanRegistrationException;
   // $FF: synthetic field
   private static Class class$java$util$Set;
   // $FF: synthetic field
   private static Class class$weblogic$management$configuration$ConfigurationMBean;
   // $FF: synthetic field
   private static Class class$javax$management$InstanceNotFoundException;
   // $FF: synthetic field
   private static Class class$weblogic$management$WebLogicMBean;
   // $FF: synthetic field
   private static Class class$weblogic$management$configuration$DomainMBean;
   // $FF: synthetic field
   private static Class class$java$lang$String;
   // $FF: synthetic field
   private static Class class$weblogic$management$MBeanCreationException;
   // $FF: synthetic field
   private static Class class$weblogic$management$runtime$RuntimeMBean;
   private static String[] _type_ids = new String[]{"RMI:weblogic.management.mbeanservers.compatibility.internal.MBeanHomeImpl:0000000000000000", "RMI:weblogic.management.MBeanHome:0000000000000000"};
   // $FF: synthetic field
   private static Class class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class class$weblogic$management$RemoteMBeanServer;
   // $FF: synthetic field
   private static Class class$java$lang$Class;

   public String[] _ids() {
      return _type_ids;
   }

   public final RemoteMBeanServer getMBeanServer() throws RemoteException {
      try {
         InputStream var1 = null;

         RemoteMBeanServer var5;
         try {
            OutputStream var2 = this._request("_get_MBeanServer", true);
            var1 = (InputStream)this._invoke(var2);
            RemoteMBeanServer var3 = (RemoteMBeanServer)PortableRemoteObject.narrow((RemoteMBeanServer)var1.read_Object(), class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getMBeanServer();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public final WebLogicMBean getMBean(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         WebLogicMBean var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBean__javax_management_ObjectName", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = (InputStream)this._invoke(var3);
            WebLogicMBean var4 = (WebLogicMBean)var2.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var4;
         } catch (ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getMBean(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final Object getProxy(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         Object var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getProxy", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = (InputStream)this._invoke(var3);
            Object var4 = (Object)Util.readAny(var2);
            return var4;
         } catch (ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getProxy(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final WebLogicMBean getMBean(String var1, String var2, String var3, String var4) throws RemoteException {
      try {
         InputStream var5 = null;

         WebLogicMBean var9;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBean__CORBA_WStringValue__CORBA_WStringValue__CORBA_WStringValue__CORBA_WStringValue", true);
            var6.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var4, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5 = (InputStream)this._invoke(var6);
            WebLogicMBean var7 = (WebLogicMBean)var5.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var7;
         } catch (ApplicationException var16) {
            var5 = (InputStream)var16.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var17) {
            var9 = this.getMBean(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

         return var9;
      } catch (SystemException var19) {
         throw Util.mapSystemException(var19);
      }
   }

   public final WebLogicMBean getMBean(String var1, String var2, String var3) throws RemoteException {
      try {
         InputStream var4 = null;

         WebLogicMBean var8;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBean__CORBA_WStringValue__CORBA_WStringValue__CORBA_WStringValue", true);
            var5.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4 = (InputStream)this._invoke(var5);
            WebLogicMBean var6 = (WebLogicMBean)var4.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var6;
         } catch (ApplicationException var15) {
            var4 = (InputStream)var15.getInputStream();
            String var7 = var4.read_string();
            if (var7.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var4.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var7);
         } catch (RemarshalException var16) {
            var8 = this.getMBean(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var8;
      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   public final WebLogicMBean getMBean(String var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         WebLogicMBean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBean__CORBA_WStringValue__CORBA_WStringValue", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = (InputStream)this._invoke(var4);
            WebLogicMBean var5 = (WebLogicMBean)var3.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var5;
         } catch (ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final WebLogicMBean getMBean(String var1, Class var2) throws RemoteException {
      try {
         InputStream var3 = null;

         WebLogicMBean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBean__CORBA_WStringValue__javax_rmi_CORBA_ClassDesc", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class);
            var3 = (InputStream)this._invoke(var4);
            WebLogicMBean var5 = (WebLogicMBean)var3.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var5;
         } catch (ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final Set getMBeansByType(String var1) throws RemoteException {
      try {
         InputStream var2 = null;

         Set var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBeansByType", true);
            var3.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var2 = (InputStream)this._invoke(var3);
            Set var4 = (Set)var2.read_value(class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
            return var4;
         } catch (ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getMBeansByType(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final Set getAllMBeans(String var1) throws RemoteException {
      try {
         InputStream var2 = null;

         Set var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getAllMBeans__CORBA_WStringValue", true);
            var3.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var2 = (InputStream)this._invoke(var3);
            Set var4 = (Set)var2.read_value(class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
            return var4;
         } catch (ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getAllMBeans(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final Set getAllMBeans() throws RemoteException {
      try {
         InputStream var1 = null;

         Set var5;
         try {
            OutputStream var2 = this._request("_get_allMBeans__", true);
            var1 = (InputStream)this._invoke(var2);
            Set var3 = (Set)var1.read_value(class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getAllMBeans();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final DomainMBean getActiveDomain() throws RemoteException {
      try {
         InputStream var1 = null;

         DomainMBean var5;
         try {
            OutputStream var2 = this._request("_get_activeDomain", true);
            var1 = (InputStream)this._invoke(var2);
            DomainMBean var3 = (DomainMBean)var1.read_value(class$weblogic$management$configuration$DomainMBean == null ? (class$weblogic$management$configuration$DomainMBean = class$("weblogic.management.configuration.DomainMBean")) : class$weblogic$management$configuration$DomainMBean);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getActiveDomain();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final ConfigurationMBean getAdminMBean(String var1, String var2, String var3) throws RemoteException {
      try {
         InputStream var4 = null;

         ConfigurationMBean var8;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getAdminMBean__CORBA_WStringValue__CORBA_WStringValue__CORBA_WStringValue", true);
            var5.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4 = (InputStream)this._invoke(var5);
            ConfigurationMBean var6 = (ConfigurationMBean)var4.read_value(class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
            return var6;
         } catch (ApplicationException var15) {
            var4 = (InputStream)var15.getInputStream();
            String var7 = var4.read_string();
            if (var7.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var4.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var7);
         } catch (RemarshalException var16) {
            var8 = this.getAdminMBean(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var8;
      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   public final ConfigurationMBean getAdminMBean(String var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         ConfigurationMBean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getAdminMBean__CORBA_WStringValue__CORBA_WStringValue", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = (InputStream)this._invoke(var4);
            ConfigurationMBean var5 = (ConfigurationMBean)var3.read_value(class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
            return var5;
         } catch (ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getAdminMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final ConfigurationMBean getConfigurationMBean(String var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         ConfigurationMBean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getConfigurationMBean", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = (InputStream)this._invoke(var4);
            ConfigurationMBean var5 = (ConfigurationMBean)var3.read_value(class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
            return var5;
         } catch (ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getConfigurationMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final RuntimeMBean getRuntimeMBean(String var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         RuntimeMBean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getRuntimeMBean", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = (InputStream)this._invoke(var4);
            RuntimeMBean var5 = (RuntimeMBean)var3.read_value(class$weblogic$management$runtime$RuntimeMBean == null ? (class$weblogic$management$runtime$RuntimeMBean = class$("weblogic.management.runtime.RuntimeMBean")) : class$weblogic$management$runtime$RuntimeMBean);
            return var5;
         } catch (ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getRuntimeMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final WebLogicMBean createAdminMBean(String var1, String var2, String var3, ConfigurationMBean var4) throws RemoteException {
      try {
         InputStream var5 = null;

         WebLogicMBean var9;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createAdminMBean__CORBA_WStringValue__CORBA_WStringValue__CORBA_WStringValue__weblogic_management_configuration_ConfigurationMBean", true);
            var6.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var4, class$weblogic$management$configuration$ConfigurationMBean == null ? (class$weblogic$management$configuration$ConfigurationMBean = class$("weblogic.management.configuration.ConfigurationMBean")) : class$weblogic$management$configuration$ConfigurationMBean);
            var5 = (InputStream)this._invoke(var6);
            WebLogicMBean var7 = (WebLogicMBean)var5.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var7;
         } catch (ApplicationException var16) {
            var5 = (InputStream)var16.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:weblogic/management/MBeanCreationEx:1.0")) {
               throw (MBeanCreationException)((MBeanCreationException)var5.read_value(class$weblogic$management$MBeanCreationException == null ? (class$weblogic$management$MBeanCreationException = class$("weblogic.management.MBeanCreationException")) : class$weblogic$management$MBeanCreationException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var17) {
            var9 = this.createAdminMBean(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

         return var9;
      } catch (SystemException var19) {
         throw Util.mapSystemException(var19);
      }
   }

   public final WebLogicMBean createAdminMBean(String var1, String var2, String var3) throws RemoteException {
      try {
         InputStream var4 = null;

         WebLogicMBean var8;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createAdminMBean__CORBA_WStringValue__CORBA_WStringValue__CORBA_WStringValue", true);
            var5.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4 = (InputStream)this._invoke(var5);
            WebLogicMBean var6 = (WebLogicMBean)var4.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var6;
         } catch (ApplicationException var15) {
            var4 = (InputStream)var15.getInputStream();
            String var7 = var4.read_string();
            if (var7.equals("IDL:weblogic/management/MBeanCreationEx:1.0")) {
               throw (MBeanCreationException)((MBeanCreationException)var4.read_value(class$weblogic$management$MBeanCreationException == null ? (class$weblogic$management$MBeanCreationException = class$("weblogic.management.MBeanCreationException")) : class$weblogic$management$MBeanCreationException));
            }

            throw new UnexpectedException(var7);
         } catch (RemarshalException var16) {
            var8 = this.createAdminMBean(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var8;
      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   public final WebLogicMBean createAdminMBean(String var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         WebLogicMBean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createAdminMBean__CORBA_WStringValue__CORBA_WStringValue", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = (InputStream)this._invoke(var4);
            WebLogicMBean var5 = (WebLogicMBean)var3.read_value(class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            return var5;
         } catch (ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:weblogic/management/MBeanCreationEx:1.0")) {
               throw (MBeanCreationException)((MBeanCreationException)var3.read_value(class$weblogic$management$MBeanCreationException == null ? (class$weblogic$management$MBeanCreationException = class$("weblogic.management.MBeanCreationException")) : class$weblogic$management$MBeanCreationException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.createAdminMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final void addManagedHome(MBeanHome var1, String var2, String var3) throws RemoteException {
      try {
         InputStream var4 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("addManagedHome", true);
            Util.writeRemoteObject(var5, var1);
            var5.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var3, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            this._invoke(var5);
         } catch (ApplicationException var13) {
            var4 = (InputStream)var13.getInputStream();
            String var7 = var4.read_string();
            throw new UnexpectedException(var7);
         } catch (RemarshalException var14) {
            this.addManagedHome(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final String getDomainName() throws RemoteException {
      try {
         InputStream var1 = null;

         String var5;
         try {
            OutputStream var2 = this._request("_get_domainName", true);
            var1 = (InputStream)this._invoke(var2);
            String var3 = (String)var1.read_value(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getDomainName();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void deleteMBean(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("deleteMBean__javax_management_ObjectName", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            this._invoke(var3);
         } catch (ApplicationException var11) {
            var2 = (InputStream)var11.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var5.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var2.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var12) {
            this.deleteMBean(var1);
         } finally {
            this._releaseReply(var2);
         }

      } catch (SystemException var14) {
         throw Util.mapSystemException(var14);
      }
   }

   public final void deleteMBean(WebLogicMBean var1) throws RemoteException {
      try {
         InputStream var2 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("deleteMBean__weblogic_management_WebLogicMBean", true);
            var3.write_value(var1, class$weblogic$management$WebLogicMBean == null ? (class$weblogic$management$WebLogicMBean = class$("weblogic.management.WebLogicMBean")) : class$weblogic$management$WebLogicMBean);
            this._invoke(var3);
         } catch (ApplicationException var11) {
            var2 = (InputStream)var11.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var5.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var2.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var12) {
            this.deleteMBean(var1);
         } finally {
            this._releaseReply(var2);
         }

      } catch (SystemException var14) {
         throw Util.mapSystemException(var14);
      }
   }
}
