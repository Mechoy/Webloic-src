package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCLicenseManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.EngineSecError;
import weblogic.wtc.jatmi.InvokeSvc;
import weblogic.wtc.jatmi.PasswordUtils;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.TypedCArray;
import weblogic.wtc.jatmi.UserTcb;
import weblogic.wtc.jatmi.atn;
import weblogic.wtc.jatmi.atncredtdom;
import weblogic.wtc.jatmi.atnctxtdom;
import weblogic.wtc.jatmi.atntdom65;
import weblogic.wtc.jatmi.dsession;
import weblogic.wtc.jatmi.rdsession;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;
import weblogic.wtc.jatmi.tplle;

public final class gwdsession extends dsession {
   public gwdsession(Timer var1, InetAddress var2, int var3, int var4, TuxXidRply var5) {
      super(var1, var2, var3, var4, var5, WTCService.canUseBetaFeatures());
   }

   public gwdsession(Timer var1, InetAddress var2, int var3, atn var4, int var5, TuxXidRply var6) {
      super(var1, var2, var3, var4, var5, var6, WTCService.canUseBetaFeatures());
   }

   public gwdsession(Timer var1, InetAddress[] var2, int[] var3, atn var4, InvokeSvc var5, int var6, TuxXidRply var7) {
      super(var1, var2, var3, var4, var5, var6, var7, WTCService.canUseBetaFeatures());
   }

   public gwdsession(Timer var1, InetAddress var2, int var3, atn var4, InvokeSvc var5, int var6, TuxXidRply var7) {
      super(var1, var2, var3, var4, var5, var6, var7, WTCService.canUseBetaFeatures());
   }

   public gwdsession(Timer var1, Socket var2, atn var3, InvokeSvc var4, int var5, boolean var6, TuxXidRply var7) throws IOException {
      super(var1, var2, var3, var4, var5, var7, var6, WTCService.canUseBetaFeatures());
   }

   private TDMRemote do_accept(TDMRemote[] var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/gwdsession/do_accept/");
      }

      TDMRemote var9 = null;
      dsession var10 = null;
      byte[] var16 = null;
      byte[] var17 = null;
      byte var18 = 0;
      boolean var19 = true;
      atncredtdom var24 = null;
      atnctxtdom var25 = null;
      TypedCArray var26 = null;
      tcm var27 = null;
      boolean var29 = false;
      WTCService var30 = WTCService.getWTCService();
      DataOutputStream var6 = this.get_output_stream();
      DataInputStream var5 = this.get_input_stream();
      tfmh var3 = new tfmh(1);

      String var13;
      try {
         if (var3.read_dom_65_tfmh(var5, 10) != 0) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/10/");
            }

