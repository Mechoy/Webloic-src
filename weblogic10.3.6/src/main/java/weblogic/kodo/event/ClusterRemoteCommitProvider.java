package weblogic.kodo.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.datacache.DataCache;
import org.apache.openjpa.event.AbstractRemoteCommitProvider;
import org.apache.openjpa.event.RemoteCommitEvent;
import org.apache.openjpa.event.RemoteCommitEventManager;
import org.apache.openjpa.event.RemoteCommitProvider;
import org.apache.openjpa.lib.conf.Configurable;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.concurrent.ConcurrentReferenceHashSet;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.util.UserException;
import weblogic.cluster.ClusterService;
import weblogic.cluster.ClusterServices;
import weblogic.cluster.GroupMessage;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.rmi.spi.HostID;

public final class ClusterRemoteCommitProvider extends AbstractRemoteCommitProvider implements RecoverListener, Configurable {
   private static final String RECOVERY_DO_NOTHING = "none";
   private static final String RECOVERY_CLEAR_ALL = "clear";
   private static Localizer _loc = Localizer.forPackage(ClusterRemoteCommitProvider.class);
   protected Configuration conf;
   private int bufferSize = 10;
   private String recoverAction = "none";
   private String topics = null;
   private MulticastSession multicastSession;
   private static Set registry = new ConcurrentReferenceHashSet(0);

   public void broadcast(RemoteCommitEvent var1) {
      ClusterRemoteCommitEvent var2 = new ClusterRemoteCommitEvent(this.getCacheTopics(), var1);

      try {
         this.multicastSession.send(var2);
      } catch (IOException var4) {
         throw new InternalException(_loc.get("transmission-error"), var4);
      }
   }

   public void setConfiguration(Configuration var1) {
      this.conf = var1;
   }

   public void endConfiguration() {
      ClusterServices var1 = ClusterService.getServices();
      if (var1 == null) {
         throw new InternalException(_loc.get("no-transport"));
      } else {
         this.multicastSession = var1.createMulticastSession(this, this.bufferSize);
         if (this.multicastSession == null) {
            throw new InternalException(_loc.get("no-multicast-session"));
         } else {
            registry.add(this.conf);
         }
      }
   }

   public void close() {
      registry.remove(this.conf);
   }

   public GroupMessage createRecoverMessage() {
      String var1 = "none".equalsIgnoreCase(this.recoverAction) ? null : this.getCacheTopics();
      return new ClusterRemoteCommitEvent(var1, (RemoteCommitEvent)null);
   }

   public int getBufferSize() {
      return this.bufferSize;
   }

   public void setBufferSize(int var1) {
      this.bufferSize = var1;
   }

   public void setRecoverAction(String var1) {
      if ("none".equalsIgnoreCase(var1)) {
         this.recoverAction = var1;
      } else {
         if (!"clear".equalsIgnoreCase(var1)) {
            throw new UserException(_loc.get("bad-recover-action", var1));
         }

         this.recoverAction = var1;
      }

   }

   public String getRecoverAction() {
      return this.recoverAction;
   }

   public void setCacheTopics(String var1) {
      this.topics = var1;
   }

   public String getCacheTopics() {
      if (this.topics == null) {
         this.topics = this.conf == null ? null : this.conf.getId();
      }

      return this.topics;
   }

   public static class ClusterRemoteCommitEvent implements GroupMessage {
      protected final Collection<String> topics;
      protected final RemoteCommitEvent event;

      public ClusterRemoteCommitEvent(String var1, RemoteCommitEvent var2) {
         this.topics = this.split(var1);
         this.event = var2;
      }

      public void execute(HostID var1) {
         if (this.topics != null && !this.topics.isEmpty()) {
            if (!this.isEcho(var1)) {
               Iterator var2 = ClusterRemoteCommitProvider.registry.iterator();

               while(var2.hasNext()) {
                  Object var3 = var2.next();
                  if (var3 instanceof OpenJPAConfiguration) {
                     RemoteCommitEventManager var4 = ((OpenJPAConfiguration)var3).getRemoteCommitEventManager();
                     if (var4 != null) {
                        RemoteCommitProvider var5 = var4.getRemoteCommitProvider();
                        if (var5 instanceof ClusterRemoteCommitProvider && this.intersects(this.topics, this.split(((ClusterRemoteCommitProvider)var5).getCacheTopics()))) {
                           if (this.isClearAll()) {
                              this.clearAll(var4.getListeners());
                           } else {
                              var4.fireEvent(this.event);
                           }
                        }
                     }
                  }
               }

            }
         }
      }

      private boolean isClearAll() {
         return this.event == null;
      }

      private void clearAll(Collection var1) {
         if (var1 != null && !var1.isEmpty()) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               if (var3 instanceof DataCache) {
                  ((DataCache)var3).clear();
               }
            }

         }
      }

      boolean isEcho(HostID var1) {
         return false;
      }

      Collection<String> split(String var1) {
         if (var1 == null) {
            return null;
         } else {
            String[] var2 = var1.split(",");
            ArrayList var3 = new ArrayList();
            String[] var4 = var2;
            int var5 = var2.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               if (var7 != null) {
                  var3.add(var7.trim());
               }
            }

            return var3;
         }
      }

      boolean intersects(Collection<String> var1, Collection<String> var2) {
         if (var1 == null && var2 == null) {
            return true;
         } else if (var1 != null && var2 != null) {
            Iterator var3 = var1.iterator();

            String var4;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               var4 = (String)var3.next();
            } while(!var2.contains(var4));

            return true;
         } else {
            return false;
         }
      }
   }
}
