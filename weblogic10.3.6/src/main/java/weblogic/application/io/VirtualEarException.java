package weblogic.application.io;

import java.text.MessageFormat;

public final class VirtualEarException extends Exception {
   public static final int PATH_CONFLICTS = 0;
   public static final int MALFORMED_DESC = 1;
   public static final int DELETE_FAILED = 2;
   private static final int LAST_ERROR_CODE = 2;
   private static final String[] templates = new String[3];
   private String message;

   public VirtualEarException(int var1) {
      this(var1, new String[0]);
   }

   public VirtualEarException(int var1, String[] var2) {
      if (var1 >= 0 && var1 <= 2) {
         this.message = MessageFormat.format(templates[var1], (Object[])var2);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public VirtualEarException(Throwable var1) {
      super(var1);
      this.message = var1.getMessage();
   }

   public String getMessage() {
      return this.message;
   }

   public String toString() {
      return this.getMessage();
   }

   static {
      templates[0] = "\nCould not create a link due to conflicting paths.\n\n  Link:         {0} -> {1}\n  Conflict(s):  {2}\n\n";
      templates[1] = "Cannot parse .beabuild.txt file. It appears to be corrupted.";
      templates[2] = "The corrupt application.xml file could not be deleted.";
   }
}
