package weblogic.management.descriptors;

import weblogic.utils.NestedException;

public final class DescriptorValidationException extends NestedException {
   public DescriptorValidationException() {
   }

   public DescriptorValidationException(String var1) {
      super(var1);
   }

   public DescriptorValidationException(Throwable var1) {
      super(var1);
   }

   public DescriptorValidationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
