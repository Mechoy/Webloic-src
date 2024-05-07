package weblogic.ejb.container.ejbc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.dd.ClusteringDescriptor;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.IIOPSecurityDescriptor;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.rmi.rmic.RmicMethodDescriptor;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;
import weblogic.utils.annotation.BeaSynthetic.Helper;

final class RMICOptions {
   private static final DebugLogger debugLogger;
   static final String INTEGRITY_OPT = "integrity";
   static final String CONFIDENTIALITY_OPT = "confidentiality";
   static final String CLIENTCERTAUTHENTICATION_OPT = "clientCertAuthentication";
   static final String CLIENTAUTHENTICATION_OPT = "clientAuthentication";
   static final String IDENTITYASSERTION_OPT = "identityAssertion";
   private final boolean noMangledNames = true;
   private final boolean noExit = true;
   private boolean clusterable = false;
   private boolean methodsAreIdempotent = false;
   private boolean propagateEnvironment = false;
   private String loadAlgorithm = null;
   private String callRouter = null;
   private String replicaHandler = null;
   private String outputDirectory = null;
   private String dgcPolicy = null;
   private String dispatchPolicy = null;
   private boolean stickToFirstServer = false;
   private boolean useServersideStubs = false;
   private String remoteRefClassName = null;
   private boolean activatable = false;
   private String integrity = null;
   private String confidentiality = null;
   private String clientCertAuthentication = null;
   private String clientAuthentication = null;
   private String identityAssertion = null;
   private BeanInfo bd;
   private ClusteringDescriptor cd;
   private Collection<RmicMethodDescriptor> rmicMethodDescriptors;

   RMICOptions(BeanInfo var1) {
      Debug.assertion(var1 instanceof ClientDrivenBeanInfo);
      this.bd = var1;
      this.cd = ((ClientDrivenBeanInfo)var1).getClusteringDescriptor();
   }

   private String homeLoadAlgorithm() {
      return rmiLoadAlgorithmOption(this.cd.getHomeLoadAlgorithm());
   }

   private String homeCallRouter() {
      return this.cd.getHomeCallRouterClassName();
   }

   private boolean isStatefulSession() {
      if (this.bd instanceof SessionBeanInfo) {
         SessionBeanInfo var1 = (SessionBeanInfo)this.bd;
         return var1.isStateful();
      } else {
         return false;
      }
   }

   private boolean isStateless() {
      if (this.bd instanceof SessionBeanInfo) {
         SessionBeanInfo var1 = (SessionBeanInfo)this.bd;
         return !var1.isStateful();
      } else {
         return false;
      }
   }

   private boolean isEntityBean() {
      return this.bd instanceof EntityBeanInfo;
   }

   private String statelessBeanLoadAlgorithm() {
      return rmiLoadAlgorithmOption(this.cd.getStatelessBeanLoadAlgorithm());
   }

   private String statelessBeanCallRouter() {
      return this.cd.getStatelessBeanCallRouterClassName();
   }

   private boolean homeIsClusterable() {
      return this.cd.getHomeIsClusterable();
   }

   private boolean isUseServersideStubs() {
      return this.cd.getUseServersideStubs();
   }

   private boolean isStatefulBeanClusterable() {
      if (this.isStatefulSession()) {
         SessionBeanInfo var1 = (SessionBeanInfo)this.bd;
         return var1.getReplicationType() != 1;
      } else {
         return false;
      }
   }

   private boolean isStatelessBeanClusterable() {
      return this.isStateless() && this.cd.getStatelessBeanIsClusterable();
   }

   private boolean isEntityBeanClusterable() {
      return this.isEntityBean() && this.homeIsClusterable();
   }

   private boolean usesBeanManagedTx() {
      if (this.bd instanceof SessionBeanInfo) {
         SessionBeanInfo var1 = (SessionBeanInfo)this.bd;
         return var1.usesBeanManagedTx();
      } else {
         return false;
      }
   }

   private static String rmiLoadAlgorithmOption(String var0) {
      if ("roundrobin".equalsIgnoreCase(var0)) {
         return "round-robin";
      } else if ("weightbased".equalsIgnoreCase(var0)) {
         return "weight-based";
      } else if ("roundrobinaffinity".equalsIgnoreCase(var0)) {
         return "round-robin-affinity";
      } else if ("weightbasedaffinity".equalsIgnoreCase(var0)) {
         return "weight-based-affinity";
      } else {
         return "randomaffinity".equalsIgnoreCase(var0) ? "random-affinity" : var0;
      }
   }

