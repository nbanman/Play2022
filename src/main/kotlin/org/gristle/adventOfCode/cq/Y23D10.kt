package org.gristle.adventOfCode.cq

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.groupValues

fun main() {
    val edgePattern = Regex("""([A-Z]{3}):(\d+)""")

    val edges: Map<String, List<Graph.Edge<String>>> = input[0]
        .lineSequence()
        .associate { line ->
            val vertex = line.take(3)

            val destinations = line
                .groupValues(edgePattern)
                .map { (destination, weight) -> Graph.Edge(destination, weight.toDouble() + 600) }

            vertex to destinations
        }

    val time = Graph.dijkstra("TYC", endCondition = { it == "EAR" }, edges).steps() - 600
    println(time)
}

private val input = listOf(
    """EAR => DEV:2694 MGE:4668 TYT:8730
TYC => IUD:2889
QJD => XGP:4144 VOA:1271
TYT => VOA:5898 EAR:8730
YNQ => GWO:9282
HOO => DWM:9886 WYY:4143 NWY:2236
LLB => NMB:3770 VJE:8955
GLU => YDR:9672
XMP => HAY:9573
HMT => BJX:8327 ZXV:1989
ZHP => WMY:9855 PDP:3175
VAP => UZZ:2936 NPD:4181
TTV => XPD:4814 RDU:2867
GBY => PXD:4592 RAB:4998 UQJ:4089
DIJ => HGO:3244 LUP:2112
XMX => JJV:6851 XAG:7252 QPO:7292
VQG => EAB:8047 ERO:1115 HGA:9289 ARA:1927
BJQ => DML:8055 BWE:9109 QDL:4026
UJB => VLB:3580
BEI => EUO:4452 UZZ:8164 AEU:8748
RZV => MIA:2520
HJE => OOB:8585 JDG:4669
HGO => DIJ:3244 RAH:6281 VUD:6201
LGH => PQV:8946 VEM:7704
UZZ => VAP:2936 BEI:8164 EGG:7428
XZJ => GIB:8424
YIJ => GIZ:2680 UME:1077 GIB:8590 HHB:9360 HJT:2769
ZXV => HMT:1989 ETY:5394 UPR:3631 NRO:7143
ERG => WWX:6106 MIA:6188
LUG => LUM:3073
UOB => WAQ:5399
XIZ => UTQ:8025
HMY => ZJB:6871 TXH:7289 UYU:4725 OZM:9991
MUA => QBW:1298 ZJB:6362 ADR:6958
IUW => QRG:3082 AYH:3624
RJD => NTV:7856
EUW => BTV:4622 AID:8515
PAN => DYN:8719
RAH => HGO:6281 EEO:2787 JJU:3358
HYU => XWZ:2639 WMY:7269
VGU => UGP:4822 QBI:8938 UTQ:8828
OOL => QYW:5078 GRD:8899 MDM:6256
QBH => PEY:8048 LEI:1603 UXR:1027
YWB => DVB:4156
UME => YIJ:1077 WWX:1452 JUZ:5950
TZM => ZDT:2920 WWV:5020 XIR:5756 DRE:2781
NVQ => GLW:2402 DQY:2563
ILQ => ZVA:8416 WUZ:1939
DHI => DHN:2716 BLW:7213
GWE => OYG:7214
DXH => LQY:3588 BBM:9067 WQM:8685
YLJ => HAM:2482
NPD => VAP:4181 IQT:1446 ETN:3144 UGP:9125
LIV => OEL:9764 TPB:9946 LTB:5528
NBH => MVG:2882 RYL:3262
ILW => INN:2349 AVD:5853
DIR => MNR:9794 TUX:8893
LQY => DXH:3588 GVN:4572
YPP => RIR:4277 ALW:2307
AYH => IUW:3624 ZZZ:4745
UJA => JUZ:4261 WRP:1958 AQG:8949
PWE => GOT:6247 VLG:2841
IAW => ULN:1862 MYV:1706
LJQ => OPB:1147
PEY => QBH:8048 LGN:7531
WOL => HNB:1818
TUV => GIB:3041 RLP:2476
BJX => HMT:8327 OYG:9985
IYO => BLW:1889
NRD => QYT:9456 DOJ:7051
NHA => ARB:3628 XDH:1856
MND => WMY:9723
NAV => XIR:1019 WUM:1544 PJL:6440 RDZ:3033
HWH => EDL:6994
QYT => NRD:9456 YPZ:6981 JQQ:8330
WMJ => MUY:1241 XWM:1020
YXV => DQY:6325 PNR:6451
TMP => ZAX:5271
GYB => DQY:4564 LUV:5283 BJY:6961
NTV => RJD:7856 NVG:7411 ANA:6318
HRV => EXY:2817 AQB:2404 AIW:7198
AYL => WQR:2362 ZZP:3086
LQO => LHY:6943
XHW => XOM:8942
WYY => HOO:4143
EWP => JMG:8762 BYO:7434
BBM => DXH:9067
JJU => RAH:3358 VPG:8180 IVQ:9478
EUO => BEI:4452 HAY:6273
XPW => ZQW:2909
BLA => GWB:2953
GOT => PWE:6247 UYU:8579
VTH => XOB:4590 NVN:3459 NPE:7139
OJP => VLB:9345
ZDT => TZM:2920 JZT:1613
LIN => ZIT:9546 HGJ:2278 PEM:4907 ATA:7746
ZJM => MGE:3751 LWE:6961
NAA => UAM:7983
BJO => EME:7665 DDN:6381
IQT => NPD:1446 APG:5449
LRM => AQG:9605 DJT:6813
XEA => PIP:9366
DML => BJQ:8055 EQM:1493
EGG => UZZ:7428 WMT:5252 WOI:8298
TQT => ATU:7973
BVY => WUM:9222 DPD:4534
APG => IQT:5449
ZAW => HZZ:3418
MEM => YHH:5007
UHY => YDR:7533 WIJ:6905 MNR:1112 GWO:8486
WWX => ERG:6106 UME:1452
YYB => QDB:8790
GIB => XZJ:8424 TUV:3041 YIJ:8590 BBP:9177
XGP => QJD:4144 RLP:2743 AQQ:9838 UUH:9308
WWT => IBV:7329
AHY => DQY:3091
NVN => VTH:3459
BEL => WIJ:3659
HAM => YLJ:2482 XGY:4778
WUZ => ILQ:1939 TRB:4513 HEN:9673
TUX => DIR:8893
YXA => HAY:1569 OYT:7206
LWW => DGO:3070
MGT => AVD:7194 UEN:9434
DAY => ZDR:6099 PLR:8747
VXV => ZYU:4131
ETD => BOW:5586 QYI:3852
ENT => UOL:4049
POB => YLE:1891
EAN => BPZ:6491
ZIT => LIN:9546 XWM:5990
XDI => DER:1573 NVG:5616
LAX => GWO:9138 EIO:8693
PRW => DYN:5636
UVI => JZW:5601 XEY:8234 VGB:4288
BDL => LUW:5065
BWE => BJQ:9109
QTY => DMV:5863
GBU => XHA:1466 HVV:7225
XHA => GBU:1466 WPZ:8034 ION:6741 ZDR:7171 XMD:1759
HMJ => LGN:5883 NHW:5540
XWM => ZIT:5990 WMJ:1020 MGU:7213
RLP => XGP:2743 TUV:2476
JQQ => QYT:8330
TRB => WUZ:4513
ZJB => HMY:6871 MUA:6362 XNY:7738 YII:9428
BOW => ETD:5586 HAY:7393 XHI:1870
ADD => VEA:6440
VMN => MGO:8915 UMT:7601
LHY => LQO:6943 ETY:9545 LWA:6363
WIJ => BEL:3659 UHY:6905 UZI:7130 WAQ:5429
YPZ => QYT:6981 WQM:3342 QRG:5034 BUZ:9207
JZT => ZDT:1613 QUV:5236
QYI => ETD:3852
VPG => JJU:8180
LMQ => ZZM:9128 INL:5833
ARX => QTJ:4100 IVW:3343
QBW => MUA:1298 AQG:1609
XEY => UVI:8234
WQZ => RDZ:3926 VOT:5887
LWE => ZJM:6961 BDP:4586
RIR => YPP:4277 JGP:8180
PQV => LGH:8946 JMG:9449 XOB:5265 MWD:6040
UQZ => EPX:1236
RTW => YWX:4943 OOH:7097 UAM:3428
RPI => ARB:3564 BPY:1283
NGR => QDL:3595
MGE => ZJM:3751 EAR:4668
DOJ => NRD:7051
TPB => LIV:9946 OPB:8077
PYL => RZY:2908
GWB => BLA:2953 VOA:8432
QIU => OYT:4605
NYW => MXR:1602
YLW => BTV:3486 XGY:6855
MIN => QZM:1021 MWR:4557 PUP:1066
WQM => YPZ:3342 DXH:8685 DPA:2173
THY => HVV:7981 ZAX:9890 HQY:4898
LMP => DPA:5003
VOA => TYT:5898 GWB:8432 RWA:8106 QJD:1271
QDB => YYB:8790 MOW:2120 GLW:2598
QMX => AIW:8075 GVN:4253
ATU => TQT:7973 WYL:8950
JRH => ALW:3259 RNW:7465 OTP:8748
PIH => PLR:6328 GOM:7045
ION => XHA:6741
MNR => DIR:9794 HZZ:8183 WAO:1288 UHY:1112
QUV => JZT:4236 WMT:9668 APB:3937
ZZM => LMQ:9128 RNB:7241 OMX:9520
BPY => RPI:1283 UIE:9708
ETY => LHY:9545 ZXV:5394 VJL:5507
ARB => NHA:3628 RPI:3564 BPZ:1867
LZU => XPD:1981 IHD:1182
LOP => WVP:7296 MUY:9613
RWA => VOA:8106 MIA:4943 XRM:4412
EIO => LAX:8693
ANA => NTV:6318 YWX:3488
QVR => RDZ:1598 RNB:1599 WAW:4168 DEV:8180
PXD => GBY:4592 VPO:9677 ZBP:1618
XNY => ZJB:7738
WAO => MNR:1288
WUM => BVY:9222 NAV:1544
PDP => ZHP:3175
AQB => AVD:4791 HRV:2404
WOZ => TXH:7154
DXP => ZHT:2353 MTY:5690
XDH => NHA:1856
OYT => QIU:4605 YXA:7206 HAY:6361
WYL => ATU:8950 DMV:3040 MGO:8021
QRG => IUW:3082 YPZ:5034 HTY:7002
INN => ILW:2349 QVN:7723
QNR => HEN:1777 XUY:6935
MWR => MIN:4557 NZJ:2258
VQM => PJL:3332 XRW:2751
APJ => VHW:6361
XGY => HAM:4778 YLW:6855
TXH => WOZ:7154 HMY:7289
IVQ => JJU:9478
XRW => VQM:2751
ZAX => TMP:5271 THY:9890
PIP => XEA:9366 HQY:8923
NZJ => MWR:2258 EXY:2569
VID => WLV:1933 PLG:5484
GIT => PMJ:8381
WRP => UJA:1958 JLB:1566 JWE:3289
XWZ => HYU:2639 ZHT:5553
NWY => HOO:2236
OEL => LIV:9764 IVW:5555
WGD => UOL:9246
DER => XDI:1573 NXN:8162 ZVW:1690
LXD => RYL:1963
GRD => OOL:8899 AMM:2113
BLW => IYO:1889 DHI:7213
RNW => JRH:7465
DWM => HOO:9886 NHW:7014 MOW:7460 VLE:3704
RWH => GVN:7425 YDR:8638
ZIE => IVW:8592 UOL:3634 BIN:9697
LGN => PEY:7531 HMJ:5883 RAB:6773
UPR => ZXV:3631
PNR => YXV:6451 MJE:9124 ZTE:9650
ALW => JRH:3259 YPP:2307
APX => YLE:3764 BIJ:1132
ZZP => AYL:3086 MVG:1535
MHX => LUV:5034
VTB => BIN:2155
ZYU => VXV:4131 WRQ:3107 QZM:2251
ETN => NPD:3144
RNB => ZZM:7241 QVR:1599
OTP => JRH:8748
EAB => VQG:8047 WPZ:2032
HZZ => ZAW:3418 MNR:8183 BJY:1034 MYQ:8942
NTN => LWA:6806
NPE => VTH:7139
EDL => HWH:6994 QVN:4179
RAB => LGN:6773 GBY:4998 WGX:2193
BTV => EUW:4622 YLW:3486 WQR:8301
HHB => YIJ:9360
JLB => WRP:1566
NWM => VLG:6567
UYU => GOT:8579 HMY:4725 JMG:4663
MAI => PMJ:1593
DDN => BJO:6381
HVE => JUZ:5936 QBI:6866
IVP => MVG:4563
WVJ => BDP:9297
LEI => QBH:1603
LLG => RRX:8935 LHZ:8494
ADR => MUA:6958 HNB:3138
YLE => POB:1891 APX:3764 VOT:7581
HEN => QNR:1777 WUZ:9673
QYW => OOL:5078 RVN:8662
LZN => GHD:2037
OZM => HMY:9991 GZD:8254 ZVA:2620
OPB => LJQ:1147 TPB:8077
ZTI => XHT:1263 RPN:1908
UZI => WIJ:7130 POQ:7571 TJR:4547
JGP => RIR:8180 NRO:7287
LVQ => DEV:8863
LUM => LUG:3073 BPZ:3327
AQQ => XGP:9838
GZD => OZM:8254 LOJ:1271 PLA:4417
OOH => RTW:7097
MNQ => VEM:6020 JUZ:2319
VEA => ADD:6440 ERO:5797
EME => BJO:7665 UIE:8110 PZJ:7844
UUH => XGP:9308
HGJ => LIN:2278 EQM:4751 IXN:1902
QZL => VJE:4116
VHW => APJ:6361 NWU:2186
PLR => PIH:6328 DAY:8747
LYI => HAY:3171 DEW:2223
ZQW => XPW:2909 QTJ:8036
WTJ => XHT:7774
WAW => QVR:4168
EQM => DML:1493 HGJ:4751 UDA:5197
RNP => WDZ:8872
UDA => EQM:5197 DGO:8501
RPN => ZTI:1908 YWN:5899
UMT => VMN:7601
EXY => HRV:2817 NZJ:2569 WQR:5462 VLB:7455 OPW:3177
PAV => DVB:4199
PUP => MIN:1066 MXR:5479
JMG => EWP:8762 PQV:9449 UYU:4663
AID => EUW:8515 WHQ:1151
LUP => DIJ:2112
JYP => EOR:5450
XOM => XHW:8942 YHH:6729
EHN => VJE:1554
LOJ => GZD:1271
TJA => UXR:6926
MJE => PNR:9124
VJL => ETY:5507
RXU => NWU:4210
XHT => ZTI:1263 WTJ:7774 RRX:1861
MHE => UGP:9012 DVB:6851 WPZ:4212
ZZZ => AYH:4745
QBI => VGU:8938 HVE:6866 JJV:5177
MIA => RZV:2520 RWA:3943 ERG:6188
MVG => NBH:2882 IVP:4563 ZZP:1535 EOR:2669
QVN => INN:7723 EDL:4179 NXN:9797
MDM => OOL:6256
OYG => GWE:7214 BJX:9985 PLG:5645
IXN => HGJ:1902
WQR => AYL:2362 BTV:8301 EXY:5462
XHI => BOW:1870 EEO:7640
QDL => NGR:3595 BJQ:4026 WIM:5645
ULN => IAW:1862 YHH:1814
YGZ => OAT:4721 TOQ:2993
HTY => QRG:7002
XOB => VTH:4590 PQV:5265
PJL => VQM:3332 NAV:6440 JDM:8856
DQY => YXV:6325 GYB:4564 AHY:3091 NVQ:2563
AIW => QMX:8075 HRV:7198 LUW:2032
HJT => YIJ:2769
IVW => OEL:5555 ZIE:8592 ARX:3343 DYN:8837
UTQ => XIZ:8025 VGU:8828
NXN => DER:8162 QVN:9797 UOL:2353
XRM => RWA:4412 OMI:8785
WMT => QUV:9668 EGG:8252 VPO:9806
DJE => YDR:8630
WAQ => UOB:5399 WIJ:5429
VUD => HGO:6201
HQY => PIP:8923 THY:4898 RVN:3277
MYV => IAW:1706
XUY => QNR:6935
UGP => VGU:4822 MHE:9012 NPD:9125
ZDR => DAY:6099 XHA:7171 PRQ:2788
VGB => UVI:4288
GIZ => YIJ:2680 WGX:1680
DJT => LRM:6813
VLB => UJB:3580 OJP:9345 EXY:7455
GHX => UXR:6197
VJE => QZL:4116 EHN:1554 LLB:8955 IYB:2500
PRQ => ZDR:2788
NRO => JGP:7287 ZXV:7143
AEU => BEI:8748 IDL:2754
DPD => BVY:4534 TIW:5456
WIM => QDL:5645
TIW => DPD:5456 GYT:2020
UEN => MGT:9434
TOQ => YGZ:2993
DGO => LWW:3070 UDA:8501
VLG => NWM:6567 PWE:2841 NMB:3278
OLG => HAY:9509
POQ => UZI:7571
WWV => TZM:5020
YWX => RTW:4943 ANA:3488
IBV => WWT:7329 JWE:7128
RVN => QYW:8662 HQY:3277
DEV => EAR:2694 LVQ:8863 QVR:8180
WVP => LOP:7296 UOI:4616
MXR => NYW:1602 PUP:5479
EOR => JYP:5450 MVG:2669
JDG => HJE:4669
RZY => PYL:2908 LTB:8927
IDL => AEU:2754
RDU => TTV:2867
NHW => DWM:7014 HMJ:5540 DHN:1196
DVB => YWB:4156 PAV:4199 MHE:6851
QTJ => ARX:4100 ZQW:8036 PEM:5449
DHN => DHI:2716 NHW:1196
TBQ => MGO:1487
LUV => MHX:5034 GYB:5283
WRQ => ZYU:3107 GHD:5413 APH:2711
NVG => NTV:7411 XDI:5616
OMI => XRM:8785
YHH => MEM:5007 XOM:6729 ULN:1814 RPO:5158 UOI:2291
DMV => QTY:5863 WYL:3040 LUW:9256
MUY => WMJ:1241 LOP:9613
APH => WRQ:2711
RYL => LXD:1963 NBH:3262
NMB => LLB:3770 VLG:3278 XPD:2096
GHD => LZN:2037 WRQ:5413 ERO:7571 OOB:4061
MGO => VMN:8915 TBQ:1487 WYL:8021
HQI => VLE:3118
MTY => DXP:5690
UOI => WVP:4616 YHH:2291
XAG => XMX:7252
XMD => XHA:1759 EPX:9379
ERO => VEA:5797 GHD:7571 VQG:1115
PLG => OYG:5645 VID:5484
APB => QUV:3937
UXR => TJA:6926 GHX:6197 QBH:1027
WMY => ZHP:9855 MND:9723 HYU:7269
IYB => VJE:2500
YII => ZJB:9428
HNB => WOL:1818 ADR:3138
ZVW => DER:1690
ZBP => PXD:1618
BJY => HZZ:1034 GYB:6961
INL => LMQ:5833 JZW:6509
BIJ => APX:1132 RRX:9799
WOI => EGG:8298
WHQ => AID:1151
YWN => RPN:5899
NWU => VHW:2186 RXU:4210 RDZ:9172
PTE => WDZ:3352
PMJ => GIT:8381 MAI:1593 JWE:3003
AVD => MGT:7194 AQB:4791 ILW:5853
OAT => YGZ:4721 MOW:2471
BBP => GIB:9177
AQG => LRM:9605 QBW:1609 UJA:8949
BUZ => YPZ:9207
JUZ => UJA:4261 UME:5950 HVE:5936 MNQ:2319
PLA => GZD:4417
EEO => RAH:2787 XHI:7640
GYT => TIW:2020
BYO => EWP:7434
OOB => HJE:8585 GHD:4061
AMM => GRD:2113 BPZ:5752 IUD:4942
XIR => NAV:1019 TZM:5756
ZVA => ILQ:8416 OZM:2620 WDZ:9288
MWD => PQV:6040
GVN => LQY:4572 RWH:7425 QMX:4253
RDZ => WQZ:3926 QVR:1598 NWU:9172 NAV:3033
DEW => LYI:2223
TJR => UZI:4547
WGX => GIZ:1680 RAB:3193
RPO => YHH:5158 UAM:2081
PEM => QTJ:5449 LIN:4907
LTB => RZY:8927 LIV:5528
MOW => QDB:2120 OAT:2471 DWM:7460
ZHT => DXP:2353 XWZ:5553 PGY:6900
JDM => PJL:8856
DYN => PAN:8719 PRW:5636 IVW:8837
LHZ => LLG:8494
WDZ => RNP:8872 PTE:3352 ZVA:9288
TTD => GWO:7496
UIE => EME:8110 BPY:9708
GWO => YNQ:9282 LAX:9138 TTD:7496 UHY:8486 WLV:2097
JJV => XMX:6851 QBI:5177
UDG => PZJ:3117
ATA => LIN:7746
RRX => LLG:8935 XHT:1861 BIJ:9799
VPO => PXD:9677 WMT:9806
UMN => GOM:1822
GLW => NVQ:2402 QDB:2598
LWA => NTN:6806 LHY:6363
IHD => LZU:1182
HVV => THY:7981 GBU:7225
VEM => MNQ:6020 LGH:7704
VLE => HQI:3118 DWM:3704
QPO => XMX:7292
GOM => UMN:1822 PIH:7045
XPD => TTV:4814 LZU:1981 NMB:2096
WLV => VID:1933 GWO:2097
DPA => LMP:5003 WQM:2173
WPZ => XHA:8034 EAB:2032 MHE:4212
OMX => ZZM:9520 PGY:3627
YDR => GLU:9672 UHY:7533 DJE:8630 RWH:8638
UAM => NAA:7983 RPO:2081 RTW:3428
EPX => UQZ:1236 XMD:9379
HAY => XMP:9573 EUO:6273 YXA:1569 BOW:7393 LYI:3171 OLG:9509 OYT:6361
ZTE => PNR:9650
JWE => IBV:7128 PMJ:3003 WRP:3289
VOT => YLE:7581 WQZ:5887
JZW => UVI:5601 INL:6509
UOL => ENT:4049 WGD:9246 NXN:2353 ZIE:3634
OPW => EXY:3177
LUW => BDL:5065 DMV:9256 AIW:2032
HGA => VQG:9289
DRE => TZM:2781
ARA => VQG:1927
PGY => ZHT:6900 OMX:3627 EUQ:9383
QZM => MIN:1021 ZYU:2251
BPZ => EAN:6491 ARB:1867 LUM:3327 AMM:5752
UQJ => GBY:4089
PZJ => UDG:3117 EME:7844
MGU => XWM:7213
MYQ => HZZ:8942
IUD => TYC:2889 AMM:4942
BDP => WVJ:9297 LWE:4586
BIN => VTB:2155 ZIE:9697
EUQ => PGY:9383""",
    """TYC => BBB:38 CCC:45
BBB => TYC:38 DDD:60 EAR:70
CCC => TYC:45 DDD:35
DDD => BBB:60 CCC:35 EAR:15
EAR => BBB:70 DDD:15"""
)