package weblogic.ejb.container.swap;

import com.oracle.pitchfork.interfaces.EnterpriseBeanProxy;
import com.oracle.pitchfork.spi.TargetWrapperImpl;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.utils.Debug;

public class BeanStateDiffChecker {
   private static final boolean debug = System.getProperty("weblogic.ejb.swap.debug") != null;
   private HashMap fields = new HashMap();

   public BeanStateDiffChecker(Class var1) throws SecurityException {
      ArrayList var2;
      for(var2 = new ArrayList(); var1 != null; var1 = var1.getSuperclass()) {
         var2.add(var1);
      }

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Field[] var4 = ((Class)var2.get(var3)).getDeclaredFields();
         AccessibleObject.setAccessible(var4, true);

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (!Modifier.isTransient(var4[var5].getModifiers())) {
               Class var6 = var4[var5].getType();
               if (this.isValidClassForReplication(var6)) {
                  if (debug) {
                     Debug.say("Adding field " + var4[var5].getName() + " of type " + var6.getName() + " to fields map");
                  }

                  this.fields.put(new Key(var4[var5]), var4[var5]);
               } else if (debug) {
                  Debug.say("The field " + var4[var5].getName() + " of type " + var6.getName() + " is not Serializable. It will not be replicated");
               }
            }
         }
      }

   }

   private boolean isValidClassForReplication(Class var1) {
      return Serializable.class.isAssignableFrom(var1) || var1.isPrimitive() || Map.class.isAssignableFrom(var1) || Set.class.isAssignableFrom(var1) || List.class.isAssignableFrom(var1);
   }

   public ArrayList calculateDiff(Object var1, BeanState var2) throws IllegalAccessException, InstantiationException {
      if (var1 instanceof EnterpriseBeanProxy) {
         var1 = ((EnterpriseBeanProxy)var1).getTarget();
         if (var1 instanceof TargetWrapperImpl) {
            var1 = ((TargetWrapperImpl)var1).getBeanTarget();
         }
      }

      ArrayList var3 = new ArrayList();
      Iterator var4 = this.fields.values().iterator();

      while(true) {
         while(var4.hasNext()) {
            Field var5 = (Field)var4.next();
            Object var6 = var2.get(var5);
            Object var7 = var5.get(var1);
            if (var6 != null && var7 == null) {
               var3.add(new Diff(var5, (Object)null));
               var2.update(var5, (Object)null);
            } else {
               Diff var8 = null;
               if (Map.class.isAssignableFrom(var5.getType())) {
                  var8 = BeanStateDiffChecker.MapDiff.calculateDiff(var7, var2, var5);
               } else if (Set.class.isAssignableFrom(var5.getType())) {
                  var8 = BeanStateDiffChecker.SetDiff.calculateDiff(var7, var2, var5);
               } else if (List.class.isAssignableFrom(var5.getType())) {
                  var8 = BeanStateDiffChecker.ListDiff.calculateDiff(var7, var2, var5);
               } else if (!eq(var6, var7)) {
                  var8 = new Diff(var5, var7);
                  var2.update(var5, var7);
               }

               if (var8 != null && !var8.isEmpty()) {
                  var3.add(var8);
               }
            }
         }

         if (var3.isEmpty()) {
            return null;
         }

         return var3;
      }
   }

   public void mergeDiff(BeanState var1, ArrayList var2) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      Key var3 = new Key();

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         Diff var5 = (Diff)var2.get(var4);
         var3.set(var5.getClassName(), var5.getFieldName());
         Field var6 = (Field)this.fields.get(var3);
         Object var7 = var1.get(var6);
         String var8 = var5.getFieldType();
         if (var8 == null) {
            var1.update(var6, (Object)null);
         } else {
            var5.merge(var1, var7, var6);
         }
      }

   }

   public void setState(Object var1, BeanState var2) throws IllegalAccessException {
      Iterator var3 = this.fields.values().iterator();

      while(var3.hasNext()) {
         Field var4 = (Field)var3.next();
         Object var5 = var2.get(var4);
         var4.set(var1, var5);
      }

   }

   private static boolean eq(Object var0, Object var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else {
         return var0 != null ? var0.equals(var1) : false;
      }
   }

   public static void main(String[] var0) {
      try {
         TestClass var1 = new TestClass();
         BeanStateDiffChecker var2 = new BeanStateDiffChecker(var1.getClass());
         BeanState var3 = new BeanState();
         ArrayList var4 = var2.calculateDiff(var1, var3);
         System.out.println("Diff:");

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            System.out.println(var4.get(var5));
         }

         System.out.println("Source state:");
         System.out.println(var3);
         BeanState var8 = new BeanState();
         var2.mergeDiff(var8, var4);
         System.out.println("Destination state:");
         System.out.println(var8);
         var1.update();
         System.out.println("************bean updated**************");
         var4 = var2.calculateDiff(var1, var3);

         for(int var6 = 0; var6 < var4.size(); ++var6) {
            System.out.println(var4.get(var6));
         }

         System.out.println("Source state:");
         System.out.println(var3);
         var2.mergeDiff(var8, var4);
         System.out.println("Destination state:");
         System.out.println(var3);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   static class MyTest {
      String firstName = "UnknownFN";
      String lastName = "UnknownLN";
      int id = 1000;

      public MyTest() {
      }

      public void setFirstName(String var1) {
         this.firstName = var1;
      }

      public void setLastName(String var1) {
         this.lastName = var1;
      }

      public void setId(int var1) {
         this.id = var1;
      }

      public String toString() {
         return this.firstName + " " + this.lastName + " - " + this.id;
      }
   }

   private static class TestClass {
      private int iprim = 0;
      private String string = "string";
      private Integer iobj = new Integer(500);
      private Map map = new HashMap();
      private String string2;
      private Number d;
      private Set set;
      private List list;
      private MyTest test;

      public TestClass() {
         this.map.put("update", "new");
         this.map.put("remove", "remove");
         this.set = new HashSet();
         this.set.add("FirstOneInSet");
         this.set.add("SecondOneInSet");
         this.list = new ArrayList();
         this.list.add("FirstOneInList");
         this.list.add("SecondOneInList");
         this.string2 = "string2";
         this.d = null;
         this.test = new MyTest();
      }

      public void update() {
         this.iprim = 100;
         this.string = "newstring";
         this.map.put("new", "new");
         this.map.put("update", "updated");
         this.map.remove("remove");
         this.set.remove("FirstOneInSet");
         this.set.remove("SecondOneInSet");
         this.list.remove("SecondOneInList");
         this.list.remove("FirstOneInList");
         this.list = null;
         this.string2 = null;
         this.d = new Double(100.056);
         this.test.setId(2000);
         this.test.setFirstName("MyFirstName");
         this.test.setLastName("MyLastName");
         this.test = null;
      }
   }

   private static class Key {
      private String className;
      private String fieldName;

      public Key() {
      }

      public Key(Field var1) {
         this.className = var1.getDeclaringClass().getName();
         this.fieldName = var1.getName();
      }

      public void set(String var1, String var2) {
         this.className = var1;
         this.fieldName = var2;
      }

      public int hashCode() {
         return this.className.hashCode() ^ this.fieldName.hashCode();
      }

      public boolean equals(Object var1) {
         Key var2 = (Key)var1;
         return var2.className.equals(this.className) && var2.fieldName.equals(this.fieldName);
      }
   }

   private static class ListDiff extends Diff implements Serializable {
      private int size;
      private HashMap modifiedEntries;

      public ListDiff(Field var1, String var2) {
         super(var1);
         this.setFieldType(var2);
      }

      public int getSize() {
         return this.size;
      }

      public HashMap getModifiedEntries() {
         return this.modifiedEntries;
      }

      public void addModified(int var1, Object var2) {
         if (this.modifiedEntries == null) {
            this.modifiedEntries = new HashMap();
         }

         this.modifiedEntries.put(new Integer(var1), var2);
      }

      public void setSize(int var1) {
         this.size = var1;
      }

      public void addAll(List var1) {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            this.addModified(var2, var1.get(var2));
         }

      }

      public boolean isEmpty() {
         return this.modifiedEntries == null || this.size == 0;
      }

      protected void merge(BeanState var1, Object var2, Field var3) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
         if (var2 == null) {
            var2 = Class.forName(this.getFieldType()).newInstance();
            var1.update(var3, var2);
         }

         List var4 = (List)var2;
         if (this.modifiedEntries != null) {
            Set var5 = this.modifiedEntries.entrySet();

            int var8;
            Object var9;
            for(Iterator var6 = var5.iterator(); var6.hasNext(); var4.set(var8, var9)) {
               Map.Entry var7 = (Map.Entry)var6.next();
               var8 = (Integer)var7.getKey();
               var9 = var7.getValue();
               int var10 = var4.size();
               if (var10 < var8 + 1) {
                  for(int var11 = var10; var11 <= var8; ++var11) {
                     var4.add((Object)null);
                  }
               }
            }
         }

         for(int var12 = var4.size(); var12 > this.size; --var12) {
            var4.remove(var12 - 1);
         }

      }

      static Diff calculateDiff(Object var0, BeanState var1, Field var2) throws InstantiationException, IllegalAccessException {
         Object var3 = var1.get(var2);
         ListDiff var4 = null;
         if (var3 == null && var0 != null) {
            int var5 = ((List)var0).size();
            var3 = var0.getClass().newInstance();
            var1.update(var2, var3);
            ListDiff var6 = new ListDiff(var2, var0.getClass().getName());
            var6.addAll((List)var0);
            var6.setSize(var5);
            ((List)var3).addAll((List)var0);
            var4 = var6;
         } else if (var3 != null && var0 != null) {
            var4 = updateFieldStateForList(var2, (List)var3, (List)var0);
         }

         return var4;
      }

      private static ListDiff updateFieldStateForList(Field var0, List var1, List var2) {
         ListDiff var3 = new ListDiff(var0, var2.getClass().getName());
         var3.setSize(var2.size());
         if (var1.isEmpty()) {
            var3.addAll(var2);
            var1.addAll(var2);
            return var3;
         } else if (var2.isEmpty()) {
            var1.clear();
            return var3;
         } else {
            int var4 = var2.size();
            boolean var5 = false;
            if (var1.size() < var4) {
               var4 = var1.size();
               var5 = true;
            }

            int var6;
            for(var6 = 0; var6 < var4; ++var6) {
               if (!BeanStateDiffChecker.eq(var1.get(var6), var2.get(var6))) {
                  var3.addModified(var6, var2.get(var6));
                  var1.set(var6, var2.get(var6));
               }
            }

            if (var5) {
               for(var6 = var4; var6 < var2.size(); ++var6) {
                  var3.addModified(var6, var2.get(var6));
                  var1.add(var6, var2.get(var6));
               }
            }

            for(var6 = var1.size() - var2.size(); var6 > 0; --var6) {
               var1.remove(var1.size() - 1);
            }

            return var3;
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Field: ").append(this.getClassName());
         var1.append(".").append(this.getFieldName()).append("\n");
         var1.append("Size : ").append(this.size).append("\n");
         var1.append("Type:  ").append(this.getFieldType()).append("\n");
         if (this.modifiedEntries != null && this.size > 0) {
            var1.append("New: ");
            Set var2 = this.modifiedEntries.entrySet();
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Map.Entry var4 = (Map.Entry)var3.next();
               var1.append(var4.getKey() + " : " + var4.getValue() + "\n");
            }
         }

         return var1.toString();
      }
   }

   private static class SetDiff extends Diff implements Serializable {
      private ArrayList newEntries;
      private ArrayList removedEntries;

      public SetDiff(Field var1, String var2) {
         super(var1);
         this.setFieldType(var2);
      }

      public ArrayList getNewEntries() {
         return this.newEntries;
      }

      public ArrayList getRemovedEntries() {
         return this.removedEntries;
      }

      public void addNew(Object var1) {
         if (this.newEntries == null) {
            this.newEntries = new ArrayList();
         }

         this.newEntries.add(var1);
      }

      public void addRemoved(Object var1) {
         if (this.removedEntries == null) {
            this.removedEntries = new ArrayList();
         }

         this.removedEntries.add(var1);
      }

      public void addAll(Set var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.addNew(var2.next());
         }

      }

      public void removeAll(Set var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.addRemoved(var2.next());
         }

      }

      public boolean isEmpty() {
         return (this.newEntries == null || this.newEntries.isEmpty()) && (this.removedEntries == null || this.removedEntries.isEmpty());
      }

      protected void merge(BeanState var1, Object var2, Field var3) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
         if (var2 == null) {
            var2 = Class.forName(this.getFieldType()).newInstance();
            var1.update(var3, var2);
         }

         Set var4 = (Set)var2;
         int var5;
         if (this.newEntries != null) {
            for(var5 = 0; var5 < this.newEntries.size(); ++var5) {
               Object var6 = this.newEntries.get(var5);
               var4.add(var6);
            }
         }

         if (this.removedEntries != null) {
            for(var5 = 0; var5 < this.removedEntries.size(); ++var5) {
               var4.remove(this.removedEntries.get(var5));
            }
         }

      }

      static Diff calculateDiff(Object var0, BeanState var1, Field var2) throws InstantiationException, IllegalAccessException {
         Object var3 = var1.get(var2);
         SetDiff var4 = null;
         if (var3 == null && var0 != null) {
            var3 = var0.getClass().newInstance();
            var1.update(var2, var3);
            SetDiff var5 = new SetDiff(var2, var0.getClass().getName());
            var5.addAll((Set)var0);
            ((Set)var3).addAll((Set)var0);
            var4 = var5;
         } else if (var3 != null && var0 != null) {
            var4 = updateFieldStateForSet(var2, (Set)var3, (Set)var0);
         }

         return var4;
      }

      private static SetDiff updateFieldStateForSet(Field var0, Set var1, Set var2) {
         SetDiff var3 = new SetDiff(var0, var2.getClass().getName());
         if (var1.isEmpty()) {
            var3.addAll(var2);
            var1.addAll(var2);
            return var3;
         } else if (var2.isEmpty()) {
            var3.removeAll(var1);
            var1.clear();
            return var3;
         } else {
            Iterator var4 = var1.iterator();

            Object var5;
            while(var4.hasNext()) {
               var5 = var4.next();
               if (!var2.contains(var5)) {
                  var3.addRemoved(var5);
                  var4.remove();
               }
            }

            var4 = var2.iterator();

            while(var4.hasNext()) {
               var5 = var4.next();
               if (!var1.contains(var5)) {
                  var3.addNew(var5);
                  var1.add(var5);
               }
            }

            return var3;
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Field: ").append(this.getClassName());
         var1.append(".").append(this.getFieldName()).append("\n");
         var1.append("Type:  ").append(this.getFieldType()).append("\n");
         int var2;
         if (this.newEntries != null && !this.newEntries.isEmpty()) {
            var1.append("New: ");

            for(var2 = 0; var2 < this.newEntries.size(); ++var2) {
               Object var3 = this.newEntries.get(var2);
               var1.append(var3).append("\n");
            }
         }

         if (this.removedEntries != null && !this.removedEntries.isEmpty()) {
            var1.append("Removed: ");

            for(var2 = 0; var2 < this.removedEntries.size(); ++var2) {
               var1.append(this.removedEntries.get(var2)).append("\n");
            }
         }

         return var1.toString();
      }
   }

   private static class MapDiff extends Diff implements Serializable {
      private ArrayList newEntries = new ArrayList();
      private ArrayList removedEntries;

      public MapDiff(Field var1, String var2) {
         super(var1);
         this.setFieldType(var2);
      }

      public ArrayList getNewEntries() {
         return this.newEntries;
      }

      public ArrayList getRemovedEntries() {
         return this.removedEntries;
      }

      public void addNew(Object var1, Object var2) {
         if (this.newEntries == null) {
            this.newEntries = new ArrayList();
         }

         this.newEntries.add(new Object[]{var1, var2});
      }

      public void addRemoved(Object var1) {
         if (this.removedEntries == null) {
            this.removedEntries = new ArrayList();
         }

         this.removedEntries.add(var1);
      }

      public void addAll(Map var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.addNew(var3.getKey(), var3.getValue());
         }

      }

      public void removeAll(Map var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.addRemoved(var3.getKey());
         }

      }

      public boolean isEmpty() {
         return (this.newEntries == null || this.newEntries.isEmpty()) && (this.removedEntries == null || this.removedEntries.isEmpty());
      }

      protected void merge(BeanState var1, Object var2, Field var3) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
         if (var2 == null) {
            var2 = Class.forName(this.getFieldType()).newInstance();
            var1.update(var3, var2);
         }

         Map var4 = (Map)var2;
         int var5;
         if (this.newEntries != null) {
            for(var5 = 0; var5 < this.newEntries.size(); ++var5) {
               Object[] var6 = (Object[])((Object[])this.newEntries.get(var5));
               var4.put(var6[0], var6[1]);
            }
         }

         if (this.removedEntries != null) {
            for(var5 = 0; var5 < this.removedEntries.size(); ++var5) {
               var4.remove(this.removedEntries.get(var5));
            }
         }

      }

      static Diff calculateDiff(Object var0, BeanState var1, Field var2) throws IllegalAccessException, InstantiationException {
         Object var3 = var1.get(var2);
         MapDiff var4 = null;
         if (var3 == null && var0 != null) {
            var3 = var0.getClass().newInstance();
            var1.update(var2, var3);
            MapDiff var5 = new MapDiff(var2, var0.getClass().getName());
            var5.addAll((Map)var0);
            ((Map)var3).putAll((Map)var0);
            var4 = var5;
         } else if (var3 != null && var0 != null) {
            var4 = updateFieldStateForMap(var2, (Map)var3, (Map)var0);
         }

         return var4;
      }

      private static MapDiff updateFieldStateForMap(Field var0, Map var1, Map var2) {
         MapDiff var3 = new MapDiff(var0, var2.getClass().getName());
         if (var1.isEmpty()) {
            var3.addAll(var2);
            var1.putAll(var2);
            return var3;
         } else if (var2.isEmpty()) {
            var3.removeAll(var1);
            var1.clear();
            return var3;
         } else {
            Iterator var4 = var1.entrySet().iterator();

            Map.Entry var5;
            Object var6;
            while(var4.hasNext()) {
               var5 = (Map.Entry)var4.next();
               var6 = var5.getKey();
               if (!var2.containsKey(var6)) {
                  var3.addRemoved(var6);
                  var4.remove();
               }
            }

            var4 = var2.entrySet().iterator();

            while(true) {
               Object var7;
               do {
                  if (!var4.hasNext()) {
                     return var3;
                  }

                  var5 = (Map.Entry)var4.next();
                  var6 = var5.getKey();
                  var7 = var5.getValue();
               } while(var1.containsKey(var6) && BeanStateDiffChecker.eq(var7, var1.get(var6)));

               var3.addNew(var5.getKey(), var5.getValue());
               var1.put(var5.getKey(), var5.getValue());
            }
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Field: ").append(this.getClassName());
         var1.append(".").append(this.getFieldName()).append("\n");
         var1.append("Type:  ").append(this.getFieldType()).append("\n");
         int var2;
         if (this.newEntries != null && !this.newEntries.isEmpty()) {
            var1.append("New: ");

            for(var2 = 0; var2 < this.newEntries.size(); ++var2) {
               Object[] var3 = (Object[])((Object[])this.newEntries.get(var2));
               var1.append(var3[0]).append("-->").append(var3[1]).append("\n");
            }
         }

         if (this.removedEntries != null && !this.removedEntries.isEmpty()) {
            var1.append("Removed: ");

            for(var2 = 0; var2 < this.removedEntries.size(); ++var2) {
               var1.append(this.removedEntries.get(var2)).append("\n");
            }
         }

         return var1.toString();
      }
   }

   private static class Diff implements Serializable {
      private String className;
      private String fieldName;
      private String fieldType;
      private Object value;

      protected Diff(Field var1) {
         this.className = var1.getDeclaringClass().getName();
         this.fieldName = var1.getName();
      }

      public Diff(Field var1, Object var2) {
         this(var1);
         this.value = var2;
         if (var2 != null) {
            this.setFieldType(var2.getClass().getName());
         }

      }

      protected void setFieldType(String var1) {
         this.fieldType = var1;
      }

      protected void merge(BeanState var1, Object var2, Field var3) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
         var1.update(var3, this.value);
      }

      public Object get() {
         return this.value;
      }

      public String getClassName() {
         return this.className;
      }

      public String getFieldName() {
         return this.fieldName;
      }

      public String getFieldType() {
         return this.fieldType;
      }

      public boolean isEmpty() {
         return false;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Field: ").append(this.getClassName());
         var1.append(".").append(this.getFieldName()).append("\n");
         var1.append("Type:  ").append(this.getFieldType()).append("\n");
         var1.append("Value: ").append(this.get()).append("\n");
         return var1.toString();
      }
   }
}
