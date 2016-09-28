## Getting Started

You must modify the configuration file of the view server. (conf/server_view.conf)
```
adapter_class_path = ${ADAPTER_PROJECT_PATH}/dist/jennifer-view-adapter-snmp-1.0.0.jar
adapter_config_path = ${ADAPTER_PROJECT_PATH}/dist/jennifer-view-adapter-snmp-1.0.0.properties
adapter_event_class_name = event.SNMPAdapter
```

After modifying the configuration file, you must restart the view server.
