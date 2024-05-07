package weblogic.tools.ui;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import weblogic.utils.Debug;

public class MyModel extends AbstractTableModel {
   private boolean targetIsString;
   private HashMap m_map = null;
   Object bean;
   Class bc;
   Method reader;
   Method writer;
   Class propType;
   boolean autoCommit = true;
   Object[] tmpModel;
   private static final Object[] NO_ARGS = new Object[0];

   private static void p(String var0) {
      System.err.println("[ListModel]: " + var0);
   }

   public MyModel(Class var1, Object var2, String var3) {
      this.bc = var1;
      this.bean = var2;

      try {
         this.reader = this.getMethod("get" + var3);
         this.writer = this.getMethod("set" + var3);
         Class[] var4 = this.writer.getParameterTypes();
         this.propType = var4[0].getComponentType();
         this.targetIsString = this.propType == String.class;
         if (!this.targetIsString) {
            this.m_map = new HashMap();
         }

      } catch (RuntimeException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new RuntimeException(var6.toString());
      }
   }

   public boolean isAutoCommit() {
      return this.autoCommit;
   }

   public void setAutoCommit(boolean var1) {
      if (!var1) {
         this.tmpModel = this.getStrings();
      } else {
         this.tmpModel = null;
      }

      this.autoCommit = var1;
   }

   public void modelToUI() {
      this.fireTableDataChanged();
   }

   public void uiToModel() {
      if (!this.isAutoCommit()) {
         this.invokeSetter(this.tmpModel);
      }

   }

   public Object getValueAt(int var1, int var2) {
      return var2 == 0 ? this.getElementAt(var1) : null;
   }

   public boolean isCellEditable(int var1, int var2) {
      return this.targetIsString;
   }

   public void setValueAt(Object var1, int var2, int var3) {
      if (var3 == 0) {
         Object[] var4 = this.getStrings();
         if (var2 >= 0 && var2 < var4.length) {
            var4[var2] = var1;
            this.setStrings(var4);
         }

      }
   }

   public Class getColumnClass(int var1) {
      return String.class;
   }

   public Object getElementAt(int var1) {
      Object[] var2 = this.getStrings();
      return var1 >= 0 && var1 < var2.length ? var2[var1] : null;
   }

   public int getColumnCount() {
      return 1;
   }

   public int getRowCount() {
      return this.getStrings().length;
   }

   public int getSize() {
      return this.getStrings().length;
   }

   public void swap(int var1) {
      Object[] var2 = this.getStrings();
      Object var3 = var2[var1 - 1];
      var2[var1 - 1] = var2[var1];
      var2[var1] = var3;
      this.setStrings(var2);
      this.fireTableRowsUpdated(var1 - 1, var1);
   }

   public void remove(int var1) {
      Object[] var2 = this.getStrings();
      String[] var3 = new String[var2.length - 1];
      if (var1 != 0) {
         System.arraycopy(var2, 0, var3, 0, var1);
      }

      if (var1 != var3.length) {
         System.arraycopy(var2, var1 + 1, var3, var1, var3.length - var1);
      }

      this.setStrings(var3);
      this.fireTableRowsDeleted(var1, var1);
      this.fireTableStructureChanged();
   }

   public void add(Object var1) {
      Object[] var2 = this.getStrings();
      Object[] var3 = new Object[var2.length + 1];
      System.arraycopy(var2, 0, var3, 0, var2.length);
      var3[var2.length] = var1;
      if (!this.targetIsString) {
         this.m_map.put(var1.toString(), var1);
      }

      this.setStrings(var3);
      this.fireTableRowsUpdated(var2.length, var2.length);
   }

   public void setBean(Object var1) {
      int var2 = this.getSize();
      this.fireTableRowsDeleted(0, var2);
      this.bean = var1;
      if (!this.isAutoCommit()) {
         this.tmpModel = this.invokeGetter();
      }

      var2 = this.getSize();
      this.fireTableRowsInserted(0, var2);
   }

