package weblogic.iiop;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.ConnectIOException;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.InvalidTransactionException;
import javax.transaction.TransactionRequiredException;
import javax.transaction.TransactionRolledbackException;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TIMEOUT;
import org.omg.CORBA.TRANSACTION_REQUIRED;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ValueBase;
import org.omg.CosNaming.NamingContextHelper;
import weblogic.corba.cos.naming.NamingContextAnyHelper;
import weblogic.corba.utils.RepositoryId;
import weblogic.corba.utils.ValueHandlerImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.RequestTimeoutException;
import weblogic.rmi.extensions.server.ActivatableRemoteReference;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.extensions.server.CollocatedRemoteReference;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.internal.ServerReference;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.WeakConcurrentHashMap;

public final class Utils {
   private static ConcurrentHashMap repositoryIdMap = new ConcurrentHashMap();
   private static WeakConcurrentHashMap abstractInterfaceMap = new WeakConcurrentHashMap();
   private static ConcurrentHashMap helperMap = new ConcurrentHashMap();
   private static ConcurrentHashMap declaredMethodMap = new ConcurrentHashMap();
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   public static final String IDL_ENTITY_HELPER = "Helper";
   static final String IDL_ENTITY_HOLDER = "Holder";
   static final String VALUE_FACTORY_HELPER = "DefaultFactory";
   public static final String IDL_PREFIX = "IDL:";
   static final String WEBLOGIC_OMG_JAVA_PREFIX = "weblogic.corba.idl.";
   static final String OMG_STUB_PREFIX = "org.omg.stub.";
   public static final String OMG_JAVA_PREFIX = "org.omg.";
   public static final String OMG_IDL_PREFIX = "IDL:omg.org/";
   static final String OMG_UNKNOWN_EX = "IDL:omg.org/CORBA/UNKNOWN:1.0";
   static final String STRING_REP = "IDL:omg.org/CORBA/WStringValue:1.0";
   static final String NULL_REPID = "IDL:omg.org/CORBA/AbstractBase:1.0";
   static final String OBJECT_REPID = "IDL:omg.org/CORBA/Object:1.0";
   static final String NAMING_REPID = "IDL:omg.org/CosNaming/NamingContext:1.0";
   static final String EJB_EXCEPTION_REPID = "RMI:javax.ejb.EJBException:0E3E8C42D0E83868:0B0EB2FF36CB22F6";
   static final String OLD_EJB_EXCEPTION_REPID = "RMI:javax.ejb.EJBException:0E3E8C42D0E83868:800C4C7C598DF61F";
   static final String USER_EX = "Ex";
   public static final String WRITE_METHOD = "write";
   public static final String NARROW_METHOD = "narrow";
   public static final String READ_METHOD = "read";
   public static final Class[] READ_METHOD_ARGS = new Class[]{InputStream.class};
   static final Class[] NO_ARGS_METHOD = new Class[0];
   static final String CTOR = "<init>";
   static final String USER_EX_SUFFIX = "ception";
   static final String USER_EXCEPTION = "Exception";
   public static final String STUB_EXT = "_IIOP_WLStub";

   private static void addToMap(String var0, String var1) {
      if (var1 != null && var0 != null) {
         repositoryIdMap.put(var0, var1);
      }

   }

   public static final String getRepositoryID(Class var0) {
      String var1 = (String)repositoryIdMap.get(var0.getName());
      if (var1 != null) {
         return var1;
      } else {
         if (Utilities.isARemote(var0)) {
            var1 = getIDFromRemote(var0);
         } else if (Throwable.class.isAssignableFrom(var0)) {
            var1 = getIDFromException(var0);
         } else if (IDLEntity.class.isAssignableFrom(var0)) {
            var1 = getIDFromIDLEntity(var0);
         } else if (Serializable.class.isAssignableFrom(var0)) {
            var1 = ValueHandlerImpl.getRepositoryID(var0);
         }

         return var1;
      }
   }

   public static final boolean isIDLInterface(Class var0) {
      return Object.class.isAssignableFrom(var0) && IDLEntity.class.isAssignableFrom(var0);
   }

