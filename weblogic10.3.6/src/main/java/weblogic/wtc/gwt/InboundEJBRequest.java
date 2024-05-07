package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.internal.TuxedoXid;
import com.bea.core.jatmi.intf.TCTask;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import weblogic.application.AppClassLoaderManager;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.InvokeInfo;
import weblogic.wtc.jatmi.Objrecv;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.SessionAcallDescriptor;
import weblogic.wtc.jatmi.TGIOPUtil;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPReplyException;
import weblogic.wtc.jatmi.TPRequestAsyncReply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TdomTranTcb;
import weblogic.wtc.jatmi.TuxedoService;
import weblogic.wtc.jatmi.TuxedoServiceHome;
import weblogic.wtc.jatmi.TypedBuffer;
import weblogic.wtc.jatmi.UserTcb;
import weblogic.wtc.jatmi.dsession;
import weblogic.wtc.jatmi.rdsession;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;

public class InboundEJBRequest implements TCTask {
   private ServiceParameters myParam;
   private OatmialServices myServices;
   private TDMLocal myLocalDomain;
   private TDMRemote myRemoteDomain;
   private TuxedoXid myXid;
   private SessionAcallDescriptor myConvDesc;
   private XAResource wlsXaResource;
   private String myName;

   InboundEJBRequest(ServiceParameters var1, TDMLocal var2, TDMRemote var3) {
      this.myParam = var1;
      this.myServices = WTCService.getOatmialServices();
      this.myLocalDomain = var2;
      this.myRemoteDomain = var3;
      this.myXid = null;
      this.myConvDesc = null;
      this.wlsXaResource = null;
   }

