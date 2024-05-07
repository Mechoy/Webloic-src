package weblogic.ant.taskdefs.antline;

import weblogic.utils.NestedException;

public class AntLineException extends NestedException {
   public AntLineException() {
   }

   public AntLineException(Throwable var1) {
      super(var1);
   }

   public AntLineException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public AntLineException(String var1) {
      super(var1);
   }
}