   private static String getIDFromRemote(Class var0) {
      String var1 = null;

      for(Class var2 = var0; Utilities.isARemote(var2); var2 = var2.getSuperclass()) {
         Class[] var3 = var2.getInterfaces();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            Class var5 = var3[var4];
            if (Utilities.isARemote(var5)) {
               if (IDLUtils.isIDLInterface(var5)) {
                  var1 = getIDFromIDLEntity(var5);
               } else {
                  var1 = IDLUtils.getTypeID(var5);
               }

               if (var1 != null) {
                  repositoryIdMap.put(var0.getName(), var1);
               }

               return var1;
            }
         }
      }

      return null;
   }

   public static String createIDFromIDLEntity(Class var0) {
      String var1 = var0.getName();
      String var2;
      if (var1.startsWith("org.omg.")) {
         var2 = "IDL:omg.org/" + var1.substring("org.omg.".length()).replace('.', '/') + ":1.0";
      } else if (var1.startsWith("weblogic.corba.idl.")) {
         var2 = "IDL:omg.org/" + var1.substring("weblogic.corba.idl.".length() + "org.omg.".length()).replace('.', '/') + ":1.0";
      } else {
         var2 = "IDL:" + var0.getName().replace('.', '/') + ":1.0";
      }

      return var2;
   }

   private static String getIDFromIDLEntity(Class var0) {
      String var1 = createIDFromIDLEntity(var0);
      if (var1 != null) {
         repositoryIdMap.put(var0.getName(), var1);
      }

      return var1;
   }

   public static String getRepositoryID(StubInfo var0) {
      String var1 = var0.getRepositoryId();
      if (var1 == null) {
         Class[] var2 = var0.getInterfaces();
         Class[] var3 = new Class[var2.length];
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;

         while(true) {
            if (var6 >= var2.length) {
               if (var5 > 1) {
                  RemoteReference var11 = var0.getRemoteRef();
                  if (var11 instanceof CollocatedRemoteReference) {
                     ServerReference var12 = ((CollocatedRemoteReference)var11).getServerReference();
                     java.lang.Object var8 = var12.getImplementation();
                     if (var8 == null) {
                        if (!(var12 instanceof ActivatableServerReference) || !(var11 instanceof ActivatableRemoteReference)) {
                           debugIIOPDetail.debug("Unexptected error: Unable to get implementation: ServerReference=" + var12 + "; RemoteReference=" + var11);
                           throw new RemoteRuntimeException("Unexptected error: unable to get implementation");
                        }

                        try {
                           var8 = ((ActivatableServerReference)var12).getImplementation(((ActivatableRemoteReference)var11).getActivationID());
                        } catch (RemoteException var10) {
                           debugIIOPDetail.debug("Unable to get implementation: ServerReference=" + var12 + "; RemoteReference=" + var11, var10);
                           throw new RemoteRuntimeException("Unable to get implementation", var10);
                        }
                     }

                     var1 = IDLUtils.getTypeID(var8.getClass());
                  }
               }

               if (var1 == null) {
                  var1 = (String)repositoryIdMap.get(var3[0].getName());
                  if (var1 == null) {
                     var1 = getIDFromRemote(var3);
                  }
               }

               var0.setRepositoryId(var1);
               break;
            }

            if (Utilities.isARemote(var2[var6])) {
               int var7 = 0;

               while(true) {
                  if (var7 >= var4) {
                     var3[var4++] = var2[var6];
                     if (Remote.class.isAssignableFrom(var2[var6])) {
                        ++var5;
                     }
                     break;
                  }

                  if (var2[var6].isAssignableFrom(var3[var7])) {
                     break;
                  }

                  ++var7;
               }
            }

            ++var6;
         }
      }

      return var1;
   }

   private static String getIDFromRemote(Class[] var0) {
      String var1 = null;
      Class[] var2 = var0;

      for(int var3 = 0; var3 < var2.length - 1; ++var3) {
         Class var4 = var2[var3];
         if (Utilities.isARemote(var4)) {
            var1 = IDLUtils.getTypeID(var4);
            if (var1 != null) {
               repositoryIdMap.put(var2[var3].getName(), var1);
            }

            return var1;
         }
      }

      return null;
   }

   public static String getIDFromException(Class var0) {
      if (!SystemException.class.isAssignableFrom(var0) && !UserException.class.isAssignableFrom(var0)) {
         if (!RuntimeException.class.isAssignableFrom(var0) && !Error.class.isAssignableFrom(var0)) {
            String var1 = var0.getName().replace('.', '/');
            if (var1.endsWith("Exception")) {
               var1 = var1.substring(0, var1.length() - "ception".length());
            }

            String var2 = "IDL:" + var1 + ":1.0";
            repositoryIdMap.put(var0.getName(), var2);
            return var2;
         } else {
            return "IDL:omg.org/CORBA/UNKNOWN:1.0";
         }
      } else {
         return getIDFromIDLEntity(var0);
      }
   }

   public static final Class getClassFromID(RepositoryId var0) {
      return getClassFromID(var0, (String)null);
   }

   public static final Class getClassFromID(RepositoryId var0, String var1) {
      Class var2 = (Class)RepositoryId.PRIMITIVE_MAP.get(var0);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = var0.getClassName();
         boolean var4 = false;

         try {
            var2 = loadClass(var3, var1, (ClassLoader)null);
         } catch (ClassNotFoundException var13) {
            if (var3.endsWith("Ex") && (var3 = var3 + "ception") != null) {
               try {
                  var2 = loadClass(var3, var1, (ClassLoader)null);
               } catch (ClassNotFoundException var11) {
                  var3 = var3.substring(0, var3.length() - "Exception".length());

                  try {
                     var2 = loadClass(var3, var1, (ClassLoader)null);
                  } catch (ClassNotFoundException var10) {
                  }
               }
            } else {
               int var14;
               if ((var14 = var3.lastIndexOf(46)) >= 0) {
                  String var6 = var3.substring(0, var14) + "Package" + var3.substring(var14);

                  try {
                     var2 = loadClass(var6, var1, (ClassLoader)null);
                  } catch (ClassNotFoundException var12) {
                     if (var3.startsWith("org.omg.") && (var3 = "weblogic.corba.idl." + var3) != null) {
                        try {
                           var2 = loadClass(var3);
                        } catch (ClassNotFoundException var9) {
                        }
                     }
                  }
               }
            }
         }

         return var2;
      }
   }

   public static Class getClassFromStub(Class var0) {
      if (var0.isInterface() && IDLEntity.class.isAssignableFrom(var0) && Object.class.isAssignableFrom(var0) && !IDLEntity.class.equals(var0)) {
         return var0;
      } else {
         Class[] var1 = var0.getInterfaces();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (IDLEntity.class.isAssignableFrom(var1[var2]) && Object.class.isAssignableFrom(var1[var2]) && !IDLEntity.class.equals(var1[var2])) {
               return var1[var2];
            }
         }

         return null;
      }
   }

   public static Class getStubFromClass(Class var0) {
      return getStubFromClass(var0, (String)null);
   }

   public static Class getStubFromClass(Class var0, String var1) {
      String var2 = null;
      if (var0.getPackage() != null) {
         String var3 = var0.getPackage().getName();
         var2 = var3 + "._" + var0.getName().substring(var3.length() + 1) + "Stub";
      } else {
         var2 = "_" + var0.getName() + "Stub";
      }

      Class var6 = null;

      try {
         var6 = loadClass(var2, var1, var0.getClassLoader());
      } catch (ClassNotFoundException var5) {
      }

      return var6;
   }

   public static String getClassNameFromStubName(String var0) {
      int var1 = var0.indexOf("_Stub.class");
      if (var1 >= 0) {
         if (var0.startsWith("org.omg.stub.")) {
            var0 = var0.substring("org.omg.stub.".length());
            var1 -= "org.omg.stub.".length();
         }

         int var2 = var0.indexOf("._");
         if (var2 >= 0) {
            var0 = var0.substring(0, var2) + "." + var0.substring(var2 + 2, var1);
         } else {
            var0 = var0.substring(1, var1);
         }

         return var0;
      } else {
         return null;
      }
   }

   public static boolean isStubName(String var0) {
      return var0.endsWith("_Stub.class") && (var0.indexOf("/_") > 0 || var0.startsWith("_"));
   }

   public static boolean isIDLException(Class var0) {
      return !IDLEntity.class.equals(var0) && IDLEntity.class.isAssignableFrom(var0) && !ValueBase.class.isAssignableFrom(var0) && Throwable.class.isAssignableFrom(var0);
   }

   public static boolean isAbstractInterface(Class var0) {
      Boolean var1 = (Boolean)abstractInterfaceMap.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         boolean var2 = IDLUtils.isAbstractInterface(var0);
         abstractInterfaceMap.put(var0, new Boolean(var2));
         return var2;
      }
   }

   static Class findIDLInterface(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         Class var1 = var0.getSuperclass();
         if (IDLEntity.class.equals(var1)) {
            return var0;
         } else {
            Class[] var2 = var0.getInterfaces();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (IDLEntity.class.equals(var2[var3])) {
                  return var0;
               }
            }

            Class var5 = null;
            if (var1 != null && var1.isInterface() && (var5 = findIDLInterface(var1)) != null) {
               return var5;
            } else {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  if ((var5 = findIDLInterface(var2[var4])) != null) {
                     return var5;
                  }
               }

               if (var1 != null && !var1.isInterface()) {
                  var5 = findIDLInterface(var1);
               }

               return var5;
            }
         }
      }
   }

   public static Class getHelper(Class var0, String var1) {
      ClassLoader var2 = var0.getClassLoader();
      Class var3 = (Class)helperMap.get(var0);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = var0.getName() + var1;

         try {
            var3 = loadClass(var4, (String)null, var2);
         } catch (ClassNotFoundException var8) {
            try {
               var3 = loadClass(var4);
            } catch (ClassNotFoundException var7) {
            }
         }

         if (var3 != null) {
            helperMap.put(var0, var3);
         }

         return var3;
      }
   }

   public static Class getIDLHelper(Class var0) {
      Class var1 = getHelper(var0, "Helper");
      if (var1 == null) {
         Class var2 = findIDLInterface(var0);
         if (var2 != null) {
            var1 = getHelper(var2, "Helper");
         }
      }

      return var1;
   }

   public static Class getIDLHolder(Class var0) {
      Class var1 = getHelper(var0, "Holder");
      if (var1 == null) {
         Class var2 = findIDLInterface(var0);
         var1 = getHelper(var2, "Holder");
      }

      return var1;
   }

   public static Method getIDLWriter(Class var0) {
      java.lang.Object var1 = null;
      Class var2 = var0;
      Class var3 = getHelper(var0, "Helper");
      if (var3 == null) {
         var2 = findIDLInterface(var0);
         var3 = getHelper(var2, "Helper");
      }

      return var3 == null ? null : getDeclaredMethod(var3, "write", new Class[]{OutputStream.class, var2});
   }

   public static Member getDeclaredMember(Class var0, String var1, Class[] var2) {
      ConcurrentHashMap var3 = (ConcurrentHashMap)declaredMethodMap.get(var0);
      if (var3 == null) {
         synchronized(declaredMethodMap) {
            var3 = (ConcurrentHashMap)declaredMethodMap.get(var0);
            if (var3 == null) {
               var3 = new ConcurrentHashMap();
               declaredMethodMap.put(var0, var3);
            }
         }
      }

      try {
         java.lang.Object var4 = (AccessibleObject)var3.get(var1);
         if (var4 == null) {
            if (var1 == "<init>") {
               var4 = var0.getDeclaredConstructor(var2);
            } else {
               var4 = var0.getDeclaredMethod(var1, var2);
            }

            if (!((AccessibleObject)var4).isAccessible()) {
               try {
                  ((AccessibleObject)var4).setAccessible(true);
               } catch (SecurityException var6) {
               }
            }

            var3.put(var1, var4);
         }

         return (Member)var4;
      } catch (NoSuchMethodException var7) {
         var3.put(var1, var1);
         return null;
      } catch (ClassCastException var8) {
         return null;
      }
   }

   public static Constructor getNoArgConstructor(Class var0) {
      return (Constructor)getDeclaredMember(var0, "<init>", NO_ARGS_METHOD);
   }

   public static Method getDeclaredMethod(Class var0, String var1, Class[] var2) {
      return (Method)getDeclaredMember(var0, var1, var2);
   }

   public static Constructor getDeclaredConstructor(Class var0, Class[] var1) {
      return (Constructor)getDeclaredMember(var0, "<init>", var1);
   }

   public static Object narrow(ObjectImpl var0, Class var1) throws InvocationTargetException, IllegalAccessException, ClassCastException {
      Class var2 = getIDLHelper(var1);
      if (var2 == null) {
         throw new ClassCastException("Couldn't find helper for: " + var1.getName());
      } else {
         Method var3 = getDeclaredMethod(var2, "narrow", new Class[]{Object.class});
         if (var3 == null) {
            throw new ClassCastException("Couldn't find helper for: " + var1.getName());
         } else {
            return (Object)var3.invoke((java.lang.Object)null, var0);
         }
      }
   }

   public static final IOException mapToRemoteException(IOException var0) {
      if (var0 instanceof UnknownHostException) {
         return new java.rmi.UnknownHostException(var0.getMessage(), var0);
      } else if (var0 instanceof ConnectException) {
         return new java.rmi.ConnectException(var0.getMessage(), var0);
      } else {
         return (IOException)(var0 instanceof SocketException ? new ConnectIOException(var0.getMessage(), var0) : var0);
      }
   }

   public static final Class loadClass(String var0) throws ClassNotFoundException {
      return loadClass(var0, (String)null, (ClassLoader)null);
   }

   public static final Class loadClass(String var0, String var1, ClassLoader var2) throws ClassNotFoundException {
      return Utilities.loadClass(var0, var1, var2);
   }

   public static final String getAnnotation(ClassLoader var0) {
      if (var0 == null) {
         var0 = Thread.currentThread().getContextClassLoader();
      }

      String var1 = null;
      if (var0 instanceof GenericClassLoader) {
         var1 = ((GenericClassLoader)var0).getAnnotation().getAnnotationString();
         if (var1.length() == 0) {
            var1 = null;
         }
      }

      return var1;
   }

   public static final SystemException mapRemoteToCORBAException(RemoteException var0) {
      java.lang.Object var1 = null;
      if (var0 instanceof java.rmi.UnknownHostException) {
         var1 = new BAD_PARAM(var0.getMessage(), 1330446344, CompletionStatus.COMPLETED_NO);
      } else if (var0 instanceof java.rmi.ConnectException) {
         var1 = new COMM_FAILURE(var0.getMessage(), 0, CompletionStatus.COMPLETED_NO);
      } else if (var0 instanceof ConnectIOException) {
         var1 = new COMM_FAILURE(var0.getMessage(), 0, CompletionStatus.COMPLETED_NO);
      } else if (var0 instanceof MarshalException) {
         var1 = new MARSHAL(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
      } else if (var0 instanceof UnmarshalException) {
         var1 = new MARSHAL(var0.getMessage(), 0, CompletionStatus.COMPLETED_NO);
      } else if (var0 instanceof NoSuchObjectException) {
         var1 = new OBJECT_NOT_EXIST(var0.getMessage(), 1330446337, CompletionStatus.COMPLETED_NO);
      } else if (var0 instanceof AccessException) {
         var1 = new NO_PERMISSION(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
      } else if (var0 instanceof TransactionRequiredException) {
         var1 = new TRANSACTION_REQUIRED(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
      } else if (var0 instanceof TransactionRolledbackException) {
         var1 = new TRANSACTION_ROLLEDBACK(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
      } else if (var0 instanceof InvalidTransactionException) {
         var1 = new INVALID_TRANSACTION(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
      } else if (var0 instanceof RequestTimeoutException) {
         var1 = new TIMEOUT(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
      }

      if (var1 != null) {
         ((SystemException)var1).initCause(var0);
      }

      return (SystemException)var1;
   }

   public static final SystemException mapToCORBAException(IOException var0) {
      if (var0.getCause() instanceof SystemException) {
         return (SystemException)var0.getCause();
      } else {
         SystemException var1 = null;
         if (var0 instanceof RemoteException) {
            var1 = mapRemoteToCORBAException((RemoteException)var0);
            if (var1 != null) {
               return var1;
            }
         }

         java.lang.Object var2;
         if (var0 instanceof UnknownHostException) {
            var2 = new BAD_PARAM(var0.getMessage(), 1330446344, CompletionStatus.COMPLETED_NO);
         } else if (var0 instanceof ConnectException) {
            var2 = new COMM_FAILURE(var0.getMessage(), 0, CompletionStatus.COMPLETED_NO);
         } else if (var0 instanceof SocketException) {
            var2 = new COMM_FAILURE(var0.getMessage(), 0, CompletionStatus.COMPLETED_NO);
         } else {
            var2 = new UNKNOWN(var0.getMessage(), 0, CompletionStatus.COMPLETED_MAYBE);
         }

         ((SystemException)var2).initCause(var0);
         return (SystemException)var2;
      }
   }

   public static final MARSHAL wrapMARSHALWithCause(Throwable var0) {
      MARSHAL var1 = new MARSHAL(var0.getMessage());
      var1.initCause(var0);
      return var1;
   }

   public static final RemoteException mapSystemException(SystemException var0) {
      java.lang.Object var1 = null;
      if (var0 instanceof COMM_FAILURE) {
         var1 = new MarshalException(createDetailMessage(var0), var0);
      } else if (!(var0 instanceof INV_OBJREF) && !(var0 instanceof OBJECT_NOT_EXIST)) {
         if (var0 instanceof NO_PERMISSION) {
            var1 = new AccessException(createDetailMessage(var0), var0);
         } else if (var0 instanceof MARSHAL) {
            if (var0.completed.value() == 1) {
               var1 = new UnmarshalException(createDetailMessage(var0), var0);
            } else {
               var1 = new MarshalException(createDetailMessage(var0), var0);
            }
         } else if (var0 instanceof TRANSACTION_REQUIRED) {
            var1 = new TransactionRequiredException(createDetailMessage(var0));
         } else if (var0 instanceof TRANSACTION_ROLLEDBACK) {
            var1 = new TransactionRolledbackException(createDetailMessage(var0));
         } else if (var0 instanceof INVALID_TRANSACTION) {
            var1 = new InvalidTransactionException(createDetailMessage(var0));
         } else if (var0 instanceof BAD_PARAM) {
            var1 = new MarshalException(createDetailMessage(var0), var0);
         } else {
            var1 = new RemoteException(createDetailMessage(var0), var0);
         }
      } else {
         var1 = new NoSuchObjectException(createDetailMessage(var0));
      }

      ((RemoteException)var1).detail = var0;
      return (RemoteException)var1;
   }

   private static final String createDetailMessage(SystemException var0) {
      StringBuffer var1 = new StringBuffer("CORBA ");
      var1.append(var0.getClass().getName().substring("org.omg.CORBA.".length()));
      var1.append(" ");
      var1.append(Integer.toHexString(var0.minor));
      switch (var0.completed.value()) {
         case 0:
            var1.append(" Yes");
            break;
         case 1:
            var1.append(" No");
            break;
         case 2:
            var1.append(" Maybe");
      }

      return var1.toString();
   }

   private static void p(String var0) {
      System.out.println("<Utils>: " + var0);
   }

   static {
      addToMap("org.omg.CosNaming.NamingContext", "IDL:omg.org/CosNaming/NamingContextExt:1.0");
      addToMap("java.lang.String", "IDL:omg.org/CORBA/WStringValue:1.0");
      addToMap("weblogic.cos.naming.NamingContext_IIOP_WLStub", NamingContextAnyHelper.id());
      addToMap("weblogic.cos.naming.NamingContextImpl", NamingContextAnyHelper.id());
      addToMap("org.omg.CosNaming.NamingContext", NamingContextHelper.id());
      addToMap("weblogic.corba.cos.naming.NamingContextAny", NamingContextAnyHelper.id());
   }
}
