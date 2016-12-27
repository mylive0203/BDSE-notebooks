# HADOOP專題12/26

安裝vagrant
http://school.soft-arch.net/blog/69382/install-vagrant-on-win

簡報範例
https://hackmd.io/s/4JuJuJGhx

---
|姓名|IP|管理1|管理2|
|:---:|--|--|--|
|瑋|10|11|12|
|均|30|31|32|
|芝|70|71|72|
|品|90|91|92|
|宗|110|111|112|
|童|170|171|172|

例如:hostname : bdseXX.example.org : XX為管理IP
|角色|主機名稱(FQDN)|ip|功能|
|:--:|---------|--|--|
|NameNode|bdse111.example.org|111|負責儲存檔案的檔名設定檔(管理HDFS)|
|SecondaryNameNode|bdse92.example.org|92|Standby的NameNode
|ResourceManager|bdse31.example.org|31|分配工作給NodeManager(管理YARN)|
|SecondaryResourceManager|bdse12.example.org|12|Standby的ResoucrceManager|
|HistoryServer|bdse71.example.org|71|儲存工作日誌(啟動)|
|DataNode<br/>NodeManger|bdse11.example.org<br/>bdse72.example.org<br/>bdse91.example.org<br/>bdse171.example.org|11<br/>72<br/>91<br/>171|負責儲存檔案與計算的機器|
|Hive+Derby|bdse71.example.org<br/>bdse172.example.org|71<br/>172|
|Ganglia|bdse32.example.org|32|
|MRTG|bdse112.example.org|112|
|QJM<br/>(zookeeper)|bdse111.example.org <br/> bdse92.example.org <br/> bdse31.example.org|111<br/>92<br/>31||
|Spark|bdse32.example.org <br/>bdse72.example.org <br/>bdse91.example.org <br/>bdse112.example.org <br/>bdse172.example.org|32<br/>72<br/>91<br/>112<br/>172|

網址:
(NameNode)綠綠的 http://bdse111.example.org:50070
(SecondaryNameNode) http://bdse92.example.org:50070
(ResourceManager)小象象 http://bdse31.example.org:8088
(SecondaryResourceManager) http://bdse12.example.org:8088
(HistoryServer)http://bdse71.example.org:19888/
(HistoryServer自己內部)http://bdse71.example.org:10020/
(Hive web UI)
- http://bdse71.example.org:10002/
- http://bdse71.example.org:10002/



<br/>
留一手的配置

---
16.04版本後dsa與rsa都相同




### core-site.xml
	fs.defaultFS
	------:54310(Hadoop1.X)
		8020(Hadoop2.X預設)
		9000(Hadoop2.X流行)
        
看預設值
hadoop官網 -> 左邊Documentation ->Release2.7.3->Configuration


使用打包好的hadoop_node
git bash
`vagrant box list`
`vagrant box remove iiiedu/hadoop_node`
`vargrant box add --name iiiedu/hadoop_node 路徑/hadoop_node.cox`

c:\Users\Student
建目錄:hadoop_classroom -> 複製Vagrantfile
>Vagrantfile:15行box名稱(抓哪個box建立此2虛擬機),71行名稱和ip(config)

開啟虛擬機
`cd hadoop_classroom`
`vagrant up`
Test:1.在pc ping vm
     2.登入vm確認不用密碼 ex:vagrant ssh bdse91

hostfile: ip FQDN alias
host裡要填入每台的IP、主機名稱username
_____
### 1.開始
> Switch to root
> sudo -i
### 2.Add hadoop cluster hosts to hosts file
> nano /etc/hosts
    
