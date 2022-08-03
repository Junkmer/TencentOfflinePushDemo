# TencentOfflinePushDemo

### _本文主要介绍怎么快速让IM接入各个厂商离线推送，主要分三步_：

## 第一步：配置对应厂商module下的 GenerateTestUserSig.java 文件下的IM SDKAPPID 和 SECRETKEY，不知道这两个参数是啥的，[点这里!!!](https://cloud.tencent.com/document/product/269/36838)

## 第二步：填写 客户端 及 IM控制台 推送配置参数信息

### 2.1：注册应用到厂商推送平台
离线推送功能依赖厂商原始通道，您需要将自己的应用注册到各个厂商的推送平台，得到 AppID 和 AppKey 等参数。目前国内支持的手机厂商有：[小米](https://dev.mi.com/console/doc/detail?pId=68)、[华为](https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/service-introduction-0000001050040060)、[OPPO](https://open.oppomobile.com/wiki/doc#id=10195)、[VIVO](https://dev.vivo.com.cn/documentCenter/doc/281)、[魅族](http://open-wiki.flyme.cn/doc-wiki/index#id?129)，海外支持 [Google FCM](https://console.firebase.google.com/u/0/?hl=zh-cn)。

### 2.2：IM 控制台配置
登录腾讯云 [即时通信 IM 控制台](https://console.qcloud.com/avc) ，添加各个厂商推送证书，并将您在上一步中获取的各厂商的 AppId、AppKey、AppSecret 等参数配置给 IM 控制台的推送证书。

- ***----小米----***

![控制台配置](https://user-images.githubusercontent.com/30644065/182319740-5921154c-a056-4dae-94a0-654e6c0c2424.png)
![客户端配置](https://user-images.githubusercontent.com/30644065/182319278-0f453def-3d60-4903-87d0-6239dcfcf6c4.png)

- ***----华为----***

![控制台配置](https://user-images.githubusercontent.com/30644065/182317433-c7e85feb-b7f2-4ff2-a2bc-bc7ae8c3c5f2.png)
![客户端配置](https://user-images.githubusercontent.com/30644065/182318759-5aa16f4e-02bd-43b9-91f1-4e9e8241c781.png)

- ***----Oppo----***

![控制台配置](https://user-images.githubusercontent.com/30644065/182317579-b8d147b3-31e9-45e0-9f73-bfa769e55968.png)
![客户端配置](https://user-images.githubusercontent.com/30644065/182318939-3054f37a-c308-4b3a-9bdf-743fc652fbbe.png)
 

  
**_注：
对于小米厂商，如果在厂商开发者官网配置了 ChannelID，需要在 [即时通信 IM 控制台](https://console.qcloud.com/avc)配置同样的 ChannelID,否则可能推送不成功_**。




## 第三步：修改 module下 build.gradle 中的 applicationId 改成自己的包名，最后即可测试推送功能
![](https://user-images.githubusercontent.com/30644065/182315571-d038b13e-b283-47ec-8deb-e75b3bd394ff.png)
