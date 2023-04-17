# 使用说明

1. 需要在Manage Jenkins -> Configure Global Security -> 去掉勾选 "Enable script security for Job DSL scripts" 然后保存



创建一个pipeline的job，然后复制create_job_pipeline.groovy的内容到pipeline的script中，并且勾选 沙盒模式，然后保存后执行就可以了。