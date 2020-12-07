root@70b4b89ca6ca:/# mongo -u admin -p '1o2XZgY1fPbJ'
MongoDB shell version v3.6.15
connecting to: mongodb://127.0.0.1:27017/?gssapiServiceName=mongodb
Implicit session: session { "id" : UUID("17584180-ebfc-48b8-bff6-95c33f6f927f") }
MongoDB server version: 3.6.15
Server has startup warnings: 
2019-11-06T08:14:21.160+0000 I STORAGE  [initandlisten] 
2019-11-06T08:14:21.161+0000 I STORAGE  [initandlisten] ** WARNING: Using the XFS filesystem is strongly recommended with the WiredTiger storage engine
2019-11-06T08:14:21.161+0000 I STORAGE  [initandlisten] **          See http://dochub.mongodb.org/core/prodnotes-filesystem
> use test
switched to db test
> db.createUser(
...    {
...      user: "test",
...      pwd: "test",
...      roles: [ "readWrite", "dbAdmin" ]
...    }
... )
Successfully added user: { "user" : "test", "roles" : [ "readWrite", "dbAdmin" ] }
> exit
bye
root@70b4b89ca6ca:/# 
