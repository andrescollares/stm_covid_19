# Data Processing

## Folders

## extra_data:

    estadisticasUY_porDepto.csv: contiene los datos referentes al COVID por departamento y día.

## logs_cluster: 

>contiene los logs de las diferentes ejecuciones realizadas en el cluster.

    log4Algoritmos.out - Dicho archivo contiene los logs de realizar la ejecución de los 4 algoritmos utilizando solamente un reducer.

    test7GB.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline variando el número de reducers utilizado. Tomando los valores de 1,2,3,7,15,25,35,47,60,69 y 87.  Dicha prueba fue realizada sobre un conjunto de datos con tamaño 7GB.

    test73GB.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline variando el número de reducers utilizado. Tomando los valores de 1,2,3,7,15,25,35,47,60,69 y 87.  Dicha prueba fue realizada sobre un conjunto de datos con tamaño 73GB.

    testOver0.0.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline utilizando exactamente 3 reducers e implementando overlapping. En dicho archivo se puede observar que las tareas reducers inician cuando el mapping va realizando un 12% de sus tareas.

    testOver0.2.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline utilizando exactamente 3 reducers e implementando overlapping. En dicho archivo se puede observar que las tareas reducers inician cuando el mapping va realizando un 26% de sus tareas.

    testOver0.5.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline utilizando exactamente 3 reducers e implementando overlapping. En dicho archivo se puede observar que las tareas reducers inician cuando el mapping va realizando un 55% de sus tareas.

    testOver0.8.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline utilizando exactamente 3 reducers e implementando overlapping. En dicho archivo se puede observar que las tareas reducers inician cuando el mapping va realizando un 89% de sus tareas.

    testOver1.0.out - Contiene los logs de realizar la ejecución del algoritmo CityMovementBusline utilizando exactamente 3 reducers e implementando overlapping. En dicho archivo se puede observar que las tareas reducers inician cuando el mapping va realizando un 100% de sus tareas.

## results: 

>Contiene los resultados obtenidos en la ejecución de los algoritmos en el cluster. Los mismos fueron utilizados para realizar las  gráficas. Además contiene los valores utilizados para las gráficas de los tests.

# Ejecución

Para instalar todo lo necesario se debe ingresar en la carpeta data_processing y ejecutar los siguientes comandos:

    python3 -m venv '.venv'
    source .venv/bin/activate
    pip install -r requirements.txt

# Código

Todo el programa se encuentra en el Jupyter Notebook: notebook.ipynb