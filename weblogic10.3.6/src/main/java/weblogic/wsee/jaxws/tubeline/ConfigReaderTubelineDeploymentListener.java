package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.util.ServiceFinder;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.jaxws.tubeline.config.AssemblerItem;
import weblogic.wsee.jaxws.tubeline.config.Config;
import weblogic.wsee.jaxws.tubeline.config.DeploymentListeners;
import weblogic.wsee.jaxws.tubeline.config.Listener;
import weblogic.wsee.jaxws.tubeline.config.Listeners;

public class ConfigReaderTubelineDeploymentListener implements TubelineDeploymentListener, ServiceInitialization {
   private Collection<Config> clientConfig = null;

   public void init(WSService var1, Class<? extends Service> var2) {
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();
      if (var3 == null) {
         var3 = ClassLoader.getSystemClassLoader();
      }

      this.clientConfig = this.readConfigFile(var3);
   }

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();
      if (var3 == null) {
         var3 = ClassLoader.getSystemClassLoader();
      }

      Collection var4 = this.clientConfig != null ? this.clientConfig : this.readConfigFile(var3);
      Iterator var5 = var4.iterator();

      while(true) {
         Listeners var8;
         do {
            DeploymentListeners var7;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               Config var6 = (Config)var5.next();
               var7 = var6.getDeploymentListeners();
            } while(var7 == null);

            var8 = var7.getClient();
         } while(var8 == null);

         List var9 = var8.getListener();
         if (var9 != null) {
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               Listener var11 = (Listener)var10.next();
               this.load(var11, var3).createClient(var1, var2);
            }
         }

         this.createItems(var2, var8.getAssemblerItem(), var3);
      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      ClassLoader var3 = var1.getEndpoint().getImplementationClass().getClassLoader();
      Collection var4 = this.readConfigFile(var3);
      Iterator var5 = var4.iterator();

      while(true) {
         Listeners var8;
         do {
            DeploymentListeners var7;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               Config var6 = (Config)var5.next();
               var7 = var6.getDeploymentListeners();
            } while(var7 == null);

            var8 = var7.getServer();
         } while(var8 == null);

         List var9 = var8.getListener();
         if (var9 != null) {
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               Listener var11 = (Listener)var10.next();
               this.load(var11, var3).createServer(var1, var2);
            }
         }

         this.createItems(var2, var8.getAssemblerItem(), var3);
      }
   }

   private TubelineDeploymentListener load(Listener var1, ClassLoader var2) {
      try {
         Class var3 = Class.forName(var1.getClazz(), true, var2);
         Iterator var4 = ServiceFinder.find(TubelineDeploymentListenerCreator.class, var2).iterator();

         TubelineDeploymentListener var6;
         do {
            if (!var4.hasNext()) {
               return (TubelineDeploymentListener)var3.newInstance();
            }

            TubelineDeploymentListenerCreator var5 = (TubelineDeploymentListenerCreator)var4.next();
            var6 = var5.create(var3);
         } while(var6 == null);

         return var6;
      } catch (ClassNotFoundException var7) {
         throw new WebServiceException(var7);
      } catch (IllegalAccessException var8) {
         throw new WebServiceException(var8);
      } catch (InstantiationException var9) {
         throw new WebServiceException(var9);
      }
   }

   private void createItems(Set<TubelineAssemblerItem> var1, List<AssemblerItem> var2, ClassLoader var3) {
      if (var2 != null) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            AssemblerItem var5 = (AssemblerItem)var4.next();
            HashSet var6 = var5.getAfter() != null ? new HashSet(var5.getAfter().getItem()) : null;
            HashSet var7 = var5.getBefore() != null ? new HashSet(var5.getBefore().getItem()) : null;
            HashSet var8 = var5.getRequired() != null ? new HashSet(var5.getRequired().getItem()) : null;
            String var9 = var5.getClazz();
            String var10 = var5.getName() != null ? var5.getName() : var9;
            var1.add(new TubelineAssemblerItem(var10, this.loadTubeFactory(var9, var3), var7, var6, var8));
         }
      }

   }

   private TubeFactory loadTubeFactory(String var1, ClassLoader var2) {
      try {
         Class var3 = Class.forName(var1, true, var2);
         Iterator var4 = ServiceFinder.find(TubeFactoryCreator.class, var2).iterator();

         TubeFactory var6;
         do {
            if (!var4.hasNext()) {
               return (TubeFactory)var3.newInstance();
            }

            TubeFactoryCreator var5 = (TubeFactoryCreator)var4.next();
            var6 = var5.create(var3);
         } while(var6 == null);

         return var6;
      } catch (ClassNotFoundException var7) {
         throw new WebServiceException(var7);
      } catch (IllegalAccessException var8) {
         throw new WebServiceException(var8);
      } catch (InstantiationException var9) {
         throw new WebServiceException(var9);
      }
   }

   private Collection<Config> readConfigFile(ClassLoader var1) {
      try {
         ArrayList var2 = new ArrayList();
         HashMap var3 = new HashMap();
         var3.put("supressAccessorWarnings", Boolean.TRUE);
         JAXBContext var4 = JAXBContext.newInstance("weblogic.wsee.jaxws.tubeline.config", var1, var3);
         Unmarshaller var5 = var4.createUnmarshaller();
         Enumeration var6 = var1.getResources("META-INF/tube-config.xml");
         if (var6 != null) {
            while(var6.hasMoreElements()) {
               var2.add((Config)var5.unmarshal((URL)var6.nextElement()));
            }
         }

         return var2;
      } catch (IOException var7) {
         throw new WebServiceException(var7);
      } catch (JAXBException var8) {
         throw new WebServiceException(var8);
      }
   }
}
