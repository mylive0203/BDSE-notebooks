# Build MongoDB Server & Configuration
* 建議使用64-bit版本
* 小數點後偶數是Stable版，奇數是Dev版


## Port
27017 : Client與Server溝通用port
28017 : Web管理介面使用的port

## 下載 MongoDB
1. 在官方網站下載綠色免安裝的.ZIP檔
2. 解壓縮到C:\nosql目錄，並改名為mongodb
```cmd=
 C:
 cd \nosql
 md mongodb-data
 cd mongodb-data
 md db
 md log
```

## 遇到問題
缺兩個檔案的錯誤
* 若使用官方的.zip檔案，解壓縮後執行出現缺少libeay32、ssleay32
* 下載官方.msi的檔案去補這兩個檔案

缺目錄檔的錯誤
* 若未建立db目錄、log目錄，系統"不"會幫你建立，需手動建立
```cmd=
md db
md log
```
## Directory
### Configuration(old)
* /data/db : dbpath預設存放Data的目錄，可透過dbpath參數指定
> dbpath=C:/nosql/mongodb-data/db
* 存放Log的位置
> logpath=C:/nosql/mongodb-data/log/mongodb.log
* port需設1024以上，避開系統使用的port number
> port=27017
* rest預設是false，建議不要開，有資安問題
> rest=true

### Configuration(new)
在mongodb-data目錄下建立組態設定檔 mongodb.config
```YAML=
storage:
    dbpath: "C:/nosql/mongodb-data/db"
systemlog:
    destination: file
    path="C:/nosql/mongodb-data/log/mongodb.log"
    logAppend: false
net:
    bindIp: 127.0.0.1
    port: 27017
    http: 
        enabled: true
        RESTInterfaceEnabled: true
        JSONEnabled: true
``` 

* MongoDB Server啟動時若沒發現目錄尚未建立。轉換目錄位置需先建立 目錄檔 以及 權限設定
* 全世界70% mongoDB server 不會去設帳號密碼，但可以自行設定 


## Daemon
* 舊版
> 啟動MongoDB的指令
```cmd=
 C:
 cd C:/nosql/mongodb/bin
 mongod --dbpath C:/nosql/mongodb-data/db
```
* 新版 ( 使用YAML語法 )
> 啟動MongoDB的指令
```cmd=
 C:
 cd C:/nosql/mongodb/bin
 mongod --config C:\nosql\mongodb-data\mongodb.config
```

## 開始試玩

use cities
 db.publishers.insert{{publisherName: "O'Reilly"}}

## 關閉MongoDB Server
1. 直接打X(不要嚇到，MongoDB真的這樣玩)
2. CTRL+C
3. 另開一個console，輸入
* db.shutdownServer()
4. 若是Linux，可以輸入
* ps -aef | grep mongod #取得Process ID
* Kill -2 PID

## 可將 MongoDB 安裝成Windows Service
安裝 :
```cmd=
 mongod --config C:\nosql\mongodb-data\mongodb.config
 net start MongoDB
 net stop MongoDB
```
