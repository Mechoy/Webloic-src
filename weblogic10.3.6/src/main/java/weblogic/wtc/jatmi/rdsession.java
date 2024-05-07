package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCSecurityManager;
import com.bea.core.jatmi.internal.TCTaskHelper;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import javax.security.auth.login.LoginException;

public final class rdsession {
   private transient DataOutputStream ostream;
   private transient HashMap rplyObjMap;
   private transient HashMap rplyXidObjMap;
   private transient boolean is_term = false;
   private transient int dom_protocol;
   private transient String local_domain_name;
   private transient gwatmi myGwatmi;
   private transient InvokeSvc myInvoker;
   private transient dsession mySession;
   private int uid;
   private BetaFeatures useBetaFeatures;
   private transient Timer myTimeService;
   private transient long myBlocktime;
   private transient TuxXidRply unknownXidRply;
   private transient ArrayList tfmhList;
   private transient ArrayList objectArrayList;
   private transient ArrayList dispatchList;
   private transient HashMap conversationReplyObjects;
   private static final int MAXCACHE = 10;

   public rdsession(DataOutputStream var1, gwatmi var2, InvokeSvc var3, int var4, String var5, Timer var6, TuxXidRply var7, BetaFeatures var8) {
      this.ostream = var1;
      this.rplyObjMap = new HashMap();
      this.rplyXidObjMap = new HashMap();
      this.myGwatmi = var2;
      this.dom_protocol = var4;
      this.local_domain_name = var5;
      this.myTimeService = var6;
      this.unknownXidRply = var7;
      this.myInvoker = var3;
      this.tfmhList = new ArrayList();
      this.objectArrayList = new ArrayList();
      this.dispatchList = new ArrayList();
      this.conversationReplyObjects = new HashMap();
      this.useBetaFeatures = var8;
   }

   public void set_BlockTime(long var1) {
      this.myBlocktime = var1;
   }

   public InvokeSvc get_invoker() {
      return this.myInvoker;
   }

   public boolean isTerm() {
      return this.is_term;
   }

