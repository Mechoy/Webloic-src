package weblogic.corba.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;
import org.omg.CORBA.portable.ValueBase;
import weblogic.ejb.spi.PortableReplaceable;
import weblogic.iiop.IDLUtils;
import weblogic.iiop.Utils;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.collections.WeakConcurrentHashMap;
import weblogic.utils.io.ObjectStreamClass;

public final class ClassInfo {
   private static final Class[] NO_ARGS_METHOD = new Class[0];
   private static final Object[] NO_ARGS = new Object[0];
   private static final Class[] WRITE_OBJECT_ARGS = new Class[]{ObjectOutputStream.class};
   private static final boolean DEBUG = false;
   private static boolean useFullRepositoyIdList = false;
   private RepositoryId repId;
   private RepositoryId localRepId;
   private RepositoryId[] repIdList;
   private Class theClass;
   private Class idlHelper;
   private Method idlWriter;
   private Method idlReader;
   private boolean abstractInterface;
   private boolean portableReplaceable;
   private boolean idlEntity;
   private boolean streamable;
   private boolean valuebase;
   private boolean string;
   private ObjectStreamClass osc;
   private static WeakConcurrentHashMap classInfoMap = new WeakConcurrentHashMap();
   private static ConcurrentHashMap repositoryIdMap = new ConcurrentHashMap();

   public ClassInfo(RepositoryId var1) {
      this(var1, (String)null);
   }

   public ClassInfo(RepositoryId var1, String var2) {
      this.abstractInterface = false;
      this.portableReplaceable = false;
      this.idlEntity = false;
      this.streamable = false;
      this.valuebase = false;
      this.string = false;
      this.repId = var1;
      this.theClass = Utils.getClassFromID(var1, var2);
      this.init();
   }

   public ClassInfo(Class var1) {
      this.abstractInterface = false;
      this.portableReplaceable = false;
      this.idlEntity = false;
      this.streamable = false;
      this.valuebase = false;
      this.string = false;
      this.theClass = var1;
      this.repId = new RepositoryId(var1);
      this.localRepId = this.repId;
      this.init();
   }

   private ClassInfo(RepositoryId var1, Class var2) {
      this.abstractInterface = false;
      this.portableReplaceable = false;
      this.idlEntity = false;
      this.streamable = false;
      this.valuebase = false;
      this.string = false;
      this.theClass = var2;
      this.repId = var1;
      this.localRepId = var1;
      this.init();
   }

   private void init() {
      if (this.theClass != null) {
         this.osc = ObjectStreamClass.lookup(this.theClass);
         this.abstractInterface = IDLUtils.isAbstractInterface(this.theClass);
         this.portableReplaceable = PortableReplaceable.class.isAssignableFrom(this.theClass);
         this.idlEntity = IDLEntity.class.isAssignableFrom(this.theClass);
         this.streamable = Streamable.class.isAssignableFrom(this.theClass);
         this.valuebase = ValueBase.class.isAssignableFrom(this.theClass);
         this.string = String.class.isAssignableFrom(this.theClass);
         if (this.localRepId == null) {
            this.repId.setClassLoader(this.theClass.getClassLoader());
            this.localRepId = new RepositoryId(this.theClass);
            if (this.localRepId.equals(this.repId)) {
               this.localRepId = this.repId;
            }
         }

         if (this.osc != null && this.osc.isCustomMarshaled() && useFullRepositoyIdList) {
            this.repIdList = RepositoryId.getRepositoryIdList(this.theClass);
         }
      }

      if (this.repId != RepositoryId.STRING && this.repId.isIDLType()) {
         try {
            this.idlHelper = Utils.loadClass(this.repId.getClassName() + "Helper");
            this.idlReader = Utils.getDeclaredMethod(this.idlHelper, "read", Utils.READ_METHOD_ARGS);
         } catch (ClassNotFoundException var2) {
         }
      }

   }

   public ObjectStreamClass getDescriptor() {
      return this.osc;
   }

   public Serializable writeReplace(Object var1) {
      ObjectStreamClass var2 = this.osc;
      Class var3 = this.theClass;

      try {
         while(var2.hasWriteReplace() && (var1 = var2.writeReplace(var1)) != null && var1.getClass() != var3) {
            var3 = var1.getClass();
            var2 = ObjectStreamClass.lookup(var3);
         }

         return (Serializable)var1;
      } catch (IOException var5) {
         throw (MARSHAL)(new MARSHAL(var5.getMessage())).initCause(var5);
      }
   }

   public Class forClass() {
      return this.theClass;
   }

   public RepositoryId getRepositoryId() {
      return this.repId;
   }

   public RepositoryId getLocalRepositoryId() {
      return this.localRepId;
   }

   public RepositoryId[] getRepositoryIdList() {
      return this.repIdList;
   }

   public static final RepositoryId getRepositoryId(String var0) {
      ClassInfo var1 = findClassInfo(new RepositoryId(var0));
      return var1.getRepositoryId();
   }

   public Class getIDLHelper() {
      return this.idlHelper;
   }

   public Method getIDLReader() {
      return this.idlReader;
   }

