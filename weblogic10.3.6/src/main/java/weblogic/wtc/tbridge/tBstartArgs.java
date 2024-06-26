package weblogic.wtc.tbridge;

import com.bea.core.jatmi.common.ntrace;

public final class tBstartArgs {
   public int redirect;
   protected int[] direction;
   protected String[] sourceName;
   protected String[] sourceQspace;
   protected String[] targetName;
   protected String[] targetQspace;
   protected String[] AccessPoint;
   protected String[] replyQ;
   protected String[] translateFML;
   protected String[] metadataFile;
   protected String transactional;
   protected int timeout;
   protected int idleTime;
   protected int retries;
   protected int retryDelay;
   protected String wlsErrorDestination;
   protected String tuxErrorQueue;
   protected int defaultRelativeBirthtime;
   protected int defaultRelativeExpiration;
   protected int expirationAdjustment;
   protected int priorityMapping;
   protected int[] pMapJmsToTux;
   protected int[] pMapTuxToJms;
   protected String deliveryModeOverride;
   protected String defaultReplyDeliveryMode;
   protected String userID;
   protected String allowNonStandardTypes;
   protected String jndiFactory;
   protected String jmsFactory;
   protected String tuxFactory;
   protected boolean[] operational;
   protected static final int MAXREDIRECTS = 1000;

   public tBstartArgs() {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/tBstartArgs/");
      }

      this.redirect = -1;
      this.direction = new int[1000];
      this.sourceName = new String[1000];
      this.sourceQspace = new String[1000];
      this.targetName = new String[1000];
      this.targetQspace = new String[1000];
      this.replyQ = new String[1000];
      this.AccessPoint = new String[1000];
      this.translateFML = new String[1000];
      this.metadataFile = new String[1000];
      this.transactional = "YES";
      this.timeout = 60;
      this.idleTime = 0;
      this.retries = 0;
      this.retryDelay = 10;
      this.wlsErrorDestination = "";
      this.tuxErrorQueue = "";
      this.defaultRelativeBirthtime = 0;
      this.defaultRelativeExpiration = 0;
      this.expirationAdjustment = 0;
      this.priorityMapping = -1;
      this.pMapJmsToTux = new int[10];
      this.pMapJmsToTux[0] = 1;
      this.pMapJmsToTux[1] = 12;
      this.pMapJmsToTux[2] = 23;
      this.pMapJmsToTux[3] = 34;
      this.pMapJmsToTux[4] = 45;
      this.pMapJmsToTux[5] = 56;
      this.pMapJmsToTux[6] = 67;
      this.pMapJmsToTux[7] = 78;
      this.pMapJmsToTux[8] = 89;
      this.pMapJmsToTux[9] = 100;
      this.pMapTuxToJms = new int[101];
      this.pMapTuxToJms[0] = 0;

      int var2;
      for(var2 = 0; var2 < 10; ++var2) {
         for(int var3 = 1; var3 < 11; ++var3) {
            this.pMapTuxToJms[var2 * 10 + var3] = var2;
         }
      }

      this.deliveryModeOverride = "";
      this.defaultReplyDeliveryMode = "";
      this.userID = "";
      this.allowNonStandardTypes = "NO";
      this.jndiFactory = "";
      this.jmsFactory = "";
      this.tuxFactory = "";
      this.operational = new boolean[1000];

      for(var2 = 0; var2 < 1000; ++var2) {
         this.operational[var2] = true;
      }