```
127.0.0.1 localhost
192.168.33.11 bdse11.example.org bdse11
192.168.33.12 bdse12.example.org bdse12
192.168.33.31 bdse31.example.org bdse31
192.168.33.32 bdse32.example.org bdse32
192.168.33.71 bdse71.example.org bdse71
192.168.33.72 bdse72.example.org bdse72
192.168.33.91 bdse91.example.org bdse91
192.168.33.92 bdse92.example.org bdse92
192.168.33.111 bdse111.example.org bdse111
192.168.33.112 bdse112.example.org bdse112
192.168.33.171 bdse171.example.org bdse171
192.168.33.172 bdse172.example.org bdse172
```
_____
1. 因為vagrant的VM每次啟動時都會重建 /etc/hosts，所以寫在vagrantfile，將裡面的nna改為主機名稱bdseXX
2. 每台機器加入:bdseXX.vm.provision "shell", path: "scripts/sethosts.sh", run: "always"
```javascript=
  config.vm.define "bdse31" do |bdse31| 
    bdse31.vm.hostname = "bdse31" 
	bdse31.vm.network :public_network, ip: "192.168.33.31"
    bdse31.vm.provider "virtualbox" do |v| 
      v.name = "bdse31" 
      v.cpus = 1
      v.memory = 4096 
    end
    //這裡加入
	bdse31.vm.provision "shell", path: "scripts/sethosts.sh", run: "always"
  end 
  
  config.vm.define "bdse32" do |bdse32| 
    bdse32.vm.hostname = "bdse32" 
	bdse32.vm.network :public_network, ip: "192.168.33.32"
    bdse32.vm.provider "virtualbox" do |v| 
      v.name = "bdse32" 
      v.cpus = 1
      v.memory = 4096 
    end
    //這裡加入
	bdse32.vm.provision "shell", path: "scripts/sethosts.sh", run: "always"
  end 
end
```
_____
# 使用事先做好的Mirror，可跳過3~9步驟
### 3.Create hadoop account, and grant root privilege
    adduser hadoop
    gpasswd -a hadoop sudo
    groups hadoop
### 4.Install Oracle Java JDK from webupd8team (https://launchpad.net/~webupd8team)
    add-apt-repository ppa:webupd8team/java
    apt-get update
    apt-get install oracle-java7-set-default
### 5.Check Oracle Java version
    java -version
    javac -version
    update-alternatives --display java
### 6.Download hadoop from apache mirror site
    cd
    wget http://apache.stu.edu.tw/hadoop/common/hadoop-2.7.2/hadoop-2.7.3.tar.gz
### 7.extract hadoop tar file to /usr/local, set owner and group to hadoop user
    tar -xvf hadoop-2.7.2.tar.gz -C /usr/local
    mv /usr/local/hadoop-2.7.2 /usr/local/hadoop
    chown -R hadoop:hadoop /usr/local/hadoop
_____    

### 8.Switch to hadoop user
    su - hadoop
### 9.Configuring SSH password-less login (on master1)
    ssh-keygen -t dsa 
      Enter file in which to save the key: [Return]
	  Enter passphrase (empty for no passphrase): [Return]
	  Enter same passphrase again: [Return]
      
    ls -l ~/.ssh 
   
    (copy public key to authorized_keys file)
    cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
      
    (copy public key to other nodes in cluster)
    ssh-copy-id -i ~/.ssh/id_dsa.pub hadoop@master2.example.org
    ...
    
    (copy private key to master2,3)
    scp ~/.ssh/id_dsa master2.example.org:/home/hadoop/.ssh
    ...
   
    exit (exit hadoop user)
# mirror 已完成的終點
_____

### 修改好Vagrantfile後
`vagrant reload`
或
`vagrant up`
開啟機器

登入檢查
`cat /etc/hosts`
ping FQDN (bdse91.example.org)
`ping -c 4 FQDN`

做自動ping每台兩次script
```
ping -c 2 bdse11.example.org
ping -c 2 bdse12.example.org
ping -c 2 bdse31.example.org
ping -c 2 bdse32.example.org
ping -c 2 bdse71.example.org
ping -c 2 bdse72.example.org
ping -c 2 bdse91.example.org
ping -c 2 bdse92.example.org
ping -c 2 bdse111.example.org
ping -c 2 bdse112.example.org
ping -c 2 bdse171.example.org
ping -c 2 bdse172.example.org
```