   public Method getIDLWriter(Class var1) {
      if (this.idlWriter == null && this.idlHelper != null) {
         this.idlWriter = this.getDeclaredMethod(this.idlHelper, "write", new Class[]{OutputStream.class, var1});
      }

      return this.idlWriter;
   }

   public boolean isAbstractInterface() {
      return this.abstractInterface;
   }

   public boolean isPortableReplaceable() {
      return this.portableReplaceable;
   }

   public boolean isIDLEntity() {
      return this.idlEntity;
   }

   public boolean isStreamable() {
      return this.streamable;
   }

   public boolean isValueBase() {
      return this.valuebase;
   }

   public boolean isString() {
      return this.string;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         try {
            ClassInfo var2 = (ClassInfo)var1;
            return var2.theClass == this.theClass && var2.repId.equals(this.repId);
         } catch (ClassCastException var3) {
            return false;
         }
      }
   }

   public int hashCode() {
      return this.repId == null ? 0 : this.repId.hashCode();
   }

   public String toString() {
      return "ClassInfo[" + this.repId.toPrettyString() + " => " + this.theClass + ", " + this.localRepId + "]";
   }

   private Method getDeclaredMethod(Class var1, String var2, Class[] var3) {
      try {
         Method var4 = var1.getDeclaredMethod(var2, var3);
         if (!var4.isAccessible()) {
            try {
               var4.setAccessible(true);
            } catch (SecurityException var6) {
            }
         }

         return var4;
      } catch (NoSuchMethodException var7) {
         return null;
      }
   }

   private static void putClassInfo(RepositoryId var0, Class var1) {
      ClassInfo var2 = new ClassInfo(var0, var1);
      var2.addToMap(var1);
      repositoryIdMap.put(var0, new ClassInfoReference(var2));
   }

   public static final ClassInfo findClassInfo(RepositoryId var0) {
      return findClassInfo(var0, (String)null);
   }

   public static final ClassInfo findClassInfo(RepositoryId var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         ClassInfoReference var2 = (ClassInfoReference)repositoryIdMap.get(var0);
         Object var3 = null;
         if (var2 != null && (var3 = var2.get()) != null) {
            return (ClassInfo)var3;
         } else {
            String var4 = Utils.getAnnotation((ClassLoader)null);
            if (var0.getAnnotation() == null && var4 != null) {
               var0 = new RepositoryId(var0, var4);
               var2 = (ClassInfoReference)repositoryIdMap.get(var0);
               Object var5 = null;
               if (var2 != null && (var5 = var2.get()) != null) {
                  ClassInfo var6 = (ClassInfo)var5;
                  if (var6.theClass == null) {
                     return var6;
                  }

                  if (var6.theClass.getClassLoader() == Thread.currentThread().getContextClassLoader()) {
                     return var6;
                  }

                  classInfoMap.remove(var6.theClass);
               }
            }

            ClassInfo var7 = new ClassInfo(var0, var1);
            repositoryIdMap.put(var0, new ClassInfoReference(var7));
            if (var7.theClass != null && classInfoMap.get(var7.theClass) == null && (var0 == var7.getLocalRepositoryId() || !var7.isValueType())) {
               var7.addToMap(var7.theClass);
            }

            return var7;
         }
      }
   }

   private final boolean isValueType() {
      if (!this.theClass.isPrimitive() && !IDLUtils.isRemote(this.theClass) && !IDLUtils.isARemote(this.theClass) && !IDLEntity.class.equals(this.theClass)) {
         if (this.theClass.isInterface() && !this.isAbstractInterface()) {
            return true;
         } else {
            return this.theClass.getComponentType() != null ? false : Serializable.class.isAssignableFrom(this.theClass);
         }
      } else {
         return false;
      }
   }

   public static final ClassInfo findClassInfo(Class var0) {
      ClassInfo var1 = null;
      ClassInfoReference var2 = (ClassInfoReference)classInfoMap.get(var0);
      Object var3 = null;
      if (var2 != null && (var3 = var2.get()) != null) {
         var1 = (ClassInfo)var3;
      }

      if (var1 == null) {
         var1 = new ClassInfo(var0);
         repositoryIdMap.put(var1.repId, new ClassInfoReference(var1));
         var1.addToMap(var0);
      }

      return var1;
   }

   private final void addToMap(Class var1) {
      classInfoMap.put(var1, new ClassInfoReference(this));
   }

   public static final void initialize(boolean var0) {
      useFullRepositoyIdList = var0;
   }

   private static void p(String var0) {
      System.out.println("<ClassInfo>: " + var0);
   }

   static {
      putClassInfo(RepositoryId.STRING, String.class);
   }

   private static class ClassInfoReference extends WeakReference {
      private int hash;

      ClassInfoReference(Object var1) {
         super(var1);
         this.hash = var1.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ClassInfoReference)) {
            return false;
         } else {
            boolean var10000;
            label31: {
               ClassInfoReference var2 = (ClassInfoReference)var1;
               Object var3 = this.get();
               Object var4 = var2.get();
               if (this.hash == var2.hash) {
                  if (var3 == null) {
                     if (var4 == null) {
                        break label31;
                     }
                  } else if (var3.equals(var4)) {
                     break label31;
                  }
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }

      public int hashCode() {
         return this.hash;
      }
   }
}
