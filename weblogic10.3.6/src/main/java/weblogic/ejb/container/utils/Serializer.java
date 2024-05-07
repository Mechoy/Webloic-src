package weblogic.ejb.container.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

public final class Serializer {
   private Serializer() {
   }

   public static void writeObject(ObjectOutputStream var0, Object var1) throws IOException, IllegalAccessException {
      for(Class var2 = var1.getClass(); !var2.equals(Object.class); var2 = var2.getSuperclass()) {
         Field[] var3 = var2.getDeclaredFields();
         Arrays.sort(var3, new Comparator<Field>() {
            public int compare(Field var1, Field var2) {
               return var1.getName().compareTo(var2.getName());
            }
         });

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4].setAccessible(true);
            if (!Modifier.isTransient(var3[var4].getModifiers())) {
               var0.writeObject(var3[var4].get(var1));
            }
         }
      }

   }

   public static void readObject(ObjectInputStream var0, Object var1) throws IOException, ClassNotFoundException, IllegalAccessException {
      for(Class var2 = var1.getClass(); !var2.equals(Object.class); var2 = var2.getSuperclass()) {
         Field[] var3 = var2.getDeclaredFields();
         Arrays.sort(var3, new Comparator<Field>() {
            public int compare(Field var1, Field var2) {
               return var1.getName().compareTo(var2.getName());
            }
         });

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4].setAccessible(true);
            if (!Modifier.isTransient(var3[var4].getModifiers())) {
               var3[var4].set(var1, var0.readObject());
            }
         }
      }

   }
}
