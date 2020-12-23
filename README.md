# flutter_android_cert_sign

flutter获取安卓包的证书签名

- 只获取SHA1签名


## Getting Started

pubspec.yaml中引入依赖：

```
dependencies:
  flutter_android_cert_sign: <最新版本>
```


最新版本查看地址：

https://pub.dev/packages/flutter_android_cert_sign/install


使用插件：

```
  String androidCertSign = await FlutterAndroidCertSign.certSign;
```




