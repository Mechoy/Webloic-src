package weblogic.tools.ui;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.table.AbstractTableModel;
import weblogic.servlet.jsp.BeanUtils;

public class BeanTableModel extends AbstractTableModel {
   Class beanClass;
   BeanInfo beanInfo;
   PropertyDescriptor[] propertyDescriptors;
   Object[] beans;
   String[] props;
   boolean[] editable;
   Object parentBean;
   Method parentSetter;
   Method parentAdder;
   private static final Object[] NO_ARGS = new Object[0];

   public BeanTableModel(Class var1, BeanInfo var2, PropertyDescriptor[] var3, Object[] var4, String[] var5, String[] var6) {
      this.beanClass = var1;
      this.beanInfo = var2;
      this.propertyDescriptors = var3;
      this.beans = var4;
      this.props = var5;
      this.editable = new boolean[var5.length];

      for(int var7 = 0; var7 < var5.length; ++var7) {
         if (!BeanUtils.isStringConvertible(var3[var7].getPropertyType())) {
            this.editable[var7] = false;
         } else {
            this.editable[var7] = true;
         }

         this.editable[var7] = true;
      }

   }

   public void setEditable(boolean var1) {
      for(int var2 = 0; var2 < this.editable.length; ++var2) {
         this.editable[var2] = var1;
      }

   }

   static void p(String var0) {
      System.err.println("[BGModel]: " + var0);
   }

   private void setParentInfo(Object var1, String var2, boolean var3) {
      if (var1 == null) {
         this.parentSetter = null;
         this.parentAdder = null;
         this.parentBean = null;
      } else {
         RuntimeException var4 = null;
         Class var5 = var1.getClass();

         while(var5 != Object.class) {
            try {
               if (var3) {
                  this.parentAdder = this.findAdder(var5, var2);
               } else {
                  this.parentAdder = null;
               }

               this.parentSetter = this.findSetter(var5, var2);
               this.parentBean = var1;
               return;
            } catch (RuntimeException var7) {
               if (var4 == null) {
                  var4 = var7;
               }

               var5 = var5.getSuperclass();
            }
         }

         if (var4 == null) {
            var4 = new RuntimeException("Couldn't find " + var5.getClass() + " .set" + var2 + "(" + this.beans.getClass().getComponentType() + ")");
         }

         throw var4;
      }
   }

   public void setParentInfo(Object var1, String var2) {
      this.setParentInfo(var1, var2, false);
   }

   public void setParentAdder(Object var1, String var2) {
      this.setParentInfo(var1, var2, true);
   }

