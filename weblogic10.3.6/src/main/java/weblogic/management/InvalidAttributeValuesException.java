package weblogic.management;

import javax.management.InvalidAttributeValueException;

public class InvalidAttributeValuesException extends InvalidAttributeValueException {
   private InvalidAttributeValueException[] list;

   public InvalidAttributeValuesException(InvalidAttributeValueException[] var1) {
      this.list = var1;
   }
}