   void setIIOPSecurityOptions() {
      IIOPSecurityDescriptor var1 = this.bd.getIIOPSecurityDescriptor();
      this.integrity = var1.getTransport_integrity();
      this.confidentiality = var1.getTransport_confidentiality();
      this.clientCertAuthentication = var1.getTransport_client_cert_authentication();
      this.clientAuthentication = var1.getClient_authentication();
      this.identityAssertion = var1.getIdentity_assertion();
   }

   private String getDispatchPolicy() {
      return this.bd.getDispatchPolicy();
   }

   private boolean getStickToFirstServer() {
      return this.bd.getStickToFirstServer();
   }

   private int getRemoteClientTimeout() {
      return this.bd.getRemoteClientTimeout();
   }

   void setHomeOptions() {
      Collection var1 = ((ClientDrivenBeanInfo)this.bd).getAllHomeMethodInfos();
      if (this.homeIsClusterable()) {
         this.clusterable = true;
         this.loadAlgorithm = this.homeLoadAlgorithm();
         this.callRouter = this.homeCallRouter();
         this.propagateEnvironment = this.isStatelessBeanClusterable() || this.isStatefulBeanClusterable() || this.isEntityBeanClusterable();
         if (this.isStateless()) {
            this.methodsAreIdempotent = true;
         }

         this.buildRmicMethodDescriptors(var1, true);
      } else {
         this.buildRmicMethodDescriptors(var1, false);
      }

      this.dgcPolicy = "managed";
      this.dispatchPolicy = this.getDispatchPolicy();
      this.stickToFirstServer = this.getStickToFirstServer();
      if (this.isUseServersideStubs()) {
         this.useServersideStubs = true;
      }

   }

   void setEOOptions(Class<?> var1) {
      ClientDrivenBeanInfo var2 = (ClientDrivenBeanInfo)this.bd;
      ArrayList var3 = new ArrayList();
      Method[] var4 = var1.getMethods();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (!Helper.isBeaSyntheticMethod(var4[var5])) {
            var3.add(var2.getRemoteMethodInfo(var4[var5]));
         }
      }

      if (this.isStateless()) {
         this.dgcPolicy = "managed";
      }

      if (this.isStatelessBeanClusterable()) {
         this.clusterable = true;
         this.loadAlgorithm = this.statelessBeanLoadAlgorithm();
         this.callRouter = this.statelessBeanCallRouter();
         this.propagateEnvironment = true;
         this.methodsAreIdempotent = false;
         this.buildRmicMethodDescriptors(var3, true);
      } else if (this.isEntityBeanClusterable()) {
         this.clusterable = true;
         this.propagateEnvironment = true;
         this.remoteRefClassName = "weblogic.rmi.cluster.ClusterActivatableRemoteRef";
         this.dgcPolicy = "managed";
         EntityBeanInfo var6 = (EntityBeanInfo)this.bd;
         if (var6.getConcurrencyStrategy() == 5) {
            this.methodsAreIdempotent = true;
         } else {
            this.methodsAreIdempotent = false;
         }

         this.buildRmicMethodDescriptors(var3, true);
         this.replicaHandler = "weblogic.rmi.cluster.EntityBeanReplicaHandler";
      } else if (this.isStatefulBeanClusterable()) {
         this.clusterable = true;
         this.replicaHandler = "weblogic.rmi.cluster.PrimarySecondaryReplicaHandler";
         this.buildRmicMethodDescriptors(var3, true);
      } else {
         this.buildRmicMethodDescriptors(var3, false);
      }