   public void connectionHasTerminated() {
      boolean var1 = ntrace.isTraceEnabled(4);
      this.is_term = true;
      this.myGwatmi.setIsTerminated();
      HashMap var2 = new HashMap();
      synchronized(this.rplyObjMap) {
         Iterator var4 = this.rplyObjMap.keySet().iterator();

         while(true) {
            if (!var4.hasNext()) {
               break;
            }

            SessionAcallDescriptor var5 = (SessionAcallDescriptor)var4.next();
            var2.put(var5, this.rplyObjMap.get(var5));
         }
      }

      Iterator var3 = var2.keySet().iterator();

      TdomTcb var9;
      while(var3.hasNext()) {
         rdtimer var7 = null;
         SessionAcallDescriptor var15 = (SessionAcallDescriptor)var3.next();
         Object[] var6 = (Object[])((Object[])var2.get(var15));
         if (var6[1] != null) {
            var7 = (rdtimer)var6[1];
            var7.cancel();
            var6[1] = null;
         }

         if (var15.hasCallBack() && var6[2] != null) {
            GatewayTpacallAsyncReply var10 = (GatewayTpacallAsyncReply)var6[2];
            TPException var11 = new TPException(12, "ERROR: Lost session connection for session <" + this.mySession.get_local_domain_name() + ", " + this.mySession.getRemoteDomainId() + ">.");
            var10.failure(this.myGwatmi, var15, var11);
            if (var1) {
               ntrace.doTrace("/rdsession/connectionHasTerminated/8: Async TPESYSTEM sent");
            }
         } else {
            var9 = new TdomTcb(3, var15.getCd(), 4194304, (String)null);
            var9.set_diagnostic(12);
            tfmh var8 = new tfmh(1);
            var8.tdom = new tcm((short)7, var9);
            if (this.remove_rplyObj(var15)) {
               ReplyQueue var17 = (ReplyQueue)var6[0];
               var17.add_reply(this.myGwatmi, var15, var8);
               if (var1) {
                  ntrace.doTrace("/rdsession/connectionHasTerminated/10: TPESYSTEM sent");
               }
            }
         }
      }

      HashMap var16 = new HashMap();
      synchronized(this.rplyXidObjMap) {
         Iterator var19 = this.rplyXidObjMap.keySet().iterator();

         while(true) {
            if (!var19.hasNext()) {
               break;
            }

            Txid var22 = (Txid)var19.next();
            var16.put(var22, this.rplyXidObjMap.get(var22));
         }
      }

      Iterator var18 = var16.keySet().iterator();

      tfmh var29;
      TdomTcb var30;
      while(var18.hasNext()) {
         var9 = null;
         Txid var20 = (Txid)var18.next();
         var30 = new TdomTcb(12, 0, 0, (String)null);
         var30.set_diagnostic(12);
         TdomTranTcb var12 = new TdomTranTcb(var20);
         var29 = new tfmh(1);
         var29.tdom = new tcm((short)7, var30);
         var29.tdomtran = new tcm((short)10, var12);
         Object[] var23 = (Object[])((Object[])var16.get(var20));
         if (var23[1] != null) {
            rdXtimer var27 = (rdXtimer)var23[1];
            var27.cancel();
            var23[1] = null;
         }

         if (this.remove_rplyXidObj(var20)) {
            TuxXidRply var25 = (TuxXidRply)var23[0];
            var25.add_reply(this.myGwatmi, var20, var29);
            if (var1) {
               ntrace.doTrace("/rdsession/connectionHasTerminated/20: trans TPESYSTEM sent");
            }
         }
      }

      HashMap var21 = this.mySession.getRMICallList();
      if (var21 != null) {
         Iterator var24 = var21.values().iterator();

         while(var24.hasNext()) {
            Object[] var26 = (Object[])((Object[])var24.next());
            int var28 = (Integer)var26[2];
            var30 = new TdomTcb(3, var28, 4194304, (String)null);
            var30.set_diagnostic(12);
            var29 = new tfmh(1);
            var29.tdom = new tcm((short)7, var30);
            var24.remove();
            if (var1) {
               ntrace.doTrace("/rdsession/connectionHasTerminated/30: send exception as reply to RMI/IIOP call: GIOPreqId =" + var28);
            }

            RMIReplyRequest var31 = new RMIReplyRequest(var29, var26, this.mySession);
            var31.execute();
         }
      }

   }

