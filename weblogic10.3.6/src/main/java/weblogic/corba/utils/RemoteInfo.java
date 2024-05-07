package weblogic.corba.utils;

import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import weblogic.corba.rmic.StubGenerator;
import weblogic.iiop.Utils;
import weblogic.rmi.internal.DescriptorManager;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.collections.WeakConcurrentHashMap;

public final class RemoteInfo {
   private RepositoryId repId;
   private Class theClass;
   private boolean idlInterface = false;
   private RuntimeDescriptor descriptor = null;
   private static WeakConcurrentHashMap classInfoMap = new WeakConcurrentHashMap();
   private static ConcurrentHashMap repositoryIdMap = new ConcurrentHashMap();

   public RemoteInfo(RepositoryId var1) {
      this.repId = var1;
      this.theClass = Utils.getClassFromID(var1);
      this.init();
   }

   public RemoteInfo(Class var1) {
      this.theClass = var1;
      this.repId = new RepositoryId(var1);
      this.init();
   }

   private RemoteInfo(RepositoryId var1, Class var2) {
      this.repId = var1;
      this.theClass = var2;
      this.init();
   }

   private void init() {
      this.idlInterface = Utils.isIDLInterface(this.theClass);
      this.repId.setClassLoader(this.theClass.getClassLoader());

      try {
         this.descriptor = DescriptorManager.createRuntimeDescriptor(this.theClass);
         if (this.descriptor == null) {
            Class var1 = Utils.getClassFromID(this.repId);
            if (var1 != null && var1 != this.theClass) {
               this.descriptor = DescriptorManager.createRuntimeDescriptor(var1);
            }
         }

      } catch (RemoteException var2) {
         throw new AssertionError(var2);
      }
   }

   public Class getTheClass() {
      return this.theClass;
   }

   public RuntimeDescriptor getDescriptor() {
      return this.descriptor;
   }

   public RepositoryId getRepositoryId() {
      return this.repId;
   }

   public String getClassName() {
      return this.theClass.getName();
   }

   public boolean isIDLInterface() {
      return this.idlInterface;
   }

   public static final RepositoryId getRepositoryId(String var0) {
      RemoteInfo var1 = findRemoteInfo(new RepositoryId(var0));
      return var1.getRepositoryId();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         try {
            RemoteInfo var2 = (RemoteInfo)var1;
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
      return this.repId.toString();
   }

   public static final RemoteInfo findRemoteInfo(RepositoryId var0) {
      return findRemoteInfo(var0, (String)null);
   }

   public static final RemoteInfo findRemoteInfo(RepositoryId var0, String var1) {
      RemoteInfoReference var2 = (RemoteInfoReference)repositoryIdMap.get(var0);
      if (var2 == null && var0.getAnnotation() == null) {
         String var3 = Utils.getAnnotation((ClassLoader)null);
         if (var3 != null) {
            var2 = (RemoteInfoReference)repositoryIdMap.get(new RepositoryId(var0, var3));
            if (var2 != null) {
               var0.setAnnotation(var3);
            }
         }
      }

      RemoteInfo var5 = var2 != null ? (RemoteInfo)var2.get() : null;
      if (var5 == null) {
         Debug.assertion(var0 != null);
         Class var4 = Utils.getClassFromID(var0, var1);
         if (var4 == null) {
            return null;
         }

         var5 = createRemoteInfo(var0, var4);
         classInfoMap.put(var4, var5);
      }

      return var5;
   }

   public static final RemoteInfo findRemoteInfo(RepositoryId var0, Class var1) {
      RemoteInfoReference var2 = (RemoteInfoReference)repositoryIdMap.get(var0);
      RemoteInfo var3 = var2 != null ? (RemoteInfo)var2.get() : null;
      if (var3 == null) {
         var3 = createRemoteInfo(var0, var1);
      }

      return var3;
   }

   private static final RemoteInfo createRemoteInfo(RepositoryId var0, Class var1) {
      Debug.assertion(var0 != null && var1 != null);
      RemoteInfo var2 = new RemoteInfo(var0, var1);
      Class var3 = Utils.getClassFromID(var0);
      if (var3 == null || var3 == var1) {
         repositoryIdMap.put(var0, new RemoteInfoReference(var2));
      }

      return var2;
   }

   public static final RemoteInfo findRemoteInfo(Class var0) {
      RemoteInfo var1 = (RemoteInfo)classInfoMap.get(var0);
      if (var1 == null) {
         var1 = new RemoteInfo(var0);
         Class var2 = Utils.getClassFromID(var1.repId);
         if (var2 == null || var2 == var0) {
            repositoryIdMap.put(var1.repId, new RemoteInfoReference(var1));
         }

         classInfoMap.put(var0, var1);
      }

      return var1;
   }

   public static final void createStubs(Class var0) {
      Object var1 = var0.getClassLoader();
      if (!(var1 instanceof GenericClassLoader)) {
         var1 = AugmentableClassLoaderManager.getAugmentableSystemClassLoader();
      }

      createStubs(var0, (ClassLoader)var1);
   }

   public static final void createStubs(Class var0, ClassLoader var1) {
      if (!var0.isInterface()) {
         (new StubGenerator(var0)).generateClass(var1);
      }

      Class[] var2 = StubGenerator.getAllInterfaces(var0);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         StubGenerator var4 = new StubGenerator(var2[var3]);
         var4.generateClass(var1);
      }

   }

   private static class RemoteInfoReference extends WeakReference {
      private int hash;

      RemoteInfoReference(Object var1) {
         super(var1);
         this.hash = var1.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof RemoteInfoReference)) {
            return false;
         } else {
            boolean var10000;
            label31: {
               RemoteInfoReference var2 = (RemoteInfoReference)var1;
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