      this.dispatchPolicy = this.getDispatchPolicy();
      this.stickToFirstServer = this.getStickToFirstServer();
      if (this.isEntityBean() && !this.isEntityBeanClusterable() || this.isStatefulSession() && !this.isStatefulBeanClusterable()) {
         this.activatable = true;
      }

   }

   void setOutputDirectory(String var1) {
      this.outputDirectory = var1;
   }

   public List<String> asList() {
      ArrayList var1 = new ArrayList();
      var1.add("-nomanglednames");
      var1.add("-noexit");
      if (this.clusterable) {
         var1.add("-clusterable");
      }

      if (this.methodsAreIdempotent) {
         var1.add("-methodsAreIdempotent");
      }

      if (this.propagateEnvironment) {
         var1.add("-propagateEnvironment");
      }

      if (this.loadAlgorithm != null) {
         var1.add("-loadAlgorithm");
         var1.add(this.loadAlgorithm.toLowerCase(Locale.ENGLISH));
      }

      if (this.callRouter != null) {
         var1.add("-callRouter");
         var1.add(this.callRouter);
      }

      if (this.replicaHandler != null) {
         var1.add("-replicaHandler");
         var1.add(this.replicaHandler);
      }

      if (this.useServersideStubs) {
         var1.add("-serverSideStubs");
      }

      if (this.dgcPolicy != null) {
         var1.add("-dgcPolicy");
         var1.add(this.dgcPolicy);
      }

      if (this.dispatchPolicy != null) {
         var1.add("-dispatchPolicy");
         var1.add(this.dispatchPolicy);
      }

      if (this.stickToFirstServer) {
         var1.add("-stickToFirstServer");
      }

      if (!this.bd.useCallByReference()) {
         var1.add("-enforceCallByValue");
      }

      if (this.bd.getNetworkAccessPoint() != null) {
         var1.add("-networkAccessPoint");
         var1.add(this.bd.getNetworkAccessPoint());
      }

      if ("weblogic.rmi.cluster.EntityRemoteRef".equals(this.remoteRefClassName)) {
         var1.add("-remoteRefClassName");
         var1.add(this.remoteRefClassName);
         var1.add("-serverRefClassName");
         var1.add("weblogic.rmi.cluster.EntityServerRef");
      } else if ("weblogic.rmi.cluster.ClusterActivatableRemoteRef".equals(this.remoteRefClassName)) {
         var1.add("-remoteRefClassName");
         var1.add(this.remoteRefClassName);
         var1.add("-serverRefClassName");
         var1.add("weblogic.rmi.cluster.ClusterActivatableServerRef");
      }

      if (this.activatable) {
         var1.add("-activatable");
      }

      if (this.integrity != null) {
         var1.add("-integrity");
         var1.add(this.integrity.toLowerCase(Locale.ENGLISH));
      }

      if (this.confidentiality != null) {
         var1.add("-confidentiality");
         var1.add(this.confidentiality.toLowerCase(Locale.ENGLISH));
      }

      if (this.clientCertAuthentication != null) {
         var1.add("-clientCertAuthentication");
         var1.add(this.clientCertAuthentication.toLowerCase(Locale.ENGLISH));
      }

      if (this.clientAuthentication != null) {
         var1.add("-clientAuthentication");
         var1.add(this.clientAuthentication.toLowerCase(Locale.ENGLISH));
      }

      if (this.identityAssertion != null) {
         var1.add("-identityAssertion");
         var1.add(this.identityAssertion.toLowerCase(Locale.ENGLISH));
      }

      return var1;
   }

   public Collection<RmicMethodDescriptor> getRmicMethodDescriptors() {
      return this.rmicMethodDescriptors;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(" -nomanglednames");
      var1.append(" -noexit");
      if (this.clusterable) {
         var1.append(" -clusterable");
      }

      if (this.methodsAreIdempotent) {
         var1.append(" -methodsAreIdempotent");
      }

      if (this.propagateEnvironment) {
         var1.append(" -propagateEnvironment");
      }

      if (this.loadAlgorithm != null) {
         var1.append(" -loadAlgorithm " + this.loadAlgorithm);
      }

      if (this.callRouter != null) {
         var1.append(" -callRouter " + this.callRouter);
      }

      if (this.replicaHandler != null) {
         var1.append(" -replicaHandler " + this.replicaHandler);
      }

      if (this.useServersideStubs) {
         var1.append(" -serverSideStubs");
      }

      if (this.dgcPolicy != null) {
         var1.append(" -dgcPolicy " + this.dgcPolicy);
      }

      if (this.dispatchPolicy != null) {
         var1.append(" -dispatchPolicy " + this.dispatchPolicy);
      }

      if (this.stickToFirstServer) {
         var1.append(" -stickToFirstServer");
      }

      if (!this.bd.useCallByReference()) {
         var1.append(" -enforceCallByValue");
      }

      if (this.activatable) {
         var1.append(" -activatable");
      }

      if (this.integrity != null) {
         var1.append(" -integrity ");
         var1.append(this.integrity);
      }

      if (this.confidentiality != null) {
         var1.append(" -confidentiality ");
         var1.append(this.confidentiality);
      }

      if (this.clientCertAuthentication != null) {
         var1.append(" -clientCertAuthentication ");
         var1.append(this.clientCertAuthentication);
      }

      if (this.clientAuthentication != null) {
         var1.append(" -clientAuthentication ");
         var1.append(this.clientAuthentication);
      }

      if (this.identityAssertion != null) {
         var1.append(" -identityAssertion ");
         var1.append(this.identityAssertion);
      }

      return var1.toString();
   }

   private void buildRmicMethodDescriptors(Collection<MethodInfo> var1, boolean var2) {
      boolean var3 = this.getRemoteClientTimeout() != 0;
      this.rmicMethodDescriptors = new LinkedHashSet();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         MethodInfo var5 = (MethodInfo)var4.next();
         boolean var6 = false;
         boolean var7 = false;
         if (!this.usesBeanManagedTx()) {
            short var8 = var5.getTransactionAttribute();
            switch (var8) {
               case 0:
                  var7 = false;
                  break;
               case 1:
                  var6 = true;
                  var7 = true;
                  break;
               case 2:
                  var6 = true;
                  var7 = true;
                  break;
               case 3:
                  var7 = true;
                  break;
               case 4:
                  var6 = true;
                  var7 = true;
                  break;
               case 5:
                  var6 = true;
                  var7 = false;
            }
         }

         RmicMethodDescriptor var11 = null;
         if (var5.isIdempotent() && var2 || var6 || !var7) {
            var11 = new RmicMethodDescriptor(this.createRMIMethodSignature(var5));
            if (var5.isIdempotent() && var2) {
               var11.setIdempotent("true");
            }

            if (var6) {
               var11.setRequiresTransaction(true);
            }
         }

         if (var3) {
            if (var11 == null) {
               var11 = new RmicMethodDescriptor(this.createRMIMethodSignature(var5));
            }

            int var9 = this.getRemoteClientTimeout() * 1000;
            if (var7 && this.bd.getTransactionTimeoutMS() > 0) {
               var9 = Math.max(var9, this.bd.getTransactionTimeoutMS());
            }

            String var10 = (new Integer(var9)).toString();
            var11.setTimeOut(var10);
            if (debugLogger.isDebugEnabled()) {
               debug("Setting the remote-client-timeout to " + this.getRemoteClientTimeout() + " seconds for the method: " + var5.getSignature());
            }
         }

         if (var11 != null) {
            this.rmicMethodDescriptors.add(var11);
         }
      }

      if (this.rmicMethodDescriptors.size() == 0) {
         this.rmicMethodDescriptors = null;
      }

   }

   private String createRMIMethodSignature(MethodInfo var1) {
      String[] var2 = var1.getMethodParams();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         int var4 = var2[var3].indexOf(91);
         if (var4 != -1) {
            int var5 = var2[var3].lastIndexOf(91);
            int var6 = var5 - var4 + 1;
            String var7 = null;
            char var8 = var2[var3].charAt(var5 + 1);
            switch (var8) {
               case 'B':
                  var7 = "byte";
                  break;
               case 'C':
                  var7 = "char";
                  break;
               case 'D':
                  var7 = "double";
                  break;
               case 'E':
               case 'G':
               case 'H':
               case 'K':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               default:
                  throw new AssertionError("Bad object type: " + var8);
               case 'F':
                  var7 = "float";
                  break;
               case 'I':
                  var7 = "int";
                  break;
               case 'J':
                  var7 = "long";
                  break;
               case 'L':
                  var7 = var2[var3].substring(var5 + 2, var2[var3].length() - 1);
                  break;
               case 'S':
                  var7 = "short";
                  break;
               case 'Z':
                  var7 = "boolean";
            }

            StringBuilder var9 = new StringBuilder();
            var9.append(var7);

            for(int var10 = 0; var10 < var6; ++var10) {
               var9.append("[]");
            }

            var2[var3] = var9.toString();
         }
      }

      return var1.getMethodName() + "(" + StringUtils.join(var2, ",") + ")";
   }

   private static void debug(String var0) {
      debugLogger.debug("[RMICOptions] " + var0);
   }

   static {
      debugLogger = EJBDebugService.compilationLogger;
   }
}
