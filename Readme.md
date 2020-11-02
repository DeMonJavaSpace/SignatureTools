# Apk签名工具

Android7.0引入了全新的APK Signature Scheme v2签名方式，使用传统的JDK jarsigner命令行签名只能进行v1签名，如果要进行v2签名就要使用SDK提供的apksigner工具。  

apksigner.jar在版本>25的SDK\build-tools\中。  
 
基于此，使用JavaFx编写了安卓Apk签名工具，方便快速进行v1&v2签名。

 1. 无需记忆复杂的命令行，点击几下即可方便快速的签名。
 2. 使用xml格式配置签名密钥，可以配置多个签名密钥。
 3. 开放源码，无需担心恶意工具入侵你的Apk。

![xx](https://github.com/iDeMonnnnnn/SignatureTools/blob/master/20201102185505.png?raw=true)  

### 使用说明

#### 下载源码编译运行

1. clone/下载代码到你的电脑，导入到IDEA中运行，编译完成后，点击Run Main运行。
 
2. 程序初始化后会先读取默认的签名配置文件（代码根目录\config.xml）
 
   - 点击“编辑密钥配置”编辑你的密钥配置。
 
   - 如果点击“编辑密钥配置”没反应，应该是没有配置xml的默认打开程序，可以找到"config.xml"用记事本打开编辑。
 
   - 编辑完，点保存关闭后。点击“编辑后，刷新密钥配置”，重新读取配置。
   
   - 可以参考config.xml格式，复制编辑保存成新的签名配置，签名前选择新密钥配置即可。
   
3. 选择一个未签名的apk签名，可以验证签名状态，然后打开签名后Apk的位置。
  
4. 修改代码，适应你的需求。
 
#### [下载zip运行](https://github.com/iDeMonnnnnn/SignatureTools/releases/tag/v1.0)

[点击下载](https://github.com/iDeMonnnnnn/SignatureTools/releases/download/v1.0/ApkSignTools.zip)

1. 解压后，双击"ApkSignTools.exe"运行签名程序。

2. 程序初始化后会先读取默认的签名配置文件（程序安装路径\app\config.xml）

   - 点击“编辑密钥配置”编辑你的密钥配置。
 
   - 如果点击“编辑密钥配置”没反应，应该是没有配置xml的默认打开程序，可以找到"config.xml"用记事本打开编辑。
 
   - 编辑完，点保存关闭后。点击“编辑后，刷新密钥配置”，重新读取配置。
   
   - 可以参考config.xml格式，复制编辑保存成新未新的签名配置，签名前选择新密钥配置即可。
   
3. 选择一个未签名的apk签名，可以验证签名状态，然后打开签名后Apk的位置。


### 签名相关命令

#### 旧版v1签名
```
jarsigner -verbose -keystore 你的密钥 -storepass 密钥密码 -keypass 别名密码 -sigfile CERT -signedjar 签名后的apk路径  待签名的apk  别名
```

#### 新版v1&v2签名
```
java -jar apksigner.jar sign  --ks 你的密钥  --ks-key-alias 别名  --ks-pass pass:密钥密码  --key-pass pass:别名密码  --out 签名后的apk路径  待签名的apk   
```

#### 验证Apk签名状态

``` 
java -jar apksigner.jar verify -v my.apk
```

#### 签名状态说明

``` 
Verified using v1 scheme (JAR signing): true
Verified using v2 scheme (APK Signature Scheme v2): true
```
第一行对应的结果，表示是否已使用v1签名。  
第二行对应的结果，表示是否已使用v2签名。

### MIT License

```
Copyright (c) 2020 DeMon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```