查帳號存在
`cat /etc/passwd | grep 'hadoop'`
看/vagrant目錄=hasoop_classroom
`df -h` 
安裝hadoop2.7.3
`tar -xvf /vagrant/hadoop-2.7.3.tar.gz -C /usr/local/`
有檔案後更名(方便hadoop_home的位置設定)
`cd /usr/local/`
`sudo mv hadoop-2.7.3/ hadoop`
改權限成hadoop
`sudo chown -R hadoop:hadoop /usr/local/hadoop`

### 10.Test password-less login (on master1,2,3)
`su - hadoop`

登入每一台 FQDN 下載public key
`ssh bdse91.example.org`....
(謹慎使用，不然會不知道自己在?層夢境)
```
ssh bdse11.example.org
ssh bdse12.example.org
ssh bdse31.example.org
ssh bdse32.example.org
ssh bdse71.example.org
ssh bdse72.example.org
ssh bdse91.example.org
ssh bdse92.example.org
ssh bdse111.example.org
ssh bdse112.example.org
ssh bdse171.example.org
ssh bdse172.example.org
```

### 發展很多機器前用同一個box，公鑰皆一樣~免煩惱step9 !!呀比~

----
### 11.設定環境變數(hadoop)
`nano ~/.bashrc`
    
    Set HADOOP_HOME
    export HADOOP_HOME=/usr/local/hadoop
    # Set JAVA_HOME 
    export JAVA_HOME=/usr/lib/jvm/java-8-oracle
    # Add Hadoop bin and sbin directory to PATH
    export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

檢驗
`source ~/.bashrc`
`echo $JAVA_HOME`
`echo $HADOOP_HOME`
`echo $PATH`

### 12.Update hadoop-env.sh
`nano /usr/local/hadoop/etc/hadoop/hadoop-env.sh`

將下列兩項取代原先環境變數
```
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
```

http://hadoop.apache.org/docs/r2.7.3/hadoop-project-dist/hadoop-common/core-default.xml




### 13.Configure core-site.xml
定義:hdfs(/tmp為暫存區)
core-site.xml設定HDFS
hdfs://bdse111.example.org (namenode，port預設8020，為了玩HA)
原本value->/tmp/hadoop換成=>/home/hadoop/tmp
放在/tmp下次起不來....
> nano /usr/local/hadoop/etc/hadoop/core-site.xml 

```
<property>
       <name>hadoop.tmp.dir</name>
       <value>/home/hadoop/tmp</value>
       <description>Temporary Directory.</description>
    </property>

    <property>
       <name>fs.defaultFS</name>
       <value>hdfs://bdse111.example.org</value>  
       #(設定為要當成NAMENODE主機的FQDN名稱)
       <description>Use HDFS as file storage engine</description>
    </property>
```
### 14.Configure mapred-site.xml

定義 : yarn的JobHistory
1. hadoop 使用 yarn作job分配
2. 指定jobhistory server存取執行過的job 

> cp /usr/local/hadoop/etc/hadoop/mapred-site.xml.template /usr/local/hadoop/etc/hadoop/mapred-site.xml	
> 
> nano /usr/local/hadoop/etc/hadoop/mapred-site.xml	
```
    <property>
       <name>mapreduce.framework.name</name>
       <value>yarn</value>
    </property>
    <property>
	   <name>mapreduce.jobhistory.address</name>
	   <value>bdse71.example.org:10020</value>
    </property>
    <property>
	   <name>mapreduce.jobhistory.webapp.address</name>
	   <value>bdse71.example.org:19888</value>
    </property>
    
```

### 15.Configure yarn-site.xml
定義 : yarn的ResourceManager
1. ResourceManager的機器
2. NodeManager 資源使用情況

> nano /usr/local/hadoop/etc/hadoop/yarn-site.xml

```
    <property>
       <name>yarn.nodemanager.aux-services</name>
       <value>mapreduce_shuffle</value>
    </property>
	<property>
       <name>yarn.nodemanager.resource.memory-mb</name>
       <value>4096</value>
    </property>
    <property>
       <name>yarn.nodemanager.resource.cpu-vcores</name>
       <value>1</value>
    </property>
    <property>
	   <name>yarn.resourcemanager.hostname</name>
	   <value>bdse31.example.org</value>
    </property>	
```

