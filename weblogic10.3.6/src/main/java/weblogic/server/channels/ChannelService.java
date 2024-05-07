package weblogic.server.channels;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.NamingException;
import weblogic.cluster.ClusterService;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.net.http.HttpURLConnection;
import weblogic.protocol.AdminServerIdentity;
import weblogic.protocol.ChannelList;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.ServerIdentityManager;
import weblogic.protocol.ServerURL;
import weblogic.protocol.URLManager;
import weblogic.protocol.UnknownProtocolException;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.protocol.configuration.NetworkAccessPointDefaultMBean;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.SubjectManager;
import weblogic.security.utils.SSLContextManager;
import weblogic.server.ServerLogger;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;
import weblogic.socket.ChannelSocketFactory;
import weblogic.t3.srvr.ServerRuntime;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;
import weblogic.utils.net.InetAddressHelper;

public class ChannelService extends ServerChannelManager implements ServerService, BeanUpdateListener {
   private static final int EXPECTED_SERVERS = 63;
   private static final int EXPECTED_CHANNELS = 31;
   private static final int EXPECTED_PROTOCOLS = 16;
   private static ConcurrentHashMap channelMap = new ConcurrentHashMap(63);
   private static ConcurrentHashMap channelsByName = new ConcurrentHashMap(63);
   private static ConcurrentHashMap localChannels = new ConcurrentHashMap(31);
   private static ConcurrentHashMap localChannelsByProtocol = new ConcurrentHashMap(16);
   private static ConcurrentHashMap localChannelsByName = new ConcurrentHashMap(31);
   private static ConcurrentHashMap EMPTY_MAP = new ConcurrentHashMap(1);
   private static boolean initialized = false;
   private static boolean DEBUG = false;
   private static boolean DEBUG_MAP = false;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private static RuntimeAccess runtimeAccess;
   private long adminChannelCreationTime;
   private static ArrayList<String> replicationChannelNames = new ArrayList();
   private boolean inCreateChannels = false;

   public String getName() {
      return "ChannelService";
   }

   public String getVersion() {
      return null;
   }

   public void start() throws ServiceFailureException {
      try {
         runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
         if (!ChannelHelper.checkConsistency(runtimeAccess.getServer())) {
            throw new ServiceFailureException("Network configuration error, check log for details.");
         } else {
            ChannelHelper.logChannelConfiguration(runtimeAccess.getServer());
            if (DEBUG) {
               p("Admin protocol is: " + ProtocolManager.getRealProtocol(ChannelService.PROTOCOL.ADMIN).getAsURLPrefix());
            }

            ServerURL.setIsServer(true);
            this.initializeServerChannels();
            initialized = true;
            ServerChannelManager.setServerChannelManager(this);
            if (AdminServerIdentity.getBootstrapIdentity() != null) {
               boolean var1 = ServerChannelManager.findServerChannel(LocalServerIdentity.getIdentity(), ChannelService.PROTOCOL.ADMIN) != null;
               boolean var2 = ServerChannelManager.findServerChannel(AdminServerIdentity.getBootstrapIdentity(), ChannelService.PROTOCOL.ADMIN) != null;
               if (var1 && !var2 || var2 && !var1) {
                  ServerLogger.logAdminChannelConflict();
                  throw new ServiceFailureException("Domain configuration must have at least one admin channel on every server, or none at all");
               }
            }

            if (URLManager.findAdministrationURL(LocalServerIdentity.getIdentity()) == null) {
               throw new ServiceFailureException("The server has no configured channels");
            } else if (AdminServerIdentity.getBootstrapIdentity() != null && !AdminServerIdentity.getBootstrapIdentity().isLocal() && isLocalChannel(URLManager.findAdministrationURL(AdminServerIdentity.getBootstrapIdentity()))) {
               throw new ServiceFailureException("The server's administration channel conflicts with the admin server's");
            } else {
               this.resetQOS();
               RemoteChannelServiceImpl.getInstance().addConnectDisconnectListener(new ConnectListener(), new DisconnectListener());
               this.injectChannelBasedSocketFactories();
               ServerLogger.logChannelsEnabled();
            }
         }
      } catch (UnknownHostException var3) {
         throw new ServiceFailureException("Initialization Failed", var3);
      }
   }

   private void injectChannelBasedSocketFactories() {
      ServerChannel var1 = ChannelService.PROTOCOL.HTTP.getHandler().getDefaultServerChannel();
      if (var1 != null) {
         HttpURLConnection.setDefaultSocketFactory(new ChannelSocketFactory(var1));
      }

   }

   private void resetQOS() {
      byte var1;
      if (findLocalServerChannel(ChannelService.PROTOCOL.ADMIN) == null) {
         var1 = 101;
         if (DEBUG) {
            p("Kernel and Server Identity is now QOS_ANY");
         }
      } else {
         var1 = 103;
         if (DEBUG) {
            p("Kernel and Server Identity is now QOS_ADMIN");
         }
      }

      kernelId.setQOS(var1);
      AuthenticatedSubject var2 = SecurityServiceManager.sendASToWire(kernelId);
      if (var2 != null) {
         var2.setQOS(var1);
      }

   }

   private void initializeServerChannels() throws UnknownHostException {
      HashMap var1 = this.createServerChannels(ManagementService.getRuntimeAccess(kernelId).getServer(), LocalServerIdentity.getIdentity(), localChannelsByProtocol, localChannelsByName);
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         localChannels.put(var3.getKey(), var3.getValue());
      }

