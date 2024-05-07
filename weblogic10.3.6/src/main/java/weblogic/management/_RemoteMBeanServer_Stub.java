package weblogic.management;

import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA_2_3.portable.InputStream;

public final class _RemoteMBeanServer_Stub extends Stub implements RemoteMBeanServer {
   // $FF: synthetic field
   private static Class class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class class$javax$management$InstanceNotFoundException;
   // $FF: synthetic field
   private static Class class$javax$management$InstanceAlreadyExistsException;
   // $FF: synthetic field
   private static Class class$javax$management$ObjectInstance;
   // $FF: synthetic field
   private static Class class$javax$management$ListenerNotFoundException;
   // $FF: synthetic field
   private static Class class$java$io$ObjectInputStream;
   // $FF: synthetic field
   private static Class class$javax$management$NotCompliantMBeanException;
   // $FF: synthetic field
   private static Class class$javax$management$MBeanRegistrationException;
   // $FF: synthetic field
   private static Class class$javax$management$OperationsException;
   // $FF: synthetic field
   private static Class class$javax$management$NotificationFilter;
   // $FF: synthetic field
   private static Class class$javax$management$NotificationListener;
   // $FF: synthetic field
   private static Class array$B;
   // $FF: synthetic field
   private static Class class$javax$management$loading$ClassLoaderRepository;
   private static String[] _type_ids = new String[]{"RMI:weblogic.management.RemoteMBeanServer:0000000000000000"};
   // $FF: synthetic field
   private static Class class$javax$management$InvalidAttributeValueException;
   // $FF: synthetic field
   private static Class class$javax$management$MBeanInfo;
   // $FF: synthetic field
   private static Class class$java$util$Set;
   // $FF: synthetic field
   private static Class array$Ljava$lang$String;
   // $FF: synthetic field
   private static Class class$javax$management$AttributeNotFoundException;
   // $FF: synthetic field
   private static Class class$javax$management$Attribute;
   // $FF: synthetic field
   private static Class class$java$lang$String;
   // $FF: synthetic field
   private static Class class$weblogic$management$MBeanHome;
   // $FF: synthetic field
   private static Class array$Ljava$lang$Object;
   // $FF: synthetic field
   private static Class class$java$lang$ClassLoader;
   // $FF: synthetic field
   private static Class class$javax$management$MBeanException;
   // $FF: synthetic field
   private static Class class$javax$management$IntrospectionException;
   // $FF: synthetic field
   private static Class class$java$lang$Integer;
   // $FF: synthetic field
   private static Class class$javax$management$AttributeList;
   // $FF: synthetic field
   private static Class class$javax$management$QueryExp;
   // $FF: synthetic field
   private static Class class$javax$management$ReflectionException;

   public String[] _ids() {
      return _type_ids;
   }