### 16.Create /usr/local/hadoop/etc/hadoop/slaves file
定義 : 
1. 設定Slaves為 : DataNode()、nodeManager()
> nano /usr/local/hadoop/etc/hadoop/slaves     (slaves原本就存在)
```
localhost(刪掉，原本就有)

(貼上)
bdse11.example.org
bdse72.example.org
bdse91.example.org
bdse171.example.org
```


### 17.(namenode的人操作)Format the Namenode on master1 node (only once)
> hdfs namenode -format
> start-dfs.sh
> jps

### 18.(DataNode的人執行)
>jps  (確認是否啟動)
`hdfs dfs -ls /` (看全部網路環境)



--
Namenode可以執行重啟
`stop-dfs.sh`
`stop-dfs.sh`
`start-dfs.sh`

複製 /etc/hosts 內的內容到WINDOWS的C:\Windows\System32\drivers\etc\hots

NameNode和DataNode : bdse111.example.org:50070
ResourcsManager : bdse31.example.org:8088
JobHistory : bdse71.example.org:19888

---

### 19.(resourcemanager要操作)
start-yarn.sh
jps


>slaves(DataNode)電腦執行jps皆看到NodeManager

### 用瀏覽器看
> cat /etc/hosts
得到全部的ip
用notepad++開才可以存檔  C:\Windows\System32\drivers\etc\hosts
全部ip存檔

(NameNode)綠綠的 http://bdse111.example.org:50070
(SecondaryNameNode) http://bdse92.example.org:50070
(ResourceManager)小象象 http://bdse31.example.org:8088
(SecondaryResourceManager) http://bdse12.example.org:8088
(HistoryServer)http://bdse71.example.org:19888/
(HistoryServer自己內部)http://bdse71.example.org:10020/

### 20. (HistoryServer)

`mr-jobhistory-daemon.sh start historyserver`
jps
`mr-jobhistory-daemon.sh stop historyserver`
jps

HistoryServer檢查自己:`lsof -nPi`19888&10020(listen) 

### 21.
Check YARN 	
    http://master1.example.org:8088/cluster (yarn cluster info)
### 22.

/usr/local/hadoop/etc/hadoop有此環境的每台機器都可測試
`hadoop jar /usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.3.jar pi 30 100`
(分30份做100次)

#### 更快速設定(改好設定檔後用SCP灑到其他主機)
cd /usr/local/hadoop/etc/hadoop

scp core-site.xml bdsexx:/usr/local/hadoop/etc/hadoop

scp mapred-site.xml bdsexx:/usr/local/hadoop/etc/hadoop

scp yarn-site.xml bdsexx:/usr/local/hadoop/etc/hadoop

scp 檔案 遠端電腦:路徑
scp:secure copy 

#### 啟動停止步驟
啟動
1.先起hdfs
2.再起YARN
3.啟動History Server
停止
1.History
2.yarn
3.hdfs
`mr-jobhistory-daemon.sh stop historyserver`
`stop-yarn.sh`
`start-dfs.sh`



-----

### 以前寫的筆記 : 
    vagrant up
    vagrant box add
                list
                outdated
                remove
                repackage
                update
                
##### 建立VM:
```
    mkdir demo2 #建VM路徑
    cd demo2 #在該workspace執行VM
    vagrant init ubuntu/trusty64 name #初始化VM
    vagrant up #啟動VM
    vagrant ssh #進入VM
    vagrant halt #關機
    vagrant destory #刪除
```    

----------

### DAY2 重新開啟
cd hadoop_classroom
vagrant up
vagrant ssh bdse
su - hadoop

#### HA建置
hadoop官網 -> 左邊Documentation -> Release2.7.3 -> 左邊HDFS下的High Availability With QJM 
使用預設的 8020 port

