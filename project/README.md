# Para iniciar hadoop
```
sudo service ssh start
bin/hdfs namenode -format //No es seguro que sea necesario ejecutar
sbin/start-dfs.sh
sbin/start-yarn.sh
```

# Para desactivar safemode
```hdfs dfsadmin -safemode leave```

# Para cargar los datos en hadoop
```hadoop fs -put viajes_stm_*.csv /user/data```

# Para ejecutar la aplicación
```hadoop jar target/scala-2.13/hello-world_2.13-1.0.jar ../data/viajes_stm*.csv /user/out```
s
# Para verificar el conteo por día
```hdfs dfs -cat /user/out.txt/part-r-00000 | awk '{s+=$2} END {printf "%.0f\n", s}'```