   public final MBeanHome getMBeanHome() throws RemoteException {
      try {
         InputStream var1 = null;

         MBeanHome var5;
         try {
            OutputStream var2 = this._request("_get_MBeanHome", true);
            var1 = (InputStream)this._invoke(var2);
            MBeanHome var3 = (MBeanHome)PortableRemoteObject.narrow((MBeanHome)var1.read_Object(), class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome);
            return var3;
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getMBeanHome();
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

   public final String getServerName() throws RemoteException {
      try {
         InputStream var1 = null;

         String var5;
         try {
            OutputStream var2 = this._request("_get_serverName", true);
            var1 = (InputStream)this._invoke(var2);
            String var3 = (String)var1.read_value(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return var3;
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getServerName();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2) throws RemoteException {
      try {
         InputStream var3 = null;

         ObjectInstance var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createMBean__CORBA_WStringValue__javax_management_ObjectName", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var3 = (InputStream)this._invoke(var4);
            ObjectInstance var5 = (ObjectInstance)var3.read_value(class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var6.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
               throw (InstanceAlreadyExistsException)((InstanceAlreadyExistsException)var3.read_value(class$javax$management$InstanceAlreadyExistsException == null ? (class$javax$management$InstanceAlreadyExistsException = class$("javax.management.InstanceAlreadyExistsException")) : class$javax$management$InstanceAlreadyExistsException));
            }

            if (var6.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var3.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            if (var6.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var3.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var6.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
               throw (NotCompliantMBeanException)((NotCompliantMBeanException)var3.read_value(class$javax$management$NotCompliantMBeanException == null ? (class$javax$management$NotCompliantMBeanException = class$("javax.management.NotCompliantMBeanException")) : class$javax$management$NotCompliantMBeanException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.createMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws RemoteException {
      try {
         InputStream var4 = null;

         ObjectInstance var8;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName", true);
            var5.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var5.write_value(var3, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4 = (InputStream)this._invoke(var5);
            ObjectInstance var6 = (ObjectInstance)var4.read_value(class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
            return var6;
         } catch (org.omg.CORBA.portable.ApplicationException var15) {
            var4 = (InputStream)var15.getInputStream();
            String var7 = var4.read_string();
            if (var7.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var4.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var7.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
               throw (InstanceAlreadyExistsException)((InstanceAlreadyExistsException)var4.read_value(class$javax$management$InstanceAlreadyExistsException == null ? (class$javax$management$InstanceAlreadyExistsException = class$("javax.management.InstanceAlreadyExistsException")) : class$javax$management$InstanceAlreadyExistsException));
            }

            if (var7.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var4.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            if (var7.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var4.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var7.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
               throw (NotCompliantMBeanException)((NotCompliantMBeanException)var4.read_value(class$javax$management$NotCompliantMBeanException == null ? (class$javax$management$NotCompliantMBeanException = class$("javax.management.NotCompliantMBeanException")) : class$javax$management$NotCompliantMBeanException));
            }

            if (var7.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var4.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var7);
         } catch (RemarshalException var16) {
            var8 = this.createMBean(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var8;
      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws RemoteException {
      try {
         InputStream var5 = null;

         ObjectInstance var9;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createMBean__CORBA_WStringValue__javax_management_ObjectName__org_omg_boxedRMI_java_lang_seq1__Object__org_omg_boxedRMI_CORBA_seq1_WStringValue", true);
            var6.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var3, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
            var6.write_value(var4, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            var5 = (InputStream)this._invoke(var6);
            ObjectInstance var7 = (ObjectInstance)var5.read_value(class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
            return var7;
         } catch (org.omg.CORBA.portable.ApplicationException var16) {
            var5 = (InputStream)var16.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var5.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var8.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
               throw (InstanceAlreadyExistsException)((InstanceAlreadyExistsException)var5.read_value(class$javax$management$InstanceAlreadyExistsException == null ? (class$javax$management$InstanceAlreadyExistsException = class$("javax.management.InstanceAlreadyExistsException")) : class$javax$management$InstanceAlreadyExistsException));
            }

            if (var8.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var5.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            if (var8.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var5.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var8.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
               throw (NotCompliantMBeanException)((NotCompliantMBeanException)var5.read_value(class$javax$management$NotCompliantMBeanException == null ? (class$javax$management$NotCompliantMBeanException = class$("javax.management.NotCompliantMBeanException")) : class$javax$management$NotCompliantMBeanException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var17) {
            var9 = this.createMBean(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

         return var9;
      } catch (SystemException var19) {
         throw Util.mapSystemException(var19);
      }
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws RemoteException {
      try {
         InputStream var6 = null;

         ObjectInstance var10;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var7 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__org_omg_boxedRMI_java_lang_seq1__Object__org_omg_boxedRMI_CORBA_seq1_WStringValue", true);
            var7.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var7.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var7.write_value(var3, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var7.write_value(var4, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
            var7.write_value(var5, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            var6 = (InputStream)this._invoke(var7);
            ObjectInstance var8 = (ObjectInstance)var6.read_value(class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
            return var8;
         } catch (org.omg.CORBA.portable.ApplicationException var17) {
            var6 = (InputStream)var17.getInputStream();
            String var9 = var6.read_string();
            if (var9.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var6.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var9.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
               throw (InstanceAlreadyExistsException)((InstanceAlreadyExistsException)var6.read_value(class$javax$management$InstanceAlreadyExistsException == null ? (class$javax$management$InstanceAlreadyExistsException = class$("javax.management.InstanceAlreadyExistsException")) : class$javax$management$InstanceAlreadyExistsException));
            }

            if (var9.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var6.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            if (var9.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var6.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var9.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
               throw (NotCompliantMBeanException)((NotCompliantMBeanException)var6.read_value(class$javax$management$NotCompliantMBeanException == null ? (class$javax$management$NotCompliantMBeanException = class$("javax.management.NotCompliantMBeanException")) : class$javax$management$NotCompliantMBeanException));
            }

            if (var9.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var6.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var9);
         } catch (RemarshalException var18) {
            var10 = this.createMBean(var1, var2, var3, var4, var5);
         } finally {
            this._releaseReply(var6);
         }

         return var10;
      } catch (SystemException var20) {
         throw Util.mapSystemException(var20);
      }
   }

   public final ObjectInstance registerMBean(Object var1, ObjectName var2) throws RemoteException {
      try {
         InputStream var3 = null;

         ObjectInstance var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("registerMBean", true);
            Util.writeAny(var4, var1);
            var4.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var3 = (InputStream)this._invoke(var4);
            ObjectInstance var5 = (ObjectInstance)var3.read_value(class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
               throw (InstanceAlreadyExistsException)((InstanceAlreadyExistsException)var3.read_value(class$javax$management$InstanceAlreadyExistsException == null ? (class$javax$management$InstanceAlreadyExistsException = class$("javax.management.InstanceAlreadyExistsException")) : class$javax$management$InstanceAlreadyExistsException));
            }

            if (var6.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
               throw (MBeanRegistrationException)((MBeanRegistrationException)var3.read_value(class$javax$management$MBeanRegistrationException == null ? (class$javax$management$MBeanRegistrationException = class$("javax.management.MBeanRegistrationException")) : class$javax$management$MBeanRegistrationException));
            }

            if (var6.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
               throw (NotCompliantMBeanException)((NotCompliantMBeanException)var3.read_value(class$javax$management$NotCompliantMBeanException == null ? (class$javax$management$NotCompliantMBeanException = class$("javax.management.NotCompliantMBeanException")) : class$javax$management$NotCompliantMBeanException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.registerMBean(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final void unregisterMBean(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("unregisterMBean", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            this._invoke(var3);
         } catch (org.omg.CORBA.portable.ApplicationException var11) {
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
            this.unregisterMBean(var1);
         } finally {
            this._releaseReply(var2);
         }

      } catch (SystemException var14) {
         throw Util.mapSystemException(var14);
      }
   }

   public final ObjectInstance getObjectInstance(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         ObjectInstance var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getObjectInstance", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = (InputStream)this._invoke(var3);
            ObjectInstance var4 = (ObjectInstance)var2.read_value(class$javax$management$ObjectInstance == null ? (class$javax$management$ObjectInstance = class$("javax.management.ObjectInstance")) : class$javax$management$ObjectInstance);
            return var4;
         } catch (org.omg.CORBA.portable.ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getObjectInstance(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final Set queryMBeans(ObjectName var1, QueryExp var2) throws RemoteException {
      try {
         InputStream var3 = null;

         Set var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("queryMBeans", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$javax$management$QueryExp == null ? (class$javax$management$QueryExp = class$("javax.management.QueryExp")) : class$javax$management$QueryExp);
            var3 = (InputStream)this._invoke(var4);
            Set var5 = (Set)var3.read_value(class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.queryMBeans(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final Set queryNames(ObjectName var1, QueryExp var2) throws RemoteException {
      try {
         InputStream var3 = null;

         Set var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("queryNames", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$javax$management$QueryExp == null ? (class$javax$management$QueryExp = class$("javax.management.QueryExp")) : class$javax$management$QueryExp);
            var3 = (InputStream)this._invoke(var4);
            Set var5 = (Set)var3.read_value(class$java$util$Set == null ? (class$java$util$Set = class$("java.util.Set")) : class$java$util$Set);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.queryNames(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final boolean isRegistered(ObjectName var1) throws RemoteException {
      try {
         Object var2 = null;

         boolean var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("isRegistered", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = this._invoke(var3);
            boolean var4 = ((org.omg.CORBA.portable.InputStream)var2).read_boolean();
            return var4;
         } catch (org.omg.CORBA.portable.ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = ((org.omg.CORBA.portable.InputStream)var2).read_string();
            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.isRegistered(var1);
         } finally {
            this._releaseReply((org.omg.CORBA.portable.InputStream)var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final Integer getMBeanCount() throws RemoteException {
      try {
         InputStream var1 = null;

         Integer var5;
         try {
            OutputStream var2 = this._request("_get_MBeanCount", true);
            var1 = (InputStream)this._invoke(var2);
            Integer var3 = (Integer)var1.read_value(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer);
            return var3;
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getMBeanCount();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final Object getAttribute(ObjectName var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         Object var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getAttribute", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = (InputStream)this._invoke(var4);
            Object var5 = (Object)Util.readAny(var3);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var3.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var6.equals("IDL:javax/management/AttributeNotFoundEx:1.0")) {
               throw (AttributeNotFoundException)((AttributeNotFoundException)var3.read_value(class$javax$management$AttributeNotFoundException == null ? (class$javax$management$AttributeNotFoundException = class$("javax.management.AttributeNotFoundException")) : class$javax$management$AttributeNotFoundException));
            }

            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getAttribute(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final AttributeList getAttributes(ObjectName var1, String[] var2) throws RemoteException {
      try {
         InputStream var3 = null;

         AttributeList var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getAttributes", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            var3 = (InputStream)this._invoke(var4);
            AttributeList var5 = (AttributeList)var3.read_value(class$javax$management$AttributeList == null ? (class$javax$management$AttributeList = class$("javax.management.AttributeList")) : class$javax$management$AttributeList);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.getAttributes(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final void setAttribute(ObjectName var1, Attribute var2) throws RemoteException {
      try {
         InputStream var3 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("setAttribute", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$javax$management$Attribute == null ? (class$javax$management$Attribute = class$("javax.management.Attribute")) : class$javax$management$Attribute);
            this._invoke(var4);
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var3 = (InputStream)var12.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/AttributeNotFoundEx:1.0")) {
               throw (AttributeNotFoundException)((AttributeNotFoundException)var3.read_value(class$javax$management$AttributeNotFoundException == null ? (class$javax$management$AttributeNotFoundException = class$("javax.management.AttributeNotFoundException")) : class$javax$management$AttributeNotFoundException));
            }

            if (var6.equals("IDL:javax/management/InvalidAttributeValueEx:1.0")) {
               throw (InvalidAttributeValueException)((InvalidAttributeValueException)var3.read_value(class$javax$management$InvalidAttributeValueException == null ? (class$javax$management$InvalidAttributeValueException = class$("javax.management.InvalidAttributeValueException")) : class$javax$management$InvalidAttributeValueException));
            }

            if (var6.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var3.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var13) {
            this.setAttribute(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final AttributeList setAttributes(ObjectName var1, AttributeList var2) throws RemoteException {
      try {
         InputStream var3 = null;

         AttributeList var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("setAttributes", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$javax$management$AttributeList == null ? (class$javax$management$AttributeList = class$("javax.management.AttributeList")) : class$javax$management$AttributeList);
            var3 = (InputStream)this._invoke(var4);
            AttributeList var5 = (AttributeList)var3.read_value(class$javax$management$AttributeList == null ? (class$javax$management$AttributeList = class$("javax.management.AttributeList")) : class$javax$management$AttributeList);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.setAttributes(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws RemoteException {
      try {
         InputStream var5 = null;

         Object var9;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("invoke", true);
            var6.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var3, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
            var6.write_value(var4, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            var5 = (InputStream)this._invoke(var6);
            Object var7 = (Object)Util.readAny(var5);
            return var7;
         } catch (org.omg.CORBA.portable.ApplicationException var16) {
            var5 = (InputStream)var16.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var8.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var5.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var8.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var5.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var17) {
            var9 = this.invoke(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

         return var9;
      } catch (SystemException var19) {
         throw Util.mapSystemException(var19);
      }
   }

   public final String getDefaultDomain() throws RemoteException {
      try {
         InputStream var1 = null;

         String var5;
         try {
            OutputStream var2 = this._request("_get_defaultDomain", true);
            var1 = (InputStream)this._invoke(var2);
            String var3 = (String)var1.read_value(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return var3;
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getDefaultDomain();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final String[] getDomains() throws RemoteException {
      try {
         InputStream var1 = null;

         String[] var5;
         try {
            OutputStream var2 = this._request("_get_domains", true);
            var1 = (InputStream)this._invoke(var2);
            String[] var3 = (String[])var1.read_value(array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            return var3;
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getDomains();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws RemoteException {
      try {
         InputStream var5 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("addNotificationListener__javax_management_ObjectName__javax_management_NotificationListener__javax_management_NotificationFilter__java_lang_Object", true);
            var6.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var2, class$javax$management$NotificationListener == null ? (class$javax$management$NotificationListener = class$("javax.management.NotificationListener")) : class$javax$management$NotificationListener);
            var6.write_value(var3, class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
            Util.writeAny(var6, var4);
            this._invoke(var6);
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var5 = (InputStream)var14.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var15) {
            this.addNotificationListener(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final void addNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws RemoteException {
      try {
         InputStream var5 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("addNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_management_NotificationFilter__java_lang_Object", true);
            var6.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var3, class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
            Util.writeAny(var6, var4);
            this._invoke(var6);
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var5 = (InputStream)var14.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var15) {
            this.addNotificationListener(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final void removeNotificationListener(ObjectName var1, ObjectName var2) throws RemoteException {
      try {
         InputStream var3 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            this._invoke(var4);
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var3 = (InputStream)var12.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
               throw (ListenerNotFoundException)((ListenerNotFoundException)var3.read_value(class$javax$management$ListenerNotFoundException == null ? (class$javax$management$ListenerNotFoundException = class$("javax.management.ListenerNotFoundException")) : class$javax$management$ListenerNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var13) {
            this.removeNotificationListener(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void removeNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws RemoteException {
      try {
         InputStream var5 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_management_NotificationFilter__java_lang_Object", true);
            var6.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var3, class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
            Util.writeAny(var6, var4);
            this._invoke(var6);
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var5 = (InputStream)var14.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var8.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
               throw (ListenerNotFoundException)((ListenerNotFoundException)var5.read_value(class$javax$management$ListenerNotFoundException == null ? (class$javax$management$ListenerNotFoundException = class$("javax.management.ListenerNotFoundException")) : class$javax$management$ListenerNotFoundException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var15) {
            this.removeNotificationListener(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final void removeNotificationListener(ObjectName var1, NotificationListener var2) throws RemoteException {
      try {
         InputStream var3 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("removeNotificationListener__javax_management_ObjectName__javax_management_NotificationListener", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$javax$management$NotificationListener == null ? (class$javax$management$NotificationListener = class$("javax.management.NotificationListener")) : class$javax$management$NotificationListener);
            this._invoke(var4);
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var3 = (InputStream)var12.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
               throw (ListenerNotFoundException)((ListenerNotFoundException)var3.read_value(class$javax$management$ListenerNotFoundException == null ? (class$javax$management$ListenerNotFoundException = class$("javax.management.ListenerNotFoundException")) : class$javax$management$ListenerNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var13) {
            this.removeNotificationListener(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void removeNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws RemoteException {
      try {
         InputStream var5 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("removeNotificationListener__javax_management_ObjectName__javax_management_NotificationListener__javax_management_NotificationFilter__java_lang_Object", true);
            var6.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var2, class$javax$management$NotificationListener == null ? (class$javax$management$NotificationListener = class$("javax.management.NotificationListener")) : class$javax$management$NotificationListener);
            var6.write_value(var3, class$javax$management$NotificationFilter == null ? (class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter")) : class$javax$management$NotificationFilter);
            Util.writeAny(var6, var4);
            this._invoke(var6);
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var5 = (InputStream)var14.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var8.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
               throw (ListenerNotFoundException)((ListenerNotFoundException)var5.read_value(class$javax$management$ListenerNotFoundException == null ? (class$javax$management$ListenerNotFoundException = class$("javax.management.ListenerNotFoundException")) : class$javax$management$ListenerNotFoundException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var15) {
            this.removeNotificationListener(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final MBeanInfo getMBeanInfo(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         MBeanInfo var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getMBeanInfo", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = (InputStream)this._invoke(var3);
            MBeanInfo var4 = (MBeanInfo)var2.read_value(class$javax$management$MBeanInfo == null ? (class$javax$management$MBeanInfo = class$("javax.management.MBeanInfo")) : class$javax$management$MBeanInfo);
            return var4;
         } catch (org.omg.CORBA.portable.ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var5.equals("IDL:javax/management/IntrospectionEx:1.0")) {
               throw (IntrospectionException)((IntrospectionException)var2.read_value(class$javax$management$IntrospectionException == null ? (class$javax$management$IntrospectionException = class$("javax.management.IntrospectionException")) : class$javax$management$IntrospectionException));
            }

            if (var5.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var2.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getMBeanInfo(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final boolean isInstanceOf(ObjectName var1, String var2) throws RemoteException {
      try {
         Object var3 = null;

         boolean var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("isInstanceOf", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var3 = this._invoke(var4);
            boolean var5 = ((org.omg.CORBA.portable.InputStream)var3).read_boolean();
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = ((org.omg.CORBA.portable.InputStream)var3).read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)((InputStream)var3).read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.isInstanceOf(var1, var2);
         } finally {
            this._releaseReply((org.omg.CORBA.portable.InputStream)var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final Object instantiate(String var1) throws RemoteException {
      try {
         InputStream var2 = null;

         Object var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("instantiate__CORBA_WStringValue", true);
            var3.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var2 = (InputStream)this._invoke(var3);
            Object var4 = (Object)Util.readAny(var2);
            return var4;
         } catch (org.omg.CORBA.portable.ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var2.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var5.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var2.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.instantiate(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final Object instantiate(String var1, ObjectName var2) throws RemoteException {
      try {
         InputStream var3 = null;

         Object var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("instantiate__CORBA_WStringValue__javax_management_ObjectName", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var3 = (InputStream)this._invoke(var4);
            Object var5 = (Object)Util.readAny(var3);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var6.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var3.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.instantiate(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final Object instantiate(String var1, Object[] var2, String[] var3) throws RemoteException {
      try {
         InputStream var4 = null;

         Object var8;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("instantiate__CORBA_WStringValue__org_omg_boxedRMI_java_lang_seq1__Object__org_omg_boxedRMI_CORBA_seq1_WStringValue", true);
            var5.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var2, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
            var5.write_value(var3, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            var4 = (InputStream)this._invoke(var5);
            Object var6 = (Object)Util.readAny(var4);
            return var6;
         } catch (org.omg.CORBA.portable.ApplicationException var15) {
            var4 = (InputStream)var15.getInputStream();
            String var7 = var4.read_string();
            if (var7.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var4.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var7.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var4.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            throw new UnexpectedException(var7);
         } catch (RemarshalException var16) {
            var8 = this.instantiate(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var8;
      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   public final Object instantiate(String var1, ObjectName var2, Object[] var3, String[] var4) throws RemoteException {
      try {
         InputStream var5 = null;

         Object var9;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var6 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("instantiate__CORBA_WStringValue__javax_management_ObjectName__org_omg_boxedRMI_java_lang_seq1__Object__org_omg_boxedRMI_CORBA_seq1_WStringValue", true);
            var6.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var6.write_value(var3, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
            var6.write_value(var4, array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
            var5 = (InputStream)this._invoke(var6);
            Object var7 = (Object)Util.readAny(var5);
            return var7;
         } catch (org.omg.CORBA.portable.ApplicationException var16) {
            var5 = (InputStream)var16.getInputStream();
            String var8 = var5.read_string();
            if (var8.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var5.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            if (var8.equals("IDL:javax/management/MBeanEx:1.0")) {
               throw (MBeanException)((MBeanException)var5.read_value(class$javax$management$MBeanException == null ? (class$javax$management$MBeanException = class$("javax.management.MBeanException")) : class$javax$management$MBeanException));
            }

            if (var8.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var5.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var8);
         } catch (RemarshalException var17) {
            var9 = this.instantiate(var1, var2, var3, var4);
         } finally {
            this._releaseReply(var5);
         }

         return var9;
      } catch (SystemException var19) {
         throw Util.mapSystemException(var19);
      }
   }

   public final ObjectInputStream deserialize(ObjectName var1, byte[] var2) throws RemoteException {
      try {
         InputStream var3 = null;

         ObjectInputStream var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("deserialize__javax_management_ObjectName__org_omg_boxedRMI_seq1_octet", true);
            var4.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var4.write_value(var2, array$B == null ? (array$B = class$("[B")) : array$B);
            var3 = (InputStream)this._invoke(var4);
            ObjectInputStream var5 = (ObjectInputStream)var3.read_value(class$java$io$ObjectInputStream == null ? (class$java$io$ObjectInputStream = class$("java.io.ObjectInputStream")) : class$java$io$ObjectInputStream);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var3.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var6.equals("IDL:javax/management/OperationsEx:1.0")) {
               throw (OperationsException)((OperationsException)var3.read_value(class$javax$management$OperationsException == null ? (class$javax$management$OperationsException = class$("javax.management.OperationsException")) : class$javax$management$OperationsException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.deserialize(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final ObjectInputStream deserialize(String var1, byte[] var2) throws RemoteException {
      try {
         InputStream var3 = null;

         ObjectInputStream var7;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("deserialize__CORBA_WStringValue__org_omg_boxedRMI_seq1_octet", true);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, array$B == null ? (array$B = class$("[B")) : array$B);
            var3 = (InputStream)this._invoke(var4);
            ObjectInputStream var5 = (ObjectInputStream)var3.read_value(class$java$io$ObjectInputStream == null ? (class$java$io$ObjectInputStream = class$("java.io.ObjectInputStream")) : class$java$io$ObjectInputStream);
            return var5;
         } catch (org.omg.CORBA.portable.ApplicationException var14) {
            var3 = (InputStream)var14.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:javax/management/OperationsEx:1.0")) {
               throw (OperationsException)((OperationsException)var3.read_value(class$javax$management$OperationsException == null ? (class$javax$management$OperationsException = class$("javax.management.OperationsException")) : class$javax$management$OperationsException));
            }

            if (var6.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var3.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var15) {
            var7 = this.deserialize(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

         return var7;
      } catch (SystemException var17) {
         throw Util.mapSystemException(var17);
      }
   }

   public final ObjectInputStream deserialize(String var1, ObjectName var2, byte[] var3) throws RemoteException {
      try {
         InputStream var4 = null;

         ObjectInputStream var8;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var5 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("deserialize__CORBA_WStringValue__javax_management_ObjectName__org_omg_boxedRMI_seq1_octet", true);
            var5.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var5.write_value(var2, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var5.write_value(var3, array$B == null ? (array$B = class$("[B")) : array$B);
            var4 = (InputStream)this._invoke(var5);
            ObjectInputStream var6 = (ObjectInputStream)var4.read_value(class$java$io$ObjectInputStream == null ? (class$java$io$ObjectInputStream = class$("java.io.ObjectInputStream")) : class$java$io$ObjectInputStream);
            return var6;
         } catch (org.omg.CORBA.portable.ApplicationException var15) {
            var4 = (InputStream)var15.getInputStream();
            String var7 = var4.read_string();
            if (var7.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var4.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            if (var7.equals("IDL:javax/management/OperationsEx:1.0")) {
               throw (OperationsException)((OperationsException)var4.read_value(class$javax$management$OperationsException == null ? (class$javax$management$OperationsException = class$("javax.management.OperationsException")) : class$javax$management$OperationsException));
            }

            if (var7.equals("IDL:javax/management/ReflectionEx:1.0")) {
               throw (ReflectionException)((ReflectionException)var4.read_value(class$javax$management$ReflectionException == null ? (class$javax$management$ReflectionException = class$("javax.management.ReflectionException")) : class$javax$management$ReflectionException));
            }

            throw new UnexpectedException(var7);
         } catch (RemarshalException var16) {
            var8 = this.deserialize(var1, var2, var3);
         } finally {
            this._releaseReply(var4);
         }

         return var8;
      } catch (SystemException var18) {
         throw Util.mapSystemException(var18);
      }
   }

   public final ClassLoader getClassLoaderFor(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         ClassLoader var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getClassLoaderFor", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = (InputStream)this._invoke(var3);
            ClassLoader var4 = (ClassLoader)var2.read_value(class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader);
            return var4;
         } catch (org.omg.CORBA.portable.ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getClassLoaderFor(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final ClassLoader getClassLoader(ObjectName var1) throws RemoteException {
      try {
         InputStream var2 = null;

         ClassLoader var6;
         try {
            org.omg.CORBA_2_3.portable.OutputStream var3 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("getClassLoader", true);
            var3.write_value(var1, class$javax$management$ObjectName == null ? (class$javax$management$ObjectName = class$("javax.management.ObjectName")) : class$javax$management$ObjectName);
            var2 = (InputStream)this._invoke(var3);
            ClassLoader var4 = (ClassLoader)var2.read_value(class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader);
            return var4;
         } catch (org.omg.CORBA.portable.ApplicationException var13) {
            var2 = (InputStream)var13.getInputStream();
            String var5 = var2.read_string();
            if (var5.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
               throw (InstanceNotFoundException)((InstanceNotFoundException)var2.read_value(class$javax$management$InstanceNotFoundException == null ? (class$javax$management$InstanceNotFoundException = class$("javax.management.InstanceNotFoundException")) : class$javax$management$InstanceNotFoundException));
            }

            throw new UnexpectedException(var5);
         } catch (RemarshalException var14) {
            var6 = this.getClassLoader(var1);
         } finally {
            this._releaseReply(var2);
         }

         return var6;
      } catch (SystemException var16) {
         throw Util.mapSystemException(var16);
      }
   }

   public final ClassLoaderRepository getClassLoaderRepository() throws RemoteException {
      try {
         InputStream var1 = null;

         ClassLoaderRepository var5;
         try {
            OutputStream var2 = this._request("_get_classLoaderRepository", true);
            var1 = (InputStream)this._invoke(var2);
            ClassLoaderRepository var3 = (ClassLoaderRepository)var1.read_value(class$javax$management$loading$ClassLoaderRepository == null ? (class$javax$management$loading$ClassLoaderRepository = class$("javax.management.loading.ClassLoaderRepository")) : class$javax$management$loading$ClassLoaderRepository);
            return var3;
         } catch (org.omg.CORBA.portable.ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getClassLoaderRepository();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }
}