### 1.設定hdfs-site.xml 
HADOOP NAME NODE HA 15~60
```htmlmixed=
    <property>
     <name>dfs.nameservices</name>
     <value>hadoop-ha</value> #可改名like~snoopy
   </property>
   <property>
     <name>dfs.ha.namenodes.hadoop-ha</name>
     <value>nn1,nn2</value>
   </property>
   <property>
     <name>dfs.namenode.rpc-address.hadoop-ha.nn1</name>
     <value>bdse111.example.org:8020</value>
   </property>
   <property>
     <name>dfs.namenode.http-address.hadoop-ha.nn1</name>
     <value>bdse111.example.org:50070</value>
   </property>
   <property>
     <name>dfs.namenode.rpc-address.hadoop-ha.nn2</name>
     <value>bdse92.example.org:8020</value>
   </property>
   <property>
     <name>dfs.namenode.http-address.hadoop-ha.nn2</name>
     <value>bdse92.example.org:50070</value>
   </property>
   <property>
     <name>dfs.namenode.shared.edits.dir</name>
 <value>qjournal://bdse111.example.org:8485;bdse92.example.org:8485;bdse31.example.org:8485/hadoop-ha</value>       # 設定QJM:儲存NameNode的資料
   </property>
   <property>
     <name>dfs.journalnode.edits.dir</name>
     <value>/home/hadoop/journalnode</value>       # 資料儲存的路徑
   </property>
   <property>
     <name>dfs.ha.fencing.methods</name>   # 避免同時起來(腦裂)
     <value>sshfence</value>
   </property>
   <property>
     <name>dfs.ha.fencing.ssh.private-key-files</name>
     <value>/home/hadoop/.ssh/id_rsa</value> #我們使用rsa
   </property>
```   
   For Hadoop DFS client only(每台->為了角色可便利更換，不是偷懶)
```htmlmixed=
<property>
     <name>dfs.client.failover.proxy.provider.hadoop-ha</name>
  <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
   </property>
   
```
   Copy to all nodes in cluster
   scp hdfs-site.xml master2.example.org:/usr/local/hadoop/etc/hadoop
   ...

## 執行pi後產生檔案
所有datanode
`hdfs dfs -ls /`
`hdfs dfs -ls /user`
`hdfs dfs -ls /user/hadoop`
可以看到所有者為hadoop，屬於群組supergroup

### 2.三台Namenode建立journalnode目錄
`id`看一下自己是誰(我是誰~)
`su - hadoop`
 `  mkdir ~/journalnode`

### 3.修改/usr/local/hadoop/etc/hadoop/core-site.xml

core-site.xml
```
<name>fs.defaultFS</name>
<value>hdfs://hadoop-ha</value>
指向剛才設定的文件，知道哪個是active哪個是stand by

```
倒數第四行原本<value>hdfs://bdse111.example.org</value>
改為<value>hdfs://hadoop-ha</value>

### 3.5(在黑板上)停止 cluster
1.停jobhistory server
2.停yarn
3.停hdfs
如果namenode沒停
cd /usr/local/hadoop/sbin
執行hadoop-daemon.sh stop NameNode
hadoop-daemon.sh stop SecondaryNameNode
停不了kill他

### 4. 日誌檔案系統
三台journalnode
`hadoop-daemon.sh start journalnode`

不是全新NameNode(建日誌)
`hdfs namenode -initializeSharedEdits`
->要看到client.QuorumJournalManager: Successfully started new epoch 1
namenode把namenode啟動
`hadoop-daemon.sh start namenode`

dhfs+journalnode = ext4 filesystem

SecondaryNameNode複製NameNode metadata
hdfs namenode -bootstrapStandby
   
(Start nn2 NameNode)
  hadoop-daemon.sh start namenode
 nn2獲得全部nn1訊息已可使用


### 5.NameNode重啟
   stop-dfs.sh
   start-dfs.sh

確認datanode
`jps`
可看到datanode

手動
(Check HA Status)
   hdfs haadmin -getServiceState nn1 
   hdfs haadmin -getServiceState nn2
