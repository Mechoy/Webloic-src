package weblogic.ejb.container.deployer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.jndi.Environment;

public class EjbJndiService {
   private static final DebugLogger debugLogger;
   private boolean clientsOnSameServer;
   private Context ctx;
   private List<BoundEnv> boundEnvs = new ArrayList();

   EjbJndiService(boolean var1) {
      this.clientsOnSameServer = var1;
   }

   public void replicatedBind(String var1, Object var2) throws NamingException {
      this.bind(var1, var2, true);
   }

   public void nonReplicatedBind(String var1, Object var2) throws NamingException {
      this.bind(var1, var2, false);
   }

   public void replicatedBind(Name var1, Object var2) throws NamingException {
      this.bind(var1, var2, true);
   }

   public void nonReplicatedBind(Name var1, Object var2) throws NamingException {
      this.bind(var1, var2, false);
   }

   public void bind(String var1, Object var2, boolean var3) throws NamingException {
      this.initCtx(var3);
      this.ctx.bind(var1, var2);
      this.boundEnvs.add(new BoundEnv(var1, var3));
   }

   public void bind(Name var1, Object var2, boolean var3) throws NamingException {
      this.initCtx(var3);
      this.ctx.bind(var1, var2);
      this.boundEnvs.add(new BoundEnv(var1.toString(), var3));
   }

   public void unbind(Name var1) {
      this.unbind(var1.toString());
   }

   public void unbind(String var1) {
      if (var1 != null) {
         boolean var2 = true;
         Iterator var3 = this.boundEnvs.iterator();

         while(var3.hasNext()) {
            BoundEnv var4 = (BoundEnv)var3.next();
            if (var1.equals(var4.name)) {
               var2 = var4.replicatedBind;
               break;
            }
         }

         this.unbind(var1, var2);
      }
   }

   private void unbind(String var1, boolean var2) {
      if (var1 != null) {
         try {
            if (this.ctx == null) {
               Environment var3 = new Environment();
               this.ctx = var3.getInitialContext();
            }

            this.initCtx(var2);
            this.ctx.unbind(var1);

            while(var1.indexOf(46) > 0) {
               int var7 = var1.lastIndexOf(46);
               var1 = var1.substring(0, var7);

               try {
                  this.ctx.destroySubcontext(var1);
               } catch (Exception var5) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("error in unbind " + var1, var5);
                  }

                  return;
               }
            }
         } catch (NamingException var6) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("error in unbind " + var1, var6);
            }
         }

      }
   }

   public void unbindAll() {
      for(int var1 = this.boundEnvs.size() - 1; var1 > -1; --var1) {
         BoundEnv var2 = (BoundEnv)this.boundEnvs.get(var1);
         this.unbind(var2.name, var2.replicatedBind);
      }

      this.boundEnvs.clear();
      if (this.ctx != null) {
         try {
            this.ctx.close();
            this.ctx = null;
         } catch (NamingException var3) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("error in unbind", var3);
            }
         }
      }

   }

   private void initCtx(boolean var1) throws NamingException {
      if (this.ctx == null) {
         Environment var2 = new Environment();
         var2.setCreateIntermediateContexts(true);
         this.ctx = var2.getInitialContext();
      }

      if (this.clientsOnSameServer) {
         this.ctx.addToEnvironment("weblogic.jndi.replicateBindings", "false");
      } else {
         this.ctx.addToEnvironment("weblogic.jndi.replicateBindings", String.valueOf(var1));
      }

   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }

   private static class BoundEnv {
      final String name;
      final boolean replicatedBind;

      BoundEnv(String var1, boolean var2) {
         this.name = var1;
         this.replicatedBind = var2;
      }
   }
}
