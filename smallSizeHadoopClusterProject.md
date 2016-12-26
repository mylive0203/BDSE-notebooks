# HADOOP專題

安裝vagrant
http://school.soft-arch.net/blog/69382/install-vagrant-on-win

----
|姓名|IP|管理1|管理2|
|:---:|--|--|--|
|瑋|10|11|12|
|均|30|31|32|
|芝|70|71|72|
|品|90|91|92|
|宗|110|111|112|
|童|170|171|172|

例如:hostname : bdseXX.example.org : XX為管理IP
|角色|主機名稱|ip|
|:--:|---------|--|
|NameNode|bdse111.example.org|111|
|SecondaryNameNode|bdse92.example.org|92|
|ResourceManager|bdse31.example.org|31|
|SecondaryResourceManager|bdse12.example.org|12|
|HistoryServer|bdse71.example.org|71|
|DataNode<br/>NodeManger|bdse11.example.org<br/>bdse72.example.org<br/>bdse91.example.org<br/>bdse171.example.org|11<br/>72<br/>91<br/>171|
|Hive+Derby|bdse172.example.org|172|
|Ganglia|bdse32.example.org|32|
|MRTG|bdse112.example.org|112|




<br/>
留一手的配置

---
16.04版本後dsa與rsa都相同




### core-sitexml
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

vagrant的VM每次啟動時會重建 /etc/hosts檔案
所以要在vagrantfile
加入:bdseXX.vm.provision "shell", path: "scripts/sethosts.sh", run: "always"
`vagrant reload`
登入檢查`cat /etc/hosts`
ping FQDN (bdse91.example.org)
`ping -c 4 FQDN`

自動ping每台兩次的script
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

df -h 可看到/vagrant目錄=hasoop_classroom
tar -xvf /vagrant/hadoop-2.7.3.tar.gz -C /usr/local/
安裝好後看有沒有成功

`ll /usr/local/`
有檔案後更名
`cd /usr/local/`
`sudo mv hadoop-2.7.3/ hadoop`

改權限成hadoop
`sudo chown -R hadoop:hadoop /usr/local/hadoop`

su - hadoop

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
##### 11.設定環境變數(hadoop)
  $nano ~/.bashrc
  $source ~/.bashrc
`env`
`echo $JAVA_HOME`
`echo $HADOOP_HOME`
`echo $PATH`
Update hadoop-env.sh

http://hadoop.apache.org/docs/r2.7.3/hadoop-project-dist/hadoop-common/core-default.xml




##### 13.Configure core-site.xml
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
##### 14.Configure mapred-site.xml

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

##### 15.Configure yarn-site.xml
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

##### 16.Create /usr/local/hadoop/etc/hadoop/slaves file
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


##### 17.(namenode的人操作)Format the Namenode on master1 node (only once)
> hdfs namenode -format
> start-dfs.sh
> jps

##### 18.(DataNode的人執行)
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

##### 19.(resourcemanager要操作)
start-yarn.sh
jps


>slaves(DataNode)電腦執行jps皆看到NodeManager

#### 用瀏覽器看
> cat /etc/hosts
得到全部的ip
用notepad++開才可以存檔  C:\Windows\System32\drivers\etc\hosts
全部ip存檔

(Namenode)綠綠的http://bdse111.example.org:50070
(ResourceManager)小象象http://bdse31.example.org:8088
(HistoryServer)http://bdse71.example.org:19888/
(HistoryServer自己內部)http://bdse71.example.org:10020/

##### 20. (HistoryServer)

`mr-jobhistory-daemon.sh start historyserver`
jps
`mr-jobhistory-daemon.sh stop historyserver`
jps

HistoryServer檢查自己:`lsof -nPi`19888&10020(listen) 

##### 21.
Check YARN 	
    http://master1.example.org:8088/cluster (yarn cluster info)
##### 22.

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


