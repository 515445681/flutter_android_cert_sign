import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class FlutterAndroidCertSign {
  static const MethodChannel _channel =
  const MethodChannel('flutter_android_cert_sign');

  static Future<String> get certSign async {
    final String certSign = await _channel.invokeMethod('getCertSign');
    return Future.value(certSign);
  }

  static Future<Iterable<dynamic>> get libSoList async {
    final String libSoList = await _channel.invokeMethod('getLibSoList');
    Iterable<dynamic> libList = jsonDecode(libSoList);
    return Future.value(libList);
  }
}
