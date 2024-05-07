package weblogic.management.descriptors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import weblogic.management.ManagementException;
import weblogic.utils.Debug;

public class XMLElementMBeanDelegate {
   protected static final String CDATA_BEGIN = "<![CDATA[";
   protected static final String CDATA_END = "]]>";
   protected PropertyChangeSupport changeSupport = null;
   private String name;

   public XMLElementMBeanDelegate() {
   }

   public XMLElementMBeanDelegate(String var1) {
      this.name = var1;
   }

   protected static String cdata(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         return var0.indexOf(62) < 0 && var0.indexOf(62) < 0 ? var0 : "<![CDATA[" + var0 + "]]>";
      }
   }

   protected static boolean comp(String var0, String var1) {
      if (var0 == var1) {
         return true;
      } else if ((var0 == null || var0.length() == 0) && (var1 == null || var1.length() == 0)) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equals(var1) : false;
      }
   }

   protected static boolean comp(Object var0, Object var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null) {
         if (var0.equals(var1)) {
            return true;
         } else if (var0.getClass().isArray() && var1.getClass().isArray()) {
            Object[] var2 = (Object[])((Object[])var0);
            Object[] var3 = (Object[])((Object[])var1);
            if (var2.length != var3.length) {
               return false;
            } else {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  if (var2[var4] != var3[var4]) {
                     if (var2[var4] == null || var3[var4] == null) {
                        return false;
                     }

                     if (!var2[var4].equals(var3[var4])) {
                        return false;
                     }
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void register() throws ManagementException {
   }

   public void unregister() throws ManagementException {
   }

   public String toXML(int var1) {
      Debug.say("WARNING:  toXML(int indentLevel) not found on class " + this.getClass().getName());
      return "";
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.getChangeSupport().addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      if (this.changeSupport != null) {
         this.getChangeSupport().removePropertyChangeListener(var1);
      }

   }

   public void firePropertyChange(String var1, Object var2, Object var3) {
      if (this.changeSupport != null) {
         this.changeSupport.firePropertyChange(var1, var2, var3);
      }

   }

   protected synchronized PropertyChangeSupport getChangeSupport() {
      if (this.changeSupport == null) {
         this.changeSupport = new PropertyChangeSupport(this);
      }

      return this.changeSupport;
   }

   protected void checkChange(String var1, String var2, String var3) {
      if (!comp(var2, var3)) {
         this.firePropertyChange(var1, var2, var3);
      }

   }

   protected void checkChange(String var1, Object var2, Object var3) {
      if (!comp(var2, var3)) {
         this.firePropertyChange(var1, var2, var3);
      }

   }

   protected void checkChange(String var1, boolean var2, boolean var3) {
      if (var2 != var3) {
         this.firePropertyChange(var1, new Boolean(var2), new Boolean(var3));
      }

   }

   protected void checkChange(String var1, int var2, int var3) {
      if (var2 != var3) {
         this.firePropertyChange(var1, new Integer(var2), new Integer(var3));
      }

   }
}
