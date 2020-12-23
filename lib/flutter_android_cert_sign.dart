
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterAndroidCertSign {
  static const MethodChannel _channel =
      const MethodChannel('flutter_android_cert_sign');

  static Future<String> get certSign async {
    final String certSign = await _channel.invokeMethod('getCertSign');
    return certSign;
  }
}