(啟動nn1)   
hdfs haadmin -transitionToActive nn1 
(如果要Standby nn1)
hdfs haadmin -transitionToStandby nn1 

### zookeeper(目前為3.4.9)
使用2n+1法則
n=1(保證一台nn running)
zookeeper = 2x1+1=3
n->application

(建議與QJM相同)好處:有NN,RESOURCE不用出網路，效能提升
#### 第一步
QJM那三台
把zookeeper-3.4.9.tar.gz檔案放到C:\Users\Student\hadoop_classroom

解壓縮 更名 改權限
```
tar -xvf zookeeper-3.4.9.tar.gz -C /usr/local
mv /usr/local/zookeeper-3.4.9 /usr/local/zookeeper
chown -R hadoop:hadoop /usr/local/zookeeper
```
#### 第二步 
確認為hadoop身份
>  su - hadoop
#### 第三步Configure zoo.cfg file on all zookeepers
更名為zoo.cfg
> cd /usr/local/zookeeper/conf
>    cp zoo_sample.cfg zoo.cfg

修改zoo.cfg
> nano zoo.cfg
```
tickTime=2000
   initLimit=10
   syncLimit=5
   dataDir=/usr/local/zookeeper/zoodata
   dataLogDir=/usr/local/zookeeper/logs
   clientPort=2181

   server.1=bdse111.example.org:2888:3888
   server.2=bdse92.example.org:2888:3888
   server.3=bdse31.example.org:2888:3888

```
#### 第四步 建目錄

> cd /usr/local/zookeeper
> mkdir logs
> mkdir zoodata

>   echo "1" > zoodata/myid (on bdse111)
>   echo "2" > zoodata/myid (on bdse92)
>   echo "3" > zoodata/myid (on bdse31)

#### 第五步 三台建立PATH
> nano ~/.bashrc
```
export ZOOKEEPER_HOME=/usr/local/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin

```
#### 第六步 啟動動物管理員
zkServer.sh start
jps
看到->8315 QuorumPeerMain
zookeeper輸入lsof -nPi 可以看一下 port:2181,port:3888

#### Configure Automatic Failover with Zookeeper(文件6-2第142行)
1.停止history,yarn,hdfs

2.改hdfs-site檔
>nano /usr/local/hadoop/etc/hadoop/hdfs-site.xml


3.Configure core-site.xml (on all nodes in cluster)
>nano /usr/local/hadoop/etc/hadoop/core-site.xml


```
<property>
<name>ha.zookeeper.quorum</name>
<value>bdse111.example.org:2181,bdse92.example.org:2181,bdse31.example.org:2181</value>
</property>
```
4. `hdfs zkfc -formatZK`
看到-> ha.ActiveStandbyElector: Successfully created /hadoop-ha/hadoop-ha in ZK. 代表成功
fc:failover controller
nn1陣亡fc馬上呼叫nn2

6.測試 停止nn1
`hadoop-daemon.sh stop namenode`
nn2自動啟，nn1 bye bye~

有了ZOO管理員之後的停止步驟
1. 停JobHistory Server
2. yarn
3. hdfs(含journalNode,zkfc)
4. zookeeper


## 第\7.rmha\2.HadoopRMHA.txt
#### 第1步(第19行)
>nano /usr/local/hadoop/etc/hadoop/yarn-site.xml

1.註解掉
```
<!--
    <property>
           <name>yarn.resourcemanager.hostname</name>
           <value>bdse31.example.org</value>
    </property>
-->
```
2.新增




(On bdse31)
   start-dfs.sh 
   
(on all resourcemanager(bdse31 and bdse12), need to manual start)
   start-yarn.sh

(ALL看狀態)
yarn rmadmin -getServiceState rm1
yarn rmadmin -getServiceState rm2

(bdse12測試)
(關掉Resourcemanager)
yarn-daemon.sh stop resourcemanager
(啟動Resourcemanager)
yarn-daemon.sh start resourcemanager


## 第\8.spark\3.HadoopSpark2.0WithYarn.txt
SPARK需要HADOOP的那些設定檔
對錶(不要相差超過10分鐘)

