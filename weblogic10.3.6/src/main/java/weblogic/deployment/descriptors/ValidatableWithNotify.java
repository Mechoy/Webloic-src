package weblogic.deployment.descriptors;

public interface ValidatableWithNotify extends Validatable {
   void addValidationListener(ValidationListener var1);

   void removeValidationListener(ValidationListener var1);
}
