jarsigner -verbose -keystore DeMon.jks -signedjar v1.apk  app.apk  key -sigfile CERT


jarsigner -verbose -keystore DeMon.jks -storepass 123456 -keypass 123456 -sigfile CERT -signedjar v1.apk  app.apk  key


  


java -jar apksigner.jar sign  --ks DeMon.jks  --ks-key-alias key  --ks-pass pass:123456  --key-pass pass:123456  --out v12.apk  app.apk    



//需要输入密码
java -jar apksigner.jar sign  --ks DeMon.jks  --out v12.apk  app.apk   


//验证是否v1 v2
java -jar apksigner.jar verify -v my.apk