   public int execute() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/InboundEJBRequest/execute/" + Thread.currentThread());
      }

      TDMExport var6 = null;
      TypedBuffer var13 = null;
      int var14 = 0;
      int var15 = 0;
      int var19 = -1;
      rdsession var20 = null;
      TdomTranTcb var21 = null;
      Object[] var22 = null;
      if (this.myParam == null) {
         if (var1) {
            ntrace.doTrace("]/InboundEJBRequest/execute/10");
         }

         return 0;
      } else {
         InvokeInfo var2;
         if ((var2 = this.myParam.get_invokeInfo()) == null) {
            if (var1) {
               ntrace.doTrace("]/InboundEJBRequest/execute/20");
            }

            return 0;
         } else {
            dsession var3;
            if ((var3 = (dsession)this.myParam.get_gwatmi()) == null) {
               if (var1) {
                  ntrace.doTrace("]/InboundEJBRequest/execute/30");
               }

               return 0;
            } else {
               Serializable var4;
               if ((var4 = var2.getReqid()) == null) {
                  if (var1) {
                     ntrace.doTrace("]/InboundEJBRequest/execute/40");
                  }

                  return 0;
               } else {
                  tfmh var17;
                  if ((var17 = var2.getServiceMessage()) == null) {
                     var3.send_failure_return(var4, new TPException(4), var19);
                     if (var1) {
                        ntrace.doTrace("]/InboundEJBRequest/execute/50");
                     }

                     return 0;
                  } else {
                     TdomTcb var18;
                     if (var17.tdom != null && (var18 = (TdomTcb)var17.tdom.body) != null) {
                        if ((var19 = var18.get_convid()) != -1) {
                           this.myConvDesc = new SessionAcallDescriptor(var19, true);
                           var20 = var3.get_rcv_place();
                        }

                        String var5;
                        if ((var5 = var2.getServiceName()) != null && !var5.equals("")) {
                           TPException var62;
                           if (var17.route != null) {
                              if (var1) {
                                 ntrace.doTrace("]/InboundEJBRequest/execute/75");
                              }

                              if (var17.user == null) {
                                 if (var19 == -1) {
                                    var3.send_failure_return(var4, new TPException(12), var19);
                                 } else {
                                    var20.remove_rplyObj(this.myConvDesc);
                                 }

                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/80");
                                 }
                              } else {
                                 if (var17.tdomtran != null && (var21 = (TdomTranTcb)var17.tdomtran.body) != null) {
                                    if (var21 == null) {
                                       var62 = new TPException(4, "NULL transaction");
                                       var3.send_failure_return(var4, var62, var19);
                                       if (var1) {
                                          ntrace.doTrace("*]/InboundEJBRequest/execute/81");
                                       }

                                       return 0;
                                    }

                                    try {
                                       this.myXid = new TuxedoXid(var21);
                                    } catch (TPException var48) {
                                       var3.send_failure_return(var4, var48, var19);
                                       if (var1) {
                                          ntrace.doTrace("*]/InboundEJBRequest/execute/81.5");
                                       }

                                       return 0;
                                    }

                                    if ((this.wlsXaResource = TCTransactionHelper.getXAResource()) == null) {
                                       var3.send_failure_return(var4, new TPException(14), var19);
                                       if (var1) {
                                          ntrace.doTrace("]/InboundEJBRequest/execute/82");
                                       }

                                       return 0;
                                    }

                                    try {
                                       this.wlsXaResource.start(this.myXid, 0);
                                    } catch (XAException var44) {
                                       var3.send_failure_return(var4, new TPException(14), var19);
                                       if (var1) {
                                          ntrace.doTrace("]/InboundEJBRequest/execute/83/XAException" + var44);
                                       }

                                       return 0;
                                    }

                                    if (var1) {
                                       ntrace.doTrace("]/InboundEJBRequest/transaction started/");
                                    }

                                    this.myServices.addInboundRdomToXid(this.myXid, this.myRemoteDomain);
                                    var22 = new Object[]{this.myXid, this.wlsXaResource, this.myRemoteDomain};
                                 }

                                 Objrecv var63 = new Objrecv(var17);
                                 MethodParameters var64 = new MethodParameters(this.myParam, var63, var22, var3);

                                 try {
                                    TGIOPUtil.injectMsgIntoRMI(var17, var64);
                                 } catch (IOException var36) {
                                    throw new RuntimeException(var36);
                                 }

                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/85");
                                 }
                              }

                              return 0;
                           } else if ((var6 = WTCService.getWTCService().getExportedService(var5, this.myLocalDomain.getAccessPoint())) == null) {
                              if (var19 == -1) {
                                 var3.send_failure_return(var4, new TPException(6), var19);
                              } else {
                                 var20.remove_rplyObj(this.myConvDesc);
                              }

                              if (var1) {
                                 ntrace.doTrace("]/InboundEJBRequest/execute/90");
                              }

                              return 0;
                           } else {
                              this.myParam.setUser();
                              if (var17.tdomtran != null && (var21 = (TdomTranTcb)var17.tdomtran.body) != null) {
                                 if (var21 == null) {
                                    var62 = new TPException(4, "NULL transaction");
                                    this.myParam.removeUser();
                                    var3.send_failure_return(var4, var62, var19);
                                    if (var1) {
                                       ntrace.doTrace("*]/InboundEJBRequest/execute/150");
                                    }

                                    return 0;
                                 }

                                 try {
                                    this.myXid = new TuxedoXid(var21);
                                 } catch (TPException var45) {
                                    this.myParam.removeUser();
                                    var3.send_failure_return(var4, var45, var19);
                                    if (var1) {
                                       ntrace.doTrace("*]/InboundEJBRequest/execute/150.5");
                                    }

                                    return 0;
                                 }

                                 if ((this.wlsXaResource = TCTransactionHelper.getXAResource()) == null) {
                                    var3.send_failure_return(var4, new TPException(14), var19);
                                    this.myParam.removeUser();
                                    if (var1) {
                                       ntrace.doTrace("]/InboundEJBRequest/execute/160");
                                    }

                                    return 0;
                                 }

                                 try {
                                    this.wlsXaResource.start(this.myXid, 0);
                                 } catch (XAException var37) {
                                    var3.send_failure_return(var4, new TPException(14), var19);
                                    this.myParam.removeUser();
                                    if (var1) {
                                       ntrace.doTrace("*]/InboundEJBRequest/execute/180/" + var37);
                                    }

                                    return 0;
                                 }

                                 if (var1) {
                                    ntrace.doTrace("transaction started");
                                 }

                                 this.myServices.addInboundRdomToXid(this.myXid, this.myRemoteDomain);
                              }

                              String var7 = var6.getEJBName();
                              if (var7 == null || "".equals(var7)) {
                                 String var8;
                                 if ((var8 = var6.getTargetClass()) != null) {
                                    TPRequestAsyncReplyImpl var61 = new TPRequestAsyncReplyImpl(this.myParam, this.myRemoteDomain, this.myXid);
                                    if (var1) {
                                       ntrace.doTrace("/InboundEJBRequest/run/looking up POJO " + var8);
                                    }

                                    Object var24 = this.getClass().getClassLoader();
                                    String var9;
                                    if ((var9 = var6.getTargetJar()) != null) {
                                       if (var9.endsWith(".jar")) {
                                          try {
                                             var24 = new POJOClassLoader(var9, (ClassLoader)var24);
                                          } catch (IOException var50) {
                                             if (var1) {
                                                ntrace.doTrace("]/InboundEJBRequest/run/problem loading jar " + var9 + ": " + var50);
                                             }

                                             var24 = this.getClass().getClassLoader();
                                          }
                                       } else {
                                          var24 = AppClassLoaderManager.getAppClassLoaderManager().findModuleLoader(var9, (String)null);
                                          if (var1) {
                                             ntrace.doTrace("/InboundEJBRequest/run/using Application Class Loader for " + var9 + " " + var24);
                                          }

                                          if (var24 == null) {
                                             var24 = this.getClass().getClassLoader();
                                          }
                                       }
                                    }

                                    Class var25 = null;
                                    Method var26 = null;
                                    Class[] var27 = new Class[]{TPServiceInformation.class, TPRequestAsyncReply.class};

                                    try {
                                       var25 = Class.forName(var8, true, (ClassLoader)var24);
                                    } catch (ClassNotFoundException var43) {
                                       this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(6, "Could not find " + var8 + " : " + var43));
                                       if (var1) {
                                          ntrace.doTrace("]/InboundEJBRequest/run/190/" + var43);
                                       }

                                       return 0;
                                    }

                                    try {
                                       var26 = var25.getMethod(var6.getResourceName(), var27);
                                    } catch (NoSuchMethodException var42) {
                                       this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(6, "Method " + var6.getResourceName() + " not found:" + var42));
                                       if (var1) {
                                          ntrace.doTrace("]/InboundEJBRequest/run/200/" + var42);
                                       }

                                       return 0;
                                    }

                                    long var28 = this.myLocalDomain.getBlockTime();
                                    if (var21 != null) {
                                       var28 = (long)(var21.getTransactionTimeout() * 1000);
                                    }

                                    InboundTimer var31 = null;
                                    if (var28 > 0L) {
                                       Timer var30 = this.myServices.getTimeService();
                                       var31 = new InboundTimer(var3, var4, var19, var61);
                                       var61.setTimeoutTask(var31);

                                       try {
                                          if (var1) {
                                             ntrace.doTrace("/InboundEJBRequest/run/Set up timer: " + var28 + " milliseconds");
                                          }

                                          var30.schedule(var31, var28);
                                       } catch (IllegalArgumentException var46) {
                                          this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(12, "Cannot schedule timer for " + var8 + ": " + var46));
                                          if (var1) {
                                             ntrace.doTrace("]/InboundEJBRequest/run/204/" + var46);
                                          }

                                          return 0;
                                       } catch (IllegalStateException var47) {
                                          this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(12, "Cannot schedule timer for " + var8 + ": " + var47));
                                          if (var1) {
                                             ntrace.doTrace("]/InboundEJBRequest/run/205/" + var47);
                                          }

                                          return 0;
                                       }
                                    }

                                    try {
                                       Object[] var32 = new Object[]{var2, var61};
                                       var26.invoke(var25.newInstance(), var32);
                                    } catch (InstantiationException var38) {
                                       this.cleanUp(var19, var3, var4, var31, new TPException(12, "Exception in POJO service " + var8 + ": " + var38));
                                       if (var1) {
                                          ntrace.doTrace("]/InboundEJBRequest/run/210/" + var38);
                                       }

                                       return 0;
                                    } catch (IllegalAccessException var39) {
                                       this.cleanUp(var19, var3, var4, var31, new TPException(12, "Exception in POJO service " + var8 + ": " + var39));
                                       if (var1) {
                                          ntrace.doTrace("]/InboundEJBRequest/run/220/" + var39);
                                       }

                                       return 0;
                                    } catch (InvocationTargetException var40) {
                                       Throwable var33 = var40.getTargetException();
                                       if (var33 instanceof TPException) {
                                          this.cleanUp(var19, var3, var4, var31, (TPException)var33);
                                          if (var1) {
                                             ntrace.doTrace("]/InboundEJBRequest/run/230/" + var33);
                                          }
                                       } else {
                                          this.cleanUp(var19, var3, var4, var31, new TPException(12, "Exception in POJO service " + var8 + ": " + (Exception)var33));
                                          if (var1) {
                                             ntrace.doTrace("]/InboundEJBRequest/run/240/" + var33);
                                          }
                                       }

                                       return 0;
                                    }

                                    if (this.wlsXaResource != null) {
                                       try {
                                          this.wlsXaResource.end(this.myXid, 67108864);
                                       } catch (XAException var41) {
                                          XAException var65 = var41;
                                          WTCLogger.logErrorXAEnd(var41);
                                          this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
                                          if (var19 == -1) {
                                             if (var61 != null) {
                                                synchronized(var61) {
                                                   if (!var61.getCalled()) {
                                                      var3.send_failure_return(var4, new TPException(10, "Exception " + var65 + " in service: " + var2.getServiceName()), var19);
                                                      var61.setCalled(true);
                                                   }
                                                }
                                             }
                                          } else {
                                             var20.remove_rplyObj(this.myConvDesc);
                                          }

                                          this.myParam.removeUser();
                                          if (var1) {
                                             ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/80/" + var41);
                                          }

                                          return 0;
                                       }
                                    }

                                    if (var1) {
                                       ntrace.doTrace("/InboundEJBRequest/run/success POJO " + var8);
                                    }

                                    return 0;
                                 }

                                 var7 = new String("tuxedo.services." + var6.getResourceName() + "Home");
                              }

                              Reply var12;
                              try {
                                 if (var1) {
                                    ntrace.doTrace("/InboundEJBRequest/execute/looking up " + var7);
                                 }

                                 Object var23 = this.myServices.getNameService().lookup(var7);
                                 TuxedoServiceHome var10 = (TuxedoServiceHome)PortableRemoteObject.narrow(var23, TuxedoServiceHome.class);
                                 TuxedoService var11 = (TuxedoService)PortableRemoteObject.narrow(var10.create(), TuxedoService.class);
                                 if (var1) {
                                    ntrace.doTrace("/InboundEJBRequest/execute/invoking EJB " + var7);
                                 }

                                 var12 = var11.service(var2);
                                 if (var1) {
                                    ntrace.doTrace("/InboundEJBRequest/execute/success EJB " + var7);
                                 }

                                 if (var12 != null) {
                                    var13 = var12.getReplyBuffer();
                                    var14 = var12.gettpurcode();
                                 }
                              } catch (NamingException var53) {
                                 this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(6));
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/250");
                                 }

                                 return 0;
                              } catch (CreateException var54) {
                                 this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(6, "Could not create " + var7 + ": " + var54));
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/280");
                                 }

                                 return 0;
                              } catch (AccessException var55) {
                                 this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(6, var7 + " is not accessible: " + var55));
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/310");
                                 }

                                 return 0;
                              } catch (RemoteException var56) {
                                 this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(6, var7 + " is not available: " + var56));
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/340");
                                 }

                                 return 0;
                              } catch (TPReplyException var57) {
                                 if (this.wlsXaResource != null) {
                                    try {
                                       this.wlsXaResource.end(this.myXid, 536870912);
                                       this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
                                       this.wlsXaResource.rollback(this.myXid);
                                    } catch (XAException var52) {
                                       if (var1) {
                                          ntrace.doTrace("/InboundEJBRequest/error ending transaction/" + var52);
                                       }
                                    }

                                    this.wlsXaResource = null;
                                 }

                                 if (var1) {
                                    ntrace.doTrace("/InboundEJBRequest/execute/tpReplyerro " + var57);
                                 }

                                 var15 = var57.gettperrno();
                                 var12 = var57.getExceptionReply();
                                 if (var12 != null) {
                                    var13 = var12.getReplyBuffer();
                                    var14 = var12.gettpurcode();
                                 }
                              } catch (TPException var58) {
                                 this.cleanUp(var19, var3, var4, (TimerTask)null, var58);
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/390/" + var58);
                                 }

                                 return 0;
                              } catch (Exception var59) {
                                 this.cleanUp(var19, var3, var4, (TimerTask)null, new TPException(12, "Exception in service: " + var59));
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/420/" + var59);
                                 }

                                 return 0;
                              }

                              if (this.wlsXaResource != null) {
                                 try {
                                    this.wlsXaResource.end(this.myXid, 67108864);
                                 } catch (XAException var49) {
                                    WTCLogger.logErrorXAEnd(var49);
                                    this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
                                    if (var19 == -1) {
                                       var3.send_failure_return(var4, new TPException(10, "Exception " + var49 + " in service: " + var5), var19);
                                    } else {
                                       var20.remove_rplyObj(this.myConvDesc);
                                    }

                                    this.myParam.removeUser();
                                    if (var1) {
                                       ntrace.doTrace("]/InboundEJBRequest/execute/430/" + var49);
                                    }

                                    return 0;
                                 }
                              }

                              if ((var18.get_flag() & 4) != 0) {
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/TPNOREPLY set");
                                 }

                                 return 0;
                              } else {
                                 tfmh var16;
                                 if (var13 == null) {
                                    var16 = new tfmh(1);
                                 } else {
                                    tcm var60 = new tcm((short)0, new UserTcb(var13));
                                    var16 = new tfmh(var13.getHintIndex(), var60, 1);
                                 }

                                 try {
                                    if (var1) {
                                       ntrace.doTrace("]/InboundEJBRequest/execute/sending success " + var4);
                                    }

                                    var3.send_success_return(var4, var16, var15, var14, var19);
                                 } catch (TPException var51) {
                                    if (var19 == -1) {
                                       var3.send_failure_return(var2.getReqid(), var51, var19);
                                    } else {
                                       var20.remove_rplyObj(this.myConvDesc);
                                    }

                                    this.myParam.removeUser();
                                    if (var1) {
                                       ntrace.doTrace("]/InboundEJBRequest/execute/460/" + var51);
                                    }

                                    return 0;
                                 }

                                 if (var19 != -1) {
                                    var20.remove_rplyObj(this.myConvDesc);
                                 }

                                 this.myParam.removeUser();
                                 if (var1) {
                                    ntrace.doTrace("]/InboundEJBRequest/execute/480");
                                 }

                                 return 0;
                              }
                           }
                        } else {
                           if (var19 == -1) {
                              var3.send_failure_return(var4, new TPException(4), var19);
                           } else {
                              var20.remove_rplyObj(this.myConvDesc);
                           }

                           if (var1) {
                              ntrace.doTrace("]/InboundEJBRequest/execute/70");
                           }

                           return 0;
                        }
                     } else {
                        var3.send_failure_return(var4, new TPException(4), var19);
                        if (var1) {
                           ntrace.doTrace("]/InboundEJBRequest/execute/60");
                        }

                        return 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void cleanUp(int var1, dsession var2, Serializable var3, TimerTask var4, TPException var5) {
      boolean var6 = ntrace.isTraceEnabled(2);
      if (var4 != null) {
         var4.cancel();
      }

      if (this.wlsXaResource != null) {
         try {
            this.wlsXaResource.end(this.myXid, 536870912);
            this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
            this.wlsXaResource.rollback(this.myXid);
         } catch (XAException var11) {
            if (var6) {
               ntrace.doTrace("/InboundEJBRequest/cleanUp/error ending transaction/" + var11);
            }
         } finally {
            this.wlsXaResource = null;
         }
      }

      if (var1 == -1) {
         var2.send_failure_return(var3, var5, var1);
      } else {
         var2.get_rcv_place().remove_rplyObj(this.myConvDesc);
      }

      this.myParam.removeUser();
   }

   public void setTaskName(String var1) {
      this.myName = new String("InboundEJBRequest$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "InboundEJBRequest$unknown" : this.myName;
   }

   private class InboundTimer extends TimerTask {
      dsession _rplyObj;
      Serializable _rd;
      int _convid = -1;
      TPRequestAsyncReplyImpl _aReply;

      InboundTimer(dsession var2, Serializable var3, int var4, TPRequestAsyncReplyImpl var5) {
         this._rplyObj = var2;
         this._rd = var3;
         this._convid = var4;
         this._aReply = var5;
      }

      public void run() {
         boolean var1 = ntrace.isTraceEnabled(2);
         if (var1) {
            ntrace.doTrace("/InboundEJBRequest$InboundTimer/send timeout");
         }

         if (this._aReply != null && !this._aReply.isDone()) {
            synchronized(this._aReply) {
               if (!this._aReply.getCalled()) {
                  InboundEJBRequest.this.cleanUp(this._convid, this._rplyObj, this._rd, (TimerTask)null, new TPException(13));
                  this._aReply.setCalled(true);
               }
            }
         } else {
            InboundEJBRequest.this.cleanUp(this._convid, this._rplyObj, this._rd, (TimerTask)null, new TPException(13));
         }

      }
   }
}
