package weblogic.deployment.descriptors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.MissingResourceException;
import weblogic.utils.AssertionError;
import weblogic.utils.localization.Localizer;

public abstract class BaseDescriptor implements ValidatableWithNotify {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private String descriptorBundle;
   protected List listeners = new ArrayList();
   protected Map errors = new HashMap();
   protected boolean isValid = true;
   protected Localizer localizer;
   private Collection propertyChangeListeners;

   public BaseDescriptor(String var1) {
      this.localizer = new Localizer(var1);
      this.propertyChangeListeners = null;
   }

   public void addValidationListener(ValidationListener var1) {
      this.listeners.add(var1);
   }

   public void removeValidationListener(ValidationListener var1) {
      ListIterator var2 = this.listeners.listIterator();

      while(var2.hasNext()) {
         if (var1.equals((ValidationListener)var2.next())) {
            this.listeners.remove(var1);
            break;
         }
      }

   }

   public boolean isValid() {
      return this.isValid && this.isValidSubObjects();
   }

   public Collection getErrors() {
      return this.errors.values();
   }

   public boolean hasErrors() {
      return !this.errors.isEmpty();
   }

   public void validate() {
      this.removeAllErrors();
      this.validateSelf();
      Iterator var1 = this.getSubObjectsIterator();

      while(var1.hasNext()) {
         ((Validatable)var1.next()).validate();
      }

   }

   public Collection getInvalidObjects() {
      ArrayList var1 = new ArrayList();
      if (!this.isValid) {
         var1.add(this);
      }

      Iterator var2 = this.getSubObjectsIterator();

      while(var2.hasNext()) {
         Collection var3 = ((BaseDescriptor)var2.next()).getInvalidObjects();
         var1.addAll(var3);
      }

      return var1;
   }

   protected void addError(String var1) {
      this.isValid = false;
      String var2 = null;

      try {
         var2 = this.localizer.getFormattedMsg(var1);
      } catch (MissingResourceException var4) {
         throw new AssertionError(var4);
      }

      this.addVE(var1, var2);
   }

   protected void addError(String var1, String var2) {
      this.isValid = false;
      String var3 = null;

      try {
         var3 = this.localizer.getFormattedMsg(var1, var2);
      } catch (MissingResourceException var5) {
         throw new AssertionError(var5);
      }

      this.addVE(var1, var3);
   }

   protected void addError(String var1, String var2, String var3) {
      this.isValid = false;
      String var4 = null;

      try {
         var4 = this.localizer.getFormattedMsg(var1, var2, var3);
      } catch (MissingResourceException var6) {
         throw new AssertionError(var6);
      }

      this.addVE(var1, var4);
   }

   protected void addError(String var1, String var2, String var3, String var4) {
      this.isValid = false;
      String var5 = null;

      try {
         var5 = this.localizer.getFormattedMsg(var1, var2, var3, var4);
      } catch (MissingResourceException var7) {
         throw new AssertionError(var7);
      }

      this.addVE(var1, var5);
   }

   private void addVE(String var1, String var2) {
      ValidationEvent var3 = new ValidationEvent(this, false, var1, var2);
      this.errors.put(var1, var3);
      this.notifyListeners(var3);
   }

   public void removeAllErrors() {
      this.isValid = true;
      this.errors = new HashMap();
   }

   protected void removeError(String var1, String var2) {
      ValidationEvent var3 = (ValidationEvent)this.errors.remove(var1);
      ValidationEvent var4 = new ValidationEvent((ValidatableWithNotify)var3.getSource(), true, var1, var2);
      this.notifyListeners(var4);
   }

   protected boolean isValidSubObjects() {
      boolean var1 = true;

      for(Iterator var2 = this.getSubObjectsIterator(); var2.hasNext(); var1 &= ((Validatable)var2.next()).isValid()) {
      }

      return var1;
   }

   protected void notifyListeners(ValidationEvent var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((ValidationListener)var2.next()).validationPerformed(var1);
      }

   }

   public abstract void validateSelf();

   public abstract Iterator getSubObjectsIterator();

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      if (this.propertyChangeListeners == null) {
         this.propertyChangeListeners = new ArrayList();
      }

      this.propertyChangeListeners.add(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      if (this.propertyChangeListeners != null) {
         this.propertyChangeListeners.remove(var1);
      }

   }

   public void firePropertyChange(PropertyChangeEvent var1) {
      if (this.propertyChangeListeners != null) {
         Iterator var2 = this.propertyChangeListeners.iterator();

         while(var2.hasNext()) {
            PropertyChangeListener var3 = (PropertyChangeListener)var2.next();
            var3.propertyChange(var1);
         }
      }

   }
}
