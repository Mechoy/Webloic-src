package weblogic.ejb.container.internal;

import weblogic.kernel.ThreadLocalStack;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class CallerSubjectStack {
   private static final ThreadLocalStack threadSubject = new ThreadLocalStack(false);

   public static AuthenticatedSubject getCurrentSubject() {
      AuthenticatedSubject var0 = null;
      Object var1 = threadSubject.get();
      if (var1 != null && var1 instanceof AuthenticatedSubject) {
         var0 = (AuthenticatedSubject)var1;
      }

      return var0;
   }

   public static void pushSubject(AuthenticatedSubject var0) {
      threadSubject.push(var0);
   }

   public static AuthenticatedSubject popSubject() {
      AuthenticatedSubject var0 = null;
      Object var1 = threadSubject.pop();
      if (var1 != null && var1 instanceof AuthenticatedSubject) {
         var0 = (AuthenticatedSubject)var1;
      }

      return var0;
   }
}
