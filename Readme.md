jarsigner -verbose -keystore g.keystore -signedjar v1.apk  app.apk  a -sigfile CERT


jarsigner -verbose -keystore g.keystore -storepass 123456 -keypass 123456 -sigfile CERT -signedjar v1.apk  app.apk  a


  


java -jar apksigner.jar sign  --ks g.keystore  --ks-key-alias a  --ks-pass pass:123456  --key-pass pass:123456  --out v12.apk  app.apk    



//需要输入密码
java -jar apksigner.jar sign  --ks g.keystore  --out v12.apk  app.apk   


//验证是否v1 v2
java -jar apksigner.jar verify -v my.apk