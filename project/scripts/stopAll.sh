hdfs --daemon stop namenode
hdfs --daemon stop datanode
yarn --daemon stop resourcemanager
yarn --daemon stop nodemanager
yarn --daemon stop proxyserver
mapred --daemon stop historyserver