   private Method findAdder(Class var1, String var2) {
      Method var3 = null;
      int var4 = var2.length();
      if (null != var2 && 's' == var2.charAt(var4 - 1)) {
         var2 = var2.substring(0, var4 - 1);
      }

      String var5 = "add" + var2;

      try {
         Method[] var6 = var1.getDeclaredMethods();

         for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
            if (var6[var7].getName().equalsIgnoreCase(var5)) {
               Method var8 = var6[var7];
               int var9 = var8.getModifiers();
               if ((var9 & 1) != 0) {
                  Class[] var10 = var8.getParameterTypes();
                  if (var10 != null && var10.length == 1) {
                     Class var11 = var10[0];
                     if (var11.isAssignableFrom(this.beanClass)) {
                        var3 = var8;
                     }
                  }
               }
            }
         }

         String var13 = "Couldn't find " + var1.getName() + "." + var5 + "(" + this.beanClass.getName() + "[])";
         return var3;
      } catch (Exception var12) {
         if (var12 instanceof RuntimeException) {
            throw (RuntimeException)var12;
         } else {
            throw new RuntimeException("nested: " + var12);
         }
      }
   }

   private Method findSetter(Class var1, String var2) {
      String var3 = "set" + var2;

      try {
         Method[] var4 = var1.getDeclaredMethods();

         for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
            if (var4[var5].getName().equalsIgnoreCase(var3)) {
               Method var6 = var4[var5];
               int var7 = var6.getModifiers();
               if ((var7 & 1) != 0) {
                  Class[] var8 = var6.getParameterTypes();
                  if (var8 != null && var8.length == 1 && var8[0].isArray()) {
                     Class var9 = var8[0].getComponentType();
                     if (var9.isAssignableFrom(this.beanClass)) {
                        return var6;
                     }
                  }
               }
            }
         }

         String var11 = "Couldn't find " + var1.getName() + "." + var3 + "(" + this.beanClass.getName() + "[])";
         throw new RuntimeException(var11);
      } catch (Exception var10) {
         if (var10 instanceof RuntimeException) {
            throw (RuntimeException)var10;
         } else {
            throw new RuntimeException("nested: " + var10);
         }
      }
   }

   public Object[] getBeans() {
      return this.beans;
   }

   public void setBeans(Object[] var1) {
      this.beans = var1;
   }

   private PropertyDescriptor getPD(String var1) {
      int var2;
      for(var2 = 0; var2 < this.propertyDescriptors.length; ++var2) {
         if (this.propertyDescriptors[var2].getName().equals(var1)) {
            return this.propertyDescriptors[var2];
         }
      }

      for(var2 = 0; var2 < this.propertyDescriptors.length; ++var2) {
         if (this.propertyDescriptors[var2].getName().equalsIgnoreCase(var1)) {
            return this.propertyDescriptors[var2];
         }
      }

      return null;
   }

   public Class getColumnClass(int var1) {
      PropertyDescriptor var2 = this.getPD(this.props[var1]);
      if (var2 == null) {
         return Object.class;
      } else {
         Class var3 = var2.getPropertyType();
         if (var3 != Boolean.class && var3 != Boolean.TYPE) {
            return !var3.isPrimitive() && !Number.class.isAssignableFrom(var3) ? Object.class : Number.class;
         } else {
            return Boolean.class;
         }
      }
   }

   public void setEditable(int var1, boolean var2) {
      if (var2) {
         PropertyDescriptor var3 = this.getPD(this.props[var1]);
         if (var3.getWriteMethod() == null) {
            String var4 = "property " + this.props[var1] + " on " + this.beanClass.getName() + " cannot be editable, it has no write method";
            throw new RuntimeException(var4);
         }
      }

      this.editable[var1] = var2;
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

   public boolean isCellEditable(int var1, int var2) {
      return this.editable[var2];
   }

   public int getColumnCount() {
      return this.props.length;
   }

   public void setValueAt(Object var1, int var2, int var3) {
      if (null != var1) {
         p("setValueAt(" + var2 + "," + var3 + ")->\"" + var1 + "\"(" + var1.getClass().getName() + ")");
         PropertyDescriptor var4 = this.getPD(this.props[var3]);
         Method var5 = var4.getWriteMethod();
         if (null == var5) {
            throw new RuntimeException("Couldn't find a setter for " + var4.getReadMethod().getName());
         } else {
            Object var6 = null;
            if (!BeanUtils.isStringConvertible(var4.getPropertyType())) {
               var6 = var1;
            } else {
               String var7 = var1.toString();
               var6 = this.convertArg(var7, var4.getPropertyType());
            }

            try {
               Object[] var10 = new Object[]{var6};
               var5.invoke(this.beans[var2], var10);
            } catch (InvocationTargetException var8) {
               handleITE(var8);
            } catch (IllegalAccessException var9) {
               var9.printStackTrace();
               throw new RuntimeException(var9.toString());
            }

         }
      }
   }

   public Object getValueAt(int var1, int var2) {
      if (var1 >= this.getRowCount()) {
         return null;
      } else if (var2 >= this.props.length) {
         return null;
      } else {
         try {
            PropertyDescriptor var3 = this.getPD(this.props[var2]);
            if (var3 == null) {
               String var8 = "bean class " + this.beanClass.getName() + " has no property " + this.props[var2];
               throw new RuntimeException(var8);
            } else {
               Method var4 = var3.getReadMethod();
               Object var5 = var4.invoke(this.beans[var1], NO_ARGS);
               if (var3.getPropertyType().isArray() && var3.getPropertyType().getComponentType() == String.class) {
                  var5 = this.makeStringList((String[])((String[])var5));
               }

               return var5;
            }
         } catch (InvocationTargetException var6) {
            handleITE(var6);
            return null;
         } catch (IllegalAccessException var7) {
            throw new RuntimeException(var7.toString());
         }
      }
   }

   public int getRowCount() {
      return this.beans.length;
   }

   public void removeRow(int var1) {
      Class var2 = this.beans.getClass().getComponentType();
      Object[] var3 = (Object[])((Object[])Array.newInstance(var2, this.beans.length - 1));
      System.arraycopy(this.beans, 0, var3, 0, var1);
      System.arraycopy(this.beans, var1 + 1, var3, var1, this.beans.length - var1 - 1);
      this.beans = var3;
      this.invokeParentSetter();
      this.fireTableRowsDeleted(var1, var1);
   }

   public void addRow(Object var1) {
      Class var2 = this.beans.getClass().getComponentType();
      Object[] var3 = (Object[])((Object[])Array.newInstance(var2, this.beans.length + 1));

      for(int var4 = 0; var4 < this.beans.length; ++var4) {
         var3[var4] = this.beans[var4];
      }

      try {
         var3[this.beans.length] = var1;
      } catch (ArrayStoreException var5) {
         throw new ArrayStoreException("Make sure the beans stored in the table and the type  returned by createNewBean() match.");
      }

      this.beans = var3;
      if (this.parentAdder != null) {
         this.invokeParentAdder(var1);
      } else {
         this.invokeParentSetter();
      }

      this.fireTableRowsInserted(this.beans.length - 1, this.beans.length - 1);
   }

   private String makeStringList(String[] var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var1 != null && var3 < var1.length; ++var3) {
         if (var1[var3] != null) {
            var2.append(var1[var3]);
         }

         if (var3 < var1.length - 1) {
            var2.append(',');
         }
      }

      return var2.toString();
   }

   private void invokeParentSetter() {
      Object[] var1 = new Object[]{this.beans};
      this.invokeMethod(this.parentSetter, var1);
   }

   private void invokeParentAdder(Object var1) {
      Object[] var2 = new Object[]{var1};
      if (this.parentAdder != null) {
         this.invokeMethod(this.parentAdder, var2);
      }

   }

   private void invokeMethod(Method var1, Object[] var2) {
      if (this.parentBean != null) {
         try {
            var1.invoke(this.parentBean, var2);
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4.toString());
         } catch (InvocationTargetException var5) {
            handleITE(var5);
         }

      }
   }

   private Object convertArg(String var1, Class var2) {
      if (var2 == String.class) {
         return var1;
      } else if (var2 != Boolean.class && var2 != Boolean.TYPE) {
         if (var2 != Byte.class && var2 != Byte.TYPE) {
            if (var2 != Double.class && var2 != Double.TYPE) {
               if (var2 != Integer.class && var2 != Integer.TYPE) {
                  if (var2 != Float.class && var2 != Float.TYPE) {
                     if (var2 != Long.class && var2 != Long.TYPE) {
                        if (var2 != Character.class && var2 != Character.TYPE) {
                           throw new IllegalArgumentException("cannot convert String to " + var2.getName());
                        } else {
                           return new Character(var1.charAt(0));
                        }
                     } else {
                        return Long.valueOf(var1.trim());
                     }
                  } else {
                     return Float.valueOf(var1.trim());
                  }
               } else {
                  return Integer.valueOf(var1.trim());
               }
            } else {
               return Double.valueOf(var1.trim());
            }
         } else {
            return Byte.valueOf(var1.trim());
         }
      } else {
         return Boolean.valueOf(var1);
      }
   }

   private static void ppp(String var0) {
      System.out.println("[BeanTableModel] " + var0);
   }
}
