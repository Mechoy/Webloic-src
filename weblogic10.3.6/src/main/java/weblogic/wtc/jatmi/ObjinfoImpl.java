package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.Tpconvert;
import com.bea.core.jatmi.common.ntrace;
import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.wtc.gwt.TDMLocalTDomain;
import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.gwt.TuxedoConnectionFactory;
import weblogic.wtc.gwt.WTCService;

public class ObjinfoImpl implements Objinfo {
   private int keyType;
   private ObjectId oid;
   private String domainId;
   private int groupId;
   private String intfId;
   private String implId = new String("");
   private int scaIntfBkt = -1;
   private int aomHandle = -1;
   private int[] svcTmid;
   private int connId = -1;
   private int connGen = -1;
   private int appKey = -1;
   private int islGrpno = -1;
   private int islSrvId = -1;
   private ClientInfo sendSrcCltinfo = new ClientInfo();
   private ClientInfo recvSrcCltinfo = new ClientInfo();
   private ClientInfo cltinfo = new ClientInfo();
   private String origDomain;
   private short isMyDomain = 1;
   private int isACallout;

   public ObjinfoImpl() {
   }

   public ObjinfoImpl(BEAObjectKey var1, ClientInfo var2, BindInfo var3, int var4) throws TPException, IOException {
      TypedFML32 var9 = null;
      tfmh var11 = null;
      boolean var12 = ntrace.isTraceEnabled(4);
      if (var12) {
         ntrace.doTrace("[/ObjinfoImpl/ObjinfoImpl/0");
      }

      if (var1 == null) {
         if (var12) {
            ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/5");
         }

         throw new TPException(4);
      } else {
         var1.getInfo(this);
         if (var2 == null) {
            if ((var4 & 1) != 0) {
               if (var12) {
                  ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/10");
               }

               throw new TPException(9);
            }
         } else if (var2.getId().getTimestamp() != 0) {
            this.cltinfo = new ClientInfo(var2);
            this.domainId = this.cltinfo.getDomain();
            this.isACallout = 1;
         }

         if (this.domainId != null && !this.domainId.equals("")) {
            TDMLocalTDomain var5 = WTCService.getWTCService().getLocalDomain(this.domainId);
            if (null == var5) {
               this.isMyDomain = 0;
               if (var12) {
                  ntrace.doTrace("/ObjinfoImpl/ObjinfoImpl/20");
               }
            }
         }

         if ((var4 & 1) != 0) {
            if (var3 == null) {
               if (var12) {
                  ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/30");
               }

               throw new TPException(9);
            }

            this.cltinfo.setCntxt(0);
            TypedFML32 var7 = new TypedFML32();

            try {
               var7.Fchg(new FmlKey(167772261, 0), var3.getHost());
               var7.Fchg(new FmlKey(33554534, 0), new Short(var3.getPort()));
               var7.Fchg(new FmlKey(33554544, 0), new Short(var3.getSSLSupports()));
               var7.Fchg(new FmlKey(33554545, 0), new Short(var3.getSSLRequires()));
            } catch (Ferror var19) {
               if (var12) {
                  ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/35");
               }

               throw new TPException(12);
            }

            String var6;
            if (this.isMyDomain == 1) {
               var6 = new String(".O_BIND");
            } else {
               if (this.domainId == null) {
                  if (var12) {
                     ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/40");
                  }

                  throw new TPException(12);
               }

               var6 = new String("//" + this.domainId);
               if (var7 instanceof StandardTypes) {
                  var9 = var7;
                  var11 = (tfmh)var7.getTfmhCache();
               }

               if (var11 == null) {
                  tcm var10 = new tcm((short)0, new UserTcb(var7));
                  var11 = new tfmh(var7.getHintIndex(), var10, 1);
               }

               if (var11.tdom_vals == null) {
                  var11.tdom_vals = new tcm((short)17, new TdomValsTcb());
                  TdomValsTcb var13 = (TdomValsTcb)var11.tdom_vals.body;
                  var13.setDescrim(1);
                  var13.setSrvc(new String(".O_BIND"));
               }

               if (var9 != null) {
                  var9.setTfmhCache(var11);
               }
            }

            TuxedoConnectionFactory var14;
            try {
               InitialContext var23 = new InitialContext();
               var14 = (TuxedoConnectionFactory)var23.lookup("tuxedo.services.TuxedoConnection");
            } catch (NamingException var21) {
               if (var12) {
                  ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/45");
               }

               throw new TPException(12, "Could not get TuxedoConnectionFactory : " + var21);
            }

            TuxedoConnection var15 = var14.getTuxedoConnection();
            if (var12) {
               ntrace.doTrace("/ObjinfoImpl/ObjinfoImpl/50 srvc = " + var6);
            }

            Reply var16;
            try {
               var16 = var15.tpcall(var6, var7, 8);
            } catch (TPException var20) {
               int var18 = var20.gettperrno();
               if (var18 != 10 && var18 != 11) {
                  if (var18 == 6) {
                     if (var12) {
                        ntrace.doTrace("/ObjinfoImpl/ObjinfoImpl/70:ERROR: Asymmetric outbound IIOP not enabled - add the -O option to the ISL in the Tuxedo Domain " + this.domainId);
                     }

                     throw new TPException(6);
                  }

                  if (var12) {
                     ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/75");
                  }

                  throw new TPException(12);
               }

               if (var12) {
                  ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/60");
               }

               throw new TPException(5);
            }

            if (var12) {
               ntrace.doTrace("/ObjinfoImpl/ObjinfoImpl/80");
            }

            var15.tpterm();
            TypedFML32 var8 = (TypedFML32)var16.getReplyBuffer();

            try {
               this.connId = ((Short)var8.Fget(new FmlKey(104, 0))).intValue();
               this.connGen = ((Long)var8.Fget(new FmlKey(33554537, 0))).intValue();
               String var17 = new String((String)var8.Fget(new FmlKey(167772266, 0)));
               if (var12) {
                  ntrace.doTrace("/ObjinfoImpl/ObjinfoImpl/85 cltId = " + var17);
               }

               this.cltinfo.setId(Tpconvert.getClientId(var17));
               this.cltinfo.setDomain(new String((String)var8.Fget(new FmlKey(167772267, 0))));
               this.domainId = this.cltinfo.getDomain();
               this.cltinfo.setPid(((Long)var8.Fget(new FmlKey(33554540, 0))).intValue());
               this.cltinfo.setQaddr(((Long)var8.Fget(new FmlKey(33554541, 0))).intValue());
               this.islGrpno = ((Long)var8.Fget(new FmlKey(33554542, 0))).intValue();
               this.islSrvId = ((Long)var8.Fget(new FmlKey(33554543, 0))).intValue();
               new ClientInfo(this.cltinfo);
               this.isACallout = 1;
            } catch (Ferror var22) {
               if (var12) {
                  ntrace.doTrace("*]/ObjinfoImpl/ObjinfoImpl/90");
               }

               throw new TPException(12);
            }
         }

         if (var12) {
            ntrace.doTrace("]/ObjinfoImpl/ObjinfoImpl/100");
         }

      }
   }