1.台灣地區完整校正
ntp server (Network Time Server套件同步)

2.手動校正
>sudo -i
>date MMMDDhhmm
ex. date 12270711

將老師提供spark-2.0.2-bin-hadoop2.7.tgz複製到C:\Users\Student\hadoop_classroom

#### 第3步
切換成管理員
>sudo -i
>cd /vagrant 
解壓縮 更名 換權限
```
   tar -xvf spark-2.0.2-bin-hadoop2.7.tgz -C /usr/local
   mv /usr/local/spark-2.0.2-bin-hadoop2.7 /usr/local/spark
   chown -R hadoop:hadoop /usr/local/spark
```
#### 第4.5步
換到hadoop
>su - hadoop

修改.bashrc 新增
>nano ~/.bashrc
```
# Set SPARK_HOME
export SPARK_HOME=/usr/local/spark
```
不要把SPARK的執行檔指令放到PATH，因為很多指令與hadoop相同，會相衝

#### 第6步

>cd /usr/local/spark/conf
>cp spark-env.sh.template spark-env.sh
   
>nano spark-env.sh
加在最後一行
spark抓到hadoop的重要步驟
`export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop`

#### 第7步
確認hadoop正常運作
#### 第8步
執行SPARK Pi

>cd $SPARK_HOME
```
   ./bin/spark-submit --class org.apache.spark.examples.SparkPi \
    --master yarn \  請resourcemanager幫忙 
    --deploy-mode cluster \
    --driver-memory 1g \   # context容量 
    --executor-memory 1g \ # 計算時給的容量
    --executor-cores 1 \  # 幾個worker通常與電腦核心數相同，效能最高
    --queue default \
    examples/jars/spark-examples*.jar \
    10
```

看結果，進到小象象http://bdse31.example.org:8088/cluster
找到執行ID 找到node 找到log 

