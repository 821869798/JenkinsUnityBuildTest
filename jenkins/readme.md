# 使用说明

1. 如果是windows，需要在高级系统设置 -> 系统变量 -> 新增： ```JAVA_TOOL_OPTIONS``` 值填入： ```-Dfile.encoding=utf-8``` 这样才能保证读取到的groovy的脚本中的中文不乱码。
或者用 ```java -Dfile.encoding=utf-8 -jar jenkins.war``` 来启动jenkins

2. 在Manage Jenkins -> Configure Global Security -> 去掉勾选 "Enable script security for Job DSL scripts" 然后保存


2. 创建一个pipeline的job，然后复制create_job_pipeline.groovy的内容到pipeline的script中，并且勾选 沙盒模式，然后保存后执行就可以了。