   public ObjectId getObjectId() {
      return this.oid;
   }

   public void setObjectId(ObjectId var1) {
      this.oid = var1;
   }

   public int getKeyType() {
      return this.keyType;
   }

   public void setKeyType(int var1) {
      this.keyType = var1;
   }

   public String getDomainId() {
      return this.domainId;
   }

   public void setOrigDomain(String var1) {
      this.origDomain = var1;
   }

   public void setDomainId(String var1) {
      this.domainId = var1;
   }

   public String getIntfId() {
      return this.intfId;
   }

   public void setIntfId(String var1) {
      this.intfId = var1;
   }

   public int getGroupId() {
      return this.groupId;
   }

   public void setGroupId(int var1) {
      this.groupId = var1;
   }

   public short getIsMyDomain() {
      return this.isMyDomain;
   }

   public void setIsMyDomain(short var1) {
      this.isMyDomain = var1;
   }

   public ClientInfo getRecvSrcCltinfo() {
      return this.recvSrcCltinfo;
   }

   public ClientInfo getSendSrcCltinfo() {
      return this.sendSrcCltinfo;
   }

   public void setRecvSrcCltinfo(ClientInfo var1) {
      this.recvSrcCltinfo = var1;
   }

   public void setSendSrcCltinfo(ClientInfo var1) {
      this.sendSrcCltinfo = var1;
   }

   public void setCltinfo(ClientInfo var1) {
      this.cltinfo = var1;
   }

   public ClientInfo getCltinfo() {
      return this.cltinfo;
   }

   public int getIsACallout() {
      return this.isACallout;
   }

   public void setIsACallout(int var1) {
      this.isACallout = var1;
   }

   public int getScaIntfBkt() {
      return this.scaIntfBkt;
   }

   public void setScaIntfBkt(int var1) {
      this.scaIntfBkt = var1;
   }

   public int getConnGen() {
      return this.connGen;
   }

   public void setConnGen(int var1) {
      this.connGen = var1;
   }

   public int getAppKey() {
      return this.appKey;
   }

   public void setAppKey(int var1) {
      this.appKey = var1;
   }

   public int getConnId() {
      return this.connId;
   }

   public void setConnId(int var1) {
      this.connId = var1;
   }

   public void setSvcTmid(int[] var1) {
      this.svcTmid = var1;
   }

   public void setAOMHandle(int var1) {
      this.aomHandle = var1;
   }
}