      channelMap.put(LocalServerIdentity.getIdentity(), localChannelsByProtocol);
      channelsByName.put(LocalServerIdentity.getIdentity(), localChannelsByName);
   }

   void registerRuntimeService() throws ManagementException, RemoteException {
      runtimeAccess.getServer().addBeanUpdateListener(this);
      SSLMBean var1 = runtimeAccess.getServer().getSSL();
      var1.addBeanUpdateListener(this);
      DomainMBean var2 = runtimeAccess.getDomain();
      var2.addBeanUpdateListener(this);
      Iterator var3 = localChannelsByProtocol.values().iterator();

      while(var3.hasNext()) {
         ArrayList var4 = (ArrayList)var3.next();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            ServerChannelImpl var6 = (ServerChannelImpl)var5.next();
            ((ServerRuntime)runtimeAccess.getServerRuntime()).addServerChannelRuntime(var6.createRuntime());
            if (DEBUG) {
               p("added runtime for: " + var6);
            }
         }
      }

      ServerHelper.exportObject((Remote)RemoteChannelServiceImpl.getInstance());
   }

   private AbstractMap findOrCreateChannels(ServerIdentity var1) {
      if (var1.isClient()) {
         return null;
      } else {
         AbstractMap var2 = (AbstractMap)channelMap.get(var1);
         if (var2 == null && var1.getDomainName().equals(LocalServerIdentity.getIdentity().getDomainName())) {
            this.createChannels(var1);
            var2 = (AbstractMap)channelMap.get(var1);
         }

         return var2;
      }
   }

   private AbstractMap findOrCreateNamedChannels(ServerIdentity var1) {
      if (var1.isClient()) {
         return null;
      } else {
         AbstractMap var2 = (AbstractMap)channelsByName.get(var1);
         if (var2 == null && var1.getDomainName().equals(LocalServerIdentity.getIdentity().getDomainName())) {
            this.createChannels(var1);
            var2 = (AbstractMap)channelsByName.get(var1);
         }

         return var2;
      }
   }

   private synchronized void createChannels(final ServerIdentity var1) {
      if (!this.inCreateChannels) {
         try {
            this.inCreateChannels = true;
            SecurityManager.runAs(kernelId, kernelId, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  RemoteChannelServiceImpl.retrieveRemoteChannels(var1);
                  if (ChannelService.channelMap.get(var1) != null) {
                     return null;
                  } else {
                     ConcurrentHashMap var1x = new ConcurrentHashMap(31);
                     ConcurrentHashMap var2 = new ConcurrentHashMap(31);
                     ServerMBean var3 = ChannelService.this.getServer(var1.getServerName());
                     if (var3 != null) {
                        ChannelService.this.createServerChannels(var3, var1, var1x, var2);
                        ChannelService.channelMap.put(var1, var1x);
                        ChannelService.channelsByName.put(var1, var2);
                     } else {
                        ChannelService.channelMap.put(var1, ChannelService.EMPTY_MAP);
                        ChannelService.channelsByName.put(var1, ChannelService.EMPTY_MAP);
                     }

                     return null;
                  }
               }
            });
         } catch (PrivilegedActionException var7) {
         } finally {
            this.inCreateChannels = false;
         }

      }
   }

   private ServerMBean getServer(String var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      return var2.lookupServer(var1);
   }

   private synchronized HashMap createServerChannels(ServerMBean var1, ServerIdentity var2, AbstractMap var3, AbstractMap var4) throws UnknownHostException {
      NetworkAccessPointMBean[] var5 = var1.getNetworkAccessPoints();
      HashMap var6 = new HashMap();

      for(int var7 = 0; var7 < var5.length; ++var7) {
         NetworkAccessPointMBean var8 = var5[var7];
         if (var2 != null && var2.isLocal()) {
            var8.addBeanUpdateListener(this);
         }

         if (var8.isEnabled()) {
            try {
               ServerChannelImpl var9 = new ServerChannelImpl(var8, var2);
               addServerChannel(var9, var6, var3, var4);
            } catch (Exception var17) {
               ServerLogger.logChannelConfigurationFailed(var8.getName(), var17);
            }
         }
      }

      if (ChannelHelper.isMultipleReplicationChannelsConfigured(var1)) {
         this.createAdditionalReplicationChannels(var1, var6, var3, var4);
      }

      Iterator var18 = ProtocolManager.iterator();

      while(true) {
         while(true) {
            Protocol var19;
            do {
               do {
                  if (!var18.hasNext()) {
                     ArrayList var24;
                     for(var18 = var6.entrySet().iterator(); var18.hasNext(); Collections.sort(var24)) {
                        Map.Entry var20 = (Map.Entry)var18.next();
                        var24 = (ArrayList)var20.getValue();
                        Collections.sort(var24);
                        boolean var25 = false;
                        boolean var11 = false;
                        boolean var12 = false;
                        boolean var13 = false;
                        boolean var14 = false;
                        Iterator var15 = var24.iterator();

                        while(true) {
                           while(var15.hasNext()) {
                              ServerChannelImpl var16 = (ServerChannelImpl)var15.next();
                              switch (var16.getProtocol().toByte()) {
                                 case 1:
                                 case 3:
                                    var11 = true;
                                    break;
                                 case 6:
                                    var12 = true;
                                    if (ProtocolManager.getRealProtocol(var16.getProtocol()) == ChannelService.PROTOCOL.HTTP || ProtocolManager.getRealProtocol(var16.getProtocol()) == ChannelService.PROTOCOL.HTTPS) {
                                       var11 = true;
                                    }
                                 case 2:
                                 case 4:
                                 case 5:
                                 case 7:
                                 case 8:
                                 case 9:
                                 default:
                                    if (var16.supportsHttp()) {
                                       var25 = true;
                                    }
                                    break;
                                 case 10:
                                 case 11:
                                    var13 = true;
                                    break;
                                 case 12:
                                 case 13:
                                    var14 = true;
                                    if (var16.supportsHttp()) {
                                       var25 = true;
                                    }
                              }
                           }

                           ServerChannelImpl var26;
                           if (!var14 && var1.getCluster() != null) {
                              var26 = (ServerChannelImpl)var24.get(0);
                              if (var26.supportsTLS()) {
                                 addServerChannel(var26.createImplicitChannel(ChannelService.PROTOCOL.CLUSTERS), var6, var3, var4);
                              } else {
                                 addServerChannel(var26.createImplicitChannel(ChannelService.PROTOCOL.CLUSTER), var6, var3, var4);
                              }
                           }

                           if (var12 && !var13) {
                              var26 = (ServerChannelImpl)var24.get(0);
                              if (var26.supportsTLS()) {
                                 addServerChannel(var26.createImplicitChannel(ChannelService.PROTOCOL.LDAPS), var6, var3, var4);
                              } else {
                                 addServerChannel(var26.createImplicitChannel(ChannelService.PROTOCOL.LDAP), var6, var3, var4);
                              }
                           }

                           if (var25 && !var11) {
                              var26 = (ServerChannelImpl)var24.get(0);
                              if (var26.supportsTLS()) {
                                 addServerChannel(var26.createImplicitChannel(ChannelService.PROTOCOL.HTTPS), var6, var3, var4);
                              } else {
                                 addServerChannel(var26.createImplicitChannel(ChannelService.PROTOCOL.HTTP), var6, var3, var4);
                              }
                           }
                           break;
                        }
                     }

                     var18 = var3.values().iterator();

                     ArrayList var21;
                     while(var18.hasNext()) {
                        var21 = (ArrayList)var18.next();
                        Collections.sort(var21);
                     }

                     var18 = var4.values().iterator();

                     while(var18.hasNext()) {
                        var21 = (ArrayList)var18.next();
                        Collections.sort(var21);
                     }

                     return var6;
                  }

                  var19 = (Protocol)var18.next();
               } while((var19.toByte() == 12 || var19.toByte() == 13) && var1.getCluster() == null);
            } while(!var19.isEnabled());

            if (var2 != null && var2.isLocal()) {
               ServerChannel var23 = var19.getHandler().getDefaultServerChannel();
               if (var23 != null && var23 instanceof ServerChannelImpl) {
                  ServerChannelImpl var10 = (ServerChannelImpl)var23;
                  if (var10.getConfig().isEnabled() && var19.getHandler().getPriority() >= 0) {
                     addServerChannel(var10, var6, var3, var4);
                  }
               }
            } else {
               NetworkAccessPointDefaultMBean var22 = new NetworkAccessPointDefaultMBean(var19, var1);
               if (var22.isEnabled()) {
                  addServerChannel(new ServerChannelImpl(var22, var19, var2), var6, var3, var4);
               }
            }
         }
      }
   }

   private void createAdditionalReplicationChannels(ServerMBean var1, AbstractMap<String, ArrayList> var2, AbstractMap<Protocol, ArrayList> var3, AbstractMap<String, ArrayList> var4) throws UnknownHostException {
      ClusterMBean var5 = var1.getCluster();
      String var6 = var5.getReplicationChannel();
      ArrayList var7 = (ArrayList)var4.get(var6);
      ServerChannelImpl var8 = (ServerChannelImpl)var7.get(0);
      boolean var9 = var5.isOneWayRmiForReplicationEnabled();

      try {
         int[] var10 = this.parseReplicationPorts(var1.getReplicationPorts());
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            ServerChannelImpl var13 = var8.cloneChannel(String.valueOf(var12 + 1));
            var13.listenPort = var10[var12];
            if (var13.publicPort > -1) {
               var13.publicPort = var13.listenPort;
            }

            if (var9) {
               var13.setT3SenderQueueDisabled(true);
            }

            replicationChannelNames.add(var13.getChannelName());
            addServerChannel(var13, var2, var3, var4);
         }
      } catch (NumberFormatException var14) {
         ServerLogger.logChannelConfigurationFailed(var6, new Exception("Failed to parse replication ports for server " + var1.getName() + " port range: " + var1.getReplicationPorts()));
      }

   }

   private int[] parseReplicationPorts(String var1) throws NumberFormatException {
      Object var2 = null;
      String[] var3 = var1.split("-");
      int[] var8;
      switch (var3.length) {
         case 1:
            var8 = new int[]{Integer.valueOf(var3[0].trim())};
            return var8;
         case 2:
            int var4 = Integer.valueOf(var3[0].trim());
            int var5 = Integer.valueOf(var3[1].trim());
            if (var5 - var4 > 0) {
               var8 = new int[var5 - var4 + 1];
               int var6 = var4;

               for(int var7 = 0; var6 <= var5; ++var7) {
                  var8[var7] = var6++;
               }

               return var8;
            }
         default:
            throw new NumberFormatException();
      }
   }

   public static List<String> getReplicationChannelNames() {
      return replicationChannelNames;
   }

   private static void addServerChannel(ServerChannelImpl var0, AbstractMap var1, AbstractMap var2, AbstractMap var3) throws UnknownHostException {
      if (var0.getConfig() != null && !var0.getConfig().isEnabled()) {
         if (DEBUG) {
            p("channel " + var0.toString() + " is not enabled");
         }

      } else if (var0.isLocal() && var0.getAddress() == null) {
         addServerChannel(new ServerChannelImpl(var0, AddressUtils.getIPAny(0, resolveDNS()), ""), var1, var2, var3);

         for(int var6 = 1; var6 < AddressUtils.getIPAny().length; ++var6) {
            addServerChannel(new ServerChannelImpl(var0, AddressUtils.getIPAny(var6, resolveDNS()), "[" + var6 + "]"), var1, var2, var3);
         }

      } else {
         if (DEBUG) {
            p("adding " + var0.toString());
         }

         String var4 = var0.getListenerKey();
         ArrayList var5;
         if (var1 != null) {
            var5 = (ArrayList)var1.get(var4);
            if (var5 == null) {
               var5 = new ArrayList();
               var1.put(var4, var5);
            }

            var5.add(var0);
         }

         var5 = (ArrayList)var2.get(var0.getProtocol());
         if (var5 == null) {
            var5 = new ArrayList();
            var2.put(var0.getProtocol(), var5);
         }

         var5.add(var0);
         var5 = (ArrayList)var3.get(var0.getChannelName());
         if (var5 == null) {
            var5 = new ArrayList();
            var3.put(var0.getChannelName(), var5);
         }

         var5.add(var0);
      }
   }

   private ServerChannelImpl[] expandServerChannels(ServerIdentity var1, ServerChannelImpl var2) throws IOException, UnknownHostException {
      if (var1.isLocal() && var2.getAddress() == null) {
         ServerChannelImpl[] var3 = new ServerChannelImpl[AddressUtils.getIPAny().length];
         var3[0] = new ServerChannelImpl(var2, AddressUtils.getIPAny(0, resolveDNS()), "");

         for(int var4 = 1; var4 < AddressUtils.getIPAny().length; ++var4) {
            var3[var4] = new ServerChannelImpl(var2, AddressUtils.getIPAny(var4, resolveDNS()), "[" + var4 + "]");
         }

         return var3;
      } else {
         return new ServerChannelImpl[]{var2};
      }
   }

   private ServerChannelImpl[] expandServerChannels(ServerIdentity var1, NetworkAccessPointMBean var2) throws IOException, UnknownHostException, UnknownProtocolException {
      return this.expandServerChannels(var1, new ServerChannelImpl(var2, var1));
   }

   private static boolean resolveDNS() {
      return runtimeAccess.getServer().isReverseDNSAllowed();
   }

   private void addLocalServerChannels(ServerChannelImpl var1) throws IOException, UnknownHostException, ManagementException {
      ServerChannelImpl[] var2 = this.expandServerChannels(LocalServerIdentity.getIdentity(), var1);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.addLocalServerChannel(var2[var3]);
      }

   }

   private ArrayList addLocalServerChannel(ServerChannelImpl var1) throws IOException, UnknownHostException, ManagementException {
      ServerIdentity var2 = LocalServerIdentity.getIdentity();
      var1.update(var2);
      ArrayList var3 = getDiscriminantSet(var1.getListenerKey());
      NetworkAccessPointMBean var4 = var1.getConfig();
      boolean var5 = var1.getProtocol() == ChannelService.PROTOCOL.ADMIN;
      boolean var6 = (var4.isHttpEnabledForThisProtocol() || var4.isTunnelingEnabled()) && (var5 || !(var4 instanceof NetworkAccessPointDefaultMBean)) && ProtocolManager.getRealProtocol(var1.getProtocol()) != ChannelService.PROTOCOL.HTTP && ProtocolManager.getRealProtocol(var1.getProtocol()) != ChannelService.PROTOCOL.HTTPS;
      boolean var7 = var4.isEnabled();
      if (var3 != null) {
         Iterator var8 = var3.iterator();

         label67:
         while(true) {
            while(true) {
               if (!var8.hasNext()) {
                  break label67;
               }

               ServerChannelImpl var9 = (ServerChannelImpl)var8.next();
               if (var9.getProtocol() == var1.getProtocol()) {
                  var9.update(var2);
                  var7 = false;
               } else if (var9.getProtocol() != ChannelService.PROTOCOL.HTTP && var9.getProtocol() != ChannelService.PROTOCOL.HTTPS) {
                  if (var9.getProtocol() == ChannelService.PROTOCOL.LDAP || var9.getProtocol() == ChannelService.PROTOCOL.LDAPS) {
                     var5 = false;
                  }
               } else if (!var1.supportsHttp() && var9.isImplicitChannel()) {
                  var8.remove();
                  getServerRuntime().removeServerChannelRuntime(var9.deleteRuntime());
               } else {
                  var6 = false;
               }
            }
         }
      }

      if (var7) {
         addServerChannel(var1, localChannels, localChannelsByProtocol, localChannelsByName);
         getServerRuntime().addServerChannelRuntime(var1.createRuntime());
      }

      ServerChannelImpl var10;
      if (var5) {
         if (var1.supportsTLS()) {
            var10 = var1.createImplicitChannel(ChannelService.PROTOCOL.LDAPS);
            addServerChannel(var10, localChannels, localChannelsByProtocol, localChannelsByName);
            getServerRuntime().addServerChannelRuntime(var10.createRuntime());
         } else {
            var10 = var1.createImplicitChannel(ChannelService.PROTOCOL.LDAP);
            addServerChannel(var10, localChannels, localChannelsByProtocol, localChannelsByName);
            getServerRuntime().addServerChannelRuntime(var10.createRuntime());
         }
      }

      if (var6) {
         if (var1.supportsTLS()) {
            var10 = var1.createImplicitChannel(ChannelService.PROTOCOL.HTTPS);
            addServerChannel(var10, localChannels, localChannelsByProtocol, localChannelsByName);
            getServerRuntime().addServerChannelRuntime(var10.createRuntime());
         } else {
            var10 = var1.createImplicitChannel(ChannelService.PROTOCOL.HTTP);
            addServerChannel(var10, localChannels, localChannelsByProtocol, localChannelsByName);
            getServerRuntime().addServerChannelRuntime(var10.createRuntime());
         }
      }

      return getDiscriminantSet(var1.getListenerKey());
   }

   private synchronized void updateLocalServerChannel(NetworkAccessPointMBean var1) {
      if (DEBUG) {
         p("updateLocalServerChannel(" + var1 + ")");
      }

      try {
         ServerChannelImpl[] var2 = this.expandServerChannels(LocalServerIdentity.getIdentity(), var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.addLocalServerChannel(var2[var3]);
            if (var1.getProtocol().equalsIgnoreCase("ADMIN")) {
               this.adminChannelCreationTime = System.currentTimeMillis();
               AdminPortService.getInstance().restartListener(var2[var3]);
            } else {
               DynamicListenThreadManager.getInstance().restartListener(var2[var3]);
            }
         }
      } catch (Exception var4) {
         ServerLogger.logChannelConfigurationFailed(var1.getName(), var4);
      }

   }

   private synchronized void removeLocalServerChannel(NetworkAccessPointMBean var1) {
      if (DEBUG) {
         p("removeLocalServerChannel(" + var1 + ")");
      }

      try {
         ServerIdentity var2 = LocalServerIdentity.getIdentity();
         ServerChannelImpl[] var3 = this.expandServerChannels(var2, var1);

         label82:
         for(int var4 = 0; var4 < var3.length; ++var4) {
            ArrayList var5 = getDiscriminantSet(var3[var4].getListenerKey());
            if (var5 != null) {
               if (var3[var4].getProtocol() == ChannelService.PROTOCOL.ADMIN) {
                  Iterator var12 = var5.iterator();

                  while(var12.hasNext()) {
                     this.removeLocalServerChannelMappings((ServerChannelImpl)var12.next());
                  }

                  var5.clear();
                  AdminPortService.getInstance().stopListener(var3[var4]);
               } else {
                  boolean var6 = false;
                  ServerChannelImpl var7 = null;
                  Iterator var8 = var5.iterator();

                  while(true) {
                     while(true) {
                        while(var8.hasNext()) {
                           ServerChannelImpl var9 = (ServerChannelImpl)var8.next();
                           if (!var9.equals(var3[var4]) && !var9.isImplicitChannel()) {
                              if (var9.getProtocol() != ChannelService.PROTOCOL.HTTP && var9.getProtocol() != ChannelService.PROTOCOL.HTTPS) {
                                 if (var9.supportsHttp()) {
                                    var7 = var9;
                                 }
                              } else {
                                 var6 = true;
                              }
                           } else {
                              var8.remove();
                              this.removeLocalServerChannelMappings(var9);
                           }
                        }

                        if (var7 != null && !var6) {
                           ServerChannelImpl var13;
                           if (var3[var4].supportsTLS()) {
                              var13 = var7.createImplicitChannel(ChannelService.PROTOCOL.HTTPS);
                           } else {
                              var13 = var7.createImplicitChannel(ChannelService.PROTOCOL.HTTP);
                           }

                           var5.add(var13);

                           try {
                              getServerRuntime().addServerChannelRuntime(var13.createRuntime());
                           } catch (ManagementException var10) {
                              ServerLogger.logChannelConfigurationFailed(var13.getChannelName(), var10);
                           }
                        }

                        DynamicListenThreadManager.getInstance().restartListener(var3[var4]);
                        continue label82;
                     }
                  }
               }
            }
         }
      } catch (Exception var11) {
         ServerLogger.logChannelConfigurationFailed(var1.getName(), var11);
      }

   }

   private synchronized void removeDefaultServerChannel(ServerChannelImpl var1) {
      ArrayList var2 = (ArrayList)localChannels.remove(var1.getListenerKey());
      if (DEBUG && var2 == null) {
         p("Could not remove: " + var1);
      }

      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            this.removeLocalServerChannelMappings((ServerChannelImpl)var3.next());
         }
      }

   }

   private void removeLocalServerChannelMappings(ServerChannelImpl var1) {
      if (DEBUG) {
         p("removing " + var1.toString());
      }

      Object var2 = localChannelsByName.remove(var1.getChannelName());
      if (DEBUG && var2 == null) {
         p("Could not remove: " + var1);
      }

      if (var1.getRuntime() != null) {
         getServerRuntime().removeServerChannelRuntime(var1.deleteRuntime());
      }

      ArrayList var3 = (ArrayList)localChannelsByProtocol.get(var1.getProtocol());
      if (var3 != null) {
         var3.remove(var1);
      }

   }

   private void restartChannelSet(ServerChannelImpl[] var1) throws IOException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (DEBUG) {
            p("restartChannelSet(" + var1[var2] + ")");
         }

         DynamicListenThreadManager.getInstance().restartListener(var1[var2]);
      }

   }

   private synchronized void updateDefaultChannels(boolean var1) {
      try {
         this.stopDefaultChannels(var1);
         ArrayList var2 = new ArrayList();
         ServerChannelImpl[] var3 = this.updateAndGetDefaultChannels(var1, var2);
         if (var3 != null && var3.length > 0) {
            if (this.isChannelListenKeyInUse(var3[0], !var1)) {
               this.stopDefaultChannels(!var1);
               this.updateAndGetDefaultChannels(!var1, (ArrayList)null);
            }

            this.addLocalServerChannels(var2);
            this.restartChannelSet(var3);
         }
      } catch (IOException var4) {
         ServerLogger.logProtocolNotConfigured(var4.getMessage());
      }

   }

   private boolean isChannelListenKeyInUse(ServerChannel var1, boolean var2) {
      boolean var3 = false;
      if (var1 != null) {
         ArrayList var4 = getDiscriminantSet(var1.getListenerKey());
         if (var4 != null) {
            ServerChannel[] var5 = (ServerChannel[])((ServerChannel[])var4.toArray(new ServerChannel[0]));
            if (var5 != null) {
               int var6 = 0;

               while(var6++ < var5.length && !var3) {
                  if (var5[var6] != null && var5[var6].supportsTLS() == var2) {
                     var3 = true;
                  }
               }
            }
         }
      }

      return var3;
   }

   private ServerChannelImpl[] updateAndGetDefaultChannels(boolean var1, ArrayList var2) throws IOException {
      ServerChannelImpl[] var3 = null;
      Iterator var4 = ProtocolManager.iterator();

      while(true) {
         Protocol var5;
         ServerChannelImpl var6;
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var4.hasNext()) {
                              return var3;
                           }

                           var5 = (Protocol)var4.next();
                        } while(!var5.isEnabled());
                     } while(var5.isSecure() != var1);
                  } while(var5.getQOS() == 103);

                  var6 = (ServerChannelImpl)var5.getHandler().getDefaultServerChannel();
               } while(var6 == null);
            } while(!var6.getConfig().isEnabled());
         } while(var5.getHandler().getPriority() < 0);

         var6.update(LocalServerIdentity.getIdentity());
         var3 = this.expandServerChannels(LocalServerIdentity.getIdentity(), var6);

         for(int var7 = 0; var7 < var3.length; ++var7) {
            if (var2 != null) {
               var2.add(var3[var7]);
            }
         }
      }
   }

   private void addLocalServerChannels(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ServerChannelImpl var3 = (ServerChannelImpl)var2.next();

         try {
            this.addLocalServerChannel(var3);
         } catch (Exception var5) {
            ServerLogger.logChannelConfigurationFailed(var3.getChannelName(), var5);
         }
      }

   }

   private synchronized void stopDefaultChannels(boolean var1) {
      try {
         Iterator var2 = ProtocolManager.iterator();

         while(var2.hasNext()) {
            Protocol var3 = (Protocol)var2.next();
            if (var3.isEnabled() && var3.isSecure() == var1) {
               ServerChannel var4 = var3.getHandler().getDefaultServerChannel();
               if (DEBUG) {
                  p("stopDefaultChannels(" + var3 + ")");
               }

               if (var3.getHandler().getPriority() >= 0 && var4 != null) {
                  ServerChannelImpl[] var5 = this.expandServerChannels(LocalServerIdentity.getIdentity(), (ServerChannelImpl)var4);

                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     DynamicListenThreadManager.getInstance().stopListener(var5[var6]);
                     this.removeDefaultServerChannel(var5[var6]);
                  }

                  return;
               }
            }
         }
      } catch (UnknownHostException var7) {
         ServerLogger.logProtocolNotConfigured(var7.getMessage());
      } catch (IOException var8) {
         ServerLogger.logProtocolNotConfigured(var8.getMessage());
      }

   }

   private synchronized void startDefaultAdminChannel() {
      if (DEBUG) {
         p("startDefaultAdminChannel()");
      }

      ServerChannelImpl var1 = (ServerChannelImpl)ProtocolHandlerAdmin.getProtocolHandler().getDefaultServerChannel();

      try {
         var1.update(LocalServerIdentity.getIdentity());
         this.addLocalServerChannels(var1);
         EnableAdminListenersService.getInstance().start();
      } catch (Exception var3) {
         ServerLogger.logChannelConfigurationFailed(var1.getChannelName(), var3);
      }

      this.resetQOS();
      this.adminChannelCreationTime = System.currentTimeMillis();
   }

   private synchronized void stopDefaultAdminChannel() {
      if (DEBUG) {
         p("stopDefaultAdminChannel()");
      }

      AdminPortService.getInstance().halt();
      ServerChannelImpl var1 = (ServerChannelImpl)ProtocolHandlerAdmin.getProtocolHandler().getDefaultServerChannel();

      try {
         ServerChannelImpl[] var2 = this.expandServerChannels(LocalServerIdentity.getIdentity(), var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.removeDefaultServerChannel(var2[var3]);
         }

         var1.update(LocalServerIdentity.getIdentity());
      } catch (Exception var4) {
         ServerLogger.logChannelConfigurationFailed(var1.getChannelName(), var4);
      }

      this.resetQOS();
      this.adminChannelCreationTime = 0L;
   }

   public static Map getLocalServerChannels() {
      return localChannels;
   }

   public static void exportServerChannels(ServerIdentity var0, ObjectOutput var1) throws IOException {
      ArrayList var2 = new ArrayList();
      AbstractMap var3 = (AbstractMap)channelMap.get(var0);
      Iterator var4;
      if (var3 != null) {
         var4 = var3.values().iterator();

         while(var4.hasNext()) {
            ArrayList var5 = (ArrayList)var4.next();
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               var2.add(var6.next());
            }
         }
      }

      var1.writeInt(var2.size());
      var4 = var2.iterator();

      while(var4.hasNext()) {
         ServerChannel var7 = (ServerChannel)var4.next();
         var1.writeObject(var7);
         if (DEBUG) {
            p("Exported: " + var7);
         }
      }

   }

   public static boolean hasChannels(ServerIdentity var0) {
      AbstractMap var1 = (AbstractMap)channelMap.get(var0);
      return var1 != null && !var1.isEmpty();
   }

   public static void importServerChannels(ServerIdentity var0, ObjectInput var1) throws IOException {
      if (!var0.isLocal() && !LocalServerIdentity.getIdentity().getPersistentIdentity().equals(var0.getPersistentIdentity())) {
         importNonLocalServerChannels(var0, var1);
      } else {
         int var2 = var1.readInt();

         while(var2-- > 0) {
            try {
               var1.readObject();
            } catch (ClassNotFoundException var4) {
            }
         }
      }

   }

   public static void importNonLocalServerChannels(ServerIdentity var0, ObjectInput var1) throws IOException {
      int var2 = var1.readInt();
      ConcurrentHashMap var3 = new ConcurrentHashMap(31);
      ConcurrentHashMap var4 = new ConcurrentHashMap(31);

      for(int var5 = 0; var5 < var2; ++var5) {
         try {
            addServerChannel((ServerChannelImpl)var1.readObject(), (AbstractMap)null, var3, var4);
         } catch (ClassNotFoundException var7) {
         }
      }

      if (DEBUG) {
         p("Imported: " + var0 + ":\n" + var3.toString());
         p("Imported: " + var0 + ":\n" + var4.toString());
      }

      channelMap.put(var0, var3);
      channelsByName.put(var0, var4);
      ServerIdentityManager.recordIdentity(var0);
   }

   private void updateConnectedServers() {
      String[] var1 = URLManager.getConnectedServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!var1[var2].equals(AdminServerIdentity.getIdentity().getServerName()) && !var1[var2].equals(LocalServerIdentity.getIdentity().getServerName())) {
            try {
               String var3 = URLManager.findAdministrationURL(var1[var2]);
               if (var3 != null) {
                  Environment var4 = new Environment();
                  var4.setProviderUrl(var3);
                  RemoteChannelService var5 = (RemoteChannelService)PortableRemoteObject.narrow(var4.getInitialReference(RemoteChannelServiceImpl.class), RemoteChannelService.class);
                  var5.updateServer(LocalServerIdentity.getIdentity().getServerName(), ServerChannelManager.getLocalChannelsForExport());
               }
            } catch (RemoteException var6) {
               ServerLogger.logServerUpdateFailed(var1[var2]);
            } catch (NamingException var7) {
               ServerLogger.logServerUpdateFailed(var1[var2]);
            }
         }
      }

   }

   protected ChannelList getLocalChannelList() {
      return new ChannelListImpl();
   }

   public static ArrayList getDiscriminantSet(String var0) {
      return (ArrayList)localChannels.get(var0);
   }

   public static boolean isLocalChannel(InetAddress var0, int var1) {
      String var2 = var0.getHostAddress();
      if (InetAddressHelper.isIPV6Address(var2)) {
         var2 = "[" + var2 + "]";
      }

      boolean var3 = localChannels.get(var0.getHostName().toLowerCase() + var1) != null || localChannels.get(var2 + var1) != null;
      if (!var3 && var0.isLoopbackAddress() && !AddressUtils.getLocalHost().isLoopbackAddress()) {
         return isLocalChannel(AddressUtils.getLocalHost(), var1);
      } else {
         if (DEBUG) {
            p("isLocalChannel(" + var0 + ", " + var1 + "): " + var3);
         }

         return var3;
      }
   }

   public static boolean isLocalChannel(String var0) {
      try {
         URI var1 = new URI(var0);
         return isLocalChannel(InetAddress.getByName(var1.getHost()), var1.getPort());
      } catch (URISyntaxException var2) {
         return false;
      } catch (UnknownHostException var3) {
         return false;
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      if (DEBUG) {
         p("prepareUpdate() " + var1.getSourceBean() + ", event dump:");
         BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            p(" " + var2[var3].getPropertyName());
         }
      }

      ServerMBean var6 = null;
      if (var1.getProposedBean() instanceof ServerMBean) {
         var6 = (ServerMBean)var1.getProposedBean();
      } else if (var1.getProposedBean() instanceof NetworkAccessPointMBean) {
         var6 = (ServerMBean)((NetworkAccessPointMBean)var1.getProposedBean()).getParent();
      } else if (var1.getProposedBean() instanceof SSLMBean) {
         var6 = (ServerMBean)((SSLMBean)var1.getProposedBean()).getParent();
      }

      if (var6 != null && !ChannelHelper.checkConsistency(var6)) {
         throw new BeanUpdateRejectedException("Inconsistent channel updates");
      } else {
         if (var1.getSourceBean() instanceof ServerMBean) {
            ServerMBean var7 = (ServerMBean)var1.getSourceBean();
            ServerMBean var4 = (ServerMBean)var1.getProposedBean();
            if (var7.getMaxOpenSockCount() < 0 && var4.getMaxOpenSockCount() >= 0 || var7.getMaxOpenSockCount() >= 0 && var4.getMaxOpenSockCount() < 0) {
               throw new BeanUpdateRejectedException("Cannot change MaxOpenSockCount enablement dynamically");
            }
         }

         BeanUpdateEvent.PropertyUpdate[] var8 = var1.getUpdateList();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            if (var1.getSourceBean() instanceof ServerMBean) {
               if (var8[var9].getAddedObject() instanceof NetworkAccessPointMBean) {
                  NetworkAccessPointMBean var5 = (NetworkAccessPointMBean)var8[var9].getAddedObject();
                  if (var5.getProtocol().equalsIgnoreCase("ADMIN") && !ChannelHelper.isLocalAdminChannelEnabled() && !AdminServerIdentity.getIdentity().isLocal()) {
                     throw new BeanUpdateRejectedException("Cannot dynamically enable adminstration port on Managed servers when they are running");
                  }
               }
            } else if (var1.getSourceBean() instanceof DomainMBean && var8[var9].getPropertyName().equals("AdministrationPortEnabled") && ((DomainMBean)var1.getProposedBean()).isAdministrationPortEnabled() != ChannelHelper.isLocalAdminChannelEnabled() && !AdminServerIdentity.getIdentity().isLocal()) {
               throw new BeanUpdateRejectedException("Cannot dynamically enable adminstration port on Managed servers when they are running");
            }
         }

      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      HashSet var2 = new HashSet();
      BeanUpdateEvent.PropertyUpdate[] var3;
      int var4;
      if (DEBUG) {
         p("activateUpdate() " + var1.getSourceBean() + ", event dump:");
         var3 = var1.getUpdateList();

         for(var4 = 0; var4 < var3.length; ++var4) {
            p(" " + var3[var4].getPropertyName());
         }
      }

      String var5;
      if (var1.getSourceBean() instanceof ServerMBean) {
         var3 = var1.getUpdateList();

         for(var4 = 0; var4 < var3.length; ++var4) {
            NetworkAccessPointMBean var11;
            if (var3[var4].getAddedObject() instanceof NetworkAccessPointMBean) {
               var11 = (NetworkAccessPointMBean)var3[var4].getAddedObject();
               if (DEBUG) {
                  p("adding: " + var11);
               }

               var11.addBeanUpdateListener(this);
               if (var11.isEnabled()) {
                  var2.add(var11);
               }
            } else if (var3[var4].getRemovedObject() instanceof NetworkAccessPointMBean) {
               var11 = (NetworkAccessPointMBean)var3[var4].getRemovedObject();
               if (DEBUG) {
                  p("remove: " + var11);
               }

               this.removeLocalServerChannel(var11);
               var2.add(new Object());
            } else {
               var5 = var3[var4].getPropertyName();
               if (!var5.equals("KeyStores") && !var5.equals("CustomIdentityKeyStoreFileName") && !var5.equals("CustomIdentityKeyStoreType") && !var5.equals("CustomIdentityKeyStorePassPhrase") && !var5.equals("CustomTrustKeyStoreFileName") && !var5.equals("CustomTrustKeyStoreType") && !var5.equals("CustomTrustKeyStorePassPhrase") && !var5.equals("JavaStandardTrustKeyStorePassPhrase")) {
                  if (var5.equals("ListenPortEnabled") && !((ServerMBean)var1.getSourceBean()).isListenPortEnabled()) {
                     this.stopDefaultChannels(false);
                     var2.add(new Object());
                  } else if (!var5.equals("ListenPortEnabled") && !var5.equals("ListenPort")) {
                     if (var5.equals("ListenAddress")) {
                        var2.add(var1.getSourceBean());
                        var2.add(((ServerMBean)var1.getSourceBean()).getSSL());
                     } else if (var5.equals("AdministrationPort")) {
                        var2.add(var1.getSourceBean());
                     } else if (var5.equals("MaxOpenSockCount")) {
                        ServerThrottle.getServerThrottle().changeMaxOpenSockets(((ServerMBean)var1.getSourceBean()).getMaxOpenSockCount());
                     }
                  } else {
                     var2.add(var1.getSourceBean());
                  }
               } else {
                  var2.add(((ServerMBean)var1.getSourceBean()).getSSL());
               }
            }
         }
      } else if (var1.getSourceBean() instanceof NetworkAccessPointMBean) {
         NetworkAccessPointMBean var8 = (NetworkAccessPointMBean)var1.getSourceBean();
         if (DEBUG) {
            p("updating: " + var8);
         }

         BeanUpdateEvent.PropertyUpdate[] var10 = var1.getUpdateList();

         for(int var12 = 0; var12 < var10.length; ++var12) {
            String var6 = var10[var12].getPropertyName();
            if (var6.equals("Enabled")) {
               if (!var8.isEnabled()) {
                  this.removeLocalServerChannel(var8);
                  var2.remove(var8);
               } else {
                  var2.add(var8);
               }
               break;
            }

            if (var6.equals("AcceptBacklog") || var6.equals("HttpEnabledForThisProtocol") || var6.equals("TunnelingEnabled") || var6.equals("OutboundEnabled") || var6.equals("TwoWaySSLEnabled") || var6.equals("PrivateKeyAlias") || var6.equals("PrivateKeyPassPhrase") || var6.equals("ClientCertificateEnforced")) {
               var2.add(var8);
            }
         }
      } else if (var1.getSourceBean() instanceof SSLMBean) {
         if (!((SSLMBean)var1.getSourceBean()).isEnabled()) {
            this.stopDefaultChannels(true);
         } else {
            var2.add(var1.getSourceBean());
         }
      } else if (var1.getSourceBean() instanceof DomainMBean) {
         var3 = var1.getUpdateList();

         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4].getPropertyName();
            if (var5.equals("AdministrationPortEnabled") || var5.equals("AdministrationPort")) {
               var2.add(var1.getSourceBean());
            }
         }
      }

      if (var2.size() > 0) {
         SSLContextManager.clearSSLContextCache();
         Iterator var9 = var2.iterator();

         while(var9.hasNext()) {
            Object var13 = var9.next();
            if (var13 instanceof NetworkAccessPointMBean) {
               this.updateLocalServerChannel((NetworkAccessPointMBean)var13);
            } else if (var13 instanceof ServerMBean) {
               this.updateDefaultChannels(false);
            } else if (var13 instanceof SSLMBean) {
               this.updateDefaultChannels(true);
            } else if (var13 instanceof DomainMBean) {
               this.stopDefaultAdminChannel();
               if (((DomainMBean)var13).isAdministrationPortEnabled()) {
                  this.startDefaultAdminChannel();
               }
            }
         }

         try {
            if (!AdminServerIdentity.getIdentity().isLocal()) {
               RemoteChannelServiceImpl.getDomainGateway().updateServer(LocalServerIdentity.getIdentity().getServerName(), ServerChannelManager.getLocalChannelsForExport());
            }
         } catch (RemoteException var7) {
            ServerLogger.logServerUpdateFailed(AdminServerIdentity.getIdentity().getServerName());
         }

         if (ClusterService.getServices() != null) {
            ClusterService.getServices().resendLocalAttributes();
         } else {
            this.updateConnectedServers();
         }
      }

   }

   public void restartSSLChannels() {
      SSLContextManager.clearSSLContextCache();
      this.updateDefaultChannels(true);
      NetworkAccessPointMBean[] var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getNetworkAccessPoints();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         NetworkAccessPointMBean var3 = var1[var2];
         byte var4 = ProtocolManager.getProtocolByName(var3.getProtocol()).getQOS();
         if (var3.isEnabled() && (var4 == 102 || var4 == 103)) {
            this.updateLocalServerChannel(var3);
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      if (DEBUG) {
         p("rollbackUpdate() " + var1.getSourceBean());
      }

   }

   public static void removeChannelEntries(String var0) {
      removeMapEntriesForServer(channelMap, var0);
      removeMapEntriesForServer(channelsByName, var0);
   }

   private static void removeMapEntriesForServer(ConcurrentHashMap var0, String var1) {
      if (var0 != null) {
         Iterator var2 = var0.entrySet().iterator();
         String var3 = LocalServerIdentity.getIdentity().getDomainName();

         while(var2.hasNext()) {
            Map.Entry var4 = (Map.Entry)var2.next();
            Object var5 = var4.getKey();
            if (var5 instanceof ServerIdentity) {
               ServerIdentity var6 = (ServerIdentity)var5;
               if (StringUtils.strcmp(var6.getDomainName(), var3) && StringUtils.strcmp(var6.getServerName(), var1)) {
                  var2.remove();
               }
            }
         }

      }
   }

   public static void removeChannelEntries(ServerIdentity var0) {
      channelMap.remove(var0);
      channelsByName.remove(var0);
   }

   private static void invalidateChannelEntriesInDomain() {
   }

   private static ServerChannel findOutboundServerChannelInternal(Protocol var0, String var1) throws IOException {
      Debug.assertion(initialized && (var0 != null || var1 != null));
      ArrayList var2;
      Iterator var3;
      ServerChannelImpl var4;
      if (var1 != null) {
         var2 = (ArrayList)localChannelsByName.get(var1);
         if (var2 != null) {
            label94: {
               var3 = var2.iterator();

               do {
                  do {
                     if (!var3.hasNext()) {
                        break label94;
                     }

                     var4 = (ServerChannelImpl)var3.next();
                  } while(!var4.getConfig().isOutboundEnabled());
               } while(var0 != null && var4.getProtocol() != var0);

               return var4;
            }
         }
      }

      if (var0 == null) {
         return null;
      } else {
         var2 = (ArrayList)localChannelsByProtocol.get(var0);
         if (var2 != null) {
            var3 = var2.iterator();

            while(var3.hasNext()) {
               var4 = (ServerChannelImpl)var3.next();
               if (var4.getConfig().isOutboundEnabled() && !var4.isSDPEnabled() && !var4.isT3SenderQueueDisabled()) {
                  return var4;
               }
            }

            if (var0 == ChannelService.PROTOCOL.ADMIN) {
               var3 = var2.iterator();

               while(var3.hasNext()) {
                  var4 = (ServerChannelImpl)var3.next();
                  if (var4.getConfig().isOutboundEnabled()) {
                     return var4;
                  }
               }
            }
         }

         if (!var0.isEnabled()) {
            throw new IOException("No configured outbound channel for " + var0.getProtocolName());
         } else {
            return var0.getHandler().getDefaultServerChannel();
         }
      }
   }

   static ServerChannel findInboundServerChannel(Protocol var0, String var1) {
      if (!initialized) {
         return null;
      } else {
         ArrayList var2 = (ArrayList)localChannelsByProtocol.get(var0);
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ServerChannel var4 = (ServerChannel)var3.next();
               if (var1 == null || var4.getChannelName().equals(var1)) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   static ServerChannel findInboundServerChannel(Protocol var0) {
      return findInboundServerChannel(var0, (String)null);
   }

   public static ArrayList findInboundServerChannels(Protocol var0) {
      Debug.assertion(initialized);
      return (ArrayList)localChannelsByProtocol.get(var0);
   }

   public static ArrayList getInboundServerChannels() {
      Debug.assertion(initialized);
      ArrayList var0 = new ArrayList();
      Iterator var1 = localChannelsByProtocol.values().iterator();

      while(var1.hasNext()) {
         var0.addAll((ArrayList)var1.next());
      }

      return var0;
   }

   public static InetSocketAddress findServerAddress(Protocol var0) {
      ServerChannel var1 = findInboundServerChannel(var0);
      return var1 == null ? null : new InetSocketAddress(var1.getPublicAddress(), var1.getPublicPort());
   }

   public static InetSocketAddress findServerAddress(String var0) {
      Protocol var1 = ProtocolManager.getProtocolByName(var0);
      return findServerAddress(var1);
   }

   private static final ServerRuntime getServerRuntime() {
      return (ServerRuntime)ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }

   private static void p(String var0) {
      System.out.println("<ChannelService>: " + var0);
   }

   public long getAdminChannelCreationTime() {
      return this.adminChannelCreationTime;
   }

   public ServerChannel getServerChannel(HostID var1) {
      if (var1 != LocalServerIdentity.getIdentity()) {
         throw new AssertionError("Only local HostID is supported" + var1);
      } else {
         return ServerChannelManager.findDefaultLocalServerChannel();
      }
   }

   protected ServerChannel getServerChannel(HostID var1, Protocol var2) {
      return this.getServerChannel(var1, var2, (String)null);
   }

   protected ServerChannel getIPv4ServerChannel(HostID var1, Protocol var2) {
      return this.getServerChannel(var1, var2, false);
   }

   protected ServerChannel getIPv6ServerChannel(HostID var1, Protocol var2) {
      return this.getServerChannel(var1, var2, true);
   }

   private ServerChannel getServerChannel(HostID var1, Protocol var2, boolean var3) {
      Debug.assertion(var1 != null);
      if (DEBUG_MAP) {
         ConcurrentHashMap var4 = (ConcurrentHashMap)channelMap.get(var1);
         p("getServerChannel(" + var1 + ", " + var2 + ") =>\n" + var4.toString());
      }

      AbstractMap var10 = this.findOrCreateChannels((ServerIdentity)var1);
      if (var10 != null) {
         ArrayList var5 = (ArrayList)var10.get(var2);
         if (var5 != null) {
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               ServerChannel var7 = (ServerChannel)var6.next();

               try {
                  InetAddress var8 = InetAddress.getByName(var7.getPublicAddress());
                  if (var8 instanceof Inet6Address) {
                     if (var3) {
                        if (DEBUG_MAP) {
                           p("getServerChannel(" + var1 + ", " + var2 + ", ipv6Only) returned " + var7);
                        }

                        return var7;
                     }
                  } else if (!var3) {
                     if (DEBUG_MAP) {
                        p("getServerChannel(" + var1 + ", " + var2 + ", ipv4Only) returned " + var7);
                     }

                     return var7;
                  }
               } catch (UnknownHostException var9) {
               }
            }
         }
      }

      if (DEBUG_MAP) {
         p("getServerChannel(" + var1 + ", " + var2 + ") returned NULL");
      }

      return null;
   }

   protected ServerChannel getServerChannel(HostID var1, Protocol var2, String var3) {
      Debug.assertion(var1 != null);
      if (DEBUG_MAP) {
         ConcurrentHashMap var4 = (ConcurrentHashMap)channelMap.get(var1);
         p("getServerChannel(" + var1 + ", " + var2 + ") =>\n" + var4.toString());
      }

      AbstractMap var8 = this.findOrCreateChannels((ServerIdentity)var1);
      if (var8 != null) {
         ArrayList var5 = (ArrayList)var8.get(var2);
         if (var5 != null) {
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               ServerChannel var7 = (ServerChannel)var6.next();
               if (var3 == null && !var7.isSDPEnabled() && !var7.isT3SenderQueueDisabled() || var7.getChannelName().equals(var3)) {
                  if (DEBUG_MAP) {
                     p("getServerChannel(" + var1 + ", " + var2 + ") returned " + var7);
                  }

                  return var7;
               }
            }
         }
      }

      if (DEBUG_MAP) {
         p("getServerChannel(" + var1 + ", " + var2 + ") returned NULL");
      }

      return null;
   }

   public ServerChannel getConfiguredServerChannel(ServerMBean var1, Protocol var2) {
      try {
         ConcurrentHashMap var3 = new ConcurrentHashMap(31);
         this.createServerChannels(var1, (ServerIdentity)null, var3, new ConcurrentHashMap(31));
         ArrayList var4 = (ArrayList)var3.get(var2);
         if (var4 != null) {
            Iterator var5 = var4.iterator();
            if (var5.hasNext()) {
               return (ServerChannel)var5.next();
            }
         }
      } catch (UnknownHostException var6) {
      }

      return null;
   }

   protected ServerChannel getServerChannel(HostID var1, String var2) {
      Debug.assertion(var1 != null);
      AbstractMap var3 = this.findOrCreateNamedChannels((ServerIdentity)var1);
      if (var3 != null) {
         ArrayList var4 = (ArrayList)var3.get(var2);
         if (DEBUG_MAP) {
            p("getServerChannel(" + var1 + ", " + var2 + ") => " + var4);
         }

         if (var4 != null) {
            Iterator var5 = var4.iterator();
            if (var5.hasNext()) {
               return (ServerChannel)var5.next();
            }
         }
      }

      return null;
   }

   protected ServerChannel getAvailableServerChannel(HostID var1, String var2) {
      Debug.assertion(var1 != null);
      AbstractMap var3 = this.findNamedChannels((ServerIdentity)var1);
      if (var3 != null) {
         ArrayList var4 = (ArrayList)var3.get(var2);
         if (DEBUG_MAP) {
            p("getServerChannel(" + var1 + ", " + var2 + ") => " + var4);
         }

         if (var4 != null) {
            Iterator var5 = var4.iterator();
            if (var5.hasNext()) {
               return (ServerChannel)var5.next();
            }
         }
      }

      return null;
   }

   private AbstractMap findNamedChannels(ServerIdentity var1) {
      if (var1.isClient()) {
         return null;
      } else {
         AbstractMap var2 = (AbstractMap)channelsByName.get(var1);
         return var2;
      }
   }

   protected ServerChannel getRelatedServerChannel(HostID var1, Protocol var2, String var3) {
      Debug.assertion(initialized);
      AbstractMap var4 = this.findOrCreateChannels((ServerIdentity)var1);
      if (var4 != null) {
         ArrayList var5 = (ArrayList)var4.get(var2);
         if (var5 != null) {
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               ServerChannel var7 = (ServerChannel)var6.next();
               if (var3.equals(var7.getPublicAddress())) {
                  return var7;
               }
            }
         }
      }

      return null;
   }

   protected ServerChannel getOutboundServerChannel(Protocol var1, String var2) {
      try {
         return findOutboundServerChannelInternal(var1, var2);
      } catch (IOException var4) {
         return null;
      }
   }

   public static void dumpTables() {
      System.out.println("Local channels:");
      System.out.println(localChannels.toString());
      System.out.println("Domain channels by protocol:");
      System.out.println(channelMap.toString());
      System.out.println("Domain channels by name:");
      System.out.println(channelsByName.toString());
   }

   private static class DisconnectListener implements weblogic.rmi.extensions.DisconnectListener {
      private DisconnectListener() {
      }

      public void onDisconnect(DisconnectEvent var1) {
         if (var1 instanceof ServerDisconnectEvent) {
            String var2 = ((ServerDisconnectEvent)var1).getServerName();
            ChannelService.removeChannelEntries(var2);
         }

      }

      // $FF: synthetic method
      DisconnectListener(Object var1) {
         this();
      }
   }

   private static class ConnectListener implements weblogic.rmi.extensions.ConnectListener {
      private ConnectListener() {
      }

      public void onConnect(ConnectEvent var1) {
      }

      // $FF: synthetic method
      ConnectListener(Object var1) {
         this();
      }
   }

   private static final class PROTOCOL {
      private static final Protocol LDAP = ProtocolManager.getProtocolByIndex(10);
      private static final Protocol LDAPS = ProtocolManager.getProtocolByIndex(11);
      private static final Protocol HTTP = ProtocolManager.getProtocolByIndex(1);
      private static final Protocol HTTPS = ProtocolManager.getProtocolByIndex(3);
      private static final Protocol ADMIN = ProtocolManager.getProtocolByIndex(6);
      private static final Protocol CLUSTER = ProtocolManager.getProtocolByIndex(12);
      private static final Protocol CLUSTERS = ProtocolManager.getProtocolByIndex(13);
      private static final Protocol SNMP = ProtocolManager.getProtocolByIndex(17);
   }
}