#### 第9步
SPARK打包jar檔產生一個zip
9-(1)
建立一個資料夾
>hdfs dfs -mkdir -p /user/spark/share/jars
上傳
>hdfs dfs -put $SPARK_HOME/jars/* /user/spark/share/jars/
檢查(看最後幾個檔案)
>hdfs dfs -ls /user/spark/share/jars

9-(2)新增一行在spark-defaults.conf最下面
>cd /usr/local/spark/conf
>cp spark-defaults.conf.template spark-defaults.conf
	   
nano spark-defaults.conf
`spark.yarn.jars hdfs://hadoop-ha/user/spark/share/jars/*`
	   
再跑一次Pi,省下打包JAR檔案的時間

#### (94行開始)Using Spark shell

#### 第7步(125行)
hdfs dfs -put /usr/local/spark/README.md /user/spark/share(一個人做就可)
hdfs dfs -ls /user/spark/share

cd $SPARK_HOME
   ./bin/spark-shell --master yarn --deploy-mode client
   (和hadoop cluster溝通，以此整合)

#### 第8步(145行) Run scala script
蘇組長寫的程式WordCount.scala
可以測試shell放著不動會不會timeout自己停掉




### 修改vcore設定
>nano /usr/local/hadoop/etc/hadoop/yarn-site.xml
vcore預設8我們目前是2
之後在加slaves

要重啟


## SPARK和HADOOP有三個東西可以整合
1.Spark-submit
2.spark-shell
3.3.pyspark

## 動物園管理員開關
`zkServer.sh stop`
`zkServer.sh start`

## 現在是沒道理
spark2.0.2整合yarn
yarn重啟，spar資訊會留在yarn的網頁

#### 第9步(187行)
離開spark shell
>:q

su hadoop變臉變一半

### ubuntu使用NTP
1.安裝軟體
`sudo -i`
`apt-get install ntp`
2.設定 time server
`nano /etc/ntp.conf`
把http://www.pool.ntp.org/zone/tw
內的四台time server取代/etc/ntp.conf內的四台time server

3.ntp服務重啟
`service ntp restart`
`service ntp status`

4.查時間是否同步
`ntpq -p`
`ll /var/lib/ntp`
查是否有ntp.drift(安裝後才會出現)


### HIVE 2.hive\6.Hive2.1DerbyInstall.txt
安裝的主機不需要有HADOOP的軟體

#### 第1.2.3.4步
照著講義做

#### 第5步
請另外一個datanode或namenode幫忙
注意權限

#### 第6步 解開更名受權
/usr/local/hive/conf/hive-default...
jdbc:
startNetworkServer要有DERBY才有辦法下此指令

#### 第7步 產生Derby的HOME

#### 第8步 建一個目錄

#### 第9步 cd到那個目錄 啟動
derby只能用固定路徑，路徑錯誤整個資量庫要重建。
0.0.0.0本機所有的ip(什麼時候有看到??)
x)hadoop官網針對hdfs-site.xml的檔案，所有NameNode、SecondaryNameNode的http-address的ip為0.0.0.0




port:1527
nohup:使用者離開系統服務照常執行
&：背景執行
就是deamonize

ip addr show
hadoop生態系統只支援ipv4(物聯網前端支援ipv6)
#### 第10步
操作步驟第74行
>>cat /etc/hosts
127.0.0.1確保所有網路服務正常運作
設定檔第500行  改第501行 成<value>org.apache.derby.jdbc.ClientDriver</value>

操作步驟第79行
設定檔第932行 改933行 成<value>org.apache.derby.jdbc.ClientDriver</value>

設定檔第957行 確認958 user name= APP

設定檔第485行 確認跟操作檔一樣

操作步驟第94行
設定檔第4052行 4053行 true換成false
(操作步驟第96行=允許遠端連入HiveServer)

#### 第11步 修改$HIVE_HOME/conf/hive-site.xml
暫存檔的設定
iotmp:存放hive運作時的佔存資料
編輯hive-site.xml
設定檔第44行換成<value>/usr/local/hive/iotmp</value>
設定檔第49行換成<value>/usr/local/hive/iotmp</value>
設定檔第1516行換成<value>/usr/local/hive/iotmp</value>
設定檔第3681行換成<value>/usr/local/hive/iotmp/operation_logs</value>
存檔，灑出去給有用HIVE的主機
#### 第12步 官網講的 
把Derby兩個jar檔copy到$HIVE_HOME/lib
>cp $DERBY_HOME/lib/derbyclient.jar $HIVE_HOME/lib
>cp $DERBY_HOME/lib/derbytools.jar $HIVE_HOME/lib

xml檔換一下，hive隨時可變，接sql，derby...

#### 第13步 正式建database
建完之後欣賞一下目錄

#### 第14步
>hive
hive>show databases(defult)
hive>show table(空的)

#### 第15步  用hadoop身分 啟動HIVEserver2
>HIVEserver2 $
確認兩個port為10000,10002(listen)
>lsof -nPi

#### 第16步 啟動新的使用者介面beeline(測試是否能連結DB)

進入beeline
>beeline

衝進去第9步建立的
>!connect jdbc:derby://localhost:1527/metastore_db APP mine
或衝進固定位置
>!connect jdbc:derby://localhost:1527//usr/local/derby/data/metastore_db APP mine

進去之後可以下derby的SQL語法
`SELECT * FROM sys.systables;`

離開時要正常離開
```
!commit
!close
!quit
```

#### 第17步(執行資料分析)
(建議執行遠端連結的指令，可順便檢查xml黨是否有錯誤)
!connect jdbc:hive2://bdse172.example.org:10000 hadoop hadoop

允許遠端連入HiveServer
beeline為新的HIVE操作介面有兩種1.本機連線2.網路連線

![](https://i.imgur.com/nTlbowl.jpg)


#### 第18步 瀏覽器網頁介面
#### 第19步


## 停用HIVE
(要做完第19步)
先停HiveServer2
查指令ID(在shell內)
1.
>jobs -l 
或
2.
>ps -ef | grep -i 'hiveserver2'
kill $ID

再停用Derby DB
>/usr/local/derby/bin/
>stopNetworkServer
>