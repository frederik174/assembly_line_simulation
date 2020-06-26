<h1>Assembly Line Simulation</h1>

Dieses Modell dient der Simulation von Fahrzeugbewegugen innerhalb der Produktion des Transporters T6  (Baseline & Multivan) 
in der Volkswagenproduktion im Werk Hannover. Die Bewegung der einzelnen Fahrzeuge wird mit einer
Zeitraffung simuliert. Ein Takt dauert 20 Sekunden statt 2 Minuten. Nach dem ende des Taktes wird das 
Fahrzeug eine Position weiter nach vorne geschoben. In jedem dritten Takt eines Bandabschnittes befindet sich 
ein RFID-Gate, das die Identität des Fahrzeugs und somit auch dessen Position erfasst.Bei der Identifikation werden 
[EPCIS-Events](https://www.gs1-germany.de/gs1-standards/datenaustausch/epcis/) erzeugt, die über einen 
[Apache Kafka Producer](https://kafka.apache.org/) an das Kafka-Cluster in der Docker-Compose Entwicklungsumgebung 
gesendet werden. Es wird zwischen Objekt- und Masterdata-Events differenziert. Die Objekt-Events entstehen, wenn das 
Fahrzeug in einem Takt von einem RFID-Gate erfasst wird. Die Masterdata-Events werden von einem MES gesendet, sobald 
das Fahrzeug einen neuen Bandabschnitt betritt.