   public void restoreTfmhToCache(tfmh var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/restoreTfmhToCache/");
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/restoreTfmhToCache/10/");
         }

      } else if (!var1.prepareForCache()) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/restoreTfmhToCache/20/");
         }

      } else {
         synchronized(this.tfmhList) {
            if (this.tfmhList.size() < 10) {
               this.tfmhList.add(var1);
               if (var2) {
                  ntrace.doTrace("/rdsession(" + this.uid + ")/addedtocache=" + var1);
               }
            } else if (var2) {
               ntrace.doTrace("/rdsession(" + this.uid + ")/notaddedtocache=" + var1);
            }
         }

         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/restoreTfmhToCache/30/");
         }

      }
   }

   public tfmh allocTfmh() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/allocTfmh/");
      }

      tfmh var2 = null;
      synchronized(this.tfmhList) {
         int var3;
         if ((var3 = this.tfmhList.size()) != 0) {
            --var3;
            var2 = (tfmh)this.tfmhList.remove(var3);
         }
      }

      if (var1) {
         ntrace.doTrace("/rdsession(" + this.uid + ")/allocTfmh/" + var2);
      }

      if (var2 == null) {
         var2 = new tfmh(1);
      }

      if (var1) {
         ntrace.doTrace("]/rdsession(" + this.uid + ")/allocTfmh/" + var2);
      }

      return var2;
   }

   public void restoreExecuteRequestToCache(MuxableExecute var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/restoreExecuteRequestToCache/");
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/restoreExecuteRequestToCache/10");
         }

      } else {
         synchronized(this.dispatchList) {
            if (this.dispatchList.size() < 10) {
               this.dispatchList.add(var1);
               if (var2) {
                  ntrace.doTrace("/rdsession(" + this.uid + ")/restoreExecuteRequestToCache/addedtocache=" + var1);
               }
            } else if (var2) {
               ntrace.doTrace("/rdsession(" + this.uid + ")/restoreExecuteRequestToCache/notaddedtocache=" + var1);
            }
         }

         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/restoreExecuteRequestToCache/20");
         }

      }
   }

   private void restoreObjectArray(Object[] var1) {
      var1[0] = null;
      var1[1] = null;
      var1[2] = null;
      var1[3] = null;
      synchronized(this.objectArrayList) {
         if (this.objectArrayList.size() < 10) {
            this.objectArrayList.add(var1);
         }

      }
   }

   private Object[] allocObjectArray() {
      Object[] var2 = null;
      synchronized(this.objectArrayList) {
         int var1 = this.objectArrayList.size();
         if (var1 > 0) {
            --var1;
            var2 = (Object[])((Object[])this.objectArrayList.remove(var1));
         }
      }

      if (var2 == null) {
         var2 = new Object[4];
      }

      return var2;
   }

   public synchronized void add_rplyObj(SessionAcallDescriptor var1, ReplyQueue var2, int var3, TpacallAsyncReply var4) throws TPException {
      boolean var5 = ntrace.isTraceEnabled(4);
      if (var5) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/add_rplyObj/" + var1 + "/" + var2 + "/" + var3);
      }

      long var6 = -1L;
      rdtimer var8 = null;
      if (var1 != null && var2 != null) {
         if (var3 == 0) {
            var6 = this.myBlocktime;
         } else if (var3 > 0) {
            var6 = (long)var3 * 1000L;
         } else {
            if (var3 != -1) {
               if (var5) {
                  ntrace.doTrace("*]/rdsession(" + this.uid + ")/add_rplyObj/20/");
               }

               throw new TPException(4, "Invalid blocking time " + var3);
            }

            var6 = -1L;
         }

         Object[] var9 = this.allocObjectArray();
         if (var6 != -1L) {
            var8 = new rdtimer(this, this.myGwatmi, var1, var2);
         }

         var9[0] = var2;
         var9[1] = var8;
         var9[2] = var4;
         var9[3] = null;
         this.rplyObjMap.put(var1, var9);
         if (var8 != null) {
            try {
               this.myTimeService.schedule(var8, var6);
            } catch (IllegalArgumentException var11) {
               this.rplyObjMap.remove(var1);
               throw new TPException(4, "Could not schedule block time " + var11);
            } catch (IllegalStateException var12) {
               this.rplyObjMap.remove(var1);
               throw new TPException(4, "Could not schedule block time " + var12);
            }
         }

         if (var5) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/add_rplyObj/40/");
         }

      } else {
         if (var5) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/add_rplyObj/10/");
         }

      }
   }

   public synchronized void addTimeoutRequest(int var1, int var2, long var3) throws TPException {
      boolean var5 = ntrace.isTraceEnabled(60000);
      if (var5) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/addTimeoutRequest/10/" + var2 + "/" + var3);
      }

      rdCtimer var6 = new rdCtimer(this.mySession, var2, var1);

      try {
         this.myTimeService.schedule(var6, var3);
         if (var5) {
            ntrace.doTrace("[/rdsession(" + this.uid + ")/addTimeoutRequest/20/" + var2 + "/" + var3 + "/Time out scheduled.");
         }

      } catch (IllegalArgumentException var8) {
         if (var5) {
            ntrace.doTrace("[/rdsession(" + this.uid + ")/addTimeoutRequest/30/" + var2 + "/" + var3 + "/Could not schedule time out: " + var8);
         }

         throw new TPException(4, "Could not schedule block time " + var8);
      } catch (IllegalStateException var9) {
         if (var5) {
            ntrace.doTrace("[/rdsession(" + this.uid + ")/addTimeoutRequest/40/" + var2 + "/" + var3 + "/Could not schedule time out: " + var9);
         }

         throw new TPException(4, "Could not schedule block time " + var9);
      }
   }

   public synchronized boolean addRplyObjTimeout(SessionAcallDescriptor var1, int var2) {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/addRplyObjTimeout/" + var1 + "/" + var2);
      }

      long var4 = -1L;
      rdtimer var6 = null;
      if (var1 == null) {
         if (var3) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/10/false");
         }

         return false;
      } else {
         if (var2 == 0) {
            var4 = this.myBlocktime;
         } else {
            if (var2 <= 0) {
               if (var2 == -1) {
                  if (var3) {
                     ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/20/true");
                  }

                  return true;
               }

               if (var3) {
                  ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/30/false");
               }

               return false;
            }

            var4 = (long)var2 * 1000L;
         }

         Object[] var7;
         if ((var7 = (Object[])((Object[])this.rplyObjMap.get(var1))) == null) {
            if (var3) {
               ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/40/true");
            }

            return true;
         } else {
            ReplyQueue var8;
            if ((var8 = (ReplyQueue)var7[0]) == null) {
               if (var3) {
                  ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/50/false");
               }

               return false;
            } else {
               if ((var6 = (rdtimer)var7[1]) != null) {
                  var6.cancel();
                  var7[1] = null;
               }

               var6 = new rdtimer(this, this.myGwatmi, var1, var8);
               var7[1] = var6;

               try {
                  this.myTimeService.schedule(var6, var4);
               } catch (IllegalArgumentException var10) {
                  if (var3) {
                     ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/60/false/" + var10);
                  }

                  return false;
               } catch (IllegalStateException var11) {
                  if (var3) {
                     ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/70/false/" + var11);
                  }

                  return false;
               }

               if (var3) {
                  ntrace.doTrace("]/rdsession(" + this.uid + ")/addRplyObjTimeout/80/true");
               }

               return true;
            }
         }
      }
   }

   public synchronized boolean remove_rplyObj(SessionAcallDescriptor var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/remove_rplyObj/" + var1);
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/remove_rplyObj/10/false");
         }

         return false;
      } else if (this.rplyObjMap.remove(var1) != null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/remove_rplyObj/20/true");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/remove_rplyObj/30/false");
         }

         return false;
      }
   }

   public synchronized Object[] removeReplyObj(SessionAcallDescriptor var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/removeReplyObj/" + var1);
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/removeReplyObj/10/null");
         }

         return null;
      } else {
         Object[] var3 = (Object[])((Object[])this.rplyObjMap.remove(var1));
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/removeReplyObj/20/" + var3);
         }

         return var3;
      }
   }

   private synchronized void addlist(tfmh var1, int var2) {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/addlist/" + var1 + "/" + var2);
      }

      rdtimer var11 = null;
      TdomTcb var4 = (TdomTcb)var1.tdom.body;
      int var10 = var4.get_convid();
      SessionAcallDescriptor var5;
      if (var2 != 5 && var2 != 6 && var10 == -1) {
         int var9 = var4.get_reqid();
         var5 = new SessionAcallDescriptor(var9, false);
      } else {
         var5 = new SessionAcallDescriptor(var10, true);
      }

      if (var3) {
         ntrace.doTrace("/rdsession(" + this.uid + ")/addlist/" + var5);
      }

      Object[] var8;
      if (var2 == 5) {
         var8 = (Object[])((Object[])this.rplyObjMap.get(var5));
      } else {
         var8 = (Object[])((Object[])this.rplyObjMap.remove(var5));
      }

      if (var8 != null) {
         if (var2 != 5 && (var11 = (rdtimer)var8[1]) != null) {
            var11.cancel();
         }

         ReplyQueue var6 = (ReplyQueue)var8[0];
         GatewayTpacallAsyncReply var7 = (GatewayTpacallAsyncReply)var8[2];
         if (var7 == null) {
            var6.add_reply(this.myGwatmi, var5, var1);
         } else {
            TpacallAsyncExecute var12 = new TpacallAsyncExecute(this.myGwatmi, var1, var5, var7);
            TCTaskHelper.schedule(var12);
         }

         if (var2 != 5) {
            this.restoreObjectArray(var8);
         }

         if (var3) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/addlist/10/");
         }

      } else {
         if (var3) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/addlist/20/");
         }

      }
   }

   public synchronized void add_rplyXidObj(Txid var1, TuxXidRply var2, int var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/add_rplyXidObj/" + var1 + "/" + var2);
      }

      long var5 = -1L;
      rdXtimer var7 = null;
      if (var1 != null && var2 != null) {
         if (var3 == 0) {
            var5 = this.myBlocktime;
         } else if (var3 > 0) {
            var5 = (long)var3 * 1000L;
         } else {
            if (var3 != -1) {
               if (var4) {
                  ntrace.doTrace("*]/rdsession(" + this.uid + ")/add_rplyXidObj/20/");
               }

               throw new TPException(4, "Invalid blocking time " + var3);
            }

            var5 = -1L;
         }

         if (var5 != -1L) {
            var7 = new rdXtimer(this, this.myGwatmi, var1, var2);
         }

         Boolean var9 = new Boolean(false);
         Object[] var8 = (Object[])((Object[])this.rplyXidObjMap.get(var1));
         if (var8 == null) {
            var8 = this.allocObjectArray();
         } else {
            var9 = (Boolean)var8[3];
         }

         var8[0] = var2;
         var8[1] = var7;
         var8[3] = var9;
         this.rplyXidObjMap.put(var1, var8);
         if (var7 != null) {
            try {
               this.myTimeService.schedule(var7, var5);
            } catch (IllegalArgumentException var11) {
               if (var4) {
                  ntrace.doTrace("*]/rdsession(" + this.uid + ")/add_rplyXidObj/30/" + var11);
               }

               throw new TPException(12, "Unable to set transaction timeout " + var11);
            } catch (IllegalStateException var12) {
               if (var4) {
                  ntrace.doTrace("*]/rdsession(" + this.uid + ")/add_rplyXidObj/40/" + var12);
               }

               throw new TPException(12, "Unable to set transaction timeout " + var12);
            }
         }

         if (var4) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/add_rplyXidObj/50/");
         }

      } else {
         if (var4) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/add_rplyXidObj/10/");
         }

      }
   }

   public synchronized boolean remove_rplyXidObj(Txid var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/remove_rplyXidObj/" + var1);
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/remove_rplyXidObj/10/false");
         }

         return false;
      } else if (this.rplyXidObjMap.remove(var1) != null) {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/remove_rplyXidObj/20/true");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/remove_rplyXidObj/30/false");
         }

         return false;
      }
   }

   private synchronized void addXidlist(tfmh var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/addXidlist/" + var1);
      }

      boolean var9 = false;
      Boolean var10 = new Boolean(false);
      TdomTranTcb var3 = (TdomTranTcb)var1.tdomtran.body;
      Txid var4 = new Txid(var3.getGlobalTransactionId());
      if (var2) {
         ntrace.doTrace("/rdsession(" + this.uid + ")/addXidlist/" + var4);
      }

      TdomTcb var8 = (TdomTcb)var1.tdom.body;
      int var11 = var8.get_opcode();
      Object[] var7;
      if (var11 == 8) {
         var9 = true;
         var7 = (Object[])((Object[])this.rplyXidObjMap.get(var4));
      } else {
         var7 = (Object[])((Object[])this.rplyXidObjMap.remove(var4));
      }

      if (var7 != null) {
         if (var9) {
            var10 = (Boolean)var7[3];
         }

         TuxXidRply var5;
         rdXtimer var6;
         if (!var10) {
            var5 = (TuxXidRply)var7[0];
            var6 = (rdXtimer)var7[1];
         } else {
            var5 = null;
            var6 = null;
         }

         if (var6 != null) {
            var6.cancel();
         }

         if (var9 && !var10) {
            var7[3] = new Boolean(true);
            this.rplyXidObjMap.put(var4, var7);
         }

         if (var5 != null) {
            var5.add_reply(this.myGwatmi, var4, var1);
            if (!var9) {
               this.restoreObjectArray(var7);
            }
         } else if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/addXidlist/ignoring duplicate opcode " + TdomTcb.print_opcode(var11) + "/");
         }

         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/addXidlist/10/");
         }

      } else {
         this.unknownXidRply.add_reply(this.myGwatmi, var4, var1);
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/addXidlist/20/");
         }

      }
   }

   private ReplyQueue allocConversationReplyObject(int var1) {
      ConversationReply var2 = new ConversationReply();
      Integer var3 = new Integer(var1);
      synchronized(this.conversationReplyObjects) {
         this.conversationReplyObjects.put(var3, var2);
         return var2;
      }
   }

   public ConversationReply getConversationReply(int var1) {
      Integer var3 = new Integer(var1);
      synchronized(this.conversationReplyObjects) {
         ConversationReply var2 = (ConversationReply)this.conversationReplyObjects.remove(var3);
         return var2;
      }
   }

   public void dispatch(tfmh var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/rdsession(" + this.uid + ")/dispatch/");
      }

      TPServiceRequest var7 = null;
      HashMap var12 = this.mySession.getRMICallList();
      Object[] var13 = null;
      TCAuthenticatedUser var14 = null;
      if (var1.tdom == null) {
         this.is_term = true;
         this.myGwatmi.setIsTerminated();
         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/10/");
         }

      } else {
         this.mySession.updateLastReceiveTime();
         TdomTcb var3 = (TdomTcb)var1.tdom.body;
         int var11 = var3.get_reqid();
         int var4 = var3.get_opcode();
         if (var2) {
            ntrace.doTrace("/rdsession(" + this.uid + ")/dispatch/TdomTcb=" + var3);
            if (var1.tdomtran != null) {
               ntrace.doTrace("/rdsession(" + this.uid + ")/dispatch/TdomTranTcb=" + (TdomTranTcb)var1.tdomtran.body);
            }
         }

         if (var4 == 1 || var4 == 4) {
            String var8;
            if (this.dom_protocol >= 15 && this.mySession.getAclPolicy() == 1) {
               if (var1.AAA != null) {
                  AaaTcb var15 = (AaaTcb)var1.AAA.body;
                  var8 = var15.getATZUserName();
               } else {
                  var8 = null;
               }
            } else {
               var8 = this.mySession.getRemoteDomainId();
            }

            try {
               var14 = TCSecurityManager.impersonate(var8);
            } catch (LoginException var27) {
               tfmh var16 = new tfmh(1);
               TdomTcb var17 = new TdomTcb(3, var11, 0, (String)null);
               var17.set_diagnostic(8);
               var16.tdom = new tcm((short)7, var17);
               synchronized(this.ostream) {
                  try {
                     if (this.dom_protocol >= 15) {
                        var16.write_tfmh(this.ostream, this.mySession.getCompressionThreshold());
                     } else {
                        var16.write_dom_65_tfmh(this.ostream, this.local_domain_name, this.dom_protocol, this.mySession.getCompressionThreshold());
                     }
                  } catch (IOException var25) {
                     if (var2) {
                        ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/15/" + "fail to send failure reply: " + var25);
                     }
                  }
               }

               if (var2) {
                  ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/20/" + "Failed to get user identity: " + var27);
               }

               return;
            }
         }

         switch (var4) {
            case 2:
            case 3:
            case 5:
            case 6:
               if (var2) {
                  ntrace.doTrace("/rdsession/dispatch/25/reqId = " + var11);
               }

               if (var12 != null) {
                  synchronized(var12) {
                     if ((var13 = (Object[])((Object[])var12.remove(new Integer(var11)))) != null) {
                        if (var2) {
                           ntrace.doTrace("/rdsession/dispatch/30:reply to RMI/IIOP call: reqId =" + var11);
                        }

                        RMIReplyRequest var32 = new RMIReplyRequest(var1, var13, this.mySession);
                        var32.execute();
                        break;
                     }
                  }
               }

               this.addlist(var1, var4);
               break;
            case 4:
               int var9 = var3.get_convid();
               SessionAcallDescriptor var10 = new SessionAcallDescriptor(var9, true);
               ReplyQueue var6 = this.allocConversationReplyObject(var9);

               try {
                  this.add_rplyObj(var10, var6, 0, (TpacallAsyncReply)null);
               } catch (TPException var24) {
                  if (var2) {
                     ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/Error dispatching " + "conversational service" + var24);
                  }

                  return;
               }
            case 1:
               try {
                  var7 = new TPServiceRequest(this.myGwatmi, this.dom_protocol, this.local_domain_name, this.ostream, this.myInvoker, var1, this.useBetaFeatures);
               } catch (TPException var23) {
                  if (var2) {
                     ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/Error dispatching service" + var23);
                  }

                  return;
               }

               if (var14 != null) {
                  var7.setTargetSubject(var14);
               }

               TCTaskHelper.schedule(var7);
               break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 24:
               if (var2) {
                  ntrace.doTrace("/rdsession/dispatch/35/reqId = " + var11);
               }

               if (var12 != null) {
                  Txid var30 = null;
                  tcm var31 = var1.tdomtran;
                  if (var31 != null) {
                     TdomTranTcb var33 = (TdomTranTcb)var1.tdomtran.body;
                     if (var33 != null) {
                        var30 = new Txid(var33.getGlobalTransactionId());
                        if (var2) {
                           ntrace.doTrace("/rdsession/dispatch/37/Txid = " + var30);
                        }
                     }
                  }

                  synchronized(var12) {
                     Iterator var18 = var12.values().iterator();

                     while(var18.hasNext()) {
                        var13 = (Object[])((Object[])var18.next());
                        if (var13[3] != null && var30.equals(var13[3])) {
                           var18.remove();
                           if (var2) {
                              ntrace.doTrace("/rdsession/dispatch/40: reply to RMI/IIOP call: GIOPreqId =" + var13[2]);
                           }

                           RMIReplyRequest var19 = new RMIReplyRequest(var1, var13, this.mySession);
                           var19.execute();
                        }
                     }
                  }
               }

               this.addXidlist(var1);
               break;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            default:
               this.is_term = true;
               this.myGwatmi.doLocalTerminate();
               if (var2) {
                  ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/50/");
               }

               return;
            case 23:
               if (var3.get_diagnostic() == 0) {
                  this.mySession.sendKeepAliveAcknowledge();
               }
         }

         if (var2) {
            ntrace.doTrace("]/rdsession(" + this.uid + ")/dispatch/60/");
         }

      }
   }

   public void setSessionReference(dsession var1) {
      this.mySession = var1;
      this.uid = var1.getUid();
   }

   private byte[] getTranXID(tfmh var1) {
      TdomTranTcb var2 = (TdomTranTcb)var1.tdomtran.body;
      return var2.getGlobalTransactionId();
   }
}