            throw new TPException(4, "Could not read message from remote domain");
         }

         TdomTcb var4 = (TdomTcb)var3.tdom.body;
         if (var4.get_opcode() != 14) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/20/");
            }

            throw new TPException(4, "Invalid opcode");
         }

         int var7 = var4.get_dom_protocol();
         this.setInProtocol(var7);
         this.setSessionFeatures(var4.getFeaturesSupported());
         int var31 = var7 & 31;
         if ((var31 < 13 || var31 == 14) && (var7 & 2147483616 & 16) == 0) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/30/");
            }

            throw new TPException(4, "ERROR: Protocol level " + var7 + " is not supported!");
         }

         String var8 = var4.get_sending_domain();

         int var14;
         for(var14 = 0; var14 < var1.length; ++var14) {
            if (var8.equals(var1[var14].getAccessPointId())) {
               var9 = var1[var14];
            }
         }

         if (var9 == null) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/50/");
            }

            throw new TPException(9, "Unknown remote domain " + var8);
         }

         if ((var10 = (dsession)var9.getTsession(false)) != null) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/60/");
            }

            if (!var10.getIAddress().equals(this.getIAddress())) {
               throw new TPException(9, "Got a connection from a remote domain that I am already connected to: " + var8);
            }

            if (this.get_sess_sec() == 0) {
               var10._dom_drop();
            } else {
               var29 = true;
            }
         }

         if (!var29) {
            var9.setTsession(this);
         }

         TDMLocalTDomain var32 = (TDMLocalTDomain)var9.getLocalAccessPointObject();
         if (var7 <= 13 && !var32.isInteroperate()) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/65/");
            }

            throw new TPException(12, "Use Interoperate option to interoperate with sites older than Tuxedo 7.1");
         }

         var13 = var32.getAccessPointId();
         String var33 = var9.getAccessPoint();
         String var34 = var32.getAccessPoint();
         TDMRemoteTDomain var35 = var30.getRemoteTDomain(var9.getAccessPointId());
         int var28;
         if (!var32.getMBean().getUseSSL().equals("TwoWay") && !var32.getMBean().getUseSSL().equals("OneWay")) {
            var28 = TCLicenseManager.acceptEncryptionLevel(var7, var35.getMinEncryptBits(), var35.getMaxEncryptBits(), var4.get_lle_flags());
         } else {
            var28 = TCLicenseManager.acceptEncryptionLevel(var7, 0, 0, var4.get_lle_flags());
         }

         if (var28 == 0) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/65/");
            }

            throw new TPException(4, "Link level encryption negotiation failure" + var4.get_lle_flags());
         }

         if (var7 >= 15) {
            this.setAclPolicy(var35.getAclPolicy());
            this.setCredentialPolicy(var35.getCredentialPolicy());
            this.setTpUserFile(var35.getTpUsrFile());
            this.setAppKey(var35.getAppKey());
            if (this.myAppKeySel != null) {
               if (this.myAppKeySel.equals("LDAP")) {
                  this.setUidKw(var35.getTuxedoUidKw());
                  this.setGidKw(var35.getTuxedoGidKw());
               } else {
                  this.setCustomAppKeyClass(var35.getCustomAppKeyClass());
                  this.setCustomAppKeyClassParam(var35.getCustomAppKeyClassParam());
               }
            }

            this.setAllowAnonymous(var35.getAllowAnonymous());
            this.setDfltAppKey(var35.getDefaultAppKey());
         }

         this.setRemoteDomainId(var35.getAccessPointId());
         this.setKeepAlive(var35.getKeepAlive());
         this.setKeepAliveWait(var35.getKeepAliveWait());
         tfmh var11 = new tfmh(1);
         TdomTcb var12 = new TdomTcb(15, var4.get_reqid(), 0, (String)null);
         var11.tdom = new tcm((short)7, var12);
         this.set_local_domain_name(var13);
         var12.set_security_type(this.get_sess_sec());
         var12.set_lle_flags(var28);
         if (var11.write_dom_65_tfmh(var6, var13, 10, Integer.MAX_VALUE) != 0) {
            if (var2) {
               ntrace.doTrace("*]/gwdsession/do_accept/70/");
            }

            throw new TPException(9, "Could not get authorization parameters from remote domain");
         }

         this.setOutProtocol(var7);
         if (var2) {
            ntrace.doTrace("...send ACALL1_RPLY to remote");
         }

         int var21;
         int var22;
         tcm var36;
         UserTcb var37;
         TypedCArray var38;
         if (var28 != 1) {
            if (var2) {
               ntrace.doTrace("/gwdsession/do_accept/do LLE protocol");
            }

            if (var7 <= 13) {
               if (var2) {
                  ntrace.doTrace("/gwdsession/do_accept/use R65 release protocol");
               }

               if (var11.read_dom_65_tfmh(var5, 13) != 0) {
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/71/");
                  }

                  throw new TPException(9, "Could not get authorization parameters from remote domain");
               }
            } else {
               if (var2) {
                  ntrace.doTrace("/gwdsession/do_accept/use R80 release protocol");
               }

               if (var11.read_tfmh(var5) != 0) {
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/72/");
                  }

                  throw new TPException(9, "Could not get authorization parameters from remote domain");
               }

               var12 = (TdomTcb)var11.tdom.body;
            }

            if (var12.get_opcode() != 20) {
               if (var2) {
                  ntrace.doTrace("*]/gwdsession/do_accept/73/");
               }

               throw new TPException(4, "Invalid opcode");
            }

            if (var2) {
               ntrace.doTrace("/gwdsession/do_accept/...recv LLE");
            }

            this.myLLE = new tplle();
            var36 = var11.user;
            var37 = (UserTcb)var36.body;
            var38 = (TypedCArray)var37.user_data;
            var17 = var38.carray;
            var22 = var17.length;
            if (var2) {
               ntrace.doTrace("recv size = " + var22);
            }

            var14 = -1;
            var21 = 2048;

            while(var14 < 0) {
               if (var2) {
                  ntrace.doTrace("/gwdsession/do_accept/lle buffer " + var21);
               }

               var16 = new byte[var21];
               var14 = this.myLLE.crypKeyeTwo(var28, var17, var16, 0);
               if (var14 < 0) {
                  var21 = -var14;
               }
            }

            if (var14 == 0) {
               if (var2) {
                  ntrace.doTrace("*]/gwdsession/do_accept/72/");
               }

               throw new TPException(12, "Unable to generate second diffie-hellman packet");
            }

            var12.setLLELength(var21);
            var12.setSendSecPDU(var16, var21);
            var26 = new TypedCArray();
            var27 = new tcm((short)0, new UserTcb(var26));
            var26.carray = var16;
            var11.user = var27;
            var26.setSendSize(var14);
            var12.set_opcode(21);
            if (var7 <= 13) {
               if (var11.write_dom_65_tfmh(var6, var13, 13, this.getCompressionThreshold()) != 0) {
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/73/");
                  }

                  throw new TPException(12, "Unable to generate second diffie-hellman packet");
               }
            } else if (var11.write_tfmh(var6, this.getCompressionThreshold()) != 0) {
               if (var2) {
                  ntrace.doTrace("*]/gwdsession/do_accept/74/");
               }

               throw new TPException(12, "Unable to generate second diffie-hellman packet");
            }

            if (var2) {
               ntrace.doTrace("/gwdsession/do_accept/...send LLE_RPLY");
            }

            switch (this.myLLE.crypFinishTwo()) {
               case 3:
                  this.setELevel(1);
                  this.myLLE = null;
                  break;
               case 4:
                  this.setELevel(2);
                  break;
               case 5:
                  this.setELevel(32);
                  break;
               case 6:
                  this.setELevel(4);
                  break;
               default:
                  this.myLLE = null;
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/74");
                  }

                  throw new TPException(12, "ERROR: Unexpected link level encryption failure");
            }

            this.setLLE();
         }

         if (this.get_sess_sec() != 0) {
            String var39;
            String var40;
            String var49;
            String var50;
            if (this.get_sess_sec() == 2) {
               TDMPasswd var47 = var30.getTDMPasswd(var34, var33);
               var49 = WTCService.getPasswordKey();
               var50 = WTCService.getEncryptionType();
               var39 = PasswordUtils.decryptPassword(var49, var47.getLocalPasswordIV(), var47.getLocalPassword(), var50);
               var40 = PasswordUtils.decryptPassword(var49, var47.getRemotePasswordIV(), var47.getRemotePassword(), var50);
               if (var39 == null || var40 == null) {
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/72/");
                  }

                  throw new TPException(8, "Could not get the domain passwords");
               }

               this.setLocalPassword(var39);
               this.setRemotePassword(var40);
            } else if (this.get_sess_sec() == 1) {
               String var48 = WTCService.getAppPasswordPWD();
               var49 = WTCService.getAppPasswordIV();
               var50 = WTCService.getPasswordKey();
               var39 = WTCService.getEncryptionType();
               var40 = PasswordUtils.decryptPassword(var50, var49, var48, var39);
               if (var40 == null) {
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/73/");
                  }

                  throw new TPException(8, "Could not get the application passwords");
               }

               this.setApplicationPassword(var40);
            }

            Object var23;
            if (this.dom_protocol <= 13) {
               var23 = new atntdom65(this.desired_name);
               ((atn)var23).setTargetName(this.getRemoteDomainId());
            } else if (this.gssatn == null) {
               var23 = new atntdom80(this.desired_name);
            } else {
               var23 = this.gssatn;
            }

            ((atn)var23).setSecurityType(this.security_type);
            ((atn)var23).setSrcName(var8);
            ((atn)var23).setDesiredName(this.desired_name);
            if (this.security_type == 1) {
               ((atn)var23).setApplicationPasswd(this.lpwd);
            } else {
               ((atn)var23).setLocalPasswd(this.lpwd);
               ((atn)var23).setRemotePasswd(this.rpwd);
            }

            if (this.dom_protocol >= 15) {
               var26 = new TypedCArray();
               var27 = new tcm((short)0, new UserTcb(var26));
               if (this.myLLE != null) {
                  ((atn)var23).setApplicationData(this.myLLE.getFingerprint());
               }
            }

            try {
               var24 = (atncredtdom)((atn)var23).gssAcquireCred(this.desired_name, this.desired_name);
            } catch (EngineSecError var43) {
               if (var2) {
                  ntrace.doTrace("*]/gwdsession/do_accept/90/");
               }

               throw new TPException(8, "Unable to acquire credentials <" + var43.errno + ">");
            }

            try {
               var25 = (atnctxtdom)((atn)var23).gssGetContext(var24, this.dom_target_name);
            } catch (EngineSecError var41) {
               if (var2) {
                  ntrace.doTrace("*]/gwdsession/do_accept/100");
               }

               throw new TPException(8, "Unable to get security context <" + var41.errno + ">");
            }

            int var46 = 1;

            while(var46 > 0) {
               var22 = ((atn)var23).getEstimatedPDURecvSize(var25);
               var21 = ((atn)var23).getEstimatedPDUSendSize(var25);
               if (var21 > 0) {
                  if (var16 == null || var16.length < var21) {
                     var16 = new byte[var21];
                  }

                  if (this.dom_protocol <= 13) {
                     switch (var25.context_state) {
                        case 1:
                           var18 = 17;
                           break;
                        case 3:
                           var18 = 19;
                     }
                  }
               }

               if (var22 > 0) {
                  if (this.dom_protocol > 13) {
                     if (var11.read_tfmh(var5) != 0) {
                        if (var2) {
                           ntrace.doTrace("*]/gwdsession/do_accept/120");
                        }

                        throw new TPException(4, "Could not receive security exchange from remote domain");
                     }

                     var36 = var11.user;
                     var37 = (UserTcb)var36.body;
                     var38 = (TypedCArray)var37.user_data;
                     var17 = var38.carray;
                     var22 = var17.length;
                     if (var2) {
                        ntrace.doTrace("recv size = " + var22);
                     }
                  } else {
                     if (var17 == null || var17.length < var22) {
                        var17 = new byte[var22];
                        if (var2) {
                           ntrace.doTrace("/gwdsession/do_accept/recv size " + var22);
                        }
                     }

                     var12.setRecvSecPDU(var17, var22);
                     if (var11.read_dom_65_tfmh(var5, 10) != 0) {
                        if (var2) {
                           ntrace.doTrace("*]/gwdsession/do_accept/110");
                        }

                        throw new TPException(4, "Could not receive security exchange from remote domain");
                     }
                  }
               }

               try {
                  var46 = ((atn)var23).gssAcceptSecContext(var25, var17, var22, var16);
               } catch (EngineSecError var44) {
                  if (var44.errno != -3005) {
                     if (var2) {
                        ntrace.doTrace("*]/gwdsession/do_accept/140/");
                     }

                     throw new TPException(8, "Security violation <" + var44.errno + ")");
                  }

                  var16 = new byte[var44.needspace];

                  try {
                     var46 = ((atn)var23).gssAcceptSecContext(var25, var17, var22, var16);
                  } catch (EngineSecError var42) {
                     if (var2) {
                        ntrace.doTrace("*]/gwdsession/do_accept/130/");
                     }

                     throw new TPException(8, "Security violation <" + var42.errno + ")");
                  }
               }

               if (var46 == -1) {
                  if (var2) {
                     ntrace.doTrace("*]/gwdsession/do_accept/145/");
                  }

                  throw new TPException(8, "Security violation");
               }

               if (var21 > 0) {
                  int var20 = ((atn)var23).getActualPDUSendSize();
                  var12.setSendSecPDU(var16, var20);
                  if (this.dom_protocol <= 13) {
                     var12.set_opcode(var18);
                     if (var11.write_dom_65_tfmh(var6, var13, 10, this.getCompressionThreshold()) != 0) {
                        if (var2) {
                           ntrace.doTrace("*]/gwdsession/do_accept/150");
                        }

                        throw new TPException(4, "Could not send mesage to remote domain");
                     }
                  } else {
                     var26.carray = var16;
                     var11.user = var27;
                     var26.setSendSize(var20);
                     var12.set_opcode(22);
                     if (var11.write_tfmh(var6, this.getCompressionThreshold()) != 0) {
                        if (var2) {
                           ntrace.doTrace("*]/gwdsession/do_accept/160");
                        }

                        throw new TPException(4, "Could not send mesage to remote domain");
                     }
                  }
               }
            }
         } else {
            this.set_authtype(0);
         }

         if (var29) {
            var9.setTsession(this);
            var10._dom_drop();
         }
      } catch (IOException var45) {
         if (var2) {
            ntrace.doTrace("*]/gwdsession/do_accept/80/");
         }

         throw new TPException(12, "Unable to get authentication level");
      }

      if (this.setUpTuxedoAAA() < 0) {
         if (var2) {
            ntrace.doTrace("*]/gwdsession/do_accept/85/");
         }

         throw new TPException(12, "Unable to setup authentication and auditing for Tuxedo");
      } else {
         rdsession var15 = new rdsession(var6, this, this.get_invoker(), this.dom_protocol, var13, this.get_TimeService(), this.getUnknownRplyObj(), WTCService.canUseBetaFeatures());
         var15.set_BlockTime(this.get_BlockTime());
         var15.setSessionReference(this);
         this.set_is_connected(true);
         this.set_rcv_place(var15);
         this.dmqDecision();
         WTCLogger.logInfoRemoteDomainConnected(this.getRemoteDomainId());
         if (var2) {
            ntrace.doTrace("]/gwdsession/do_accept/1000/success/" + var9);
         }

         return var9;
      }
   }

   public synchronized TDMRemote tpinit(TDMRemote[] var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/gwdsession/tpinit/");
      }

      TDMRemote var3 = null;
      if (this.get_is_connected()) {
         if (var2) {
            ntrace.doTrace("*]/gwdsession/tpinit/10/");
         }

         throw new TPException(9, "Can not init object more than once");
      } else if (this.getIsTerminated()) {
         if (var2) {
            ntrace.doTrace("*]/dsession/tpinit/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if (this.get_is_connector()) {
         if (var2) {
            ntrace.doTrace("*]/dsession/tpinit/30/");
         }

         throw new TPException(9, "We are connecting, not accepting");
      } else {
         var3 = this.do_accept(var1);
         if (var2) {
            ntrace.doTrace("]/dsession/tpinit/30/" + var3);
         }

         return var3;
      }
   }
}
