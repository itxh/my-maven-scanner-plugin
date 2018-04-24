# spdb-maven-scanner-plugin


该项目是一个maven build 插件，设计为在maven 项目集成到jenkins后，执行mvn install 打包前执行该插件。


该项目提供的功能:

1. 扫描项目src/main/java。
2. 扫描项目src/test/java。
3. 扫描项目src/main/resouces。
4. 读取配置文件,定义扫描行为。
5. 打印扫描日志，扫描到敏感内容，编译失败并提供失败信息。
6. 将结果输出到控制台和磁盘文件。

# Quick Started

Step 1: 在需要使用该插件的项目的pom.xml文件中声明该插件：

```java
<plugin>
   <groupId>com.csii.pe</groupId>
   <artifactId>com.csii.pe.ci.scanner</artifactId>
   <version>1.0-SNAPSHOT</version>
   <executions>
      <execution>
         <id>scanner</id>
         <phase>process-sources</phase>
         <goals>
            <goal>scanner</goal>
         </goals>
      </execution>
   </executions>
</plugin> 
```
将spdb-maven-scanner-plugin插件的scanner绑定在了process-sources阶段，那么该项目mvn install 的时候会执行该插件。

Step 2: 配置插件配置文件
 
 参考本插件源吗中exmaple_config/config.yaml 这是主配置文件，在这里面填写其他配置文件的决定地址。
 
config.yaml

```java
##########################################################
#                    maven版本文件扫描
#    注意：
#         1.所有url及目录配置需以"/"或" \"结尾
#         2.所有key: value配置value前需有空格
#         3.该文件聚合其他配置文件，需填写决定地址
###########################################################
# 需要扫描的关键字目录
rule_file: /tmp/config/rule.txt
# 指定扫描特定后缀文件
suffix_match_file: /tmp/config/suffix_match_file.txt
# 过滤指定后缀文件
suffix_filter_file: /tmp/config/suffix_filter_file.txt
```

rule.txt

> 该文件配置需扫描的关键字,每一个一行

```java
password
zhangsan
lisi
```


suffix_filter_file.txt

> 该文件配置过滤的文件，每一个一行

```
.svn
.css
.jpg
.png
.gif
.setting
.project
.metadata
.plugins
.prefs
.ptlist
.MF
.swf
.sh
.dtd
.scc
.log
.lck
.zip
.php
.classes
.project
.jar
.dwt
.dnx
.xsl
```

suffix_match_file.txt

> 该文件配置指定扫描的文件,每一个一行，优先级比suffix_filter_file.txt 高，当该文件没内容时，过滤suffix_filter_file.txt 里面的文件，有内容时只扫描指定的文件

```
.java
```


Step 3. 使用

mvn install -Dscanner.config=/Users/lettuce/GitRepo/spdb-maven-scanner-plugin/exmaple_config/config.yaml

-Dscanner.config 指定为config.yaml 的决定地址


如有任何问题,请提交Issues。

