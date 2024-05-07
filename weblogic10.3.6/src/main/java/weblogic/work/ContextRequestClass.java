package weblogic.work;

import java.security.AccessController;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.spi.WLSGroup;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

final class ContextRequestClass extends ServiceClassSupport {
   private static final AbstractSubject kernelId = (AbstractSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String name;
   private RequestClass defaultRequestClass;
   private ConcurrentHashMap groupMap = new ConcurrentHashMap();
   private ConcurrentHashMap usernameMap = new ConcurrentHashMap();
   private final SubjectManager subjectManager = SubjectManager.getSubjectManager();

   public ContextRequestClass(String var1) {
      super(var1);
      this.name = var1;
      this.defaultRequestClass = new FairShareRequestClass(var1);
   }

   public void addUser(String var1, RequestClass var2) {
      this.usernameMap.put(var1, var2);
   }

   public void addGroup(String var1, RequestClass var2) {
      this.groupMap.put(var1, var2);
   }

   public RequestClass getEffective(AuthenticatedSubject var1) {
      AuthenticatedSubject var2 = var1;
      if (var1 == null) {
         var2 = (AuthenticatedSubject)this.subjectManager.getCurrentSubject(kernelId);
      }

      if (var2 == null) {
         return this.defaultRequestClass;
      } else {
         RequestClass var3 = (RequestClass)this.usernameMap.get(SubjectUtils.getUsername(var2));
         if (var3 != null) {
            return var3;
         } else {
            Set var4 = var2.getPrincipals();
            if (var4 == null) {
               return this.defaultRequestClass;
            } else {
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  Object var6 = var5.next();
                  if (var6 instanceof WLSGroup) {
                     var3 = (RequestClass)this.groupMap.get(((WLSGroup)var6).getName());
                     if (var3 != null) {
                        return var3;
                     }
                  }
               }

               var3 = (RequestClass)this.groupMap.get("everyone");
               if (var3 != null) {
                  return var3;
               } else {
                  return this.defaultRequestClass;
               }
            }
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public void timeElapsed(long var1, ServiceClassesStats var3) {
   }

   public void cleanup() {
      super.cleanup();
      this.defaultRequestClass.cleanup();
   }
}
