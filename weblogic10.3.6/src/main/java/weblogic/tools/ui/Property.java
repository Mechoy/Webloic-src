package weblogic.tools.ui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import weblogic.utils.Debug;

public abstract class Property implements FocusListener {
   protected boolean autoCommit;
   protected Object bean;
   protected PropertyDescriptor pd;
   protected JLabel label;
   private boolean required;
   private static final Object[] NO_ARGS = new Object[0];

   private static void p(String var0) {
      System.err.println("[Property]: " + var0);
   }

   public Property(Object var1, PropertyDescriptor var2, String var3) {
      this(var1, var2, var3, false);
   }

   public Property(Object var1, PropertyDescriptor var2, String var3, boolean var4) {
      this.autoCommit = true;
      this.bean = var1;
      this.pd = var2;
      if (var3 == null) {
         var3 = this.pd.getName();
      }

      this.required = var4;
      this.label = var4 ? UIFactory.getMandatoryLabel(var3) : UIFactory.getLabel(var3);
      Component var5 = this.getComponent();
      if (var5 != null) {
         var5.addFocusListener(this);
      }

   }

   public abstract Component getComponent();

   protected abstract Object getCurrentUIValue();

   protected abstract void setCurrentUIValue(Object var1);

   public boolean isAutoCommit() {
      return this.autoCommit;
   }

   public void setAutoCommit(boolean var1) {
      this.autoCommit = var1;
   }

   public void focusGained(FocusEvent var1) {
   }

   public void focusLost(FocusEvent var1) {
      if (this.isAutoCommit() && var1.getSource() == this.getComponent()) {
         this.uiToModel();
      }

   }

   public boolean hasSeparateLabel() {
      return true;
   }

   public Object getBean() {
      return this.bean;
   }

   public void setBean(Object var1) {
      this.bean = var1;
   }

   public void setTooltip(String var1) {
      if (var1 != null) {
         Component var2 = this.getComponent();
         if (var2 != null && var2 instanceof JComponent) {
            JComponent var3 = (JComponent)var2;
            var3.setToolTipText(var1);
         }

      }
   }

   public String getHelpAnchor() {
      String var1 = null;
      Component var2 = this.getComponent();
      if (var2 != null && var2 instanceof JComponent) {
         JComponent var3 = (JComponent)var2;
         Object var4 = var3.getClientProperty("wl.helpanchor");
         if (var4 != null) {
            var1 = var4.toString();
         }
      }

      return var1;
   }

   public void setHelpAnchor(String var1) {
      Component var2 = this.getComponent();
      if (var2 != null && var2 instanceof JComponent) {
         JComponent var3 = (JComponent)var2;
         var3.putClientProperty("wl.helpanchor", var1);
      }

   }

   public JLabel getLabel() {
      return this.label;
   }

   public void modelToUI() {
      this.setCurrentUIValue(this.invokeGetter());
   }

   public boolean isUIEmpty() {
      return false;
   }

   public void uiToModel() {
      this.invokeSetter(this.getCurrentUIValue());
   }

   protected static void handleITE(InvocationTargetException var0) {
      Throwable var1 = var0.getTargetException();
      if (var1 instanceof RuntimeException) {
         throw (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      } else {
         throw new RuntimeException(var1.toString());
      }
   }

   public boolean isRequired() {
      return this.required;
   }

   protected Object invokeGetter() {
      Debug.assertion(this.bean != null);

      try {
         return this.pd.getReadMethod().invoke(this.bean, NO_ARGS);
      } catch (InvocationTargetException var2) {
         handleITE(var2);
         return null;
      } catch (IllegalAccessException var3) {
         throw new RuntimeException(var3.toString());
      }
   }

   protected void invokeSetter(Object var1) {
      Debug.assertion(this.bean != null);

      try {
         Object[] var2 = new Object[1];
         if (this.pd.getPropertyType() == Integer.TYPE) {
            var1 = new Integer(Integer.parseInt(var1.toString()));
         }

         var2[0] = var1;
         Debug.assertion(null != this.pd.getWriteMethod(), "Couldn't find a write method for " + this.pd.getReadMethod());
         this.pd.getWriteMethod().invoke(this.bean, var2);
      } catch (InvocationTargetException var3) {
         handleITE(var3);
      } catch (IllegalAccessException var4) {
         throw new RuntimeException(var4.toString());
      }

   }

   public static Property parseSpec(Class var0, Object var1, BeanInfo var2, PropertyDescriptor[] var3, Object[] var4) {
      if (var2 == null) {
         try {
            var2 = Introspector.getBeanInfo(var0);
         } catch (Exception var14) {
            var14.printStackTrace();
            throw new RuntimeException(var14.toString());
         }
      }

      if (var3 == null) {
         var3 = var2.getPropertyDescriptors();
      }

      String var5 = (String)var4[0];
      String var6 = (String)var4[1];
      String var7 = (String)var4[2];
      PropertyDescriptor var8 = getPD(var3, var5, var0);
      Class var9 = var8.getPropertyType();
      Object var10 = null;
      Object var11 = var4[var4.length - 1];
      boolean var12 = var11 == Boolean.TRUE;
      if (var4.length > 3 && var4[3] != null && !(var4[3] instanceof Boolean)) {
         if (var4[3] instanceof String[]) {
            String[] var13 = (String[])((String[])var4[3]);
            var10 = new ListProperty(var1, var8, var6, var13, var12);
         } else if (var4[3] instanceof Object[]) {
            Object[] var15 = (Object[])((Object[])var4[3]);
            var10 = new ObjectProperty(var1, var8, var6, var15, var12);
         }
      } else if (var9 != Boolean.TYPE && var9 != Boolean.class) {
         if (!var9.isPrimitive() && !Number.class.isAssignableFrom(var9)) {
            var10 = new StringProperty(var1, var8, var6, var12);
         } else {
            var10 = new NumberProperty(var1, var8, var6);
         }
      } else {
         var10 = new BooleanProperty(var1, var8, var6);
      }

      if (var4.length > 4 && var4[4] != null) {
         String var16 = var4[4].toString();
         ((Property)var10).setHelpAnchor(var16);
      }

      if (var10 == null) {
         throw new RuntimeException("bad data for prop: '" + var5 + "' of " + var0.getName());
      } else {
         if (var7 != null) {
            ((Property)var10).setTooltip(var7);
         }

         return (Property)var10;
      }
   }

   private static Class[] getAllInterfaces(Class var0) {
      ArrayList var1 = new ArrayList();

      for(Class var2 = var0; var2 != null; var2 = var2.getSuperclass()) {
         var1.add(var2);
         Class[] var3 = var2.getInterfaces();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1.add(var3[var4]);
         }
      }

      return (Class[])((Class[])var1.toArray(new Class[var1.size()]));
   }

   static PropertyDescriptor getPD(PropertyDescriptor[] var0, String var1, Class var2) {
      PropertyDescriptor var3 = null;
      Class[] var5 = getAllInterfaces(var2);

      for(int var6 = 0; var6 < var5.length && var3 == null; ++var6) {
         for(int var7 = 0; var7 < var0.length; ++var7) {
            if (var1.equalsIgnoreCase(var0[var7].getName())) {
               var3 = var0[var7];
            }
         }

         try {
            BeanInfo var9 = Introspector.getBeanInfo(var5[var6]);
            var0 = var9.getPropertyDescriptors();
         } catch (Exception var8) {
            var0 = new PropertyDescriptor[0];
         }
      }

      if (var3 == null) {
         throw new RuntimeException("cannot find property '" + var1 + "' for class " + var2.getName());
      } else {
         return var3;
      }
   }
}
