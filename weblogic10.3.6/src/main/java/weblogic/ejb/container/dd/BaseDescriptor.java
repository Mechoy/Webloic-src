package weblogic.ejb.container.dd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.MissingResourceException;
import weblogic.ejb.container.utils.validation.Validatable;
import weblogic.ejb.container.utils.validation.ValidatableWithNotify;
import weblogic.ejb.container.utils.validation.ValidationEvent;
import weblogic.ejb.container.utils.validation.ValidationListener;
import weblogic.utils.AssertionError;
import weblogic.utils.localization.Localizer;

public abstract class BaseDescriptor implements ValidatableWithNotify {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   private static boolean verbose = System.getProperty("weblogic.ejb.deployment.verbose") != null;
   private String descriptorBundle;
   protected List listeners;
   protected Map errors;
   protected boolean isValid;
   protected Localizer localizer;

   public BaseDescriptor() {
      this.listeners = new ArrayList();
      this.errors = new HashMap();
      this.isValid = true;
   }

   public BaseDescriptor(String var1) {
      this();
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
      if (verbose) {
         System.err.println(System.identityHashCode(this) + ": Added error " + var3);
      }

      this.errors.put(var1, var3);
      if (debug) {
         System.err.println(System.identityHashCode(this) + ": errors.values().size() = " + this.errors.values().size());
      }

      this.notifyListeners(var3);
   }

   public void removeAllErrors() {
      if (debug) {
         System.err.println(System.identityHashCode(this) + ": Remove all errors");
      }

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
}
