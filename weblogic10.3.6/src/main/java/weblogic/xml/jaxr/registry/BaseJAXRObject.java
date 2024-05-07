package weblogic.xml.jaxr.registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.UnsupportedCapabilityException;
import weblogic.xml.jaxr.registry.infomodel.RegistryObjectImpl;

public abstract class BaseJAXRObject {
   private static ThreadLocal inProcess = new ThreadLocal();

   private boolean isInProcess() {
      return (Boolean)inProcess.get();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BaseJAXRObject)) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         BaseJAXRObject var2 = (BaseJAXRObject)var1;
         Object[] var3 = this.getDefiningElements();
         if (var3 == null) {
            return super.equals(var1);
         } else {
            Object[] var4 = var2.getDefiningElements();
            boolean var5 = Arrays.equals(var3, var4);
            return var5;
         }
      }
   }

   public int hashCode() {
      Object[] var1 = this.getDefiningElements();
      if (var1 == null) {
         return super.hashCode();
      } else if (var1.length == 0) {
         return 0;
      } else {
         int var2 = 17;

         for(int var3 = 0; var3 < var1.length; ++var3) {
            Object var4 = var1[var3];
            if (var4 != null) {
               var2 = 37 * var2 + var4.hashCode();
            }
         }

         return var2;
      }
   }

   public String toString() {
      inProcess.set(Boolean.TRUE);
      Object[] var1 = this.getDefiningElements();
      if (var1 == null) {
         return super.toString();
      } else {
         String[] var2 = this.getVariableNames();
         String var3 = toStringImplementation(this, var1, var2);
         inProcess.set(Boolean.FALSE);
         return var3;
      }
   }

   protected Object[] getDefiningElements() {
      return null;
   }

   protected String[] getVariableNames() {
      return null;
   }

   protected static String toStringImplementation(Object var0, Object[] var1, String[] var2) {
      if (var1 != null && var1.length != 0) {
         boolean var3 = false;
         if (var2 != null && var2.length == var1.length) {
            var3 = true;
         }

         StringBuffer var4 = new StringBuffer();
         var4.append("\n");
         var4.append(var0.getClass().getName());
         var4.append(": \n[\n");

         for(int var5 = 0; var5 < var1.length; ++var5) {
            Object var6 = var1[var5];
            if (var3) {
               var4.append(var2[var5]);
            } else if (var6 != null) {
               var4.append(var6.getClass().getName());
            } else {
               var4.append("unknown");
            }

            var4.append(": ");
            if (var6 instanceof BaseJAXRObject && ((BaseJAXRObject)var6).isInProcess()) {
               var4.append("");
            } else {
               var4.append(var6);
            }

            var4.append("\n");
         }

         var4.append("]");
         return var4.toString();
      } else {
         return null;
      }
   }

   protected static String toString(Object[] var0) {
      String var1 = var0 == null ? "[]" : Arrays.asList(var0).toString();
      return var1;
   }

   protected static Object[] mergeObjectArrays(Object[] var0, Object[] var1) {
      if (var0 != null && var0.length != 0) {
         if (var1 != null && var1.length != 0) {
            Object[] var2 = new Object[var0.length + var1.length];
            System.arraycopy(var0, 0, var2, 0, var0.length);
            System.arraycopy(var1, 0, var2, var0.length, var1.length);
            return var2;
         } else {
            return var0;
         }
      } else {
         return var1;
      }
   }

   protected static String[] mergeStringArrays(String[] var0, String[] var1) {
      if (var0 != null && var0.length != 0) {
         if (var1 != null && var1.length != 0) {
            String[] var2 = new String[var0.length + var1.length];
            System.arraycopy(var0, 0, var2, 0, var0.length);
            System.arraycopy(var1, 0, var2, var0.length, var1.length);
            return var2;
         } else {
            return var0;
         }
      } else {
         return var1;
      }
   }

   protected String getName(Object var1) {
      String var2;
      if (var1 == null) {
         var2 = "Null";
      } else if (var1 instanceof RegistryObjectImpl) {
         RegistryObjectImpl var3 = (RegistryObjectImpl)var1;
         var2 = var3.getName().toString();
      } else {
         var2 = var1.getClass().getName();
      }

      return var2;
   }

   protected String getNames(Collection var1) {
      StringBuffer var2 = new StringBuffer();
      if (var1 == null) {
         var2.append("Null");
      } else {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            var2.append("-").append(this.getName(var3.next()));
         }
      }

      return var2.toString();
   }

   protected void checkCapability(RegistryService var1, int var2) throws JAXRException {
      if (var2 > var1.getCapabilityProfile().getCapabilityLevel()) {
         throw new UnsupportedCapabilityException();
      }
   }
}