   void setStrings(Object[] var1) {
      if (var1 == null) {
         var1 = (Object[])((Object[])Array.newInstance(this.propType, 0));
      }

      if (!this.isAutoCommit()) {
         this.tmpModel = (Object[])((Object[])var1.clone());
      } else {
         this.invokeSetter(var1);
      }
   }

   private void invokeSetter(Object[] var1) {
      if (var1 == null) {
         var1 = (Object[])((Object[])Array.newInstance(this.propType, 0));
      }

      Object[] var2 = new Object[1];
      Class var3 = null;
      if (this.targetIsString) {
         var3 = String.class;
      } else {
         if (var1.length > 0) {
            var3 = this.m_map.get(var1[0].toString()).getClass();
         }

         if (var3 == null) {
            var3 = this.propType;
         }
      }

      Object var4 = Array.newInstance(var3, var1.length);
      int var6;
      if (this.targetIsString) {
         String[] var5 = (String[])((String[])var4);

         for(var6 = 0; var6 < var1.length; ++var6) {
            var5[var6] = (String)var1[var6];
         }

         var2[0] = var5;
      } else {
         Object[] var9 = (Object[])((Object[])var4);

         for(var6 = 0; var6 < var1.length; ++var6) {
            var9[var6] = this.m_map.get(var1[var6].toString());
            Debug.assertion(null != var9[var6], "Couldn't find object " + var1[var6]);
         }

         var2[0] = var9;
      }

      try {
         this.writer.invoke(this.bean, var2);
      } catch (InvocationTargetException var7) {
         handleITE(var7);
      } catch (IllegalAccessException var8) {
         throw new RuntimeException(var8.toString());
      }

   }

   Object[] getStrings() {
      if (!this.isAutoCommit()) {
         return (Object[])((Object[])this.tmpModel.clone());
      } else {
         return (Object[])(this.bean == null ? new String[0] : this.invokeGetter());
      }
   }

   private Object[] invokeGetter() {
      try {
         Object var1 = this.reader.invoke(this.bean, NO_ARGS);
         if (var1 == null) {
            return new String[0];
         } else if (this.targetIsString) {
            String[] var7 = (String[])((String[])var1);
            return (String[])((String[])var7.clone());
         } else {
            Object[] var2 = (Object[])((Object[])var1);
            String[] var3 = new String[var2.length];

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3[var4] = var2[var4].toString();
               this.m_map.put(var3[var4].toString(), var2[var4]);
            }

            return var3;
         }
      } catch (InvocationTargetException var5) {
         handleITE(var5);
         return null;
      } catch (IllegalAccessException var6) {
         throw new RuntimeException(var6.toString());
      }
   }

   private static void handleITE(InvocationTargetException var0) {
      Throwable var1 = var0.getTargetException();
      if (var1 instanceof RuntimeException) {
         throw (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      } else {
         throw new RuntimeException(var1.toString());
      }
   }

   private Method getMethod(String var1) throws NoSuchMethodException {
      return getMethod(this.bc, var1);
   }

   public static Method getMethod(Class var0, String var1) throws NoSuchMethodException {
      Method[] var2 = var0.getDeclaredMethods();
      Class[] var3 = null;

      for(int var4 = 0; var2 != null && var4 < var2.length; ++var4) {
         if (var2[var4].getName().equalsIgnoreCase(var1)) {
            Method var5 = var2[var4];
            int var6 = var5.getModifiers();
            if ((var6 & 1) != 0) {
               var3 = var5.getParameterTypes();
               if (var1.startsWith("set")) {
                  if (var3 != null && var3.length == 1 && var3[0].isArray()) {
                     return var5;
                  }
               } else if (var3 == null || var3.length == 0 || var3.length == 1 && var3[0] == Void.TYPE) {
                  return var5;
               }
            }
         }
      }

      String var7 = "cannot find appropriate method " + var0.getName() + "." + var1 + "(" + (null != var3 ? var3.getClass().toString() : ") ");
      throw new IllegalArgumentException(var7);
   }
}
