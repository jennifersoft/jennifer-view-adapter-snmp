## Important Notices

It is available from Jennifer Server version 5.2.3. If you want to use the old method, use the 'old' branch.


## Getting started

Execute the following in the Jennifer management screen.

 1. Extension & Notice > Adapter and Plugin
 2. Click the Add button.
 3. Select the 'EVENT' type.
 4. Enter the 'snmp' ID.
 5. Enter the adapter path directly or upload the file.
 6. Enter the class 'event.SNMPAdapter'.

## How to use Options

Adapter options are shown in the table below.

| Key           | Default Value |
| ------------- |:-------------:|
| pattern       | [%time] domain=%domainName(%domainId), instance=%instanceName(%instanceId), level=%eventLevel, name=%eventName, value=%value |
| date_format   | yyyy-MM-dd HH:mm:ss |
| trap_oid_fatal | 1.3.6.1.4.1.27767.1.1 |
| trap_oid_warning | 1.3.6.1.4.1.27767.1.1 |
| trap_oid_normal | 1.3.6.1.4.1.27767.1.1 |
| trap_target_community | public |
| trap_target_address | 127.0.0.1/162 |
