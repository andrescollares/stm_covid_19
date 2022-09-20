# export HADOOP_OPTS=-Dfs.defaultFS=hdfs://$HOSTNAME:9900

rm -rf /scratch/andres.collares/*

hdfs namenode -format

hdfs --daemon start namenode
hdfs --daemon start datanode
yarn --daemon start resourcemanager
yarn --daemon start nodemanager
yarn --daemon start proxyserver
mapred --daemon start historyserver

