# Spanner CRUD APIs

# Create maven project using below command
```
mvn archetype:generate -DgroupId=com.infogain.gcp.poc -DartifactId=spanner-crud -Dversion=1.0.0 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

# Spanner DDL scripts
* pnr table
```
CREATE TABLE pnr (
    pnr_id STRING(MAX),
    lastUpdateTimestamp TIMESTAMP NOT NULL OPTIONS (allow_commit_timestamp=true),
    mobileNumber STRING(MAX),
    remark STRING(MAX),
) PRIMARY KEY (pnr_id);
```

* poller commit timestamp
```
CREATE TABLE POLLER_COMMIT_TIMESTAMPS (
    last_commit_timestamp TIMESTAMP
) PRIMARY KEY (last_commit_timestamp);
```

* test table
```
create table emp
(
	empno int64 not null,
	ename string(1024),
	job string(1024),
	salary int64 not null
) primary key(empno);
```