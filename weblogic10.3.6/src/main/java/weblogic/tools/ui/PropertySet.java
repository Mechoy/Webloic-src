package weblogic.tools.ui;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PropertySet {
   protected Object bean;
   protected Property[] props;
   protected Class bc;

   public PropertySet(Class var1, Object[][] var2) {
      this((Object)null, var1, var2);
   }

   public PropertySet(Class var1, PropertyInfo[] var2) {
      this.bc = var1;
      this.props = this.parseInfo(var1, var2);
   }

   public PropertySet(Object var1, Class var2, Object[][] var3) {
      this.bean = var1;
      this.bc = var2;
      this.props = this.parseData(var2, var1, var3);
   }

   public PropertySet(Class var1, Property[] var2) {
      this.bc = var1;
      this.props = (Property[])((Property[])var2.clone());
   }

   public void setAutoCommit(boolean var1) {
      for(int var2 = 0; var2 < this.props.length; ++var2) {
         this.props[var2].setAutoCommit(var1);
      }

   }

   public StringProperty findString(String var1) {
      Property var2 = this.findPropByName(var1);
      if (var2 == null) {
         throw new RuntimeException("no such property '" + var1 + "' on " + this.bc.getName());
      } else {
         return (StringProperty)var2;
      }
   }

   public Property findPropByName(String var1) {
      for(int var2 = 0; this.props != null && var2 < this.props.length; ++var2) {
         if (var1.equalsIgnoreCase(this.props[var2].pd.getName())) {
            return this.props[var2];
         }
      }

      return null;
   }

   public Property[] getProps() {
      return this.props;
   }

   public void setBean(Object var1) {
      this.bean = var1;
      if (this.bean != null) {
         for(int var2 = 0; var2 < this.props.length; ++var2) {
            this.props[var2].setBean(var1);
         }

         this.modelToUI();
      }

   }

   public void modelToUI() {
      for(int var1 = 0; var1 < this.props.length; ++var1) {
         this.props[var1].modelToUI();
      }

   }

   public Object createNewBean() {
      if (this.bc.isInterface()) {
         throw new RuntimeException("cannot instantiate " + this.bc.getName() + " (it is an interface)");
      } else {
         int var1 = this.bc.getModifiers();
         if ((var1 & 1) == 0) {
            throw new RuntimeException("cannot instantiate " + this.bc.getName() + " (not public class)");
         } else if ((var1 & 1024) != 0) {
            throw new RuntimeException("cannot instantiate " + this.bc.getName() + " (it is abstract class)");
         } else {
            try {
               Class[] var2 = new Class[0];
               Constructor var3 = this.bc.getDeclaredConstructor(var2);
               return var3 != null ? var3.newInstance() : null;
            } catch (InvocationTargetException var4) {
               Property.handleITE(var4);
               return null;
            } catch (Exception var5) {
               var5.printStackTrace();
               throw new RuntimeException("cannot instantiate " + this.bc.getName() + "(it has no public default constructor)");
            }
         }
      }
   }

   public void uiToModel() {
      for(int var1 = 0; var1 < this.props.length; ++var1) {
         this.props[var1].uiToModel();
      }

   }

   private Property[] parseInfo(Class var1, PropertyInfo[] var2) {
      BeanInfo var3 = null;
      PropertyDescriptor[] var4 = null;

      try {
         var3 = Introspector.getBeanInfo(var1);
         var4 = var3.getPropertyDescriptors();
      } catch (RuntimeException var15) {
         throw var15;
      } catch (Exception var16) {
         throw new RuntimeException(var16.toString());
      }

      Property[] var5 = new Property[var2.length];

      for(int var6 = 0; var6 < var2.length; ++var6) {
         PropertyInfo var7 = var2[var6];
         String var8 = var7.getName();
         String var9 = var7.getLabel();
         String var10 = var7.getTooltip();
         boolean var11 = var7.isRequired();
         PropertyDescriptor var12 = Property.getPD(var4, var8, var1);
         Class var13 = var12.getPropertyType();
         if (var7.getConstrainedObjects() != null) {
            ObjectProperty var19 = new ObjectProperty(var12, var9, var7.getConstrainedObjects(), var11);
            var19.setAllowNull(var7.getAllowNullObject());
            var5[var6] = var19;
         } else if (var7.getConstrainedStrings() != null) {
            ListProperty var18 = new ListProperty(var12, var9, var7.getConstrainedStrings(), var11);
            var18.setAllowEditing(var7.getAllowListEditing());
            var18.setSelectFirstElement(var7.getSelectFirstListElement());
            var5[var6] = var18;
         } else if (var13 != Boolean.TYPE && var13 != Boolean.class) {
            if (!var13.isPrimitive() && !Number.class.isAssignableFrom(var13)) {
               StringProperty var17 = new StringProperty(var12, var9, var11);
               var17.setEmptyIsNull(var7.isEmptyStringNull());
               var5[var6] = var17;
            } else {
               NumberProperty var14 = new NumberProperty(var12, var9);
               var14.setMin(var7.getNumberMin());
               var14.setMax(var7.getNumberMax());
               var14.setIncrement(var7.getNumberIncrement());
               var5[var6] = var14;
            }
         } else {
            var5[var6] = new BooleanProperty(var12, var9);
         }
      }

      return var5;
   }

   private Property[] parseData(Class var1, Object var2, Object[][] var3) {
      PropertyInfo[] var4 = new PropertyInfo[var3.length];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = PropertyInfo.fromArray(var3[var5]);
      }

      return this.parseInfo(var1, var4);
   }
}