      if (var1) {
         ntrace.doTrace("]/tBstartArgs/10/");
      }

   }

   public void print_tBstartArgs() {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/print_tBstartArgs/");
      }

      if (!ntrace.isTraceEnabled(16)) {
         if (var1) {
            ntrace.doTrace("]/print_tBstartArgs/10");
         }

      } else {
         int var2;
         if (this.redirect == -1) {
            ntrace.doTrace("/print_tBstartArgs/redirect=Undefined/");
         } else {
            for(var2 = 0; var2 < this.redirect; ++var2) {
               ntrace.doTrace("/print_tBstartArgs/redirect[" + var2 + "]/");
               if (this.direction[var2] == 1) {
                  ntrace.doTrace("/print_tBstartArgs/redirect/direction=TUXQ2JMSQ/");
               } else if (this.direction[var2] == 2) {
                  ntrace.doTrace("/print_tBstartArgs/redirect/direction=JMSQ2TUXQ/");
               } else if (this.direction[var2] == 3) {
                  ntrace.doTrace("/print_tBstartArgs/redirect/direction=JMSQ2TUXS/");
               } else if (this.direction[var2] == 4) {
                  ntrace.doTrace("/print_tBstartArgs/redirect/direction=JMSQ2JMSQ/");
               } else {
                  ntrace.doTrace("/print_tBstartArgs/redirect/direction=Undefined!/");
               }

               ntrace.doTrace("/print_tBstartArgs/redirect/sourceName=" + this.sourceName[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/sourceQspace=" + this.sourceQspace[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/targetName=" + this.targetName[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/targetQspace=" + this.targetQspace[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/AccessPoint=" + this.AccessPoint[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/replyQ=" + this.replyQ[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/translateFML=" + this.translateFML[var2] + "/");
               ntrace.doTrace("/print_tBstartArgs/redirect/metadataFile=" + this.metadataFile[var2] + "/");
               if (this.operational[var2]) {
                  ntrace.doTrace("/print_tBstartArgs/redirect[" + var2 + "]: operational=run/");
               } else {
                  ntrace.doTrace("/print_tBstartArgs/redirect[" + var2 + "]: operational=stop/");
               }
            }
         }

         ntrace.doTrace("/print_tBstartArgs/transactional=" + this.transactional + "/");
         ntrace.doTrace("/print_tBstartArgs/timeout=" + this.timeout + "/");
         ntrace.doTrace("/print_tBstartArgs/idleTime=" + this.idleTime + "/");
         ntrace.doTrace("/print_tBstartArgs/retries=" + this.retries + "/");
         ntrace.doTrace("/print_tBstartArgs/retryDelay=" + this.retryDelay + "/");
         ntrace.doTrace("/print_tBstartArgs/wlsErrorDestination=" + this.wlsErrorDestination + "/");
         ntrace.doTrace("/print_tBstartArgs/tuxErrorQueue=" + this.tuxErrorQueue + "/");
         ntrace.doTrace("/print_tBstartArgs/defaultRelativeBirthtime=" + this.defaultRelativeBirthtime + "/");
         ntrace.doTrace("/print_tBstartArgs/defaultRelativeExpiration=" + this.defaultRelativeExpiration + "/");
         ntrace.doTrace("/print_tBstartArgs/expirationAdjustment=" + this.expirationAdjustment + "/");
         ntrace.doTrace("/print_tBstartArgs/priorityMapping=" + this.priorityMapping + "/");

         for(var2 = 0; var2 < 10; ++var2) {
            ntrace.doTrace("/print_tBstartArgs/pMapJmsToTux[" + var2 + "]=" + this.pMapJmsToTux[var2] + "/");
         }

         for(var2 = 0; var2 < 101; ++var2) {
            ntrace.doTrace("/print_tBstartArgs/pMapTuxToJms[" + var2 + "]=" + this.pMapTuxToJms[var2] + "/");
         }

         ntrace.doTrace("/print_tBstartArgs/deliveryModeOverride=" + this.deliveryModeOverride + "/");
         ntrace.doTrace("/print_tBstartArgs/defaultReplyDeliveryMode=" + this.defaultReplyDeliveryMode + "/");
         ntrace.doTrace("/print_tBstartArgs/userID=" + this.userID + "/");
         ntrace.doTrace("/print_tBstartArgs/allowNonStandardTypes=" + this.allowNonStandardTypes + "/");
         ntrace.doTrace("/print_tBstartArgs/jndiFactory=" + this.jndiFactory + "/");
         ntrace.doTrace("/print_tBstartArgs/jmsFactory=" + this.jmsFactory + "/");
         ntrace.doTrace("/print_tBstartArgs/tuxFactory=" + this.tuxFactory + "/");
         if (var1) {
            ntrace.doTrace("]/print_tBstartArgs/20/");
         }

      }
   }

   public tBstartArgs get_tBstartArgs() {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/get_tBstartArgs/");
         ntrace.doTrace("]/get_tBstartArgs/");
      }

      return this;
   }
}
