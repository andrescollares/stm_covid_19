#!/bin/bash
#SBATCH --job-name=hadoop-hpc-test1
#SBATCH --nodes=1
#SBATCH --cpus-per-task=40
#SBATCH --mem=128436
#SBATCH --time=03:00:00
#SBATCH --tmp=120G
#SBATCH --partition=besteffort
#SBATCH --qos=besteffort

source /etc/profile.d/modules.sh

cd ~/

# export HADOOP_OPTS="-Dfs.defaultFS=hdfs://$HOSTNAME:9900"

./startAll.sh
./startHDFS.sh

hadoop jar grupo_c_2.13-1.0_3.jar CityMovementBusline /data/ /outputs/$1
hdfs dfs -copyToLocal /outputs/$1/part-r-00000 ./out$1

./stopAll.sh
