package weblogic.servlet.security.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.servlet.internal.WebAppServletContext;

public final class ExternalRoleCheckerManager implements ExternalRoleChecker {
   private static final List<ExternalRoleCheckerFactory> externalRoleCheckerFactories = new LinkedList();
   private WebAppServletContext mCtx;
   private List<ExternalRoleChecker> externalRoleCheckers;

   public static synchronized void addExternalRoleCheckerFactory(ExternalRoleCheckerFactory var0) {
      externalRoleCheckerFactories.add(var0);
   }

   public static List<ExternalRoleCheckerFactory> getExternalRoleCheckerFactories() {
      return externalRoleCheckerFactories;
   }

   public ExternalRoleCheckerManager(WebAppServletContext var1) {
      this.mCtx = var1;
   }

   public boolean isExternalRole(String var1) {
      Iterator var2;
      if (this.externalRoleCheckers == null) {
         this.externalRoleCheckers = new ArrayList();
         var2 = getExternalRoleCheckerFactories().iterator();

         while(var2.hasNext()) {
            ExternalRoleCheckerFactory var3 = (ExternalRoleCheckerFactory)var2.next();
            ExternalRoleChecker var4 = var3.getExternalRoleChecker(this.mCtx);
            if (var4 != null) {
               this.externalRoleCheckers.add(var4);
            }
         }
      }

      var2 = this.externalRoleCheckers.iterator();

      ExternalRoleChecker var5;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var5 = (ExternalRoleChecker)var2.next();
      } while(!var5.isExternalRole(var1));

      return true;
   }

   static {
      addExternalRoleCheckerFactory(new ExternalRoleCheckerFactory() {
         public ExternalRoleChecker getExternalRoleChecker(WebAppServletContext var1) {
            return new ExternalRoleChecker() {
               public boolean isExternalRole(String var1) {
                  return false;
               }
            };
         }
      });
   }
}
