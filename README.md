# spdb-maven-scanner-plugin
--

该项目是一个maven build 插件，设计为在maven 项目集成到jenkins后，执行mvn install 打包前执行该插件。

该项目提供一下功能:

1. 扫描项目src/main/java。
2. 扫描项目src/test/java。
3. 扫描项目src/main/resouces。
4. 读取配置文件,定义扫描行为。
5. 打印扫描日志，扫描到敏感内容，编译失败并提供失败信息。

# Quick Started