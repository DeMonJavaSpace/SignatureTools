# Apk签名工具

Android7.0引入了全新的APK Signature Scheme v2签名方式，使用传统的JDK jarsigner命令行签名只能进行v1签名，如果要进行v2签名就要使用SDK提供的apksigner工具。  

apksigner.jar在版本>25的SDK\build-tools\中。  
 
基于此，使用JavaFx编写了安卓Apk签名工具，方便快速进行v1&v2签名。

 1. 无需记忆复杂的命令行，点击几下即可方便快速的签名。
 2. 使用xml格式配置签名密钥，可以配置多个签名密钥。
 3. 开放源码，无需担心恶意工具入侵你的Apk。

![xx](https://github.com/iDeMonnnnnn/SignatureTools/blob/master/20201102185505.jpg?raw=true)  

### 使用说明
#### 运行环境
1. Windows系统
2. JDK1.8环境变量

#### 1.下载源码编译运行

1. clone/下载代码到你的电脑，导入到IDEA中运行，编译完成后，点击Run Main运行。
 
2. 程序初始化后会先读取默认的签名配置文件（代码根目录\config.xml）
 
   - 点击“编辑密钥配置”编辑你的密钥配置。
 
   - 如果点击“编辑密钥配置”没反应，应该是没有配置xml的默认打开程序，可以找到"config.xml"用记事本打开编辑。
 
   - 编辑完，点保存关闭后。点击“编辑后，刷新密钥配置”，重新读取配置。
   
   - 可以参考config.xml格式，复制编辑保存成新的签名配置，签名前选择新密钥配置即可。
   
3. 选择一个未签名的apk签名，配置渠道信息，然后打开签名后Apk的位置，验证签名状态。
  
4. 修改代码，适应你的需求。
 
#### 2.[下载压缩包解压运行](https://github.com/iDeMonnnnnn/SignatureTools/releases/tag/v1.2)

1. 下载附件[ApkSignTools.7z](https://github.com/DeMonJavaSpace/SignatureTools/releases/download/v1.2/ApkSignTools.7z)
   
2. 解压后，双击"ApkSignTools.exe"运行签名程序。

3. 程序初始化后会先读取默认的签名配置文件（程序安装路径\app\config.xml）

   - 点击“编辑密钥配置”编辑你的密钥配置。
 
   - 如果点击“编辑密钥配置”没反应，应该是没有配置xml的默认打开程序，可以找到"config.xml"用记事本打开编辑。
 
   - 编辑完，点保存关闭后。点击“编辑后，刷新密钥配置”，重新读取配置。
   
   - 可以参考config.xml格式，复制编辑保存成新未新的签名配置，签名前选择新密钥配置即可。

4. 选择一个未签名的apk签名，配置渠道信息，然后打开签名后Apk的位置，验证签名状态。

### Apk渠道相关

基于[美团渠道包解决方案](https://tech.meituan.com/2014/06/13/mt-apk-packaging.html)  新增渠道写入功能。

需要注意的点是，由于写入多渠道空白文件会破坏App结构，在v1签名下没有影响，但是会导致v2签名成功后校验失败无法安装。  
为了解决这个问题，需要在写入渠道后，v2签名前，使用```zipalign.exe```先将apk对齐。  

#### zipalign对齐

由于写入多渠道空白文件会破坏App结构，所以要先使用zipalign对齐。

```
zipalign -v 4 " + 需要签名的apk +" "+ 对齐后的apk
```

#### 注意

```zipalign.exe```位于AndroidSDK的```build-tools```目录下，不同的系统平台会有差异。

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

### 问题&bug

请[issues](https://github.com/DeMonJavaSpace/SignatureTools/issues)

### 更多

关于作者更多JavaFx程序请访问: <https://github.com/DeMonJavaSpace>

### License

```
  Copyright (C) DeMon, SignatureTools Open Source Project

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
